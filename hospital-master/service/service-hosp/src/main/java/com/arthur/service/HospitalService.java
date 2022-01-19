package com.arthur.service;

import com.arthur.model.hosp.Hospital;
import com.arthur.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
public interface HospitalService {
    void save(Map<String, Object> map);

    Hospital getHospitalByHoscode(String hoscode);

    Page<Hospital> findHospitals(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> fetchHospDetails(String id);

    List<Hospital> findHospitalByHoscode(String hosname);

    Map<String, Object> fetchHospDetail(String hoscode);

}
