package com.arthur.model.per;

import com.arthur.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @authur arthur
 * @desc
 */
@Data
@ApiModel(description = "Person")
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "person")
public class Person extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "姓名")
    @TableField(value ="user_name")
    private String userName;

    @ApiModelProperty(value = "家庭住址")
    @TableField(value = "user_address")
    private String userAddress;

    @ApiModelProperty(value = "出生年月")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "user_birthday")
    private Date userBirthday;

    @ApiModelProperty(value = "籍贯")
    @TableField(value = "native_place")
    private String nativePlace;

    @ApiModelProperty(value = "身份证号码")
    @TableField(value = "id_number")
    private String idNumber;


}
