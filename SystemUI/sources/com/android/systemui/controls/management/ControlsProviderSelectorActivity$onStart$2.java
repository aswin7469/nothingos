package com.android.systemui.controls.management;

import android.content.ComponentName;
import com.android.systemui.controls.controller.ControlsController;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsProviderSelectorActivity.kt */
/* synthetic */ class ControlsProviderSelectorActivity$onStart$2 extends FunctionReferenceImpl implements Function1<ComponentName, Integer> {
    ControlsProviderSelectorActivity$onStart$2(Object obj) {
        super(1, obj, ControlsController.class, "countFavoritesForComponent", "countFavoritesForComponent(Landroid/content/ComponentName;)I", 0);
    }

    public final Integer invoke(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "p0");
        return Integer.valueOf(((ControlsController) this.receiver).countFavoritesForComponent(componentName));
    }
}
