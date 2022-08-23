package com.dmall.vltava.domain.coverage.task;

import lombok.Data;

import java.util.Set;

/**
 * @author Rob
 * @date Create in 4:37 PM 2020/3/19
 */
@Data
public class LineCoverageVO {
    private Integer lineNum;
    /**
     * 以jenkins+git build 当前git提交version为准
     */
    private Boolean hasCoveredInLatestVersion;
    private Boolean hasCoveredInOldVersion;
    private Set<String> coveredVersionSet;
}
