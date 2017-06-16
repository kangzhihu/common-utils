package com.meta.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhihu
 * Time: 2016/12/11 21:19
 * Email:kangzhihu@163.com
 * Descriptions:对象序列化与反序列化工具类，pojo对象必须有getter对象才能正常反序列化
 */

/**
 * add JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
 *
 */
public class JsonUtils {
    private static final transient Logger dbLogger = LoggerFactory.getLogger(JsonUtils.class);

    public static <T> T readJsonToObject(Class<T> clazz, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        try {
            T result = mapper.readValue(json, clazz);
            return result;
        } catch (Exception e) {
            dbLogger.error(StringUtils.substring(json, 0, 500), e, 15);
        }
        return null;
    }

    public static <T> T readJsonToObject(TypeReference<T> type, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        try {
            T result = mapper.readValue(json, type);
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + e.getMessage());
        }
        return null;
    }

    public static <T> List<T> readJsonToObjectList(Class<T> clazz, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> result = mapper.readValue(json, type);
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static Map readJsonToMap(String json) {
        if (StringUtils.isBlank(json)) {
            return new HashMap();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        try {
            Map result = mapper.readValue(json, Map.class);
            if (result == null)
                result = new HashMap();
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + e.getMessage());
            return new HashMap();
        }

    }

    //-----------------------------------------------------------------
    public static <T> T readJsonToObject(Class<T> clazz, String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        try {
            T result = mapper.readValue(json, clazz);
            return result;
        } catch (Exception e) {
            dbLogger.error(StringUtils.substring(json, 0, 500), e, 15);
        }
        return null;
    }

    public static <T> T readJsonToObject(TypeReference<T> type, String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        try {
            T result = mapper.readValue(json, type);
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + e.getMessage());
        }
        return null;
    }

    public static <T> List<T> readJsonToObjectList(Class<T> clazz, String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }

        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> result = mapper.readValue(json, type);
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static Map readJsonToMap(String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return new HashMap();
        }
        ObjectMapper mapper = new ObjectMapper();
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        try {
            Map result = mapper.readValue(json, Map.class);
            if (result == null)
                result = new HashMap();
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + e.getMessage());
            return new HashMap();
        }
    }

    public static String writeObjectToJson(Object object) {
        return writeObjectToJson(object, false);
    }

    public static void writeObjectToStream(OutputStream os, Object object, boolean excludeNull) {
        writeObject(object, os, null, excludeNull);
    }

    public static void writeObjectToWriter(Writer writer, Object object, boolean excludeNull) {
        writeObject(object, null, writer, excludeNull);
    }

    public static String writeObjectToJson(Object object, boolean excludeNull) {
        return writeObject(object, null, null, excludeNull);
    }

    @SuppressWarnings("rawtypes")
    private static String writeObject(Object object, OutputStream os, Writer writer, boolean excludeNull) {
        if (object == null)
            return null;
        if (object instanceof Map) {
            try {
                ((Map) object).remove(null);
            } catch (Exception e) {
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        //不自动关闭流
        mapper.getFactory().disable(Feature.AUTO_CLOSE_TARGET);
        if (excludeNull) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }

        try {
            if (os != null) {
                mapper.writeValue(os, object);
            } else if (writer != null) {
                mapper.writeValue(writer, object);
            } else {
                String data = mapper.writeValueAsString(object);
                return data;
            }
        } catch (Exception e) {
            dbLogger.error("object:" + object + "\n" + e.getMessage());
        }
        return null;
    }

    public static String writeMapToJson(Map<String, String> dataMap) {
        if (dataMap == null)
            return null;
        if (dataMap instanceof HashMap) {
            try {
                dataMap.remove(null);
            } catch (Exception e) {
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        try {
            String data = mapper.writeValueAsString(dataMap);
            return data;
        } catch (Exception e) {
            dbLogger.error("", e);
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String addJsonKeyValue(String json, String key, String value) {
        Map info = readJsonToMap(json);
        info.put(key, value);
        return writeMapToJson(info);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String removeJsonKeyValue(String json, String key) {
        Map info = readJsonToMap(json);
        info.remove(key);
        return writeMapToJson(info);
    }

    public static String getJsonValueByKey(String json, String key) {
        @SuppressWarnings("unchecked")
        Map<String, String> info = readJsonToMap(json);
        return info.get(key);
    }

}
