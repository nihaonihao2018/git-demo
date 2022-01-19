package com.arthur.service;

import com.arthur.model.user.UserInfo;
import com.arthur.vo.user.LoginVo;
import com.arthur.vo.user.UserAuthVo;
import com.arthur.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * @authur arthur
 * @desc
 */
public interface UserInfoService extends IService<UserInfo> {
    HashMap<String, Object> login(LoginVo loginVo);

    UserInfo selectUserInfoByOpenId(Object openid);


    void userAuth(long userId, UserAuthVo userAuthVo);

    IPage<UserInfo> selectPage(Page<UserInfo> userInfoPage, UserInfoQueryVo userInfoQueryVo);

    void lockUserInfo(Long userId, Integer status);

    HashMap<String, Object> show(Long userId);

    void approval(Long userId, Integer authStatus);
}
