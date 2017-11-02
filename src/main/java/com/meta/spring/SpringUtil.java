package com.meta.spring;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-11-02&nbsp;22:37<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;Spring 工具类<br/><br/>
 */
public class SpringUtil implements ApplicationContextAware {

    private ApplicationContext appContext;

    public SpringUtil() {
    }
    private static class ContextHolder {
        private static final SpringUtil instance = new SpringUtil();
    }
    public static <T> T getBean(String name, Class<T> requiredType) {
        return ContextHolder.instance.appContext.getBean(name, requiredType);
    }

    public static <T> T getBean(String name) {
        return (T)ContextHolder.instance.appContext.getBean(name);
    }

    public void setApplicationContext(ApplicationContext appContext) {
        ContextHolder.instance.appContext = appContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        String beanName = StringUtils.EMPTY;
        String className = requiredType.getSimpleName();
        if (StringUtils.isNotBlank(className)) {
            beanName = new StringBuffer(className.length()).append(Character.toLowerCase(className.charAt(0)))
                    .append(className.substring(1)).toString();
        }
        return getBean(beanName, requiredType);
    }

}
