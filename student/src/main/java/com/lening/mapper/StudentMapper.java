package com.lening.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lening.entity.StudentBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 牛胜浩
 * @date 2021/5/31 9:23
 */
@Mapper
public interface StudentMapper extends BaseMapper<StudentBean> {
}
