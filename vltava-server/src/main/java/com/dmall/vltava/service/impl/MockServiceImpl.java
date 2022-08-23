package com.dmall.vltava.service.impl;

import com.alibaba.fastjson.JSON;
import com.dmall.vltava.dao.AppManageMapper;
import com.dmall.vltava.domain.base.*;
import com.dmall.vltava.domain.enums.TaskStatusEnum;
import com.dmall.vltava.domain.mock.MockActionVO;
import com.dmall.vltava.domain.mock.MockVO;
import com.dmall.vltava.manager.MockManager;
import com.dmall.vltava.service.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
                agentService.updateData(taskId);
                // 注入成功变更状态
                mockVO.setTaskStatus(TaskStatusEnum.READY.getKey());
                if (!updateStatus(mockVO)) {
                    throw new CommonException("变更状态失败，请重试");
                }
                agentService.updateStatus(taskId);
                return CommonResult.pass("保存成功，可以点击任务状态开始mock了", mockVO.getId());
            } else {
                agentService.updateData(taskId);
                return CommonResult.pass("保存成功", mockVO.getId());
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
    public CommonResult retry(MockVO mockVO) {
        mockManager.updateMysqlStatus(mockVO);
        agentService.updateData(mockVO.getId());
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
