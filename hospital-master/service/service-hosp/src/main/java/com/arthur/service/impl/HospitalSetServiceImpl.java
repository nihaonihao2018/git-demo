package com.arthur.service.impl;

import com.arthur.common.exception.YyghException;
import com.arthur.common.result.ResultCodeEnum;
import com.arthur.mapper.HospitalSetMapper;
import com.arthur.model.hosp.HospitalSet;
import com.arthur.service.HospitalSetService;
import com.arthur.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @authur arthur
 * @desc
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {


    @Override
    public String getSign(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();

        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        //System.out.println(hospitalSet);
        return hospitalSet.getSignKey();
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        SignInfoVo signInfoVo = new SignInfoVo();
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);

        if(null==hospitalSet){
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }

        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());

        return signInfoVo;
    }
}
