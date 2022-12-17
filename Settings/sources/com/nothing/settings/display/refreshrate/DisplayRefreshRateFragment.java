package com.nothing.settings.display.refreshrate;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import androidx.preference.Preference;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.widget.RadioButtonPreference;
import com.nothing.experience.AppTracking;
import java.util.concurrent.Executor;

public class DisplayRefreshRateFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nt_screen_refresh_rate);
    private Context mContext;
    private DeviceConfigDisplaySettings mDeviceConfigDisplaySettings;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private RadioButtonPreference mHighPreference;
    /* access modifiers changed from: private */
    public IDeviceConfigChange mOnDeviceConfigChange;
    private float mPeakRefreshRate;
    private RadioButtonPreference mStandardPreference;

    private interface IDeviceConfigChange {
        void onDefaultRefreshRateChanged();
    }

    public String getLogTag() {
        return "DisplayRefreshRateFragment";
    }

    public int getMetricsCategory() {
        return 0;
    }

    public int getPreferenceScreenResId() {
        return R$xml.nt_screen_refresh_rate;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mHandler = new Handler(this.mContext.getMainLooper());
        this.mDeviceConfigDisplaySettings = new DeviceConfigDisplaySettings();
        this.mOnDeviceConfigChange = new IDeviceConfigChange() {
            public void onDefaultRefreshRateChanged() {
                DisplayRefreshRateFragment.this.updatePrefStatus();
            }
        };
        this.mPeakRefreshRate = Settings.System.getFloat(this.mContext.getContentResolver(), "peak_refresh_rate", getConfigDefaultPeakRefreshRate());
        Log.d("DisplayRefreshRateFragment", "mPeakRefreshRate : " + this.mPeakRefreshRate);
        initPreference();
        updatePrefStatus();
    }

    private float getPeakRefreshRate() {
        float f;
        Display display = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(0);
        if (display == null) {
            Log.w("DisplayRefreshRateFragment", "No valid default display device");
            f = 60.0f;
        } else {
            f = findPeakRefreshRate(display.getSupportedModes());
        }
        Log.d("DisplayRefreshRateFragment", "peakRefreshRate = " + f);
        return f;
    }

    private void initPreference() {
        this.mHighPreference = (RadioButtonPreference) findPreference("nt_screen_refresh_rate_high");
        this.mStandardPreference = (RadioButtonPreference) findPreference("nt_screen_refresh_rate_standard");
    }

    public void updatePrefStatus() {
        if (Math.round(this.mPeakRefreshRate) > Math.round(60.0f)) {
            this.mHighPreference.setChecked(true);
            this.mStandardPreference.setChecked(false);
            return;
        }
        this.mHighPreference.setChecked(false);
        this.mStandardPreference.setChecked(true);
    }

    private void logEvent(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("high_refresh", z ? 1 : 0);
        Log.d("event", "high refresh:" + z);
        AppTracking.getInstance(this.mContext).logProductEvent("Display_Event", bundle);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        Log.d("DisplayRefreshRateFragment", "onPreferenceTreeClick " + preference.getKey());
        if ("nt_screen_refresh_rate_high".equals(preference.getKey())) {
            this.mHighPreference.setChecked(true);
            this.mStandardPreference.setChecked(false);
            this.mPeakRefreshRate = getPeakRefreshRate();
            logEvent(true);
        } else if ("nt_screen_refresh_rate_standard".equals(preference.getKey())) {
            this.mHighPreference.setChecked(false);
            this.mStandardPreference.setChecked(true);
            this.mPeakRefreshRate = 60.0f;
            logEvent(false);
        }
        Log.d("DisplayRefreshRateFragment", "setChecked to: " + this.mPeakRefreshRate);
        Settings.System.putFloat(this.mContext.getContentResolver(), "peak_refresh_rate", this.mPeakRefreshRate);
        return super.onPreferenceTreeClick(preference);
    }

    private float findPeakRefreshRate(Display.Mode[] modeArr) {
        float f = 60.0f;
        for (Display.Mode mode : modeArr) {
            if (((float) Math.round(mode.getRefreshRate())) > f) {
                f = mode.getRefreshRate();
            }
        }
        return f;
    }

    public class DeviceConfigDisplaySettings implements DeviceConfig.OnPropertiesChangedListener, Executor {
        private DeviceConfigDisplaySettings() {
        }

        public float getDefaultPeakRefreshRate() {
            float f = DeviceConfig.getFloat("display_manager", "peak_refresh_rate_default", -1.0f);
            Log.d("DisplayRefreshRateFragment", "DeviceConfig getDefaultPeakRefreshRate : " + f);
            return f;
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (DisplayRefreshRateFragment.this.mOnDeviceConfigChange != null) {
                DisplayRefreshRateFragment.this.mOnDeviceConfigChange.onDefaultRefreshRateChanged();
                DisplayRefreshRateFragment.this.updatePrefStatus();
            }
        }

        public void execute(Runnable runnable) {
            if (DisplayRefreshRateFragment.this.mHandler != null) {
                DisplayRefreshRateFragment.this.mHandler.post(runnable);
            }
        }
    }

    private float getConfigDefaultPeakRefreshRate() {
        float defaultPeakRefreshRate = this.mDeviceConfigDisplaySettings.getDefaultPeakRefreshRate();
        if (defaultPeakRefreshRate == -1.0f) {
            defaultPeakRefreshRate = (float) this.mContext.getResources().getInteger(17694794);
        }
        Log.d("DisplayRefreshRateFragment", "defaultPeakRefreshRate:" + defaultPeakRefreshRate);
        return defaultPeakRefreshRate;
    }
}
