package com.meta.exception;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/6/18 0:09<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;非法参数异常类<br/><br/>
 */

public class IllegalArgExeption extends RuntimeException {

    private static final long serialVersionUID = -84965145826716496L;

    private String code;

    public IllegalArgExeption() {
        super();
    }

    public IllegalArgExeption(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IllegalArgExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgExeption(String code) {
        super();
        this.code = code;
    }

    public IllegalArgExeption(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public IllegalArgExeption(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
