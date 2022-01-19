package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.model.hosp.Hospital;
import com.arthur.model.hosp.Schedule;
import com.arthur.service.DepartmentService;
import com.arthur.service.HospitalService;
import com.arthur.service.HospitalSetService;
import com.arthur.service.ScheduleService;
import com.arthur.vo.hosp.DepartmentVo;
import com.arthur.vo.hosp.HospitalQueryVo;
import com.arthur.vo.hosp.ScheduleOrderVo;
import com.arthur.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "医院前台接口")
@RestController
@RequestMapping(value = "/api/hosp/hospital")
public class ApiHospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "分页查询医院列表信息")
    @GetMapping(value = "/findHospitalsByPage/{page}/{limit}")
    public Result findHospitalPage(@PathVariable(value = "page") Integer page,
                                   @PathVariable(value = "limit") Integer limit,
                                   HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitals = hospitalService.findHospitals(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据hosname查询医院信息")
    @GetMapping(value = "/findHospitalByHosname/{hosname}")
    public Result findHospitalByHosname(@PathVariable(value = "hosname")String hosname){
        List<Hospital> hospitals=hospitalService.findHospitalByHoscode(hosname);
        return Result.ok(hospitals);

    }
    @ApiOperation(value = "根据hoscode查询科室信息")
    @GetMapping(value = "/department/{hoscode}")
    public Result index(@PathVariable(value = "hoscode")String hoscode){
        List<DepartmentVo> list = departmentService.getDepTree(hoscode);
        return Result.ok(list);

    }

    @ApiOperation(value = "根据hoscode查询医院详细信息")
    @GetMapping(value = "/hospitalDetail/{hoscode}")
    public Result item(@PathVariable(value = "hoscode")String hoscode){
        Map<String,Object> map=hospitalService.fetchHospDetail(hoscode);
        return Result.ok(map);
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping(value = "/auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingScheduleRule(@PathVariable(value = "page") Integer page,
                                         @PathVariable(value = "limit") Integer limit,
                                         @PathVariable(value = "hoscode") String hoscode,
                                         @PathVariable(value = "depcode") String depcode){
        return Result.ok(scheduleService.getBookingScheduleRule(page,limit,hoscode,depcode));
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping(value = "/auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(@PathVariable(value = "hoscode")String hoscode,
                                   @PathVariable(value = "depcode")String depcode,
                                   @PathVariable(value = "workDate")String workDate){

        return Result.ok(scheduleService.fetchScheduleDetails(hoscode,depcode,workDate));
    }

    @ApiOperation(value = "根据scheduleId获取排班信息")
    @GetMapping(value = "/getSchedule/{scheduleId}")
    public Result getSchedule(@PathVariable(value = "scheduleId") String scheduleId){
        Schedule schedule=scheduleService.getScheduleById(scheduleId);
        return Result.ok(schedule);
    }

    @ApiOperation(value = "远程调用，根据id查询预约下单数据")
    @GetMapping(value = "/inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable(value = "scheduleId")String scheduleId){

        ScheduleOrderVo scheduleOrderVo=scheduleService.getScheduleOrderVo(scheduleId);
        return scheduleOrderVo;
    }

    @ApiOperation(value = "远程调用，根据hoscode获取医院签名")
    @GetMapping(value = "/inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(@PathVariable(value = "hoscode")String hoscode){
        SignInfoVo signInfoVo=hospitalSetService.getSignInfoVo(hoscode);
        return signInfoVo;
    }
}
