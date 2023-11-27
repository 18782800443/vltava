package com.testhuamou.vltava.domain.mock.client;


import java.io.Serializable;

/**
 * @author Rob
 * @date Create in 1:49 下午 2020/9/10
 */
public class MockClientBase implements Serializable {
    /**
     * mock id
     */
    protected Integer id;
    /**
     * 应用id
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
