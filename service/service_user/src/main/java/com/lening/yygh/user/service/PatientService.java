package com.lening.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lening.yygh.model.user.Patient;

import java.util.List;

/**
 * @author 牛胜浩
 * @date 2021/6/9 10:41
 */
public interface PatientService extends IService<Patient> {

    //获取就诊人列表
    List<Patient> findAllUserId(Long userId);

    //根据id获取就诊人信息
    Patient getPatient(long id);


}
