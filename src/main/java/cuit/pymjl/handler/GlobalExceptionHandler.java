package cuit.pymjl.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.result.Result;
import cuit.pymjl.result.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> error(ConstraintViolationException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder res = new StringBuilder("参数异常: ");
        constraintViolations.forEach(c -> res.append(c.getMessage()).append(" "));
        return ResultUtil.fail(res.toString().trim());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public Result<String> argumentError(Exception e) {
        e.printStackTrace();
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        }
        StringBuilder msg = new StringBuilder();
        assert bindingResult != null;
        bindingResult.getFieldErrors().forEach((fieldError) ->
                msg.append(fieldError.getDefaultMessage()).append(" ")
        );
        log.error(msg);
        return ResultUtil.fail(msg.toString().trim());
    }
}
