package com.android.systemui.statusbar.phone.ongoingcall;

import android.view.MotionEvent;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/view/MotionEvent;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: OngoingCallController.kt */
final class OngoingCallController$updateGestureListening$2$1 extends Lambda implements Function1<MotionEvent, Unit> {
    final /* synthetic */ OngoingCallController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    OngoingCallController$updateGestureListening$2$1(OngoingCallController ongoingCallController) {
        super(1);
        this.this$0 = ongoingCallController;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((MotionEvent) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "<anonymous parameter 0>");
        this.this$0.onSwipeAwayGestureDetected();
    }
}
