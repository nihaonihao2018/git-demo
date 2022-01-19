package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.model.cmn.Dict;
import com.arthur.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping(value = "/admin/cmn/dict")
//@CrossOrigin
public class DictController {

    @Resource
    private DictService dictService;

    @ApiOperation(value = "按id查数据字典")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list=dictService.findChildData(id);
        return Result.ok(list);
    }

    @ApiOperation(value = "导出数据字典")
    @GetMapping("/exportDict")
    public void exportDict(HttpServletResponse response){
        dictService.exportDict(response);
    }

    @ApiOperation(value = "导入数据字典")
    @PostMapping("/importDict")
    public Result importDict(MultipartFile file){
        dictService.importDict(file);
        return Result.ok();
    }

    @ApiOperation(value = "根据value和dictCode查找数据字典")
    @GetMapping("/findName/{dictCode}/{value}")
    public String findName(@PathVariable("dictCode") String dictCode,
                           @PathVariable("value") String value){

        String dictName=dictService.findName(dictCode,value);
        return dictName;
    }

    @ApiOperation(value = "根据value和dictCode查找数据字典")
    @GetMapping("/findName/{value}")
    public String findName(@PathVariable("value") String value){
        String dictName=dictService.findName("",value);
        return dictName;
    }

    @ApiOperation(value = "根据dictCode查询下面的数据字典")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable(value = "dictCode") String dictCode){
        List<Dict> dict=dictService.findByDictCode(dictCode);
        return Result.ok(dict);

    }

}
