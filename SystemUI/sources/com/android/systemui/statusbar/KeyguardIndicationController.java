package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.IActivityManager;
import android.app.IStopUserCallback;
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
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.ViewClippingUtil;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class KeyguardIndicationController {
    private String mAlignmentIndication;
    private final IBatteryStats mBatteryInfo;
    private int mBatteryLevel;
    private boolean mBatteryOverheated;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private BroadcastReceiver mBroadcastReceiver;
    private int mChargingSpeed;
    private long mChargingTimeRemaining;
    private int mChargingWattage;
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final DockManager mDockManager;
    private boolean mDozing;
    private boolean mEnableBatteryDefender;
    private final DelayableExecutor mExecutor;
    private final FalsingManager mFalsingManager;
    private boolean mHideTransientMessageOnScreenOff;
    private final IActivityManager mIActivityManager;
    private ViewGroup mIndicationArea;
    private boolean mInited;
    protected ColorStateList mInitialTextColorState;
    private final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LockPatternUtils mLockPatternUtils;
    private KeyguardIndicationTextView mLockScreenIndicationView;
    protected int mLockScreenMode;
    private String mMessageToShowOnScreenOn;
    private KeyguardIndicationTextView mOwnInfoIndicationView;
    private boolean mPowerCharged;
    private boolean mPowerPluggedIn;
    private boolean mPowerPluggedInWired;
    private String mRestingIndication;
    protected KeyguardIndicationRotateTextViewController mRotateTextViewController;
    private StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final StatusBarStateController mStatusBarStateController;
    private KeyguardIndicationTextView mTopIndicationView;
    private CharSequence mTransientIndication;
    private KeyguardUpdateMonitorCallback mUpdateMonitorCallback;
    private final UserManager mUserManager;
    private boolean mVisible;
    private final SettableWakeLock mWakeLock;
    private boolean mBatteryPresent = true;
    private final ViewClippingUtil.ClippingParameters mClippingParams = new ViewClippingUtil.ClippingParameters() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.1
        public boolean shouldFinish(View view) {
            return view == KeyguardIndicationController.this.mIndicationArea;
        }
    };
    private final KeyguardUpdateMonitorCallback mTickReceiver = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.4
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTimeChanged() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateIndication(false);
            }
        }
    };
    private final Handler mHandler = new Handler() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.5
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                KeyguardIndicationController.this.hideTransientIndication();
            } else if (i != 2) {
            } else {
                KeyguardIndicationController.this.showActionToUnlock();
            }
        }
    };
    private StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.6
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onStateChanged(int i) {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            keyguardIndicationController.setVisible(z);
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozingChanged(boolean z) {
            if (KeyguardIndicationController.this.mDozing == z) {
                return;
            }
            KeyguardIndicationController.this.mDozing = z;
            if (KeyguardIndicationController.this.mHideTransientMessageOnScreenOff && KeyguardIndicationController.this.mDozing) {
                KeyguardIndicationController.this.hideTransientIndication();
            }
            KeyguardIndicationController.this.updateIndication(false);
        }
    };
    private KeyguardStateController.Callback mKeyguardStateCallback = new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.7
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onUnlockedChanged() {
            KeyguardIndicationController.this.updateIndication(false);
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onKeyguardShowingChanged() {
            if (!KeyguardIndicationController.this.mKeyguardStateController.isShowing()) {
                KeyguardIndicationController.this.mTopIndicationView.clearMessages();
                KeyguardIndicationController.this.mLockScreenIndicationView.clearMessages();
            }
        }
    };

    private String getTrustManagedIndication() {
        return null;
    }

    public KeyguardIndicationController(Context context, WakeLock.Builder builder, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, IBatteryStats iBatteryStats, UserManager userManager, DelayableExecutor delayableExecutor, FalsingManager falsingManager, LockPatternUtils lockPatternUtils, IActivityManager iActivityManager, KeyguardBypassController keyguardBypassController) {
        this.mContext = context;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mDockManager = dockManager;
        this.mWakeLock = new SettableWakeLock(builder.setTag("Doze:KeyguardIndication").build(), "KeyguardIndication");
        this.mBatteryInfo = iBatteryStats;
        this.mUserManager = userManager;
        this.mExecutor = delayableExecutor;
        this.mLockPatternUtils = lockPatternUtils;
        this.mIActivityManager = iActivityManager;
        this.mFalsingManager = falsingManager;
        this.mKeyguardBypassController = keyguardBypassController;
    }

    public void init() {
        if (this.mInited) {
            return;
        }
        this.mInited = true;
        this.mDockManager.addAlignmentStateListener(new DockManager.AlignmentStateListener(this) { // from class: com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda1
        });
        this.mKeyguardUpdateMonitor.registerCallback(getKeyguardCallback());
        this.mKeyguardUpdateMonitor.registerCallback(this.mTickReceiver);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
        this.mStatusBarStateListener.onDozingChanged(this.mStatusBarStateController.isDozing());
    }

    public void setIndicationArea(ViewGroup viewGroup) {
        this.mIndicationArea = viewGroup;
        this.mTopIndicationView = (KeyguardIndicationTextView) viewGroup.findViewById(R$id.keyguard_indication_text);
        this.mLockScreenIndicationView = (KeyguardIndicationTextView) viewGroup.findViewById(R$id.keyguard_indication_text_bottom);
        this.mOwnInfoIndicationView = (KeyguardIndicationTextView) viewGroup.findViewById(R$id.keyguard_indication_owner_info_text);
        KeyguardIndicationTextView keyguardIndicationTextView = this.mTopIndicationView;
        this.mInitialTextColorState = keyguardIndicationTextView != null ? keyguardIndicationTextView.getTextColors() : ColorStateList.valueOf(-1);
        this.mRotateTextViewController = new KeyguardIndicationRotateTextViewController(this.mLockScreenIndicationView, this.mExecutor, this.mStatusBarStateController);
        updateIndication(false);
        updateDisclosure();
        if (this.mBroadcastReceiver == null) {
            this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    KeyguardIndicationController.this.updateDisclosure();
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.USER_REMOVED");
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        }
    }

    protected KeyguardUpdateMonitorCallback getKeyguardCallback() {
        if (this.mUpdateMonitorCallback == null) {
            this.mUpdateMonitorCallback = new BaseKeyguardCallback();
        }
        return this.mUpdateMonitorCallback;
    }

    private void updateIndications(boolean z, int i) {
        updateOwnerInfo();
        updateBattery(z);
        updateUserLocked(i);
        updateTransient();
        updateTrust(i, getTrustGrantedIndication(), getTrustManagedIndication());
        updateAlignment();
        updateLogoutView();
        updateResting();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisclosure() {
        if (((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final Object get() {
                boolean isOrganizationOwnedDevice;
                isOrganizationOwnedDevice = KeyguardIndicationController.this.isOrganizationOwnedDevice();
                return Boolean.valueOf(isOrganizationOwnedDevice);
            }
        })).booleanValue()) {
            this.mRotateTextViewController.updateIndication(1, new KeyguardIndication.Builder().setMessage(getDisclosureText(getOrganizationOwnedDeviceOrganizationName())).setTextColor(this.mInitialTextColorState).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(1);
        }
        updateResting();
    }

    private CharSequence getDisclosureText(CharSequence charSequence) {
        Resources resources = this.mContext.getResources();
        if (charSequence == null) {
            return resources.getText(R$string.do_disclosure_generic);
        }
        if (this.mDevicePolicyManager.isDeviceManaged()) {
            DevicePolicyManager devicePolicyManager = this.mDevicePolicyManager;
            if (devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return resources.getString(R$string.do_financed_disclosure_with_name, charSequence);
            }
        }
        return resources.getString(R$string.do_disclosure_with_name, charSequence);
    }

    private void updateOwnerInfo() {
        String deviceOwnerInfo = this.mLockPatternUtils.getDeviceOwnerInfo();
        if (deviceOwnerInfo == null && this.mLockPatternUtils.isOwnerInfoEnabled(KeyguardUpdateMonitor.getCurrentUser())) {
            deviceOwnerInfo = this.mLockPatternUtils.getOwnerInfo(KeyguardUpdateMonitor.getCurrentUser());
        }
        if (!TextUtils.isEmpty(deviceOwnerInfo)) {
            this.mOwnInfoIndicationView.setVisibility(0);
            this.mOwnInfoIndicationView.switchIndication(deviceOwnerInfo, null);
            return;
        }
        this.mOwnInfoIndicationView.setVisibility(8);
    }

    private void updateBattery(boolean z) {
        if (this.mPowerPluggedIn || this.mEnableBatteryDefender) {
            String computePowerIndication = computePowerIndication();
            Log.d("KeyguardIndication", "ACTION_BATTERY_CHANGED updateBattery powerIndication=" + computePowerIndication + ", time=" + System.currentTimeMillis());
            this.mRotateTextViewController.updateIndication(3, new KeyguardIndication.Builder().setMessage(computePowerIndication).setTextColor(this.mInitialTextColorState).build(), z);
            return;
        }
        this.mRotateTextViewController.hideIndication(3);
    }

    private void updateUserLocked(int i) {
        if (!this.mKeyguardUpdateMonitor.isUserUnlocked(i)) {
            this.mRotateTextViewController.updateIndication(8, new KeyguardIndication.Builder().setMessage(this.mContext.getResources().getText(17040564)).setTextColor(this.mInitialTextColorState).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(8);
        }
    }

    private void updateTransient() {
        if (!TextUtils.isEmpty(this.mTransientIndication)) {
            this.mRotateTextViewController.showTransient(this.mTransientIndication);
        } else {
            this.mRotateTextViewController.hideTransient();
        }
    }

    private void updateTrust(int i, CharSequence charSequence, CharSequence charSequence2) {
        if (!TextUtils.isEmpty(charSequence) && this.mKeyguardUpdateMonitor.getUserHasTrust(i)) {
            this.mRotateTextViewController.updateIndication(6, new KeyguardIndication.Builder().setMessage(charSequence).setTextColor(this.mInitialTextColorState).build(), false);
        } else if (!TextUtils.isEmpty(charSequence2) && this.mKeyguardUpdateMonitor.getUserTrustIsManaged(i) && !this.mKeyguardUpdateMonitor.getUserHasTrust(i)) {
            this.mRotateTextViewController.updateIndication(6, new KeyguardIndication.Builder().setMessage(charSequence2).setTextColor(this.mInitialTextColorState).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(6);
        }
    }

    private void updateAlignment() {
        if (!TextUtils.isEmpty(this.mAlignmentIndication)) {
            this.mRotateTextViewController.updateIndication(4, new KeyguardIndication.Builder().setMessage(this.mAlignmentIndication).setTextColor(ColorStateList.valueOf(this.mContext.getColor(R$color.misalignment_text_color))).build(), true);
        } else {
            this.mRotateTextViewController.hideIndication(4);
        }
    }

    private void updateResting() {
        if (!TextUtils.isEmpty(this.mRestingIndication) && !this.mRotateTextViewController.hasIndications()) {
            this.mRotateTextViewController.updateIndication(7, new KeyguardIndication.Builder().setMessage(this.mRestingIndication).setTextColor(this.mInitialTextColorState).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(7);
        }
    }

    private void updateLogoutView() {
        if (this.mKeyguardUpdateMonitor.isLogoutEnabled() && KeyguardUpdateMonitor.getCurrentUser() != 0) {
            this.mRotateTextViewController.updateIndication(2, new KeyguardIndication.Builder().setMessage(this.mContext.getResources().getString(17040330)).setTextColor(Utils.getColorAttr(this.mContext, 17957102)).setBackground(this.mContext.getDrawable(R$drawable.logout_button_background)).setClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    KeyguardIndicationController.this.lambda$updateLogoutView$2(view);
                }
            }).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLogoutView$2(View view) {
        if (this.mFalsingManager.isFalseTap(1)) {
            return;
        }
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        try {
            this.mIActivityManager.switchUser(0);
            this.mIActivityManager.stopUser(currentUser, true, (IStopUserCallback) null);
        } catch (RemoteException e) {
            Log.e("KeyguardIndication", "Failed to logout user", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOrganizationOwnedDevice() {
        return this.mDevicePolicyManager.isDeviceManaged() || this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    private CharSequence getOrganizationOwnedDeviceOrganizationName() {
        if (this.mDevicePolicyManager.isDeviceManaged()) {
            return this.mDevicePolicyManager.getDeviceOwnerOrganizationName();
        }
        if (!this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile()) {
            return null;
        }
        return getWorkProfileOrganizationName();
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
            updateIndication(false);
        } else if (z) {
        } else {
            hideTransientIndication();
        }
    }

    @VisibleForTesting
    String getTrustGrantedIndication() {
        return this.mContext.getString(R$string.keyguard_indication_trust_unlocked);
    }

    @VisibleForTesting
    void setPowerPluggedIn(boolean z) {
        this.mPowerPluggedIn = z;
    }

    public void hideTransientIndicationDelayed(long j) {
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1), j);
    }

    public void showTransientIndication(int i) {
        showTransientIndication(this.mContext.getResources().getString(i));
    }

    public void showTransientIndication(CharSequence charSequence) {
        showTransientIndication(charSequence, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTransientIndication(CharSequence charSequence, boolean z, boolean z2) {
        this.mTransientIndication = charSequence;
        this.mHideTransientMessageOnScreenOff = z2 && charSequence != null;
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        if (this.mDozing && !TextUtils.isEmpty(this.mTransientIndication)) {
            this.mWakeLock.setAcquired(true);
        }
        hideTransientIndicationDelayed(5000L);
        updateIndication(false);
    }

    public void hideTransientIndication() {
        if (this.mTransientIndication != null) {
            this.mTransientIndication = null;
            this.mHideTransientMessageOnScreenOff = false;
            this.mHandler.removeMessages(1);
            this.mRotateTextViewController.hideTransient();
            updateIndication(false);
        }
    }

    protected final void updateIndication(boolean z) {
        if (TextUtils.isEmpty(this.mTransientIndication)) {
            this.mWakeLock.setAcquired(false);
        }
        if (!this.mVisible) {
            return;
        }
        this.mIndicationArea.setVisibility(0);
        if (this.mDozing) {
            this.mLockScreenIndicationView.setVisibility(8);
            this.mTopIndicationView.setVisibility(0);
            this.mTopIndicationView.setTextColor(-1);
            if (!TextUtils.isEmpty(this.mTransientIndication)) {
                this.mTopIndicationView.switchIndication(this.mTransientIndication, null);
                return;
            } else if (!this.mBatteryPresent) {
                this.mIndicationArea.setVisibility(8);
                return;
            } else if (!TextUtils.isEmpty(this.mAlignmentIndication)) {
                this.mTopIndicationView.switchIndication(this.mAlignmentIndication, null);
                this.mTopIndicationView.setTextColor(this.mContext.getColor(R$color.misalignment_text_color));
                return;
            } else if (this.mPowerPluggedIn || this.mEnableBatteryDefender) {
                String computePowerIndication = computePowerIndication();
                if (z) {
                    animateText(this.mTopIndicationView, computePowerIndication);
                } else {
                    this.mTopIndicationView.switchIndication(computePowerIndication, null);
                }
                this.mOwnInfoIndicationView.setVisibility(8);
                return;
            } else {
                String format = NumberFormat.getPercentInstance().format(this.mBatteryLevel / 100.0f);
                this.mTopIndicationView.setVisibility(8);
                this.mOwnInfoIndicationView.switchIndication(format, null);
                this.mOwnInfoIndicationView.setVisibility(0);
                return;
            }
        }
        this.mTopIndicationView.setVisibility(8);
        this.mTopIndicationView.setText((CharSequence) null);
        this.mLockScreenIndicationView.setVisibility(0);
        updateIndications(z, KeyguardUpdateMonitor.getCurrentUser());
    }

    private void animateText(final KeyguardIndicationTextView keyguardIndicationTextView, final String str) {
        int integer = this.mContext.getResources().getInteger(R$integer.wired_charging_keyguard_text_animation_distance);
        int integer2 = this.mContext.getResources().getInteger(R$integer.wired_charging_keyguard_text_animation_duration_up);
        final int integer3 = this.mContext.getResources().getInteger(R$integer.wired_charging_keyguard_text_animation_duration_down);
        keyguardIndicationTextView.animate().cancel();
        ViewClippingUtil.setClippingDeactivated(keyguardIndicationTextView, true, this.mClippingParams);
        keyguardIndicationTextView.animate().translationYBy(integer).setInterpolator(Interpolators.LINEAR).setDuration(integer2).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.3
            private boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                keyguardIndicationTextView.switchIndication(str, null);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                keyguardIndicationTextView.setTranslationY(0.0f);
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (this.mCancelled) {
                    ViewClippingUtil.setClippingDeactivated(keyguardIndicationTextView, false, KeyguardIndicationController.this.mClippingParams);
                } else {
                    keyguardIndicationTextView.animate().setDuration(integer3).setInterpolator(Interpolators.BOUNCE).translationY(0.0f).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.3.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator2) {
                            keyguardIndicationTextView.setTranslationY(0.0f);
                            AnonymousClass3 anonymousClass3 = AnonymousClass3.this;
                            ViewClippingUtil.setClippingDeactivated(keyguardIndicationTextView, false, KeyguardIndicationController.this.mClippingParams);
                        }
                    });
                }
            }
        });
    }

    protected String computePowerIndication() {
        int i;
        if (this.mBatteryOverheated) {
            return this.mContext.getResources().getString(R$string.keyguard_plugged_in_charging_limited, NumberFormat.getPercentInstance().format(this.mBatteryLevel / 100.0f));
        } else if (this.mPowerCharged) {
            return this.mContext.getResources().getString(R$string.keyguard_charged);
        } else {
            if (this.mPowerPluggedInWired) {
                int i2 = this.mChargingSpeed;
                if (i2 == 0) {
                    i = R$string.keyguard_plugged_in_charging_slowly;
                } else if (i2 == 2) {
                    i = R$string.keyguard_plugged_in_charging_fast;
                } else {
                    i = R$string.keyguard_plugged_in;
                }
            } else {
                i = R$string.keyguard_plugged_in_wireless;
            }
            return this.mContext.getResources().getString(i, NumberFormat.getPercentInstance().format(this.mBatteryLevel / 100.0f));
        }
    }

    public void setStatusBarKeyguardViewManager(StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
    }

    public void showActionToUnlock() {
        if (!this.mDozing || this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(KeyguardUpdateMonitor.getCurrentUser())) {
            if (this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                if (this.mStatusBarKeyguardViewManager.isShowingAlternateAuth() || !this.mKeyguardUpdateMonitor.isFaceEnrolled()) {
                    return;
                }
                this.mStatusBarKeyguardViewManager.showBouncerMessage(this.mContext.getString(R$string.keyguard_retry), this.mInitialTextColorState);
                return;
            }
            showTransientIndication(this.mContext.getString(R$string.keyguard_unlock), false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTryFingerprintMsg(String str) {
        if (this.mKeyguardUpdateMonitor.isUdfpsAvailable()) {
            if (this.mKeyguardBypassController.getUserHasDeviceEntryIntent()) {
                showTransientIndication(R$string.keyguard_unlock_press);
            } else {
                showTransientIndication(R$string.keyguard_face_failed_use_fp);
            }
        } else {
            showTransientIndication(R$string.keyguard_try_fingerprint);
        }
        if (!TextUtils.isEmpty(str)) {
            this.mLockScreenIndicationView.announceForAccessibility(str);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardIndicationController:");
        printWriter.println("  mInitialTextColorState: " + this.mInitialTextColorState);
        printWriter.println("  mPowerPluggedInWired: " + this.mPowerPluggedInWired);
        printWriter.println("  mPowerPluggedIn: " + this.mPowerPluggedIn);
        printWriter.println("  mPowerCharged: " + this.mPowerCharged);
        printWriter.println("  mChargingSpeed: " + this.mChargingSpeed);
        printWriter.println("  mChargingWattage: " + this.mChargingWattage);
        printWriter.println("  mMessageToShowOnScreenOn: " + this.mMessageToShowOnScreenOn);
        printWriter.println("  mDozing: " + this.mDozing);
        printWriter.println("  mBatteryLevel: " + this.mBatteryLevel);
        printWriter.println("  mBatteryPresent: " + this.mBatteryPresent);
        StringBuilder sb = new StringBuilder();
        sb.append("  mTextView.getText(): ");
        KeyguardIndicationTextView keyguardIndicationTextView = this.mTopIndicationView;
        sb.append((Object) (keyguardIndicationTextView == null ? null : keyguardIndicationTextView.getText()));
        printWriter.println(sb.toString());
        printWriter.println("  computePowerIndication(): " + computePowerIndication());
        this.mRotateTextViewController.dump(fileDescriptor, printWriter, strArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class BaseKeyguardCallback extends KeyguardUpdateMonitorCallback {
        protected BaseKeyguardCallback() {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onLockScreenModeChanged(int i) {
            KeyguardIndicationController.this.mLockScreenMode = i;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onRefreshBatteryInfo(BatteryStatus batteryStatus) {
            int i = batteryStatus.status;
            boolean z = false;
            boolean z2 = i == 2 || i == 5;
            boolean z3 = KeyguardIndicationController.this.mPowerPluggedIn;
            KeyguardIndicationController.this.mPowerPluggedInWired = batteryStatus.isPluggedInWired() && z2;
            KeyguardIndicationController.this.mPowerPluggedIn = batteryStatus.isPluggedIn() && z2;
            KeyguardIndicationController.this.mPowerCharged = batteryStatus.isCharged();
            KeyguardIndicationController.this.mChargingWattage = batteryStatus.maxChargingWattage;
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            keyguardIndicationController.mChargingSpeed = batteryStatus.getChargingSpeed(keyguardIndicationController.mContext);
            KeyguardIndicationController.this.mBatteryLevel = batteryStatus.level;
            KeyguardIndicationController.this.mBatteryPresent = batteryStatus.present;
            KeyguardIndicationController.this.mBatteryOverheated = batteryStatus.isOverheated();
            KeyguardIndicationController keyguardIndicationController2 = KeyguardIndicationController.this;
            keyguardIndicationController2.mEnableBatteryDefender = keyguardIndicationController2.mBatteryOverheated && batteryStatus.isPluggedIn();
            try {
                KeyguardIndicationController keyguardIndicationController3 = KeyguardIndicationController.this;
                keyguardIndicationController3.mChargingTimeRemaining = keyguardIndicationController3.mPowerPluggedIn ? KeyguardIndicationController.this.mBatteryInfo.computeChargeTimeRemaining() : -1L;
            } catch (RemoteException e) {
                Log.e("KeyguardIndication", "Error calling IBatteryStats: ", e);
                KeyguardIndicationController.this.mChargingTimeRemaining = -1L;
            }
            KeyguardIndicationController keyguardIndicationController4 = KeyguardIndicationController.this;
            if (!z3 && keyguardIndicationController4.mPowerPluggedInWired) {
                z = true;
            }
            keyguardIndicationController4.updateIndication(z);
            if (KeyguardIndicationController.this.mDozing) {
                if (z3 || !KeyguardIndicationController.this.mPowerPluggedIn) {
                    if (!z3 || KeyguardIndicationController.this.mPowerPluggedIn) {
                        return;
                    }
                    KeyguardIndicationController.this.hideTransientIndication();
                    return;
                }
                KeyguardIndicationController keyguardIndicationController5 = KeyguardIndicationController.this;
                keyguardIndicationController5.showTransientIndication(keyguardIndicationController5.computePowerIndication());
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricHelp(int i, String str, BiometricSourceType biometricSourceType) {
            boolean z = true;
            if (!KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true)) {
                return;
            }
            if (i != -2) {
                z = false;
            }
            if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(str, KeyguardIndicationController.this.mInitialTextColorState);
            } else if (!KeyguardIndicationController.this.mKeyguardUpdateMonitor.isScreenOn()) {
                if (!z) {
                    return;
                }
                KeyguardIndicationController.this.mHandler.sendMessageDelayed(KeyguardIndicationController.this.mHandler.obtainMessage(2), 1300L);
            } else if (biometricSourceType != BiometricSourceType.FACE || !shouldSuppressFaceMsgAndShowTryFingerprintMsg()) {
                KeyguardIndicationController.this.showTransientIndication(str, false, z);
            } else {
                KeyguardIndicationController.this.showTryFingerprintMsg(str);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
            if (shouldSuppressBiometricError(i, biometricSourceType, KeyguardIndicationController.this.mKeyguardUpdateMonitor)) {
                return;
            }
            if (biometricSourceType == BiometricSourceType.FACE && shouldSuppressFaceMsgAndShowTryFingerprintMsg() && !KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isScreenOn()) {
                KeyguardIndicationController.this.showTryFingerprintMsg(str);
            } else if (i == 3) {
                if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing() || !KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUdfpsEnrolled() || !KeyguardIndicationController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                    if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isShowingAlternateAuth()) {
                        KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(KeyguardIndicationController.this.mContext.getResources().getString(R$string.keyguard_unlock_press), KeyguardIndicationController.this.mInitialTextColorState);
                        return;
                    } else {
                        KeyguardIndicationController.this.showActionToUnlock();
                        return;
                    }
                }
                KeyguardIndicationController.this.showTryFingerprintMsg(str);
            } else if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(str, KeyguardIndicationController.this.mInitialTextColorState);
            } else if (KeyguardIndicationController.this.mKeyguardUpdateMonitor.isScreenOn()) {
                KeyguardIndicationController.this.showTransientIndication(str, true, true);
            } else {
                KeyguardIndicationController.this.mMessageToShowOnScreenOn = str;
            }
        }

        private boolean shouldSuppressBiometricError(int i, BiometricSourceType biometricSourceType, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
                return shouldSuppressFingerprintError(i, keyguardUpdateMonitor);
            }
            if (biometricSourceType != BiometricSourceType.FACE) {
                return false;
            }
            return shouldSuppressFaceError(i, keyguardUpdateMonitor);
        }

        private boolean shouldSuppressFingerprintError(int i, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            return (!keyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true) && i != 9) || i == 5 || i == 10;
        }

        private boolean shouldSuppressFaceMsgAndShowTryFingerprintMsg() {
            return KeyguardIndicationController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true);
        }

        private boolean shouldSuppressFaceError(int i, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            return (!keyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true) && i != 9) || i == 5;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTrustAgentErrorMessage(CharSequence charSequence) {
            KeyguardIndicationController.this.showTransientIndication(charSequence, true, false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onScreenTurnedOn() {
            if (KeyguardIndicationController.this.mMessageToShowOnScreenOn != null) {
                KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                keyguardIndicationController.showTransientIndication(keyguardIndicationController.mMessageToShowOnScreenOn, true, false);
                KeyguardIndicationController.this.hideTransientIndicationDelayed(5000L);
                KeyguardIndicationController.this.mMessageToShowOnScreenOn = null;
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            if (!z || biometricSourceType != BiometricSourceType.FACE) {
                return;
            }
            KeyguardIndicationController.this.hideTransientIndication();
            KeyguardIndicationController.this.mMessageToShowOnScreenOn = null;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            super.onBiometricAuthenticated(i, biometricSourceType, z);
            KeyguardIndicationController.this.mHandler.sendEmptyMessage(1);
            if (biometricSourceType != BiometricSourceType.FACE || KeyguardIndicationController.this.mKeyguardBypassController.canBypass()) {
                return;
            }
            KeyguardIndicationController.this.mHandler.sendEmptyMessage(2);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitchComplete(int i) {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateIndication(false);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserUnlocked() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateIndication(false);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onLogoutEnabledChanged() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateIndication(false);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onRequireUnlockForNfc() {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            keyguardIndicationController.showTransientIndication(keyguardIndicationController.mContext.getString(R$string.require_unlock_for_nfc), false, false);
            KeyguardIndicationController.this.hideTransientIndicationDelayed(5000L);
        }
    }
}
