package com.arthur.easyexcel.mode;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @authur arthur
 * @desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @ExcelProperty(value = "用户id")
    private Integer uid;

    @ExcelProperty(value = "用户姓名")
    private String uname;
}
