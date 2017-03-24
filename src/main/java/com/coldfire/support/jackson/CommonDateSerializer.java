package com.coldfire.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/14 23:02
 * Email:kangzhihu@163.com
 * Descriptions:自定义序列化日期类，ObjectMapper 可用来序列化和反序列化对象，JsonParser 是用来反序列化的，JsonGenerator 是序列化的。
 *
 */

public class CommonDateSerializer extends JsonSerializer<Date> {

    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = null;
        if (date instanceof Timestamp){
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }else{
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        String formattedDate = formatter.format(date);
        jsonGenerator.writeString(formattedDate);
    }
}
