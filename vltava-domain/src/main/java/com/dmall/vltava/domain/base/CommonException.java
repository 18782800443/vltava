package com.testhuamou.vltava.domain.base;

import lombok.Data;

/**
 * @author Rob
 * @date Create in 10:31 AM 2019/11/21
 */
@Data
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


}
