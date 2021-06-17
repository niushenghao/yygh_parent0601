package com.lening.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lening.yygh.model.user.Patient;
import com.lening.yygh.model.user.UserInfo;
import com.lening.yygh.vo.user.LoginVo;
import com.lening.yygh.vo.user.UserAuthVo;
import com.lening.yygh.vo.user.UserInfoQueryVo;

import java.util.List;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/3 17:12
 */
public interface UserInfoService extends IService<UserInfo> {

    //用户手机号登录
    Map<String, Object> loginUser(LoginVo loginVo);

    //根据openid
    UserInfo selectWxUserInfoOpenId(String openid);

    //用户认证
    void userAuth(Long userId, UserAuthVo userAuthVo);

    //用户列表带分页
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    //用户锁定
    void lock(Long userId, Integer status);

    //查看详情
    Map<String, Object> show(Long userId);

    //认证审批
    void approval(Long userId, Integer authStatus);
}
