package sun.nio.p035fs;

import com.sun.nio.file.ExtendedCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.LinkOption;
import java.nio.file.LinkPermission;
import java.nio.file.StandardCopyOption;
import java.p026io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/* renamed from: sun.nio.fs.UnixCopyFile */
class UnixCopyFile {
    static native void transfer(int i, int i2, long j) throws UnixException;

    private UnixCopyFile() {
    }

    /* renamed from: sun.nio.fs.UnixCopyFile$Flags */
    private static class Flags {
        boolean atomicMove;
        boolean copyBasicAttributes;
        boolean copyNonPosixAttributes;
        boolean copyPosixAttributes;
        boolean failIfUnableToCopyBasic;
        boolean failIfUnableToCopyNonPosix;
        boolean failIfUnableToCopyPosix;
        boolean followLinks;
        boolean interruptible;
        boolean replaceExisting;

        private Flags() {
        }

        static Flags fromCopyOptions(CopyOption... copyOptionArr) {
            Flags flags = new Flags();
            flags.followLinks = true;
            for (ExtendedCopyOption extendedCopyOption : copyOptionArr) {
                if (extendedCopyOption == StandardCopyOption.REPLACE_EXISTING) {
                    flags.replaceExisting = true;
                } else if (extendedCopyOption == LinkOption.NOFOLLOW_LINKS) {
                    flags.followLinks = false;
                } else if (extendedCopyOption == StandardCopyOption.COPY_ATTRIBUTES) {
                    flags.copyBasicAttributes = true;
                    flags.copyPosixAttributes = true;
                    flags.copyNonPosixAttributes = true;
                    flags.failIfUnableToCopyBasic = true;
                } else if (extendedCopyOption == ExtendedCopyOption.INTERRUPTIBLE) {
                    flags.interruptible = true;
                } else {
                    extendedCopyOption.getClass();
                    throw new UnsupportedOperationException("Unsupported copy option");
                }
            }
            return flags;
        }

        static Flags fromMoveOptions(CopyOption... copyOptionArr) {
            Flags flags = new Flags();
            for (LinkOption linkOption : copyOptionArr) {
                if (linkOption == StandardCopyOption.ATOMIC_MOVE) {
                    flags.atomicMove = true;
                } else if (linkOption == StandardCopyOption.REPLACE_EXISTING) {
                    flags.replaceExisting = true;
                } else if (linkOption != LinkOption.NOFOLLOW_LINKS) {
                    linkOption.getClass();
                    throw new UnsupportedOperationException("Unsupported copy option");
                }
            }
            flags.copyBasicAttributes = true;
            flags.copyPosixAttributes = true;
            flags.copyNonPosixAttributes = true;
            flags.failIfUnableToCopyBasic = true;
            return flags;
        }
    }

    private static void copyDirectory(UnixPath unixPath, UnixFileAttributes unixFileAttributes, UnixPath unixPath2, Flags flags) throws IOException {
        int i;
        try {
            UnixNativeDispatcher.mkdir(unixPath2, unixFileAttributes.mode());
        } catch (UnixException e) {
            e.rethrowAsIOException(unixPath2);
        }
        if (flags.copyBasicAttributes || flags.copyPosixAttributes || flags.copyNonPosixAttributes) {
            int i2 = -1;
            try {
                i = UnixNativeDispatcher.open(unixPath2, UnixConstants.O_RDONLY, 0);
            } catch (UnixException e2) {
                if (flags.copyNonPosixAttributes && flags.failIfUnableToCopyNonPosix) {
                    try {
                        UnixNativeDispatcher.rmdir(unixPath2);
                    } catch (UnixException unused) {
                    }
                    e2.rethrowAsIOException(unixPath2);
                }
                i = -1;
            }
            try {
                if (flags.copyPosixAttributes) {
                    if (i >= 0) {
                        UnixNativeDispatcher.fchown(i, unixFileAttributes.uid(), unixFileAttributes.gid());
                        UnixNativeDispatcher.fchmod(i, unixFileAttributes.mode());
                    } else {
                        UnixNativeDispatcher.chown(unixPath2, unixFileAttributes.uid(), unixFileAttributes.gid());
                        UnixNativeDispatcher.chmod(unixPath2, unixFileAttributes.mode());
                    }
                }
            } catch (UnixException e3) {
                if (flags.failIfUnableToCopyPosix) {
                    e3.rethrowAsIOException(unixPath2);
                }
            } catch (Throwable th) {
                if (i >= 0) {
                    UnixNativeDispatcher.close(i);
                }
                try {
                    UnixNativeDispatcher.rmdir(unixPath2);
                } catch (UnixException unused2) {
                }
                throw th;
            }
            if (flags.copyNonPosixAttributes && i >= 0) {
                try {
                    i2 = UnixNativeDispatcher.open(unixPath, UnixConstants.O_RDONLY, 0);
                } catch (UnixException e4) {
                    if (flags.failIfUnableToCopyNonPosix) {
                        e4.rethrowAsIOException(unixPath);
                    }
                }
                if (i2 >= 0) {
                    unixPath.getFileSystem().copyNonPosixAttributes(i2, i);
                    UnixNativeDispatcher.close(i2);
                }
            }
            if (flags.copyBasicAttributes) {
                if (i >= 0) {
                    try {
                        if (UnixNativeDispatcher.futimesSupported()) {
                            UnixNativeDispatcher.futimes(i, unixFileAttributes.lastAccessTime().mo61354to(TimeUnit.MICROSECONDS), unixFileAttributes.lastModifiedTime().mo61354to(TimeUnit.MICROSECONDS));
                        }
                    } catch (UnixException e5) {
                        if (flags.failIfUnableToCopyBasic) {
                            e5.rethrowAsIOException(unixPath2);
                        }
                    }
                }
                UnixNativeDispatcher.utimes(unixPath2, unixFileAttributes.lastAccessTime().mo61354to(TimeUnit.MICROSECONDS), unixFileAttributes.lastModifiedTime().mo61354to(TimeUnit.MICROSECONDS));
            }
            if (i >= 0) {
                UnixNativeDispatcher.close(i);
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:13|45|46|47|48|49|50) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x00aa */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:49:0x00aa=Splitter:B:49:0x00aa, B:41:0x009d=Splitter:B:41:0x009d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyFile(sun.nio.p035fs.UnixPath r4, sun.nio.p035fs.UnixFileAttributes r5, sun.nio.p035fs.UnixPath r6, sun.nio.p035fs.UnixCopyFile.Flags r7, long r8) throws java.p026io.IOException {
        /*
            r0 = -1
            int r1 = sun.nio.p035fs.UnixConstants.O_RDONLY     // Catch:{ UnixException -> 0x0009 }
            r2 = 0
            int r1 = sun.nio.p035fs.UnixNativeDispatcher.open(r4, r1, r2)     // Catch:{ UnixException -> 0x0009 }
            goto L_0x000e
        L_0x0009:
            r1 = move-exception
            r1.rethrowAsIOException((sun.nio.p035fs.UnixPath) r4)
            r1 = r0
        L_0x000e:
            int r2 = sun.nio.p035fs.UnixConstants.O_WRONLY     // Catch:{ UnixException -> 0x0022 }
            int r3 = sun.nio.p035fs.UnixConstants.O_CREAT     // Catch:{ UnixException -> 0x0022 }
            r2 = r2 | r3
            int r3 = sun.nio.p035fs.UnixConstants.O_EXCL     // Catch:{ UnixException -> 0x0022 }
            r2 = r2 | r3
            int r3 = r5.mode()     // Catch:{ UnixException -> 0x0022 }
            int r0 = sun.nio.p035fs.UnixNativeDispatcher.open(r6, r2, r3)     // Catch:{ UnixException -> 0x0022 }
            goto L_0x0026
        L_0x001f:
            r4 = move-exception
            goto L_0x00ab
        L_0x0022:
            r2 = move-exception
            r2.rethrowAsIOException((sun.nio.p035fs.UnixPath) r6)     // Catch:{ all -> 0x001f }
        L_0x0026:
            transfer(r0, r1, r8)     // Catch:{ UnixException -> 0x002d }
            goto L_0x0031
        L_0x002a:
            r4 = move-exception
            goto L_0x00a4
        L_0x002d:
            r8 = move-exception
            r8.rethrowAsIOException(r4, r6)     // Catch:{ all -> 0x002a }
        L_0x0031:
            boolean r8 = r7.copyPosixAttributes     // Catch:{ all -> 0x002a }
            if (r8 == 0) goto L_0x0050
            int r8 = r5.uid()     // Catch:{ UnixException -> 0x0048 }
            int r9 = r5.gid()     // Catch:{ UnixException -> 0x0048 }
            sun.nio.p035fs.UnixNativeDispatcher.fchown(r0, r8, r9)     // Catch:{ UnixException -> 0x0048 }
            int r8 = r5.mode()     // Catch:{ UnixException -> 0x0048 }
            sun.nio.p035fs.UnixNativeDispatcher.fchmod(r0, r8)     // Catch:{ UnixException -> 0x0048 }
            goto L_0x0050
        L_0x0048:
            r8 = move-exception
            boolean r9 = r7.failIfUnableToCopyPosix     // Catch:{ all -> 0x002a }
            if (r9 == 0) goto L_0x0050
            r8.rethrowAsIOException((sun.nio.p035fs.UnixPath) r6)     // Catch:{ all -> 0x002a }
        L_0x0050:
            boolean r8 = r7.copyNonPosixAttributes     // Catch:{ all -> 0x002a }
            if (r8 == 0) goto L_0x005b
            sun.nio.fs.UnixFileSystem r4 = r4.getFileSystem()     // Catch:{ all -> 0x002a }
            r4.copyNonPosixAttributes(r1, r0)     // Catch:{ all -> 0x002a }
        L_0x005b:
            boolean r4 = r7.copyBasicAttributes     // Catch:{ all -> 0x002a }
            if (r4 == 0) goto L_0x009d
            boolean r4 = sun.nio.p035fs.UnixNativeDispatcher.futimesSupported()     // Catch:{ UnixException -> 0x0095 }
            if (r4 == 0) goto L_0x007d
            java.nio.file.attribute.FileTime r4 = r5.lastAccessTime()     // Catch:{ UnixException -> 0x0095 }
            java.util.concurrent.TimeUnit r8 = java.util.concurrent.TimeUnit.MICROSECONDS     // Catch:{ UnixException -> 0x0095 }
            long r8 = r4.mo61354to(r8)     // Catch:{ UnixException -> 0x0095 }
            java.nio.file.attribute.FileTime r4 = r5.lastModifiedTime()     // Catch:{ UnixException -> 0x0095 }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MICROSECONDS     // Catch:{ UnixException -> 0x0095 }
            long r4 = r4.mo61354to(r5)     // Catch:{ UnixException -> 0x0095 }
            sun.nio.p035fs.UnixNativeDispatcher.futimes(r0, r8, r4)     // Catch:{ UnixException -> 0x0095 }
            goto L_0x009d
        L_0x007d:
            java.nio.file.attribute.FileTime r4 = r5.lastAccessTime()     // Catch:{ UnixException -> 0x0095 }
            java.util.concurrent.TimeUnit r8 = java.util.concurrent.TimeUnit.MICROSECONDS     // Catch:{ UnixException -> 0x0095 }
            long r8 = r4.mo61354to(r8)     // Catch:{ UnixException -> 0x0095 }
            java.nio.file.attribute.FileTime r4 = r5.lastModifiedTime()     // Catch:{ UnixException -> 0x0095 }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MICROSECONDS     // Catch:{ UnixException -> 0x0095 }
            long r4 = r4.mo61354to(r5)     // Catch:{ UnixException -> 0x0095 }
            sun.nio.p035fs.UnixNativeDispatcher.utimes(r6, r8, r4)     // Catch:{ UnixException -> 0x0095 }
            goto L_0x009d
        L_0x0095:
            r4 = move-exception
            boolean r5 = r7.failIfUnableToCopyBasic     // Catch:{ all -> 0x002a }
            if (r5 == 0) goto L_0x009d
            r4.rethrowAsIOException((sun.nio.p035fs.UnixPath) r6)     // Catch:{ all -> 0x002a }
        L_0x009d:
            sun.nio.p035fs.UnixNativeDispatcher.close(r0)     // Catch:{ all -> 0x001f }
            sun.nio.p035fs.UnixNativeDispatcher.close(r1)
            return
        L_0x00a4:
            sun.nio.p035fs.UnixNativeDispatcher.close(r0)     // Catch:{ all -> 0x001f }
            sun.nio.p035fs.UnixNativeDispatcher.unlink(r6)     // Catch:{ UnixException -> 0x00aa }
        L_0x00aa:
            throw r4     // Catch:{ all -> 0x001f }
        L_0x00ab:
            sun.nio.p035fs.UnixNativeDispatcher.close(r1)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixCopyFile.copyFile(sun.nio.fs.UnixPath, sun.nio.fs.UnixFileAttributes, sun.nio.fs.UnixPath, sun.nio.fs.UnixCopyFile$Flags, long):void");
    }

    private static void copyLink(UnixPath unixPath, UnixFileAttributes unixFileAttributes, UnixPath unixPath2, Flags flags) throws IOException {
        byte[] bArr;
        try {
            bArr = UnixNativeDispatcher.readlink(unixPath);
        } catch (UnixException e) {
            e.rethrowAsIOException(unixPath);
            bArr = null;
        }
        try {
            UnixNativeDispatcher.symlink(bArr, unixPath2);
            if (flags.copyPosixAttributes) {
                try {
                    UnixNativeDispatcher.lchown(unixPath2, unixFileAttributes.uid(), unixFileAttributes.gid());
                } catch (UnixException unused) {
                }
            }
        } catch (UnixException e2) {
            e2.rethrowAsIOException(unixPath2);
        }
    }

    private static void copySpecial(UnixPath unixPath, UnixFileAttributes unixFileAttributes, UnixPath unixPath2, Flags flags) throws IOException {
        try {
            UnixNativeDispatcher.mknod(unixPath2, unixFileAttributes.mode(), unixFileAttributes.rdev());
        } catch (UnixException e) {
            e.rethrowAsIOException(unixPath2);
        }
        try {
            if (flags.copyPosixAttributes) {
                UnixNativeDispatcher.chown(unixPath2, unixFileAttributes.uid(), unixFileAttributes.gid());
                UnixNativeDispatcher.chmod(unixPath2, unixFileAttributes.mode());
            }
        } catch (UnixException e2) {
            if (flags.failIfUnableToCopyPosix) {
                e2.rethrowAsIOException(unixPath2);
            }
        } catch (Throwable th) {
            try {
                UnixNativeDispatcher.unlink(unixPath2);
            } catch (UnixException unused) {
            }
            throw th;
        }
        if (flags.copyBasicAttributes) {
            try {
                UnixNativeDispatcher.utimes(unixPath2, unixFileAttributes.lastAccessTime().mo61354to(TimeUnit.MICROSECONDS), unixFileAttributes.lastModifiedTime().mo61354to(TimeUnit.MICROSECONDS));
            } catch (UnixException e3) {
                if (flags.failIfUnableToCopyBasic) {
                    e3.rethrowAsIOException(unixPath2);
                }
            }
        }
    }

    static void move(UnixPath unixPath, UnixPath unixPath2, CopyOption... copyOptionArr) throws IOException {
        UnixFileAttributes unixFileAttributes;
        if (System.getSecurityManager() != null) {
            unixPath.checkWrite();
            unixPath2.checkWrite();
        }
        Flags fromMoveOptions = Flags.fromMoveOptions(copyOptionArr);
        if (fromMoveOptions.atomicMove) {
            try {
                UnixNativeDispatcher.rename(unixPath, unixPath2);
            } catch (UnixException e) {
                if (e.errno() != UnixConstants.EXDEV) {
                    e.rethrowAsIOException(unixPath, unixPath2);
                    return;
                }
                throw new AtomicMoveNotSupportedException(unixPath.getPathForExceptionMessage(), unixPath2.getPathForExceptionMessage(), e.errorString());
            }
        } else {
            UnixFileAttributes unixFileAttributes2 = null;
            boolean z = false;
            try {
                unixFileAttributes = UnixFileAttributes.get(unixPath, false);
            } catch (UnixException e2) {
                e2.rethrowAsIOException(unixPath);
                unixFileAttributes = null;
            }
            try {
                unixFileAttributes2 = UnixFileAttributes.get(unixPath2, false);
            } catch (UnixException unused) {
            }
            if (unixFileAttributes2 != null) {
                z = true;
            }
            if (z) {
                if (!unixFileAttributes.isSameFile(unixFileAttributes2)) {
                    if (fromMoveOptions.replaceExisting) {
                        try {
                            if (unixFileAttributes2.isDirectory()) {
                                UnixNativeDispatcher.rmdir(unixPath2);
                            } else {
                                UnixNativeDispatcher.unlink(unixPath2);
                            }
                        } catch (UnixException e3) {
                            if (!unixFileAttributes2.isDirectory() || !(e3.errno() == UnixConstants.EEXIST || e3.errno() == UnixConstants.ENOTEMPTY)) {
                                e3.rethrowAsIOException(unixPath2);
                            } else {
                                throw new DirectoryNotEmptyException(unixPath2.getPathForExceptionMessage());
                            }
                        }
                    } else {
                        throw new FileAlreadyExistsException(unixPath2.getPathForExceptionMessage());
                    }
                } else {
                    return;
                }
            }
            try {
                UnixNativeDispatcher.rename(unixPath, unixPath2);
            } catch (UnixException e4) {
                if (!(e4.errno() == UnixConstants.EXDEV || e4.errno() == UnixConstants.EISDIR)) {
                    e4.rethrowAsIOException(unixPath, unixPath2);
                }
                if (unixFileAttributes.isDirectory()) {
                    copyDirectory(unixPath, unixFileAttributes, unixPath2, fromMoveOptions);
                } else if (unixFileAttributes.isSymbolicLink()) {
                    copyLink(unixPath, unixFileAttributes, unixPath2, fromMoveOptions);
                } else if (unixFileAttributes.isDevice()) {
                    copySpecial(unixPath, unixFileAttributes, unixPath2, fromMoveOptions);
                } else {
                    copyFile(unixPath, unixFileAttributes, unixPath2, fromMoveOptions, 0);
                }
                try {
                    if (unixFileAttributes.isDirectory()) {
                        UnixNativeDispatcher.rmdir(unixPath);
                    } else {
                        UnixNativeDispatcher.unlink(unixPath);
                    }
                } catch (UnixException e5) {
                    try {
                        if (unixFileAttributes.isDirectory()) {
                            UnixNativeDispatcher.rmdir(unixPath2);
                        } else {
                            UnixNativeDispatcher.unlink(unixPath2);
                        }
                    } catch (UnixException unused2) {
                    }
                    if (!unixFileAttributes.isDirectory() || !(e5.errno() == UnixConstants.EEXIST || e5.errno() == UnixConstants.ENOTEMPTY)) {
                        e5.rethrowAsIOException(unixPath);
                        return;
                    }
                    throw new DirectoryNotEmptyException(unixPath.getPathForExceptionMessage());
                }
            }
        }
    }

    static void copy(final UnixPath unixPath, final UnixPath unixPath2, CopyOption... copyOptionArr) throws IOException {
        final UnixFileAttributes unixFileAttributes;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            unixPath.checkRead();
            unixPath2.checkWrite();
        }
        final Flags fromCopyOptions = Flags.fromCopyOptions(copyOptionArr);
        UnixFileAttributes unixFileAttributes2 = null;
        try {
            unixFileAttributes = UnixFileAttributes.get(unixPath, fromCopyOptions.followLinks);
        } catch (UnixException e) {
            e.rethrowAsIOException(unixPath);
            unixFileAttributes = null;
        }
        if (securityManager != null && unixFileAttributes.isSymbolicLink()) {
            securityManager.checkPermission(new LinkPermission("symbolic"));
        }
        boolean z = false;
        try {
            unixFileAttributes2 = UnixFileAttributes.get(unixPath2, false);
        } catch (UnixException unused) {
        }
        if (unixFileAttributes2 != null) {
            z = true;
        }
        if (z) {
            if (!unixFileAttributes.isSameFile(unixFileAttributes2)) {
                if (fromCopyOptions.replaceExisting) {
                    try {
                        if (unixFileAttributes2.isDirectory()) {
                            UnixNativeDispatcher.rmdir(unixPath2);
                        } else {
                            UnixNativeDispatcher.unlink(unixPath2);
                        }
                    } catch (UnixException e2) {
                        if (!unixFileAttributes2.isDirectory() || !(e2.errno() == UnixConstants.EEXIST || e2.errno() == UnixConstants.ENOTEMPTY)) {
                            e2.rethrowAsIOException(unixPath2);
                        } else {
                            throw new DirectoryNotEmptyException(unixPath2.getPathForExceptionMessage());
                        }
                    }
                } else {
                    throw new FileAlreadyExistsException(unixPath2.getPathForExceptionMessage());
                }
            } else {
                return;
            }
        }
        if (unixFileAttributes.isDirectory()) {
            copyDirectory(unixPath, unixFileAttributes, unixPath2, fromCopyOptions);
        } else if (unixFileAttributes.isSymbolicLink()) {
            copyLink(unixPath, unixFileAttributes, unixPath2, fromCopyOptions);
        } else if (!fromCopyOptions.interruptible) {
            copyFile(unixPath, unixFileAttributes, unixPath2, fromCopyOptions, 0);
        } else {
            try {
                Cancellable.runInterruptibly(new Cancellable() {
                    public void implRun() throws IOException {
                        UnixCopyFile.copyFile(UnixPath.this, unixFileAttributes, unixPath2, fromCopyOptions, addressToPollForCancel());
                    }
                });
            } catch (ExecutionException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof IOException) {
                    throw ((IOException) cause);
                }
                throw new IOException(cause);
            }
        }
    }
}
