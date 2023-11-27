package com.testhuamou.vltava.service;

import com.testhuamou.vltava.domain.coverage.TraceDO;

/**
 * @author Rob
 * @date Create in 11:14 AM 2020/1/10
 */
public interface TraceService {
    /**
     * 保存trace记录
     * @param traceDO DO
     */
    void save(TraceDO traceDO);

}
