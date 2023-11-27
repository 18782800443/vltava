package com.testhuamou.vltava.domain.enums;

import com.testhuamou.vltava.domain.base.CommonException;

/**
 * @author Rob
 * @date Create in 2:22 PM 2020/2/10
 */
public enum TaskStatusEnum {
    PREPARE(0, "准备中"),
    READY(1,"待运行"),
    RUNNING(2, "运行中"),
    PAUSE(3,"暂停中"),
    END(4, "结束"),
    ERROR(5, "异常");

    private TaskStatusEnum(Integer key, String desc) {
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
        for (TaskStatusEnum e:TaskStatusEnum.class.getEnumConstants()){
            if (e.key.equals(key)){
                return e.desc;
            }
        }
        throw new CommonException("没有对应枚举:" + key);
    }

}
