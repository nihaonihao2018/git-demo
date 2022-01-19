package com.arthur.vo.per;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @authur arthur
 * @desc
 */
@Data
@ApiModel(description = "身份证信息")
@NoArgsConstructor
@AllArgsConstructor
public class PersonVo {


    @ApiModelProperty(value = "姓名")
    @TableField(value ="user_name")
    private String userName;

    @ApiModelProperty(value = "家庭住址")
    @TableField(value = "user_address")
    private String userAddress;

    @ApiModelProperty(value = "出生年月")
    @TableField(value = "user_birthday")
    private Data userBirthday;

    @ApiModelProperty(value = "籍贯")
    @TableField(value = "native_place")
    private String nativePlace;

    @ApiModelProperty(value = "身份证号码")
    @TableField(value = "id_number")
    private String idNumber;
}
