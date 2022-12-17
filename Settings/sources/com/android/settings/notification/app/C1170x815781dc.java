package com.android.settings.notification.app;

import android.app.people.ConversationChannel;
import java.util.function.Function;

/* renamed from: com.android.settings.notification.app.RecentConversationsPreferenceController$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1170x815781dc implements Function {
    public final /* synthetic */ RecentConversationsPreferenceController f$0;

    public /* synthetic */ C1170x815781dc(RecentConversationsPreferenceController recentConversationsPreferenceController) {
        this.f$0 = recentConversationsPreferenceController;
    }

    public final Object apply(Object obj) {
        return this.f$0.createConversationPref((ConversationChannel) obj);
    }
}
