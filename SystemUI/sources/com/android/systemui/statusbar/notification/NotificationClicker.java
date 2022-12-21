package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.systemui.DejankUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.Optional;
import javax.inject.Inject;

public final class NotificationClicker implements View.OnClickListener {
    private static final String TAG = "NotificationClicker";
    private final Optional<Bubbles> mBubblesOptional;
    private final Optional<CentralSurfaces> mCentralSurfacesOptional;
    private final NotificationClickerLogger mLogger;
    /* access modifiers changed from: private */
    public final NotificationActivityStarter mNotificationActivityStarter;
    private ExpandableNotificationRow.OnDragSuccessListener mOnDragSuccessListener;

    private NotificationClicker(NotificationClickerLogger notificationClickerLogger, Optional<CentralSurfaces> optional, Optional<Bubbles> optional2, NotificationActivityStarter notificationActivityStarter) {
        this.mOnDragSuccessListener = new ExpandableNotificationRow.OnDragSuccessListener() {
            public void onDragSuccess(NotificationEntry notificationEntry) {
                NotificationClicker.this.mNotificationActivityStarter.onDragSuccess(notificationEntry);
            }
        };
        this.mLogger = notificationClickerLogger;
        this.mCentralSurfacesOptional = optional;
        this.mBubblesOptional = optional2;
        this.mNotificationActivityStarter = notificationActivityStarter;
    }

    public void onClick(View view) {
        if (!(view instanceof ExpandableNotificationRow)) {
            Log.e(TAG, "NotificationClicker called on a view that is not a notification row.");
            return;
        }
        this.mCentralSurfacesOptional.ifPresent(new NotificationClicker$$ExternalSyntheticLambda0(view));
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        NotificationEntry entry = expandableNotificationRow.getEntry();
        this.mLogger.logOnClick(entry);
        if (isMenuVisible(expandableNotificationRow)) {
            this.mLogger.logMenuVisible(entry);
            expandableNotificationRow.animateResetTranslation();
        } else if (expandableNotificationRow.isChildInGroup() && isMenuVisible(expandableNotificationRow.getNotificationParent())) {
            this.mLogger.logParentMenuVisible(entry);
            expandableNotificationRow.getNotificationParent().animateResetTranslation();
        } else if (expandableNotificationRow.isSummaryWithChildren() && expandableNotificationRow.areChildrenExpanded()) {
            this.mLogger.logChildrenExpanded(entry);
        } else if (expandableNotificationRow.areGutsExposed()) {
            this.mLogger.logGutsExposed(entry);
        } else {
            expandableNotificationRow.setJustClicked(true);
            DejankUtils.postAfterTraversal(new NotificationClicker$$ExternalSyntheticLambda1(expandableNotificationRow));
            if (!expandableNotificationRow.getEntry().isBubble() && this.mBubblesOptional.isPresent()) {
                this.mBubblesOptional.get().collapseStack();
            }
            this.mNotificationActivityStarter.onNotificationClicked(entry, expandableNotificationRow);
        }
    }

    private boolean isMenuVisible(ExpandableNotificationRow expandableNotificationRow) {
        return expandableNotificationRow.getProvider() != null && expandableNotificationRow.getProvider().isMenuVisible();
    }

    public void register(ExpandableNotificationRow expandableNotificationRow, StatusBarNotification statusBarNotification) {
        Notification notification = statusBarNotification.getNotification();
        if (notification.contentIntent == null && notification.fullScreenIntent == null && !expandableNotificationRow.getEntry().isBubble()) {
            expandableNotificationRow.setOnClickListener((View.OnClickListener) null);
            expandableNotificationRow.setOnDragSuccessListener((ExpandableNotificationRow.OnDragSuccessListener) null);
            return;
        }
        expandableNotificationRow.setOnClickListener(this);
        expandableNotificationRow.setOnDragSuccessListener(this.mOnDragSuccessListener);
    }

    public static class Builder {
        private final NotificationClickerLogger mLogger;

        @Inject
        public Builder(NotificationClickerLogger notificationClickerLogger) {
            this.mLogger = notificationClickerLogger;
        }

        public NotificationClicker build(Optional<CentralSurfaces> optional, Optional<Bubbles> optional2, NotificationActivityStarter notificationActivityStarter) {
            return new NotificationClicker(this.mLogger, optional, optional2, notificationActivityStarter);
        }
    }
}
