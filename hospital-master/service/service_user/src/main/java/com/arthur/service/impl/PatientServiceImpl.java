package com.arthur.service.impl;

import com.arthur.cmn.feign.DictFeignClient;
import com.arthur.enums.DictEnum;
import com.arthur.mapper.PatientMapper;
import com.arthur.model.user.Patient;
import com.arthur.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "patientService")
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Resource
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> findAllPatient(long userId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Patient> patients = baseMapper.selectList(wrapper);
        patients.stream().forEach(patient -> {
            this.packPatient(patient);
        });
        return patients;
    }

    @Override
    public Patient getPatientById(Long id) {
        Patient patient = baseMapper.selectById(id);
        this.packPatient(patient);
        return patient;
    }

    private Patient packPatient(Patient patient) {
        //证件人证件类型
        String dictFeignClientName = dictFeignClient.findName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        //联系人证件类型
        String feignClientName = dictFeignClient.findName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());
        //省
        String provinceName = dictFeignClient.findName(patient.getProvinceCode());
        //市
        String cityName = dictFeignClient.findName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.findName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", dictFeignClientName);
        patient.getParam().put("contactsCertificatesTypeString", feignClientName);
        patient.getParam().put("provinceString", provinceName);
        patient.getParam().put("cityString", cityName);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceName + cityName + districtString + patient.getAddress());

        return patient;
    }
}
