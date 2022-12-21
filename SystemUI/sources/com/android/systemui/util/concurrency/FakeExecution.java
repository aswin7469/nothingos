package com.android.systemui.util.concurrency;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\u0004H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/util/concurrency/FakeExecution;", "Lcom/android/systemui/util/concurrency/Execution;", "()V", "simulateMainThread", "", "getSimulateMainThread", "()Z", "setSimulateMainThread", "(Z)V", "assertIsMainThread", "", "isMainThread", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: Execution.kt */
public final class FakeExecution implements Execution {
    private boolean simulateMainThread = true;

    public final boolean getSimulateMainThread() {
        return this.simulateMainThread;
    }

    public final void setSimulateMainThread(boolean z) {
        this.simulateMainThread = z;
    }

    public void assertIsMainThread() {
        if (!this.simulateMainThread) {
            throw new IllegalStateException("should be called from the main thread");
        }
    }

    public boolean isMainThread() {
        return this.simulateMainThread;
    }
}
