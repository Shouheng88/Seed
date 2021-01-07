package com.seed.base.model.business;

import java.io.Serializable;

/**
 * The restful request object wrapper.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:36
 */
public class BusinessRequest<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Request id used to specify given request. */
    private String requestId;

    /** User id */
    private Long userId;

    /** User token. */
    private String token;

    /** Encrypt request key. */
    private String key;

    /** The App id */
    private Long appId;

    /** The client information. */
    private ClientInfo clientInfo;

    /** App request parameters. */
    private T apiParams;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public T getApiParams() {
        return apiParams;
    }

    public void setApiParams(T apiParams) {
        this.apiParams = apiParams;
    }

    @Override
    public String toString() {
        return "BusinessRequest{" +
                "requestId='" + requestId + '\'' +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", key='" + key + '\'' +
                ", appId=" + appId +
                ", clientInfo=" + clientInfo +
                ", apiParams=" + apiParams +
                '}';
    }
}
