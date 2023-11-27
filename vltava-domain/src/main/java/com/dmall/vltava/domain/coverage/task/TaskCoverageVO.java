package com.testhuamou.vltava.domain.coverage.task;

import com.testhuamou.vltava.domain.coverage.CoverageVO;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Map;

/**
 * @author Rob
 * @date Create in 4:48 PM 2020/3/19
 */
@Entity()
@Data
public class TaskCoverageVO extends CoverageVO {
    // 需要加锁，所以不用线程安全的

    private Map<String, FileCoverageVO> fileCoverageMap;
    private Float latestVersionTaskCoverageRate;
    private Float allVersionTaskCoverageRate;
    private Integer agentMsgCount;
}
