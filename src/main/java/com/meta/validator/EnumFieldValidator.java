package com.meta.validator;

import com.meta.annotation.EnumField;
import com.meta.bean.FieldUtil;
import com.meta.common.EnumUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Author: zhihu.kang<br/>
 * Data: 2018-01-13&nbsp;23:48<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;枚举类属性注解校验器类<br/><br/>
 */
public class EnumFieldValidator extends AbstractFiledValidator {

    /**
     * 枚举类属性注解校验器，需要配合{@link EnumField}使用
     * @param object
     * @param filed
     * @param annotation
     */
    @Override
    public void validate(Object object, Field filed, Annotation annotation) {
        String value = (String) getFiledValue(object, filed);
        EnumField enumFieldAnno = (EnumField) annotation;
        //不为空的情况需要校验枚举的有效性
        if (value != null) {
            try {
                Object obj = EnumUtil.findEnumByCode(enumFieldAnno.clazz(),value,enumFieldAnno.attr());
                if(obj == null){
                    throw new IllegalArgumentException("["+ FieldUtil.getFieldFullName(filed)+"]非法的枚举值！");
                }
            }catch (Exception e){
                throw new IllegalArgumentException("["+ FieldUtil.getFieldFullName(filed)+"]获取对应的枚举值错误！");
            }
        }
    }
}
