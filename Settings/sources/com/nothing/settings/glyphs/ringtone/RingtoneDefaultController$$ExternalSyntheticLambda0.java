package com.nothing.settings.glyphs.ringtone;

import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RingtoneDefaultController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Preference f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ RingtoneDefaultController$$ExternalSyntheticLambda0(Preference preference, String str) {
        this.f$0 = preference;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.setSummary((CharSequence) this.f$1);
    }
}
