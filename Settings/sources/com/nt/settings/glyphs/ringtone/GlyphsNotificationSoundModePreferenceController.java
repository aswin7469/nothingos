package com.nt.settings.glyphs.ringtone;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes2.dex */
public class GlyphsNotificationSoundModePreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private OnSoundModeChangeListener mListener;
    private SwitchPreference mPreference;

    /* loaded from: classes2.dex */
    public interface OnSoundModeChangeListener {
        void onSoundModeChange(boolean z);
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsNotificationSoundModePreferenceController(Context context, String str) {
        super(context, str);
    }

    public GlyphsNotificationSoundModePreferenceController(Context context, String str, OnSoundModeChangeListener onSoundModeChangeListener) {
        super(context, str);
        this.mListener = onSoundModeChangeListener;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SwitchPreference switchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = switchPreference;
        init(switchPreference);
    }

    private void init(SwitchPreference switchPreference) {
        switchPreference.setOnPreferenceChangeListener(this);
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        OnSoundModeChangeListener onSoundModeChangeListener = this.mListener;
        if (onSoundModeChangeListener != null) {
            onSoundModeChangeListener.onSoundModeChange(((Boolean) obj).booleanValue());
        }
        this.mPreference.setChecked(((Boolean) obj).booleanValue());
        return false;
    }
}
