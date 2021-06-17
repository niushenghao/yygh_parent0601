package com.lening.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lening.yygh.model.hosp.HospitalSet;
import com.lening.yygh.vo.order.SignInfoVo;

/**
 * 创作时间：2021/4/25 16:38
 * 作者：牛胜浩
 * mybatis-plus里面给我们提供了自己的servivce，只要我们的service继承他的service接口，然后把泛型给他，
 * 我们的service里面自动就拥有他的service接口里面的所有这样我们就可以直接在controller中调用
 */
public interface HospitalSetService extends IService<HospitalSet> {

    //获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);

    //2 根据传递过来医院编码，查询数据库，查询签名
    String getSignKey(String hoscode);
}
