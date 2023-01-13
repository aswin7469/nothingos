package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b&\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\u0006H&J\u0010\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u000bH&J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\rH&J\b\u0010\u000e\u001a\u00020\u0006H&J\u0010\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u000bH&J\b\u0010\u0010\u001a\u00020\u0011H&J\b\u0010\u0012\u001a\u00020\u0011H&¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifStabilityManager;", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/Pluggable;", "name", "", "(Ljava/lang/String;)V", "isEntryReorderingAllowed", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "isEveryChangeAllowed", "isGroupChangeAllowed", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "isGroupPruneAllowed", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "isPipelineRunAllowed", "isSectionChangeAllowed", "onBeginRun", "", "onEntryReorderSuppressed", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifStabilityManager.kt */
public abstract class NotifStabilityManager extends Pluggable<NotifStabilityManager> {
    public abstract boolean isEntryReorderingAllowed(ListEntry listEntry);

    public abstract boolean isEveryChangeAllowed();

    public abstract boolean isGroupChangeAllowed(NotificationEntry notificationEntry);

    public abstract boolean isGroupPruneAllowed(GroupEntry groupEntry);

    public abstract boolean isPipelineRunAllowed();

    public abstract boolean isSectionChangeAllowed(NotificationEntry notificationEntry);

    public abstract void onBeginRun();

    public abstract void onEntryReorderSuppressed();

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected NotifStabilityManager(String str) {
        super(str);
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
    }
}
