package com.nothing.settings.glyphs.ringtone;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.nothing.settings.glyphs.preference.RingtoneSelectorPreference;

public class RingtoneSelectorPreferenceController extends BasePreferenceController implements LifecycleObserver, OnResume, OnPause, OnDestroy, OnStop {
    protected static final String KEY_RINGTONE_SELECTOR = "key_glyphs_ringtone_selector";
    protected RingtoneSelectorPreference mPreference;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY_RINGTONE_SELECTOR;
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

    public void onDestroy() {
    }

    public void onResume() {
    }

    public void onStop() {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RingtoneSelectorPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RingtoneSelectorPreference) preferenceScreen.findPreference(KEY_RINGTONE_SELECTOR);
    }

    public void onPause() {
        RingtoneSelectorPreference ringtoneSelectorPreference = this.mPreference;
        if (ringtoneSelectorPreference != null) {
            ringtoneSelectorPreference.onPause();
        }
    }
}
