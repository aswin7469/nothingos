package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.phone.NotifPanelEvents;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.p026io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

@SysUISingleton
public class VisualStabilityCoordinator implements Coordinator, Dumpable, NotifPanelEvents.Listener {
    protected static final long ALLOW_SECTION_CHANGE_TIMEOUT = 500;
    private final DelayableExecutor mDelayableExecutor;
    /* access modifiers changed from: private */
    public Map<String, Runnable> mEntriesThatCanChangeSection = new HashMap();
    /* access modifiers changed from: private */
    public final Set<String> mEntriesWithSuppressedSectionChange = new HashSet();
    /* access modifiers changed from: private */
    public final HeadsUpManager mHeadsUpManager;
    /* access modifiers changed from: private */
    public boolean mIsSuppressingEntryReorder = false;
    /* access modifiers changed from: private */
    public boolean mIsSuppressingGroupChange = false;
    /* access modifiers changed from: private */
    public boolean mIsSuppressingPipelineRun = false;
    private boolean mNotifPanelCollapsing;
    private final NotifPanelEvents mNotifPanelEvents;
    private boolean mNotifPanelLaunchingActivity;
    private final NotifStabilityManager mNotifStabilityManager = new NotifStabilityManager("VisualStabilityCoordinator") {
        public void onBeginRun() {
            boolean unused = VisualStabilityCoordinator.this.mIsSuppressingPipelineRun = false;
            boolean unused2 = VisualStabilityCoordinator.this.mIsSuppressingGroupChange = false;
            VisualStabilityCoordinator.this.mEntriesWithSuppressedSectionChange.clear();
            boolean unused3 = VisualStabilityCoordinator.this.mIsSuppressingEntryReorder = false;
        }

        public boolean isPipelineRunAllowed() {
            VisualStabilityCoordinator visualStabilityCoordinator = VisualStabilityCoordinator.this;
            VisualStabilityCoordinator.access$076(visualStabilityCoordinator, visualStabilityCoordinator.mPipelineRunAllowed ^ true ? 1 : 0);
            return VisualStabilityCoordinator.this.mPipelineRunAllowed;
        }

        public boolean isGroupChangeAllowed(NotificationEntry notificationEntry) {
            boolean z = VisualStabilityCoordinator.this.mReorderingAllowed || VisualStabilityCoordinator.this.mHeadsUpManager.isAlerting(notificationEntry.getKey());
            VisualStabilityCoordinator.access$176(VisualStabilityCoordinator.this, z ^ true ? 1 : 0);
            return z;
        }

        public boolean isGroupPruneAllowed(GroupEntry groupEntry) {
            boolean access$500 = VisualStabilityCoordinator.this.mReorderingAllowed;
            VisualStabilityCoordinator.access$176(VisualStabilityCoordinator.this, access$500 ^ true ? 1 : 0);
            return access$500;
        }

        public boolean isSectionChangeAllowed(NotificationEntry notificationEntry) {
            boolean z = VisualStabilityCoordinator.this.mReorderingAllowed || VisualStabilityCoordinator.this.mHeadsUpManager.isAlerting(notificationEntry.getKey()) || VisualStabilityCoordinator.this.mEntriesThatCanChangeSection.containsKey(notificationEntry.getKey());
            if (!z) {
                VisualStabilityCoordinator.this.mEntriesWithSuppressedSectionChange.add(notificationEntry.getKey());
            }
            return z;
        }

        public boolean isEntryReorderingAllowed(ListEntry listEntry) {
            return VisualStabilityCoordinator.this.mReorderingAllowed;
        }

        public boolean isEveryChangeAllowed() {
            return VisualStabilityCoordinator.this.mReorderingAllowed;
        }

        public void onEntryReorderSuppressed() {
            boolean unused = VisualStabilityCoordinator.this.mIsSuppressingEntryReorder = true;
        }
    };
    /* access modifiers changed from: private */
    public boolean mPanelExpanded;
    /* access modifiers changed from: private */
    public boolean mPipelineRunAllowed;
    /* access modifiers changed from: private */
    public boolean mPulsing;
    /* access modifiers changed from: private */
    public boolean mReorderingAllowed;
    /* access modifiers changed from: private */
    public boolean mScreenOn;
    private final StatusBarStateController mStatusBarStateController;
    final StatusBarStateController.StateListener mStatusBarStateControllerListener = new StatusBarStateController.StateListener() {
        public void onPulsingChanged(boolean z) {
            boolean unused = VisualStabilityCoordinator.this.mPulsing = z;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }

        public void onExpandedChanged(boolean z) {
            boolean unused = VisualStabilityCoordinator.this.mPanelExpanded = z;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }
    };
    private final VisualStabilityProvider mVisualStabilityProvider;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    final WakefulnessLifecycle.Observer mWakefulnessObserver = new WakefulnessLifecycle.Observer() {
        public void onFinishedGoingToSleep() {
            boolean unused = VisualStabilityCoordinator.this.mScreenOn = false;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }

        public void onStartedWakingUp() {
            boolean unused = VisualStabilityCoordinator.this.mScreenOn = true;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }
    };

    /* JADX WARNING: type inference failed for: r2v2, types: [boolean, byte] */
    static /* synthetic */ boolean access$076(VisualStabilityCoordinator visualStabilityCoordinator, int i) {
        ? r2 = (byte) (i | visualStabilityCoordinator.mIsSuppressingPipelineRun);
        visualStabilityCoordinator.mIsSuppressingPipelineRun = r2;
        return r2;
    }

    /* JADX WARNING: type inference failed for: r2v2, types: [boolean, byte] */
    static /* synthetic */ boolean access$176(VisualStabilityCoordinator visualStabilityCoordinator, int i) {
        ? r2 = (byte) (i | visualStabilityCoordinator.mIsSuppressingGroupChange);
        visualStabilityCoordinator.mIsSuppressingGroupChange = r2;
        return r2;
    }

    @Inject
    public VisualStabilityCoordinator(DelayableExecutor delayableExecutor, DumpManager dumpManager, HeadsUpManager headsUpManager, NotifPanelEvents notifPanelEvents, StatusBarStateController statusBarStateController, VisualStabilityProvider visualStabilityProvider, WakefulnessLifecycle wakefulnessLifecycle) {
        this.mHeadsUpManager = headsUpManager;
        this.mVisualStabilityProvider = visualStabilityProvider;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mStatusBarStateController = statusBarStateController;
        this.mDelayableExecutor = delayableExecutor;
        this.mNotifPanelEvents = notifPanelEvents;
        dumpManager.registerDumpable(this);
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
        boolean z = true;
        if (!(this.mWakefulnessLifecycle.getWakefulness() == 2 || this.mWakefulnessLifecycle.getWakefulness() == 1)) {
            z = false;
        }
        this.mScreenOn = z;
        this.mStatusBarStateController.addCallback(this.mStatusBarStateControllerListener);
        this.mPulsing = this.mStatusBarStateController.isPulsing();
        this.mNotifPanelEvents.registerListener(this);
        notifPipeline.setVisualStabilityManager(this.mNotifStabilityManager);
    }

    /* access modifiers changed from: private */
    public void updateAllowedStates() {
        this.mPipelineRunAllowed = !isPanelCollapsingOrLaunchingActivity();
        boolean isReorderingAllowed = isReorderingAllowed();
        this.mReorderingAllowed = isReorderingAllowed;
        if ((this.mPipelineRunAllowed && this.mIsSuppressingPipelineRun) || (isReorderingAllowed && (this.mIsSuppressingGroupChange || isSuppressingSectionChange() || this.mIsSuppressingEntryReorder))) {
            this.mNotifStabilityManager.invalidateList();
        }
        this.mVisualStabilityProvider.setReorderingAllowed(this.mReorderingAllowed);
    }

    private boolean isSuppressingSectionChange() {
        return !this.mEntriesWithSuppressedSectionChange.isEmpty();
    }

    private boolean isPanelCollapsingOrLaunchingActivity() {
        return this.mNotifPanelCollapsing || this.mNotifPanelLaunchingActivity;
    }

    private boolean isReorderingAllowed() {
        return (!this.mScreenOn || !this.mPanelExpanded) && !this.mPulsing;
    }

    public void temporarilyAllowSectionChanges(NotificationEntry notificationEntry, long j) {
        String key = notificationEntry.getKey();
        boolean isSectionChangeAllowed = this.mNotifStabilityManager.isSectionChangeAllowed(notificationEntry);
        if (this.mEntriesThatCanChangeSection.containsKey(key)) {
            this.mEntriesThatCanChangeSection.get(key).run();
        }
        this.mEntriesThatCanChangeSection.put(key, this.mDelayableExecutor.executeAtTime(new VisualStabilityCoordinator$$ExternalSyntheticLambda0(this, key), j + 500));
        if (!isSectionChangeAllowed) {
            this.mNotifStabilityManager.invalidateList();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$temporarilyAllowSectionChanges$0$com-android-systemui-statusbar-notification-collection-coordinator-VisualStabilityCoordinator */
    public /* synthetic */ void mo40313x23f5a63b(String str) {
        this.mEntriesThatCanChangeSection.remove(str);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("pipelineRunAllowed: " + this.mPipelineRunAllowed);
        printWriter.println("  notifPanelCollapsing: " + this.mNotifPanelCollapsing);
        printWriter.println("  launchingNotifActivity: " + this.mNotifPanelLaunchingActivity);
        printWriter.println("reorderingAllowed: " + this.mReorderingAllowed);
        printWriter.println("  screenOn: " + this.mScreenOn);
        printWriter.println("  panelExpanded: " + this.mPanelExpanded);
        printWriter.println("  pulsing: " + this.mPulsing);
        printWriter.println("isSuppressingPipelineRun: " + this.mIsSuppressingPipelineRun);
        printWriter.println("isSuppressingGroupChange: " + this.mIsSuppressingGroupChange);
        printWriter.println("isSuppressingEntryReorder: " + this.mIsSuppressingEntryReorder);
        printWriter.println("entriesWithSuppressedSectionChange: " + this.mEntriesWithSuppressedSectionChange.size());
        for (String str : this.mEntriesWithSuppressedSectionChange) {
            printWriter.println("  " + str);
        }
        printWriter.println("entriesThatCanChangeSection: " + this.mEntriesThatCanChangeSection.size());
        for (String str2 : this.mEntriesThatCanChangeSection.keySet()) {
            printWriter.println("  " + str2);
        }
    }

    public void onPanelCollapsingChanged(boolean z) {
        this.mNotifPanelCollapsing = z;
        updateAllowedStates();
    }

    public void onLaunchingActivityChanged(boolean z) {
        this.mNotifPanelLaunchingActivity = z;
        updateAllowedStates();
    }
}
