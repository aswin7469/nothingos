package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationIconAreaController$$ExternalSyntheticLambda5 implements Function {
    public static final /* synthetic */ NotificationIconAreaController$$ExternalSyntheticLambda5 INSTANCE = new NotificationIconAreaController$$ExternalSyntheticLambda5();

    private /* synthetic */ NotificationIconAreaController$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        StatusBarIconView lambda$updateStatusBarIcons$2;
        lambda$updateStatusBarIcons$2 = NotificationIconAreaController.lambda$updateStatusBarIcons$2((NotificationEntry) obj);
        return lambda$updateStatusBarIcons$2;
    }
}
