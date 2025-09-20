package com.xiaoxin.iam.common.exception;

import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.common.result.ResultCode;
import com.xiaoxin.iam.common.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常 [{}]: {}", request.getRequestURI(), e.getFullMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 认证异常
     */
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleAuthException(AuthException e, HttpServletRequest request) {
        log.warn("认证异常 [{}]: {}", request.getRequestURI(), e.getFullMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 权限异常
     */
    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handlePermissionException(PermissionException e, HttpServletRequest request) {
        log.warn("权限异常 [{}]: {}", request.getRequestURI(), e.getFullMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 数据异常
     */
    @ExceptionHandler(DataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleDataException(DataException e, HttpServletRequest request) {
        log.warn("数据异常 [{}]: {}", request.getRequestURI(), e.getFullMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(ValidationException e, HttpServletRequest request) {
        log.warn("参数验证异常 [{}]: {}", request.getRequestURI(), e.getFullMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("系统异常 [{}]: {}", request.getRequestURI(), e.getFullMessage(), e);
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常 - @RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("参数校验异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 参数校验异常 - @ModelAttribute
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("参数绑定异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 参数校验异常 - @RequestParam
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.warn("参数约束异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.iterator().next().getMessage();
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("请求参数缺失异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        String message = "请求参数缺失: " + e.getParameterName();
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 请求参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("请求参数类型不匹配异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        String message = "请求参数类型不匹配: " + e.getName() + ", 期望类型: " + e.getRequiredType().getSimpleName();
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * HTTP消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("HTTP消息不可读异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), "请求体格式错误");
    }

    /**
     * HTTP请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("HTTP请求方法不支持异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        String message = "请求方法不支持: " + e.getMethod() + ", 支持的方法: " + String.join(", ", e.getSupportedMethods());
        return Result.failed(ResultCode.METHOD_NOT_ALLOWED.getCode(), message);
    }

    /**
     * 404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("404异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.NOT_FOUND.getCode(), "请求的资源不存在");
    }

    /**
     * 非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("非法参数异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 非法状态异常
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.warn("非法状态异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 数据库访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("数据库访问异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.failed(ResultCode.DATABASE_ERROR.getCode(), "数据库操作失败");
    }

    /**
     * SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("SQL异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.failed(ResultCode.DATABASE_ERROR.getCode(), "数据库操作失败");
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常 [{}]: {}", request.getRequestURI(), ExceptionUtils.getStackTrace(e));
        return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }

    /**
     * 数组越界异常
     */
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e, HttpServletRequest request) {
        log.error("数组越界异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }

    /**
     * 类型转换异常
     */
    @ExceptionHandler(ClassCastException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleClassCastException(ClassCastException e, HttpServletRequest request) {
        log.error("类型转换异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }

    /**
     * 数字格式异常
     */
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleNumberFormatException(NumberFormatException e, HttpServletRequest request) {
        log.warn("数字格式异常 [{}]: {}", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), "数字格式错误");
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }

    /**
     * 错误异常
     */
    @ExceptionHandler(Error.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleError(Error e, HttpServletRequest request) {
        log.error("系统错误 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }
}
