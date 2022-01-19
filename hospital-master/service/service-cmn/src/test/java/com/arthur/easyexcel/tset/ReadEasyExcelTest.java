package com.arthur.easyexcel.tset;

import com.alibaba.excel.EasyExcel;
import com.arthur.easyexcel.listener.EasyExcelListener;
import com.arthur.easyexcel.mode.UserRead;

import java.io.File;

/**
 * @authur arthur
 * @desc
 */
public class ReadEasyExcelTest {
    public static void main(String[] args){
        File file = new File("D:\\work\\ideaprojects\\guangda\\yygh-parent\\easyexcel1.xlsx");

        EasyExcel.read(file,UserRead.class,new EasyExcelListener()).sheet("用户信息").doRead();
    }
}
