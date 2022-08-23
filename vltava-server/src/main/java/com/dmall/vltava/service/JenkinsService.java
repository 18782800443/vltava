package com.dmall.vltava.service;

import com.dmall.vltava.domain.git.GitVO;
import com.dmall.vltava.domain.jenkins.JenkinsBuildVO;

/**
 * @author Rob
 * @date Create in 11:40 AM 2019/11/26
 */
public interface JenkinsService {
    /**
     * 获取当前jenkins工程信息
     * @param jenkinsBuildVO vo
     * @return vo
     */
    void getLatestBuildInfo(JenkinsBuildVO jenkinsBuildVO);

    /**
     * 将jenkins信息存入缓存
     * @param jenkinsBuildVO vo
     */
    void saveCache(JenkinsBuildVO jenkinsBuildVO);

    /**
     * 获取当前系统jenkins信息
     * @param systemId 系统id
     * @return
     */
    JenkinsBuildVO getCache(Integer systemId);

    /**
     * 是否存在该job
     * @param jobName
     * @return
     */
    Boolean projectExists(String jobName);

}
