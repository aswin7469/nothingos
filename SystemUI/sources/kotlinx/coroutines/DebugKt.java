package kotlinx.coroutines;

import java.util.concurrent.atomic.AtomicLong;
import kotlinx.coroutines.internal.SystemPropsKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: Debug.kt */
/* loaded from: classes2.dex */
public final class DebugKt {
    private static final boolean ASSERTIONS_ENABLED = false;
    @NotNull
    private static final AtomicLong COROUTINE_ID;
    private static final boolean DEBUG;
    private static final boolean RECOVER_STACK_TRACES;

    public static final boolean getASSERTIONS_ENABLED() {
        return ASSERTIONS_ENABLED;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0038, code lost:
        if (r0.equals("on") != false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0043, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0041, code lost:
        if (r0.equals("") != false) goto L20;
     */
    static {
        boolean z;
        String systemProp = SystemPropsKt.systemProp("kotlinx.coroutines.debug");
        boolean z2 = true;
        if (systemProp != null) {
            int hashCode = systemProp.hashCode();
            if (hashCode != 0) {
                if (hashCode != 3551) {
                    if (hashCode == 109935) {
                    }
                }
                throw new IllegalStateException(("System property 'kotlinx.coroutines.debug' has unrecognized value '" + systemProp + '\'').toString());
            }
            DEBUG = z;
            if (z || !SystemPropsKt.systemProp("kotlinx.coroutines.stacktrace.recovery", true)) {
                z2 = false;
            }
            RECOVER_STACK_TRACES = z2;
            COROUTINE_ID = new AtomicLong(0L);
        }
        z = false;
        DEBUG = z;
        if (z) {
        }
        z2 = false;
        RECOVER_STACK_TRACES = z2;
        COROUTINE_ID = new AtomicLong(0L);
    }

    public static final boolean getDEBUG() {
        return DEBUG;
    }

    public static final boolean getRECOVER_STACK_TRACES() {
        return RECOVER_STACK_TRACES;
    }

    @NotNull
    public static final AtomicLong getCOROUTINE_ID() {
        return COROUTINE_ID;
    }
}
