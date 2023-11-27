package com.testhuamou.vltava.domain.mock;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Rob
 * @date Create in 5:01 下午 2020/5/25
 */
@Data
public class SleepTimeVO implements Serializable {
    Integer baseTime;
    // 0 =false 1= true
    Integer needRandom;
    Integer randomStart;
    Integer randomEnd;
    public Boolean isNeedRandom(){
        return needRandom == 1;
    }
}
