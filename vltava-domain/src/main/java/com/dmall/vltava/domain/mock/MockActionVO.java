package com.dmall.vltava.domain.mock;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Rob
 * @date Create in 1:42 下午 2020/5/26
 */
@Data
public class MockActionVO implements Serializable {
    String expectKey;
    /**
     * MockTypeEnum
     */
    Integer mockType;
    String expectValue;
    SleepTimeVO sleepTimeVO;
    String errorMsg;
    String mockKey;
    String className;
    String methodName;
    Boolean implicit;
    Integer taskStatus;
    Boolean entrance;
    String uid;
    Integer id;
    Boolean dynamicChange;
    String requestPath;
    String responsePath;

}
