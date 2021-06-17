package com.lening.yygh.user.api;

import com.lening.cmn.client.DictFeignClient;
import com.lening.yygh.common.result.Result;
import com.lening.yygh.common.utils.AuthContextHolder;
import com.lening.yygh.enums.DictEnum;
import com.lening.yygh.model.user.UserInfo;
import com.lening.yygh.user.service.UserInfoService;
import com.lening.yygh.vo.user.LoginVo;
import com.lening.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private DictFeignClient dictFeignClient;

    //用户手机号登录接口
    @PostMapping("login")
    @ApiOperation(value = "用户登录")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> map = userInfoService.loginUser(loginVo);
        return Result.ok(map);
    }

    //用户认证接口
    @PostMapping("/auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request){
        //传递两个参数，第一个是用户id，第二个认证数据vo
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    //获取用户id信息接口
    @GetMapping("/auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        String certificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), userInfo.getCertificatesType());
        userInfo.getParam().put("certificatesTypeString",certificatesTypeString);
        return Result.ok(userInfo);
    }
}
