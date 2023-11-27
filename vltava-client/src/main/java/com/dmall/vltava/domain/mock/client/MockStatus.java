package com.testhuamou.vltava.domain.mock.client;

/**
 * @author Rob
 * @date Create in 1:48 下午 2020/9/10
 */
public class MockStatus extends MockClientBase{
    /**
     * TaskStatusEnum
     */
    Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
