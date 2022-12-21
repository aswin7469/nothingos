package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class DynamicChildBindController {
    private static final int CHILD_BIND_CUTOFF = 9;
    private static final int EXTRA_VIEW_BUFFER_COUNT = 1;
    private final int mChildBindCutoff;
    private final RowContentBindStage mStage;

    @Inject
    public DynamicChildBindController(RowContentBindStage rowContentBindStage) {
        this(rowContentBindStage, 9);
    }

    DynamicChildBindController(RowContentBindStage rowContentBindStage, int i) {
        this.mStage = rowContentBindStage;
        this.mChildBindCutoff = i;
    }

    public void updateContentViews(Map<NotificationEntry, List<NotificationEntry>> map) {
        for (NotificationEntry next : map.keySet()) {
            List list = map.get(next);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    NotificationEntry notificationEntry = (NotificationEntry) list.get(i);
                    if (i >= this.mChildBindCutoff) {
                        if (hasContent(notificationEntry)) {
                            freeContent(notificationEntry);
                        }
                    } else if (!hasContent(notificationEntry)) {
                        bindContent(notificationEntry);
                    }
                }
            } else if (!hasContent(next)) {
                bindContent(next);
            }
        }
    }

    private boolean hasContent(NotificationEntry notificationEntry) {
        ExpandableNotificationRow row = notificationEntry.getRow();
        return (row.getPrivateLayout().getContractedChild() == null && row.getPrivateLayout().getExpandedChild() == null) ? false : true;
    }

    private void freeContent(NotificationEntry notificationEntry) {
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mStage.getStageParams(notificationEntry);
        rowContentBindParams.markContentViewsFreeable(1);
        rowContentBindParams.markContentViewsFreeable(2);
        this.mStage.requestRebind(notificationEntry, (NotifBindPipeline.BindCallback) null);
    }

    private void bindContent(NotificationEntry notificationEntry) {
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mStage.getStageParams(notificationEntry);
        rowContentBindParams.requireContentViews(1);
        rowContentBindParams.requireContentViews(2);
        this.mStage.requestRebind(notificationEntry, (NotifBindPipeline.BindCallback) null);
    }
}
