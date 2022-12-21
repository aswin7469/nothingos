package com.android.systemui.statusbar.notification.collection.coordinator;

import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
/* synthetic */ class HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$4 extends FunctionReferenceImpl implements Function1<String, GroupLocation> {
    HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$4(Object obj) {
        super(1, obj, HeadsUpCoordinatorKt.class, "getLocation", "getLocation(Ljava/util/Map;Ljava/lang/String;)Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupLocation;", 1);
    }

    public final GroupLocation invoke(String str) {
        Intrinsics.checkNotNullParameter(str, "p0");
        return HeadsUpCoordinatorKt.getLocation((Map) this.receiver, str);
    }
}
