package com.android.wm.shell.common.magnetictarget;

import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes2.dex */
public /* synthetic */ class MagnetizedObject$animateStuckToTarget$1 extends FunctionReferenceImpl implements Function5<MagnetizedObject.MagneticTarget, Float, Float, Boolean, Function0<? extends Unit>, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public MagnetizedObject$animateStuckToTarget$1(MagnetizedObject<T> magnetizedObject) {
        super(5, magnetizedObject, MagnetizedObject.class, "animateStuckToTargetInternal", "animateStuckToTargetInternal(Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagneticTarget;FFZLkotlin/jvm/functions/Function0;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function5
    public /* bridge */ /* synthetic */ Unit invoke(MagnetizedObject.MagneticTarget magneticTarget, Float f, Float f2, Boolean bool, Function0<? extends Unit> function0) {
        invoke(magneticTarget, f.floatValue(), f2.floatValue(), bool.booleanValue(), (Function0<Unit>) function0);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull MagnetizedObject.MagneticTarget p0, float f, float f2, boolean z, @Nullable Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        ((MagnetizedObject) this.receiver).animateStuckToTargetInternal(p0, f, f2, z, function0);
    }
}
