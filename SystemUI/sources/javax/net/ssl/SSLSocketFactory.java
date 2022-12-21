package javax.net.ssl;

import java.net.Socket;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.Locale;
import javax.net.SocketFactory;
import sun.security.action.GetPropertyAction;

public abstract class SSLSocketFactory extends SocketFactory {
    static final boolean DEBUG;
    private static SSLSocketFactory defaultSocketFactory = null;
    private static int lastVersion = -1;

    public abstract Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException;

    public abstract String[] getDefaultCipherSuites();

    public abstract String[] getSupportedCipherSuites();

    static {
        String lowerCase = ((String) AccessController.doPrivileged(new GetPropertyAction("javax.net.debug", ""))).toLowerCase(Locale.ENGLISH);
        DEBUG = lowerCase.contains("all") || lowerCase.contains("ssl");
    }

    private static void log(String str) {
        if (DEBUG) {
            System.out.println(str);
        }
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
        log("SSLSocketFactory instantiation failed: " + r0.toString());
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x004c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized javax.net.SocketFactory getDefault() {
        /*
            java.lang.String r0 = "instantiated an instance of class "
            java.lang.String r1 = "class "
            java.lang.String r2 = "SSLSocketFactory instantiation failed: "
            java.lang.Class<javax.net.ssl.SSLSocketFactory> r3 = javax.net.ssl.SSLSocketFactory.class
            monitor-enter(r3)
            javax.net.ssl.SSLSocketFactory r4 = defaultSocketFactory     // Catch:{ all -> 0x00c8 }
            if (r4 == 0) goto L_0x0019
            int r4 = lastVersion     // Catch:{ all -> 0x00c8 }
            int r5 = java.security.Security.getVersion()     // Catch:{ all -> 0x00c8 }
            if (r4 != r5) goto L_0x0019
            javax.net.ssl.SSLSocketFactory r0 = defaultSocketFactory     // Catch:{ all -> 0x00c8 }
            monitor-exit(r3)
            return r0
        L_0x0019:
            int r4 = java.security.Security.getVersion()     // Catch:{ all -> 0x00c8 }
            lastVersion = r4     // Catch:{ all -> 0x00c8 }
            javax.net.ssl.SSLSocketFactory r4 = defaultSocketFactory     // Catch:{ all -> 0x00c8 }
            r5 = 0
            defaultSocketFactory = r5     // Catch:{ all -> 0x00c8 }
            java.lang.String r6 = "ssl.SocketFactory.provider"
            java.lang.String r6 = getSecurityProperty(r6)     // Catch:{ all -> 0x00c8 }
            if (r6 == 0) goto L_0x00a1
            if (r4 == 0) goto L_0x0040
            java.lang.Class r7 = r4.getClass()     // Catch:{ all -> 0x00c8 }
            java.lang.String r7 = r7.getName()     // Catch:{ all -> 0x00c8 }
            boolean r7 = r6.equals(r7)     // Catch:{ all -> 0x00c8 }
            if (r7 == 0) goto L_0x0040
            defaultSocketFactory = r4     // Catch:{ all -> 0x00c8 }
            monitor-exit(r3)
            return r4
        L_0x0040:
            java.lang.String r4 = "setting up default SSLSocketFactory"
            log(r4)     // Catch:{ all -> 0x00c8 }
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
            javax.net.ssl.SSLSocketFactory r1 = (javax.net.ssl.SSLSocketFactory) r1     // Catch:{ Exception -> 0x004a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x004a }
            r4.<init>((java.lang.String) r0)     // Catch:{ Exception -> 0x004a }
            r4.append((java.lang.String) r6)     // Catch:{ Exception -> 0x004a }
            java.lang.String r0 = r4.toString()     // Catch:{ Exception -> 0x004a }
            log(r0)     // Catch:{ Exception -> 0x004a }
            defaultSocketFactory = r1     // Catch:{ Exception -> 0x004a }
            monitor-exit(r3)
            return r1
        L_0x008e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c8 }
            r1.<init>((java.lang.String) r2)     // Catch:{ all -> 0x00c8 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00c8 }
            r1.append((java.lang.String) r0)     // Catch:{ all -> 0x00c8 }
            java.lang.String r0 = r1.toString()     // Catch:{ all -> 0x00c8 }
            log(r0)     // Catch:{ all -> 0x00c8 }
        L_0x00a1:
            javax.net.ssl.SSLContext r0 = javax.net.ssl.SSLContext.getDefault()     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
            if (r0 == 0) goto L_0x00ae
            javax.net.ssl.SSLSocketFactory r0 = r0.getSocketFactory()     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
            defaultSocketFactory = r0     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
            goto L_0x00bc
        L_0x00ae:
            javax.net.ssl.DefaultSSLSocketFactory r0 = new javax.net.ssl.DefaultSSLSocketFactory     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
            java.lang.String r2 = "No factory found."
            r1.<init>((java.lang.String) r2)     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
            r0.<init>(r1)     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
            defaultSocketFactory = r0     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
        L_0x00bc:
            javax.net.ssl.SSLSocketFactory r0 = defaultSocketFactory     // Catch:{ NoSuchAlgorithmException -> 0x00c0 }
            monitor-exit(r3)
            return r0
        L_0x00c0:
            r0 = move-exception
            javax.net.ssl.DefaultSSLSocketFactory r1 = new javax.net.ssl.DefaultSSLSocketFactory     // Catch:{ all -> 0x00c8 }
            r1.<init>(r0)     // Catch:{ all -> 0x00c8 }
            monitor-exit(r3)
            return r1
        L_0x00c8:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.net.ssl.SSLSocketFactory.getDefault():javax.net.SocketFactory");
    }

    static String getSecurityProperty(final String str) {
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                String property = Security.getProperty(String.this);
                if (property == null) {
                    return property;
                }
                String trim = property.trim();
                if (trim.length() == 0) {
                    return null;
                }
                return trim;
            }
        });
    }

    public Socket createSocket(Socket socket, InputStream inputStream, boolean z) throws IOException {
        throw new UnsupportedOperationException();
    }
}
