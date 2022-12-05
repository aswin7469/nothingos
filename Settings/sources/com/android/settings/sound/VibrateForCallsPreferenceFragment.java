package com.android.settings.sound;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class VibrateForCallsPreferenceFragment extends RadioButtonPickerFragment {
    static final String KEY_ALWAYS_VIBRATE = "always_vibrate";
    static final String KEY_NEVER_VIBRATE = "never_vibrate";
    static final String KEY_RAMPING_RINGER = "ramping_ringer";
    private final Map<String, VibrateForCallsCandidateInfo> mCandidates = new ArrayMap();
    private int mDefaultRingVibrationIntensity;

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1827;
    }

    @Override // com.android.settings.widget.RadioButtonPickerFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mDefaultRingVibrationIntensity = ((Vibrator) context.getSystemService(Vibrator.class)).getDefaultRingVibrationIntensity();
        loadCandidates(context);
    }

    private void loadCandidates(Context context) {
        this.mCandidates.put(KEY_NEVER_VIBRATE, new VibrateForCallsCandidateInfo(KEY_NEVER_VIBRATE, R.string.vibrate_when_ringing_option_never_vibrate));
        this.mCandidates.put(KEY_ALWAYS_VIBRATE, new VibrateForCallsCandidateInfo(KEY_ALWAYS_VIBRATE, R.string.vibrate_when_ringing_option_always_vibrate));
    }

    private void updateSettings(VibrateForCallsCandidateInfo vibrateForCallsCandidateInfo) {
        String key = vibrateForCallsCandidateInfo.getKey();
        if (TextUtils.equals(key, KEY_ALWAYS_VIBRATE)) {
            Settings.System.putInt(getContext().getContentResolver(), "vibrate_when_ringing", 1);
            Settings.Global.putInt(getContext().getContentResolver(), "apply_ramping_ringer", 0);
            Settings.System.putInt(getContext().getContentResolver(), "ring_vibration_intensity", this.mDefaultRingVibrationIntensity);
            Log.d("VibrateForCallsPreferenceFragment", "updateSettings: set RING_VIBRATION_INTENSITY: " + this.mDefaultRingVibrationIntensity);
        } else if (TextUtils.equals(key, KEY_RAMPING_RINGER)) {
            Settings.System.putInt(getContext().getContentResolver(), "vibrate_when_ringing", 0);
            Settings.Global.putInt(getContext().getContentResolver(), "apply_ramping_ringer", 1);
            Settings.System.putInt(getContext().getContentResolver(), "ring_vibration_intensity", 0);
        } else if (TextUtils.equals(key, "sync_with_ringtone_vibrate")) {
            Settings.System.putInt(getContext().getContentResolver(), "vibrate_when_ringing", 0);
            Settings.Global.putInt(getContext().getContentResolver(), "apply_ramping_ringer", 0);
            Settings.System.putInt(getContext().getContentResolver(), "ring_vibration_intensity", this.mDefaultRingVibrationIntensity);
        } else {
            Settings.System.putInt(getContext().getContentResolver(), "vibrate_when_ringing", 0);
            Settings.Global.putInt(getContext().getContentResolver(), "apply_ramping_ringer", 0);
            Settings.System.putInt(getContext().getContentResolver(), "ring_vibration_intensity", 0);
        }
    }

    @Override // com.android.settings.widget.RadioButtonPickerFragment
    protected List<? extends CandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mCandidates.get(KEY_NEVER_VIBRATE));
        arrayList.add(this.mCandidates.get(KEY_ALWAYS_VIBRATE));
        return arrayList;
    }

    @Override // com.android.settings.widget.RadioButtonPickerFragment
    protected String getDefaultKey() {
        return Settings.Global.getInt(getContext().getContentResolver(), "apply_ramping_ringer", 0) == 1 ? KEY_RAMPING_RINGER : Settings.System.getInt(getContext().getContentResolver(), "vibrate_when_ringing", 0) == 0 ? KEY_NEVER_VIBRATE : KEY_ALWAYS_VIBRATE;
    }

    @Override // com.android.settings.widget.RadioButtonPickerFragment
    protected boolean setDefaultKey(String str) {
        VibrateForCallsCandidateInfo vibrateForCallsCandidateInfo = this.mCandidates.get(str);
        if (vibrateForCallsCandidateInfo == null) {
            Log.e("VibrateForCallsPreferenceFragment", "Unknown vibrate for calls candidate (key = " + str + ")!");
            return false;
        }
        updateSettings(vibrateForCallsCandidateInfo);
        return true;
    }

    @Override // com.android.settings.widget.RadioButtonPickerFragment, com.android.settings.core.InstrumentedPreferenceFragment
    protected int getPreferenceScreenResId() {
        return R.xml.vibrate_for_calls_settings;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class VibrateForCallsCandidateInfo extends CandidateInfo {
        private final String mKey;
        private final int mLabelId;

        @Override // com.android.settingslib.widget.CandidateInfo
        public Drawable loadIcon() {
            return null;
        }

        VibrateForCallsCandidateInfo(String str, int i) {
            super(true);
            this.mKey = str;
            this.mLabelId = i;
        }

        @Override // com.android.settingslib.widget.CandidateInfo
        public CharSequence loadLabel() {
            return VibrateForCallsPreferenceFragment.this.getContext().getString(this.mLabelId);
        }

        @Override // com.android.settingslib.widget.CandidateInfo
        public String getKey() {
            return this.mKey;
        }
    }
}
