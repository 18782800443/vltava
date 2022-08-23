package com.dmall.vltava.utils;

import com.dmall.vltava.domain.annotation.HandleException;
import com.dmall.vltava.domain.base.VersionVO;
import com.dmall.vltava.domain.enums.TaskStatusEnum;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Rob
 * @date Create in 4:29 PM 2020/3/12
 */
@Component
public class RedisUtils {
    /**
     * Vltava:AttachTypeEnum:taskId
     */
    public static final String COMMON_KEY = "Vltava:%s:%s";
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    RedissonClient redis;

    public Boolean update(String key, VersionVO versionVO) {
        RBucket<VersionVO> rBucket = redis.getBucket(key);

        if (rBucket.get() != null && rBucket.get().getVersion().equals(versionVO.getVersion())) {
            logger.info(String.format("redis key【 %s 】: %s version %s 已为最新值，不更新", key, TaskStatusEnum.getDescByKey(versionVO.getTaskStatus()), versionVO.getVersion()));
            return true;
        } else {
            Integer count = 0;
            while (count < 3) {
                if (rBucket.get() == null && rBucket.trySet(versionVO, 1, TimeUnit.DAYS)) {
                    logger.info(String.format("redis key【 %s 】: %s version %s 设置成功", key, TaskStatusEnum.getDescByKey(versionVO.getTaskStatus()), versionVO.getVersion()));
                    return true;
                } else if (rBucket.get() != null && rBucket.compareAndSet(rBucket.get(), versionVO)){
                    logger.info(String.format("redis key【 %s 】: %s version %s 更新成功", key, TaskStatusEnum.getDescByKey(versionVO.getTaskStatus()), versionVO.getVersion()));
                    return true;
                }
                else {
                    count++;
                }
            }
            logger.warn(String.format("redis key【 %s 】: %s version %s 更新失败", key, TaskStatusEnum.getDescByKey(versionVO.getTaskStatus()), versionVO.getVersion()));
            return false;
        }
    }

    public void updateByBatch(Map<String, VersionVO> kvMap) {
        logger.info("尝试批量更新redis状态及存活时间");
        RBatch batch = redis.createBatch(BatchOptions.defaults().skipResult());
        for (String key : kvMap.keySet()) {
            batch.getBucket(key).setAsync(kvMap.get(key), 1, TimeUnit.DAYS);
        }
        Future<BatchResult<?>> future = batch.executeAsync();
    }
}

