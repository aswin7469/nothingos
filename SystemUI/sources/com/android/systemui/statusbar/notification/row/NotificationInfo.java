package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.icu.lang.UCharacter;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Set;

public class NotificationInfo extends LinearLayout implements NotificationGuts.GutsContent {
    public static final int ACTION_NONE = 0;
    private static final int ACTION_TOGGLE_ALERT = 5;
    static final int ACTION_TOGGLE_SILENT = 2;
    private static final int BEHAVIOR_ALERTING = 0;
    private static final int BEHAVIOR_AUTOMATIC = 2;
    private static final int BEHAVIOR_SILENT = 1;
    private static final String TAG = "InfoGuts";
    private int mActualHeight;
    private String mAppName;
    private OnAppSettingsClickListener mAppSettingsClickListener;
    private int mAppUid;
    private AssistantFeedbackController mAssistantFeedbackController;
    private TextView mAutomaticDescriptionView;
    private ChannelEditorDialogController mChannelEditorDialogController;
    private Integer mChosenImportance;
    private String mDelegatePkg;
    private NotificationEntry mEntry;
    private NotificationGuts mGutsContainer;
    private INotificationManager mINotificationManager;
    private boolean mIsAutomaticChosen;
    private boolean mIsDeviceProvisioned;
    private boolean mIsNonblockable;
    private boolean mIsSingleDefaultChannel;
    private boolean mIsSystemRegisteredCall;
    private MetricsLogger mMetricsLogger;
    private int mNumUniqueChannelsInRow;
    private View.OnClickListener mOnAlert = new NotificationInfo$$ExternalSyntheticLambda6(this);
    private View.OnClickListener mOnAutomatic = new NotificationInfo$$ExternalSyntheticLambda5(this);
    private View.OnClickListener mOnDismissSettings = new NotificationInfo$$ExternalSyntheticLambda8(this);
    private OnSettingsClickListener mOnSettingsClickListener;
    private View.OnClickListener mOnSilent = new NotificationInfo$$ExternalSyntheticLambda7(this);
    private OnUserInteractionCallback mOnUserInteractionCallback;
    private String mPackageName;
    private Drawable mPkgIcon;
    private PackageManager mPm;
    private boolean mPresentingChannelEditorDialog = false;
    private boolean mPressedApply;
    private TextView mPriorityDescriptionView;
    private StatusBarNotification mSbn;
    private boolean mShowAutomaticSetting;
    private TextView mSilentDescriptionView;
    private NotificationChannel mSingleNotificationChannel;
    boolean mSkipPost = false;
    private int mStartingChannelImportance;
    private UiEventLogger mUiEventLogger;
    private Set<NotificationChannel> mUniqueChannelsInRow;
    private boolean mWasShownHighPriority;

    @Retention(RetentionPolicy.SOURCE)
    private @interface AlertingBehavior {
    }

    public interface CheckSaveListener {
        void checkSave(Runnable runnable, StatusBarNotification statusBarNotification);
    }

    public @interface NotificationInfoAction {
    }

    public interface OnAppSettingsClickListener {
        void onClick(View view, Intent intent);
    }

    public interface OnSettingsClickListener {
        void onClick(View view, NotificationChannel notificationChannel, int i);
    }

    public View getContentView() {
        return this;
    }

    public boolean isAnimating() {
        return false;
    }

    public boolean needsFalsingProtection() {
        return true;
    }

    public boolean willBeRemoved() {
        return false;
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-notification-row-NotificationInfo */
    public /* synthetic */ void mo41638x36838637(View view) {
        this.mIsAutomaticChosen = true;
        applyAlertingBehavior(2, true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-statusbar-notification-row-NotificationInfo */
    public /* synthetic */ void mo41639xa0b30e56(View view) {
        this.mChosenImportance = 3;
        this.mIsAutomaticChosen = false;
        applyAlertingBehavior(0, true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-statusbar-notification-row-NotificationInfo */
    public /* synthetic */ void mo41640xae29675(View view) {
        this.mChosenImportance = 2;
        this.mIsAutomaticChosen = false;
        applyAlertingBehavior(1, true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-statusbar-notification-row-NotificationInfo */
    public /* synthetic */ void mo41641x75121e94(View view) {
        this.mPressedApply = true;
        this.mGutsContainer.closeControls(view, true);
    }

    public NotificationInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPriorityDescriptionView = (TextView) findViewById(C1894R.C1898id.alert_summary);
        this.mSilentDescriptionView = (TextView) findViewById(C1894R.C1898id.silence_summary);
        this.mAutomaticDescriptionView = (TextView) findViewById(C1894R.C1898id.automatic_summary);
    }

    public void bindNotification(PackageManager packageManager, INotificationManager iNotificationManager, OnUserInteractionCallback onUserInteractionCallback, ChannelEditorDialogController channelEditorDialogController, String str, NotificationChannel notificationChannel, Set<NotificationChannel> set, NotificationEntry notificationEntry, OnSettingsClickListener onSettingsClickListener, OnAppSettingsClickListener onAppSettingsClickListener, UiEventLogger uiEventLogger, boolean z, boolean z2, boolean z3, AssistantFeedbackController assistantFeedbackController) throws RemoteException {
        this.mINotificationManager = iNotificationManager;
        this.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mChannelEditorDialogController = channelEditorDialogController;
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mPackageName = str;
        this.mUniqueChannelsInRow = set;
        this.mNumUniqueChannelsInRow = set.size();
        this.mEntry = notificationEntry;
        this.mSbn = notificationEntry.getSbn();
        this.mPm = packageManager;
        this.mAppSettingsClickListener = onAppSettingsClickListener;
        this.mAppName = this.mPackageName;
        this.mOnSettingsClickListener = onSettingsClickListener;
        this.mSingleNotificationChannel = notificationChannel;
        this.mStartingChannelImportance = notificationChannel.getImportance();
        this.mWasShownHighPriority = z3;
        this.mIsNonblockable = z2;
        this.mAppUid = this.mSbn.getUid();
        this.mDelegatePkg = this.mSbn.getOpPkg();
        this.mIsDeviceProvisioned = z;
        this.mShowAutomaticSetting = this.mAssistantFeedbackController.isFeedbackEnabled();
        this.mUiEventLogger = uiEventLogger;
        boolean z4 = false;
        this.mIsSystemRegisteredCall = this.mSbn.getNotification().isStyle(Notification.CallStyle.class) && this.mINotificationManager.isInCall(this.mSbn.getPackageName(), this.mSbn.getUid());
        int numNotificationChannelsForPackage = this.mINotificationManager.getNumNotificationChannelsForPackage(str, this.mAppUid, false);
        int i = this.mNumUniqueChannelsInRow;
        if (i != 0) {
            this.mIsSingleDefaultChannel = i == 1 && this.mSingleNotificationChannel.getId().equals(NotificationChannelCompat.DEFAULT_CHANNEL_ID) && numNotificationChannelsForPackage == 1;
            if (getAlertingBehavior() == 2) {
                z4 = true;
            }
            this.mIsAutomaticChosen = z4;
            bindHeader();
            bindChannelDetails();
            bindInlineControls();
            logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_OPEN);
            this.mMetricsLogger.write(notificationControlsLogMaker());
            return;
        }
        throw new IllegalArgumentException("bindNotification requires at least one channel");
    }

    private void bindInlineControls() {
        if (this.mIsSystemRegisteredCall) {
            findViewById(C1894R.C1898id.non_configurable_call_text).setVisibility(0);
            findViewById(C1894R.C1898id.non_configurable_text).setVisibility(8);
            findViewById(C1894R.C1898id.non_configurable_multichannel_text).setVisibility(8);
            findViewById(C1894R.C1898id.interruptiveness_settings).setVisibility(8);
            ((TextView) findViewById(C1894R.C1898id.done)).setText(C1894R.string.inline_done_button);
            findViewById(C1894R.C1898id.turn_off_notifications).setVisibility(8);
        } else if (this.mIsNonblockable) {
            findViewById(C1894R.C1898id.non_configurable_text).setVisibility(0);
            findViewById(C1894R.C1898id.non_configurable_call_text).setVisibility(8);
            findViewById(C1894R.C1898id.non_configurable_multichannel_text).setVisibility(8);
            findViewById(C1894R.C1898id.interruptiveness_settings).setVisibility(8);
            ((TextView) findViewById(C1894R.C1898id.done)).setText(C1894R.string.inline_done_button);
            findViewById(C1894R.C1898id.turn_off_notifications).setVisibility(8);
        } else if (this.mNumUniqueChannelsInRow > 1) {
            findViewById(C1894R.C1898id.non_configurable_call_text).setVisibility(8);
            findViewById(C1894R.C1898id.non_configurable_text).setVisibility(8);
            findViewById(C1894R.C1898id.interruptiveness_settings).setVisibility(8);
            findViewById(C1894R.C1898id.non_configurable_multichannel_text).setVisibility(0);
        } else {
            findViewById(C1894R.C1898id.non_configurable_call_text).setVisibility(8);
            findViewById(C1894R.C1898id.non_configurable_text).setVisibility(8);
            findViewById(C1894R.C1898id.non_configurable_multichannel_text).setVisibility(8);
            findViewById(C1894R.C1898id.interruptiveness_settings).setVisibility(0);
        }
        View findViewById = findViewById(C1894R.C1898id.turn_off_notifications);
        findViewById.setOnClickListener(getTurnOffNotificationsClickListener());
        findViewById.setVisibility((!findViewById.hasOnClickListeners() || this.mIsNonblockable) ? 8 : 0);
        View findViewById2 = findViewById(C1894R.C1898id.done);
        findViewById2.setOnClickListener(this.mOnDismissSettings);
        findViewById2.setAccessibilityDelegate(this.mGutsContainer.getAccessibilityDelegate());
        View findViewById3 = findViewById(C1894R.C1898id.silence);
        View findViewById4 = findViewById(C1894R.C1898id.alert);
        findViewById3.setOnClickListener(this.mOnSilent);
        findViewById4.setOnClickListener(this.mOnAlert);
        View findViewById5 = findViewById(C1894R.C1898id.automatic);
        if (this.mShowAutomaticSetting) {
            this.mAutomaticDescriptionView.setText(Html.fromHtml(this.mContext.getText(this.mAssistantFeedbackController.getInlineDescriptionResource(this.mEntry)).toString()));
            findViewById5.setVisibility(0);
            findViewById5.setOnClickListener(this.mOnAutomatic);
        } else {
            findViewById5.setVisibility(8);
        }
        applyAlertingBehavior(getAlertingBehavior(), false);
    }

    private void bindHeader() {
        this.mPkgIcon = null;
        try {
            ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(this.mPackageName, 795136);
            if (applicationInfo != null) {
                this.mAppName = String.valueOf((Object) this.mPm.getApplicationLabel(applicationInfo));
                this.mPkgIcon = this.mPm.getApplicationIcon(applicationInfo);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            this.mPkgIcon = this.mPm.getDefaultActivityIcon();
        }
        ((ImageView) findViewById(C1894R.C1898id.pkg_icon)).setImageDrawable(this.mPkgIcon);
        ((TextView) findViewById(C1894R.C1898id.pkg_name)).setText(this.mAppName);
        bindDelegate();
        View findViewById = findViewById(C1894R.C1898id.app_settings);
        Intent appSettingsIntent = getAppSettingsIntent(this.mPm, this.mPackageName, this.mSingleNotificationChannel, this.mSbn.getId(), this.mSbn.getTag());
        int i = 0;
        if (appSettingsIntent == null || TextUtils.isEmpty(this.mSbn.getNotification().getSettingsText())) {
            findViewById.setVisibility(8);
        } else {
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(new NotificationInfo$$ExternalSyntheticLambda3(this, appSettingsIntent));
        }
        View findViewById2 = findViewById(C1894R.C1898id.info);
        findViewById2.setOnClickListener(getSettingsOnClickListener());
        if (!findViewById2.hasOnClickListeners()) {
            i = 8;
        }
        findViewById2.setVisibility(i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindHeader$4$com-android-systemui-statusbar-notification-row-NotificationInfo */
    public /* synthetic */ void mo41634x23f3d4f(Intent intent, View view) {
        this.mAppSettingsClickListener.onClick(view, intent);
    }

    private View.OnClickListener getSettingsOnClickListener() {
        int i = this.mAppUid;
        if (i < 0 || this.mOnSettingsClickListener == null || !this.mIsDeviceProvisioned) {
            return null;
        }
        return new NotificationInfo$$ExternalSyntheticLambda2(this, i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getSettingsOnClickListener$5$com-android-systemui-statusbar-notification-row-NotificationInfo */
    public /* synthetic */ void mo41635x3805ca74(int i, View view) {
        this.mOnSettingsClickListener.onClick(view, this.mNumUniqueChannelsInRow > 1 ? null : this.mSingleNotificationChannel, i);
    }

    private View.OnClickListener getTurnOffNotificationsClickListener() {
        return new NotificationInfo$$ExternalSyntheticLambda4(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getTurnOffNotificationsClickListener$7$com-android-systemui-statusbar-notification-row-NotificationInfo */
    public /* synthetic */ void mo41637x6fb61c66(View view) {
        ChannelEditorDialogController channelEditorDialogController;
        if (!this.mPresentingChannelEditorDialog && (channelEditorDialogController = this.mChannelEditorDialogController) != null) {
            this.mPresentingChannelEditorDialog = true;
            channelEditorDialogController.prepareDialogForApp(this.mAppName, this.mPackageName, this.mAppUid, this.mUniqueChannelsInRow, this.mPkgIcon, this.mOnSettingsClickListener);
            this.mChannelEditorDialogController.setOnFinishListener(new NotificationInfo$$ExternalSyntheticLambda0(this));
            this.mChannelEditorDialogController.show();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getTurnOffNotificationsClickListener$6$com-android-systemui-statusbar-notification-row-NotificationInfo */
    public /* synthetic */ void mo41636x5869447() {
        this.mPresentingChannelEditorDialog = false;
        this.mGutsContainer.closeControls(this, false);
    }

    private void bindChannelDetails() throws RemoteException {
        bindName();
        bindGroup();
    }

    private void bindName() {
        TextView textView = (TextView) findViewById(C1894R.C1898id.channel_name);
        if (this.mIsSingleDefaultChannel || this.mNumUniqueChannelsInRow > 1) {
            textView.setVisibility(8);
        } else {
            textView.setText(this.mSingleNotificationChannel.getName());
        }
    }

    private void bindDelegate() {
        TextView textView = (TextView) findViewById(C1894R.C1898id.delegate_name);
        if (!TextUtils.equals(this.mPackageName, this.mDelegatePkg)) {
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r0 = r4.mINotificationManager.getNotificationChannelGroupForPackage(r4.mSingleNotificationChannel.getGroup(), r4.mPackageName, r4.mAppUid);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void bindGroup() throws android.os.RemoteException {
        /*
            r4 = this;
            android.app.NotificationChannel r0 = r4.mSingleNotificationChannel
            if (r0 == 0) goto L_0x0021
            java.lang.String r0 = r0.getGroup()
            if (r0 == 0) goto L_0x0021
            android.app.INotificationManager r0 = r4.mINotificationManager
            android.app.NotificationChannel r1 = r4.mSingleNotificationChannel
            java.lang.String r1 = r1.getGroup()
            java.lang.String r2 = r4.mPackageName
            int r3 = r4.mAppUid
            android.app.NotificationChannelGroup r0 = r0.getNotificationChannelGroupForPackage(r1, r2, r3)
            if (r0 == 0) goto L_0x0021
            java.lang.CharSequence r0 = r0.getName()
            goto L_0x0022
        L_0x0021:
            r0 = 0
        L_0x0022:
            r1 = 2131428014(0x7f0b02ae, float:1.847766E38)
            android.view.View r4 = r4.findViewById(r1)
            android.widget.TextView r4 = (android.widget.TextView) r4
            if (r0 == 0) goto L_0x0035
            r4.setText(r0)
            r0 = 0
            r4.setVisibility(r0)
            goto L_0x003a
        L_0x0035:
            r0 = 8
            r4.setVisibility(r0)
        L_0x003a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.NotificationInfo.bindGroup():void");
    }

    private void saveImportance() {
        if (!this.mIsNonblockable) {
            if (this.mChosenImportance == null) {
                this.mChosenImportance = Integer.valueOf(this.mStartingChannelImportance);
            }
            updateImportance();
        }
    }

    private void updateImportance() {
        if (this.mChosenImportance != null) {
            logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_SAVE_IMPORTANCE);
            this.mMetricsLogger.write(importanceChangeLogMaker());
            int intValue = this.mChosenImportance.intValue();
            if (this.mStartingChannelImportance != -1000 && ((this.mWasShownHighPriority && this.mChosenImportance.intValue() >= 3) || (!this.mWasShownHighPriority && this.mChosenImportance.intValue() < 3))) {
                intValue = this.mStartingChannelImportance;
            }
            new Handler((Looper) Dependency.get(Dependency.BG_LOOPER)).post(new UpdateImportanceRunnable(this.mINotificationManager, this.mPackageName, this.mAppUid, this.mNumUniqueChannelsInRow == 1 ? this.mSingleNotificationChannel : null, this.mStartingChannelImportance, intValue, this.mIsAutomaticChosen));
            this.mOnUserInteractionCallback.onImportanceChanged(this.mEntry);
        }
    }

    public boolean post(Runnable runnable) {
        if (!this.mSkipPost) {
            return super.post(runnable);
        }
        runnable.run();
        return true;
    }

    private void applyAlertingBehavior(int i, boolean z) {
        boolean z2 = true;
        if (z) {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(0);
            transitionSet.addTransition(new Fade(2)).addTransition(new ChangeBounds()).addTransition(new Fade(1).setStartDelay(150).setDuration(200).setInterpolator(Interpolators.FAST_OUT_SLOW_IN));
            transitionSet.setDuration(350);
            transitionSet.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            TransitionManager.beginDelayedTransition(this, transitionSet);
        }
        View findViewById = findViewById(C1894R.C1898id.alert);
        View findViewById2 = findViewById(C1894R.C1898id.silence);
        View findViewById3 = findViewById(C1894R.C1898id.automatic);
        if (i == 0) {
            this.mPriorityDescriptionView.setVisibility(0);
            this.mSilentDescriptionView.setVisibility(8);
            this.mAutomaticDescriptionView.setVisibility(8);
            post(new NotificationInfo$$ExternalSyntheticLambda9(findViewById, findViewById2, findViewById3));
        } else if (i == 1) {
            this.mSilentDescriptionView.setVisibility(0);
            this.mPriorityDescriptionView.setVisibility(8);
            this.mAutomaticDescriptionView.setVisibility(8);
            post(new NotificationInfo$$ExternalSyntheticLambda10(findViewById, findViewById2, findViewById3));
        } else if (i == 2) {
            this.mAutomaticDescriptionView.setVisibility(0);
            this.mPriorityDescriptionView.setVisibility(8);
            this.mSilentDescriptionView.setVisibility(8);
            post(new NotificationInfo$$ExternalSyntheticLambda1(findViewById3, findViewById, findViewById2));
        } else {
            throw new IllegalArgumentException("Unrecognized alerting behavior: " + i);
        }
        if (getAlertingBehavior() == i) {
            z2 = false;
        }
        ((TextView) findViewById(C1894R.C1898id.done)).setText(z2 ? C1894R.string.inline_ok_button : C1894R.string.inline_done_button);
    }

    static /* synthetic */ void lambda$applyAlertingBehavior$8(View view, View view2, View view3) {
        view.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    static /* synthetic */ void lambda$applyAlertingBehavior$9(View view, View view2, View view3) {
        view.setSelected(false);
        view2.setSelected(true);
        view3.setSelected(false);
    }

    static /* synthetic */ void lambda$applyAlertingBehavior$10(View view, View view2, View view3) {
        view.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    public void onFinishedClosing() {
        Integer num = this.mChosenImportance;
        if (num != null) {
            this.mStartingChannelImportance = num.intValue();
        }
        bindInlineControls();
        logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_CLOSE);
        this.mMetricsLogger.write(notificationControlsLogMaker().setType(2));
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (this.mGutsContainer != null && accessibilityEvent.getEventType() == 32) {
            if (this.mGutsContainer.isExposed()) {
                accessibilityEvent.getText().add(this.mContext.getString(C1894R.string.notification_channel_controls_opened_accessibility, new Object[]{this.mAppName}));
                return;
            }
            accessibilityEvent.getText().add(this.mContext.getString(C1894R.string.notification_channel_controls_closed_accessibility, new Object[]{this.mAppName}));
        }
    }

    private Intent getAppSettingsIntent(PackageManager packageManager, String str, NotificationChannel notificationChannel, int i, String str2) {
        Intent intent = new Intent("android.intent.action.MAIN").addCategory(NotificationCompat.INTENT_CATEGORY_NOTIFICATION_PREFERENCES).setPackage(str);
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
        if (queryIntentActivities == null || queryIntentActivities.size() == 0 || queryIntentActivities.get(0) == null) {
            return null;
        }
        ActivityInfo activityInfo = queryIntentActivities.get(0).activityInfo;
        intent.setClassName(activityInfo.packageName, activityInfo.name);
        if (notificationChannel != null) {
            intent.putExtra(NotificationCompat.EXTRA_CHANNEL_ID, notificationChannel.getId());
        }
        intent.putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, i);
        intent.putExtra(NotificationCompat.EXTRA_NOTIFICATION_TAG, str2);
        return intent;
    }

    public void setGutsParent(NotificationGuts notificationGuts) {
        this.mGutsContainer = notificationGuts;
    }

    public boolean shouldBeSaved() {
        return this.mPressedApply;
    }

    public boolean handleCloseControls(boolean z, boolean z2) {
        ChannelEditorDialogController channelEditorDialogController;
        if (this.mPresentingChannelEditorDialog && (channelEditorDialogController = this.mChannelEditorDialogController) != null) {
            this.mPresentingChannelEditorDialog = false;
            channelEditorDialogController.setOnFinishListener((OnChannelEditorDialogFinishedListener) null);
            this.mChannelEditorDialogController.close();
        }
        if (z) {
            saveImportance();
        }
        return false;
    }

    public int getActualHeight() {
        return this.mActualHeight;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mActualHeight = getHeight();
    }

    private static class UpdateImportanceRunnable implements Runnable {
        private final int mAppUid;
        private final NotificationChannel mChannelToUpdate;
        private final int mCurrentImportance;
        private final INotificationManager mINotificationManager;
        private final int mNewImportance;
        private final String mPackageName;
        private final boolean mUnlockImportance;

        public UpdateImportanceRunnable(INotificationManager iNotificationManager, String str, int i, NotificationChannel notificationChannel, int i2, int i3, boolean z) {
            this.mINotificationManager = iNotificationManager;
            this.mPackageName = str;
            this.mAppUid = i;
            this.mChannelToUpdate = notificationChannel;
            this.mCurrentImportance = i2;
            this.mNewImportance = i3;
            this.mUnlockImportance = z;
        }

        public void run() {
            try {
                NotificationChannel notificationChannel = this.mChannelToUpdate;
                if (notificationChannel == null) {
                    this.mINotificationManager.setNotificationsEnabledWithImportanceLockForPackage(this.mPackageName, this.mAppUid, this.mNewImportance >= this.mCurrentImportance);
                } else if (this.mUnlockImportance) {
                    this.mINotificationManager.unlockNotificationChannel(this.mPackageName, this.mAppUid, notificationChannel.getId());
                } else {
                    notificationChannel.setImportance(this.mNewImportance);
                    this.mChannelToUpdate.lockFields(4);
                    this.mINotificationManager.updateNotificationChannelForPackage(this.mPackageName, this.mAppUid, this.mChannelToUpdate);
                }
            } catch (RemoteException e) {
                Log.e(NotificationInfo.TAG, "Unable to update notification importance", e);
            }
        }
    }

    private void logUiEvent(NotificationControlsEvent notificationControlsEvent) {
        StatusBarNotification statusBarNotification = this.mSbn;
        if (statusBarNotification != null) {
            this.mUiEventLogger.logWithInstanceId(notificationControlsEvent, statusBarNotification.getUid(), this.mSbn.getPackageName(), this.mSbn.getInstanceId());
        }
    }

    private LogMaker getLogMaker() {
        StatusBarNotification statusBarNotification = this.mSbn;
        if (statusBarNotification == null) {
            return new LogMaker(1621);
        }
        return statusBarNotification.getLogMaker().setCategory(1621);
    }

    private LogMaker importanceChangeLogMaker() {
        Integer num = this.mChosenImportance;
        return getLogMaker().setCategory(UCharacter.UnicodeBlock.SOGDIAN_ID).setType(4).setSubtype(Integer.valueOf(num != null ? num.intValue() : this.mStartingChannelImportance).intValue() - this.mStartingChannelImportance);
    }

    private LogMaker notificationControlsLogMaker() {
        return getLogMaker().setCategory(204).setType(1).setSubtype(0);
    }

    private int getAlertingBehavior() {
        if (!this.mShowAutomaticSetting || this.mSingleNotificationChannel.hasUserSetImportance()) {
            return this.mWasShownHighPriority ^ true ? 1 : 0;
        }
        return 2;
    }
}
