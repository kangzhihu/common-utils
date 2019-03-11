package com.meta.dto;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
com.meta.annotation.Mask;
/**
* 关键字屏蔽Builder，类ToString实现
*/
public class ToStringBuilder extends ReflectionToStringBuilder {

    public SecureToStringBuilder(final Object object, final ToStringStyle style) {
        super(object, style);
    }

    public static String toString(final Object object, final ToStringStyle style) {
        return (new SecureToStringBuilder(object, style)).toString();
    }

    @Override
    protected void appendFieldsIn(final Class clazz) {
        if (clazz.isArray()) {
            this.reflectionAppendArray(this.getObject());
            return;
        }

        final Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (final Field field : fields) {
            final String fieldName = field.getName();
            if (this.accept(field)) {
                try {
                    // Warning: Field.get(Object) creates wrappers objects
                    // for primitive types.
                    Object fieldValue = this.getValue(field);

                    // 脱敏处理，需要脱敏的字段加Sensitive注解
                    Sensitive sensitive = field.getAnnotation(Mask.class);
                    if (sensitive != null) {
                        fieldValue = sensitive.type().mask.apply(fieldValue);
                    }

                    this.append(fieldName, fieldValue);
                } catch (final IllegalAccessException ex) {
                    //this can't happen. Would get a Security exception
                    // instead
                    //throw a runtime exception in case the impossible
                    // happens.
                    throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
                }
            }
        }
    }
  
  //外部
    public static void main(String[] args) {
     abstract class BaseDomain {

        @Override
        public String toString() {
            return this.toString(ToStringStyle.SHORT_PREFIX_STYLE);
        }

        /** 指定ToStringStyle的序列化方法 */
        public String toString(ToStringStyle toStringStyle) {
            return SecureToStringBuilder.toString(this, toStringStyle);
        }

    }
  }

}
