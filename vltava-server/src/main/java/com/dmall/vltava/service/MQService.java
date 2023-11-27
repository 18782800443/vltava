package com.testhuamou.vltava.service;

import com.testhuamou.vltava.domain.base.AgentMsgVO;

/**
 * @author Rob
 * @date Create in 2:35 PM 2020/1/20
 */
public interface MQService {
    /**
     * 消费agentMsg接口
     * @param agentMsgVO
     */
    void consume(AgentMsgVO agentMsgVO);


}
