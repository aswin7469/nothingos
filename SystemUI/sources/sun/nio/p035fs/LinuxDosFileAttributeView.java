package sun.nio.p035fs;

import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.p026io.IOException;
import java.util.Map;
import java.util.Set;
import sun.misc.Unsafe;
import sun.nio.p035fs.AbstractBasicFileAttributeView;
import sun.nio.p035fs.UnixFileAttributeViews;

/* renamed from: sun.nio.fs.LinuxDosFileAttributeView */
class LinuxDosFileAttributeView extends UnixFileAttributeViews.Basic implements DosFileAttributeView {
    private static final String ARCHIVE_NAME = "archive";
    private static final int DOS_XATTR_ARCHIVE = 32;
    private static final int DOS_XATTR_HIDDEN = 2;
    private static final String DOS_XATTR_NAME = "user.DOSATTRIB";
    private static final byte[] DOS_XATTR_NAME_AS_BYTES = Util.toBytes(DOS_XATTR_NAME);
    private static final int DOS_XATTR_READONLY = 1;
    private static final int DOS_XATTR_SYSTEM = 4;
    private static final String HIDDEN_NAME = "hidden";
    private static final String READONLY_NAME = "readonly";
    private static final String SYSTEM_NAME = "system";
    private static final Set<String> dosAttributeNames = Util.newSet(basicAttributeNames, READONLY_NAME, ARCHIVE_NAME, SYSTEM_NAME, HIDDEN_NAME);
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    public String name() {
        return "dos";
    }

    LinuxDosFileAttributeView(UnixPath unixPath, boolean z) {
        super(unixPath, z);
    }

    public void setAttribute(String str, Object obj) throws IOException {
        if (str.equals(READONLY_NAME)) {
            setReadOnly(((Boolean) obj).booleanValue());
        } else if (str.equals(ARCHIVE_NAME)) {
            setArchive(((Boolean) obj).booleanValue());
        } else if (str.equals(SYSTEM_NAME)) {
            setSystem(((Boolean) obj).booleanValue());
        } else if (str.equals(HIDDEN_NAME)) {
            setHidden(((Boolean) obj).booleanValue());
        } else {
            super.setAttribute(str, obj);
        }
    }

    public Map<String, Object> readAttributes(String[] strArr) throws IOException {
        AbstractBasicFileAttributeView.AttributesBuilder create = AbstractBasicFileAttributeView.AttributesBuilder.create(dosAttributeNames, strArr);
        DosFileAttributes readAttributes = readAttributes();
        addRequestedBasicAttributes(readAttributes, create);
        if (create.match(READONLY_NAME)) {
            create.add(READONLY_NAME, Boolean.valueOf(readAttributes.isReadOnly()));
        }
        if (create.match(ARCHIVE_NAME)) {
            create.add(ARCHIVE_NAME, Boolean.valueOf(readAttributes.isArchive()));
        }
        if (create.match(SYSTEM_NAME)) {
            create.add(SYSTEM_NAME, Boolean.valueOf(readAttributes.isSystem()));
        }
        if (create.match(HIDDEN_NAME)) {
            create.add(HIDDEN_NAME, Boolean.valueOf(readAttributes.isHidden()));
        }
        return create.unmodifiableMap();
    }

    /* JADX INFO: finally extract failed */
    public DosFileAttributes readAttributes() throws IOException {
        this.file.checkRead();
        int openForAttributeAccess = this.file.openForAttributeAccess(this.followLinks);
        try {
            final UnixFileAttributes unixFileAttributes = UnixFileAttributes.get(openForAttributeAccess);
            final int dosAttribute = getDosAttribute(openForAttributeAccess);
            C47971 r3 = new DosFileAttributes() {
                public FileTime lastModifiedTime() {
                    return unixFileAttributes.lastModifiedTime();
                }

                public FileTime lastAccessTime() {
                    return unixFileAttributes.lastAccessTime();
                }

                public FileTime creationTime() {
                    return unixFileAttributes.creationTime();
                }

                public boolean isRegularFile() {
                    return unixFileAttributes.isRegularFile();
                }

                public boolean isDirectory() {
                    return unixFileAttributes.isDirectory();
                }

                public boolean isSymbolicLink() {
                    return unixFileAttributes.isSymbolicLink();
                }

                public boolean isOther() {
                    return unixFileAttributes.isOther();
                }

                public long size() {
                    return unixFileAttributes.size();
                }

                public Object fileKey() {
                    return unixFileAttributes.fileKey();
                }

                public boolean isReadOnly() {
                    return (dosAttribute & 1) != 0;
                }

                public boolean isHidden() {
                    return (dosAttribute & 2) != 0;
                }

                public boolean isArchive() {
                    return (dosAttribute & 32) != 0;
                }

                public boolean isSystem() {
                    return (dosAttribute & 4) != 0;
                }
            };
            UnixNativeDispatcher.close(openForAttributeAccess);
            return r3;
        } catch (UnixException e) {
            e.rethrowAsIOException(this.file);
            UnixNativeDispatcher.close(openForAttributeAccess);
            return null;
        } catch (Throwable th) {
            UnixNativeDispatcher.close(openForAttributeAccess);
            throw th;
        }
    }

    public void setReadOnly(boolean z) throws IOException {
        updateDosAttribute(1, z);
    }

    public void setHidden(boolean z) throws IOException {
        updateDosAttribute(2, z);
    }

    public void setArchive(boolean z) throws IOException {
        updateDosAttribute(32, z);
    }

    public void setSystem(boolean z) throws IOException {
        updateDosAttribute(4, z);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:0|1|2|(7:4|(1:6)|7|(1:9)|33|10|(5:14|15|16|17|18))|19|20|21) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x005e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getDosAttribute(int r9) throws sun.nio.p035fs.UnixException {
        /*
            r8 = this;
            r8 = 24
            sun.nio.fs.NativeBuffer r0 = sun.nio.p035fs.NativeBuffers.getNativeBuffer(r8)
            r1 = 0
            byte[] r2 = DOS_XATTR_NAME_AS_BYTES     // Catch:{ UnixException -> 0x0068 }
            long r3 = r0.address()     // Catch:{ UnixException -> 0x0068 }
            int r8 = sun.nio.p035fs.LinuxNativeDispatcher.fgetxattr(r9, r2, r3, r8)     // Catch:{ UnixException -> 0x0068 }
            if (r8 <= 0) goto L_0x005e
            sun.misc.Unsafe r9 = unsafe     // Catch:{ UnixException -> 0x0068 }
            long r2 = r0.address()     // Catch:{ UnixException -> 0x0068 }
            long r4 = (long) r8     // Catch:{ UnixException -> 0x0068 }
            long r2 = r2 + r4
            r4 = 1
            long r2 = r2 - r4
            byte r9 = r9.getByte(r2)     // Catch:{ UnixException -> 0x0068 }
            if (r9 != 0) goto L_0x0026
            int r8 = r8 + -1
        L_0x0026:
            byte[] r9 = new byte[r8]     // Catch:{ UnixException -> 0x0068 }
            r2 = r1
        L_0x0029:
            if (r2 >= r8) goto L_0x003c
            sun.misc.Unsafe r3 = unsafe     // Catch:{ UnixException -> 0x0068 }
            long r4 = r0.address()     // Catch:{ UnixException -> 0x0068 }
            long r6 = (long) r2     // Catch:{ UnixException -> 0x0068 }
            long r4 = r4 + r6
            byte r3 = r3.getByte(r4)     // Catch:{ UnixException -> 0x0068 }
            r9[r2] = r3     // Catch:{ UnixException -> 0x0068 }
            int r2 = r2 + 1
            goto L_0x0029
        L_0x003c:
            java.lang.String r8 = sun.nio.p035fs.Util.toString(r9)     // Catch:{ UnixException -> 0x0068 }
            int r9 = r8.length()     // Catch:{ UnixException -> 0x0068 }
            r2 = 3
            if (r9 < r2) goto L_0x005e
            java.lang.String r9 = "0x"
            boolean r9 = r8.startsWith(r9)     // Catch:{ UnixException -> 0x0068 }
            if (r9 == 0) goto L_0x005e
            r9 = 2
            java.lang.String r8 = r8.substring(r9)     // Catch:{ NumberFormatException -> 0x005e }
            r9 = 16
            int r8 = java.lang.Integer.parseInt(r8, r9)     // Catch:{ NumberFormatException -> 0x005e }
            r0.release()
            return r8
        L_0x005e:
            sun.nio.fs.UnixException r8 = new sun.nio.fs.UnixException     // Catch:{ UnixException -> 0x0068 }
            java.lang.String r9 = "Value of user.DOSATTRIB attribute is invalid"
            r8.<init>((java.lang.String) r9)     // Catch:{ UnixException -> 0x0068 }
            throw r8     // Catch:{ UnixException -> 0x0068 }
        L_0x0066:
            r8 = move-exception
            goto L_0x0076
        L_0x0068:
            r8 = move-exception
            int r9 = r8.errno()     // Catch:{ all -> 0x0066 }
            int r2 = sun.nio.p035fs.UnixConstants.ENODATA     // Catch:{ all -> 0x0066 }
            if (r9 != r2) goto L_0x0075
            r0.release()
            return r1
        L_0x0075:
            throw r8     // Catch:{ all -> 0x0066 }
        L_0x0076:
            r0.release()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.LinuxDosFileAttributeView.getDosAttribute(int):int");
    }

    private void updateDosAttribute(int i, boolean z) throws IOException {
        NativeBuffer asNativeBuffer;
        this.file.checkWrite();
        int openForAttributeAccess = this.file.openForAttributeAccess(this.followLinks);
        try {
            int dosAttribute = getDosAttribute(openForAttributeAccess);
            int i2 = z ? i | dosAttribute : (~i) & dosAttribute;
            if (i2 != dosAttribute) {
                byte[] bytes = Util.toBytes("0x" + Integer.toHexString(i2));
                asNativeBuffer = NativeBuffers.asNativeBuffer(bytes);
                LinuxNativeDispatcher.fsetxattr(openForAttributeAccess, DOS_XATTR_NAME_AS_BYTES, asNativeBuffer.address(), bytes.length + 1);
                asNativeBuffer.release();
            }
        } catch (UnixException e) {
            try {
                e.rethrowAsIOException(this.file);
            } catch (Throwable th) {
                UnixNativeDispatcher.close(openForAttributeAccess);
                throw th;
            }
        } catch (Throwable th2) {
            asNativeBuffer.release();
            throw th2;
        }
        UnixNativeDispatcher.close(openForAttributeAccess);
    }
}
