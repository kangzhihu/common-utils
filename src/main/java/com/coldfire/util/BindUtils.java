package com.coldfire.util;

import com.coldfire.support.spring.MapDataBinder;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/26 21:43
 * Email:kangzhihu@163.com
 * Descriptions:绑定工具类。
 * 该类依赖于org.springframework.spring框架
 */
public class BindUtils {

    /**
     * 将数据绑定到指定bean上
     * @param bean
     * @param dataParam map数据
     * @return
     */
    public static BindException bindData(Object bean, Map dataParam){
        MapDataBinder binder = new MapDataBinder(bean);
        binder.bind(dataParam);
        BindException errors = new BindException(binder.getBindingResult());
        return errors;
    }

    public static BindException bindData(Object bean , Map map, String[] allowedFields ,String[] disallowedFields){
        MapDataBinder binder = new MapDataBinder(bean);
        if(allowedFields!=null && allowedFields.length>0){
            binder.setAllowedFields(allowedFields);
        }
        if(disallowedFields!=null && disallowedFields.length>0){
            binder.setDisallowedFields(disallowedFields);
        }
        binder.bind(map);
        BindException errors = new BindException(binder.getBindingResult());
        return errors;
    }

    /**
     * 将指定的数据绑定到bean上
     * @param bean
     * @param dataParam allow为true时，只绑定fields中包含的数据，否则将fields中的数据不绑定
     * @param allow 绑定/排除fields中的数据
     * @param fields
     * @return
     */
    public static BindException bind(Object bean ,Map dataParam, boolean allow, String[] fields){
        Map bindData = null;
        if(allow) bindData = new HashMap();
        else bindData = new HashMap(dataParam);
        for(String field: fields){
            Object value = dataParam.get(field);
            if(allow) {
                if(dataParam.containsKey(field)) bindData.put(field, value);
            }else{
                bindData.remove(field);
            }
        }
        return bindData(bean, bindData);
    }

}
