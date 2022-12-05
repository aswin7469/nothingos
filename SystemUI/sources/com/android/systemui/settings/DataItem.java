package com.android.systemui.settings;

import com.android.systemui.settings.UserTracker;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: UserTrackerImpl.kt */
/* loaded from: classes.dex */
public final class DataItem {
    @NotNull
    private final WeakReference<UserTracker.Callback> callback;
    @NotNull
    private final Executor executor;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DataItem)) {
            return false;
        }
        DataItem dataItem = (DataItem) obj;
        return Intrinsics.areEqual(this.callback, dataItem.callback) && Intrinsics.areEqual(this.executor, dataItem.executor);
    }

    public int hashCode() {
        return (this.callback.hashCode() * 31) + this.executor.hashCode();
    }

    @NotNull
    public String toString() {
        return "DataItem(callback=" + this.callback + ", executor=" + this.executor + ')';
    }

    public DataItem(@NotNull WeakReference<UserTracker.Callback> callback, @NotNull Executor executor) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.callback = callback;
        this.executor = executor;
    }

    @NotNull
    public final WeakReference<UserTracker.Callback> getCallback() {
        return this.callback;
    }

    @NotNull
    public final Executor getExecutor() {
        return this.executor;
    }

    public final boolean sameOrEmpty(@NotNull UserTracker.Callback other) {
        Intrinsics.checkNotNullParameter(other, "other");
        UserTracker.Callback callback = this.callback.get();
        if (callback == null) {
            return true;
        }
        return callback.equals(other);
    }
}
