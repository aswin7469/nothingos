package com.android.systemui.util.concurrency;

import android.os.Looper;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\tH\u0016R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, mo64987d2 = {"Lcom/android/systemui/util/concurrency/ExecutionImpl;", "Lcom/android/systemui/util/concurrency/Execution;", "()V", "mainLooper", "Landroid/os/Looper;", "kotlin.jvm.PlatformType", "assertIsMainThread", "", "isMainThread", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: Execution.kt */
public final class ExecutionImpl implements Execution {
    private final Looper mainLooper = Looper.getMainLooper();

    public void assertIsMainThread() {
        if (!this.mainLooper.isCurrentThread()) {
            throw new IllegalStateException("should be called from the main thread. Main thread name=" + this.mainLooper.getThread().getName() + " Thread.currentThread()=" + Thread.currentThread().getName());
        }
    }

    public boolean isMainThread() {
        return this.mainLooper.isCurrentThread();
    }
}
