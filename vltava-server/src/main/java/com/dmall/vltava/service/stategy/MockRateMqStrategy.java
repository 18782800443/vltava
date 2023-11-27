//package com.testhuamou.vltava.service.stategy;
//
//import com.alibaba.fastjson.JSON;
//import com.testhuamou.vltava.controller.WebSocketServer;
//import com.testhuamou.vltava.domain.base.AgentMsgVO;
//import com.testhuamou.vltava.domain.coverage.CoverageVO;
//import com.testhuamou.vltava.domain.coverage.RateMsgVO;
//import com.testhuamou.vltava.domain.enums.TaskStatusEnum;
//import com.testhuamou.vltava.domain.mock.MockVO;
//import com.testhuamou.vltava.domain.mock.RegisterVO;
//import com.testhuamou.vltava.service.CoverageService;
//import com.testhuamou.vltava.service.MockService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * @author Rob
// * @date Create in 6:11 下午 2020/6/2
// */
//public class MockRateMqStrategy implements MqStrategy {
//
//    @Autowired
//    WebSocketServer webSocketServer;
//
//    @Autowired
//    MockService mockService;
//
//    public static final Logger logger = LoggerFactory.getLogger(MockRateMqStrategy.class);
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
//            MockVO mockVO = new MockVO();
//            mockVO.setId(agentMsgVO.getTaskId());
//            mockVO.setTaskStatus(TaskStatusEnum.READY.getKey());
//            mockService.updateStatus(mockVO);
//            webSocketServer.close(taskType, agentMsgVO.getTaskId());
//        }
//    }
//
//    @Override
//    public void register(RegisterVO registerVO) {
//
//    }
//}