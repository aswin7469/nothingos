package com.nothing.settings.glyphs.notification;

import android.content.DialogInterface;
import android.service.notification.ConversationChannelWrapper;
import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddedAppAndConversationController$$ExternalSyntheticLambda4 implements DialogInterface.OnClickListener {
    public final /* synthetic */ AddedAppAndConversationController f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ ConversationChannelWrapper f$2;

    public /* synthetic */ AddedAppAndConversationController$$ExternalSyntheticLambda4(AddedAppAndConversationController addedAppAndConversationController, Preference preference, ConversationChannelWrapper conversationChannelWrapper) {
        this.f$0 = addedAppAndConversationController;
        this.f$1 = preference;
        this.f$2 = conversationChannelWrapper;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateDialog$5(this.f$1, this.f$2, dialogInterface, i);
    }
}
