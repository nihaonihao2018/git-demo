package com.arthur.service;

import com.arthur.model.order.OrderInfo;
import com.arthur.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface PaymentInfoService extends IService<PaymentInfo> {

    void savePaymentInfo(OrderInfo orderInfo, Integer status);

    void paySuccess(String out_trade_no, Integer status, Map<String, String> map);

    //得到payment
    PaymentInfo getPaymentInfo(long orderId,Integer status);
}
