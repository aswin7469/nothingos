package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: StatusBarLocationPublisher.kt */
/* loaded from: classes.dex */
public final class StatusBarLocationPublisher implements CallbackController<StatusBarMarginUpdatedListener> {
    @NotNull
    private final Set<WeakReference<StatusBarMarginUpdatedListener>> listeners = new LinkedHashSet();
    private int marginLeft;
    private int marginRight;

    public final int getMarginLeft() {
        return this.marginLeft;
    }

    public final int getMarginRight() {
        return this.marginRight;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(@NotNull StatusBarMarginUpdatedListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.add(new WeakReference<>(listener));
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(@NotNull StatusBarMarginUpdatedListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        WeakReference<StatusBarMarginUpdatedListener> weakReference = null;
        for (WeakReference<StatusBarMarginUpdatedListener> weakReference2 : this.listeners) {
            if (Intrinsics.areEqual(weakReference2.get(), listener)) {
                weakReference = weakReference2;
            }
        }
        if (weakReference != null) {
            this.listeners.remove(weakReference);
        }
    }

    public final void updateStatusBarMargin(int i, int i2) {
        this.marginLeft = i;
        this.marginRight = i2;
        notifyListeners();
    }

    private final void notifyListeners() {
        List<WeakReference> list;
        synchronized (this) {
            list = CollectionsKt.toList(this.listeners);
            Unit unit = Unit.INSTANCE;
        }
        for (WeakReference weakReference : list) {
            if (weakReference.get() == null) {
                this.listeners.remove(weakReference);
            }
            StatusBarMarginUpdatedListener statusBarMarginUpdatedListener = (StatusBarMarginUpdatedListener) weakReference.get();
            if (statusBarMarginUpdatedListener != null) {
                statusBarMarginUpdatedListener.onStatusBarMarginUpdated(getMarginLeft(), getMarginRight());
            }
        }
    }
}
