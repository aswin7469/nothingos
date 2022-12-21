package java.util.logging;

public abstract class Handler {
    private static final int offValue = Level.OFF.intValue();
    private volatile String encoding;
    private volatile ErrorManager errorManager = new ErrorManager();
    private volatile Filter filter;
    private volatile Formatter formatter;
    private volatile Level logLevel = Level.ALL;
    private final LogManager manager = LogManager.getLogManager();
    boolean sealed = true;

    public abstract void close() throws SecurityException;

    public abstract void flush();

    public abstract void publish(LogRecord logRecord);

    protected Handler() {
    }

    public synchronized void setFormatter(Formatter formatter2) throws SecurityException {
        checkPermission();
        formatter2.getClass();
        this.formatter = formatter2;
    }

    public Formatter getFormatter() {
        return this.formatter;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        throw new java.p026io.UnsupportedEncodingException(r2);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0013 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setEncoding(java.lang.String r2) throws java.lang.SecurityException, java.p026io.UnsupportedEncodingException {
        /*
            r1 = this;
            monitor-enter(r1)
            r1.checkPermission()     // Catch:{ all -> 0x001d }
            if (r2 == 0) goto L_0x0019
            boolean r0 = java.nio.charset.Charset.isSupported(r2)     // Catch:{ IllegalCharsetNameException -> 0x0013 }
            if (r0 == 0) goto L_0x000d
            goto L_0x0019
        L_0x000d:
            java.io.UnsupportedEncodingException r0 = new java.io.UnsupportedEncodingException     // Catch:{ IllegalCharsetNameException -> 0x0013 }
            r0.<init>(r2)     // Catch:{ IllegalCharsetNameException -> 0x0013 }
            throw r0     // Catch:{ IllegalCharsetNameException -> 0x0013 }
        L_0x0013:
            java.io.UnsupportedEncodingException r0 = new java.io.UnsupportedEncodingException     // Catch:{ all -> 0x001d }
            r0.<init>(r2)     // Catch:{ all -> 0x001d }
            throw r0     // Catch:{ all -> 0x001d }
        L_0x0019:
            r1.encoding = r2     // Catch:{ all -> 0x001d }
            monitor-exit(r1)
            return
        L_0x001d:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.Handler.setEncoding(java.lang.String):void");
    }

    public String getEncoding() {
        return this.encoding;
    }

    public synchronized void setFilter(Filter filter2) throws SecurityException {
        checkPermission();
        this.filter = filter2;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public synchronized void setErrorManager(ErrorManager errorManager2) {
        checkPermission();
        if (errorManager2 != null) {
            this.errorManager = errorManager2;
        } else {
            throw new NullPointerException();
        }
    }

    public ErrorManager getErrorManager() {
        checkPermission();
        return this.errorManager;
    }

    /* access modifiers changed from: protected */
    public void reportError(String str, Exception exc, int i) {
        try {
            this.errorManager.error(str, exc, i);
        } catch (Exception e) {
            System.err.println("Handler.reportError caught:");
            e.printStackTrace();
        }
    }

    public synchronized void setLevel(Level level) throws SecurityException {
        if (level != null) {
            checkPermission();
            this.logLevel = level;
        } else {
            throw new NullPointerException();
        }
    }

    public Level getLevel() {
        return this.logLevel;
    }

    public boolean isLoggable(LogRecord logRecord) {
        int intValue = getLevel().intValue();
        if (logRecord.getLevel().intValue() < intValue || intValue == offValue) {
            return false;
        }
        Filter filter2 = getFilter();
        if (filter2 == null) {
            return true;
        }
        return filter2.isLoggable(logRecord);
    }

    /* access modifiers changed from: package-private */
    public void checkPermission() throws SecurityException {
        if (this.sealed) {
            this.manager.checkPermission();
        }
    }
}
