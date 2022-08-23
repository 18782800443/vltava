package com.dmall.vltava.controller;

import com.alibaba.fastjson.JSON;
import com.dmall.dmg.sdk4.rocketmq.consumer.RocketMessageHandler;
import com.dmall.dmg.sdk4.rocketmq.message.RocketMsg;
import com.dmall.dmg.sdk4.rocketmq.message.RocketMsgExt;
import com.dmall.vltava.domain.mock.RegisterVO;
import com.dmall.vltava.service.TraceService;
import com.dmall.vltava.service.factory.MqStrategyFactory;
import com.dmall.vltava.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * @author Rob
 * @date Create in 4:20 PM 2020/1/9
 */
@Component
public class MQConsumer implements RocketMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MQConsumer.class);

    @Autowired
    SpringContextUtils springContextUtils;

    @Autowired
    TraceService traceService;

    @Autowired
    MqStrategyFactory mqStrategyFactory;


    @Override
    public void onMessage(List<RocketMsgExt> rktMsgs) {
            if (springContextUtils.inDev()) {
                logger.info("dev环境，跳过MQ");
                return;
            }
            for (RocketMsg msg : rktMsgs) {
                //消费业务逻辑
                logger.info("【MQ】consume:" + msg.getMessage());
                if (StringUtils.isEmpty(msg.getMessage())) {
                    logger.warn("MQ消息为空！");
                    return;
                }
                RegisterVO registerVO = JSON.parseObject(msg.getMessage(), RegisterVO.class);
                mqStrategyFactory.getMockRegStrategy().register(registerVO);
            }
        }
}
