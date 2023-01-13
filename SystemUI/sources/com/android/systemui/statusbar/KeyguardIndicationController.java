package com.android.systemui.statusbar;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.ViewClippingUtil;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.systemui.C1894R;
import com.android.systemui.DejankUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.KeyguardIndicationControllerEx;
import java.p026io.PrintWriter;
import java.text.NumberFormat;
import javax.inject.Inject;

@SysUISingleton
public class KeyguardIndicationController {
    private static final float BOUNCE_ANIMATION_FINAL_Y = 0.0f;
    private static final boolean DEBUG_CHARGING_SPEED = false;
    private static final int MSG_HIDE_BIOMETRIC_MESSAGE = 3;
    private static final int MSG_HIDE_TRANSIENT = 1;
    private static final int MSG_SHOW_ACTION_TO_UNLOCK = 2;
    private static final String TAG = "KeyguardIndication";
    private static final long TRANSIENT_BIOMETRIC_ERROR_TIMEOUT = 1300;
    private String mAlignmentIndication;
    @Background
    protected final DelayableExecutor mBackgroundExecutor;
    /* access modifiers changed from: private */
    public final IBatteryStats mBatteryInfo;
    /* access modifiers changed from: private */
    public int mBatteryLevel;
    /* access modifiers changed from: private */
    public boolean mBatteryOverheated;
    /* access modifiers changed from: private */
    public boolean mBatteryPresent = true;
    private CharSequence mBiometricMessage;
    /* access modifiers changed from: private */
    public boolean mBouncerShow;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private BroadcastReceiver mBroadcastReceiver;
    /* access modifiers changed from: private */
    public int mChargingSpeed;
    /* access modifiers changed from: private */
    public long mChargingTimeRemaining;
    /* access modifiers changed from: private */
    public int mChargingWattage;
    private final ViewClippingUtil.ClippingParameters mClippingParams = new ViewClippingUtil.ClippingParameters() {
        public boolean shouldFinish(View view) {
            return view == KeyguardIndicationController.this.mIndicationArea;
        }
    };
    /* access modifiers changed from: private */
    public final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final DockManager mDockManager;
    /* access modifiers changed from: private */
    public boolean mDozing;
    /* access modifiers changed from: private */
    public boolean mEnableBatteryDefender;
    @Main
    protected final DelayableExecutor mExecutor;
    private final FalsingManager mFalsingManager;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private final IActivityManager mIActivityManager;
    /* access modifiers changed from: private */
    public ViewGroup mIndicationArea;
    private boolean mInited;
    protected ColorStateList mInitialTextColorState;
    /* access modifiers changed from: private */
    public final KeyguardBypassController mKeyguardBypassController;
    private KeyguardStateController.Callback mKeyguardStateCallback;
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LockPatternUtils mLockPatternUtils;
    private KeyguardIndicationTextView mLockScreenIndicationView;
    /* access modifiers changed from: private */
    public String mMessageToShowOnScreenOn;
    private boolean mOrganizationOwnedDevice;
    /* access modifiers changed from: private */
    public KeyguardIndicationTextView mOwnInfoIndicationView;
    /* access modifiers changed from: private */
    public boolean mPowerCharged;
    /* access modifiers changed from: private */
    public boolean mPowerPluggedIn;
    /* access modifiers changed from: private */
    public boolean mPowerPluggedInDock;
    /* access modifiers changed from: private */
    public boolean mPowerPluggedInWired;
    /* access modifiers changed from: private */
    public boolean mPowerPluggedInWireless;
    private String mRestingIndication;
    protected KeyguardIndicationRotateTextViewController mRotateTextViewController;
    /* access modifiers changed from: private */
    public ScreenLifecycle mScreenLifecycle;
    private final ScreenLifecycle.Observer mScreenObserver;
    /* access modifiers changed from: private */
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    protected final StatusBarStateController mStatusBarStateController;
    private StatusBarStateController.StateListener mStatusBarStateListener;
    /* access modifiers changed from: private */
    public KeyguardIndicationTextView mTopIndicationView;
    private CharSequence mTransientIndication;
    /* access modifiers changed from: private */
    public CharSequence mTrustGrantedIndication;
    private KeyguardUpdateMonitorCallback mUpdateMonitorCallback;
    private final UserManager mUserManager;
    /* access modifiers changed from: private */
    public boolean mVisible;
    private final SettableWakeLock mWakeLock;

    private String getTrustManagedIndication() {
        return null;
    }

    @Inject
    public KeyguardIndicationController(Context context, @Main Looper looper, WakeLock.Builder builder, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, IBatteryStats iBatteryStats, UserManager userManager, @Main DelayableExecutor delayableExecutor, @Background DelayableExecutor delayableExecutor2, FalsingManager falsingManager, LockPatternUtils lockPatternUtils, ScreenLifecycle screenLifecycle, IActivityManager iActivityManager, KeyguardBypassController keyguardBypassController) {
        ScreenLifecycle screenLifecycle2 = screenLifecycle;
        C25552 r2 = new ScreenLifecycle.Observer() {
            public void onScreenTurnedOn() {
                if (KeyguardIndicationController.this.mMessageToShowOnScreenOn != null) {
                    KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                    keyguardIndicationController.showBiometricMessage((CharSequence) keyguardIndicationController.mMessageToShowOnScreenOn);
                    KeyguardIndicationController.this.hideBiometricMessageDelayed(5000);
                    String unused = KeyguardIndicationController.this.mMessageToShowOnScreenOn = null;
                }
            }
        };
        this.mScreenObserver = r2;
        this.mBouncerShow = false;
        this.mStatusBarStateListener = new StatusBarStateController.StateListener() {
            public void onStateChanged(int i) {
                KeyguardIndicationController.this.setVisible(i == 1);
                if (i != 1) {
                    KeyguardIndicationController.this.mOwnInfoIndicationView.clearMessages();
                }
            }

            public void onDozingChanged(boolean z) {
                if (KeyguardIndicationController.this.mDozing != z) {
                    boolean unused = KeyguardIndicationController.this.mDozing = z;
                    if (KeyguardIndicationController.this.mDozing) {
                        KeyguardIndicationController.this.hideBiometricMessage();
                    }
                    KeyguardIndicationController.this.updateDeviceEntryIndication(false);
                }
            }
        };
        this.mKeyguardStateCallback = new KeyguardStateController.Callback() {
            public void onUnlockedChanged() {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }

            public void onKeyguardShowingChanged() {
                if (!KeyguardIndicationController.this.mKeyguardStateController.isShowing()) {
                    KeyguardIndicationController.this.mTopIndicationView.clearMessages();
                    KeyguardIndicationController.this.mRotateTextViewController.clearMessages();
                    return;
                }
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        };
        this.mContext = context;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mDockManager = dockManager;
        WakeLock.Builder builder2 = builder;
        this.mWakeLock = new SettableWakeLock(builder.setTag("Doze:KeyguardIndication").build(), TAG);
        this.mBatteryInfo = iBatteryStats;
        this.mUserManager = userManager;
        this.mExecutor = delayableExecutor;
        this.mBackgroundExecutor = delayableExecutor2;
        this.mLockPatternUtils = lockPatternUtils;
        this.mIActivityManager = iActivityManager;
        this.mFalsingManager = falsingManager;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mScreenLifecycle = screenLifecycle2;
        screenLifecycle2.addObserver(r2);
        Looper looper2 = looper;
        this.mHandler = new Handler(looper) {
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    KeyguardIndicationController.this.hideTransientIndication();
                } else if (message.what == 2) {
                    KeyguardIndicationController.this.showActionToUnlock();
                } else if (message.what == 3) {
                    KeyguardIndicationController.this.hideBiometricMessage();
                }
            }
        };
    }

    public void init() {
        if (!this.mInited) {
            this.mInited = true;
            this.mDockManager.addAlignmentStateListener(new KeyguardIndicationController$$ExternalSyntheticLambda2(this));
            this.mKeyguardUpdateMonitor.registerCallback(getKeyguardCallback());
            this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
            this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
            this.mStatusBarStateListener.onDozingChanged(this.mStatusBarStateController.isDozing());
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$init$1$com-android-systemui-statusbar-KeyguardIndicationController */
    public /* synthetic */ void mo38588xf7d60e40(int i) {
        this.mHandler.post(new KeyguardIndicationController$$ExternalSyntheticLambda5(this, i));
    }

    public void setIndicationArea(ViewGroup viewGroup) {
        this.mIndicationArea = viewGroup;
        this.mTopIndicationView = (KeyguardIndicationTextView) viewGroup.findViewById(C1894R.C1898id.keyguard_indication_text);
        this.mLockScreenIndicationView = (KeyguardIndicationTextView) viewGroup.findViewById(C1894R.C1898id.keyguard_indication_text_bottom);
        this.mOwnInfoIndicationView = (KeyguardIndicationTextView) viewGroup.findViewById(C1894R.C1898id.keyguard_indication_owner_info_text);
        KeyguardIndicationTextView keyguardIndicationTextView = this.mTopIndicationView;
        this.mInitialTextColorState = keyguardIndicationTextView != null ? keyguardIndicationTextView.getTextColors() : ColorStateList.valueOf(-1);
        this.mRotateTextViewController = new KeyguardIndicationRotateTextViewController(this.mLockScreenIndicationView, this.mExecutor, this.mStatusBarStateController);
        updateDeviceEntryIndication(false);
        updateOrganizedOwnedDevice();
        if (this.mBroadcastReceiver == null) {
            this.mBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    KeyguardIndicationController.this.updateOrganizedOwnedDevice();
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.USER_REMOVED");
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        }
    }

    public void destroy() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
    }

    /* access modifiers changed from: private */
    /* renamed from: handleAlignStateChanged */
    public void mo38587x3d606dbf(int i) {
        String str;
        if (i == 1) {
            str = this.mContext.getResources().getString(C1894R.string.dock_alignment_slow_charging);
        } else {
            str = i == 2 ? this.mContext.getResources().getString(C1894R.string.dock_alignment_not_charging) : "";
        }
        if (!str.equals(this.mAlignmentIndication)) {
            this.mAlignmentIndication = str;
            updateDeviceEntryIndication(false);
        }
    }

    /* access modifiers changed from: protected */
    public KeyguardUpdateMonitorCallback getKeyguardCallback() {
        if (this.mUpdateMonitorCallback == null) {
            this.mUpdateMonitorCallback = new BaseKeyguardCallback();
        }
        return this.mUpdateMonitorCallback;
    }

    private void updateLockScreenIndications(boolean z, int i) {
        updateBiometricMessage();
        updateTransient();
        updateLockScreenDisclosureMsg();
        updateLockScreenOwnerInfo();
        updateLockScreenBatteryMsg(z);
        updateLockScreenUserLockedMsg(i);
        updateLockScreenTrustMsg(i, getTrustGrantedIndication(), getTrustManagedIndication());
        updateLockScreenAlignmentMsg();
        updateLockScreenLogoutView();
        updateLockScreenRestingMsg();
    }

    /* access modifiers changed from: private */
    public void updateOrganizedOwnedDevice() {
        this.mOrganizationOwnedDevice = ((Boolean) DejankUtils.whitelistIpcs(new KeyguardIndicationController$$ExternalSyntheticLambda9(this))).booleanValue();
        updateDeviceEntryIndication(false);
    }

    private void updateLockScreenDisclosureMsg() {
        if (this.mOrganizationOwnedDevice) {
            this.mBackgroundExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda1(this));
        } else {
            this.mRotateTextViewController.hideIndication(1);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateLockScreenDisclosureMsg$3$com-android-systemui-statusbar-KeyguardIndicationController */
    public /* synthetic */ void mo38592x379d10ec() {
        this.mExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda10(this, getDisclosureText(getOrganizationOwnedDeviceOrganizationName())));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateLockScreenDisclosureMsg$2$com-android-systemui-statusbar-KeyguardIndicationController */
    public /* synthetic */ void mo38591x7d27706b(CharSequence charSequence) {
        if (this.mKeyguardStateController.isShowing()) {
            this.mRotateTextViewController.updateIndication(1, new KeyguardIndication.Builder().setMessage(charSequence).setTextColor(this.mInitialTextColorState).build(), false);
        }
    }

    private CharSequence getDisclosureText(CharSequence charSequence) {
        Resources resources = this.mContext.getResources();
        if (charSequence == null) {
            return this.mDevicePolicyManager.getResources().getString("SystemUi.KEYGUARD_MANAGEMENT_DISCLOSURE", new KeyguardIndicationController$$ExternalSyntheticLambda7(resources));
        }
        if (this.mDevicePolicyManager.isDeviceManaged()) {
            DevicePolicyManager devicePolicyManager = this.mDevicePolicyManager;
            if (devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return resources.getString(C1894R.string.do_financed_disclosure_with_name, new Object[]{charSequence});
            }
        }
        return this.mDevicePolicyManager.getResources().getString("SystemUi.KEYGUARD_NAMED_MANAGEMENT_DISCLOSURE", new KeyguardIndicationController$$ExternalSyntheticLambda8(resources, charSequence), new Object[]{charSequence});
    }

    /* access modifiers changed from: private */
    public void updateLockScreenOwnerInfo() {
        this.mBackgroundExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateLockScreenOwnerInfo$7$com-android-systemui-statusbar-KeyguardIndicationController */
    public /* synthetic */ void mo38595xcc59335d() {
        String deviceOwnerInfo = this.mLockPatternUtils.getDeviceOwnerInfo();
        if (deviceOwnerInfo == null && this.mLockPatternUtils.isOwnerInfoEnabled(KeyguardUpdateMonitor.getCurrentUser())) {
            deviceOwnerInfo = this.mLockPatternUtils.getOwnerInfo(KeyguardUpdateMonitor.getCurrentUser());
        }
        this.mExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda11(this, deviceOwnerInfo));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateLockScreenOwnerInfo$6$com-android-systemui-statusbar-KeyguardIndicationController */
    public /* synthetic */ void mo38594x11e392dc(String str) {
        if (TextUtils.isEmpty(str) || !this.mKeyguardStateController.isShowing() || this.mBouncerShow) {
            this.mOwnInfoIndicationView.setVisibility(8);
            return;
        }
        this.mOwnInfoIndicationView.setVisibility(0);
        this.mOwnInfoIndicationView.switchIndication(str, (KeyguardIndication) null, false, (Runnable) null);
    }

    private void updateLockScreenBatteryMsg(boolean z) {
        if (this.mPowerPluggedIn || this.mEnableBatteryDefender) {
            this.mRotateTextViewController.updateIndication(3, new KeyguardIndication.Builder().setMessage(computePowerIndication()).setTextColor(this.mInitialTextColorState).build(), z);
            return;
        }
        this.mRotateTextViewController.hideIndication(3);
    }

    private void updateLockScreenUserLockedMsg(int i) {
        if (!this.mKeyguardUpdateMonitor.isUserUnlocked(i)) {
            this.mRotateTextViewController.updateIndication(8, new KeyguardIndication.Builder().setMessage(((KeyguardIndicationControllerEx) NTDependencyEx.get(KeyguardIndicationControllerEx.class)).getUserLockedIndication()).setTextColor(this.mInitialTextColorState).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(8);
        }
    }

    private void updateBiometricMessage() {
        if (this.mDozing) {
            updateDeviceEntryIndication(false);
        } else if (!TextUtils.isEmpty(this.mBiometricMessage)) {
            this.mRotateTextViewController.updateIndication(11, new KeyguardIndication.Builder().setMessage(this.mBiometricMessage).setMinVisibilityMillis(Long.valueOf((long) KeyguardIndicationRotateTextViewController.IMPORTANT_MSG_MIN_DURATION)).setTextColor(this.mInitialTextColorState).build(), true);
        } else {
            this.mRotateTextViewController.hideIndication(11);
        }
    }

    private void updateTransient() {
        if (this.mDozing) {
            updateDeviceEntryIndication(false);
        } else if (!TextUtils.isEmpty(this.mTransientIndication)) {
            this.mRotateTextViewController.showTransient(this.mTransientIndication);
        } else {
            this.mRotateTextViewController.hideTransient();
        }
    }

    private void updateLockScreenTrustMsg(int i, CharSequence charSequence, CharSequence charSequence2) {
        boolean userHasTrust = this.mKeyguardUpdateMonitor.getUserHasTrust(i);
        if (!TextUtils.isEmpty(charSequence) && userHasTrust) {
            this.mRotateTextViewController.updateIndication(6, new KeyguardIndication.Builder().setMessage(charSequence).setTextColor(this.mInitialTextColorState).build(), true);
            hideBiometricMessage();
        } else if (TextUtils.isEmpty(charSequence2) || !this.mKeyguardUpdateMonitor.getUserTrustIsManaged(i) || userHasTrust) {
            this.mRotateTextViewController.hideIndication(6);
        } else {
            this.mRotateTextViewController.updateIndication(6, new KeyguardIndication.Builder().setMessage(charSequence2).setTextColor(this.mInitialTextColorState).build(), false);
        }
    }

    private void updateLockScreenAlignmentMsg() {
        if (!TextUtils.isEmpty(this.mAlignmentIndication)) {
            this.mRotateTextViewController.updateIndication(4, new KeyguardIndication.Builder().setMessage(this.mAlignmentIndication).setTextColor(ColorStateList.valueOf(this.mContext.getColor(C1894R.C1895color.misalignment_text_color))).build(), true);
        } else {
            this.mRotateTextViewController.hideIndication(4);
        }
    }

    private void updateLockScreenRestingMsg() {
        if (TextUtils.isEmpty(this.mRestingIndication) || this.mRotateTextViewController.hasIndications()) {
            this.mRotateTextViewController.hideIndication(7);
        } else {
            this.mRotateTextViewController.updateIndication(7, new KeyguardIndication.Builder().setMessage(this.mRestingIndication).setTextColor(this.mInitialTextColorState).build(), false);
        }
    }

    private void updateLockScreenLogoutView() {
        if (this.mKeyguardUpdateMonitor.isLogoutEnabled() && KeyguardUpdateMonitor.getCurrentUser() != 0) {
            this.mRotateTextViewController.updateIndication(2, new KeyguardIndication.Builder().setMessage(this.mContext.getResources().getString(17040402)).setTextColor(Utils.getColorAttr(this.mContext, 17957103)).setBackground(this.mContext.getDrawable(C1894R.C1896drawable.logout_button_background)).setClickListener(new KeyguardIndicationController$$ExternalSyntheticLambda6(this)).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(2);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateLockScreenLogoutView$8$com-android-systemui-statusbar-KeyguardIndicationController */
    public /* synthetic */ void mo38593xb29e1346(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            KeyguardUpdateMonitor.getCurrentUser();
            this.mDevicePolicyManager.logoutUser();
        }
    }

    /* access modifiers changed from: private */
    public boolean isOrganizationOwnedDevice() {
        return this.mDevicePolicyManager.isDeviceManaged() || this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    private CharSequence getOrganizationOwnedDeviceOrganizationName() {
        if (this.mDevicePolicyManager.isDeviceManaged()) {
            return this.mDevicePolicyManager.getDeviceOwnerOrganizationName();
        }
        if (this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile()) {
            return getWorkProfileOrganizationName();
        }
        return null;
    }

    private CharSequence getWorkProfileOrganizationName() {
        int workProfileUserId = getWorkProfileUserId(UserHandle.myUserId());
        if (workProfileUserId == -10000) {
            return null;
        }
        return this.mDevicePolicyManager.getOrganizationNameForUser(workProfileUserId);
    }

    private int getWorkProfileUserId(int i) {
        for (UserInfo userInfo : this.mUserManager.getProfiles(i)) {
            if (userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        return -10000;
    }

    public void setVisible(boolean z) {
        this.mVisible = z;
        this.mIndicationArea.setVisibility(z ? 0 : 8);
        if (z) {
            if (!this.mHandler.hasMessages(1)) {
                hideTransientIndication();
            }
            updateDeviceEntryIndication(false);
        } else if (!z) {
            hideTransientIndication();
        }
    }

    public void setRestingIndication(String str) {
        this.mRestingIndication = str;
        updateDeviceEntryIndication(false);
    }

    /* access modifiers changed from: package-private */
    public String getTrustGrantedIndication() {
        if (TextUtils.isEmpty(this.mTrustGrantedIndication)) {
            return this.mContext.getString(C1894R.string.keyguard_indication_trust_unlocked);
        }
        return this.mTrustGrantedIndication.toString();
    }

    /* access modifiers changed from: package-private */
    public void setPowerPluggedIn(boolean z) {
        this.mPowerPluggedIn = z;
    }

    public void hideTransientIndicationDelayed(long j) {
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1), j);
    }

    public void hideBiometricMessageDelayed(long j) {
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(3), j);
    }

    public void showTransientIndication(int i) {
        showTransientIndication((CharSequence) this.mContext.getResources().getString(i));
    }

    /* access modifiers changed from: private */
    public void showTransientIndication(CharSequence charSequence) {
        this.mTransientIndication = charSequence;
        this.mHandler.removeMessages(1);
        hideTransientIndicationDelayed(5000);
        updateTransient();
    }

    public void showBiometricMessage(int i) {
        showBiometricMessage((CharSequence) this.mContext.getResources().getString(i));
    }

    /* access modifiers changed from: private */
    public void showBiometricMessage(CharSequence charSequence) {
        if (!TextUtils.equals(charSequence, this.mBiometricMessage)) {
            this.mBiometricMessage = charSequence;
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            hideBiometricMessageDelayed(5000);
            updateBiometricMessage();
        }
    }

    /* access modifiers changed from: private */
    public void hideBiometricMessage() {
        if (this.mBiometricMessage != null) {
            this.mBiometricMessage = null;
            this.mHandler.removeMessages(3);
            updateBiometricMessage();
        }
    }

    public void hideTransientIndication() {
        if (this.mTransientIndication != null) {
            this.mTransientIndication = null;
            this.mHandler.removeMessages(1);
            updateTransient();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00b7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateDeviceEntryIndication(boolean r8) {
        /*
            r7 = this;
            boolean r0 = r7.mVisible
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            android.view.ViewGroup r0 = r7.mIndicationArea
            r1 = 0
            r0.setVisibility(r1)
            boolean r0 = r7.mDozing
            r2 = 0
            r3 = 8
            if (r0 == 0) goto L_0x00dd
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r8 = r7.mLockScreenIndicationView
            r8.setVisibility(r3)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r8 = r7.mTopIndicationView
            r8.setText(r2)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r8 = r7.mTopIndicationView
            r8.setVisibility(r1)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r8 = r7.mTopIndicationView
            r0 = -1
            r8.setTextColor(r0)
            java.lang.CharSequence r8 = r7.mBiometricMessage
            boolean r8 = android.text.TextUtils.isEmpty(r8)
            r0 = 1
            if (r8 != 0) goto L_0x0034
            java.lang.CharSequence r8 = r7.mBiometricMessage
        L_0x0032:
            r4 = r1
            goto L_0x008f
        L_0x0034:
            java.lang.CharSequence r8 = r7.mTransientIndication
            boolean r8 = android.text.TextUtils.isEmpty(r8)
            if (r8 != 0) goto L_0x003f
            java.lang.CharSequence r8 = r7.mTransientIndication
            goto L_0x0032
        L_0x003f:
            boolean r8 = r7.mBatteryPresent
            if (r8 != 0) goto L_0x0049
            android.view.ViewGroup r7 = r7.mIndicationArea
            r7.setVisibility(r3)
            return
        L_0x0049:
            java.lang.String r8 = r7.mAlignmentIndication
            boolean r8 = android.text.TextUtils.isEmpty(r8)
            if (r8 != 0) goto L_0x0062
            java.lang.String r8 = r7.mAlignmentIndication
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r4 = r7.mTopIndicationView
            android.content.Context r5 = r7.mContext
            r6 = 2131100372(0x7f0602d4, float:1.7813124E38)
            int r5 = r5.getColor(r6)
            r4.setTextColor(r5)
            goto L_0x0032
        L_0x0062:
            boolean r8 = r7.mPowerPluggedIn
            if (r8 != 0) goto L_0x008a
            boolean r8 = r7.mEnableBatteryDefender
            if (r8 == 0) goto L_0x006b
            goto L_0x008a
        L_0x006b:
            java.text.NumberFormat r8 = java.text.NumberFormat.getPercentInstance()
            int r4 = r7.mBatteryLevel
            float r4 = (float) r4
            r5 = 1120403456(0x42c80000, float:100.0)
            float r4 = r4 / r5
            double r4 = (double) r4
            java.lang.String r8 = r8.format((double) r4)
            java.lang.Class<com.nothing.systemui.doze.AODController> r4 = com.nothing.systemui.doze.AODController.class
            java.lang.Object r4 = com.nothing.systemui.NTDependencyEx.get(r4)
            com.nothing.systemui.doze.AODController r4 = (com.nothing.systemui.doze.AODController) r4
            boolean r4 = r4.shouldShowAODView()
            if (r4 == 0) goto L_0x0032
            r4 = r0
            goto L_0x008f
        L_0x008a:
            java.lang.String r8 = r7.computePowerIndication()
            goto L_0x0032
        L_0x008f:
            if (r4 == 0) goto L_0x00b7
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r4 = r7.mTopIndicationView
            r4.setVisibility(r3)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r3 = r7.mOwnInfoIndicationView
            java.lang.CharSequence r3 = r3.getText()
            boolean r3 = android.text.TextUtils.equals(r3, r8)
            if (r3 != 0) goto L_0x00b1
            com.android.systemui.util.wakelock.SettableWakeLock r3 = r7.mWakeLock
            r3.setAcquired(r0)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r3 = r7.mOwnInfoIndicationView
            com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda3 r4 = new com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda3
            r4.<init>(r7)
            r3.switchIndication(r8, r2, r0, r4)
        L_0x00b1:
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r7 = r7.mOwnInfoIndicationView
            r7.setVisibility(r1)
            goto L_0x00dc
        L_0x00b7:
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r4 = r7.mTopIndicationView
            java.lang.CharSequence r4 = r4.getText()
            boolean r4 = android.text.TextUtils.equals(r4, r8)
            if (r4 != 0) goto L_0x00dc
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r4 = r7.mOwnInfoIndicationView
            r4.setVisibility(r3)
            com.android.systemui.util.wakelock.SettableWakeLock r3 = r7.mWakeLock
            r3.setAcquired(r0)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r3 = r7.mTopIndicationView
            com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda4 r4 = new com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda4
            r4.<init>(r7)
            r3.switchIndication(r8, r2, r0, r4)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r7 = r7.mTopIndicationView
            r7.setVisibility(r1)
        L_0x00dc:
            return
        L_0x00dd:
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r0 = r7.mTopIndicationView
            r0.setVisibility(r3)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r0 = r7.mTopIndicationView
            r0.setText(r2)
            com.android.systemui.statusbar.phone.KeyguardIndicationTextView r0 = r7.mLockScreenIndicationView
            r0.setVisibility(r1)
            int r0 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
            r7.updateLockScreenIndications(r8, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.KeyguardIndicationController.updateDeviceEntryIndication(boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateDeviceEntryIndication$9$com-android-systemui-statusbar-KeyguardIndicationController */
    public /* synthetic */ void mo38590x47a25d4d() {
        this.mWakeLock.setAcquired(false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateDeviceEntryIndication$10$com-android-systemui-statusbar-KeyguardIndicationController */
    public /* synthetic */ void mo38589x315b9317() {
        this.mWakeLock.setAcquired(false);
    }

    /* access modifiers changed from: protected */
    public String computePowerIndication() {
        if (this.mBatteryOverheated) {
            String format = NumberFormat.getPercentInstance().format((double) (((float) this.mBatteryLevel) / 100.0f));
            return this.mContext.getResources().getString(C1894R.string.keyguard_plugged_in_charging_limited, new Object[]{format});
        } else if (this.mPowerCharged) {
            return this.mContext.getResources().getString(C1894R.string.keyguard_charged);
        } else {
            boolean z = this.mPowerPluggedInWired;
            int i = C1894R.string.keyguard_plugged_in;
            if (z) {
                int i2 = this.mChargingSpeed;
                if (i2 == 0) {
                    i = C1894R.string.keyguard_plugged_in_charging_slowly;
                } else if (i2 == 2) {
                    i = C1894R.string.keyguard_plugged_in_charging_fast;
                }
            } else if (this.mPowerPluggedInWireless) {
                i = C1894R.string.keyguard_plugged_in_wireless;
            } else if (this.mPowerPluggedInDock) {
                i = C1894R.string.keyguard_plugged_in_dock;
            }
            String format2 = NumberFormat.getPercentInstance().format((double) (((float) this.mBatteryLevel) / 100.0f));
            return this.mContext.getResources().getString(i, new Object[]{format2});
        }
    }

    public void setStatusBarKeyguardViewManager(StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
    }

    public void showActionToUnlock() {
        if (this.mDozing && !this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(KeyguardUpdateMonitor.getCurrentUser())) {
            return;
        }
        if (this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
            if (!this.mStatusBarKeyguardViewManager.isShowingAlternateAuth() && this.mKeyguardUpdateMonitor.isFaceEnrolled()) {
                this.mStatusBarKeyguardViewManager.showBouncerMessage(this.mContext.getString(C1894R.string.keyguard_retry), this.mInitialTextColorState);
            }
        } else if (!this.mKeyguardUpdateMonitor.isUdfpsSupported() || !this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(KeyguardUpdateMonitor.getCurrentUser())) {
            showBiometricMessage((CharSequence) this.mContext.getString(C1894R.string.keyguard_unlock));
        } else {
            showBiometricMessage((CharSequence) this.mContext.getString(this.mKeyguardUpdateMonitor.getIsFaceAuthenticated() ? C1894R.string.keyguard_face_successful_unlock_press : C1894R.string.keyguard_unlock_press));
        }
    }

    /* access modifiers changed from: private */
    public void showFaceFailedTryFingerprintMsg(int i, String str) {
        showBiometricMessage((int) C1894R.string.keyguard_face_failed_use_fp);
        if (!TextUtils.isEmpty(str)) {
            this.mLockScreenIndicationView.announceForAccessibility(str);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardIndicationController:");
        printWriter.println("  mInitialTextColorState: " + this.mInitialTextColorState);
        printWriter.println("  mPowerPluggedInWired: " + this.mPowerPluggedInWired);
        printWriter.println("  mPowerPluggedIn: " + this.mPowerPluggedIn);
        printWriter.println("  mPowerCharged: " + this.mPowerCharged);
        printWriter.println("  mChargingSpeed: " + this.mChargingSpeed);
        printWriter.println("  mChargingWattage: " + this.mChargingWattage);
        printWriter.println("  mMessageToShowOnScreenOn: " + this.mMessageToShowOnScreenOn);
        printWriter.println("  mDozing: " + this.mDozing);
        printWriter.println("  mTransientIndication: " + this.mTransientIndication);
        printWriter.println("  mBiometricMessage: " + this.mBiometricMessage);
        printWriter.println("  mBatteryLevel: " + this.mBatteryLevel);
        printWriter.println("  mBatteryPresent: " + this.mBatteryPresent);
        StringBuilder sb = new StringBuilder("  AOD text: ");
        KeyguardIndicationTextView keyguardIndicationTextView = this.mTopIndicationView;
        printWriter.println(sb.append((Object) keyguardIndicationTextView == null ? null : keyguardIndicationTextView.getText()).toString());
        printWriter.println("  computePowerIndication(): " + computePowerIndication());
        printWriter.println("  trustGrantedIndication: " + getTrustGrantedIndication());
        this.mRotateTextViewController.dump(printWriter, strArr);
    }

    protected class BaseKeyguardCallback extends KeyguardUpdateMonitorCallback {
        public static final int HIDE_DELAY_MS = 5000;

        protected BaseKeyguardCallback() {
        }

        public void onTimeChanged() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void onRefreshBatteryInfo(BatteryStatus batteryStatus) {
            boolean z = false;
            boolean z2 = batteryStatus.status == 2 || batteryStatus.isCharged();
            boolean access$600 = KeyguardIndicationController.this.mPowerPluggedIn;
            boolean unused = KeyguardIndicationController.this.mPowerPluggedInWired = batteryStatus.isPluggedInWired() && z2;
            boolean unused2 = KeyguardIndicationController.this.mPowerPluggedInWireless = batteryStatus.isPluggedInWireless() && z2;
            boolean unused3 = KeyguardIndicationController.this.mPowerPluggedInDock = batteryStatus.isPluggedInDock() && z2;
            boolean unused4 = KeyguardIndicationController.this.mPowerPluggedIn = batteryStatus.isPluggedIn() && z2;
            boolean unused5 = KeyguardIndicationController.this.mPowerCharged = batteryStatus.isCharged();
            int unused6 = KeyguardIndicationController.this.mChargingWattage = batteryStatus.maxChargingWattage;
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            int unused7 = keyguardIndicationController.mChargingSpeed = batteryStatus.getChargingSpeed(keyguardIndicationController.mContext);
            int unused8 = KeyguardIndicationController.this.mBatteryLevel = batteryStatus.level;
            boolean unused9 = KeyguardIndicationController.this.mBatteryPresent = batteryStatus.present;
            boolean unused10 = KeyguardIndicationController.this.mBatteryOverheated = batteryStatus.isOverheated();
            KeyguardIndicationController keyguardIndicationController2 = KeyguardIndicationController.this;
            boolean unused11 = keyguardIndicationController2.mEnableBatteryDefender = keyguardIndicationController2.mBatteryOverheated && batteryStatus.isPluggedIn();
            try {
                KeyguardIndicationController keyguardIndicationController3 = KeyguardIndicationController.this;
                long unused12 = keyguardIndicationController3.mChargingTimeRemaining = keyguardIndicationController3.mPowerPluggedIn ? KeyguardIndicationController.this.mBatteryInfo.computeChargeTimeRemaining() : -1;
            } catch (RemoteException e) {
                Log.e(KeyguardIndicationController.TAG, "Error calling IBatteryStats: ", e);
                long unused13 = KeyguardIndicationController.this.mChargingTimeRemaining = -1;
            }
            KeyguardIndicationController keyguardIndicationController4 = KeyguardIndicationController.this;
            if (!access$600 && keyguardIndicationController4.mPowerPluggedInWired) {
                z = true;
            }
            keyguardIndicationController4.updateDeviceEntryIndication(z);
            if (!KeyguardIndicationController.this.mDozing) {
                return;
            }
            if (!access$600 && KeyguardIndicationController.this.mPowerPluggedIn) {
                KeyguardIndicationController keyguardIndicationController5 = KeyguardIndicationController.this;
                keyguardIndicationController5.showTransientIndication((CharSequence) keyguardIndicationController5.computePowerIndication());
            } else if (access$600 && !KeyguardIndicationController.this.mPowerPluggedIn) {
                KeyguardIndicationController.this.hideTransientIndication();
            }
        }

        public void onBiometricHelp(int i, String str, BiometricSourceType biometricSourceType) {
            boolean z = true;
            if (KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true)) {
                if (i != -2) {
                    z = false;
                }
                if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(str, KeyguardIndicationController.this.mInitialTextColorState);
                } else if (KeyguardIndicationController.this.mScreenLifecycle.getScreenState() == 2) {
                    KeyguardIndicationController.this.showBiometricMessage((CharSequence) str);
                } else if (z) {
                    KeyguardIndicationController.this.mHandler.sendMessageDelayed(KeyguardIndicationController.this.mHandler.obtainMessage(2), KeyguardIndicationController.TRANSIENT_BIOMETRIC_ERROR_TIMEOUT);
                }
            }
        }

        public void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
            if (!shouldSuppressBiometricError(i, biometricSourceType, KeyguardIndicationController.this.mKeyguardUpdateMonitor)) {
                if (i == 3) {
                    if (!KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUdfpsEnrolled() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                        KeyguardIndicationController.this.showFaceFailedTryFingerprintMsg(i, str);
                    } else if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isShowingAlternateAuth()) {
                        KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(KeyguardIndicationController.this.mContext.getResources().getString(C1894R.string.keyguard_try_fingerprint), KeyguardIndicationController.this.mInitialTextColorState);
                    } else {
                        KeyguardIndicationController.this.showActionToUnlock();
                    }
                } else if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(str, KeyguardIndicationController.this.mInitialTextColorState);
                } else if (KeyguardIndicationController.this.mScreenLifecycle.getScreenState() == 2) {
                    KeyguardIndicationController.this.showBiometricMessage((CharSequence) str);
                } else {
                    String unused = KeyguardIndicationController.this.mMessageToShowOnScreenOn = str;
                }
            }
        }

        private boolean shouldSuppressBiometricError(int i, BiometricSourceType biometricSourceType, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
                return shouldSuppressFingerprintError(i, keyguardUpdateMonitor);
            }
            if (biometricSourceType == BiometricSourceType.FACE) {
                return shouldSuppressFaceError(i, keyguardUpdateMonitor);
            }
            return false;
        }

        private boolean shouldSuppressFingerprintError(int i, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            return (!keyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true) && i != 9) || i == 5 || i == 10;
        }

        private boolean shouldSuppressFaceError(int i, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            return (!keyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true) && i != 9) || i == 5;
        }

        public void onTrustChanged(int i) {
            if (KeyguardUpdateMonitor.getCurrentUser() == i) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void showTrustGrantedMessage(CharSequence charSequence) {
            CharSequence unused = KeyguardIndicationController.this.mTrustGrantedIndication = charSequence;
            KeyguardIndicationController.this.updateDeviceEntryIndication(false);
        }

        public void onTrustAgentErrorMessage(CharSequence charSequence) {
            KeyguardIndicationController.this.showBiometricMessage(charSequence);
        }

        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            if (z && biometricSourceType == BiometricSourceType.FACE) {
                KeyguardIndicationController.this.hideBiometricMessage();
                String unused = KeyguardIndicationController.this.mMessageToShowOnScreenOn = null;
            }
        }

        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            super.onBiometricAuthenticated(i, biometricSourceType, z);
            KeyguardIndicationController.this.hideBiometricMessage();
            if (biometricSourceType == BiometricSourceType.FACE && !KeyguardIndicationController.this.mKeyguardBypassController.canBypass()) {
                KeyguardIndicationController.this.showActionToUnlock();
            }
        }

        public void onUserSwitchComplete(int i) {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void onUserUnlocked() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void onLogoutEnabledChanged() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void onRequireUnlockForNfc() {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            keyguardIndicationController.showTransientIndication((CharSequence) keyguardIndicationController.mContext.getString(C1894R.string.require_unlock_for_nfc));
            KeyguardIndicationController.this.hideTransientIndicationDelayed(5000);
        }

        public void onKeyguardBouncerFullyShowingChanged(boolean z) {
            boolean unused = KeyguardIndicationController.this.mBouncerShow = z;
            if (KeyguardIndicationController.this.mDozing) {
                return;
            }
            if (z) {
                KeyguardIndicationController.this.mOwnInfoIndicationView.clearMessages();
            } else {
                KeyguardIndicationController.this.updateLockScreenOwnerInfo();
            }
        }
    }
}
