package com.testhuamou.vltava.controller;

import com.alibaba.fastjson.JSON;
import com.testhuamou.dmg.sdk4.rocketmq.consumer.RocketMessageHandler;
import com.testhuamou.dmg.sdk4.rocketmq.message.RocketMsg;
import com.testhuamou.dmg.sdk4.rocketmq.message.RocketMsgExt;
import com.testhuamou.vltava.dao.AppManageMapper;
import com.testhuamou.vltava.dao.MockManageMapper;
import com.testhuamou.vltava.domain.app.AppVO;
import com.testhuamou.vltava.domain.mock.MockManage;
import com.testhuamou.vltava.domain.mock.MockVO;
import com.testhuamou.vltava.domain.mock.RegisterVO;
import com.testhuamou.vltava.manager.MockManager;
import com.testhuamou.vltava.service.MockService;
import com.testhuamou.vltava.service.TraceService;
import com.testhuamou.vltava.service.factory.MqStrategyFactory;
import com.testhuamou.vltava.utils.HttpUtils;
import com.testhuamou.vltava.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    MockService mockService;

    @Autowired
    MockManageMapper mockManageMapper;

    @Autowired
    AppManageMapper appManageMapper;

    @Autowired
    MockManager mockManager;

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
            //this.mockVoRSend(registerVO);
        }
    }
//
//    private void mockVoRSend(RegisterVO registerVO) {
//        //每次收到服务端注册或更新app信息的MQ就进行策略重推
//        String systemUniqueName = registerVO.getSystemUniqueName();
//        String zone = registerVO.getZone();
//        String group = registerVO.getGroup();
//        if (Objects.nonNull(systemUniqueName) && Objects.nonNull(zone) && Objects.nonNull(group)) {
//            //通过systemUniqueName查询mock策略
//            List<MockManage> mockManages = null;
//            try {
//                mockManages = mockManageMapper.getMocks(systemUniqueName, zone, group);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            List<MockVO> mockVOS = null;
//            if (Objects.nonNull(mockManages)) {
//                mockVOS = mockManages.stream().map(c -> mockManager.convert(c)).collect(Collectors.toList());
//            }
//            try {
//                mockVOS.stream().forEach(mockVO -> HttpUtils.updateData(registerVO, Collections.singletonList(mockVO)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
