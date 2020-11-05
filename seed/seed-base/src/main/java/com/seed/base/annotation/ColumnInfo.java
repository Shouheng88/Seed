package com.seed.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to add more information for column defined
 * by {@link javax.persistence.Column}.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 11:33
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnInfo {

    /** Add comment information for given column, */
    String comment() default "";

    /**
     * Used to specify if given column was newly added one, if the value
     * was true, the generator will generate SQL used to add column to table.
     */
    boolean added() default false;
}
