package kotlinx.coroutines.internal;

import kotlin.TypeCastException;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ThreadContextElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ThreadContext.kt */
/* loaded from: classes2.dex */
public final class ThreadContextKt {
    private static final Symbol ZERO = new Symbol("ZERO");
    private static final Function2<Object, CoroutineContext.Element, Object> countAll = ThreadContextKt$countAll$1.INSTANCE;
    private static final Function2<ThreadContextElement<?>, CoroutineContext.Element, ThreadContextElement<?>> findOne = ThreadContextKt$findOne$1.INSTANCE;
    private static final Function2<ThreadState, CoroutineContext.Element, ThreadState> updateState = ThreadContextKt$updateState$1.INSTANCE;
    private static final Function2<ThreadState, CoroutineContext.Element, ThreadState> restoreState = ThreadContextKt$restoreState$1.INSTANCE;

    @NotNull
    public static final Object threadContextElements(@NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Object fold = context.fold(0, countAll);
        if (fold == null) {
            Intrinsics.throwNpe();
        }
        return fold;
    }

    @Nullable
    public static final Object updateThreadContext(@NotNull CoroutineContext context, @Nullable Object obj) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (obj == null) {
            obj = threadContextElements(context);
        }
        if (obj == 0) {
            return ZERO;
        }
        if (obj instanceof Integer) {
            return context.fold(new ThreadState(context, ((Number) obj).intValue()), updateState);
        }
        if (obj == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
        }
        return ((ThreadContextElement) obj).updateThreadContext(context);
    }

    public static final void restoreThreadContext(@NotNull CoroutineContext context, @Nullable Object obj) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (obj == ZERO) {
            return;
        }
        if (obj instanceof ThreadState) {
            ((ThreadState) obj).start();
            context.fold(obj, restoreState);
            return;
        }
        Object fold = context.fold(null, findOne);
        if (fold == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
        }
        ((ThreadContextElement) fold).restoreThreadContext(context, obj);
    }
}
