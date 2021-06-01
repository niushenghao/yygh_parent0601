package com.lening.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 牛胜浩
 * @date 2021/5/31 10:35
 */
@Data
public class StudentQueryVo {

    @ApiModelProperty(value = "学生姓名")
    private String sname;

    @ApiModelProperty(value = "小年龄")
    private Integer age;

    @ApiModelProperty(value = "大年龄")
    private Integer sage;
}
