package kotlinx.coroutines.internal;

import java.util.ArrayDeque;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
import kotlinx.coroutines.DebugKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: StackTraceRecovery.kt */
/* loaded from: classes2.dex */
public final class StackTraceRecoveryKt {
    private static final String baseContinuationImplClassName;
    private static final String stackTraceRecoveryClassName;

    /* JADX WARN: Multi-variable type inference failed */
    static {
        String str;
        Object m1934constructorimpl;
        String str2 = "kotlin.coroutines.jvm.internal.BaseContinuationImpl";
        try {
            Result.Companion companion = Result.Companion;
            Class<?> cls = Class.forName(str2);
            Intrinsics.checkExpressionValueIsNotNull(cls, "Class.forName(baseContinuationImplClass)");
            str = Result.m1934constructorimpl(cls.getCanonicalName());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            str = Result.m1934constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m1936exceptionOrNullimpl(str) == null) {
            str2 = str;
        }
        baseContinuationImplClassName = str2;
        try {
            Result.Companion companion3 = Result.Companion;
            Intrinsics.checkExpressionValueIsNotNull(StackTraceRecoveryKt.class, "Class.forName(stackTraceRecoveryClass)");
            m1934constructorimpl = Result.m1934constructorimpl(StackTraceRecoveryKt.class.getCanonicalName());
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.Companion;
            m1934constructorimpl = Result.m1934constructorimpl(ResultKt.createFailure(th2));
        }
        if (Result.m1936exceptionOrNullimpl(m1934constructorimpl) != null) {
            m1934constructorimpl = "kotlinx.coroutines.internal.StackTraceRecoveryKt";
        }
        stackTraceRecoveryClassName = (String) m1934constructorimpl;
    }

    @NotNull
    public static final <E extends Throwable> E recoverStackTrace(@NotNull E exception, @NotNull Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return (!DebugKt.getRECOVER_STACK_TRACES() || !(continuation instanceof CoroutineStackFrame)) ? exception : (E) recoverFromStackFrame(exception, (CoroutineStackFrame) continuation);
    }

    private static final <E extends Throwable> E recoverFromStackFrame(E e, CoroutineStackFrame coroutineStackFrame) {
        Pair causeAndStacktrace = causeAndStacktrace(e);
        Throwable th = (Throwable) causeAndStacktrace.component1();
        StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) causeAndStacktrace.component2();
        Throwable tryCopyException = ExceptionsConstuctorKt.tryCopyException(th);
        if (tryCopyException != null) {
            ArrayDeque<StackTraceElement> createStackTrace = createStackTrace(coroutineStackFrame);
            if (createStackTrace.isEmpty()) {
                return e;
            }
            if (th != e) {
                mergeRecoveredTraces(stackTraceElementArr, createStackTrace);
            }
            return (E) createFinalException(th, tryCopyException, createStackTrace);
        }
        return e;
    }

    private static final <E extends Throwable> E createFinalException(E e, E e2, ArrayDeque<StackTraceElement> arrayDeque) {
        arrayDeque.addFirst(artificialFrame("Coroutine boundary"));
        StackTraceElement[] causeTrace = e.getStackTrace();
        Intrinsics.checkExpressionValueIsNotNull(causeTrace, "causeTrace");
        String baseContinuationImplClassName2 = baseContinuationImplClassName;
        Intrinsics.checkExpressionValueIsNotNull(baseContinuationImplClassName2, "baseContinuationImplClassName");
        int frameIndex = frameIndex(causeTrace, baseContinuationImplClassName2);
        int i = 0;
        if (frameIndex == -1) {
            Object[] array = arrayDeque.toArray(new StackTraceElement[0]);
            if (array == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            e2.setStackTrace((StackTraceElement[]) array);
            return e2;
        }
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[arrayDeque.size() + frameIndex];
        for (int i2 = 0; i2 < frameIndex; i2++) {
            stackTraceElementArr[i2] = causeTrace[i2];
        }
        for (StackTraceElement stackTraceElement : arrayDeque) {
            stackTraceElementArr[frameIndex + i] = stackTraceElement;
            i++;
        }
        e2.setStackTrace(stackTraceElementArr);
        return e2;
    }

    private static final <E extends Throwable> Pair<E, StackTraceElement[]> causeAndStacktrace(@NotNull E e) {
        boolean z;
        Throwable cause = e.getCause();
        if (cause != null && Intrinsics.areEqual(cause.getClass(), e.getClass())) {
            StackTraceElement[] currentTrace = e.getStackTrace();
            Intrinsics.checkExpressionValueIsNotNull(currentTrace, "currentTrace");
            int length = currentTrace.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = false;
                    break;
                }
                StackTraceElement it = currentTrace[i];
                Intrinsics.checkExpressionValueIsNotNull(it, "it");
                if (isArtificial(it)) {
                    z = true;
                    break;
                }
                i++;
            }
            if (z) {
                return TuplesKt.to(cause, currentTrace);
            }
            return TuplesKt.to(e, new StackTraceElement[0]);
        }
        return TuplesKt.to(e, new StackTraceElement[0]);
    }

    @NotNull
    public static final <E extends Throwable> E unwrap(@NotNull E exception) {
        E e;
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (DebugKt.getRECOVER_STACK_TRACES() && (e = (E) exception.getCause()) != null) {
            boolean z = true;
            if (!(!Intrinsics.areEqual(e.getClass(), exception.getClass()))) {
                StackTraceElement[] stackTrace = exception.getStackTrace();
                Intrinsics.checkExpressionValueIsNotNull(stackTrace, "exception.stackTrace");
                int length = stackTrace.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = false;
                        break;
                    }
                    StackTraceElement it = stackTrace[i];
                    Intrinsics.checkExpressionValueIsNotNull(it, "it");
                    if (isArtificial(it)) {
                        break;
                    }
                    i++;
                }
                if (z) {
                    return e;
                }
            }
        }
        return exception;
    }

    private static final ArrayDeque<StackTraceElement> createStackTrace(CoroutineStackFrame coroutineStackFrame) {
        ArrayDeque<StackTraceElement> arrayDeque = new ArrayDeque<>();
        StackTraceElement stackTraceElement = coroutineStackFrame.getStackTraceElement();
        if (stackTraceElement != null) {
            arrayDeque.add(stackTraceElement);
        }
        while (true) {
            coroutineStackFrame = coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame != null) {
                StackTraceElement stackTraceElement2 = coroutineStackFrame.getStackTraceElement();
                if (stackTraceElement2 != null) {
                    arrayDeque.add(stackTraceElement2);
                }
            } else {
                return arrayDeque;
            }
        }
    }

    @NotNull
    public static final StackTraceElement artificialFrame(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        return new StackTraceElement("\b\b\b(" + message, "\b", "\b", -1);
    }

    public static final boolean isArtificial(@NotNull StackTraceElement isArtificial) {
        boolean startsWith$default;
        Intrinsics.checkParameterIsNotNull(isArtificial, "$this$isArtificial");
        String className = isArtificial.getClassName();
        Intrinsics.checkExpressionValueIsNotNull(className, "className");
        startsWith$default = StringsKt__StringsJVMKt.startsWith$default(className, "\b\b\b", false, 2, null);
        return startsWith$default;
    }

    private static final boolean elementWiseEquals(@NotNull StackTraceElement stackTraceElement, StackTraceElement stackTraceElement2) {
        return stackTraceElement.getLineNumber() == stackTraceElement2.getLineNumber() && Intrinsics.areEqual(stackTraceElement.getMethodName(), stackTraceElement2.getMethodName()) && Intrinsics.areEqual(stackTraceElement.getFileName(), stackTraceElement2.getFileName()) && Intrinsics.areEqual(stackTraceElement.getClassName(), stackTraceElement2.getClassName());
    }

    private static final int frameIndex(@NotNull StackTraceElement[] stackTraceElementArr, String str) {
        int length = stackTraceElementArr.length;
        for (int i = 0; i < length; i++) {
            if (Intrinsics.areEqual(str, stackTraceElementArr[i].getClassName())) {
                return i;
            }
        }
        return -1;
    }

    private static final void mergeRecoveredTraces(StackTraceElement[] stackTraceElementArr, ArrayDeque<StackTraceElement> arrayDeque) {
        int length = stackTraceElementArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            } else if (isArtificial(stackTraceElementArr[i])) {
                break;
            } else {
                i++;
            }
        }
        int i2 = i + 1;
        int length2 = stackTraceElementArr.length - 1;
        if (length2 >= i2) {
            while (true) {
                StackTraceElement stackTraceElement = stackTraceElementArr[length2];
                StackTraceElement last = arrayDeque.getLast();
                Intrinsics.checkExpressionValueIsNotNull(last, "result.last");
                if (elementWiseEquals(stackTraceElement, last)) {
                    arrayDeque.removeLast();
                }
                arrayDeque.addFirst(stackTraceElementArr[length2]);
                if (length2 == i2) {
                    return;
                }
                length2--;
            }
        }
    }
}
