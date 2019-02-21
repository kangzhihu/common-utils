package com.meta.exception;

import com.alibaba.common.lang.MessageUtil;
import com.alibaba.common.lang.StringUtil;
import com.alipay.common.error.ErrorContext;
import com.meta.enum.ErrorDetailEnum;

/**
 * 底层异常
 * 
 */
public class BaseException extends RuntimeException {

    /** serial UID */
    private static final long serialVersionUID = -7374117528802243686L;

    /** 异常错误代码 */
    private ErrorDetailEnum   code             = ErrorDetailEnum.UNKNOWN_EXCEPTION;

    /** 错误上下文 */
    private ErrorContext      errorContext;

    /**
     * 创建一个<code>TradeException</code>
     * 
     * @param code 错误码
     */
    public BaseException(ErrorDetailEnum code) {
        super(code.getDescription());
        this.code = code;
    }

    /**
     * 创建一个<code>BaseException</code>
     * 
     * @param code
     * @param errorContext
     */
    public BaseException(ErrorDetailEnum code, ErrorContext errorContext) {
        this(code);
        this.errorContext = errorContext;
    }

    /**
     * 创建一个<code>BaseException</code>
     * 
     * @param code 错误码
     * @param errorMessage 错误描述 
     */
    public BaseException(ErrorDetailEnum code, String errorMessage) {
        super(errorMessage);
        this.code = code;
    }

    /**
     * 创建一个<code>BaseException</code>
     * 
     * @param code 错误码
     * @param errorMessage 错误描述格式
     * @param params 错误描述参数
     */
    public BaseException(ErrorDetailEnum code, String errorMessage, Object... params) {
        this(code, format(errorMessage, params));
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param code         错误码
     * @param errorContext 错误上下文
     * @param errorMessage 错误码
     */
    public BaseException(ErrorDetailEnum code, ErrorContext errorContext, String errorMessage) {
        this(code, errorMessage);
        this.errorContext = errorContext;
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param code         错误码
     * @param errorContext 错误上下文
     * @param errorMessage 错误描述格式
     * @param params 错误描述参数
     */
    public BaseException(ErrorDetailEnum code, ErrorContext errorContext, String errorMessage,
                          Object... params) {
        this(code, errorContext, format(errorMessage, params));
    }

    /**
     * 创建一个<code>TradeException</code>
     * 
     * @param code 错误码
     * @param cause 异常
     */
    public BaseException(ErrorDetailEnum code, Throwable cause) {
        super(code.getDescription(), cause);
        this.code = code;
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param code         错误码
     * @param errorContext 错误上下文
     * @param cause        异常
     */
    public BaseException(ErrorDetailEnum code, ErrorContext errorContext, Throwable cause) {
        this(code, cause);
        this.errorContext = errorContext;
    }

    /**
     * 创建一个<code>BaseException</code>
     * 
     * @param code         错误码
     * @param errorMessage 错误描述
     * @param cause        异常
     */
    public BaseException(ErrorDetailEnum code, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.code = code;
    }

    /**
     * 创建一个<code>BaseException</code>
     * 
     * @param code         错误码
     * @param errorMessage 错误描述格式
     * @param cause        异常
     * @param params 错误描述参数
     */
    public BaseException(ErrorDetailEnum code, String errorMessage, Throwable cause,
                          Object... params) {
        this(code, format(errorMessage, params), cause);
    }

    /**
     * 错误信息参数化格式化
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

    /** 
     * @see java.lang.Throwable#toString()
     */
    @Override
    public final String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return s + ": " + code.getCode() + "[" + message + "]。";
    }

    /**
     * @return Returns the code.
     */
    public ErrorDetailEnum getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>errorContext</tt>.
     * 
     * @return property value of errorContext
     */
    public ErrorContext getErrorContext() {
        return errorContext;
    }

    /**
     * 
     * @return String
     */
    public String getErrorMessage() {
        if (code != null) {
            return code.getDescription();
        }
        return null;
    }

    /**
     * 
     * @return String
     */
    public String getErrorCode() {
        if (code != null) {
            return code.getCode();
        }
        return null;
    }

    /**
     * 获取被调用系统的错误码，若不存在返回空字符串
     * 
     * @return
     */
    public String getTargetSysErrorCode() {

        String code = StringUtil.EMPTY_STRING;

        if (errorContext != null) {
            code = errorContext.fetchCurrentErrorCode();
        }

        return code;
    }
}
