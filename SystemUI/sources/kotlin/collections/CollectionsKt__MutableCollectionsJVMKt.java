package kotlin.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MutableCollectionsJVM.kt */
/* loaded from: classes2.dex */
public class CollectionsKt__MutableCollectionsJVMKt extends CollectionsKt__IteratorsKt {
    public static <T extends Comparable<? super T>> void sort(@NotNull List<T> sort) {
        Intrinsics.checkNotNullParameter(sort, "$this$sort");
        if (sort.size() > 1) {
            Collections.sort(sort);
        }
    }

    public static <T> void sortWith(@NotNull List<T> sortWith, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(sortWith, "$this$sortWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (sortWith.size() > 1) {
            Collections.sort(sortWith, comparator);
        }
    }
}
