package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public class PartialConversationInfo extends LinearLayout implements NotificationGuts.GutsContent {
    static final int ACTION_SETTINGS = 5;
    private static final String TAG = "PartialConvoGuts";
    private String mAppName;
    private int mAppUid;
    private ChannelEditorDialogController mChannelEditorDialogController;
    private String mDelegatePkg;
    private NotificationGuts mGutsContainer;
    private INotificationManager mINotificationManager;
    private boolean mIsDeviceProvisioned;
    private boolean mIsNonBlockable;
    private NotificationChannel mNotificationChannel;
    private View.OnClickListener mOnDone = new PartialConversationInfo$$ExternalSyntheticLambda2(this);
    private NotificationInfo.OnSettingsClickListener mOnSettingsClickListener;
    private String mPackageName;
    private Drawable mPkgIcon;
    private PackageManager mPm;
    private boolean mPresentingChannelEditorDialog = false;
    private boolean mPressedApply;
    private StatusBarNotification mSbn;
    private int mSelectedAction = -1;
    boolean mSkipPost = false;
    private Set<NotificationChannel> mUniqueChannelsInRow;

    @Retention(RetentionPolicy.SOURCE)
    private @interface Action {
    }

    public View getContentView() {
        return this;
    }

    public boolean handleCloseControls(boolean z, boolean z2) {
        return false;
    }

    public boolean isAnimating() {
        return false;
    }

    public boolean needsFalsingProtection() {
        return true;
    }

    public void onFinishedClosing() {
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
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-notification-row-PartialConversationInfo */
    public /* synthetic */ void mo41724xcab7bb4(View view) {
        this.mPressedApply = true;
        this.mGutsContainer.closeControls(view, true);
    }

    public PartialConversationInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bindNotification(PackageManager packageManager, INotificationManager iNotificationManager, ChannelEditorDialogController channelEditorDialogController, String str, NotificationChannel notificationChannel, Set<NotificationChannel> set, NotificationEntry notificationEntry, NotificationInfo.OnSettingsClickListener onSettingsClickListener, boolean z, boolean z2) {
        this.mSelectedAction = -1;
        this.mINotificationManager = iNotificationManager;
        this.mPackageName = str;
        StatusBarNotification sbn = notificationEntry.getSbn();
        this.mSbn = sbn;
        this.mPm = packageManager;
        this.mAppName = this.mPackageName;
        this.mOnSettingsClickListener = onSettingsClickListener;
        this.mNotificationChannel = notificationChannel;
        this.mAppUid = sbn.getUid();
        this.mDelegatePkg = this.mSbn.getOpPkg();
        this.mIsDeviceProvisioned = z;
        this.mIsNonBlockable = z2;
        this.mChannelEditorDialogController = channelEditorDialogController;
        this.mUniqueChannelsInRow = set;
        bindHeader();
        bindActions();
        View findViewById = findViewById(C1894R.C1898id.turn_off_notifications);
        findViewById.setOnClickListener(getTurnOffNotificationsClickListener());
        findViewById.setVisibility((!findViewById.hasOnClickListeners() || this.mIsNonBlockable) ? 8 : 0);
        View findViewById2 = findViewById(C1894R.C1898id.done);
        findViewById2.setOnClickListener(this.mOnDone);
        findViewById2.setAccessibilityDelegate(this.mGutsContainer.getAccessibilityDelegate());
    }

    private void bindActions() {
        View.OnClickListener settingsOnClickListener = getSettingsOnClickListener();
        View findViewById = findViewById(C1894R.C1898id.info);
        findViewById.setOnClickListener(settingsOnClickListener);
        findViewById.setVisibility(findViewById.hasOnClickListeners() ? 0 : 8);
        findViewById(C1894R.C1898id.settings_link).setOnClickListener(settingsOnClickListener);
        ((TextView) findViewById(C1894R.C1898id.non_configurable_text)).setText(getResources().getString(C1894R.string.no_shortcut, new Object[]{this.mAppName}));
    }

    private void bindHeader() {
        bindPackage();
        bindDelegate();
    }

    private View.OnClickListener getSettingsOnClickListener() {
        int i = this.mAppUid;
        if (i < 0 || this.mOnSettingsClickListener == null || !this.mIsDeviceProvisioned) {
            return null;
        }
        return new PartialConversationInfo$$ExternalSyntheticLambda1(this, i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getSettingsOnClickListener$1$com-android-systemui-statusbar-notification-row-PartialConversationInfo */
    public /* synthetic */ void mo41721x294cccd3(int i, View view) {
        this.mOnSettingsClickListener.onClick(view, this.mNotificationChannel, i);
    }

    private View.OnClickListener getTurnOffNotificationsClickListener() {
        return new PartialConversationInfo$$ExternalSyntheticLambda0(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getTurnOffNotificationsClickListener$3$com-android-systemui-statusbar-notification-row-PartialConversationInfo */
    public /* synthetic */ void mo41723x5dabc6a1(View view) {
        ChannelEditorDialogController channelEditorDialogController;
        if (!this.mPresentingChannelEditorDialog && (channelEditorDialogController = this.mChannelEditorDialogController) != null) {
            this.mPresentingChannelEditorDialog = true;
            channelEditorDialogController.prepareDialogForApp(this.mAppName, this.mPackageName, this.mAppUid, this.mUniqueChannelsInRow, this.mPkgIcon, this.mOnSettingsClickListener);
            this.mChannelEditorDialogController.setOnFinishListener(new PartialConversationInfo$$ExternalSyntheticLambda3(this));
            this.mChannelEditorDialogController.show();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getTurnOffNotificationsClickListener$2$com-android-systemui-statusbar-notification-row-PartialConversationInfo */
    public /* synthetic */ void mo41722x969fdfa0() {
        this.mPresentingChannelEditorDialog = false;
        this.mGutsContainer.closeControls(this, false);
    }

    private void bindPackage() {
        try {
            ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(this.mPackageName, 795136);
            if (applicationInfo != null) {
                this.mAppName = String.valueOf((Object) this.mPm.getApplicationLabel(applicationInfo));
                this.mPkgIcon = this.mPm.getApplicationIcon(applicationInfo);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            this.mPkgIcon = this.mPm.getDefaultActivityIcon();
        }
        ((TextView) findViewById(C1894R.C1898id.name)).setText(this.mAppName);
        ((ImageView) findViewById(C1894R.C1898id.icon)).setImageDrawable(this.mPkgIcon);
    }

    private void bindDelegate() {
        TextView textView = (TextView) findViewById(C1894R.C1898id.delegate_name);
        if (!TextUtils.equals(this.mPackageName, this.mDelegatePkg)) {
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
    }

    private void bindGroup() {
        NotificationChannel notificationChannel = this.mNotificationChannel;
        CharSequence charSequence = null;
        if (!(notificationChannel == null || notificationChannel.getGroup() == null)) {
            try {
                NotificationChannelGroup notificationChannelGroupForPackage = this.mINotificationManager.getNotificationChannelGroupForPackage(this.mNotificationChannel.getGroup(), this.mPackageName, this.mAppUid);
                if (notificationChannelGroupForPackage != null) {
                    charSequence = notificationChannelGroupForPackage.getName();
                }
            } catch (RemoteException unused) {
            }
        }
        TextView textView = (TextView) findViewById(C1894R.C1898id.group_name);
        if (charSequence != null) {
            textView.setText(charSequence);
            textView.setVisibility(0);
            return;
        }
        textView.setVisibility(8);
    }

    public boolean post(Runnable runnable) {
        if (!this.mSkipPost) {
            return super.post(runnable);
        }
        runnable.run();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
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

    public void setGutsParent(NotificationGuts notificationGuts) {
        this.mGutsContainer = notificationGuts;
    }

    public boolean shouldBeSaved() {
        return this.mPressedApply;
    }

    public int getActualHeight() {
        return getHeight();
    }
}
