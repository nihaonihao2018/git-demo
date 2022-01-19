package com.arthur.repository;

import com.arthur.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @authur arthur
 * @desc
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    Hospital getHospitalByHoscode(String hoscode);


    List<Hospital> findHospitalByHosnameLike(String hosname);
}
