package com.android.systemui.controls.p010ui;

import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl$show$1 */
/* compiled from: ControlsUiControllerImpl.kt */
/* synthetic */ class ControlsUiControllerImpl$show$1 extends FunctionReferenceImpl implements Function1<List<? extends SelectionItem>, Unit> {
    ControlsUiControllerImpl$show$1(Object obj) {
        super(1, obj, ControlsUiControllerImpl.class, "showSeedingView", "showSeedingView(Ljava/util/List;)V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((List<SelectionItem>) (List) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(List<SelectionItem> list) {
        Intrinsics.checkNotNullParameter(list, "p0");
        ((ControlsUiControllerImpl) this.receiver).showSeedingView(list);
    }
}
