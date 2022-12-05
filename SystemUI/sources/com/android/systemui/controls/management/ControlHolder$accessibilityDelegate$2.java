package com.android.systemui.controls.management;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public /* synthetic */ class ControlHolder$accessibilityDelegate$2 extends FunctionReferenceImpl implements Function0<Integer> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ControlHolder$accessibilityDelegate$2(ControlHolder controlHolder) {
        super(0, controlHolder, ControlHolder.class, "getLayoutPosition", "getLayoutPosition()I", 0);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [int, java.lang.Integer] */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final Integer mo1951invoke() {
        return ((ControlHolder) this.receiver).getLayoutPosition();
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ Integer mo1951invoke() {
        return Integer.valueOf(mo1951invoke());
    }
}
