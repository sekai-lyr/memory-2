package com.youkeda.application.ebusiness.control;

import com.youkeda.application.ebusiness.model.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        Result<?> result = new Result<>();
        result.setSuccess(false);
        result.setCode("500");
        // 把真实错误信息返回给前端
        result.setMessage("系统异常：" + e.getMessage());
        return result;
    }
}