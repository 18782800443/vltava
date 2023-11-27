package com.testhuamou.vltava.service.remote;

import com.alibaba.fastjson.JSON;
import com.testhuamou.vltava.domain.annotation.HandleException;
import com.testhuamou.vltava.domain.base.CommonException;
import com.testhuamou.vltava.domain.base.CommonResult;
import com.testhuamou.vltava.domain.enums.MockTypeEnum;
import com.testhuamou.vltava.domain.enums.TaskStatusEnum;
import com.testhuamou.vltava.domain.mock.*;

import com.testhuamou.vltava.domain.mock.client.MockClientBase;
import com.testhuamou.vltava.domain.mock.client.MockRule;
import com.testhuamou.vltava.domain.mock.client.MockStatus;
import com.testhuamou.vltava.manager.MockManager;
import com.testhuamou.vltava.service.AgentService;
import com.testhuamou.vltava.service.MockClient;
import com.testhuamou.vltava.service.MockService;
import com.testhuamou.vltava.service.impl.AgentServiceImpl;
import org.apache.dubbo.common.utils.PojoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Rob
 * @date Create in 1:57 下午 2020/9/14
 */
@org.apache.dubbo.config.annotation.Service
@Service("MockClient")
public class MockClientServiceImpl implements MockClient {
    public static final Logger logger = LoggerFactory.getLogger(MockClientServiceImpl.class);


    @Autowired
    MockManager mockManager;

    @Autowired
    MockService mockService;

    @Autowired
    AgentService agentService;

    @HandleException
    @Override
    public CommonResult get(Integer id) {
        logger.info("receive message");
        return CommonResult.pass("success", mockManager.getMockVoById(id));
    }

    @HandleException
    @Override
    public CommonResult put(MockRule mockRule) {
        return update(mockRule, true);
    }

    @HandleException
    @Override
    public CommonResult remove(MockRule mockRule) {
        return update(mockRule, false);
    }

    @HandleException
    @Override
    public CommonResult updateStatus(MockStatus mockStatus) {
        MockVO mockVO = getAllRules(mockStatus);
        if (mockStatus.getStatus().equals(TaskStatusEnum.RUNNING.getKey()) || mockStatus.getStatus().equals(TaskStatusEnum.PAUSE.getKey())) {
            return CommonResult.fail("接口仅能修改为状态：运行/暂停");
        }
        if (mockVO.getTaskStatus().equals(TaskStatusEnum.READY.getKey()) && !mockStatus.getStatus().equals(TaskStatusEnum.RUNNING.getKey())) {
            return CommonResult.fail("待运行规则仅能变更为运行中");
        }
        mockVO.setTaskStatus(mockStatus.getStatus());
        if (!mockService.updateStatus(mockVO)) {
            return CommonResult.fail("变更状态失败，请重试");
        }
        agentService.updateStatus(mockVO.getId());
        return CommonResult.pass("success");
    }

    @HandleException
    @Override
    public CommonResult serializeObj(Object obj) {
        if (obj == null) {
            return CommonResult.fail("入参不能为空");
        }
        String result = null;
        try {
            result = JSON.toJSONString(PojoUtils.generalize(obj));
            return CommonResult.pass(result);
        } catch (Exception e) {
            return CommonResult.fail(e.getMessage());
        }
    }

    private MockVO getAllRules(MockClientBase mockClientBase) {
        if (mockClientBase == null || mockClientBase.getId() == null) {
            throw new CommonException("请求必要数据不能为空");
        }
        MockVO mockVO = mockManager.getMockVoById(mockClientBase.getId());
        if (mockVO.getTaskStatus().equals(TaskStatusEnum.PREPARE.getKey())) {
            throw new CommonException("规则准备中，无法通过接口修改");
        }
        if (mockVO.getTaskStatus().equals(TaskStatusEnum.END.getKey())) {
            throw new CommonException("规则已关闭，无法通过接口修改");
        }
        if (mockVO.getTaskStatus().equals(TaskStatusEnum.ERROR.getKey())) {
            throw new CommonException("规则异常，无法通过接口修改");
        }
        return mockVO;
    }

    private CommonResult update(MockRule mockRule, Boolean add) {
        CommonResult checkResult = check(mockRule);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        for (MockActionVO rule : mockRule.getRules()) {
            // 创建mockKey
            if (mockRule.getGenerateMockKey()) {
                rule.setMockKey(UUID.randomUUID().toString());
            }
            cleanRule(rule);
        }
        MockVO mockVO = getAllRules(mockRule);
        Set<String> newExpectKeySet = mockRule.getRules().stream().map(MockActionVO::getExpectKey).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> newMockKeySet = mockRule.getRules().stream().map(MockActionVO::getMockKey).filter(Objects::nonNull).collect(Collectors.toSet());
        mockVO.getMockActionList().removeIf(mockActionVO -> newExpectKeySet.contains(mockActionVO.getExpectKey()));
        mockVO.getMockActionList().removeIf(mockActionVO -> newMockKeySet.contains(mockActionVO.getMockKey()));
        if (add) {
            mockVO.getMockActionList().addAll(mockRule.getRules());
        }
        if (mockManager.save(mockVO) == 1) {
            agentService.updateData(mockVO.getId());
            return CommonResult.pass("success", mockVO);
        } else {
            return CommonResult.fail("保存异常，请联系管理员");
        }
    }

    /**
     * 清理rule多余参数， 保证作为判断主键内容的唯一性
     *
     * @param mockActionVO
     */
    private void cleanRule(MockActionVO mockActionVO) {
        switch (mockActionVO.getMockType()) {
            //MockTypeEnum.RETURN_VALUE
            case 0:
                mockActionVO.setSleepTimeVO(null);
                mockActionVO.setErrorMsg(null);
                break;
            //MockTypeEnum.SLEEP
            case 1:
                mockActionVO.setExpectValue(null);
                mockActionVO.setErrorMsg(null);
                break;
            //MockTypeEnum.THROW_EXCEPTION
            case 2:
                mockActionVO.setExpectValue(null);
                mockActionVO.setSleepTimeVO(null);
                break;
            default:
                break;
        }
    }

    private CommonResult check(MockRule mockRule) {
        if (mockRule.getRules() == null || mockRule.getRules().size() == 0) {
            return CommonResult.fail("规则不能为空");
        }
        for (MockActionVO rule : mockRule.getRules()) {
            if (!mockRule.getGenerateMockKey()) {
                if (rule.getExpectKey() == null || "".equals(rule.getExpectKey().trim())) {
                    return CommonResult.fail("expectKey为非法值");
                }
            }
            if (rule.getMockType() == null || !MockTypeEnum.containsKey(rule.getMockType())) {
                return CommonResult.fail("mockType为非法值");
            }
            if (rule.getMockType().equals(MockTypeEnum.RETURN_VALUE.getKey()) && (rule.getExpectValue() == null || "".equals(rule.getExpectValue().trim()))) {
                return CommonResult.fail("mockType=0时，expectValue不能为空");
            }
            if (rule.getMockType().equals(MockTypeEnum.THROW_EXCEPTION.getKey()) && (rule.getErrorMsg() == null || "".equals(rule.getErrorMsg().trim()))) {
                return CommonResult.fail("mockType=2时，errorMsg不能为空");
            }
            if (rule.getMockType().equals(MockTypeEnum.SLEEP.getKey())) {
                if (rule.getSleepTimeVO() == null) {
                    return CommonResult.fail("mockType=1时，sleepTimeVO不能为空");
                }
                if (rule.getSleepTimeVO().getNeedRandom() == null || (rule.getSleepTimeVO().getNeedRandom() != 0 && rule.getSleepTimeVO().getNeedRandom() != 1)) {
                    return CommonResult.fail("needRandom为非法值");
                }
                if (rule.getSleepTimeVO().getNeedRandom() == 0 && rule.getSleepTimeVO().getBaseTime() == null) {
                    return CommonResult.fail("needRandom=0时，baseTime不能为空");
                }
                if (rule.getSleepTimeVO().getNeedRandom() == 1) {
                    if (rule.getSleepTimeVO().getRandomStart() == null || rule.getSleepTimeVO().getRandomEnd() == null) {
                        return CommonResult.fail("needRandom=1时，randomStart && randomEnd不能为空");
                    }
                    if (rule.getSleepTimeVO().getRandomEnd() < rule.getSleepTimeVO().getRandomStart()) {
                        return CommonResult.fail("needRandom=1时，需randomStart <= randomEnd");
                    }
                }
            }
        }
        return CommonResult.pass("");
    }
}
