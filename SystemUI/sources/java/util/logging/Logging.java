package java.util.logging;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

class Logging implements LoggingMXBean {
    private static String EMPTY_STRING = "";
    private static LogManager logManager = LogManager.getLogManager();

    Logging() {
    }

    public List<String> getLoggerNames() {
        Enumeration<String> loggerNames = logManager.getLoggerNames();
        ArrayList arrayList = new ArrayList();
        while (loggerNames.hasMoreElements()) {
            arrayList.add(loggerNames.nextElement());
        }
        return arrayList;
    }

    public String getLoggerLevel(String str) {
        Logger logger = logManager.getLogger(str);
        if (logger == null) {
            return null;
        }
        Level level = logger.getLevel();
        if (level == null) {
            return EMPTY_STRING;
        }
        return level.getLevelName();
    }

    public void setLoggerLevel(String str, String str2) {
        Level level;
        if (str != null) {
            Logger logger = logManager.getLogger(str);
            if (logger != null) {
                if (str2 != null) {
                    level = Level.findLevel(str2);
                    if (level == null) {
                        throw new IllegalArgumentException("Unknown level \"" + str2 + "\"");
                    }
                } else {
                    level = null;
                }
                logger.setLevel(level);
                return;
            }
            throw new IllegalArgumentException("Logger " + str + "does not exist");
        }
        throw new NullPointerException("loggerName is null");
    }

    public String getParentLoggerName(String str) {
        Logger logger = logManager.getLogger(str);
        if (logger == null) {
            return null;
        }
        Logger parent = logger.getParent();
        if (parent == null) {
            return EMPTY_STRING;
        }
        return parent.getName();
    }
}
