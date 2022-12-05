package com.nt.settings.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.widget.RadioButtonPreference;
import com.nt.settings.utils.NtUtils;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class NtScreenRefreshRateFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_screen_refresh_rate);
    private Context mContext;
    private DeviceConfigDisplaySettings mDeviceConfigDisplaySettings;
    private Handler mHandler;
    private RadioButtonPreference mHighPreference;
    private IDeviceConfigChange mOnDeviceConfigChange;
    private float mPeakRefreshRate;
    private RadioButtonPreference mStandardPreference;

    /* loaded from: classes2.dex */
    private interface IDeviceConfigChange {
        void onDefaultRefreshRateChanged();
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_screen_refresh_rate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return NtScreenRefreshRateFragment.class.getName();
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mHandler = new Handler(this.mContext.getMainLooper());
        this.mDeviceConfigDisplaySettings = new DeviceConfigDisplaySettings();
        this.mOnDeviceConfigChange = new IDeviceConfigChange() { // from class: com.nt.settings.display.NtScreenRefreshRateFragment.1
            @Override // com.nt.settings.display.NtScreenRefreshRateFragment.IDeviceConfigChange
            public void onDefaultRefreshRateChanged() {
                NtScreenRefreshRateFragment.this.updatePrefStatus();
            }
        };
        this.mPeakRefreshRate = Settings.System.getFloat(this.mContext.getContentResolver(), "peak_refresh_rate", getConfigDefaultPeakRefreshRate());
        Log.d("NtScreenRefreshRateFrag", "@_@ ------ mPeakRefreshRate : " + this.mPeakRefreshRate);
        initPreference();
        updatePrefStatus();
    }

    private float getPeakRefreshRate() {
        float findPeakRefreshRate;
        Display display = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(0);
        if (display == null) {
            Log.w("NtScreenRefreshRateFrag", "No valid default display device");
            findPeakRefreshRate = NtUtils.DEFAULT_REFRESH_RATE;
        } else {
            findPeakRefreshRate = findPeakRefreshRate(display.getSupportedModes());
        }
        Log.d("NtScreenRefreshRateFrag", "@_@ ------ peakRefreshRate = " + findPeakRefreshRate);
        return findPeakRefreshRate;
    }

    private void initPreference() {
        this.mHighPreference = (RadioButtonPreference) findPreference("nt_screen_refresh_rate_high");
        this.mStandardPreference = (RadioButtonPreference) findPreference("nt_screen_refresh_rate_standard");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePrefStatus() {
        if (Math.round(this.mPeakRefreshRate) > Math.round(NtUtils.DEFAULT_REFRESH_RATE)) {
            this.mHighPreference.setChecked(true);
            this.mStandardPreference.setChecked(false);
            return;
        }
        this.mHighPreference.setChecked(false);
        this.mStandardPreference.setChecked(true);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    public boolean onPreferenceTreeClick(Preference preference) {
        Log.d("NtScreenRefreshRateFrag", "@_@ ------ onPreferenceTreeClick " + preference.getKey());
        if ("nt_screen_refresh_rate_high".equals(preference.getKey())) {
            this.mHighPreference.setChecked(true);
            this.mStandardPreference.setChecked(false);
            this.mPeakRefreshRate = getPeakRefreshRate();
        } else if ("nt_screen_refresh_rate_standard".equals(preference.getKey())) {
            this.mHighPreference.setChecked(false);
            this.mStandardPreference.setChecked(true);
            this.mPeakRefreshRate = NtUtils.DEFAULT_REFRESH_RATE;
        }
        Log.d("NtScreenRefreshRateFrag", "@_@ ------ setChecked to : " + this.mPeakRefreshRate);
        Settings.System.putFloat(this.mContext.getContentResolver(), "peak_refresh_rate", this.mPeakRefreshRate);
        return super.onPreferenceTreeClick(preference);
    }

    private float findPeakRefreshRate(Display.Mode[] modeArr) {
        float f = NtUtils.DEFAULT_REFRESH_RATE;
        for (Display.Mode mode : modeArr) {
            if (Math.round(mode.getRefreshRate()) > f) {
                f = mode.getRefreshRate();
            }
        }
        return f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DeviceConfigDisplaySettings implements DeviceConfig.OnPropertiesChangedListener, Executor {
        private DeviceConfigDisplaySettings() {
        }

        public float getDefaultPeakRefreshRate() {
            float f = DeviceConfig.getFloat("display_manager", "peak_refresh_rate_default", -1.0f);
            Log.d("NtScreenRefreshRateFrag", "@_@ ------ DeviceConfig getDefaultPeakRefreshRate : " + f);
            return f;
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (NtScreenRefreshRateFragment.this.mOnDeviceConfigChange != null) {
                NtScreenRefreshRateFragment.this.mOnDeviceConfigChange.onDefaultRefreshRateChanged();
                NtScreenRefreshRateFragment.this.updatePrefStatus();
            }
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            if (NtScreenRefreshRateFragment.this.mHandler != null) {
                NtScreenRefreshRateFragment.this.mHandler.post(runnable);
            }
        }
    }

    private float getConfigDefaultPeakRefreshRate() {
        float defaultPeakRefreshRate = this.mDeviceConfigDisplaySettings.getDefaultPeakRefreshRate();
        if (defaultPeakRefreshRate == -1.0f) {
            defaultPeakRefreshRate = this.mContext.getResources().getInteger(17694788);
        }
        Log.d("NtScreenRefreshRateFrag", "@_@ ------ getConfigDefaultPeakRefreshRate : " + defaultPeakRefreshRate);
        return defaultPeakRefreshRate;
    }
}
