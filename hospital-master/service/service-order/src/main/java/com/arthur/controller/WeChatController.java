package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.enums.PaymentTypeEnum;
import com.arthur.service.PaymentInfoService;
import com.arthur.service.WeChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "微信相关接口")
@RestController
@RequestMapping(value = "/api/order/weChat")
public class WeChatController {

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @ApiOperation(value = "生成微信二维码")
    @GetMapping(value = "/createNative/{orderId}")
    public Result createNative(@PathVariable(value = "orderId") Long orderId){

        Map map=weChatService.createNative(orderId);
        return Result.ok(map);
    }

    @ApiOperation(value = "查看支付状态")
    @GetMapping(value = "/queryPayStatus/{orderId}")
    public Result queryPayStatus(@PathVariable(value = "orderId")Long orderId){
        Map<String, String> map=weChatService.queryPayStatus(orderId, PaymentTypeEnum.WEIXIN.getStatus());
        System.out.println("支付状态"+map);
        if(map==null){
            return Result.fail().message("支付错误");
        }
        if("SUCCESS".equals(map.get("trade_state"))){
            System.out.println("成功");
            String out_trade_no = map.get("out_trade_no");
            paymentInfoService.paySuccess(out_trade_no,PaymentTypeEnum.WEIXIN.getStatus(),map);
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");

    }


}
