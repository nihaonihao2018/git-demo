package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.service.DepartmentService;
import com.arthur.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @authur arthur
 * @desc
 */
@RestController
@Api(tags = "科室管理")
@RequestMapping(value = "/admin/hosp/department")
//@CrossOrigin
public class DeparmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "科室列表")
    @GetMapping(value = "/list/{hosCode}")
    public Result getDeptList(@PathVariable("hosCode")String hosCode){
        List<DepartmentVo> list=departmentService.getDepTree(hosCode);
        return Result.ok(list);

    }

}
