package com.testhuamou.vltava.domain.git;

import lombok.Data;

import java.util.List;

/**
 * @author Rob
 * @date Create in 11:34 AM 2019/11/21
 */
@Data
public class GitDiffVO extends GitVO {
    private List<GitCommitVO> commitList;
    private List<GitDiffDetailVO> detailList;


}
