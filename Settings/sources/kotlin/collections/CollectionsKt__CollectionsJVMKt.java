package kotlin.collections;

import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: CollectionsJVM.kt */
/* loaded from: classes2.dex */
class CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static final <T> Object[] copyToArrayOfAny(@NotNull T[] copyToArrayOfAny, boolean z) {
        Intrinsics.checkNotNullParameter(copyToArrayOfAny, "$this$copyToArrayOfAny");
        if (!z || !Intrinsics.areEqual(copyToArrayOfAny.getClass(), Object[].class)) {
            Object[] copyOf = Arrays.copyOf(copyToArrayOfAny, copyToArrayOfAny.length, Object[].class);
            Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(… Array<Any?>::class.java)");
            return copyOf;
        }
        return copyToArrayOfAny;
    }
}
