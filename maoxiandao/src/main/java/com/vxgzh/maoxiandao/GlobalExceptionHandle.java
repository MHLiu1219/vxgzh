package com.vxgzh.maoxiandao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandle
 *
 * @Author: lmh
 * @CreateTime: 2020-09-16
 * @Description:
 */
@RestControllerAdvice
public class GlobalExceptionHandle {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(value =Exception.class)
    public String exceptionHandler(Exception e){
        logger.error("未知错误",e);
        return "服务错误";
    }
}