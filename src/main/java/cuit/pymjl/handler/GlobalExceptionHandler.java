package cuit.pymjl.handler;

import cuit.pymjl.exception.AppException;
import cuit.pymjl.result.Result;
import cuit.pymjl.result.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/29 11:07
 **/
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<String> error(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResultUtil.fail();
    }

    @ExceptionHandler(AppException.class)
    public Result<String> error(AppException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResultUtil.fail(e.getMessage());
    }
}
