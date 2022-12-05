package com.android.systemui.statusbar;

import android.app.RemoteInputHistoryItem;
import android.os.Parcelable;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationRemoteInputManager$$ExternalSyntheticLambda4 implements Function {
    public static final /* synthetic */ NotificationRemoteInputManager$$ExternalSyntheticLambda4 INSTANCE = new NotificationRemoteInputManager$$ExternalSyntheticLambda4();

    private /* synthetic */ NotificationRemoteInputManager$$ExternalSyntheticLambda4() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        RemoteInputHistoryItem lambda$rebuildNotificationWithRemoteInputInserted$4;
        lambda$rebuildNotificationWithRemoteInputInserted$4 = NotificationRemoteInputManager.lambda$rebuildNotificationWithRemoteInputInserted$4((Parcelable) obj);
        return lambda$rebuildNotificationWithRemoteInputInserted$4;
    }
}
