package com.arthur.hosp.feign;

import com.arthur.vo.hosp.ScheduleOrderVo;
import com.arthur.vo.order.SignInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @authur arthur
 * @desc
 */
@Repository
@FeignClient(value = "service-hosp")
public interface HospitalFeignClient {

    @GetMapping(value = "/api/hosp/hospital/inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable(value = "scheduleId")String scheduleId);

    @GetMapping(value = "/api/hosp/hospital/inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(@PathVariable(value = "hoscode")String hoscode);
}
