package com.lening.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lening.entity.StudentBean;
import com.lening.entity.StudentQueryVo;
import com.lening.service.StudentService;
import com.lening.yygh.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 牛胜浩
 * @date 2021/5/31 9:27
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/student")
@CrossOrigin
public class StudentController {

    @Resource
    private StudentService studentService;

    @PostMapping("findAllPage/{current}/{limit}")
    public Result getStudentList(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) StudentQueryVo studentBean) {
        //分页，常见page对象,把分页参数给他
        //还要检查分页的驱动有没有初始化
        System.out.println(studentBean);
        Page<StudentBean> page = new Page<>(current, limit);

        //条件查询
        //构建条件
        QueryWrapper<StudentBean> wrapper = new QueryWrapper<>();
        if (studentBean!=null){
            String sname = studentBean.getSname();
            Integer age = studentBean.getAge();
            Integer sage = studentBean.getSage();
            if (sname!=null){
                wrapper.like("sname", studentBean.getSname());
            }
            if (!StringUtils.isEmpty(age)){
                wrapper.gt("age", studentBean.getAge());
            }
            if (!StringUtils.isEmpty(sage)){
                wrapper.lt("age",  studentBean.getSage());
            }
        }
        Page<StudentBean> StudentBean = studentService.page(page,wrapper);
        System.out.println(StudentBean.getRecords());
        return Result.ok(StudentBean);
    }
}
