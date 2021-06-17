package com.lening.yygh.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.lening.yygh.enums.PaymentTypeEnum;
import com.lening.yygh.model.order.OrderInfo;
import com.lening.yygh.order.service.OrderService;
import com.lening.yygh.order.service.PaymentService;
import com.lening.yygh.order.service.WeixinService;
import com.lening.yygh.order.utils.ConstantPropertiesUtils;
import com.lening.yygh.order.utils.HttpClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 牛胜浩
 * @date 2021/6/16 21:04
 */
@Service
public class WeixinServiceImpl implements WeixinService{

    @Resource
    private OrderService orderService;

    @Resource
    private PaymentService paymentService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Map createNative(Long orderId) {
        try {
            //从redis中获取数据
            Map payMap = (Map) redisTemplate.opsForValue().get(orderId.toString());
            if (payMap!=null){
                return payMap;
            }
            //1.根据orderId获取订单信息
            OrderInfo order = orderService.getById(orderId);
            //2.向支付记录表添加信息
            paymentService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());
            //3.设置参数，调用微信接口生成二维码
            //吧参数转成xml格式  使用商户key进行加密
            Map paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            String body = order.getReserveDate() + "就诊"+ order.getDepname();
            paramMap.put("body", body);
            paramMap.put("out_trade_no", order.getOutTradeNo());
            //paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
            paramMap.put("total_fee", "1");
            paramMap.put("spbill_create_ip", "127.0.0.1");
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            paramMap.put("trade_type", "NATIVE");
            //4、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            //5.微信返回相关数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6.封装结果集
            Map map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("totalFee", order.getAmount());
            map.put("resultCode", resultMap.get("result_code"));
            map.put("codeUrl", resultMap.get("code_url"));

            if (resultMap.get("result_code")!=null){
                redisTemplate.opsForValue().set(orderId.toString(), map,120, TimeUnit.MINUTES);
            }
            return map;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, String> queryPayStatus(Long orderId) {
        try {
            //1 根据orderId获取订单信息
            OrderInfo orderInfo = orderService.getById(orderId);

            //2 封装提交参数
            Map paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            //3 设置请求内容
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();

            //4 得到微信接口返回数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println("支付状态resultMap:"+resultMap);
            //5 把接口数据返回
            return resultMap;
        }catch(Exception e) {
            return null;
        }
    }


}
