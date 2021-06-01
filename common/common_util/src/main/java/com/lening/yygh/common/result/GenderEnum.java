package com.lening.yygh.common.result;


import lombok.Getter;

/**
 * @author 牛胜浩
 * @date 2021/4/26 16:36
 */
@Getter
public enum GenderEnum {

    MEN(1,"男"),
    WOMEN(2,"女"),
    BAOMI(3,"保密");

    private Integer code;
    private String message;

    private GenderEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
