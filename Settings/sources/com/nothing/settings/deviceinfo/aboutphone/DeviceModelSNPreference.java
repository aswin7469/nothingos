package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;

public class DeviceModelSNPreference extends Preference {
    public DeviceModelSNPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    public DeviceModelSNPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DeviceModelSNPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DeviceModelSNPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    private void init() {
        setLayoutResource(R$layout.about_phone_two_columns_layout);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((RelativeLayout) preferenceViewHolder.findViewById(R$id.about_info_two_left_cl)).setBackground((Drawable) null);
        ((RelativeLayout) preferenceViewHolder.findViewById(R$id.about_info_two_right_cl)).setBackground((Drawable) null);
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_left_title)).setText(R$string.nt_model_number);
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_left_summary)).setText(Build.MODEL);
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_right_title)).setText(R$string.status_serial_number);
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_hardware_right_summary)).setText(Build.getSerial());
    }
}
