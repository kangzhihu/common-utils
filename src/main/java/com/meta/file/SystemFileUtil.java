package com.meta.file;

import java.io.File;

/**
 * @program: common-utils
 * @description: 系统文件处理
 * @author: kangzhihu
 * @create: 2020/03/22 20:23
 */
public class SystemFileUtil {

    /**
     * 获取指定路径的可磁盘用比例
     * @param path
     * @return physicRatio
     */
    public static double getDiskPartitionSpaceUsedPercent(final String path) {
        if (null == path || path.isEmpty())
            return -1;

        try {
            File file = new File(path);

            if (!file.exists())
                return -1;

            long totalSpace = file.getTotalSpace();

            if (totalSpace > 0) {
                long freeSpace = file.getFreeSpace();
                long usedSpace = totalSpace - freeSpace;

                return usedSpace / (double) totalSpace;
            }
        } catch (Exception e) {
            return -1;
        }

        return -1;
    }

}
