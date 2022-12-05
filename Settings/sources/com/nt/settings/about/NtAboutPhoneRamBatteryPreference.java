package com.nt.settings.about;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.utils.ThreadUtils;
import java.io.BufferedReader;
import java.io.FileReader;
/* loaded from: classes2.dex */
public class NtAboutPhoneRamBatteryPreference extends Preference {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private Context mContext;
    private TextView mLeftSummary;
    private TextView mLeftTitle;
    private RelativeLayout mRelativeLayoutLeft;
    private RelativeLayout mRelativeLayoutRight;
    private TextView mRightSummary;
    private TextView mRightTitle;

    public NtAboutPhoneRamBatteryPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public NtAboutPhoneRamBatteryPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NtAboutPhoneRamBatteryPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NtAboutPhoneRamBatteryPreference(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R.layout.nt_about_phone_two_columns_item);
        this.mContext = context;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        if (DEBUG) {
            Log.d("NtAboutPhoneRamBattery", "@_@ ------ onBindViewHolder");
        }
        RelativeLayout relativeLayout = (RelativeLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_two_left_cl);
        this.mRelativeLayoutLeft = relativeLayout;
        relativeLayout.setBackground(null);
        RelativeLayout relativeLayout2 = (RelativeLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_two_right_cl);
        this.mRelativeLayoutRight = relativeLayout2;
        relativeLayout2.setBackground(null);
        this.mLeftTitle = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_left_title);
        setLeftTitle(R.string.memory);
        this.mLeftSummary = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_left_summary);
        this.mRightTitle = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_right_title);
        setRightTitle(R.string.power_usage_summary_title);
        this.mRightSummary = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_hardware_right_summary);
        getRamAndUpdateUi();
    }

    private void setLeftTitle(int i) {
        TextView textView = this.mLeftTitle;
        if (textView != null) {
            textView.setText(i);
        }
    }

    private void setLeftSummary(String str) {
        TextView textView = this.mLeftSummary;
        if (textView != null) {
            textView.setText(str);
        }
    }

    private void setRightTitle(int i) {
        TextView textView = this.mRightTitle;
        if (textView != null) {
            textView.setText(i);
        }
    }

    private void setRightSummary(String str) {
        TextView textView = this.mRightSummary;
        if (textView != null) {
            textView.setText(str);
        }
    }

    private void getRamAndUpdateUi() {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.about.NtAboutPhoneRamBatteryPreference$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NtAboutPhoneRamBatteryPreference.this.lambda$getRamAndUpdateUi$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRamAndUpdateUi$1() {
        final String totalRam = getTotalRam();
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.about.NtAboutPhoneRamBatteryPreference$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                NtAboutPhoneRamBatteryPreference.this.lambda$getRamAndUpdateUi$0(totalRam);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateRamBattery */
    public void lambda$getRamAndUpdateUi$0(String str) {
        setLeftSummary(str);
        setRightSummary(getBatteryLevel() + "%");
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
