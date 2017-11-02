package com.meta.math;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-11-02&nbsp;22:19<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;Decimal数据格式化<br/><br/>
 */
public class Decimal {
    private static final transient Logger logger = LoggerFactory.getLogger(Decimal.class);

    /**
     * 去掉BigDecimal后面的零
     * 例如 ：195.00为195 195.50为195.5
     * @param s
     * @return
     */
    public static String trimDecimal(String s) {
        try {
            if (StringUtils.isBlank(s)) {
                return s;
            }
            return trimDecimal(new BigDecimal(s));
        } catch (Throwable e) {
            logger.error("InterTradeUtils trimDecimal String is error.searchContext={}", s, e);
            return s;
        }
    }

    /**
     * 去掉BigDecimal后面的零
     * 例如 ：195.00为195 195.50为195.5
     * @param bigDecimal
     * @return
     */
    public static String trimDecimal(BigDecimal bigDecimal) {
        if (bigDecimal == null)
            return StringUtils.EMPTY;
        return new DecimalFormat("0.##").format(bigDecimal);
    }

    /**
     * 去掉BigDecimal后面的零
     * 例如 ：195.00为195 195.50为195.5
     * @param a
     * @return
     */
    public static String trimDecimal(double a) {
        try {
            return new DecimalFormat("0.##").format(new BigDecimal(a));
        } catch (Throwable e) {
            logger.error("InterTradeUtils trimDecimal double is error.searchContext={}", a, e);
            return "";
        }
    }

}
