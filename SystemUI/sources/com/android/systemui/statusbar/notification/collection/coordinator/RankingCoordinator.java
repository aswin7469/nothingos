package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

@CoordinatorScope
public class RankingCoordinator implements Coordinator {
    public static final boolean SHOW_ALL_SECTIONS = false;
    private final NodeController mAlertingHeaderController;
    private final NotifSectioner mAlertingNotifSectioner = new NotifSectioner("Alerting", 5) {
        public NodeController getHeaderNodeController() {
            return null;
        }

        public boolean isInSection(ListEntry listEntry) {
            return RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry);
        }
    };
    /* access modifiers changed from: private */
    public final NotifFilter mDndVisualEffectsFilter = new NotifFilter("DndSuppressingVisualEffects") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            if (RankingCoordinator.this.mStatusBarStateController.isDozing() && notificationEntry.shouldSuppressAmbient()) {
                return true;
            }
            if (RankingCoordinator.this.mStatusBarStateController.isDozing() || !notificationEntry.shouldSuppressNotificationList()) {
                return false;
            }
            return true;
        }
    };
    /* access modifiers changed from: private */
    public boolean mHasMinimizedEntries;
    /* access modifiers changed from: private */
    public boolean mHasSilentEntries;
    /* access modifiers changed from: private */
    public final HighPriorityProvider mHighPriorityProvider;
    private final NotifSectioner mMinimizedNotifSectioner = new NotifSectioner("Minimized", 6) {
        public boolean isInSection(ListEntry listEntry) {
            return !RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry) && listEntry.getRepresentativeEntry().isAmbient();
        }

        public NodeController getHeaderNodeController() {
            return RankingCoordinator.this.mSilentNodeController;
        }

        public void onEntriesUpdated(List<ListEntry> list) {
            int i = 0;
            boolean unused = RankingCoordinator.this.mHasMinimizedEntries = false;
            while (true) {
                if (i >= list.size()) {
                    break;
                } else if (list.get(i).getRepresentativeEntry().getSbn().isClearable()) {
                    boolean unused2 = RankingCoordinator.this.mHasMinimizedEntries = true;
                    break;
                } else {
                    i++;
                }
            }
            RankingCoordinator.this.mSilentHeaderController.setClearSectionEnabled(RankingCoordinator.this.mHasMinimizedEntries | RankingCoordinator.this.mHasSilentEntries);
        }
    };
    private final SectionClassifier mSectionClassifier;
    /* access modifiers changed from: private */
    public final SectionHeaderController mSilentHeaderController;
    /* access modifiers changed from: private */
    public final NodeController mSilentNodeController;
    private final NotifSectioner mSilentNotifSectioner = new NotifSectioner("Silent", 6) {
        public boolean isInSection(ListEntry listEntry) {
            return !RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry) && !listEntry.getRepresentativeEntry().isAmbient();
        }

        public NodeController getHeaderNodeController() {
            return RankingCoordinator.this.mSilentNodeController;
        }

        public void onEntriesUpdated(List<ListEntry> list) {
            int i = 0;
            boolean unused = RankingCoordinator.this.mHasSilentEntries = false;
            while (true) {
                if (i >= list.size()) {
                    break;
                } else if (list.get(i).getRepresentativeEntry().getSbn().isClearable()) {
                    boolean unused2 = RankingCoordinator.this.mHasSilentEntries = true;
                    break;
                } else {
                    i++;
                }
            }
            RankingCoordinator.this.mSilentHeaderController.setClearSectionEnabled(RankingCoordinator.this.mHasMinimizedEntries | RankingCoordinator.this.mHasSilentEntries);
        }
    };
    private final StatusBarStateController.StateListener mStatusBarStateCallback = new StatusBarStateController.StateListener() {
        public void onDozingChanged(boolean z) {
            RankingCoordinator.this.mDndVisualEffectsFilter.invalidateList();
        }
    };
    /* access modifiers changed from: private */
    public final StatusBarStateController mStatusBarStateController;
    private final NotifFilter mSuspendedFilter = new NotifFilter("IsSuspendedFilter") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return notificationEntry.getRanking().isSuspended();
        }
    };

    @Inject
    public RankingCoordinator(StatusBarStateController statusBarStateController, HighPriorityProvider highPriorityProvider, SectionClassifier sectionClassifier, NodeController nodeController, SectionHeaderController sectionHeaderController, NodeController nodeController2) {
        this.mStatusBarStateController = statusBarStateController;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mSectionClassifier = sectionClassifier;
        this.mAlertingHeaderController = nodeController;
        this.mSilentNodeController = nodeController2;
        this.mSilentHeaderController = sectionHeaderController;
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mStatusBarStateController.addCallback(this.mStatusBarStateCallback);
        this.mSectionClassifier.setMinimizedSections(Collections.singleton(this.mMinimizedNotifSectioner));
        notifPipeline.addPreGroupFilter(this.mSuspendedFilter);
        notifPipeline.addPreGroupFilter(this.mDndVisualEffectsFilter);
    }

    public NotifSectioner getAlertingSectioner() {
        return this.mAlertingNotifSectioner;
    }

    public NotifSectioner getSilentSectioner() {
        return this.mSilentNotifSectioner;
    }

    public NotifSectioner getMinimizedSectioner() {
        return this.mMinimizedNotifSectioner;
    }
}
