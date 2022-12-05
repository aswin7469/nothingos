package com.android.systemui.controls.management;

import android.content.ComponentName;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
/* synthetic */ class ControlsProviderSelectorActivity$onStart$1 extends FunctionReferenceImpl implements Function1<ComponentName, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ControlsProviderSelectorActivity$onStart$1(ControlsProviderSelectorActivity controlsProviderSelectorActivity) {
        super(1, controlsProviderSelectorActivity, ControlsProviderSelectorActivity.class, "launchFavoritingActivity", "launchFavoritingActivity(Landroid/content/ComponentName;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(ComponentName componentName) {
        invoke2(componentName);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(@Nullable ComponentName componentName) {
        ((ControlsProviderSelectorActivity) this.receiver).launchFavoritingActivity(componentName);
    }
}
