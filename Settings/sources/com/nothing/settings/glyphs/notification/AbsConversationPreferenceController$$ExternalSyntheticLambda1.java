package com.nothing.settings.glyphs.notification;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AbsConversationPreferenceController$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ AbsConversationPreferenceController f$0;

    public /* synthetic */ AbsConversationPreferenceController$$ExternalSyntheticLambda1(AbsConversationPreferenceController absConversationPreferenceController) {
        this.f$0 = absConversationPreferenceController;
    }

    public final Object apply(Object obj) {
        return this.f$0.createConversationPref((ConversationChannelWrapper) obj);
    }
}
