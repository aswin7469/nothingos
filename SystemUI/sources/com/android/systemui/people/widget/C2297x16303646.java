package com.android.systemui.people.widget;

import android.app.people.ConversationChannel;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;

/* renamed from: com.android.systemui.people.widget.PeopleSpaceWidgetManager$TileConversationListener$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2297x16303646 implements Runnable {
    public final /* synthetic */ PeopleSpaceWidgetManager.TileConversationListener f$0;
    public final /* synthetic */ ConversationChannel f$1;

    public /* synthetic */ C2297x16303646(PeopleSpaceWidgetManager.TileConversationListener tileConversationListener, ConversationChannel conversationChannel) {
        this.f$0 = tileConversationListener;
        this.f$1 = conversationChannel;
    }

    public final void run() {
        this.f$0.mo35199x9b1799fc(this.f$1);
    }
}
