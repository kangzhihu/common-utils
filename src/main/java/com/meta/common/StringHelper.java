package com.meta.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-07-18&nbsp;23:00<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;字符串工具类<br/><br/>
 */
public class StringHelper {
    private static final transient Logger logger = LoggerFactory.getLogger(StringHelper.class);

    /**
     * 截取字符--动态截取制定宽度的字符
     * @param text
     * @param start 开始位置
     * @param length 截取字节byte个数（中文个数*3）
     * @return
     */
    public static String substring(String text, int start, int length) {
        if (text == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int currentLength = 0;
        char[] chars = text.toCharArray();
        for(int index = start;index<chars.length;index++){
            char c = chars[index];
            currentLength += String.valueOf(c).getBytes(StandardCharsets.UTF_8).length;
            if (currentLength <= length) {
                sb.append(c);
            } else {
                break;
            }
        }
        return sb.toString();
    }

}
