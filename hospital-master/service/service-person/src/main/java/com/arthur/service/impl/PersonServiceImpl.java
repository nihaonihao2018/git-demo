package com.arthur.service.impl;

import com.arthur.mapper.PersonMapper;
import com.arthur.model.per.Person;
import com.arthur.service.PersonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "personService")
public class PersonServiceImpl extends ServiceImpl<PersonMapper,Person> implements PersonService {


    @Override
    public IPage<Person> selectAllpage(Page<Person> personPage, Person person) {

        QueryWrapper<Person> wrapper = new QueryWrapper<>();


        if(person!=null){
            String idNumber = person.getIdNumber();
            String nativePlace = person.getNativePlace();
            String userAddress = person.getUserAddress();
            Date userBirthday = person.getUserBirthday();
            String userName = person.getUserName();
            if(!StringUtils.isEmpty(idNumber)){
                wrapper.eq("id_number",idNumber);
            }
            if(!StringUtils.isEmpty(nativePlace)){
                wrapper.eq("native_place",nativePlace);
            }
            if(!StringUtils.isEmpty(userAddress)){
                wrapper.eq("user_address",userAddress);
            }
            if(!StringUtils.isEmpty(userBirthday)){
                wrapper.eq("user_birthday",userBirthday);
            }
            if(!StringUtils.isEmpty(userName)){
                wrapper.eq("user_name",userName);
            }

        }

        Page<Person> selectPage = baseMapper.selectPage(personPage, wrapper);

        return selectPage;
    }
}
