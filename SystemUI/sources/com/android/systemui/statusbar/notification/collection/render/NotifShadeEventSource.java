package com.android.systemui.statusbar.notification.collection.render;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NotifShadeEventSource;", "", "setNotifRemovedByUserCallback", "", "callback", "Ljava/lang/Runnable;", "setShadeEmptiedCallback", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifShadeEventSource.kt */
public interface NotifShadeEventSource {
    void setNotifRemovedByUserCallback(Runnable runnable);

    void setShadeEmptiedCallback(Runnable runnable);
}
