package com.arthur.easyexcel.tset;

import com.alibaba.excel.EasyExcel;
import com.arthur.easyexcel.mode.User;

import java.io.File;
import java.util.ArrayList;

/**
 * @authur arthur
 * @desc
 */
public class EasyExcelTset {
    public static void main(String[] args){

        ArrayList<User> users = new ArrayList<>();
        for(int i=1;i<=10;i++){
            User user = new User();
            user.setUid(i);
            user.setUname("齐端"+i);
            users.add(user);
        }

        File file = new File("D:\\work\\ideaprojects\\guangda\\yygh-parent\\easyexcel1.xlsx");
        EasyExcel.write(file, User.class).sheet("用户信息").doWrite(users);
    }

}
