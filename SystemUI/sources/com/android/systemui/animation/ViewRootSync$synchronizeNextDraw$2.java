package com.android.systemui.animation;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ViewRootSync.kt */
/* synthetic */ class ViewRootSync$synchronizeNextDraw$2 extends FunctionReferenceImpl implements Function0<Unit> {
    ViewRootSync$synchronizeNextDraw$2(Object obj) {
        super(0, obj, Runnable.class, "run", "run()V", 0);
    }

    public final void invoke() {
        ((Runnable) this.receiver).run();
    }
}
