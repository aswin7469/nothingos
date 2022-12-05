package com.android.systemui.controls.management;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public /* synthetic */ class ControlHolder$accessibilityDelegate$1 extends FunctionReferenceImpl implements Function1<Boolean, CharSequence> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ControlHolder$accessibilityDelegate$1(ControlHolder controlHolder) {
        super(1, controlHolder, ControlHolder.class, "stateDescription", "stateDescription(Z)Ljava/lang/CharSequence;", 0);
    }

    @Nullable
    public final CharSequence invoke(boolean z) {
        CharSequence stateDescription;
        stateDescription = ((ControlHolder) this.receiver).stateDescription(z);
        return stateDescription;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ CharSequence mo1949invoke(Boolean bool) {
        return invoke(bool.booleanValue());
    }
}
