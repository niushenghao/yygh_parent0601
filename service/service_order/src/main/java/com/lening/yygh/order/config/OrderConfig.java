package com.lening.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 牛胜浩
 * @date 2021/6/11 11:08
 */
@Configuration
@MapperScan("com.lening.yygh.order.mapper")
public class OrderConfig {
}
