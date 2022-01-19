package com.arthur.common.utils;

import com.arthur.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @authur arthur
 * @desc
 */
public class AuthContextHolder {

    public static long getUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }

    public static String getUserName(HttpServletRequest request){
        String token = request.getHeader("token");
        String userName = JwtHelper.getUserName(token);
        return userName;
    }
}
