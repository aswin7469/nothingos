package com.android.settings.notification.app;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ConversationListPreferenceController$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ ConversationListPreferenceController f$0;

    public /* synthetic */ ConversationListPreferenceController$$ExternalSyntheticLambda1(ConversationListPreferenceController conversationListPreferenceController) {
        this.f$0 = conversationListPreferenceController;
    }

    public final Object apply(Object obj) {
        return this.f$0.createConversationPref((ConversationChannelWrapper) obj);
    }
}
