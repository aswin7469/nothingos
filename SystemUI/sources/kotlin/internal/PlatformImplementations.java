package kotlin.internal;

import java.lang.reflect.Method;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PlatformImplementations.kt */
/* loaded from: classes2.dex */
public class PlatformImplementations {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PlatformImplementations.kt */
    /* loaded from: classes2.dex */
    public static final class ReflectThrowable {
        @NotNull
        public static final ReflectThrowable INSTANCE = new ReflectThrowable();
        @Nullable
        public static final Method addSuppressed;
        @Nullable
        public static final Method getSuppressed;

        /* JADX WARN: Removed duplicated region for block: B:10:0x0046 A[LOOP:0: B:2:0x0015->B:10:0x0046, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:11:0x004a A[EDGE_INSN: B:11:0x004a->B:12:0x004a ?: BREAK  , SYNTHETIC] */
        static {
            Method method;
            Method it;
            boolean z;
            Method[] throwableMethods = Throwable.class.getMethods();
            Intrinsics.checkNotNullExpressionValue(throwableMethods, "throwableMethods");
            int length = throwableMethods.length;
            int i = 0;
            int i2 = 0;
            while (true) {
                method = null;
                if (i2 >= length) {
                    it = null;
                    break;
                }
                it = throwableMethods[i2];
                Intrinsics.checkNotNullExpressionValue(it, "it");
                if (Intrinsics.areEqual(it.getName(), "addSuppressed")) {
                    Class<?>[] parameterTypes = it.getParameterTypes();
                    Intrinsics.checkNotNullExpressionValue(parameterTypes, "it.parameterTypes");
                    if (Intrinsics.areEqual((Class) ArraysKt.singleOrNull(parameterTypes), Throwable.class)) {
                        z = true;
                        if (!z) {
                            break;
                        }
                        i2++;
                    }
                }
                z = false;
                if (!z) {
                }
            }
            addSuppressed = it;
            int length2 = throwableMethods.length;
            while (true) {
                if (i >= length2) {
                    break;
                }
                Method it2 = throwableMethods[i];
                Intrinsics.checkNotNullExpressionValue(it2, "it");
                if (Intrinsics.areEqual(it2.getName(), "getSuppressed")) {
                    method = it2;
                    break;
                }
                i++;
            }
            getSuppressed = method;
        }

        private ReflectThrowable() {
        }
    }

    public void addSuppressed(@NotNull Throwable cause, @NotNull Throwable exception) {
        Intrinsics.checkNotNullParameter(cause, "cause");
        Intrinsics.checkNotNullParameter(exception, "exception");
        Method method = ReflectThrowable.addSuppressed;
        if (method != null) {
            method.invoke(cause, exception);
        }
    }
}
