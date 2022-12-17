package com.android.settings.core;

import android.widget.Switch;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TogglePreferenceController$$ExternalSyntheticLambda0 implements OnMainSwitchChangeListener {
    public final /* synthetic */ TogglePreferenceController f$0;

    public /* synthetic */ TogglePreferenceController$$ExternalSyntheticLambda0(TogglePreferenceController togglePreferenceController) {
        this.f$0 = togglePreferenceController;
    }

    public final void onSwitchChanged(Switch switchR, boolean z) {
        this.f$0.lambda$displayPreference$0(switchR, z);
    }
}
