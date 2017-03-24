package com.coldfire.util;

import com.coldfire.inter.CommonLogger;
import com.coldfire.model.JsonLogger;
import com.coldfire.model.SimpleLogger;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/11 21:42
 * Email:kangzhihu@163.com
 * Descriptions:日志工具类，定制日志格式，统计错误信息
 */
public class LoggerUtils {
    private static boolean notUseJson = false;
    private static String logClassName;

    static {//Todo:优化为使用文件读取工具类读取特定系统配置(log.useJson)
        notUseJson = new File("/notUseJson.txt").exists();
    }

    private LoggerUtils(){}

    private static int singleMax = 0;//最近一次异常所产生的总次数
    private static String singleMaxName = "";//最近一次异常的name
    private static AtomicInteger criticalCount = new AtomicInteger();//严重错误总数

    public static int getCriticalCount() {
        return criticalCount.get();
    }

    public static int getSingleMax() {
        return singleMax;
    }

    public static String getSingleMaxName() {
        return singleMaxName;
    }

    private static Long exceptionTime = System.currentTimeMillis();

    public static Long getExceptionTime() {
        return exceptionTime;
    }

    private static Map<String/*方法或者异常名*/, AtomicInteger> exceptionCount = new ConcurrentHashMap<String, AtomicInteger>();
    private static Set<String> criticalException = new HashSet<String>(Arrays.asList(/*严重异常列表*/
            "java.lang.ArrayIndexOutOfBoundsException",
            "java.net.UnknownHostException",
            "org.springframework.dao.DataAccessResourceFailureException",
            "org.springframework.dao.RecoverableDataAccessException",
            "org.springframework.jdbc.BadSqlGrammarException",
            "org.springframework.jms.UncategorizedJmsException",
            "java.lang.StackOverflowError",
            "java.lang.NoClassDefFoundError",
            "com.alibaba.dubbo.rpc.ProviderException",//alibaba dubbo
            "com.mongodb.MongoException"
    ));

    public static void incrementExceptionCount(Throwable e, String traceMethod) {
        if (null != e) {
            String expName = e.getClass().getName();
            if (criticalException.contains(expName)) {
                criticalCount.incrementAndGet();
            }
            if (StringUtils.isNotBlank(traceMethod)) {
                expName += "@" + traceMethod;
            }
            AtomicInteger count = exceptionCount.get(expName);
            if (null == count) {
                count = new AtomicInteger(1);
                exceptionCount.put(expName, count);
            } else {
                int nextCount = count.incrementAndGet();
                if (nextCount > singleMax) {
                    singleMax = nextCount;
                    singleMaxName = expName;
                }
            }
        }
    }

    public static Map<String, Integer> resetExceptionCount() {
        Map<String, AtomicInteger> before = exceptionCount;
        exceptionCount = new ConcurrentHashMap<String, AtomicInteger>();
        int singleMax = 0;
        String singleMaxName = "";
        criticalCount.set(0);
        exceptionTime = System.currentTimeMillis();
        return getMap(before);
    }

    private static Map<String, Integer> getMap(Map<String, AtomicInteger> map) {
        Map<String, Integer> retMap = new HashMap<String, Integer>();
        for (Map.Entry<String, AtomicInteger> entry : map.entrySet()) {
            retMap.put(entry.getKey(), entry.getValue().get());
        }
        return retMap;
    }

    public static CommonLogger getLogger(Class clazz, String serverIp, String systemId) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logClassName = clazz.getCanonicalName();
        if (notUseJson) return new SimpleLogger(logger);
        return new JsonLogger(logger, serverIp, systemId);
    }

    public static CommonLogger getLogger(String name/*canonicalName*/, String serverIp, String systemId) {
        Logger logger = LoggerFactory.getLogger(name);
        logClassName = name;
        if (notUseJson) return new SimpleLogger(logger);
        return new JsonLogger(logger, serverIp, systemId);
    }

    public static CommonLogger getLogger(Class clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (notUseJson) return new SimpleLogger(logger);
        return new JsonLogger(logger, BaseIpConfig.getServerIp(), BaseIpConfig.getHostName());
    }

    public static String getExceptionTrace(Throwable e) {
        return getExceptionTrace(e, 100);
    }

    /**
     * 获取异常栈信息
     * @param e 异常
     * @param rows 显示的异常信息行数
     * @return
     */
    public static String getExceptionTrace(Throwable e, int rows) {
        StringBuffer result = new StringBuffer(e.getClass().getCanonicalName() + ": " + e.getMessage());
        rows--;
        String tracePackage = getTracePackage();//TODO 方法关联获取或者使用系统配置文件给出包名（log.package）
        String tracedMethod = getExceptionTrace(result, e, rows, tracePackage);
        incrementExceptionCount(e, tracedMethod);
        return result.toString();
    }

    private static String getTracePackage() {
        if (StringUtils.isNotBlank(logClassName)) {
            String[] pak = StringUtils.split(logClassName, ".");
            if (pak.length > 1) {
                return new StringBuffer(pak[0]).append(pak[1]).toString();
            } else {
                return logClassName;
            }
        } else {
            return "com.";
        }
    }

    private static String getExceptionTrace(StringBuffer result, Throwable e, int rows, String tracePackage) {
        result.append(e);
        StackTraceElement[] traces = e.getStackTrace();
        String tmp = "", tracedMethod = null;
        for (int i = 0; i < traces.length && rows >= 0; i++) {
            tmp = traces[i].toString();
            //将指定行数中的所有指定包下的异常全部过滤出来
            if (tracedMethod == null && StringUtils.contains(tmp, tracePackage)) {
                tracedMethod = tmp;
            }
            result.append("\n\tat " + tmp);
            rows--;
        }
        if (rows > 0) {
            Throwable ourCause = e.getCause();
            if (ourCause != null) {
                result.append(ourCause);
                result.append("\nCaused by");
                rows--;
                if (rows > 0) {
                    String trace = getExceptionTrace(result, ourCause, rows, tracePackage);
                    if (tracedMethod == null) {
                        tracedMethod = trace;
                    }
                }
            }
        }
        return tracedMethod;
    }

}
