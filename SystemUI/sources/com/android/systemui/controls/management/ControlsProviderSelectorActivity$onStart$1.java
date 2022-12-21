package com.android.systemui.controls.management;

import android.content.ComponentName;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsProviderSelectorActivity.kt */
/* synthetic */ class ControlsProviderSelectorActivity$onStart$1 extends FunctionReferenceImpl implements Function1<ComponentName, Unit> {
    ControlsProviderSelectorActivity$onStart$1(Object obj) {
        super(1, obj, ControlsProviderSelectorActivity.class, "launchFavoritingActivity", "launchFavoritingActivity(Landroid/content/ComponentName;)V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((ComponentName) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(ComponentName componentName) {
        ((ControlsProviderSelectorActivity) this.receiver).launchFavoritingActivity(componentName);
    }
}
