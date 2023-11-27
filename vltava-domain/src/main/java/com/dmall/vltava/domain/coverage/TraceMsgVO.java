package com.testhuamou.vltava.domain.coverage;

import java.util.Map;
import java.util.Set;

/**
 * @author Rob
 */
public class TraceMsgVO {
    private Integer systemId;
    private String traceId;
    private String entrance;
    /**
     * 每次调用均分为 layer + class + method 缩略码
     * 所以固定长度为3
     */
    private String tracePath;
    private String param;
    private Long startTime;
    private Long endTime;
    private Boolean throwErr;
    private Map<String,String> classMap;
    private Map<String,String> methodMap;
    private Map<String,Set<Integer>> coverMap;
    private Integer layer;

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getTracePath() {
        return tracePath;
    }

    public void setTracePath(String tracePath) {
        this.tracePath = tracePath;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Boolean getThrowErr() {
        return throwErr;
    }

    public void setThrowErr(Boolean throwErr) {
        this.throwErr = throwErr;
    }

    public Map<String, String> getClassMap() {
        return classMap;
    }

    public void setClassMap(Map<String, String> classMap) {
        this.classMap = classMap;
    }

    public Map<String, String> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<String, String> methodMap) {
        this.methodMap = methodMap;
    }

    public Map<String, Set<Integer>> getCoverMap() {
        return coverMap;
    }

    public void setCoverMap(Map<String, Set<Integer>> coverMap) {
        this.coverMap = coverMap;
    }
}
