package com.nothing.settings.glyphs.notification;

import androidx.preference.Preference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddedAppAndConversationController$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ AddedAppAndConversationController f$0;
    public final /* synthetic */ AtomicInteger f$1;

    public /* synthetic */ AddedAppAndConversationController$$ExternalSyntheticLambda2(AddedAppAndConversationController addedAppAndConversationController, AtomicInteger atomicInteger) {
        this.f$0 = addedAppAndConversationController;
        this.f$1 = atomicInteger;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$populateConversations$1(this.f$1, (Preference) obj);
    }
}
