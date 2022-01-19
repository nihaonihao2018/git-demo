package com.arthur.mapper;

import com.arthur.model.user.Patient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @authur arthur
 * @desc
 */
@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
}
