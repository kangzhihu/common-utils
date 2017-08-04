package com.meta.common;

import com.google.common.base.Strings;

/**
 * Created by zhihu
 * Time: 2016/8/4 9:19
 * Email:kangzhihu@163.com
 * Descriptions:系统环境工具类
 */
public class OsUtils {

    public boolean isOSWindows() {
        String osName = System.getProperty("os.name");
        if (Strings.isNullOrEmpty(osName)) {
            return false;
        }
        return osName.startsWith("Windows");
    }
}
