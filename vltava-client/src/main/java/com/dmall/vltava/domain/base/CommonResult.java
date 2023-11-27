package com.testhuamou.vltava.domain.base;


import java.io.Serializable;

/**
 * @author Rob
 * @date Create in 1:52 PM 2019/11/20
 */
public class CommonResult implements Serializable {


    private boolean success;
    private String msg;
    private Object data;

    public CommonResult(){}

    public CommonResult(Boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public static CommonResult pass(String msg) {
        return new CommonResult(true, msg, "");
    }

    public static CommonResult pass(Object data) {
        return new CommonResult(true, "", data);
    }

    public static CommonResult pass(String msg, Object data) {
        return new CommonResult(true, msg, data);
    }


    public static CommonResult fail(String msg, Object data) {
        return new CommonResult(false, msg, data);
    }

    public static CommonResult fail(String msg) {
        return new CommonResult(false, msg, "");
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
