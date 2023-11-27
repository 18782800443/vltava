package com.testhuamou.vltava.domain.enums;

import com.testhuamou.vltava.domain.base.CommonException;

/**
 * @author Rob
 * @date Create in 2:22 PM 2020/2/10
 */
public enum AttachTypeEnum {
    /**
     * attach进度Type为对应枚举 + 10000, desc需要首字母大写满足AgentController反射
     */
    COVERAGE(1, "Coverage"),
    MOCK(2, "Mock");

    private AttachTypeEnum(Integer key, String desc) {
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
        for (AttachTypeEnum e:AttachTypeEnum.class.getEnumConstants()){
            if (e.key.equals(key)){
                return e.desc;
            }
        }
        throw new CommonException("没有对应枚举:" + key);
    }

}
