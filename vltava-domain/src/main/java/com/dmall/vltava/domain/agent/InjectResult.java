package com.testhuamou.vltava.domain.agent;

import com.testhuamou.vltava.domain.enums.InjectStatusEnum;
import com.testhuamou.vltava.domain.mock.MockActionVO;
import com.testhuamou.vltava.domain.mock.MockVO;

import java.util.List;

/**
 * @author Rob
 * @date Create in 11:15 上午 2020/11/10
 */
public class InjectResult {
    private String injectKey;
    private String msg;
    // 取reference
    private List<MockActionVO> failActionList;

    public InjectResult(String injectKey, String msg, List<MockActionVO> failActionList) {
        this.injectKey = injectKey;
        this.msg = msg;
        this.failActionList = failActionList;
    }

    public InjectResult(String injectKey, String msg) {
        this.injectKey = injectKey;
        this.msg = msg;
    }

    public InjectResult(String injectKey) {
        this.injectKey = injectKey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MockActionVO> getFailActionList() {
        return failActionList;
    }

    public void setFailActionList(List<MockActionVO> failActionList) {
        this.failActionList = failActionList;
    }

    public String getInjectKey() {
        return injectKey;
    }

    public void setInjectKey(String injectKey) {
        this.injectKey = injectKey;
    }
}
