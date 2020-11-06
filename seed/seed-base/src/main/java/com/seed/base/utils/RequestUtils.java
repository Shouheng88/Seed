package com.seed.base.utils;

import com.seed.base.model.business.BusinessResponse;
import com.seed.base.model.enums.ResultCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 18:15
 */
public final class RequestUtils {

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    /**
     * Get ip address from request.
     *
     * @param request the servlet request
     * @return        the ip address
     */
    public static String getIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // remove unkonwn
                if (ip.startsWith("unknown")) {
                    ip = ip.substring(ip.indexOf("unknown") + "unknown".length());
                }
                // remove useless information
                ip = ip.trim();
                if (ip.startsWith(",")) {
                    ip = ip.substring(1);
                }
                if (ip.indexOf(",") > 0) {
                    ip = ip.substring(0, ip.indexOf(","));
                }
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public static String getMethodUrl(HttpServletRequest servletRequest) {
        return getMethodUrl(servletRequest.getRequestURL().toString());
    }

    public static String getMethodUrl(String url) {
        int portIndex = url.lastIndexOf(':');
        if (portIndex < 0) return url;
        String tempUrl = url.substring(portIndex);
        int partIndex = url.indexOf('/');
        if (partIndex < 0) return tempUrl;
        return tempUrl.substring(partIndex);
    }

    public static boolean writeResponse(HttpServletResponse response, ResultCode resultCode) {
        PrintWriter writer;
        try {
            String json = JsonMapper.nonNullAndDefaultDateFormatMapper().toJson(BusinessResponse.fail(resultCode));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            writer = response.getWriter();
            assert json != null;
            writer.write(json);
            writer.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get request body witch is decorated by {@link RequestBody} from method and its arguments.
     *
     * @param method the method
     * @param args   arguments of method
     * @return       the request body
     */
    public static Object getRequestBody(Method method, Object[] args) {
        if (args == null) return null;
        Annotation[][] array = method.getParameterAnnotations();
        for (int i=0, len=array.length; i<len; i++) {
            Annotation[] annotations = array[i];
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestBody) {
                    return args[i];
                }
            }
        }
        return null;
    }

    public static Params getRequiredParams(Method method) {
        Params params = new Params();
        if (method.isAnnotationPresent(ApiImplicitParams.class)) {
            ApiImplicitParam[] implicitParams = method.getAnnotation(ApiImplicitParams.class).value();
            Stream.of(implicitParams)
                    .filter(ApiImplicitParam::required)
                    .map(param -> {
                        boolean apiParams = param.name().contains("apiParams.");
                        return new Param(apiParams, param.name(),
                                // Where "10" is the length of "apiParams."
                                apiParams ? param.name().substring(10) : param.name(),
                                param.allowEmptyValue());
                    })
                    .forEach(params::add);
        }
        return params;
    }

    public static class Param {
        /** Is filed from {@link com.seed.base.model.business.BusinessRequest#apiParams} */
        public final boolean apiParams;
        /** Full name, for example, "apiParams.xxx", used to output in log. */
        public final String fullName;
        /** Filed name, for example, "xxx" */
        public final String filedName;
        /** Is filed allow empty, defined by {@link ApiImplicitParam#allowEmptyValue()} */
        public final boolean allowEmpty;

        public Param(boolean apiParams, String fullName, String filedName, boolean allowEmpty) {
            this.apiParams = apiParams;
            this.fullName = fullName;
            this.filedName = filedName;
            this.allowEmpty = allowEmpty;
        }
    }

    public static class Params extends LinkedList<Param> {

        public boolean hasApiParams() {
            return stream().anyMatch(param -> param.apiParams);
        }

        /** Parameters of {@link com.seed.base.model.business.BusinessRequest#apiParams} */
        public Stream<Param> apiParams() {
            return stream().filter(param -> param.apiParams);
        }

        /** Parameters of {@link com.seed.base.model.business.BusinessRequest}. */
        public Stream<Param> params() {
            return stream().filter(param -> !param.apiParams);
        }
    }
}
