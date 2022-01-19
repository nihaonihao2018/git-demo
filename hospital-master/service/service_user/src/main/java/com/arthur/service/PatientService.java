package com.arthur.service;

import com.arthur.model.user.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @authur arthur
 * @desc
 */
public interface PatientService extends IService<Patient> {
    List<Patient> findAllPatient(long userId);

    Patient getPatientById(Long id);
}
