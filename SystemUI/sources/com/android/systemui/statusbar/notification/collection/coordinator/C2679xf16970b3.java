package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.function.BiFunction;

/* renamed from: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$mNotifCollectionListener$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2679xf16970b3 implements BiFunction {
    public final /* synthetic */ NotificationEntry f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ C2679xf16970b3(NotificationEntry notificationEntry, boolean z, boolean z2, boolean z3, boolean z4) {
        this.f$0 = notificationEntry;
        this.f$1 = z;
        this.f$2 = z2;
        this.f$3 = z3;
        this.f$4 = z4;
    }

    public final Object apply(Object obj, Object obj2) {
        return HeadsUpCoordinator$mNotifCollectionListener$1.m3106onEntryUpdated$lambda1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (String) obj, (HeadsUpCoordinator.PostedEntry) obj2);
    }
}
