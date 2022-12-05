package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: Collections.kt */
/* loaded from: classes2.dex */
public class CollectionsKt__CollectionsKt extends CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static final <T> Collection<T> asCollection(@NotNull T[] asCollection) {
        Intrinsics.checkNotNullParameter(asCollection, "$this$asCollection");
        return new ArrayAsCollection(asCollection, false);
    }

    @NotNull
    public static <T> List<T> emptyList() {
        return EmptyList.INSTANCE;
    }

    @NotNull
    public static <T> List<T> listOf(@NotNull T... elements) {
        List<T> emptyList;
        List<T> asList;
        Intrinsics.checkNotNullParameter(elements, "elements");
        if (elements.length > 0) {
            asList = ArraysKt___ArraysJvmKt.asList(elements);
            return asList;
        }
        emptyList = emptyList();
        return emptyList;
    }

    @NotNull
    public static <T> List<T> mutableListOf(@NotNull T... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return elements.length == 0 ? new ArrayList() : new ArrayList(new ArrayAsCollection(elements, true));
    }

    public static <T> int getLastIndex(@NotNull List<? extends T> lastIndex) {
        Intrinsics.checkNotNullParameter(lastIndex, "$this$lastIndex");
        return lastIndex.size() - 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static <T> List<T> optimizeReadOnlyList(@NotNull List<? extends T> optimizeReadOnlyList) {
        List<T> emptyList;
        List<T> listOf;
        Intrinsics.checkNotNullParameter(optimizeReadOnlyList, "$this$optimizeReadOnlyList");
        int size = optimizeReadOnlyList.size();
        if (size == 0) {
            emptyList = emptyList();
            return emptyList;
        } else if (size != 1) {
            return optimizeReadOnlyList;
        } else {
            listOf = CollectionsKt__CollectionsJVMKt.listOf(optimizeReadOnlyList.get(0));
            return listOf;
        }
    }

    public static /* synthetic */ int binarySearch$default(List list, Comparable comparable, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = list.size();
        }
        return binarySearch(list, comparable, i, i2);
    }

    public static final <T extends Comparable<? super T>> int binarySearch(@NotNull List<? extends T> binarySearch, @Nullable T t, int i, int i2) {
        int compareValues;
        Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        rangeCheck$CollectionsKt__CollectionsKt(binarySearch.size(), i, i2);
        int i3 = i2 - 1;
        while (i <= i3) {
            int i4 = (i + i3) >>> 1;
            compareValues = ComparisonsKt__ComparisonsKt.compareValues(binarySearch.get(i4), t);
            if (compareValues < 0) {
                i = i4 + 1;
            } else if (compareValues <= 0) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i + 1);
    }

    private static final void rangeCheck$CollectionsKt__CollectionsKt(int i, int i2, int i3) {
        if (i2 > i3) {
            throw new IllegalArgumentException("fromIndex (" + i2 + ") is greater than toIndex (" + i3 + ").");
        } else if (i2 < 0) {
            throw new IndexOutOfBoundsException("fromIndex (" + i2 + ") is less than zero.");
        } else if (i3 <= i) {
        } else {
            throw new IndexOutOfBoundsException("toIndex (" + i3 + ") is greater than size (" + i + ").");
        }
    }

    public static void throwIndexOverflow() {
        throw new ArithmeticException("Index overflow has happened.");
    }
}
