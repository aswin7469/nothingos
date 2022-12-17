package com.android.settings.notification.app;

import androidx.preference.Preference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ConversationListPreferenceController$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ ConversationListPreferenceController f$0;
    public final /* synthetic */ AtomicInteger f$1;

    public /* synthetic */ ConversationListPreferenceController$$ExternalSyntheticLambda2(ConversationListPreferenceController conversationListPreferenceController, AtomicInteger atomicInteger) {
        this.f$0 = conversationListPreferenceController;
        this.f$1 = atomicInteger;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$populateConversations$1(this.f$1, (Preference) obj);
    }
}
