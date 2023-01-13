package com.android.systemui.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\t\u0010\u0004\u001a\u00020\u0005H\u0002J\u000e\u0010\u0006\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"com/android/systemui/util/collection/RingBuffer$iterator$1", "", "position", "", "hasNext", "", "next", "()Ljava/lang/Object;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RingBuffer.kt */
public final class RingBuffer$iterator$1 implements Iterator<T>, KMappedMarker {
    private int position;
    final /* synthetic */ RingBuffer<T> this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    RingBuffer$iterator$1(RingBuffer<T> ringBuffer) {
        this.this$0 = ringBuffer;
    }

    public T next() {
        if (this.position < this.this$0.getSize()) {
            T t = this.this$0.get(this.position);
            this.position++;
            return t;
        }
        throw new NoSuchElementException();
    }

    public boolean hasNext() {
        return this.position < this.this$0.getSize();
    }
}
