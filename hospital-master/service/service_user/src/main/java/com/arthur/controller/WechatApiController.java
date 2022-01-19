package com.arthur.controller;

import com.alibaba.fastjson.JSONObject;
import com.arthur.common.exception.YyghException;
import com.arthur.common.helper.JwtHelper;
import com.arthur.common.result.Result;
import com.arthur.common.result.ResultCodeEnum;
import com.arthur.config.ConstantWxPropertiesUtil;
import com.arthur.model.user.UserInfo;
import com.arthur.service.UserInfoService;
import com.arthur.util.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "微信登录功能实现")
@Controller
@RequestMapping(value = "/api/ucenter/wx")
@Slf4j
public class WechatApiController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "微信二维码")
    @ResponseBody
    @GetMapping(value = "/getLoginParam")
    public Result genQrConnect() throws UnsupportedEncodingException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid", ConstantWxPropertiesUtil.WX_OPEN_APP_ID);
        map.put("scope","snsapi_login");
        map.put("state",System.currentTimeMillis()+"");
        String wxOpenRedirectUrl = ConstantWxPropertiesUtil.WX_OPEN_REDIRECT_URL;
        wxOpenRedirectUrl= URLEncoder.encode(wxOpenRedirectUrl,"utf-8");
        map.put("redirectUri",wxOpenRedirectUrl);
        return Result.ok(map);
    }

    @ApiOperation(value = "微信回调")
    @GetMapping(value = "/callback")
    public String callback(String code, String state) throws UnsupportedEncodingException {
        //获取授权临时票据
        System.out.println("微信授权服务器回调。。。。。。");
        System.out.println("state = " + state);
        System.out.println("code = " + code);
        if(StringUtils.isEmpty(code)||StringUtils.isEmpty(state)){
            log.error("非法回调请求");
            throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        StringBuffer stringBuffer = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");
        String accessTokenUrl  = String.format(String.valueOf(stringBuffer), ConstantWxPropertiesUtil.WX_OPEN_APP_ID, ConstantWxPropertiesUtil.WX_OPEN_APP_SECRET, code);

        String result=null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
            //System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        JSONObject jsonObject = JSONObject.parseObject(result);

        if(jsonObject.get("errorcode")!=null){
            log.error("获取access_token失败：" + jsonObject.getString("errcode") + jsonObject.getString("errmsg"));
            throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        Object access_token = jsonObject.get("access_token");
        Object openid = jsonObject.get("openid");



        String baseUserInfoUrl="https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        String realurl = String.format(baseUserInfoUrl, access_token, openid);
        String resultUserInfo ;
        try {
            resultUserInfo = HttpClientUtils.get(realurl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }


        //System.out.println(resultUserInfo);
        JSONObject object = JSONObject.parseObject(resultUserInfo);
        if(object.get("errcode") != null){
            log.error("获取用户信息失败：" + object.getString("errcode") + object.getString("errmsg"));
            throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        Object nickname = object.get("nickname");
        Object headimgurl = object.get("headimgurl");

        UserInfo userInfo=userInfoService.selectUserInfoByOpenId(openid);
        if(userInfo==null){
            userInfo=new UserInfo();
            userInfo.setNickName((String) nickname);
            userInfo.setStatus(1);
            userInfo.setOpenid((String) openid);
            userInfoService.save(userInfo);
        }

        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        if(StringUtils.isEmpty(userInfo.getPhone())) {
            map.put("openid", userInfo.getOpenid());
        } else {
            map.put("openid", "");
        }
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        //System.out.println(map.get("openid"));

        return "redirect:" + ConstantWxPropertiesUtil.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+"&openid="+map.get("openid")+"&name="+URLEncoder.encode((String) map.get("name"),"utf-8");
    }
}
