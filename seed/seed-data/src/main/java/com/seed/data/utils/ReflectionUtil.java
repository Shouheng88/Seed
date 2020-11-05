package com.seed.data.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 22:13
 */
public final class ReflectionUtil {

    private static Map<Class, List<Field>> fieldMap;

    public static List<Field> getAllFields(Object obj) {
        return getAllFields(obj.getClass());
    }

    public static List<Field> getAllFields(Class<?> clz) {
        return getAllFields(clz, FieldsOrder.PARENT_2_CHILDREN);
    }

    public static List<Field> getAllFields(Class<?> clz, FieldsOrder fieldsOrder) {
        if (fieldMap == null) fieldMap = new ConcurrentHashMap<>();
        List<Field> ret = fieldMap.get(clz);
        if (ret != null) return ret;
        ret = new LinkedList<>();
        List<Class> classes = new LinkedList<>();
        while (clz != null) {
            if (fieldsOrder == FieldsOrder.PARENT_2_CHILDREN) {
                classes.add(0, clz);
            } else {
                classes.add(clz);
            }
            clz = clz.getSuperclass();
        }
        for (Class clazz : classes) {
            for (Field f : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    ret.add(f);
                }
            }
        }
        return ret;
    }

    public static List<Field> getAllFieldsWithAnnotation(Object obj, Class<? extends Annotation> annotationClass) {
        return getAllFields(obj).stream().filter(field -> field.isAnnotationPresent(annotationClass)).collect(Collectors.toList());
    }

    public static boolean hasField(Class<?> cls, String filed) {
        return Arrays.stream(cls.getDeclaredFields()).anyMatch(field -> field.getName().equals(filed));
    }

    public static boolean getBooleanValue(Class<?> cls, String filed) {
        try {
            return cls.getField(filed).getBoolean(cls.newInstance());
        } catch (IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getStringValue(Class<?> cls, String filed) {
        try {
            return (String) cls.getField(filed).get(cls.newInstance());
        } catch (IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copy(Object from, Object to) {
        if (from == null || to == null) return;
        try {
            for (Field f : ReflectionUtil.getAllFields(from)) {
                f.setAccessible(true);
                f.set(to, f.get(from));
            }
        } catch (Exception e) {
            throw new RuntimeException("cannot copy members from " + from + " to " + to);
        }
    }

    public static boolean isPrimitiveType(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == Byte.class
                || clazz == Short.class
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Boolean.class
                || clazz == Float.class
                || clazz == Double.class
                || clazz == String.class
                || clazz == Date.class;
    }

    public enum FieldsOrder {
        CHILDREN_2_PARENT,
        PARENT_2_CHILDREN
    }
}
