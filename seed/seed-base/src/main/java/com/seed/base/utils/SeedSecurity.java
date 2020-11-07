package com.seed.base.utils;

import com.seed.base.model.business.BusinessRequest;

/**
 * This class was used to save and retrieve request information.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 14:39
 */
public final class SeedSecurity {

    private static final ThreadLocal<CachedSession> SESSIONS = new ThreadLocal<>();

    private static final ThreadLocal<CachedRequest> REQUESTS = new ThreadLocal<>();

    private static class Holder {
        private static SeedSecurity seedSecurity = new SeedSecurity();
    }

    public static SeedSecurity get() {
        return Holder.seedSecurity;
    }

    private SeedSecurity() { }

    /** Save request info */
    public void saveRequest(String method, String url) {
        REQUESTS.set(new CachedRequest(method, url));
    }

    /** Retrieve cached request */
    public CachedRequest getRequest() {
        return REQUESTS.get();
    }

    /** Remove cached request. */
    public void removeRequest() {
        REQUESTS.remove();
    }

    /** Save request session */
    public void saveSession(BusinessRequest request) {
        SESSIONS.set(new CachedSession(request));
    }

    /** Get cached session */
    public CachedSession getSession() {
        return SESSIONS.get();
    }

    /** Remove session */
    public void removeSession() {
        SESSIONS.remove();
    }

    /** Save get user id */
    public Long getUserId() {
        if (SESSIONS.get() == null || SESSIONS.get().businessRequest == null) return null;
        return SESSIONS.get().businessRequest.getUserId();
    }

    /** Save get request id */
    public String getRequestId() {
        if (SESSIONS.get() == null || SESSIONS.get().businessRequest == null) return null;
        return SESSIONS.get().businessRequest.getRequestId();
    }

    public static class CachedSession {
        public final BusinessRequest businessRequest;

        CachedSession(BusinessRequest businessRequest) {
            this.businessRequest = businessRequest;
        }
    }

    public static class CachedRequest {
        public final String method;
        public final String url;

        CachedRequest(String method, String url) {
            this.method = method;
            this.url = url;
        }
    }
}
