package com.seed.base.model.business;

import com.seed.base.model.enums.DeviceType;

import java.io.Serializable;
import java.util.Date;

/**
 * The client info definition.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:38
 */
public class ClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** APP version etc. */
    private String version;

    /** Device time. */
    private Date time;

    /** Device id. */
    private String id;

    /** Device type of client. */
    private DeviceType type;

    /** User id of client. */
    private Long userId;

    /** Token of client. */
    private String token;

    /** Language of client. */
    private String language;

    /** Timezone of client. */
    private String timezone;

    /** Ip address of client. */
    private String ip;

    /** Details about platform version, Android or iOS version, browser version etc. */
    private String platform;

    /** The network type of client. */
    private String networkType;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "version='" + version + '\'' +
                ", time=" + time +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", language='" + language + '\'' +
                ", timezone='" + timezone + '\'' +
                ", ip='" + ip + '\'' +
                ", platform='" + platform + '\'' +
                ", networkType='" + networkType + '\'' +
                '}';
    }
}
