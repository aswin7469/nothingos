package com.android.systemui.controls.ui;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlViewHolder.kt */
/* loaded from: classes.dex */
public final class ControlViewHolder$setErrorStatus$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ String $text;
    final /* synthetic */ ControlViewHolder this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlViewHolder$setErrorStatus$1(ControlViewHolder controlViewHolder, String str) {
        super(0);
        this.this$0 = controlViewHolder;
        this.$text = str;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        ControlViewHolder controlViewHolder = this.this$0;
        String text = this.$text;
        Intrinsics.checkNotNullExpressionValue(text, "text");
        controlViewHolder.setStatusText(text, true);
    }
}
