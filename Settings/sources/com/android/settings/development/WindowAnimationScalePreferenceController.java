package com.android.settings.development;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.IWindowManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R$array;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class WindowAnimationScalePreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final float DEFAULT_VALUE = 1.0f;
    static final int WINDOW_ANIMATION_SCALE_SELECTOR = 0;
    private final String[] mListSummaries;
    private final String[] mListValues;
    private final IWindowManager mWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));

    public String getPreferenceKey() {
        return "window_animation_scale";
    }

    public WindowAnimationScalePreferenceController(Context context) {
        super(context);
        this.mListValues = context.getResources().getStringArray(R$array.window_animation_scale_values);
        this.mListSummaries = context.getResources().getStringArray(R$array.window_animation_scale_entries);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        writeAnimationScaleOption(obj);
        return true;
    }

    public void updateState(Preference preference) {
        updateAnimationScaleValue();
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        writeAnimationScaleOption((Object) null);
    }

    private void writeAnimationScaleOption(Object obj) {
        float f;
        if (obj != null) {
            try {
                f = Float.parseFloat(obj.toString());
            } catch (RemoteException unused) {
                return;
            }
        } else {
            f = DEFAULT_VALUE;
        }
        this.mWindowManager.setAnimationScale(0, f);
        updateAnimationScaleValue();
    }

    private void updateAnimationScaleValue() {
        try {
            int i = 0;
            float animationScale = this.mWindowManager.getAnimationScale(0);
            int i2 = 0;
            while (true) {
                String[] strArr = this.mListValues;
                if (i2 >= strArr.length) {
                    break;
                } else if (animationScale <= Float.parseFloat(strArr[i2])) {
                    i = i2;
                    break;
                } else {
                    i2++;
                }
            }
            ListPreference listPreference = (ListPreference) this.mPreference;
            listPreference.setValue(this.mListValues[i]);
            listPreference.setSummary(this.mListSummaries[i]);
        } catch (RemoteException unused) {
        }
    }
}
