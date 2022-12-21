package com.android.systemui.statusbar.phone;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001:\u0001\u0007J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/NotifPanelEvents;", "", "registerListener", "", "listener", "Lcom/android/systemui/statusbar/phone/NotifPanelEvents$Listener;", "unregisterListener", "Listener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifPanelEvents.kt */
public interface NotifPanelEvents {

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/NotifPanelEvents$Listener;", "", "onLaunchingActivityChanged", "", "isLaunchingActivity", "", "onPanelCollapsingChanged", "isCollapsing", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NotifPanelEvents.kt */
    public interface Listener {
        void onLaunchingActivityChanged(boolean z);

        void onPanelCollapsingChanged(boolean z);
    }

    void registerListener(Listener listener);

    void unregisterListener(Listener listener);
}