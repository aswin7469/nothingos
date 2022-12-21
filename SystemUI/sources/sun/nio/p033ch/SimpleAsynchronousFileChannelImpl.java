package sun.nio.p033ch;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* renamed from: sun.nio.ch.SimpleAsynchronousFileChannelImpl */
public class SimpleAsynchronousFileChannelImpl extends AsynchronousFileChannelImpl {
    /* access modifiers changed from: private */

    /* renamed from: nd */
    public static final FileDispatcher f890nd = new FileDispatcherImpl();
    /* access modifiers changed from: private */
    public final NativeThreadSet threads = new NativeThreadSet(2);

    /* renamed from: sun.nio.ch.SimpleAsynchronousFileChannelImpl$DefaultExecutorHolder */
    private static class DefaultExecutorHolder {
        static final ExecutorService defaultExecutor = ThreadPool.createDefault().executor();

        private DefaultExecutorHolder() {
        }
    }

    SimpleAsynchronousFileChannelImpl(FileDescriptor fileDescriptor, boolean z, boolean z2, ExecutorService executorService) {
        super(fileDescriptor, z, z2, executorService);
    }

    public static AsynchronousFileChannel open(FileDescriptor fileDescriptor, boolean z, boolean z2, ThreadPool threadPool) {
        return new SimpleAsynchronousFileChannelImpl(fileDescriptor, z, z2, threadPool == null ? DefaultExecutorHolder.defaultExecutor : threadPool.executor());
    }

    public void close() throws IOException {
        synchronized (this.fdObj) {
            if (!this.closed) {
                this.closed = true;
                invalidateAllLocks();
                this.threads.signalAndWait();
                this.closeLock.writeLock().lock();
                this.closeLock.writeLock().unlock();
                f890nd.close(this.fdObj);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:12:0x0028=Splitter:B:12:0x0028, B:22:0x003b=Splitter:B:22:0x003b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long size() throws java.p026io.IOException {
        /*
            r9 = this;
            sun.nio.ch.NativeThreadSet r0 = r9.threads
            int r0 = r0.add()
            r1 = 1
            r2 = 0
            r3 = 0
            r9.begin()     // Catch:{ all -> 0x0033 }
            r5 = r3
        L_0x000e:
            sun.nio.ch.FileDispatcher r7 = f890nd     // Catch:{ all -> 0x0031 }
            java.io.FileDescriptor r8 = r9.fdObj     // Catch:{ all -> 0x0031 }
            long r5 = r7.size(r8)     // Catch:{ all -> 0x0031 }
            r7 = -3
            int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r7 != 0) goto L_0x0022
            boolean r7 = r9.isOpen()     // Catch:{ all -> 0x0031 }
            if (r7 != 0) goto L_0x000e
        L_0x0022:
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 < 0) goto L_0x0027
            goto L_0x0028
        L_0x0027:
            r1 = r2
        L_0x0028:
            r9.end(r1)     // Catch:{ all -> 0x003f }
            sun.nio.ch.NativeThreadSet r9 = r9.threads
            r9.remove(r0)
            return r5
        L_0x0031:
            r7 = move-exception
            goto L_0x0035
        L_0x0033:
            r7 = move-exception
            r5 = r3
        L_0x0035:
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 < 0) goto L_0x003a
            goto L_0x003b
        L_0x003a:
            r1 = r2
        L_0x003b:
            r9.end(r1)     // Catch:{ all -> 0x003f }
            throw r7     // Catch:{ all -> 0x003f }
        L_0x003f:
            r1 = move-exception
            sun.nio.ch.NativeThreadSet r9 = r9.threads
            r9.remove(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.size():long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x005f  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:34:0x0060=Splitter:B:34:0x0060, B:24:0x004d=Splitter:B:24:0x004d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.nio.channels.AsynchronousFileChannel truncate(long r12) throws java.p026io.IOException {
        /*
            r11 = this;
            r0 = 0
            int r2 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r2 < 0) goto L_0x0071
            boolean r2 = r11.writing
            if (r2 == 0) goto L_0x006b
            sun.nio.ch.NativeThreadSet r2 = r11.threads
            int r2 = r2.add()
            r3 = 1
            r4 = 0
            r11.begin()     // Catch:{ all -> 0x0058 }
            r5 = r0
        L_0x0016:
            sun.nio.ch.FileDispatcher r7 = f890nd     // Catch:{ all -> 0x0056 }
            java.io.FileDescriptor r8 = r11.fdObj     // Catch:{ all -> 0x0056 }
            long r5 = r7.size(r8)     // Catch:{ all -> 0x0056 }
            r7 = -3
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 != 0) goto L_0x002a
            boolean r9 = r11.isOpen()     // Catch:{ all -> 0x0056 }
            if (r9 != 0) goto L_0x0016
        L_0x002a:
            int r9 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r9 >= 0) goto L_0x0047
            boolean r9 = r11.isOpen()     // Catch:{ all -> 0x0056 }
            if (r9 == 0) goto L_0x0047
        L_0x0034:
            sun.nio.ch.FileDispatcher r9 = f890nd     // Catch:{ all -> 0x0056 }
            java.io.FileDescriptor r10 = r11.fdObj     // Catch:{ all -> 0x0056 }
            int r5 = r9.truncate(r10, r12)     // Catch:{ all -> 0x0056 }
            long r5 = (long) r5     // Catch:{ all -> 0x0056 }
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 != 0) goto L_0x0047
            boolean r9 = r11.isOpen()     // Catch:{ all -> 0x0056 }
            if (r9 != 0) goto L_0x0034
        L_0x0047:
            int r12 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
            if (r12 <= 0) goto L_0x004c
            goto L_0x004d
        L_0x004c:
            r3 = r4
        L_0x004d:
            r11.end(r3)     // Catch:{ all -> 0x0064 }
            sun.nio.ch.NativeThreadSet r12 = r11.threads
            r12.remove(r2)
            return r11
        L_0x0056:
            r12 = move-exception
            goto L_0x005a
        L_0x0058:
            r12 = move-exception
            r5 = r0
        L_0x005a:
            int r13 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
            if (r13 <= 0) goto L_0x005f
            goto L_0x0060
        L_0x005f:
            r3 = r4
        L_0x0060:
            r11.end(r3)     // Catch:{ all -> 0x0064 }
            throw r12     // Catch:{ all -> 0x0064 }
        L_0x0064:
            r12 = move-exception
            sun.nio.ch.NativeThreadSet r11 = r11.threads
            r11.remove(r2)
            throw r12
        L_0x006b:
            java.nio.channels.NonWritableChannelException r11 = new java.nio.channels.NonWritableChannelException
            r11.<init>()
            throw r11
        L_0x0071:
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.String r12 = "Negative size"
            r11.<init>((java.lang.String) r12)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.truncate(long):java.nio.channels.AsynchronousFileChannel");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0020  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0031  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:11:0x0021=Splitter:B:11:0x0021, B:20:0x0032=Splitter:B:20:0x0032} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void force(boolean r7) throws java.p026io.IOException {
        /*
            r6 = this;
            sun.nio.ch.NativeThreadSet r0 = r6.threads
            int r0 = r0.add()
            r1 = 1
            r2 = 0
            r6.begin()     // Catch:{ all -> 0x002c }
            r3 = r2
        L_0x000c:
            sun.nio.ch.FileDispatcher r4 = f890nd     // Catch:{ all -> 0x002a }
            java.io.FileDescriptor r5 = r6.fdObj     // Catch:{ all -> 0x002a }
            int r3 = r4.force(r5, r7)     // Catch:{ all -> 0x002a }
            r4 = -3
            if (r3 != r4) goto L_0x001d
            boolean r4 = r6.isOpen()     // Catch:{ all -> 0x002a }
            if (r4 != 0) goto L_0x000c
        L_0x001d:
            if (r3 < 0) goto L_0x0020
            goto L_0x0021
        L_0x0020:
            r1 = r2
        L_0x0021:
            r6.end(r1)     // Catch:{ all -> 0x0036 }
            sun.nio.ch.NativeThreadSet r6 = r6.threads
            r6.remove(r0)
            return
        L_0x002a:
            r7 = move-exception
            goto L_0x002e
        L_0x002c:
            r7 = move-exception
            r3 = r2
        L_0x002e:
            if (r3 < 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r1 = r2
        L_0x0032:
            r6.end(r1)     // Catch:{ all -> 0x0036 }
            throw r7     // Catch:{ all -> 0x0036 }
        L_0x0036:
            r7 = move-exception
            sun.nio.ch.NativeThreadSet r6 = r6.threads
            r6.remove(r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.force(boolean):void");
    }

    /* access modifiers changed from: package-private */
    public <A> Future<FileLock> implLock(long j, long j2, boolean z, A a, CompletionHandler<FileLock, ? super A> completionHandler) {
        CompletionHandler<FileLock, ? super A> completionHandler2 = completionHandler;
        if (z && !this.reading) {
            throw new NonReadableChannelException();
        } else if (z || this.writing) {
            FileLockImpl addToFileLockTable = addToFileLockTable(j, j2, z);
            PendingFuture pendingFuture = null;
            if (addToFileLockTable == null) {
                ClosedChannelException closedChannelException = new ClosedChannelException();
                if (completionHandler2 == null) {
                    return CompletedFuture.withFailure(closedChannelException);
                }
                Invoker.invokeIndirectly(completionHandler2, a, null, (Throwable) closedChannelException, (Executor) this.executor);
                return null;
            }
            A a2 = a;
            if (completionHandler2 == null) {
                pendingFuture = new PendingFuture(this);
            }
            PendingFuture pendingFuture2 = pendingFuture;
            final long j3 = j;
            final long j4 = j2;
            final boolean z2 = z;
            final FileLockImpl fileLockImpl = addToFileLockTable;
            final CompletionHandler<FileLock, ? super A> completionHandler3 = completionHandler;
            final PendingFuture pendingFuture3 = pendingFuture2;
            final A a3 = a;
            try {
                this.executor.execute(new Runnable() {
                    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0044, code lost:
                        r1 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
                        r10.this$0.end();
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0082, code lost:
                        throw r1;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0083, code lost:
                        r1 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0084, code lost:
                        sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.m5590$$Nest$fgetthreads(r10.this$0).remove(r0);
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:33:0x008d, code lost:
                        throw r1;
                     */
                    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:10:0x0037, B:18:0x0047] */
                    /* JADX WARNING: Removed duplicated region for block: B:10:0x0037 A[SYNTHETIC, Splitter:B:10:0x0037] */
                    /* JADX WARNING: Removed duplicated region for block: B:13:0x003e A[SYNTHETIC, Splitter:B:13:0x003e] */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                            r10 = this;
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r0 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                            sun.nio.ch.NativeThreadSet r0 = r0.threads
                            int r0 = r0.add()
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r1 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x0046 }
                            r1.begin()     // Catch:{ IOException -> 0x0046 }
                        L_0x000f:
                            sun.nio.ch.FileDispatcher r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.f890nd     // Catch:{ IOException -> 0x0046 }
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r1 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x0046 }
                            java.io.FileDescriptor r3 = r1.fdObj     // Catch:{ IOException -> 0x0046 }
                            r4 = 1
                            long r5 = r3     // Catch:{ IOException -> 0x0046 }
                            long r7 = r5     // Catch:{ IOException -> 0x0046 }
                            boolean r9 = r7     // Catch:{ IOException -> 0x0046 }
                            int r1 = r2.lock(r3, r4, r5, r7, r9)     // Catch:{ IOException -> 0x0046 }
                            r2 = 2
                            if (r1 != r2) goto L_0x002d
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x0046 }
                            boolean r2 = r2.isOpen()     // Catch:{ IOException -> 0x0046 }
                            if (r2 != 0) goto L_0x000f
                        L_0x002d:
                            if (r1 != 0) goto L_0x003e
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r1 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x0046 }
                            boolean r1 = r1.isOpen()     // Catch:{ IOException -> 0x0046 }
                            if (r1 == 0) goto L_0x003e
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r1 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ all -> 0x0083 }
                            r1.end()     // Catch:{ all -> 0x0083 }
                            r1 = 0
                            goto L_0x0060
                        L_0x003e:
                            java.nio.channels.AsynchronousCloseException r1 = new java.nio.channels.AsynchronousCloseException     // Catch:{ IOException -> 0x0046 }
                            r1.<init>()     // Catch:{ IOException -> 0x0046 }
                            throw r1     // Catch:{ IOException -> 0x0046 }
                        L_0x0044:
                            r1 = move-exception
                            goto L_0x007d
                        L_0x0046:
                            r1 = move-exception
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ all -> 0x0044 }
                            sun.nio.ch.FileLockImpl r3 = r8     // Catch:{ all -> 0x0044 }
                            r2.removeFromFileLockTable(r3)     // Catch:{ all -> 0x0044 }
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ all -> 0x0044 }
                            boolean r2 = r2.isOpen()     // Catch:{ all -> 0x0044 }
                            if (r2 != 0) goto L_0x005b
                            java.nio.channels.AsynchronousCloseException r1 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0044 }
                            r1.<init>()     // Catch:{ all -> 0x0044 }
                        L_0x005b:
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ all -> 0x0083 }
                            r2.end()     // Catch:{ all -> 0x0083 }
                        L_0x0060:
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                            sun.nio.ch.NativeThreadSet r2 = r2.threads
                            r2.remove(r0)
                            java.nio.channels.CompletionHandler r0 = r9
                            if (r0 != 0) goto L_0x0075
                            sun.nio.ch.PendingFuture r0 = r10
                            sun.nio.ch.FileLockImpl r10 = r8
                            r0.setResult(r10, r1)
                            goto L_0x007c
                        L_0x0075:
                            java.lang.Object r2 = r11
                            sun.nio.ch.FileLockImpl r10 = r8
                            sun.nio.p033ch.Invoker.invokeUnchecked(r0, r2, r10, r1)
                        L_0x007c:
                            return
                        L_0x007d:
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ all -> 0x0083 }
                            r2.end()     // Catch:{ all -> 0x0083 }
                            throw r1     // Catch:{ all -> 0x0083 }
                        L_0x0083:
                            r1 = move-exception
                            sun.nio.ch.SimpleAsynchronousFileChannelImpl r10 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                            sun.nio.ch.NativeThreadSet r10 = r10.threads
                            r10.remove(r0)
                            throw r1
                        */
                        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.C47681.run():void");
                    }
                });
                return pendingFuture2;
            } catch (Throwable th) {
                removeFromFileLockTable(addToFileLockTable);
                throw th;
            }
        } else {
            throw new NonWritableChannelException();
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.nio.channels.FileLock tryLock(long r11, long r13, boolean r15) throws java.p026io.IOException {
        /*
            r10 = this;
            if (r15 == 0) goto L_0x000d
            boolean r0 = r10.reading
            if (r0 == 0) goto L_0x0007
            goto L_0x000d
        L_0x0007:
            java.nio.channels.NonReadableChannelException r10 = new java.nio.channels.NonReadableChannelException
            r10.<init>()
            throw r10
        L_0x000d:
            if (r15 != 0) goto L_0x001a
            boolean r0 = r10.writing
            if (r0 == 0) goto L_0x0014
            goto L_0x001a
        L_0x0014:
            java.nio.channels.NonWritableChannelException r10 = new java.nio.channels.NonWritableChannelException
            r10.<init>()
            throw r10
        L_0x001a:
            sun.nio.ch.FileLockImpl r0 = r10.addToFileLockTable(r11, r13, r15)
            if (r0 == 0) goto L_0x007a
            sun.nio.ch.NativeThreadSet r1 = r10.threads
            int r1 = r1.add()
            r10.begin()     // Catch:{ all -> 0x006d }
        L_0x0029:
            sun.nio.ch.FileDispatcher r2 = f890nd     // Catch:{ all -> 0x006d }
            java.io.FileDescriptor r3 = r10.fdObj     // Catch:{ all -> 0x006d }
            r4 = 0
            r5 = r11
            r7 = r13
            r9 = r15
            int r2 = r2.lock(r3, r4, r5, r7, r9)     // Catch:{ all -> 0x006d }
            r3 = 2
            if (r2 != r3) goto L_0x003e
            boolean r4 = r10.isOpen()     // Catch:{ all -> 0x006d }
            if (r4 != 0) goto L_0x0029
        L_0x003e:
            if (r2 != 0) goto L_0x004f
            boolean r11 = r10.isOpen()     // Catch:{ all -> 0x006d }
            if (r11 == 0) goto L_0x004f
            r10.end()
            sun.nio.ch.NativeThreadSet r10 = r10.threads
            r10.remove(r1)
            return r0
        L_0x004f:
            r11 = -1
            if (r2 != r11) goto L_0x005f
            r10.removeFromFileLockTable(r0)
            r10.end()
            sun.nio.ch.NativeThreadSet r10 = r10.threads
            r10.remove(r1)
            r10 = 0
            return r10
        L_0x005f:
            if (r2 != r3) goto L_0x0067
            java.nio.channels.AsynchronousCloseException r11 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x006d }
            r11.<init>()     // Catch:{ all -> 0x006d }
            throw r11     // Catch:{ all -> 0x006d }
        L_0x0067:
            java.lang.AssertionError r11 = new java.lang.AssertionError     // Catch:{ all -> 0x006d }
            r11.<init>()     // Catch:{ all -> 0x006d }
            throw r11     // Catch:{ all -> 0x006d }
        L_0x006d:
            r11 = move-exception
            r10.removeFromFileLockTable(r0)
            r10.end()
            sun.nio.ch.NativeThreadSet r10 = r10.threads
            r10.remove(r1)
            throw r11
        L_0x007a:
            java.nio.channels.ClosedChannelException r10 = new java.nio.channels.ClosedChannelException
            r10.<init>()
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.tryLock(long, long, boolean):java.nio.channels.FileLock");
    }

    /* access modifiers changed from: protected */
    public void implRelease(FileLockImpl fileLockImpl) throws IOException {
        f890nd.release(this.fdObj, fileLockImpl.position(), fileLockImpl.size());
    }

    /* access modifiers changed from: package-private */
    public <A> Future<Integer> implRead(ByteBuffer byteBuffer, long j, A a, CompletionHandler<Integer, ? super A> completionHandler) {
        if (j < 0) {
            throw new IllegalArgumentException("Negative position");
        } else if (!this.reading) {
            throw new NonReadableChannelException();
        } else if (!byteBuffer.isReadOnly()) {
            PendingFuture pendingFuture = null;
            if (!isOpen() || byteBuffer.remaining() == 0) {
                ClosedChannelException closedChannelException = isOpen() ? null : new ClosedChannelException();
                if (completionHandler == null) {
                    return CompletedFuture.withResult(0, closedChannelException);
                }
                Invoker.invokeIndirectly(completionHandler, a, 0, (Throwable) closedChannelException, (Executor) this.executor);
                return null;
            }
            if (completionHandler == null) {
                pendingFuture = new PendingFuture(this);
            }
            final ByteBuffer byteBuffer2 = byteBuffer;
            final long j2 = j;
            final CompletionHandler<Integer, ? super A> completionHandler2 = completionHandler;
            final PendingFuture pendingFuture2 = pendingFuture;
            final A a2 = a;
            this.executor.execute(new Runnable() {
                /* JADX WARNING: Removed duplicated region for block: B:8:0x002d A[Catch:{ IOException -> 0x004e, all -> 0x004c }] */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r7 = this;
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r0 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        sun.nio.ch.NativeThreadSet r0 = r0.threads
                        int r0 = r0.add()
                        r1 = 0
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x004e }
                        r2.begin()     // Catch:{ IOException -> 0x004e }
                    L_0x0010:
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x004e }
                        java.io.FileDescriptor r2 = r2.fdObj     // Catch:{ IOException -> 0x004e }
                        java.nio.ByteBuffer r3 = r4     // Catch:{ IOException -> 0x004e }
                        long r4 = r5     // Catch:{ IOException -> 0x004e }
                        sun.nio.ch.FileDispatcher r6 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.f890nd     // Catch:{ IOException -> 0x004e }
                        int r1 = sun.nio.p033ch.IOUtil.read(r2, r3, r4, r6)     // Catch:{ IOException -> 0x004e }
                        r2 = -3
                        if (r1 != r2) goto L_0x002b
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x004e }
                        boolean r2 = r2.isOpen()     // Catch:{ IOException -> 0x004e }
                        if (r2 != 0) goto L_0x0010
                    L_0x002b:
                        if (r1 >= 0) goto L_0x003c
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x004e }
                        boolean r2 = r2.isOpen()     // Catch:{ IOException -> 0x004e }
                        if (r2 == 0) goto L_0x0036
                        goto L_0x003c
                    L_0x0036:
                        java.nio.channels.AsynchronousCloseException r2 = new java.nio.channels.AsynchronousCloseException     // Catch:{ IOException -> 0x004e }
                        r2.<init>()     // Catch:{ IOException -> 0x004e }
                        throw r2     // Catch:{ IOException -> 0x004e }
                    L_0x003c:
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        r2.end()
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        sun.nio.ch.NativeThreadSet r2 = r2.threads
                        r2.remove(r0)
                        r0 = 0
                        goto L_0x006b
                    L_0x004c:
                        r1 = move-exception
                        goto L_0x0083
                    L_0x004e:
                        r2 = move-exception
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r3 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ all -> 0x004c }
                        boolean r3 = r3.isOpen()     // Catch:{ all -> 0x004c }
                        if (r3 != 0) goto L_0x005c
                        java.nio.channels.AsynchronousCloseException r2 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x004c }
                        r2.<init>()     // Catch:{ all -> 0x004c }
                    L_0x005c:
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r3 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        r3.end()
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r3 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        sun.nio.ch.NativeThreadSet r3 = r3.threads
                        r3.remove(r0)
                        r0 = r2
                    L_0x006b:
                        java.nio.channels.CompletionHandler r2 = r7
                        if (r2 != 0) goto L_0x0079
                        sun.nio.ch.PendingFuture r7 = r8
                        java.lang.Integer r1 = java.lang.Integer.valueOf((int) r1)
                        r7.setResult(r1, r0)
                        goto L_0x0082
                    L_0x0079:
                        java.lang.Object r7 = r9
                        java.lang.Integer r1 = java.lang.Integer.valueOf((int) r1)
                        sun.nio.p033ch.Invoker.invokeUnchecked(r2, r7, r1, r0)
                    L_0x0082:
                        return
                    L_0x0083:
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        r2.end()
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r7 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        sun.nio.ch.NativeThreadSet r7 = r7.threads
                        r7.remove(r0)
                        throw r1
                    */
                    throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.C47692.run():void");
                }
            });
            return pendingFuture;
        } else {
            throw new IllegalArgumentException("Read-only buffer");
        }
    }

    /* access modifiers changed from: package-private */
    public <A> Future<Integer> implWrite(ByteBuffer byteBuffer, long j, A a, CompletionHandler<Integer, ? super A> completionHandler) {
        if (j < 0) {
            throw new IllegalArgumentException("Negative position");
        } else if (this.writing) {
            PendingFuture pendingFuture = null;
            if (!isOpen() || byteBuffer.remaining() == 0) {
                ClosedChannelException closedChannelException = isOpen() ? null : new ClosedChannelException();
                if (completionHandler == null) {
                    return CompletedFuture.withResult(0, closedChannelException);
                }
                Invoker.invokeIndirectly(completionHandler, a, 0, (Throwable) closedChannelException, (Executor) this.executor);
                return null;
            }
            if (completionHandler == null) {
                pendingFuture = new PendingFuture(this);
            }
            final ByteBuffer byteBuffer2 = byteBuffer;
            final long j2 = j;
            final CompletionHandler<Integer, ? super A> completionHandler2 = completionHandler;
            final PendingFuture pendingFuture2 = pendingFuture;
            final A a2 = a;
            this.executor.execute(new Runnable() {
                /* JADX WARNING: Removed duplicated region for block: B:8:0x002d A[Catch:{ IOException -> 0x004e, all -> 0x004c }] */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r7 = this;
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r0 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        sun.nio.ch.NativeThreadSet r0 = r0.threads
                        int r0 = r0.add()
                        r1 = 0
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x004e }
                        r2.begin()     // Catch:{ IOException -> 0x004e }
                    L_0x0010:
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x004e }
                        java.io.FileDescriptor r2 = r2.fdObj     // Catch:{ IOException -> 0x004e }
                        java.nio.ByteBuffer r3 = r4     // Catch:{ IOException -> 0x004e }
                        long r4 = r5     // Catch:{ IOException -> 0x004e }
                        sun.nio.ch.FileDispatcher r6 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.f890nd     // Catch:{ IOException -> 0x004e }
                        int r1 = sun.nio.p033ch.IOUtil.write(r2, r3, r4, r6)     // Catch:{ IOException -> 0x004e }
                        r2 = -3
                        if (r1 != r2) goto L_0x002b
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x004e }
                        boolean r2 = r2.isOpen()     // Catch:{ IOException -> 0x004e }
                        if (r2 != 0) goto L_0x0010
                    L_0x002b:
                        if (r1 >= 0) goto L_0x003c
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ IOException -> 0x004e }
                        boolean r2 = r2.isOpen()     // Catch:{ IOException -> 0x004e }
                        if (r2 == 0) goto L_0x0036
                        goto L_0x003c
                    L_0x0036:
                        java.nio.channels.AsynchronousCloseException r2 = new java.nio.channels.AsynchronousCloseException     // Catch:{ IOException -> 0x004e }
                        r2.<init>()     // Catch:{ IOException -> 0x004e }
                        throw r2     // Catch:{ IOException -> 0x004e }
                    L_0x003c:
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        r2.end()
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        sun.nio.ch.NativeThreadSet r2 = r2.threads
                        r2.remove(r0)
                        r0 = 0
                        goto L_0x006b
                    L_0x004c:
                        r1 = move-exception
                        goto L_0x0083
                    L_0x004e:
                        r2 = move-exception
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r3 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this     // Catch:{ all -> 0x004c }
                        boolean r3 = r3.isOpen()     // Catch:{ all -> 0x004c }
                        if (r3 != 0) goto L_0x005c
                        java.nio.channels.AsynchronousCloseException r2 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x004c }
                        r2.<init>()     // Catch:{ all -> 0x004c }
                    L_0x005c:
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r3 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        r3.end()
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r3 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        sun.nio.ch.NativeThreadSet r3 = r3.threads
                        r3.remove(r0)
                        r0 = r2
                    L_0x006b:
                        java.nio.channels.CompletionHandler r2 = r7
                        if (r2 != 0) goto L_0x0079
                        sun.nio.ch.PendingFuture r7 = r8
                        java.lang.Integer r1 = java.lang.Integer.valueOf((int) r1)
                        r7.setResult(r1, r0)
                        goto L_0x0082
                    L_0x0079:
                        java.lang.Object r7 = r9
                        java.lang.Integer r1 = java.lang.Integer.valueOf((int) r1)
                        sun.nio.p033ch.Invoker.invokeUnchecked(r2, r7, r1, r0)
                    L_0x0082:
                        return
                    L_0x0083:
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r2 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        r2.end()
                        sun.nio.ch.SimpleAsynchronousFileChannelImpl r7 = sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.this
                        sun.nio.ch.NativeThreadSet r7 = r7.threads
                        r7.remove(r0)
                        throw r1
                    */
                    throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SimpleAsynchronousFileChannelImpl.C47703.run():void");
                }
            });
            return pendingFuture;
        } else {
            throw new NonWritableChannelException();
        }
    }
}
