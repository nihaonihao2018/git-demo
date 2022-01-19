package com.arthur.scheduled;

import com.arthur.common.constant.MqConst;
import com.arthur.rabbitmq.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private RabbitService rabbitService;

    //0 0 8 * * ?   每天八点定时任务
    @Scheduled(cron = "0/30 * * * * ?")
    public void task1(){
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK,MqConst.ROUTING_TASK_8,"");
    }


}
