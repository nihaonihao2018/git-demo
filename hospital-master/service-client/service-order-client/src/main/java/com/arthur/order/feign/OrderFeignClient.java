package com.arthur.order.feign;


import com.arthur.vo.order.OrderCountQueryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Repository
@FeignClient(value = "service-order")
public interface OrderFeignClient {

    @PostMapping(value = "/api/order/orderInfo/inner/getCountMap")
    Map<String,Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo);
}
