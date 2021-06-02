package com.lening.yygh.hosp.service;

import com.lening.yygh.model.hosp.Department;
import com.lening.yygh.vo.hosp.DepartmentQueryVo;
import com.lening.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/29 14:37
 */
public interface DepartmentService {

    void saveDepartment(Map<String, Object> paramMap);

    Page<Department> getDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    //根据医院编号，查询医院所有科室列表
    List<DepartmentVo> findDeptTree(String hoscode);

    //根据科室编号，和医院编号，查询科室名称
    String getDepName(String hoscode, String depcode);
}
