package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationIconAreaController$$ExternalSyntheticLambda7 implements Function {
    public static final /* synthetic */ NotificationIconAreaController$$ExternalSyntheticLambda7 INSTANCE = new NotificationIconAreaController$$ExternalSyntheticLambda7();

    private /* synthetic */ NotificationIconAreaController$$ExternalSyntheticLambda7() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        StatusBarIconView lambda$updateShelfIcons$1;
        lambda$updateShelfIcons$1 = NotificationIconAreaController.lambda$updateShelfIcons$1((NotificationEntry) obj);
        return lambda$updateShelfIcons$1;
    }
}
