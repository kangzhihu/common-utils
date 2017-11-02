package com.meta.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLEncoder {
    private static final Logger logger = LoggerFactory.getLogger(URLEncoder.class);

    public static String encode(String v, String enc) {
        String ret = "";
        if (v == null)
            return ret;

        try {
            ret = java.net.URLEncoder.encode(v, enc);
        } catch (UnsupportedEncodingException e) {
            logger.error("URLEncoder encode is error.value={}, enc={}", v, enc, e);
        }
        return ret;
    }

    public static String encode(String v) {
        if (v == null)
            return "";

        return java.net.URLEncoder.encode(v);
    }

    public static String decode(String v) {
        return java.net.URLDecoder.decode(v);
    }

    public static String decode(String v, String enc) {
        String ret = "";
        if (v == null)
            return ret;

        try {
            ret = java.net.URLDecoder.decode(v, enc);
        } catch (UnsupportedEncodingException e) {
            logger.error("URLEncoder decode is error.value={}, enc={}", v, enc, e);
        }
        return ret;
    }

    public static String decodeByUTF8(String v) {
        return decode(v, HTTP.UTF_8);
    }

    public static String encodeByUTF8(String v) {
        return encode(v, HTTP.UTF_8);
    }

    /**
     * encode url中的中文字符
     * 
     * @param content
     * @return
     */

    public static String urlEncodeChinese(String content) {
        if (StringUtils.isNotBlank(content)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < content.length(); i++) {
                if (isChineseCharacter(content.charAt(i))) {
                    try {
                        sb.append(URLEncoder.encode(content.substring(i, i + 1), "UTF-8"));
                    } catch (Exception e) {
                        logger.error("URLEncoder urlEncodeChinese is error.content=}{", content, e);
                    }
                } else {
                    sb.append(content.charAt(i));
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 中文判断
     * 
     * @param c
     * @return
     */
    public static boolean isChineseCharacter(char c) {

        int value = (int) c;
        int low = Integer.parseInt("4e00", 16);
        int up = Integer.parseInt("9fa5", 16);
        if (value > low && value < up)
            return true;

        return false;
    }

}
