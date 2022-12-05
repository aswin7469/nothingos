package kotlin.sequences;

import java.util.Iterator;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public class SequencesKt__SequencesKt extends SequencesKt__SequencesJVMKt {
    @NotNull
    public static <T> Sequence<T> asSequence(@NotNull final Iterator<? extends T> asSequence) {
        Intrinsics.checkNotNullParameter(asSequence, "$this$asSequence");
        return constrainOnce(new Sequence<T>() { // from class: kotlin.sequences.SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<T> iterator() {
                return asSequence;
            }
        });
    }

    @NotNull
    public static <T> Sequence<T> sequenceOf(@NotNull T... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return elements.length == 0 ? SequencesKt.emptySequence() : ArraysKt.asSequence(elements);
    }

    @NotNull
    public static <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }

    @NotNull
    public static final <T> Sequence<T> flatten(@NotNull Sequence<? extends Sequence<? extends T>> flatten) {
        Intrinsics.checkNotNullParameter(flatten, "$this$flatten");
        return flatten$SequencesKt__SequencesKt(flatten, SequencesKt__SequencesKt$flatten$1.INSTANCE);
    }

    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(Sequence<? extends T> sequence, Function1<? super T, ? extends Iterator<? extends R>> function1) {
        if (sequence instanceof TransformingSequence) {
            return ((TransformingSequence) sequence).flatten$kotlin_stdlib(function1);
        }
        return new FlatteningSequence(sequence, SequencesKt__SequencesKt$flatten$3.INSTANCE, function1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static final <T> Sequence<T> constrainOnce(@NotNull Sequence<? extends T> constrainOnce) {
        Intrinsics.checkNotNullParameter(constrainOnce, "$this$constrainOnce");
        return constrainOnce instanceof ConstrainedOnceSequence ? constrainOnce : new ConstrainedOnceSequence(constrainOnce);
    }
}
