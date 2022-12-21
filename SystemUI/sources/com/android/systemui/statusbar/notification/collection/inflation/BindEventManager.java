package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.ListenerSet;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001:\u0001\fB\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005J\u000e\u0010\u000b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005R\u001a\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManager;", "", "()V", "listeners", "Lcom/android/systemui/util/ListenerSet;", "Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManager$Listener;", "getListeners", "()Lcom/android/systemui/util/ListenerSet;", "addListener", "", "listener", "removeListener", "Listener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BindEventManager.kt */
public class BindEventManager {
    private final ListenerSet<Listener> listeners = new ListenerSet<>();

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bæ\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManager$Listener;", "", "onViewBound", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: BindEventManager.kt */
    public interface Listener {
        void onViewBound(NotificationEntry notificationEntry);
    }

    /* access modifiers changed from: protected */
    public final ListenerSet<Listener> getListeners() {
        return this.listeners;
    }

    public final boolean addListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.addIfAbsent(listener);
    }

    public final boolean removeListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.remove(listener);
    }
}
