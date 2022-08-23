package com.dmall.vltava.domain.mock;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rob
 * @date Create in 11:04 上午 2020/12/10
 */
@Data
public class TraceVO {
    String tid;
    List<InvokeVO> invokeList;
    String startTime;
    String endTime;

    public TraceVO(String tid) {
        this.tid = tid;
        this.startTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss.SSS");
        this.invokeList = new ArrayList<>();
    }
}
