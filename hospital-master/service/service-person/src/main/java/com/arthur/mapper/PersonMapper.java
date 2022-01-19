package com.arthur.mapper;

import com.arthur.model.per.Person;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @authur arthur
 * @desc
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {
}
