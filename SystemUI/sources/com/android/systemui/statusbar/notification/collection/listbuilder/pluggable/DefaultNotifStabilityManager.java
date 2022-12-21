package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0004H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u0004H\u0016J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\tH\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u000fH\u0016¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/DefaultNotifStabilityManager;", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifStabilityManager;", "()V", "isEntryReorderingAllowed", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "isEveryChangeAllowed", "isGroupChangeAllowed", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "isGroupPruneAllowed", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "isPipelineRunAllowed", "isSectionChangeAllowed", "onBeginRun", "", "onEntryReorderSuppressed", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifStabilityManager.kt */
public final class DefaultNotifStabilityManager extends NotifStabilityManager {
    public static final DefaultNotifStabilityManager INSTANCE = new DefaultNotifStabilityManager();

    public boolean isEntryReorderingAllowed(ListEntry listEntry) {
        Intrinsics.checkNotNullParameter(listEntry, "entry");
        return true;
    }

    public boolean isEveryChangeAllowed() {
        return true;
    }

    public boolean isGroupChangeAllowed(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return true;
    }

    public boolean isGroupPruneAllowed(GroupEntry groupEntry) {
        Intrinsics.checkNotNullParameter(groupEntry, "entry");
        return true;
    }

    public boolean isPipelineRunAllowed() {
        return true;
    }

    public boolean isSectionChangeAllowed(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return true;
    }

    public void onBeginRun() {
    }

    public void onEntryReorderSuppressed() {
    }

    private DefaultNotifStabilityManager() {
        super("DefaultNotifStabilityManager");
    }
}
