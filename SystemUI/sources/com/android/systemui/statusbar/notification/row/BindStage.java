package com.android.systemui.statusbar.notification.row;

import android.util.ArrayMap;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class BindStage<Params> extends BindRequester {
    private Map<NotificationEntry, Params> mContentParams = new ArrayMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface StageCallback {
        void onStageFinished(NotificationEntry notificationEntry);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void abortStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void executeStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, StageCallback stageCallback);

    /* renamed from: newStageParams */
    protected abstract Params mo1156newStageParams();

    public final Params getStageParams(NotificationEntry notificationEntry) {
        Params params = this.mContentParams.get(notificationEntry);
        if (params == null) {
            Log.wtf("BindStage", String.format("Entry does not have any stage parameters. key: %s", notificationEntry.getKey()));
            return mo1156newStageParams();
        }
        return params;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void createStageParams(NotificationEntry notificationEntry) {
        this.mContentParams.put(notificationEntry, mo1156newStageParams());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void deleteStageParams(NotificationEntry notificationEntry) {
        this.mContentParams.remove(notificationEntry);
    }
}
