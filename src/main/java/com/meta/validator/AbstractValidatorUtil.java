package com.meta.validator;

import com.meta.annotation.EnumField;
import com.meta.annotation.Required;
import com.meta.bean.FieldUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: zhihu.kang<br/>
 * Data: 2018-01-14&nbsp;21:11<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;注解校验工具类<br/><br/>
 */
public class AbstractValidatorUtil {

    /**
     * 注解校验器的Map映射
     */
    private static Map<Class<? extends Annotation>, AbstractFiledValidator> filedValidatorMap = new HashMap<>();

    /**
     * 缓存已解析出的Field对应的注解列表
     */
    private static Map<Field, Annotation[]> annotationOfFieldMap = new HashMap<>();

    /**
     * 缓存被校验类的Fields
     */
    private static Map<Class<?>, Field[]> classFieldMap = new HashMap<>();

    static {
        filedValidatorMap.put(Required.class, new RequiredValidator());
        filedValidatorMap.put(EnumField.class, new EnumFieldValidator());
    }

    /**
     * 若需要递归校验，则需要自己赋值，注意检查的深度
     */
    private static final String GROUP_ID = "com.";

    public static void validate(Object obj) {
        if (obj == null) {
            throw new NullPointerException("被校验对象不能为空");
        }
        Field[] fields = classFieldMap.get(obj.getClass());
        if (fields == null) {
            fields = FieldUtil.getAllFields(obj);
            classFieldMap.put(obj.getClass(), fields);
        }
        for (Field field : fields) {
            /**优先从缓存map中获取已缓存的队形field注解标识*/
            Annotation[] annotations = annotationOfFieldMap.get(field);
            if (annotations == null) {
                field.setAccessible(true);
                annotations = field.getAnnotations();
                annotationOfFieldMap.put(field, annotations);
            }
            for (Annotation annotation : annotations) {
                if (filedValidatorMap.containsKey(annotation)) {
                    AbstractFiledValidator validator = filedValidatorMap.get(annotation);
                    validator.validate(obj, field, annotation);
                }
            }
            Object fieldValue = FieldUtil.getFieldValue(obj, field);
            //对于自定义对象进行属性递归校验
            attrObjectCheck(fieldValue, field);

            //属性为list 对象校验
            attrListCheck(fieldValue);
        }

    }

    //对于自定义对象进行属性递归校验
    private static void attrObjectCheck(Object fieldValue, Field field) {
        if (fieldValue != null && field.getType().getName().indexOf(GROUP_ID) > -1) {
            validate(fieldValue);
        }
    }

    //属性为list 对象校验
    private static void attrListCheck(Object fieldValue) {
        if (fieldValue != null && fieldValue instanceof List) {
            for (Object o : (List) fieldValue) {
                if (o != null && o.getClass().getName().indexOf(GROUP_ID) > -1) {
                    validate(fieldValue);
                }
            }
        }
    }


}
