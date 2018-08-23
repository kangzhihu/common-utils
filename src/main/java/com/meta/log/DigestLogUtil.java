/**
* 监控日志工具类
*/
public class DigestLogUtil {

    /** 线程变量声明 */
    private static final ThreadLocal<Long> starTimeContainer = new ThreadLocal<Long>();

    /**
     * 监控日志
     *
     */
    public static void printScheduleDigest(ScheduleEventEnum event, String appName, String taskKey,
                                           String taskId, String taskStatus, String sliceId,
                                           String sliceStatus, String message) {
        Long time = starTimeContainer.get();
        LogUtil.info(LogType.MONITOR, "{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}", event.name(), appName,
                taskKey, taskId, taskStatus, sliceId, sliceStatus, message,
                time != null ? System.currentTimeMillis() - time + "ms" : "");
    }

    /**
     * 开始计时
     */
    public static void start() {
        starTimeContainer.set(System.currentTimeMillis());
    }

    /**
     * 清除线程变量
     */
    public static void clear() {
        starTimeContainer.remove();
    }

}
