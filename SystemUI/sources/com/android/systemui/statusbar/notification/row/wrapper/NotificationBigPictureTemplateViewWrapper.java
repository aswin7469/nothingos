package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.service.notification.StatusBarNotification;
import android.view.View;
import androidx.core.app.NotificationCompat;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

public class NotificationBigPictureTemplateViewWrapper extends NotificationTemplateViewWrapper {
    protected NotificationBigPictureTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
    }

    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        super.onContentUpdated(expandableNotificationRow);
        updateImageTag(expandableNotificationRow.getEntry().getSbn());
    }

    private void updateImageTag(StatusBarNotification statusBarNotification) {
        Icon icon = (Icon) statusBarNotification.getNotification().extras.getParcelable(NotificationCompat.EXTRA_LARGE_ICON_BIG, Icon.class);
        if (icon != null) {
            this.mRightIcon.setTag(C1894R.C1898id.image_icon_tag, icon);
            this.mLeftIcon.setTag(C1894R.C1898id.image_icon_tag, icon);
            return;
        }
        this.mRightIcon.setTag(C1894R.C1898id.image_icon_tag, getLargeIcon(statusBarNotification.getNotification()));
    }
}
