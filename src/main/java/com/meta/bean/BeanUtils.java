package com.meta.bean;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Description:<br/>
 * Created by zhihu.kang <br/>
 * Time: 22:20<br/>
 * Email:kangzhihu@163.com<br/>
 */
public class BeanUtils {
    private static transient Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    public static boolean isEmpty(Object obj){
        if(obj == null){
            return true;
        }
        if(obj instanceof String){
            return ((String) obj).isEmpty();
        }
        if(obj instanceof Collection){
            return ((Collection) obj).isEmpty();
        }
        if(obj instanceof Map){
            return ((Map) obj).isEmpty();
        }
        if(obj.getClass().isArray()){
            return Array.getLength(obj) == 0;
        }
        if(obj instanceof CharSequence){
            return ((CharSequence) obj).length() == 0;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj){
        return !isEmpty(obj);
    }
    /**
     * 设置bean对象的属性值，仅支持简单对象和Collection对象
     *
     * @param bean
     * @param property
     * @param value
     */
    public static void setProperty(final Object bean, final String property, final Object value) {
        try {
            if (bean instanceof Collection) {
                ((Collection) bean).stream().forEach((obj) -> {
                    setSimpleBeanProperty(obj, property, value);
                });
            } else {
                setSimpleBeanProperty(bean, property, value);
            }
        } catch (Exception e) {

        }
    }

    private static void setSimpleBeanProperty(final Object bean, final String property, final Object value) {
        try {
            PropertyUtils.setProperty(bean, property, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象列表中所有对象的属性集合
     * @param beanList 对象列表
     * @param field 属性名
     * @param distinct 是否去重
     * @param <T> 返回的属性类型
     * @return
     */
    public static <T> List<T> getBeanProperty(List beanList, String field, boolean distinct) {
        List<T> newList = new ArrayList();
        if (beanList == null || beanList.isEmpty()) {
            return newList;
        }
        beanList.stream().forEach((bean) -> {
            try {
                Map map = org.apache.commons.beanutils.BeanUtils.describe(bean);
                if (map.get(field) != null) {
                    newList.add((T) map.get(field));
                }
            } catch (Exception e) {
                logger.error("unexpected exception on describe operation!");
            }
        });
        if (distinct) {
            return new ArrayList(new HashSet(newList));
        }
        return newList;
    }

}
