package kotlinx.coroutines;

import java.util.Iterator;
import java.util.List;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: CoroutineExceptionHandlerImpl.kt */
/* loaded from: classes2.dex */
public final class CoroutineExceptionHandlerImplKt {
    private static final List<CoroutineExceptionHandler> handlers;

    static {
        Sequence asSequence;
        List<CoroutineExceptionHandler> list;
        Iterator m = CoroutineExceptionHandlerImplKt$$ExternalSyntheticServiceLoad0.m();
        Intrinsics.checkExpressionValueIsNotNull(m, "ServiceLoader.load(\n    ….classLoader\n).iterator()");
        asSequence = SequencesKt__SequencesKt.asSequence(m);
        list = SequencesKt___SequencesKt.toList(asSequence);
        handlers = list;
    }

    public static final void handleCoroutineExceptionImpl(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        for (CoroutineExceptionHandler coroutineExceptionHandler : handlers) {
            try {
                coroutineExceptionHandler.handleException(context, exception);
            } catch (Throwable th) {
                Thread currentThread = Thread.currentThread();
                Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, CoroutineExceptionHandlerKt.handlerException(exception, th));
            }
        }
        Thread currentThread2 = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread2, "currentThread");
        currentThread2.getUncaughtExceptionHandler().uncaughtException(currentThread2, exception);
    }
}
