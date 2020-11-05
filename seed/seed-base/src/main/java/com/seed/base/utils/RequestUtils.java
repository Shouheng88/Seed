package com.seed.base.utils;

import com.seed.base.model.business.BusinessResponse;
import com.seed.base.model.enums.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

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
}
