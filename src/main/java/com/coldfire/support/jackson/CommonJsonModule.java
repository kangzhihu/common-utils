package com.coldfire.support.jackson;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/14 23:31
 * Email:kangzhihu@163.com
 * Descriptions: 扩展module，将自定义序列化方法类注册到ObjectMapper实例上的SimpleModule接口实现。
 * 对JSON 中属性值处理方式定制类的注册。
 */
public class CommonJsonModule  extends SimpleModule {
    private static final long serialVersionUID = -764440920967424296L;
    public static CommonJsonModule COMMON_MODULE = new CommonJsonModule();

    public static FistLetterUpperCasePropertyNamingStrategy FIST_LETTER_UPPER_PROPERTY_STRATEGY = new FistLetterUpperCasePropertyNamingStrategy();

    private CommonJsonModule(){
        //注册特定的类型序列化与反序列化处理类（格式定制）
        addDeserializer(Timestamp.class, CommonDeserializers.timestampDeserializer);
        addDeserializer(Date.class, CommonDeserializers.dateDeserializer);
        addDeserializer(java.sql.Date.class, CommonDeserializers.sqlDateDeserializer);
        addSerializer(Timestamp.class, new CommonDateSerializer());
        addSerializer(Date.class, new CommonDateSerializer());
        addSerializer(java.sql.Date.class, new CommonDateSerializer());
    }

    //属性值首字母大写处理定制（属性值定制）
    public static class FistLetterUpperCasePropertyNamingStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        private static final long serialVersionUID = 471380582816394339L;

        @Override
        public String translate(String propertyName) {
            if ("objectName".equals(propertyName)) {
                return propertyName;
            }
            String name = propertyName.replaceAll("^\\w", propertyName.toUpperCase().substring(0, 1));
            return name;
        }
    }
}
