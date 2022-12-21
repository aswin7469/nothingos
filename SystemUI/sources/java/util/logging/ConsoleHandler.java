package java.util.logging;

public class ConsoleHandler extends StreamHandler {
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
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.ConsoleHandler.configure():void");
    }

    public ConsoleHandler() {
        this.sealed = false;
        configure();
        setOutputStream(System.err);
        this.sealed = true;
    }

    public void publish(LogRecord logRecord) {
        super.publish(logRecord);
        flush();
    }

    public void close() {
        flush();
    }
}
