package com.android.systemui.util;

import android.util.SparseArray;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001\"B\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0003H\u0016J\u0015\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001eJ\u0018\u0010\u001f\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u001b\u001a\u00020\u0003H\u0002¢\u0006\u0002\u0010 J\b\u0010!\u001a\u00020\u001aH\u0016R&\u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00028\u00000\t0\b8VX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\"\u0010\f\u001a\u0016\u0012\u0012\u0012\u0010\u0012\f\u0012\n \u000f*\u0004\u0018\u00018\u00008\u00000\u000e0\rX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00030\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000bR\u0014\u0010\u0012\u001a\u00020\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u00168VX\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018¨\u0006#"}, mo65043d2 = {"Lcom/android/systemui/util/SparseArrayMapWrapper;", "T", "", "", "sparseArray", "Landroid/util/SparseArray;", "(Landroid/util/SparseArray;)V", "entries", "", "", "getEntries", "()Ljava/util/Set;", "entrySequence", "Lkotlin/sequences/Sequence;", "Lcom/android/systemui/util/SparseArrayMapWrapper$Entry;", "kotlin.jvm.PlatformType", "keys", "getKeys", "size", "getSize", "()I", "values", "", "getValues", "()Ljava/util/Collection;", "containsKey", "", "key", "containsValue", "value", "(Ljava/lang/Object;)Z", "get", "(I)Ljava/lang/Object;", "isEmpty", "Entry", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SparseArrayUtils.kt */
final class SparseArrayMapWrapper<T> implements Map<Integer, T>, KMappedMarker {
    /* access modifiers changed from: private */
    public final Sequence<Entry<T>> entrySequence = SequencesKt.sequence(new SparseArrayMapWrapper$entrySequence$1(this, (Continuation<? super SparseArrayMapWrapper$entrySequence$1>) null));
    private final Set<Integer> keys = new SparseArrayMapWrapper$keys$1(this);
    /* access modifiers changed from: private */
    public final SparseArray<T> sparseArray;

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public T compute(Integer num, BiFunction<? super Integer, ? super T, ? extends T> biFunction) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public /* bridge */ /* synthetic */ Object compute(Object obj, BiFunction biFunction) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public T computeIfAbsent(Integer num, Function<? super Integer, ? extends T> function) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public /* bridge */ /* synthetic */ Object computeIfAbsent(Object obj, Function function) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public T computeIfPresent(Integer num, BiFunction<? super Integer, ? super T, ? extends T> biFunction) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public /* bridge */ /* synthetic */ Object computeIfPresent(Object obj, BiFunction biFunction) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public T merge(Integer num, T t, BiFunction<? super T, ? super T, ? extends T> biFunction) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public /* bridge */ /* synthetic */ Object merge(Object obj, Object obj2, BiFunction biFunction) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public T put(int i, T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void putAll(Map<? extends Integer, ? extends T> map) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public T putIfAbsent(Integer num, T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public /* bridge */ /* synthetic */ Object putIfAbsent(Object obj, Object obj2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public T remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public T replace(Integer num, T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public /* bridge */ /* synthetic */ Object replace(Object obj, Object obj2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean replace(Integer num, T t, T t2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public /* bridge */ /* synthetic */ boolean replace(Object obj, Object obj2, Object obj3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void replaceAll(BiFunction<? super Integer, ? super T, ? extends T> biFunction) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public SparseArrayMapWrapper(SparseArray<T> sparseArray2) {
        Intrinsics.checkNotNullParameter(sparseArray2, "sparseArray");
        this.sparseArray = sparseArray2;
    }

    public final /* bridge */ boolean containsKey(Object obj) {
        if (!(obj instanceof Integer)) {
            return false;
        }
        return containsKey(((Number) obj).intValue());
    }

    public final /* bridge */ Set<Map.Entry<Integer, T>> entrySet() {
        return getEntries();
    }

    public final /* bridge */ T get(Object obj) {
        if (!(obj instanceof Integer)) {
            return null;
        }
        return get(((Number) obj).intValue());
    }

    public final /* bridge */ Set<Integer> keySet() {
        return getKeys();
    }

    public final /* bridge */ int size() {
        return getSize();
    }

    public final /* bridge */ Collection<T> values() {
        return getValues();
    }

    @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010&\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u0000*\u0004\b\u0001\u0010\u00012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00028\u0001¢\u0006\u0002\u0010\u0006J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\u000e\u0010\r\u001a\u00028\u0001HÆ\u0003¢\u0006\u0002\u0010\nJ(\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00010\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00028\u0001HÆ\u0001¢\u0006\u0002\u0010\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0014\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\u0005\u001a\u00028\u0001X\u0004¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\n¨\u0006\u0017"}, mo65043d2 = {"Lcom/android/systemui/util/SparseArrayMapWrapper$Entry;", "T", "", "", "key", "value", "(ILjava/lang/Object;)V", "getKey", "()Ljava/lang/Integer;", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "component2", "copy", "(ILjava/lang/Object;)Lcom/android/systemui/util/SparseArrayMapWrapper$Entry;", "equals", "", "other", "", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: SparseArrayUtils.kt */
    private static final class Entry<T> implements Map.Entry<Integer, T>, KMappedMarker {
        private final int key;
        private final T value;

        public static /* synthetic */ Entry copy$default(Entry entry, int i, Object obj, int i2, Object obj2) {
            if ((i2 & 1) != 0) {
                i = entry.getKey().intValue();
            }
            if ((i2 & 2) != 0) {
                obj = entry.getValue();
            }
            return entry.copy(i, obj);
        }

        public final int component1() {
            return getKey().intValue();
        }

        public final T component2() {
            return getValue();
        }

        public final Entry<T> copy(int i, T t) {
            return new Entry<>(i, t);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            return getKey().intValue() == entry.getKey().intValue() && Intrinsics.areEqual(getValue(), entry.getValue());
        }

        public int hashCode() {
            return (getKey().hashCode() * 31) + (getValue() == null ? 0 : getValue().hashCode());
        }

        public T setValue(T t) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public String toString() {
            return "Entry(key=" + getKey().intValue() + ", value=" + getValue() + ')';
        }

        public Entry(int i, T t) {
            this.key = i;
            this.value = t;
        }

        public Integer getKey() {
            return Integer.valueOf(this.key);
        }

        public T getValue() {
            return this.value;
        }
    }

    public Set<Map.Entry<Integer, T>> getEntries() {
        return new SparseArrayMapWrapper$entries$1(this);
    }

    public Set<Integer> getKeys() {
        return this.keys;
    }

    public int getSize() {
        return this.sparseArray.size();
    }

    public Collection<T> getValues() {
        return new SparseArrayMapWrapper$values$1(this);
    }

    public boolean containsKey(int i) {
        return this.sparseArray.contains(i);
    }

    public boolean containsValue(Object obj) {
        return this.sparseArray.indexOfValue(obj) >= 0;
    }

    public T get(int i) {
        return this.sparseArray.get(i);
    }

    public boolean isEmpty() {
        return this.sparseArray.size() == 0;
    }
}
