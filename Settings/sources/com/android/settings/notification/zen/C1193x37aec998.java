package com.android.settings.notification.zen;

import androidx.preference.Preference;
import com.android.settingslib.applications.ApplicationsState;

/* renamed from: com.android.settings.notification.zen.ZenModeAllBypassingAppsPreferenceController$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1193x37aec998 implements Runnable {
    public final /* synthetic */ ZenModeAllBypassingAppsPreferenceController f$0;
    public final /* synthetic */ ApplicationsState.AppEntry f$1;
    public final /* synthetic */ Preference f$2;

    public /* synthetic */ C1193x37aec998(ZenModeAllBypassingAppsPreferenceController zenModeAllBypassingAppsPreferenceController, ApplicationsState.AppEntry appEntry, Preference preference) {
        this.f$0 = zenModeAllBypassingAppsPreferenceController;
        this.f$1 = appEntry;
        this.f$2 = preference;
    }

    public final void run() {
        this.f$0.lambda$updateIcon$1(this.f$1, this.f$2);
    }
}
