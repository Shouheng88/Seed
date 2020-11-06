package com.seed.portal.config;

import com.seed.base.model.business.BusinessResponse;
import com.seed.base.model.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Global exception handler.
 *
 * @see RestfulApiAspect#around(ProceedingJoinPoint)
 * @see com.seed.portal.controller.ExceptionHandler
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 15:04
 */
@Slf4j
@ControllerAdvice
public class ExceptionConfig {

    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BusinessResponse methodNotAllowedHandler(HttpServletRequest req, Exception e) {
        log.error("Method Not Allowed: {} {}", req.getRequestURL(), req.getMethod(), e);
        return BusinessResponse.fail(ResultCode.METHOD_NOT_ALLOWED);
    }

    @ResponseBody
    @ExceptionHandler(value = ServletException.class)
    public BusinessResponse servletExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("Servlet exception: {} {}", req.getRequestURL(), req.getMethod(), e);
        return BusinessResponse.fail(ResultCode.BAD_REQUEST);
    }

    /**
     * Handle runtime exception.
     *
     * @param req request
     * @param e   the exception
     * @return    the result
     */
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public BusinessResponse servletRuntimeExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("Servlet runtime exception : {} {}", req.getRequestURL(), req.getMethod(), e);
        return BusinessResponse.fail(String.valueOf(ResultCode.BAD_REQUEST.code), ResultCode.BAD_REQUEST.message);
    }
}
