package kotlin.coroutines.jvm.internal;

import org.jetbrains.annotations.Nullable;
/* compiled from: CoroutineStackFrame.kt */
/* loaded from: classes2.dex */
public interface CoroutineStackFrame {
    @Nullable
    CoroutineStackFrame getCallerFrame();

    @Nullable
    StackTraceElement getStackTraceElement();
}
