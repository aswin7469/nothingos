package com.nothing.settings.glyphs.notification;

import com.android.settingslib.widget.AppPreference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AbsConversationPreferenceController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ AppPreference f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ AbsConversationPreferenceController$$ExternalSyntheticLambda3(AppPreference appPreference, String str) {
        this.f$0 = appPreference;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.setSummary((CharSequence) this.f$1);
    }
}
