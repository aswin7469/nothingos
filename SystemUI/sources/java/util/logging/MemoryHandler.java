package java.util.logging;

public class MemoryHandler extends Handler {
    private static final int DEFAULT_SIZE = 1000;
    private LogRecord[] buffer;
    int count;
    private volatile Level pushLevel;
    private int size;
    int start;
    private Handler target;

    private void configure() {
        LogManager logManager = LogManager.getLogManager();
        String name = getClass().getName();
        this.pushLevel = logManager.getLevelProperty(name + ".push", Level.SEVERE);
        int intProperty = logManager.getIntProperty(name + ".size", 1000);
        this.size = intProperty;
        if (intProperty <= 0) {
            this.size = 1000;
        }
        setLevel(logManager.getLevelProperty(name + ".level", Level.ALL));
        setFilter(logManager.getFilterProperty(name + ".filter", (Filter) null));
        setFormatter(logManager.getFormatterProperty(name + ".formatter", new SimpleFormatter()));
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0040 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MemoryHandler() {
        /*
            r4 = this;
            r4.<init>()
            r0 = 0
            r4.sealed = r0
            r4.configure()
            r0 = 1
            r4.sealed = r0
            java.util.logging.LogManager r0 = java.util.logging.LogManager.getLogManager()
            java.lang.Class r1 = r4.getClass()
            java.lang.String r1 = r1.getName()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".target"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r0 = r0.getProperty(r2)
            if (r0 == 0) goto L_0x0072
            java.lang.ClassLoader r1 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ Exception -> 0x0040 }
            java.lang.Class r1 = r1.loadClass(r0)     // Catch:{ Exception -> 0x0040 }
            java.lang.Object r1 = r1.newInstance()     // Catch:{ Exception -> 0x0040 }
            java.util.logging.Handler r1 = (java.util.logging.Handler) r1     // Catch:{ Exception -> 0x0040 }
            r4.target = r1     // Catch:{ Exception -> 0x0040 }
            goto L_0x0054
        L_0x0040:
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x0058 }
            java.lang.ClassLoader r1 = r1.getContextClassLoader()     // Catch:{ Exception -> 0x0058 }
            java.lang.Class r1 = r1.loadClass(r0)     // Catch:{ Exception -> 0x0058 }
            java.lang.Object r1 = r1.newInstance()     // Catch:{ Exception -> 0x0058 }
            java.util.logging.Handler r1 = (java.util.logging.Handler) r1     // Catch:{ Exception -> 0x0058 }
            r4.target = r1     // Catch:{ Exception -> 0x0058 }
        L_0x0054:
            r4.init()
            return
        L_0x0058:
            r4 = move-exception
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "MemoryHandler can't load handler target \""
            r2.<init>((java.lang.String) r3)
            r2.append((java.lang.String) r0)
            java.lang.String r0 = "\""
            r2.append((java.lang.String) r0)
            java.lang.String r0 = r2.toString()
            r1.<init>(r0, r4)
            throw r1
        L_0x0072:
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "The handler "
            r0.<init>((java.lang.String) r2)
            r0.append((java.lang.String) r1)
            java.lang.String r1 = " does not specify a target"
            r0.append((java.lang.String) r1)
            java.lang.String r0 = r0.toString()
            r4.<init>((java.lang.String) r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.MemoryHandler.<init>():void");
    }

    private void init() {
        this.buffer = new LogRecord[this.size];
        this.start = 0;
        this.count = 0;
    }

    public MemoryHandler(Handler handler, int i, Level level) {
        if (handler == null || level == null) {
            throw null;
        } else if (i > 0) {
            this.sealed = false;
            configure();
            this.sealed = true;
            this.target = handler;
            this.pushLevel = level;
            this.size = i;
            init();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0039, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void publish(java.util.logging.LogRecord r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.isLoggable(r6)     // Catch:{ all -> 0x003a }
            if (r0 != 0) goto L_0x0009
            monitor-exit(r5)
            return
        L_0x0009:
            int r0 = r5.start     // Catch:{ all -> 0x003a }
            int r1 = r5.count     // Catch:{ all -> 0x003a }
            int r2 = r0 + r1
            java.util.logging.LogRecord[] r3 = r5.buffer     // Catch:{ all -> 0x003a }
            int r4 = r3.length     // Catch:{ all -> 0x003a }
            int r2 = r2 % r4
            r3[r2] = r6     // Catch:{ all -> 0x003a }
            int r2 = r3.length     // Catch:{ all -> 0x003a }
            if (r1 >= r2) goto L_0x001d
            int r1 = r1 + 1
            r5.count = r1     // Catch:{ all -> 0x003a }
            goto L_0x0025
        L_0x001d:
            int r0 = r0 + 1
            r5.start = r0     // Catch:{ all -> 0x003a }
            int r1 = r3.length     // Catch:{ all -> 0x003a }
            int r0 = r0 % r1
            r5.start = r0     // Catch:{ all -> 0x003a }
        L_0x0025:
            java.util.logging.Level r6 = r6.getLevel()     // Catch:{ all -> 0x003a }
            int r6 = r6.intValue()     // Catch:{ all -> 0x003a }
            java.util.logging.Level r0 = r5.pushLevel     // Catch:{ all -> 0x003a }
            int r0 = r0.intValue()     // Catch:{ all -> 0x003a }
            if (r6 < r0) goto L_0x0038
            r5.push()     // Catch:{ all -> 0x003a }
        L_0x0038:
            monitor-exit(r5)
            return
        L_0x003a:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.MemoryHandler.publish(java.util.logging.LogRecord):void");
    }

    public synchronized void push() {
        for (int i = 0; i < this.count; i++) {
            LogRecord[] logRecordArr = this.buffer;
            this.target.publish(logRecordArr[(this.start + i) % logRecordArr.length]);
        }
        this.start = 0;
        this.count = 0;
    }

    public void flush() {
        this.target.flush();
    }

    public void close() throws SecurityException {
        this.target.close();
        setLevel(Level.OFF);
    }

    public synchronized void setPushLevel(Level level) throws SecurityException {
        if (level != null) {
            checkPermission();
            this.pushLevel = level;
        } else {
            throw new NullPointerException();
        }
    }

    public Level getPushLevel() {
        return this.pushLevel;
    }

    public boolean isLoggable(LogRecord logRecord) {
        return super.isLoggable(logRecord);
    }
}
