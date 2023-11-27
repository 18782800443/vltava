package com.testhuamou.vltava.domain.enums;

import com.testhuamou.vltava.domain.base.CommonException;

/**
 * @author Rob
 * @date Create in 4:11 下午 2020/5/25
 */
public enum MockTypeEnum {
    RETURN_VALUE(0, "returnValue"),
    SLEEP(1, "sleep"),
    THROW_EXCEPTION(2, "throwException");

    private MockTypeEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    private Integer key;

    private String desc;

    public Integer getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByKey(Integer key){
        for (MockTypeEnum e:MockTypeEnum.class.getEnumConstants()){
            if (e.key.equals(key)){
                return e.desc;
            }
        }
        throw new CommonException("没有对应枚举:" + key);
    }

    public static Boolean containsKey(Integer key){
        for (MockTypeEnum e:MockTypeEnum.class.getEnumConstants()){
            if (e.key.equals(key)){
                return true;
            }
        }
        return false;
    }
}
