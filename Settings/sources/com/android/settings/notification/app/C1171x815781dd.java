package com.android.settings.notification.app;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/* renamed from: com.android.settings.notification.app.RecentConversationsPreferenceController$$ExternalSyntheticLambda2 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1171x815781dd implements Consumer {
    public final /* synthetic */ RecentConversationsPreferenceController f$0;
    public final /* synthetic */ AtomicInteger f$1;
    public final /* synthetic */ AtomicBoolean f$2;

    public /* synthetic */ C1171x815781dd(RecentConversationsPreferenceController recentConversationsPreferenceController, AtomicInteger atomicInteger, AtomicBoolean atomicBoolean) {
        this.f$0 = recentConversationsPreferenceController;
        this.f$1 = atomicInteger;
        this.f$2 = atomicBoolean;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$populateConversations$2(this.f$1, this.f$2, (RecentConversationPreference) obj);
    }
}
