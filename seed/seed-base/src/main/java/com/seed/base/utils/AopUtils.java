package com.seed.base.utils;

import com.seed.base.annotation.HideLog;
import com.seed.base.exception.DAOException;
import com.seed.base.exception.ParameterException;
import com.seed.base.model.PackVo;
import com.seed.base.model.business.BusinessRequest;
import com.seed.base.model.enums.ResultCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 15:02
 */
public final class AopUtils {

    /**
     * This method is a wrapper, used to display log of services and other methods.
     *
     * @param point       the point
     * @return            the result of point
     * @throws Throwable  the exception raised by point
     */
    public static Object methodAround(ProceedingJoinPoint point) throws Throwable {
        long timeBegin = System.currentTimeMillis();

        Class targetClass = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();

        List<Object> args = argumentFilter(point.getArgs(), signature.getMethod().getParameterAnnotations());

        Logger logger = LoggerFactory.getLogger(targetClass);
        logger.info("[#{}][Start][{}]", signature.getName(), Arrays.toString(args.toArray()));

        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            logger.error("[#{}][Error][{}ms]Due:", signature.getName(), timeCost(timeBegin), throwable);
            if (throwable instanceof RuntimeException || throwable instanceof Error) {
                // exception occurred, throw upwards
                throw throwable;
            } else {
                return handleException(throwable);
            }
        } finally {
            logger.info("[#{}][End][{}ms][{}]", signature.getName(), timeCost(timeBegin), result);
        }

        return result;
    }

    /**
     * Same as {@link #methodAround(ProceedingJoinPoint)} except that we included
     * some parameters just in order to output more useful message to help us
     * find out production problems.
     *
     * @param point      proceeding join point
     * @param userId     user id
     * @param requestId  request id
     * @return           result of point
     * @throws Throwable exception
     */
    public static Object methodAround(ProceedingJoinPoint point, Long userId, String requestId) throws Throwable {
        long timeBegin = System.currentTimeMillis();

        Class targetClass = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();

        List<Object> args = argumentFilter(point.getArgs(), signature.getMethod().getParameterAnnotations());

        Logger logger = LoggerFactory.getLogger(targetClass);
        logger.info("[#{}][Start][{}][{}][{}]", signature.getName(), userId, requestId, Arrays.toString(args.toArray()));

        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            logger.error("[#{}][Error][{}ms][{}][{}]Due:", signature.getName(), timeCost(timeBegin), userId, requestId, throwable);
            if (throwable instanceof RuntimeException || throwable instanceof Error) {
                // exception occurred, throw upwards
                throw throwable;
            } else {
                return handleException(throwable);
            }
        } finally {
            logger.info("[#{}][End][{}ms][{}][{}][{}]", signature.getName(), timeCost(timeBegin), userId, requestId, result);
        }

        return result;
    }

    /**
     * Filter for arguments by annotation. Mainly used to hide log of given argument decorated by {@link HideLog}.
     *
     * @param args        arguments
     * @param annotations annotations
     * @return            filtered arguments.
     */
    public static List<Object> argumentFilter(Object[] args, Annotation[][] annotations) {
        List<Object> ret = new ArrayList<>(args.length);
        ret.addAll(Arrays.asList(args));
        if (annotations != null) {
            for (int i=0, len_i=annotations.length; i<len_i; i++) {
                for (int j=0, len_j=annotations[i].length; j<len_j; j++) {
                    if (annotations[i][j].annotationType() == HideLog.class) {
                        if (i < ret.size()) {
                            ret.remove(i);
                            break;
                        }
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Filter for filed by annotation. Mainly used to hide log of given filed decorated by {@link HideLog}.
     *
     * @param o the target
     * @return  the fields
     */
    public static List<String> fieldsFilter(Object o) {
        if (o == null) return null;
        if (o instanceof BusinessRequest) {
            o = ((BusinessRequest) o).getApiParams();
        }
        List<String> list = new LinkedList<>();
        Field[] fields = o.getClass().getDeclaredFields();
        if (fields != null) {
            list.addAll(Arrays.stream(fields)
                    .filter(field -> field.isAnnotationPresent(HideLog.class))
                    .map(Field::getName)
                    .collect(Collectors.toList()));
        }
        return list;
    }

    /**
     * Handle exception, wrap exception.
     *
     * @param ex the exception
     * @return   the exception pack.
     */
    public static PackVo handleException(Throwable ex) {
        if (ex instanceof DAOException) {
            return PackVo.fail(ResultCode.ERROR_DAO_EXCEPTION);
        } else if (ex instanceof ParameterException) {
            return PackVo.fail(ResultCode.ERROR_REQUEST_PARAMETER);
        }
        return PackVo.fail(ResultCode.FAILED);
    }

    /**
     * Calculate time from given begin time until now.
     *
     * @param beginTime the begin time/
     * @return          the time cost in milliseconds
     */
    private static long timeCost(long beginTime) {
        return System.currentTimeMillis() - beginTime;
    }

    private AopUtils() {
        throw new UnsupportedOperationException("u can't initialize me");
    }
}
