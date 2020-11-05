package com.seed.base.utils;

import com.seed.base.annotation.HideLog;
import com.seed.base.exception.DAOException;
import com.seed.base.exception.ParameterException;
import com.seed.base.model.PackVo;
import com.seed.base.model.enums.ResultCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Handle exception, wrap exception.
     *
     * @param ex the exception
     * @return   the exception pack.
     */
    private static PackVo handleException(Throwable ex) {
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
