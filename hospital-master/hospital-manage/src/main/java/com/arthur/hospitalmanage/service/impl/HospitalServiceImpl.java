package com.arthur.hospitalmanage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.arthur.hospitalmanage.mapper.OrderInfoMapper;
import com.arthur.hospitalmanage.mapper.ScheduleMapper;
import com.arthur.hospitalmanage.model.OrderInfo;
import com.arthur.hospitalmanage.model.Patient;
import com.arthur.hospitalmanage.model.Schedule;
import com.arthur.hospitalmanage.service.HospitalService;
import com.arthur.hospitalmanage.util.ResultCodeEnum;
import com.arthur.hospitalmanage.util.YyghException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	private ScheduleMapper hospitalMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> submitOrder(Map<String, Object> paramMap) {
        //log.info(JSONObject.toJSONString(paramMap));
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        String reserveDate = (String)paramMap.get("reserveDate");
        String reserveTime = (String)paramMap.get("reserveTime");
        String amount = (String)paramMap.get("amount");
        String patientId= (String) paramMap.get("patient_id");
        long aLong = Long.parseLong(patientId);
        //log.info(amount);

        QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
        wrapper.eq("hos_schedule_id",hosScheduleId);
        Schedule schedule = hospitalMapper.selectOne(wrapper);

        //Schedule schedule = this.getSchedule(hosScheduleId);
        if(null == schedule) {

            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }


        if(!schedule.getHoscode().equals(hoscode)
                ||!schedule.getDepcode().equals(depcode)
                ||!schedule.getAmount().equals(amount)) {
            //System.out.println(depcode);
            //System.out.println(schedule.getDepcode());
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }

        //就诊人信息
        Patient patient = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Patient.class);
        log.info(JSONObject.toJSONString(patient));
        //处理就诊人业务
        //Long patientId = this.savePatient(patient);

        Map<String, Object> resultMap = new HashMap<>();
        int availableNumber = schedule.getAvailableNumber().intValue() - 1;
        if(availableNumber > 0) {
            schedule.setAvailableNumber(availableNumber);
            hospitalMapper.updateById(schedule);

            //记录预约记录
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setPatientId(aLong);
            orderInfo.setScheduleId(hosScheduleId);
            int number = schedule.getReservedNumber().intValue() - schedule.getAvailableNumber().intValue();
            orderInfo.setNumber(number);
            orderInfo.setAmount(new BigDecimal(amount));
            String fetchTime = "0".equals(reserveDate) ? " 09:30前" : " 14:00前";
            orderInfo.setFetchTime(reserveTime + fetchTime);
            orderInfo.setFetchAddress("一楼9号窗口");
            //默认 未支付
            orderInfo.setOrderStatus(0);
            orderInfoMapper.insert(orderInfo);

            resultMap.put("resultCode","0000");
            resultMap.put("resultMsg","预约成功");
            //预约记录唯一标识（医院预约记录主键）
            resultMap.put("hosRecordId", orderInfo.getScheduleId());
            //预约号序
            resultMap.put("number", number);
            //取号时间
            resultMap.put("fetchTime", reserveDate + "09:00前");
            //取号地址
            resultMap.put("fetchAddress", "一层114窗口");
            //排班可预约数
            resultMap.put("reservedNumber", schedule.getReservedNumber());
            //排班剩余预约数
            resultMap.put("availableNumber", schedule.getAvailableNumber());
        } else {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        return resultMap;
    }

    @Override
    public void updatePayStatus(Map<String, Object> paramMap) {
        String hoscode = (String)paramMap.get("hoscode");
        String hosRecordId = (String)paramMap.get("hosRecordId");
        String patient_id = (String) paramMap.get("patient_id");

        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("schedule_id",hosRecordId);
        wrapper.ne("order_status",-1);
        wrapper.eq("patient_id",patient_id);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        //OrderInfo orderInfo = orderInfoMapper.selectById(hosRecordId);
        if(null == orderInfo) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        //已支付
        orderInfo.setOrderStatus(1);
        orderInfo.setPayTime(new Date());
        orderInfoMapper.updateById(orderInfo);
    }

    @Override
    public void updateCancelStatus(Map<String, Object> paramMap) {
        String hoscode = (String)paramMap.get("hoscode");
        String hosRecordId = (String)paramMap.get("hosRecordId");
        String patient_id = (String) paramMap.get("patient_id");

        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("schedule_id",hosRecordId);
        wrapper.ne("order_status",-1);
        wrapper.eq("patient_id",patient_id);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        QueryWrapper<Schedule> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("hos_schedule_id",hosRecordId);
        Schedule schedule = hospitalMapper.selectOne(wrapper1);

        //OrderInfo orderInfo = orderInfoMapper.selectById(hosRecordId);
        if(null == orderInfo) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        if(null == schedule) {

            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        //已取消
        orderInfo.setOrderStatus(-1);
        orderInfo.setQuitTime(new Date());
        orderInfoMapper.updateById(orderInfo);
        int i = schedule.getAvailableNumber().intValue() + 1;
        schedule.setAvailableNumber(i);
        if(schedule.getAvailableNumber()>0){
            schedule.setUpdateTime(new Date());
            hospitalMapper.updateById(schedule);
        }
    }

    private Schedule getSchedule(String frontSchId) {
        return hospitalMapper.selectById(frontSchId);
    }

    /**
     * 医院处理就诊人信息
     * @param patient
     */
    private Long savePatient(Patient patient) {
        // 业务：略
        return 1L;
    }


}
