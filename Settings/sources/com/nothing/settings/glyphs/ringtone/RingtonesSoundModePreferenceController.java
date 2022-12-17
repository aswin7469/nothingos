package com.nothing.settings.glyphs.ringtone;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.BasePreferenceController;

public class RingtonesSoundModePreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private OnSoundModeChangeListener mListener;
    private SwitchPreference mPreference;
    private int mRingtoneType;
    private int mSoundOnly;

    public interface OnSoundModeChangeListener {
        void onSoundModeChange(boolean z);
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public RingtonesSoundModePreferenceController(Context context, String str) {
        super(context, str);
        this.mRingtoneType = 1;
    }

    public RingtonesSoundModePreferenceController(Context context, String str, OnSoundModeChangeListener onSoundModeChangeListener) {
        super(context, str);
        this.mRingtoneType = 1;
        this.mListener = onSoundModeChangeListener;
    }

    public RingtonesSoundModePreferenceController(Context context, String str, int i, int i2, OnSoundModeChangeListener onSoundModeChangeListener) {
        super(context, str);
        this.mListener = onSoundModeChangeListener;
        this.mRingtoneType = i;
        this.mSoundOnly = i2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SwitchPreference switchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = switchPreference;
        init(switchPreference);
    }

    private void init(SwitchPreference switchPreference) {
        boolean z = true;
        if (this.mSoundOnly != 1) {
            z = false;
        }
        switchPreference.setChecked(z);
        OnSoundModeChangeListener onSoundModeChangeListener = this.mListener;
        if (onSoundModeChangeListener != null) {
            onSoundModeChangeListener.onSoundModeChange(switchPreference.isChecked());
        }
        switchPreference.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        OnSoundModeChangeListener onSoundModeChangeListener = this.mListener;
        if (onSoundModeChangeListener != null) {
            onSoundModeChangeListener.onSoundModeChange(((Boolean) obj).booleanValue());
        }
        this.mPreference.setChecked(((Boolean) obj).booleanValue());
        return false;
    }
}
