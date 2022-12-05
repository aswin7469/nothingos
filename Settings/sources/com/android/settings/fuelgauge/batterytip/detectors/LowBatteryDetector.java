package com.android.settings.fuelgauge.batterytip.detectors;

import android.content.Context;
import android.os.PowerManager;
import com.android.settings.fuelgauge.BatteryInfo;
import com.android.settings.fuelgauge.batterytip.BatteryTipPolicy;
import com.android.settings.fuelgauge.batterytip.tips.BatteryTip;
import com.android.settings.fuelgauge.batterytip.tips.LowBatteryTip;
/* loaded from: classes.dex */
public class LowBatteryDetector {
    private BatteryInfo mBatteryInfo;
    private BatteryTipPolicy mPolicy;
    private PowerManager mPowerManager;
    private int mWarningLevel;

    public LowBatteryDetector(Context context, BatteryTipPolicy batteryTipPolicy, BatteryInfo batteryInfo) {
        this.mPolicy = batteryTipPolicy;
        this.mBatteryInfo = batteryInfo;
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        this.mWarningLevel = context.getResources().getInteger(17694848);
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0029, code lost:
        if (r1 < java.util.concurrent.TimeUnit.HOURS.toMicros(r8.mPolicy.lowBatteryHour)) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public BatteryTip detect() {
        boolean z;
        BatteryTipPolicy batteryTipPolicy;
        boolean z2;
        boolean isPowerSaveMode = this.mPowerManager.isPowerSaveMode();
        BatteryInfo batteryInfo = this.mBatteryInfo;
        boolean z3 = true;
        int i = 0;
        if (batteryInfo.batteryLevel > this.mWarningLevel) {
            if (batteryInfo.discharging) {
                long j = batteryInfo.remainingTimeUs;
                if (j != 0) {
                }
            }
            z = false;
            batteryTipPolicy = this.mPolicy;
            z2 = !batteryTipPolicy.lowBatteryEnabled && !isPowerSaveMode;
            if (!batteryTipPolicy.testLowBatteryTip && (!this.mBatteryInfo.discharging || !z)) {
                z3 = false;
            }
            if (z2 || !z3) {
                i = 2;
            }
            return new LowBatteryTip(i, isPowerSaveMode);
        }
        z = true;
        batteryTipPolicy = this.mPolicy;
        if (!batteryTipPolicy.lowBatteryEnabled) {
        }
        if (!batteryTipPolicy.testLowBatteryTip) {
            z3 = false;
        }
        if (z2) {
        }
        i = 2;
        return new LowBatteryTip(i, isPowerSaveMode);
    }
}
