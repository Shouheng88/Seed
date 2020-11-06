package com.seed.portal;

import cn.hutool.core.util.StrUtil;
import com.seed.base.BaseGenerator;
import com.seed.base.annotation.ApiInfo;
import com.seed.base.annotation.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

/**
 * Api generator is used to generate resources for Android.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 11:18
 */
public final class ApiGenerator extends BaseGenerator {
    private static String outputDirectory;
    private static String apiClassesPackage;
    private static String repoClassesPackage;
    private static String author;

    public static void main(String...args) {
        GeneratorConfiguration configuration = ApiGenerator.class.getAnnotation(GeneratorConfiguration.class);
        if (configuration == null) {
            System.err.println("ERROR: You need to config generator by @GeneratorConfiguration !");
            return;
        }
        outputDirectory = configuration.outputDirectory();
        apiClassesPackage = configuration.apiClassPackage();
        repoClassesPackage = configuration.repoClassPackage();
        author = configuration.author();
        // do generator
        Stream.of(configuration.controllers()).forEach(it -> {
            Api api = (Api) it.getDeclaredAnnotation(Api.class);
            // generate Retrofit API interfaces
            String serviceFileName = getServiceInterfaceFileName(it);
            String serviceFullContent = header(apiClassesPackage, author, api.tags()[0]) + String.format(serviceBody(it), serviceApi(it));
            printFile(serviceFileName, serviceFullContent, outputDirectory);
            // generate repos
            String[] bodies = repoMethods(it);
            String repoFileName = getRepoFileName(it);
            String repoFullContent = header(repoClassesPackage, author, api.tags()[0]) + String.format(repoBody(it), bodies[0], bodies[1]);
            printFile(repoFileName, repoFullContent, outputDirectory);
        });
    }

    private static String getServiceInterfaceFileName(Class clazz) {
        String className = clazz.getSimpleName();
        int idx = className.indexOf("Controller");
        String interfaceName = className.substring(0, idx);
        return interfaceName + "Service.kt";
    }

    private static String getRepoFileName(Class clazz) {
        String className = clazz.getSimpleName();
        int idx = className.indexOf("Controller");
        String interfaceName = className.substring(0, idx);
        return interfaceName + "Repo.kt";
    }

    private static String serviceBody(Class clazz) {
        String className = clazz.getSimpleName();
        int idx = className.indexOf("Controller");
        String interfaceName = className.substring(0, idx);
        return  "interface " + interfaceName + "Service {\n" +
                "\n%s" +
                "}\n\n";
    }

    private static String repoBody(Class clazz) {
        String className = clazz.getSimpleName();
        int idx = className.indexOf("Controller");
        String repoName = className.substring(0, idx) + "Repo";
        return "class " + repoName + " private constructor() {" +
                "\n%s\n" +
                "    companion object {\n" +
                "        val instance: " + repoName + " by lazy { " + repoName +  "() }\n" +
                "    }\n" +
                "}\n" +
                "%s\n";
    }

    private static String serviceApi(Class<?> clazz) {
        RequestMapping mapping = clazz.getDeclaredAnnotation(RequestMapping.class);
        String path = mapping.path()[0];
        StringBuilder sb = new StringBuilder();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getAnnotation(PostMapping.class) != null)
                .forEach(method -> {
                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                    String apiName = apiOperation.value();

                    ApiInfo apiInfo = method.getAnnotation(ApiInfo.class);
                    boolean needAuth = true;
                    if (apiInfo != null && !apiInfo.auth()) {
                        needAuth = false;
                    }

                    Paging paging = apiInfo == null ? null : apiInfo.paging();
                    int maxPageSize = 0;
                    boolean needPage = false;
                    if (paging != null && paging.enable()) {
                        needPage = true;
                        maxPageSize = paging.maxPageSize();
                    }

                    ApiImplicitParams apiImplicitParams = method.getAnnotation(ApiImplicitParams.class);
                    List<String> params = new ArrayList<>();
                    if (apiImplicitParams != null) {
                        Arrays.asList(apiImplicitParams.value()).forEach(apiImplicitParam -> {
                            String filedName = apiImplicitParam.name();
                            String filedValue = apiImplicitParam.value();
                            boolean fieldRequired = apiImplicitParam.required();
                            params.add((params.size() + 1) + ". "
                                    + filedName + " "
                                    + filedValue
                                    + (fieldRequired ? " (Required): " : ""));
                        });
                    }

                    String comment = "    /**\n" +
                            "     * Api: " + apiName + "\n" +
                            "     * " + "\n" +
                            "     * Desc: " + "\n" +
                            (needAuth ? "     * - Auth\n" : "") +
                            "     * " + (needPage ? "- Pageable, max page size " + maxPageSize + " ；\n     * \n" : "\n") +
                            "     * Parameter required: \n";
                    for (String param : params) {
                        comment += "     * " + param + "\n";
                    }
                    comment += "     */\n";

                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    String subPath = postMapping.value()[0];

                    String methodName = method.getName();
                    String request = getSimplifiedGeneric(method.getGenericParameterTypes()[0]);
                    String response = getSimplifiedGeneric(method.getGenericReturnType());

                    String api = comment +
                            "    @POST(\"" + path + subPath + "\")\n" +
                            "    fun " + methodName + "Async(@Body request: " + request + "): Deferred<" + response +  ">\n";

                    sb.append(api).append("\n");
                });

        return sb.toString();
    }

    private static String[] repoMethods(Class<?> clazz) {
        String className = clazz.getSimpleName();
        int idx = className.indexOf("Controller");
        String interfaceName = className.substring(0, idx);
        String serviceName = interfaceName + "Service";

        StringBuilder methodBuilder = new StringBuilder();
        StringBuilder callbackBuilder = new StringBuilder();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getAnnotation(PostMapping.class) != null)
                .forEach(method -> {
                    String methodName = method.getName();

                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                    String apiName = apiOperation.value();

                    methodBuilder.append("\n");
                    methodBuilder.append("    /**\n");
                    methodBuilder.append("     * " + apiName + "\n");
                    methodBuilder.append("     */");
                    methodBuilder.append(
                            "\n    fun " + methodName + "(");
                    ApiImplicitParams apiImplicitParams = method.getAnnotation(ApiImplicitParams.class);
                    ApiImplicitParam[] params = apiImplicitParams == null ? new ApiImplicitParam[0] :
                            Stream.of(apiImplicitParams.value())
                                    .filter(param -> param.name().contains("."))
                                    .toArray(ApiImplicitParam[]::new);
                    String soContent = "";
                    for (ApiImplicitParam param : params) {
                        String fieldName = param.name();
                        fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
                        methodBuilder.append(fieldName).append(": ").append("String").append(", ");
                        soContent += ("                        this." + fieldName + " = " + fieldName + "\n");
                    }
                    String methodNameUpperFirst = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                    String callbackName = "On" + methodNameUpperFirst + "Listener";
                    String callbackMethodNameSuc = "on" + methodNameUpperFirst + "Suc";
                    String callbackMethodNameFailed = "on" + methodNameUpperFirst + "Failed";
                    String soName = getGenericTypeSimple(method.getGenericParameterTypes()[0]);
                    String voName = getSimplifiedGeneric(method.getGenericReturnType());
                    voName = voName.replace("BusinessResponse<", ""); // Remove prefix
                    voName = voName.substring(0, voName.length()-1); // Remove >
                    voName = voName.equals("Any") ? "" : voName;
                    methodBuilder.append("listener: " + callbackName + "?");
                    methodBuilder.append(") {\n");
                    methodBuilder.append(
                            "        GlobalScope.launch(Dispatchers.Main) {\n" +
                                    "            val res = withContext(Dispatchers.IO) {\n" +
                                    "                Net.connect(Server.get(" + serviceName +  "::class.java)\n" +
                                    "                    ." + methodName + "Async(BusinessRequest.get(" + soName + "().apply {\n" +
                                    soContent +
                                    "                    })))\n" +
                                    "            }\n" +
                                    "            if (res.success) {\n" +
                                    "                listener?." + callbackMethodNameSuc + "("  + (StrUtil.isEmpty(voName) ? "" : "res.data!!")  +  ")\n" +
                                    "            } else {\n" +
                                    "                listener?." + callbackMethodNameFailed + "(res.code!!, res.message!!)\n" +
                                    "            }\n" +
                                    "        }\n" +
                                    "    }\n");

                    callbackBuilder.append("\ninterface " + callbackName + " {\n\n");
                    callbackBuilder.append("    fun " + callbackMethodNameSuc + "(" + (StrUtil.isEmpty(voName) ? "" : "data: " + voName) + ")\n\n");
                    callbackBuilder.append("    fun " + callbackMethodNameFailed + "(code: String, msg: String)\n}\n");
                });
        return new String[]{methodBuilder.toString(), callbackBuilder.toString()};
    }

    private static String getSimplifiedGeneric(Type type) {
        String response = "<" + type.getTypeName();
//        System.out.println(response);
        Map<String, String> map = new HashMap<>();
        char[] arrRes = response.toCharArray();
        int x1 = -1;
        int x2 = -1;
        int x3 = -1;
        for (int i=0, len=arrRes.length; i<len; i++) {
            if (arrRes[i] == '<' || arrRes[i] == '>') {
                if (x1 == -1) {
                    x1 = i;
                } else {
                    x3 = i;
                }
            }
            if (arrRes[i] == '.') {
                x2 = i;
            }
            if (i == len-1 && x3 == -1) {
                x3 = len-1;
            }
            if (x1 != -1 && x2 != -1 && x3 != -1) {
                String from = response.substring(x1+1, x3);
                String to = response.substring(x2 + 1, x3);
                map.put(from, to);
                x1 = x3;
                x2 = -1;
                x3 = -1;
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            response = response.replace(entry.getKey(), entry.getValue());
        }
        response = response.substring(1);
        if (response.indexOf('>') == -1) {
            response += "<Any>";
        }
//        System.out.println(response);
        return response;
    }

    private static String getGenericTypeSimple(Type type) {
        String name = type.getTypeName();
        char[] arrRes = name.toCharArray();
        int a=-1, b=-1;
        for (int i=0, len=arrRes.length; i<len; i++) {
            if (arrRes[i] == '<') {
                // last one
                a=i;
            }
            if (b==-1 && arrRes[i] == '>') {
                // first one
                b=i;
            }
        }
        if (a == -1 || b == -1) {
            return "";
        }
        name = name.substring(a+1, b);
        name = name.substring(name.lastIndexOf('.') + 1);
        return name;
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {ElementType.TYPE})
    @interface GeneratorConfiguration {

        /** Controllers to generate */
        Class[] controllers();

        /** Resources output directory */
        String outputDirectory();

        /** Repo package name */
        String repoClassPackage();

        /** Api class package name */
        String apiClassPackage();

        /** Author for file header */
        String author();
    }
}
