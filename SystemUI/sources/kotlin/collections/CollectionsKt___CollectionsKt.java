package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.text.StringsKt__AppendableKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: _Collections.kt */
/* loaded from: classes2.dex */
public class CollectionsKt___CollectionsKt extends CollectionsKt___CollectionsJvmKt {
    public static <T> boolean contains(@NotNull Iterable<? extends T> contains, T t) {
        Intrinsics.checkNotNullParameter(contains, "$this$contains");
        if (contains instanceof Collection) {
            return ((Collection) contains).contains(t);
        }
        return indexOf(contains, t) >= 0;
    }

    public static <T> T elementAt(@NotNull Iterable<? extends T> elementAt, int i) {
        Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        if (elementAt instanceof List) {
            return (T) ((List) elementAt).get(i);
        }
        return (T) elementAtOrElse(elementAt, i, new CollectionsKt___CollectionsKt$elementAt$1(i));
    }

    public static final <T> T elementAtOrElse(@NotNull Iterable<? extends T> elementAtOrElse, int i, @NotNull Function1<? super Integer, ? extends T> defaultValue) {
        int lastIndex;
        Intrinsics.checkNotNullParameter(elementAtOrElse, "$this$elementAtOrElse");
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        if (elementAtOrElse instanceof List) {
            List list = (List) elementAtOrElse;
            if (i >= 0) {
                lastIndex = CollectionsKt__CollectionsKt.getLastIndex(list);
                if (i <= lastIndex) {
                    return (T) list.get(i);
                }
            }
            return defaultValue.mo1949invoke(Integer.valueOf(i));
        } else if (i < 0) {
            return defaultValue.mo1949invoke(Integer.valueOf(i));
        } else {
            int i2 = 0;
            for (T t : elementAtOrElse) {
                int i3 = i2 + 1;
                if (i == i2) {
                    return t;
                }
                i2 = i3;
            }
            return defaultValue.mo1949invoke(Integer.valueOf(i));
        }
    }

    @Nullable
    public static <T> T elementAtOrNull(@NotNull Iterable<? extends T> elementAtOrNull, int i) {
        Intrinsics.checkNotNullParameter(elementAtOrNull, "$this$elementAtOrNull");
        if (elementAtOrNull instanceof List) {
            return (T) getOrNull((List) elementAtOrNull, i);
        }
        if (i < 0) {
            return null;
        }
        int i2 = 0;
        for (T t : elementAtOrNull) {
            int i3 = i2 + 1;
            if (i == i2) {
                return t;
            }
            i2 = i3;
        }
        return null;
    }

    public static final <T> T first(@NotNull Iterable<? extends T> first) {
        Intrinsics.checkNotNullParameter(first, "$this$first");
        if (first instanceof List) {
            return (T) CollectionsKt.first((List<? extends Object>) first);
        }
        Iterator<? extends T> it = first.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }
        return it.next();
    }

    public static <T> T first(@NotNull List<? extends T> first) {
        Intrinsics.checkNotNullParameter(first, "$this$first");
        if (first.isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        return first.get(0);
    }

    @Nullable
    public static <T> T firstOrNull(@NotNull List<? extends T> firstOrNull) {
        Intrinsics.checkNotNullParameter(firstOrNull, "$this$firstOrNull");
        if (firstOrNull.isEmpty()) {
            return null;
        }
        return firstOrNull.get(0);
    }

    @Nullable
    public static final <T> T getOrNull(@NotNull List<? extends T> getOrNull, int i) {
        int lastIndex;
        Intrinsics.checkNotNullParameter(getOrNull, "$this$getOrNull");
        if (i >= 0) {
            lastIndex = CollectionsKt__CollectionsKt.getLastIndex(getOrNull);
            if (i <= lastIndex) {
                return getOrNull.get(i);
            }
        }
        return null;
    }

    public static final <T> int indexOf(@NotNull Iterable<? extends T> indexOf, T t) {
        Intrinsics.checkNotNullParameter(indexOf, "$this$indexOf");
        if (indexOf instanceof List) {
            return ((List) indexOf).indexOf(t);
        }
        int i = 0;
        for (T t2 : indexOf) {
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            if (Intrinsics.areEqual(t, t2)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static final <T> T last(@NotNull Iterable<? extends T> last) {
        Intrinsics.checkNotNullParameter(last, "$this$last");
        if (last instanceof List) {
            return (T) CollectionsKt.last((List<? extends Object>) last);
        }
        Iterator<? extends T> it = last.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }
        T next = it.next();
        while (it.hasNext()) {
            next = it.next();
        }
        return next;
    }

    public static <T> T last(@NotNull List<? extends T> last) {
        int lastIndex;
        Intrinsics.checkNotNullParameter(last, "$this$last");
        if (last.isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        lastIndex = CollectionsKt__CollectionsKt.getLastIndex(last);
        return last.get(lastIndex);
    }

    public static <T> T single(@NotNull Iterable<? extends T> single) {
        Intrinsics.checkNotNullParameter(single, "$this$single");
        if (single instanceof List) {
            return (T) single((List<? extends Object>) single);
        }
        Iterator<? extends T> it = single.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }
        T next = it.next();
        if (it.hasNext()) {
            throw new IllegalArgumentException("Collection has more than one element.");
        }
        return next;
    }

    public static final <T> T single(@NotNull List<? extends T> single) {
        Intrinsics.checkNotNullParameter(single, "$this$single");
        int size = single.size();
        if (size != 0) {
            if (size == 1) {
                return single.get(0);
            }
            throw new IllegalArgumentException("List has more than one element.");
        }
        throw new NoSuchElementException("List is empty.");
    }

    @NotNull
    public static <T> List<T> drop(@NotNull Iterable<? extends T> drop, int i) {
        ArrayList arrayList;
        List<T> optimizeReadOnlyList;
        List<T> listOf;
        List<T> emptyList;
        List<T> list;
        Intrinsics.checkNotNullParameter(drop, "$this$drop");
        int i2 = 0;
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        } else if (i == 0) {
            list = toList(drop);
            return list;
        } else {
            if (drop instanceof Collection) {
                Collection collection = (Collection) drop;
                int size = collection.size() - i;
                if (size <= 0) {
                    emptyList = CollectionsKt__CollectionsKt.emptyList();
                    return emptyList;
                } else if (size == 1) {
                    listOf = CollectionsKt__CollectionsJVMKt.listOf(last(drop));
                    return listOf;
                } else {
                    arrayList = new ArrayList(size);
                    if (drop instanceof List) {
                        if (drop instanceof RandomAccess) {
                            int size2 = collection.size();
                            while (i < size2) {
                                arrayList.add(((List) drop).get(i));
                                i++;
                            }
                        } else {
                            ListIterator listIterator = ((List) drop).listIterator(i);
                            while (listIterator.hasNext()) {
                                arrayList.add(listIterator.next());
                            }
                        }
                        return arrayList;
                    }
                }
            } else {
                arrayList = new ArrayList();
            }
            for (T t : drop) {
                if (i2 >= i) {
                    arrayList.add(t);
                } else {
                    i2++;
                }
            }
            optimizeReadOnlyList = CollectionsKt__CollectionsKt.optimizeReadOnlyList(arrayList);
            return optimizeReadOnlyList;
        }
    }

    @NotNull
    public static <T> List<T> take(@NotNull Iterable<? extends T> take, int i) {
        List<T> optimizeReadOnlyList;
        List<T> listOf;
        List<T> list;
        List<T> emptyList;
        Intrinsics.checkNotNullParameter(take, "$this$take");
        int i2 = 0;
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        } else if (i == 0) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            return emptyList;
        } else {
            if (take instanceof Collection) {
                if (i >= ((Collection) take).size()) {
                    list = toList(take);
                    return list;
                } else if (i == 1) {
                    listOf = CollectionsKt__CollectionsJVMKt.listOf(first(take));
                    return listOf;
                }
            }
            ArrayList arrayList = new ArrayList(i);
            for (T t : take) {
                arrayList.add(t);
                i2++;
                if (i2 == i) {
                    break;
                }
            }
            optimizeReadOnlyList = CollectionsKt__CollectionsKt.optimizeReadOnlyList(arrayList);
            return optimizeReadOnlyList;
        }
    }

    @NotNull
    public static <T> List<T> takeLast(@NotNull List<? extends T> takeLast, int i) {
        List<T> listOf;
        List<T> list;
        List<T> emptyList;
        Intrinsics.checkNotNullParameter(takeLast, "$this$takeLast");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        } else if (i == 0) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            return emptyList;
        } else {
            int size = takeLast.size();
            if (i >= size) {
                list = toList(takeLast);
                return list;
            } else if (i == 1) {
                listOf = CollectionsKt__CollectionsJVMKt.listOf(CollectionsKt.last((List<? extends Object>) takeLast));
                return listOf;
            } else {
                ArrayList arrayList = new ArrayList(i);
                if (takeLast instanceof RandomAccess) {
                    for (int i2 = size - i; i2 < size; i2++) {
                        arrayList.add(takeLast.get(i2));
                    }
                } else {
                    ListIterator<? extends T> listIterator = takeLast.listIterator(size - i);
                    while (listIterator.hasNext()) {
                        arrayList.add(listIterator.next());
                    }
                }
                return arrayList;
            }
        }
    }

    @NotNull
    public static <T> List<T> reversed(@NotNull Iterable<? extends T> reversed) {
        List<T> list;
        Intrinsics.checkNotNullParameter(reversed, "$this$reversed");
        if ((reversed instanceof Collection) && ((Collection) reversed).size() <= 1) {
            list = toList(reversed);
            return list;
        }
        List<T> mutableList = toMutableList(reversed);
        CollectionsKt___CollectionsJvmKt.reverse(mutableList);
        return mutableList;
    }

    @NotNull
    public static <T extends Comparable<? super T>> List<T> sorted(@NotNull Iterable<? extends T> sorted) {
        List<T> asList;
        List<T> list;
        Intrinsics.checkNotNullParameter(sorted, "$this$sorted");
        if (sorted instanceof Collection) {
            Collection collection = (Collection) sorted;
            if (collection.size() <= 1) {
                list = toList(sorted);
                return list;
            }
            Object[] array = collection.toArray(new Comparable[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
            Comparable[] comparableArr = (Comparable[]) array;
            ArraysKt___ArraysJvmKt.sort(comparableArr);
            asList = ArraysKt___ArraysJvmKt.asList(comparableArr);
            return asList;
        }
        List<T> mutableList = toMutableList(sorted);
        CollectionsKt__MutableCollectionsJVMKt.sort(mutableList);
        return mutableList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static <T> List<T> sortedWith(@NotNull Iterable<? extends T> sortedWith, @NotNull Comparator<? super T> comparator) {
        List<T> asList;
        List<T> list;
        Intrinsics.checkNotNullParameter(sortedWith, "$this$sortedWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (sortedWith instanceof Collection) {
            Collection collection = (Collection) sortedWith;
            if (collection.size() <= 1) {
                list = toList(sortedWith);
                return list;
            }
            Object[] array = collection.toArray(new Object[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
            ArraysKt___ArraysJvmKt.sortWith(array, comparator);
            asList = ArraysKt___ArraysJvmKt.asList(array);
            return asList;
        }
        List<T> mutableList = toMutableList(sortedWith);
        CollectionsKt__MutableCollectionsJVMKt.sortWith(mutableList, comparator);
        return mutableList;
    }

    @NotNull
    public static final <T, C extends Collection<? super T>> C toCollection(@NotNull Iterable<? extends T> toCollection, @NotNull C destination) {
        Intrinsics.checkNotNullParameter(toCollection, "$this$toCollection");
        Intrinsics.checkNotNullParameter(destination, "destination");
        for (T t : toCollection) {
            destination.add(t);
        }
        return destination;
    }

    @NotNull
    public static final <T> HashSet<T> toHashSet(@NotNull Iterable<? extends T> toHashSet) {
        int collectionSizeOrDefault;
        int mapCapacity;
        Intrinsics.checkNotNullParameter(toHashSet, "$this$toHashSet");
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(toHashSet, 12);
        mapCapacity = MapsKt__MapsJVMKt.mapCapacity(collectionSizeOrDefault);
        return (HashSet) toCollection(toHashSet, new HashSet(mapCapacity));
    }

    @NotNull
    public static <T> List<T> toList(@NotNull Iterable<? extends T> toList) {
        List<T> optimizeReadOnlyList;
        List<T> emptyList;
        List<T> listOf;
        List<T> mutableList;
        Intrinsics.checkNotNullParameter(toList, "$this$toList");
        if (toList instanceof Collection) {
            Collection collection = (Collection) toList;
            int size = collection.size();
            if (size == 0) {
                emptyList = CollectionsKt__CollectionsKt.emptyList();
                return emptyList;
            } else if (size == 1) {
                listOf = CollectionsKt__CollectionsJVMKt.listOf(toList instanceof List ? ((List) toList).get(0) : toList.iterator().next());
                return listOf;
            } else {
                mutableList = toMutableList((Collection) collection);
                return mutableList;
            }
        }
        optimizeReadOnlyList = CollectionsKt__CollectionsKt.optimizeReadOnlyList(toMutableList(toList));
        return optimizeReadOnlyList;
    }

    @NotNull
    public static final <T> List<T> toMutableList(@NotNull Iterable<? extends T> toMutableList) {
        List<T> mutableList;
        Intrinsics.checkNotNullParameter(toMutableList, "$this$toMutableList");
        if (toMutableList instanceof Collection) {
            mutableList = toMutableList((Collection) ((Collection) toMutableList));
            return mutableList;
        }
        return (List) toCollection(toMutableList, new ArrayList());
    }

    @NotNull
    public static <T> List<T> toMutableList(@NotNull Collection<? extends T> toMutableList) {
        Intrinsics.checkNotNullParameter(toMutableList, "$this$toMutableList");
        return new ArrayList(toMutableList);
    }

    @NotNull
    public static <T> Set<T> toSet(@NotNull Iterable<? extends T> toSet) {
        Set<T> emptySet;
        int mapCapacity;
        Intrinsics.checkNotNullParameter(toSet, "$this$toSet");
        if (toSet instanceof Collection) {
            Collection collection = (Collection) toSet;
            int size = collection.size();
            if (size == 0) {
                emptySet = SetsKt__SetsKt.emptySet();
                return emptySet;
            } else if (size == 1) {
                return SetsKt__SetsJVMKt.setOf(toSet instanceof List ? ((List) toSet).get(0) : toSet.iterator().next());
            } else {
                mapCapacity = MapsKt__MapsJVMKt.mapCapacity(collection.size());
                return (Set) toCollection(toSet, new LinkedHashSet(mapCapacity));
            }
        }
        return SetsKt__SetsKt.optimizeReadOnlySet((Set) toCollection(toSet, new LinkedHashSet()));
    }

    @NotNull
    public static <T> List<T> distinct(@NotNull Iterable<? extends T> distinct) {
        Set mutableSet;
        List<T> list;
        Intrinsics.checkNotNullParameter(distinct, "$this$distinct");
        mutableSet = toMutableSet(distinct);
        list = toList(mutableSet);
        return list;
    }

    @NotNull
    public static <T> Set<T> intersect(@NotNull Iterable<? extends T> intersect, @NotNull Iterable<? extends T> other) {
        Set<T> mutableSet;
        Intrinsics.checkNotNullParameter(intersect, "$this$intersect");
        Intrinsics.checkNotNullParameter(other, "other");
        mutableSet = toMutableSet(intersect);
        CollectionsKt__MutableCollectionsKt.retainAll(mutableSet, other);
        return mutableSet;
    }

    @NotNull
    public static <T> Set<T> subtract(@NotNull Iterable<? extends T> subtract, @NotNull Iterable<? extends T> other) {
        Set<T> mutableSet;
        Intrinsics.checkNotNullParameter(subtract, "$this$subtract");
        Intrinsics.checkNotNullParameter(other, "other");
        mutableSet = toMutableSet(subtract);
        CollectionsKt__MutableCollectionsKt.removeAll(mutableSet, other);
        return mutableSet;
    }

    @NotNull
    public static <T> Set<T> toMutableSet(@NotNull Iterable<? extends T> toMutableSet) {
        Intrinsics.checkNotNullParameter(toMutableSet, "$this$toMutableSet");
        return toMutableSet instanceof Collection ? new LinkedHashSet((Collection) toMutableSet) : (Set) toCollection(toMutableSet, new LinkedHashSet());
    }

    @NotNull
    public static <T> Set<T> union(@NotNull Iterable<? extends T> union, @NotNull Iterable<? extends T> other) {
        Set<T> mutableSet;
        Intrinsics.checkNotNullParameter(union, "$this$union");
        Intrinsics.checkNotNullParameter(other, "other");
        mutableSet = toMutableSet(union);
        CollectionsKt__MutableCollectionsKt.addAll(mutableSet, other);
        return mutableSet;
    }

    @Nullable
    public static <T extends Comparable<? super T>> T min(@NotNull Iterable<? extends T> min) {
        Intrinsics.checkNotNullParameter(min, "$this$min");
        return (T) CollectionsKt.minOrNull(min);
    }

    @Nullable
    public static <T extends Comparable<? super T>> T minOrNull(@NotNull Iterable<? extends T> minOrNull) {
        Intrinsics.checkNotNullParameter(minOrNull, "$this$minOrNull");
        Iterator<? extends T> it = minOrNull.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (next.compareTo(next2) > 0) {
                next = next2;
            }
        }
        return next;
    }

    @NotNull
    public static <T> List<T> minus(@NotNull Iterable<? extends T> minus, T t) {
        int collectionSizeOrDefault;
        Intrinsics.checkNotNullParameter(minus, "$this$minus");
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(minus, 10);
        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
        boolean z = false;
        for (T t2 : minus) {
            boolean z2 = true;
            if (!z && Intrinsics.areEqual(t2, t)) {
                z = true;
                z2 = false;
            }
            if (z2) {
                arrayList.add(t2);
            }
        }
        return arrayList;
    }

    @NotNull
    public static <T> List<T> plus(@NotNull Collection<? extends T> plus, T t) {
        Intrinsics.checkNotNullParameter(plus, "$this$plus");
        ArrayList arrayList = new ArrayList(plus.size() + 1);
        arrayList.addAll(plus);
        arrayList.add(t);
        return arrayList;
    }

    @NotNull
    public static <T> List<T> plus(@NotNull Collection<? extends T> plus, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkNotNullParameter(plus, "$this$plus");
        Intrinsics.checkNotNullParameter(elements, "elements");
        if (elements instanceof Collection) {
            Collection collection = (Collection) elements;
            ArrayList arrayList = new ArrayList(plus.size() + collection.size());
            arrayList.addAll(plus);
            arrayList.addAll(collection);
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList(plus);
        CollectionsKt__MutableCollectionsKt.addAll(arrayList2, elements);
        return arrayList2;
    }

    public static /* synthetic */ Appendable joinTo$default(Iterable iterable, Appendable appendable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
        String str = (i2 & 2) != 0 ? ", " : charSequence;
        CharSequence charSequence5 = "";
        CharSequence charSequence6 = (i2 & 4) != 0 ? charSequence5 : charSequence2;
        if ((i2 & 8) == 0) {
            charSequence5 = charSequence3;
        }
        return joinTo(iterable, appendable, str, charSequence6, charSequence5, (i2 & 16) != 0 ? -1 : i, (i2 & 32) != 0 ? "..." : charSequence4, (i2 & 64) != 0 ? null : function1);
    }

    @NotNull
    public static final <T, A extends Appendable> A joinTo(@NotNull Iterable<? extends T> joinTo, @NotNull A buffer, @NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix, int i, @NotNull CharSequence truncated, @Nullable Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkNotNullParameter(joinTo, "$this$joinTo");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        Intrinsics.checkNotNullParameter(separator, "separator");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        Intrinsics.checkNotNullParameter(postfix, "postfix");
        Intrinsics.checkNotNullParameter(truncated, "truncated");
        buffer.append(prefix);
        int i2 = 0;
        for (T t : joinTo) {
            i2++;
            if (i2 > 1) {
                buffer.append(separator);
            }
            if (i >= 0 && i2 > i) {
                break;
            }
            StringsKt__AppendableKt.appendElement(buffer, t, function1);
        }
        if (i >= 0 && i2 > i) {
            buffer.append(truncated);
        }
        buffer.append(postfix);
        return buffer;
    }

    public static /* synthetic */ String joinToString$default(Iterable iterable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            charSequence = ", ";
        }
        CharSequence charSequence5 = "";
        CharSequence charSequence6 = (i2 & 2) != 0 ? charSequence5 : charSequence2;
        if ((i2 & 4) == 0) {
            charSequence5 = charSequence3;
        }
        if ((i2 & 8) != 0) {
            i = -1;
        }
        int i3 = i;
        if ((i2 & 16) != 0) {
            charSequence4 = "...";
        }
        CharSequence charSequence7 = charSequence4;
        if ((i2 & 32) != 0) {
            function1 = null;
        }
        return joinToString(iterable, charSequence, charSequence6, charSequence5, i3, charSequence7, function1);
    }

    @NotNull
    public static final <T> String joinToString(@NotNull Iterable<? extends T> joinToString, @NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix, int i, @NotNull CharSequence truncated, @Nullable Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkNotNullParameter(joinToString, "$this$joinToString");
        Intrinsics.checkNotNullParameter(separator, "separator");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        Intrinsics.checkNotNullParameter(postfix, "postfix");
        Intrinsics.checkNotNullParameter(truncated, "truncated");
        String sb = ((StringBuilder) joinTo(joinToString, new StringBuilder(), separator, prefix, postfix, i, truncated, function1)).toString();
        Intrinsics.checkNotNullExpressionValue(sb, "joinTo(StringBuilder(), …ed, transform).toString()");
        return sb;
    }

    @NotNull
    public static <T> Sequence<T> asSequence(@NotNull final Iterable<? extends T> asSequence) {
        Intrinsics.checkNotNullParameter(asSequence, "$this$asSequence");
        return new Sequence<T>() { // from class: kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<T> iterator() {
                return asSequence.iterator();
            }
        };
    }
}
