package com.nothing.systemui.battery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.C1893R;
import com.android.systemui.battery.BatteryMeterView;
import com.nothing.systemui.statusbar.policy.BatteryStateEx;
import com.nothing.systemui.util.NTLogUtil;

public class BatteryMeterViewEx {
    private static final String TAG = "BatteryMeterViewEx";
    private static final int TEMP_MAX = 530;
    private static final int TEMP_MIN = 0;
    private static final int VOLT_MAX = 4480;
    private static BatteryStateEx sBatteryStateEx = new BatteryStateEx();

    public static void addBatteryImageView(Context context, BatteryMeterView batteryMeterView, ImageView imageView) {
        FrameLayout frameLayout = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(context.getResources().getDimensionPixelSize(C1893R.dimen.status_bar_battery_icon_width), context.getResources().getDimensionPixelSize(C1893R.dimen.status_bar_battery_icon_height));
        layoutParams.gravity = 17;
        frameLayout.addView(imageView, layoutParams);
        batteryMeterView.addView(frameLayout, new ViewGroup.MarginLayoutParams(context.getResources().getDimensionPixelSize(C1893R.dimen.battery_icon_container_size), -1));
    }

    public static void refreshByBatteryStateEx(Context context, ImageView imageView, Drawable drawable, boolean z) {
        if (z) {
            NTLogUtil.m1682i("BatteryMeterView", "refreshByBatteryStateEx: batteryStateUnknown");
            return;
        }
        if (sBatteryStateEx.status == 4) {
            if (sBatteryStateEx.voltage > VOLT_MAX) {
                imageView.setImageDrawable(context.getDrawable(C1893R.C1895drawable.ic_battery_alert));
                NTLogUtil.m1682i("BatteryMeterView", "refreshByBatteryStateEx: volt > max");
                return;
            } else if (sBatteryStateEx.temperature >= TEMP_MAX) {
                imageView.setImageDrawable(context.getDrawable(C1893R.C1895drawable.ic_battery_pause));
                NTLogUtil.m1682i("BatteryMeterView", "refreshByBatteryStateEx: temp >= max");
                return;
            } else if (sBatteryStateEx.temperature <= 0) {
                imageView.setImageDrawable(context.getDrawable(C1893R.C1895drawable.ic_battery_pause));
                NTLogUtil.m1682i("BatteryMeterView", "refreshByBatteryStateEx: temp <= min");
                return;
            }
        }
        imageView.setImageDrawable(drawable);
    }

    public static void updateBatteryStateEx(BatteryStateEx batteryStateEx) {
        boolean z = true;
        boolean z2 = sBatteryStateEx.status != batteryStateEx.status;
        boolean z3 = sBatteryStateEx.temperature != batteryStateEx.temperature;
        if (sBatteryStateEx.voltage == batteryStateEx.voltage) {
            z = false;
        }
        if (z2 || z3 || z) {
            NTLogUtil.m1682i(TAG, "batteryStateExt changed from: " + batteryStateEx + " to " + sBatteryStateEx);
        }
        sBatteryStateEx.copy(batteryStateEx);
    }

    public static BatteryStateEx getBatteryStateEx() {
        return sBatteryStateEx;
    }
}
