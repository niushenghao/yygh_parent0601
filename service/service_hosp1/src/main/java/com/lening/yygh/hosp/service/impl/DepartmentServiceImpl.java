package com.lening.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lening.yygh.hosp.repository.DepartmentRepository;
import com.lening.yygh.hosp.service.DepartmentService;
import com.lening.yygh.model.hosp.Department;
import com.lening.yygh.vo.hosp.DepartmentQueryVo;
import com.lening.yygh.vo.hosp.DepartmentVo;
import com.sun.deploy.net.MessageHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建一个list，最终数据放到list中
        List<DepartmentVo> list = new ArrayList<>();

        //根据医院编号,查询所有医院信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        //获取科室列表 departmentList
        List<Department> departmentList = departmentRepository.findAll(example);

        //根据大科室编号 bigcode分组，获取每个大科室里面下级子科室
        Map<String, List<Department>> departmentMap = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for (Map.Entry<String,List<Department>> entry : departmentMap.entrySet()){
            //大科室编号
            String bigcode = entry.getKey();
            //大科室编号对应的全局数据
            List<Department> department1 = entry.getValue();

            //封装大科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(department1.get(0).getBigname());

            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : department1) {
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                //封装到list集合
                children.add(departmentVo2);
            }
            //把小科室list集合放到大科室children里面
            departmentVo1.setChildren(children);
            //放到最终result里面
            list.add(departmentVo1);
        }
        return list;
    }

    //根据科室编号，和医院编号，查询科室名称
    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null) {
            return department.getDepname();
        }
        return null;
    }

    @Override
    public Department getDepartmentList(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
    }
}
