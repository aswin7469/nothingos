package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Arrays.kt */
/* loaded from: classes2.dex */
public class ArraysKt___ArraysKt extends ArraysKt___ArraysJvmKt {
    public static <T> boolean contains(@NotNull T[] contains, T t) {
        Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return indexOf(contains, t) >= 0;
    }

    public static boolean contains(@NotNull int[] contains, int i) {
        Intrinsics.checkNotNullParameter(contains, "$this$contains");
        return indexOf(contains, i) >= 0;
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

    public static final int indexOf(@NotNull int[] indexOf, int i) {
        Intrinsics.checkNotNullParameter(indexOf, "$this$indexOf");
        int length = indexOf.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (i == indexOf[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static char single(@NotNull char[] single) {
        Intrinsics.checkNotNullParameter(single, "$this$single");
        int length = single.length;
        if (length != 0) {
            if (length == 1) {
                return single[0];
            }
            throw new IllegalArgumentException("Array has more than one element.");
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @Nullable
    public static <T> T singleOrNull(@NotNull T[] singleOrNull) {
        Intrinsics.checkNotNullParameter(singleOrNull, "$this$singleOrNull");
        if (singleOrNull.length == 1) {
            return singleOrNull[0];
        }
        return null;
    }

    @NotNull
    public static <T> List<T> drop(@NotNull T[] drop, int i) {
        int coerceAtLeast;
        Intrinsics.checkNotNullParameter(drop, "$this$drop");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        }
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(drop.length - i, 0);
        return takeLast(drop, coerceAtLeast);
    }

    @NotNull
    public static <T> List<T> filterNotNull(@NotNull T[] filterNotNull) {
        Intrinsics.checkNotNullParameter(filterNotNull, "$this$filterNotNull");
        return (List) filterNotNullTo(filterNotNull, new ArrayList());
    }

    @NotNull
    public static final <C extends Collection<? super T>, T> C filterNotNullTo(@NotNull T[] filterNotNullTo, @NotNull C destination) {
        Intrinsics.checkNotNullParameter(filterNotNullTo, "$this$filterNotNullTo");
        Intrinsics.checkNotNullParameter(destination, "destination");
        for (T t : filterNotNullTo) {
            if (t != null) {
                destination.add(t);
            }
        }
        return destination;
    }

    @NotNull
    public static final <T> List<T> takeLast(@NotNull T[] takeLast, int i) {
        List<T> listOf;
        List<T> emptyList;
        Intrinsics.checkNotNullParameter(takeLast, "$this$takeLast");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        } else if (i == 0) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            return emptyList;
        } else {
            int length = takeLast.length;
            if (i >= length) {
                return toList(takeLast);
            }
            if (i == 1) {
                listOf = CollectionsKt__CollectionsJVMKt.listOf(takeLast[length - 1]);
                return listOf;
            }
            ArrayList arrayList = new ArrayList(i);
            for (int i2 = length - i; i2 < length; i2++) {
                arrayList.add(takeLast[i2]);
            }
            return arrayList;
        }
    }

    @NotNull
    public static final <T> T[] sortedArrayWith(@NotNull T[] sortedArrayWith, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(sortedArrayWith, "$this$sortedArrayWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (sortedArrayWith.length == 0) {
            return sortedArrayWith;
        }
        T[] tArr = (T[]) Arrays.copyOf(sortedArrayWith, sortedArrayWith.length);
        Intrinsics.checkNotNullExpressionValue(tArr, "java.util.Arrays.copyOf(this, size)");
        ArraysKt___ArraysJvmKt.sortWith(tArr, comparator);
        return tArr;
    }

    @NotNull
    public static <T> List<T> sortedWith(@NotNull T[] sortedWith, @NotNull Comparator<? super T> comparator) {
        List<T> asList;
        Intrinsics.checkNotNullParameter(sortedWith, "$this$sortedWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        asList = ArraysKt___ArraysJvmKt.asList(sortedArrayWith(sortedWith, comparator));
        return asList;
    }

    public static final <T> int getLastIndex(@NotNull T[] lastIndex) {
        Intrinsics.checkNotNullParameter(lastIndex, "$this$lastIndex");
        return lastIndex.length - 1;
    }

    @NotNull
    public static final <T, C extends Collection<? super T>> C toCollection(@NotNull T[] toCollection, @NotNull C destination) {
        Intrinsics.checkNotNullParameter(toCollection, "$this$toCollection");
        Intrinsics.checkNotNullParameter(destination, "destination");
        for (T t : toCollection) {
            destination.add(t);
        }
        return destination;
    }

    @NotNull
    public static final <T> List<T> toList(@NotNull T[] toList) {
        List<T> emptyList;
        List<T> listOf;
        List<T> mutableList;
        Intrinsics.checkNotNullParameter(toList, "$this$toList");
        int length = toList.length;
        if (length == 0) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            return emptyList;
        } else if (length == 1) {
            listOf = CollectionsKt__CollectionsJVMKt.listOf(toList[0]);
            return listOf;
        } else {
            mutableList = toMutableList(toList);
            return mutableList;
        }
    }

    @NotNull
    public static List<Integer> toList(@NotNull int[] toList) {
        List<Integer> emptyList;
        List<Integer> listOf;
        List<Integer> mutableList;
        Intrinsics.checkNotNullParameter(toList, "$this$toList");
        int length = toList.length;
        if (length == 0) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            return emptyList;
        } else if (length == 1) {
            listOf = CollectionsKt__CollectionsJVMKt.listOf(Integer.valueOf(toList[0]));
            return listOf;
        } else {
            mutableList = toMutableList(toList);
            return mutableList;
        }
    }

    @NotNull
    public static <T> List<T> toMutableList(@NotNull T[] toMutableList) {
        Intrinsics.checkNotNullParameter(toMutableList, "$this$toMutableList");
        return new ArrayList(CollectionsKt__CollectionsKt.asCollection(toMutableList));
    }

    @NotNull
    public static List<Integer> toMutableList(@NotNull int[] toMutableList) {
        Intrinsics.checkNotNullParameter(toMutableList, "$this$toMutableList");
        ArrayList arrayList = new ArrayList(toMutableList.length);
        for (int i : toMutableList) {
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }

    @NotNull
    public static <T> Set<T> toSet(@NotNull T[] toSet) {
        Set<T> emptySet;
        int mapCapacity;
        Intrinsics.checkNotNullParameter(toSet, "$this$toSet");
        int length = toSet.length;
        if (length == 0) {
            emptySet = SetsKt__SetsKt.emptySet();
            return emptySet;
        } else if (length == 1) {
            return SetsKt__SetsJVMKt.setOf(toSet[0]);
        } else {
            mapCapacity = MapsKt__MapsJVMKt.mapCapacity(toSet.length);
            return (Set) toCollection(toSet, new LinkedHashSet(mapCapacity));
        }
    }

    @NotNull
    public static <T> Sequence<T> asSequence(@NotNull final T[] asSequence) {
        Sequence<T> emptySequence;
        Intrinsics.checkNotNullParameter(asSequence, "$this$asSequence");
        if (asSequence.length == 0) {
            emptySequence = SequencesKt__SequencesKt.emptySequence();
            return emptySequence;
        }
        return new Sequence<T>() { // from class: kotlin.collections.ArraysKt___ArraysKt$asSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<T> iterator() {
                return ArrayIteratorKt.iterator(asSequence);
            }
        };
    }
}
