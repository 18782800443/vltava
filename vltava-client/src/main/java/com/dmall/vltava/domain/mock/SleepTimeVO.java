package com.dmall.vltava.domain.mock;

import java.io.Serializable;

/**
 * @author Rob
 * @date Create in 5:01 下午 2020/5/25
 */
public class SleepTimeVO implements Serializable {
    Integer baseTime;
    // 0 =false 1= true
    Integer needRandom;
    Integer randomStart;
    Integer randomEnd;

    public Boolean isNeedRandom() {
        return needRandom == 1;
    }

    public Integer getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(Integer baseTime) {
        this.baseTime = baseTime;
    }

    public Integer getNeedRandom() {
        return needRandom;
    }

    public void setNeedRandom(Integer needRandom) {
        this.needRandom = needRandom;
    }

    public void setNeedRandom(Boolean needRandom) {
        this.needRandom = needRandom ? 1 : 0;
    }

    public Integer getRandomStart() {
        return randomStart;
    }

    public void setRandomStart(Integer randomStart) {
        this.randomStart = randomStart;
    }

    public Integer getRandomEnd() {
        return randomEnd;
    }

    public void setRandomEnd(Integer randomEnd) {
        this.randomEnd = randomEnd;
    }
}
