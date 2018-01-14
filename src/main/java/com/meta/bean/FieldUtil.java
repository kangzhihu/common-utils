package com.meta.bean;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;

/**
 * Author: zhihu.kang<br/>
 * Data: 2018-01-13&nbsp;23:30<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;属性操作类<br/><br/>
 */
public class FieldUtil {

    /**
     * 获取类属性全名称
     *
     * @param field
     * @return
     */
    public static String getFieldFullName(Field field) {
        return field.getDeclaringClass().getName() + "." + field.getName();
    }

    /**
     * 从指定类型中获查找到的第一个Filed(包含父类filed)
     *
     * @param clazz
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static Field getFiledByClass(Class<?> clazz, String fieldName) throws Exception {
        if (clazz == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (StringUtils.equals(field.getName(), fieldName)) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 获取对象所有的属性
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getFields();
        while (clazz.getSuperclass() != null) {
            fields = (Field[]) ArrayUtils.addAll(fields, (clazz = clazz.getSuperclass()).getFields());
        }
        return fields;
    }

    /**
     * 设置对象的属性值
     * @param object
     * @param fieldName
     * @param fieldValue
     * @return
     */
    public static boolean setFieldValue(Object object,String fieldName,String fieldValue){
       try {
           Class<?> clazz = object.getClass();
           Field field = getFiledByClass(clazz,fieldName);
           field.setAccessible(true);
           field.set(object,fieldValue);
       }catch (Exception e){
           return false;
       }
        return true;
    }

    public static Object getFieldValue(Object object,Field field){
        Object value = null;
        try {
            field.setAccessible(true);
            value = field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("["+getFieldFullName(field)+"]反射取值发生异常");
        }
        return value;
    }
}
