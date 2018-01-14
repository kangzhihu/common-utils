package com.meta.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: zhihu.kang<br/>
 * Data: 2018-01-13&nbsp;23:08<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;枚举赋值校验<br/><br/>
 * <pre>{@code
 * Class Persion{
 *     private String name;
 *     {@code @EnumField(clazz=SexEnum.class,attr="sex")}
 *     private String sex;</br>
 * }
 *
 *  }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface EnumField {
    /**
     * 枚举类
     */
    Class<?> clazz();

    /**
     * 对应枚举的哪个属性
     */
    String attr();
}
