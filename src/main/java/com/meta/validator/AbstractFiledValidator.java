package com.meta.validator;

import com.meta.bean.FieldUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Author: zhihu.kang<br/>
 * Data: 2018-01-13&nbsp;23:23<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;filed校验抽象类<br/><br/>
 */
public abstract class AbstractFiledValidator {

    public abstract void validate(Object object, Field filed, Annotation annotation);

    protected static Object getFiledValue(Object object,Field field){
        Object value = null;
        try {
            field.setAccessible(true);
            value = field.get(object);
        }catch (Exception e){
            throw new  RuntimeException("["+ FieldUtil.getFieldFullName(field)+"]取值发生异常！");
        }
        return value;
    }
}
