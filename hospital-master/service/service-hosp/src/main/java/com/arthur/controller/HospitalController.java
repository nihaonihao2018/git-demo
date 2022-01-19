package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.model.hosp.Hospital;
import com.arthur.service.HospitalService;
import com.arthur.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
@RestController
@Slf4j
@Api(tags = "医院管理")
@RequestMapping(value = "/admin/hosp/hospital")
//@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "分页查询医院信息")
    @GetMapping(value = "/list/findAll/{page}/{limit}")
    public Result findHospital(@PathVariable("page") Integer page,
                               @PathVariable("limit") Integer limit,
                                HospitalQueryVo hospitalQueryVo
                               ){
        Page<Hospital> hospitals=hospitalService.findHospitals(page,limit,hospitalQueryVo);
        //System.out.println(hospitals.getContent());
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "更新医院状态")
    @GetMapping(value = "/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable("id")String id,@PathVariable("status")Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    @ApiOperation(value = "查看医院详情")
    @GetMapping(value = "/fetchHosp/{id}")
    public Result fetchHospDetails(@PathVariable("id")String id){
        Map<String,Object>map=hospitalService.fetchHospDetails(id);
        return Result.ok(map);
    }

}
