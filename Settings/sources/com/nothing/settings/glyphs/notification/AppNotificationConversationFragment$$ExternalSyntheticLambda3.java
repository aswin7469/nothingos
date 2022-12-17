package com.nothing.settings.glyphs.notification;

import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppNotificationConversationFragment$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ AppNotificationConversationFragment f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ AppNotificationConversationFragment$$ExternalSyntheticLambda3(AppNotificationConversationFragment appNotificationConversationFragment, List list, List list2) {
        this.f$0 = appNotificationConversationFragment;
        this.f$1 = list;
        this.f$2 = list2;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$checkUiBlocker$1(this.f$1, this.f$2, (AbstractPreferenceController) obj);
    }
}
