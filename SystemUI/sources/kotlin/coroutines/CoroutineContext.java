package kotlin.coroutines;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: CoroutineContext.kt */
/* loaded from: classes2.dex */
public interface CoroutineContext {

    /* compiled from: CoroutineContext.kt */
    /* loaded from: classes2.dex */
    public interface Key<E extends Element> {
    }

    <R> R fold(R r, @NotNull Function2<? super R, ? super Element, ? extends R> function2);

    @Nullable
    <E extends Element> E get(@NotNull Key<E> key);

    @NotNull
    CoroutineContext minusKey(@NotNull Key<?> key);

    @NotNull
    CoroutineContext plus(@NotNull CoroutineContext coroutineContext);

    /* compiled from: CoroutineContext.kt */
    /* loaded from: classes2.dex */
    public static final class DefaultImpls {
        @NotNull
        public static CoroutineContext plus(@NotNull CoroutineContext coroutineContext, @NotNull CoroutineContext context) {
            Intrinsics.checkNotNullParameter(context, "context");
            return context == EmptyCoroutineContext.INSTANCE ? coroutineContext : (CoroutineContext) context.fold(coroutineContext, CoroutineContext$plus$1.INSTANCE);
        }
    }

    /* compiled from: CoroutineContext.kt */
    /* loaded from: classes2.dex */
    public interface Element extends CoroutineContext {
        @Override // kotlin.coroutines.CoroutineContext
        @Nullable
        <E extends Element> E get(@NotNull Key<E> key);

        @NotNull
        Key<?> getKey();

        /* compiled from: CoroutineContext.kt */
        /* loaded from: classes2.dex */
        public static final class DefaultImpls {
            @NotNull
            public static CoroutineContext plus(@NotNull Element element, @NotNull CoroutineContext context) {
                Intrinsics.checkNotNullParameter(context, "context");
                return DefaultImpls.plus(element, context);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Nullable
            public static <E extends Element> E get(@NotNull Element element, @NotNull Key<E> key) {
                Intrinsics.checkNotNullParameter(key, "key");
                if (Intrinsics.areEqual(element.getKey(), key)) {
                    return element;
                }
                return null;
            }

            public static <R> R fold(@NotNull Element element, R r, @NotNull Function2<? super R, ? super Element, ? extends R> operation) {
                Intrinsics.checkNotNullParameter(operation, "operation");
                return operation.mo1950invoke(r, element);
            }

            @NotNull
            public static CoroutineContext minusKey(@NotNull Element element, @NotNull Key<?> key) {
                Intrinsics.checkNotNullParameter(key, "key");
                return Intrinsics.areEqual(element.getKey(), key) ? EmptyCoroutineContext.INSTANCE : element;
            }
        }
    }
}
