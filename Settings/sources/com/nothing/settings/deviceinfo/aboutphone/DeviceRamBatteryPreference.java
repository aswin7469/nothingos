package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settingslib.utils.ThreadUtils;
import java.io.BufferedReader;
import java.io.FileReader;

public class DeviceRamBatteryPreference extends Preference {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private TextView mBatterySummary;
    private TextView mBatteryTitle;
    private Context mContext;
    private TextView mRamSummary;
    private TextView mRamTitle;

    public DeviceRamBatteryPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public DeviceRamBatteryPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DeviceRamBatteryPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DeviceRamBatteryPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    private void init(Context context) {
        setLayoutResource(R$layout.about_phone_two_columns_layout);
        this.mContext = context;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((RelativeLayout) preferenceViewHolder.findViewById(R$id.about_info_two_left_cl)).setBackground((Drawable) null);
        ((RelativeLayout) preferenceViewHolder.findViewById(R$id.about_info_two_right_cl)).setBackground((Drawable) null);
        this.mRamTitle = (TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_left_title);
        setRamItemTitle(R$string.memory);
        this.mRamSummary = (TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_left_summary);
        this.mBatteryTitle = (TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_right_title);
        setBatteryTitle(R$string.power_usage_summary_title);
        this.mBatterySummary = (TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_right_summary);
        getRamAndUpdateUi();
    }

    private void setRamItemTitle(int i) {
        TextView textView = this.mRamTitle;
        if (textView != null) {
            textView.setText(i);
        }
    }

    private void setRamItemSummary(String str) {
        TextView textView = this.mRamSummary;
        if (textView != null) {
            textView.setText(str);
        }
    }

    private void setBatteryTitle(int i) {
        TextView textView = this.mBatteryTitle;
        if (textView != null) {
            textView.setText(i);
        }
    }

    private void setBatterySummary(String str) {
        TextView textView = this.mBatterySummary;
        if (textView != null) {
            textView.setText(str);
        }
    }

    private void getRamAndUpdateUi() {
        ThreadUtils.postOnBackgroundThread((Runnable) new DeviceRamBatteryPreference$$ExternalSyntheticLambda0(this));
    }

    public void getTotalRamInfo() {
        ThreadUtils.postOnMainThread(new DeviceRamBatteryPreference$$ExternalSyntheticLambda1(this, getTotalRam()));
    }

    /* renamed from: updateRamInfoToUI */
    public void lambda$getTotalRamInfo$0(String str) {
        setRamItemSummary(str);
        setBatterySummary(getBatteryLevel() + "%");
    }

    private String getTotalRam() {
        String str = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 4096);
            str = bufferedReader.readLine().split("\\s+")[1];
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int ceil = str != null ? (int) Math.ceil(new Float(Float.valueOf(str).floatValue() / 1048576.0f).doubleValue()) : 0;
        return ceil + "GB";
    }

    private int getBatteryLevel() {
        BatteryManager batteryManager = (BatteryManager) this.mContext.getSystemService("batterymanager");
        if (batteryManager != null) {
            return batteryManager.getIntProperty(4);
        }
        return 0;
    }
}
