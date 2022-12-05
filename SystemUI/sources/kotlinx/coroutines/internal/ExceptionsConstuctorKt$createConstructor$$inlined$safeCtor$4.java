package kotlinx.coroutines.internal;

import java.lang.reflect.Constructor;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ExceptionsConstuctor.kt */
/* loaded from: classes2.dex */
public final class ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4 extends Lambda implements Function1<Throwable, Throwable> {
    final /* synthetic */ Constructor $constructor$inlined;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4(Constructor constructor) {
        super(1);
        this.$constructor$inlined = constructor;
    }

    @Override // kotlin.jvm.functions.Function1
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Throwable mo1949invoke(@NotNull Throwable e) {
        Object m1934constructorimpl;
        Object newInstance;
        Intrinsics.checkParameterIsNotNull(e, "e");
        try {
            Result.Companion companion = Result.Companion;
            newInstance = this.$constructor$inlined.newInstance(new Object[0]);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            m1934constructorimpl = Result.m1934constructorimpl(ResultKt.createFailure(th));
        }
        if (newInstance == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Throwable");
        }
        Throwable th2 = (Throwable) newInstance;
        th2.initCause(e);
        m1934constructorimpl = Result.m1934constructorimpl(th2);
        if (Result.m1938isFailureimpl(m1934constructorimpl)) {
            m1934constructorimpl = null;
        }
        return (Throwable) m1934constructorimpl;
    }
}
