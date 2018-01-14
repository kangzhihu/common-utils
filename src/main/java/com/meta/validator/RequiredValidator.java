package com.meta.validator;

import com.meta.bean.FieldUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Author: zhihu.kang<br/>
 * Data: 2018-01-14&nbsp;0:11<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;Required属性注解校验器类<br/><br/>
 */
public class RequiredValidator extends AbstractFiledValidator{
    /**
     * Required属性注解校验器，需要配合{@link com.meta.annotation.Required}使用
     * @param object
     * @param filed
     * @param annotation
     */
    @Override
    public void validate(Object object, Field filed, Annotation annotation) {
        Object value = getFiledValue(object,filed);
        if(value == null || (value instanceof String && StringUtils.isBlank((String)value))){
            throw new IllegalArgumentException("["+ FieldUtil.getFieldFullName(filed)+"]必填值为空");
        }
    }
}
