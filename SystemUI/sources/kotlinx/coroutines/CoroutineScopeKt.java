package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.ContextScope;
import org.jetbrains.annotations.NotNull;
/* compiled from: CoroutineScope.kt */
/* loaded from: classes2.dex */
public final class CoroutineScopeKt {
    @NotNull
    public static final CoroutineScope CoroutineScope(@NotNull CoroutineContext context) {
        CompletableJob Job$default;
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (context.get(Job.Key) == null) {
            Job$default = JobKt__JobKt.Job$default(null, 1, null);
            context = context.plus(Job$default);
        }
        return new ContextScope(context);
    }
}
