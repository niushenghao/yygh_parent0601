package com.lening.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author 牛胜浩
 * @date 2021/5/26 10:12
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.lening")
public class ServiceHosp1Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHosp1Application.class, args);
    }
}
