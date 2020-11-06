package com.seed.data.utils;

import cn.hutool.core.util.StrUtil;
import com.seed.base.annotation.ColumnInfo;
import com.seed.base.annotation.TableInfo;
import com.seed.base.utils.ReflectionUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 21:36
 */
public final class JPAHelper {

    /** Get table name */
    public static String getTableName(Class<?> type) {
        if (!type.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException("can't get table name.");
        return type.getAnnotation(Table.class).name();
    }

    /** Get table comment */
    public static String getTableComment(Class<?> type) {
        if (!type.isAnnotationPresent(TableInfo.class)) return "";
        return type.getAnnotation(TableInfo.class).comment();
    }

    /** Get Mybatis type of filed */
    public static String getMybatisType(Field field) {
        String res = "VARCHAR";
        Class type = field.getType();
        if (type == Double.class || type == double.class) {
            res = "DOUBLE";
        } else if (type == Float.class || type == float.class) {
            res = "FLOAT";
        } else if (type == Boolean.class || type == boolean.class) {
            res = "BIT";
        } else if (type == Byte.class || type == byte.class) {
            res = "TINYINT";
        } else if (type == Short.class || type == short.class) {
            res = "SMALLINT";
        } else if (type == Integer.class || type == int.class) {
            res = "INTEGER";
        } else if (type == Date.class || type == Long.class || type == long.class) {
            res = "BIGINT";
        } else if (type.getSuperclass() == Enum.class) {
            return "SMALLINT";
        }
        return res;
    }

    /** Get column type of filed */
    public static String getColumnType(Field f, Class type) {
        String columnType;
        if (f.isAnnotationPresent(Column.class) && !StrUtil.isEmpty(f.getAnnotation(Column.class).columnDefinition())) {
            // Use definition of Column annotation.
            columnType = f.getAnnotation(Column.class).columnDefinition();
        } else if (f.isAnnotationPresent(Id.class)) {
            // Handle Id
            columnType = "BIGINT UNSIGNED";
        } else {
            if (Integer.class == type) {
                columnType = "INT UNSIGNED";
            } else if (Long.class == type) {
                columnType = "BIGINT UNSIGNED";
            } else if (Short.class == type) {
                columnType = "SMALLINT UNSIGNED";
            } else if (Float.class == type || Double.class == type) {
                columnType = "DOUBLE(19,4)";
            } else if (Boolean.class == type) {
                columnType = "TINYINT(1)";
            } else if (Date.class == type) {
                columnType = "BIGINT UNSIGNED";
            } else if (Enum.class == type.getSuperclass()) {
                columnType = "SMALLINT";
            } else if (f.isAnnotationPresent(Column.class)) {
                columnType = "VARCHAR(" + f.getAnnotation(Column.class).length() + ")";
            } else {
                columnType = "VARCHAR(255)";
            }
        }
        return columnType;
    }

    /** Get column or filed definitions of given class. */
    public static List<ColumnModel> getColumnModels(Class clazz) {
        List<Field> fields = ReflectionUtil.getAllFields(clazz, ReflectionUtil.FieldsOrder.CHILDREN_2_PARENT);
        List<ColumnModel> columnModels = fields.stream().filter(field -> !"serialVersionUID".equalsIgnoreCase(field.getName())).map(f -> {
            String columnName = f.getName();
            return new ColumnModel(
                    f.getName(),
                    f.getType(),
                    f.isAnnotationPresent(Column.class) ? f.getAnnotation(Column.class).name() : columnName,
                    getColumnType(f, f.getType()),
                    getMybatisType(f),
                    f.isAnnotationPresent(ColumnInfo.class) ? f.getAnnotation(ColumnInfo.class).comment() : "",
                    f.getAnnotation(Column.class).nullable(),
                    f.isAnnotationPresent(ColumnInfo.class) && f.getAnnotation(ColumnInfo.class).added()
            );
        }).collect(Collectors.toList());
        // move id to first place
        int index = 0;
        for (ColumnModel columnModel : columnModels) {
            if ("id".equalsIgnoreCase(columnModel.columnName)) {
                break;
            }
            index++;
        }
        ColumnModel idColumnModel = columnModels.remove(index);
        columnModels.add(0, idColumnModel);
        return columnModels;
    }

    public static class ColumnModel {
        /** Property used in Mybatis mapper, same as filed name */
        public final String prop;

        /** Property type, the java field type. */
        public final Class<?> propType;

        /** Table column name */
        public final String columnName;

        /** Table column type */
        public final String columnType;

        /** Mybatis of field */
        public final String mybatisType;

        /** Column comment used to generate SQL */
        public final String columnComment;

        /** Is filed nullable */
        public final boolean nullable;

        /** Is new filed, if true, will generate "ALTER TABLE xxx ADD COLUMN xxx" SQL as well. */
        public final boolean added;

        public ColumnModel(String prop, Class<?> propType, String columnName, String columnType,
                           String mybatisType, String columnComment, boolean nullable, boolean added) {
            this.prop = prop;
            this.propType = propType;
            this.columnName = columnName;
            this.columnType = columnType;
            this.mybatisType = mybatisType;
            this.columnComment = columnComment;
            this.nullable = nullable;
            this.added = added;
        }
    }
}
