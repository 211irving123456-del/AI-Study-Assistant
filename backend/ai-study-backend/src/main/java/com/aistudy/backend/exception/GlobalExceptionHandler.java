package com.aistudy.backend.exception;


import com.aistudy.backend.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;



@RestControllerAdvice
public class GlobalExceptionHandler {



    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(
            BusinessException e
    ){


        return Result.error(
                400,
                e.getMessage()
        );

    }




    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(
            MethodArgumentNotValidException e
    ){


        String message =
                e.getBindingResult()
                 .getFieldErrors()
                 .get(0)
                 .getDefaultMessage();



        return Result.error(
                400,
                message
        );

    }




    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(
            Exception e
    ){


        e.printStackTrace();


        return Result.error(
                500,
                "服务器异常"
        );

    }



}