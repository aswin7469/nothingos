package com.nothing.settings.glyphs.notification;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AbsConversationPreferenceController$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ AbsConversationPreferenceController f$0;

    public /* synthetic */ AbsConversationPreferenceController$$ExternalSyntheticLambda0(AbsConversationPreferenceController absConversationPreferenceController) {
        this.f$0 = absConversationPreferenceController;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$populateConversations$0((ConversationChannelWrapper) obj);
    }
}
