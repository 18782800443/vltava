package com.testhuamou.vltava.domain.enums;

import com.testhuamou.vltava.domain.base.CommonException;

/**
 * @author Rob
 * @date Create in 10:34 上午 2020/8/28
 */
public enum InjectStatusEnum {
    SUCCESS("success", "注入成功"), FAIL("FAIL", "注入失败"), PART("PART", "部分成功");

    InjectStatusEnum(String key, String desc) {
        this.key =key;
        this.desc =desc;
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
        for (InjectStatusEnum e:InjectStatusEnum.class.getEnumConstants()){
            if (e.key.equals(key)){
                return e.desc;
            }
        }
        throw new CommonException("没有对应枚举:" + key);
    }
}
