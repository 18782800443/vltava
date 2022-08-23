package com.dmall.vltava.service;

import com.dmall.vltava.domain.app.AppVO;
import com.dmall.vltava.domain.base.BaseFilter;
import com.dmall.vltava.domain.base.FilteredResult;
import com.dmall.vltava.domain.mock.RegisterVO;

import java.util.List;
import java.util.Map;

/**
 * @author Rob
 * @date Create in 1:31 PM 2020/1/14
 */
public interface AppService{
    /**
     * 保存应用
     * @param appVO
     */
    void save(AppVO appVO);

    /**
     * 确认是否存在
     * @param appVO
     * @return
     */
    Boolean appExists(AppVO appVO);

    /**
     * 获取过滤后的应用列表
     * @param baseFilter
     * @return
     */
    FilteredResult<List<AppVO>> getFilteredList(BaseFilter baseFilter);

    /**
     * 获取应用信息 cache
     * @param appId
     * @return
     */
    AppVO getAppInfo(Integer appId);

    /**
     * 通过注册信息获取应用信息
     * @param registerVO reg
     * @return
     */
    AppVO getAppInfoByRegisterInfo(RegisterVO registerVO);
}
