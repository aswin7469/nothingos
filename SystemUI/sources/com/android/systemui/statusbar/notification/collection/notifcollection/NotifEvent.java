package com.android.systemui.statusbar.notification.collection.notifcollection;

import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public abstract class NotifEvent {
    public /* synthetic */ NotifEvent(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener);

    private NotifEvent() {
    }

    public final void dispatchTo(@NotNull List<? extends NotifCollectionListener> listeners) {
        Intrinsics.checkNotNullParameter(listeners, "listeners");
        int size = listeners.size() - 1;
        if (size >= 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                dispatchToListener(listeners.get(i));
                if (i2 > size) {
                    return;
                }
                i = i2;
            }
        }
    }
}
