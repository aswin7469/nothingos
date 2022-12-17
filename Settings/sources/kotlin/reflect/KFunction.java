package kotlin.reflect;

import kotlin.Function;

/* compiled from: KFunction.kt */
public interface KFunction<R> extends KCallable<R>, Function<R> {
    boolean isExternal();

    boolean isInfix();

    boolean isInline();

    boolean isOperator();

    boolean isSuspend();
}
