package com.android.systemui.util.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010(\n\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\u000b\u0010\u000f\u001a\u00028\u0000¢\u0006\u0002\u0010\u0010J \u0010\u0011\u001a\u00020\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00120\u0014H\bø\u0001\u0000J\u0016\u0010\u0015\u001a\u00028\u00002\u0006\u0010\u0016\u001a\u00020\u0004H\u0002¢\u0006\u0002\u0010\u0017J\u0010\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u000bH\u0002J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00028\u00000\u001bH\u0002R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\tX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e\u0002\u0007\n\u0005\b20\u0001¨\u0006\u001c"}, mo64987d2 = {"Lcom/android/systemui/util/collection/RingBuffer;", "T", "", "maxSize", "", "factory", "Lkotlin/Function0;", "(ILkotlin/jvm/functions/Function0;)V", "buffer", "", "omega", "", "size", "getSize", "()I", "advance", "()Ljava/lang/Object;", "forEach", "", "action", "Lkotlin/Function1;", "get", "index", "(I)Ljava/lang/Object;", "indexOf", "position", "iterator", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RingBuffer.kt */
public final class RingBuffer<T> implements Iterable<T>, KMappedMarker {
    private final List<T> buffer;
    private final Function0<T> factory;
    private final int maxSize;
    private long omega;

    public RingBuffer(int i, Function0<? extends T> function0) {
        Intrinsics.checkNotNullParameter(function0, "factory");
        this.maxSize = i;
        this.factory = function0;
        ArrayList arrayList = new ArrayList(i);
        for (int i2 = 0; i2 < i; i2++) {
            arrayList.add(null);
        }
        this.buffer = arrayList;
    }

    public final int getSize() {
        long j = this.omega;
        int i = this.maxSize;
        return j < ((long) i) ? (int) j : i;
    }

    public final T advance() {
        int indexOf = indexOf(this.omega);
        this.omega++;
        T t = this.buffer.get(indexOf);
        if (t != null) {
            return t;
        }
        T invoke = this.factory.invoke();
        this.buffer.set(indexOf, invoke);
        return invoke;
    }

    public final T get(int i) {
        if (i < 0 || i >= getSize()) {
            throw new IndexOutOfBoundsException("Index " + i + " is out of bounds");
        }
        T t = this.buffer.get(indexOf(Math.max(this.omega, (long) this.maxSize) + ((long) i)));
        Intrinsics.checkNotNull(t);
        return t;
    }

    public final void forEach(Function1<? super T, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "action");
        int size = getSize();
        for (int i = 0; i < size; i++) {
            function1.invoke(get(i));
        }
    }

    public Iterator<T> iterator() {
        return new RingBuffer$iterator$1(this);
    }

    private final int indexOf(long j) {
        return (int) (j % ((long) this.maxSize));
    }
}
