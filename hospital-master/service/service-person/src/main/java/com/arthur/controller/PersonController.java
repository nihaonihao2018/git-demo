package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.model.per.Person;
import com.arthur.service.PersonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @authur arthur
 * @desc
 */

@Api(value = "身份证信息处理")
@RestController
@RequestMapping(value = "/admin/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @ApiOperation(value = "添加个人身份信息")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result addPerson(@RequestBody Person person){

        personService.save(person);

        return Result.ok();
    }

    @ApiOperation(value = "修改身份信息")
    @PostMapping(value = "/update")
    public Result updatePerson(@RequestBody Person person){

        person.setUpdateTime(null);
        personService.updateById(person);

        return Result.ok();
    }

    @ApiOperation(value = "删除身份信息")
    @DeleteMapping(value = "/remove/{id}")
    public Result removePerson(@PathVariable(value = "id") Integer id){

        personService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "分页查询身份信息")
    @PostMapping(value = "/{page}/{limit}")
    public Result selectAll(@PathVariable(value = "page") Integer page,
                            @PathVariable(value = "limit") Integer limit,
                            @RequestBody(required = false) Person person){
        Page<Person> personPage = new Page<>(page,limit);
        IPage<Person> personIPage=personService.selectAllpage(personPage,person);
        return Result.ok(personIPage);
    }


    @ApiOperation(value = "批量删除身份信息")
    @DeleteMapping(value = "/delete/persons")
    public Result deletePersons(@RequestBody List<Long> list){

        boolean b = personService.removeByIds(list);
        if(b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据id查找身份信息")
    @GetMapping(value = "/select/{id}")
    public Result selectPersionById(@PathVariable(value = "id")Integer id){

        Person person = personService.getById(id);
        return Result.ok(person);
    }


}
