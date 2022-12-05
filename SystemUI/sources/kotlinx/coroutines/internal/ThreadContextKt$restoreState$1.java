package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.ThreadContextElement;
import org.jetbrains.annotations.NotNull;
/* compiled from: ThreadContext.kt */
/* loaded from: classes2.dex */
final class ThreadContextKt$restoreState$1 extends Lambda implements Function2<ThreadState, CoroutineContext.Element, ThreadState> {
    public static final ThreadContextKt$restoreState$1 INSTANCE = new ThreadContextKt$restoreState$1();

    ThreadContextKt$restoreState$1() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final ThreadState mo1950invoke(@NotNull ThreadState state, @NotNull CoroutineContext.Element element) {
        Intrinsics.checkParameterIsNotNull(state, "state");
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (element instanceof ThreadContextElement) {
            ((ThreadContextElement) element).restoreThreadContext(state.getContext(), state.take());
        }
        return state;
    }
}
