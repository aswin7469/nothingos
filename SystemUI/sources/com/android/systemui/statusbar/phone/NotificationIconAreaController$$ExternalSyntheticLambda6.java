package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationIconAreaController$$ExternalSyntheticLambda6 implements Function {
    public static final /* synthetic */ NotificationIconAreaController$$ExternalSyntheticLambda6 INSTANCE = new NotificationIconAreaController$$ExternalSyntheticLambda6();

    private /* synthetic */ NotificationIconAreaController$$ExternalSyntheticLambda6() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        StatusBarIconView lambda$updateCenterIcon$3;
        lambda$updateCenterIcon$3 = NotificationIconAreaController.lambda$updateCenterIcon$3((NotificationEntry) obj);
        return lambda$updateCenterIcon$3;
    }
}
