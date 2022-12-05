package com.google.common.util.concurrent;

import com.google.errorprone.annotations.DoNotMock;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
@DoNotMock("Use TestingExecutors.sameThreadScheduledExecutor, or wrap a real Executor from java.util.concurrent.Executors with MoreExecutors.listeningDecorator")
/* loaded from: classes2.dex */
public interface ListeningExecutorService extends ExecutorService {
    @Override // java.util.concurrent.ExecutorService, com.google.common.util.concurrent.ListeningExecutorService
    /* renamed from: submit */
    ListenableFuture<?> mo845submit(Runnable runnable);

    @Override // java.util.concurrent.ExecutorService, com.google.common.util.concurrent.ListeningExecutorService
    /* renamed from: submit */
    <T> ListenableFuture<T> mo847submit(Callable<T> callable);
}
