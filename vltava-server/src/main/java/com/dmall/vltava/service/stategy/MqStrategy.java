package com.testhuamou.vltava.service.stategy;

import com.testhuamou.vltava.domain.base.AgentMsgVO;
import com.testhuamou.vltava.domain.mock.RegisterVO;

/**
 * @author Rob
 * @date Create in 5:35 PM 2020/2/27
 */
public interface MqStrategy {
    public static final Integer RATE_ADDTION = 10000;

    /**
     * mq消费
     * @param agentMsgVO
     */
    void consume(AgentMsgVO agentMsgVO);

    /**
     * mq注册
     * @param registerVO
     */
    void register(RegisterVO registerVO);
}
