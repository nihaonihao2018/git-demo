package com.arthur.common.exception;

import com.arthur.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @authur arthur
 * @desc
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result globalError(Exception e){
        e.printStackTrace();
        return Result.fail();
    }
    @ExceptionHandler(YyghException.class)
    public Result yyghException(YyghException e){
        return Result.build(e.getCode(),e.getMessage());
    }
}
