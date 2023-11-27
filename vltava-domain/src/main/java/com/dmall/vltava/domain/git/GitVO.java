package com.testhuamou.vltava.domain.git;

import lombok.Data;

/**
 * @author Rob
 * @date Create in 5:17 PM 2019/11/20
 */
@Data
public class GitVO {
    private Integer gitId;
    private String branchName;
    private String fromVersion;
    private String toVersion;
    private String gitName;


}
