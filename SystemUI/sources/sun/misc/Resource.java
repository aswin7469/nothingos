package sun.misc;

import java.net.URL;
import java.nio.ByteBuffer;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.jar.Manifest;
import sun.nio.ByteBuffered;

public abstract class Resource {
    private InputStream cis;

    public Certificate[] getCertificates() {
        return null;
    }

    public CodeSigner[] getCodeSigners() {
        return null;
    }

    public abstract URL getCodeSourceURL();

    public abstract int getContentLength() throws IOException;

    public abstract InputStream getInputStream() throws IOException;

    public Manifest getManifest() throws IOException {
        return null;
    }

    public abstract String getName();

    public abstract URL getURL();

    private synchronized InputStream cachedInputStream() throws IOException {
        if (this.cis == null) {
            this.cis = getInputStream();
        }
        return this.cis;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        java.lang.Thread.interrupted();
        r1 = r2;
        r7 = 0;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0037 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getBytes() throws java.p026io.IOException {
        /*
            r10 = this;
            java.io.InputStream r0 = r10.cachedInputStream()
            boolean r1 = java.lang.Thread.interrupted()
            r2 = 1
        L_0x0009:
            int r10 = r10.getContentLength()     // Catch:{ InterruptedIOException -> 0x006f }
            r3 = 0
            byte[] r4 = new byte[r3]     // Catch:{ all -> 0x0060 }
            r5 = -1
            r6 = 2147483647(0x7fffffff, float:NaN)
            if (r10 != r5) goto L_0x0017
            r10 = r6
        L_0x0017:
            r5 = r3
        L_0x0018:
            if (r5 >= r10) goto L_0x0052
            int r7 = r4.length     // Catch:{ all -> 0x0060 }
            if (r5 < r7) goto L_0x0030
            int r7 = r10 - r5
            int r8 = r4.length     // Catch:{ all -> 0x0060 }
            int r8 = r8 + 1024
            int r7 = java.lang.Math.min((int) r7, (int) r8)     // Catch:{ all -> 0x0060 }
            int r8 = r4.length     // Catch:{ all -> 0x0060 }
            int r9 = r5 + r7
            if (r8 >= r9) goto L_0x0032
            byte[] r4 = java.util.Arrays.copyOf((byte[]) r4, (int) r9)     // Catch:{ all -> 0x0060 }
            goto L_0x0032
        L_0x0030:
            int r7 = r4.length     // Catch:{ all -> 0x0060 }
            int r7 = r7 - r5
        L_0x0032:
            int r7 = r0.read(r4, r5, r7)     // Catch:{ InterruptedIOException -> 0x0037 }
            goto L_0x003c
        L_0x0037:
            java.lang.Thread.interrupted()     // Catch:{ all -> 0x0060 }
            r1 = r2
            r7 = r3
        L_0x003c:
            if (r7 >= 0) goto L_0x0050
            if (r10 != r6) goto L_0x0048
            int r10 = r4.length     // Catch:{ all -> 0x0060 }
            if (r10 == r5) goto L_0x0052
            byte[] r4 = java.util.Arrays.copyOf((byte[]) r4, (int) r5)     // Catch:{ all -> 0x0060 }
            goto L_0x0052
        L_0x0048:
            java.io.EOFException r10 = new java.io.EOFException     // Catch:{ all -> 0x0060 }
            java.lang.String r3 = "Detect premature EOF"
            r10.<init>(r3)     // Catch:{ all -> 0x0060 }
            throw r10     // Catch:{ all -> 0x0060 }
        L_0x0050:
            int r5 = r5 + r7
            goto L_0x0018
        L_0x0052:
            r0.close()     // Catch:{ InterruptedIOException -> 0x0056, IOException -> 0x0055 }
        L_0x0055:
            r2 = r1
        L_0x0056:
            if (r2 == 0) goto L_0x005f
            java.lang.Thread r10 = java.lang.Thread.currentThread()
            r10.interrupt()
        L_0x005f:
            return r4
        L_0x0060:
            r10 = move-exception
            r0.close()     // Catch:{ InterruptedIOException -> 0x0065, IOException -> 0x0064 }
        L_0x0064:
            r2 = r1
        L_0x0065:
            if (r2 == 0) goto L_0x006e
            java.lang.Thread r0 = java.lang.Thread.currentThread()
            r0.interrupt()
        L_0x006e:
            throw r10
        L_0x006f:
            java.lang.Thread.interrupted()
            r1 = r2
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.Resource.getBytes():byte[]");
    }

    public ByteBuffer getByteBuffer() throws IOException {
        InputStream cachedInputStream = cachedInputStream();
        if (cachedInputStream instanceof ByteBuffered) {
            return ((ByteBuffered) cachedInputStream).getByteBuffer();
        }
        return null;
    }
}
