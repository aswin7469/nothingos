package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RemoteInputCoordinator$$ExternalSyntheticLambda0 implements SmartReplyController.Callback {
    public final /* synthetic */ RemoteInputCoordinator f$0;

    public /* synthetic */ RemoteInputCoordinator$$ExternalSyntheticLambda0(RemoteInputCoordinator remoteInputCoordinator) {
        this.f$0 = remoteInputCoordinator;
    }

    public final void onSmartReplySent(NotificationEntry notificationEntry, CharSequence charSequence) {
        this.f$0.onSmartReplySent(notificationEntry, charSequence);
    }
}
