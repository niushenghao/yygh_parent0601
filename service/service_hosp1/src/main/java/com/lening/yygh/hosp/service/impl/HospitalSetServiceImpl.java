package com.lening.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lening.yygh.common.exception.YyghException;
import com.lening.yygh.common.result.ResultCodeEnum;
import com.lening.yygh.hosp.mapper.HospitalSetMapper;
import com.lening.yygh.hosp.service.HospitalSetService;
import com.lening.yygh.model.hosp.HospitalSet;
import com.lening.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 创作时间：2021/4/25 16:42
 * 作者：牛胜浩
 * 需要我们来实现他的实现类，然后把mapper和我们的反省类给他给他
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Resource
    private HospitalSetMapper hospitalSetMapper;

    //2 根据传递过来医院编码，查询数据库，查询签名
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }
}
