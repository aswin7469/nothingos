package java.util.logging;

public class ErrorManager {
    public static final int CLOSE_FAILURE = 3;
    public static final int FLUSH_FAILURE = 2;
    public static final int FORMAT_FAILURE = 5;
    public static final int GENERIC_FAILURE = 0;
    public static final int OPEN_FAILURE = 4;
    public static final int WRITE_FAILURE = 1;
    private boolean reported = false;

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0039, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void error(java.lang.String r3, java.lang.Exception r4, int r5) {
        /*
            r2 = this;
            java.lang.String r0 = "java.util.logging.ErrorManager: "
            monitor-enter(r2)
            boolean r1 = r2.reported     // Catch:{ all -> 0x003a }
            if (r1 == 0) goto L_0x0009
            monitor-exit(r2)
            return
        L_0x0009:
            r1 = 1
            r2.reported = r1     // Catch:{ all -> 0x003a }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x003a }
            r1.<init>((java.lang.String) r0)     // Catch:{ all -> 0x003a }
            r1.append((int) r5)     // Catch:{ all -> 0x003a }
            java.lang.String r5 = r1.toString()     // Catch:{ all -> 0x003a }
            if (r3 == 0) goto L_0x002e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x003a }
            r0.<init>()     // Catch:{ all -> 0x003a }
            r0.append((java.lang.String) r5)     // Catch:{ all -> 0x003a }
            java.lang.String r5 = ": "
            r0.append((java.lang.String) r5)     // Catch:{ all -> 0x003a }
            r0.append((java.lang.String) r3)     // Catch:{ all -> 0x003a }
            java.lang.String r5 = r0.toString()     // Catch:{ all -> 0x003a }
        L_0x002e:
            java.io.PrintStream r3 = java.lang.System.err     // Catch:{ all -> 0x003a }
            r3.println((java.lang.String) r5)     // Catch:{ all -> 0x003a }
            if (r4 == 0) goto L_0x0038
            r4.printStackTrace()     // Catch:{ all -> 0x003a }
        L_0x0038:
            monitor-exit(r2)
            return
        L_0x003a:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.ErrorManager.error(java.lang.String, java.lang.Exception, int):void");
    }
}
