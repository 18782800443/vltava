package com.dmall.vltava.domain.coverage;


import com.dmall.vltava.domain.base.AgentMsgVO;

import java.util.Date;

/**
 * @author Rob
 * @date Create in 11:11 AM 2020/1/10
 */
public class TraceDO {
    private Date createTime;
    private Integer jenkinsBuildId;
    private AgentMsgVO data;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getJenkinsBuildId() {
        return jenkinsBuildId;
    }

    public void setJenkinsBuildId(Integer jenkinsBuildId) {
        this.jenkinsBuildId = jenkinsBuildId;
    }

    public AgentMsgVO getData() {
        return data;
    }

    public void setData(AgentMsgVO data) {
        this.data = data;
    }
}
