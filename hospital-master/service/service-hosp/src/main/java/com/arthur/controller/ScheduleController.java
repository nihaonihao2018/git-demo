package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.model.hosp.Schedule;
import com.arthur.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
@RestController
@Api(tags = "排班管理")
@RequestMapping(value = "/admin/hosp/schedule")
//@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "查询排班列表")
    @GetMapping(value = "getSchedule/{page}/{limit}/{hoscode}/{depcode}")
    public Result findScheduleDetail(@PathVariable("page")Integer page,
                                     @PathVariable("limit")Integer limit,
                                     @PathVariable("hoscode")String hoscode,
                                     @PathVariable("depcode")String depcode){

        Map<String,Object> map=scheduleService.getScheduleDetail(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    @ApiOperation(value = "查询排班详情")
    @GetMapping(value = "/fetchSchedule/{hoscode}/{depcode}/{workDate}")
    public Result fetchScheduleDetails(@PathVariable("hoscode")String hoscode,
                                       @PathVariable("depcode")String depcode,
                                       @PathVariable("workDate")String workdate){
        List<Schedule> schedules=scheduleService.fetchScheduleDetails(hoscode,depcode,workdate);
        return Result.ok(schedules);
    }


}
