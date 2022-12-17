package com.nothing.settings.glyphs.notification;

import android.service.notification.ConversationChannelWrapper;
import com.android.settingslib.widget.AppPreference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AbsConversationPreferenceController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ AbsConversationPreferenceController f$0;
    public final /* synthetic */ AppPreference f$1;
    public final /* synthetic */ ConversationChannelWrapper f$2;

    public /* synthetic */ AbsConversationPreferenceController$$ExternalSyntheticLambda4(AbsConversationPreferenceController absConversationPreferenceController, AppPreference appPreference, ConversationChannelWrapper conversationChannelWrapper) {
        this.f$0 = absConversationPreferenceController;
        this.f$1 = appPreference;
        this.f$2 = conversationChannelWrapper;
    }

    public final void run() {
        this.f$0.lambda$createConversationPref$3(this.f$1, this.f$2);
    }
}
