package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.enums.OrderStatusEnum;
import com.arthur.model.order.OrderInfo;
import com.arthur.service.OrderService;
import com.arthur.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "订单管理端接口")
@RestController
@RequestMapping(value = "/admin/order/orderInfo")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "订单分页查询")
    @GetMapping(value = "/{page}/{limit}")
    public Result selectOrderInfoList(@PathVariable(value = "page") Long page,
                                      @PathVariable(value = "limit") Long limit,
                                      @ApiParam(value = "orderQueryVo",name = "查询对象",required = false) OrderQueryVo orderQueryVo){
        Page<OrderInfo> infoPage=new Page<>(page,limit);
        IPage<OrderInfo> iPage=orderService.selectOrderInfoPage(infoPage,orderQueryVo);
        return Result.ok(iPage);

    }

    @ApiOperation(value = "查询订单状态列表")
    @GetMapping(value = "/getStatusList")
    public Result selectStatusList(){
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @ApiOperation(value = "查看订单详情")
    @GetMapping(value = "/show/{orderId}")
    public Result showDetail(@PathVariable(value = "orderId") Long orderId){
        Map<String,Object> mapDetail=orderService.selectOrderInfoDetail(orderId);
        return Result.ok(mapDetail);
    }
}
