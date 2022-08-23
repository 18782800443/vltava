package com.dmall.vltava.service.impl;

import com.dmall.vltava.domain.base.VersionVO;
import com.dmall.vltava.domain.coverage.CoverageVO;
import com.dmall.vltava.service.RedisService;
import com.dmall.vltava.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rob
 * @date Create in 4:10 下午 2020/5/27
 */
@Service("RedisService")
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private static final Map<String, VersionVO> FAILED_MAP = new ConcurrentHashMap<>();

    @Autowired
    RedisUtils redis;

    @Override
    public Boolean update(String key, VersionVO versionVO) {
        if (redis.update(key,versionVO )) {
            return true;
        } else {
            FAILED_MAP.put(key, versionVO);
            logger.warn(String.format("更新redis key 【 %s 】: %s version %s 失败，已加入重试列表", key, versionVO.getTaskStatus(), versionVO.getVersion()));
            return false;
        }
    }

    @Override
    public void updateBatch(Map<String, VersionVO> versionMap) {
        redis.updateByBatch(versionMap);
    }

    @Override
    public void syncFailedList() {
        synchronized (FAILED_MAP) {
            logger.info("尝试同步redis失败队列，当前更新失败的redis数量为：" + FAILED_MAP.size());
            for (String key : FAILED_MAP.keySet()) {
                if (redis.update(key, FAILED_MAP.get(key))) {
                    FAILED_MAP.remove(key);
                }
            }
        }
    }
}
