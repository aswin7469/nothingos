package sun.nio.p035fs;

import java.nio.ByteBuffer;
import java.nio.file.FileSystemException;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.List;
import sun.misc.Unsafe;
import sun.nio.p033ch.DirectBuffer;

/* renamed from: sun.nio.fs.LinuxUserDefinedFileAttributeView */
class LinuxUserDefinedFileAttributeView extends AbstractUserDefinedFileAttributeView {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String USER_NAMESPACE = "user.";
    private static final int XATTR_NAME_MAX = 255;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private final UnixPath file;
    private final boolean followLinks;

    private byte[] nameAsBytes(UnixPath unixPath, String str) throws IOException {
        if (str != null) {
            String str2 = USER_NAMESPACE + str;
            byte[] bytes = Util.toBytes(str2);
            if (bytes.length <= 255) {
                return bytes;
            }
            throw new FileSystemException(unixPath.getPathForExceptionMessage(), (String) null, "'" + str2 + "' is too big");
        }
        throw new NullPointerException("'name' is null");
    }

    private List<String> asList(long j, int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            if (unsafe.getByte(((long) i3) + j) == 0) {
                int i4 = i3 - i2;
                byte[] bArr = new byte[i4];
                for (int i5 = 0; i5 < i4; i5++) {
                    bArr[i5] = unsafe.getByte(((long) i2) + j + ((long) i5));
                }
                String util = Util.toString(bArr);
                if (util.startsWith(USER_NAMESPACE)) {
                    arrayList.add(util.substring(5));
                }
                i2 = i3 + 1;
            }
        }
        return arrayList;
    }

    LinuxUserDefinedFileAttributeView(UnixPath unixPath, boolean z) {
        this.file = unixPath;
        this.followLinks = z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x007c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.String> list() throws java.p026io.IOException {
        /*
            r7 = this;
            java.lang.SecurityManager r0 = java.lang.System.getSecurityManager()
            if (r0 == 0) goto L_0x0011
            sun.nio.fs.UnixPath r0 = r7.file
            java.lang.String r0 = r0.getPathForPermissionCheck()
            r1 = 1
            r2 = 0
            r7.checkAccess(r0, r1, r2)
        L_0x0011:
            sun.nio.fs.UnixPath r0 = r7.file
            boolean r1 = r7.followLinks
            int r0 = r0.openForAttributeAccess(r1)
            r1 = 0
            r2 = 1024(0x400, float:1.435E-42)
            sun.nio.fs.NativeBuffer r3 = sun.nio.p035fs.NativeBuffers.getNativeBuffer(r2)     // Catch:{ all -> 0x0079 }
        L_0x0020:
            long r4 = r3.address()     // Catch:{ UnixException -> 0x0040 }
            int r4 = sun.nio.p035fs.LinuxNativeDispatcher.flistxattr(r0, r4, r2)     // Catch:{ UnixException -> 0x0040 }
            long r5 = r3.address()     // Catch:{ UnixException -> 0x0040 }
            java.util.List r4 = r7.asList(r5, r4)     // Catch:{ UnixException -> 0x0040 }
            java.util.List r7 = java.util.Collections.unmodifiableList(r4)     // Catch:{ UnixException -> 0x0040 }
            if (r3 == 0) goto L_0x0039
            r3.release()
        L_0x0039:
            sun.nio.p035fs.LinuxNativeDispatcher.close(r0)
            return r7
        L_0x003d:
            r7 = move-exception
            r1 = r3
            goto L_0x007a
        L_0x0040:
            r4 = move-exception
            int r5 = r4.errno()     // Catch:{ all -> 0x003d }
            int r6 = sun.nio.p035fs.UnixConstants.ERANGE     // Catch:{ all -> 0x003d }
            if (r5 != r6) goto L_0x0058
            r5 = 32768(0x8000, float:4.5918E-41)
            if (r2 >= r5) goto L_0x0058
            r3.release()     // Catch:{ all -> 0x003d }
            int r2 = r2 * 2
            sun.nio.fs.NativeBuffer r3 = sun.nio.p035fs.NativeBuffers.getNativeBuffer(r2)     // Catch:{ all -> 0x0079 }
            goto L_0x0020
        L_0x0058:
            java.nio.file.FileSystemException r2 = new java.nio.file.FileSystemException     // Catch:{ all -> 0x003d }
            sun.nio.fs.UnixPath r7 = r7.file     // Catch:{ all -> 0x003d }
            java.lang.String r7 = r7.getPathForExceptionMessage()     // Catch:{ all -> 0x003d }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x003d }
            r5.<init>()     // Catch:{ all -> 0x003d }
            java.lang.String r6 = "Unable to get list of extended attributes: "
            r5.append((java.lang.String) r6)     // Catch:{ all -> 0x003d }
            java.lang.String r4 = r4.getMessage()     // Catch:{ all -> 0x003d }
            r5.append((java.lang.String) r4)     // Catch:{ all -> 0x003d }
            java.lang.String r4 = r5.toString()     // Catch:{ all -> 0x003d }
            r2.<init>(r7, r1, r4)     // Catch:{ all -> 0x003d }
            throw r2     // Catch:{ all -> 0x003d }
        L_0x0079:
            r7 = move-exception
        L_0x007a:
            if (r1 == 0) goto L_0x007f
            r1.release()
        L_0x007f:
            sun.nio.p035fs.LinuxNativeDispatcher.close(r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.LinuxUserDefinedFileAttributeView.list():java.util.List");
    }

    public int size(String str) throws IOException {
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), true, false);
        }
        int openForAttributeAccess = this.file.openForAttributeAccess(this.followLinks);
        try {
            int fgetxattr = LinuxNativeDispatcher.fgetxattr(openForAttributeAccess, nameAsBytes(this.file, str), 0, 0);
            LinuxNativeDispatcher.close(openForAttributeAccess);
            return fgetxattr;
        } catch (UnixException e) {
            String pathForExceptionMessage = this.file.getPathForExceptionMessage();
            throw new FileSystemException(pathForExceptionMessage, (String) null, "Unable to get size of extended attribute '" + str + "': " + e.getMessage());
        } catch (Throwable th) {
            LinuxNativeDispatcher.close(openForAttributeAccess);
            throw th;
        }
    }

    public int read(String str, ByteBuffer byteBuffer) throws IOException {
        long j;
        NativeBuffer nativeBuffer;
        String str2;
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), true, false);
        }
        if (!byteBuffer.isReadOnly()) {
            int position = byteBuffer.position();
            int limit = byteBuffer.limit();
            int i = position <= limit ? limit - position : 0;
            if (byteBuffer instanceof DirectBuffer) {
                j = ((DirectBuffer) byteBuffer).address() + ((long) position);
                nativeBuffer = null;
            } else {
                nativeBuffer = NativeBuffers.getNativeBuffer(i);
                j = nativeBuffer.address();
            }
            int openForAttributeAccess = this.file.openForAttributeAccess(this.followLinks);
            try {
                int fgetxattr = LinuxNativeDispatcher.fgetxattr(openForAttributeAccess, nameAsBytes(this.file, str), j, i);
                if (i != 0) {
                    if (nativeBuffer != null) {
                        for (int i2 = 0; i2 < fgetxattr; i2++) {
                            byteBuffer.put(unsafe.getByte(((long) i2) + j));
                        }
                    }
                    byteBuffer.position(position + fgetxattr);
                    LinuxNativeDispatcher.close(openForAttributeAccess);
                    if (nativeBuffer != null) {
                        nativeBuffer.release();
                    }
                    return fgetxattr;
                } else if (fgetxattr <= 0) {
                    LinuxNativeDispatcher.close(openForAttributeAccess);
                    if (nativeBuffer != null) {
                        nativeBuffer.release();
                    }
                    return 0;
                } else {
                    throw new UnixException(UnixConstants.ERANGE);
                }
            } catch (UnixException e) {
                if (e.errno() == UnixConstants.ERANGE) {
                    str2 = "Insufficient space in buffer";
                } else {
                    str2 = e.getMessage();
                }
                throw new FileSystemException(this.file.getPathForExceptionMessage(), (String) null, "Error reading extended attribute '" + str + "': " + str2);
            } catch (Throwable th) {
                if (nativeBuffer != null) {
                    nativeBuffer.release();
                }
                throw th;
            }
        } else {
            throw new IllegalArgumentException("Read-only buffer");
        }
    }

    public int write(String str, ByteBuffer byteBuffer) throws IOException {
        long j;
        NativeBuffer nativeBuffer;
        int i = 0;
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), false, true);
        }
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i2 = position <= limit ? limit - position : 0;
        if (byteBuffer instanceof DirectBuffer) {
            j = ((DirectBuffer) byteBuffer).address() + ((long) position);
            nativeBuffer = null;
        } else {
            nativeBuffer = NativeBuffers.getNativeBuffer(i2);
            j = nativeBuffer.address();
            if (byteBuffer.hasArray()) {
                while (i < i2) {
                    unsafe.putByte(((long) i) + j, byteBuffer.get());
                    i++;
                }
            } else {
                byte[] bArr = new byte[i2];
                byteBuffer.get(bArr);
                byteBuffer.position(position);
                while (i < i2) {
                    unsafe.putByte(((long) i) + j, bArr[i]);
                    i++;
                }
            }
        }
        int openForAttributeAccess = this.file.openForAttributeAccess(this.followLinks);
        try {
            LinuxNativeDispatcher.fsetxattr(openForAttributeAccess, nameAsBytes(this.file, str), j, i2);
            byteBuffer.position(position + i2);
            LinuxNativeDispatcher.close(openForAttributeAccess);
            if (nativeBuffer != null) {
                nativeBuffer.release();
            }
            return i2;
        } catch (UnixException e) {
            throw new FileSystemException(this.file.getPathForExceptionMessage(), (String) null, "Error writing extended attribute '" + str + "': " + e.getMessage());
        } catch (Throwable th) {
            if (nativeBuffer != null) {
                nativeBuffer.release();
            }
            throw th;
        }
    }

    public void delete(String str) throws IOException {
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), false, true);
        }
        int openForAttributeAccess = this.file.openForAttributeAccess(this.followLinks);
        try {
            LinuxNativeDispatcher.fremovexattr(openForAttributeAccess, nameAsBytes(this.file, str));
            LinuxNativeDispatcher.close(openForAttributeAccess);
        } catch (UnixException e) {
            String pathForExceptionMessage = this.file.getPathForExceptionMessage();
            throw new FileSystemException(pathForExceptionMessage, (String) null, "Unable to delete extended attribute '" + str + "': " + e.getMessage());
        } catch (Throwable th) {
            LinuxNativeDispatcher.close(openForAttributeAccess);
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0024, code lost:
        if (unsafe.getByte(((long) r6) + r2) != 0) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0026, code lost:
        r8 = r6 - r7;
        r9 = new byte[r8];
        r10 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r10 >= r8) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002d, code lost:
        r9[r10] = unsafe.getByte((((long) r7) + r2) + ((long) r10));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0039, code lost:
        r10 = r10 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        copyExtendedAttribute(r1, r9, r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0044, code lost:
        r8 = r17;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0049, code lost:
        if (r4 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004b, code lost:
        r4.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        r2 = r4.address();
        r6 = 0;
        r7 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001a, code lost:
        if (r6 >= r0) goto L_0x0049;
     */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void copyExtendedAttributes(int r16, int r17) {
        /*
            r1 = r16
            r2 = 0
            r0 = 1024(0x400, float:1.435E-42)
            sun.nio.fs.NativeBuffer r3 = sun.nio.p035fs.NativeBuffers.getNativeBuffer(r0)     // Catch:{ all -> 0x0072 }
            r4 = r3
            r3 = r0
        L_0x000b:
            long r5 = r4.address()     // Catch:{ UnixException -> 0x0052 }
            int r0 = sun.nio.p035fs.LinuxNativeDispatcher.flistxattr(r1, r5, r3)     // Catch:{ UnixException -> 0x0052 }
            long r2 = r4.address()     // Catch:{ all -> 0x004f }
            r5 = 0
            r6 = r5
            r7 = r6
        L_0x001a:
            if (r6 >= r0) goto L_0x0049
            sun.misc.Unsafe r8 = unsafe     // Catch:{ all -> 0x004f }
            long r9 = (long) r6     // Catch:{ all -> 0x004f }
            long r9 = r9 + r2
            byte r8 = r8.getByte(r9)     // Catch:{ all -> 0x004f }
            if (r8 != 0) goto L_0x0044
            int r8 = r6 - r7
            byte[] r9 = new byte[r8]     // Catch:{ all -> 0x004f }
            r10 = r5
        L_0x002b:
            if (r10 >= r8) goto L_0x003c
            sun.misc.Unsafe r11 = unsafe     // Catch:{ all -> 0x004f }
            long r12 = (long) r7     // Catch:{ all -> 0x004f }
            long r12 = r12 + r2
            long r14 = (long) r10     // Catch:{ all -> 0x004f }
            long r12 = r12 + r14
            byte r11 = r11.getByte(r12)     // Catch:{ all -> 0x004f }
            r9[r10] = r11     // Catch:{ all -> 0x004f }
            int r10 = r10 + 1
            goto L_0x002b
        L_0x003c:
            r8 = r17
            copyExtendedAttribute(r1, r9, r8)     // Catch:{ UnixException -> 0x0041 }
        L_0x0041:
            int r7 = r6 + 1
            goto L_0x0046
        L_0x0044:
            r8 = r17
        L_0x0046:
            int r6 = r6 + 1
            goto L_0x001a
        L_0x0049:
            if (r4 == 0) goto L_0x004e
            r4.release()
        L_0x004e:
            return
        L_0x004f:
            r0 = move-exception
            r2 = r4
            goto L_0x0073
        L_0x0052:
            r0 = move-exception
            r8 = r17
            int r0 = r0.errno()     // Catch:{ all -> 0x004f }
            int r5 = sun.nio.p035fs.UnixConstants.ERANGE     // Catch:{ all -> 0x004f }
            if (r0 != r5) goto L_0x006c
            r0 = 32768(0x8000, float:4.5918E-41)
            if (r3 >= r0) goto L_0x006c
            r4.release()     // Catch:{ all -> 0x004f }
            int r3 = r3 * 2
            sun.nio.fs.NativeBuffer r4 = sun.nio.p035fs.NativeBuffers.getNativeBuffer(r3)     // Catch:{ all -> 0x0072 }
            goto L_0x000b
        L_0x006c:
            if (r4 == 0) goto L_0x0071
            r4.release()
        L_0x0071:
            return
        L_0x0072:
            r0 = move-exception
        L_0x0073:
            if (r2 == 0) goto L_0x0078
            r2.release()
        L_0x0078:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.LinuxUserDefinedFileAttributeView.copyExtendedAttributes(int, int):void");
    }

    private static void copyExtendedAttribute(int i, byte[] bArr, int i2) throws UnixException {
        int fgetxattr = LinuxNativeDispatcher.fgetxattr(i, bArr, 0, 0);
        NativeBuffer nativeBuffer = NativeBuffers.getNativeBuffer(fgetxattr);
        try {
            long address = nativeBuffer.address();
            LinuxNativeDispatcher.fsetxattr(i2, bArr, address, LinuxNativeDispatcher.fgetxattr(i, bArr, address, fgetxattr));
        } finally {
            nativeBuffer.release();
        }
    }
}
