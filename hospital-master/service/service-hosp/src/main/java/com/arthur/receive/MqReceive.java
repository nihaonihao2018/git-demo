package com.arthur.receive;


import com.arthur.common.constant.MqConst;
import com.arthur.model.hosp.Schedule;
import com.arthur.service.ScheduleService;
import com.arthur.vo.order.OrderMqVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqReceive {


    @Autowired
    private ScheduleService scheduleService;

    /*@RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER,durable ="true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVoMe orderMqVoMe, Message message, Channel channel){
        Schedule schedule = scheduleService.getScheduleById(orderMqVoMe.getScheduleId());
        schedule.setAvailableNumber(orderMqVoMe.getAvailableNumber());
        schedule.setReservedNumber(orderMqVoMe.getReservedNumber());
        scheduleService.updateSchedule(schedule);
    }*/

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel)  {
        //下单成功更新预约数
        Schedule schedule = scheduleService.getScheduleById(orderMqVo.getScheduleId());
        if(orderMqVo.getAvailableNumber()!=null){
            schedule.setReservedNumber(orderMqVo.getReservedNumber());
            schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
            scheduleService.updateSchedule(schedule);
        }else {
            int i = schedule.getAvailableNumber().intValue() + 1;
            schedule.setAvailableNumber(i);
            scheduleService.updateSchedule(schedule);
        }


    }


}
