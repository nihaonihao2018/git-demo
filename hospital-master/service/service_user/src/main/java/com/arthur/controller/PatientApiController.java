package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.common.utils.AuthContextHolder;
import com.arthur.model.user.Patient;
import com.arthur.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "就诊人管理")
@RestController
@RequestMapping(value = "/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "根据用户id查找所有就诊人")
    @GetMapping(value = "/auth/findAll")
    public Result findAllPatient(HttpServletRequest request){
        long userId = AuthContextHolder.getUserId(request);
        List<Patient> patients=patientService.findAllPatient(userId);
        return Result.ok(patients);
    }

    @ApiOperation(value = "添加就诊人")
    @PostMapping(value = "/auth/save")
    public Result savePatient(@RequestBody Patient patient,HttpServletRequest request){

        long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    @ApiOperation(value = "根据id查找就诊人")
    @GetMapping(value = "/auth/get/{id}")
    public Result getPatient(@PathVariable(value = "id") Long id){
        Patient patient=patientService.getPatientById(id);
        return Result.ok(patient);
    }


    @ApiOperation(value = "更新就诊人")
    @PostMapping(value = "/auth/update")
    public Result updatePatient(@RequestBody Patient patient){
        patientService.updateById(patient);
        return Result.ok();
    }

    @ApiOperation(value = "删除就诊人")
    @DeleteMapping(value = "/auth/remove/{id}")
    public Result deletePatient(@PathVariable(value = "id") Long id){
        patientService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "远程调用，通过id获取就诊人信息")
    @GetMapping(value = "/inner/get/{id}")
    public Patient getPatientOrder(@PathVariable(value = "id") Long id){
        return patientService.getPatientById(id);
    }

}
