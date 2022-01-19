package com.arthur.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.arthur.common.exception.YyghException;
import com.arthur.common.helper.HttpRequestHelper;
import com.arthur.common.result.ResultCodeEnum;
import com.arthur.enums.OrderStatusEnum;
import com.arthur.enums.PaymentStatusEnum;
import com.arthur.hosp.feign.HospitalFeignClient;
import com.arthur.mapper.PaymentInfoMapper;
import com.arthur.model.order.OrderInfo;
import com.arthur.model.order.PaymentInfo;
import com.arthur.service.OrderService;
import com.arthur.service.PaymentInfoService;
import com.arthur.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service(value = "paymentInfoService")
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HospitalFeignClient hospitalFeignClient;

    @Override
    public void savePaymentInfo(OrderInfo orderInfo, Integer status) {
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderInfo.getId());
        wrapper.eq("payment_type",status);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            return;
        }
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(status);
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")+"|"+orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(orderInfo.getAmount());
        baseMapper.insert(paymentInfo);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void paySuccess(String out_trade_no, Integer status, Map<String, String> map) {
        //1.查询paymentInfo并修改
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("out_trade_no",out_trade_no);
        wrapper.eq("payment_type",status);
        PaymentInfo paymentInfo = baseMapper.selectOne(wrapper);
        if(paymentInfo!=null){
            paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
            paymentInfo.setCallbackTime(new Date());
            paymentInfo.setTradeNo(map.get("transaction_id"));
            paymentInfo.setCallbackContent(map.toString());
            baseMapper.updateById(paymentInfo);
        }

        //2.修改订单状态
        OrderInfo orderInfo = orderService.getOrderInfo(paymentInfo.getOrderId());
        if(orderInfo!=null){
            orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
            orderService.updateById(orderInfo);
        }

        //3.修改医院订单状态
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode());
        System.out.println(signInfoVo);
        if(null == signInfoVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode",orderInfo.getHoscode());
        reqMap.put("hosRecordId",orderInfo.getHosRecordId());
        reqMap.put("patient_id",orderInfo.getPatientId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(reqMap, signInfoVo.getSignKey());
        reqMap.put("sign",sign);
        System.out.println(reqMap);
        JSONObject jsonObject = HttpRequestHelper.sendRequest(reqMap, "http://localhost:9998/order/updatePayStatus");
        if(jsonObject.getInteger("code") != 200) {
            throw new YyghException(jsonObject.getString("message"), ResultCodeEnum.FAIL.getCode());
        }

    }

    @Override
    public PaymentInfo getPaymentInfo(long orderId, Integer status) {
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderId);
        wrapper.eq("payment_type",status);
        PaymentInfo paymentInfo = baseMapper.selectOne(wrapper);
        System.out.println(paymentInfo);
        return paymentInfo;
    }
}
