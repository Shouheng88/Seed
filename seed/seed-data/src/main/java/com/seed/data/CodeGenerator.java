package com.seed.data;

import cn.hutool.core.util.ReflectUtil;
import com.seed.base.BaseGenerator;
import com.seed.data.utils.JPAHelper;

import javax.persistence.Table;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 21:23
 */
@CodeGenerator.GeneratorConfiguration(
        models = {

        },
        useSpecifyDirs = false,
        fileTypesToGenerate = {
                CodeGenerator.TypeToGenerate.SQL
        },
        coverExistFiles = true,
        outputDirectory = "F:/DDLFiles",
        sqlOutputDirectory = "beauty-data/src/main/resources/schema",
        primaryKeyAutoIncrement = 66,
        mapperOutputDirectory = "beauty-data/src/main/resources/mybatis/mapper",
        voOutputDirectory = "beauty-data/src/main/java/me/shouheng/data/model/vo",
        soOutputDirectory = "beauty-data/src/main/java/me/shouheng/data/model/so",
        daoOutputDirectory = "beauty-data/src/main/java/me/shouheng/data/dao",
        daoPackage = "me.shouheng.data.dao",
        serviceOutputDirectory = "beauty-portal/src/main/java/me/shouheng/beauty/service",
        serviceImplOutputDirectory = "beauty-portal/src/main/java/me/shouheng/beauty/service/impl",
        serviceTestOutputDirectory = "beauty-portal/src/test/java/me/shouheng/beauty",
        author = "<a href=\"mailto:shouheng2015@gmail.com\">WngShhng</a>"
)
public final class CodeGenerator extends BaseGenerator {
    private static final String DIR_SPLIT = System.getProperty("file.separator");
    private static final String LINE_SPLIT = System.getProperty("line.separator");

    private static final String VO_SUFFIX = "Vo";
    private static final String SO_SUFFIX = "So";
    private static final String PACK_VO_PREFIX = "Pack";
    private static final String PACK_VO_SUFFIX = "Vo";
    private static final String MAPPER_SUFFIX = "Mapper";
    private static final String TEST_CLASS_SUFFIX = "ServiceTest";
    private static final String DAO_INTERFACE_SUFFIX = "DAO";
    private static final String SERVICE_INTERFACE_SUFFIX = "Service";
    private static final String SERVICE_IMPL_SUFFIX = "ServiceImpl";
    private static final String CREATE_METHOD_NAME_PREFIX = "create";
    private static final String DELETE_METHOD_NAME_PREFIX = "delete";
    private static final String GET_METHOD_NAME_PREFIX = "get";
    private static final String SEARCH_METHOD_NAME_PREFIX = "search";
    private static final String UPDATE_METHOD_NAME_PREFIX = "update";
    private static final String JAVA_FILE_SUFFIX = ".java";
    private static final String SQL_SUFFIX = ".sql";
    private static final String XML_SUFFIX = ".xml";
    private static final String TEST_ANNOTATION = "@Test";
    private static final String REMARK_UPDATED_STR = "remark was updated";
    private static final String COMMA = ",";
    private static final String BLANK_FOR_METHOD_BODY = "        ";
    private static final String BLANK_FOR_METHOD = "    ";
    private static final String BLANK_FOR_PARAM = " ";

    private static String outputDirectory;
    private static String allOutputDirectory;
    private static List<TypeToGenerate> typeToGenerate;
    private static int primaryKeyAutoIncrement;
    private static boolean useSpecifyDirectories;
    private static boolean coverExistFiles;
    private static Class currentPoClass = null;
    private static String sqlOutputDirectory;
    private static String mapperOutputDirectory;
    private static String daoPackage;
    private static String voOutputDirectory;
    private static String soOutputDirectory;
    private static String daoOutputDirectory;
    private static String serviceOutputDirectory;
    private static String serviceImplOutputDirectory;
    private static String serviceTestOutputDirectory;
    private static String author;

    private static String poClassSimpleName;
    private static String voClassSimpleName;
    private static String packVoClassSimpleName;
    private static String packVoClassSimpleNameSimplified;
    private static String soClassSimpleName;
    private static String daoInterfaceClassName;
    private static String serviceInterfaceClassName;
    private static String serviceImplClassName;
    private static List<JPAHelper.ColumnModel> columnModels = new LinkedList<>();


    public static void main(String[] args) {
        GeneratorConfiguration configuration = CodeGenerator.class.getAnnotation(GeneratorConfiguration.class);
        if (configuration == null) {
            System.err.println("ERROR: You need to configuration the generator by @GeneratorConfiguration !");
            return;
        }
        // read configurations
        outputDirectory = configuration.outputDirectory();
        useSpecifyDirectories = configuration.useSpecifyDirs();
        typeToGenerate = Arrays.asList(configuration.fileTypesToGenerate());
        coverExistFiles = configuration.coverExistFiles();
        sqlOutputDirectory = configuration.sqlOutputDirectory();
        primaryKeyAutoIncrement = configuration.primaryKeyAutoIncrement();
        mapperOutputDirectory = configuration.mapperOutputDirectory();
        daoPackage = configuration.daoPackage();
        voOutputDirectory = configuration.voOutputDirectory();
        soOutputDirectory = configuration.soOutputDirectory();
        daoOutputDirectory = configuration.daoOutputDirectory();
        serviceOutputDirectory = configuration.serviceOutputDirectory();
        serviceImplOutputDirectory = configuration.serviceImplOutputDirectory();
        serviceTestOutputDirectory = configuration.serviceTestOutputDirectory();
        author = configuration.author();
        // do generate
        Stream.of(configuration.models()).forEach(it -> {
            currentPoClass = it;
            initAllParam();
            columnModels = JPAHelper.getColumnModels(currentPoClass);
            initAllComponentNames(currentPoClass);
            printAllComponent(currentPoClass);
        });
    }


    private static void initAllParam() {
        allOutputDirectory = outputDirectory + DIR_SPLIT + currentPoClass.getSimpleName();
        File outPutDir = new File(allOutputDirectory);
        if (!outPutDir.exists()) {
            boolean createDirSuccess = outPutDir.mkdirs();
            System.out.println((createDirSuccess ? "Crated directory: " : "Failed to create directory: ") + outPutDir);
        }
        System.out.println();
    }

    private static void initAllComponentNames(Class clazz) {
        poClassSimpleName = clazz.getSimpleName();
        voClassSimpleName = poClassSimpleName + VO_SUFFIX;
        soClassSimpleName = poClassSimpleName + SO_SUFFIX;
        packVoClassSimpleName = PACK_VO_PREFIX + PACK_VO_SUFFIX + "<" + poClassSimpleName + PACK_VO_SUFFIX + ">";
        packVoClassSimpleNameSimplified = PACK_VO_PREFIX + PACK_VO_SUFFIX + "<>";
        daoInterfaceClassName = poClassSimpleName + DAO_INTERFACE_SUFFIX;
        serviceInterfaceClassName = poClassSimpleName + SERVICE_INTERFACE_SUFFIX;
        serviceImplClassName = poClassSimpleName + SERVICE_IMPL_SUFFIX;
    }

    private static void printAllComponent(Class clazz) {
        boolean isAll = typeToGenerate.indexOf(TypeToGenerate.ALL) != -1;
        if (!useSpecifyDirectories || isAll || typeToGenerate.indexOf(TypeToGenerate.SQL) != -1) {
            String sqlContent = getSqlContent(clazz);
            outputContent2File(useSpecifyDirectories ? sqlOutputDirectory : allOutputDirectory,
                    poClassSimpleName + SQL_SUFFIX, sqlContent);
        }
        if (!useSpecifyDirectories || isAll || typeToGenerate.indexOf(TypeToGenerate.MAPPER) != -1) {
            String mapperContent = getMapper(clazz);
            outputContent2File(useSpecifyDirectories ? mapperOutputDirectory : allOutputDirectory,
                    poClassSimpleName + MAPPER_SUFFIX + XML_SUFFIX, mapperContent);
        }
        if (!useSpecifyDirectories || isAll || typeToGenerate.indexOf(TypeToGenerate.SERVICE) != -1) {
            String serviceInterfaceContent = getServiceInterfaceContent();
            outputContent2File(useSpecifyDirectories ? serviceOutputDirectory : allOutputDirectory,
                    serviceInterfaceClassName + JAVA_FILE_SUFFIX, serviceInterfaceContent);
        }
        if (!useSpecifyDirectories || isAll || typeToGenerate.indexOf(TypeToGenerate.SERVICE_IMPL) != -1) {
            String serviceImplContent = getServiceImplContent();
            outputContent2File(useSpecifyDirectories ? serviceImplOutputDirectory : allOutputDirectory,
                    serviceImplClassName + JAVA_FILE_SUFFIX, serviceImplContent);
        }
        if (!useSpecifyDirectories || isAll || typeToGenerate.indexOf(TypeToGenerate.VO) != -1) {
            String voContent = getVoContent(clazz);
            outputContent2File(useSpecifyDirectories ? voOutputDirectory : allOutputDirectory,
                    voClassSimpleName + JAVA_FILE_SUFFIX, voContent);
        }
        if (!useSpecifyDirectories || isAll || typeToGenerate.indexOf(TypeToGenerate.DAO) != -1) {
            String daoInterfaceContent = getDAOInterfaceContent();
            outputContent2File(useSpecifyDirectories ? daoOutputDirectory : allOutputDirectory,
                    daoInterfaceClassName + JAVA_FILE_SUFFIX, daoInterfaceContent);
        }
        if (!useSpecifyDirectories || isAll || typeToGenerate.indexOf(TypeToGenerate.SO) != -1) {
            String soContent = getSearchSoContent();
            outputContent2File(useSpecifyDirectories ? soOutputDirectory : allOutputDirectory,
                    soClassSimpleName + JAVA_FILE_SUFFIX, soContent);
        }
        if (!useSpecifyDirectories || isAll || typeToGenerate.indexOf(TypeToGenerate.SERVICE_TEST) != -1) {
            String testClassContent = getTestClassContent();
            outputContent2File(useSpecifyDirectories ? serviceTestOutputDirectory : allOutputDirectory,
                    poClassSimpleName + TEST_CLASS_SUFFIX + JAVA_FILE_SUFFIX, testClassContent);
        }
    }


    private static String getMapper(Class clazz) {
        String tableName = JPAHelper.getTableName(clazz);
        String typeName = clazz.getSimpleName();

        // typeAlias
        System.out.println("<typeAlias alias=\"" + typeName + "\" type=\"" + clazz.getName() + "\"/>");
        System.out.println();

        String indentation1 = "    ";
        String indentation2 = "        ";
        String indentation3 = "            ";

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SPLIT);
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" " + LINE_SPLIT);
        sb.append("        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">" + LINE_SPLIT);
        sb.append("<mapper namespace=\"" + daoPackage + "." + daoInterfaceClassName + "\">" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // insert
        sb.append(indentation1 + "<insert id=\"insert\" parameterType=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "insert into " + tableName + "(\n");
        for (int k=0, size=columnModels.size(); k<size; k++) {
            JPAHelper.ColumnModel model = columnModels.get(k);
            if (k == size-1) {
                sb.append(indentation2 + "<!-- " + k + "-->" + model.columnName + LINE_SPLIT);
            } else {
                sb.append(indentation2 + "<!-- " + k + "-->" + model.columnName + ",\n");
            }
        }
        sb.append(indentation2 + ")\n");
        sb.append(indentation2 + "values(\n");
        for (int k=0, size=columnModels.size(); k<size; k++) {
            JPAHelper.ColumnModel model = columnModels.get(k);
            if (k == size-1) {
                sb.append(indentation2 + "<!-- " + k + "-->#{" + model.prop + ":" + model.mybatisType + "}\n");
            } else {
                sb.append(indentation2 + "<!-- " + k + "-->#{" + model.prop + ":" + model.mybatisType + "},\n");
            }
        }
        sb.append(indentation2 + ")\n");
        // handle primary key
        sb.append(indentation2 + "<selectKey resultType=\"java.lang.Long\" order=\"AFTER\" keyProperty=\"id\">\n");
        sb.append(indentation3 + "select last_insert_id() as id\n");
        sb.append(indentation2 + "</selectKey>\n");
        sb.append(indentation1 + "</insert>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // update
        sb.append(indentation1 + "<update id=\"update\" parameterType=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "update " + tableName + " set\n");
        for (int k=0, size=columnModels.size(); k<size; k++) {
            JPAHelper.ColumnModel model = columnModels.get(k);
            if (!model.columnName.equals("lock_version") && !model.columnName.equals("id")) {
                sb.append(indentation3 + model.columnName + "=#{" + model.prop + ":" + model.mybatisType + "},\n");
            }
        }
        sb.append(indentation3 + "LOCK_VERSION = LOCK_VERSION+1\n");
        sb.append(indentation3 + "where ID=#{id} and LOCK_VERSION=#{lockVersion} \n");
        sb.append(indentation1 + "</update>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // updatePOSelective
        printUpdatePOSelective(sb, tableName, typeName);

        // selectByPrimaryKey
        sb.append(indentation1 + "<select id=\"selectByPrimaryKey\" parameterType=\"long\" resultType=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "select * from " + tableName + " where id=#{id}" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // selectVoByPrimaryKey
        sb.append(indentation1 + "<select id=\"selectVoByPrimaryKey\" parameterType=\"long\" resultType=\"" + typeName + "Vo" + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "select * from " + tableName + " where id=#{id}" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // searchBySo
        sb.append(indentation1 + "<select id=\"searchBySo\" resultType=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "select t.* from " + tableName + " t\n");
        sb.append(indentation2 + "<include refid=\"SO_Where_Clause\" />" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // searchVosBySo
        sb.append(indentation1 + "<select id=\"searchVosBySo\" resultType=\"" + typeName + "Vo" + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "select t.* from " + tableName + " t\n");
        sb.append(indentation2 + "<include refid=\"SO_Where_Clause\" />" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // searchCountBySo
        sb.append(indentation1 + "<select id=\"searchCountBySo\" resultType=\"long\">" + LINE_SPLIT);
        sb.append(indentation2 + "select count(t.id) from " + tableName + " t\n");
        sb.append(indentation2 + "<include refid=\"SO_Where_Clause\" />" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // SO_Where_Clause
        sb.append(indentation1 + "<sql id=\"SO_Where_Clause\">" + LINE_SPLIT);
        sb.append(indentation2 + "<where>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);
        sb.append(indentation2 + "</where>" + LINE_SPLIT);
        sb.append(indentation2 + "<include refid=\"Base.Order_By_Clause\" />" + LINE_SPLIT);
        sb.append(indentation1 + "</sql>" + LINE_SPLIT);

        // deleteByPrimaryKey
        sb.append(indentation1 + "<delete id=\"deleteByPrimaryKey\" parameterType=\"long\">" + LINE_SPLIT);
        sb.append(indentation2 + "delete from " + tableName + " where id=#{id}" + LINE_SPLIT);
        sb.append(indentation1 + "</delete>" + LINE_SPLIT);

        sb.append("</mapper>" + LINE_SPLIT);
        System.out.println(sb.toString());
        return sb.toString();
    }

    private static void printUpdatePOSelective(StringBuilder sb, String table, String typeName) {
        sb.append(MessageFormat.format("    <update id=\"updatePOSelective\" parameterType=\"{0}\">", typeName) + LINE_SPLIT);
        sb.append("        update " + table + LINE_SPLIT);
        sb.append("        <set>" + LINE_SPLIT);

        for (JPAHelper.ColumnModel model : columnModels) {
            if (!"lock_version".equals(model.columnName) && !"id".equals(model.columnName)) {
                sb.append("            " + MessageFormat.format("<if test=\"{0} != null \">", model.prop) + LINE_SPLIT);
                sb.append("                " + model.columnName + "=#{" + model.prop + ":" + model.mybatisType + "}" + COMMA + LINE_SPLIT);
                sb.append("            </if>" + LINE_SPLIT);
            }
        }

        sb.append("            LOCK_VERSION = LOCK_VERSION+1" + LINE_SPLIT);
        sb.append("        </set>" + LINE_SPLIT);
        sb.append("            where ID=#{id} and LOCK_VERSION=#{lockVersion} " + LINE_SPLIT);

        sb.append("    </update>").append(LINE_SPLIT);
        sb.append(LINE_SPLIT);
    }


    private static String getSqlContent(Class clazz) {
        return table(clazz) + primaryKey(clazz) + autoIncrement(clazz) + addedColumnSqls(clazz);
    }

    private static String table(Class clazz) {
        StringBuilder sql = new StringBuilder();

        String tableName = JPAHelper.getTableName(clazz);
        String tableComment = JPAHelper.getTableComment(clazz);
        sql.append("--Create Table " + tableName + LINE_SPLIT);
        sql.append("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        JPAHelper.ColumnModel columnModel;
        for (int i=0, size=columnModels.size(); i<size; i++) {
            columnModel = columnModels.get(i);
            if (i == (size - 1)) {
                sql.append(LINE_SPLIT
                        + columnModel.columnName + " " + columnModel.columnType
                        + (columnModel.nullable ? "" : " NOT NULL")
                        + " COMMENT \'" + columnModel.columnComment + "\'"
                        +")");
            } else {
                sql.append(LINE_SPLIT
                        + columnModel.columnName + " " + columnModel.columnType
                        + (columnModel.nullable ? "" : " NOT NULL")
                        + " COMMENT \'" + columnModel.columnComment +  "\'"
                        + ",");
            }
        }
        sql.append(" COMMENT \'" + tableComment +  "\';");
        sql.append(LINE_SPLIT);
        System.out.println(sql.toString());
        return sql.toString();
    }

    private static String primaryKey(Class clazz) {
        StringBuilder key = new StringBuilder();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            String tableName = table.name();
            key.append("--Add Primary Key " + key + LINE_SPLIT);
            key.append("ALTER TABLE " + tableName + " MODIFY id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT;");
            key.append(LINE_SPLIT);
        }
        System.out.println(key.toString());
        return key.toString();
    }

    private static String autoIncrement(Class clazz) {
        StringBuilder key = new StringBuilder();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            String tableName = table.name();
            key.append("--Modify Primary Key " + key + LINE_SPLIT);
            key.append("ALTER TABLE " + tableName + " AUTO_INCREMENT = " + primaryKeyAutoIncrement + ";");
            key.append(LINE_SPLIT);
        }
        System.out.println(key.toString());
        return key.toString();
    }

    private static String addedColumnSqls(Class clazz) {
        StringBuilder sb = new StringBuilder();
        String tableName = JPAHelper.getTableName(clazz);
        JPAHelper.ColumnModel columnModel;
        for (int i=0, size=columnModels.size(); i<size; i++) {
            columnModel = columnModels.get(i);
            if (columnModel.added) {
                sb.append("--Add New Column " + columnModel.columnComment + LINE_SPLIT);
                sb.append("ALTER TABLE " + tableName
                        + " ADD COLUMN " + columnModel.columnName + " " + columnModel.columnType
                        + (columnModel.nullable ? "" : " NOT NULL"));
                if (i == 0) {
                    sb.append(" FIRST");
                } else {
                    sb.append(" AFTER " + columnModels.get(i-1).columnName);
                }
                sb.append(" COMMENT \'" + columnModel.columnComment +  "\';");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static String getTestClassContent() {
        StringBuilder sb = new StringBuilder();

        String testAnnotationStartStr = TEST_ANNOTATION;
        String serviceTestClassName = String.format("%sServiceTest", poClassSimpleName);

        // start
        sb.append(header(author, serviceTestClassName));
        sb.append(String.format("public class %s extends SpringBaseTest {", serviceTestClassName));
        sb.append(LINE_SPLIT);
        sb.append(LINE_SPLIT);

        sb.append(fillBlankForString("@Autowired"));
        sb.append(LINE_SPLIT);
        sb.append(fillBlankForString(String.format("private %s %s;", serviceInterfaceClassName,
                convertFirstLetterToLower(serviceInterfaceClassName))));
        sb.append(LINE_SPLIT);

        // create entry method
        sb.append(LINE_SPLIT);
        String createEntryMethodHeader = generateMethodHeader(Accessibility.PUBLIC,
                voClassSimpleName, "create", null, null);
        String createEntryMethodBody = getCreateEntryMethodBodyForTest();
        sb.append(generateEntireMethod(createEntryMethodHeader, createEntryMethodBody));
        sb.append(LINE_SPLIT);

        // testCreate method
        sb.append(LINE_SPLIT);
        sb.append(BLANK_FOR_METHOD + testAnnotationStartStr + LINE_SPLIT);
        String createMethodHeader = generateMethodHeader(Accessibility.PUBLIC, "void", "testCreate", null, null);
        String createMethodBody = getCreateMethodBodyForTest();
        sb.append(generateEntireMethod(createMethodHeader, createMethodBody));
        sb.append(LINE_SPLIT);

        // testSearch method
        sb.append(LINE_SPLIT);
        sb.append(BLANK_FOR_METHOD + testAnnotationStartStr + LINE_SPLIT);
        String testSearchMethodHeader = generateMethodHeader(Accessibility.PUBLIC, "void", "testSearch", null, null);
        String testSearchMethodBody = getTestSearchMethodBodyForTest();
        sb.append(generateEntireMethod(testSearchMethodHeader, testSearchMethodBody));
        sb.append(LINE_SPLIT);

        // testSearchCount method
        sb.append(LINE_SPLIT);
        sb.append(BLANK_FOR_METHOD + testAnnotationStartStr + LINE_SPLIT);
        String testSearchCountMethodHeader = generateMethodHeader(Accessibility.PUBLIC, "void", "testSearchCount", null, null);
        String testSearchCountMethodBody = getTestSearchCountMethodBodyForTest();
        sb.append(generateEntireMethod(testSearchCountMethodHeader, testSearchCountMethodBody));
        sb.append(LINE_SPLIT);

        // testUpdate method
        sb.append(LINE_SPLIT);
        sb.append(BLANK_FOR_METHOD + testAnnotationStartStr + LINE_SPLIT);
        String testUpdateMethodHeader = generateMethodHeader(Accessibility.PUBLIC, "void", "testUpdate", null, null);
        String testUpdateMethodBody = getUpdateMethodBodyForTest();
        sb.append(generateEntireMethod(testUpdateMethodHeader, testUpdateMethodBody));
        sb.append(LINE_SPLIT);
        sb.append(LINE_SPLIT);

        // end
        sb.append("}");
        sb.append(LINE_SPLIT);
        return sb.toString();
    }

    private static String getUpdateMethodBodyForTest() {
        return getMethodBodyLineByFormatContent(String.format("%s vo = this.create();", voClassSimpleName)) +
                getMethodBodyLineByFormatContent(String.format("%s voTemp = %s.get%s(vo.getId()).getVo();",
                        voClassSimpleName, convertFirstLetterToLower(serviceInterfaceClassName), poClassSimpleName)) +
                getMethodBodyLineByFormatContent(String.format("voTemp.setRemark(\"%s\");", REMARK_UPDATED_STR)) +
                getMethodBodyLineByFormatContent(String.format("%s updateRes = %s.update%s(voTemp).getVo();",
                        voClassSimpleName, convertFirstLetterToLower(serviceInterfaceClassName), poClassSimpleName)) +
                fillBlankForMethodBody(String.format(
                        "Assert.assertTrue(updateRes !=null && \"%s\".equals(updateRes.getRemark()));", REMARK_UPDATED_STR));
    }

    private static String getTestSearchCountMethodBodyForTest() {
        return getMethodBodyLineByFormatContent(String.format("%s vo = this.create();", voClassSimpleName)) +
                getMethodBodyLineByFormatContent(String.format("%s so = new %s();", soClassSimpleName,
                        soClassSimpleName)) +
                getMethodBodyLineByFormatContent(String.format("long count = %s.search%sCount(so).getUdf1();",
                        convertFirstLetterToLower(serviceInterfaceClassName), poClassSimpleName)) +
                fillBlankForMethodBody("Assert.assertTrue(count > 0);");
    }

    private static String getTestSearchMethodBodyForTest() {
        return getMethodBodyLineByFormatContent(String.format("%s vo = this.create();", voClassSimpleName)) +
                getMethodBodyLineByFormatContent(String.format("%s so = new %s();", soClassSimpleName,
                        soClassSimpleName)) +
                getMethodBodyLineByFormatContent(String.format("List<%s> voList = %s.search%s(so).getVoList();",
                        voClassSimpleName, convertFirstLetterToLower(serviceInterfaceClassName), poClassSimpleName)) +
                fillBlankForMethodBody("Assert.assertTrue(voList != null && voList.size() > 0);");
    }

    private static String getMethodBodyLineByFormatContent(String s) {
        String res = fillBlankForMethodBody(s);
        res += LINE_SPLIT;
        return res;
    }

    private static String getCreateMethodBodyForTest() {
        return fillBlankForMethodBody(String.format("%s vo = this.create();", voClassSimpleName)) +
                LINE_SPLIT +
                fillBlankForMethodBody("Assert.assertTrue(vo!= null);");
    }

    private static String getCreateEntryMethodBodyForTest() {
        return fillBlankForMethodBody(String.format("%s vo = MockTestUtil.getJavaBean(%s.class);", voClassSimpleName, voClassSimpleName)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("return %s.%s(vo).getVo();",
                        convertFirstLetterToLower(serviceInterfaceClassName), CREATE_METHOD_NAME_PREFIX + poClassSimpleName));
    }


    private static String getDAOInterfaceContent() {
        return header(author, daoInterfaceClassName) + String.format("public interface %s extends DAO<%s, %s> {", daoInterfaceClassName, poClassSimpleName, voClassSimpleName) +
                LINE_SPLIT +
                LINE_SPLIT +
                "}" +
                LINE_SPLIT;
    }

    private static String getSearchSoContent() {
        return header(author, soClassSimpleName) +
                "@Data" +
                LINE_SPLIT +
                "@Repository" +
                LINE_SPLIT +
                "@ToString(callSuper = true)" +
                LINE_SPLIT +
                "@EqualsAndHashCode(callSuper = true)" +
                LINE_SPLIT +
                String.format("public class %s extends SearchObject {", soClassSimpleName) +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString("private static final long serialVersionUID = 1L;") +
                LINE_SPLIT +
                LINE_SPLIT +
                "}" +
                LINE_SPLIT;
    }

    private static String getServiceInterfaceContent() {
        return header(author, serviceInterfaceClassName) + String.format("public interface %s {", serviceInterfaceClassName) +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(Accessibility.DEFAULT, packVoClassSimpleName,
                        CREATE_METHOD_NAME_PREFIX + poClassSimpleName, voClassSimpleName, "vo", false) + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(Accessibility.DEFAULT, packVoClassSimpleName,
                        GET_METHOD_NAME_PREFIX + poClassSimpleName, "Long", "primaryKey", false) + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(Accessibility.DEFAULT, packVoClassSimpleName,
                        UPDATE_METHOD_NAME_PREFIX + poClassSimpleName, voClassSimpleName, "vo", false) + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(Accessibility.DEFAULT, packVoClassSimpleName,
                        DELETE_METHOD_NAME_PREFIX + poClassSimpleName, "Long", "primaryKey", false) + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(Accessibility.DEFAULT, packVoClassSimpleName,
                        SEARCH_METHOD_NAME_PREFIX + poClassSimpleName, soClassSimpleName, "so", false) + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(Accessibility.DEFAULT, packVoClassSimpleName,
                        SEARCH_METHOD_NAME_PREFIX + poClassSimpleName + "Count", soClassSimpleName, "so", false) + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                "}" +
                LINE_SPLIT;
    }

    private static String getServiceImplContent() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(header(author, serviceImplClassName));

        buffer.append(String.format("@Slf4j\n@Service(\"%sService\")", convertFirstLetterToLower(poClassSimpleName)));
        buffer.append(LINE_SPLIT);

        // start
        buffer.append(String.format("public class %s implements %s {", serviceImplClassName, serviceInterfaceClassName));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // autowire some class
        fillAutowireClassForServiceImpl(buffer);

        // create
        buffer.append(LINE_SPLIT);
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String createMethodHeader = generateMethodHeader(Accessibility.PUBLIC, packVoClassSimpleName, CREATE_METHOD_NAME_PREFIX
                + poClassSimpleName, voClassSimpleName, "vo");
        String createMethodBody = getCreateMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(createMethodHeader, createMethodBody));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // get
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String getMethodHeader = generateMethodHeader(Accessibility.PUBLIC, packVoClassSimpleName, GET_METHOD_NAME_PREFIX + poClassSimpleName,
                "Long", "primaryKey");
        String getMethodBody = getGetMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(getMethodHeader, getMethodBody));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // update
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String updateMethodHeader = generateMethodHeader(Accessibility.PUBLIC, packVoClassSimpleName, UPDATE_METHOD_NAME_PREFIX
                + poClassSimpleName, voClassSimpleName, "vo");
        String updateMethodBody = getUpdateMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(updateMethodHeader, updateMethodBody));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // delete
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String deleteMethodHeader = generateMethodHeader(Accessibility.PUBLIC, packVoClassSimpleName, DELETE_METHOD_NAME_PREFIX
                + poClassSimpleName, "Long", "primaryKey");
        String deleteMethodBody = getDeleteMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(deleteMethodHeader, deleteMethodBody));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // search
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String searchMethodHeader = generateMethodHeader(Accessibility.PUBLIC, packVoClassSimpleName, SEARCH_METHOD_NAME_PREFIX
                + poClassSimpleName, soClassSimpleName, "so");
        String searchMethodBody = getSearchMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(searchMethodHeader, searchMethodBody));

        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // searchCount
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String searchCountMethodHeader = generateMethodHeader(Accessibility.PUBLIC, packVoClassSimpleName, SEARCH_METHOD_NAME_PREFIX
                + poClassSimpleName + "Count", soClassSimpleName, "so");
        String searchCountMethodBody = getSearchCountMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(searchCountMethodHeader, searchCountMethodBody));

        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // end
        buffer.append("}");
        buffer.append(LINE_SPLIT);

        return buffer.toString();
    }

    private static String getCreateMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", packVoClassSimpleName, packVoClassSimpleNameSimplified)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("%s entity = dozerBeanUtil.convert(vo, %s.class);", poClassSimpleName, poClassSimpleName)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("int ret = %s.insert(entity);", convertFirstLetterToLower(daoInterfaceClassName))) +
                LINE_SPLIT +
                fillBlankForMethodBody("if (ret != 1) {") +
                LINE_SPLIT +
                fillBlankForMethodBody("    throw new DAOException(\"Failed to create entity : \" + vo);") +
                LINE_SPLIT +
                fillBlankForMethodBody("}") +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("packVo.setVo(dozerBeanUtil.convert(entity, %s.class));", voClassSimpleName)) +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getGetMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", packVoClassSimpleName, packVoClassSimpleNameSimplified)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("packVo.setVo(%s.selectVoByPrimaryKey(primaryKey));", convertFirstLetterToLower(daoInterfaceClassName))) +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getSearchMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", packVoClassSimpleName, packVoClassSimpleNameSimplified)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("List<%s> voList = %s.searchVosBySo(so);", voClassSimpleName, convertFirstLetterToLower(daoInterfaceClassName))) +
                LINE_SPLIT +
                fillBlankForMethodBody("packVo.setVoList(voList);") +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getSearchCountMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", packVoClassSimpleName, packVoClassSimpleNameSimplified)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("Long count = %s.searchCountBySo(so);", convertFirstLetterToLower(daoInterfaceClassName))) +
                LINE_SPLIT +
                fillBlankForMethodBody("packVo.setUdf1(count);") +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getUpdateMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", packVoClassSimpleName, packVoClassSimpleNameSimplified)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("%s po = dozerBeanUtil.convert(vo, %s.class);", poClassSimpleName, poClassSimpleName)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("int ret = %s.update(po);", convertFirstLetterToLower(daoInterfaceClassName))) +
                LINE_SPLIT +
                fillBlankForMethodBody("if (ret != 1) {") +
                LINE_SPLIT +
                fillBlankForMethodBody("    throw new DAOException(\"Failed to update entity : \" + vo);") +
                LINE_SPLIT +
                fillBlankForMethodBody("}") +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("packVo.setVo(%s.selectVoByPrimaryKey(po.getId()));", convertFirstLetterToLower(daoInterfaceClassName))) +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static void fillAutowireClassForServiceImpl(StringBuffer buffer) {
        String daoFieldName = convertFirstLetterToLower(daoInterfaceClassName);
        buffer.append(fillBlankForString(String.format("private %s %s;", daoInterfaceClassName, daoFieldName)));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        buffer.append(fillBlankForString("private DozerBeanUtil dozerBeanUtil;"));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        buffer.append(fillBlankForString("@Autowired"));
        buffer.append(LINE_SPLIT);
        buffer.append(fillBlankForString("public " + serviceImplClassName + "(" +
                String.format("%s %s, ", daoInterfaceClassName, daoFieldName) + "DozerBeanUtil dozerBeanUtil) {"));
        buffer.append(LINE_SPLIT);
        buffer.append(fillBlankForMethodBody(String.format("this.%s = %s;", daoFieldName, daoFieldName)));
        buffer.append(LINE_SPLIT);
        buffer.append(fillBlankForMethodBody("this.dozerBeanUtil = dozerBeanUtil;"));
        buffer.append(LINE_SPLIT);
        buffer.append(fillBlankForString("}"));
        buffer.append(LINE_SPLIT);
    }

    private static String getDeleteMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", packVoClassSimpleName, packVoClassSimpleNameSimplified)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("int ret = %s.deleteByPrimaryKey(primaryKey);", convertFirstLetterToLower(daoInterfaceClassName))) +
                LINE_SPLIT +
                fillBlankForMethodBody("if (ret != 1) {") +
                LINE_SPLIT +
                fillBlankForMethodBody("    throw new DAOException(\"Failed to delete entity with primary key : \" + primaryKey);") +
                LINE_SPLIT +
                fillBlankForMethodBody("}") +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getVoContent(Class clazz) {
        String voContent =
                header(author, voClassSimpleName) +
                        "@Data" +
                        LINE_SPLIT +
                        "@Repository" +
                        LINE_SPLIT +
                        "@ToString(callSuper = true)" +
                        LINE_SPLIT +
                        "@EqualsAndHashCode(callSuper = true)" +
                        LINE_SPLIT +
                        String.format("public class %s extends AbstractVo {", voClassSimpleName) +
                        LINE_SPLIT +
                        LINE_SPLIT +
                        "    private static final long serialVersionUID = 1L;" +
                        LINE_SPLIT;

        Field[] fields = ReflectUtil.getFieldsDirectly(clazz, false);
        for (Field field : fields) {
            voContent += (LINE_SPLIT + "    private " + field.getType().getSimpleName() + " " + field.getName() + ";" + LINE_SPLIT);
        }

        voContent += (LINE_SPLIT + "}");
        return voContent;
    }


    private static String generateEntireMethod(String methodHeader, String methodBody) {
        return fillBlankForString(methodHeader) + "{" +
                LINE_SPLIT +
                methodBody +
                LINE_SPLIT +
                fillBlankForString("}");
    }

    private static String  fillBlankForMethodBody(String s) {
        return BLANK_FOR_METHOD_BODY + s;
    }

    private static String fillBlankForString(String s) {
        return BLANK_FOR_METHOD + s;
    }

    private static String generateMethodHeader(Accessibility accessibility,
                                               String returnName,
                                               String methodName,
                                               String paramType,
                                               String paramName) {
        String firstPart = (accessibility == Accessibility.DEFAULT ? "" : accessibility.name + BLANK_FOR_PARAM )
                + returnName + BLANK_FOR_PARAM + methodName + "(";
        if (paramType != null && paramName != null) {
            firstPart += paramType + BLANK_FOR_PARAM + paramName;
        }
        firstPart += ") ";
        return firstPart;
    }

    private static String generateMethodHeader(Accessibility accessibility,
                                               String returnName,
                                               String methodName,
                                               String paramType,
                                               String paramName,
                                               boolean withSpace) {
        String firstPart = (accessibility == Accessibility.DEFAULT ? "" : accessibility.name + BLANK_FOR_PARAM )
                + returnName + BLANK_FOR_PARAM + methodName + "(";
        if (paramType != null && paramName != null) {
            firstPart += paramType + BLANK_FOR_PARAM + paramName;
        }
        firstPart += (withSpace ? ") " : ")");
        return firstPart;
    }


    private static void outputContent2File(String outputDir, String fileName, String content) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            String outputPath = outputDir + DIR_SPLIT + fileName;
            if (new File(outputPath).exists() && !coverExistFiles) {
                System.out.println("FATAL: file existed cover these files may lead to data loss!");
                return;
            }
            fw = new FileWriter(new File(outputPath));
            bw = new BufferedWriter(fw);
            bw.write(content);
            System.out.println("====successFull write the file:" + fileName + " to path:" + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private CodeGenerator() {
        throw new UnsupportedOperationException("You can't initialize me!");
    }

    enum Accessibility {
        PUBLIC("public"),
        PROTECTED("protected"),
        DEFAULT(""),
        PRIVATE("private");

        String name;

        Accessibility(String name) {
            this.name = name;
        }
    }

    enum TypeToGenerate {
        /** Every kind of file will be generated */
        ALL,
        SQL,
        MAPPER,
        VO,
        SO,
        DAO,
        SERVICE,
        SERVICE_IMPL,
        SERVICE_TEST
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {ElementType.TYPE})
    @interface GeneratorConfiguration {

        /** The models to generate everything... */
        Class[] models();

        /** Use specify  */
        boolean useSpecifyDirs();

        /** The class files type to generate. */
        TypeToGenerate[] fileTypesToGenerate() default TypeToGenerate.ALL;

        /** Should cover given file if it's already exists. */
        boolean coverExistFiles() default false;

        /** Output directory of everything */
        String outputDirectory() default "";

        /** SQL files output directory */
        String sqlOutputDirectory() default "";

        /** Specify primary key auto increment start value */
        int primaryKeyAutoIncrement() default 6000;

        /** Mapper xml files output directory. */
        String mapperOutputDirectory() default "";

        /** Package name of DAO classes, for example me.shouheng.data.dao
         * This value will be used to define namespace of Mybatis mappers. */
        String daoPackage() default "";

        /** Vo class files output directory */
        String voOutputDirectory() default "";

        /** So class files output directory. */
        String soOutputDirectory() default "";

        /** DAO interface class files output directory */
        String daoOutputDirectory() default "";

        /** Service interface class files output directory. */
        String serviceOutputDirectory() default "";

        /** Service implementation class files output directory */
        String serviceImplOutputDirectory() default "";

        /** Service test class files output directory */
        String serviceTestOutputDirectory() default "";

        /** Author for java file header. */
        String author() default "";
    }
}
