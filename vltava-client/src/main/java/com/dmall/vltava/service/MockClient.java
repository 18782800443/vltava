package com.testhuamou.vltava.service;

import com.testhuamou.vltava.domain.base.CommonResult;
import com.testhuamou.vltava.domain.mock.client.MockRule;
import com.testhuamou.vltava.domain.mock.client.MockStatus;

/**
 * @author Rob
 * @date Create in 1:55 下午 2020/9/14
 */
public interface MockClient {

    /**
     * 获取对应id的mock规则信息
     * @param id
     * @return allMockRules
     */
    public CommonResult get(Integer id);
    /**
     * 添加mock rule， 新增覆盖类似map.put逻辑
     * @param mockRule
     * @return allMockRules
     */
    public CommonResult put(MockRule mockRule);

    /**
     * 删除mock rule
     * @param mockRule
     * @return
     */
    public CommonResult remove(MockRule mockRule);

    /**
     * 更改任务状态， 仅支持开始、暂停
     * @param mockStatus
     * @return
     */
    public CommonResult updateStatus(MockStatus mockStatus);

    /**
     * 获取序列化后的string，需传入实例化的请求体
     * 返回结果供mock返回参数使用
     * @param obj
     * @return
     */
    public CommonResult serializeObj(Object obj);
}
