package com.lening.yygh.order.service;

import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/16 21:04
 */
public interface WeixinService {

    //生成微信支付二维码
    Map createNative(Long orderId);

    //调用微信接口实现支付状态查询
    Map<String, String> queryPayStatus(Long orderId);
}
