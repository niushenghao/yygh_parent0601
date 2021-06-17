package com.lening.yygh.common.rabbit.constant;

/**
 * @author 牛胜浩
 * @date 2021/6/11 16:32
 */
public class MqConst {

    /**
     * 预约下单
     */
    public static final String EXCHANGE_DIRECT_ORDER
            = "exchange.direct.order";
    public static final String ROUTING_ORDER = "order";
    //队列
    public static final String QUEUE_ORDER  = "queue.order";
    /**
     * 短信
     */
    public static final String EXCHANGE_DIRECT_MSM = "exchange.direct.msm";
    public static final String ROUTING_MSM_ITEM = "msm.item";
    //队列
    public static final String QUEUE_MSM_ITEM  = "queue.msm.item";
}
