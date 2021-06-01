package com.lening.yygh.hosp.service;

import com.lening.yygh.model.hosp.Department;
import com.lening.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/29 14:37
 */
public interface DepartmentService {

    void saveDepartment(Map<String, Object> paramMap);

    Page<Department> getDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);
}
