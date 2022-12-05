package kotlin.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CollectionsJVM.kt */
/* loaded from: classes2.dex */
public class CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static <T> List<T> listOf(T t) {
        List<T> singletonList = Collections.singletonList(t);
        Intrinsics.checkNotNullExpressionValue(singletonList, "java.util.Collections.singletonList(element)");
        return singletonList;
    }

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
