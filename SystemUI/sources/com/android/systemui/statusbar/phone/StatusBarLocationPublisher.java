package com.android.systemui.statusbar.phone;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0003J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0016J\b\u0010\u0011\u001a\u00020\u000fH\u0002J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0016J\u0016\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\bR\u001a\u0010\u0004\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u00060\u0005X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001e\u0010\f\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000b¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarLocationPublisher;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/statusbar/phone/StatusBarMarginUpdatedListener;", "()V", "listeners", "", "Ljava/lang/ref/WeakReference;", "<set-?>", "", "marginLeft", "getMarginLeft", "()I", "marginRight", "getMarginRight", "addCallback", "", "listener", "notifyListeners", "removeCallback", "updateStatusBarMargin", "left", "right", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarLocationPublisher.kt */
public final class StatusBarLocationPublisher implements CallbackController<StatusBarMarginUpdatedListener> {
    private final Set<WeakReference<StatusBarMarginUpdatedListener>> listeners = new LinkedHashSet();
    private int marginLeft;
    private int marginRight;

    public final int getMarginLeft() {
        return this.marginLeft;
    }

    public final int getMarginRight() {
        return this.marginRight;
    }

    public void addCallback(StatusBarMarginUpdatedListener statusBarMarginUpdatedListener) {
        Intrinsics.checkNotNullParameter(statusBarMarginUpdatedListener, "listener");
        this.listeners.add(new WeakReference(statusBarMarginUpdatedListener));
    }

    public void removeCallback(StatusBarMarginUpdatedListener statusBarMarginUpdatedListener) {
        Intrinsics.checkNotNullParameter(statusBarMarginUpdatedListener, "listener");
        WeakReference weakReference = null;
        for (WeakReference next : this.listeners) {
            if (Intrinsics.areEqual(next.get(), (Object) statusBarMarginUpdatedListener)) {
                weakReference = next;
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
                statusBarMarginUpdatedListener.onStatusBarMarginUpdated(this.marginLeft, this.marginRight);
            }
        }
    }
}
