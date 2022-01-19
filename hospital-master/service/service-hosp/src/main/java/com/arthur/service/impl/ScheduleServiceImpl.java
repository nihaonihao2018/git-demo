package com.arthur.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.arthur.common.exception.YyghException;
import com.arthur.common.result.ResultCodeEnum;
import com.arthur.common.utils.DateUtil;
import com.arthur.model.hosp.BookingRule;
import com.arthur.model.hosp.Department;
import com.arthur.model.hosp.Hospital;
import com.arthur.model.hosp.Schedule;
import com.arthur.repository.ScheduleRepository;
import com.arthur.service.DepartmentService;
import com.arthur.service.HospitalService;
import com.arthur.service.ScheduleService;
import com.arthur.vo.hosp.BookingScheduleRuleVo;
import com.arthur.vo.hosp.ScheduleOrderVo;
import com.arthur.vo.hosp.ScheduleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository  scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void save(Map<String, Object> map) {
        String jsonString = JSONObject.toJSONString(map);
        Schedule schedule = JSONObject.parseObject(jsonString, Schedule.class);
        String hoscode = schedule.getHoscode();
        String hosScheduleId = schedule.getHosScheduleId();
        Schedule schedule1=scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode,hosScheduleId);
        if(null!=schedule1){
            //修改
            schedule1.setUpdateTime(new Date());
            schedule1.setIsDeleted(0);
            schedule1.setStatus(1);
            scheduleRepository.save(schedule1);
        }else {
            //添加
            schedule.setUpdateTime(new Date());
            schedule.setCreateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }

    }

    @Override
    public Page<Schedule> findSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page-1, limit, sort);
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setStatus(1);

        Example<Schedule> example = Example.of(schedule, matcher);

        Page<Schedule> all = scheduleRepository.findAll(example, pageable);

        return all;
    }

    @Override
    public void deleteSchedule(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if(schedule!=null){
            scheduleRepository.delete(schedule);
        }
    }

    @Override
    public Map<String, Object> getScheduleDetail(Integer page, Integer limit, String hoscode, String depcode) {
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                .count().as("docCount")
                .sum("reservedNumber").as("reservedNumber")
                .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.DESC,"workDate"),
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggregate.getMappedResults();

        Aggregation newAggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> aggregate1 = mongoTemplate.aggregate(newAggregation, Schedule.class, BookingScheduleRuleVo.class);
        int total = aggregate1.getMappedResults().size();

        for(BookingScheduleRuleVo bsrv:bookingScheduleRuleVoList){
            Date workDate = bsrv.getWorkDate();
            String dayOfWeek = DateUtil.getDayOfWeek(new DateTime(workDate));
            bsrv.setDayOfWeek(dayOfWeek);
        }

        Hospital hospitals = hospitalService.getHospitalByHoscode(hoscode);
        String hosname = hospitals.getHosname();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bookingScheduleRuleList",bookingScheduleRuleVoList);
        hashMap.put("total",total);

        HashMap<String, Object> hospital = new HashMap<>();
        hospital.put("hosname",hosname);

        hashMap.put("baseMap",hospital);

        return hashMap;
    }

    @Override
    public List<Schedule> fetchScheduleDetails(String hoscode, String depcode, String workdate) {
        System.out.println(new DateTime(workdate).toDate());
        List<Schedule> schedules=scheduleRepository.getScheduleByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,new DateTime(workdate).toDate());
        System.out.println(schedules);
        schedules.stream().forEach(schedule -> {
            String hoscode1 = schedule.getHoscode();
            Hospital hospital = hospitalService.getHospitalByHoscode(hoscode1);
            schedule.getParam().put("hosname",hospital.getHosname());

            Department department=departmentService.getDepartmentByDepcode(schedule.getDepcode());
            schedule.getParam().put("depname",department.getDepname());

            String dayOfWeek = DateUtil.getDayOfWeek(new DateTime(schedule.getWorkDate()));
            schedule.getParam().put("dayOfWeek",dayOfWeek);

        });

        return schedules;
    }

    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {

        HashMap<String, Object> result = new HashMap<>();

        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        if(hospital==null){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        IPage iPage=this.getListDate(page,limit,bookingRule);
        //System.out.println(iPage);

        List<Date> dateList = iPage.getRecords();

        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(dateList);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregationResults.getMappedResults();

        //System.out.println(scheduleVoList);
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if(scheduleVoList!=null) {
            scheduleVoMap = scheduleVoList.stream().
                    collect(
                            Collectors.toMap(BookingScheduleRuleVo::getWorkDate,
                                    BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }

        //System.out.println(scheduleVoMap);

        ArrayList<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for(int i=0;i<dateList.size();i++){
            Date date = dateList.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            if(bookingScheduleRuleVo==null){
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                bookingScheduleRuleVo.setDocCount(0);
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            bookingScheduleRuleVo.setDayOfWeek(DateUtil.getDayOfWeek(new DateTime(date)));

            if(i==(dateList.size()-1)&&page==iPage.getPages()){
                bookingScheduleRuleVo.setStatus(1);

            }else {
                bookingScheduleRuleVo.setStatus(0);
            }
            if(i==0&&page==1){
                System.out.println(bookingRule.getStopTime());
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                System.out.println(stopTime);
                if(stopTime.isBeforeNow()){
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);

        }

        result.put("bookingScheduleList",bookingScheduleRuleVoList);
        result.put("total",iPage.getTotal());

        HashMap<String, String> baseMap = new HashMap<>();

        baseMap.put("hosname",hospitalService.getHospitalByHoscode(hoscode).getHosname());
        Department department=departmentService.getDepartmentByHoscodeAndDepcode(hoscode,depcode);

        baseMap.put("bigname",department.getBigname());
        baseMap.put("depname",department.getDepname());

        baseMap.put("workDateString",new DateTime().toString("yyyy年MM月"));
        baseMap.put("releaseTime",bookingRule.getReleaseTime());
        baseMap.put("stopTime",bookingRule.getStopTime());
        result.put("baseMap",baseMap);
        //System.out.println(result);

        return result;
    }

    @Override
    public Schedule getScheduleById(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        schedule.getParam().put("hosname",hospitalService.getHospitalByHoscode(schedule.getHoscode()).getHosname());
        schedule.getParam().put("depname",departmentService.getDepartmentByHoscodeAndDepcode(schedule.getHoscode(),schedule.getDepcode()).getDepname());
        schedule.getParam().put("dayOfWeek",DateUtil.getDayOfWeek(new DateTime(schedule.getWorkDate())));

        return schedule;
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        if(schedule==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        Hospital hospital = hospitalService.getHospitalByHoscode(schedule.getHoscode());
        if(hospital==null){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        if(bookingRule==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        scheduleOrderVo.setHoscode(hospital.getHoscode());
        scheduleOrderVo.setHosname(hospital.getHosname());
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        scheduleOrderVo.setDepname(departmentService.getDepartmentByHoscodeAndDepcode(schedule.getHoscode(),schedule.getDepcode()).getDepname());
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());

        Integer quitDay = bookingRule.getQuitDay();
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plus(quitDay).toDate(), bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());

        DateTime startTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStopTime(startTime.toDate());

        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStopTime(stopTime.toDate());
        return scheduleOrderVo;
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        schedule.setUpdateTime(new DateTime().toDate());
        scheduleRepository.save(schedule);

    }

    private IPage getListDate(Integer page, Integer limit, BookingRule bookingRule) {

        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        Integer cycle = bookingRule.getCycle();
        //System.out.println(cycle);
        if(releaseTime.isBeforeNow()){
            cycle+=1;
        }
        ArrayList<Date> dateList = new ArrayList<>();
        for(int i=0;i<cycle;i++){
            DateTime dateTime = new DateTime().plusDays(i);
            String s = dateTime.toString("yyyy-MM-dd");
            Date date = new DateTime(s).toDate();
            dateList.add(date);
        }

        ArrayList<Date> pageDateList = new ArrayList<>();
        int start=(page-1)*limit;
        int end=(page-1)*limit+limit;
        if(end>dateList.size()){
            end=dateList.size();
        }
        for(int i=start;i<end;i++){
            pageDateList.add(dateList.get(i));
        }

        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page,7,dateList.size());

        //System.out.println(dateList);
        iPage.setRecords(pageDateList);
        return iPage;
    }

    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " "+ timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }
}
