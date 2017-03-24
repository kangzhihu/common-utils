package com.coldfire.support.spring;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/26 22:37
 * Email:kangzhihu@163.com
 * Descriptions:
 */
public class MapPropertyValues  extends MutablePropertyValues {
    private static final long serialVersionUID = 556111463980292229L;
    public static final String DEFAULT_PREFIX_SEPARATOR = "_";

    public MapPropertyValues(Map dataParams) {
        this(dataParams, null, null);
    }

    /**
     * Create new ServletRequestPropertyValues using the given prefix and
     * the default prefix separator (the underscore character "_").
     * @param dataParams HTTP request
     * @param prefix the prefix for parameters (the full prefix will
     * consist of this plus the separator)
     * @see #DEFAULT_PREFIX_SEPARATOR
     */
    public MapPropertyValues(Map<String, Object> dataParams, String prefix) {
        this(dataParams, prefix, DEFAULT_PREFIX_SEPARATOR);
    }

    /**
     * Create new ServletRequestPropertyValues supplying both prefix and
     * prefix separator.
     * @param dataParams HTTP request
     * @param prefix the prefix for parameters (the full prefix will
     * consist of this plus the separator)
     * @param prefixSeparator separator delimiting prefix (e.g. "spring")
     * and the rest of the parameter name ("param1", "param2")
     */
    public MapPropertyValues(Map dataParams, String prefix, String prefixSeparator) {
        super(getParametersStartingWith(dataParams, (prefix != null) ? prefix + prefixSeparator : null));
    }

    public static Map<String, Object> getParametersStartingWith(Map dataParams, String prefix) {
        Assert.notNull(dataParams, "requestParams must not be null");
        Iterator paramNames = dataParams.keySet().iterator();
        Map params = new TreeMap();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasNext()) {
            String paramName = (String) paramNames.next();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                Object values = dataParams.get(paramName);
                if (values == null) {
                    // Do nothing, no values found at all.
                }else{
                    if(values instanceof String[]){
                        if (((String[])values).length > 1) {
                            params.put(unprefixed, values);
                        }else {
                            params.put(unprefixed, ((String[])values)[0]);
                        }
                    }else{
                        params.put(unprefixed, values);
                    }
                }
            }
        }
        return params;
    }
}
