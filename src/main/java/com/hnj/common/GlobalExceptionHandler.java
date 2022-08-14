package com.hnj.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public R<String> exceptionHandler(DuplicateKeyException ex){
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split=ex.getMessage().split(" ");
            String msg=split[2]+"已存在！";
            return R.error(msg);
        }
        return R.error("未知错误!");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public void exceptionHandler(FileNotFoundException ex){
        log.info("文件不存在！");
    }

    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception ex){
        ex.printStackTrace();
        return R.error("系统错误");

    }
}
