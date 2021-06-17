package com.lening.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lening.yygh.model.order.OrderInfo;
import com.lening.yygh.model.order.PaymentInfo;

import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/16 21:05
 */
public interface PaymentService extends IService<PaymentInfo> {
    /**
     * 保存交易记录
     * @param order
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    void savePaymentInfo(OrderInfo order, Integer paymentType);

    //更新订单状态
    void paySuccess(String out_trade_no, Map<String, String> resultMap);

    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);

}
