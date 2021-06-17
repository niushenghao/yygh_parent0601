package com.lening.yygh.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 牛胜浩
 * @date 2021/6/11 11:02
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.lening"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lening"})
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}
