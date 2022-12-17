package com.android.settings.notification.zen;

import androidx.preference.Preference;
import com.android.settingslib.applications.ApplicationsState;

/* renamed from: com.android.settings.notification.zen.ZenModeAddBypassingAppsPreferenceController$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1189x33e36a98 implements Runnable {
    public final /* synthetic */ ZenModeAddBypassingAppsPreferenceController f$0;
    public final /* synthetic */ ApplicationsState.AppEntry f$1;
    public final /* synthetic */ Preference f$2;

    public /* synthetic */ C1189x33e36a98(ZenModeAddBypassingAppsPreferenceController zenModeAddBypassingAppsPreferenceController, ApplicationsState.AppEntry appEntry, Preference preference) {
        this.f$0 = zenModeAddBypassingAppsPreferenceController;
        this.f$1 = appEntry;
        this.f$2 = preference;
    }

    public final void run() {
        this.f$0.lambda$updateIcon$1(this.f$1, this.f$2);
    }
}
