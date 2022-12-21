package com.android.systemui.statusbar.notification.row;

import android.util.ArrayMap;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Map;

public abstract class BindStage<Params> extends BindRequester {
    private static final String TAG = "BindStage";
    private Map<NotificationEntry, Params> mContentParams = new ArrayMap();

    interface StageCallback {
        void onStageFinished(NotificationEntry notificationEntry);
    }

    /* access modifiers changed from: protected */
    public abstract void abortStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow);

    /* access modifiers changed from: protected */
    public abstract void executeStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, StageCallback stageCallback);

    /* access modifiers changed from: protected */
    public abstract Params newStageParams();

    public final Params getStageParams(NotificationEntry notificationEntry) {
        Params params = this.mContentParams.get(notificationEntry);
        if (params != null) {
            return params;
        }
        Log.wtf(TAG, String.format("Entry does not have any stage parameters. key: %s", notificationEntry.getKey()));
        return newStageParams();
    }

    /* access modifiers changed from: package-private */
    public final void createStageParams(NotificationEntry notificationEntry) {
        this.mContentParams.put(notificationEntry, newStageParams());
    }

    /* access modifiers changed from: package-private */
    public final void deleteStageParams(NotificationEntry notificationEntry) {
        this.mContentParams.remove(notificationEntry);
    }
}
