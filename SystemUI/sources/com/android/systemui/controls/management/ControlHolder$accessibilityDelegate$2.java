package com.android.systemui.controls.management;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlAdapter.kt */
/* synthetic */ class ControlHolder$accessibilityDelegate$2 extends FunctionReferenceImpl implements Function0<Integer> {
    ControlHolder$accessibilityDelegate$2(Object obj) {
        super(0, obj, ControlHolder.class, "getLayoutPosition", "getLayoutPosition()I", 0);
    }

    public final Integer invoke() {
        return Integer.valueOf(((ControlHolder) this.receiver).getLayoutPosition());
    }
}
