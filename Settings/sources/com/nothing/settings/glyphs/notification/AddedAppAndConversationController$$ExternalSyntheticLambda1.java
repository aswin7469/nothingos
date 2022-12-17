package com.nothing.settings.glyphs.notification;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddedAppAndConversationController$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ AddedAppAndConversationController f$0;

    public /* synthetic */ AddedAppAndConversationController$$ExternalSyntheticLambda1(AddedAppAndConversationController addedAppAndConversationController) {
        this.f$0 = addedAppAndConversationController;
    }

    public final Object apply(Object obj) {
        return this.f$0.createConversationPref((ConversationChannelWrapper) obj);
    }
}
