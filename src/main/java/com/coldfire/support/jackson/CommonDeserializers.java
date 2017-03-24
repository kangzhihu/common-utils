package com.coldfire.support.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/14 23:09
 * Email:kangzhihu@163.com
 * Descriptions:自定义反序列化类，方便对特定格式的数据进行处理，ObjectMapper 可用来序列化和反序列化对象，JsonParser 是用来反序列化的，JsonGenerator 是序列化的
 */
public class CommonDeserializers {

    public static final CommonDateDeserializer dateDeserializer = new CommonDateDeserializer();
    public static final CommonTimestampDeserializer timestampDeserializer = new CommonTimestampDeserializer();
    public static final CommonSqlDateDeserializer sqlDateDeserializer = new CommonSqlDateDeserializer();

    //自定义反序列化Date类
    private static class CommonDateDeserializer extends DateDeserializers.DateDeserializer {
        private static final long serialVersionUID = 2838324514546099039L;

        @Override
        protected java.util.Date _parseDate(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            try {
                if (t == JsonToken.VALUE_NUMBER_INT) {
                    return new java.util.Date(jp.getLongValue());
                }
                if (t == JsonToken.VALUE_STRING) {
                    /*
                     * As per [JACKSON-203], take empty Strings to mean null
					 */
                    String str = jp.getText().trim();
                    if (str.length() == 0) {
                        return null;
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    return formatter.parse(str);
                }
                throw ctxt.mappingException(_valueClass);
            } catch (ParseException e) {
                throw ctxt.mappingException(_valueClass);
            } catch (IllegalArgumentException iae) {
                throw ctxt.weirdStringException(null, _valueClass, "not a valid representation (error: " + iae.getMessage() + ")");
            }
        }
    }

    //自定义反序列化Timestamp类
    private static class CommonTimestampDeserializer extends DateDeserializers.TimestampDeserializer {
        private static final long serialVersionUID = -8049402816880830231L;

        @Override
        protected java.util.Date _parseDate(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            try {
                if (t == JsonToken.VALUE_NUMBER_INT) {
                    return new java.util.Date(jp.getLongValue());
                }
                if (t == JsonToken.VALUE_STRING) {
                    /*
                     * As per [JACKSON-203], take empty Strings to mean null
					 */
                    String str = jp.getText().trim();
                    if (str.length() == 0) {
                        return null;
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return formatter.parse(str);
                }
                throw ctxt.mappingException(_valueClass);
            } catch (ParseException e) {
                throw ctxt.mappingException(_valueClass);
            } catch (IllegalArgumentException iae) {
                throw ctxt.weirdStringException(null, _valueClass, "not a valid representation (error: " + iae.getMessage() + ")");
            }
        }
    }

    //自定义反序列化SqlDate类
    private static class CommonSqlDateDeserializer extends DateDeserializers.SqlDateDeserializer {
        private static final long serialVersionUID = -8049402816880830231L;

        @Override
        protected java.sql.Date _parseDate(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            try {
                if (t == JsonToken.VALUE_NUMBER_INT) {
                    return new java.sql.Date(jp.getLongValue());
                }
                if (t == JsonToken.VALUE_STRING) {
                    /*
					 * As per [JACKSON-203], take empty Strings to mean null
					 */
                    String str = jp.getText().trim();
                    if (str.length() == 0) {
                        return null;
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    return new java.sql.Date(formatter.parse(str).getTime());
                }
                throw ctxt.mappingException(_valueClass);
            } catch (ParseException e) {
                throw ctxt.mappingException(_valueClass);
            } catch (IllegalArgumentException iae) {
                throw ctxt.weirdStringException(null, _valueClass, "not a valid representation (error: " + iae.getMessage() + ")");
            }
        }
    }

}
