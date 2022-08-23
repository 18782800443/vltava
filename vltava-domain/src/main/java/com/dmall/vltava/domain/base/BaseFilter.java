package com.dmall.vltava.domain.base;

import lombok.Data;

/**
 * @author Rob
 * @date Create in 3:45 PM 2020/1/13
 */
@Data
public class BaseFilter {
    private Integer pageNum;
    private Integer pageSize;
    private Integer appId;
    private String appName;
    private String taskName;
    private String className;
    private String methodName;
    private String description;
}
