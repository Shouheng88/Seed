package com.seed.portal.controller;

import com.seed.base.model.business.BusinessResponse;
import com.seed.base.model.enums.ResultCode;
import com.seed.base.utils.RequestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Global exception handler.
 *
 * @see com.seed.portal.config.ExceptionConfig
 * @see com.seed.portal.config.RestfulApiAspect#around(ProceedingJoinPoint)
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 18:05
 */
@Slf4j
@Controller
@Api(hidden = true, tags = "Exception handler")
public class ExceptionHandler implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @ResponseBody
    @RequestMapping(value = {"/error"})
    @ApiOperation(value = "Custom error direct page", hidden = true)
    public Object error(HttpServletRequest req, Exception e) {
        String reqUrl = RequestUtils.getMethodUrl(req);
        // [Err][POST][/xxx]Due:xxx
        log.error("[Err][{}][{}]Due:", req.getMethod(), reqUrl, e);
        if (reqUrl.startsWith("/rest")) {
            return BusinessResponse.fail(ResultCode.BAD_REQUEST);
        } else {
            return "error";
        }
    }

    @GetMapping(value = {"/error"})
    @ApiOperation(value = "GET exception handler", hidden = true)
    public Object errorPage(HttpServletRequest req, Exception e) {
        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String reqUrl = RequestUtils.getMethodUrl(req);
        log.error("[Err][{}][{}][{}]Due:", req.getMethod(), reqUrl, statusCode, e);
        if (statusCode != null) {
            switch (statusCode) {
                case 404:
                    return "404";
                case 500:
                    return "500";
                default:
                    return "error";
            }
        }
        return "error";
    }

}
