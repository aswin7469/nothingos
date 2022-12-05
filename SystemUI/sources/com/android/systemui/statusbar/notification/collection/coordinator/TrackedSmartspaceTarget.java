package com.android.systemui.statusbar.notification.collection.coordinator;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SmartspaceDedupingCoordinator.kt */
/* loaded from: classes.dex */
public final class TrackedSmartspaceTarget {
    private long alertExceptionExpires;
    @Nullable
    private Runnable cancelTimeoutRunnable;
    @NotNull
    private final String key;
    private boolean shouldFilter;

    public TrackedSmartspaceTarget(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        this.key = key;
    }

    @NotNull
    public final String getKey() {
        return this.key;
    }

    @Nullable
    public final Runnable getCancelTimeoutRunnable() {
        return this.cancelTimeoutRunnable;
    }

    public final void setCancelTimeoutRunnable(@Nullable Runnable runnable) {
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
