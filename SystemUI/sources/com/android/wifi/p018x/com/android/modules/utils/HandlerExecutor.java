package com.android.wifi.p018x.com.android.modules.utils;

import android.os.Handler;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/* renamed from: com.android.wifi.x.com.android.modules.utils.HandlerExecutor */
public class HandlerExecutor implements Executor {
    private final Handler mHandler;

    public HandlerExecutor(Handler handler) {
        this.mHandler = (Handler) Objects.requireNonNull(handler);
    }

    public void execute(Runnable runnable) {
        if (!this.mHandler.post(runnable)) {
            throw new RejectedExecutionException(this.mHandler + " is shutting down");
        }
    }
}