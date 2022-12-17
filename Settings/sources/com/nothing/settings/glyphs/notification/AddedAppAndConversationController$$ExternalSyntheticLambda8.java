package com.nothing.settings.glyphs.notification;

import android.service.notification.ConversationChannelWrapper;
import com.nothing.settings.glyphs.preference.PrimaryDeletePreference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddedAppAndConversationController$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ AddedAppAndConversationController f$0;
    public final /* synthetic */ PrimaryDeletePreference f$1;
    public final /* synthetic */ ConversationChannelWrapper f$2;

    public /* synthetic */ AddedAppAndConversationController$$ExternalSyntheticLambda8(AddedAppAndConversationController addedAppAndConversationController, PrimaryDeletePreference primaryDeletePreference, ConversationChannelWrapper conversationChannelWrapper) {
        this.f$0 = addedAppAndConversationController;
        this.f$1 = primaryDeletePreference;
        this.f$2 = conversationChannelWrapper;
    }

    public final void run() {
        this.f$0.lambda$createConversationPref$3(this.f$1, this.f$2);
    }
}
