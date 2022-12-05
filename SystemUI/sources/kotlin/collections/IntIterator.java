package kotlin.collections;

import java.util.Iterator;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
/* compiled from: Iterators.kt */
/* loaded from: classes2.dex */
public abstract class IntIterator implements Iterator<Integer>, KMappedMarker {
    public abstract int nextInt();

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    @NotNull
    /* renamed from: next */
    public final Integer mo1947next() {
        return Integer.valueOf(nextInt());
    }
}
