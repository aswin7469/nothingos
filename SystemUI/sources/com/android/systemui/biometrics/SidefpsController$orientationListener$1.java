package com.android.systemui.biometrics;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SidefpsController.kt */
final class SidefpsController$orientationListener$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ SidefpsController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SidefpsController$orientationListener$1(SidefpsController sidefpsController) {
        super(0);
        this.this$0 = sidefpsController;
    }

    public final void invoke() {
        this.this$0.onOrientationChanged();
    }
}
