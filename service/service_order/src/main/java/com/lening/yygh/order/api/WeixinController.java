package com.lening.yygh.order.api;

import com.lening.yygh.common.result.Result;
import com.lening.yygh.enums.PaymentTypeEnum;
import com.lening.yygh.order.service.PaymentService;
import com.lening.yygh.order.service.WeixinService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/16 21:04
 */
@RestController
@RequestMapping("/api/order/weixin")
public class WeixinController {

    @Resource
    private WeixinService weiXinService;

    @Resource
    private PaymentService paymentService;

    //生成微信支付二维码
    @GetMapping("/createNative/{orderId}")
    public Result createNative(@PathVariable("orderId") Long orderId) {
        Map map = weiXinService.createNative(orderId);
        return Result.ok(map);
    }

    //查询支付状态
    @PostMapping("queryPayStatus/{orderId}")
    public Result queryPayStatus(@PathVariable Long orderId) {
        //调用微信接口实现支付状态查询
        Map<String,String> resultMap = weiXinService.queryPayStatus(orderId);
        //判断
        if(resultMap == null) {
            return Result.fail().message("支付出错");
        }
        if("SUCCESS".equals(resultMap.get("trade_state"))) { //支付成功
            //更新订单状态
            String out_trade_no = resultMap.get("out_trade_no");//订单编码
            paymentService.paySuccess(out_trade_no,resultMap);
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");
    }
}
