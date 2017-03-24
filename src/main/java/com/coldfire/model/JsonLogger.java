package com.coldfire.model;

import com.coldfire.inter.CommonLogger;
import com.coldfire.util.LoggerUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.ext.LoggerWrapper;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import java.util.Map;

public class JsonLogger extends LoggerWrapper implements CommonLogger {
    private static String systemId = "NOTSET";
    private static String server = "127001";
    private static final String FQCN = JsonLogger.class.getName();
    static Marker FLOW_MARKER = MarkerFactory.getMarker("FLOW");
    static Marker ENTRY_MARKER = MarkerFactory.getMarker("ENTRY");
    static Marker EXIT_MARKER = MarkerFactory.getMarker("EXIT");

    static Marker EXCEPTION_MARKER = MarkerFactory.getMarker("EXCEPTION");
    static Marker THROWING_MARKER = MarkerFactory.getMarker("THROWING");
    static Marker CATCHING_MARKER = MarkerFactory.getMarker("CATCHING");

    static String EXIT_MESSAGE_0 = "exit";
    static String EXIT_MESSAGE_1 = "exit with ({})";

    static String ENTRY_MESSAGE_0 = "entry";
    static String ENTRY_MESSAGE_1 = "entry with ({})";
    static String ENTRY_MESSAGE_2 = "entry with ({}, {})";
    static String ENTRY_MESSAGE_3 = "entry with ({}, {}, {})";
    static String ENTRY_MESSAGE_4 = "entry with ({}, {}, {}, {})";
    static int ENTRY_MESSAGE_ARRAY_LEN = 5;
    static String[] ENTRY_MESSAGE_ARRAY = new String[ENTRY_MESSAGE_ARRAY_LEN];

    static {
        ENTRY_MARKER.add(FLOW_MARKER);
        EXIT_MARKER.add(FLOW_MARKER);
        THROWING_MARKER.add(EXCEPTION_MARKER);
        CATCHING_MARKER.add(EXCEPTION_MARKER);

        ENTRY_MESSAGE_ARRAY[0] = ENTRY_MESSAGE_0;
        ENTRY_MESSAGE_ARRAY[1] = ENTRY_MESSAGE_1;
        ENTRY_MESSAGE_ARRAY[2] = ENTRY_MESSAGE_2;
        ENTRY_MESSAGE_ARRAY[3] = ENTRY_MESSAGE_3;
        ENTRY_MESSAGE_ARRAY[4] = ENTRY_MESSAGE_4;
    }

    public enum Level {
        TRACE("TRACE", LocationAwareLogger.TRACE_INT), DEBUG("DEBUG", LocationAwareLogger.DEBUG_INT), INFO("INFO", LocationAwareLogger.INFO_INT), WARN(
                "WARN", LocationAwareLogger.WARN_INT), ERROR("ERROR", LocationAwareLogger.ERROR_INT);

        private final String name;
        private final int level;

        @Override
        public String toString() {
            return this.name;
        }

        public int intValue() {
            return this.level;
        }

        private Level(String name, int level) {
            this.name = name;
            this.level = level;
        }
    }

    /**
     * Given an underlying logger, construct an XLogger
     *
     * @param logger underlying logger
     */
    public JsonLogger(Logger logger, String serverIp2, String systemId2) {
        // If class B extends A, assuming B does not override method x(), the caller
        // of new B().x() is A and not B, see also
        // http://bugzilla.slf4j.org/show_bug.cgi?id=114
        super(logger, LoggerWrapper.class.getName());
        if (StringUtils.isNotBlank(systemId2) && StringUtils.equals(systemId, "NOTSET")) {
            systemId = systemId2;
        }
        if (StringUtils.isNotBlank(serverIp2) && StringUtils.equals(server, "127001")) {
            server = StringUtils.replace(serverIp2, ".", "");
        }

    }

    /**
     * Log method entry.
     *
     * @param argArray supplied parameters
     */
    public void entry(Object... argArray) {
        if (instanceofLAL && logger.isTraceEnabled(ENTRY_MARKER)) {
            String messagePattern = null;
            if (argArray.length < ENTRY_MESSAGE_ARRAY_LEN) {
                messagePattern = ENTRY_MESSAGE_ARRAY[argArray.length];
            } else {
                messagePattern = buildMessagePattern(argArray.length);
            }
            FormattingTuple tp = MessageFormatter.arrayFormat(messagePattern, argArray);
            ((LocationAwareLogger) logger).log(ENTRY_MARKER, FQCN, LocationAwareLogger.TRACE_INT, tp.getMessage(), argArray, tp.getThrowable());
        }
    }

    /**
     * Log method exit
     */
    public void exit() {
        if (instanceofLAL && logger.isTraceEnabled(ENTRY_MARKER)) {
            ((LocationAwareLogger) logger).log(EXIT_MARKER, FQCN, LocationAwareLogger.TRACE_INT, EXIT_MESSAGE_0, null, null);
        }
    }

    /**
     * Log method exit
     *
     * @param result The result of the method being exited
     */
    public void exit(Object result) {
        if (instanceofLAL && logger.isTraceEnabled(ENTRY_MARKER)) {
            FormattingTuple tp = MessageFormatter.format(EXIT_MESSAGE_1, result);
            ((LocationAwareLogger) logger).log(EXIT_MARKER, FQCN, LocationAwareLogger.TRACE_INT, tp.getMessage(), new Object[]{result}, tp
                    .getThrowable());
        }
    }

    /**
     * Log an exception being thrown. The generated log event uses Level ERROR.
     *
     * @param throwable the exception being caught.
     */
    public void throwing(Throwable throwable) {
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(THROWING_MARKER, FQCN, LocationAwareLogger.ERROR_INT, "throwing", null, throwable);
        }
    }

    /**
     * Log an exception being thrown allowing the log level to be specified.
     *
     * @param level     the logging level to use.
     * @param throwable the exception being caught.
     */
    public void throwing(Level level, Throwable throwable) {
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(THROWING_MARKER, FQCN, level.level, "throwing", null, throwable);
        }
    }

    /**
     * Log an exception being caught. The generated log event uses Level ERROR.
     *
     * @param throwable the exception being caught.
     */
    public void catching(Throwable throwable) {
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(CATCHING_MARKER, FQCN, LocationAwareLogger.ERROR_INT, "catching", null, throwable);
        }
    }

    /**
     * Log an exception being caught allowing the log level to be specified.
     *
     * @param level     the logging level to use.
     * @param throwable the exception being caught.
     */
    public void catching(Level level, Throwable throwable) {
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(CATCHING_MARKER, FQCN, level.level, "catching", null, throwable);
        }
    }

    private static String buildMessagePattern(int len) {
        StringBuilder sb = new StringBuilder();
        sb.append(" entry with (");
        for (int i = 0; i < len; i++) {
            sb.append("{}");
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void warn(String msg, Throwable t) {
        LogMessager logMsg = new LogMessager(null, server, systemId, msg, t);
        msg = logMsg.getDataStr();
        if (!logger.isWarnEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
        } else {
            logger.warn(msg);
        }
    }

    @Override
    public void warn(String msg) {
        LogMessager logMsg = new LogMessager(null, server, systemId, msg);
        msg = logMsg.getDataStr();
        if (!logger.isWarnEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
        } else {
            logger.warn(msg);
        }
    }

    @Override
    public void error(String msg) {
        LogMessager logMsg = new LogMessager(null, server, systemId, msg);
        msg = logMsg.getDataStr();
        if (!logger.isErrorEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
        } else {
            logger.error(msg);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        LogMessager logMsg = new LogMessager(null, server, systemId, msg, t);
        msg = logMsg.getDataStr();
        if (!logger.isErrorEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
        } else {
            logger.error(msg);
        }
    }

    @Override
    public void warnMap(String type, Map msgMap) {
        LogMessager logMsg = new LogMessager(type, server, systemId, msgMap);
        String msg = logMsg.getDataStr();
        if (!logger.isWarnEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
        } else {
            logger.warn(msg);
        }
    }

    @Override
    public void warnMap(Map msgMap) {
        LogMessager logMsg = new LogMessager(null, server, systemId, msgMap);
        String msg = logMsg.getDataStr();
        if (!logger.isWarnEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
        } else {
            logger.warn(msg);
        }
    }

    @Override
    public void errorMap(String type, Map msgMap) {
        LogMessager logMsg = new LogMessager(type, server, systemId, msgMap);
        String msg = logMsg.getDataStr();
        if (!logger.isErrorEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
        } else {
            logger.error(msg);
        }
    }

    @Override
    public void errorMap(Map msgMap) {
        LogMessager logMsg = new LogMessager(null, server, systemId, msgMap);
        String msg = logMsg.getDataStr();
        if (!logger.isErrorEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
        } else {
            logger.error(msg);
        }
    }

    @Override
    public void warnWithType(String type, String msg, Throwable t) {
        LogMessager logMsg = new LogMessager(type, server, systemId, msg, t);
        msg = logMsg.getDataStr();
        if (!logger.isWarnEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
        } else {
            logger.warn(msg);
        }
    }

    @Override
    public void warnWithType(String type, String msg) {
        LogMessager logMsg = new LogMessager(type, server, systemId, msg);
        msg = logMsg.getDataStr();
        if (!logger.isWarnEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
        } else {
            logger.warn(msg);
        }
    }

    @Override
    public void errorWithType(String type, String msg) {
        LogMessager logMsg = new LogMessager(type, server, systemId, msg);
        msg = logMsg.getDataStr();
        if (!logger.isErrorEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
        } else {
            logger.error(msg);
        }
    }

    @Override
    public void errorWithType(String type, String msg, Throwable t) {
        LogMessager logMsg = new LogMessager(type, server, systemId, msg, t);
        msg = logMsg.getDataStr();
        if (!logger.isErrorEnabled())
            return;
        if (instanceofLAL) {
            ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
        } else {
            logger.error(msg);
        }
    }

    @Override
    public void warn(String msg, Throwable e, int rows) {
        msg += "\n" + LoggerUtils.getExceptionTrace(e, rows);
        warn(msg);
    }

    @Override
    public void error(String msg, Throwable e, int rows) {
        msg += "\n" + LoggerUtils.getExceptionTrace(e, rows);
        error(msg);
    }

    @Override
    public void warnWithType(String type, String msg, Throwable e, int rows) {
        msg += "\n" + LoggerUtils.getExceptionTrace(e, rows);
        warnWithType(type, msg);
    }

    @Override
    public void errorWithType(String type, String msg, Throwable e, int rows) {
        msg += "\n" + LoggerUtils.getExceptionTrace(e, rows);
        errorWithType(type, msg);
    }

    @Override
    public void warn(Throwable e, int rows) {
        String msg = LoggerUtils.getExceptionTrace(e, rows);
        warn(msg);
    }

    @Override
    public void error(Throwable e, int rows) {
        String msg = LoggerUtils.getExceptionTrace(e, rows);
        error(msg);
    }
}
