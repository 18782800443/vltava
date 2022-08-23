package com.dmall.vltava.domain.jenkins;

import lombok.Data;

import java.util.Date;

/**
 * @author Rob
 * @date Create in 11:42 AM 2019/11/26
 */
@Data
public class JenkinsBuildVO {
    private String jobName;
    private Integer lastSuccessfulBuild;
    private String branch;
    private String gitVersion;
    private String builder;
    private Date buildTime;

}
