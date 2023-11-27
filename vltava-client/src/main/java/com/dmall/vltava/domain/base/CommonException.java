package com.testhuamou.vltava.domain.base;


/**
 * @author Rob
 * @date Create in 10:31 AM 2019/11/21
 */
public class CommonException extends RuntimeException {
    private String code;
    private String sourceMsg;
    private Object data;

    public CommonException(String msg){
        super(msg);
        this.sourceMsg = msg;
    };

    public CommonException(String msg, Object data){
        super(msg);
        this.sourceMsg = msg;
        this.data = data;
    }

    public CommonException(String msg, String code, Object data){
        super(msg);
        this.sourceMsg = msg;
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSourceMsg() {
        return sourceMsg;
    }

    public void setSourceMsg(String sourceMsg) {
        this.sourceMsg = sourceMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
