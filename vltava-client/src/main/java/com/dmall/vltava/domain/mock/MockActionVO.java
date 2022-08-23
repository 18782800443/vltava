package com.dmall.vltava.domain.mock;


import java.io.Serializable;

/**
 * @author Rob
 * @date Create in 1:42 下午 2020/5/26
 */
public class MockActionVO implements Serializable {
    String expectKey;
    /**
     * MockTypeEnum
     */
    Integer mockType;
    String expectValue;
    SleepTimeVO sleepTimeVO;
    String errorMsg;

    public String getExpectKey() {
        return expectKey;
    }

    public void setExpectKey(String expectKey) {
        this.expectKey = expectKey;
    }

    public Integer getMockType() {
        return mockType;
    }

    public void setMockType(Integer mockType) {
        this.mockType = mockType;
    }

    public String getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String expectValue) {
        this.expectValue = expectValue;
    }

    public SleepTimeVO getSleepTimeVO() {
        return sleepTimeVO;
    }

    public void setSleepTimeVO(SleepTimeVO sleepTimeVO) {
        this.sleepTimeVO = sleepTimeVO;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
