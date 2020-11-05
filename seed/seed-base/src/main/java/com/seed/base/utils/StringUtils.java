package com.seed.base.utils;

import java.util.List;
import java.util.function.Function;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 15:14
 */
public final class StringUtils {

    /**
     * Connect list by connector.
     *
     * @param list      the list
     * @param connector the connector
     * @param <T>       the data type
     * @return          the result
     */
    public static <T> String connect(List<T> list, String connector) {
        return connect(list, connector, Object::toString);
    }

    /**
     * Connect list by connector and function.
     *
     * @param list      the data list
     * @param connector the connector
     * @param function  the function to specify how every element was converted to string
     * @param <T>       the data type
     * @return          the result
     */
    public static <T> String connect(List<T> list, String connector, Function<T, String> function) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i=0, len=list.size(); i<len; i++) {
            if (i != len-1) {
                sb.append(function.apply(list.get(i))).append(connector);
            } else {
                sb.append(function.apply(list.get(i)));
            }
        }
        return sb.toString();
    }
}
