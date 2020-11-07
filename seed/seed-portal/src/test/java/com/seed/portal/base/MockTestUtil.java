package com.seed.portal.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Utils used to mock data.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 20:17
 */
public final class MockTestUtil {

    private static double curD = 100.1;

    private static float curF = 10000.2f;

    private static int curInt = 9000005;

    private static long curL = 1000;

    private static int curS = 1;

    private static String[] rs = new String[] { "A", "C", "D", "E", "F", "J", "H", "I", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "c", "d", "e", "f", "j", "h", "i", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "f" };

    private static String getFieldName(String methodName) {
        String s = "set";
        String ups = methodName.toLowerCase();
        String key = null;
        if (ups.contains(s)) {
            key = ups.substring(s.length());
        }
        return key;
    }

    public static <T> T getJavaBean(Class<T> c) {
        return getJavaBean(c, true);
    }

    public static <T> T getJavaBean(Class<T> c, boolean ignoreId) {
        T object = null;
        List<Method> allMethods = new ArrayList<>();
        List<Field> allFields = new ArrayList<>();
        try {
            object = c.newInstance();
            Method[] methods = c.getDeclaredMethods();
            Field[] fields = c.getDeclaredFields();
            allMethods.addAll(Arrays.asList(methods));
            allFields.addAll(Arrays.asList(fields));
            Class superClass = c.getSuperclass();
            while (superClass != null) {
                allMethods.addAll(Arrays.asList(superClass.getDeclaredMethods()));
                allFields.addAll(Arrays.asList(superClass.getDeclaredFields()));
                superClass = superClass.getSuperclass();
            }
            for (Method m : allMethods) {
                if (m.getName().contains("set")) {
                    String field = getFieldName(m.getName());
                    // Ignore id
                    if (ignoreId && field.equals("id")) {
                        continue;
                    }
                    // Handle other fields
                    m.invoke(object, getValue(field, allFields));
                }
            }

        } catch (Exception e) {
             e.printStackTrace();
        }
        return object;
    }

    private static String getRand(int size) {
        Random random = new Random();
        StringBuilder rvs = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int status = random.nextInt(size);
            if (status < rs.length && status > 0) {
                rvs.append(rs[status]);
            } else {
                rvs.append("A");
            }
        }
        return rvs.toString();
    }

    private static Object getValue(String field, List<Field> allFields) {
        for (Field f : allFields) {
            if (f.getName().toLowerCase().equals(field)) {
                if (Enum.class.isAssignableFrom(f.getType())) {
                    try {
                        Object[] values = (Object[]) f.getType().getDeclaredMethod("values").invoke(null);
                        return values[new Random().nextInt(values.length)];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return getValueOfType(f.getType().getSimpleName());
                }
            }
        }
        return null;
    }

    private static Object getValueOfType(String type) {
        Object temp = null;
        String st = type.toLowerCase();
        Random random = new Random(10010);
        switch (st) {
            case "int":
            case "integer":
                temp = curInt;
                curInt++;
                break;
            case "long":
                temp = curL;
                curL++;
                break;
            case "string":
                temp = curS + getRand(6);
                curS++;
                break;
            case "double":
                temp = curD;
                curD++;
                break;
            case "float":
                temp = curF;
                curF++;
                break;
            case "boolean":
                temp = random.nextBoolean();
                break;
            case "date":
                temp = new Date();
                break;
        }
        return (temp);
    }
}
