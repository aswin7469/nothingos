package com.android.systemui.p012qs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.VisibilityChangedDispatcher;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.util.ViewController;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.inject.Named;

@QSScope
/* renamed from: com.android.systemui.qs.QSSecurityFooter */
public class QSSecurityFooter extends ViewController<View> implements View.OnClickListener, DialogInterface.OnClickListener, VisibilityChangedDispatcher {
    protected static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final boolean DEBUG_FORCE_VISIBLE = false;
    protected static final String TAG = "QSSecurityFooter";
    /* access modifiers changed from: private */
    public final ActivityStarter mActivityStarter;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final Callback mCallback = new Callback();
    private Context mContext;
    /* access modifiers changed from: private */
    public AlertDialog mDialog;
    private final DialogLaunchAnimator mDialogLaunchAnimator;
    private final DevicePolicyManager mDpm;
    /* access modifiers changed from: private */
    public int mFooterIconId = C1894R.C1896drawable.ic_info_outline;
    /* access modifiers changed from: private */
    public final TextView mFooterText = ((TextView) this.mView.findViewById(C1894R.C1898id.footer_text));
    /* access modifiers changed from: private */
    public CharSequence mFooterTextContent = null;
    protected C2349H mHandler;
    /* access modifiers changed from: private */
    public boolean mIsVisible;
    private final Handler mMainHandler;
    private Supplier<String> mManagementDialogCaCertStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda9(this);
    private Supplier<String> mManagementDialogNetworkStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda12(this);
    private Supplier<String> mManagementDialogStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda8(this);
    private Supplier<String> mManagementMessageSupplier = new QSSecurityFooter$$ExternalSyntheticLambda14(this);
    private Supplier<String> mManagementMonitoringStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda15(this);
    private Supplier<String> mManagementMultipleVpnStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda16(this);
    private Supplier<String> mManagementTitleSupplier = new QSSecurityFooter$$ExternalSyntheticLambda7(this);
    private Supplier<String> mMonitoringSubtitleCaCertStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda19(this);
    private Supplier<String> mMonitoringSubtitleNetworkStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda20(this);
    private Supplier<String> mMonitoringSubtitleVpnStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda21(this);
    /* access modifiers changed from: private */
    public final ImageView mPrimaryFooterIcon = ((ImageView) this.mView.findViewById(C1894R.C1898id.primary_footer_icon));
    /* access modifiers changed from: private */
    public Drawable mPrimaryFooterIconDrawable;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.app.action.SHOW_DEVICE_MONITORING_DIALOG")) {
                QSSecurityFooter.this.showDeviceMonitoringDialog();
            }
        }
    };
    private final SecurityController mSecurityController;
    private final AtomicBoolean mShouldUseSettingsButton = new AtomicBoolean(false);
    private final Runnable mUpdateDisplayState = new Runnable() {
        public void run() {
            if (QSSecurityFooter.this.mFooterTextContent != null) {
                QSSecurityFooter.this.mFooterText.setText(QSSecurityFooter.this.mFooterTextContent);
            }
            QSSecurityFooter.this.mView.setVisibility(!QSSecurityFooter.this.mIsVisible ? 8 : 0);
            if (QSSecurityFooter.this.mVisibilityChangedListener != null) {
                QSSecurityFooter.this.mVisibilityChangedListener.onVisibilityChanged(QSSecurityFooter.this.mView.getVisibility());
            }
        }
    };
    private final Runnable mUpdatePrimaryIcon = new Runnable() {
        public void run() {
            if (QSSecurityFooter.this.mPrimaryFooterIconDrawable != null) {
                QSSecurityFooter.this.mPrimaryFooterIcon.setImageDrawable(QSSecurityFooter.this.mPrimaryFooterIconDrawable);
            } else {
                QSSecurityFooter.this.mPrimaryFooterIcon.setImageResource(QSSecurityFooter.this.mFooterIconId);
            }
        }
    };
    private final UserTracker mUserTracker;
    private Supplier<String> mViewPoliciesButtonStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda23(this);
    /* access modifiers changed from: private */
    public VisibilityChangedDispatcher.OnVisibilityChangedListener mVisibilityChangedListener;
    private Supplier<String> mWorkProfileDialogCaCertStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda10(this);
    private Supplier<String> mWorkProfileDialogNetworkStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda13(this);
    private Supplier<String> mWorkProfileMonitoringStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda17(this);
    private Supplier<String> mWorkProfileNetworkStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda18(this);

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2930lambda$new$0$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_title_device_owned);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2931lambda$new$1$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.quick_settings_disclosure_management);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2937lambda$new$2$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.quick_settings_disclosure_management_monitoring);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2938lambda$new$3$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.quick_settings_disclosure_management_vpns);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2939lambda$new$4$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.quick_settings_disclosure_managed_profile_monitoring);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$5$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2940lambda$new$5$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.quick_settings_disclosure_managed_profile_network_activity);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$6$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2941lambda$new$6$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_subtitle_ca_certificate);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$7$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2942lambda$new$7$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_subtitle_network_logging);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$8$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2943lambda$new$8$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_subtitle_vpn);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$9$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2944lambda$new$9$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_button_view_policies);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$10$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2932lambda$new$10$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_description_management);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$11$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2933lambda$new$11$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_description_management_ca_certificate);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$12$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2934lambda$new$12$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_description_managed_profile_ca_certificate);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$13$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2935lambda$new$13$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_description_management_network_logging);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$14$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2936lambda$new$14$comandroidsystemuiqsQSSecurityFooter() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(C1894R.string.monitoring_description_managed_profile_network_logging);
    }

    @Inject
    QSSecurityFooter(@Named("qs_security_footer") View view, UserTracker userTracker, @Main Handler handler, ActivityStarter activityStarter, SecurityController securityController, DialogLaunchAnimator dialogLaunchAnimator, @Background Looper looper, BroadcastDispatcher broadcastDispatcher) {
        super(view);
        this.mContext = view.getContext();
        this.mDpm = (DevicePolicyManager) view.getContext().getSystemService(DevicePolicyManager.class);
        this.mMainHandler = handler;
        this.mActivityStarter = activityStarter;
        this.mSecurityController = securityController;
        this.mHandler = new C2349H(looper);
        this.mUserTracker = userTracker;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mBroadcastDispatcher.registerReceiverWithHandler(this.mReceiver, new IntentFilter("android.app.action.SHOW_DEVICE_MONITORING_DIALOG"), this.mHandler, UserHandle.ALL);
        this.mView.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
        this.mView.setOnClickListener((View.OnClickListener) null);
    }

    public void setListening(boolean z) {
        if (z) {
            this.mSecurityController.addCallback(this.mCallback);
            refreshState();
            return;
        }
        this.mSecurityController.removeCallback(this.mCallback);
    }

    public void setOnVisibilityChangedListener(VisibilityChangedDispatcher.OnVisibilityChangedListener onVisibilityChangedListener) {
        this.mVisibilityChangedListener = onVisibilityChangedListener;
    }

    public void onConfigurationChanged() {
        FontSizeUtils.updateFontSize(this.mFooterText, C1894R.dimen.qs_tile_text_size);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.qs_footer_padding);
        this.mView.setPaddingRelative(dimensionPixelSize, 0, dimensionPixelSize, 0);
        this.mView.setBackground(this.mContext.getDrawable(C1894R.C1896drawable.qs_security_footer_background));
    }

    public View getView() {
        return this.mView;
    }

    public boolean hasFooter() {
        return this.mView.getVisibility() != 8;
    }

    public void onClick(View view) {
        if (hasFooter()) {
            this.mHandler.sendEmptyMessage(0);
        }
    }

    /* access modifiers changed from: private */
    public void handleClick() {
        showDeviceMonitoringDialog();
        DevicePolicyEventLogger.createEvent(57).write();
    }

    public void showDeviceMonitoringDialog() {
        createDialog();
    }

    public void refreshState() {
        this.mHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: private */
    public void handleRefreshState() {
        int i;
        boolean isDeviceManaged = this.mSecurityController.isDeviceManaged();
        UserInfo userInfo = this.mUserTracker.getUserInfo();
        boolean z = UserManager.isDeviceInDemoMode(this.mContext) && userInfo != null && userInfo.isDemo();
        boolean hasWorkProfile = this.mSecurityController.hasWorkProfile();
        boolean hasCACertInCurrentUser = this.mSecurityController.hasCACertInCurrentUser();
        boolean hasCACertInWorkProfile = this.mSecurityController.hasCACertInWorkProfile();
        boolean isNetworkLoggingEnabled = this.mSecurityController.isNetworkLoggingEnabled();
        String primaryVpnName = this.mSecurityController.getPrimaryVpnName();
        String workProfileVpnName = this.mSecurityController.getWorkProfileVpnName();
        CharSequence deviceOwnerOrganizationName = this.mSecurityController.getDeviceOwnerOrganizationName();
        CharSequence workProfileOrganizationName = this.mSecurityController.getWorkProfileOrganizationName();
        boolean isProfileOwnerOfOrganizationOwnedDevice = this.mSecurityController.isProfileOwnerOfOrganizationOwnedDevice();
        boolean isParentalControlsEnabled = this.mSecurityController.isParentalControlsEnabled();
        boolean isWorkProfileOn = this.mSecurityController.isWorkProfileOn();
        boolean z2 = hasCACertInWorkProfile || workProfileVpnName != null || (hasWorkProfile && isNetworkLoggingEnabled);
        boolean z3 = (isDeviceManaged && !z) || hasCACertInCurrentUser || primaryVpnName != null || isProfileOwnerOfOrganizationOwnedDevice || isParentalControlsEnabled || (z2 && isWorkProfileOn);
        this.mIsVisible = z3;
        if (!z3 || !isProfileOwnerOfOrganizationOwnedDevice || (z2 && isWorkProfileOn)) {
            this.mView.setClickable(true);
            this.mView.findViewById(C1894R.C1898id.footer_icon).setVisibility(0);
        } else {
            this.mView.setClickable(false);
            this.mView.findViewById(C1894R.C1898id.footer_icon).setVisibility(8);
        }
        this.mFooterTextContent = getFooterText(isDeviceManaged, hasWorkProfile, hasCACertInCurrentUser, hasCACertInWorkProfile, isNetworkLoggingEnabled, primaryVpnName, workProfileVpnName, deviceOwnerOrganizationName, workProfileOrganizationName, isProfileOwnerOfOrganizationOwnedDevice, isParentalControlsEnabled, isWorkProfileOn);
        if (primaryVpnName == null && workProfileVpnName == null) {
            i = C1894R.C1896drawable.ic_info_outline;
        } else {
            i = this.mSecurityController.isVpnBranded() ? C1894R.C1896drawable.stat_sys_branded_vpn : C1894R.C1896drawable.stat_sys_vpn_ic;
        }
        if (this.mFooterIconId != i) {
            this.mFooterIconId = i;
        }
        if (!isParentalControlsEnabled) {
            this.mPrimaryFooterIconDrawable = null;
        } else if (this.mPrimaryFooterIconDrawable == null) {
            this.mPrimaryFooterIconDrawable = this.mSecurityController.getIcon(this.mSecurityController.getDeviceAdminInfo());
        }
        this.mMainHandler.post(this.mUpdatePrimaryIcon);
        this.mMainHandler.post(this.mUpdateDisplayState);
    }

    /* access modifiers changed from: protected */
    public CharSequence getFooterText(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, String str, String str2, CharSequence charSequence, CharSequence charSequence2, boolean z6, boolean z7, boolean z8) {
        if (z7) {
            return this.mContext.getString(C1894R.string.quick_settings_disclosure_parental_controls);
        }
        if (!z) {
            return getManagedAndPersonalProfileFooterText(z2, z3, z4, z5, str, str2, charSequence2, z6, z8);
        }
        return getManagedDeviceFooterText(z3, z4, z5, str, str2, charSequence);
    }

    private String getManagedDeviceFooterText(boolean z, boolean z2, boolean z3, String str, String str2, CharSequence charSequence) {
        if (z || z2 || z3) {
            return getManagedDeviceMonitoringText(charSequence);
        }
        if (str == null && str2 == null) {
            return getMangedDeviceGeneralText(charSequence);
        }
        return getManagedDeviceVpnText(str, str2, charSequence);
    }

    private String getManagedDeviceMonitoringText(CharSequence charSequence) {
        if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_MANAGEMENT_MONITORING", this.mManagementMonitoringStringSupplier);
        }
        return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_MONITORING", new QSSecurityFooter$$ExternalSyntheticLambda26(this, charSequence), new Object[]{charSequence});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getManagedDeviceMonitoringText$15$com-android-systemui-qs-QSSecurityFooter */
    public /* synthetic */ String mo36258xc7125115(CharSequence charSequence) {
        return this.mContext.getString(C1894R.string.quick_settings_disclosure_named_management_monitoring, new Object[]{charSequence});
    }

    private String getManagedDeviceVpnText(String str, String str2, CharSequence charSequence) {
        if (str == null || str2 == null) {
            if (str == null) {
                str = str2;
            }
            if (charSequence == null) {
                return this.mDpm.getResources().getString("SystemUi.QS_MSG_MANAGEMENT_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda11(this, str), new Object[]{str});
            }
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda22(this, charSequence, str), new Object[]{charSequence, str});
        } else if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_MANAGEMENT_MULTIPLE_VPNS", this.mManagementMultipleVpnStringSupplier);
        } else {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_MULTIPLE_VPNS", new QSSecurityFooter$$ExternalSyntheticLambda0(this, charSequence), new Object[]{charSequence});
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getManagedDeviceVpnText$16$com-android-systemui-qs-QSSecurityFooter */
    public /* synthetic */ String mo36259xb5411768(CharSequence charSequence) {
        return this.mContext.getString(C1894R.string.quick_settings_disclosure_named_management_vpns, new Object[]{charSequence});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getManagedDeviceVpnText$17$com-android-systemui-qs-QSSecurityFooter */
    public /* synthetic */ String mo36260xbca64c87(String str) {
        return this.mContext.getString(C1894R.string.quick_settings_disclosure_management_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getManagedDeviceVpnText$18$com-android-systemui-qs-QSSecurityFooter */
    public /* synthetic */ String mo36261xc40b81a6(CharSequence charSequence, String str) {
        return this.mContext.getString(C1894R.string.quick_settings_disclosure_named_management_named_vpn, new Object[]{charSequence, str});
    }

    private String getMangedDeviceGeneralText(CharSequence charSequence) {
        if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_MANAGEMENT", this.mManagementMessageSupplier);
        }
        if (isFinancedDevice()) {
            return this.mContext.getString(C1894R.string.quick_settings_financed_disclosure_named_management, new Object[]{charSequence});
        }
        return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_MANAGEMENT", new QSSecurityFooter$$ExternalSyntheticLambda27(this, charSequence), new Object[]{charSequence});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getMangedDeviceGeneralText$19$com-android-systemui-qs-QSSecurityFooter */
    public /* synthetic */ String mo36263x4645137a(CharSequence charSequence) {
        return this.mContext.getString(C1894R.string.quick_settings_disclosure_named_management, new Object[]{charSequence});
    }

    private String getManagedAndPersonalProfileFooterText(boolean z, boolean z2, boolean z3, boolean z4, String str, String str2, CharSequence charSequence, boolean z5, boolean z6) {
        if (z2 || (z3 && z6)) {
            return getMonitoringText(z2, z3, charSequence, z6);
        }
        if (str != null || (str2 != null && z6)) {
            return getVpnText(z, str, str2, z6);
        }
        if (z && z4 && z6) {
            return getManagedProfileNetworkActivityText();
        }
        if (z5) {
            return getMangedDeviceGeneralText(charSequence);
        }
        return null;
    }

    private String getMonitoringText(boolean z, boolean z2, CharSequence charSequence, boolean z3) {
        if (!z2 || !z3) {
            if (z) {
                return this.mContext.getString(C1894R.string.quick_settings_disclosure_monitoring);
            }
            return null;
        } else if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_WORK_PROFILE_MONITORING", this.mWorkProfileMonitoringStringSupplier);
        } else {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_WORK_PROFILE_MONITORING", new QSSecurityFooter$$ExternalSyntheticLambda28(this, charSequence), new Object[]{charSequence});
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getMonitoringText$20$com-android-systemui-qs-QSSecurityFooter */
    public /* synthetic */ String mo36264x10b55e98(CharSequence charSequence) {
        return this.mContext.getString(C1894R.string.quick_settings_disclosure_named_managed_profile_monitoring, new Object[]{charSequence});
    }

    private String getVpnText(boolean z, String str, String str2, boolean z2) {
        if (str != null && str2 != null) {
            return this.mContext.getString(C1894R.string.quick_settings_disclosure_vpns);
        }
        if (str2 != null && z2) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_WORK_PROFILE_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda5(this, str2), new Object[]{str2});
        } else if (str == null) {
            return null;
        } else {
            if (z) {
                return this.mDpm.getResources().getString("SystemUi.QS_MSG_PERSONAL_PROFILE_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda6(this, str), new Object[]{str});
            }
            return this.mContext.getString(C1894R.string.quick_settings_disclosure_named_vpn, new Object[]{str});
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getVpnText$21$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2928lambda$getVpnText$21$comandroidsystemuiqsQSSecurityFooter(String str) {
        return this.mContext.getString(C1894R.string.quick_settings_disclosure_managed_profile_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getVpnText$22$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2929lambda$getVpnText$22$comandroidsystemuiqsQSSecurityFooter(String str) {
        return this.mContext.getString(C1894R.string.quick_settings_disclosure_personal_profile_named_vpn, new Object[]{str});
    }

    private String getManagedProfileNetworkActivityText() {
        return this.mDpm.getResources().getString("SystemUi.QS_MSG_WORK_PROFILE_NETWORK", this.mWorkProfileNetworkStringSupplier);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            Intent intent = new Intent("android.settings.ENTERPRISE_PRIVACY_SETTINGS");
            dialogInterface.dismiss();
            this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }
    }

    private void createDialog() {
        this.mShouldUseSettingsButton.set(false);
        this.mHandler.post(new QSSecurityFooter$$ExternalSyntheticLambda29(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createDialog$24$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ void m2922lambda$createDialog$24$comandroidsystemuiqsQSSecurityFooter() {
        this.mMainHandler.post(new QSSecurityFooter$$ExternalSyntheticLambda24(this, getSettingsButton(), createDialogView()));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createDialog$23$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ void m2921lambda$createDialog$23$comandroidsystemuiqsQSSecurityFooter(String str, View view) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext, 0);
        this.mDialog = systemUIDialog;
        systemUIDialog.requestWindowFeature(1);
        this.mDialog.setButton(-1, getPositiveButton(), this);
        AlertDialog alertDialog = this.mDialog;
        if (!this.mShouldUseSettingsButton.get()) {
            str = getNegativeButton();
        }
        alertDialog.setButton(-2, str, this);
        this.mDialog.setView(view);
        if (this.mView.isAggregatedVisible()) {
            this.mDialogLaunchAnimator.showFromView(this.mDialog, this.mView);
        } else {
            this.mDialog.show();
        }
    }

    /* access modifiers changed from: package-private */
    public Dialog getDialog() {
        return this.mDialog;
    }

    /* access modifiers changed from: package-private */
    public View createDialogView() {
        if (this.mSecurityController.isParentalControlsEnabled()) {
            return createParentalControlsDialogView();
        }
        return createOrganizationDialogView();
    }

    private View createOrganizationDialogView() {
        boolean isDeviceManaged = this.mSecurityController.isDeviceManaged();
        boolean hasWorkProfile = this.mSecurityController.hasWorkProfile();
        CharSequence deviceOwnerOrganizationName = this.mSecurityController.getDeviceOwnerOrganizationName();
        boolean hasCACertInCurrentUser = this.mSecurityController.hasCACertInCurrentUser();
        boolean hasCACertInWorkProfile = this.mSecurityController.hasCACertInWorkProfile();
        boolean isNetworkLoggingEnabled = this.mSecurityController.isNetworkLoggingEnabled();
        String primaryVpnName = this.mSecurityController.getPrimaryVpnName();
        String workProfileVpnName = this.mSecurityController.getWorkProfileVpnName();
        boolean z = false;
        View inflate = LayoutInflater.from(this.mContext).inflate(C1894R.layout.quick_settings_footer_dialog, (ViewGroup) null, false);
        ((TextView) inflate.findViewById(C1894R.C1898id.device_management_subtitle)).setText(getManagementTitle(deviceOwnerOrganizationName));
        CharSequence managementMessage = getManagementMessage(isDeviceManaged, deviceOwnerOrganizationName);
        if (managementMessage == null) {
            inflate.findViewById(C1894R.C1898id.device_management_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(C1894R.C1898id.device_management_disclosures).setVisibility(0);
            ((TextView) inflate.findViewById(C1894R.C1898id.device_management_warning)).setText(managementMessage);
            this.mShouldUseSettingsButton.set(true);
        }
        CharSequence caCertsMessage = getCaCertsMessage(isDeviceManaged, hasCACertInCurrentUser, hasCACertInWorkProfile);
        if (caCertsMessage == null) {
            inflate.findViewById(C1894R.C1898id.ca_certs_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(C1894R.C1898id.ca_certs_disclosures).setVisibility(0);
            TextView textView = (TextView) inflate.findViewById(C1894R.C1898id.ca_certs_warning);
            textView.setText(caCertsMessage);
            textView.setMovementMethod(new LinkMovementMethod());
            ((TextView) inflate.findViewById(C1894R.C1898id.ca_certs_subtitle)).setText(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MONITORING_CA_CERT_SUBTITLE", this.mMonitoringSubtitleCaCertStringSupplier));
        }
        CharSequence networkLoggingMessage = getNetworkLoggingMessage(isDeviceManaged, isNetworkLoggingEnabled);
        if (networkLoggingMessage == null) {
            inflate.findViewById(C1894R.C1898id.network_logging_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(C1894R.C1898id.network_logging_disclosures).setVisibility(0);
            ((TextView) inflate.findViewById(C1894R.C1898id.network_logging_warning)).setText(networkLoggingMessage);
            ((TextView) inflate.findViewById(C1894R.C1898id.network_logging_subtitle)).setText(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MONITORING_NETWORK_SUBTITLE", this.mMonitoringSubtitleNetworkStringSupplier));
        }
        CharSequence vpnMessage = getVpnMessage(isDeviceManaged, hasWorkProfile, primaryVpnName, workProfileVpnName);
        if (vpnMessage == null) {
            inflate.findViewById(C1894R.C1898id.vpn_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(C1894R.C1898id.vpn_disclosures).setVisibility(0);
            TextView textView2 = (TextView) inflate.findViewById(C1894R.C1898id.vpn_warning);
            textView2.setText(vpnMessage);
            textView2.setMovementMethod(new LinkMovementMethod());
            ((TextView) inflate.findViewById(C1894R.C1898id.vpn_subtitle)).setText(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MONITORING_VPN_SUBTITLE", this.mMonitoringSubtitleVpnStringSupplier));
        }
        boolean z2 = managementMessage != null;
        boolean z3 = caCertsMessage != null;
        boolean z4 = networkLoggingMessage != null;
        if (vpnMessage != null) {
            z = true;
        }
        configSubtitleVisibility(z2, z3, z4, z, inflate);
        return inflate;
    }

    private View createParentalControlsDialogView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(C1894R.layout.quick_settings_footer_dialog_parental_controls, (ViewGroup) null, false);
        DeviceAdminInfo deviceAdminInfo = this.mSecurityController.getDeviceAdminInfo();
        Drawable icon = this.mSecurityController.getIcon(deviceAdminInfo);
        if (icon != null) {
            ((ImageView) inflate.findViewById(C1894R.C1898id.parental_controls_icon)).setImageDrawable(icon);
        }
        ((TextView) inflate.findViewById(C1894R.C1898id.parental_controls_title)).setText(this.mSecurityController.getLabel(deviceAdminInfo));
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void configSubtitleVisibility(boolean z, boolean z2, boolean z3, boolean z4, View view) {
        if (!z) {
            int i = z3 ? (z2 ? 1 : 0) + true : z2;
            if (z4) {
                i++;
            }
            if (i == 1) {
                if (z2) {
                    view.findViewById(C1894R.C1898id.ca_certs_subtitle).setVisibility(8);
                }
                if (z3) {
                    view.findViewById(C1894R.C1898id.network_logging_subtitle).setVisibility(8);
                }
                if (z4) {
                    view.findViewById(C1894R.C1898id.vpn_subtitle).setVisibility(8);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String getSettingsButton() {
        return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_VIEW_POLICIES", this.mViewPoliciesButtonStringSupplier);
    }

    private String getPositiveButton() {
        return this.mContext.getString(C1894R.string.f262ok);
    }

    private String getNegativeButton() {
        if (this.mSecurityController.isParentalControlsEnabled()) {
            return this.mContext.getString(C1894R.string.monitoring_button_view_controls);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public CharSequence getManagementMessage(boolean z, CharSequence charSequence) {
        if (!z) {
            return null;
        }
        if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT", this.mManagementDialogStringSupplier);
        }
        if (isFinancedDevice()) {
            return this.mContext.getString(C1894R.string.monitoring_financed_description_named_management, new Object[]{charSequence, charSequence});
        }
        return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_NAMED_MANAGEMENT", new QSSecurityFooter$$ExternalSyntheticLambda25(this, charSequence), new Object[]{charSequence});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getManagementMessage$25$com-android-systemui-qs-QSSecurityFooter */
    public /* synthetic */ String mo36262xca8ef810(CharSequence charSequence) {
        return this.mContext.getString(C1894R.string.monitoring_description_named_management, new Object[]{charSequence});
    }

    /* access modifiers changed from: protected */
    public CharSequence getCaCertsMessage(boolean z, boolean z2, boolean z3) {
        if (!z2 && !z3) {
            return null;
        }
        if (z) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_CA_CERT", this.mManagementDialogCaCertStringSupplier);
        }
        if (z3) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_WORK_PROFILE_CA_CERT", this.mWorkProfileDialogCaCertStringSupplier);
        }
        return this.mContext.getString(C1894R.string.monitoring_description_ca_certificate);
    }

    /* access modifiers changed from: protected */
    public CharSequence getNetworkLoggingMessage(boolean z, boolean z2) {
        if (!z2) {
            return null;
        }
        if (z) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_NETWORK", this.mManagementDialogNetworkStringSupplier);
        }
        return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_WORK_PROFILE_NETWORK", this.mWorkProfileDialogNetworkStringSupplier);
    }

    /* access modifiers changed from: protected */
    public CharSequence getVpnMessage(boolean z, boolean z2, String str, String str2) {
        if (str == null && str2 == null) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (z) {
            if (str == null || str2 == null) {
                if (str == null) {
                    str = str2;
                }
                spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda1(this, str), new Object[]{str}));
            } else {
                spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_TWO_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda30(this, str, str2), new Object[]{str, str2}));
            }
        } else if (str != null && str2 != null) {
            spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_TWO_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda2(this, str, str2), new Object[]{str, str2}));
        } else if (str2 != null) {
            spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_WORK_PROFILE_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda3(this, str2), new Object[]{str2}));
        } else if (z2) {
            spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_PERSONAL_PROFILE_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda4(this, str), new Object[]{str}));
        } else {
            spannableStringBuilder.append(this.mContext.getString(C1894R.string.monitoring_description_named_vpn, new Object[]{str}));
        }
        spannableStringBuilder.append(this.mContext.getString(C1894R.string.monitoring_description_vpn_settings_separator));
        spannableStringBuilder.append(this.mContext.getString(C1894R.string.monitoring_description_vpn_settings), new VpnSpan(), 0);
        return spannableStringBuilder;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getVpnMessage$26$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2923lambda$getVpnMessage$26$comandroidsystemuiqsQSSecurityFooter(String str, String str2) {
        return this.mContext.getString(C1894R.string.monitoring_description_two_named_vpns, new Object[]{str, str2});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getVpnMessage$27$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2924lambda$getVpnMessage$27$comandroidsystemuiqsQSSecurityFooter(String str) {
        return this.mContext.getString(C1894R.string.monitoring_description_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getVpnMessage$28$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2925lambda$getVpnMessage$28$comandroidsystemuiqsQSSecurityFooter(String str, String str2) {
        return this.mContext.getString(C1894R.string.monitoring_description_two_named_vpns, new Object[]{str, str2});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getVpnMessage$29$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2926lambda$getVpnMessage$29$comandroidsystemuiqsQSSecurityFooter(String str) {
        return this.mContext.getString(C1894R.string.monitoring_description_managed_profile_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getVpnMessage$30$com-android-systemui-qs-QSSecurityFooter  reason: not valid java name */
    public /* synthetic */ String m2927lambda$getVpnMessage$30$comandroidsystemuiqsQSSecurityFooter(String str) {
        return this.mContext.getString(C1894R.string.monitoring_description_personal_profile_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: package-private */
    public CharSequence getManagementTitle(CharSequence charSequence) {
        if (charSequence == null || !isFinancedDevice()) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_TITLE", this.mManagementTitleSupplier);
        }
        return this.mContext.getString(C1894R.string.monitoring_title_financed_device, new Object[]{charSequence});
    }

    private boolean isFinancedDevice() {
        if (this.mSecurityController.isDeviceManaged()) {
            SecurityController securityController = this.mSecurityController;
            if (securityController.getDeviceOwnerType(securityController.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: com.android.systemui.qs.QSSecurityFooter$Callback */
    private class Callback implements SecurityController.SecurityControllerCallback {
        private Callback() {
        }

        public void onStateChanged() {
            QSSecurityFooter.this.refreshState();
        }
    }

    /* renamed from: com.android.systemui.qs.QSSecurityFooter$H */
    private class C2349H extends Handler {
        private static final int CLICK = 0;
        private static final int REFRESH_STATE = 1;

        private C2349H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            try {
                if (message.what == 1) {
                    QSSecurityFooter.this.handleRefreshState();
                } else if (message.what == 0) {
                    QSSecurityFooter.this.handleClick();
                }
            } catch (Throwable th) {
                Log.w(QSSecurityFooter.TAG, "Error in " + null, th);
            }
        }
    }

    /* renamed from: com.android.systemui.qs.QSSecurityFooter$VpnSpan */
    protected class VpnSpan extends ClickableSpan {
        public int hashCode() {
            return 314159257;
        }

        protected VpnSpan() {
        }

        public void onClick(View view) {
            Intent intent = new Intent("android.settings.VPN_SETTINGS");
            QSSecurityFooter.this.mDialog.dismiss();
            QSSecurityFooter.this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }

        public boolean equals(Object obj) {
            return obj instanceof VpnSpan;
        }
    }
}
