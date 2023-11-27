package com.testhuamou.vltava.domain.enums;

import com.testhuamou.vltava.domain.base.CommonException;

/**
 * @author Rob
 * @date Create in 2:22 PM 2020/2/10
 */
public enum ErrorCodeEnum {
    UPDATE_REDIS_FAIL("0001","更新redis失败");

    private ErrorCodeEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    private String key;

    private String desc;

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByKey(Integer key){
        for (ErrorCodeEnum e:ErrorCodeEnum.class.getEnumConstants()){
            if (e.key.equals(key)){
                return e.desc;
            }
        }
        throw new CommonException("没有对应枚举:" + key);
    }

}
