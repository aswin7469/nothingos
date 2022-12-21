package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Handler;
import android.os.UserManager;
import android.provider.Settings;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import com.android.keyguard.CarrierTextController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.fragment.StatusBarIconBlocklistKt;
import com.android.systemui.statusbar.phone.fragment.StatusBarSystemEventAnimator;
import com.android.systemui.statusbar.phone.userswitcher.OnUserSwitcherPreferenceChangeListener;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.SecureSettings;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class KeyguardStatusBarViewController extends ViewController<KeyguardStatusBarView> {
    private static final AnimationProperties KEYGUARD_HUN_PROPERTIES = new AnimationProperties().setDuration(360);
    private final SystemStatusAnimationCallback mAnimationCallback = new SystemStatusAnimationCallback() {
        public Animator onSystemEventAnimationFinish(boolean z) {
            return KeyguardStatusBarViewController.this.mSystemEventAnimator.onSystemEventAnimationFinish(z);
        }

        public Animator onSystemEventAnimationBegin() {
            return KeyguardStatusBarViewController.this.mSystemEventAnimator.onSystemEventAnimationBegin();
        }
    };
    private final SystemStatusAnimationScheduler mAnimationScheduler;
    private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new KeyguardStatusBarViewController$$ExternalSyntheticLambda5(this);
    private final BatteryController mBatteryController;
    private boolean mBatteryListening;
    private final BatteryMeterViewController mBatteryMeterViewController;
    private final BatteryController.BatteryStateChangeCallback mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() {
        public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).onBatteryLevelChanged(z2);
        }
    };
    /* access modifiers changed from: private */
    public final BiometricUnlockController mBiometricUnlockController;
    private final List<String> mBlockedIcons = new ArrayList();
    private final CarrierTextController mCarrierTextController;
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onDensityOrFontScaleChanged() {
            ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).loadDimens();
            StatusBarSystemEventAnimator unused = KeyguardStatusBarViewController.this.mSystemEventAnimator = new StatusBarSystemEventAnimator(KeyguardStatusBarViewController.this.mView, KeyguardStatusBarViewController.this.getResources());
        }

        public void onThemeChanged() {
            ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).onOverlayChanged();
            KeyguardStatusBarViewController.this.onThemeChanged();
        }

        public void onConfigChanged(Configuration configuration) {
            KeyguardStatusBarViewController.this.updateUserSwitcher();
        }
    };
    /* access modifiers changed from: private */
    public boolean mDelayShowingKeyguardStatusBar;
    /* access modifiers changed from: private */
    public boolean mDozing;
    private float mExplicitAlpha = -1.0f;
    private final StatusBarUserSwitcherFeatureController mFeatureController;
    /* access modifiers changed from: private */
    public boolean mFirstBypassAttempt;
    private final AnimatableProperty mHeadsUpShowingAmountAnimation = AnimatableProperty.from("KEYGUARD_HEADS_UP_SHOWING_AMOUNT", new KeyguardStatusBarViewController$$ExternalSyntheticLambda2(this), new KeyguardStatusBarViewController$$ExternalSyntheticLambda3(this), C1893R.C1897id.keyguard_hun_animator_tag, C1893R.C1897id.keyguard_hun_animator_end_tag, C1893R.C1897id.keyguard_hun_animator_start_tag);
    private final StatusBarContentInsetsProvider mInsetsProvider;
    /* access modifiers changed from: private */
    public final KeyguardBypassController mKeyguardBypassController;
    private float mKeyguardHeadsUpShowingAmount = 0.0f;
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    /* access modifiers changed from: private */
    public float mKeyguardStatusBarAnimateAlpha = 1.0f;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            if (KeyguardStatusBarViewController.this.mFirstBypassAttempt && KeyguardStatusBarViewController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(z)) {
                boolean unused = KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar = true;
            }
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            if (z) {
                KeyguardStatusBarViewController.this.updateUserSwitcher();
            }
        }

        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            boolean z2 = true;
            if (!(KeyguardStatusBarViewController.this.mStatusBarState == 1 || KeyguardStatusBarViewController.this.mStatusBarState == 2)) {
                z2 = false;
            }
            if (!z && KeyguardStatusBarViewController.this.mFirstBypassAttempt && z2 && !KeyguardStatusBarViewController.this.mDozing && !KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar && !KeyguardStatusBarViewController.this.mBiometricUnlockController.isBiometricUnlock()) {
                boolean unused = KeyguardStatusBarViewController.this.mFirstBypassAttempt = false;
                KeyguardStatusBarViewController.this.animateKeyguardStatusBarIn();
            }
        }

        public void onFinishedGoingToSleep(int i) {
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            boolean unused = keyguardStatusBarViewController.mFirstBypassAttempt = keyguardStatusBarViewController.mKeyguardBypassController.getBypassEnabled();
            boolean unused2 = KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar = false;
        }
    };
    private final Object mLock = new Object();
    private final Executor mMainExecutor;
    private final NotificationPanelViewController.NotificationPanelViewStateProvider mNotificationPanelViewStateProvider;
    private final int mNotificationsHeaderCollideDistance;
    private final UserInfoController.OnUserInfoChangedListener mOnUserInfoChangedListener = new KeyguardStatusBarViewController$$ExternalSyntheticLambda4(this);
    private final SecureSettings mSecureSettings;
    private boolean mShowingKeyguardHeadsUp;
    private final StatusBarIconController mStatusBarIconController;
    /* access modifiers changed from: private */
    public int mStatusBarState;
    private final SysuiStatusBarStateController mStatusBarStateController;
    private final StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onStateChanged(int i) {
            int unused = KeyguardStatusBarViewController.this.mStatusBarState = i;
        }
    };
    private final StatusBarUserInfoTracker mStatusBarUserInfoTracker;
    /* access modifiers changed from: private */
    public StatusBarSystemEventAnimator mSystemEventAnimator;
    private StatusBarIconController.TintedIconManager mTintedIconManager;
    private final StatusBarIconController.TintedIconManager.Factory mTintedIconManagerFactory;
    private final UserInfoController mUserInfoController;
    private final UserManager mUserManager;
    private final StatusBarUserSwitcherController mUserSwitcherController;
    private final ContentObserver mVolumeSettingObserver = new ContentObserver((Handler) null) {
        public void onChange(boolean z) {
            KeyguardStatusBarViewController.this.updateBlockedIcons();
        }
    };

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-KeyguardStatusBarViewController */
    public /* synthetic */ void mo44295xd1760466(View view, Float f) {
        this.mKeyguardHeadsUpShowingAmount = f.floatValue();
        updateViewState();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-statusbar-phone-KeyguardStatusBarViewController */
    public /* synthetic */ Float mo44296xa566505(View view) {
        return Float.valueOf(this.mKeyguardHeadsUpShowingAmount);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-statusbar-phone-KeyguardStatusBarViewController */
    public /* synthetic */ void mo44297x4336c5a4(String str, Drawable drawable, String str2) {
        ((KeyguardStatusBarView) this.mView).onUserInfoChanged(drawable);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-statusbar-phone-KeyguardStatusBarViewController */
    public /* synthetic */ void mo44298x7c172643(ValueAnimator valueAnimator) {
        this.mKeyguardStatusBarAnimateAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateViewState();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public KeyguardStatusBarViewController(KeyguardStatusBarView keyguardStatusBarView, CarrierTextController carrierTextController, ConfigurationController configurationController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, BatteryController batteryController, UserInfoController userInfoController, StatusBarIconController statusBarIconController, StatusBarIconController.TintedIconManager.Factory factory, BatteryMeterViewController batteryMeterViewController, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, KeyguardUpdateMonitor keyguardUpdateMonitor, BiometricUnlockController biometricUnlockController, SysuiStatusBarStateController sysuiStatusBarStateController, StatusBarContentInsetsProvider statusBarContentInsetsProvider, UserManager userManager, StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, StatusBarUserSwitcherController statusBarUserSwitcherController, StatusBarUserInfoTracker statusBarUserInfoTracker, SecureSettings secureSettings, @Main Executor executor) {
        super(keyguardStatusBarView);
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController2 = statusBarUserSwitcherFeatureController;
        this.mCarrierTextController = carrierTextController;
        this.mConfigurationController = configurationController;
        this.mAnimationScheduler = systemStatusAnimationScheduler;
        this.mBatteryController = batteryController;
        this.mUserInfoController = userInfoController;
        this.mStatusBarIconController = statusBarIconController;
        this.mTintedIconManagerFactory = factory;
        this.mBatteryMeterViewController = batteryMeterViewController;
        this.mNotificationPanelViewStateProvider = notificationPanelViewStateProvider;
        this.mKeyguardStateController = keyguardStateController2;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBiometricUnlockController = biometricUnlockController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mInsetsProvider = statusBarContentInsetsProvider;
        this.mUserManager = userManager;
        this.mFeatureController = statusBarUserSwitcherFeatureController2;
        this.mUserSwitcherController = statusBarUserSwitcherController;
        this.mStatusBarUserInfoTracker = statusBarUserInfoTracker;
        this.mSecureSettings = secureSettings;
        this.mMainExecutor = executor;
        this.mFirstBypassAttempt = keyguardBypassController.getBypassEnabled();
        keyguardStateController2.addCallback(new KeyguardStateController.Callback() {
            public void onKeyguardFadingAwayChanged() {
                if (!KeyguardStatusBarViewController.this.mKeyguardStateController.isKeyguardFadingAway()) {
                    boolean unused = KeyguardStatusBarViewController.this.mFirstBypassAttempt = false;
                    boolean unused2 = KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar = false;
                }
            }
        });
        Resources resources = getResources();
        updateBlockedIcons();
        this.mNotificationsHeaderCollideDistance = resources.getDimensionPixelSize(C1893R.dimen.header_notifications_collide_distance);
        ((KeyguardStatusBarView) this.mView).setKeyguardUserAvatarEnabled(!statusBarUserSwitcherFeatureController.isStatusBarUserSwitcherFeatureEnabled());
        statusBarUserSwitcherFeatureController2.addCallback((OnUserSwitcherPreferenceChangeListener) new KeyguardStatusBarViewController$$ExternalSyntheticLambda6(this));
        this.mSystemEventAnimator = new StatusBarSystemEventAnimator(this.mView, resources);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-android-systemui-statusbar-phone-KeyguardStatusBarViewController */
    public /* synthetic */ void mo44299xb4f786e2(boolean z) {
        ((KeyguardStatusBarView) this.mView).setKeyguardUserAvatarEnabled(!z);
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        super.onInit();
        this.mCarrierTextController.init();
        this.mBatteryMeterViewController.init();
        this.mUserSwitcherController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mAnimationScheduler.addCallback(this.mAnimationCallback);
        this.mUserInfoController.addCallback(this.mOnUserInfoChangedListener);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        if (this.mTintedIconManager == null) {
            StatusBarIconController.TintedIconManager create = this.mTintedIconManagerFactory.create((ViewGroup) ((KeyguardStatusBarView) this.mView).findViewById(C1893R.C1897id.statusIcons));
            this.mTintedIconManager = create;
            create.setBlockList(getBlockedIcons());
            this.mStatusBarIconController.addIconGroup(this.mTintedIconManager);
        }
        ((KeyguardStatusBarView) this.mView).setOnApplyWindowInsetsListener(new KeyguardStatusBarViewController$$ExternalSyntheticLambda0(this));
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("status_bar_show_vibrate_icon"), false, this.mVolumeSettingObserver, -1);
        updateUserSwitcher();
        onThemeChanged();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$5$com-android-systemui-statusbar-phone-KeyguardStatusBarViewController */
    public /* synthetic */ WindowInsets mo44300xd10be31f(View view, WindowInsets windowInsets) {
        return ((KeyguardStatusBarView) this.mView).updateWindowInsets(windowInsets, this.mInsetsProvider);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mAnimationScheduler.removeCallback(this.mAnimationCallback);
        this.mUserInfoController.removeCallback(this.mOnUserInfoChangedListener);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mSecureSettings.unregisterContentObserver(this.mVolumeSettingObserver);
        StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
        if (tintedIconManager != null) {
            this.mStatusBarIconController.removeIconGroup(tintedIconManager);
        }
    }

    public void onThemeChanged() {
        ((KeyguardStatusBarView) this.mView).onThemeChanged(this.mTintedIconManager);
    }

    public void setKeyguardUserSwitcherEnabled(boolean z) {
        ((KeyguardStatusBarView) this.mView).setKeyguardUserSwitcherEnabled(z);
        this.mStatusBarUserInfoTracker.checkEnabled();
    }

    public void setBatteryListening(boolean z) {
        if (z != this.mBatteryListening) {
            this.mBatteryListening = z;
            if (z) {
                this.mBatteryController.addCallback(this.mBatteryStateChangeCallback);
            } else {
                this.mBatteryController.removeCallback(this.mBatteryStateChangeCallback);
            }
        }
    }

    public void setNoTopClipping() {
        ((KeyguardStatusBarView) this.mView).setTopClipping(0);
    }

    public void updateTopClipping(int i) {
        ((KeyguardStatusBarView) this.mView).setTopClipping(i - ((KeyguardStatusBarView) this.mView).getTop());
    }

    public void setDozing(boolean z) {
        this.mDozing = z;
    }

    public void animateKeyguardStatusBarIn() {
        ((KeyguardStatusBarView) this.mView).setVisibility(0);
        ((KeyguardStatusBarView) this.mView).setAlpha(0.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.addUpdateListener(this.mAnimatorUpdateListener);
        ofFloat.setDuration(360);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.start();
    }

    public void animateKeyguardStatusBarOut(long j, long j2) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{((KeyguardStatusBarView) this.mView).getAlpha(), 0.0f});
        ofFloat.addUpdateListener(this.mAnimatorUpdateListener);
        ofFloat.setStartDelay(j);
        ofFloat.setDuration(j2);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).setVisibility(4);
                ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).setAlpha(1.0f);
                float unused = KeyguardStatusBarViewController.this.mKeyguardStatusBarAnimateAlpha = 1.0f;
            }
        });
        ofFloat.start();
    }

    public void updateViewState() {
        if (isKeyguardShowing()) {
            float min = 1.0f - Math.min(1.0f, this.mNotificationPanelViewStateProvider.getLockscreenShadeDragProgress() * 2.0f);
            float f = this.mExplicitAlpha;
            if (f == -1.0f) {
                f = Math.min(getKeyguardContentsAlpha(), min) * this.mKeyguardStatusBarAnimateAlpha * (1.0f - this.mKeyguardHeadsUpShowingAmount);
            }
            int i = 0;
            boolean z = (this.mFirstBypassAttempt && this.mKeyguardUpdateMonitor.shouldListenForFace()) || this.mDelayShowingKeyguardStatusBar;
            if (f == 0.0f || this.mDozing || z) {
                i = 4;
            }
            updateViewState(f, i);
        }
    }

    public void updateViewState(float f, int i) {
        ((KeyguardStatusBarView) this.mView).setAlpha(f);
        ((KeyguardStatusBarView) this.mView).setVisibility(i);
    }

    private float getKeyguardContentsAlpha() {
        float f;
        float f2;
        if (isKeyguardShowing()) {
            f2 = this.mNotificationPanelViewStateProvider.getPanelViewExpandedHeight();
            f = (float) (((KeyguardStatusBarView) this.mView).getHeight() + this.mNotificationsHeaderCollideDistance);
        } else {
            f2 = this.mNotificationPanelViewStateProvider.getPanelViewExpandedHeight();
            f = (float) ((KeyguardStatusBarView) this.mView).getHeight();
        }
        return (float) Math.pow((double) MathUtils.saturate(f2 / f), 0.75d);
    }

    /* access modifiers changed from: private */
    public void updateUserSwitcher() {
        ((KeyguardStatusBarView) this.mView).setUserSwitcherEnabled(this.mUserManager.isUserSwitcherEnabled(getResources().getBoolean(C1893R.bool.qs_show_user_switcher_for_single_user)));
    }

    /* access modifiers changed from: package-private */
    public void updateBlockedIcons() {
        List<String> statusBarIconBlocklist = StatusBarIconBlocklistKt.getStatusBarIconBlocklist(getResources(), this.mSecureSettings);
        synchronized (this.mLock) {
            this.mBlockedIcons.clear();
            this.mBlockedIcons.addAll(statusBarIconBlocklist);
        }
        this.mMainExecutor.execute(new KeyguardStatusBarViewController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateBlockedIcons$6$com-android-systemui-statusbar-phone-KeyguardStatusBarViewController */
    public /* synthetic */ void mo44301x6e64a4f() {
        StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
        if (tintedIconManager != null) {
            tintedIconManager.setBlockList(getBlockedIcons());
        }
    }

    /* access modifiers changed from: package-private */
    public List<String> getBlockedIcons() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mBlockedIcons);
        }
        return arrayList;
    }

    public void updateForHeadsUp() {
        updateForHeadsUp(true);
    }

    /* access modifiers changed from: package-private */
    public void updateForHeadsUp(boolean z) {
        boolean z2 = isKeyguardShowing() && this.mNotificationPanelViewStateProvider.shouldHeadsUpBeVisible();
        if (this.mShowingKeyguardHeadsUp != z2) {
            this.mShowingKeyguardHeadsUp = z2;
            float f = 0.0f;
            if (isKeyguardShowing()) {
                KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) this.mView;
                AnimatableProperty animatableProperty = this.mHeadsUpShowingAmountAnimation;
                if (z2) {
                    f = 1.0f;
                }
                PropertyAnimator.setProperty(keyguardStatusBarView, animatableProperty, f, KEYGUARD_HUN_PROPERTIES, z);
                return;
            }
            PropertyAnimator.applyImmediately((KeyguardStatusBarView) this.mView, this.mHeadsUpShowingAmountAnimation, 0.0f);
        }
    }

    private boolean isKeyguardShowing() {
        return this.mStatusBarState == 1;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardStatusBarView:");
        printWriter.println("  mBatteryListening: " + this.mBatteryListening);
        printWriter.println("  mExplicitAlpha: " + this.mExplicitAlpha);
        ((KeyguardStatusBarView) this.mView).dump(printWriter, strArr);
    }

    public void setAlpha(float f) {
        this.mExplicitAlpha = f;
        updateViewState();
    }
}
