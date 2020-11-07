package com.seed.portal.config;

import cn.hutool.core.util.StrUtil;
import com.seed.base.annotation.ApiInfo;
import com.seed.base.annotation.Limiting;
import com.seed.base.model.enums.ResultCode;
import com.seed.base.utils.RequestUtils;
import com.seed.data.manager.LimitingManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 11:52
 */
@Slf4j
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /** This is the switch for interceptor, you can disable it in develop environment. */
    @Value(value = "${com.seed.intercept.enable}")
    private Boolean interceptorEnable;

    private LimitingManager limitingManager;

    @Autowired
    public InterceptorConfig(LimitingManager limitingManager) {
        this.limitingManager = limitingManager;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (interceptorEnable == null || interceptorEnable) {
            registry.addInterceptor(new ApiInterceptor(limitingManager)).addPathPatterns("/**");
            registry.addInterceptor(new RestApiInterceptor(limitingManager)).addPathPatterns("/rest/**");
        }
    }

    private static class ApiInterceptor implements HandlerInterceptor {

        private LimitingManager limitingManager;

        ApiInterceptor(LimitingManager limitingManager) {
            this.limitingManager = limitingManager;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return normalRequestLimit(request);
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) { /* noop */ }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) { /* noop */ }

        private boolean normalRequestLimit(HttpServletRequest request) {
            String ip = RequestUtils.getIpAddress(request);
            String url = RequestUtils.getMethodUrl(request);
            if (StrUtil.isEmpty(ip) || StrUtil.isEmpty(url)) {
                log.error("[Limit][Invalid][{} to {}].", ip, url);
                return false;
            }
            // Increase visit count and check limiting
            return limitingManager.increaseNormalVisit(ip, url);
        }
    }

    private static class RestApiInterceptor implements HandlerInterceptor {

        private LimitingManager limitingManager;

        RestApiInterceptor(LimitingManager limitingManager) {
            this.limitingManager = limitingManager;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                return checkRequestLimiting(request, response, handlerMethod);
            }
            // Do not intercept and let it go
            return true;
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) { /* noop */ }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) { /* noop */ }

        private boolean checkRequestLimiting(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
            ApiInfo apiInfo = handlerMethod.getMethodAnnotation(ApiInfo.class);
            Limiting limiting = apiInfo != null ? apiInfo.limiting() : null;
            Integer limit = limiting != null && limiting.enable() ? limiting.limit() : null;
            String ip = RequestUtils.getIpAddress(request);
            String url = RequestUtils.getMethodUrl(request);

            boolean shouldProceed = checkAndRecordVisitCount(ip, url, limit, limiting);

            if (!shouldProceed) {
                log.warn("[Limit][{}][{}]", request.getMethod(), url);
                boolean succeed = RequestUtils.writeResponse(response, ResultCode.REQUEST_LIMIT_LIMIT_COUNT);
                if (!succeed) log.error("Error while writing the response.");
                return false;
            }
            return true;
        }

        private boolean checkAndRecordVisitCount(String ip, String url, Integer limit, Limiting limiting) {
            if (StrUtil.isEmpty(ip) || StrUtil.isEmpty(url)) {
                log.error("Failed to record visit count from ip [{}] to [{}].", ip, url);
                return false;
            }
            if (limit == null || limiting == null) return true;
            return limitingManager.increaseIpToUrlVisitCount(ip, url, limiting);
        }
    }

}
