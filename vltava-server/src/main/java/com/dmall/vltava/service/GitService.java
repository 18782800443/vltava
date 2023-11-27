package com.testhuamou.vltava.service;

import com.testhuamou.vltava.domain.git.GitCommitVO;
import com.testhuamou.vltava.domain.git.GitDiffVO;
import com.testhuamou.vltava.domain.git.GitVO;

import java.util.List;
import java.util.Map;

/**
 * @author Rob
 * @date Create in 1:46 PM 2019/11/20
 */
public interface GitService {
    /**
     * 获取项目名称和id
     * @return map
     */
    public Map<String,Integer> getProjectMap();

    /**
     * 根据提供版本号返回增量代码
     * @param gitVO vo
     * @return vo
     */
    public GitDiffVO getIncreasedCode(GitVO gitVO);

    /**
     * 根据项目id获取commits
     * @param gitVO vo
     * @return list
     */
    public List<GitCommitVO> getProjectCommits(GitVO gitVO);

    /**
     * 判断是否存在，若存在则赋值Id (请求一次git api，返回信息带有id)
     * @param gitVO
     * @return
     */
    public Boolean getGitInfo(GitVO gitVO);

}
