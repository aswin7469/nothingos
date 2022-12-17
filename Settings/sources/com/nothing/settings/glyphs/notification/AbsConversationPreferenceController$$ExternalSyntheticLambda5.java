package com.nothing.settings.glyphs.notification;

import android.service.notification.ConversationChannelWrapper;
import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AbsConversationPreferenceController$$ExternalSyntheticLambda5 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ AbsConversationPreferenceController f$0;
    public final /* synthetic */ ConversationChannelWrapper f$1;

    public /* synthetic */ AbsConversationPreferenceController$$ExternalSyntheticLambda5(AbsConversationPreferenceController absConversationPreferenceController, ConversationChannelWrapper conversationChannelWrapper) {
        this.f$0 = absConversationPreferenceController;
        this.f$1 = conversationChannelWrapper;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$createConversationPref$4(this.f$1, preference);
    }
}
