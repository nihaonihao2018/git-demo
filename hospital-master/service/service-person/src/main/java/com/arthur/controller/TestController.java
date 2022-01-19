package com.arthur.controller;

import com.arthur.bean.Teacher;
import com.arthur.common.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
@RestController
@RequestMapping(value = "/admin/person")
public class TestController {
    @PostMapping(value = "/get",consumes ="application/json;charset=utf-8" )
    public Result getPerson(Teacher person){
        return Result.ok(person);
    }
    @PostMapping(value = "/get1",consumes ="application/json;charset=utf-8" )
    public Result getPerson1(@RequestParam Map<String,String> map){
        return Result.ok(map);
    }
}
