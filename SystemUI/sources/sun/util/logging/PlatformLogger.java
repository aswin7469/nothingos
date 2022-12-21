package sun.util.logging;

import android.net.wifi.WifiEnterpriseConfig;
import java.lang.ref.WeakReference;
import java.p026io.PrintStream;
import java.p026io.PrintWriter;
import java.p026io.StringWriter;
import java.p026io.Writer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlatformLogger {
    private static final int ALL = Integer.MIN_VALUE;
    private static final int CONFIG = 700;
    /* access modifiers changed from: private */
    public static final Level DEFAULT_LEVEL = Level.INFO;
    private static final int FINE = 500;
    private static final int FINER = 400;
    private static final int FINEST = 300;
    private static final int INFO = 800;
    private static final int OFF = Integer.MAX_VALUE;
    private static final int SEVERE = 1000;
    private static final int WARNING = 900;
    private static Map<String, WeakReference<PlatformLogger>> loggers = new HashMap();
    private static boolean loggingEnabled = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
        public Boolean run() {
            return Boolean.valueOf((System.getProperty("java.util.logging.config.class") == null && System.getProperty("java.util.logging.config.file") == null) ? false : true);
        }
    })).booleanValue();
    private volatile JavaLoggerProxy javaLoggerProxy;
    private volatile LoggerProxy loggerProxy;

    public enum Level {
        ALL,
        FINEST,
        FINER,
        FINE,
        CONFIG,
        INFO,
        WARNING,
        SEVERE,
        OFF;
        
        private static final int[] LEVEL_VALUES = null;
        Object javaLevel;

        static {
            LEVEL_VALUES = new int[]{Integer.MIN_VALUE, 300, 400, 500, PlatformLogger.CONFIG, PlatformLogger.INFO, PlatformLogger.WARNING, 1000, Integer.MAX_VALUE};
        }

        public int intValue() {
            return LEVEL_VALUES[ordinal()];
        }

        static Level valueOf(int i) {
            if (i == Integer.MIN_VALUE) {
                return ALL;
            }
            if (i == 300) {
                return FINEST;
            }
            if (i == 400) {
                return FINER;
            }
            if (i == 500) {
                return FINE;
            }
            if (i == PlatformLogger.CONFIG) {
                return CONFIG;
            }
            if (i == PlatformLogger.INFO) {
                return INFO;
            }
            if (i == PlatformLogger.WARNING) {
                return WARNING;
            }
            if (i == 1000) {
                return SEVERE;
            }
            if (i == Integer.MAX_VALUE) {
                return OFF;
            }
            int[] iArr = LEVEL_VALUES;
            int binarySearch = Arrays.binarySearch(iArr, 0, iArr.length - 2, i);
            Level[] values = values();
            if (binarySearch < 0) {
                binarySearch = (-binarySearch) - 1;
            }
            return values[binarySearch];
        }
    }

    public static synchronized PlatformLogger getLogger(String str) {
        PlatformLogger platformLogger;
        synchronized (PlatformLogger.class) {
            WeakReference weakReference = loggers.get(str);
            platformLogger = weakReference != null ? (PlatformLogger) weakReference.get() : null;
            if (platformLogger == null) {
                platformLogger = new PlatformLogger(str);
                loggers.put(str, new WeakReference(platformLogger));
            }
        }
        return platformLogger;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void redirectPlatformLoggers() {
        /*
            java.lang.Class<sun.util.logging.PlatformLogger> r0 = sun.util.logging.PlatformLogger.class
            monitor-enter(r0)
            boolean r1 = loggingEnabled     // Catch:{ all -> 0x003d }
            if (r1 != 0) goto L_0x003b
            boolean r1 = sun.util.logging.LoggingSupport.isAvailable()     // Catch:{ all -> 0x003d }
            if (r1 != 0) goto L_0x000e
            goto L_0x003b
        L_0x000e:
            r1 = 1
            loggingEnabled = r1     // Catch:{ all -> 0x003d }
            java.util.Map<java.lang.String, java.lang.ref.WeakReference<sun.util.logging.PlatformLogger>> r1 = loggers     // Catch:{ all -> 0x003d }
            java.util.Set r1 = r1.entrySet()     // Catch:{ all -> 0x003d }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x003d }
        L_0x001b:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x003d }
            if (r2 == 0) goto L_0x0039
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x003d }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ all -> 0x003d }
            java.lang.Object r2 = r2.getValue()     // Catch:{ all -> 0x003d }
            java.lang.ref.WeakReference r2 = (java.lang.ref.WeakReference) r2     // Catch:{ all -> 0x003d }
            java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x003d }
            sun.util.logging.PlatformLogger r2 = (sun.util.logging.PlatformLogger) r2     // Catch:{ all -> 0x003d }
            if (r2 == 0) goto L_0x001b
            r2.redirectToJavaLoggerProxy()     // Catch:{ all -> 0x003d }
            goto L_0x001b
        L_0x0039:
            monitor-exit(r0)
            return
        L_0x003b:
            monitor-exit(r0)
            return
        L_0x003d:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.logging.PlatformLogger.redirectPlatformLoggers():void");
    }

    private void redirectToJavaLoggerProxy() {
        DefaultLoggerProxy cast = DefaultLoggerProxy.class.cast(this.loggerProxy);
        JavaLoggerProxy javaLoggerProxy2 = new JavaLoggerProxy(cast.name, cast.level);
        this.javaLoggerProxy = javaLoggerProxy2;
        this.loggerProxy = javaLoggerProxy2;
    }

    private PlatformLogger(String str) {
        if (loggingEnabled) {
            JavaLoggerProxy javaLoggerProxy2 = new JavaLoggerProxy(str);
            this.javaLoggerProxy = javaLoggerProxy2;
            this.loggerProxy = javaLoggerProxy2;
            return;
        }
        this.loggerProxy = new DefaultLoggerProxy(str);
    }

    public boolean isEnabled() {
        return this.loggerProxy.isEnabled();
    }

    public String getName() {
        return this.loggerProxy.name;
    }

    public boolean isLoggable(Level level) {
        level.getClass();
        JavaLoggerProxy javaLoggerProxy2 = this.javaLoggerProxy;
        return javaLoggerProxy2 != null ? javaLoggerProxy2.isLoggable(level) : this.loggerProxy.isLoggable(level);
    }

    public Level level() {
        return this.loggerProxy.getLevel();
    }

    public void setLevel(Level level) {
        this.loggerProxy.setLevel(level);
    }

    public void severe(String str) {
        this.loggerProxy.doLog(Level.SEVERE, str);
    }

    public void severe(String str, Throwable th) {
        this.loggerProxy.doLog(Level.SEVERE, str, th);
    }

    public void severe(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.SEVERE, str, objArr);
    }

    public void warning(String str) {
        this.loggerProxy.doLog(Level.WARNING, str);
    }

    public void warning(String str, Throwable th) {
        this.loggerProxy.doLog(Level.WARNING, str, th);
    }

    public void warning(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.WARNING, str, objArr);
    }

    public void info(String str) {
        this.loggerProxy.doLog(Level.INFO, str);
    }

    public void info(String str, Throwable th) {
        this.loggerProxy.doLog(Level.INFO, str, th);
    }

    public void info(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.INFO, str, objArr);
    }

    public void config(String str) {
        this.loggerProxy.doLog(Level.CONFIG, str);
    }

    public void config(String str, Throwable th) {
        this.loggerProxy.doLog(Level.CONFIG, str, th);
    }

    public void config(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.CONFIG, str, objArr);
    }

    public void fine(String str) {
        this.loggerProxy.doLog(Level.FINE, str);
    }

    public void fine(String str, Throwable th) {
        this.loggerProxy.doLog(Level.FINE, str, th);
    }

    public void fine(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.FINE, str, objArr);
    }

    public void finer(String str) {
        this.loggerProxy.doLog(Level.FINER, str);
    }

    public void finer(String str, Throwable th) {
        this.loggerProxy.doLog(Level.FINER, str, th);
    }

    public void finer(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.FINER, str, objArr);
    }

    public void finest(String str) {
        this.loggerProxy.doLog(Level.FINEST, str);
    }

    public void finest(String str, Throwable th) {
        this.loggerProxy.doLog(Level.FINEST, str, th);
    }

    public void finest(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.FINEST, str, objArr);
    }

    private static abstract class LoggerProxy {
        final String name;

        /* access modifiers changed from: package-private */
        public abstract void doLog(Level level, String str);

        /* access modifiers changed from: package-private */
        public abstract void doLog(Level level, String str, Throwable th);

        /* access modifiers changed from: package-private */
        public abstract void doLog(Level level, String str, Object... objArr);

        /* access modifiers changed from: package-private */
        public abstract Level getLevel();

        /* access modifiers changed from: package-private */
        public abstract boolean isEnabled();

        /* access modifiers changed from: package-private */
        public abstract boolean isLoggable(Level level);

        /* access modifiers changed from: package-private */
        public abstract void setLevel(Level level);

        protected LoggerProxy(String str) {
            this.name = str;
        }
    }

    private static final class DefaultLoggerProxy extends LoggerProxy {
        private static final String formatString = LoggingSupport.getSimpleFormat(false);
        private Date date = new Date();
        volatile Level effectiveLevel = deriveEffectiveLevel((Level) null);
        volatile Level level = null;

        private static PrintStream outputStream() {
            return System.err;
        }

        DefaultLoggerProxy(String str) {
            super(str);
        }

        /* access modifiers changed from: package-private */
        public boolean isEnabled() {
            return this.effectiveLevel != Level.OFF;
        }

        /* access modifiers changed from: package-private */
        public Level getLevel() {
            return this.level;
        }

        /* access modifiers changed from: package-private */
        public void setLevel(Level level2) {
            if (this.level != level2) {
                this.level = level2;
                this.effectiveLevel = deriveEffectiveLevel(level2);
            }
        }

        /* access modifiers changed from: package-private */
        public void doLog(Level level2, String str) {
            if (isLoggable(level2)) {
                outputStream().print(format(level2, str, (Throwable) null));
            }
        }

        /* access modifiers changed from: package-private */
        public void doLog(Level level2, String str, Throwable th) {
            if (isLoggable(level2)) {
                outputStream().print(format(level2, str, th));
            }
        }

        /* access modifiers changed from: package-private */
        public void doLog(Level level2, String str, Object... objArr) {
            if (isLoggable(level2)) {
                outputStream().print(format(level2, formatMessage(str, objArr), (Throwable) null));
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isLoggable(Level level2) {
            Level level3 = this.effectiveLevel;
            return level2.intValue() >= level3.intValue() && level3 != Level.OFF;
        }

        private Level deriveEffectiveLevel(Level level2) {
            return level2 == null ? PlatformLogger.DEFAULT_LEVEL : level2;
        }

        private String formatMessage(String str, Object... objArr) {
            if (objArr != null) {
                try {
                    if (objArr.length != 0) {
                        if (str.indexOf("{0") < 0 && str.indexOf("{1") < 0 && str.indexOf("{2") < 0) {
                            if (str.indexOf("{3") < 0) {
                                return str;
                            }
                        }
                        return MessageFormat.format(str, objArr);
                    }
                } catch (Exception unused) {
                }
            }
            return str;
        }

        private synchronized String format(Level level2, String str, Throwable th) {
            String str2;
            this.date.setTime(System.currentTimeMillis());
            str2 = "";
            if (th != null) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter((Writer) stringWriter);
                printWriter.println();
                th.printStackTrace(printWriter);
                printWriter.close();
                str2 = stringWriter.toString();
            }
            return String.format(formatString, this.date, getCallerInfo(), this.name, level2.name(), str, str2);
        }

        private String getCallerInfo() {
            String str;
            String str2;
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            int length = stackTrace.length;
            boolean z = true;
            int i = 0;
            while (true) {
                if (i >= length) {
                    str = null;
                    str2 = null;
                    break;
                }
                StackTraceElement stackTraceElement = stackTrace[i];
                str = stackTraceElement.getClassName();
                if (z) {
                    if (str.equals("sun.util.logging.PlatformLogger")) {
                        z = false;
                    }
                } else if (!str.equals("sun.util.logging.PlatformLogger")) {
                    str2 = stackTraceElement.getMethodName();
                    break;
                }
                i++;
            }
            if (str == null) {
                return this.name;
            }
            return str + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + str2;
        }
    }

    private static final class JavaLoggerProxy extends LoggerProxy {
        private final Object javaLogger;

        static {
            for (Level level : Level.values()) {
                level.javaLevel = LoggingSupport.parseLevel(level.name());
            }
        }

        JavaLoggerProxy(String str) {
            this(str, (Level) null);
        }

        JavaLoggerProxy(String str, Level level) {
            super(str);
            Object logger = LoggingSupport.getLogger(str);
            this.javaLogger = logger;
            if (level != null) {
                LoggingSupport.setLevel(logger, level.javaLevel);
            }
        }

        /* access modifiers changed from: package-private */
        public void doLog(Level level, String str) {
            LoggingSupport.log(this.javaLogger, level.javaLevel, str);
        }

        /* access modifiers changed from: package-private */
        public void doLog(Level level, String str, Throwable th) {
            LoggingSupport.log(this.javaLogger, level.javaLevel, str, th);
        }

        /* access modifiers changed from: package-private */
        public void doLog(Level level, String str, Object... objArr) {
            if (isLoggable(level)) {
                int length = objArr != null ? objArr.length : 0;
                String[] strArr = new String[length];
                for (int i = 0; i < length; i++) {
                    strArr[i] = String.valueOf(objArr[i]);
                }
                LoggingSupport.log(this.javaLogger, level.javaLevel, str, (Object[]) strArr);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isEnabled() {
            return LoggingSupport.isLoggable(this.javaLogger, Level.OFF.javaLevel);
        }

        /* access modifiers changed from: package-private */
        public Level getLevel() {
            Object level = LoggingSupport.getLevel(this.javaLogger);
            if (level == null) {
                return null;
            }
            try {
                return Level.valueOf(LoggingSupport.getLevelName(level));
            } catch (IllegalArgumentException unused) {
                return Level.valueOf(LoggingSupport.getLevelValue(level));
            }
        }

        /* access modifiers changed from: package-private */
        public void setLevel(Level level) {
            LoggingSupport.setLevel(this.javaLogger, level == null ? null : level.javaLevel);
        }

        /* access modifiers changed from: package-private */
        public boolean isLoggable(Level level) {
            return LoggingSupport.isLoggable(this.javaLogger, level.javaLevel);
        }
    }
}
