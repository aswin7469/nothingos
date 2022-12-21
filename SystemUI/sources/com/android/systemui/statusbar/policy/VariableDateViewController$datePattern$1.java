package com.android.systemui.statusbar.policy;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: VariableDateViewController.kt */
/* synthetic */ class VariableDateViewController$datePattern$1 extends FunctionReferenceImpl implements Function0<Unit> {
    VariableDateViewController$datePattern$1(Object obj) {
        super(0, obj, VariableDateViewController.class, "updateClock", "updateClock()V", 0);
    }

    public final void invoke() {
        ((VariableDateViewController) this.receiver).updateClock();
    }
}
