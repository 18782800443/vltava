package com.dmall.vltava.service.factory;

import com.dmall.vltava.domain.base.AgentMsgVO;
import com.dmall.vltava.domain.enums.AttachTypeEnum;
import com.dmall.vltava.service.stategy.MqStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rob
 * @date Create in 5:34 PM 2020/2/27
 */
@Service
public class MqStrategyFactory {
    private static final Logger logger = LoggerFactory.getLogger(MqStrategyFactory.class);

    @Autowired
    Map<String, MqStrategy> mqStrategyMap = new HashMap<>(4);

    public MqStrategy getMqConsumeInstance(AgentMsgVO agentMsgVO) {
        String key = AttachTypeEnum.getDescByKey(agentMsgVO.getTaskType() % MqStrategy.RATE_ADDTION) + (agentMsgVO.getTaskType() > MqStrategy.RATE_ADDTION ? "Rate" : "");
        return mqStrategyMap.get(key);
    }

    public MqStrategy getMockRegStrategy(){
        return mqStrategyMap.get("MockMqStrategy");
    }
}
