package com.nothing.settings.glyphs.notification;

import androidx.preference.Preference;

/* renamed from: com.nothing.settings.glyphs.notification.NotificationSettingPreferenceController$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C2024xfd87330b implements Runnable {
    public final /* synthetic */ Preference f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ C2024xfd87330b(Preference preference, String str) {
        this.f$0 = preference;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.setSummary((CharSequence) this.f$1);
    }
}
