package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.common.utils.RandomUtil;
import com.arthur.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @authur arthur
 * @desc
 */
@Api(tags = "短信验证码发送")
@RestController
@RequestMapping(value = "/api/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @ApiOperation(value = "发送验证码")
    @GetMapping(value = "/send/{phone}")
    public Result sendCode(@PathVariable(value = "phone")String phone){
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return Result.ok();
        }
        code= RandomUtil.getSixBitRandom();
        boolean isSend=msmService.send(phone,code);
        if(isSend){
            redisTemplate.opsForValue().set(phone,code,2, TimeUnit.MINUTES);
            return Result.ok();
        }else {
            return Result.fail().message("发送信息失败");
        }

    }
}
