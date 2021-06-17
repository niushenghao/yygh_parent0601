package com.lening.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lening.yygh.model.order.OrderInfo;
import com.lening.yygh.model.user.UserInfo;
import com.lening.yygh.vo.order.OrderQueryVo;

import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/11 11:05
 */
public interface OrderService extends IService<OrderInfo> {

    //生成挂号订单.
    Long saveOrder(String scheduleId, Long patientId);

    //根据订单id查询订单详情
    OrderInfo getOrder(String orderId);

    //订单列表（条件查询带分页）
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    //根据订单id查询订单详情
    Map<String,Object> show(Long orderId);
}

