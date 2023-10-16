package com.dmall.vltava.service;

import com.dmall.vltava.domain.mock.RegisterVO;

import java.util.List;

/**
 * @author Rob
 * @date Create in 3:48 下午 2020/7/29
 */
public interface DockerManageService {
    /**
     * 保存/更新
     * @param registerVO
     * @return 返回是否需要推送数据到agent
     */
    public Boolean save(RegisterVO registerVO);

    /**
     * 获取最新消息 name + zone下的所有分组信息
     * @param registerVO
     * @return
     */
    public List<RegisterVO> getSystemInfoAllGroup(RegisterVO registerVO);
    /**
     * 获取最新消息 name + zone + group
     * @param registerVO
     * @return
     */
    public RegisterVO getSystemInfo(RegisterVO registerVO);
}
