package com.arthur.service;

import com.arthur.model.order.PaymentInfo;
import com.arthur.model.order.RefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RefundInfoService extends IService<RefundInfo> {
    //保存退钱信息
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
