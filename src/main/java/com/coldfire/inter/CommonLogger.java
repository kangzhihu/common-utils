package com.coldfire.inter;

import java.util.Map;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/11 21:57
 * Email:kangzhihu@163.com
 * Descriptions:
 */
public interface CommonLogger {
    void warn(String msg);

    void error(String msg);

    void warn(String msg, Throwable e);

    void warn(Throwable e, int rows);

    void warn(String msg, Throwable e, int rows);

    void error(String msg, Throwable e);

    void error(String msg, Throwable e, int rows);

    void error(Throwable e, int rows);

    void warnMap(Map msgMap);

    void warnMap(String type, Map msgMap);

    void errorMap(Map msgMap);

    void errorMap(String type, Map msgMap);

    void warnWithType(String type, String msg, Throwable e);

    void warnWithType(String type, String msg, Throwable e, int rows);

    void warnWithType(String type, String msg);

    void errorWithType(String type, String msg);

    void errorWithType(String type, String msg, Throwable e);

    void errorWithType(String type, String msg, Throwable e, int rows);
}
