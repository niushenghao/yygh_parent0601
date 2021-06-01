package com.lening.yygh.hosp.repository;

import com.lening.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 牛胜浩
 * @date 2021/5/29 16:51
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    /**
     *按照spring-data的规范来写
     * 获取部门 可以用get，find，select，都可以
     * getDepartment获取部门
     * 按照什么条件yHoscode  按照医院编码
     * 有两个条件，而且这两个条件并列条件，都要满足，用and把另一个条件连接上就OK啦
     * 自动识别参数
     *
     */
    Department getDepartmentByHoscodeAndDepcode(String hoscode,String depcode);
}
