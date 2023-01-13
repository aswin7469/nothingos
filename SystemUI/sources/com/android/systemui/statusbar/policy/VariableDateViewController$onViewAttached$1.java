package com.android.systemui.statusbar.policy;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: VariableDateViewController.kt */
/* synthetic */ class VariableDateViewController$onViewAttached$1 extends FunctionReferenceImpl implements Function0<Unit> {
    VariableDateViewController$onViewAttached$1(Object obj) {
        super(0, obj, VariableDateViewController.class, "updateClock", "updateClock()V", 0);
    }

    public final void invoke() {
        ((VariableDateViewController) this.receiver).updateClock();
    }
}
