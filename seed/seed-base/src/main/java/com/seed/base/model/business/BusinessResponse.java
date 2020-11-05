package com.seed.base.model.business;

import com.seed.base.model.enums.ResultCode;

import java.io.Serializable;
import java.util.Date;

/**
 * The restful response object wrapper.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:46
 */
public class BusinessResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Was the response succeed. */
    public final boolean success;

    /** Response data. */
    public final T data;

    /** Response code. */
    public final String code;

    /** Message for response. */
    public final String message;

    /** Timestamp of server. */
    public final Date timestamp;

    /** Reserved filed. */
    public final Long udf1;

    /** Reserved filed. */
    public final Double udf2;

    /** Reserved filed. */
    public final Boolean udf3;

    /** Reserved filed. */
    public final String udf4;

    /** Reserved filed. */
    public final Object udf5;

    public BusinessResponse(boolean success, T data, String code, String message, Date timestamp,
                            Long udf1, Double udf2, Boolean udf3, String udf4, Object udf5) {
        this.success = success;
        this.data = data;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.udf1 = udf1;
        this.udf2 = udf2;
        this.udf3 = udf3;
        this.udf4 = udf4;
        this.udf5 = udf5;
    }

    /*============================================= static success =============================================*/

    public static <T> BusinessResponse<T> success(T data) {
        return new BusinessResponse<>(true, data, null, null, new Date(),null, null, null, null, null);
    }

    public static <T> BusinessResponse<T> success(T data, long udf1) {
        return new BusinessResponse<>(true, data, null, null, new Date(), udf1, null, null, null, null);
    }

    public static <T> BusinessResponse<T> success(T data, double udf2) {
        return new BusinessResponse<>(true, data, null, null, new Date(), null, udf2, null, null, null);
    }

    public static <T> BusinessResponse<T> success(T data, Boolean udf3) {
        return new BusinessResponse<>(true, data, null, null, new Date(), null, null, udf3, null, null);
    }

    public static <T> BusinessResponse<T> success(T data, String udf4) {
        return new BusinessResponse<>(true, data, null, null, new Date(), null, null, null, udf4, null);
    }

    public static <T> BusinessResponse<T> success(T data, Object udf5) {
        return new BusinessResponse<>(true, data, null, null, new Date(), null, null, null, null, udf5);
    }

    public static <T> BusinessResponse<T> success(T data, Long udf1, Double udf2, Boolean udf3, String udf4, Object udf5) {
        return new BusinessResponse<>(true, data, null, null, new Date(), udf1, udf2, udf3, udf4, udf5);
    }

    /*============================================= static fail =============================================*/

    public static <T> BusinessResponse<T> fail() {
        return fail(ResultCode.FAILED);
    }

    public static <T> BusinessResponse<T> fail(ResultCode resultCode) {
        return fail(String.valueOf(resultCode.code), resultCode.message);
    }

    public static <T> BusinessResponse<T> fail(String code, String message) {
        return fail(code, message, null);
    }

    public static <T> BusinessResponse<T> fail(String code, String message, long udf1) {
        return new BusinessResponse<>(false, null, code, message, new Date(), udf1, null, null, null, null);
    }

    public static <T> BusinessResponse<T> fail(String code, String message, double udf2) {
        return new BusinessResponse<>(false, null, code, message, new Date(), null, udf2, null, null, null);
    }

    public static <T> BusinessResponse<T> fail(String code, String message, boolean udf3) {
        return new BusinessResponse<>(false, null, code, message, new Date(), null, null, udf3, null, null);
    }

    public static <T> BusinessResponse<T> fail(String code, String message, String udf4) {
        return new BusinessResponse<>(false, null, code, message, new Date(), null, null, null, udf4, null);
    }

    public static <T> BusinessResponse<T> fail(String code, String message, Object udf5) {
        return new BusinessResponse<>(false, null, code, message, new Date(), null, null, null, null, udf5);
    }

    public static <T> BusinessResponse<T> fail(String code, String message, Long udf1, Double udf2, Boolean udf3, String udf4, Object udf5) {
        return new BusinessResponse<>(false, null, code, message, new Date(), udf1, udf2, udf3, udf4, udf5);
    }

    @Override
    public String toString() {
        return "BusinessResponse{" +
                "success=" + success +
                ", data=" + data +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", udf1=" + udf1 +
                ", udf2=" + udf2 +
                ", udf3=" + udf3 +
                ", udf4='" + udf4 + '\'' +
                ", udf5=" + udf5 +
                '}';
    }
}
