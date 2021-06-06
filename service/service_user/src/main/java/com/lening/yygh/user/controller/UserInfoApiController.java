package com.lening.yygh.user.controller;

import com.lening.yygh.common.result.Result;
import com.lening.yygh.model.user.UserInfo;
import com.lening.yygh.user.service.UserInfoService;
import com.lening.yygh.vo.user.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/3 17:11
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户登录")
public class UserInfoApiController {

    @Resource
    private UserInfoService userInfoService;

    //用户手机号登录接口
    @PostMapping("login")
    @ApiOperation(value = "用户登录")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> map = userInfoService.loginUser(loginVo);
        return Result.ok(map);
    }


}
