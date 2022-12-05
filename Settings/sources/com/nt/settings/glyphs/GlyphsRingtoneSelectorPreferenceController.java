package com.nt.settings.glyphs;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference;
/* loaded from: classes2.dex */
public class GlyphsRingtoneSelectorPreferenceController extends BasePreferenceController implements LifecycleObserver, OnResume, OnPause, OnDestroy, OnStop {
    protected static final String KEY_RINGTONE_SELECTOR = "key_glyphs_ringtone_selector";
    protected GlyphsRingtoneSelectorPreference mPreference;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY_RINGTONE_SELECTOR;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnDestroy
    public void onDestroy() {
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsRingtoneSelectorPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (GlyphsRingtoneSelectorPreference) preferenceScreen.findPreference(KEY_RINGTONE_SELECTOR);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference = this.mPreference;
        if (glyphsRingtoneSelectorPreference != null) {
            glyphsRingtoneSelectorPreference.onPause();
        }
    }
}
