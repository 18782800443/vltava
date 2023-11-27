package com.testhuamou.vltava.service.template;

import com.testhuamou.vltava.domain.base.AgentMsgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Rob
 * @date Create in 10:33 AM 2020/1/21
 */
@Component
abstract public class AbstractConsumeTemplate {
    public static final Logger logger = LoggerFactory.getLogger(AbstractConsumeTemplate.class);

    /**
     * 消费消息
     * @param agentMsgVO
     */
    public void consume(AgentMsgVO agentMsgVO){
        checkStatus();
        storeMsg(agentMsgVO);
        digestMsg(agentMsgVO);

    }

    /**
     * 检查mysql连接状态, 无法正常连接需要报错 -> mq消息报错会重试
     */
    abstract protected void checkStatus();

    /**
     * 解析 消化消息
     */
    abstract protected void digestMsg(AgentMsgVO agentMsgVO);

    /**
     * 任务存库 容灾, 定时任务
     */
    abstract protected void storeTask();

    /**
     * 消息存库
     */
    abstract protected void storeMsg(AgentMsgVO agentMsgVO);

    /**
     * mongo mysql对比消息，同步差异；定时任务
     */
    abstract protected void checkAndSyncData();



}
