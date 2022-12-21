package com.nothing.systemui.statusbar.policy;

import android.content.Intent;
import com.nothing.systemui.util.NTLogUtil;

public class BatteryControllerImplEx {
    private static final String TAG = "BatteryControllerImplEx";
    private final BatteryStateEx mBatteryStateEx = new BatteryStateEx();

    public void updateBatteryStateEx(Intent intent) {
        int intExtra = intent.getIntExtra("status", 1);
        int intExtra2 = intent.getIntExtra("temperature", -1);
        int intExtra3 = intent.getIntExtra("voltage", -1);
        this.mBatteryStateEx.status = intExtra;
        this.mBatteryStateEx.temperature = intExtra2;
        this.mBatteryStateEx.voltage = intExtra3;
        NTLogUtil.m1682i(TAG, "updateBatteryStateEx: " + this.mBatteryStateEx);
    }

    public BatteryStateEx getBatteryStateEx() {
        return this.mBatteryStateEx;
    }
}
