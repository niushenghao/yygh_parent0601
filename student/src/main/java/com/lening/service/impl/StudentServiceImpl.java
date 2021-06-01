package com.lening.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lening.entity.StudentBean;
import com.lening.mapper.StudentMapper;
import com.lening.service.StudentService;
import com.lening.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 牛胜浩
 * @date 2021/5/31 9:25
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentBean> implements StudentService {

    @Resource
    private StudentMapper studentMapper;
}
