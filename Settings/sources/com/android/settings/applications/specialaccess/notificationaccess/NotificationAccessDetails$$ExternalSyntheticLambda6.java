package com.android.settings.applications.specialaccess.notificationaccess;

import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationAccessDetails$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ PreferenceScreen f$0;

    public /* synthetic */ NotificationAccessDetails$$ExternalSyntheticLambda6(PreferenceScreen preferenceScreen) {
        this.f$0 = preferenceScreen;
    }

    public final void accept(Object obj) {
        NotificationAccessDetails.lambda$enable$5(this.f$0, (AbstractPreferenceController) obj);
    }
}
