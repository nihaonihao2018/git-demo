package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.common.utils.MD5;
import com.arthur.model.hosp.HospitalSet;
import com.arthur.service.HospitalSetService;
import com.arthur.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @authur arthur
 * @desc
 */
@RestController
@RequestMapping(value = "/admin/hosp/hospitalSet")
@Api(tags = "医院设置管理")
//@CrossOrigin
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //1 查询医院设置表所有信息
    @ApiOperation(value = "查询医院设置表所有信息")
    @GetMapping(value = "/findAll")
    public Result finAllHospitalSet(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //2.逻辑删除医院设置
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping(value = "/{id}")
    public Result deleteHospitalSet(@PathVariable("id") Long id){
        boolean b = hospitalSetService.removeById(id);
        if(b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //3 条件查询带分页
    @ApiOperation(value = "条件查询带分页")
    @PostMapping(value = "/findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable("current") Long current,
                                  @PathVariable("limit") Long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){

        Page<HospitalSet> page = new Page<>(current,limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hoscode = hospitalSetQueryVo.getHoscode();
        String hosname = hospitalSetQueryVo.getHosname();
        if(!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode",hoscode);
        }
        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hosname);
        }
        Page<HospitalSet> pageHospital = hospitalSetService.page(page, wrapper);
        return Result.ok(pageHospital);
    }


    //4.添加医院设置
    @ApiOperation(value = "添加医院设置")
    @PostMapping(value = "/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){

        hospitalSet.setStatus(1);
        Random random = new Random();
        String encrypt = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(encrypt);
        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }

    //5.根据id获取医院设置

    @ApiOperation("根据id获取医院设置")
    @GetMapping(value = "/getHospitalSet/{id}")
    public Result getHospitalSet(@PathVariable("id") Long id){
        //int i=5/0;
        /* try {
            int i=5/0;
        }catch (YyghException e){
            throw new YyghException("失败",201);
        }*/

        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }


    //6.修改医院设置
    @ApiOperation(value = "修改医院设置")
    @PutMapping(value = "/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean b = hospitalSetService.updateById(hospitalSet);
        if(b){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }

    //7.批量删除医院设置

    @ApiOperation("批量删除医院设置")
    @DeleteMapping(value = "/removeHospitals")
    public Result removeHospitals(@RequestBody List<Long> list){
        boolean b = hospitalSetService.removeByIds(list);
        if(b){
            return  Result.ok();
        }else {
            return Result.fail();
        }
        //return Result.ok();
    }


    //8.医院设置锁定解锁

    @ApiOperation(value = "医院设置锁定解锁")
    @PutMapping(value = "/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id") Long id,
                                  @PathVariable("status") Integer status){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean b = hospitalSetService.updateById(hospitalSet);
        if(b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //9.发送signKey

    @ApiOperation(value = "发送signKey")
    @PutMapping(value = "/sengSignKey/{id}")
    public Result sengSignKey(@PathVariable("id") Long id){

        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        return Result.ok();
    }

}
