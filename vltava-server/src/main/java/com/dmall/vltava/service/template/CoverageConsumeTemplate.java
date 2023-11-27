package com.testhuamou.vltava.service.template;

import com.alibaba.fastjson.JSON;
import com.testhuamou.vltava.domain.base.AgentMsgVO;
import com.testhuamou.vltava.domain.coverage.TraceMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rob
 * @date Create in 11:10 AM 2020/3/19
 */
@Component("CoverageConsumeTemplate")
public class CoverageConsumeTemplate extends AbstractConsumeTemplate {

    private static Boolean mongoStatus;
    private static Boolean sqlStatus;

    @Override
    protected void checkStatus() {

    }

    @Override
    protected void digestMsg(AgentMsgVO agentMsgVO) {
        TraceMsgVO traceMsgVO = JSON.parseObject(JSON.toJSONString(agentMsgVO.getMsgData()),TraceMsgVO.class);

    }

    @Override
    protected void storeTask() {

    }

    @Override
    protected void storeMsg(AgentMsgVO agentMsgVO) {

    }

    @Override
    protected void checkAndSyncData() {

    }
}
