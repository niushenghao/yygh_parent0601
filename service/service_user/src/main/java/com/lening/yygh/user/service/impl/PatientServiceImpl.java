package com.lening.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lening.cmn.client.DictFeignClient;
import com.lening.yygh.enums.DictEnum;
import com.lening.yygh.model.user.Patient;
import com.lening.yygh.user.mapper.PatientMapper;
import com.lening.yygh.user.service.PatientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 牛胜浩
 * @date 2021/6/9 10:41
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Resource
    private PatientMapper patientMapper;

    @Resource
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> findAllUserId(Long userId) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        List<Patient> list = patientMapper.selectList(queryWrapper);

        //通过远程调用，数据字典的信息
        list.stream().forEach(patient ->{
            this.packPatient(patient);
        });
        return list;
    }

    //根据id获取就诊人信息
    @Override
    public Patient getPatient(long id) {
        Patient patient = baseMapper.selectById(id);
        this.packPatient(patient);
        return patient;
    }

    //patient对象其他参数的封装
    private Patient packPatient(Patient patient) {
        //根据证据类型编码
        String certificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }
}
