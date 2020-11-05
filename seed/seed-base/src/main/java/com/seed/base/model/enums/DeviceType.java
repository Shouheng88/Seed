package com.seed.base.model.enums;

import org.springframework.lang.Nullable;

/**
 * Device of platform enum
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 11:24
 */
public enum DeviceType {
    /** Default unknown device type */
    UNKNOWN(    0),
    /** Android */
    ANDROID(    1),
    /** iOS */
    iOS(        2),
    /** Front end */
    FE(         3),
    /** WeChat little program */
    WX(         4);

    public final int id;

    DeviceType(int id) {
        this.id = id;
    }

    @Nullable
    public static DeviceType getTypeById(int id) {
        for (DeviceType deviceType : values()) {
            if (deviceType.id == id) {
                return deviceType;
            }
        }
        return null;
    }
}
