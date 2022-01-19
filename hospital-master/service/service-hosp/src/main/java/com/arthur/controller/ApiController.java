package com.arthur.controller;

import com.alibaba.excel.util.StringUtils;
import com.arthur.common.exception.YyghException;
import com.arthur.common.helper.HttpRequestHelper;
import com.arthur.common.result.Result;
import com.arthur.common.result.ResultCodeEnum;
import com.arthur.common.utils.MD5;
import com.arthur.model.hosp.Department;
import com.arthur.model.hosp.Hospital;
import com.arthur.model.hosp.Schedule;
import com.arthur.service.DepartmentService;
import com.arthur.service.HospitalService;
import com.arthur.service.HospitalSetService;
import com.arthur.service.ScheduleService;
import com.arthur.vo.hosp.DepartmentQueryVo;
import com.arthur.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "医院管理api接口")
@RestController
@RequestMapping(value = "/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("删除医院排班信息")
    @PostMapping(value = "/schedule/remove")
    public Result deleteSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");
        String sign = (String) map.get("sign");
        String signs = hospitalSetService.getSign(hoscode);
        String s = MD5.encrypt(signs);
        if(!s.equals(sign)){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String hosScheduleId = (String) map.get("hosScheduleId");
        scheduleService.deleteSchedule(hoscode,hosScheduleId);
        return Result.ok();


    }

    @ApiOperation(value = "分页查询医院排班信息")
    @PostMapping(value = "/schedule/list")
    public Result findSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");
        String sign = (String) map.get("sign");
        String signs = hospitalSetService.getSign(hoscode);
        String s = MD5.encrypt(signs);
        if(!s.equals(sign)){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        int page=StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String)map.get("page"));
        int limit=StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String)map.get("limit"));
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        Page<Schedule> schedules=scheduleService.findSchedule(page,limit,scheduleQueryVo);
        return Result.ok(schedules);

    }

    @ApiOperation(value = "上传医院排班信息")
    @PostMapping(value = "/saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");
        String sign = (String) map.get("sign");
        String signs = hospitalSetService.getSign(hoscode);
        String s = MD5.encrypt(signs);
        if(!s.equals(sign)){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(map);
        return Result.ok();

    }

    @ApiOperation(value = "删除医院部门（科室）信息")
    @PostMapping(value = "/department/remove")
    public Result deletedeparment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");
        departmentService.deleteDepartment(hoscode,depcode);
        return Result.ok();
    }

    @ApiOperation(value = "分页查询医院部门（科室）")
    @PostMapping(value = "/department/list")
    private Result findDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");
        String sign = (String) map.get("sign");
        String signs = hospitalSetService.getSign(hoscode);
        String s = MD5.encrypt(signs);
        if(!s.equals(sign)){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        int page=StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String)map.get("page"));
        int limit=StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String)map.get("limit"));
        //int page = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String)map.get("page"));
        //int limit = StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String)map.get("limit"));
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        Page<Department> departments=departmentService.findDepartment(page,limit,departmentQueryVo);

        return Result.ok(departments);
    }

    @ApiOperation(value = "上传医院部门信息")
    @PostMapping(value = "/saveDepartment")
    private Result saveDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");
        String sign = (String) map.get("sign");
        //String depcode = (String) map.get("depcode");
        String signs = hospitalSetService.getSign(hoscode);
        String s = MD5.encrypt(signs);
        if(!s.equals(sign)){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.save(map);
        return Result.ok();

    }

    @ApiOperation(value = "查询医院信息")
    @RequestMapping(value = "/hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");
        String sign = (String) map.get("sign");
        String signs = hospitalSetService.getSign(hoscode);
        String s = MD5.encrypt(signs);
        if(!s.equals(sign)){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital=hospitalService.getHospitalByHoscode(hoscode);
        return Result.ok(hospital);


    }

    @ApiOperation(value = "保存hospital")
    @RequestMapping(value = "/saveHospital")
    public Result saveHospital(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String sign = (String) map.get("sign");
        String hoscode = (String) map.get("hoscode");
        String signs=hospitalSetService.getSign(hoscode);
        String s = MD5.encrypt(signs);
        if(!sign.equals(s)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        String logoData = (String) map.get("logoData");
        if(!StringUtils.isEmpty(logoData)){
            String all = logoData.replaceAll(" ", "+");
            map.put("logoData",all);
        }

        hospitalService.save(map);
        return Result.ok();
    }

}
