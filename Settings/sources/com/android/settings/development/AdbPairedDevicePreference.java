package com.android.settings.development;

import android.content.Context;
import android.debug.PairDevice;
import android.os.Bundle;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;

public class AdbPairedDevicePreference extends Preference {
    private PairDevice mPairedDevice;

    public AdbPairedDevicePreference(PairDevice pairDevice, Context context) {
        super(context);
        this.mPairedDevice = pairDevice;
        setWidgetLayoutResource(getWidgetLayoutResourceId());
        refresh();
    }

    /* access modifiers changed from: protected */
    public int getWidgetLayoutResourceId() {
        return R$layout.preference_widget_gear_optional_background;
    }

    public void refresh() {
        setTitle(this, this.mPairedDevice);
    }

    public void setPairedDevice(PairDevice pairDevice) {
        this.mPairedDevice = pairDevice;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R$id.settings_button);
        View findViewById2 = preferenceViewHolder.findViewById(R$id.settings_button_no_background);
        findViewById.setVisibility(4);
        findViewById2.setVisibility(0);
    }

    static void setTitle(AdbPairedDevicePreference adbPairedDevicePreference, PairDevice pairDevice) {
        adbPairedDevicePreference.setTitle((CharSequence) pairDevice.name);
        adbPairedDevicePreference.setSummary(pairDevice.connected ? adbPairedDevicePreference.getContext().getText(R$string.adb_wireless_device_connected_summary) : "");
    }

    public void savePairedDeviceToExtras(Bundle bundle) {
        bundle.putParcelable("paired_device", this.mPairedDevice);
    }
}
