package com.testhuamou.vltava.domain.base;

/**
 * @author Rob
 */
public class AgentMsgVO<T> {
    private Integer taskId;
    // 分类枚举，attach进度为 10000+对应枚举值
    private Integer taskType;
    private T msgData;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public T getMsgData() {
        return msgData;
    }

    public void setMsgData(T msgData) {
        this.msgData = msgData;
    }

    public AgentMsgVO(Integer taskId, Integer taskType, T msgData){
        this.taskId = taskId;
        this.taskType = taskType;
        this.msgData = msgData;
    }
}
