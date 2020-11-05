package com.seed.base.model;

import com.seed.base.model.business.BusinessResponse;
import com.seed.base.model.enums.ResultCode;

import java.io.Serializable;
import java.util.List;

/**
 * The pack wrapper used to transfer data inside the sever.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:51
 */
public class PackVo<T> implements Serializable {

    private static long serialVersionUID = -1L;

    /** Is succeed. */
    private boolean success = true;

    /** Data of object. */
    private T vo;

    /** Data of list. */
    private List<T> voList;

    /** Yhe code. */
    private String code;

    /** The message. */
    private String message;

    /** Reserved filed. */
    private Long udf1;

    /** Reserved filed. */
    private Double udf2;

    /** Reserved filed. */
    private Boolean udf3;

    /** Reserved filed. */
    private String udf4;

    /** Reserved filed. */
    private Object udf5;

    /*========================================== static methods ================================================*/

    public static <E> PackVo<E> success() {
        return new PackVo<>();
    }

    public static <E> PackVo<E> success(E vo) {
        PackVo<E> packVo = new PackVo<>();
        packVo.setSuccess(true);
        packVo.setVo(vo);
        return packVo;
    }

    public static <E> PackVo<E> success(List<E> voList) {
        PackVo<E> packVo = new PackVo<>();
        packVo.setSuccess(true);
        packVo.setVoList(voList);
        return packVo;
    }

    public static <E> PackVo<E> fail() {
        PackVo<E> packVo = new PackVo<>();
        packVo.setSuccess(false);
        return packVo;
    }

    public static <E> PackVo<E> fail(String code, String message) {
        PackVo<E> packVo = new PackVo<>();
        packVo.setSuccess(false);
        packVo.setCode(code);
        packVo.setMessage(message);
        return packVo;
    }

    public static <E> PackVo<E> fail(ResultCode resultCode) {
        PackVo<E> packVo = new PackVo<>();
        packVo.setSuccess(false);
        packVo.setCode(resultCode.name());
        packVo.setMessage(resultCode.message);
        return packVo;
    }

    public static <T> BusinessResponse<T> toResponse(PackVo<T> packVo) {
        if (packVo.isSuccess()) {
            return BusinessResponse.success(packVo.getVo(), packVo.getUdf1(),
                    packVo.getUdf2(), packVo.getUdf3(), packVo.getUdf4(), packVo.getUdf5());
        } else {
            return BusinessResponse.fail(packVo.getCode(), packVo.getMessage(), packVo.getUdf1(),
                    packVo.getUdf2(), packVo.getUdf3(), packVo.getUdf4(), packVo.getUdf5());
        }
    }

    public static <T> BusinessResponse<List<T>> toListResponse(PackVo<T> packVo) {
        if (packVo.isSuccess()) {
            return BusinessResponse.success(packVo.getVoList(), packVo.getUdf1(),
                    packVo.getUdf2(), packVo.getUdf3(), packVo.getUdf4(), packVo.getUdf5());
        } else {
            return BusinessResponse.fail(packVo.getCode(), packVo.getMessage(), packVo.getUdf1(),
                    packVo.getUdf2(), packVo.getUdf3(), packVo.getUdf4(), packVo.getUdf5());
        }
    }

    public static BusinessResponse toRawResponse(PackVo packVo) {
        if (packVo.isSuccess()) {
            return BusinessResponse.success(packVo.getVo(), packVo.getUdf1(), packVo.getUdf2(),
                    packVo.getUdf3(), packVo.getUdf4(), packVo.getUdf5());
        } else {
            return BusinessResponse.fail(packVo.getCode(), packVo.getMessage(), packVo.getUdf1(),
                    packVo.getUdf2(), packVo.getUdf3(), packVo.getUdf4(), packVo.getUdf5());
        }
    }

    /*========================================== setters & getters ================================================*/

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getVo() {
        return vo;
    }

    public void setVo(T vo) {
        this.vo = vo;
    }

    public List<T> getVoList() {
        return voList;
    }

    public void setVoList(List<T> voList) {
        this.voList = voList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUdf1() {
        return udf1;
    }

    public void setUdf1(Long udf1) {
        this.udf1 = udf1;
    }

    public Double getUdf2() {
        return udf2;
    }

    public void setUdf2(Double udf2) {
        this.udf2 = udf2;
    }

    public Boolean getUdf3() {
        return udf3;
    }

    public void setUdf3(Boolean udf3) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4(String udf4) {
        this.udf4 = udf4;
    }

    public Object getUdf5() {
        return udf5;
    }

    public void setUdf5(Object udf5) {
        this.udf5 = udf5;
    }

    @Override
    public String toString() {
        return "PackVo{" +
                "success=" + success +
                ", vo=" + vo +
                ", voList=" + voList +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", udf1=" + udf1 +
                ", udf2=" + udf2 +
                ", udf3=" + udf3 +
                ", udf4='" + udf4 + '\'' +
                ", udf5=" + udf5 +
                '}';
    }
}
