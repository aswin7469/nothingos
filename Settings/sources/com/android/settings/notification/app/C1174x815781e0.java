package com.android.settings.notification.app;

import android.app.people.ConversationChannel;
import androidx.preference.Preference;

/* renamed from: com.android.settings.notification.app.RecentConversationsPreferenceController$$ExternalSyntheticLambda5 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1174x815781e0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ RecentConversationsPreferenceController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ ConversationChannel f$3;
    public final /* synthetic */ String f$4;
    public final /* synthetic */ RecentConversationPreference f$5;

    public /* synthetic */ C1174x815781e0(RecentConversationsPreferenceController recentConversationsPreferenceController, String str, int i, ConversationChannel conversationChannel, String str2, RecentConversationPreference recentConversationPreference) {
        this.f$0 = recentConversationsPreferenceController;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = conversationChannel;
        this.f$4 = str2;
        this.f$5 = recentConversationPreference;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$createConversationPref$4(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, preference);
    }
}
