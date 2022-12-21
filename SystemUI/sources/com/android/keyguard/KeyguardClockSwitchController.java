package com.android.keyguard;

import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.keyguard.clock.ClockManager;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationIconContainer;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.SecureSettings;
import com.nothing.systemui.keyguard.NTKeyguardClockView;
import com.nothing.systemui.keyguard.NTKeyguardClockViewController;
import com.nothing.systemui.keyguard.NTKeyguardClockViewLarge;
import com.nothing.systemui.keyguard.NTKeyguardClockViewLargeController;
import java.p026io.PrintWriter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class KeyguardClockSwitchController extends ViewController<KeyguardClockSwitch> implements Dumpable {
    private static final boolean CUSTOM_CLOCKS_ENABLED = true;
    private final BatteryController mBatteryController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private boolean mCanShowDoubleLineClock = true;
    private final ClockManager.ClockChangedListener mClockChangedListener = new KeyguardClockSwitchController$$ExternalSyntheticLambda2(this);
    private FrameLayout mClockFrame;
    private final ClockManager mClockManager;
    private AnimatableClockController mClockViewController;
    private final SysuiColorExtractor mColorExtractor;
    private final ColorExtractor.OnColorsChangedListener mColorsListener = new KeyguardClockSwitchController$$ExternalSyntheticLambda1(this);
    private int mCurrentClockSize = 1;
    private ContentObserver mDoubleLineClockObserver = new ContentObserver((Handler) null) {
        public void onChange(boolean z) {
            KeyguardClockSwitchController.this.updateDoubleLineClock();
        }
    };
    private final DumpManager mDumpManager;
    private int mKeyguardClockTopMargin = 0;
    private final KeyguardSliceViewController mKeyguardSliceViewController;
    private final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    private final KeyguardUnlockAnimationController.KeyguardUnlockAnimationListener mKeyguardUnlockAnimationListener = new KeyguardUnlockAnimationController.KeyguardUnlockAnimationListener() {
        public void onUnlockAnimationFinished() {
            KeyguardClockSwitchController.this.setClipChildrenForUnlock(true);
        }
    };
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private FrameLayout mLargeClockFrame;
    private AnimatableClockController mLargeClockViewController;
    private NTKeyguardClockViewController mNothingClockViewController;
    private NTKeyguardClockViewLargeController mNothingClockViewLargeController;
    private final NotificationIconAreaController mNotificationIconAreaController;
    private boolean mOnlyClock = false;
    private final Resources mResources;
    private final SecureSettings mSecureSettings;
    private final LockscreenSmartspaceController mSmartspaceController;
    private View mSmartspaceView;
    private ViewGroup mStatusArea;
    private final StatusBarStateController mStatusBarStateController;
    private Executor mUiExecutor;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardClockSwitchController  reason: not valid java name */
    public /* synthetic */ void m2284lambda$new$0$comandroidkeyguardKeyguardClockSwitchController(ColorExtractor colorExtractor, int i) {
        if ((i & 2) != 0) {
            ((KeyguardClockSwitch) this.mView).updateColors(getGradientColors());
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public KeyguardClockSwitchController(KeyguardClockSwitch keyguardClockSwitch, StatusBarStateController statusBarStateController, SysuiColorExtractor sysuiColorExtractor, ClockManager clockManager, KeyguardSliceViewController keyguardSliceViewController, NotificationIconAreaController notificationIconAreaController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor, LockscreenSmartspaceController lockscreenSmartspaceController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SecureSettings secureSettings, @Main Executor executor, @Main Resources resources, DumpManager dumpManager) {
        super(keyguardClockSwitch);
        this.mStatusBarStateController = statusBarStateController;
        this.mColorExtractor = sysuiColorExtractor;
        this.mClockManager = clockManager;
        this.mKeyguardSliceViewController = keyguardSliceViewController;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mBatteryController = batteryController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mSmartspaceController = lockscreenSmartspaceController;
        this.mResources = resources;
        this.mSecureSettings = secureSettings;
        this.mUiExecutor = executor;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mDumpManager = dumpManager;
    }

    public void setOnlyClock(boolean z) {
        this.mOnlyClock = z;
    }

    public void onInit() {
        this.mKeyguardSliceViewController.init();
        this.mClockFrame = (FrameLayout) ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.lockscreen_clock_view);
        this.mLargeClockFrame = (FrameLayout) ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.lockscreen_clock_view_large);
        AnimatableClockController animatableClockController = new AnimatableClockController((AnimatableClockView) ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.animatable_clock_view), this.mStatusBarStateController, this.mBroadcastDispatcher, this.mBatteryController, this.mKeyguardUpdateMonitor, this.mResources);
        this.mClockViewController = animatableClockController;
        animatableClockController.init();
        AnimatableClockController animatableClockController2 = new AnimatableClockController((AnimatableClockView) ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.animatable_clock_view_large), this.mStatusBarStateController, this.mBroadcastDispatcher, this.mBatteryController, this.mKeyguardUpdateMonitor, this.mResources);
        this.mLargeClockViewController = animatableClockController2;
        animatableClockController2.init();
        NTKeyguardClockViewController nTKeyguardClockViewController = new NTKeyguardClockViewController((NTKeyguardClockView) ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.nothing_keyguard_clock_view), this.mStatusBarStateController, this.mBroadcastDispatcher, this.mBatteryController, this.mKeyguardUpdateMonitor);
        this.mNothingClockViewController = nTKeyguardClockViewController;
        nTKeyguardClockViewController.init();
        NTKeyguardClockViewLargeController nTKeyguardClockViewLargeController = new NTKeyguardClockViewLargeController((NTKeyguardClockViewLarge) ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.nothing_keyguard_clock_view_large), this.mStatusBarStateController, this.mBroadcastDispatcher, this.mBatteryController, this.mKeyguardUpdateMonitor);
        this.mNothingClockViewLargeController = nTKeyguardClockViewLargeController;
        nTKeyguardClockViewLargeController.init();
        this.mDumpManager.unregisterDumpable(getClass().toString());
        this.mDumpManager.registerDumpable(getClass().toString(), this);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mClockManager.addOnClockChangedListener(this.mClockChangedListener);
        this.mColorExtractor.addOnColorsChangedListener(this.mColorsListener);
        ((KeyguardClockSwitch) this.mView).updateColors(getGradientColors());
        this.mKeyguardClockTopMargin = ((KeyguardClockSwitch) this.mView).getResources().getDimensionPixelSize(C1893R.dimen.keyguard_clock_top_margin);
        if (this.mOnlyClock) {
            ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.keyguard_slice_view).setVisibility(8);
            ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.left_aligned_notification_icon_container).setVisibility(8);
            return;
        }
        updateAodIcons();
        this.mStatusArea = (ViewGroup) ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.keyguard_status_area);
        if (this.mSmartspaceController.isEnabled()) {
            View findViewById = ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.keyguard_slice_view);
            int indexOfChild = this.mStatusArea.indexOfChild(findViewById);
            findViewById.setVisibility(8);
            addSmartspaceView(indexOfChild);
            updateClockLayout();
        }
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("lockscreen_use_double_line_clock"), false, this.mDoubleLineClockObserver, -1);
        updateDoubleLineClock();
        this.mKeyguardUnlockAnimationController.addKeyguardUnlockAnimationListener(this.mKeyguardUnlockAnimationListener);
    }

    /* access modifiers changed from: package-private */
    public int getNotificationIconAreaHeight() {
        return this.mNotificationIconAreaController.getHeight();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mClockManager.removeOnClockChangedListener(this.mClockChangedListener);
        this.mColorExtractor.removeOnColorsChangedListener(this.mColorsListener);
        ((KeyguardClockSwitch) this.mView).setClockPlugin((ClockPlugin) null, this.mStatusBarStateController.getState());
        this.mSecureSettings.unregisterContentObserver(this.mDoubleLineClockObserver);
        this.mKeyguardUnlockAnimationController.removeKeyguardUnlockAnimationListener(this.mKeyguardUnlockAnimationListener);
    }

    /* access modifiers changed from: package-private */
    public void onLocaleListChanged() {
        int indexOfChild;
        if (this.mSmartspaceController.isEnabled() && (indexOfChild = this.mStatusArea.indexOfChild(this.mSmartspaceView)) >= 0) {
            this.mStatusArea.removeView(this.mSmartspaceView);
            addSmartspaceView(indexOfChild);
        }
    }

    private void addSmartspaceView(int i) {
        this.mSmartspaceView = this.mSmartspaceController.buildAndConnectView((ViewGroup) this.mView);
        this.mStatusArea.addView(this.mSmartspaceView, i, new LinearLayout.LayoutParams(-1, -2));
        this.mSmartspaceView.setPaddingRelative(getContext().getResources().getDimensionPixelSize(C1893R.dimen.below_clock_padding_start), 0, getContext().getResources().getDimensionPixelSize(C1893R.dimen.below_clock_padding_end), 0);
        this.mKeyguardUnlockAnimationController.setLockscreenSmartspace(this.mSmartspaceView);
    }

    public void onDensityOrFontScaleChanged() {
        ((KeyguardClockSwitch) this.mView).onDensityOrFontScaleChanged();
        this.mKeyguardClockTopMargin = ((KeyguardClockSwitch) this.mView).getResources().getDimensionPixelSize(C1893R.dimen.keyguard_clock_top_margin);
        updateClockLayout();
    }

    private void updateClockLayout() {
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(C1893R.dimen.keyguard_large_clock_top_margin) - ((int) this.mLargeClockViewController.getBottom());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.topMargin = dimensionPixelSize;
        this.mLargeClockFrame.setLayoutParams(layoutParams);
    }

    public void displayClock(int i, boolean z) {
        if (this.mCanShowDoubleLineClock || i != 0) {
            this.mCurrentClockSize = i;
            boolean switchToClock = ((KeyguardClockSwitch) this.mView).switchToClock(i, z);
            if (z && switchToClock && i == 0) {
                this.mLargeClockViewController.animateAppear();
            }
        }
    }

    public void animateFoldToAod() {
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.animateFoldAppear();
            this.mLargeClockViewController.animateFoldAppear();
        }
    }

    public boolean hasCustomClock() {
        return ((KeyguardClockSwitch) this.mView).hasCustomClock();
    }

    public float getClockTextSize() {
        return ((KeyguardClockSwitch) this.mView).getTextSize();
    }

    /* access modifiers changed from: package-private */
    public void refresh() {
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.refreshTime();
            this.mLargeClockViewController.refreshTime();
        }
        LockscreenSmartspaceController lockscreenSmartspaceController = this.mSmartspaceController;
        if (lockscreenSmartspaceController != null) {
            lockscreenSmartspaceController.requestSmartspaceUpdate();
        }
        NTKeyguardClockViewController nTKeyguardClockViewController = this.mNothingClockViewController;
        if (nTKeyguardClockViewController != null) {
            nTKeyguardClockViewController.refreshTime();
        }
        NTKeyguardClockViewLargeController nTKeyguardClockViewLargeController = this.mNothingClockViewLargeController;
        if (nTKeyguardClockViewLargeController != null) {
            nTKeyguardClockViewLargeController.refreshTime();
        }
        ((KeyguardClockSwitch) this.mView).refresh();
    }

    /* access modifiers changed from: package-private */
    public void updatePosition(int i, float f, AnimationProperties animationProperties, boolean z) {
        if (getCurrentLayoutDirection() == 1) {
            i = -i;
        }
        float f2 = (float) i;
        PropertyAnimator.setProperty(this.mClockFrame, AnimatableProperty.TRANSLATION_X, f2, animationProperties, z);
        PropertyAnimator.setProperty(this.mLargeClockFrame, AnimatableProperty.SCALE_X, f, animationProperties, z);
        PropertyAnimator.setProperty(this.mLargeClockFrame, AnimatableProperty.SCALE_Y, f, animationProperties, z);
        ViewGroup viewGroup = this.mStatusArea;
        if (viewGroup != null) {
            PropertyAnimator.setProperty(viewGroup, AnimatableProperty.TRANSLATION_X, f2, animationProperties, z);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateTimeZone(TimeZone timeZone) {
        ((KeyguardClockSwitch) this.mView).onTimeZoneChanged(timeZone);
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.onTimeZoneChanged(timeZone);
            this.mLargeClockViewController.onTimeZoneChanged(timeZone);
        }
        NTKeyguardClockViewController nTKeyguardClockViewController = this.mNothingClockViewController;
        if (nTKeyguardClockViewController != null) {
            nTKeyguardClockViewController.onTimeZoneChanged(timeZone);
        }
        NTKeyguardClockViewLargeController nTKeyguardClockViewLargeController = this.mNothingClockViewLargeController;
        if (nTKeyguardClockViewLargeController != null) {
            nTKeyguardClockViewLargeController.onTimeZoneChanged(timeZone);
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshFormat() {
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.refreshFormat();
            this.mLargeClockViewController.refreshFormat();
        }
        NTKeyguardClockViewController nTKeyguardClockViewController = this.mNothingClockViewController;
        if (nTKeyguardClockViewController != null) {
            nTKeyguardClockViewController.refreshFormat();
        }
        NTKeyguardClockViewLargeController nTKeyguardClockViewLargeController = this.mNothingClockViewLargeController;
        if (nTKeyguardClockViewLargeController != null) {
            nTKeyguardClockViewLargeController.refreshFormat();
        }
    }

    /* access modifiers changed from: package-private */
    public int getClockBottom(int i) {
        if (this.mLargeClockFrame.getVisibility() != 0) {
            return this.mClockFrame.findViewById(C1893R.C1897id.animatable_clock_view).getHeight() + i + this.mKeyguardClockTopMargin;
        }
        View findViewById = this.mLargeClockFrame.findViewById(C1893R.C1897id.animatable_clock_view_large);
        return (this.mLargeClockFrame.getHeight() / 2) + (findViewById.getHeight() / 2);
    }

    /* access modifiers changed from: package-private */
    public boolean isClockTopAligned() {
        return this.mLargeClockFrame.getVisibility() != 0;
    }

    private void updateAodIcons() {
        this.mNotificationIconAreaController.setupAodIcons((NotificationIconContainer) ((KeyguardClockSwitch) this.mView).findViewById(C1893R.C1897id.left_aligned_notification_icon_container));
    }

    /* access modifiers changed from: private */
    public void setClockPlugin(ClockPlugin clockPlugin) {
        ((KeyguardClockSwitch) this.mView).setClockPlugin(clockPlugin, this.mStatusBarStateController.getState());
    }

    private ColorExtractor.GradientColors getGradientColors() {
        return this.mColorExtractor.getColors(2);
    }

    private int getCurrentLayoutDirection() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
    }

    /* access modifiers changed from: private */
    public void updateDoubleLineClock() {
        boolean z = true;
        if (this.mSecureSettings.getIntForUser("lockscreen_use_double_line_clock", 1, -2) == 0) {
            z = false;
        }
        this.mCanShowDoubleLineClock = z;
        if (!z) {
            this.mUiExecutor.execute(new KeyguardClockSwitchController$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateDoubleLineClock$1$com-android-keyguard-KeyguardClockSwitchController */
    public /* synthetic */ void mo25717xba64efcf() {
        displayClock(1, true);
    }

    /* access modifiers changed from: private */
    public void setClipChildrenForUnlock(boolean z) {
        ((KeyguardClockSwitch) this.mView).setClipChildren(z);
        ViewGroup viewGroup = this.mStatusArea;
        if (viewGroup != null) {
            viewGroup.setClipChildren(z);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("currentClockSizeLarge=" + (this.mCurrentClockSize == 0));
        printWriter.println("mCanShowDoubleLineClock=" + this.mCanShowDoubleLineClock);
        this.mClockViewController.dump(printWriter);
        this.mLargeClockViewController.dump(printWriter);
    }
}
