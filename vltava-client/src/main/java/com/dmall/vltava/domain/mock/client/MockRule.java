package com.testhuamou.vltava.domain.mock.client;

import com.testhuamou.vltava.domain.mock.MockActionVO;


import java.util.List;

/**
 * @author Rob
 * @date Create in 1:48 下午 2020/9/10
 */
public class MockRule extends MockClientBase{
    /**
     * 期望行为
     */
    List<MockActionVO> rules;
    /**
     * 是否生成mockKey？
     * true时：
     * 1. 不需要在MockActionVO设置expectKey，会以生成的mockKey为唯一id进行匹配
     * 2. 返回结果中会带有生成的mockKey，请查看后保存
     * 3. 使用R-DOT框架的同学需要触发此mockAction时，
     *    调用com.testhuamou.rdot.action.base.BaseRPC#autoRun(java.lang.String) || com.testhuamou.rdot.action.base.BaseRPC#autoRunAsObj(java.lang.String)
     * 4. 不使用框架的同学需要触发此mockAction时，请求需带上隐式传参 {"vltavaMockKey": mockKey}
     *
     * false时： 必须要在MockActionVO设置expectKey
     */
    Boolean generateMockKey;

    public Boolean getGenerateMockKey() {
        return generateMockKey;
    }

    public void setGenerateMockKey(Boolean generateMockKey) {
        this.generateMockKey = generateMockKey;
    }

    public List<MockActionVO> getRules() {
        return rules;
    }

    public void setRules(List<MockActionVO> rules) {
        this.rules = rules;
    }
}
