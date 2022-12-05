package com.android.wm.shell.bubbles;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DismissView.kt */
/* loaded from: classes2.dex */
public final class DismissView$hide$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ DismissView this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DismissView$hide$1(DismissView dismissView) {
        super(0);
        this.this$0 = dismissView;
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
        this.this$0.setVisibility(4);
    }
}
