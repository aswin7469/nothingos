package com.android.systemui.statusbar.notification.row;

import android.util.Log;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.FeedbackIcon;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.NotifViewController;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.dagger.AppName;
import com.android.systemui.statusbar.notification.row.dagger.NotificationKey;
import com.android.systemui.statusbar.notification.row.dagger.NotificationRowScope;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wifi.p018x.com.android.internal.util.Protocol;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;

@NotificationRowScope
public class ExpandableNotificationRowController implements NotifViewController {
    private static final String TAG = "NotifRowController";
    private final ActivatableNotificationViewController mActivatableNotificationViewController;
    private final boolean mAllowLongPress;
    private final String mAppName;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    /* access modifiers changed from: private */
    public final SystemClock mClock;
    private final ExpandableNotificationRowDragController mDragController;
    private final ExpandableNotificationRow.ExpansionLogger mExpansionLogger = new ExpandableNotificationRowController$$ExternalSyntheticLambda0(this);
    private final FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    private final FeatureFlags mFeatureFlags;
    private final GroupExpansionManager mGroupExpansionManager;
    private final GroupMembershipManager mGroupMembershipManager;
    private final HeadsUpManager mHeadsUpManager;
    private final KeyguardBypassController mKeyguardBypassController;
    private final NotificationListContainer mListContainer;
    private final NotificationMediaManager mMediaManager;
    private final MetricsLogger mMetricsLogger;
    private final NotificationGutsManager mNotificationGutsManager;
    private final String mNotificationKey;
    private final NotificationLogger mNotificationLogger;
    private final ExpandableNotificationRow.OnExpandClickListener mOnExpandClickListener;
    private final ExpandableNotificationRow.CoordinateOnClickListener mOnFeedbackClickListener;
    private final OnUserInteractionCallback mOnUserInteractionCallback;
    private final PeopleNotificationIdentifier mPeopleNotificationIdentifier;
    /* access modifiers changed from: private */
    public final PluginManager mPluginManager;
    private final RemoteInputViewSubcomponent.Factory mRemoteInputViewSubcomponentFactory;
    private final RowContentBindStage mRowContentBindStage;
    private final SmartReplyConstants mSmartReplyConstants;
    private final SmartReplyController mSmartReplyController;
    /* access modifiers changed from: private */
    public final StatusBarStateController mStatusBarStateController;
    /* access modifiers changed from: private */
    public final StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onStateChanged(int i) {
            ExpandableNotificationRow access$100 = ExpandableNotificationRowController.this.mView;
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            access$100.setOnKeyguard(z);
        }
    };
    /* access modifiers changed from: private */
    public final ExpandableNotificationRow mView;

    public void onViewAdded() {
    }

    public void onViewMoved() {
    }

    public void onViewRemoved() {
    }

    @Inject
    public ExpandableNotificationRowController(ExpandableNotificationRow expandableNotificationRow, ActivatableNotificationViewController activatableNotificationViewController, RemoteInputViewSubcomponent.Factory factory, MetricsLogger metricsLogger, NotificationListContainer notificationListContainer, NotificationMediaManager notificationMediaManager, SmartReplyConstants smartReplyConstants, SmartReplyController smartReplyController, PluginManager pluginManager, SystemClock systemClock, @AppName String str, @NotificationKey String str2, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, GroupExpansionManager groupExpansionManager, RowContentBindStage rowContentBindStage, NotificationLogger notificationLogger, HeadsUpManager headsUpManager, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, StatusBarStateController statusBarStateController, NotificationGutsManager notificationGutsManager, @Named("allow_notif_longpress") boolean z, OnUserInteractionCallback onUserInteractionCallback, FalsingManager falsingManager, FalsingCollector falsingCollector, FeatureFlags featureFlags, PeopleNotificationIdentifier peopleNotificationIdentifier, Optional<BubblesManager> optional, ExpandableNotificationRowDragController expandableNotificationRowDragController) {
        NotificationGutsManager notificationGutsManager2 = notificationGutsManager;
        this.mView = expandableNotificationRow;
        this.mListContainer = notificationListContainer;
        this.mRemoteInputViewSubcomponentFactory = factory;
        this.mActivatableNotificationViewController = activatableNotificationViewController;
        this.mMediaManager = notificationMediaManager;
        this.mPluginManager = pluginManager;
        this.mClock = systemClock;
        this.mAppName = str;
        this.mNotificationKey = str2;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mGroupMembershipManager = groupMembershipManager;
        this.mGroupExpansionManager = groupExpansionManager;
        this.mRowContentBindStage = rowContentBindStage;
        this.mNotificationLogger = notificationLogger;
        this.mHeadsUpManager = headsUpManager;
        this.mOnExpandClickListener = onExpandClickListener;
        this.mStatusBarStateController = statusBarStateController;
        this.mNotificationGutsManager = notificationGutsManager2;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mFalsingManager = falsingManager;
        Objects.requireNonNull(notificationGutsManager);
        this.mOnFeedbackClickListener = new ExpandableNotificationRowController$$ExternalSyntheticLambda1(notificationGutsManager2);
        this.mAllowLongPress = z;
        this.mFalsingCollector = falsingCollector;
        this.mFeatureFlags = featureFlags;
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier;
        this.mBubblesManagerOptional = optional;
        this.mDragController = expandableNotificationRowDragController;
        this.mMetricsLogger = metricsLogger;
        this.mSmartReplyConstants = smartReplyConstants;
        this.mSmartReplyController = smartReplyController;
    }

    public void init(NotificationEntry notificationEntry) {
        this.mActivatableNotificationViewController.init();
        ExpandableNotificationRow expandableNotificationRow = this.mView;
        ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
        expandableNotificationRow2.initialize(notificationEntry, this.mRemoteInputViewSubcomponentFactory, this.mAppName, this.mNotificationKey, this.mExpansionLogger, this.mKeyguardBypassController, this.mGroupMembershipManager, this.mGroupExpansionManager, this.mHeadsUpManager, this.mRowContentBindStage, this.mOnExpandClickListener, this.mMediaManager, this.mOnFeedbackClickListener, this.mFalsingManager, this.mFalsingCollector, this.mStatusBarStateController, this.mPeopleNotificationIdentifier, this.mOnUserInteractionCallback, this.mBubblesManagerOptional, this.mNotificationGutsManager, this.mMetricsLogger, this.mSmartReplyConstants, this.mSmartReplyController);
        this.mView.setDescendantFocusability(Protocol.BASE_NSD_MANAGER);
        if (this.mAllowLongPress) {
            if (this.mFeatureFlags.isEnabled(Flags.NOTIFICATION_DRAG_TO_CONTENTS)) {
                this.mView.setDragController(this.mDragController);
            }
            this.mView.setLongPressListener(new ExpandableNotificationRowController$$ExternalSyntheticLambda2(this));
        }
        if (NotificationRemoteInputManager.ENABLE_REMOTE_INPUT) {
            this.mView.setDescendantFocusability(131072);
        }
        this.mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View view) {
                ExpandableNotificationRowController.this.mView.getEntry().setInitializationTime(ExpandableNotificationRowController.this.mClock.elapsedRealtime());
                boolean z = false;
                ExpandableNotificationRowController.this.mPluginManager.addPluginListener(ExpandableNotificationRowController.this.mView, NotificationMenuRowPlugin.class, false);
                ExpandableNotificationRow access$100 = ExpandableNotificationRowController.this.mView;
                if (ExpandableNotificationRowController.this.mStatusBarStateController.getState() == 1) {
                    z = true;
                }
                access$100.setOnKeyguard(z);
                ExpandableNotificationRowController.this.mStatusBarStateController.addCallback(ExpandableNotificationRowController.this.mStatusBarStateListener);
            }

            public void onViewDetachedFromWindow(View view) {
                ExpandableNotificationRowController.this.mPluginManager.removePluginListener(ExpandableNotificationRowController.this.mView);
                ExpandableNotificationRowController.this.mStatusBarStateController.removeCallback(ExpandableNotificationRowController.this.mStatusBarStateListener);
            }
        });
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$init$0$com-android-systemui-statusbar-notification-row-ExpandableNotificationRowController */
    public /* synthetic */ boolean mo41255x51dbc3a3(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        if (!this.mView.isSummaryWithChildren()) {
            return this.mNotificationGutsManager.openGuts(view, i, i2, menuItem);
        }
        this.mView.expandNotification();
        return true;
    }

    /* access modifiers changed from: private */
    public void logNotificationExpansion(String str, boolean z, boolean z2) {
        this.mNotificationLogger.onExpansionChanged(str, z, z2);
    }

    public String getNodeLabel() {
        return this.mView.getEntry().getKey();
    }

    public View getView() {
        return this.mView;
    }

    public View getChildAt(int i) {
        return this.mView.getChildNotificationAt(i);
    }

    public void addChildAt(NodeController nodeController, int i) {
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) nodeController.getView();
        this.mView.addChildNotification((ExpandableNotificationRow) nodeController.getView(), i);
        this.mListContainer.notifyGroupChildAdded(expandableNotificationRow);
        expandableNotificationRow.setChangingPosition(false);
    }

    public void moveChildTo(NodeController nodeController, int i) {
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) nodeController.getView();
        expandableNotificationRow.setChangingPosition(true);
        this.mView.removeChildNotification(expandableNotificationRow);
        this.mView.addChildNotification(expandableNotificationRow, i);
        expandableNotificationRow.setChangingPosition(false);
    }

    public void removeChild(NodeController nodeController, boolean z) {
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) nodeController.getView();
        if (z) {
            expandableNotificationRow.setChangingPosition(true);
        }
        this.mView.removeChildNotification(expandableNotificationRow);
        if (!z) {
            this.mListContainer.notifyGroupChildRemoved(expandableNotificationRow, this.mView);
        }
    }

    public int getChildCount() {
        List<ExpandableNotificationRow> attachedChildren = this.mView.getAttachedChildren();
        if (attachedChildren != null) {
            return attachedChildren.size();
        }
        return 0;
    }

    public void setUntruncatedChildCount(int i) {
        if (this.mView.isSummaryWithChildren()) {
            this.mView.setUntruncatedChildCount(i);
        } else {
            Log.w(TAG, "Called setUntruncatedChildCount(" + i + ") on a leaf row");
        }
    }

    public void setSystemExpanded(boolean z) {
        this.mView.setSystemExpanded(z);
    }

    public void setLastAudiblyAlertedMs(long j) {
        this.mView.setLastAudiblyAlertedMs(j);
    }

    public void setFeedbackIcon(FeedbackIcon feedbackIcon) {
        this.mView.setFeedbackIcon(feedbackIcon);
    }
}
