package com.android.systemui.wmshell;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
import java.util.function.IntConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubblesManager$$ExternalSyntheticLambda1 implements IntConsumer {
    public final /* synthetic */ BubblesManager f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ NotificationEntry f$2;

    public /* synthetic */ BubblesManager$$ExternalSyntheticLambda1(BubblesManager bubblesManager, List list, NotificationEntry notificationEntry) {
        this.f$0 = bubblesManager;
        this.f$1 = list;
        this.f$2 = notificationEntry;
    }

    public final void accept(int i) {
        this.f$0.mo47516x200aa1f8(this.f$1, this.f$2, i);
    }
}
