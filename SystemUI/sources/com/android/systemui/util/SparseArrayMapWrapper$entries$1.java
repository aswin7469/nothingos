package com.android.systemui.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mo65042d1 = {"\u0000+\n\u0000\n\u0002\u0010\"\n\u0002\u0010&\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00028\u00000\u00020\u0001J\u001d\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00028\u00000\u0002H\u0002J\"\u0010\n\u001a\u00020\b2\u0018\u0010\u000b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00028\u00000\u00020\fH\u0016J\b\u0010\r\u001a\u00020\bH\u0016J\u001b\u0010\u000e\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00028\u00000\u00020\u000fH\u0002R\u0014\u0010\u0004\u001a\u00020\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"}, mo65043d2 = {"com/android/systemui/util/SparseArrayMapWrapper$entries$1", "", "", "", "size", "getSize", "()I", "contains", "", "element", "containsAll", "elements", "", "isEmpty", "iterator", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SparseArrayUtils.kt */
public final class SparseArrayMapWrapper$entries$1 implements Set<Map.Entry<? extends Integer, ? extends T>>, KMappedMarker {
    final /* synthetic */ SparseArrayMapWrapper<T> this$0;

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean add(Map.Entry<Integer, ? extends T> entry) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(Collection<? extends Map.Entry<? extends Integer, ? extends T>> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    public <T> T[] toArray(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "array");
        return CollectionToArray.toArray(this, tArr);
    }

    SparseArrayMapWrapper$entries$1(SparseArrayMapWrapper<T> sparseArrayMapWrapper) {
        this.this$0 = sparseArrayMapWrapper;
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        return contains((Map.Entry) obj);
    }

    public final /* bridge */ int size() {
        return getSize();
    }

    public int getSize() {
        return this.this$0.size();
    }

    public boolean contains(Map.Entry<Integer, ? extends T> entry) {
        Intrinsics.checkNotNullParameter(entry, "element");
        Object obj = this.this$0.sparseArray.get(entry.getKey().intValue());
        return obj != null && Intrinsics.areEqual(obj, (Object) entry.getValue());
    }

    public boolean containsAll(Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "elements");
        Iterable<Map.Entry> iterable = collection;
        if (((Collection) iterable).isEmpty()) {
            return true;
        }
        for (Map.Entry contains : iterable) {
            if (!contains((Object) contains)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator<Map.Entry<Integer, T>> iterator() {
        return this.this$0.entrySequence.iterator();
    }
}
