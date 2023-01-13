package java.util.logging;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.beans.PropertyChangeListener;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.p026io.BufferedInputStream;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.PrintStream;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.WeakHashMap;

public class LogManager {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String LOGGING_MXBEAN_NAME = "java.util.logging:type=Logging";
    private static final int MAX_ITERATIONS = 400;
    /* access modifiers changed from: private */
    public static final Level defaultLevel = Level.INFO;
    private static LoggingMXBean loggingMXBean = null;
    /* access modifiers changed from: private */
    public static final LogManager manager = ((LogManager) AccessController.doPrivileged(new PrivilegedAction<LogManager>() {
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0036  */
        /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.logging.LogManager run() {
            /*
                r5 = this;
                r5 = 0
                java.lang.String r0 = "java.util.logging.manager"
                java.lang.String r0 = java.lang.System.getProperty(r0)     // Catch:{ Exception -> 0x0017 }
                if (r0 == 0) goto L_0x0034
                java.lang.Class r1 = java.util.logging.LogManager.getClassInstance(r0)     // Catch:{ Exception -> 0x0015 }
                java.lang.Object r1 = r1.newInstance()     // Catch:{ Exception -> 0x0015 }
                java.util.logging.LogManager r1 = (java.util.logging.LogManager) r1     // Catch:{ Exception -> 0x0015 }
                r5 = r1
                goto L_0x0034
            L_0x0015:
                r1 = move-exception
                goto L_0x0019
            L_0x0017:
                r1 = move-exception
                r0 = r5
            L_0x0019:
                java.io.PrintStream r2 = java.lang.System.err
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                java.lang.String r4 = "Could not load Logmanager \""
                r3.<init>((java.lang.String) r4)
                r3.append((java.lang.String) r0)
                java.lang.String r0 = "\""
                r3.append((java.lang.String) r0)
                java.lang.String r0 = r3.toString()
                r2.println((java.lang.String) r0)
                r1.printStackTrace()
            L_0x0034:
                if (r5 != 0) goto L_0x003b
                java.util.logging.LogManager r5 = new java.util.logging.LogManager
                r5.<init>()
            L_0x003b:
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.logging.LogManager.C44301.run():java.util.logging.LogManager");
        }
    }));
    private WeakHashMap<Object, LoggerContext> contextsMap;
    private final Permission controlPermission;
    /* access modifiers changed from: private */
    public boolean deathImminent;
    /* access modifiers changed from: private */
    public volatile boolean initializationDone;
    /* access modifiers changed from: private */
    public boolean initializedCalled;
    /* access modifiers changed from: private */
    public boolean initializedGlobalHandlers;
    private final Map<Object, Integer> listenerMap;
    /* access modifiers changed from: private */
    public final ReferenceQueue<Logger> loggerRefQueue;
    private volatile Properties props;
    private volatile boolean readPrimordialConfiguration;
    /* access modifiers changed from: private */
    public volatile Logger rootLogger;
    private final LoggerContext systemContext;
    private final LoggerContext userContext;

    private class Cleaner extends Thread {
        private Cleaner() {
            setContextClassLoader((ClassLoader) null);
        }

        public void run() {
            LogManager unused = LogManager.manager;
            synchronized (LogManager.this) {
                LogManager.this.deathImminent = true;
                LogManager.this.initializedGlobalHandlers = true;
            }
            LogManager.this.reset();
        }
    }

    protected LogManager() {
        this(checkSubclassPermissions());
    }

    private LogManager(Void voidR) {
        this.props = new Properties();
        this.listenerMap = new HashMap();
        this.systemContext = new SystemLoggerContext();
        this.userContext = new LoggerContext();
        this.initializedGlobalHandlers = true;
        this.initializedCalled = false;
        this.initializationDone = false;
        this.contextsMap = null;
        this.loggerRefQueue = new ReferenceQueue<>();
        this.controlPermission = new LoggingPermission("control", (String) null);
        try {
            Runtime.getRuntime().addShutdownHook(new Cleaner());
        } catch (IllegalStateException unused) {
        }
    }

    private static Void checkSubclassPermissions() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        securityManager.checkPermission(new RuntimePermission("shutdownHooks"));
        securityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
        return null;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x002c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void ensureLogManagerInitialized() {
        /*
            r2 = this;
            boolean r0 = r2.initializationDone
            if (r0 != 0) goto L_0x0030
            java.util.logging.LogManager r0 = manager
            if (r2 == r0) goto L_0x0009
            goto L_0x0030
        L_0x0009:
            monitor-enter(r2)
            boolean r0 = r2.initializedCalled     // Catch:{ all -> 0x002d }
            r1 = 1
            if (r0 != r1) goto L_0x0011
            r0 = r1
            goto L_0x0012
        L_0x0011:
            r0 = 0
        L_0x0012:
            if (r0 != 0) goto L_0x002b
            boolean r0 = r2.initializationDone     // Catch:{ all -> 0x002d }
            if (r0 == 0) goto L_0x0019
            goto L_0x002b
        L_0x0019:
            r2.initializedCalled = r1     // Catch:{ all -> 0x002d }
            java.util.logging.LogManager$2 r0 = new java.util.logging.LogManager$2     // Catch:{ all -> 0x0027 }
            r0.<init>(r2)     // Catch:{ all -> 0x0027 }
            java.security.AccessController.doPrivileged(r0)     // Catch:{ all -> 0x0027 }
            r2.initializationDone = r1     // Catch:{ all -> 0x002d }
            monitor-exit(r2)     // Catch:{ all -> 0x002d }
            return
        L_0x0027:
            r0 = move-exception
            r2.initializationDone = r1     // Catch:{ all -> 0x002d }
            throw r0     // Catch:{ all -> 0x002d }
        L_0x002b:
            monitor-exit(r2)     // Catch:{ all -> 0x002d }
            return
        L_0x002d:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x002d }
            throw r0
        L_0x0030:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.LogManager.ensureLogManagerInitialized():void");
    }

    public static LogManager getLogManager() {
        LogManager logManager = manager;
        if (logManager != null) {
            logManager.ensureLogManagerInitialized();
        }
        return logManager;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:3|4|(2:6|(2:8|9)(3:10|11|12))|13|14|15|20) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x001a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readPrimordialConfiguration() {
        /*
            r1 = this;
            boolean r0 = r1.readPrimordialConfiguration
            if (r0 != 0) goto L_0x001f
            monitor-enter(r1)
            boolean r0 = r1.readPrimordialConfiguration     // Catch:{ all -> 0x001c }
            if (r0 != 0) goto L_0x001a
            java.io.PrintStream r0 = java.lang.System.out     // Catch:{ all -> 0x001c }
            if (r0 != 0) goto L_0x000f
            monitor-exit(r1)     // Catch:{ all -> 0x001c }
            return
        L_0x000f:
            r0 = 1
            r1.readPrimordialConfiguration = r0     // Catch:{ all -> 0x001c }
            java.util.logging.LogManager$3 r0 = new java.util.logging.LogManager$3     // Catch:{ Exception -> 0x001a }
            r0.<init>()     // Catch:{ Exception -> 0x001a }
            java.security.AccessController.doPrivileged(r0)     // Catch:{ Exception -> 0x001a }
        L_0x001a:
            monitor-exit(r1)     // Catch:{ all -> 0x001c }
            goto L_0x001f
        L_0x001c:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x001c }
            throw r0
        L_0x001f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.LogManager.readPrimordialConfiguration():void");
    }

    @Deprecated
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) throws SecurityException {
        PropertyChangeListener propertyChangeListener2 = (PropertyChangeListener) Objects.requireNonNull(propertyChangeListener);
        checkPermission();
        synchronized (this.listenerMap) {
            Integer num = this.listenerMap.get(propertyChangeListener2);
            int i = 1;
            if (num != null) {
                i = 1 + num.intValue();
            }
            this.listenerMap.put(propertyChangeListener2, Integer.valueOf(i));
        }
    }

    @Deprecated
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) throws SecurityException {
        checkPermission();
        if (propertyChangeListener != null) {
            synchronized (this.listenerMap) {
                Integer num = this.listenerMap.get(propertyChangeListener);
                if (num != null) {
                    int intValue = num.intValue();
                    if (intValue == 1) {
                        this.listenerMap.remove(propertyChangeListener);
                    } else {
                        this.listenerMap.put(propertyChangeListener, Integer.valueOf(intValue - 1));
                    }
                }
            }
        }
    }

    private LoggerContext getUserContext() {
        return this.userContext;
    }

    /* access modifiers changed from: package-private */
    public final LoggerContext getSystemContext() {
        return this.systemContext;
    }

    private List<LoggerContext> contexts() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(getSystemContext());
        arrayList.add(getUserContext());
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public Logger demandLogger(String str, String str2, Class<?> cls) {
        Logger logger = getLogger(str);
        if (logger == null) {
            Logger logger2 = new Logger(str, str2, cls, this, false);
            while (!addLogger(logger2)) {
                logger = getLogger(str);
                if (logger != null) {
                }
            }
            return logger2;
        }
        return logger;
    }

    /* access modifiers changed from: package-private */
    public Logger demandSystemLogger(String str, String str2) {
        final Logger logger;
        final Logger demandLogger = getSystemContext().demandLogger(str, str2);
        do {
            if (addLogger(demandLogger)) {
                logger = demandLogger;
                continue;
            } else {
                logger = getLogger(str);
                continue;
            }
        } while (logger == null);
        if (logger != demandLogger && demandLogger.accessCheckedHandlers().length == 0) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    for (Handler addHandler : logger.accessCheckedHandlers()) {
                        demandLogger.addHandler(addHandler);
                    }
                    return null;
                }
            });
        }
        return demandLogger;
    }

    /* access modifiers changed from: private */
    public static Class getClassInstance(String str) throws ClassNotFoundException {
        try {
            return ClassLoader.getSystemClassLoader().loadClass(str);
        } catch (ClassNotFoundException unused) {
            return Thread.currentThread().getContextClassLoader().loadClass(str);
        }
    }

    class LoggerContext {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final Hashtable<String, LoggerWeakRef> namedLoggers;
        private final LogNode root;

        static {
            Class<LogManager> cls = LogManager.class;
        }

        private LoggerContext() {
            this.namedLoggers = new Hashtable<>();
            this.root = new LogNode((LogNode) null, this);
        }

        /* access modifiers changed from: package-private */
        public final boolean requiresDefaultLoggers() {
            boolean z = getOwner() == LogManager.manager;
            if (z) {
                getOwner().ensureLogManagerInitialized();
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        public final LogManager getOwner() {
            return LogManager.this;
        }

        /* access modifiers changed from: package-private */
        public final Logger getRootLogger() {
            return getOwner().rootLogger;
        }

        /* access modifiers changed from: package-private */
        public final Logger getGlobalLogger() {
            return Logger.global;
        }

        /* access modifiers changed from: package-private */
        public Logger demandLogger(String str, String str2) {
            return getOwner().demandLogger(str, str2, (Class<?>) null);
        }

        private void ensureInitialized() {
            if (requiresDefaultLoggers()) {
                ensureDefaultLogger(getRootLogger());
                ensureDefaultLogger(getGlobalLogger());
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
            return r0;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized java.util.logging.Logger findLogger(java.lang.String r2) {
            /*
                r1 = this;
                monitor-enter(r1)
                r1.ensureInitialized()     // Catch:{ all -> 0x001e }
                java.util.Hashtable<java.lang.String, java.util.logging.LogManager$LoggerWeakRef> r0 = r1.namedLoggers     // Catch:{ all -> 0x001e }
                java.lang.Object r2 = r0.get(r2)     // Catch:{ all -> 0x001e }
                java.util.logging.LogManager$LoggerWeakRef r2 = (java.util.logging.LogManager.LoggerWeakRef) r2     // Catch:{ all -> 0x001e }
                if (r2 != 0) goto L_0x0011
                monitor-exit(r1)
                r1 = 0
                return r1
            L_0x0011:
                java.lang.Object r0 = r2.get()     // Catch:{ all -> 0x001e }
                java.util.logging.Logger r0 = (java.util.logging.Logger) r0     // Catch:{ all -> 0x001e }
                if (r0 != 0) goto L_0x001c
                r2.dispose()     // Catch:{ all -> 0x001e }
            L_0x001c:
                monitor-exit(r1)
                return r0
            L_0x001e:
                r2 = move-exception
                monitor-exit(r1)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.logging.LogManager.LoggerContext.findLogger(java.lang.String):java.util.logging.Logger");
        }

        private void ensureAllDefaultLoggers(Logger logger) {
            if (requiresDefaultLoggers()) {
                String name = logger.getName();
                if (!name.isEmpty()) {
                    ensureDefaultLogger(getRootLogger());
                    if (!Logger.GLOBAL_LOGGER_NAME.equals(name)) {
                        ensureDefaultLogger(getGlobalLogger());
                    }
                }
            }
        }

        private void ensureDefaultLogger(Logger logger) {
            if (requiresDefaultLoggers() && logger != null) {
                if ((logger == Logger.global || logger == LogManager.this.rootLogger) && !this.namedLoggers.containsKey(logger.getName())) {
                    addLocalLogger(logger, false);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean addLocalLogger(Logger logger) {
            return addLocalLogger(logger, requiresDefaultLoggers());
        }

        /* access modifiers changed from: package-private */
        public synchronized boolean addLocalLogger(Logger logger, boolean z) {
            if (z) {
                ensureAllDefaultLoggers(logger);
            }
            String name = logger.getName();
            if (name != null) {
                LoggerWeakRef loggerWeakRef = this.namedLoggers.get(name);
                Logger logger2 = null;
                if (loggerWeakRef != null) {
                    if (!loggerWeakRef.refersTo(null)) {
                        return false;
                    }
                    loggerWeakRef.dispose();
                }
                LogManager owner = getOwner();
                logger.setLogManager(owner);
                Objects.requireNonNull(owner);
                LoggerWeakRef loggerWeakRef2 = new LoggerWeakRef(logger);
                this.namedLoggers.put(name, loggerWeakRef2);
                Level levelProperty = owner.getLevelProperty(name + ".level", (Level) null);
                if (levelProperty != null && !logger.isLevelInitialized()) {
                    LogManager.doSetLevel(logger, levelProperty);
                }
                processParentHandlers(logger, name);
                LogNode node = getNode(name);
                node.loggerRef = loggerWeakRef2;
                LogNode logNode = node.parent;
                while (true) {
                    if (logNode != null) {
                        LoggerWeakRef loggerWeakRef3 = logNode.loggerRef;
                        if (loggerWeakRef3 != null && (logger2 = (Logger) loggerWeakRef3.get()) != null) {
                            break;
                        }
                        logNode = logNode.parent;
                    } else {
                        break;
                    }
                }
                if (logger2 != null) {
                    LogManager.doSetParent(logger, logger2);
                }
                node.walkAndSetParent(logger);
                loggerWeakRef2.setNode(node);
                return true;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: package-private */
        public synchronized void removeLoggerRef(String str, LoggerWeakRef loggerWeakRef) {
            this.namedLoggers.remove(str, loggerWeakRef);
        }

        /* access modifiers changed from: package-private */
        public synchronized Enumeration<String> getLoggerNames() {
            ensureInitialized();
            return this.namedLoggers.keys();
        }

        private void processParentHandlers(final Logger logger, final String str) {
            final LogManager owner = getOwner();
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    if (logger == owner.rootLogger) {
                        return null;
                    }
                    LogManager logManager = owner;
                    if (logManager.getBooleanProperty(str + ".useParentHandlers", true)) {
                        return null;
                    }
                    logger.setUseParentHandlers(false);
                    return null;
                }
            });
            int i = 1;
            while (true) {
                int indexOf = str.indexOf(BaseIconCache.EMPTY_CLASS_NAME, i);
                if (indexOf >= 0) {
                    String substring = str.substring(0, indexOf);
                    if (owner.getProperty(substring + ".level") == null) {
                        if (owner.getProperty(substring + ".handlers") == null) {
                            i = indexOf + 1;
                        }
                    }
                    demandLogger(substring, (String) null);
                    i = indexOf + 1;
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public LogNode getNode(String str) {
            String str2;
            if (str == null || str.equals("")) {
                return this.root;
            }
            LogNode logNode = this.root;
            while (str.length() > 0) {
                int indexOf = str.indexOf(BaseIconCache.EMPTY_CLASS_NAME);
                if (indexOf > 0) {
                    String substring = str.substring(0, indexOf);
                    str2 = str.substring(indexOf + 1);
                    str = substring;
                } else {
                    str2 = "";
                }
                if (logNode.children == null) {
                    logNode.children = new HashMap<>();
                }
                LogNode logNode2 = logNode.children.get(str);
                if (logNode2 == null) {
                    logNode2 = new LogNode(logNode, this);
                    logNode.children.put(str, logNode2);
                }
                logNode = logNode2;
                str = str2;
            }
            return logNode;
        }
    }

    final class SystemLoggerContext extends LoggerContext {
        SystemLoggerContext() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Logger demandLogger(String str, String str2) {
            Logger findLogger = findLogger(str);
            if (findLogger == null) {
                Logger logger = new Logger(str, str2, (Class<?>) null, getOwner(), true);
                do {
                    if (addLocalLogger(logger)) {
                        findLogger = logger;
                        continue;
                    } else {
                        findLogger = findLogger(str);
                        continue;
                    }
                } while (findLogger == null);
            }
            return findLogger;
        }
    }

    private void loadLoggerHandlers(final Logger logger, String str, final String str2) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                String[] r0 = LogManager.this.parseClassNames(str2);
                for (String str : r0) {
                    try {
                        Handler handler = (Handler) LogManager.getClassInstance(str).newInstance();
                        String property = LogManager.this.getProperty(str + ".level");
                        if (property != null) {
                            Level findLevel = Level.findLevel(property);
                            if (findLevel != null) {
                                handler.setLevel(findLevel);
                            } else {
                                System.err.println("Can't set level for " + str);
                            }
                        }
                        logger.addHandler(handler);
                    } catch (Exception e) {
                        System.err.println("Can't load log handler \"" + str + "\"");
                        System.err.println("" + e);
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }

    final class LoggerWeakRef extends WeakReference<Logger> {
        private boolean disposed = false;
        private String name;
        private LogNode node;
        private WeakReference<Logger> parentRef;

        LoggerWeakRef(Logger logger) {
            super(logger, LogManager.this.loggerRefQueue);
            this.name = logger.getName();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0010, code lost:
            r2 = r0.context;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0012, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            r0.context.removeLoggerRef(r5.name, r5);
            r5.name = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001e, code lost:
            if (r0.loggerRef != r5) goto L_0x0022;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0020, code lost:
            r0.loggerRef = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0022, code lost:
            r5.node = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0024, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0029, code lost:
            r0 = r5.parentRef;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x002b, code lost:
            if (r0 == null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x002d, code lost:
            r0 = r0.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0033, code lost:
            if (r0 == null) goto L_0x0038;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0035, code lost:
            r0.removeChildLogger(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0038, code lost:
            r5.parentRef = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000b, code lost:
            r0 = r5.node;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x000e, code lost:
            if (r0 == null) goto L_0x0029;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispose() {
            /*
                r5 = this;
                monitor-enter(r5)
                boolean r0 = r5.disposed     // Catch:{ all -> 0x003b }
                if (r0 == 0) goto L_0x0007
                monitor-exit(r5)     // Catch:{ all -> 0x003b }
                return
            L_0x0007:
                r0 = 1
                r5.disposed = r0     // Catch:{ all -> 0x003b }
                monitor-exit(r5)     // Catch:{ all -> 0x003b }
                java.util.logging.LogManager$LogNode r0 = r5.node
                r1 = 0
                if (r0 == 0) goto L_0x0029
                java.util.logging.LogManager$LoggerContext r2 = r0.context
                monitor-enter(r2)
                java.util.logging.LogManager$LoggerContext r3 = r0.context     // Catch:{ all -> 0x0026 }
                java.lang.String r4 = r5.name     // Catch:{ all -> 0x0026 }
                r3.removeLoggerRef(r4, r5)     // Catch:{ all -> 0x0026 }
                r5.name = r1     // Catch:{ all -> 0x0026 }
                java.util.logging.LogManager$LoggerWeakRef r3 = r0.loggerRef     // Catch:{ all -> 0x0026 }
                if (r3 != r5) goto L_0x0022
                r0.loggerRef = r1     // Catch:{ all -> 0x0026 }
            L_0x0022:
                r5.node = r1     // Catch:{ all -> 0x0026 }
                monitor-exit(r2)     // Catch:{ all -> 0x0026 }
                goto L_0x0029
            L_0x0026:
                r5 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0026 }
                throw r5
            L_0x0029:
                java.lang.ref.WeakReference<java.util.logging.Logger> r0 = r5.parentRef
                if (r0 == 0) goto L_0x003a
                java.lang.Object r0 = r0.get()
                java.util.logging.Logger r0 = (java.util.logging.Logger) r0
                if (r0 == 0) goto L_0x0038
                r0.removeChildLogger(r5)
            L_0x0038:
                r5.parentRef = r1
            L_0x003a:
                return
            L_0x003b:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x003b }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.logging.LogManager.LoggerWeakRef.dispose():void");
        }

        /* access modifiers changed from: package-private */
        public void setNode(LogNode logNode) {
            this.node = logNode;
        }

        /* access modifiers changed from: package-private */
        public void setParentRef(WeakReference<Logger> weakReference) {
            this.parentRef = weakReference;
        }
    }

    /* access modifiers changed from: package-private */
    public final void drainLoggerRefQueueBounded() {
        LoggerWeakRef loggerWeakRef;
        for (int i = 0; i < 400 && (r1 = this.loggerRefQueue) != null && (loggerWeakRef = (LoggerWeakRef) r1.poll()) != null; i++) {
            loggerWeakRef.dispose();
        }
    }

    public boolean addLogger(Logger logger) {
        String name = logger.getName();
        name.getClass();
        drainLoggerRefQueueBounded();
        if (!getUserContext().addLocalLogger(logger)) {
            return false;
        }
        loadLoggerHandlers(logger, name, name + ".handlers");
        return true;
    }

    /* access modifiers changed from: private */
    public static void doSetLevel(final Logger logger, final Level level) {
        if (System.getSecurityManager() == null) {
            logger.setLevel(level);
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    Logger.this.setLevel(level);
                    return null;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static void doSetParent(final Logger logger, final Logger logger2) {
        if (System.getSecurityManager() == null) {
            logger.setParent(logger2);
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    Logger.this.setParent(logger2);
                    return null;
                }
            });
        }
    }

    public Logger getLogger(String str) {
        return getUserContext().findLogger(str);
    }

    public Enumeration<String> getLoggerNames() {
        return getUserContext().getLoggerNames();
    }

    public void readConfiguration() throws IOException, SecurityException {
        InputStream inputStream;
        checkPermission();
        String property = System.getProperty("java.util.logging.config.class");
        if (property != null) {
            try {
                getClassInstance(property).newInstance();
                return;
            } catch (Exception e) {
                PrintStream printStream = System.err;
                printStream.println("Logging configuration class \"" + property + "\" failed");
                PrintStream printStream2 = System.err;
                printStream2.println("" + e);
            }
        }
        String property2 = System.getProperty("java.util.logging.config.file");
        if (property2 == null) {
            String property3 = System.getProperty("java.home");
            if (property3 != null) {
                property2 = new File(new File(property3, "lib"), "logging.properties").getCanonicalPath();
            } else {
                throw new Error("Can't find java.home ??");
            }
        }
        try {
            inputStream = new FileInputStream(property2);
        } catch (Exception e2) {
            inputStream = LogManager.class.getResourceAsStream("logging.properties");
            if (inputStream == null) {
                throw e2;
            }
        }
        try {
            readConfiguration(new BufferedInputStream(inputStream));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public void reset() throws SecurityException {
        checkPermission();
        synchronized (this) {
            this.props = new Properties();
            this.initializedGlobalHandlers = true;
        }
        for (LoggerContext next : contexts()) {
            Enumeration<String> loggerNames = next.getLoggerNames();
            while (loggerNames.hasMoreElements()) {
                Logger findLogger = next.findLogger(loggerNames.nextElement());
                if (findLogger != null) {
                    resetLogger(findLogger);
                }
            }
        }
    }

    private void resetLogger(Logger logger) {
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            logger.removeHandler(handler);
            try {
                handler.close();
            } catch (Exception unused) {
            }
        }
        String name = logger.getName();
        if (name == null || !name.equals("")) {
            logger.setLevel((Level) null);
        } else {
            logger.setLevel(defaultLevel);
        }
    }

    /* access modifiers changed from: private */
    public String[] parseClassNames(String str) {
        String property = getProperty(str);
        int i = 0;
        if (property == null) {
            return new String[0];
        }
        String trim = property.trim();
        ArrayList arrayList = new ArrayList();
        while (i < trim.length()) {
            int i2 = i;
            while (i2 < trim.length() && !Character.isWhitespace(trim.charAt(i2)) && trim.charAt(i2) != ',') {
                i2++;
            }
            String substring = trim.substring(i, i2);
            int i3 = i2 + 1;
            String trim2 = substring.trim();
            if (trim2.length() != 0) {
                arrayList.add(trim2);
            }
            i = i3;
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public void readConfiguration(InputStream inputStream) throws IOException, SecurityException {
        HashMap hashMap;
        checkPermission();
        reset();
        this.props.load(inputStream);
        String[] parseClassNames = parseClassNames("config");
        for (String str : parseClassNames) {
            try {
                getClassInstance(str).newInstance();
            } catch (Exception e) {
                System.err.println("Can't load config class \"" + str + "\"");
                System.err.println("" + e);
            }
        }
        setLevelsOnExistingLoggers();
        synchronized (this.listenerMap) {
            hashMap = !this.listenerMap.isEmpty() ? new HashMap(this.listenerMap) : null;
        }
        if (hashMap != null) {
            Object newPropertyChangeEvent = Beans.newPropertyChangeEvent(LogManager.class, (String) null, (Object) null, (Object) null);
            for (Map.Entry entry : hashMap.entrySet()) {
                Object key = entry.getKey();
                int intValue = ((Integer) entry.getValue()).intValue();
                for (int i = 0; i < intValue; i++) {
                    Beans.invokePropertyChange(key, newPropertyChangeEvent);
                }
            }
        }
        synchronized (this) {
            this.initializedGlobalHandlers = false;
        }
    }

    public String getProperty(String str) {
        return this.props.getProperty(str);
    }

    /* access modifiers changed from: package-private */
    public String getStringProperty(String str, String str2) {
        String property = getProperty(str);
        if (property == null) {
            return str2;
        }
        return property.trim();
    }

    /* access modifiers changed from: package-private */
    public int getIntProperty(String str, int i) {
        String property = getProperty(str);
        if (property == null) {
            return i;
        }
        try {
            return Integer.parseInt(property.trim());
        } catch (Exception unused) {
            return i;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean getBooleanProperty(String str, boolean z) {
        String property = getProperty(str);
        if (property == null) {
            return z;
        }
        String lowerCase = property.toLowerCase();
        if (lowerCase.equals("true") || lowerCase.equals("1")) {
            return true;
        }
        if (lowerCase.equals("false") || lowerCase.equals("0")) {
            return false;
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
        r0 = java.util.logging.Level.findLevel(r0.trim());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.logging.Level getLevelProperty(java.lang.String r1, java.util.logging.Level r2) {
        /*
            r0 = this;
            java.lang.String r0 = r0.getProperty(r1)
            if (r0 != 0) goto L_0x0007
            return r2
        L_0x0007:
            java.lang.String r0 = r0.trim()
            java.util.logging.Level r0 = java.util.logging.Level.findLevel(r0)
            if (r0 == 0) goto L_0x0012
            r2 = r0
        L_0x0012:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.LogManager.getLevelProperty(java.lang.String, java.util.logging.Level):java.util.logging.Level");
    }

    /* access modifiers changed from: package-private */
    public Filter getFilterProperty(String str, Filter filter) {
        String property = getProperty(str);
        if (property != null) {
            try {
                return (Filter) getClassInstance(property).newInstance();
            } catch (Exception unused) {
            }
        }
        return filter;
    }

    /* access modifiers changed from: package-private */
    public Formatter getFormatterProperty(String str, Formatter formatter) {
        String property = getProperty(str);
        if (property != null) {
            try {
                return (Formatter) getClassInstance(property).newInstance();
            } catch (Exception unused) {
            }
        }
        return formatter;
    }

    /* access modifiers changed from: private */
    public synchronized void initializeGlobalHandlers() {
        if (!this.initializedGlobalHandlers) {
            this.initializedGlobalHandlers = true;
            if (!this.deathImminent) {
                loadLoggerHandlers(this.rootLogger, (String) null, "handlers");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(this.controlPermission);
        }
    }

    public void checkAccess() throws SecurityException {
        checkPermission();
    }

    private static class LogNode {
        HashMap<String, LogNode> children;
        final LoggerContext context;
        LoggerWeakRef loggerRef;
        LogNode parent;

        LogNode(LogNode logNode, LoggerContext loggerContext) {
            this.parent = logNode;
            this.context = loggerContext;
        }

        /* access modifiers changed from: package-private */
        public void walkAndSetParent(Logger logger) {
            Logger logger2;
            HashMap<String, LogNode> hashMap = this.children;
            if (hashMap != null) {
                for (LogNode next : hashMap.values()) {
                    LoggerWeakRef loggerWeakRef = next.loggerRef;
                    if (loggerWeakRef == null) {
                        logger2 = null;
                    } else {
                        logger2 = (Logger) loggerWeakRef.get();
                    }
                    if (logger2 == null) {
                        next.walkAndSetParent(logger);
                    } else {
                        LogManager.doSetParent(logger2, logger);
                    }
                }
            }
        }
    }

    private final class RootLogger extends Logger {
        private RootLogger() {
            super("", (String) null, (Class<?>) null, LogManager.this, true);
        }

        public void log(LogRecord logRecord) {
            LogManager.this.initializeGlobalHandlers();
            super.log(logRecord);
        }

        public void addHandler(Handler handler) {
            LogManager.this.initializeGlobalHandlers();
            super.addHandler(handler);
        }

        public void removeHandler(Handler handler) {
            LogManager.this.initializeGlobalHandlers();
            super.removeHandler(handler);
        }

        /* access modifiers changed from: package-private */
        public Handler[] accessCheckedHandlers() {
            LogManager.this.initializeGlobalHandlers();
            return super.accessCheckedHandlers();
        }
    }

    private synchronized void setLevelsOnExistingLoggers() {
        Enumeration<?> propertyNames = this.props.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String str = (String) propertyNames.nextElement();
            if (str.endsWith(".level")) {
                String substring = str.substring(0, str.length() - 6);
                Level levelProperty = getLevelProperty(str, (Level) null);
                if (levelProperty == null) {
                    PrintStream printStream = System.err;
                    printStream.println("Bad level value for property: " + str);
                } else {
                    for (LoggerContext findLogger : contexts()) {
                        Logger findLogger2 = findLogger.findLogger(substring);
                        if (findLogger2 != null) {
                            findLogger2.setLevel(levelProperty);
                        }
                    }
                }
            }
        }
    }

    public static synchronized LoggingMXBean getLoggingMXBean() {
        LoggingMXBean loggingMXBean2;
        synchronized (LogManager.class) {
            if (loggingMXBean == null) {
                loggingMXBean = new Logging();
            }
            loggingMXBean2 = loggingMXBean;
        }
        return loggingMXBean2;
    }

    private static class Beans {
        private static final Class<?> propertyChangeEventClass;
        private static final Class<?> propertyChangeListenerClass;
        private static final Method propertyChangeMethod;
        private static final Constructor<?> propertyEventCtor;

        private Beans() {
        }

        static {
            Class<?> cls = getClass("java.beans.PropertyChangeListener");
            propertyChangeListenerClass = cls;
            Class<?> cls2 = getClass("java.beans.PropertyChangeEvent");
            propertyChangeEventClass = cls2;
            propertyChangeMethod = getMethod(cls, "propertyChange", cls2);
            propertyEventCtor = getConstructor(cls2, Object.class, String.class, Object.class, Object.class);
        }

        private static Class<?> getClass(String str) {
            try {
                return Class.forName(str, true, Beans.class.getClassLoader());
            } catch (ClassNotFoundException unused) {
                return null;
            }
        }

        private static Constructor<?> getConstructor(Class<?> cls, Class<?>... clsArr) {
            if (cls == null) {
                return null;
            }
            try {
                return cls.getDeclaredConstructor(clsArr);
            } catch (NoSuchMethodException e) {
                throw new AssertionError((Object) e);
            }
        }

        private static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
            if (cls == null) {
                return null;
            }
            try {
                return cls.getMethod(str, clsArr);
            } catch (NoSuchMethodException e) {
                throw new AssertionError((Object) e);
            }
        }

        static boolean isBeansPresent() {
            return (propertyChangeListenerClass == null || propertyChangeEventClass == null) ? false : true;
        }

        static Object newPropertyChangeEvent(Object obj, String str, Object obj2, Object obj3) {
            try {
                return propertyEventCtor.newInstance(obj, str, obj2, obj3);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException e2) {
                Throwable cause = e2.getCause();
                if (cause instanceof Error) {
                    throw ((Error) cause);
                } else if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                } else {
                    throw new AssertionError((Object) e2);
                }
            }
        }

        static void invokePropertyChange(Object obj, Object obj2) {
            try {
                propertyChangeMethod.invoke(obj, obj2);
            } catch (IllegalAccessException e) {
                throw new AssertionError((Object) e);
            } catch (InvocationTargetException e2) {
                Throwable cause = e2.getCause();
                if (cause instanceof Error) {
                    throw ((Error) cause);
                } else if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                } else {
                    throw new AssertionError((Object) e2);
                }
            }
        }
    }
}
