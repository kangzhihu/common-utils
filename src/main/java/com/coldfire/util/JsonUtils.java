package com.coldfire.util;

import com.coldfire.inter.CommonLogger;
import com.coldfire.support.jackson.CommonJsonModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang.StringUtils;

import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.coldfire
 * Time: 2016/12/11 21:19
 * Email:kangzhihu@163.com
 * Descriptions:对象序列化与反序列化工具类，pojo对象必须有getter对象才能正常反序列化
 */
public class JsonUtils {
    private static final transient CommonLogger dbLogger = LoggerUtils.getLogger(JsonUtils.class);

    private JsonUtils(){}

    /**
     * 解析json字符串为指定类型的pojo
     *
     * @param clazz pojo的类
     * @param json  json字符串
     * @return
     */
    public static <T> T readJsonToObject(Class<T> clazz, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//对于无setter的属性进行反序列化时不返回异常
        mapper.registerModule(CommonJsonModule.COMMON_MODULE);
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + LoggerUtils.getExceptionTrace(e, 15));
        }
        return null;
    }

    /**
     * 解析json字符串为List列表
     *
     * @param elementClass list中元素的类型
     * @param json         json字符串
     * @param <T>
     * @return
     */
    public static <T> T readJsonToObjectList(Class<T> elementClass, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//对于无setter的属性进行反序列化时不返回异常
        mapper.registerModule(CommonJsonModule.COMMON_MODULE);
        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, elementClass);
            return mapper.readValue(json, type);//使用type而不直接使用List.class是为了防止List中类复杂？
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + LoggerUtils.getExceptionTrace(e, 15));
        }
        return null;
    }

    /**
     * 解析json字符串为Map列表
     *
     * @param json
     * @return
     */
    public static Map readJsonToMap(String json) {
        if (StringUtils.isBlank(json)) {
            return new HashMap();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(CommonJsonModule.COMMON_MODULE);
        try {
            Map result = mapper.readValue(json, Map.class);
            return null == result ? new HashMap() :result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + LoggerUtils.getExceptionTrace(e, 15));
            return new HashMap();
        }
    }

    /**
     * 解析json字符串,可以解析复杂的类型数据
     *
     * @param type 类信息解析对象，传递泛型类T的完整类信息，<br/>
     *             exp：TypeReference ref = new TypeReference<List<Integer>>() { }.
     * @param json json字符串
     * @param <T>
     * @return
     */
    public static <T> T readJsonToObject(TypeReference<T> type, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//对于无setter的属性进行反序列化时不返回异常
        mapper.registerModule(CommonJsonModule.COMMON_MODULE);
        try {
            T result = mapper.readValue(json, type);
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + LoggerUtils.getExceptionTrace(e, 15));
        }
        return null;
    }

    /**
     * @param clazz
     * @param json
     * @param pns json字符串值解析规则
     * @param <T>
     * @return
     */
    public static <T> T readJsonToObject(Class<T> clazz, String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(CommonJsonModule.COMMON_MODULE);
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
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(CommonJsonModule.COMMON_MODULE);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        try {
            T result = mapper.readValue(json, type);
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + LoggerUtils.getExceptionTrace(e, 15));
        }
        return null;
    }

    public static <T> List<T> readJsonToObjectList(Class<T> clazz, String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(CommonJsonModule.COMMON_MODULE);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> result = mapper.readValue(json, type);
            return result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + LoggerUtils.getExceptionTrace(e, 15));
        }
        return null;
    }

    public static Map readJsonToMap(String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return new HashMap();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(CommonJsonModule.COMMON_MODULE);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        try {
            Map result = mapper.readValue(json, Map.class);
            return null == result ? new HashMap() :result;
        } catch (Exception e) {
            dbLogger.error("json:" + StringUtils.substring(json, 0, 500) + "\n" + LoggerUtils.getExceptionTrace(e, 15));
            return new HashMap();
        }
    }

    /**
     * 序列化对象
     *
     * @param object object to serialize
     * @return
     */
    public static String writeObjectToJson(Object object) {
        return writeObjectToJson(object, false);
    }

    /**
     * 序列化对象到指定OutputStream
     *
     * @param os          需要序列化的OutputStream流
     * @param object      object to serialize
     * @param excludeNull
     */
    public static String writeObjectToStream(OutputStream os, Object object, boolean excludeNull) {
        return writeObjectToJson(object, os, null, excludeNull);
    }

    /**
     * 序列化对象到指定Writer
     *
     * @param writer      字符流writer
     * @param object      object to serialize
     * @param excludeNull 不序列化null
     */
    public static String writeObjectToWriter(Writer writer, Object object, boolean excludeNull) {
        return writeObjectToJson(object, null, writer, excludeNull);
    }

    /**
     * 序列化对象
     *
     * @param object      object to serialize
     * @param excludeNull 不序列化null
     * @return
     */
    public static String writeObjectToJson(Object object, boolean excludeNull) {
        return writeObjectToJson(object, null, null, excludeNull);
    }

    private static String writeObjectToJson(Object object, OutputStream os, Writer writer, boolean excludeNull) {
        if (null == object) return null;
        if (object instanceof Map) {
            try {
                ((Map) object).remove(null);
            } catch (Exception e) {
                dbLogger.warn("remove map null exception");
            }//why use try...catch..?
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper disable = mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);//禁止将map中null值进行序列化，减少数据量
        mapper.getFactory().disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);//不自动关闭流
        if (excludeNull) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        try {
            mapper.registerModule(CommonJsonModule.COMMON_MODULE);
            if (null != os) {
                mapper.writeValue(os, object);
            } else if (null != writer) {
                mapper.writeValue(writer, object);
            } else {
                String data = mapper.writeValueAsString(object);
                return data;
            }
        } catch (Exception e) {
            dbLogger.error("object:" + object + "\n" + LoggerUtils.getExceptionTrace(e, 15));
        }
        return null;
    }

    /**
     * 将map转化为json字符串
     * @param dataMap
     * @return
     */
    public static String writeMapToJson(Map<String, String> dataMap){
        if(dataMap==null) return null;
        if(dataMap instanceof HashMap){
            try{dataMap.remove(null);}catch(Exception e){}
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

    /**
     * 向map格式的json字符串中添加键值对
     * @param json
     * @param key
     * @param value
     * @return 添加后的jso字符串
     */
    public static String addJsonKeyValue(String json, String key, String value){
        Map info = readJsonToMap(json);
        info.put(key, value);
        return writeMapToJson(info);
    }

    /**
     * 删除map格式的json字符串中的指定值
     * @param json
     * @param key
     * @return 移除后的json字符串
     */
    public static String removeJsonKeyValue(String json, String key){
        Map info = readJsonToMap(json);
        info.remove(key);
        return writeMapToJson(info);
    }

    /**
     * 获取map格式的json字符串中的指定key的值
     * @param json
     * @param key
     * @return key的value
     */
    public static String getJsonValueByKey(String json, String key){
        Map<String, String> info = readJsonToMap(json);
        return info.get(key);
    }
}
