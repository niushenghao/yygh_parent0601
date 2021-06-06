package com.lening.yygh.msm.service;

/**
 * @author 牛胜浩
 * @date 2021/6/5 11:54
 */
public interface MsmService {

    //发送手机验证码
    boolean send(String phone, String code);
}
