package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class DeviceCpuInfoController extends BasePreferenceController {
    public static final String KEY = "nt_hardware_processor";
    private static final String KEY_CPU = "ro.soc.model";
    private static final String TAG = "DeviceCpuInfoController";
    private final Fragment mFragment;
    private Preference mPreference;

    public int getAvailabilityStatus() {
        return 1;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DeviceCpuInfoController(Context context, Fragment fragment) {
        super(context, KEY);
        this.mFragment = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(KEY);
    }

    public void updateState(Preference preference) {
        if (this.mPreference != null) {
            String str = SystemProperties.get(KEY_CPU);
            this.mPreference.setSummary((CharSequence) this.mContext.getResources().getString(R$string.nt_processor_summary, new Object[]{str}));
        }
    }
}
