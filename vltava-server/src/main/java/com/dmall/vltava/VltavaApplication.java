package com.testhuamou.vltava;

import com.testhuamou.admiral.client.springboot.AdmiralSpringbootConfigurationInitializer;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
@MapperScan("com.testhuamou.vltava.mapper")
@EnableDubbo(scanBasePackages = {"com.testhuamou.vltava.service.remote"})
@ImportResource(value = {"classpath:config/testhuamou-dafka.xml"})
@Import(AdmiralSpringbootConfigurationInitializer.class)
public class VltavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(VltavaApplication.class, args);
    }

}
