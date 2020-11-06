package com.seed.base.annotation;

import java.lang.annotation.*;

/**
 * Api page configuration
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 12:02
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface Paging {

    /** Is page enabled */
    boolean enable() default true;

    /** Default page index */
    int defaultPageIndex() default 0;

    /** Default page size */
    int defaultPageSize() default 20;

    /** Max page size */
    int maxPageSize() default 50;
}
