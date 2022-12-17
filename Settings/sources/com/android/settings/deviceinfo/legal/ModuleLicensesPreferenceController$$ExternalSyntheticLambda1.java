package com.android.settings.deviceinfo.legal;

import android.content.pm.ModuleInfo;
import androidx.preference.PreferenceGroup;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ModuleLicensesPreferenceController$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ PreferenceGroup f$0;

    public /* synthetic */ ModuleLicensesPreferenceController$$ExternalSyntheticLambda1(PreferenceGroup preferenceGroup) {
        this.f$0 = preferenceGroup;
    }

    public final void accept(Object obj) {
        this.f$0.addPreference(new ModuleLicensePreference(this.f$0.getContext(), (ModuleInfo) obj));
    }
}
