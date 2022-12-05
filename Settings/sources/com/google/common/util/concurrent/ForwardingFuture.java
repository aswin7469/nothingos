package com.google.common.util.concurrent;

import com.google.common.collect.ForwardingObject;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
@CanIgnoreReturnValue
/* loaded from: classes2.dex */
public abstract class ForwardingFuture<V> extends ForwardingObject implements Future<V> {
    @Override // com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    protected abstract Future<? extends V> mo848delegate();

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z) {
        return mo848delegate().cancel(z);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return mo848delegate().isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return mo848delegate().isDone();
    }

    @Override // java.util.concurrent.Future
    public V get() throws InterruptedException, ExecutionException {
        return mo848delegate().get();
    }

    @Override // java.util.concurrent.Future
    public V get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return mo848delegate().get(j, timeUnit);
    }
}
