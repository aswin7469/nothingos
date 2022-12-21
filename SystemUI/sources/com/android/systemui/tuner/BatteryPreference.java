package com.android.systemui.tuner;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import androidx.preference.DropDownPreference;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;

public class BatteryPreference extends DropDownPreference implements TunerService.Tunable {
    private static final String DEFAULT = "default";
    private static final String DISABLED = "disabled";
    private static final String PERCENT = "percent";
    private final String mBattery;
    private boolean mBatteryEnabled;
    private boolean mHasPercentage;
    private boolean mHasSetValue;
    private ArraySet<String> mHideList;

    public BatteryPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBattery = context.getString(17041555);
        setEntryValues(new CharSequence[]{PERCENT, DEFAULT, DISABLED});
    }

    public void onAttached() {
        super.onAttached();
        boolean z = false;
        if (Settings.System.getInt(getContext().getContentResolver(), "status_bar_show_battery_percent", 0) != 0) {
            z = true;
        }
        this.mHasPercentage = z;
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, StatusBarIconController.ICON_HIDE_LIST);
    }

    public void onDetached() {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
        super.onDetached();
    }

    public void onTuningChanged(String str, String str2) {
        if (StatusBarIconController.ICON_HIDE_LIST.equals(str)) {
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(getContext(), str2);
            this.mHideList = iconHideList;
            this.mBatteryEnabled = !iconHideList.contains(this.mBattery);
        }
        if (!this.mHasSetValue) {
            this.mHasSetValue = true;
            boolean z = this.mBatteryEnabled;
            if (z && this.mHasPercentage) {
                setValue(PERCENT);
            } else if (z) {
                setValue(DEFAULT);
            } else {
                setValue(DISABLED);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean persistString(String str) {
        boolean equals = PERCENT.equals(str);
        MetricsLogger.action(getContext(), 237, equals);
        Settings.System.putInt(getContext().getContentResolver(), "status_bar_show_battery_percent", equals ? 1 : 0);
        if (DISABLED.equals(str)) {
            this.mHideList.add(this.mBattery);
        } else {
            this.mHideList.remove(this.mBattery);
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue(StatusBarIconController.ICON_HIDE_LIST, TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, this.mHideList));
        return true;
    }
}
