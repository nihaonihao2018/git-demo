package com.arthur.service;

import com.arthur.model.per.Person;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @authur arthur
 * @desc
 */
public interface PersonService extends IService<Person> {
    IPage<Person> selectAllpage(Page<Person> personPage, Person person);
}
