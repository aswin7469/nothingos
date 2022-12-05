package com.android.systemui.controls.management;

import android.content.ComponentName;
import com.android.systemui.controls.controller.ControlsController;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
/* synthetic */ class ControlsProviderSelectorActivity$onStart$2 extends FunctionReferenceImpl implements Function1<ComponentName, Integer> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ControlsProviderSelectorActivity$onStart$2(ControlsController controlsController) {
        super(1, controlsController, ControlsController.class, "countFavoritesForComponent", "countFavoritesForComponent(Landroid/content/ComponentName;)I", 0);
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final int invoke2(@NotNull ComponentName p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        return ((ControlsController) this.receiver).countFavoritesForComponent(p0);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Integer mo1949invoke(ComponentName componentName) {
        return Integer.valueOf(invoke2(componentName));
    }
}
