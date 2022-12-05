package kotlin.collections;

import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Arrays.kt */
/* loaded from: classes2.dex */
public class ArraysKt___ArraysKt extends ArraysKt___ArraysJvmKt {
    public static final <T> boolean contains(@NotNull T[] contains, T t) {
        Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return indexOf(contains, t) >= 0;
    }

    public static final <T> int indexOf(@NotNull T[] indexOf, T t) {
        Intrinsics.checkNotNullParameter(indexOf, "$this$indexOf");
        int i = 0;
        if (t == null) {
            int length = indexOf.length;
            while (i < length) {
                if (indexOf[i] == null) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        int length2 = indexOf.length;
        while (i < length2) {
            if (Intrinsics.areEqual(t, indexOf[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @NotNull
    public static <T> List<T> toMutableList(@NotNull T[] toMutableList) {
        Intrinsics.checkNotNullParameter(toMutableList, "$this$toMutableList");
        return new ArrayList(CollectionsKt__CollectionsKt.asCollection(toMutableList));
    }
}
