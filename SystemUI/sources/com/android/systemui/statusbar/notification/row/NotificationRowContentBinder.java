package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface NotificationRowContentBinder {
    public static final int FLAG_CONTENT_VIEW_ALL = 15;
    public static final int FLAG_CONTENT_VIEW_CONTRACTED = 1;
    public static final int FLAG_CONTENT_VIEW_EXPANDED = 2;
    public static final int FLAG_CONTENT_VIEW_HEADS_UP = 4;
    public static final int FLAG_CONTENT_VIEW_PUBLIC = 8;

    public static class BindParams {
        public boolean isLowPriority;
        public boolean usesIncreasedHeadsUpHeight;
        public boolean usesIncreasedHeight;
    }

    public interface InflationCallback {
        void handleInflationException(NotificationEntry notificationEntry, Exception exc);

        void onAsyncInflationFinished(NotificationEntry notificationEntry);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface InflationFlag {
    }

    void bindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i, BindParams bindParams, boolean z, InflationCallback inflationCallback);

    void cancelBind(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow);

    void unbindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i);
}
