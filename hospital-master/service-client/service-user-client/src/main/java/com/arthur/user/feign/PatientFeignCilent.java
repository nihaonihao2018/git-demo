package com.arthur.user.feign;

import com.arthur.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @authur arthur
 * @desc
 */
@Repository
@FeignClient(value = "service-user")
public interface PatientFeignCilent {
    @GetMapping(value = "/api/user/patient/inner/get/{id}")
    public Patient getPatientOrder(@PathVariable(value = "id") Long id);

}
