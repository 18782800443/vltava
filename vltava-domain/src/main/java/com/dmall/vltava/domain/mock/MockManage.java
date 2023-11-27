package com.testhuamou.vltava.domain.mock;

import tk.mybatis.mapper.annotation.Version;

import java.util.Date;
import javax.persistence.*;

@Table(name = "mock_manage")
public class MockManage {
    @Id
    private Integer id;

    @Column(name = "app_id")
    private Integer appId;

    private String actions;

    @Column(name = "task_status")
    private Integer taskStatus;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    private Integer yn;

    @Version
    private Integer version;

    private Integer implicit;

    private String descriptions;

    private Integer connect;

    public String getMockKey() {
        return mockKey;
    }

    public void setMockKey(String mockKey) {
        this.mockKey = mockKey;
    }

    @Column(name = "mock_key")
    private String mockKey;

    public Integer getConnect() {
        return connect;
    }

    public void setConnect(Integer connect) {
        this.connect = connect;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return app_id
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * @return actions
     */
    public String getActions() {
        return actions;
    }

    /**
     * @param actions
     */
    public void setActions(String actions) {
        this.actions = actions;
    }

    /**
     * @return task_status
     */
    public Integer getTaskStatus() {
        return taskStatus;
    }

    /**
     * @param taskStatus
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return yn
     */
    public Integer getYn() {
        return yn;
    }

    /**
     * @param yn
     */
    public void setYn(Integer yn) {
        this.yn = yn;
    }

    /**
     * @return version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getImplicit() {
        return implicit;
    }

    public void setImplicit(Integer implicit) {
        this.implicit = implicit;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

}