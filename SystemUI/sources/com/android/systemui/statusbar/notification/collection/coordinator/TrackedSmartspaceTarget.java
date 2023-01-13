package com.android.systemui.statusbar.notification.collection.coordinator;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/TrackedSmartspaceTarget;", "", "key", "", "(Ljava/lang/String;)V", "alertExceptionExpires", "", "getAlertExceptionExpires", "()J", "setAlertExceptionExpires", "(J)V", "cancelTimeoutRunnable", "Ljava/lang/Runnable;", "getCancelTimeoutRunnable", "()Ljava/lang/Runnable;", "setCancelTimeoutRunnable", "(Ljava/lang/Runnable;)V", "getKey", "()Ljava/lang/String;", "shouldFilter", "", "getShouldFilter", "()Z", "setShouldFilter", "(Z)V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartspaceDedupingCoordinator.kt */
final class TrackedSmartspaceTarget {
    private long alertExceptionExpires;
    private Runnable cancelTimeoutRunnable;
    private final String key;
    private boolean shouldFilter;

    public TrackedSmartspaceTarget(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.key = str;
    }

    public final String getKey() {
        return this.key;
    }

    public final Runnable getCancelTimeoutRunnable() {
        return this.cancelTimeoutRunnable;
    }

    public final void setCancelTimeoutRunnable(Runnable runnable) {
        this.cancelTimeoutRunnable = runnable;
    }

    public final long getAlertExceptionExpires() {
        return this.alertExceptionExpires;
    }

    public final void setAlertExceptionExpires(long j) {
        this.alertExceptionExpires = j;
    }

    public final boolean getShouldFilter() {
        return this.shouldFilter;
    }

    public final void setShouldFilter(boolean z) {
        this.shouldFilter = z;
    }
}
