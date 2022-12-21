package java.util.logging;

import java.util.List;
import sun.util.logging.LoggingProxy;

class LoggingProxyImpl implements LoggingProxy {
    static final LoggingProxy INSTANCE = new LoggingProxyImpl();

    private LoggingProxyImpl() {
    }

    public Object getLogger(String str) {
        return Logger.getPlatformLogger(str);
    }

    public Object getLevel(Object obj) {
        return ((Logger) obj).getLevel();
    }

    public void setLevel(Object obj, Object obj2) {
        ((Logger) obj).setLevel((Level) obj2);
    }

    public boolean isLoggable(Object obj, Object obj2) {
        return ((Logger) obj).isLoggable((Level) obj2);
    }

    public void log(Object obj, Object obj2, String str) {
        ((Logger) obj).log((Level) obj2, str);
    }

    public void log(Object obj, Object obj2, String str, Throwable th) {
        ((Logger) obj).log((Level) obj2, str, th);
    }

    public void log(Object obj, Object obj2, String str, Object... objArr) {
        ((Logger) obj).log((Level) obj2, str, objArr);
    }

    public List<String> getLoggerNames() {
        return LogManager.getLoggingMXBean().getLoggerNames();
    }

    public String getLoggerLevel(String str) {
        return LogManager.getLoggingMXBean().getLoggerLevel(str);
    }

    public void setLoggerLevel(String str, String str2) {
        LogManager.getLoggingMXBean().setLoggerLevel(str, str2);
    }

    public String getParentLoggerName(String str) {
        return LogManager.getLoggingMXBean().getParentLoggerName(str);
    }

    public Object parseLevel(String str) {
        Level findLevel = Level.findLevel(str);
        if (findLevel != null) {
            return findLevel;
        }
        throw new IllegalArgumentException("Unknown level \"" + str + "\"");
    }

    public String getLevelName(Object obj) {
        return ((Level) obj).getLevelName();
    }

    public int getLevelValue(Object obj) {
        return ((Level) obj).intValue();
    }

    public String getProperty(String str) {
        return LogManager.getLogManager().getProperty(str);
    }
}
