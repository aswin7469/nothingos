package sun.nio.p033ch;

import java.p026io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* renamed from: sun.nio.ch.CompletedFuture */
final class CompletedFuture<V> implements Future<V> {
    private final Throwable exc;
    private final V result;

    public boolean cancel(boolean z) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return true;
    }

    private CompletedFuture(V v, Throwable th) {
        this.result = v;
        this.exc = th;
    }

    static <V> CompletedFuture<V> withResult(V v) {
        return new CompletedFuture<>(v, (Throwable) null);
    }

    static <V> CompletedFuture<V> withFailure(Throwable th) {
        if (!(th instanceof IOException) && !(th instanceof SecurityException)) {
            th = new IOException(th);
        }
        return new CompletedFuture<>((Object) null, th);
    }

    static <V> CompletedFuture<V> withResult(V v, Throwable th) {
        if (th == null) {
            return withResult(v);
        }
        return withFailure(th);
    }

    public V get() throws ExecutionException {
        Throwable th = this.exc;
        if (th == null) {
            return this.result;
        }
        throw new ExecutionException(th);
    }

    public V get(long j, TimeUnit timeUnit) throws ExecutionException {
        timeUnit.getClass();
        Throwable th = this.exc;
        if (th == null) {
            return this.result;
        }
        throw new ExecutionException(th);
    }
}
