package com.lening.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lening.yygh.model.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 牛胜浩
 * @date 2021/6/11 11:07
 */
@Mapper
public interface Ordermapper extends BaseMapper<OrderInfo> {
}
