package com.testhuamou.vltava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Rob
 * @date Create in 5:27 PM 2019/11/20
 */
public class CacheHandler {
    private static final Logger logger = LoggerFactory.getLogger(CacheHandler.class);
    private static com.google.common.cache.Cache<String, Object> cache;

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
            cache.put(key, value);
        }
    }

    public static Object get(String key) {
        return (key != null && !"".equals(key)) ? cache.getIfPresent(key) : null;
    }

    public static void remove(String key) {
        if (key != null && !"".equals(key)) {
            cache.invalidate(key);
        }
    }

}
