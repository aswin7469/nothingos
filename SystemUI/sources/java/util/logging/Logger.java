package java.util.logging;

import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public class Logger {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String GLOBAL_LOGGER_NAME = "global";
    /* access modifiers changed from: private */
    public static final LoggerBundle NO_RESOURCE_BUNDLE = new LoggerBundle((String) null, (ResourceBundle) null);
    /* access modifiers changed from: private */
    public static final LoggerBundle SYSTEM_BUNDLE = new LoggerBundle(SYSTEM_LOGGER_RB_NAME, (ResourceBundle) null);
    static final String SYSTEM_LOGGER_RB_NAME = "sun.util.logging.resources.logging";
    private static final Handler[] emptyHandlers = new Handler[0];
    @Deprecated
    public static final Logger global = new Logger(GLOBAL_LOGGER_NAME);
    private static final int offValue = Level.OFF.intValue();
    private static final Object treeLock = new Object();
    private boolean anonymous;
    private WeakReference<ClassLoader> callersClassLoaderRef;
    private ResourceBundle catalog;
    private Locale catalogLocale;
    private String catalogName;
    private volatile Filter filter;
    private final CopyOnWriteArrayList<Handler> handlers;
    private final boolean isSystemLogger;
    private ArrayList<LogManager.LoggerWeakRef> kids;
    private volatile Level levelObject;
    private volatile int levelValue;
    private volatile LoggerBundle loggerBundle;
    private volatile LogManager manager;
    private String name;
    private volatile Logger parent;
    private volatile boolean useParentHandlers;

    private static final class LoggerBundle {
        final String resourceBundleName;
        final ResourceBundle userBundle;

        private LoggerBundle(String str, ResourceBundle resourceBundle) {
            this.resourceBundleName = str;
            this.userBundle = resourceBundle;
        }

        /* access modifiers changed from: package-private */
        public boolean isSystemBundle() {
            return Logger.SYSTEM_LOGGER_RB_NAME.equals(this.resourceBundleName);
        }

        static LoggerBundle get(String str, ResourceBundle resourceBundle) {
            if (str == null && resourceBundle == null) {
                return Logger.NO_RESOURCE_BUNDLE;
            }
            if (!Logger.SYSTEM_LOGGER_RB_NAME.equals(str) || resourceBundle != null) {
                return new LoggerBundle(str, resourceBundle);
            }
            return Logger.SYSTEM_BUNDLE;
        }
    }

    public static final Logger getGlobal() {
        LogManager.getLogManager();
        return global;
    }

    protected Logger(String str, String str2) {
        this(str, str2, (Class<?>) null, LogManager.getLogManager(), false);
    }

    Logger(String str, String str2, Class<?> cls, LogManager logManager, boolean z) {
        this.handlers = new CopyOnWriteArrayList<>();
        this.loggerBundle = NO_RESOURCE_BUNDLE;
        this.useParentHandlers = true;
        this.manager = logManager;
        this.isSystemLogger = z;
        setupResourceInfo(str2, cls);
        this.name = str;
        this.levelValue = Level.INFO.intValue();
    }

    private void setCallersClassLoaderRef(Class<?> cls) {
        ClassLoader classLoader = cls != null ? cls.getClassLoader() : null;
        if (classLoader != null) {
            this.callersClassLoaderRef = new WeakReference<>(classLoader);
        }
    }

    private ClassLoader getCallersClassLoader() {
        WeakReference<ClassLoader> weakReference = this.callersClassLoaderRef;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    private Logger(String str) {
        this.handlers = new CopyOnWriteArrayList<>();
        this.loggerBundle = NO_RESOURCE_BUNDLE;
        this.useParentHandlers = true;
        this.name = str;
        this.isSystemLogger = true;
        this.levelValue = Level.INFO.intValue();
    }

    /* access modifiers changed from: package-private */
    public void setLogManager(LogManager logManager) {
        this.manager = logManager;
    }

    private void checkPermission() throws SecurityException {
        if (!this.anonymous) {
            if (this.manager == null) {
                this.manager = LogManager.getLogManager();
            }
            this.manager.checkPermission();
        }
    }

    private static class SystemLoggerHelper {
        static boolean disableCallerCheck = getBooleanProperty("sun.util.logging.disableCallerCheck");

        private SystemLoggerHelper() {
        }

        private static boolean getBooleanProperty(final String str) {
            return Boolean.valueOf((String) AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty(String.this);
                }
            })).booleanValue();
        }
    }

    private static Logger demandLogger(String str, String str2, Class<?> cls) {
        LogManager logManager = LogManager.getLogManager();
        if (System.getSecurityManager() == null || SystemLoggerHelper.disableCallerCheck || cls.getClassLoader() != null) {
            return logManager.demandLogger(str, str2, cls);
        }
        return logManager.demandSystemLogger(str, str2);
    }

    @CallerSensitive
    public static Logger getLogger(String str) {
        return demandLogger(str, (String) null, Reflection.getCallerClass());
    }

    @CallerSensitive
    public static Logger getLogger(String str, String str2) {
        Class<?> callerClass = Reflection.getCallerClass();
        Logger demandLogger = demandLogger(str, str2, callerClass);
        demandLogger.setupResourceInfo(str2, callerClass);
        return demandLogger;
    }

    static Logger getPlatformLogger(String str) {
        return LogManager.getLogManager().demandSystemLogger(str, SYSTEM_LOGGER_RB_NAME);
    }

    public static Logger getAnonymousLogger() {
        return getAnonymousLogger((String) null);
    }

    @CallerSensitive
    public static Logger getAnonymousLogger(String str) {
        LogManager logManager = LogManager.getLogManager();
        logManager.drainLoggerRefQueueBounded();
        Logger logger = new Logger((String) null, str, Reflection.getCallerClass(), logManager, false);
        logger.anonymous = true;
        logger.doSetParent(logManager.getLogger(""));
        return logger;
    }

    public ResourceBundle getResourceBundle() {
        return findResourceBundle(getResourceBundleName(), true);
    }

    public String getResourceBundleName() {
        return this.loggerBundle.resourceBundleName;
    }

    public void setFilter(Filter filter2) throws SecurityException {
        checkPermission();
        this.filter = filter2;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public void log(LogRecord logRecord) {
        Handler[] handlerArr;
        boolean z;
        if (isLoggable(logRecord.getLevel())) {
            Filter filter2 = this.filter;
            if (filter2 == null || filter2.isLoggable(logRecord)) {
                Logger logger = this;
                while (logger != null) {
                    if (this.isSystemLogger) {
                        handlerArr = logger.accessCheckedHandlers();
                    } else {
                        handlerArr = logger.getHandlers();
                    }
                    for (Handler publish : handlerArr) {
                        publish.publish(logRecord);
                    }
                    if (this.isSystemLogger) {
                        z = logger.useParentHandlers;
                    } else {
                        z = logger.getUseParentHandlers();
                    }
                    if (z) {
                        logger = this.isSystemLogger ? logger.parent : logger.getParent();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private void doLog(LogRecord logRecord) {
        logRecord.setLoggerName(this.name);
        LoggerBundle effectiveLoggerBundle = getEffectiveLoggerBundle();
        ResourceBundle resourceBundle = effectiveLoggerBundle.userBundle;
        String str = effectiveLoggerBundle.resourceBundleName;
        if (!(str == null || resourceBundle == null)) {
            logRecord.setResourceBundleName(str);
            logRecord.setResourceBundle(resourceBundle);
        }
        log(logRecord);
    }

    public void log(Level level, String str) {
        if (isLoggable(level)) {
            doLog(new LogRecord(level, str));
        }
    }

    public void log(Level level, Supplier<String> supplier) {
        if (isLoggable(level)) {
            doLog(new LogRecord(level, supplier.get()));
        }
    }

    public void log(Level level, String str, Object obj) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str);
            logRecord.setParameters(new Object[]{obj});
            doLog(logRecord);
        }
    }

    public void log(Level level, String str, Object[] objArr) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str);
            logRecord.setParameters(objArr);
            doLog(logRecord);
        }
    }

    public void log(Level level, String str, Throwable th) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str);
            logRecord.setThrown(th);
            doLog(logRecord);
        }
    }

    public void log(Level level, Throwable th, Supplier<String> supplier) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, supplier.get());
            logRecord.setThrown(th);
            doLog(logRecord);
        }
    }

    public void logp(Level level, String str, String str2, String str3) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str3);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            doLog(logRecord);
        }
    }

    public void logp(Level level, String str, String str2, Supplier<String> supplier) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, supplier.get());
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            doLog(logRecord);
        }
    }

    public void logp(Level level, String str, String str2, String str3, Object obj) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str3);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setParameters(new Object[]{obj});
            doLog(logRecord);
        }
    }

    public void logp(Level level, String str, String str2, String str3, Object[] objArr) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str3);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setParameters(objArr);
            doLog(logRecord);
        }
    }

    public void logp(Level level, String str, String str2, String str3, Throwable th) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str3);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setThrown(th);
            doLog(logRecord);
        }
    }

    public void logp(Level level, String str, String str2, Throwable th, Supplier<String> supplier) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, supplier.get());
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setThrown(th);
            doLog(logRecord);
        }
    }

    private void doLog(LogRecord logRecord, String str) {
        logRecord.setLoggerName(this.name);
        if (str != null) {
            logRecord.setResourceBundleName(str);
            logRecord.setResourceBundle(findResourceBundle(str, false));
        }
        log(logRecord);
    }

    private void doLog(LogRecord logRecord, ResourceBundle resourceBundle) {
        logRecord.setLoggerName(this.name);
        if (resourceBundle != null) {
            logRecord.setResourceBundleName(resourceBundle.getBaseBundleName());
            logRecord.setResourceBundle(resourceBundle);
        }
        log(logRecord);
    }

    @Deprecated
    public void logrb(Level level, String str, String str2, String str3, String str4) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str4);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            doLog(logRecord, str3);
        }
    }

    @Deprecated
    public void logrb(Level level, String str, String str2, String str3, String str4, Object obj) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str4);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setParameters(new Object[]{obj});
            doLog(logRecord, str3);
        }
    }

    @Deprecated
    public void logrb(Level level, String str, String str2, String str3, String str4, Object[] objArr) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str4);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setParameters(objArr);
            doLog(logRecord, str3);
        }
    }

    public void logrb(Level level, String str, String str2, ResourceBundle resourceBundle, String str3, Object... objArr) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str3);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            if (!(objArr == null || objArr.length == 0)) {
                logRecord.setParameters(objArr);
            }
            doLog(logRecord, resourceBundle);
        }
    }

    @Deprecated
    public void logrb(Level level, String str, String str2, String str3, String str4, Throwable th) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str4);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setThrown(th);
            doLog(logRecord, str3);
        }
    }

    public void logrb(Level level, String str, String str2, ResourceBundle resourceBundle, String str3, Throwable th) {
        if (isLoggable(level)) {
            LogRecord logRecord = new LogRecord(level, str3);
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setThrown(th);
            doLog(logRecord, resourceBundle);
        }
    }

    public void entering(String str, String str2) {
        logp(Level.FINER, str, str2, "ENTRY");
    }

    public void entering(String str, String str2, Object obj) {
        logp(Level.FINER, str, str2, "ENTRY {0}", obj);
    }

    public void entering(String str, String str2, Object[] objArr) {
        if (objArr == null) {
            logp(Level.FINER, str, str2, "ENTRY");
        } else if (isLoggable(Level.FINER)) {
            String str3 = "ENTRY";
            for (int i = 0; i < objArr.length; i++) {
                str3 = str3 + " {" + i + "}";
            }
            logp(Level.FINER, str, str2, str3, objArr);
        }
    }

    public void exiting(String str, String str2) {
        logp(Level.FINER, str, str2, "RETURN");
    }

    public void exiting(String str, String str2, Object obj) {
        logp(Level.FINER, str, str2, "RETURN {0}", obj);
    }

    public void throwing(String str, String str2, Throwable th) {
        if (isLoggable(Level.FINER)) {
            LogRecord logRecord = new LogRecord(Level.FINER, "THROW");
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(str2);
            logRecord.setThrown(th);
            doLog(logRecord);
        }
    }

    public void severe(String str) {
        log(Level.SEVERE, str);
    }

    public void warning(String str) {
        log(Level.WARNING, str);
    }

    public void info(String str) {
        log(Level.INFO, str);
    }

    public void config(String str) {
        log(Level.CONFIG, str);
    }

    public void fine(String str) {
        log(Level.FINE, str);
    }

    public void finer(String str) {
        log(Level.FINER, str);
    }

    public void finest(String str) {
        log(Level.FINEST, str);
    }

    public void severe(Supplier<String> supplier) {
        log(Level.SEVERE, supplier);
    }

    public void warning(Supplier<String> supplier) {
        log(Level.WARNING, supplier);
    }

    public void info(Supplier<String> supplier) {
        log(Level.INFO, supplier);
    }

    public void config(Supplier<String> supplier) {
        log(Level.CONFIG, supplier);
    }

    public void fine(Supplier<String> supplier) {
        log(Level.FINE, supplier);
    }

    public void finer(Supplier<String> supplier) {
        log(Level.FINER, supplier);
    }

    public void finest(Supplier<String> supplier) {
        log(Level.FINEST, supplier);
    }

    public void setLevel(Level level) throws SecurityException {
        checkPermission();
        synchronized (treeLock) {
            this.levelObject = level;
            updateEffectiveLevel();
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean isLevelInitialized() {
        return this.levelObject != null;
    }

    public Level getLevel() {
        return this.levelObject;
    }

    public boolean isLoggable(Level level) {
        return level.intValue() >= this.levelValue && this.levelValue != offValue;
    }

    public String getName() {
        return this.name;
    }

    public void addHandler(Handler handler) throws SecurityException {
        handler.getClass();
        checkPermission();
        this.handlers.add(handler);
    }

    public void removeHandler(Handler handler) throws SecurityException {
        checkPermission();
        if (handler != null) {
            this.handlers.remove((Object) handler);
        }
    }

    public Handler[] getHandlers() {
        return accessCheckedHandlers();
    }

    /* access modifiers changed from: package-private */
    public Handler[] accessCheckedHandlers() {
        return (Handler[]) this.handlers.toArray(emptyHandlers);
    }

    public void setUseParentHandlers(boolean z) {
        checkPermission();
        this.useParentHandlers = z;
    }

    public boolean getUseParentHandlers() {
        return this.useParentHandlers;
    }

    private static ResourceBundle findSystemResourceBundle(final Locale locale) {
        return (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() {
            public ResourceBundle run() {
                try {
                    return ResourceBundle.getBundle(Logger.SYSTEM_LOGGER_RB_NAME, Locale.this, ClassLoader.getSystemClassLoader());
                } catch (MissingResourceException e) {
                    throw new InternalError(e.toString());
                }
            }
        });
    }

    private synchronized ResourceBundle findResourceBundle(String str, boolean z) {
        if (str == null) {
            return null;
        }
        Locale locale = Locale.getDefault();
        LoggerBundle loggerBundle2 = this.loggerBundle;
        if (loggerBundle2.userBundle != null && str.equals(loggerBundle2.resourceBundleName)) {
            return loggerBundle2.userBundle;
        } else if (this.catalog != null && locale.equals(this.catalogLocale) && str.equals(this.catalogName)) {
            return this.catalog;
        } else if (str.equals(SYSTEM_LOGGER_RB_NAME)) {
            ResourceBundle findSystemResourceBundle = findSystemResourceBundle(locale);
            this.catalog = findSystemResourceBundle;
            this.catalogName = str;
            this.catalogLocale = locale;
            return findSystemResourceBundle;
        } else {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = ClassLoader.getSystemClassLoader();
            }
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(str, locale, contextClassLoader);
                this.catalog = bundle;
                this.catalogName = str;
                this.catalogLocale = locale;
                return bundle;
            } catch (MissingResourceException unused) {
                if (!z) {
                    return null;
                }
                ClassLoader callersClassLoader = getCallersClassLoader();
                if (callersClassLoader == null || callersClassLoader == contextClassLoader) {
                    return null;
                }
                try {
                    ResourceBundle bundle2 = ResourceBundle.getBundle(str, locale, callersClassLoader);
                    this.catalog = bundle2;
                    this.catalogName = str;
                    this.catalogLocale = locale;
                    return bundle2;
                } catch (MissingResourceException unused2) {
                    return null;
                }
            }
        }
    }

    private synchronized void setupResourceInfo(String str, Class<?> cls) {
        LoggerBundle loggerBundle2 = this.loggerBundle;
        if (loggerBundle2.resourceBundleName != null) {
            if (!loggerBundle2.resourceBundleName.equals(str)) {
                throw new IllegalArgumentException(loggerBundle2.resourceBundleName + " != " + str);
            }
        } else if (str != null) {
            setCallersClassLoaderRef(cls);
            if (this.isSystemLogger && getCallersClassLoader() != null) {
                checkPermission();
            }
            if (findResourceBundle(str, true) != null) {
                this.loggerBundle = LoggerBundle.get(str, (ResourceBundle) null);
                return;
            }
            this.callersClassLoaderRef = null;
            throw new MissingResourceException("Can't find " + str + " bundle", str, "");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setResourceBundle(java.util.ResourceBundle r4) {
        /*
            r3 = this;
            r3.checkPermission()
            java.lang.String r0 = r4.getBaseBundleName()
            if (r0 == 0) goto L_0x0037
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L_0x0037
            monitor-enter(r3)
            java.util.logging.Logger$LoggerBundle r1 = r3.loggerBundle     // Catch:{ all -> 0x0034 }
            java.lang.String r2 = r1.resourceBundleName     // Catch:{ all -> 0x0034 }
            if (r2 == 0) goto L_0x0021
            java.lang.String r1 = r1.resourceBundleName     // Catch:{ all -> 0x0034 }
            boolean r1 = r1.equals(r0)     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x001f
            goto L_0x0021
        L_0x001f:
            r1 = 0
            goto L_0x0022
        L_0x0021:
            r1 = 1
        L_0x0022:
            if (r1 == 0) goto L_0x002c
            java.util.logging.Logger$LoggerBundle r4 = java.util.logging.Logger.LoggerBundle.get(r0, r4)     // Catch:{ all -> 0x0034 }
            r3.loggerBundle = r4     // Catch:{ all -> 0x0034 }
            monitor-exit(r3)     // Catch:{ all -> 0x0034 }
            return
        L_0x002c:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x0034 }
            java.lang.String r0 = "can't replace resource bundle"
            r4.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0034 }
            throw r4     // Catch:{ all -> 0x0034 }
        L_0x0034:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0034 }
            throw r4
        L_0x0037:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "resource bundle must have a name"
            r3.<init>((java.lang.String) r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.Logger.setResourceBundle(java.util.ResourceBundle):void");
    }

    public Logger getParent() {
        return this.parent;
    }

    public void setParent(Logger logger) {
        logger.getClass();
        if (this.manager == null) {
            this.manager = LogManager.getLogManager();
        }
        this.manager.checkPermission();
        doSetParent(logger);
    }

    private void doSetParent(Logger logger) {
        synchronized (treeLock) {
            LogManager.LoggerWeakRef loggerWeakRef = null;
            if (this.parent != null) {
                Iterator<LogManager.LoggerWeakRef> it = this.parent.kids.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    LogManager.LoggerWeakRef next = it.next();
                    if (((Logger) next.get()) == this) {
                        it.remove();
                        loggerWeakRef = next;
                        break;
                    }
                }
            }
            this.parent = logger;
            if (this.parent.kids == null) {
                this.parent.kids = new ArrayList<>(2);
            }
            if (loggerWeakRef == null) {
                LogManager logManager = this.manager;
                Objects.requireNonNull(logManager);
                loggerWeakRef = new LogManager.LoggerWeakRef(this);
            }
            loggerWeakRef.setParentRef(new WeakReference(this.parent));
            this.parent.kids.add(loggerWeakRef);
            updateEffectiveLevel();
        }
    }

    /* access modifiers changed from: package-private */
    public final void removeChildLogger(LogManager.LoggerWeakRef loggerWeakRef) {
        synchronized (treeLock) {
            Iterator<LogManager.LoggerWeakRef> it = this.kids.iterator();
            while (it.hasNext()) {
                if (it.next() == loggerWeakRef) {
                    it.remove();
                    return;
                }
            }
        }
    }

    private void updateEffectiveLevel() {
        int i;
        if (this.levelObject != null) {
            i = this.levelObject.intValue();
        } else if (this.parent != null) {
            i = this.parent.levelValue;
        } else {
            i = Level.INFO.intValue();
        }
        if (this.levelValue != i) {
            this.levelValue = i;
            if (this.kids != null) {
                for (int i2 = 0; i2 < this.kids.size(); i2++) {
                    Logger logger = (Logger) this.kids.get(i2).get();
                    if (logger != null) {
                        logger.updateEffectiveLevel();
                    }
                }
            }
        }
    }

    private LoggerBundle getEffectiveLoggerBundle() {
        String str;
        LoggerBundle loggerBundle2 = this.loggerBundle;
        if (loggerBundle2.isSystemBundle()) {
            return SYSTEM_BUNDLE;
        }
        ResourceBundle resourceBundle = getResourceBundle();
        if (resourceBundle != null && resourceBundle == loggerBundle2.userBundle) {
            return loggerBundle2;
        }
        if (resourceBundle != null) {
            return LoggerBundle.get(getResourceBundleName(), resourceBundle);
        }
        Logger logger = this.parent;
        while (logger != null) {
            LoggerBundle loggerBundle3 = logger.loggerBundle;
            if (loggerBundle3.isSystemBundle()) {
                return SYSTEM_BUNDLE;
            }
            if (loggerBundle3.userBundle != null) {
                return loggerBundle3;
            }
            if (this.isSystemLogger) {
                str = logger.isSystemLogger ? loggerBundle3.resourceBundleName : null;
            } else {
                str = logger.getResourceBundleName();
            }
            if (str != null) {
                return LoggerBundle.get(str, findResourceBundle(str, true));
            }
            logger = this.isSystemLogger ? logger.parent : logger.getParent();
        }
        return NO_RESOURCE_BUNDLE;
    }
}
