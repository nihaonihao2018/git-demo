package com.arthur.service;

import com.arthur.model.hosp.HospitalSet;
import com.arthur.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @authur arthur
 * @desc
 */
public interface HospitalSetService extends IService<HospitalSet> {


    String getSign(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}
