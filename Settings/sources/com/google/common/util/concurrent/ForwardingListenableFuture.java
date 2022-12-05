package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
@CanIgnoreReturnValue
/* loaded from: classes2.dex */
public abstract class ForwardingListenableFuture<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
    @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    protected abstract ListenableFuture<? extends V> mo848delegate();

    protected ForwardingListenableFuture() {
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable runnable, Executor executor) {
        mo848delegate().addListener(runnable, executor);
    }

    /* loaded from: classes2.dex */
    public static abstract class SimpleForwardingListenableFuture<V> extends ForwardingListenableFuture<V> {
        private final ListenableFuture<V> delegate;

        /* JADX INFO: Access modifiers changed from: protected */
        public SimpleForwardingListenableFuture(ListenableFuture<V> listenableFuture) {
            this.delegate = (ListenableFuture) Preconditions.checkNotNull(listenableFuture);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        /* renamed from: delegate */
        public final ListenableFuture<V> mo848delegate() {
            return this.delegate;
        }
    }
}
