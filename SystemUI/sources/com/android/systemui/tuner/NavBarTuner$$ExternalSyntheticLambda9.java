package com.android.systemui.tuner;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NavBarTuner$$ExternalSyntheticLambda9 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ NavBarTuner f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ ListPreference f$3;
    public final /* synthetic */ ListPreference f$4;

    public /* synthetic */ NavBarTuner$$ExternalSyntheticLambda9(NavBarTuner navBarTuner, Preference preference, String str, ListPreference listPreference, ListPreference listPreference2) {
        this.f$0 = navBarTuner;
        this.f$1 = preference;
        this.f$2 = str;
        this.f$3 = listPreference;
        this.f$4 = listPreference2;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.m3258lambda$bindButton$9$comandroidsystemuitunerNavBarTuner(this.f$1, this.f$2, this.f$3, this.f$4, preference);
    }
}
