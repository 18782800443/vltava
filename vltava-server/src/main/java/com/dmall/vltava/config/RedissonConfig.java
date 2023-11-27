package com.testhuamou.vltava.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rob
 * @date Create in 2:18 PM 2018/12/14
 */
@Configuration
public class RedissonConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedissonConfig.class);
    private static RedissonClient redissonClient = null;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String link = "redis://" + host + ":" + port;
        config.useSingleServer().setAddress(link).setRetryAttempts(2).setRetryInterval(500).setTimeout(3000)
                .setConnectionMinimumIdleSize(4).setConnectionPoolSize(5).setIdleConnectionTimeout(10000);
        try {
            redissonClient = Redisson.create(config);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return redissonClient;
    }


}
