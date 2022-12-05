package com.android.settingslib.graph;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ThemedBatteryDrawable.kt */
/* loaded from: classes.dex */
public final class ThemedBatteryDrawable$invalidateRunnable$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ ThemedBatteryDrawable this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ThemedBatteryDrawable$invalidateRunnable$1(ThemedBatteryDrawable themedBatteryDrawable) {
        super(0);
        this.this$0 = themedBatteryDrawable;
    }

    @Override // kotlin.jvm.functions.Function0
    public /* bridge */ /* synthetic */ Unit invoke() {
        invoke2();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2() {
        this.this$0.invalidateSelf();
    }
}
