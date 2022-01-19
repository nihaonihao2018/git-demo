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
public class UserRead {
    @ExcelProperty(value = "用户id",index = 0)
    private Integer uid;

    @ExcelProperty(value = "用户姓名",index = 1)
    private String uname;
}
