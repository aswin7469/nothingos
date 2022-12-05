package kotlin.sequences;

import java.util.HashSet;
import java.util.Iterator;
import kotlin.collections.AbstractIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
final class DistinctIterator<T, K> extends AbstractIterator<T> {
    private final Function1<T, K> keySelector;
    private final HashSet<K> observed = new HashSet<>();
    private final Iterator<T> source;

    /* JADX WARN: Multi-variable type inference failed */
    public DistinctIterator(@NotNull Iterator<? extends T> source, @NotNull Function1<? super T, ? extends K> keySelector) {
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(keySelector, "keySelector");
        this.source = source;
        this.keySelector = keySelector;
    }

    @Override // kotlin.collections.AbstractIterator
    protected void computeNext() {
        while (this.source.hasNext()) {
            T next = this.source.next();
            if (this.observed.add(this.keySelector.mo1949invoke(next))) {
                setNext(next);
                return;
            }
        }
        done();
    }
}
