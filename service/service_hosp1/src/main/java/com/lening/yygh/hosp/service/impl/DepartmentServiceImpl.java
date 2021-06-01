package com.lening.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lening.yygh.hosp.repository.DepartmentRepository;
import com.lening.yygh.hosp.service.DepartmentService;
import com.lening.yygh.model.hosp.Department;
import com.lening.yygh.vo.hosp.DepartmentQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/29 16:55
 */
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentRepository departmentRepository;

    @Override
    public void saveDepartment(Map<String, Object> paramMap) {
        /**
         * 把他实体对象，从对象中去拿
         */
        Department department = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Department.class);

        String hoscode = department.getHoscode();

        /**
         * 先去查询在判断，用的是医院编码和科室编码
         */
        Department targetDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if(targetDepartment!=null){
            /**
             * 修改
             */
            department.setId(targetDepartment.getId());
            department.setUpdateTime(new Date());
            departmentRepository.save(department);
        }else{
            /**
             * 就是新增
             */
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);

            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> getDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        /**
         * mongoB
         * 用创建时间排序，倒序
         */
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page-1, limit, sort);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Department> example = Example.of(department, matcher);
        Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department!=null){
            departmentRepository.deleteById(department.getId());
        }
    }
}
