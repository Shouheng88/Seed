package com.seed.base.model.enums;

/**
 * Common result code, you can also add your own custom result code here.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 11:29
 */
public enum ResultCode {
    SUCCESS(           200, "succeed"),
    FAILED(            500, "failed"),
    BAD_REQUEST(       400, "bad request"),
    UNAUTHORIZED(      401, "not login or session expired"),
    FORBIDDEN(         403, "forbidden"),
    METHOD_NOT_ALLOWED(405, "method not allowed"),

    ERROR_DAO_EXCEPTION(            20001, "failed to operate database"),
    ERROR_REQUEST_PARAMETER(        20002, "parameter check failed");

    public final long code;

    public final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
