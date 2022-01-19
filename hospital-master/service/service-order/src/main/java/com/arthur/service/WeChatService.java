package com.arthur.service;

import java.util.Map;

public interface WeChatService {
    Map createNative(Long orderId);

    Map<String, String> queryPayStatus(Long orderId, Integer status);

    //退钱
    boolean refund(long orderId);
}
