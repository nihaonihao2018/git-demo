package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.model.user.UserInfo;
import com.arthur.service.UserInfoService;
import com.arthur.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "用户信息查询系列")
@RestController
@RequestMapping(value = "/admin/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "分页查询用户信息")
    @GetMapping(value = "/{page}/{limit}")
    public Result getUserInfoPage(@PathVariable(value = "page") Long page,
                                  @PathVariable(value = "limit") Long limit,
                                  UserInfoQueryVo userInfoQueryVo){

        System.out.println(userInfoQueryVo);
        Page<UserInfo> userInfoPage = new Page<>(page,limit);
        IPage<UserInfo> infoIPage=userInfoService.selectPage(userInfoPage,userInfoQueryVo);
        return Result.ok(infoIPage);
    }

    @ApiOperation(value = "用户锁定")
    @GetMapping(value = "/lock/{userId}/{status}")
    public Result lock(@PathVariable(value = "userId") Long userId,
                       @PathVariable(value = "status") Integer status){
        userInfoService.lockUserInfo(userId,status);
        return Result.ok();
    }

    @ApiOperation(value = "用户详情")
    @GetMapping(value = "/show/{userId}")
    public Result show(@PathVariable(value = "userId") Long userId){
        HashMap<String,Object> map=userInfoService.show(userId);
        return Result.ok(map);
    }

    @ApiOperation(value = "认证审批")
    @GetMapping(value = "/approval/{userId}/{authStatus}")
    public Result approval(@PathVariable(value = "userId") Long userId,@PathVariable(value = "authStatus") Integer authStatus){
        userInfoService.approval(userId,authStatus);
        return Result.ok();
    }
}
