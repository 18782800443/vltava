//package com.dmall.vltava.service.stategy;
//
//import com.alibaba.fastjson.JSON;
//import com.dmall.vltava.controller.WebSocketServer;
//import com.dmall.vltava.domain.base.AgentMsgVO;
//import com.dmall.vltava.domain.coverage.CoverageVO;
//import com.dmall.vltava.domain.coverage.RateMsgVO;
//import com.dmall.vltava.domain.enums.TaskStatusEnum;
//import com.dmall.vltava.service.CoverageService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author Rob
// * @date Create in 5:40 PM 2020/2/27
// */
//@Component("CoverageRate")
//public class CoverageRateMqStrategy implements MqStrategy {
//
//    @Autowired
//    WebSocketServer webSocketServer;
//
//    @Autowired
//    CoverageService coverageService;
//
//    public static final Logger logger = LoggerFactory.getLogger(CoverageRateMqStrategy.class);
//
//    @Override
//    public void consume(AgentMsgVO agentMsgVO) {
//        logger.info("mq消费：" + JSON.toJSONString(agentMsgVO));
//        RateMsgVO rateMsgVO = JSON.parseObject(JSON.toJSONString(agentMsgVO.getMsgData()), RateMsgVO.class);
//        Integer rate = rateMsgVO.getRate();
//        Integer taskType = agentMsgVO.getTaskType() - RATE_ADDTION;
//        webSocketServer.sendMessage(taskType, agentMsgVO.getTaskId(), rate.toString());
//        if (rateMsgVO.getFinish() != null && rateMsgVO.getFinish()) {
//            logger.info("attach完成，将切换状态至进行待运行");
//            CoverageVO coverageVO = new CoverageVO();
//            coverageVO.setId(agentMsgVO.getTaskId());
//            coverageVO.setTaskStatus(TaskStatusEnum.READY.getKey());
//            coverageService.updateStatus(coverageVO);
//            webSocketServer.close(taskType, agentMsgVO.getTaskId());
//        }
//    }
//
//
//}
