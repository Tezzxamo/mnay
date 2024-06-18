package cn.mnay.controller.global;

import cn.mnay.common.exception.BusinessException;
import cn.mnay.common.model.model.RFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static cn.mnay.common.enums.error.CodeEnum.NULL_POINTER_ERROR;
import static cn.mnay.common.enums.error.CodeEnum.VALIDATION_ERROR;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return RFactory.newError("Required request body is missing!");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return RFactory.newError(e.getMessage());
    }


    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException(BusinessException e) {
        return e.getReturnMsg();
    }

    @ExceptionHandler(NullPointerException.class)
    public Object handleNullPointerException(NullPointerException e) {
        String message = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining(","));
        return RFactory.newResult(NULL_POINTER_ERROR, message);
    }

    /**
     * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public Object BindExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        return RFactory.newResult(VALIDATION_ERROR, message);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Object ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        return RFactory.newResult(VALIDATION_ERROR, message);
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        return RFactory.newResult(VALIDATION_ERROR, message);
    }

    /**
     * 处理异常-兜底处理
     *
     * @param e e
     * @return {@link Object}
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) throws Exception {
        // 抛出AccessDeniedException异常,交由Spring Security处理
        if (e instanceof AccessDeniedException) {
            throw e;
        }
        log.error("未知异常！原因是:", e);
        return RFactory.newError(e.getMessage());
    }
}
