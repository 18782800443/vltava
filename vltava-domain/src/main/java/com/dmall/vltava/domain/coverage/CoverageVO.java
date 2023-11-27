package com.testhuamou.vltava.domain.coverage;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Rob
 * @date Create in 10:04 AM 2020/1/21
 */
@Data
public class CoverageVO  extends  CoverageManage{

    private String appName;

    private AtomicInteger serialNum;

    private String jenkinsJobName;


}
