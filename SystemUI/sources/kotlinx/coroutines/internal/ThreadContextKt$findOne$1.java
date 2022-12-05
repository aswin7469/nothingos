package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.ThreadContextElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ThreadContext.kt */
/* loaded from: classes2.dex */
final class ThreadContextKt$findOne$1 extends Lambda implements Function2<ThreadContextElement<?>, CoroutineContext.Element, ThreadContextElement<?>> {
    public static final ThreadContextKt$findOne$1 INSTANCE = new ThreadContextKt$findOne$1();

    ThreadContextKt$findOne$1() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final ThreadContextElement<?> mo1950invoke(@Nullable ThreadContextElement<?> threadContextElement, @NotNull CoroutineContext.Element element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (threadContextElement != null) {
            return threadContextElement;
        }
        if (!(element instanceof ThreadContextElement)) {
            element = null;
        }
        return (ThreadContextElement) element;
    }
}
