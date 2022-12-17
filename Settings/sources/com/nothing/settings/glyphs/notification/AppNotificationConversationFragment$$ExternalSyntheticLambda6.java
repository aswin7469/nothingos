package com.nothing.settings.glyphs.notification;

import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppNotificationConversationFragment$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ PreferenceScreen f$0;

    public /* synthetic */ AppNotificationConversationFragment$$ExternalSyntheticLambda6(PreferenceScreen preferenceScreen) {
        this.f$0 = preferenceScreen;
    }

    public final void accept(Object obj) {
        ((AbstractPreferenceController) obj).displayPreference(this.f$0);
    }
}
