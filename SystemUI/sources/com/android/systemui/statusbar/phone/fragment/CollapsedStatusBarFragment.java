package com.android.systemui.statusbar.phone.fragment;

import android.animation.Animator;
import android.app.Fragment;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.OperatorNameView;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.EncryptionHelper;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class CollapsedStatusBarFragment extends Fragment implements CommandQueue.Callbacks, StatusBarStateController.StateListener, SystemStatusAnimationCallback {
    private static final String EXTRA_PANEL_STATE = "panel_state";
    public static final int FADE_IN_DELAY = 50;
    public static final int FADE_IN_DURATION = 320;
    public static final String STATUS_BAR_ICON_MANAGER_TAG = "status_bar_icon_manager";
    public static final String TAG = "CollapsedStatusBarFragment";
    private final SystemStatusAnimationScheduler mAnimationScheduler;
    private List<String> mBlockedIcons = new ArrayList();
    private final CarrierConfigTracker.CarrierConfigChangedListener mCarrierConfigCallback = new CarrierConfigTracker.CarrierConfigChangedListener() {
        public void onCarrierConfigChanged() {
            if (CollapsedStatusBarFragment.this.mOperatorNameViewController == null) {
                CollapsedStatusBarFragment.this.initOperatorName();
            }
        }
    };
    private final CarrierConfigTracker mCarrierConfigTracker;
    private View mClockView;
    private final CollapsedStatusBarFragmentLogger mCollapsedStatusBarFragmentLogger;
    /* access modifiers changed from: private */
    public final CommandQueue mCommandQueue;
    private StatusBarIconController.DarkIconManager mDarkIconManager;
    private final CarrierConfigTracker.DefaultDataSubscriptionChangedListener mDefaultDataListener = new CarrierConfigTracker.DefaultDataSubscriptionChangedListener() {
        public void onDefaultSubscriptionChanged(int i) {
            if (CollapsedStatusBarFragment.this.mOperatorNameViewController == null) {
                CollapsedStatusBarFragment.this.initOperatorName();
            }
        }
    };
    /* access modifiers changed from: private */
    public int mDisabled1;
    /* access modifiers changed from: private */
    public int mDisabled2;
    private final FeatureFlags mFeatureFlags;
    private final KeyguardStateController mKeyguardStateController;
    private final StatusBarLocationPublisher mLocationPublisher;
    private final Executor mMainExecutor;
    private final NetworkController mNetworkController;
    private final NotificationIconAreaController mNotificationIconAreaController;
    private View mNotificationIconAreaInner;
    private final NotificationPanelViewController mNotificationPanelViewController;
    private View mOngoingCallChip;
    private final OngoingCallController mOngoingCallController;
    private final OngoingCallListener mOngoingCallListener = new OngoingCallListener() {
        public void onOngoingCallStateChanged(boolean z) {
            CollapsedStatusBarFragment collapsedStatusBarFragment = CollapsedStatusBarFragment.this;
            collapsedStatusBarFragment.disable(collapsedStatusBarFragment.getContext().getDisplayId(), CollapsedStatusBarFragment.this.mDisabled1, CollapsedStatusBarFragment.this.mDisabled2, z);
        }
    };
    /* access modifiers changed from: private */
    public OperatorNameViewController mOperatorNameViewController;
    private final OperatorNameViewController.Factory mOperatorNameViewControllerFactory;
    private final PanelExpansionStateManager mPanelExpansionStateManager;
    private final SecureSettings mSecureSettings;
    private SignalCallback mSignalCallback = new SignalCallback() {
        public void setIsAirplaneMode(IconState iconState) {
            CollapsedStatusBarFragment.this.mCommandQueue.recomputeDisableFlags(CollapsedStatusBarFragment.this.getContext().getDisplayId(), true);
        }
    };
    private PhoneStatusBarView mStatusBar;
    private StatusBarFragmentComponent mStatusBarFragmentComponent;
    private final StatusBarFragmentComponent.Factory mStatusBarFragmentComponentFactory;
    private final StatusBarHideIconsForBouncerManager mStatusBarHideIconsForBouncerManager;
    private final StatusBarIconController mStatusBarIconController;
    private View.OnLayoutChangeListener mStatusBarLayoutListener = new CollapsedStatusBarFragment$$ExternalSyntheticLambda0(this);
    private final StatusBarStateController mStatusBarStateController;
    private StatusBarSystemEventAnimator mSystemEventAnimator;
    private LinearLayout mSystemIconArea;
    private boolean mSystemIconAreaPendingToShow;
    private final ContentObserver mVolumeSettingObserver = new ContentObserver((Handler) null) {
        public void onChange(boolean z) {
            CollapsedStatusBarFragment.this.updateBlockedIcons();
        }
    };

    public void onStateChanged(int i) {
    }

    public CollapsedStatusBarFragment(StatusBarFragmentComponent.Factory factory, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, NotificationIconAreaController notificationIconAreaController, PanelExpansionStateManager panelExpansionStateManager, FeatureFlags featureFlags, StatusBarIconController statusBarIconController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, KeyguardStateController keyguardStateController, NotificationPanelViewController notificationPanelViewController, NetworkController networkController, StatusBarStateController statusBarStateController, CommandQueue commandQueue, CarrierConfigTracker carrierConfigTracker, CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger, OperatorNameViewController.Factory factory2, SecureSettings secureSettings, @Main Executor executor) {
        this.mStatusBarFragmentComponentFactory = factory;
        this.mOngoingCallController = ongoingCallController;
        this.mAnimationScheduler = systemStatusAnimationScheduler;
        this.mLocationPublisher = statusBarLocationPublisher;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        this.mFeatureFlags = featureFlags;
        this.mStatusBarIconController = statusBarIconController;
        this.mStatusBarHideIconsForBouncerManager = statusBarHideIconsForBouncerManager;
        this.mKeyguardStateController = keyguardStateController;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mNetworkController = networkController;
        this.mStatusBarStateController = statusBarStateController;
        this.mCommandQueue = commandQueue;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mCollapsedStatusBarFragmentLogger = collapsedStatusBarFragmentLogger;
        this.mOperatorNameViewControllerFactory = factory2;
        this.mSecureSettings = secureSettings;
        this.mMainExecutor = executor;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C1894R.layout.status_bar, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        StatusBarFragmentComponent create = this.mStatusBarFragmentComponentFactory.create(this);
        this.mStatusBarFragmentComponent = create;
        create.init();
        PhoneStatusBarView phoneStatusBarView = (PhoneStatusBarView) view;
        this.mStatusBar = phoneStatusBarView;
        View findViewById = phoneStatusBarView.findViewById(C1894R.C1898id.status_bar_contents);
        findViewById.addOnLayoutChangeListener(this.mStatusBarLayoutListener);
        updateStatusBarLocation(findViewById.getLeft(), findViewById.getRight());
        if (bundle != null && bundle.containsKey(EXTRA_PANEL_STATE)) {
            this.mStatusBar.restoreHierarchyState(bundle.getSparseParcelableArray(EXTRA_PANEL_STATE));
        }
        StatusBarIconController.DarkIconManager darkIconManager = new StatusBarIconController.DarkIconManager((LinearLayout) view.findViewById(C1894R.C1898id.statusIcons), this.mFeatureFlags);
        this.mDarkIconManager = darkIconManager;
        darkIconManager.setShouldLog(true);
        updateBlockedIcons();
        this.mStatusBarIconController.addIconGroup(this.mDarkIconManager);
        this.mSystemIconArea = (LinearLayout) this.mStatusBar.findViewById(C1894R.C1898id.system_icon_area);
        this.mClockView = this.mStatusBar.findViewById(C1894R.C1898id.clock);
        this.mOngoingCallChip = this.mStatusBar.findViewById(C1894R.C1898id.ongoing_call_chip);
        showSystemIconArea(false);
        showClock(false);
        initEmergencyCryptkeeperText();
        initOperatorName();
        initNotificationIconArea();
        this.mSystemEventAnimator = new StatusBarSystemEventAnimator(this.mSystemIconArea, getResources());
        this.mCarrierConfigTracker.addCallback(this.mCarrierConfigCallback);
        this.mCarrierConfigTracker.addDefaultDataSubscriptionChangedListener(this.mDefaultDataListener);
    }

    /* access modifiers changed from: package-private */
    public void updateBlockedIcons() {
        this.mBlockedIcons.clear();
        List asList = Arrays.asList(getResources().getStringArray(C1894R.array.config_collapsed_statusbar_icon_blocklist));
        String string = getString(17041585);
        boolean z = this.mSecureSettings.getIntForUser("status_bar_show_vibrate_icon", 0, -2) == 0;
        for (int i = 0; i < asList.size(); i++) {
            if (!((String) asList.get(i)).equals(string)) {
                this.mBlockedIcons.add((String) asList.get(i));
            } else if (z) {
                this.mBlockedIcons.add((String) asList.get(i));
            }
        }
        this.mMainExecutor.execute(new CollapsedStatusBarFragment$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateBlockedIcons$0$com-android-systemui-statusbar-phone-fragment-CollapsedStatusBarFragment */
    public /* synthetic */ void mo45450x91f80fd0() {
        this.mDarkIconManager.setBlockList(this.mBlockedIcons);
    }

    /* access modifiers changed from: package-private */
    public List<String> getBlockedIcons() {
        return this.mBlockedIcons;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        SparseArray sparseArray = new SparseArray();
        this.mStatusBar.saveHierarchyState(sparseArray);
        bundle.putSparseParcelableArray(EXTRA_PANEL_STATE, sparseArray);
    }

    public void onResume() {
        super.onResume();
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.addCallback(this);
        initOngoingCallChip();
        this.mAnimationScheduler.addCallback((SystemStatusAnimationCallback) this);
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("status_bar_show_vibrate_icon"), false, this.mVolumeSettingObserver, -1);
    }

    public void onPause() {
        super.onPause();
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.removeCallback(this);
        this.mOngoingCallController.removeCallback(this.mOngoingCallListener);
        this.mAnimationScheduler.removeCallback((SystemStatusAnimationCallback) this);
        this.mSecureSettings.unregisterContentObserver(this.mVolumeSettingObserver);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mStatusBarIconController.removeIconGroup(this.mDarkIconManager);
        if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
            this.mNetworkController.removeCallback(this.mSignalCallback);
        }
        this.mCarrierConfigTracker.removeCallback(this.mCarrierConfigCallback);
        this.mCarrierConfigTracker.removeDataSubscriptionChangedListener(this.mDefaultDataListener);
    }

    public void initNotificationIconArea() {
        ViewGroup viewGroup = (ViewGroup) this.mStatusBar.findViewById(C1894R.C1898id.notification_icon_area);
        View notificationInnerAreaView = this.mNotificationIconAreaController.getNotificationInnerAreaView();
        this.mNotificationIconAreaInner = notificationInnerAreaView;
        if (notificationInnerAreaView.getParent() != null) {
            ((ViewGroup) this.mNotificationIconAreaInner.getParent()).removeView(this.mNotificationIconAreaInner);
        }
        viewGroup.addView(this.mNotificationIconAreaInner);
        updateNotificationIconAreaAndCallChip(this.mDisabled1, false);
    }

    public StatusBarFragmentComponent getStatusBarFragmentComponent() {
        return this.mStatusBarFragmentComponent;
    }

    public void disable(int i, int i2, int i3, boolean z) {
        if (i == getContext().getDisplayId()) {
            int adjustDisableFlags = adjustDisableFlags(i2);
            this.mCollapsedStatusBarFragmentLogger.logDisableFlagChange(new DisableFlagsLogger.DisableState(i2, i3), new DisableFlagsLogger.DisableState(adjustDisableFlags, i3));
            int i4 = this.mDisabled1 ^ adjustDisableFlags;
            int i5 = this.mDisabled2 ^ i3;
            this.mDisabled1 = adjustDisableFlags;
            this.mDisabled2 = i3;
            if (!((i4 & 1048576) == 0 && (i5 & 2) == 0)) {
                if ((adjustDisableFlags & 1048576) == 0 && (i3 & 2) == 0) {
                    showSystemIconArea(z);
                    showOperatorName(z);
                } else {
                    hideSystemIconArea(z);
                    hideOperatorName(z);
                }
            }
            if (!((67108864 & i4) == 0 && (131072 & i4) == 0)) {
                updateNotificationIconAreaAndCallChip(adjustDisableFlags, z);
            }
            if ((i4 & 8388608) != 0 || this.mClockView.getVisibility() != clockHiddenMode()) {
                if ((adjustDisableFlags & 8388608) != 0) {
                    hideClock(z);
                } else {
                    showClock(z);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public int adjustDisableFlags(int i) {
        boolean shouldBeVisible = this.mStatusBarFragmentComponent.getHeadsUpAppearanceController().shouldBeVisible();
        if (shouldBeVisible) {
            i |= 8388608;
        }
        if (!this.mKeyguardStateController.isLaunchTransitionFadingAway() && !this.mKeyguardStateController.isKeyguardFadingAway() && shouldHideNotificationIcons() && (this.mStatusBarStateController.getState() != 1 || !shouldBeVisible)) {
            i = i | 131072 | 1048576 | 8388608;
        }
        if (this.mNetworkController != null && EncryptionHelper.IS_DATA_ENCRYPTED) {
            if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
                i |= 131072;
            }
            if (!this.mNetworkController.isRadioOn()) {
                i |= 1048576;
            }
        }
        if (this.mStatusBarStateController.isDozing() && this.mNotificationPanelViewController.hasCustomClock()) {
            i |= 9437184;
        }
        return this.mOngoingCallController.hasOngoingCall() ? -67108865 & i : 67108864 | i;
    }

    private void updateNotificationIconAreaAndCallChip(int i, boolean z) {
        boolean z2 = true;
        boolean z3 = (131072 & i) != 0;
        boolean z4 = (i & 67108864) == 0;
        if (z3 || z4) {
            hideNotificationIconArea(z);
        } else {
            showNotificationIconArea(z);
        }
        if (!z4 || z3) {
            z2 = false;
        }
        if (z2) {
            showOngoingCallChip(z);
        } else {
            hideOngoingCallChip(z);
        }
        this.mOngoingCallController.notifyChipVisibilityChanged(z2);
    }

    private boolean shouldHideNotificationIcons() {
        if (this.mPanelExpansionStateManager.isClosed() || !this.mNotificationPanelViewController.hideStatusBarIconsWhenExpanded()) {
            return this.mStatusBarHideIconsForBouncerManager.getShouldHideStatusBarIconsForBouncer();
        }
        return true;
    }

    private void hideSystemIconArea(boolean z) {
        this.mSystemIconAreaPendingToShow = false;
        animateHide(this.mSystemIconArea, z);
    }

    private void showSystemIconArea(boolean z) {
        int animationState = this.mAnimationScheduler.getAnimationState();
        if (animationState == 0 || animationState == 5) {
            animateShow(this.mSystemIconArea, z);
        } else {
            this.mSystemIconAreaPendingToShow = true;
        }
    }

    private void hideClock(boolean z) {
        animateHiddenState(this.mClockView, clockHiddenMode(), z);
    }

    private void showClock(boolean z) {
        animateShow(this.mClockView, z);
    }

    public void hideOngoingCallChip(boolean z) {
        animateHiddenState(this.mOngoingCallChip, 8, z);
    }

    public void showOngoingCallChip(boolean z) {
        animateShow(this.mOngoingCallChip, z);
    }

    private int clockHiddenMode() {
        return (this.mPanelExpansionStateManager.isClosed() || this.mKeyguardStateController.isShowing() || this.mStatusBarStateController.isDozing()) ? 8 : 4;
    }

    public void hideNotificationIconArea(boolean z) {
        animateHide(this.mNotificationIconAreaInner, z);
    }

    public void showNotificationIconArea(boolean z) {
        animateShow(this.mNotificationIconAreaInner, z);
    }

    public void hideOperatorName(boolean z) {
        OperatorNameViewController operatorNameViewController = this.mOperatorNameViewController;
        if (operatorNameViewController != null) {
            animateHide(operatorNameViewController.getView(), z);
        }
    }

    public void showOperatorName(boolean z) {
        OperatorNameViewController operatorNameViewController = this.mOperatorNameViewController;
        if (operatorNameViewController != null) {
            animateShow(operatorNameViewController.getView(), z);
        }
    }

    private void animateHiddenState(View view, int i, boolean z) {
        view.animate().cancel();
        if (!z) {
            view.setAlpha(0.0f);
            view.setVisibility(i);
            return;
        }
        view.animate().alpha(0.0f).setDuration(160).setStartDelay(0).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(new CollapsedStatusBarFragment$$ExternalSyntheticLambda1(view, i));
    }

    private void animateHide(View view, boolean z) {
        animateHiddenState(view, 4, z);
    }

    private void animateShow(View view, boolean z) {
        view.animate().cancel();
        view.setVisibility(0);
        if (!z) {
            view.setAlpha(1.0f);
            return;
        }
        view.animate().alpha(1.0f).setDuration(320).setInterpolator(Interpolators.ALPHA_IN).setStartDelay(50).withEndAction((Runnable) null);
        if (this.mKeyguardStateController.isKeyguardFadingAway()) {
            view.animate().setDuration(this.mKeyguardStateController.getKeyguardFadingAwayDuration()).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).start();
        }
    }

    private void initEmergencyCryptkeeperText() {
        View findViewById = this.mStatusBar.findViewById(C1894R.C1898id.emergency_cryptkeeper_text);
        if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
            if (findViewById != null) {
                ((ViewStub) findViewById).inflate();
            }
            this.mNetworkController.addCallback(this.mSignalCallback);
        } else if (findViewById != null) {
            ((ViewGroup) findViewById.getParent()).removeView(findViewById);
        }
    }

    /* access modifiers changed from: private */
    public void initOperatorName() {
        if (this.mCarrierConfigTracker.getShowOperatorNameInStatusBarConfig(SubscriptionManager.getDefaultDataSubscriptionId())) {
            OperatorNameViewController create = this.mOperatorNameViewControllerFactory.create((OperatorNameView) ((ViewStub) this.mStatusBar.findViewById(C1894R.C1898id.operator_name)).inflate());
            this.mOperatorNameViewController = create;
            create.init();
            if (this.mKeyguardStateController.isShowing()) {
                hideOperatorName(false);
            }
        }
    }

    private void initOngoingCallChip() {
        this.mOngoingCallController.addCallback(this.mOngoingCallListener);
        this.mOngoingCallController.setChipView(this.mOngoingCallChip);
    }

    public void onDozingChanged(boolean z) {
        disable(getContext().getDisplayId(), this.mDisabled1, this.mDisabled2, false);
    }

    public Animator onSystemEventAnimationBegin() {
        return this.mSystemEventAnimator.onSystemEventAnimationBegin();
    }

    public Animator onSystemEventAnimationFinish(boolean z) {
        if (this.mSystemIconAreaPendingToShow) {
            this.mSystemIconAreaPendingToShow = false;
            animateShow(this.mSystemIconArea, false);
        }
        return this.mSystemEventAnimator.onSystemEventAnimationFinish(z);
    }

    private boolean isSystemIconAreaDisabled() {
        return ((this.mDisabled1 & 1048576) == 0 && (this.mDisabled2 & 2) == 0) ? false : true;
    }

    private void updateStatusBarLocation(int i, int i2) {
        this.mLocationPublisher.updateStatusBarMargin(i - this.mStatusBar.getLeft(), this.mStatusBar.getRight() - i2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-statusbar-phone-fragment-CollapsedStatusBarFragment */
    public /* synthetic */ void mo45449x8ee6af5f(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i != i5 || i3 != i7) {
            updateStatusBarLocation(i, i3);
        }
    }
}
