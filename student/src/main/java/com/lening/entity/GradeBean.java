package com.lening.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 牛胜浩
 * @date 2021/5/31 9:21
 */
@Data
@TableName("tb_grade")
@ApiModel(description = "班级")
public class GradeBean implements Serializable {

    @ApiModelProperty(value = "gid")
    @TableId(type = IdType.AUTO)
    @TableField("gid")
    private Long gid;

    @ApiModelProperty(value = "年龄")
    @TableField("gname")
    private String gname;
}
