package java.util.logging;

import java.p026io.OutputStream;
import java.p026io.OutputStreamWriter;
import java.p026io.UnsupportedEncodingException;
import java.p026io.Writer;

public class StreamHandler extends Handler {
    private boolean doneHeader;
    private OutputStream output;
    private volatile Writer writer;

    /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0075 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void configure() {
        /*
            r5 = this;
            java.util.logging.LogManager r0 = java.util.logging.LogManager.getLogManager()
            java.lang.Class r1 = r5.getClass()
            java.lang.String r1 = r1.getName()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".level"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            java.util.logging.Level r3 = java.util.logging.Level.INFO
            java.util.logging.Level r2 = r0.getLevelProperty(r2, r3)
            r5.setLevel(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".filter"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            r3 = 0
            java.util.logging.Filter r2 = r0.getFilterProperty(r2, r3)
            r5.setFilter(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r4 = ".formatter"
            r2.append((java.lang.String) r4)
            java.lang.String r2 = r2.toString()
            java.util.logging.SimpleFormatter r4 = new java.util.logging.SimpleFormatter
            r4.<init>()
            java.util.logging.Formatter r2 = r0.getFormatterProperty(r2, r4)
            r5.setFormatter(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0075 }
            r2.<init>()     // Catch:{ Exception -> 0x0075 }
            r2.append((java.lang.String) r1)     // Catch:{ Exception -> 0x0075 }
            java.lang.String r1 = ".encoding"
            r2.append((java.lang.String) r1)     // Catch:{ Exception -> 0x0075 }
            java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x0075 }
            java.lang.String r0 = r0.getStringProperty(r1, r3)     // Catch:{ Exception -> 0x0075 }
            r5.setEncoding(r0)     // Catch:{ Exception -> 0x0075 }
            goto L_0x0078
        L_0x0075:
            r5.setEncoding(r3)     // Catch:{ Exception -> 0x0078 }
        L_0x0078:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.StreamHandler.configure():void");
    }

    public StreamHandler() {
        this.sealed = false;
        configure();
        this.sealed = true;
    }

    public StreamHandler(OutputStream outputStream, Formatter formatter) {
        this.sealed = false;
        configure();
        setFormatter(formatter);
        setOutputStream(outputStream);
        this.sealed = true;
    }

    /* access modifiers changed from: protected */
    public synchronized void setOutputStream(OutputStream outputStream) throws SecurityException {
        if (outputStream != null) {
            try {
                flushAndClose();
                this.output = outputStream;
                this.doneHeader = false;
                String encoding = getEncoding();
                if (encoding == null) {
                    this.writer = new OutputStreamWriter(this.output);
                } else {
                    this.writer = new OutputStreamWriter(this.output, encoding);
                }
            } catch (UnsupportedEncodingException e) {
                throw new Error("Unexpected exception " + e);
            } catch (Throwable th) {
                throw th;
            }
        } else {
            throw new NullPointerException();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0023, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setEncoding(java.lang.String r3) throws java.lang.SecurityException, java.p026io.UnsupportedEncodingException {
        /*
            r2 = this;
            monitor-enter(r2)
            super.setEncoding(r3)     // Catch:{ all -> 0x0024 }
            java.io.OutputStream r0 = r2.output     // Catch:{ all -> 0x0024 }
            if (r0 != 0) goto L_0x000a
            monitor-exit(r2)
            return
        L_0x000a:
            r2.flush()     // Catch:{ all -> 0x0024 }
            if (r3 != 0) goto L_0x0019
            java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch:{ all -> 0x0024 }
            java.io.OutputStream r0 = r2.output     // Catch:{ all -> 0x0024 }
            r3.<init>(r0)     // Catch:{ all -> 0x0024 }
            r2.writer = r3     // Catch:{ all -> 0x0024 }
            goto L_0x0022
        L_0x0019:
            java.io.OutputStreamWriter r0 = new java.io.OutputStreamWriter     // Catch:{ all -> 0x0024 }
            java.io.OutputStream r1 = r2.output     // Catch:{ all -> 0x0024 }
            r0.<init>((java.p026io.OutputStream) r1, (java.lang.String) r3)     // Catch:{ all -> 0x0024 }
            r2.writer = r0     // Catch:{ all -> 0x0024 }
        L_0x0022:
            monitor-exit(r2)
            return
        L_0x0024:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.StreamHandler.setEncoding(java.lang.String):void");
    }

    public synchronized void publish(LogRecord logRecord) {
        if (isLoggable(logRecord)) {
            try {
                String format = getFormatter().format(logRecord);
                try {
                    if (!this.doneHeader) {
                        this.writer.write(getFormatter().getHead(this));
                        this.doneHeader = true;
                    }
                    this.writer.write(format);
                } catch (Exception e) {
                    reportError((String) null, e, 1);
                }
            } catch (Exception e2) {
                reportError((String) null, e2, 5);
                return;
            }
        } else {
            return;
        }
        return;
    }

    public boolean isLoggable(LogRecord logRecord) {
        if (this.writer == null || logRecord == null) {
            return false;
        }
        return super.isLoggable(logRecord);
    }

    public synchronized void flush() {
        if (this.writer != null) {
            try {
                this.writer.flush();
            } catch (Exception e) {
                reportError((String) null, e, 2);
            }
        }
        return;
    }

    private synchronized void flushAndClose() throws SecurityException {
        checkPermission();
        if (this.writer != null) {
            try {
                if (!this.doneHeader) {
                    this.writer.write(getFormatter().getHead(this));
                    this.doneHeader = true;
                }
                this.writer.write(getFormatter().getTail(this));
                this.writer.flush();
                this.writer.close();
            } catch (Exception e) {
                reportError((String) null, e, 3);
            }
            this.writer = null;
            this.output = null;
        }
    }

    public synchronized void close() throws SecurityException {
        flushAndClose();
    }
}
