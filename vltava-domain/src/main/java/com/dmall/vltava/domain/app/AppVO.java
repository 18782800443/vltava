package com.dmall.vltava.domain.app;

import com.dmall.vltava.util.ExtendedNextVersion;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "app_manage")
public class AppVO {
    @Id
    private Integer id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "system_unique_name")
    private String systemUniqueName;

    @Column(name = "zone")
    private String zone;

    @Column(name = "build_group")
    private String buildGroup;

    @Version(nextVersion = ExtendedNextVersion.class)
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "_tenant_id")
    private Long tenantId;

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
     * @return app_name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }


    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSystemUniqueName() {
        return systemUniqueName;
    }

    public void setSystemUniqueName(String systemUniqueName) {
        this.systemUniqueName = systemUniqueName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getBuildGroup() {
        return buildGroup;
    }

    public void setBuildGroup(String buildGroup) {
        this.buildGroup = buildGroup;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}