package com.lening.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lening.yygh.model.user.UserInfo;
import com.lening.yygh.vo.user.LoginVo;

import java.util.List;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/3 17:12
 */
public interface UserInfoService extends IService<UserInfo> {

    //用户手机号登录
    Map<String, Object> loginUser(LoginVo loginVo);

}
