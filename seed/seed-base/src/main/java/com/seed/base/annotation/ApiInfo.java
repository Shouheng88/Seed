package com.seed.base.annotation;

import java.lang.annotation.*;

/**
 * Restful api configuration.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 12:13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface ApiInfo {

    /** Does given api require authentication */
    boolean auth() default true;

    /** Does given api require administrator authorization */
    boolean admin() default true;

    /** Paging configuration for given api */
    Paging paging() default @Paging(enable = false);

    /** Limiting configuration for given api */
    Limiting limiting() default @Limiting(enable = false);
}
