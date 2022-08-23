package com.dmall.vltava.domain.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Rob
 * @date Create in 5:43 PM 2020/3/12
 */
@Data
public class VersionVO implements Serializable {
    public VersionVO(Integer taskStatus, Integer version, Object data){
        this.taskStatus = taskStatus;
        this.version = version;
        this.data = data;
    }
    public VersionVO(Integer taskStatus, Integer version){
        this.taskStatus = taskStatus;
        this.version = version;
    }
    private Integer taskStatus;
    private Integer version;
    private Object data;

}
