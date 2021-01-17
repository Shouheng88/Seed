package com.seed.base.boot;

import com.seed.base.model.enums.IEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2021/1/17 21:20
 */
public class Int2EnumFactory implements ConverterFactory<Integer, IEnum> {

    private static final Map<Class, Converter> converterMap = new HashMap<>();

    @Override
    public <T extends IEnum> Converter<Integer, T> getConverter(Class<T> targetType) {
        Converter result = converterMap.get(targetType);
        if(result == null) {
            result = new IntegerToEnumConverter<>(targetType);
            converterMap.put(targetType, result);
        }
        return result;
    }

    private static class IntegerToEnumConverter<T extends IEnum> implements Converter<Integer, T> {

        /** Map from enum id to enum constant. */
        private Map<Integer, T> enumMap = new HashMap<>();

        IntegerToEnumConverter(Class<T> enumType) {
            T[] enums = enumType.getEnumConstants();
            if (enums != null) {
                for (T e : enums) {
                    enumMap.put(e.getId(), e);
                }
            }
        }

        /**
         * Get enum from source. This will return null if the source doesn't match any enum.
         *
         * @param source the source
         * @return       the enum constant
         */
        @Override
        public T convert(Integer source) {
            return enumMap.get(source);
        }
    }
}
