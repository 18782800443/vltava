package com.testhuamou.vltava.service.impl;

import com.testhuamou.vltava.domain.base.AgentMsgVO;
import com.testhuamou.vltava.service.MQService;
import org.springframework.stereotype.Service;

/**
 * @author Rob
 * @date Create in 2:35 PM 2020/1/20
 */
@Service("MQService")
public class MQServiceImpl implements MQService {

    @Override
    public void consume(AgentMsgVO agentMsgVO) {

    }
}
