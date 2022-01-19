package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "文件管理")
@RestController
@Slf4j
@RequestMapping(value = "/api/oss/file")
public class FileApiController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "/upload")
    public Result fileUpload(MultipartFile file){
        String url=fileService.upload(file);
        return Result.ok(url);
    }
}
