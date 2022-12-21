package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u000bH&J\b\u0010\f\u001a\u00020\rH\u0016J\u0016\u0010\u000e\u001a\u00020\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0012À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NotifViewRenderer;", "", "getGroupController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifGroupController;", "group", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "getRowController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifRowController;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getStackController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStackController;", "onDispatchComplete", "", "onRenderList", "notifList", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifViewRenderer.kt */
public interface NotifViewRenderer {
    NotifGroupController getGroupController(GroupEntry groupEntry);

    NotifRowController getRowController(NotificationEntry notificationEntry);

    NotifStackController getStackController();

    void onDispatchComplete() {
    }

    void onRenderList(List<? extends ListEntry> list);
}
