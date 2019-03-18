package com.meta.common;

import com.meta.enums.ErrorDetailEnum;
import com.meta.exception.BaseException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * 断言工具类
 * 断言的值若不为期待的值，则抛异常
 */
public class AssertUtil {

    /**
     * 断言表达式的值为true，否则抛BaseException
     *
     * @param expValue 断言表达式的值
     * @param errCode 异常错误码
     * @throws BaseException
     */
    public static void assertTrue(boolean expValue, ErrorDetailEnum errCode) throws BaseException {
        if (!expValue) {
            throw new BaseException(errCode);
        }
    }

    /**
     * 断言表达式的值为true，否则抛BaseException
     * 
     * @param expValue 断言表达式的值
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertTrue(boolean expValue, ErrorDetailEnum errCode,
                                  String errMsg) throws BaseException {
        if (!expValue) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言表达式的值为true，否则抛BaseException
     * 
     * @param expValue 断言表达式的值
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertTrue(boolean expValue, ErrorDetailEnum errCode, String errMsg,
                                  Object... params) throws BaseException {
        assertTrue(expValue, errCode, format(errMsg, params));
    }

    /**
     * 断言表达式的值为false，否则抛BaseException
     * 
     * @param expValue 断言表达式的值
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertFalse(boolean expValue, ErrorDetailEnum errCode,
                                   String errMsg) throws BaseException {
        if (expValue) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言表达式的值为false，否则抛BaseException
     * 
     * @param expValue 断言表达式的值
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertFalse(boolean expValue, ErrorDetailEnum errCode, String errMsg,
                                   Object... params) throws BaseException {
        assertFalse(expValue, errCode, format(errMsg, params));
    }

    /**
     * 断言两个对象相等，否则抛BaseException
     *
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @throws BaseException
     */
    public static void assertEquals(Object obj1, Object obj2,
                                    ErrorDetailEnum errCode) throws BaseException {
        if (obj1 == null) {
            assertNull(obj2, errCode);
            return;
        }

        if (!obj1.equals(obj2)) {
            throw new BaseException(errCode);
        }
    }

    /**
     * 断言两个对象相等，否则抛BaseException
     * 
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertEquals(Object obj1, Object obj2, ErrorDetailEnum errCode,
                                    String errMsg) throws BaseException {
        if (obj1 == null) {
            assertNull(obj2, errCode, errMsg);
            return;
        }

        if (!obj1.equals(obj2)) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言两个对象相等，否则抛BaseException
     * 
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertEquals(Object obj1, Object obj2, ErrorDetailEnum errCode,
                                    String errMsg, Object... params) throws BaseException {
        assertEquals(obj1, obj2, errCode, format(errMsg, params));
    }

    /**
     * 断言两个对象不等，否则抛BaseException
     * 
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertNotEquals(Object obj1, Object obj2, ErrorDetailEnum errCode,
                                       String errMsg) throws BaseException {
        if (obj1 == null) {
            assertNotNull(obj2, errCode, errMsg);
            return;
        }

        if (obj1.equals(obj2)) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言两个对象不等，否则抛BaseException
     * 
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertNotEquals(Object obj1, Object obj2, ErrorDetailEnum errCode,
                                       String errMsg, Object... params) throws BaseException {
        assertNotEquals(obj1, obj2, errCode, format(errMsg, params));
    }

    /**
     * 断言对象至少等于容器中的一个
     * 
     * @param obj1 断言对象
     * @param objects 断言对象所在容器
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertEqualsAny(Object obj1, Object[] objects, ErrorDetailEnum errCode,
                                       String errMsg) throws BaseException {
        if (null == objects) {
            throw new BaseException(errCode, errMsg);
        }

        for (Object obj2 : objects) {
            if (obj1 == null) {
                if (obj2 == null) {
                    return;
                }
                continue;
            }

            if (obj1.equals(obj2)) {
                return;
            }
        }

        throw new BaseException(errCode, errMsg);
    }

    /**
     * 断言对象至少等于容器中的一个
     * 
     * @param obj1 断言对象
     * @param objects 断言对象所在容器
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertEqualsAny(Object obj1, Object[] objects, ErrorDetailEnum errCode,
                                       String errMsg, Object... params) throws BaseException {
        assertEqualsAny(obj1, objects, errCode, format(errMsg, params));
    }

    /**
     * 断言两个对象相同，否则抛BaseException
     * 
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertIs(Object obj1, Object obj2, ErrorDetailEnum errCode,
                                String errMsg) throws BaseException {
        if (obj1 != obj2) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言两个对象相同，否则抛BaseException
     * 
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertIs(Object obj1, Object obj2, ErrorDetailEnum errCode, String errMsg,
                                Object... params) throws BaseException {
        assertIs(obj1, obj2, errCode, format(errMsg, params));
    }

    /**
     * 断言两个对象不同，否则抛BaseException
     * 
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertIsNot(Object obj1, Object obj2, ErrorDetailEnum errCode,
                                   String errMsg) throws BaseException {
        if (obj1 == obj2) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言两个对象不同，否则抛BaseException
     * 
     * @param obj1 A对象
     * @param obj2 B对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertIsNot(Object obj1, Object obj2, ErrorDetailEnum errCode, String errMsg,
                                   Object... params) throws BaseException {
        assertIsNot(obj1, obj2, errCode, format(errMsg, params));
    }

    /**
     * 断言对象在容器中
     * 
     * @param obj1 断言对象
     * @param objs 断言对象所在容器
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertIn(Object obj1, Object[] objs, ErrorDetailEnum errCode,
                                String errMsg) throws BaseException {
        if (null == objs) {
            throw new BaseException(errCode, errMsg);
        }

        for (Object obj : objs) {
            if (obj == obj1) {
                return;
            }
        }

        throw new BaseException(errCode, errMsg);
    }

    /**
     * 断言对象在容器中
     * 
     * @param obj1 断言对象
     * @param objs 断言对象所在容器
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertIn(Object obj1, Object[] objs, ErrorDetailEnum errCode, String errMsg,
                                Object... params) throws BaseException {
        assertIn(obj1, objs, errCode, format(errMsg, params));
    }

    /**
     * 断言字符串为空，否则抛BaseException
     * 
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertEmpty(String str, ErrorDetailEnum errCode,
                                   String errMsg) throws BaseException {
        if (StringUtils.isNotEmpty(str)) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言字符串为空，否则抛BaseException
     * 
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertEmpty(String str, ErrorDetailEnum errCode, String errMsg,
                                   Object... params) throws BaseException {
        assertEmpty(str, errCode, format(errMsg, params));
    }

    /**
     * 断言字符串为非空，否则抛BaseException
     * 
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertNotEmpty(String str, ErrorDetailEnum errCode,
                                      String errMsg) throws BaseException {
        if (StringUtils.isEmpty(str)) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言字符串为非空，否则抛BaseException
     * 
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertNotEmpty(String str, ErrorDetailEnum errCode, String errMsg,
                                      Object... params) throws BaseException {
        assertNotEmpty(str, errCode, format(errMsg, params));
    }

    /**
     * 断言字符串为空，否则抛BaseException
     * 
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertBlank(String str, ErrorDetailEnum errCode,
                                   String errMsg) throws BaseException {
        if (StringUtils.isNotBlank(str)) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言字符串为空，否则抛BaseException
     * 
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertBlank(String str, ErrorDetailEnum errCode, String errMsg,
                                   Object... params) throws BaseException {
        assertBlank(str, errCode, format(errMsg, params));
    }

    /**
     * 断言字符串为非空，否则抛BaseException
     *
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @throws BaseException
     */
    public static void assertNotBlank(String str, ErrorDetailEnum errCode) throws BaseException {
        if (StringUtils.isBlank(str)) {
            throw new BaseException(errCode);
        }
    }

    /**
     * 断言字符串为非空，否则抛BaseException
     * 
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertNotBlank(String str, ErrorDetailEnum errCode,
                                      String errMsg) throws BaseException {
        if (StringUtils.isBlank(str)) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言字符串为非空，否则抛BaseException
     * 
     * @param str 断言字符串
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertNotBlank(String str, ErrorDetailEnum errCode, String errMsg,
                                      Object... params) throws BaseException {
        assertNotBlank(str, errCode, format(errMsg, params));
    }

    /**
     * 断言对象为null，否则抛BaseException
     *
     * @param object 断言对象
     * @param errCode 异常错误码
     * @throws BaseException
     */
    public static void assertNull(Object object, ErrorDetailEnum errCode) throws BaseException {
        if (object != null) {
            throw new BaseException(errCode);
        }
    }

    /**
     * 断言对象为null，否则抛BaseException
     * 
     * @param object 断言对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertNull(Object object, ErrorDetailEnum errCode,
                                  String errMsg) throws BaseException {
        if (object != null) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言对象为null，否则抛BaseException
     * 
     * @param object 断言对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertNull(Object object, ErrorDetailEnum errCode, String errMsg,
                                  Object... params) throws BaseException {
        assertNull(object, errCode, format(errMsg, params));
    }

    /**
     * 断言对象非null，否则抛BaseException
     * 
     * @param object 断言对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    public static void assertNotNull(Object object, ErrorDetailEnum errCode,
                                     String errMsg) throws BaseException {
        if (null == object) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言对象非null，否则抛BaseException
     * 
     * @param object 断言对象
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    public static void assertNotNull(Object object, ErrorDetailEnum errCode, String errMsg,
                                     Object... params) throws BaseException {
        assertNotNull(object, errCode, format(errMsg, params));
    }

    /**
     * 断言对象非null，否则抛BaseException
     * 
     * @param object 断言对象
     * @param errCode 异常错误码
     * @throws BaseException 
     */
    public static void assertNotNull(Object object, ErrorDetailEnum errCode) throws BaseException {
        if (null == object) {
            throw new BaseException(errCode);
        }
    }

    /**
     * 断言集合不为空或null，否则抛BaseException
     * 
     * @param collection 断言集合
     * @param errCode 异常错误码
     * @param errMsg 异常描述
     * @throws BaseException 
     */
    @SuppressWarnings("rawtypes")
    public static void assertNotBlank(Collection collection, ErrorDetailEnum errCode,
                                      String errMsg) throws BaseException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BaseException(errCode, errMsg);
        }
    }

    /**
     * 断言集合不为空或null，否则抛BaseException
     * 
     * @param collection 断言集合
     * @param errCode 异常错误码
     * @param errMsg 异常描述模板
     * @param params 异常描述模板填充变量
     * @throws BaseException
     */
    @SuppressWarnings("rawtypes")
    public static void assertNotBlank(Collection collection, ErrorDetailEnum errCode, String errMsg,
                                      Object... params) throws BaseException {
        assertNotBlank(collection, errCode, format(errMsg, params));
    }

    /**
     * 模板信息参数化格式化
     *
     * @param message 错误信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params  错误格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     * @return message
     */
    private static String format(String message, Object... params) {
        if (params != null && params.length != 0) {
            return MessageUtil.formatMessage(message, params);
        }
        return message;

    }
}
