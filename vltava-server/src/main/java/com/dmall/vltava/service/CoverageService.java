package com.testhuamou.vltava.service;

import com.testhuamou.vltava.domain.app.AppVO;
import com.testhuamou.vltava.domain.base.BaseFilter;
import com.testhuamou.vltava.domain.base.FilteredResult;
import com.testhuamou.vltava.domain.base.VersionVO;
import com.testhuamou.vltava.domain.coverage.CoverageManage;
import com.testhuamou.vltava.domain.coverage.CoverageVO;

import java.util.List;
import java.util.Map;

/**
 * @author Rob
 * @date Create in 2:32 PM 2020/2/7
 */
public interface CoverageService {

    /**
     * 获取过滤后的列表
     * @param baseFilter
     * @return
     */
    FilteredResult<List<CoverageManage>> getFilteredList(BaseFilter baseFilter);

    /**
     * 保存任务
     * @param coverageVO
     */
    void save(CoverageVO coverageVO);

    /**
     * 判断是否存在重复名称
     * @param coverageVO
     * @return
     */
    Boolean exist(CoverageVO coverageVO);

    /**
     * 获取任务id
     * @param coverageVO
     * @return
     */
    Integer getTaskId(CoverageVO coverageVO);

    /**
     * 追踪准备工作：sandbox注入服务器进程
     * @param coverageVO
     * @return
     */
    void prepareTrace(CoverageVO coverageVO);

    /**
     * 设置task对应mysql + Redis key的状态值
     * @param coverageVO
     */
    Boolean updateStatus(CoverageVO coverageVO);

    /**
     * 设置task对应Redis key的状态值
     * @param coverageVO
     */
    Boolean updateRedisStatus(CoverageVO coverageVO);

    /**
     * 日常同步sql状态到redis
     */
    void syncStatus();

    /**
     * agent调用获取api侧最新版本
     * @param taskList
     * @return
     */
    Map<Integer,VersionVO> pullVersion(List<Integer> taskList);

    /**
     * 清除状态为end 且修改时间大于一天的任务，yn=0， redis更新状态不包括yn=0
     */
    void clearInvalid();

}
