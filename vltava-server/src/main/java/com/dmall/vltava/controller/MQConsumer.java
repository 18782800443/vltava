package com.dmall.vltava.controller;

import com.alibaba.fastjson.JSON;
import com.dmall.dmg.sdk4.comm.Constants;
import com.dmall.dmg.sdk4.comm.auth.AuthInfo;
import com.dmall.dmg.sdk4.comm.auth.DMGAuthException;
import com.dmall.dmg.sdk4.rocketmq.consumer.CoreRocketConsumerImpl;
import com.dmall.dmg.sdk4.rocketmq.consumer.RocketMessageHandler;
import com.dmall.dmg.sdk4.rocketmq.message.RocketMsg;
import com.dmall.dmg.sdk4.rocketmq.message.RocketMsgExt;
import com.dmall.monitor.sdk.MonitorConfig;
import com.dmall.vltava.domain.annotation.HandleException;
import com.dmall.vltava.domain.base.AgentMsgVO;
import com.dmall.vltava.domain.mock.RegisterVO;
import com.dmall.vltava.service.TraceService;
import com.dmall.vltava.service.factory.MqStrategyFactory;
import com.dmall.vltava.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * @author Rob
 * @date Create in 4:20 PM 2020/1/9
 */
@Component
public class MQConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MQConsumer.class);

    private final static String PROJECT_CODE = "Vltava";
    private final static String APP_CODE = "vltava-service";
    private final static String SECRET_KEY = "6FB9535C-C6EB-471E-8734-88CFBA6243D9";
    private final static String DMC_SERVER_ADDRESS = "http://testds.dmc.dmall.com:37777/";
    private final static String DMG_SERVER_ADDRESS = "testamp.dmg.dmall.com:37788";
    //hosts配置 10.248.8.12  testamp.dmg.dmall.com
    private final static String TOPIC = "rkt_vltava_req_test";
    private final static String GROUP = "group_vltava_service";

    @Autowired
    SpringContextUtils springContextUtils;

    @Autowired
    TraceService traceService;

    @Autowired
    MqStrategyFactory mqStrategyFactory;

    @PostConstruct
    public void mqConsumer() {
        if (springContextUtils.inDev()) {
            logger.info("dev环境，跳过MQ");
            return;
        }
        MonitorConfig monitorConfig = new MonitorConfig(PROJECT_CODE, APP_CODE, DMC_SERVER_ADDRESS, true);
        monitorConfig.monitorInit();

        AuthInfo authInfo = new AuthInfo(SECRET_KEY, DMG_SERVER_ADDRESS, 1000);

        /*
         * 更多属性配置，请参考：WIKI：http://wiki.dmall.com:8090/pages/viewpage.action?pageId=18404458
         */
        Properties props = new Properties();
        /*
        从哪里开始消费,
        默认 CONSUME_FROM_LAST_OFFSET 从最新的消息开始消费。
        CONSUME_FROM_FIRST_OFFSET 从磁盘上（超过7天的消息会被删）的第一条消息开始消费
        */
        props.setProperty(Constants.RocketConsumer.CONSUME_FROM_WHERE_NAME, "CONSUME_FROM_LAST_OFFSET");

        CoreRocketConsumerImpl rocketconsumer = new CoreRocketConsumerImpl();
        rocketconsumer.setAuthInfo(authInfo);
        rocketconsumer.setTopic(TOPIC);
        rocketconsumer.setGroup(GROUP);
        //非必填,默认: *
        //rocketconsumer.setTags("*");
        rocketconsumer.setProps(props);
        rocketconsumer.setMessageHandler(new RocketMessageHandler() {
            @HandleException
            @Override
            public void onMessage(List<RocketMsgExt> rktMsgs) {
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
        });
        try {
            rocketconsumer.doInit();
        } catch (DMGAuthException e) {
            e.printStackTrace();
            try {
                logger.info("等待三秒后将重试连接rocketMQ...");
                Thread.sleep(3000);
                rocketconsumer.doInit();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        rocketconsumer.start();
        logger.info("MQ初始化成功！");
    }

}
