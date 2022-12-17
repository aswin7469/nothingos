package com.nothing.settings.glyphs.notification;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddedAppAndConversationController$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ AddedAppAndConversationController f$0;

    public /* synthetic */ AddedAppAndConversationController$$ExternalSyntheticLambda0(AddedAppAndConversationController addedAppAndConversationController) {
        this.f$0 = addedAppAndConversationController;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$populateConversations$0((ConversationChannelWrapper) obj);
    }
}
