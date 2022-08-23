package com.dmall.vltava.domain.mock;

import lombok.Data;

/**
 * @author Rob
 * @date Create in 11:06 上午 2020/12/10
 */
@Data
public class InvokeVO {
    public InvokeVO(String className, String methodName, String request) {
        this.className = className;
        this.methodName = methodName;
        this.request = request;
    }

    String className;
    String methodName;
    String request;
    Boolean success;
    String response;
    Throwable throwable;

    public void endWithResp(String resp){
        this.response = resp;
        this.success = true;
    }

    public void endWithError(Throwable throwable){
        this.throwable = throwable;
        this.success = false;
    }
}
