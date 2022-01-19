package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.common.utils.AuthContextHolder;
import com.arthur.model.user.UserInfo;
import com.arthur.service.UserInfoService;
import com.arthur.vo.user.LoginVo;
import com.arthur.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "用户管理api接口")
@RestController
@RequestMapping(value = "/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "登录验证")
    @PostMapping(value = "/login")
    public Result login(@RequestBody LoginVo loginVo){
        HashMap<String, Object> map = userInfoService.login(loginVo);
        return Result.ok(map);
    }

    @ApiOperation(value = "用户认证")
    @PostMapping(value = "/auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request){
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();

    }

    @ApiOperation(value = "用户信息")
    @GetMapping(value = "/auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }

}
