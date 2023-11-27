package com.testhuamou.vltava.domain.coverage.task;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author Rob
 * @date Create in 4:36 PM 2020/3/19
 */
@Data
public class FileCoverageVO {
    private String fileName;
    private Map<Integer, LineCoverageVO> lineCoverageMap;
    private Float latestVersionCoverageRate;
    private Float allVersionCoverageRate;
}
