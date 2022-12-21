package sun.nio.p035fs;

import dalvik.system.CloseGuard;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.ClosedDirectoryStreamException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.LinkOption;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.p026io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import sun.nio.p035fs.UnixUserPrincipals;

/* renamed from: sun.nio.fs.UnixSecureDirectoryStream */
class UnixSecureDirectoryStream implements SecureDirectoryStream<Path> {
    /* access modifiers changed from: private */
    public final int dfd;
    /* access modifiers changed from: private */

    /* renamed from: ds */
    public final UnixDirectoryStream f912ds;
    private final CloseGuard guard;

    UnixSecureDirectoryStream(UnixPath dir, long dp, int dfd2, DirectoryStream.Filter<? super Path> filter) {
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.f912ds = new UnixDirectoryStream(dir, dp, filter);
        this.dfd = dfd2;
        if (dfd2 != -1) {
            closeGuard.open("close");
        }
    }

    /* JADX INFO: finally extract failed */
    public void close() throws IOException {
        this.f912ds.writeLock().lock();
        try {
            if (this.f912ds.closeImpl()) {
                UnixNativeDispatcher.close(this.dfd);
            }
            this.f912ds.writeLock().unlock();
            this.guard.close();
        } catch (Throwable th) {
            this.f912ds.writeLock().unlock();
            throw th;
        }
    }

    public Iterator<Path> iterator() {
        return this.f912ds.iterator(this);
    }

    private UnixPath getName(Path obj) {
        if (obj == null) {
            throw new NullPointerException();
        } else if (obj instanceof UnixPath) {
            return (UnixPath) obj;
        } else {
            throw new ProviderMismatchException();
        }
    }

    public SecureDirectoryStream<Path> newDirectoryStream(Path obj, LinkOption... options) throws IOException {
        UnixPath name = getName(obj);
        UnixPath resolve = this.f912ds.directory().resolve((Path) name);
        boolean followLinks = Util.followLinks(options);
        if (System.getSecurityManager() != null) {
            resolve.checkRead();
        }
        this.f912ds.readLock().lock();
        try {
            if (this.f912ds.isOpen()) {
                int i = -1;
                int i2 = -1;
                long j = 0;
                int i3 = UnixConstants.O_RDONLY;
                if (!followLinks) {
                    i3 |= UnixConstants.O_NOFOLLOW;
                }
                i = UnixNativeDispatcher.openat(this.dfd, name.asByteArray(), i3, 0);
                i2 = UnixNativeDispatcher.dup(i);
                j = UnixNativeDispatcher.fdopendir(i);
                int i4 = i;
                UnixSecureDirectoryStream unixSecureDirectoryStream = new UnixSecureDirectoryStream(resolve, j, i2, (DirectoryStream.Filter<? super Path>) null);
                this.f912ds.readLock().unlock();
                return unixSecureDirectoryStream;
            }
            throw new ClosedDirectoryStreamException();
        } catch (UnixException e) {
            if (-1 != -1) {
                UnixNativeDispatcher.close(-1);
            }
            if (-1 != -1) {
                UnixNativeDispatcher.close(-1);
            }
            if (e.errno() != UnixConstants.ENOTDIR) {
                e.rethrowAsIOException(name);
            } else {
                throw new NotDirectoryException(name.toString());
            }
        } catch (Throwable th) {
            this.f912ds.readLock().unlock();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public SeekableByteChannel newByteChannel(Path obj, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        UnixPath name = getName(obj);
        int unixMode = UnixFileModeAttribute.toUnixMode(UnixFileModeAttribute.ALL_READWRITE, fileAttributeArr);
        String pathForPermissionCheck = this.f912ds.directory().resolve((Path) name).getPathForPermissionCheck();
        this.f912ds.readLock().lock();
        try {
            if (this.f912ds.isOpen()) {
                FileChannel newFileChannel = UnixChannelFactory.newFileChannel(this.dfd, name, pathForPermissionCheck, set, unixMode);
                this.f912ds.readLock().unlock();
                return newFileChannel;
            }
            throw new ClosedDirectoryStreamException();
        } catch (UnixException e) {
            e.rethrowAsIOException(name);
            this.f912ds.readLock().unlock();
            return null;
        } catch (Throwable th) {
            this.f912ds.readLock().unlock();
            throw th;
        }
    }

    private void implDelete(Path obj, boolean haveFlags, int flags) throws IOException {
        UnixPath name = getName(obj);
        if (System.getSecurityManager() != null) {
            this.f912ds.directory().resolve((Path) name).checkDelete();
        }
        this.f912ds.readLock().lock();
        try {
            if (this.f912ds.isOpen()) {
                if (!haveFlags) {
                    UnixFileAttributes unixFileAttributes = null;
                    int i = 0;
                    unixFileAttributes = UnixFileAttributes.get(this.dfd, name, false);
                    if (unixFileAttributes.isDirectory()) {
                        i = 512;
                    }
                    flags = i;
                }
                UnixNativeDispatcher.unlinkat(this.dfd, name.asByteArray(), flags);
                this.f912ds.readLock().unlock();
                return;
            }
            throw new ClosedDirectoryStreamException();
        } catch (UnixException e) {
            if ((flags & 512) != 0) {
                if (e.errno() == UnixConstants.EEXIST || e.errno() == UnixConstants.ENOTEMPTY) {
                    throw new DirectoryNotEmptyException((String) null);
                }
            }
            e.rethrowAsIOException(name);
        } catch (UnixException e2) {
            e2.rethrowAsIOException(name);
        } catch (Throwable th) {
            this.f912ds.readLock().unlock();
            throw th;
        }
    }

    public void deleteFile(Path file) throws IOException {
        implDelete(file, true, 0);
    }

    public void deleteDirectory(Path dir) throws IOException {
        implDelete(dir, true, 512);
    }

    public void move(Path fromObj, SecureDirectoryStream<Path> secureDirectoryStream, Path toObj) throws IOException {
        UnixPath name = getName(fromObj);
        UnixPath name2 = getName(toObj);
        if (secureDirectoryStream == null) {
            throw new NullPointerException();
        } else if (secureDirectoryStream instanceof UnixSecureDirectoryStream) {
            this = (UnixSecureDirectoryStream) secureDirectoryStream;
            if (System.getSecurityManager() != null) {
                this.f912ds.directory().resolve((Path) name).checkWrite();
                this.f912ds.directory().resolve((Path) name2).checkWrite();
            }
            this.f912ds.readLock().lock();
            try {
                this.f912ds.readLock().lock();
                try {
                    if (!this.f912ds.isOpen() || !this.f912ds.isOpen()) {
                        throw new ClosedDirectoryStreamException();
                    }
                    UnixNativeDispatcher.renameat(this.dfd, name.asByteArray(), this.dfd, name2.asByteArray());
                    this.f912ds.readLock().unlock();
                } catch (UnixException e) {
                    if (e.errno() != UnixConstants.EXDEV) {
                        e.rethrowAsIOException(name, name2);
                    } else {
                        throw new AtomicMoveNotSupportedException(name.toString(), name2.toString(), e.errorString());
                    }
                } catch (Throwable th) {
                    this.f912ds.readLock().unlock();
                    throw th;
                }
            } finally {
                this.f912ds.readLock().unlock();
            }
        } else {
            throw new ProviderMismatchException();
        }
    }

    private <V extends FileAttributeView> V getFileAttributeViewImpl(UnixPath file, Class<V> cls, boolean followLinks) {
        if (cls != null) {
            Class<V> cls2 = cls;
            if (cls2 == BasicFileAttributeView.class) {
                return new BasicFileAttributeViewImpl(file, followLinks);
            }
            if (cls2 == PosixFileAttributeView.class || cls2 == FileOwnerAttributeView.class) {
                return new PosixFileAttributeViewImpl(file, followLinks);
            }
            FileAttributeView fileAttributeView = null;
            return null;
        }
        throw new NullPointerException();
    }

    public <V extends FileAttributeView> V getFileAttributeView(Class<V> cls) {
        return getFileAttributeViewImpl((UnixPath) null, cls, false);
    }

    public <V extends FileAttributeView> V getFileAttributeView(Path obj, Class<V> cls, LinkOption... options) {
        return getFileAttributeViewImpl(getName(obj), cls, Util.followLinks(options));
    }

    /* renamed from: sun.nio.fs.UnixSecureDirectoryStream$BasicFileAttributeViewImpl */
    private class BasicFileAttributeViewImpl implements BasicFileAttributeView {
        final UnixPath file;
        final boolean followLinks;

        public String name() {
            return "basic";
        }

        BasicFileAttributeViewImpl(UnixPath unixPath, boolean z) {
            this.file = unixPath;
            this.followLinks = z;
        }

        /* access modifiers changed from: package-private */
        public int open() throws IOException {
            int i = UnixConstants.O_RDONLY;
            if (!this.followLinks) {
                i |= UnixConstants.O_NOFOLLOW;
            }
            try {
                return UnixNativeDispatcher.openat(UnixSecureDirectoryStream.this.dfd, this.file.asByteArray(), i, 0);
            } catch (UnixException e) {
                e.rethrowAsIOException(this.file);
                return -1;
            }
        }

        /* access modifiers changed from: private */
        public void checkWriteAccess() {
            if (System.getSecurityManager() == null) {
                return;
            }
            if (this.file == null) {
                UnixSecureDirectoryStream.this.f912ds.directory().checkWrite();
            } else {
                UnixSecureDirectoryStream.this.f912ds.directory().resolve((Path) this.file).checkWrite();
            }
        }

        /* JADX INFO: finally extract failed */
        public BasicFileAttributes readAttributes() throws IOException {
            UnixFileAttributes unixFileAttributes;
            UnixSecureDirectoryStream.this.f912ds.readLock().lock();
            try {
                if (UnixSecureDirectoryStream.this.f912ds.isOpen()) {
                    if (System.getSecurityManager() != null) {
                        if (this.file == null) {
                            UnixSecureDirectoryStream.this.f912ds.directory().checkRead();
                        } else {
                            UnixSecureDirectoryStream.this.f912ds.directory().resolve((Path) this.file).checkRead();
                        }
                    }
                    if (this.file == null) {
                        unixFileAttributes = UnixFileAttributes.get(UnixSecureDirectoryStream.this.dfd);
                    } else {
                        unixFileAttributes = UnixFileAttributes.get(UnixSecureDirectoryStream.this.dfd, this.file, this.followLinks);
                    }
                    BasicFileAttributes asBasicFileAttributes = unixFileAttributes.asBasicFileAttributes();
                    UnixSecureDirectoryStream.this.f912ds.readLock().unlock();
                    return asBasicFileAttributes;
                }
                throw new ClosedDirectoryStreamException();
            } catch (UnixException e) {
                e.rethrowAsIOException(this.file);
                UnixSecureDirectoryStream.this.f912ds.readLock().unlock();
                return null;
            } catch (Throwable th) {
                UnixSecureDirectoryStream.this.f912ds.readLock().unlock();
                throw th;
            }
        }

        public void setTimes(FileTime fileTime, FileTime fileTime2, FileTime fileTime3) throws IOException {
            int r5;
            checkWriteAccess();
            UnixSecureDirectoryStream.this.f912ds.readLock().lock();
            try {
                if (UnixSecureDirectoryStream.this.f912ds.isOpen()) {
                    r5 = this.file == null ? UnixSecureDirectoryStream.this.dfd : open();
                    if (fileTime == null || fileTime2 == null) {
                        try {
                            UnixFileAttributes unixFileAttributes = UnixFileAttributes.get(r5);
                            if (fileTime == null) {
                                fileTime = unixFileAttributes.lastModifiedTime();
                            }
                            if (fileTime2 == null) {
                                fileTime2 = unixFileAttributes.lastAccessTime();
                            }
                        } catch (UnixException e) {
                            e.rethrowAsIOException(this.file);
                        }
                    }
                    try {
                        UnixNativeDispatcher.futimes(r5, fileTime2.mo61298to(TimeUnit.MICROSECONDS), fileTime.mo61298to(TimeUnit.MICROSECONDS));
                    } catch (UnixException e2) {
                        e2.rethrowAsIOException(this.file);
                    }
                    if (this.file != null) {
                        UnixNativeDispatcher.close(r5);
                    }
                    UnixSecureDirectoryStream.this.f912ds.readLock().unlock();
                    return;
                }
                throw new ClosedDirectoryStreamException();
            } catch (Throwable th) {
                UnixSecureDirectoryStream.this.f912ds.readLock().unlock();
                throw th;
            }
        }
    }

    /* renamed from: sun.nio.fs.UnixSecureDirectoryStream$PosixFileAttributeViewImpl */
    private class PosixFileAttributeViewImpl extends BasicFileAttributeViewImpl implements PosixFileAttributeView {
        public String name() {
            return "posix";
        }

        PosixFileAttributeViewImpl(UnixPath unixPath, boolean z) {
            super(unixPath, z);
        }

        private void checkWriteAndUserAccess() {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                checkWriteAccess();
                securityManager.checkPermission(new RuntimePermission("accessUserInformation"));
            }
        }

        /* JADX INFO: finally extract failed */
        public PosixFileAttributes readAttributes() throws IOException {
            UnixFileAttributes unixFileAttributes;
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                if (this.file == null) {
                    UnixSecureDirectoryStream.this.f912ds.directory().checkRead();
                } else {
                    UnixSecureDirectoryStream.this.f912ds.directory().resolve((Path) this.file).checkRead();
                }
                securityManager.checkPermission(new RuntimePermission("accessUserInformation"));
            }
            UnixSecureDirectoryStream.this.f912ds.readLock().lock();
            try {
                if (UnixSecureDirectoryStream.this.f912ds.isOpen()) {
                    if (this.file == null) {
                        unixFileAttributes = UnixFileAttributes.get(UnixSecureDirectoryStream.this.dfd);
                    } else {
                        unixFileAttributes = UnixFileAttributes.get(UnixSecureDirectoryStream.this.dfd, this.file, this.followLinks);
                    }
                    UnixSecureDirectoryStream.this.f912ds.readLock().unlock();
                    return unixFileAttributes;
                }
                throw new ClosedDirectoryStreamException();
            } catch (UnixException e) {
                e.rethrowAsIOException(this.file);
                UnixSecureDirectoryStream.this.f912ds.readLock().unlock();
                return null;
            } catch (Throwable th) {
                UnixSecureDirectoryStream.this.f912ds.readLock().unlock();
                throw th;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0036, code lost:
            if (r0 >= 0) goto L_0x0038;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0038, code lost:
            sun.nio.p035fs.UnixNativeDispatcher.close(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0048, code lost:
            if (r0 >= 0) goto L_0x0038;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setPermissions(java.util.Set<java.nio.file.attribute.PosixFilePermission> r3) throws java.p026io.IOException {
            /*
                r2 = this;
                r2.checkWriteAndUserAccess()
                sun.nio.fs.UnixSecureDirectoryStream r0 = sun.nio.p035fs.UnixSecureDirectoryStream.this
                sun.nio.fs.UnixDirectoryStream r0 = r0.f912ds
                java.util.concurrent.locks.Lock r0 = r0.readLock()
                r0.lock()
                sun.nio.fs.UnixSecureDirectoryStream r0 = sun.nio.p035fs.UnixSecureDirectoryStream.this     // Catch:{ all -> 0x0069 }
                sun.nio.fs.UnixDirectoryStream r0 = r0.f912ds     // Catch:{ all -> 0x0069 }
                boolean r0 = r0.isOpen()     // Catch:{ all -> 0x0069 }
                if (r0 == 0) goto L_0x0063
                sun.nio.fs.UnixPath r0 = r2.file     // Catch:{ all -> 0x0069 }
                if (r0 != 0) goto L_0x0027
                sun.nio.fs.UnixSecureDirectoryStream r0 = sun.nio.p035fs.UnixSecureDirectoryStream.this     // Catch:{ all -> 0x0069 }
                int r0 = r0.dfd     // Catch:{ all -> 0x0069 }
                goto L_0x002b
            L_0x0027:
                int r0 = r2.open()     // Catch:{ all -> 0x0069 }
            L_0x002b:
                int r3 = sun.nio.p035fs.UnixFileModeAttribute.toUnixMode(r3)     // Catch:{ UnixException -> 0x003e }
                sun.nio.p035fs.UnixNativeDispatcher.fchmod(r0, r3)     // Catch:{ UnixException -> 0x003e }
                sun.nio.fs.UnixPath r3 = r2.file     // Catch:{ all -> 0x0069 }
                if (r3 == 0) goto L_0x004b
                if (r0 < 0) goto L_0x004b
            L_0x0038:
                sun.nio.p035fs.UnixNativeDispatcher.close(r0)     // Catch:{ all -> 0x0069 }
                goto L_0x004b
            L_0x003c:
                r3 = move-exception
                goto L_0x0059
            L_0x003e:
                r3 = move-exception
                sun.nio.fs.UnixPath r1 = r2.file     // Catch:{ all -> 0x003c }
                r3.rethrowAsIOException((sun.nio.p035fs.UnixPath) r1)     // Catch:{ all -> 0x003c }
                sun.nio.fs.UnixPath r3 = r2.file     // Catch:{ all -> 0x0069 }
                if (r3 == 0) goto L_0x004b
                if (r0 < 0) goto L_0x004b
                goto L_0x0038
            L_0x004b:
                sun.nio.fs.UnixSecureDirectoryStream r2 = sun.nio.p035fs.UnixSecureDirectoryStream.this
                sun.nio.fs.UnixDirectoryStream r2 = r2.f912ds
                java.util.concurrent.locks.Lock r2 = r2.readLock()
                r2.unlock()
                return
            L_0x0059:
                sun.nio.fs.UnixPath r1 = r2.file     // Catch:{ all -> 0x0069 }
                if (r1 == 0) goto L_0x0062
                if (r0 < 0) goto L_0x0062
                sun.nio.p035fs.UnixNativeDispatcher.close(r0)     // Catch:{ all -> 0x0069 }
            L_0x0062:
                throw r3     // Catch:{ all -> 0x0069 }
            L_0x0063:
                java.nio.file.ClosedDirectoryStreamException r3 = new java.nio.file.ClosedDirectoryStreamException     // Catch:{ all -> 0x0069 }
                r3.<init>()     // Catch:{ all -> 0x0069 }
                throw r3     // Catch:{ all -> 0x0069 }
            L_0x0069:
                r3 = move-exception
                sun.nio.fs.UnixSecureDirectoryStream r2 = sun.nio.p035fs.UnixSecureDirectoryStream.this
                sun.nio.fs.UnixDirectoryStream r2 = r2.f912ds
                java.util.concurrent.locks.Lock r2 = r2.readLock()
                r2.unlock()
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixSecureDirectoryStream.PosixFileAttributeViewImpl.setPermissions(java.util.Set):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
            if (r0 >= 0) goto L_0x0034;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0034, code lost:
            sun.nio.p035fs.UnixNativeDispatcher.close(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0044, code lost:
            if (r0 >= 0) goto L_0x0034;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void setOwners(int r2, int r3) throws java.p026io.IOException {
            /*
                r1 = this;
                r1.checkWriteAndUserAccess()
                sun.nio.fs.UnixSecureDirectoryStream r0 = sun.nio.p035fs.UnixSecureDirectoryStream.this
                sun.nio.fs.UnixDirectoryStream r0 = r0.f912ds
                java.util.concurrent.locks.Lock r0 = r0.readLock()
                r0.lock()
                sun.nio.fs.UnixSecureDirectoryStream r0 = sun.nio.p035fs.UnixSecureDirectoryStream.this     // Catch:{ all -> 0x0065 }
                sun.nio.fs.UnixDirectoryStream r0 = r0.f912ds     // Catch:{ all -> 0x0065 }
                boolean r0 = r0.isOpen()     // Catch:{ all -> 0x0065 }
                if (r0 == 0) goto L_0x005f
                sun.nio.fs.UnixPath r0 = r1.file     // Catch:{ all -> 0x0065 }
                if (r0 != 0) goto L_0x0027
                sun.nio.fs.UnixSecureDirectoryStream r0 = sun.nio.p035fs.UnixSecureDirectoryStream.this     // Catch:{ all -> 0x0065 }
                int r0 = r0.dfd     // Catch:{ all -> 0x0065 }
                goto L_0x002b
            L_0x0027:
                int r0 = r1.open()     // Catch:{ all -> 0x0065 }
            L_0x002b:
                sun.nio.p035fs.UnixNativeDispatcher.fchown(r0, r2, r3)     // Catch:{ UnixException -> 0x003a }
                sun.nio.fs.UnixPath r2 = r1.file     // Catch:{ all -> 0x0065 }
                if (r2 == 0) goto L_0x0047
                if (r0 < 0) goto L_0x0047
            L_0x0034:
                sun.nio.p035fs.UnixNativeDispatcher.close(r0)     // Catch:{ all -> 0x0065 }
                goto L_0x0047
            L_0x0038:
                r2 = move-exception
                goto L_0x0055
            L_0x003a:
                r2 = move-exception
                sun.nio.fs.UnixPath r3 = r1.file     // Catch:{ all -> 0x0038 }
                r2.rethrowAsIOException((sun.nio.p035fs.UnixPath) r3)     // Catch:{ all -> 0x0038 }
                sun.nio.fs.UnixPath r2 = r1.file     // Catch:{ all -> 0x0065 }
                if (r2 == 0) goto L_0x0047
                if (r0 < 0) goto L_0x0047
                goto L_0x0034
            L_0x0047:
                sun.nio.fs.UnixSecureDirectoryStream r1 = sun.nio.p035fs.UnixSecureDirectoryStream.this
                sun.nio.fs.UnixDirectoryStream r1 = r1.f912ds
                java.util.concurrent.locks.Lock r1 = r1.readLock()
                r1.unlock()
                return
            L_0x0055:
                sun.nio.fs.UnixPath r3 = r1.file     // Catch:{ all -> 0x0065 }
                if (r3 == 0) goto L_0x005e
                if (r0 < 0) goto L_0x005e
                sun.nio.p035fs.UnixNativeDispatcher.close(r0)     // Catch:{ all -> 0x0065 }
            L_0x005e:
                throw r2     // Catch:{ all -> 0x0065 }
            L_0x005f:
                java.nio.file.ClosedDirectoryStreamException r2 = new java.nio.file.ClosedDirectoryStreamException     // Catch:{ all -> 0x0065 }
                r2.<init>()     // Catch:{ all -> 0x0065 }
                throw r2     // Catch:{ all -> 0x0065 }
            L_0x0065:
                r2 = move-exception
                sun.nio.fs.UnixSecureDirectoryStream r1 = sun.nio.p035fs.UnixSecureDirectoryStream.this
                sun.nio.fs.UnixDirectoryStream r1 = r1.f912ds
                java.util.concurrent.locks.Lock r1 = r1.readLock()
                r1.unlock()
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixSecureDirectoryStream.PosixFileAttributeViewImpl.setOwners(int, int):void");
        }

        public UserPrincipal getOwner() throws IOException {
            return readAttributes().owner();
        }

        public void setOwner(UserPrincipal userPrincipal) throws IOException {
            if (!(userPrincipal instanceof UnixUserPrincipals.User)) {
                throw new ProviderMismatchException();
            } else if (!(userPrincipal instanceof UnixUserPrincipals.Group)) {
                setOwners(((UnixUserPrincipals.User) userPrincipal).uid(), -1);
            } else {
                throw new IOException("'owner' parameter can't be a group");
            }
        }

        public void setGroup(GroupPrincipal groupPrincipal) throws IOException {
            if (groupPrincipal instanceof UnixUserPrincipals.Group) {
                setOwners(-1, ((UnixUserPrincipals.Group) groupPrincipal).gid());
                return;
            }
            throw new ProviderMismatchException();
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws IOException {
        CloseGuard closeGuard = this.guard;
        if (closeGuard != null) {
            closeGuard.warnIfOpen();
        }
        close();
    }
}
