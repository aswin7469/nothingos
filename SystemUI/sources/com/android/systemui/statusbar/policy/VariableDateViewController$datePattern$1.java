package com.android.systemui.statusbar.policy;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: VariableDateViewController.kt */
/* loaded from: classes2.dex */
public /* synthetic */ class VariableDateViewController$datePattern$1 extends FunctionReferenceImpl implements Function0<Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public VariableDateViewController$datePattern$1(VariableDateViewController variableDateViewController) {
        super(0, variableDateViewController, VariableDateViewController.class, "updateClock", "updateClock()V", 0);
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
        ((VariableDateViewController) this.receiver).updateClock();
    }
}
