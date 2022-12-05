package kotlin.sequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__AppendableKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: _Sequences.kt */
/* loaded from: classes2.dex */
public class SequencesKt___SequencesKt extends SequencesKt___SequencesJvmKt {
    @NotNull
    public static <T> Sequence<T> filter(@NotNull Sequence<? extends T> filter, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkNotNullParameter(filter, "$this$filter");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        return new FilteringSequence(filter, true, predicate);
    }

    @NotNull
    public static <T> Sequence<T> filterNot(@NotNull Sequence<? extends T> filterNot, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkNotNullParameter(filterNot, "$this$filterNot");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        return new FilteringSequence(filterNot, false, predicate);
    }

    @NotNull
    public static <T> Sequence<T> filterNotNull(@NotNull Sequence<? extends T> filterNotNull) {
        Sequence<T> filterNot;
        Intrinsics.checkNotNullParameter(filterNotNull, "$this$filterNotNull");
        filterNot = filterNot(filterNotNull, SequencesKt___SequencesKt$filterNotNull$1.INSTANCE);
        Objects.requireNonNull(filterNot, "null cannot be cast to non-null type kotlin.sequences.Sequence<T>");
        return filterNot;
    }

    @NotNull
    public static <T> Sequence<T> take(@NotNull Sequence<? extends T> take, int i) {
        Sequence<T> emptySequence;
        Intrinsics.checkNotNullParameter(take, "$this$take");
        if (i >= 0) {
            if (i != 0) {
                return take instanceof DropTakeSequence ? ((DropTakeSequence) take).mo1948take(i) : new TakeSequence(take, i);
            }
            emptySequence = SequencesKt__SequencesKt.emptySequence();
            return emptySequence;
        }
        throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
    }

    @NotNull
    public static <T> Sequence<T> sortedWith(@NotNull final Sequence<? extends T> sortedWith, @NotNull final Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(sortedWith, "$this$sortedWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return new Sequence<T>() { // from class: kotlin.sequences.SequencesKt___SequencesKt$sortedWith$1
            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<T> iterator() {
                List mutableList = SequencesKt___SequencesKt.toMutableList(sortedWith);
                CollectionsKt__MutableCollectionsJVMKt.sortWith(mutableList, comparator);
                return mutableList.iterator();
            }
        };
    }

    @NotNull
    public static final <T, C extends Collection<? super T>> C toCollection(@NotNull Sequence<? extends T> toCollection, @NotNull C destination) {
        Intrinsics.checkNotNullParameter(toCollection, "$this$toCollection");
        Intrinsics.checkNotNullParameter(destination, "destination");
        for (T t : toCollection) {
            destination.add(t);
        }
        return destination;
    }

    @NotNull
    public static <T> List<T> toList(@NotNull Sequence<? extends T> toList) {
        List<T> optimizeReadOnlyList;
        Intrinsics.checkNotNullParameter(toList, "$this$toList");
        optimizeReadOnlyList = CollectionsKt__CollectionsKt.optimizeReadOnlyList(toMutableList(toList));
        return optimizeReadOnlyList;
    }

    @NotNull
    public static final <T> List<T> toMutableList(@NotNull Sequence<? extends T> toMutableList) {
        Intrinsics.checkNotNullParameter(toMutableList, "$this$toMutableList");
        return (List) toCollection(toMutableList, new ArrayList());
    }

    @NotNull
    public static <T, R> Sequence<R> flatMap(@NotNull Sequence<? extends T> flatMap, @NotNull Function1<? super T, ? extends Sequence<? extends R>> transform) {
        Intrinsics.checkNotNullParameter(flatMap, "$this$flatMap");
        Intrinsics.checkNotNullParameter(transform, "transform");
        return new FlatteningSequence(flatMap, transform, SequencesKt___SequencesKt$flatMap$2.INSTANCE);
    }

    @NotNull
    public static <T, R> Sequence<R> map(@NotNull Sequence<? extends T> map, @NotNull Function1<? super T, ? extends R> transform) {
        Intrinsics.checkNotNullParameter(map, "$this$map");
        Intrinsics.checkNotNullParameter(transform, "transform");
        return new TransformingSequence(map, transform);
    }

    @NotNull
    public static <T, R> Sequence<R> mapIndexed(@NotNull Sequence<? extends T> mapIndexed, @NotNull Function2<? super Integer, ? super T, ? extends R> transform) {
        Intrinsics.checkNotNullParameter(mapIndexed, "$this$mapIndexed");
        Intrinsics.checkNotNullParameter(transform, "transform");
        return new TransformingIndexedSequence(mapIndexed, transform);
    }

    @NotNull
    public static <T, R> Sequence<R> mapNotNull(@NotNull Sequence<? extends T> mapNotNull, @NotNull Function1<? super T, ? extends R> transform) {
        Sequence<R> filterNotNull;
        Intrinsics.checkNotNullParameter(mapNotNull, "$this$mapNotNull");
        Intrinsics.checkNotNullParameter(transform, "transform");
        filterNotNull = filterNotNull(new TransformingSequence(mapNotNull, transform));
        return filterNotNull;
    }

    @NotNull
    public static <T> Sequence<T> distinct(@NotNull Sequence<? extends T> distinct) {
        Intrinsics.checkNotNullParameter(distinct, "$this$distinct");
        return distinctBy(distinct, SequencesKt___SequencesKt$distinct$1.INSTANCE);
    }

    @NotNull
    public static final <T, K> Sequence<T> distinctBy(@NotNull Sequence<? extends T> distinctBy, @NotNull Function1<? super T, ? extends K> selector) {
        Intrinsics.checkNotNullParameter(distinctBy, "$this$distinctBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        return new DistinctSequence(distinctBy, selector);
    }

    @NotNull
    public static <T> Sequence<T> plus(@NotNull Sequence<? extends T> plus, @NotNull Sequence<? extends T> elements) {
        Sequence sequenceOf;
        Intrinsics.checkNotNullParameter(plus, "$this$plus");
        Intrinsics.checkNotNullParameter(elements, "elements");
        sequenceOf = SequencesKt__SequencesKt.sequenceOf(plus, elements);
        return SequencesKt__SequencesKt.flatten(sequenceOf);
    }

    @NotNull
    public static <T> Sequence<Pair<T, T>> zipWithNext(@NotNull Sequence<? extends T> zipWithNext) {
        Intrinsics.checkNotNullParameter(zipWithNext, "$this$zipWithNext");
        return zipWithNext(zipWithNext, SequencesKt___SequencesKt$zipWithNext$1.INSTANCE);
    }

    @NotNull
    public static final <T, R> Sequence<R> zipWithNext(@NotNull Sequence<? extends T> zipWithNext, @NotNull Function2<? super T, ? super T, ? extends R> transform) {
        Sequence<R> sequence;
        Intrinsics.checkNotNullParameter(zipWithNext, "$this$zipWithNext");
        Intrinsics.checkNotNullParameter(transform, "transform");
        sequence = SequencesKt__SequenceBuilderKt.sequence(new SequencesKt___SequencesKt$zipWithNext$2(zipWithNext, transform, null));
        return sequence;
    }

    @NotNull
    public static final <T, A extends Appendable> A joinTo(@NotNull Sequence<? extends T> joinTo, @NotNull A buffer, @NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix, int i, @NotNull CharSequence truncated, @Nullable Function1<? super T, ? extends CharSequence> function1) {
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

    public static /* synthetic */ String joinToString$default(Sequence sequence, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
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
        return joinToString(sequence, charSequence, charSequence6, charSequence5, i3, charSequence7, function1);
    }

    @NotNull
    public static final <T> String joinToString(@NotNull Sequence<? extends T> joinToString, @NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix, int i, @NotNull CharSequence truncated, @Nullable Function1<? super T, ? extends CharSequence> function1) {
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
    public static <T> Iterable<T> asIterable(@NotNull Sequence<? extends T> asIterable) {
        Intrinsics.checkNotNullParameter(asIterable, "$this$asIterable");
        return new SequencesKt___SequencesKt$asIterable$$inlined$Iterable$1(asIterable);
    }
}
