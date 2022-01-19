package com.arthur.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.arthur.common.constant.MqConst;
import com.arthur.common.exception.YyghException;
import com.arthur.common.helper.HttpRequestHelper;
import com.arthur.common.result.ResultCodeEnum;
import com.arthur.enums.OrderStatusEnum;
import com.arthur.hosp.feign.HospitalFeignClient;
import com.arthur.mapper.OrderMapper;
import com.arthur.model.order.OrderInfo;
import com.arthur.model.user.Patient;
import com.arthur.rabbitmq.RabbitService;
import com.arthur.service.OrderService;
import com.arthur.service.WeChatService;
import com.arthur.user.feign.PatientFeignCilent;
import com.arthur.vo.hosp.ScheduleOrderVo;
import com.arthur.vo.order.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private PatientFeignCilent patientFeignCilent;

    @Autowired
    private HospitalFeignClient hospitalFeignClient;

    @Autowired
    private WeChatService weChatService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        Patient patient = patientFeignCilent.getPatientOrder(patientId);
        if(patient==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        ScheduleOrderVo scheduleOrderVo = hospitalFeignClient.getScheduleOrderVo(scheduleId);
        if(scheduleOrderVo==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        if(new DateTime(scheduleOrderVo.getStartTime()).isAfterNow()
                ||new DateTime(scheduleOrderVo.getEndTime()).isBeforeNow()){
            throw new YyghException(ResultCodeEnum.TIME_NO);
        }

        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(scheduleOrderVo.getHoscode());
        if(scheduleOrderVo.getAvailableNumber()==null){
            throw new YyghException(ResultCodeEnum.NUMBER_NO);
        }

        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(scheduleOrderVo,orderInfo);
        String outTradeNo = System.currentTimeMillis() + "" + new Random().nextInt(100);
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setScheduleId(scheduleId);
        orderInfo.setUserId(patient.getUserId());
        orderInfo.setPatientId(patientId);
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientPhone(patient.getPhone());
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());
        this.save(orderInfo);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",orderInfo.getHoscode());
        paramMap.put("depcode",orderInfo.getDepcode());
        paramMap.put("hosScheduleId",orderInfo.getScheduleId());
        paramMap.put("reserveDate",new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd"));
        paramMap.put("reserveTime", orderInfo.getReserveTime());
        paramMap.put("amount",orderInfo.getAmount());
        paramMap.put("name", patient.getName());
        paramMap.put("certificatesType",patient.getCertificatesType());
        paramMap.put("certificatesNo", patient.getCertificatesNo());
        paramMap.put("sex",patient.getSex());
        paramMap.put("birthdate", patient.getBirthdate());
        paramMap.put("phone",patient.getPhone());
        paramMap.put("isMarry", patient.getIsMarry());
        paramMap.put("provinceCode",patient.getProvinceCode());
        paramMap.put("cityCode", patient.getCityCode());
        paramMap.put("districtCode",patient.getDistrictCode());
        paramMap.put("address",patient.getAddress());
        //联系人
        paramMap.put("contactsName",patient.getContactsName());
        paramMap.put("contactsCertificatesType", patient.getContactsCertificatesType());
        paramMap.put("contactsCertificatesNo",patient.getContactsCertificatesNo());
        paramMap.put("contactsPhone",patient.getContactsPhone());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("patient_id",orderInfo.getPatientId());
        String sign = HttpRequestHelper.getSign(paramMap, signInfoVo.getSignKey());
        paramMap.put("sign",sign);

        JSONObject result = HttpRequestHelper.sendRequest(paramMap, "http://localhost:9998/order/submitOrder");
        if(result.getInteger("code")==200){
            JSONObject jsonObject = result.getJSONObject("data");
            //预约记录唯一标识（医院预约记录主键）
            String hosRecordId = jsonObject.getString("hosRecordId");
            //预约序号
            Integer number = jsonObject.getInteger("number");
            //取号时间
            String fetchTime = jsonObject.getString("fetchTime");
            //取号地址
            String fetchAddress = jsonObject.getString("fetchAddress");
            //更新订单
            orderInfo.setHosRecordId(hosRecordId);
            orderInfo.setNumber(number);
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
            baseMapper.updateById(orderInfo);
            //排班可预约数
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            //排班剩余预约数
            Integer availableNumber = jsonObject.getInteger("availableNumber");
            //发送mq信息更新号源和短信通知

            OrderMqVoMe orderMqVoMe = new OrderMqVoMe();
            orderMqVoMe.setScheduleId(hosRecordId);
            orderMqVoMe.setAvailableNumber(availableNumber);
            orderMqVoMe.setReservedNumber(reservedNumber);

            rabbitService.sendMessage(
                    MqConst.EXCHANGE_DIRECT_ORDER,
                    MqConst.ROUTING_ORDER,
                    orderMqVoMe);

        }else {
            throw new YyghException(result.getString("message"),ResultCodeEnum.FAIL.getCode());
        }



        return orderInfo.getId();
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        OrderInfo info = packageOrderInfo(orderInfo);

        return info;
    }

    @Override
    public IPage<OrderInfo> selectOrderInfoPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo) {
        String name = orderQueryVo.getKeyword();//医院名称
        String orderStatus = orderQueryVo.getOrderStatus();//订单状态
        Long patientId = orderQueryVo.getPatientId();//就诊人编号
        String reserveDate = orderQueryVo.getReserveDate();//安排时间
        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderQueryVo.getCreateTimeEnd();
        String outTradeNo = orderQueryVo.getOutTradeNo();//订单号
        String patientName = orderQueryVo.getPatientName();//就诊人姓名

        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            wrapper.eq("hosname",name);
        }
        if(!StringUtils.isEmpty(orderStatus)){
            wrapper.eq("order_status",orderStatus);
        }
        if(!StringUtils.isEmpty(patientId)){
            wrapper.eq("patient_id",patientId);
        }
        if(!StringUtils.isEmpty(reserveDate)){
            wrapper.eq("reserve_date",reserveDate);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le("create_time",createTimeEnd);
        }
        if(!StringUtils.isEmpty(outTradeNo)){
            wrapper.eq("out_trade_no",outTradeNo);
        }
        if(!StringUtils.isEmpty(patientName)){
            wrapper.eq("patient_name",patientName);
        }

        Page<OrderInfo> orderInfoPage = baseMapper.selectPage(pageParam,wrapper);
        orderInfoPage.getRecords().stream().forEach(orderInfo -> {
            packageOrderInfo(orderInfo);
        });

        return orderInfoPage;
    }

    @Override
    public Map<String, Object> selectOrderInfoDetail(Long orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        OrderInfo packageOrderInfo = packageOrderInfo(orderInfo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderInfo",packageOrderInfo);
        Patient patient = patientFeignCilent.getPatientOrder(orderInfo.getPatientId());
        map.put("patient",patient);
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelOrder(Long orderId) {
        OrderInfo orderInfo = getOrderInfo(orderId);
        DateTime quitTime = new DateTime(orderInfo.getQuitTime());
        if (quitTime.isBeforeNow()) {
            throw new YyghException(ResultCodeEnum.CANCEL_ORDER_FAIL);
        }
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode());
        if(signInfoVo==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode",orderInfo.getHoscode());
        reqMap.put("patient_id",orderInfo.getPatientId());
        reqMap.put("hosRecordId",orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(reqMap, signInfoVo.getSignKey());
        reqMap.put("sign", sign);

        JSONObject result = HttpRequestHelper.sendRequest(reqMap, "http://localhost:9998/order/updateCancelStatus");
        if(result.getInteger("code")!=200){
            throw new YyghException(result.getString("message"),ResultCodeEnum.FAIL.getCode());
        }else {
            //已支付的订单
            if (orderInfo.getOrderStatus().intValue()==OrderStatusEnum.PAID.getStatus().intValue()) {
                boolean refund = weChatService.refund(orderId);
                if(!refund){
                    throw new YyghException(ResultCodeEnum.CANCEL_ORDER_FAIL);
                }
            }

            orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
            baseMapper.updateById(orderInfo);

            //更新schedule可预约数量（+1）
            OrderMqVoMe orderMqVoMe = new OrderMqVoMe();
            orderMqVoMe.setScheduleId(orderInfo.getScheduleId());
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER,MqConst.ROUTING_ORDER,orderMqVoMe);


        }


        return true;
    }

    @Override
    public List<OrderInfo> selectOrderByWorkDate(String scheduleId, Long patientId, Date workDate) {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("hos_schedule_id",scheduleId);
        wrapper.eq("patient_id",patientId);
        wrapper.eq("reserve_date",workDate);
        //OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        //if(orderInfo!=null){

        //    OrderInfo info = packageOrderInfo(orderInfo);
        //    return info;
        //}
        //return orderInfo;

        List<OrderInfo> orderInfos = baseMapper.selectList(wrapper);
        List<OrderInfo> collect = orderInfos.stream().filter(orderInfo -> {
            return orderInfo.getOrderStatus().intValue()
                    != OrderStatusEnum.CANCLE.getStatus().intValue();
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public void patientCalled() {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.ne("order_status",OrderStatusEnum.CANCLE.getStatus().intValue());
        wrapper.eq("reserve_date",new DateTime().toString("yyyy-MM-dd"));
        List<OrderInfo> orderInfos = baseMapper.selectList(wrapper);
        System.out.println(orderInfos);

    }

    @Override
    public Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo) {
        HashMap<String, Object> map = new HashMap<>();
        List<OrderCountVo> orderCountVos = baseMapper.selectOrderCount(orderCountQueryVo);
        List<Integer> countList = orderCountVos.stream()
                .map(OrderCountVo::getCount).collect(Collectors.toList());
        List<String> dateList = orderCountVos.stream()
                .map(OrderCountVo::getReserveDate).collect(Collectors.toList());
        map.put("dateList",dateList);
        map.put("countList",countList);

        return map;
    }

    private OrderInfo packageOrderInfo(OrderInfo orderInfo){
        orderInfo.getParam().put("orderStatusString",OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
        return orderInfo;
    }
}
