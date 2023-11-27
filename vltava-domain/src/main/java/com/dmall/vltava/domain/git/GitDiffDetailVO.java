package com.testhuamou.vltava.domain.git;

import lombok.Data;

import java.util.List;

/**
 * @author Rob
 * @date Create in 11:37 AM 2019/11/21
 */
@Data
public class GitDiffDetailVO {
    private String oldPath;
    private String newPath;
    private Boolean newFile;
    private Boolean renamedFile;
    private Boolean deletedFile;
    private String classDefine;
    private List<GitCodeBlock> codeBlockList;


}
