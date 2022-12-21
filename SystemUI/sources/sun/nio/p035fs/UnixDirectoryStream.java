package sun.nio.p035fs;

import dalvik.system.CloseGuard;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.p026io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* renamed from: sun.nio.fs.UnixDirectoryStream */
class UnixDirectoryStream implements DirectoryStream<Path> {
    /* access modifiers changed from: private */
    public final UnixPath dir;
    /* access modifiers changed from: private */

    /* renamed from: dp */
    public final long f910dp;
    /* access modifiers changed from: private */
    public final DirectoryStream.Filter<? super Path> filter;
    private final CloseGuard guard;
    private volatile boolean isClosed;
    private Iterator<Path> iterator;
    private final ReentrantReadWriteLock streamLock = new ReentrantReadWriteLock(true);

    UnixDirectoryStream(UnixPath unixPath, long j, DirectoryStream.Filter<? super Path> filter2) {
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.dir = unixPath;
        this.f910dp = j;
        this.filter = filter2;
        closeGuard.open("close");
    }

    /* access modifiers changed from: protected */
    public final UnixPath directory() {
        return this.dir;
    }

    /* access modifiers changed from: protected */
    public final Lock readLock() {
        return this.streamLock.readLock();
    }

    /* access modifiers changed from: protected */
    public final Lock writeLock() {
        return this.streamLock.writeLock();
    }

    /* access modifiers changed from: protected */
    public final boolean isOpen() {
        return !this.isClosed;
    }

    /* access modifiers changed from: protected */
    public final boolean closeImpl() throws IOException {
        if (this.isClosed) {
            return false;
        }
        this.isClosed = true;
        try {
            UnixNativeDispatcher.closedir(this.f910dp);
            this.guard.close();
            return true;
        } catch (UnixException e) {
            throw new IOException(e.errorString());
        }
    }

    public void close() throws IOException {
        writeLock().lock();
        try {
            closeImpl();
        } finally {
            writeLock().unlock();
        }
    }

    /* access modifiers changed from: protected */
    public final Iterator<Path> iterator(DirectoryStream<Path> directoryStream) {
        UnixDirectoryIterator unixDirectoryIterator;
        if (!this.isClosed) {
            synchronized (this) {
                if (this.iterator == null) {
                    unixDirectoryIterator = new UnixDirectoryIterator(directoryStream);
                    this.iterator = unixDirectoryIterator;
                } else {
                    throw new IllegalStateException("Iterator already obtained");
                }
            }
            return unixDirectoryIterator;
        }
        throw new IllegalStateException("Directory stream is closed");
    }

    public Iterator<Path> iterator() {
        return iterator(this);
    }

    /* renamed from: sun.nio.fs.UnixDirectoryStream$UnixDirectoryIterator */
    private class UnixDirectoryIterator implements Iterator<Path> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean atEof = false;
        private Path nextEntry;
        private final DirectoryStream<Path> stream;

        static {
            Class<UnixDirectoryStream> cls = UnixDirectoryStream.class;
        }

        UnixDirectoryIterator(DirectoryStream<Path> directoryStream) {
            this.stream = directoryStream;
        }

        private boolean isSelfOrParent(byte[] bArr) {
            if (bArr[0] == 46 && (bArr.length == 1 || (bArr.length == 2 && bArr[1] == 46))) {
                return true;
            }
            return false;
        }

        private Path readNextEntry() {
            UnixPath resolve;
            while (true) {
                UnixDirectoryStream.this.readLock().lock();
                try {
                    byte[] readdir = UnixDirectoryStream.this.isOpen() ? UnixNativeDispatcher.readdir(UnixDirectoryStream.this.f910dp) : null;
                    UnixDirectoryStream.this.readLock().unlock();
                    if (readdir == null) {
                        this.atEof = true;
                        return null;
                    } else if (!isSelfOrParent(readdir)) {
                        resolve = UnixDirectoryStream.this.dir.resolve(readdir);
                        try {
                            if (UnixDirectoryStream.this.filter == null || UnixDirectoryStream.this.filter.accept(resolve)) {
                                return resolve;
                            }
                        } catch (IOException e) {
                            throw new DirectoryIteratorException(e);
                        }
                    }
                } catch (UnixException e2) {
                    throw new DirectoryIteratorException(e2.asIOException(UnixDirectoryStream.this.dir));
                } catch (Throwable th) {
                    UnixDirectoryStream.this.readLock().unlock();
                    throw th;
                }
            }
            return resolve;
        }

        public synchronized boolean hasNext() {
            if (this.nextEntry == null && !this.atEof) {
                this.nextEntry = readNextEntry();
            }
            return this.nextEntry != null;
        }

        public synchronized Path next() {
            Path path;
            path = this.nextEntry;
            if (path != null || this.atEof) {
                this.nextEntry = null;
            } else {
                path = readNextEntry();
            }
            if (path == null) {
                throw new NoSuchElementException();
            }
            return path;
        }

        public void remove() {
            throw new UnsupportedOperationException();
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
