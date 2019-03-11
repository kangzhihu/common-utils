package com.meta.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * Author: zhihu.kang<br/>
 * Data: 2018-01-13&nbsp;22:20<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;ToString类，包装toString()方法<br/><br/>
 * 若需要定制toString()则继承本类
 * 主要服务于Vo层或者Model层日志显示
 */
public abstract class ToString implements Serializable {
    private static final long serialVersionUID = -7861822564831477896L;

    //打印时忽略的属性
    private static final String[] EXCLUDE_FILED_NAMES = new String[]{"style","serialVersionUID"};

    private ToStringStyle style = new MaskedToStringStyle();

    private String mask = "******";

    /**
     * 屏蔽字段列表
     * @param
     */
    public abstract List<String> maskFileds();

    /**
     * 设置屏蔽字段样式
     * @param mask
     */
    public void setMaskStyle(String mask){
        this.mask = mask;
    }

    @Override
    public final String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this,style);
        builder.setExcludeFieldNames(EXCLUDE_FILED_NAMES);
        return super.toString();
    }

    private class MaskedToStringStyle extends ToStringStyle {

        private static final long serialVersionUID = 3067662000895048796L;


        {
            setUseShortClassName(true);
            setUseIdentityHashCode(true);
        }

        @Override
        protected void appendInternal(StringBuffer buffer, String fieldName, Object value, boolean detail) {
            if (maskFileds().contains(fieldName)) {
                buffer.append(mask);
            } else {
                super.appendInternal(buffer, fieldName, value, detail);
            }
        }
    }
}
