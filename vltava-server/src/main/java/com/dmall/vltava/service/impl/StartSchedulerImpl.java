package com.testhuamou.vltava.service.impl;


import com.testhuamou.vltava.service.CoverageService;
import com.testhuamou.vltava.service.HealthService;
import com.testhuamou.vltava.service.MockService;
import com.testhuamou.vltava.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;


/**
 * @author Rob
 * @date Create in 2:34 PM 2018/12/14
 */
@Component
public class StartSchedulerImpl implements ApplicationRunner {
//    @Autowired
//    CoverageService coverageService;

    @Autowired
    MockService mockService;

    @Autowired
    RedisService redisService;

    @Autowired
    HealthService healthService;

    public static final Logger logger = LoggerFactory.getLogger(StartSchedulerImpl.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        syncRedis();
    }

    @Scheduled(fixedDelay = 1000 * 60 * 30)
    private void healthCheck(){
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                logger.info("start health check...");
                healthService.check();
                logger.info("health check finish.");
            }
        });
    }

//    @Scheduled(fixedDelay = 1000 * 60 * 30)
//    private void syncRedis() {
//        CompletableFuture.runAsync(new Runnable() {
//            @Override
//            public void run() {
//                logger.info("Initializing Redis ...");
//                coverageService.syncStatus();
//                logger.info("Redis initialized completely");
//            }
//        });
//    }

//    @Scheduled(fixedDelay = 1000 * 60 )
//    private void syncFailedRedis(){
//        CompletableFuture.runAsync(new Runnable() {
//            @Override
//            public void run() {
//                redisService.syncFailedList();
//            }
//        });
//    }

//    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 )
//    private void clearInvalidTask(){
//        CompletableFuture.runAsync(new Runnable() {
//            @Override
//            public void run() {
//                coverageService.clearInvalid();
//            }
//        });
//    }


}

