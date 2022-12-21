package javax.net.ssl;

import javax.net.ServerSocketFactory;

public abstract class SSLServerSocketFactory extends ServerSocketFactory {
    private static SSLServerSocketFactory defaultServerSocketFactory = null;
    private static int lastVersion = -1;

    public abstract String[] getDefaultCipherSuites();

    public abstract String[] getSupportedCipherSuites();

    private static void log(String str) {
        if (SSLSocketFactory.DEBUG) {
            System.out.println(str);
        }
    }

    protected SSLServerSocketFactory() {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:25|26|(1:28)|(1:30)) */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r4 = java.lang.Thread.currentThread().getContextClassLoader();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0054, code lost:
        if (r4 == null) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0056, code lost:
        r4 = java.lang.ClassLoader.getSystemClassLoader();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005a, code lost:
        if (r4 != null) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x005c, code lost:
        r5 = java.lang.Class.forName(r6, true, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        log("SSLServerSocketFactory instantiation failed: " + r0);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x004c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized javax.net.ServerSocketFactory getDefault() {
        /*
            java.lang.String r0 = "instantiated an instance of class "
            java.lang.String r1 = "class "
            java.lang.String r2 = "SSLServerSocketFactory instantiation failed: "
            java.lang.Class<javax.net.ssl.SSLServerSocketFactory> r3 = javax.net.ssl.SSLServerSocketFactory.class
            monitor-enter(r3)
            javax.net.ssl.SSLServerSocketFactory r4 = defaultServerSocketFactory     // Catch:{ all -> 0x00c4 }
            if (r4 == 0) goto L_0x0019
            int r4 = lastVersion     // Catch:{ all -> 0x00c4 }
            int r5 = java.security.Security.getVersion()     // Catch:{ all -> 0x00c4 }
            if (r4 != r5) goto L_0x0019
            javax.net.ssl.SSLServerSocketFactory r0 = defaultServerSocketFactory     // Catch:{ all -> 0x00c4 }
            monitor-exit(r3)
            return r0
        L_0x0019:
            int r4 = java.security.Security.getVersion()     // Catch:{ all -> 0x00c4 }
            lastVersion = r4     // Catch:{ all -> 0x00c4 }
            javax.net.ssl.SSLServerSocketFactory r4 = defaultServerSocketFactory     // Catch:{ all -> 0x00c4 }
            r5 = 0
            defaultServerSocketFactory = r5     // Catch:{ all -> 0x00c4 }
            java.lang.String r6 = "ssl.ServerSocketFactory.provider"
            java.lang.String r6 = javax.net.ssl.SSLSocketFactory.getSecurityProperty(r6)     // Catch:{ all -> 0x00c4 }
            if (r6 == 0) goto L_0x009d
            if (r4 == 0) goto L_0x0040
            java.lang.Class r7 = r4.getClass()     // Catch:{ all -> 0x00c4 }
            java.lang.String r7 = r7.getName()     // Catch:{ all -> 0x00c4 }
            boolean r7 = r6.equals(r7)     // Catch:{ all -> 0x00c4 }
            if (r7 == 0) goto L_0x0040
            defaultServerSocketFactory = r4     // Catch:{ all -> 0x00c4 }
            monitor-exit(r3)
            return r4
        L_0x0040:
            java.lang.String r4 = "setting up default SSLServerSocketFactory"
            log(r4)     // Catch:{ all -> 0x00c4 }
            java.lang.Class r5 = java.lang.Class.forName(r6)     // Catch:{ ClassNotFoundException -> 0x004c }
            goto L_0x0061
        L_0x004a:
            r0 = move-exception
            goto L_0x008e
        L_0x004c:
            java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x004a }
            java.lang.ClassLoader r4 = r4.getContextClassLoader()     // Catch:{ Exception -> 0x004a }
            if (r4 != 0) goto L_0x005a
            java.lang.ClassLoader r4 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ Exception -> 0x004a }
        L_0x005a:
            if (r4 == 0) goto L_0x0061
            r5 = 1
            java.lang.Class r5 = java.lang.Class.forName(r6, r5, r4)     // Catch:{ Exception -> 0x004a }
        L_0x0061:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x004a }
            r4.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x004a }
            r4.append((java.lang.String) r6)     // Catch:{ Exception -> 0x004a }
            java.lang.String r1 = " is loaded"
            r4.append((java.lang.String) r1)     // Catch:{ Exception -> 0x004a }
            java.lang.String r1 = r4.toString()     // Catch:{ Exception -> 0x004a }
            log(r1)     // Catch:{ Exception -> 0x004a }
            java.lang.Object r1 = r5.newInstance()     // Catch:{ Exception -> 0x004a }
            javax.net.ssl.SSLServerSocketFactory r1 = (javax.net.ssl.SSLServerSocketFactory) r1     // Catch:{ Exception -> 0x004a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x004a }
            r4.<init>((java.lang.String) r0)     // Catch:{ Exception -> 0x004a }
            r4.append((java.lang.String) r6)     // Catch:{ Exception -> 0x004a }
            java.lang.String r0 = r4.toString()     // Catch:{ Exception -> 0x004a }
            log(r0)     // Catch:{ Exception -> 0x004a }
            defaultServerSocketFactory = r1     // Catch:{ Exception -> 0x004a }
            monitor-exit(r3)
            return r1
        L_0x008e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c4 }
            r1.<init>((java.lang.String) r2)     // Catch:{ all -> 0x00c4 }
            r1.append((java.lang.Object) r0)     // Catch:{ all -> 0x00c4 }
            java.lang.String r0 = r1.toString()     // Catch:{ all -> 0x00c4 }
            log(r0)     // Catch:{ all -> 0x00c4 }
        L_0x009d:
            javax.net.ssl.SSLContext r0 = javax.net.ssl.SSLContext.getDefault()     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
            if (r0 == 0) goto L_0x00aa
            javax.net.ssl.SSLServerSocketFactory r0 = r0.getServerSocketFactory()     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
            defaultServerSocketFactory = r0     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
            goto L_0x00b8
        L_0x00aa:
            javax.net.ssl.DefaultSSLServerSocketFactory r0 = new javax.net.ssl.DefaultSSLServerSocketFactory     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
            java.lang.String r2 = "No factory found."
            r1.<init>((java.lang.String) r2)     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
            r0.<init>(r1)     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
            defaultServerSocketFactory = r0     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
        L_0x00b8:
            javax.net.ssl.SSLServerSocketFactory r0 = defaultServerSocketFactory     // Catch:{ NoSuchAlgorithmException -> 0x00bc }
            monitor-exit(r3)
            return r0
        L_0x00bc:
            r0 = move-exception
            javax.net.ssl.DefaultSSLServerSocketFactory r1 = new javax.net.ssl.DefaultSSLServerSocketFactory     // Catch:{ all -> 0x00c4 }
            r1.<init>(r0)     // Catch:{ all -> 0x00c4 }
            monitor-exit(r3)
            return r1
        L_0x00c4:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.net.ssl.SSLServerSocketFactory.getDefault():javax.net.ServerSocketFactory");
    }
}
