package com.nothing.settings.glyphs.notification;

import android.service.notification.ConversationChannelWrapper;
import androidx.preference.Preference;
import com.nothing.settings.glyphs.notification.AddedAppAndConversationController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddedAppAndConversationController$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AddedAppAndConversationController.C20181 f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ ConversationChannelWrapper f$2;

    public /* synthetic */ AddedAppAndConversationController$1$$ExternalSyntheticLambda0(AddedAppAndConversationController.C20181 r1, Preference preference, ConversationChannelWrapper conversationChannelWrapper) {
        this.f$0 = r1;
        this.f$1 = preference;
        this.f$2 = conversationChannelWrapper;
    }

    public final void run() {
        this.f$0.lambda$onClickDelete$0(this.f$1, this.f$2);
    }
}
