package com.lening.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 牛胜浩
 * @date 2021/6/3 17:08
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.lening")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.lening")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
