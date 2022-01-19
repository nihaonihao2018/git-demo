package com.arthur.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.arthur.enums.PaymentTypeEnum;
import com.arthur.enums.RefundStatusEnum;
import com.arthur.model.order.OrderInfo;
import com.arthur.model.order.PaymentInfo;
import com.arthur.model.order.RefundInfo;
import com.arthur.service.OrderService;
import com.arthur.service.PaymentInfoService;
import com.arthur.service.RefundInfoService;
import com.arthur.service.WeChatService;
import com.arthur.utils.ConstantPropertiesUtils;
import com.arthur.utils.HttpClient;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service(value = "weChatService")
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RefundInfoService refundInfoService;

    @Override
    public Map createNative(Long orderId) {


        try {
            Map mapResult= (Map) redisTemplate.opsForValue().get(orderId.toString());
            if(mapResult!=null){
                return mapResult;
            }
            OrderInfo orderInfo = orderService.getOrderInfo(orderId);
            paymentInfoService.savePaymentInfo(orderInfo, PaymentTypeEnum.WEIXIN.getStatus());

            //1、设置参数
            Map paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            String body = orderInfo.getReserveDate() + "就诊"+ orderInfo.getDepname();
            paramMap.put("body", body);
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            //paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
            paramMap.put("total_fee", "1");
            paramMap.put("spbill_create_ip", "127.0.0.1");
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            paramMap.put("trade_type", "NATIVE");
            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            //3.返回微信端的出参
            String xml = client.getContent();
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(xml);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("orderId",orderId);
            map.put("totalFee",orderInfo.getAmount());
            map.put("resultCode",xmlToMap.get("result_code"));
            map.put("codeUrl",xmlToMap.get("code_url"));
            redisTemplate.opsForValue().set(orderId.toString(),map,120, TimeUnit.MINUTES);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
}

    @Override
    public Map<String, String> queryPayStatus(Long orderId, Integer status) {

        try {
            OrderInfo orderInfo = orderService.getOrderInfo(orderId);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("appid", ConstantPropertiesUtils.APPID);
            hashMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            hashMap.put("out_trade_no", orderInfo.getOutTradeNo());
            hashMap.put("nonce_str", WXPayUtil.generateNonceStr());
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(hashMap,ConstantPropertiesUtils.PARTNERKEY));
            httpClient.setHttps(true);
            httpClient.post();
            String xml = httpClient.getContent();
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(xml);
            return xmlToMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }


    }

    @Override
    public boolean refund(long orderId) {

        try {
            PaymentInfo paymentInfo = paymentInfoService.getPaymentInfo(orderId, PaymentTypeEnum.WEIXIN.getStatus());
            RefundInfo refundInfo = refundInfoService.saveRefundInfo(paymentInfo);
            if(refundInfo.getRefundStatus().intValue()== RefundStatusEnum.REFUND.getStatus().intValue()){
                return true;
            }
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("appid",ConstantPropertiesUtils.APPID);       //公众账号ID
            paramMap.put("mch_id",ConstantPropertiesUtils.PARTNER);   //商户编号
            paramMap.put("nonce_str",WXPayUtil.generateNonceStr());
            paramMap.put("transaction_id",paymentInfo.getTradeNo()); //微信订单号
            paramMap.put("out_trade_no",paymentInfo.getOutTradeNo()); //商户订单编号
            paramMap.put("out_refund_no","tk"+paymentInfo.getOutTradeNo()); //商户退款单号
//       paramMap.put("total_fee",paymentInfoQuery.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
//       paramMap.put("refund_fee",paymentInfoQuery.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
            paramMap.put("total_fee","1");
            paramMap.put("refund_fee","1");
            String xml = WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/secapi/pay/refund");
            httpClient.setXmlParam(xml);
            httpClient.setHttps(true);
            httpClient.setCert(true);
            httpClient.setCertPassword(ConstantPropertiesUtils.PARTNER);
            httpClient.post();
            String content = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            if(map!=null&& WXPayConstants.SUCCESS.equalsIgnoreCase(map.get("result_code"))){
                refundInfo.setCallbackTime(new Date());
                refundInfo.setTradeNo(map.get("refund_id"));
                refundInfo.setRefundStatus(RefundStatusEnum.REFUND.getStatus());
                refundInfo.setCallbackContent(JSONObject.toJSONString(map));
                refundInfoService.updateById(refundInfo);
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }
}
