package com.meta.exception;

/**
 * Created by zhihu.kang<br/>
 * Time: 2016/12/27 22:48<br/>
 * Email:kangzhihu@163.com<br/>
 * Descriptions:<br/>
 */
public class NoSuchPropertyException  extends RuntimeException {

    private static final long serialVersionUID = -6655814925412772573L;

    public NoSuchPropertyException()
    {
        super();
    }

    public NoSuchPropertyException(String msg)
    {
        super(msg);
    }
}
