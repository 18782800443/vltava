package com.dmall.vltava.domain.git;

import lombok.Data;

import java.util.Date;

/**
 * @author Rob
 * @date Create in 11:35 AM 2019/11/21
 */
@Data
public class GitCommitVO {
    private String commitId;
    private String title;
    private String message;
    private String author;
    private Date commitTime;


}
