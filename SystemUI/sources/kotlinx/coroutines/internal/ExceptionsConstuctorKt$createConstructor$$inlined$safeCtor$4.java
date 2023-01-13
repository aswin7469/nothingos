package kotlinx.coroutines.internal;

import java.lang.reflect.Constructor;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n¨\u0006\u0003"}, mo65043d2 = {"<anonymous>", "", "e", "kotlinx/coroutines/internal/ExceptionsConstuctorKt$safeCtor$1"}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: ExceptionsConstuctor.kt */
public final class ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4 extends Lambda implements Function1<Throwable, Throwable> {
    final /* synthetic */ Constructor $constructor$inlined;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4(Constructor constructor) {
        super(1);
        this.$constructor$inlined = constructor;
    }

    public final Throwable invoke(Throwable th) {
        Object obj;
        try {
            Result.Companion companion = Result.Companion;
            Object newInstance = this.$constructor$inlined.newInstance(new Object[0]);
            if (newInstance != null) {
                Throwable th2 = (Throwable) newInstance;
                th2.initCause(th);
                obj = Result.m3954constructorimpl(th2);
                if (Result.m3960isFailureimpl(obj)) {
                    obj = null;
                }
                return (Throwable) obj;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Throwable");
        } catch (Throwable th3) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.m3954constructorimpl(ResultKt.createFailure(th3));
        }
    }
}
