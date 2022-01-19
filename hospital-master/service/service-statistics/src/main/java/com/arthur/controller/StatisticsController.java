package com.arthur.controller;

import com.arthur.common.result.Result;
import com.arthur.order.feign.OrderFeignClient;
import com.arthur.vo.order.OrderCountQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/statistics")
public class StatisticsController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @ApiOperation(value = "管理端统计时间以及数量")
    @PostMapping(value = "/getCountMap")
    public Result getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo){
        System.out.println(orderCountQueryVo);
        return Result.ok(orderFeignClient.getCountMap(orderCountQueryVo));
    }
}
