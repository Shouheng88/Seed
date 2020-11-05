package com.seed.base.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 15:18
 */
public final class JsonMapper {

    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(JsonInclude.Include include) {
        this(include, DATE_FORMAT);
    }

    public JsonMapper(JsonInclude.Include include, DateFormat dateFormat) {
        mapper = new ObjectMapper();
        if (dateFormat != null) {
            mapper.setDateFormat(dateFormat);
        }
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * Get a mapper that only output the non-null and non-empty properties to json.
     * Suggested to be used in outer interfaces.
     *
     * @return mapper object
     */
    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * Get a mapper that only output changed properties to json, the most saving one.
     * Suggested to be used in inside interfaces.
     *
     *
     * @return mapper object
     */
    public static JsonMapper nonDefaultMapper() {
        return new JsonMapper(JsonInclude.Include.NON_DEFAULT);
    }

    /**
     * Get a mapper that only output the non-null properties to json.
     *
     * @return mapper object
     */
    public static JsonMapper nonNullMapper() {
        return new JsonMapper(JsonInclude.Include.NON_NULL);
    }

    /**
     * Get a mapper that the time format was long.
     *
     * @return mapper object
     */
    public static JsonMapper nonNullAndDefaultDateFormatMapper() {
        return new JsonMapper(JsonInclude.Include.NON_NULL, null);
    }

    /**
     * The object might by POJO, collection or array. If the object was null,
     * "null" will be returned, if it's empty list or array, "[]" will be returned.
     *
     * @param object object
     * @return       json string
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("Write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * Transfer object to json with inner type.
     *
     * @param object     the object
     * @param outerClass the outer class
     * @param innerClass the inner class
     * @return           json string
     */
    public String toJsonWithInnerType(Object object, Class outerClass, Class innerClass) {
        try {
            return mapper.writerFor(this.createCollectionType(outerClass, innerClass)).writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("Write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * Get object from JSON string. Mainly used to get POJO or simple collection like List&lt;String&gt;.
     * As for complicated JSON string, use {@link #fromJson(String, JavaType)} instead.
     * <p/>
     * If the JSON string was "null", Null will be returned.
     * If the JSON string was "[]", empty list will be returned.
     * <p/>
     * @see #fromJson(String, JavaType)
     * @see #fromJson(String, Class, Class)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StrUtil.isEmpty(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("Parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * Get complicated collection from JSON string, like List&lt;Bean&gt;.
     *
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StrUtil.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("Parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * Get complicated collection from JSON string, like List&lt;Bean&gt;.
     *
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, Class<T> outClass, Class<?> innerClass) {
        if (StrUtil.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) mapper.readValue(jsonString, this.createCollectionType(outClass, innerClass));
        } catch (IOException e) {
            logger.warn("Parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * Construct collection. For example:
     * ArrayList&lt;MyBean&gt;: constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap&lt;String, MyBean&gt;: constructCollectionType(HashMap.class,String.class, MyBean.class)
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * Update object bt json string. This method will only change the properties existed in json string.
     */
    public <T> T update(String jsonString, T object) {
        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            logger.warn("Update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * Output object in JSONP format.
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * Set read and write enums by its {@link Enum#toString()} and {@link Enum#valueOf(Class, String)} method.
     */
    public void enableEnumUseToString() {
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    /**
     * Get current mapper for further operation.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}
