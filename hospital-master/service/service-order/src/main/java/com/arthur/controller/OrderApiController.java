package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.common.utils.AuthContextHolder;
import com.arthur.enums.OrderStatusEnum;
import com.arthur.model.order.OrderInfo;
import com.arthur.service.OrderService;
import com.arthur.vo.order.OrderCountQueryVo;
import com.arthur.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
@RestController
@RequestMapping(value = "/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "创建订单")
    @PostMapping(value = "/auth/submitOrder/{scheduleId}/{patientId}/{workDate}")
    public Result saveOrder(@PathVariable(value = "scheduleId") String scheduleId,
                            @PathVariable(value = "patientId") Long patientId,
                            @PathVariable(value = "workDate")String workDate){

        DateTimeFormatter pattern = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date date = pattern.parseDateTime(workDate).toDate();
        List<OrderInfo> orderInfos=orderService.selectOrderByWorkDate(scheduleId,patientId,date);
        System.out.println(orderInfos);
        if(orderInfos.size()>0){
            return Result.fail().message("该就诊人当日以及预约过");
        }
        Long orderId=orderService.saveOrder(scheduleId,patientId);
        return Result.ok(orderId);
    }

    @ApiOperation(value = "根据orderId查询订单详情")
    @GetMapping(value = "/auth/getOrders/{orderId}")
    public Result getOrderInfo(@PathVariable(value = "orderId")Long orderId){

        OrderInfo orderInfo=orderService.getOrderInfo(orderId);
        return Result.ok(orderInfo);
    }

    @ApiOperation(value = "分页查询订单列表")
    @GetMapping(value = "/auth/{page}/{limit}")
    public Result selectOrderInfoPage(@PathVariable(value = "page") Long page,
                                      @PathVariable(value = "limit") Long limit,
                                      OrderQueryVo orderQueryVo,
                                      HttpServletRequest request){
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam=new Page<>(page,limit);
        IPage<OrderInfo> iPage=orderService.selectOrderInfoPage(pageParam,orderQueryVo);
        return Result.ok(iPage);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping(value = "/auth/getStatusList")
    public Result getOrderStatusList(){
        return Result.ok(OrderStatusEnum.getStatusList());
    }


    @ApiOperation(value = "取消订单（退款）")
    @GetMapping(value = "/auth/cancelOrder/{orderId}")
    public Result cancelOrder(@PathVariable(value = "orderId")Long orderId){
        boolean b=orderService.cancelOrder(orderId);
        return Result.ok(b);
    }

    @ApiOperation(value = "获取统计数据")
    @PostMapping(value = "/inner/getCountMap")
    public Map<String,Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo){
        Map<String,Object> map=orderService.getCountMap(orderCountQueryVo);
        return map;
    }
}
