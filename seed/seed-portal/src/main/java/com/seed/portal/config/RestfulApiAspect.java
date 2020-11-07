package com.seed.portal.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.seed.base.annotation.ApiInfo;
import com.seed.base.annotation.Paging;
import com.seed.base.model.PackVo;
import com.seed.base.model.business.BusinessRequest;
import com.seed.base.model.business.BusinessResponse;
import com.seed.base.model.enums.ResultCode;
import com.seed.base.model.query.Pageable;
import com.seed.base.utils.*;
import com.seed.portal.service.CheckService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Restful api aspect configuration.
 *
 * For exception handler, see also,
 *
 * @see ExceptionConfig
 * @see com.seed.portal.controller.ExceptionHandler
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 15:13
 */
@Aspect
@Component
public class RestfulApiAspect {

    @Autowired
    private SeedSecurity security;

    @Autowired
    private CheckService checkService;

    /** Only handle restful api */
    @Pointcut("within(com.seed.portal.controller.rest..*)")
    public void controllerPointCut() { }

    @Around(value = "controllerPointCut()")
    public Object around(ProceedingJoinPoint point) {
        long timeBegin = System.currentTimeMillis();

        Class targetClass = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        Logger logger = LoggerFactory.getLogger(targetClass);

        // Handle api log output, output request info and hide long fields.
        Object result = null;
        Object body = RequestUtils.getRequestBody(method, point.getArgs());
        JsonMapper jsonMapper = getJsonMapper(AopUtils.fieldsFilter(body));
        String bodyJson = jsonMapper.toJson(body);
        logger.info("[Request][{}][{}][{}]", security.getRequest().method, security.getRequest().url, bodyJson);

        // Check authentication.
        if (!checkCertificateAvailable(point, method)) {
            if (method.getReturnType() == WebAsyncTask.class) {
                result = new WebAsyncTask<>(() -> BusinessResponse.fail(ResultCode.UNAUTHORIZED));
            } else {
                result = BusinessResponse.fail(ResultCode.UNAUTHORIZED);
            }
            logger.error("[#{}][Error][{}ms][UNAUTHORIZED][{}]", signature.getName(),
                    System.currentTimeMillis()-timeBegin, result);
            return result;
        }

        // Check request parameters.
        BusinessResponse response = checkParameters(method, point.getArgs(), logger);
        if (response != null) {
            logger.error("[#{}][Error][{}ms][PARAMETER][{}][{}][{}]", signature.getName(),
                    System.currentTimeMillis()-timeBegin, security.getUserId(), security.getRequestId(), response);
            return response;
        }

        // Execute method of controller
        List<Object> args = AopUtils.argumentFilter(point.getArgs(), method.getParameterAnnotations());
        logger.info("[#{}][Start][{}][{}][{}]", signature.getName(), security.getUserId(), security.getRequestId(), args);
        try {
            result = point.proceed();
        } catch (Throwable ex) {
            logger.error("[#{}][Error][{}ms][{}][{}]Due:", signature.getName(),
                    System.currentTimeMillis()-timeBegin, security.getUserId(), security.getRequestId(), ex);
            result = AopUtils.handleException(ex);
        } finally {
            logger.info("[#{}][End][{}ms][{}][{}][{}]", signature.getName(),
                    System.currentTimeMillis()-timeBegin, security.getUserId(), security.getRequestId(), result);
            security.removeSession();
        }

        return result;
    }

    private JsonMapper getJsonMapper(List<String> fields) {
        JsonMapper jsonMapper = JsonMapper.nonNullMapper();
        if (!fields.isEmpty()) {
            // filter out given fields
            SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept(fields.toArray(new String[0]));
            FilterProvider filters = new SimpleFilterProvider().addFilter("logFilter", theFilter);
            jsonMapper.getMapper().setFilterProvider(filters);
        }
        return jsonMapper;
    }

    private boolean checkCertificateAvailable(ProceedingJoinPoint point, Method method) {
        boolean needAuth = !method.isAnnotationPresent(ApiInfo.class) || method.getAnnotation(ApiInfo.class).auth();
        if (needAuth) {
            return Stream.of(point.getArgs()).anyMatch(arg -> {
                if (arg instanceof BusinessRequest) {
                    // Check certificate and save request information.
                    PackVo<Boolean> packVo = checkService.checkCertificate((BusinessRequest) arg);
                    boolean valid = packVo.isSuccess() && packVo.getVo();
                    if (valid) security.saveSession((BusinessRequest) arg);
                    return valid;
                }
                return false;
            });
        } else {
            Stream.of(point.getArgs()).forEach(arg -> {
                // Save the business request anyway.
                if (arg instanceof BusinessRequest) security.saveSession((BusinessRequest) arg);
            });
            return true;
        }
    }

    private BusinessResponse checkParameters(Method method, Object[] args, Logger logger) {
        if (method.getParameterCount() == 0) return null;

        RequestUtils.Params params = RequestUtils.getRequiredParams(method);

        // Illegal State
        if (!params.isEmpty() && args == null) return BusinessResponse.fail(ResultCode.ERROR_REQUEST_PARAMETER);

        List<BusinessRequest> requests = Arrays.stream(args)
                .filter(o -> o instanceof BusinessRequest)
                .map(o -> (BusinessRequest) o).collect(Collectors.toList());
        BusinessRequest request = requests.size() > 0 ? requests.get(0) : null;

        if (!params.isEmpty() && request != null) {
            List<RequestUtils.Param> missedParams = new LinkedList<>();
            // Check parameters of BusinessRequest
            boolean passed = params.params().allMatch(param -> checkParam(method, param, request, missedParams, logger));
            if (!passed) {
                String result = StringUtils.connect(missedParams, ",", param -> param.fullName);
                return BusinessResponse.fail(String.valueOf(ResultCode.ERROR_REQUEST_PARAMETER.code), "Missed Parameters : " + result);
            }
            // Check parameters of BusinessRequest#apiParams
            Object apiParams = request.getApiParams();
            if (params.hasApiParams() && apiParams == null) {
                return BusinessResponse.fail(String.valueOf(ResultCode.ERROR_REQUEST_PARAMETER.code), "Missed Parameters : apiParams");
            }
            if (apiParams != null) {
                Map<String, Field> fieldMap = getAllFields(apiParams);
                passed = params.apiParams().allMatch(param -> checkParam(
                        method, param, apiParams, fieldMap, missedParams, logger));
            }
            // Handle paging
            setupPaging(method, args);
            if (passed) return null;
            String result = StringUtils.connect(missedParams, ",", requiredParam1 -> requiredParam1.fullName);
            return BusinessResponse.fail(String.valueOf(ResultCode.ERROR_REQUEST_PARAMETER.code), "Missed Parameters : " + result);
        } else if (!params.isEmpty()) {
            // Parameter required, but the argument of method was not BusinessRequest.
            logger.error("[#{}][Error][The api might have define error]", method.getName());
        } else {
            // Handle paging
            setupPaging(method, args);
        }
        return null;
    }

    private boolean checkParam(Method method, RequestUtils.Param param,
                               Object o, List<RequestUtils.Param> missedParams, Logger logger) {
        try {
            Field field = o.getClass().getDeclaredField(param.filedName);
            if (!field.isAccessible()) field.setAccessible(true);
            Object value = field.get(o);
            boolean hasValue;
            if (!param.allowEmpty && value instanceof String) {
                hasValue = !StrUtil.isEmpty(StrUtil.trim((CharSequence) value));
            } else {
                hasValue = value != null;
            }
            if (!hasValue) {
                missedParams.add(param);
                logger.info("[#{}][Error][Miss params: {}]", method.getName(), param.filedName);
            }
            return hasValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("[#{}][Error][No field: {}]", method.getName(), param.filedName);
        }
        return false;
    }

    private boolean checkParam(Method method, RequestUtils.Param param, Object o,
                               Map<String, Field> fieldMap, List<RequestUtils.Param> missedParams, Logger logger) {
        try {
            Field field = fieldMap.get(param.filedName);
            if (field == null) return false;
            if (!field.isAccessible()) field.setAccessible(true);
            Object value = field.get(o);
            boolean hasValue;
            if (!param.allowEmpty && value instanceof String) {
                hasValue = !StrUtil.isEmpty(StrUtil.trim((CharSequence) value));
            } else {
                hasValue = value != null;
            }
            if (!hasValue) {
                missedParams.add(param);
                logger.info("[#{}][Error][Miss params: {}]", method.getName(), param.filedName);
            }
            return hasValue;
        } catch (IllegalAccessException e) {
            logger.error("[#{}][Error][No field: {}]", method.getName(), param.filedName);
        }
        return false;
    }

    private Map<String, Field> getAllFields(Object o) {
        List<Field> fields = ReflectionUtil.getAllFields(o.getClass());
        Map<String, Field> fieldMap = new HashMap<>(8);
        fields.forEach(field -> fieldMap.put(field.getName(), field));
        return fieldMap;
    }

    private void setupPaging(Method method, Object[] args) {
        ApiInfo apiInfo;
        Paging paging;
        if (method.isAnnotationPresent(ApiInfo.class)
                && (apiInfo = method.getAnnotation(ApiInfo.class)) != null
                && (paging = apiInfo.paging()).enable()
                && args.length > 0
                && args[0] instanceof BusinessRequest) {
            BusinessRequest request = (BusinessRequest) args[0];
            Object apiParams = request.getApiParams();
            if (apiParams instanceof Pageable) {
                Pageable page = (Pageable) apiParams;
                setupPageableValues(page, paging);
            }
        }
    }

    private void setupPageableValues(Pageable page, Paging paging) {
        page.setPageSize(page.getPageSize() == null ? paging.defaultPageSize()
                : Math.min(paging.maxPageSize(), page.getPageSize()));
        page.setPage(page.getPage() == null ? paging.defaultPageIndex() : page.getPage());
    }

}
