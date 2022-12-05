package com.nothingos.keyguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.NumberFormat;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
/* loaded from: classes2.dex */
public class NothingKeyguardClockViewLargeController extends ViewController<NothingKeyguardClockViewLarge> {
    private final BatteryController mBatteryController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final NumberFormat mBurmeseNf;
    private final String mBurmeseNumerals;
    private final KeyguardBypassController mBypassController;
    private float mDozeAmount;
    private boolean mIsCharging;
    private boolean mIsDozing;
    boolean mKeyguardShowing;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private Locale mLocale;
    private int mLockScreenColor;
    private final StatusBarStateController mStatusBarStateController;
    private final int mDozingColor = -1;
    private final BatteryController.BatteryStateChangeCallback mBatteryCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.nothingos.keyguard.NothingKeyguardClockViewLargeController.1
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            NothingKeyguardClockViewLargeController nothingKeyguardClockViewLargeController = NothingKeyguardClockViewLargeController.this;
            if (nothingKeyguardClockViewLargeController.mKeyguardShowing) {
                boolean unused = nothingKeyguardClockViewLargeController.mIsCharging;
            }
            NothingKeyguardClockViewLargeController.this.mIsCharging = z2;
        }
    };
    private final BroadcastReceiver mLocaleBroadcastReceiver = new BroadcastReceiver() { // from class: com.nothingos.keyguard.NothingKeyguardClockViewLargeController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NothingKeyguardClockViewLargeController.this.updateLocale();
        }
    };
    private final StatusBarStateController.StateListener mStatusBarStatePersistentListener = new StatusBarStateController.StateListener() { // from class: com.nothingos.keyguard.NothingKeyguardClockViewLargeController.3
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozeAmountChanged(float f, float f2) {
            if (NothingKeyguardClockViewLargeController.this.mDozeAmount != 0.0f || f != 1.0f) {
                int i = (NothingKeyguardClockViewLargeController.this.mDozeAmount > 1.0f ? 1 : (NothingKeyguardClockViewLargeController.this.mDozeAmount == 1.0f ? 0 : -1));
            }
            boolean z = f > NothingKeyguardClockViewLargeController.this.mDozeAmount;
            NothingKeyguardClockViewLargeController.this.mDozeAmount = f;
            if (NothingKeyguardClockViewLargeController.this.mIsDozing != z) {
                NothingKeyguardClockViewLargeController.this.mIsDozing = z;
            }
        }
    };
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.nothingos.keyguard.NothingKeyguardClockViewLargeController.4
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChanged(boolean z) {
            NothingKeyguardClockViewLargeController nothingKeyguardClockViewLargeController = NothingKeyguardClockViewLargeController.this;
            nothingKeyguardClockViewLargeController.mKeyguardShowing = z;
            if (!z) {
                nothingKeyguardClockViewLargeController.reset();
            }
        }
    };
    private final float mBurmeseLineSpacing = getContext().getResources().getFloat(R$dimen.keyguard_clock_line_spacing_scale_burmese);
    private final float mDefaultLineSpacing = getContext().getResources().getFloat(R$dimen.keyguard_clock_line_spacing_scale);

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
    }

    public NothingKeyguardClockViewLargeController(NothingKeyguardClockViewLarge nothingKeyguardClockViewLarge, StatusBarStateController statusBarStateController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardBypassController keyguardBypassController) {
        super(nothingKeyguardClockViewLarge);
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.forLanguageTag("my"));
        this.mBurmeseNf = numberFormat;
        this.mStatusBarStateController = statusBarStateController;
        this.mIsDozing = statusBarStateController.isDozing();
        this.mDozeAmount = statusBarStateController.getDozeAmount();
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBypassController = keyguardBypassController;
        this.mBatteryController = batteryController;
        this.mBurmeseNumerals = numberFormat.format(1234567890L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mIsDozing = this.mStatusBarStateController.isDozing();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
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

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mLocaleBroadcastReceiver);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mBatteryController.removeCallback(this.mBatteryCallback);
        if (!((NothingKeyguardClockViewLarge) this.mView).isAttachedToWindow()) {
            this.mStatusBarStateController.removeCallback(this.mStatusBarStatePersistentListener);
        }
    }

    public void refreshTime() {
        ((NothingKeyguardClockViewLarge) this.mView).refreshTime();
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
        ((NothingKeyguardClockViewLarge) this.mView).onTimeZoneChanged(timeZone);
    }

    public void refreshFormat() {
        ((NothingKeyguardClockViewLarge) this.mView).refreshFormat();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLocale() {
        Locale locale = Locale.getDefault();
        if (!Objects.equals(locale, this.mLocale)) {
            this.mLocale = locale;
            if (NumberFormat.getInstance(locale).format(1234567890L).equals(this.mBurmeseNumerals)) {
                ((NothingKeyguardClockViewLarge) this.mView).setLineSpacingScale(this.mBurmeseLineSpacing);
            } else {
                ((NothingKeyguardClockViewLarge) this.mView).setLineSpacingScale(this.mDefaultLineSpacing);
            }
            ((NothingKeyguardClockViewLarge) this.mView).refreshFormat();
        }
    }

    private void initColors() {
        this.mLockScreenColor = Utils.getColorAttrDefaultColor(getContext(), R$attr.wallpaperTextColorAccent);
    }
}
