package com.seed.base.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Visit limiting configuration.
 *
 * {@link #enable()} was used to config if given annotation was enabled.
 * {@link #limit()}, {@link #timeout()} and {@link #timeUnit()} were used to
 * config visit count. For example, the default configuration means, a visitor
 * can only visit 5 times in one second.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 12:14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface Limiting {

    /** Is limiting enabled */
    boolean enable() default true;

    /** Limit count */
    int limit() default 5;

    /** The time configuration */
    int timeout() default 1;

    /** The time unit */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
