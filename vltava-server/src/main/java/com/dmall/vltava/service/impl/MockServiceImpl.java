package com.testhuamou.vltava.service.impl;

import com.alibaba.fastjson.JSON;
import com.testhuamou.vltava.dao.AppManageMapper;
import com.testhuamou.vltava.domain.base.*;
import com.testhuamou.vltava.domain.enums.TaskStatusEnum;
import com.testhuamou.vltava.domain.mock.MockActionVO;
import com.testhuamou.vltava.domain.mock.MockVO;
import com.testhuamou.vltava.domain.mock.RegisterVO;
import com.testhuamou.vltava.manager.MockManager;
import com.testhuamou.vltava.service.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Rob
 * @date Create in 1:18 下午 2020/5/27
 */
@Service("MockService")
public class MockServiceImpl implements MockService {
    public static final Logger logger = LoggerFactory.getLogger(MockServiceImpl.class);

    @Autowired
    AppService appService;

    @Autowired
    AppManageMapper appManageMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    DockerManageService dockerManageService;

    @Autowired
    AgentService agentService;

    @Autowired
    MockManager mockManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CommonResult save(MockVO mockVO) {
        boolean isNew = mockVO.getId() == null;
        boolean needUpload = true;
        if (!isNew) {
            MockVO exist = mockManager.getMockVoById(mockVO.getId());
            if (JSON.toJSONString(exist.getMockActionList()).equals(JSON.toJSONString(mockVO.getMockActionList()))) {
                needUpload = false;
            }
        }
        mockManager.save(mockVO);
        Integer taskId = isNew ? mockManager.getTaskId(mockVO) : mockVO.getId();
        mockVO.setId(taskId);
        if (needUpload) {
            if (isNew) {
                List<RegisterVO> successRegisterVO = agentService.updateDataAllGroup(taskId);
                // 注入成功变更状态
                mockVO.setTaskStatus(TaskStatusEnum.READY.getKey());
                if (updateStatusAllGroup(mockVO).isEmpty()) {
                    throw new CommonException("变更状态失败，请重试");
                }
                agentService.updateSuccessRegisterVoStatus(successRegisterVO, taskId);
                return CommonResult.pass("保存成功的分组:" + successRegisterVO.stream().map(RegisterVO::getGroup).collect(Collectors.toList()));
            } else {
                List<RegisterVO> successRegisterVO = agentService.updateDataAllGroup(taskId);
                return CommonResult.pass("保存成功的分组" + successRegisterVO.stream().map(RegisterVO::getGroup).collect(Collectors.toList()));
            }
        } else {
            return CommonResult.pass("保存成功", mockVO.getId());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateStatus(MockVO mockVO) {
        if (agentService.canUpdate(mockVO.getId())) {
            throw new CommonException("Docker重启数据加载中, 暂停修改状态，请1分钟后重试!");
        }
        logger.info(String.format("尝试变更【 %s 】状态: %s ", mockVO.getId(), TaskStatusEnum.getDescByKey(mockVO.getTaskStatus())));
        if (1 == mockManager.updateMysqlStatus(mockVO)) {
            agentService.updateStatus(mockVO.getId());
            return true;
        } else {
            throw new CommonException("变更状态失败，请重试");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<RegisterVO> updateStatusAllGroup(MockVO mockVO) {
        if (agentService.canUpdate(mockVO.getId())) {
            throw new CommonException("Docker重启数据加载中, 暂停修改状态，请1分钟后重试!");
        }
        logger.info(String.format("尝试变更【 %s 】状态: %s ", mockVO.getId(), TaskStatusEnum.getDescByKey(mockVO.getTaskStatus())));
        if (1 == mockManager.updateMysqlStatus(mockVO)) {
            List<RegisterVO> registerVOList = agentService.updateStatusAllGroup(mockVO.getId());
            return registerVOList;
        } else {
            throw new CommonException("变更状态失败，请重试");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CommonResult retry(MockVO mockVO) {
        mockManager.updateMysqlStatus(mockVO);
        List<RegisterVO> registerVOList = agentService.updateDataAllGroup(mockVO.getId());
        if (registerVOList.isEmpty()) {
            throw new CommonException("重试失败，请重试");
        }
        mockManager.updateConnect(mockVO.getAppId(), true);
        return CommonResult.pass("重试成功");
    }

    @Override
    public void formatData(MockVO mockVO) {
        if (StringUtils.isNotEmpty(mockVO.getDescriptions())) {
            mockVO.setDescriptions(mockVO.getDescriptions().trim());
        }
        if (mockVO.hasImplicit()) {
            boolean alreadyAdd = false;
            for (MockActionVO mockActionVO : mockVO.getMockActionList()) {
                if (mockActionVO.getEntrance() != null && mockActionVO.getEntrance()) {
                    mockActionVO.setClassName(mockVO.getEntranceClassName());
                    mockActionVO.setMethodName(mockVO.getEntranceMethodName());
                    alreadyAdd = true;
                    break;
                }
            }
            if (!alreadyAdd) {
                MockActionVO actionVO = new MockActionVO();
                actionVO.setClassName(mockVO.getEntranceClassName());
                actionVO.setMethodName(mockVO.getEntranceMethodName());
                actionVO.setEntrance(true);
                mockVO.getMockActionList().add(actionVO);
            }
        } else {
            mockVO.setEntranceClassName(null);
            mockVO.setEntranceMethodName(null);
            mockVO.getMockActionList().removeIf(mockActionVO -> {
                return mockActionVO.getEntrance() != null && mockActionVO.getEntrance();
            });
        }
        for (MockActionVO mockActionVO : mockVO.getMockActionList()) {
            mockActionVO.setClassName(mockActionVO.getClassName().trim());
            mockActionVO.setMethodName(mockActionVO.getMethodName().trim());
            mockActionVO.setMockKey(mockVO.getMockKey());
            mockActionVO.setImplicit(mockVO.hasImplicit());
            mockActionVO.setExpectKey(mockVO.hasImplicit() ? null : mockActionVO.getExpectKey().trim());
            if (mockActionVO.getEntrance() == null) {
                mockActionVO.setEntrance(false);
            }
            if (StringUtils.isEmpty(mockActionVO.getUid())) {
                mockActionVO.setUid(UUID.randomUUID().toString());
            }
            if (mockActionVO.getExpectValue() != null) {
                mockActionVO.setExpectValue(mockActionVO.getExpectValue().trim());
            }
        }
    }
}
