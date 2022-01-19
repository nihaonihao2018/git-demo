package com.arthur.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.arthur.cmn.feign.DictFeignClient;
import com.arthur.model.hosp.BookingRule;
import com.arthur.model.hosp.Hospital;
import com.arthur.repository.HospitalRepository;
import com.arthur.service.HospitalService;
import com.arthur.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "hospitalService")
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;


    @Override
    public void save(Map<String, Object> map) {
        String jsonString = JSONObject.toJSONString(map);
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist=hospitalRepository.getHospitalByHoscode(hoscode);
        //存在即修改
        if(null!=hospitalExist){
            hospital.setId(hospitalExist.getId());
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setUpdateTime(new Date());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else { //不存在新增
            hospital.setStatus(0);//0未上线 1上线
            hospital.setUpdateTime(new Date());
            hospital.setCreateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);

        }
    }

    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    @Override
    public Page<Hospital> findHospitals(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable= PageRequest.of(page-1,limit,sort);
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> all = hospitalRepository.findAll(example, pageable);
        //System.out.println(DictEnum.HOSTYPE.getDictCode());
        all.getContent().stream().forEach(hosp->{
            String hostypeString = dictFeignClient.findName("Hostype", hosp.getHostype());
            String cityString = dictFeignClient.findName(hosp.getCityCode());
            String districtString = dictFeignClient.findName(hosp.getDistrictCode());
            String provinceString = dictFeignClient.findName(hosp.getProvinceCode());
            hosp.getParam().put("hostypeString",hostypeString);
            hosp.getParam().put("fullAddress",provinceString+cityString+districtString+hosp.getAddress());

        });

        return all;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        //System.out.println(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> fetchHospDetails(String id) {
        HashMap<String, Object> map = new HashMap<>();
        Hospital hospital = hospitalRepository.findById(id).get();
        String hostypeString = dictFeignClient.findName("Hostype", hospital.getHostype());
        String cityString = dictFeignClient.findName(hospital.getCityCode());
        String districtString = dictFeignClient.findName(hospital.getDistrictCode());
        String provinceString = dictFeignClient.findName(hospital.getProvinceCode());
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString+hospital.getAddress());
        map.put("hospital",hospital);
        BookingRule bookingRule = hospital.getBookingRule();
        map.put("bookingRule",bookingRule);
        hospital.setBookingRule(null);
        return map;
    }

    @Override
    public List<Hospital> findHospitalByHoscode(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);

    }

    @Override
    public Map<String, Object> fetchHospDetail(String hoscode) {
        HashMap<String, Object> map = new HashMap<>();
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        String hostypeString = dictFeignClient.findName("Hostype", hospital.getHostype());
        String cityString = dictFeignClient.findName(hospital.getCityCode());
        String districtString = dictFeignClient.findName(hospital.getDistrictCode());
        String provinceString = dictFeignClient.findName(hospital.getProvinceCode());
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString+hospital.getAddress());
        map.put("hospital",hospital);
        map.put("bookingRule",hospital.getBookingRule());
        hospital.setBookingRule(null);

        return map;
    }


}
