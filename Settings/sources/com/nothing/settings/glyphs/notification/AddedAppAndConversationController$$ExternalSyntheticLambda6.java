package com.nothing.settings.glyphs.notification;

import com.nothing.settings.glyphs.preference.PrimaryDeletePreference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddedAppAndConversationController$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ PrimaryDeletePreference f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ AddedAppAndConversationController$$ExternalSyntheticLambda6(PrimaryDeletePreference primaryDeletePreference, String str) {
        this.f$0 = primaryDeletePreference;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.setSummary((CharSequence) this.f$1);
    }
}
