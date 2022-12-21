package com.android.systemui.statusbar.notification.collection.provider;

import android.util.ArraySet;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.util.ListenerSet;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005J\u000e\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005J\b\u0010\u0012\u001a\u00020\u000fH\u0002J\u000e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\u0002\n\u0000R$\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0007@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\rX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/provider/VisualStabilityProvider;", "", "()V", "allListeners", "Lcom/android/systemui/util/ListenerSet;", "Lcom/android/systemui/statusbar/notification/collection/provider/OnReorderingAllowedListener;", "value", "", "isReorderingAllowed", "()Z", "setReorderingAllowed", "(Z)V", "temporaryListeners", "Landroid/util/ArraySet;", "addPersistentReorderingAllowedListener", "", "listener", "addTemporaryReorderingAllowedListener", "notifyReorderingAllowed", "removeReorderingAllowedListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: VisualStabilityProvider.kt */
public final class VisualStabilityProvider {
    private final ListenerSet<OnReorderingAllowedListener> allListeners = new ListenerSet<>();
    private boolean isReorderingAllowed = true;
    private final ArraySet<OnReorderingAllowedListener> temporaryListeners = new ArraySet<>();

    public final boolean isReorderingAllowed() {
        return this.isReorderingAllowed;
    }

    public final void setReorderingAllowed(boolean z) {
        if (this.isReorderingAllowed != z) {
            this.isReorderingAllowed = z;
            if (z) {
                notifyReorderingAllowed();
            }
        }
    }

    private final void notifyReorderingAllowed() {
        for (OnReorderingAllowedListener onReorderingAllowedListener : this.allListeners) {
            if (this.temporaryListeners.remove(onReorderingAllowedListener)) {
                this.allListeners.remove(onReorderingAllowedListener);
            }
            onReorderingAllowedListener.onReorderingAllowed();
        }
    }

    public final void addPersistentReorderingAllowedListener(OnReorderingAllowedListener onReorderingAllowedListener) {
        Intrinsics.checkNotNullParameter(onReorderingAllowedListener, "listener");
        this.temporaryListeners.remove(onReorderingAllowedListener);
        this.allListeners.addIfAbsent(onReorderingAllowedListener);
    }

    public final void addTemporaryReorderingAllowedListener(OnReorderingAllowedListener onReorderingAllowedListener) {
        Intrinsics.checkNotNullParameter(onReorderingAllowedListener, "listener");
        if (this.allListeners.addIfAbsent(onReorderingAllowedListener)) {
            this.temporaryListeners.add(onReorderingAllowedListener);
        }
    }

    public final void removeReorderingAllowedListener(OnReorderingAllowedListener onReorderingAllowedListener) {
        Intrinsics.checkNotNullParameter(onReorderingAllowedListener, "listener");
        this.temporaryListeners.remove(onReorderingAllowedListener);
        this.allListeners.remove(onReorderingAllowedListener);
    }
}
