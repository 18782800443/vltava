package com.dmall.vltava.service.stategy;

import com.alibaba.fastjson.JSON;
import com.dmall.vltava.domain.app.AppVO;
import com.dmall.vltava.domain.base.AgentMsgVO;
import com.dmall.vltava.domain.mock.RegisterVO;
import com.dmall.vltava.manager.MockManager;
import com.dmall.vltava.service.AgentService;
import com.dmall.vltava.service.AppService;
import com.dmall.vltava.service.DockerManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rob
 * @date Create in 6:11 下午 2020/6/2
 */
@Component("MockMqStrategy")
public class MockMqStrategy implements MqStrategy {
    public static final Logger logger = LoggerFactory.getLogger(MockMqStrategy.class);

    @Autowired
    MockManager mockManager;

    @Autowired
    DockerManageService dockerManageService;

    @Autowired
    AppService appService;

    @Autowired
    AgentService agentService;

    @Override
    public void consume(AgentMsgVO agentMsgVO) {
        logger.info("mq消费：" + JSON.toJSONString(agentMsgVO));
//        mockService.consume(agentMsgVO);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void register(RegisterVO registerVO) {
        StringBuilder stringBuilder = new StringBuilder();
        if (registerVO.getIp() == null) {
            stringBuilder.append("ip不能为空");
        }
        if (registerVO.getSystemUniqueName() == null){
            stringBuilder.append("system_unique_name不能为空");
        }
        if(registerVO.getZone() == null){
            stringBuilder.append("zone不能为空");
        }
        if (registerVO.getGroup() == null){
            stringBuilder.append("group不能为空");
        }
        if (!"".equals(stringBuilder.toString())){
            logger.error(stringBuilder.toString() + " " + registerVO);
        } else {
            Boolean needUpload = dockerManageService.save(registerVO);
            if (needUpload){
                AppVO appVO = appService.getAppInfoByRegisterInfo(registerVO);
                if (appVO != null){
                    agentService.uploadDataAsync(appVO.getId(), registerVO);
                }else{
                    logger.error("未找到应用的blue分组信息: " + JSON.toJSONString(registerVO));
                }
            } else {
                logger.info("当前服务在平台无应用，仅保存 ：" + JSON.toJSONString(registerVO));
            }
        }
    }
}