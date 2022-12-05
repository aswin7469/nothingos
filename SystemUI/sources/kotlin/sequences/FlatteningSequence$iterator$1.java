package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class FlatteningSequence$iterator$1 implements Iterator<E>, KMappedMarker {
    @Nullable
    private Iterator<? extends E> itemIterator;
    @NotNull
    private final Iterator<T> iterator;
    final /* synthetic */ FlatteningSequence this$0;

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FlatteningSequence$iterator$1(FlatteningSequence flatteningSequence) {
        Sequence sequence;
        this.this$0 = flatteningSequence;
        sequence = flatteningSequence.sequence;
        this.iterator = sequence.iterator();
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [E, java.lang.Object] */
    @Override // java.util.Iterator
    public E next() {
        if (!ensureItemIterator()) {
            throw new NoSuchElementException();
        }
        Iterator<? extends E> it = this.itemIterator;
        Intrinsics.checkNotNull(it);
        return it.next();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return ensureItemIterator();
    }

    private final boolean ensureItemIterator() {
        Function1 function1;
        Function1 function12;
        Iterator<? extends E> it = this.itemIterator;
        if (it != 0 && !it.hasNext()) {
            this.itemIterator = null;
        }
        while (true) {
            if (this.itemIterator == null) {
                if (this.iterator.hasNext()) {
                    Object next = this.iterator.next();
                    function1 = this.this$0.iterator;
                    function12 = this.this$0.transformer;
                    Iterator<? extends E> it2 = (Iterator) function1.mo1949invoke(function12.mo1949invoke(next));
                    if (it2.hasNext()) {
                        this.itemIterator = it2;
                        break;
                    }
                } else {
                    return false;
                }
            } else {
                break;
            }
        }
        return true;
    }
}
