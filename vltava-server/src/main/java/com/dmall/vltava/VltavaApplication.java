package com.dmall.vltava;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
@MapperScan("com.dmall.vltava.mapper")
@EnableDubbo(scanBasePackages = {"com.dmall.vltava.service.remote"})
public class VltavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VltavaApplication.class, args);
    }

}
