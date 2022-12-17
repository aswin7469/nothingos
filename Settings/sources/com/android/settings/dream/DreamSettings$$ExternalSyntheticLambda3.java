package com.android.settings.dream;

import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamSettings$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ DreamSettings f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ DreamSettings$$ExternalSyntheticLambda3(DreamSettings dreamSettings, boolean z) {
        this.f$0 = dreamSettings;
        this.f$1 = z;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$setAllPreferencesEnabled$0(this.f$1, (AbstractPreferenceController) obj);
    }
}
