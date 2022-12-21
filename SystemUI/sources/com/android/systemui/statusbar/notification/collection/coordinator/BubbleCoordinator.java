package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.wmshell.BubblesManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;

@CoordinatorScope
public class BubbleCoordinator implements Coordinator {
    private static final String TAG = "BubbleCoordinator";
    /* access modifiers changed from: private */
    public final Optional<BubblesManager> mBubblesManagerOptional;
    /* access modifiers changed from: private */
    public final Optional<Bubbles> mBubblesOptional;
    /* access modifiers changed from: private */
    public final NotifDismissInterceptor mDismissInterceptor = new NotifDismissInterceptor() {
        public String getName() {
            return BubbleCoordinator.TAG;
        }

        public void setCallback(NotifDismissInterceptor.OnEndDismissInterception onEndDismissInterception) {
            NotifDismissInterceptor.OnEndDismissInterception unused = BubbleCoordinator.this.mOnEndDismissInterception = onEndDismissInterception;
        }

        public boolean shouldInterceptDismissal(NotificationEntry notificationEntry) {
            if (!BubbleCoordinator.this.mBubblesManagerOptional.isPresent() || !((BubblesManager) BubbleCoordinator.this.mBubblesManagerOptional.get()).handleDismissalInterception(notificationEntry)) {
                BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
                return false;
            }
            BubbleCoordinator.this.mInterceptedDismissalEntries.add(notificationEntry.getKey());
            return true;
        }

        public void cancelDismissInterception(NotificationEntry notificationEntry) {
            BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
        }
    };
    /* access modifiers changed from: private */
    public final Set<String> mInterceptedDismissalEntries = new HashSet();
    private final BubblesManager.NotifCallback mNotifCallback = new BubblesManager.NotifCallback() {
        public void maybeCancelSummary(NotificationEntry notificationEntry) {
        }

        public void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i) {
            NotificationEntry entry;
            if (!BubbleCoordinator.this.mNotifPipeline.isNewPipelineEnabled() && (entry = BubbleCoordinator.this.mNotifPipeline.getEntry(notificationEntry.getKey())) != null) {
                notificationEntry = entry;
            }
            if (BubbleCoordinator.this.isInterceptingDismissal(notificationEntry)) {
                BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
                BubbleCoordinator.this.mOnEndDismissInterception.onEndDismissInterception(BubbleCoordinator.this.mDismissInterceptor, notificationEntry, dismissedByUserStats);
            } else if (BubbleCoordinator.this.mNotifPipeline.getEntry(notificationEntry.getKey()) != null) {
                BubbleCoordinator.this.mNotifCollection.dismissNotification(notificationEntry, dismissedByUserStats);
            }
        }

        public void invalidateNotifications(String str) {
            BubbleCoordinator.this.mNotifFilter.invalidateList();
        }
    };
    /* access modifiers changed from: private */
    public final NotifCollection mNotifCollection;
    /* access modifiers changed from: private */
    public final NotifFilter mNotifFilter = new NotifFilter(TAG) {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return BubbleCoordinator.this.mBubblesOptional.isPresent() && ((Bubbles) BubbleCoordinator.this.mBubblesOptional.get()).isBubbleNotificationSuppressedFromShade(notificationEntry.getKey(), notificationEntry.getSbn().getGroupKey());
        }
    };
    /* access modifiers changed from: private */
    public NotifPipeline mNotifPipeline;
    /* access modifiers changed from: private */
    public NotifDismissInterceptor.OnEndDismissInterception mOnEndDismissInterception;

    @Inject
    public BubbleCoordinator(Optional<BubblesManager> optional, Optional<Bubbles> optional2, NotifCollection notifCollection) {
        this.mBubblesManagerOptional = optional;
        this.mBubblesOptional = optional2;
        this.mNotifCollection = notifCollection;
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mNotifPipeline = notifPipeline;
        notifPipeline.addNotificationDismissInterceptor(this.mDismissInterceptor);
        this.mNotifPipeline.addPreGroupFilter(this.mNotifFilter);
        this.mBubblesManagerOptional.ifPresent(new BubbleCoordinator$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$attach$0$com-android-systemui-statusbar-notification-collection-coordinator-BubbleCoordinator */
    public /* synthetic */ void mo40179x34e6e404(BubblesManager bubblesManager) {
        bubblesManager.addNotifCallback(this.mNotifCallback);
    }

    /* access modifiers changed from: private */
    public boolean isInterceptingDismissal(NotificationEntry notificationEntry) {
        return this.mInterceptedDismissalEntries.contains(notificationEntry.getKey());
    }
}
