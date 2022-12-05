package com.android.systemui.controls.ui;

import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public /* synthetic */ class ControlsUiControllerImpl$show$5 extends FunctionReferenceImpl implements Function1<List<? extends SelectionItem>, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ControlsUiControllerImpl$show$5(ControlsUiControllerImpl controlsUiControllerImpl) {
        super(1, controlsUiControllerImpl, ControlsUiControllerImpl.class, "showControlsView", "showControlsView(Ljava/util/List;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(List<? extends SelectionItem> list) {
        invoke2((List<SelectionItem>) list);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(@NotNull List<SelectionItem> p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        ((ControlsUiControllerImpl) this.receiver).showControlsView(p0);
    }
}
