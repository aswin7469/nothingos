package com.android.systemui.controls.management;

import com.android.systemui.controls.ControlStatus;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/controls/management/ControlStatusWrapper;", "it", "Lcom/android/systemui/controls/ControlStatus;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AllModel.kt */
final class AllModel$createWrappers$values$1 extends Lambda implements Function1<ControlStatus, ControlStatusWrapper> {
    public static final AllModel$createWrappers$values$1 INSTANCE = new AllModel$createWrappers$values$1();

    AllModel$createWrappers$values$1() {
        super(1);
    }

    public final ControlStatusWrapper invoke(ControlStatus controlStatus) {
        Intrinsics.checkNotNullParameter(controlStatus, "it");
        return new ControlStatusWrapper(controlStatus);
    }
}
