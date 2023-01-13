package com.nothing.systemui.keyguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.NumberFormat;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class NTKeyguardClockViewLargeController extends ViewController<NTKeyguardClockViewLarge> {
    private static final int FORMAT_NUMBER = 1234567890;
    private final BatteryController.BatteryStateChangeCallback mBatteryCallback;
    private final BatteryController mBatteryController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final float mBurmeseLineSpacing;
    private final NumberFormat mBurmeseNf;
    private final String mBurmeseNumerals;
    private final float mDefaultLineSpacing;
    /* access modifiers changed from: private */
    public float mDozeAmount;
    private final int mDozingColor = -1;
    /* access modifiers changed from: private */
    public boolean mIsCharging;
    /* access modifiers changed from: private */
    public boolean mIsDozing;
    boolean mKeyguardShowing;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private Locale mLocale;
    private final BroadcastReceiver mLocaleBroadcastReceiver;
    private int mLockScreenColor;
    private final StatusBarStateController mStatusBarStateController;
    private final StatusBarStateController.StateListener mStatusBarStatePersistentListener;

    /* access modifiers changed from: private */
    public void reset() {
    }

    public void animateAppear() {
    }

    public NTKeyguardClockViewLargeController(NTKeyguardClockViewLarge nTKeyguardClockViewLarge, StatusBarStateController statusBarStateController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        super(nTKeyguardClockViewLarge);
        NumberFormat instance = NumberFormat.getInstance(Locale.forLanguageTag("my"));
        this.mBurmeseNf = instance;
        this.mBatteryCallback = new BatteryController.BatteryStateChangeCallback() {
            public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
                if (NTKeyguardClockViewLargeController.this.mKeyguardShowing) {
                    boolean unused = NTKeyguardClockViewLargeController.this.mIsCharging;
                }
                boolean unused2 = NTKeyguardClockViewLargeController.this.mIsCharging = z2;
            }
        };
        this.mLocaleBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                NTKeyguardClockViewLargeController.this.updateLocale();
            }
        };
        this.mStatusBarStatePersistentListener = new StatusBarStateController.StateListener() {
            public void onDozeAmountChanged(float f, float f2) {
                if (!(NTKeyguardClockViewLargeController.this.mDozeAmount == 0.0f && f == 1.0f)) {
                    int i = (NTKeyguardClockViewLargeController.this.mDozeAmount > 1.0f ? 1 : (NTKeyguardClockViewLargeController.this.mDozeAmount == 1.0f ? 0 : -1));
                }
                boolean z = f > NTKeyguardClockViewLargeController.this.mDozeAmount;
                float unused = NTKeyguardClockViewLargeController.this.mDozeAmount = f;
                if (NTKeyguardClockViewLargeController.this.mIsDozing != z) {
                    boolean unused2 = NTKeyguardClockViewLargeController.this.mIsDozing = z;
                }
            }
        };
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                NTKeyguardClockViewLargeController.this.mKeyguardShowing = z;
                if (!NTKeyguardClockViewLargeController.this.mKeyguardShowing) {
                    NTKeyguardClockViewLargeController.this.reset();
                }
            }
        };
        this.mStatusBarStateController = statusBarStateController;
        this.mIsDozing = statusBarStateController.isDozing();
        this.mDozeAmount = statusBarStateController.getDozeAmount();
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBatteryController = batteryController;
        this.mBurmeseNumerals = instance.format(1234567890);
        this.mBurmeseLineSpacing = getContext().getResources().getFloat(C1894R.dimen.keyguard_clock_line_spacing_scale_burmese);
        this.mDefaultLineSpacing = getContext().getResources().getFloat(C1894R.dimen.keyguard_clock_line_spacing_scale);
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.mIsDozing = this.mStatusBarStateController.isDozing();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        updateLocale();
        this.mBroadcastDispatcher.registerReceiver(this.mLocaleBroadcastReceiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        this.mDozeAmount = this.mStatusBarStateController.getDozeAmount();
        this.mBatteryController.addCallback(this.mBatteryCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStatePersistentListener);
        this.mStatusBarStateController.addCallback(this.mStatusBarStatePersistentListener);
        refreshTime();
        initColors();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mLocaleBroadcastReceiver);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mBatteryController.removeCallback(this.mBatteryCallback);
        if (!((NTKeyguardClockViewLarge) this.mView).isAttachedToWindow()) {
            this.mStatusBarStateController.removeCallback(this.mStatusBarStatePersistentListener);
        }
    }

    public void refreshTime() {
        ((NTKeyguardClockViewLarge) this.mView).refreshTime();
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
        ((NTKeyguardClockViewLarge) this.mView).onTimeZoneChanged(timeZone);
    }

    public void refreshFormat() {
        ((NTKeyguardClockViewLarge) this.mView).refreshFormat();
    }

    /* access modifiers changed from: private */
    public void updateLocale() {
        Locale locale = Locale.getDefault();
        if (!Objects.equals(locale, this.mLocale)) {
            this.mLocale = locale;
            if (NumberFormat.getInstance(locale).format(1234567890).equals(this.mBurmeseNumerals)) {
                ((NTKeyguardClockViewLarge) this.mView).setLineSpacingScale(this.mBurmeseLineSpacing);
            } else {
                ((NTKeyguardClockViewLarge) this.mView).setLineSpacingScale(this.mDefaultLineSpacing);
            }
            ((NTKeyguardClockViewLarge) this.mView).refreshFormat();
        }
    }

    private void initColors() {
        this.mLockScreenColor = Utils.getColorAttrDefaultColor(getContext(), C1894R.attr.wallpaperTextColorAccent);
    }
}
