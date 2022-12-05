package kotlinx.coroutines;

import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.SystemPropsKt;
import kotlinx.coroutines.scheduling.DefaultScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: CoroutineContext.kt */
/* loaded from: classes2.dex */
public final class CoroutineContextKt {
    private static final boolean useCoroutinesScheduler;

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0028, code lost:
        if (r0.equals("on") != false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0031, code lost:
        if (r0.equals("") != false) goto L21;
     */
    static {
        boolean z;
        String systemProp = SystemPropsKt.systemProp("kotlinx.coroutines.scheduler");
        if (systemProp != null) {
            int hashCode = systemProp.hashCode();
            if (hashCode != 0) {
                if (hashCode != 3551) {
                    if (hashCode == 109935 && systemProp.equals("off")) {
                        z = false;
                    }
                }
                throw new IllegalStateException(("System property 'kotlinx.coroutines.scheduler' has unrecognized value '" + systemProp + '\'').toString());
            }
            useCoroutinesScheduler = z;
        }
        z = true;
        useCoroutinesScheduler = z;
    }

    @NotNull
    public static final CoroutineDispatcher createDefaultDispatcher() {
        return useCoroutinesScheduler ? DefaultScheduler.INSTANCE : CommonPool.INSTANCE;
    }

    @NotNull
    public static final CoroutineContext newCoroutineContext(@NotNull CoroutineScope newCoroutineContext, @NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(newCoroutineContext, "$this$newCoroutineContext");
        Intrinsics.checkParameterIsNotNull(context, "context");
        CoroutineContext plus = newCoroutineContext.getCoroutineContext().plus(context);
        CoroutineContext plus2 = DebugKt.getDEBUG() ? plus.plus(new CoroutineId(DebugKt.getCOROUTINE_ID().incrementAndGet())) : plus;
        return (plus == Dispatchers.getDefault() || plus.get(ContinuationInterceptor.Key) != null) ? plus2 : plus2.plus(Dispatchers.getDefault());
    }

    @Nullable
    public static final String getCoroutineName(@NotNull CoroutineContext coroutineName) {
        CoroutineId coroutineId;
        String str;
        Intrinsics.checkParameterIsNotNull(coroutineName, "$this$coroutineName");
        if (DebugKt.getDEBUG() && (coroutineId = (CoroutineId) coroutineName.get(CoroutineId.Key)) != null) {
            CoroutineName coroutineName2 = (CoroutineName) coroutineName.get(CoroutineName.Key);
            if (coroutineName2 == null || (str = coroutineName2.getName()) == null) {
                str = "coroutine";
            }
            return str + '#' + coroutineId.getId();
        }
        return null;
    }
}
