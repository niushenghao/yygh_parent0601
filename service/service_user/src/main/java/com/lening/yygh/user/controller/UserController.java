package com.lening.yygh.user.controller;

import com.lening.yygh.common.result.Result;
import com.lening.yygh.model.user.UserInfo;
import com.lening.yygh.user.service.UserInfoService;
import com.lening.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/9 19:45
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserInfoService userInfoService;

    //用户列表带分页
    @GetMapping("{page}/{limit}")
    public Result findAllPage(@PathVariable Long page, @PathVariable Long limit, UserInfoQueryVo userInfoQueryVo){
        Page<UserInfo> pageParam = new Page<>(page,limit);
        IPage<UserInfo> pageModel = userInfoService.selectPage(pageParam,userInfoQueryVo);
        return Result.ok(pageModel);
    }

    //用户锁定
    @GetMapping("lock/{userId}/{status}")
    public Result lock(@PathVariable Long userId,@PathVariable Integer status){
        userInfoService.lock(userId,status);
        return Result.ok();
    }

    //查看详情
    @GetMapping("show/{userId}")
    public Result show(@PathVariable Long userId) {
        Map<String,Object> map = userInfoService.show(userId);
        return Result.ok(map);
    }

    //认证审批
    @GetMapping("approval/{userId}/{authStatus}")
    public Result approval(@PathVariable Long userId,@PathVariable Integer authStatus) {
        userInfoService.approval(userId,authStatus);
        return Result.ok();
    }

}
