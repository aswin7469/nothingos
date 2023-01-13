package com.android.systemui.media.taptotransfer.common;

import android.view.MotionEvent;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTttChipControllerCommon.kt */
/* synthetic */ class MediaTttChipControllerCommon$displayChip$1 extends FunctionReferenceImpl implements Function1<MotionEvent, Unit> {
    MediaTttChipControllerCommon$displayChip$1(Object obj) {
        super(1, obj, MediaTttChipControllerCommon.class, "onScreenTapped", "onScreenTapped(Landroid/view/MotionEvent;)V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((MotionEvent) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "p0");
        ((MediaTttChipControllerCommon) this.receiver).onScreenTapped(motionEvent);
    }
}
