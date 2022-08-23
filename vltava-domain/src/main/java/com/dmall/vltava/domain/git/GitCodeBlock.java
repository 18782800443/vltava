package com.dmall.vltava.domain.git;

import java.util.List;

/**
 * @author Rob
 * @date Create in 1:49 PM 2019/11/21
 */
public class GitCodeBlock {
    private Integer beginLine;
    private List<GitLineVO> lineList;

    public Integer getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(Integer beginLine) {
        this.beginLine = beginLine;
    }

    public List<GitLineVO> getLineList() {
        return lineList;
    }

    public void setLineList(List<GitLineVO> lineList) {
        this.lineList = lineList;
    }
}
