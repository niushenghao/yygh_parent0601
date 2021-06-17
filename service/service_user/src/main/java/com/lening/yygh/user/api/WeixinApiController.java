package com.lening.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.lening.yygh.common.helper.JwtHelper;
import com.lening.yygh.common.result.Result;
import com.lening.yygh.model.user.UserInfo;
import com.lening.yygh.user.service.UserInfoService;
import com.lening.yygh.user.utils.ConstantPropertiesUtil;
import com.lening.yygh.user.utils.HttpClientUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/7 15:39
 */
@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Resource
    private UserInfoService userInfoService;

    //1.生成微信扫描二维码
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect(){
        try {
            Map<String,Object> map = new HashedMap<>();
            map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
            map.put("scope", "snsapi_login");
            String wxOpenRedirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
            wxOpenRedirectUrl = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
            map.put("redirectUri",wxOpenRedirectUrl);
            map.put("state", System.currentTimeMillis()+"");
            return Result.ok(map);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //2.回调方法，得到扫描人的信息
    @GetMapping("/callback")
    public String callback(String code,String state){
        //获取授权临时票据
        System.out.println("微信授权服务器回调。。。。。。");
        System.out.println("state = " + state);
        System.out.println("code = " + code);

        //拿着code和微信id和密钥，请求微信固定地址，通过地址得到两个返回值
        //使用code和appid以及appscrect换取access_token
        //  %s   占位符
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        //使用httpclient请求这个地址
        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo:"+accessTokenInfo);
            //从返回字符串里获取两个值openid 和 access_token
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            //判断数据库中是否存在扫码人的信息
            UserInfo userInfo = userInfoService.selectWxUserInfoOpenId(openid);
            if(userInfo==null){
                //数据库中不存在信息
                //拿着openid和access_token 请求微信地址，得到扫码人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo"+
                        "?access_token=%s"+
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultInfo"+resultInfo);
                JSONObject resultInfoJson = JSONObject.parseObject(resultInfo);
                //解析用户信息
                //用户信息
                String nickname = resultInfoJson.getString("nickname");
                //用户头像
                String headimgurl = resultInfoJson.getString("headimgurl");

                //获取扫描人信息添加数据库
                userInfo = new UserInfo();
                userInfo.setNickName(nickname);
                userInfo.setOpenid(openid);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }


            //返回name和token字符串
            Map<String,Object> map = new HashedMap<>();
            String name = userInfo.getName();
            if (StringUtils.isEmpty(name)){
                name = userInfo.getNickName();
            }
            if (StringUtils.isEmpty(name)){
                name = userInfo.getPhone();
            }
            map.put("name", name);

            //判断userInfo是否有手机号，如果手机号为空，返回openid
            //如果手机号不为空，返回openid值是空字符串
            //前端判断：如果openid不为空，绑定手机号，如果openid为空，不需要绑定手机号
            if (StringUtils.isEmpty(userInfo.getPhone())){
                map.put("openid", openid);
            }else {
                map.put("openid", "");
            }

            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            //跳转到前端页面
            return "redirect:" + ConstantPropertiesUtil.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+ "&openid="+map.get("openid")+"&name="+URLEncoder.encode((String) map.get("name"),"utf-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
