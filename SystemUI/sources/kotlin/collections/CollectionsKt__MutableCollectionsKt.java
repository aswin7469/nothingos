package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MutableCollections.kt */
/* loaded from: classes2.dex */
public class CollectionsKt__MutableCollectionsKt extends CollectionsKt__MutableCollectionsJVMKt {
    public static <T> boolean addAll(@NotNull Collection<? super T> addAll, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkNotNullParameter(addAll, "$this$addAll");
        Intrinsics.checkNotNullParameter(elements, "elements");
        if (elements instanceof Collection) {
            return addAll.addAll((Collection) elements);
        }
        boolean z = false;
        Iterator<? extends T> it = elements.iterator();
        while (it.hasNext()) {
            if (addAll.add((T) it.next())) {
                z = true;
            }
        }
        return z;
    }

    public static <T> boolean addAll(@NotNull Collection<? super T> addAll, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkNotNullParameter(addAll, "$this$addAll");
        Intrinsics.checkNotNullParameter(elements, "elements");
        Iterator<? extends T> it = elements.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (addAll.add((T) it.next())) {
                z = true;
            }
        }
        return z;
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> removeAll, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkNotNullParameter(removeAll, "$this$removeAll");
        Intrinsics.checkNotNullParameter(elements, "elements");
        return TypeIntrinsics.asMutableCollection(removeAll).removeAll(CollectionsKt__IterablesKt.convertToSetForSetOperationWith(elements, removeAll));
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> retainAll, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkNotNullParameter(retainAll, "$this$retainAll");
        Intrinsics.checkNotNullParameter(elements, "elements");
        return TypeIntrinsics.asMutableCollection(retainAll).retainAll(CollectionsKt__IterablesKt.convertToSetForSetOperationWith(elements, retainAll));
    }

    public static <T> boolean removeAll(@NotNull Iterable<? extends T> removeAll, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkNotNullParameter(removeAll, "$this$removeAll");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable) removeAll, (Function1) predicate, true);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(Iterable<? extends T> iterable, Function1<? super T, Boolean> function1, boolean z) {
        Iterator<? extends T> it = iterable.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            if (function1.mo1949invoke((T) it.next()).booleanValue() == z) {
                it.remove();
                z2 = true;
            }
        }
        return z2;
    }

    public static <T> boolean removeAll(@NotNull List<T> removeAll, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkNotNullParameter(removeAll, "$this$removeAll");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((List) removeAll, (Function1) predicate, true);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(List<T> list, Function1<? super T, Boolean> function1, boolean z) {
        int i;
        if (!(list instanceof RandomAccess)) {
            Objects.requireNonNull(list, "null cannot be cast to non-null type kotlin.collections.MutableIterable<T>");
            return filterInPlace$CollectionsKt__MutableCollectionsKt(TypeIntrinsics.asMutableIterable(list), function1, z);
        }
        int lastIndex = CollectionsKt.getLastIndex(list);
        if (lastIndex >= 0) {
            int i2 = 0;
            i = 0;
            while (true) {
                T t = list.get(i2);
                if (function1.mo1949invoke(t).booleanValue() != z) {
                    if (i != i2) {
                        list.set(i, t);
                    }
                    i++;
                }
                if (i2 == lastIndex) {
                    break;
                }
                i2++;
            }
        } else {
            i = 0;
        }
        if (i >= list.size()) {
            return false;
        }
        int lastIndex2 = CollectionsKt.getLastIndex(list);
        if (lastIndex2 < i) {
            return true;
        }
        while (true) {
            list.remove(lastIndex2);
            if (lastIndex2 == i) {
                return true;
            }
            lastIndex2--;
        }
    }
}
