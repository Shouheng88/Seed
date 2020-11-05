package com.seed.base.utils;

import com.seed.base.model.AbstractPo;
import com.seed.base.model.AbstractVo;

import java.util.Date;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 15:17
 */
public final class ModelUtils {

    public static <T extends AbstractPo> T getPo(Class<T> modelType) {
        T model = null;
        try {
            model = modelType.newInstance();
            model.setLockVersion(0L);
            model.setUpdatedTime(new Date());
            model.setCreatedTime(new Date());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static <T extends AbstractVo> T getVo(Class<T> modelType) {
        T model = null;
        try {
            model = modelType.newInstance();
            model.setLockVersion(0L);
            model.setUpdatedTime(new Date());
            model.setCreatedTime(new Date());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }
}
