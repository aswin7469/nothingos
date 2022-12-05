package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class TransformingSequence<T, R> implements Sequence<R> {
    private final Sequence<T> sequence;
    private final Function1<T, R> transformer;

    /* JADX WARN: Multi-variable type inference failed */
    public TransformingSequence(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends R> transformer) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        Intrinsics.checkNotNullParameter(transformer, "transformer");
        this.sequence = sequence;
        this.transformer = transformer;
    }

    @Override // kotlin.sequences.Sequence
    @NotNull
    public Iterator<R> iterator() {
        return new TransformingSequence$iterator$1(this);
    }

    @NotNull
    public final <E> Sequence<E> flatten$kotlin_stdlib(@NotNull Function1<? super R, ? extends Iterator<? extends E>> iterator) {
        Intrinsics.checkNotNullParameter(iterator, "iterator");
        return new FlatteningSequence(this.sequence, this.transformer, iterator);
    }
}
