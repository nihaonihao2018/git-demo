package com.arthur.service;

import com.arthur.model.order.OrderInfo;
import com.arthur.vo.order.OrderCountQueryVo;
import com.arthur.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

    OrderInfo getOrderInfo(Long orderId);

    IPage<OrderInfo> selectOrderInfoPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    Map<String, Object> selectOrderInfoDetail(Long orderId);

    boolean cancelOrder(Long orderId);

    List<OrderInfo> selectOrderByWorkDate(String scheduleId, Long patientId, Date workDate);

    void patientCalled();

    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}
