package sun.nio.p033ch;

import java.nio.channels.AsynchronousChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* renamed from: sun.nio.ch.PendingFuture */
final class PendingFuture<V, A> implements Future<V> {
    private static final CancellationException CANCELLED = new CancellationException();
    private final A attachment;
    private final AsynchronousChannel channel;
    private volatile Object context;
    private volatile Throwable exc;
    private final CompletionHandler<V, ? super A> handler;
    private volatile boolean haveResult;
    private CountDownLatch latch;
    private volatile V result;
    private Future<?> timeoutTask;

    PendingFuture(AsynchronousChannel asynchronousChannel, CompletionHandler<V, ? super A> completionHandler, A a, Object obj) {
        this.channel = asynchronousChannel;
        this.handler = completionHandler;
        this.attachment = a;
        this.context = obj;
    }

    PendingFuture(AsynchronousChannel asynchronousChannel, CompletionHandler<V, ? super A> completionHandler, A a) {
        this.channel = asynchronousChannel;
        this.handler = completionHandler;
        this.attachment = a;
    }

    PendingFuture(AsynchronousChannel asynchronousChannel) {
        this(asynchronousChannel, (CompletionHandler) null, (Object) null);
    }

    PendingFuture(AsynchronousChannel asynchronousChannel, Object obj) {
        this(asynchronousChannel, (CompletionHandler) null, (Object) null, obj);
    }

    /* access modifiers changed from: package-private */
    public AsynchronousChannel channel() {
        return this.channel;
    }

    /* access modifiers changed from: package-private */
    public CompletionHandler<V, ? super A> handler() {
        return this.handler;
    }

    /* access modifiers changed from: package-private */
    public A attachment() {
        return this.attachment;
    }

    /* access modifiers changed from: package-private */
    public void setContext(Object obj) {
        this.context = obj;
    }

    /* access modifiers changed from: package-private */
    public Object getContext() {
        return this.context;
    }

    /* access modifiers changed from: package-private */
    public void setTimeoutTask(Future<?> future) {
        synchronized (this) {
            if (this.haveResult) {
                future.cancel(false);
            } else {
                this.timeoutTask = future;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0015, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean prepareForWait() {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.haveResult     // Catch:{ all -> 0x0016 }
            if (r0 == 0) goto L_0x0008
            monitor-exit(r2)     // Catch:{ all -> 0x0016 }
            r2 = 0
            return r2
        L_0x0008:
            java.util.concurrent.CountDownLatch r0 = r2.latch     // Catch:{ all -> 0x0016 }
            r1 = 1
            if (r0 != 0) goto L_0x0014
            java.util.concurrent.CountDownLatch r0 = new java.util.concurrent.CountDownLatch     // Catch:{ all -> 0x0016 }
            r0.<init>(r1)     // Catch:{ all -> 0x0016 }
            r2.latch = r0     // Catch:{ all -> 0x0016 }
        L_0x0014:
            monitor-exit(r2)     // Catch:{ all -> 0x0016 }
            return r1
        L_0x0016:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0016 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.PendingFuture.prepareForWait():boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setResult(V r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.haveResult     // Catch:{ all -> 0x001d }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r1)     // Catch:{ all -> 0x001d }
            return
        L_0x0007:
            r1.result = r2     // Catch:{ all -> 0x001d }
            r2 = 1
            r1.haveResult = r2     // Catch:{ all -> 0x001d }
            java.util.concurrent.Future<?> r2 = r1.timeoutTask     // Catch:{ all -> 0x001d }
            if (r2 == 0) goto L_0x0014
            r0 = 0
            r2.cancel(r0)     // Catch:{ all -> 0x001d }
        L_0x0014:
            java.util.concurrent.CountDownLatch r2 = r1.latch     // Catch:{ all -> 0x001d }
            if (r2 == 0) goto L_0x001b
            r2.countDown()     // Catch:{ all -> 0x001d }
        L_0x001b:
            monitor-exit(r1)     // Catch:{ all -> 0x001d }
            return
        L_0x001d:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x001d }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.PendingFuture.setResult(java.lang.Object):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setFailure(java.lang.Throwable r2) {
        /*
            r1 = this;
            boolean r0 = r2 instanceof java.p026io.IOException
            if (r0 != 0) goto L_0x000e
            boolean r0 = r2 instanceof java.lang.SecurityException
            if (r0 != 0) goto L_0x000e
            java.io.IOException r0 = new java.io.IOException
            r0.<init>((java.lang.Throwable) r2)
            r2 = r0
        L_0x000e:
            monitor-enter(r1)
            boolean r0 = r1.haveResult     // Catch:{ all -> 0x002b }
            if (r0 == 0) goto L_0x0015
            monitor-exit(r1)     // Catch:{ all -> 0x002b }
            return
        L_0x0015:
            r1.exc = r2     // Catch:{ all -> 0x002b }
            r2 = 1
            r1.haveResult = r2     // Catch:{ all -> 0x002b }
            java.util.concurrent.Future<?> r2 = r1.timeoutTask     // Catch:{ all -> 0x002b }
            if (r2 == 0) goto L_0x0022
            r0 = 0
            r2.cancel(r0)     // Catch:{ all -> 0x002b }
        L_0x0022:
            java.util.concurrent.CountDownLatch r2 = r1.latch     // Catch:{ all -> 0x002b }
            if (r2 == 0) goto L_0x0029
            r2.countDown()     // Catch:{ all -> 0x002b }
        L_0x0029:
            monitor-exit(r1)     // Catch:{ all -> 0x002b }
            return
        L_0x002b:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x002b }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.PendingFuture.setFailure(java.lang.Throwable):void");
    }

    /* access modifiers changed from: package-private */
    public void setResult(V v, Throwable th) {
        if (th == null) {
            setResult(v);
        } else {
            setFailure(th);
        }
    }

    public V get() throws ExecutionException, InterruptedException {
        if (!this.haveResult && prepareForWait()) {
            this.latch.await();
        }
        if (this.exc == null) {
            return this.result;
        }
        if (this.exc == CANCELLED) {
            throw new CancellationException();
        }
        throw new ExecutionException(this.exc);
    }

    public V get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.haveResult && prepareForWait() && !this.latch.await(j, timeUnit)) {
            throw new TimeoutException();
        } else if (this.exc == null) {
            return this.result;
        } else {
            if (this.exc == CANCELLED) {
                throw new CancellationException();
            }
            throw new ExecutionException(this.exc);
        }
    }

    /* access modifiers changed from: package-private */
    public Throwable exception() {
        if (this.exc != CANCELLED) {
            return this.exc;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public V value() {
        return this.result;
    }

    public boolean isCancelled() {
        return this.exc == CANCELLED;
    }

    public boolean isDone() {
        return this.haveResult;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0028, code lost:
        if (r4 == false) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        channel().close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean cancel(boolean r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.haveResult     // Catch:{ all -> 0x0039 }
            r1 = 0
            if (r0 == 0) goto L_0x0008
            monitor-exit(r3)     // Catch:{ all -> 0x0039 }
            return r1
        L_0x0008:
            java.nio.channels.AsynchronousChannel r0 = r3.channel()     // Catch:{ all -> 0x0039 }
            boolean r0 = r0 instanceof sun.nio.p033ch.Cancellable     // Catch:{ all -> 0x0039 }
            if (r0 == 0) goto L_0x0019
            java.nio.channels.AsynchronousChannel r0 = r3.channel()     // Catch:{ all -> 0x0039 }
            sun.nio.ch.Cancellable r0 = (sun.nio.p033ch.Cancellable) r0     // Catch:{ all -> 0x0039 }
            r0.onCancel(r3)     // Catch:{ all -> 0x0039 }
        L_0x0019:
            java.util.concurrent.CancellationException r0 = CANCELLED     // Catch:{ all -> 0x0039 }
            r3.exc = r0     // Catch:{ all -> 0x0039 }
            r0 = 1
            r3.haveResult = r0     // Catch:{ all -> 0x0039 }
            java.util.concurrent.Future<?> r2 = r3.timeoutTask     // Catch:{ all -> 0x0039 }
            if (r2 == 0) goto L_0x0027
            r2.cancel(r1)     // Catch:{ all -> 0x0039 }
        L_0x0027:
            monitor-exit(r3)     // Catch:{ all -> 0x0039 }
            if (r4 == 0) goto L_0x0031
            java.nio.channels.AsynchronousChannel r4 = r3.channel()     // Catch:{ IOException -> 0x0031 }
            r4.close()     // Catch:{ IOException -> 0x0031 }
        L_0x0031:
            java.util.concurrent.CountDownLatch r3 = r3.latch
            if (r3 == 0) goto L_0x0038
            r3.countDown()
        L_0x0038:
            return r0
        L_0x0039:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0039 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.PendingFuture.cancel(boolean):boolean");
    }
}
