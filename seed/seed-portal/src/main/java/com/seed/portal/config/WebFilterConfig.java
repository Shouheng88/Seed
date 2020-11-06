package com.seed.portal.config;

import com.seed.base.utils.RequestUtils;
import com.seed.base.utils.SeedSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Web filter config, currently, only be used to output log about request.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 14:32
 */
@Slf4j
@Configuration
public class WebFilterConfig {

    @Bean
    public FilterRegistrationBean<ApiFilter> apiScanner() {
        FilterRegistrationBean<ApiFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        ApiFilter filter = new ApiFilter();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("ApiFilter");
        filterRegistrationBean.setOrder(-99999999);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<RestfulApiFilter> restfulApiScanner() {
        FilterRegistrationBean<RestfulApiFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        RestfulApiFilter filter = new RestfulApiFilter();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns("/rest/*");
        filterRegistrationBean.setName("RestfulApiFilter");
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    public static class RestfulApiFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) { /* noop */ }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                             FilterChain filterChain) throws IOException, ServletException {
            long start = System.currentTimeMillis();
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            filterChain.doFilter(servletRequest, servletResponse);
            String reqUrl = RequestUtils.getMethodUrl(request);
            // Example: [POST][/rest/xxx][200][200ms]
            log.info("[{}][{}][{}][{}ms]", request.getMethod(), reqUrl, response.getStatus(), System.currentTimeMillis()-start);
        }

        @Override
        public void destroy() { /* noop */ }
    }

    public static class ApiFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) { /* noop */ }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                             FilterChain filterChain) throws IOException, ServletException {
            long start = System.currentTimeMillis();
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String reqUrl = RequestUtils.getMethodUrl(request);
            SeedSecurity.get().saveRequest(request.getMethod(), reqUrl);
            filterChain.doFilter(servletRequest, servletResponse);
            if (!reqUrl.startsWith("/rest/")) {
                // Example: [POST][/rest/xxx][200][200ms]
                log.info("[{}][{}][{}][{}ms]", request.getMethod(), reqUrl, response.getStatus(), System.currentTimeMillis()-start);
            }
        }

        @Override
        public void destroy() { /* noop */ }
    }
}
