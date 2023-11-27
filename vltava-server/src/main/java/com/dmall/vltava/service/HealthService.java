package com.testhuamou.vltava.service;

/**
 * @author Rob
 * @date Create in 6:10 下午 2020/11/23
 */
public interface HealthService {
    /**
     * 对未结束的任务进行健康检查
     */
    void check();
}
