package com.testhuamou.vltava.domain.coverage;

import com.testhuamou.vltava.util.ExtendedNextVersion;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "coverage_manage")
public class CoverageManage {
    @Id
    private Integer id;

    @Column(name = "app_id")
    private Integer appId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "file_filters")
    private String fileFilters;

    @Column(name = "msg_filters")
    private String msgFilters;

    @Column(name = "task_status")
    private Integer taskStatus;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    @Version(nextVersion = ExtendedNextVersion.class)
    private Integer version;

    private Integer yn;

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
     * @return task_name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getFileFilters() {
        return fileFilters;
    }

    public void setFileFilters(String fileFilters) {
        this.fileFilters = fileFilters;
    }

    public String getMsgFilters() {
        return msgFilters;
    }

    public void setMsgFilters(String msgFilters) {
        this.msgFilters = msgFilters;
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

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}