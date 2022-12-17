package com.android.settings.core;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class InstrumentedPreferenceFragment$$ExternalSyntheticLambda0 implements PreferenceManager.OnPreferenceTreeClickListener {
    public final /* synthetic */ InstrumentedPreferenceFragment f$0;

    public /* synthetic */ InstrumentedPreferenceFragment$$ExternalSyntheticLambda0(InstrumentedPreferenceFragment instrumentedPreferenceFragment) {
        this.f$0 = instrumentedPreferenceFragment;
    }

    public final boolean onPreferenceTreeClick(Preference preference) {
        return this.f$0.lambda$onStart$0(preference);
    }
}
