package com.dmall.vltava.utils;

import com.dmall.vltava.domain.base.CommonException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Rob
 */
public class CacheUtil {
    public static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);
    private static Cache<String, Object> cache;
    private static final Map<String, ReentrantLock> LOCK_MAP = new HashMap<>();

    static {
        cache = CacheBuilder.newBuilder().maximumSize(10000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .initialCapacity(20)
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Object> removalNotification) {
                        logger.info("缓存被移除{}:{}", removalNotification.getKey());
                    }
                }).build();
    }


    public static void set(String key, Object value) {
        if (key != null && !"".equals(key) && !"".equals(value)) {
            logger.info("key 【 "+key+" 】缓存已设置");
            cache.put(key, value);
            if (!LOCK_MAP.containsKey(key)) {
                LOCK_MAP.put(key, new ReentrantLock());
            }
        }
    }

    public static Object get(String key) {
        return (key != null && !"".equals(key)) ? cache.getIfPresent(key) : null;
    }

    public static Object get(String key, Callable callable) {
        Object getObj = get(key);
        if (getObj == null) {
            if (!LOCK_MAP.containsKey(key)){
                LOCK_MAP.put(key, new ReentrantLock());
            }
            ReentrantLock lock = LOCK_MAP.get(key);
            if (lock.tryLock()) {
                try {
                    logger.warn("KEY【 " + key + " 】缓存无效，加锁重新获取缓存ing...");
                    set(key, callable.call());
                } catch (Exception e) {
//                    e.printStackTrace();
                    throw new CommonException(e.getMessage(), e.getStackTrace());
                } finally {
                    lock.unlock();
                    logger.warn("KEY【 " + key + " 】已解锁");
                }
                return get(key,callable);
            } else {
                try {
                    logger.warn("KEY【 " + key + " 】缓存无效，已有线程重入中，等待500ms后重试...");
                    Thread.sleep(500);
                    return get(key,callable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new CommonException(e.getMessage(), e.getStackTrace());
                }
            }
        } else {
            return getObj;
        }
    }

    public static void remove(String key) {
        if (key != null && !"".equals(key)) {
            cache.invalidate(key);
        }
    }
}
