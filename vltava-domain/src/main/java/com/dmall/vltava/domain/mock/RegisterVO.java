package com.dmall.vltava.domain.mock;

/**
 * @author Rob
 * @date Create in 3:28 下午 2020/7/29
 */
public class RegisterVO {
    private String ip;
    private Integer port;
    private String systemUniqueName;
    private String zone;
    private String group;
    private Long time;

    public RegisterVO(){}

    public RegisterVO(String systemUniqueName, String zone, String group){
        this.systemUniqueName = systemUniqueName;
        this.zone = zone;
        this.group = group;
    }

    public RegisterVO(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSystemUniqueName() {
        return systemUniqueName;
    }

    public void setSystemUniqueName(String systemUniqueName) {
        this.systemUniqueName = systemUniqueName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
