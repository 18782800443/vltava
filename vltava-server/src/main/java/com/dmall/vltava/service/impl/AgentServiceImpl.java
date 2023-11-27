package com.testhuamou.vltava.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.testhuamou.vltava.domain.agent.InjectResult;
import com.testhuamou.vltava.domain.app.AppVO;
import com.testhuamou.vltava.domain.base.CommonException;
import com.testhuamou.vltava.domain.enums.InjectStatusEnum;
import com.testhuamou.vltava.domain.enums.TaskStatusEnum;
import com.testhuamou.vltava.domain.mock.MockActionVO;
import com.testhuamou.vltava.domain.mock.MockVO;
import com.testhuamou.vltava.domain.mock.RegisterVO;
import com.testhuamou.vltava.manager.MockManager;
import com.testhuamou.vltava.service.AgentService;
import com.testhuamou.vltava.service.AppService;
import com.testhuamou.vltava.service.DockerManageService;
import com.testhuamou.vltava.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author Rob
 * @date Create in 3:14 下午 2020/8/21
 */
@Service("AgentService")
public class AgentServiceImpl implements AgentService {
    public static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);
    private static final Set<Integer> RESTART_SET = new HashSet<>();

    private static MockManager mockManager;

    @Autowired
    public AgentServiceImpl(MockManager mockManager) {
        AgentServiceImpl.mockManager = mockManager;
    }

    @Autowired
    AppService appService;

    @Autowired
    DockerManageService dockerManageService;


    @Override
    public void uploadDataAsync(Integer appId) {
        List<MockVO> mockVOList = mockManager.getMockVoByAppId(appId);
        if (mockVOList.size() == 0) {
            logger.info("当前应用无进行中mock, skip");
            return;
        }
        RegisterVO registerVO = uploadPrepare(mockVOList);
        CompletableFuture.runAsync(new ContinuesUpload(mockVOList, registerVO)).exceptionally(fn -> {
            logger.error(fn.getMessage());
            fn.printStackTrace();
            return null;
        });
    }

    @Override
    public void updateData(Integer taskId) {
        MockVO mockVO = mockManager.getMockVoById(taskId);
        RegisterVO registerVO = uploadPrepare(mockVO);
        String resp = HttpUtils.updateData(registerVO, Collections.singletonList(mockVO));
        dealResult(resp);
    }

    @Override
    public void updateStatus(Integer taskId) {
        MockVO mockVO = mockManager.getStatusById(taskId);
        RegisterVO registerVO = uploadPrepare(mockVO);
        String resp = HttpUtils.updateStatus(registerVO, mockVO);
        dealResult(resp);
    }

    @Override
    public void healthCheck(String ip, String port) {

    }

    @Override
    public Boolean canUpdate(Integer taskId) {
        return RESTART_SET.contains(taskId);
    }

    private static void addRestartServer(Integer mockId) {
        RESTART_SET.add(mockId);
    }

    private static void removeRestartServer(Integer mockId) {
        RESTART_SET.remove(mockId);
    }

    static class ContinuesUpload implements Runnable {
        private List<MockVO> mockVOList;
        private RegisterVO registerVO;

        public ContinuesUpload(List<MockVO> mockVOList, RegisterVO registerVO) {
            this.mockVOList = mockVOList;
            this.registerVO = registerVO;
        }

        @Override
        public void run() {
            Integer count = 0;
            String errorResp = null;
            Integer appId = mockVOList.get(0).getAppId();
            Boolean isConnect = false;
            addRestartServer(appId);
            while (count < 10) {
                count++;
                String msg = String.format("第%s次尝试推送信息至：【%s】-【%s】 at %s", count, registerVO.getSystemUniqueName(), registerVO.getZone(), registerVO.getIp());
                logger.info(String.format("%s，data: %s", msg, JSON.toJSONString(mockVOList)));
                InjectResult resp = JSON.parseObject(HttpUtils.updateData(registerVO, mockVOList), InjectResult.class);
                if (InjectStatusEnum.FAIL.getKey().equals(resp.getInjectKey())) {
                    logger.info(msg + "失败！");
                    try {
                        Thread.sleep(count * count * 1000);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                        e.printStackTrace();
                    }
                } else if (InjectStatusEnum.SUCCESS.getKey().equals(resp.getInjectKey())) {
                    logger.info(msg + "成功！");
                    try {
                        isConnect = true;
                        mockManager.updateConnect(appId, true);
                        removeRestartServer(appId);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        logger.error(JSON.toJSONString(e.getStackTrace()));
                    }
                    return;
                } else if (InjectStatusEnum.PART.getKey().equals(resp.getInjectKey())) {
                    logger.warn(msg + "部分成功！");
                    // 重试
                    if (count < 10) {
                        try {
                            Thread.sleep(count * count * 1000);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage(), e);
                            e.printStackTrace();
                        }
                        // 尝试多次仍然无法注入则对应用例为异常
                    } else {
                        try {
                            isConnect = true;
                            logger.warn(resp.getMsg());
                            for (MockActionVO failRef : resp.getFailActionList()) {
                                List<Integer> errorIds = new ArrayList<>();
                                for (MockVO mockVO : mockVOList) {
                                    for (MockActionVO mockActionVO : mockVO.getMockActionList()) {
                                        if (mockActionVO.getClassName().equals(failRef.getClassName()) && mockActionVO.getMethodName().equals(failRef.getMethodName())) {
                                            if (!mockVO.getTaskStatus().equals(TaskStatusEnum.ERROR.getKey())) {
                                                mockVO.setTaskStatus(TaskStatusEnum.ERROR.getKey());
                                                mockManager.updateMysqlStatus(mockVO);
                                                errorIds.add(mockVO.getId());
                                            }
                                            break;
                                        }
                                    }
                                }
                                logger.warn(String.format("尝试多次仍然无法注入，标记为异常方法: 【class】 %s 【method】 %s， 影响规则：%s", failRef.getClassName(), failRef.getMethodName(), JSON.toJSONString(errorIds)));
                            }
                            mockManager.updateConnect(appId, true);
                            removeRestartServer(appId);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            logger.error(JSON.toJSONString(e.getStackTrace()));
                        }
                    }
                } else {
                    errorResp = JSON.toJSONString(resp);
                    logger.error(msg + ", 返回消息异常： " + resp);
                    break;
                }
            }
            removeRestartServer(appId);
            mockManager.updateConnect(appId, false);
            if (count == 9) {
                logger.error(String.format("多次尝试推送信息至：【%s】-【%s】 at %s 失败， 请检查方法是否正确或联系管理员!", registerVO.getSystemUniqueName(), registerVO.getZone(), registerVO.getIp()));
            } else {
                throw new CommonException(errorResp);
            }
        }
    }

    private RegisterVO uploadPrepare(List<MockVO> mockVOList) {
        MockVO mockVO = mockVOList.get(0);
        AppVO appVO = appService.getAppInfo(mockVO.getAppId());
        for (MockVO vo : mockVOList) {
            vo.setAppVo(appVO);
            // 设置任务状态到action, updateStatus 只有部分信息
            if (vo.getMockActionList() != null) {
                for (MockActionVO mockActionVO : vo.getMockActionList()) {
                    mockActionVO.setTaskStatus(vo.getTaskStatus());
                }
            }
        }
        RegisterVO registerVO = dockerManageService.getSystemInfo(new RegisterVO(mockVO.getAppVo().getSystemUniqueName(), mockVO.getAppVo().getZone(), mockVO.getAppVo().getBuildGroup()));
        if (registerVO == null) {
            throw new CommonException("当前系统暂无注册信息，请检查是否部署在单元");
        }
        return registerVO;
    }

    /**
     * 上传数据准备
     *
     * @param mockVO
     * @return
     */
    private RegisterVO uploadPrepare(MockVO mockVO) {
        AppVO appVO = appService.getAppInfo(mockVO.getAppId());
        mockVO.setAppVo(appVO);
        // 设置任务状态到action, updateStatus 只有部分信息
        if (mockVO.getMockActionList() != null) {
            for (MockActionVO mockActionVO : mockVO.getMockActionList()) {
                mockActionVO.setTaskStatus(mockVO.getTaskStatus());
            }
        }
        RegisterVO registerVO = dockerManageService.getSystemInfo(new RegisterVO(mockVO.getAppVo().getSystemUniqueName(), mockVO.getAppVo().getZone(), mockVO.getAppVo().getBuildGroup()));
        if (registerVO == null) {
            throw new CommonException("当前系统暂无注册信息，请检查是否部署在单元");
        }
        return registerVO;
    }

    private void dealResult(String resp) {
        logger.info("agent resp：" + resp);
        try {
            InjectResult injectResult = JSON.parseObject(resp, InjectResult.class);
            if (InjectStatusEnum.FAIL.getKey().equals(injectResult.getInjectKey())) {
                throw new CommonException(injectResult.getMsg() == null ? "注入失败,多次重试后请联系管理员" : injectResult.getMsg());
            }
            if (InjectStatusEnum.PART.getKey().equals(injectResult.getInjectKey())) {
                throw new CommonException(injectResult.getMsg() + " Fail Reference: " + JSON.toJSONString(injectResult.getFailActionList()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CommonException(resp);
        }
    }
}
