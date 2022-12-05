package kotlin.collections;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class CollectionsKt extends CollectionsKt___CollectionsKt {
    public static /* bridge */ /* synthetic */ <T> boolean addAll(@NotNull Collection<? super T> collection, @NotNull Sequence<? extends T> sequence) {
        return CollectionsKt__MutableCollectionsKt.addAll(collection, sequence);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> asSequence(@NotNull Iterable<? extends T> iterable) {
        return CollectionsKt___CollectionsKt.asSequence(iterable);
    }

    public static /* bridge */ /* synthetic */ <T> int collectionSizeOrDefault(@NotNull Iterable<? extends T> iterable, int i) {
        return CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, i);
    }

    public static /* bridge */ /* synthetic */ <T> boolean contains(@NotNull Iterable<? extends T> iterable, T t) {
        return CollectionsKt___CollectionsKt.contains(iterable, t);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> distinct(@NotNull Iterable<? extends T> iterable) {
        return CollectionsKt___CollectionsKt.distinct(iterable);
    }

    public static /* bridge */ /* synthetic */ <T> T elementAt(@NotNull Iterable<? extends T> iterable, int i) {
        return (T) CollectionsKt___CollectionsKt.elementAt(iterable, i);
    }

    @Nullable
    public static /* bridge */ /* synthetic */ <T> T elementAtOrNull(@NotNull Iterable<? extends T> iterable, int i) {
        return (T) CollectionsKt___CollectionsKt.elementAtOrNull(iterable, i);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> emptyList() {
        return CollectionsKt__CollectionsKt.emptyList();
    }

    public static /* bridge */ /* synthetic */ <T> T first(@NotNull List<? extends T> list) {
        return (T) CollectionsKt___CollectionsKt.first((List<? extends Object>) list);
    }

    @Nullable
    public static /* bridge */ /* synthetic */ <T> T firstOrNull(@NotNull List<? extends T> list) {
        return (T) CollectionsKt___CollectionsKt.firstOrNull(list);
    }

    public static /* bridge */ /* synthetic */ <T> int getLastIndex(@NotNull List<? extends T> list) {
        return CollectionsKt__CollectionsKt.getLastIndex(list);
    }

    public static /* bridge */ /* synthetic */ Appendable joinTo$default(Iterable iterable, Appendable appendable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
        return CollectionsKt___CollectionsKt.joinTo$default(iterable, appendable, charSequence, charSequence2, charSequence3, i, charSequence4, function1, i2, obj);
    }

    public static /* bridge */ /* synthetic */ String joinToString$default(Iterable iterable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
        return CollectionsKt___CollectionsKt.joinToString$default(iterable, charSequence, charSequence2, charSequence3, i, charSequence4, function1, i2, obj);
    }

    public static /* bridge */ /* synthetic */ <T> T last(@NotNull List<? extends T> list) {
        return (T) CollectionsKt___CollectionsKt.last((List<? extends Object>) list);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> listOf(T t) {
        return CollectionsKt__CollectionsJVMKt.listOf(t);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> listOf(@NotNull T... tArr) {
        return CollectionsKt__CollectionsKt.listOf((Object[]) tArr);
    }

    @Nullable
    public static /* bridge */ /* synthetic */ <T extends Comparable<? super T>> T min(@NotNull Iterable<? extends T> iterable) {
        return (T) CollectionsKt___CollectionsKt.min(iterable);
    }

    @Nullable
    public static /* bridge */ /* synthetic */ <T extends Comparable<? super T>> T minOrNull(@NotNull Iterable<? extends T> iterable) {
        return (T) CollectionsKt___CollectionsKt.minOrNull(iterable);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> mutableListOf(@NotNull T... tArr) {
        return CollectionsKt__CollectionsKt.mutableListOf(tArr);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> plus(@NotNull Collection<? extends T> collection, @NotNull Iterable<? extends T> iterable) {
        return CollectionsKt___CollectionsKt.plus((Collection) collection, (Iterable) iterable);
    }

    public static /* bridge */ /* synthetic */ <T> T single(@NotNull Iterable<? extends T> iterable) {
        return (T) CollectionsKt___CollectionsKt.single(iterable);
    }

    public static /* bridge */ /* synthetic */ void throwIndexOverflow() {
        CollectionsKt__CollectionsKt.throwIndexOverflow();
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> toList(@NotNull Iterable<? extends T> iterable) {
        return CollectionsKt___CollectionsKt.toList(iterable);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Set<T> toSet(@NotNull Iterable<? extends T> iterable) {
        return CollectionsKt___CollectionsKt.toSet(iterable);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Set<T> union(@NotNull Iterable<? extends T> iterable, @NotNull Iterable<? extends T> iterable2) {
        return CollectionsKt___CollectionsKt.union(iterable, iterable2);
    }
}
