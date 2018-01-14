package com.meta.common;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Author: zhihu.kang<br/>
 * Data: 2018-01-13&nbsp;23:55<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;枚举工具类<br/><br/>
 */
public class EnumUtil {

    public static Object findEnumByCode(Class<?> clazz,String code,String codeField) throws Exception{
        for(Object enumObj:clazz.getEnumConstants()){
            if(StringUtils.equals(BeanUtils.getSimpleProperty(enumObj,codeField),code)){
                return enumObj;
            }
        }
        return null;
    }

    public static <T> String getCode(T enumObj,String codeField)throws Exception{
        return BeanUtils.getSimpleProperty(enumObj,codeField);
    }
}
