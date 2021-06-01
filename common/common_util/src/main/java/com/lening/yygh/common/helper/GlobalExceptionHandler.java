package com.lening.yygh.common.helper;

import com.lening.yygh.common.exception.YyghException;
import com.lening.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 牛胜浩
 * @date 2021/5/24 17:16
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //要是有异常，自己来处理
    //自己的返回了result，在result中封装一下
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e){
        return Result.build(e.getCode(), e.getMessage());
    }
}
