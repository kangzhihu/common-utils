package com.coldfire.support.spring;

import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/26 22:14
 * Email:kangzhihu@163.com
 * Descriptions:自定义Timestamp编辑器，
 * spring实际是先利用java.beans.PropertyEditor中的setAdText方法来把string格式的输入转换为bean属性。
 */
public class TimestampEditor extends PropertyEditorSupport {
    private String full_timeformat = "yyyy-MM-dd HH:mm:ss";

    public TimestampEditor(){
    }

    public TimestampEditor(String dateFormat){
        this.full_timeformat = dateFormat;
    }
    @Override
    public void setAsText(String value) {
        if(StringUtils.isBlank(value)){
            this.setValue(null);
            return;
        }
        String formtText = full_timeformat;
        if(value.length()<=full_timeformat.length()){
            formtText = StringUtils.substring(full_timeformat, 0, value.length());
        }
        Timestamp date = format(formtText, value);
        this.setValue(date);
    }

    protected Timestamp format(String formtText, String value){
        try {
            DateFormat format = new SimpleDateFormat(formtText);
            Timestamp date = new Timestamp(format.parse(value).getTime());
            return date;
        } catch (ParseException e) {
        }
        return null;
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? new SimpleDateFormat(full_timeformat).format(value) : "");
    }
}
