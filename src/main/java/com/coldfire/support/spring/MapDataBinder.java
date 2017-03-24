package com.coldfire.support.spring;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.DataBinder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/26 22:05
 * Email:kangzhihu@163.com
 * Descriptions:自定义数据绑定类，该类依赖于spring框架
 */
public class MapDataBinder extends DataBinder {
    public MapDataBinder(Object target){
        super(target);
        regEditor();
    }

    public MapDataBinder(Object target,String objectName){
        super(target,objectName);
        regEditor();
    }

    private void regEditor(){
        //线程不安全，需要重新生成---why???
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, true);
        TimestampEditor timestampEditor = new TimestampEditor();
        dateFormat.setLenient(false);
        //TODO:timestampFormat.setLenient(false);
        this.registerCustomEditor(Date.class, null, dateEditor);
        //注册自定义编辑器
        this.registerCustomEditor(Timestamp.class, null, timestampEditor);
    }

    /**
     * 将数据绑定到target上
     * @param dataMap
     */
    public void bind(Map dataMap) {
        MutablePropertyValues mpvs = new MapPropertyValues(dataMap);
        doBind(mpvs);
    }
}
