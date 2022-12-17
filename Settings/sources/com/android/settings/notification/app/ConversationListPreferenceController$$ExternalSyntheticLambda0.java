package com.android.settings.notification.app;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ConversationListPreferenceController$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ ConversationListPreferenceController f$0;

    public /* synthetic */ ConversationListPreferenceController$$ExternalSyntheticLambda0(ConversationListPreferenceController conversationListPreferenceController) {
        this.f$0 = conversationListPreferenceController;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$populateConversations$0((ConversationChannelWrapper) obj);
    }
}
