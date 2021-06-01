package com.lening.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 牛胜浩
 * @date 2021/5/31 9:14
 */
@Data
@TableName("tb_student")
@ApiModel(description = "学生")
public class StudentBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "sid")
    @TableId(type = IdType.AUTO)
    @TableField("sid")
    private Long sid;

    @ApiModelProperty(value = "年龄")
    @TableField("sname")
    private String sname;

    @ApiModelProperty(value = "年龄")
    @TableField("age")
    private Integer age;

    @ApiModelProperty(value = "生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("birthday")
    private Date birthday;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
