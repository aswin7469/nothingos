package sun.nio.p035fs;

import com.sun.nio.file.SensitivityWatchEventModifier;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.attribute.BasicFileAttributes;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* renamed from: sun.nio.fs.PollingWatchService */
class PollingWatchService extends AbstractWatchService {
    /* access modifiers changed from: private */
    public final Map<Object, PollingWatchKey> map = new HashMap();
    /* access modifiers changed from: private */
    public final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    });

    PollingWatchService() {
    }

    /* access modifiers changed from: package-private */
    public WatchKey register(final Path path, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException {
        final HashSet hashSet = new HashSet(kindArr.length);
        int i = 0;
        for (WatchEvent.Kind<Object> kind : kindArr) {
            if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_DELETE) {
                hashSet.add(kind);
            } else if (kind != StandardWatchEventKinds.OVERFLOW) {
                if (kind == null) {
                    throw new NullPointerException("An element in event set is 'null'");
                }
                throw new UnsupportedOperationException(kind.name());
            }
        }
        if (!hashSet.isEmpty()) {
            final SensitivityWatchEventModifier sensitivityWatchEventModifier = SensitivityWatchEventModifier.MEDIUM;
            if (modifierArr.length > 0) {
                int length = modifierArr.length;
                while (i < length) {
                    SensitivityWatchEventModifier sensitivityWatchEventModifier2 = modifierArr[i];
                    sensitivityWatchEventModifier2.getClass();
                    if (sensitivityWatchEventModifier2 instanceof SensitivityWatchEventModifier) {
                        sensitivityWatchEventModifier = sensitivityWatchEventModifier2;
                        i++;
                    } else {
                        throw new UnsupportedOperationException("Modifier not supported");
                    }
                }
            }
            if (isOpen()) {
                try {
                    return (WatchKey) AccessController.doPrivileged(new PrivilegedExceptionAction<PollingWatchKey>() {
                        public PollingWatchKey run() throws IOException {
                            return PollingWatchService.this.doPrivilegedRegister(path, hashSet, sensitivityWatchEventModifier);
                        }
                    });
                } catch (PrivilegedActionException e) {
                    Throwable cause = e.getCause();
                    if (cause == null || !(cause instanceof IOException)) {
                        throw new AssertionError((Object) e);
                    }
                    throw ((IOException) cause);
                }
            } else {
                throw new ClosedWatchServiceException();
            }
        } else {
            throw new IllegalArgumentException("No events to register");
        }
    }

    /* access modifiers changed from: private */
    public PollingWatchKey doPrivilegedRegister(Path path, Set<? extends WatchEvent.Kind<?>> set, SensitivityWatchEventModifier sensitivityWatchEventModifier) throws IOException {
        PollingWatchKey pollingWatchKey;
        BasicFileAttributes readAttributes = Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
        if (readAttributes.isDirectory()) {
            Object fileKey = readAttributes.fileKey();
            if (fileKey != null) {
                synchronized (closeLock()) {
                    if (isOpen()) {
                        synchronized (this.map) {
                            pollingWatchKey = this.map.get(fileKey);
                            if (pollingWatchKey == null) {
                                pollingWatchKey = new PollingWatchKey(path, this, fileKey);
                                this.map.put(fileKey, pollingWatchKey);
                            } else {
                                pollingWatchKey.disable();
                            }
                        }
                        pollingWatchKey.enable(set, (long) sensitivityWatchEventModifier.sensitivityValueInSeconds());
                    } else {
                        throw new ClosedWatchServiceException();
                    }
                }
                return pollingWatchKey;
            }
            throw new AssertionError((Object) "File keys must be supported");
        }
        throw new NotDirectoryException(path.toString());
    }

    /* access modifiers changed from: package-private */
    public void implClose() throws IOException {
        synchronized (this.map) {
            for (Map.Entry<Object, PollingWatchKey> value : this.map.entrySet()) {
                PollingWatchKey pollingWatchKey = (PollingWatchKey) value.getValue();
                pollingWatchKey.disable();
                pollingWatchKey.invalidate();
            }
            this.map.clear();
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                PollingWatchService.this.scheduledExecutor.shutdown();
                return null;
            }
        });
    }

    /* renamed from: sun.nio.fs.PollingWatchService$CacheEntry */
    private static class CacheEntry {
        /* access modifiers changed from: private */
        public long lastModified;
        private int lastTickCount;

        CacheEntry(long j, int i) {
            this.lastModified = j;
            this.lastTickCount = i;
        }

        /* access modifiers changed from: package-private */
        public int lastTickCount() {
            return this.lastTickCount;
        }

        /* access modifiers changed from: package-private */
        public long lastModified() {
            return this.lastModified;
        }

        /* access modifiers changed from: package-private */
        public void update(long j, int i) {
            this.lastModified = j;
            this.lastTickCount = i;
        }
    }

    /* renamed from: sun.nio.fs.PollingWatchService$PollingWatchKey */
    private class PollingWatchKey extends AbstractWatchKey {
        private Map<Path, CacheEntry> entries = new HashMap();
        private Set<? extends WatchEvent.Kind<?>> events;
        private final Object fileKey;
        private ScheduledFuture<?> poller;
        private int tickCount = 0;
        private volatile boolean valid = true;

        PollingWatchKey(Path path, PollingWatchService pollingWatchService, Object obj) throws IOException {
            super(path, pollingWatchService);
            DirectoryStream<Path> newDirectoryStream;
            this.fileKey = obj;
            try {
                newDirectoryStream = Files.newDirectoryStream(path);
                for (Path next : newDirectoryStream) {
                    this.entries.put(next.getFileName(), new CacheEntry(Files.getLastModifiedTime(next, LinkOption.NOFOLLOW_LINKS).toMillis(), this.tickCount));
                }
                if (newDirectoryStream != null) {
                    newDirectoryStream.close();
                    return;
                }
                return;
            } catch (DirectoryIteratorException e) {
                throw e.getCause();
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
            throw th;
        }

        /* access modifiers changed from: package-private */
        public Object fileKey() {
            return this.fileKey;
        }

        public boolean isValid() {
            return this.valid;
        }

        /* access modifiers changed from: package-private */
        public void invalidate() {
            this.valid = false;
        }

        /* access modifiers changed from: package-private */
        public void enable(Set<? extends WatchEvent.Kind<?>> set, long j) {
            synchronized (this) {
                this.events = set;
                this.poller = PollingWatchService.this.scheduledExecutor.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        PollingWatchKey.this.poll();
                    }
                }, j, j, TimeUnit.SECONDS);
            }
        }

        /* access modifiers changed from: package-private */
        public void disable() {
            synchronized (this) {
                ScheduledFuture<?> scheduledFuture = this.poller;
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
            }
        }

        public void cancel() {
            this.valid = false;
            synchronized (PollingWatchService.this.map) {
                PollingWatchService.this.map.remove(fileKey());
            }
            disable();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
            cancel();
            signal();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00eb, code lost:
            return;
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x009c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:38:0x00a4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x00e4 */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x00b5 A[Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void poll() {
            /*
                r10 = this;
                monitor-enter(r10)
                boolean r0 = r10.valid     // Catch:{ all -> 0x00ec }
                if (r0 != 0) goto L_0x0007
                monitor-exit(r10)
                return
            L_0x0007:
                int r0 = r10.tickCount     // Catch:{ all -> 0x00ec }
                r1 = 1
                int r0 = r0 + r1
                r10.tickCount = r0     // Catch:{ all -> 0x00ec }
                java.nio.file.Path r0 = r10.watchable()     // Catch:{ IOException -> 0x00e4 }
                java.nio.file.DirectoryStream r0 = java.nio.file.Files.newDirectoryStream(r0)     // Catch:{ IOException -> 0x00e4 }
                java.util.Iterator r2 = r0.iterator()     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
            L_0x0019:
                boolean r3 = r2.hasNext()     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                if (r3 == 0) goto L_0x009c
                java.lang.Object r3 = r2.next()     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.Path r3 = (java.nio.file.Path) r3     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.LinkOption[] r4 = new java.nio.file.LinkOption[r1]     // Catch:{ IOException -> 0x0019 }
                java.nio.file.LinkOption r5 = java.nio.file.LinkOption.NOFOLLOW_LINKS     // Catch:{ IOException -> 0x0019 }
                r6 = 0
                r4[r6] = r5     // Catch:{ IOException -> 0x0019 }
                java.nio.file.attribute.FileTime r4 = java.nio.file.Files.getLastModifiedTime(r3, r4)     // Catch:{ IOException -> 0x0019 }
                long r4 = r4.toMillis()     // Catch:{ IOException -> 0x0019 }
                java.util.Map<java.nio.file.Path, sun.nio.fs.PollingWatchService$CacheEntry> r6 = r10.entries     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.Path r7 = r3.getFileName()     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.lang.Object r6 = r6.get(r7)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                sun.nio.fs.PollingWatchService$CacheEntry r6 = (sun.nio.p035fs.PollingWatchService.CacheEntry) r6     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                if (r6 != 0) goto L_0x007a
                java.util.Map<java.nio.file.Path, sun.nio.fs.PollingWatchService$CacheEntry> r6 = r10.entries     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.Path r7 = r3.getFileName()     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                sun.nio.fs.PollingWatchService$CacheEntry r8 = new sun.nio.fs.PollingWatchService$CacheEntry     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                int r9 = r10.tickCount     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                r8.<init>(r4, r9)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                r6.put(r7, r8)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.util.Set<? extends java.nio.file.WatchEvent$Kind<?>> r4 = r10.events     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.WatchEvent$Kind<java.nio.file.Path> r5 = java.nio.file.StandardWatchEventKinds.ENTRY_CREATE     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                boolean r4 = r4.contains(r5)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                if (r4 == 0) goto L_0x0066
                java.nio.file.WatchEvent$Kind<java.nio.file.Path> r4 = java.nio.file.StandardWatchEventKinds.ENTRY_CREATE     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.Path r3 = r3.getFileName()     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                r10.signalEvent(r4, r3)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                goto L_0x0019
            L_0x0066:
                java.util.Set<? extends java.nio.file.WatchEvent$Kind<?>> r4 = r10.events     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.WatchEvent$Kind<java.nio.file.Path> r5 = java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                boolean r4 = r4.contains(r5)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                if (r4 == 0) goto L_0x0019
                java.nio.file.WatchEvent$Kind<java.nio.file.Path> r4 = java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.Path r3 = r3.getFileName()     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                r10.signalEvent(r4, r3)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                goto L_0x0019
            L_0x007a:
                long r7 = r6.lastModified     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                int r7 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
                if (r7 == 0) goto L_0x0095
                java.util.Set<? extends java.nio.file.WatchEvent$Kind<?>> r7 = r10.events     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.WatchEvent$Kind<java.nio.file.Path> r8 = java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                boolean r7 = r7.contains(r8)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                if (r7 == 0) goto L_0x0095
                java.nio.file.WatchEvent$Kind<java.nio.file.Path> r7 = java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                java.nio.file.Path r3 = r3.getFileName()     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                r10.signalEvent(r7, r3)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
            L_0x0095:
                int r3 = r10.tickCount     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                r6.update(r4, r3)     // Catch:{ DirectoryIteratorException -> 0x009c, all -> 0x00a0 }
                goto L_0x0019
            L_0x009c:
                r0.close()     // Catch:{ IOException -> 0x00a5 }
                goto L_0x00a5
            L_0x00a0:
                r1 = move-exception
                r0.close()     // Catch:{ IOException -> 0x00a4 }
            L_0x00a4:
                throw r1     // Catch:{ all -> 0x00ec }
            L_0x00a5:
                java.util.Map<java.nio.file.Path, sun.nio.fs.PollingWatchService$CacheEntry> r0 = r10.entries     // Catch:{ all -> 0x00ec }
                java.util.Set r0 = r0.entrySet()     // Catch:{ all -> 0x00ec }
                java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x00ec }
            L_0x00af:
                boolean r1 = r0.hasNext()     // Catch:{ all -> 0x00ec }
                if (r1 == 0) goto L_0x00e2
                java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x00ec }
                java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ all -> 0x00ec }
                java.lang.Object r2 = r1.getValue()     // Catch:{ all -> 0x00ec }
                sun.nio.fs.PollingWatchService$CacheEntry r2 = (sun.nio.p035fs.PollingWatchService.CacheEntry) r2     // Catch:{ all -> 0x00ec }
                int r2 = r2.lastTickCount()     // Catch:{ all -> 0x00ec }
                int r3 = r10.tickCount     // Catch:{ all -> 0x00ec }
                if (r2 == r3) goto L_0x00af
                java.lang.Object r1 = r1.getKey()     // Catch:{ all -> 0x00ec }
                java.nio.file.Path r1 = (java.nio.file.Path) r1     // Catch:{ all -> 0x00ec }
                r0.remove()     // Catch:{ all -> 0x00ec }
                java.util.Set<? extends java.nio.file.WatchEvent$Kind<?>> r2 = r10.events     // Catch:{ all -> 0x00ec }
                java.nio.file.WatchEvent$Kind<java.nio.file.Path> r3 = java.nio.file.StandardWatchEventKinds.ENTRY_DELETE     // Catch:{ all -> 0x00ec }
                boolean r2 = r2.contains(r3)     // Catch:{ all -> 0x00ec }
                if (r2 == 0) goto L_0x00af
                java.nio.file.WatchEvent$Kind<java.nio.file.Path> r2 = java.nio.file.StandardWatchEventKinds.ENTRY_DELETE     // Catch:{ all -> 0x00ec }
                r10.signalEvent(r2, r1)     // Catch:{ all -> 0x00ec }
                goto L_0x00af
            L_0x00e2:
                monitor-exit(r10)
                return
            L_0x00e4:
                r10.cancel()     // Catch:{ all -> 0x00ec }
                r10.signal()     // Catch:{ all -> 0x00ec }
                monitor-exit(r10)
                return
            L_0x00ec:
                r0 = move-exception
                monitor-exit(r10)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.PollingWatchService.PollingWatchKey.poll():void");
        }
    }
}
