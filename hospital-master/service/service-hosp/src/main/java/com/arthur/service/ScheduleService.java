package com.arthur.service;

import com.arthur.model.hosp.Schedule;
import com.arthur.vo.hosp.ScheduleOrderVo;
import com.arthur.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
public interface ScheduleService {
    void save(Map<String, Object> map);

    Page<Schedule> findSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void deleteSchedule(String hoscode, String hosScheduleId);

    Map<String, Object> getScheduleDetail(Integer page, Integer limit, String hoscode, String depcode);

    List<Schedule> fetchScheduleDetails(String hoscode, String depcode, String workdate);

    Map<String,Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    Schedule getScheduleById(String scheduleId);

    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    void updateSchedule(Schedule schedule);
}
