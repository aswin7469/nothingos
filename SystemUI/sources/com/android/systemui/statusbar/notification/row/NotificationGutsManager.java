package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.notification.ConversationIconFactory;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.Objects;
import java.util.Optional;

public class NotificationGutsManager implements Dumpable, NotificationLifetimeExtender, NotifGutsViewManager {
    private static final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
    public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args";
    private static final String TAG = "NotificationGutsManager";
    /* access modifiers changed from: private */
    public final AccessibilityManager mAccessibilityManager;
    private final AppWidgetManager mAppWidgetManager;
    private final AssistantFeedbackController mAssistantFeedbackController;
    private final Handler mBgHandler;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    private final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    private final ChannelEditorDialogController mChannelEditorDialogController;
    private NotificationInfo.CheckSaveListener mCheckSaveListener;
    private final Context mContext;
    private final UserContextProvider mContextTracker;
    private final DeviceProvisionedController mDeviceProvisionedController = ((DeviceProvisionedController) Dependency.get(DeviceProvisionedController.class));
    /* access modifiers changed from: private */
    public NotifGutsViewListener mGutsListener;
    /* access modifiers changed from: private */
    public NotificationMenuRowPlugin.MenuItem mGutsMenuItem;
    private final HighPriorityProvider mHighPriorityProvider;
    protected String mKeyToRemoveOnGutsClosed;
    private final LauncherApps mLauncherApps;
    /* access modifiers changed from: private */
    public NotificationListContainer mListContainer;
    private final NotificationLockscreenUserManager mLockscreenUserManager = ((NotificationLockscreenUserManager) Dependency.get(NotificationLockscreenUserManager.class));
    private final Handler mMainHandler;
    private final MetricsLogger mMetricsLogger = ((MetricsLogger) Dependency.get(MetricsLogger.class));
    private NotificationActivityStarter mNotificationActivityStarter;
    private NotificationGuts mNotificationGutsExposed;
    private NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;
    private final INotificationManager mNotificationManager;
    private OnSettingsClickListener mOnSettingsClickListener;
    private final OnUserInteractionCallback mOnUserInteractionCallback;
    private Runnable mOpenRunnable;
    private final PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    private NotificationPresenter mPresenter;
    private final ShadeController mShadeController;
    private final ShortcutManager mShortcutManager;
    /* access modifiers changed from: private */
    public final StatusBarStateController mStatusBarStateController = ((StatusBarStateController) Dependency.get(StatusBarStateController.class));
    private final UiEventLogger mUiEventLogger;

    public interface OnSettingsClickListener {
        void onSettingsClick(String str);
    }

    public NotificationGutsManager(Context context, Lazy<Optional<CentralSurfaces>> lazy, @Main Handler handler, @Background Handler handler2, AccessibilityManager accessibilityManager, HighPriorityProvider highPriorityProvider, INotificationManager iNotificationManager, NotificationEntryManager notificationEntryManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, LauncherApps launcherApps, ShortcutManager shortcutManager, ChannelEditorDialogController channelEditorDialogController, UserContextProvider userContextProvider, AssistantFeedbackController assistantFeedbackController, Optional<BubblesManager> optional, UiEventLogger uiEventLogger, OnUserInteractionCallback onUserInteractionCallback, ShadeController shadeController, DumpManager dumpManager) {
        this.mContext = context;
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mMainHandler = handler;
        this.mBgHandler = handler2;
        this.mAccessibilityManager = accessibilityManager;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mNotificationManager = iNotificationManager;
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
        this.mLauncherApps = launcherApps;
        this.mShortcutManager = shortcutManager;
        this.mContextTracker = userContextProvider;
        this.mChannelEditorDialogController = channelEditorDialogController;
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mBubblesManagerOptional = optional;
        this.mUiEventLogger = uiEventLogger;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mShadeController = shadeController;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
        dumpManager.registerDumpable(this);
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, NotificationInfo.CheckSaveListener checkSaveListener, OnSettingsClickListener onSettingsClickListener) {
        this.mPresenter = notificationPresenter;
        this.mListContainer = notificationListContainer;
        this.mCheckSaveListener = checkSaveListener;
        this.mOnSettingsClickListener = onSettingsClickListener;
    }

    public void setNotificationActivityStarter(NotificationActivityStarter notificationActivityStarter) {
        this.mNotificationActivityStarter = notificationActivityStarter;
    }

    public void onDensityOrFontScaleChanged(NotificationEntry notificationEntry) {
        setExposedGuts(notificationEntry.getGuts());
        bindGuts(notificationEntry.getRow());
    }

    private void startAppNotificationSettingsActivity(String str, int i, NotificationChannel notificationChannel, ExpandableNotificationRow expandableNotificationRow) {
        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", str);
        intent.putExtra("app_uid", i);
        if (notificationChannel != null) {
            Bundle bundle = new Bundle();
            intent.putExtra(EXTRA_FRAGMENT_ARG_KEY, notificationChannel.getId());
            bundle.putString(EXTRA_FRAGMENT_ARG_KEY, notificationChannel.getId());
            intent.putExtra(":settings:show_fragment_args", bundle);
        }
        this.mNotificationActivityStarter.startNotificationGutsIntent(intent, i, expandableNotificationRow);
    }

    private void startAppDetailsSettingsActivity(String str, int i, NotificationChannel notificationChannel, ExpandableNotificationRow expandableNotificationRow) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", str, (String) null));
        intent.putExtra("android.provider.extra.APP_PACKAGE", str);
        intent.putExtra("app_uid", i);
        if (notificationChannel != null) {
            intent.putExtra(EXTRA_FRAGMENT_ARG_KEY, notificationChannel.getId());
        }
        this.mNotificationActivityStarter.startNotificationGutsIntent(intent, i, expandableNotificationRow);
    }

    /* access modifiers changed from: protected */
    public void startAppOpsSettingsActivity(String str, int i, ArraySet<Integer> arraySet, ExpandableNotificationRow expandableNotificationRow) {
        if (arraySet.contains(24)) {
            if (arraySet.contains(26) || arraySet.contains(27)) {
                startAppDetailsSettingsActivity(str, i, (NotificationChannel) null, expandableNotificationRow);
                return;
            }
            Intent intent = new Intent("android.settings.MANAGE_APP_OVERLAY_PERMISSION");
            intent.setData(Uri.fromParts("package", str, (String) null));
            this.mNotificationActivityStarter.startNotificationGutsIntent(intent, i, expandableNotificationRow);
        } else if (arraySet.contains(26) || arraySet.contains(27)) {
            Intent intent2 = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
            intent2.putExtra("android.intent.extra.PACKAGE_NAME", str);
            this.mNotificationActivityStarter.startNotificationGutsIntent(intent2, i, expandableNotificationRow);
        }
    }

    private void startConversationSettingsActivity(int i, ExpandableNotificationRow expandableNotificationRow) {
        this.mNotificationActivityStarter.startNotificationGutsIntent(new Intent("android.settings.CONVERSATION_SETTINGS"), i, expandableNotificationRow);
    }

    private boolean bindGuts(ExpandableNotificationRow expandableNotificationRow) {
        expandableNotificationRow.ensureGutsInflated();
        return bindGuts(expandableNotificationRow, this.mGutsMenuItem);
    }

    /* access modifiers changed from: protected */
    public boolean bindGuts(ExpandableNotificationRow expandableNotificationRow, NotificationMenuRowPlugin.MenuItem menuItem) {
        NotificationEntry entry = expandableNotificationRow.getEntry();
        expandableNotificationRow.setGutsView(menuItem);
        expandableNotificationRow.setTag(entry.getSbn().getPackageName());
        expandableNotificationRow.getGuts().setClosedListener(new NotificationGutsManager$$ExternalSyntheticLambda8(this, expandableNotificationRow, entry));
        View gutsView = menuItem.getGutsView();
        try {
            if (gutsView instanceof NotificationSnooze) {
                initializeSnoozeView(expandableNotificationRow, (NotificationSnooze) gutsView);
                return true;
            } else if (gutsView instanceof NotificationInfo) {
                initializeNotificationInfo(expandableNotificationRow, (NotificationInfo) gutsView);
                return true;
            } else if (gutsView instanceof NotificationConversationInfo) {
                initializeConversationNotificationInfo(expandableNotificationRow, (NotificationConversationInfo) gutsView);
                return true;
            } else if (gutsView instanceof PartialConversationInfo) {
                initializePartialConversationNotificationInfo(expandableNotificationRow, (PartialConversationInfo) gutsView);
                return true;
            } else if (!(gutsView instanceof FeedbackInfo)) {
                return true;
            } else {
                initializeFeedbackInfo(expandableNotificationRow, (FeedbackInfo) gutsView);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "error binding guts", e);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindGuts$0$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41608x8a83fb1b(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry, NotificationGuts notificationGuts) {
        expandableNotificationRow.onGutsClosed();
        if (!notificationGuts.willBeRemoved() && !expandableNotificationRow.isRemoved()) {
            this.mListContainer.onHeightChanged(expandableNotificationRow, !this.mPresenter.isPresenterFullyCollapsed());
        }
        if (this.mNotificationGutsExposed == notificationGuts) {
            this.mNotificationGutsExposed = null;
            this.mGutsMenuItem = null;
        }
        NotifGutsViewListener notifGutsViewListener = this.mGutsListener;
        if (notifGutsViewListener != null) {
            notifGutsViewListener.onGutsClose(notificationEntry);
        }
        String key = notificationEntry.getKey();
        if (key.equals(this.mKeyToRemoveOnGutsClosed)) {
            this.mKeyToRemoveOnGutsClosed = null;
            NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
            if (notificationSafeToRemoveCallback != null) {
                notificationSafeToRemoveCallback.onSafeToRemove(key);
            }
        }
    }

    private void initializeSnoozeView(ExpandableNotificationRow expandableNotificationRow, NotificationSnooze notificationSnooze) {
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        notificationSnooze.setSnoozeListener(this.mListContainer.getSwipeActionHelper());
        notificationSnooze.setStatusBarNotification(sbn);
        notificationSnooze.setSnoozeOptions(expandableNotificationRow.getEntry().getSnoozeCriteria());
        guts.setHeightChangedListener(new NotificationGutsManager$$ExternalSyntheticLambda7(this, expandableNotificationRow));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initializeSnoozeView$1$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41615x6f8ac8ed(ExpandableNotificationRow expandableNotificationRow, NotificationGuts notificationGuts) {
        this.mListContainer.onHeightChanged(expandableNotificationRow, expandableNotificationRow.isShown());
    }

    private void initializeFeedbackInfo(ExpandableNotificationRow expandableNotificationRow, FeedbackInfo feedbackInfo) {
        if (this.mAssistantFeedbackController.getFeedbackIcon(expandableNotificationRow.getEntry()) != null) {
            StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
            FeedbackInfo feedbackInfo2 = feedbackInfo;
            ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
            feedbackInfo2.bindGuts(CentralSurfaces.getPackageManagerForUser(this.mContext, sbn.getUser().getIdentifier()), sbn, expandableNotificationRow.getEntry(), expandableNotificationRow2, this.mAssistantFeedbackController);
        }
    }

    /* access modifiers changed from: package-private */
    public void initializeNotificationInfo(ExpandableNotificationRow expandableNotificationRow, NotificationInfo notificationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda10 notificationGutsManager$$ExternalSyntheticLambda10;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = CentralSurfaces.getPackageManagerForUser(this.mContext, user.getIdentifier());
        NotificationGutsManager$$ExternalSyntheticLambda9 notificationGutsManager$$ExternalSyntheticLambda9 = new NotificationGutsManager$$ExternalSyntheticLambda9(this, guts, sbn, expandableNotificationRow);
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda10 = new NotificationGutsManager$$ExternalSyntheticLambda10(this, guts, sbn, packageName, expandableNotificationRow);
        } else {
            notificationGutsManager$$ExternalSyntheticLambda10 = null;
        }
        notificationInfo.bindNotification(packageManagerForUser, this.mNotificationManager, this.mOnUserInteractionCallback, this.mChannelEditorDialogController, packageName, expandableNotificationRow.getEntry().getChannel(), expandableNotificationRow.getUniqueChannels(), expandableNotificationRow.getEntry(), notificationGutsManager$$ExternalSyntheticLambda10, notificationGutsManager$$ExternalSyntheticLambda9, this.mUiEventLogger, this.mDeviceProvisionedController.isDeviceProvisioned(), expandableNotificationRow.getIsNonblockable(), this.mHighPriorityProvider.isHighPriority(expandableNotificationRow.getEntry()), this.mAssistantFeedbackController);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initializeNotificationInfo$2$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41612x9d3f6b3c(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, ExpandableNotificationRow expandableNotificationRow, View view, Intent intent) {
        this.mMetricsLogger.action(206);
        notificationGuts.resetFalsingCheck();
        this.mNotificationActivityStarter.startNotificationGutsIntent(intent, statusBarNotification.getUid(), expandableNotificationRow);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initializeNotificationInfo$3$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41613x644b523d(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    /* access modifiers changed from: package-private */
    public void initializePartialConversationNotificationInfo(ExpandableNotificationRow expandableNotificationRow, PartialConversationInfo partialConversationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda6 notificationGutsManager$$ExternalSyntheticLambda6;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = CentralSurfaces.getPackageManagerForUser(this.mContext, user.getIdentifier());
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda6 = new NotificationGutsManager$$ExternalSyntheticLambda6(this, guts, sbn, packageName, expandableNotificationRow);
        } else {
            notificationGutsManager$$ExternalSyntheticLambda6 = null;
        }
        partialConversationInfo.bindNotification(packageManagerForUser, this.mNotificationManager, this.mChannelEditorDialogController, packageName, expandableNotificationRow.getEntry().getChannel(), expandableNotificationRow.getUniqueChannels(), expandableNotificationRow.getEntry(), notificationGutsManager$$ExternalSyntheticLambda6, this.mDeviceProvisionedController.isDeviceProvisioned(), expandableNotificationRow.getIsNonblockable());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initializePartialConversationNotificationInfo$4$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41614x9b984b08(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    /* access modifiers changed from: package-private */
    public void initializeConversationNotificationInfo(ExpandableNotificationRow expandableNotificationRow, NotificationConversationInfo notificationConversationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda5 notificationGutsManager$$ExternalSyntheticLambda5;
        ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        NotificationEntry entry = expandableNotificationRow.getEntry();
        StatusBarNotification sbn = entry.getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = CentralSurfaces.getPackageManagerForUser(this.mContext, user.getIdentifier());
        new NotificationGutsManager$$ExternalSyntheticLambda3(this, guts, sbn, expandableNotificationRow2);
        NotificationGutsManager$$ExternalSyntheticLambda4 notificationGutsManager$$ExternalSyntheticLambda4 = new NotificationGutsManager$$ExternalSyntheticLambda4(this, sbn, expandableNotificationRow2);
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda5 = new NotificationGutsManager$$ExternalSyntheticLambda5(this, guts, sbn, packageName, expandableNotificationRow);
        } else {
            notificationGutsManager$$ExternalSyntheticLambda5 = null;
        }
        ConversationIconFactory conversationIconFactory = r7;
        Context context = this.mContext;
        PackageManager packageManager = packageManagerForUser;
        ConversationIconFactory conversationIconFactory2 = new ConversationIconFactory(context, this.mLauncherApps, packageManager, IconDrawableFactory.newInstance(context, false), this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.notification_guts_conversation_icon_size));
        notificationConversationInfo.bindNotification(notificationConversationInfo.getSelectedAction(), this.mShortcutManager, packageManager, this.mPeopleSpaceWidgetManager, this.mNotificationManager, this.mOnUserInteractionCallback, packageName, entry.getChannel(), entry, entry.getBubbleMetadata(), notificationGutsManager$$ExternalSyntheticLambda5, conversationIconFactory, this.mContextTracker.getUserContext(), this.mDeviceProvisionedController.isDeviceProvisioned(), this.mMainHandler, this.mBgHandler, notificationGutsManager$$ExternalSyntheticLambda4, this.mBubblesManagerOptional, this.mShadeController);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initializeConversationNotificationInfo$5$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41609x211757a2(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, ExpandableNotificationRow expandableNotificationRow, View view, Intent intent) {
        this.mMetricsLogger.action(206);
        notificationGuts.resetFalsingCheck();
        this.mNotificationActivityStarter.startNotificationGutsIntent(intent, statusBarNotification.getUid(), expandableNotificationRow);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initializeConversationNotificationInfo$6$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41610xe8233ea3(StatusBarNotification statusBarNotification, ExpandableNotificationRow expandableNotificationRow) {
        startConversationSettingsActivity(statusBarNotification.getUid(), expandableNotificationRow);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initializeConversationNotificationInfo$7$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41611xaf2f25a4(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    public void closeAndSaveGuts(boolean z, boolean z2, boolean z3, int i, int i2, boolean z4) {
        NotificationGuts notificationGuts = this.mNotificationGutsExposed;
        if (notificationGuts != null) {
            notificationGuts.removeCallbacks(this.mOpenRunnable);
            this.mNotificationGutsExposed.closeControls(z, z3, i, i2, z2);
        }
        if (z4) {
            this.mListContainer.resetExposedMenuView(false, true);
        }
    }

    public NotificationGuts getExposedGuts() {
        return this.mNotificationGutsExposed;
    }

    public void setExposedGuts(NotificationGuts notificationGuts) {
        this.mNotificationGutsExposed = notificationGuts;
    }

    public ExpandableNotificationRow.LongPressListener getNotificationLongClicker() {
        return new NotificationGutsManager$$ExternalSyntheticLambda2(this);
    }

    public boolean openGuts(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        if ((menuItem.getGutsView() instanceof NotificationGuts.GutsContent) && ((NotificationGuts.GutsContent) menuItem.getGutsView()).needsFalsingProtection()) {
            StatusBarStateController statusBarStateController = this.mStatusBarStateController;
            if (statusBarStateController instanceof StatusBarStateControllerImpl) {
                ((StatusBarStateControllerImpl) statusBarStateController).setLeaveOpenOnKeyguardHide(true);
            }
            Optional optional = this.mCentralSurfacesOptionalLazy.get();
            if (optional.isPresent()) {
                ((CentralSurfaces) optional.get()).executeRunnableDismissingKeyguard(new NotificationGutsManager$$ExternalSyntheticLambda0(this, view, i, i2, menuItem), (Runnable) null, false, true, true);
                return true;
            }
        }
        return mo41616xbf413270(view, i, i2, menuItem);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$openGuts$9$com-android-systemui-statusbar-notification-row-NotificationGutsManager */
    public /* synthetic */ void mo41617x864d1971(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        this.mMainHandler.post(new NotificationGutsManager$$ExternalSyntheticLambda1(this, view, i, i2, menuItem));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: openGutsInternal */
    public boolean mo41616xbf413270(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        if (view.getWindowToken() == null) {
            Log.e(TAG, "Trying to show notification guts, but not attached to window");
            return false;
        }
        final ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        view.performHapticFeedback(0);
        if (expandableNotificationRow.areGutsExposed()) {
            closeAndSaveGuts(false, false, true, -1, -1, true);
            return false;
        }
        expandableNotificationRow.ensureGutsInflated();
        NotificationGuts guts = expandableNotificationRow.getGuts();
        this.mNotificationGutsExposed = guts;
        if (!bindGuts(expandableNotificationRow, menuItem) || guts == null) {
            return false;
        }
        guts.setVisibility(4);
        final NotificationGuts notificationGuts = guts;
        final int i3 = i;
        final int i4 = i2;
        final NotificationMenuRowPlugin.MenuItem menuItem2 = menuItem;
        C27711 r0 = new Runnable() {
            public void run() {
                if (expandableNotificationRow.getWindowToken() == null) {
                    Log.e(NotificationGutsManager.TAG, "Trying to show notification guts in post(), but not attached to window");
                    return;
                }
                notificationGuts.setVisibility(0);
                boolean z = NotificationGutsManager.this.mStatusBarStateController.getState() == 1 && !NotificationGutsManager.this.mAccessibilityManager.isTouchExplorationEnabled();
                NotificationGuts notificationGuts = notificationGuts;
                boolean z2 = !expandableNotificationRow.isBlockingHelperShowing();
                int i = i3;
                int i2 = i4;
                ExpandableNotificationRow expandableNotificationRow = expandableNotificationRow;
                Objects.requireNonNull(expandableNotificationRow);
                notificationGuts.openControls(z2, i, i2, z, new NotificationGutsManager$1$$ExternalSyntheticLambda0(expandableNotificationRow));
                if (NotificationGutsManager.this.mGutsListener != null) {
                    NotificationGutsManager.this.mGutsListener.onGutsOpen(expandableNotificationRow.getEntry(), notificationGuts);
                }
                expandableNotificationRow.closeRemoteInput();
                NotificationGutsManager.this.mListContainer.onHeightChanged(expandableNotificationRow, true);
                NotificationMenuRowPlugin.MenuItem unused = NotificationGutsManager.this.mGutsMenuItem = menuItem2;
            }
        };
        this.mOpenRunnable = r0;
        guts.post(r0);
        return true;
    }

    public void setCallback(NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback) {
        this.mNotificationLifetimeFinishedCallback = notificationSafeToRemoveCallback;
    }

    public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        return (notificationEntry == null || this.mNotificationGutsExposed == null || notificationEntry.getGuts() == null || this.mNotificationGutsExposed != notificationEntry.getGuts() || this.mNotificationGutsExposed.isLeavebehind()) ? false : true;
    }

    public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mKeyToRemoveOnGutsClosed = notificationEntry.getKey();
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Keeping notification because it's showing guts. " + notificationEntry.getKey());
                return;
            }
            return;
        }
        String str = this.mKeyToRemoveOnGutsClosed;
        if (str != null && str.equals(notificationEntry.getKey())) {
            this.mKeyToRemoveOnGutsClosed = null;
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Notification that was kept for guts was updated. " + notificationEntry.getKey());
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationGutsManager state:");
        printWriter.print("  mKeyToRemoveOnGutsClosed (legacy): ");
        printWriter.println(this.mKeyToRemoveOnGutsClosed);
    }

    public void setGutsListener(NotifGutsViewListener notifGutsViewListener) {
        this.mGutsListener = notifGutsViewListener;
    }
}
