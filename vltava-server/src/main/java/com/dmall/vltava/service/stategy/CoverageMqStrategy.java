//package com.testhuamou.vltava.service.stategy;
//
//import com.alibaba.fastjson.JSON;
//import com.testhuamou.vltava.domain.base.AgentMsgVO;
//import com.testhuamou.vltava.service.template.AbstractConsumeTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
///**
// * @author Rob
// * @date Create in 5:31 PM 2020/2/27
// */
//@Component("Coverage")
//public class CoverageMqStrategy implements MqStrategy {
//
//    @Autowired
//    @Qualifier("CoverageConsumeTemplate")
//    AbstractConsumeTemplate coverageConsumeTemplate;
//
//    public static final Logger logger = LoggerFactory.getLogger(CoverageMqStrategy.class);
//
//    @Override
//    public void consume(AgentMsgVO agentMsgVO) {
//        logger.info("mq消费：" + JSON.toJSONString(agentMsgVO));
//        coverageConsumeTemplate.consume(agentMsgVO);
//    }
//}
