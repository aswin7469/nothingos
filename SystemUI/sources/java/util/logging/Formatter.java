package java.util.logging;

public abstract class Formatter {
    public abstract String format(LogRecord logRecord);

    public String getHead(Handler handler) {
        return "";
    }

    public String getTail(Handler handler) {
        return "";
    }

    protected Formatter() {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:6|7) */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004c, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        r0 = r3.getMessage();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0014 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String formatMessage(java.util.logging.LogRecord r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            java.lang.String r0 = r3.getMessage()     // Catch:{ all -> 0x004f }
            java.util.ResourceBundle r1 = r3.getResourceBundle()     // Catch:{ all -> 0x004f }
            if (r1 == 0) goto L_0x0018
            java.lang.String r0 = r3.getMessage()     // Catch:{ MissingResourceException -> 0x0014 }
            java.lang.String r0 = r1.getString(r0)     // Catch:{ MissingResourceException -> 0x0014 }
            goto L_0x0018
        L_0x0014:
            java.lang.String r0 = r3.getMessage()     // Catch:{ all -> 0x004f }
        L_0x0018:
            java.lang.Object[] r3 = r3.getParameters()     // Catch:{ Exception -> 0x004d }
            if (r3 == 0) goto L_0x004b
            int r1 = r3.length     // Catch:{ Exception -> 0x004d }
            if (r1 != 0) goto L_0x0022
            goto L_0x004b
        L_0x0022:
            java.lang.String r1 = "{0"
            int r1 = r0.indexOf((java.lang.String) r1)     // Catch:{ Exception -> 0x004d }
            if (r1 >= 0) goto L_0x0045
            java.lang.String r1 = "{1"
            int r1 = r0.indexOf((java.lang.String) r1)     // Catch:{ Exception -> 0x004d }
            if (r1 >= 0) goto L_0x0045
            java.lang.String r1 = "{2"
            int r1 = r0.indexOf((java.lang.String) r1)     // Catch:{ Exception -> 0x004d }
            if (r1 >= 0) goto L_0x0045
            java.lang.String r1 = "{3"
            int r1 = r0.indexOf((java.lang.String) r1)     // Catch:{ Exception -> 0x004d }
            if (r1 < 0) goto L_0x0043
            goto L_0x0045
        L_0x0043:
            monitor-exit(r2)
            return r0
        L_0x0045:
            java.lang.String r3 = java.text.MessageFormat.format(r0, r3)     // Catch:{ Exception -> 0x004d }
            monitor-exit(r2)
            return r3
        L_0x004b:
            monitor-exit(r2)
            return r0
        L_0x004d:
            monitor-exit(r2)
            return r0
        L_0x004f:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.Formatter.formatMessage(java.util.logging.LogRecord):java.lang.String");
    }
}
