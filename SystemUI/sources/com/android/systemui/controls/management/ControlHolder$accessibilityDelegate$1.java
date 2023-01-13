package com.android.systemui.controls.management;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlAdapter.kt */
/* synthetic */ class ControlHolder$accessibilityDelegate$1 extends FunctionReferenceImpl implements Function1<Boolean, CharSequence> {
    ControlHolder$accessibilityDelegate$1(Object obj) {
        super(1, obj, ControlHolder.class, "stateDescription", "stateDescription(Z)Ljava/lang/CharSequence;", 0);
    }

    public final CharSequence invoke(boolean z) {
        return ((ControlHolder) this.receiver).stateDescription(z);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Boolean) obj).booleanValue());
    }
}
