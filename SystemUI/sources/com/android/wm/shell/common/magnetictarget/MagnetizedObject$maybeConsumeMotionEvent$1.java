package com.android.wm.shell.common.magnetictarget;

import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes2.dex */
public final class MagnetizedObject$maybeConsumeMotionEvent$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ MagnetizedObject.MagneticTarget $flungToTarget;
    final /* synthetic */ MagnetizedObject<T> this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MagnetizedObject$maybeConsumeMotionEvent$1(MagnetizedObject<T> magnetizedObject, MagnetizedObject.MagneticTarget magneticTarget) {
        super(0);
        this.this$0 = magnetizedObject;
        this.$flungToTarget = magneticTarget;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        this.this$0.getMagnetListener().onReleasedInTarget(this.$flungToTarget);
        ((MagnetizedObject) this.this$0).targetObjectIsStuckTo = null;
        this.this$0.vibrateIfEnabled(5);
    }
}
