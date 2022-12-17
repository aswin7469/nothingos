package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public interface ListeningExecutorService extends ExecutorService {
    ListenableFuture<?> submit(Runnable runnable);

    <T> ListenableFuture<T> submit(Callable<T> callable);
}
