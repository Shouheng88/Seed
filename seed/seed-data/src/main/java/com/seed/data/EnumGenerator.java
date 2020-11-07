package com.seed.data;

import com.seed.base.BaseGenerator;
import com.seed.base.model.enums.DeviceType;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Database enum handler generator.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 20:21
 */
@EnumGenerator.GeneratorConfiguration(
        enums = {DeviceType.class},
        valuesWhenNull = {
                /*DeviceType.UNKNOWN*/0
        },
        outputDirectory = "seed-data/src/main/java/com/seed/data/dao/handler",
        author = "<a href=\"mailto:shouheng2015@gmail.com\">Shouheng.W</a>")
public final class EnumGenerator extends BaseGenerator {

    private static String outputDirectory;

    private static String author;

    private static Map<Class, Integer> enumValueMap = new HashMap<>();

    public static void main(String ...args) {
        GeneratorConfiguration configuration = EnumGenerator.class.getAnnotation(GeneratorConfiguration.class);
        if (configuration == null) {
            System.err.println("ERROR: You should specify configuration by @GeneratorConfiguration!");
            return;
        }
        outputDirectory = configuration.outputDirectory();
        author = configuration.author();
        Class[] enums = configuration.enums();
        int[] values = configuration.valuesWhenNull();
        if (enums.length != values.length) {
            System.err.println("ERROR: Returned values of enums() and valuesWhenNull() should have the same length!");
            return;
        }
        for (int i=0, len=enums.length; i<len; i++) {
            if (values[i] != GeneratorConfiguration.NULL_CONSTANT) {
                enumValueMap.put(enums[i], values[i]);
            }
        }
        Stream.of(configuration.enums()).forEach(EnumGenerator::generateEnumHandler);
    }

    private static void generateEnumHandler(Class enumClass) {
        String classSimpleName = enumClass.getSimpleName();
        String componentName = String.format("%sTypeHandler", classSimpleName);
        String valueWhenNull;
        if (enumValueMap.containsKey(enumClass)) {
            int defValue = enumValueMap.get(enumClass);
            try {
                Enum en = (Enum) enumClass.getMethod("getTypeById", int.class).invoke(null, defValue);
                valueWhenNull = classSimpleName + "." + en.name();
            } catch (NoSuchMethodException e) {
                System.err.println("ERROR: Only the enum contains one static method #getTypeById(int) was supported!");
                return;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return;
            }
        } else {
            valueWhenNull = "null";
        }
        String content = header(author, componentName) +
                String.format("@MappedTypes(value = {%s.class})\n", classSimpleName) +
                String.format("public class %s extends BaseTypeHandler<%s> {\n\n", componentName, classSimpleName) +
                "    @Override\n" +
                String.format("    public void setNonNullParameter(PreparedStatement ps, int i, %s parameter, JdbcType jdbcType) throws SQLException {\n", classSimpleName) +
                "        ps.setInt(i, parameter.id);\n" +
                "    }" + "\n" +
                "\n" +
                "    @Override\n" +
                String.format("    public %s getNullableResult(ResultSet rs, String columnName) throws SQLException {\n", classSimpleName) +
                "        try {\n" +
                "            int id = rs.getInt(columnName);\n" +
                String.format("            return %s.getTypeById(id);\n", classSimpleName) +
                "        } catch (Exception ex) {\n" +
                String.format("            return %s;\n", valueWhenNull) +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                String.format("    public %s getNullableResult(ResultSet rs, int columnIndex) throws SQLException {\n", classSimpleName) +
                "        if (rs.wasNull()) {\n" +
                String.format("            return %s;\n", valueWhenNull) +
                "        } else {\n" +
                "            int id = rs.getInt(columnIndex);\n" +
                String.format("            return %s.getTypeById(id);\n", classSimpleName) +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                String.format("    public %s getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {\n", classSimpleName) +
                "        if (cs.wasNull()) {\n" +
                String.format("            return %s;\n", valueWhenNull) +
                "        } else {\n" +
                "            int id = cs.getInt(columnIndex);\n" +
                String.format("            return %s.getTypeById(id);\n", classSimpleName) +
                "        }\n" +
                "    }\n" +
                "}\n";
        System.out.println(content);
        printFile(String.format("%sTypeHandler.java", classSimpleName), content, outputDirectory);
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {ElementType.TYPE})
    @interface GeneratorConfiguration {

        int NULL_CONSTANT = -3864;

        /** The enums to generate handlers. */
        Class[] enums();

        /**
         * Used to specify returned values when given data read from
         * database was null. The values specified by this method should have
         * same length of {@link #enums()} method. If you set the value as
         * {@link #NULL_CONSTANT} the enum will be set as null.
         */
        int[] valuesWhenNull();

        /** The handler class files output directory. */
        String outputDirectory();

        /** Specify author, witch was used to generate java file header. */
        String author() default "";
    }
}
