package com.android.systemui.statusbar.notification.collection;

import java.util.List;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u001e\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u0006R\u0018\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u0006ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000eÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStore;", "", "activeNotifCount", "Lcom/android/systemui/statusbar/notification/collection/NotifLiveData;", "", "getActiveNotifCount", "()Lcom/android/systemui/statusbar/notification/collection/NotifLiveData;", "activeNotifList", "", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getActiveNotifList", "hasActiveNotifs", "", "getHasActiveNotifs", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifLiveDataStore.kt */
public interface NotifLiveDataStore {
    NotifLiveData<Integer> getActiveNotifCount();

    NotifLiveData<List<NotificationEntry>> getActiveNotifList();

    NotifLiveData<Boolean> getHasActiveNotifs();
}
