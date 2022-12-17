package com.android.settings.notification.zen;

import android.graphics.drawable.Drawable;
import androidx.preference.Preference;

/* renamed from: com.android.settings.notification.zen.ZenModeAllBypassingAppsPreferenceController$$ExternalSyntheticLambda2 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1194x37aec999 implements Runnable {
    public final /* synthetic */ Preference f$0;
    public final /* synthetic */ Drawable f$1;

    public /* synthetic */ C1194x37aec999(Preference preference, Drawable drawable) {
        this.f$0 = preference;
        this.f$1 = drawable;
    }

    public final void run() {
        this.f$0.setIcon(this.f$1);
    }
}
