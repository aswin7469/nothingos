package com.android.keyguard;

import android.graphics.Rect;
import android.util.Slog;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.util.TimeZone;
import javax.inject.Inject;

public class KeyguardStatusViewController extends ViewController<KeyguardStatusView> {
    private static final AnimationProperties CLOCK_ANIMATION_PROPERTIES = new AnimationProperties().setDuration(360);
    /* access modifiers changed from: private */
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final String TAG = "KeyguardStatusViewController";
    private final Rect mClipBounds = new Rect();
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onLocaleListChanged() {
            KeyguardStatusViewController.this.refreshTime();
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.onLocaleListChanged();
        }

        public void onDensityOrFontScaleChanged() {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.onDensityOrFontScaleChanged();
        }
    };
    private KeyguardUpdateMonitorCallback mInfoCallback = new KeyguardUpdateMonitorCallback() {
        public void onTimeChanged() {
            KeyguardStatusViewController.this.refreshTime();
        }

        public void onTimeFormatChanged(String str) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.refreshFormat();
        }

        public void onTimeZoneChanged(TimeZone timeZone) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.updateTimeZone(timeZone);
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            if (z) {
                if (KeyguardStatusViewController.DEBUG) {
                    Slog.v(KeyguardStatusViewController.TAG, "refresh statusview showing:" + z);
                }
                KeyguardStatusViewController.this.refreshTime();
            }
        }

        public void onUserSwitchComplete(int i) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.refreshFormat();
        }
    };
    /* access modifiers changed from: private */
    public final KeyguardClockSwitchController mKeyguardClockSwitchController;
    private final KeyguardSliceViewController mKeyguardSliceViewController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardVisibilityHelper mKeyguardVisibilityHelper;

    @Inject
    public KeyguardStatusViewController(KeyguardStatusView keyguardStatusView, KeyguardSliceViewController keyguardSliceViewController, KeyguardClockSwitchController keyguardClockSwitchController, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController) {
        super(keyguardStatusView);
        this.mKeyguardSliceViewController = keyguardSliceViewController;
        this.mKeyguardClockSwitchController = keyguardClockSwitchController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mConfigurationController = configurationController;
        this.mKeyguardVisibilityHelper = new KeyguardVisibilityHelper(this.mView, keyguardStateController, dozeParameters, screenOffAnimationController, true);
    }

    public void onInit() {
        this.mKeyguardClockSwitchController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public void dozeTimeTick() {
        refreshTime();
        this.mKeyguardSliceViewController.refresh();
    }

    public void setDarkAmount(float f) {
        ((KeyguardStatusView) this.mView).setDarkAmount(f);
    }

    public void displayClock(int i, boolean z) {
        this.mKeyguardClockSwitchController.displayClock(i, z);
    }

    public void animateFoldToAod() {
        this.mKeyguardClockSwitchController.animateFoldToAod();
    }

    public boolean hasCustomClock() {
        return this.mKeyguardClockSwitchController.hasCustomClock();
    }

    public void setTranslationYExcludingMedia(float f) {
        ((KeyguardStatusView) this.mView).setChildrenTranslationYExcludingMediaView(f);
    }

    public void setAlpha(float f) {
        if (!this.mKeyguardVisibilityHelper.isVisibilityAnimating()) {
            ((KeyguardStatusView) this.mView).setAlpha(f);
        }
    }

    public void setPivotX(float f) {
        ((KeyguardStatusView) this.mView).setPivotX(f);
    }

    public void setPivotY(float f) {
        ((KeyguardStatusView) this.mView).setPivotY(f);
    }

    public float getClockTextSize() {
        return this.mKeyguardClockSwitchController.getClockTextSize();
    }

    public int getLockscreenHeight() {
        return ((KeyguardStatusView) this.mView).getHeight() - this.mKeyguardClockSwitchController.getNotificationIconAreaHeight();
    }

    public int getClockBottom(int i) {
        return this.mKeyguardClockSwitchController.getClockBottom(i);
    }

    public boolean isClockTopAligned() {
        return this.mKeyguardClockSwitchController.isClockTopAligned();
    }

    public void setStatusAccessibilityImportance(int i) {
        ((KeyguardStatusView) this.mView).setImportantForAccessibility(i);
    }

    public void updatePosition(int i, int i2, float f, boolean z) {
        AnimationProperties animationProperties = CLOCK_ANIMATION_PROPERTIES;
        PropertyAnimator.setProperty((KeyguardStatusView) this.mView, AnimatableProperty.f376Y, (float) i2, animationProperties, z);
        this.mKeyguardClockSwitchController.updatePosition(i, f, animationProperties, z);
    }

    public void setKeyguardStatusViewVisibility(int i, boolean z, boolean z2, int i2) {
        this.mKeyguardVisibilityHelper.setViewVisibility(i, z, z2, i2);
    }

    /* access modifiers changed from: private */
    public void refreshTime() {
        this.mKeyguardClockSwitchController.refresh();
    }

    public void setClipBounds(Rect rect) {
        if (rect != null) {
            this.mClipBounds.set(rect.left, (int) (((float) rect.top) - ((KeyguardStatusView) this.mView).getY()), rect.right, (int) (((float) rect.bottom) - ((KeyguardStatusView) this.mView).getY()));
            ((KeyguardStatusView) this.mView).setClipBounds(this.mClipBounds);
            return;
        }
        ((KeyguardStatusView) this.mView).setClipBounds((Rect) null);
    }
}
