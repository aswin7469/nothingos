package kotlinx.coroutines;

import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
import kotlinx.coroutines.ThreadContextElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: CoroutineContext.kt */
/* loaded from: classes2.dex */
public final class CoroutineId extends AbstractCoroutineContextElement implements ThreadContextElement<String> {
    public static final Key Key = new Key(null);
    private final long id;

    public boolean equals(@Nullable Object obj) {
        if (this != obj) {
            if (obj instanceof CoroutineId) {
                if (this.id == ((CoroutineId) obj).id) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        long j = this.id;
        return (int) (j ^ (j >>> 32));
    }

    @Override // kotlin.coroutines.AbstractCoroutineContextElement, kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return (R) ThreadContextElement.DefaultImpls.fold(this, r, operation);
    }

    @Override // kotlin.coroutines.AbstractCoroutineContextElement, kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    @Nullable
    public <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return (E) ThreadContextElement.DefaultImpls.get(this, key);
    }

    @Override // kotlin.coroutines.AbstractCoroutineContextElement, kotlin.coroutines.CoroutineContext
    @NotNull
    public CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return ThreadContextElement.DefaultImpls.minusKey(this, key);
    }

    @Override // kotlin.coroutines.AbstractCoroutineContextElement, kotlin.coroutines.CoroutineContext
    @NotNull
    public CoroutineContext plus(@NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return ThreadContextElement.DefaultImpls.plus(this, context);
    }

    public final long getId() {
        return this.id;
    }

    /* compiled from: CoroutineContext.kt */
    /* loaded from: classes2.dex */
    public static final class Key implements CoroutineContext.Key<CoroutineId> {
        private Key() {
        }

        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public CoroutineId(long j) {
        super(Key);
        this.id = j;
    }

    @NotNull
    public String toString() {
        return "CoroutineId(" + this.id + ')';
    }

    @Override // kotlinx.coroutines.ThreadContextElement
    @NotNull
    public String updateThreadContext(@NotNull CoroutineContext context) {
        String str;
        int lastIndexOf$default;
        Intrinsics.checkParameterIsNotNull(context, "context");
        CoroutineName coroutineName = (CoroutineName) context.get(CoroutineName.Key);
        if (coroutineName == null || (str = coroutineName.getName()) == null) {
            str = "coroutine";
        }
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
        String oldName = currentThread.getName();
        Intrinsics.checkExpressionValueIsNotNull(oldName, "oldName");
        lastIndexOf$default = StringsKt__StringsKt.lastIndexOf$default((CharSequence) oldName, " @", 0, false, 6, (Object) null);
        if (lastIndexOf$default < 0) {
            lastIndexOf$default = oldName.length();
        }
        StringBuilder sb = new StringBuilder(str.length() + lastIndexOf$default + 10);
        String substring = oldName.substring(0, lastIndexOf$default);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        sb.append(substring);
        sb.append(" @");
        sb.append(str);
        sb.append('#');
        sb.append(this.id);
        String sb2 = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(sb2, "StringBuilder(capacity).…builderAction).toString()");
        currentThread.setName(sb2);
        return oldName;
    }

    @Override // kotlinx.coroutines.ThreadContextElement
    public void restoreThreadContext(@NotNull CoroutineContext context, @NotNull String oldState) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(oldState, "oldState");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        currentThread.setName(oldState);
    }
}
