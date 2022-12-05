package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ThreadContextElement.kt */
/* loaded from: classes2.dex */
public interface ThreadContextElement<S> extends CoroutineContext.Element {

    /* compiled from: ThreadContextElement.kt */
    /* loaded from: classes2.dex */
    public static final class DefaultImpls {
        public static <S, R> R fold(ThreadContextElement<S> threadContextElement, R r, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
            Intrinsics.checkParameterIsNotNull(operation, "operation");
            return (R) CoroutineContext.Element.DefaultImpls.fold(threadContextElement, r, operation);
        }

        @Nullable
        public static <S, E extends CoroutineContext.Element> E get(ThreadContextElement<S> threadContextElement, @NotNull CoroutineContext.Key<E> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return (E) CoroutineContext.Element.DefaultImpls.get(threadContextElement, key);
        }

        @NotNull
        public static <S> CoroutineContext minusKey(ThreadContextElement<S> threadContextElement, @NotNull CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return CoroutineContext.Element.DefaultImpls.minusKey(threadContextElement, key);
        }

        @NotNull
        public static <S> CoroutineContext plus(ThreadContextElement<S> threadContextElement, @NotNull CoroutineContext context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return CoroutineContext.Element.DefaultImpls.plus(threadContextElement, context);
        }
    }

    void restoreThreadContext(@NotNull CoroutineContext coroutineContext, S s);

    S updateThreadContext(@NotNull CoroutineContext coroutineContext);
}
