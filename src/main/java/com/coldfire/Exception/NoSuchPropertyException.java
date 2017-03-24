package com.coldfire.Exception;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/27 22:48
 * Email:kangzhihu@163.com
 * Descriptions:
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
