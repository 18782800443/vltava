package com.testhuamou.vltava.domain;

import javax.persistence.*;

@Table(name = "docker_manage")
public class DockerManage {
    @Id
    private Long id;

    @Column(name = "system_unique_name")
    private String systemUniqueName;

    private String zone;

    private String ip;

    @Column(name = "reg_time")
    private Long regTime;

    @Column(name = "_port")
    private Integer port;

    @Column(name = "build_group")
    private String buildGroup;

    public String getBuildGroup() {
        return buildGroup;
    }

    public void setBuildGroup(String buildGroup) {
        this.buildGroup = buildGroup;
    }

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return system_unique_name
     */
    public String getSystemUniqueName() {
        return systemUniqueName;
    }

    /**
     * @param systemUniqueName
     */
    public void setSystemUniqueName(String systemUniqueName) {
        this.systemUniqueName = systemUniqueName;
    }

    /**
     * @return zone
     */
    public String getZone() {
        return zone;
    }

    /**
     * @param zone
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return reg_time
     */
    public Long getRegTime() {
        return regTime;
    }

    /**
     * @param regTime
     */
    public void setRegTime(Long regTime) {
        this.regTime = regTime;
    }

    /**
     * @return _port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @param port
     */
    public void setPort(Integer port) {
        this.port = port;
    }
}