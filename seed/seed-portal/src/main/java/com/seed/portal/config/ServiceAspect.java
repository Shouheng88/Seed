package com.seed.portal.config;

import com.seed.base.utils.AopUtils;
import com.seed.base.utils.SeedSecurity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Aspect configuration for services.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 14:57
 */
@Aspect
@Component
public class ServiceAspect {

    @Autowired
    private SeedSecurity security;

    @Pointcut(value = "within(com.seed.portal.service.impl..*)")
    public void servicePointcut() { }

    @Around(value = "servicePointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return AopUtils.methodAround(point, security.getUserId(), security.getRequestId());
    }
}
