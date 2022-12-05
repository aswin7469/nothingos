package kotlin.sequences;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class SequencesKt extends SequencesKt___SequencesKt {
    @NotNull
    public static /* bridge */ /* synthetic */ <T> Iterable<T> asIterable(@NotNull Sequence<? extends T> sequence) {
        return SequencesKt___SequencesKt.asIterable(sequence);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> asSequence(@NotNull Iterator<? extends T> it) {
        return SequencesKt__SequencesKt.asSequence(it);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> distinct(@NotNull Sequence<? extends T> sequence) {
        return SequencesKt___SequencesKt.distinct(sequence);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> emptySequence() {
        return SequencesKt__SequencesKt.emptySequence();
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> filterNot(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, Boolean> function1) {
        return SequencesKt___SequencesKt.filterNot(sequence, function1);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> filterNotNull(@NotNull Sequence<? extends T> sequence) {
        return SequencesKt___SequencesKt.filterNotNull(sequence);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T, R> Sequence<R> flatMap(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends Sequence<? extends R>> function1) {
        return SequencesKt___SequencesKt.flatMap(sequence, function1);
    }

    public static /* bridge */ /* synthetic */ String joinToString$default(Sequence sequence, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
        return SequencesKt___SequencesKt.joinToString$default(sequence, charSequence, charSequence2, charSequence3, i, charSequence4, function1, i2, obj);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T, R> Sequence<R> map(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends R> function1) {
        return SequencesKt___SequencesKt.map(sequence, function1);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T, R> Sequence<R> mapNotNull(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends R> function1) {
        return SequencesKt___SequencesKt.mapNotNull(sequence, function1);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> sequence(@NotNull Function2<? super SequenceScope<? super T>, ? super Continuation<? super Unit>, ? extends Object> function2) {
        return SequencesKt__SequenceBuilderKt.sequence(function2);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> sequenceOf(@NotNull T... tArr) {
        return SequencesKt__SequencesKt.sequenceOf(tArr);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> sortedWith(@NotNull Sequence<? extends T> sequence, @NotNull Comparator<? super T> comparator) {
        return SequencesKt___SequencesKt.sortedWith(sequence, comparator);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> take(@NotNull Sequence<? extends T> sequence, int i) {
        return SequencesKt___SequencesKt.take(sequence, i);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> toList(@NotNull Sequence<? extends T> sequence) {
        return SequencesKt___SequencesKt.toList(sequence);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<Pair<T, T>> zipWithNext(@NotNull Sequence<? extends T> sequence) {
        return SequencesKt___SequencesKt.zipWithNext(sequence);
    }
}
