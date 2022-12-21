package javax.crypto;

import java.net.URL;
import java.security.cert.Certificate;

final class JarVerifier {
    private CryptoPermissions appPerms = null;
    private URL jarURL;
    private boolean savePerms;

    static void verifyPolicySigned(Certificate[] certificateArr) throws Exception {
    }

    JarVerifier(URL url, boolean z) {
        this.jarURL = url;
        this.savePerms = z;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void verify() throws java.util.jar.JarException, java.p026io.IOException {
        /*
            r5 = this;
            java.lang.String r0 = "Cannot load/parse"
            java.lang.String r1 = "Cannot load "
            boolean r2 = r5.savePerms
            if (r2 != 0) goto L_0x0009
            return
        L_0x0009:
            java.net.URL r2 = r5.jarURL
            java.lang.String r2 = r2.getProtocol()
            java.lang.String r3 = "jar"
            boolean r2 = r2.equalsIgnoreCase(r3)
            if (r2 == 0) goto L_0x001a
            java.net.URL r2 = r5.jarURL
            goto L_0x0038
        L_0x001a:
            java.net.URL r2 = new java.net.URL
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "jar:"
            r3.<init>((java.lang.String) r4)
            java.net.URL r4 = r5.jarURL
            java.lang.String r4 = r4.toString()
            r3.append((java.lang.String) r4)
            java.lang.String r4 = "!/"
            r3.append((java.lang.String) r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
        L_0x0038:
            r3 = 0
            javax.crypto.JarVerifier$1 r4 = new javax.crypto.JarVerifier$1     // Catch:{ PrivilegedActionException -> 0x008c }
            r4.<init>(r2)     // Catch:{ PrivilegedActionException -> 0x008c }
            java.lang.Object r4 = java.security.AccessController.doPrivileged(r4)     // Catch:{ PrivilegedActionException -> 0x008c }
            java.util.jar.JarFile r4 = (java.util.jar.JarFile) r4     // Catch:{ PrivilegedActionException -> 0x008c }
            if (r4 == 0) goto L_0x0084
            java.lang.String r1 = "cryptoPerms"
            java.util.jar.JarEntry r1 = r4.getJarEntry(r1)     // Catch:{ all -> 0x0081 }
            if (r1 == 0) goto L_0x0079
            javax.crypto.CryptoPermissions r2 = new javax.crypto.CryptoPermissions     // Catch:{ Exception -> 0x005d }
            r2.<init>()     // Catch:{ Exception -> 0x005d }
            r5.appPerms = r2     // Catch:{ Exception -> 0x005d }
            java.io.InputStream r1 = r4.getInputStream(r1)     // Catch:{ Exception -> 0x005d }
            r2.load(r1)     // Catch:{ Exception -> 0x005d }
            goto L_0x0084
        L_0x005d:
            r1 = move-exception
            java.util.jar.JarException r2 = new java.util.jar.JarException     // Catch:{ all -> 0x0081 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0081 }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0081 }
            java.net.URL r5 = r5.jarURL     // Catch:{ all -> 0x0081 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0081 }
            r3.append((java.lang.String) r5)     // Catch:{ all -> 0x0081 }
            java.lang.String r5 = r3.toString()     // Catch:{ all -> 0x0081 }
            r2.<init>(r5)     // Catch:{ all -> 0x0081 }
            r2.initCause(r1)     // Catch:{ all -> 0x0081 }
            throw r2     // Catch:{ all -> 0x0081 }
        L_0x0079:
            java.util.jar.JarException r5 = new java.util.jar.JarException     // Catch:{ all -> 0x0081 }
            java.lang.String r0 = "Can not find cryptoPerms"
            r5.<init>(r0)     // Catch:{ all -> 0x0081 }
            throw r5     // Catch:{ all -> 0x0081 }
        L_0x0081:
            r5 = move-exception
            r3 = r4
            goto L_0x00a3
        L_0x0084:
            if (r4 == 0) goto L_0x0089
            r4.close()
        L_0x0089:
            return
        L_0x008a:
            r5 = move-exception
            goto L_0x00a3
        L_0x008c:
            r5 = move-exception
            java.lang.SecurityException r0 = new java.lang.SecurityException     // Catch:{ all -> 0x008a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x008a }
            r4.<init>((java.lang.String) r1)     // Catch:{ all -> 0x008a }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x008a }
            r4.append((java.lang.String) r1)     // Catch:{ all -> 0x008a }
            java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x008a }
            r0.<init>(r1, r5)     // Catch:{ all -> 0x008a }
            throw r0     // Catch:{ all -> 0x008a }
        L_0x00a3:
            if (r3 == 0) goto L_0x00a8
            r3.close()
        L_0x00a8:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.crypto.JarVerifier.verify():void");
    }

    /* access modifiers changed from: package-private */
    public CryptoPermissions getPermissions() {
        return this.appPerms;
    }
}
