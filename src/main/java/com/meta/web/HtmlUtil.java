package com.meta.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-11-02&nbsp;22:14<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;html工具类<br/><br/>
 */
public class HtmlUtil {
    private static final transient Logger logger = LoggerFactory.getLogger(HtmlUtil.class);


    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
    private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
    private static final Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
    private static final Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");

    /**
     * 删除Html标签
     *
     * @param htmlStr Html标签字符串
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 删除Html标签
     *
     * @param htmlStr Html标签字符串
     * @return 删除Html标签
     */
    public static String delHTMLTagAndSpace(String htmlStr) {
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车

        return htmlStr.trim(); // 返回文本字符串
    }

    public static String replaceAll2Br(String origin) {
        if (StringUtils.isBlank(origin)) {
            return origin;
        }
        Matcher m = CRLF.matcher(origin);
        if (m.find()) {
            return m.replaceAll("<br>");
        }
        return origin;
    }

}
