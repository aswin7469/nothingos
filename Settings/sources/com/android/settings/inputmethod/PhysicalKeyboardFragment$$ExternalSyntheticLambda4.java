package com.android.settings.inputmethod;

import androidx.preference.Preference;
import com.android.settings.inputmethod.PhysicalKeyboardFragment;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PhysicalKeyboardFragment$$ExternalSyntheticLambda4 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ PhysicalKeyboardFragment f$0;
    public final /* synthetic */ PhysicalKeyboardFragment.HardKeyboardDeviceInfo f$1;

    public /* synthetic */ PhysicalKeyboardFragment$$ExternalSyntheticLambda4(PhysicalKeyboardFragment physicalKeyboardFragment, PhysicalKeyboardFragment.HardKeyboardDeviceInfo hardKeyboardDeviceInfo) {
        this.f$0 = physicalKeyboardFragment;
        this.f$1 = hardKeyboardDeviceInfo;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updateHardKeyboards$2(this.f$1, preference);
    }
}
