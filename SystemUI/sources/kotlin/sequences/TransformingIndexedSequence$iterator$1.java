package kotlin.sequences;

import java.util.Iterator;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class TransformingIndexedSequence$iterator$1 implements Iterator<R>, KMappedMarker {
    private int index;
    @NotNull
    private final Iterator<T> iterator;
    final /* synthetic */ TransformingIndexedSequence this$0;

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransformingIndexedSequence$iterator$1(TransformingIndexedSequence transformingIndexedSequence) {
        Sequence sequence;
        this.this$0 = transformingIndexedSequence;
        sequence = transformingIndexedSequence.sequence;
        this.iterator = sequence.iterator();
    }

    /* JADX WARN: Type inference failed for: r3v3, types: [R, java.lang.Object] */
    @Override // java.util.Iterator
    public R next() {
        Function2 function2;
        function2 = this.this$0.transformer;
        int i = this.index;
        this.index = i + 1;
        if (i < 0) {
            CollectionsKt__CollectionsKt.throwIndexOverflow();
        }
        return function2.mo1950invoke(Integer.valueOf(i), this.iterator.next());
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.iterator.hasNext();
    }
}
