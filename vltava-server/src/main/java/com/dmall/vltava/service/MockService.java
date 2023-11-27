package com.testhuamou.vltava.service;

import com.testhuamou.vltava.domain.base.*;
import com.testhuamou.vltava.domain.mock.MockVO;
import com.testhuamou.vltava.domain.mock.RegisterVO;

import java.util.List;

/**
 * @author Rob
 * @date Create in 3:09 下午 2020/5/26
 */
public interface MockService {
    /**
     * 保存任务
     * @param mockVO
     */
    CommonResult save(MockVO mockVO);

    /**
     * 设置task对应的状态值
     * @param mockVO
     */
    Boolean updateStatus(MockVO mockVO);

    /**
     * 设置所有分组对应的状态值
     * @param mockVO
     * @return
     */
    List<RegisterVO> updateStatusAllGroup(MockVO mockVO);

    /**
     * 重试
     * @param mockVO
     * @return
     */
    CommonResult retry(MockVO mockVO);

    /**
     * 整理数据
     *
     * @param mockVO
     */
    void formatData(MockVO mockVO);

}
