package com.android.p019wm.shell.common.magnetictarget;

import com.android.p019wm.shell.common.magnetictarget.MagnetizedObject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\u0010\u0000\u001a\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "T", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.common.magnetictarget.MagnetizedObject$maybeConsumeMotionEvent$1 */
/* compiled from: MagnetizedObject.kt */
final class MagnetizedObject$maybeConsumeMotionEvent$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ MagnetizedObject.MagneticTarget $flungToTarget;
    final /* synthetic */ MagnetizedObject<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MagnetizedObject$maybeConsumeMotionEvent$1(MagnetizedObject<T> magnetizedObject, MagnetizedObject.MagneticTarget magneticTarget) {
        super(0);
        this.this$0 = magnetizedObject;
        this.$flungToTarget = magneticTarget;
    }

    public final void invoke() {
        this.this$0.getMagnetListener().onReleasedInTarget(this.$flungToTarget);
        this.this$0.targetObjectIsStuckTo = null;
        this.this$0.vibrateIfEnabled(5);
    }
}
