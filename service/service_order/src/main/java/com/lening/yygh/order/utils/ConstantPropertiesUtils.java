package com.lening.yygh.order.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 牛胜浩
 * @date 2021/6/16 20:59
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;


    public static String APPID;
    public static String PARTNER;
    public static String PARTNERKEY;


    @Override
    public void afterPropertiesSet() throws Exception {
        APPID = appid;
        PARTNER = partner;
        PARTNERKEY = partnerkey;
    }
}
