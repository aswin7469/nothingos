package com.nothing.settings.sound;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VibrateForCallsPreferenceFragment extends RadioButtonPickerFragment {
    private final Map<String, VibrateForCallsCandidateInfo> mCandidates = new ArrayMap();
    private int mDefaultRingVibrationIntensity;

    public int getMetricsCategory() {
        return 1827;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mDefaultRingVibrationIntensity = loadDefaultIntensity(context, 17694800);
        loadCandidates(context);
    }

    private int loadDefaultIntensity(Context context, int i) {
        if (context != null) {
            return context.getResources().getInteger(i);
        }
        return 2;
    }

    private void loadCandidates(Context context) {
        this.mCandidates.put("never_vibrate", new VibrateForCallsCandidateInfo("never_vibrate", R$string.vibrate_when_ringing_option_never_vibrate));
        this.mCandidates.put("always_vibrate", new VibrateForCallsCandidateInfo("always_vibrate", R$string.vibrate_when_ringing_option_always_vibrate));
    }

    private void updateSettings(VibrateForCallsCandidateInfo vibrateForCallsCandidateInfo) {
        String key = vibrateForCallsCandidateInfo.getKey();
        ContentResolver contentResolver = getContext().getContentResolver();
        if (TextUtils.equals(key, "always_vibrate")) {
            Settings.System.putInt(contentResolver, "vibrate_when_ringing", 1);
            Settings.Global.putInt(contentResolver, "apply_ramping_ringer", 0);
            Settings.System.putInt(contentResolver, "ring_vibration_intensity", this.mDefaultRingVibrationIntensity);
            Log.d("VibrateForCallsPreferenceFragment", "updateSettings: set RING_VIBRATION_INTENSITY: " + this.mDefaultRingVibrationIntensity);
        } else if (TextUtils.equals(key, "ramping_ringer")) {
            Settings.System.putInt(contentResolver, "vibrate_when_ringing", 0);
            Settings.Global.putInt(contentResolver, "apply_ramping_ringer", 1);
            Settings.System.putInt(contentResolver, "ring_vibration_intensity", 0);
        } else if (TextUtils.equals(key, "sync_with_ringtone_vibrate")) {
            Settings.System.putInt(contentResolver, "vibrate_when_ringing", 0);
            Settings.Global.putInt(contentResolver, "apply_ramping_ringer", 0);
            Settings.System.putInt(contentResolver, "ring_vibration_intensity", this.mDefaultRingVibrationIntensity);
        } else {
            Settings.System.putInt(contentResolver, "vibrate_when_ringing", 0);
            Settings.Global.putInt(contentResolver, "apply_ramping_ringer", 0);
            Settings.System.putInt(contentResolver, "ring_vibration_intensity", 0);
        }
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mCandidates.get("never_vibrate"));
        arrayList.add(this.mCandidates.get("always_vibrate"));
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        if (Settings.Global.getInt(getContext().getContentResolver(), "apply_ramping_ringer", 0) == 1) {
            return "ramping_ringer";
        }
        return Settings.System.getInt(getContext().getContentResolver(), "vibrate_when_ringing", 0) == 0 ? "never_vibrate" : "always_vibrate";
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        VibrateForCallsCandidateInfo vibrateForCallsCandidateInfo = this.mCandidates.get(str);
        if (vibrateForCallsCandidateInfo == null) {
            Log.e("VibrateForCallsPreferenceFragment", "Unknown vibrate for calls candidate (key = " + str + ")!");
            return false;
        }
        updateSettings(vibrateForCallsCandidateInfo);
        return true;
    }

    public int getPreferenceScreenResId() {
        return R$xml.vibrate_for_calls_settings;
    }

    public class VibrateForCallsCandidateInfo extends CandidateInfo {
        private final String mKey;
        private final int mLabelId;

        public Drawable loadIcon() {
            return null;
        }

        VibrateForCallsCandidateInfo(String str, int i) {
            super(true);
            this.mKey = str;
            this.mLabelId = i;
        }

        public CharSequence loadLabel() {
            return VibrateForCallsPreferenceFragment.this.getContext().getString(this.mLabelId);
        }

        public String getKey() {
            return this.mKey;
        }
    }
}
