package com.android.systemui.animation;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ViewRootSync.kt */
/* synthetic */ class ViewRootSync$synchronizeNextDraw$2 extends FunctionReferenceImpl implements Function0<Unit> {
    ViewRootSync$synchronizeNextDraw$2(Object obj) {
        super(0, obj, Runnable.class, "run", "run()V", 0);
    }

    public final void invoke() {
        ((Runnable) this.receiver).run();
    }
}
