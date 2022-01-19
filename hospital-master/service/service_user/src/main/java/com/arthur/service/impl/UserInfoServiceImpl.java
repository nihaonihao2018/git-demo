package com.arthur.service.impl;

import com.arthur.common.exception.YyghException;
import com.arthur.common.helper.JwtHelper;
import com.arthur.common.result.ResultCodeEnum;
import com.arthur.enums.AuthStatusEnum;
import com.arthur.mapper.UserInfoMapper;
import com.arthur.model.user.Patient;
import com.arthur.model.user.UserInfo;
import com.arthur.service.PatientService;
import com.arthur.service.UserInfoService;
import com.arthur.vo.user.LoginVo;
import com.arthur.vo.user.UserAuthVo;
import com.arthur.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "userInfoService")
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private PatientService patientService;

    @Override
    public HashMap<String, Object> login(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        log.info(phone+"-----"+code);
        if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(code)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //绑定手机号码
        UserInfo userInfo = null;
        if(!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectUserInfoByOpenId(loginVo.getOpenid());
            //QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            //wrapper.eq("phone",loginVo.getPhone());
            //UserInfo info = baseMapper.selectOne(wrapper);
            if(null != userInfo) {
                //if(null!=info.getPhone()){
                    //info.setNickName(userInfo.getNickName());
                    //info.setOpenid(userInfo.getOpenid());
                    //this.updateById(info);
                //}else {
                QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
                wrapper.eq("phone",loginVo.getPhone());
                UserInfo info = baseMapper.selectOne(wrapper);
                if(null!=info){
                    baseMapper.delete(wrapper);
                }
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
                //}

            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }


        if(null==userInfo){
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone",phone);
            userInfo = baseMapper.selectOne(wrapper);

            if(userInfo==null){
                userInfo=new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                this.save(userInfo);
            }

        }

        if(userInfo.getStatus()==0){
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        String s = redisTemplate.opsForValue().get(phone);
        if(!code.equals(s)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        HashMap<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)){
            name=userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)){
            name=userInfo.getPhone();
        }
        map.put("name",name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token",token);
        return map;
    }

    @Override
    public UserInfo selectUserInfoByOpenId(Object openid) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        return userInfo;
    }

    @Override
    public void userAuth(long userId, UserAuthVo userAuthVo) {
        UserInfo userInfo = baseMapper.selectById(userId);
        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        baseMapper.updateById(userInfo);
    }

    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> userInfoPage, UserInfoQueryVo userInfoQueryVo) {

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if(userInfoQueryVo!=null){
            String name = userInfoQueryVo.getKeyword();
            Integer authStatus = userInfoQueryVo.getAuthStatus();
            Integer status = userInfoQueryVo.getStatus();
            String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();
            String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();
            if(!StringUtils.isEmpty(name)){
                wrapper.like("name",name);
            }
            if(!StringUtils.isEmpty(authStatus)){
                wrapper.eq("auth_status",authStatus);
            }
            if(!StringUtils.isEmpty(status)){
                wrapper.eq("status",status);
            }
            if(!StringUtils.isEmpty(createTimeBegin)){
                wrapper.ge("create_time",createTimeBegin);
            }
            if(!StringUtils.isEmpty(createTimeEnd)){
                wrapper.le("create_time",createTimeEnd);
            }
        }


        Page<UserInfo> selectPage = baseMapper.selectPage(userInfoPage, wrapper);
        selectPage.getRecords().stream().forEach(userInfo -> {
            this.packagePage(userInfo);
        });
        return selectPage;
    }

    @Override
    public void lockUserInfo(Long userId, Integer status) {
        if(status.intValue()==0||status.intValue()==1){
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

    @Override
    public HashMap<String, Object> show(Long userId) {
        HashMap<String, Object> map = new HashMap<>();
        UserInfo userInfo = baseMapper.selectById(userId);
        UserInfo packageUserInfo = this.packagePage(userInfo);
        List<Patient> allPatient = patientService.findAllPatient(userId);
        map.put("userInfo",packageUserInfo);
        map.put("patientList",allPatient);

        return map;
    }

    @Override
    public void approval(Long userId, Integer authStatus) {
        if(authStatus.intValue()==2||authStatus.intValue()==-1){
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    private UserInfo packagePage(UserInfo userInfo) {
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        String statusString = userInfo.getStatus().intValue() == 0 ? "锁定" : "正常";
        userInfo.getParam().put("statusString",statusString);
        return userInfo;
    }


}
