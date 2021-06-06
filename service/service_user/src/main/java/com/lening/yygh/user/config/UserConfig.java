package com.lening.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * @author 牛胜浩
 * @date 2021/6/3 17:15
 */
@Configurable
@MapperScan("com.lening.yygh.user.mapper")
public class UserConfig {
}
