package com.lening.yygh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 牛胜浩
 * @date 2021/5/25 10:10
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.lening.yygh.cmn.mapper"})
@EnableDiscoveryClient
public class ServiceCmnApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class,args);
    }
}
