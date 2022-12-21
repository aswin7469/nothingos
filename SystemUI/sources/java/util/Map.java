package java.util;

import java.util.ImmutableCollections;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Map<K, V> {
    void clear();

    boolean containsKey(Object obj);

    boolean containsValue(Object obj);

    Set<Entry<K, V>> entrySet();

    boolean equals(Object obj);

    V get(Object obj);

    int hashCode();

    boolean isEmpty();

    Set<K> keySet();

    V put(K k, V v);

    void putAll(Map<? extends K, ? extends V> map);

    V remove(Object obj);

    int size();

    Collection<V> values();

    public interface Entry<K, V> {
        boolean equals(Object obj);

        K getKey();

        V getValue();

        int hashCode();

        V setValue(V v);

        static <K extends Comparable<? super K>, V> Comparator<Entry<K, V>> comparingByKey() {
            return new Map$Entry$$ExternalSyntheticLambda0();
        }

        static <K, V extends Comparable<? super V>> Comparator<Entry<K, V>> comparingByValue() {
            return new Map$Entry$$ExternalSyntheticLambda1();
        }

        static <K, V> Comparator<Entry<K, V>> comparingByKey(Comparator<? super K> comparator) {
            Objects.requireNonNull(comparator);
            return new Map$Entry$$ExternalSyntheticLambda2(comparator);
        }

        static <K, V> Comparator<Entry<K, V>> comparingByValue(Comparator<? super V> comparator) {
            Objects.requireNonNull(comparator);
            return new Map$Entry$$ExternalSyntheticLambda3(comparator);
        }
    }

    V getOrDefault(Object obj, V v) {
        V v2 = get(obj);
        return (v2 != null || containsKey(obj)) ? v2 : v;
    }

    void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        for (Entry entry : entrySet()) {
            try {
                biConsumer.accept(entry.getKey(), entry.getValue());
            } catch (IllegalStateException e) {
                throw new ConcurrentModificationException((Throwable) e);
            }
        }
    }

    void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        for (Entry entry : entrySet()) {
            try {
                try {
                    entry.setValue(biFunction.apply(entry.getKey(), entry.getValue()));
                } catch (IllegalStateException e) {
                    throw new ConcurrentModificationException((Throwable) e);
                }
            } catch (IllegalStateException e2) {
                throw new ConcurrentModificationException((Throwable) e2);
            }
        }
    }

    V putIfAbsent(K k, V v) {
        V v2 = get(k);
        return v2 == null ? put(k, v) : v2;
    }

    boolean remove(Object obj, Object obj2) {
        Object obj3 = get(obj);
        if (!Objects.equals(obj3, obj2)) {
            return false;
        }
        if (obj3 == null && !containsKey(obj)) {
            return false;
        }
        remove(obj);
        return true;
    }

    boolean replace(K k, V v, V v2) {
        Object obj = get(k);
        if (!Objects.equals(obj, v)) {
            return false;
        }
        if (obj == null && !containsKey(k)) {
            return false;
        }
        put(k, v2);
        return true;
    }

    V replace(K k, V v) {
        V v2 = get(k);
        return (v2 != null || containsKey(k)) ? put(k, v) : v2;
    }

    V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
        V apply;
        Objects.requireNonNull(function);
        V v = get(k);
        if (v != null || (apply = function.apply(k)) == null) {
            return v;
        }
        put(k, apply);
        return apply;
    }

    V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Object obj = get(k);
        if (obj != null) {
            V apply = biFunction.apply(k, obj);
            if (apply != null) {
                put(k, apply);
                return apply;
            }
            remove(k);
        }
        return null;
    }

    V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Object obj = get(k);
        V apply = biFunction.apply(k, obj);
        if (apply != null) {
            put(k, apply);
            return apply;
        } else if (obj == null && !containsKey(k)) {
            return null;
        } else {
            remove(k);
            return null;
        }
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [java.util.function.BiFunction<? super V, ? super V, ? extends V>, java.util.function.BiFunction, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    V merge(K r2, V r3, java.util.function.BiFunction<? super V, ? super V, ? extends V> r4) {
        /*
            r1 = this;
            java.util.Objects.requireNonNull(r4)
            java.util.Objects.requireNonNull(r3)
            java.lang.Object r0 = r1.get(r2)
            if (r0 != 0) goto L_0x000d
            goto L_0x0011
        L_0x000d:
            java.lang.Object r3 = r4.apply(r0, r3)
        L_0x0011:
            if (r3 != 0) goto L_0x0017
            r1.remove(r2)
            goto L_0x001a
        L_0x0017:
            r1.put(r2, r3)
        L_0x001a:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Map.merge(java.lang.Object, java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1734of() {
        return ImmutableCollections.Map0.instance();
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1735of(K k, V v) {
        return new ImmutableCollections.Map1(k, v);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1736of(K k, V v, K k2, V v2) {
        return new ImmutableCollections.MapN(k, v, k2, v2);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1737of(K k, V v, K k2, V v2, K k3, V v3) {
        return new ImmutableCollections.MapN(k, v, k2, v2, k3, v3);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1738of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new ImmutableCollections.MapN(k, v, k2, v2, k3, v3, k4, v4);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1739of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new ImmutableCollections.MapN(k, v, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1740of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return new ImmutableCollections.MapN(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1741of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return new ImmutableCollections.MapN(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1742of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return new ImmutableCollections.MapN(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1743of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return new ImmutableCollections.MapN(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    /* renamed from: of */
    static <K, V> Map<K, V> m1744of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return new ImmutableCollections.MapN(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    @SafeVarargs
    static <K, V> Map<K, V> ofEntries(Entry<? extends K, ? extends V>... entryArr) {
        if (entryArr.length == 0) {
            return ImmutableCollections.Map0.instance();
        }
        if (entryArr.length == 1) {
            return new ImmutableCollections.Map1(entryArr[0].getKey(), entryArr[0].getValue());
        }
        Object[] objArr = new Object[(entryArr.length << 1)];
        int i = 0;
        for (Entry<? extends K, ? extends V> entry : entryArr) {
            int i2 = i + 1;
            objArr[i] = entry.getKey();
            i = i2 + 1;
            objArr[i2] = entry.getValue();
        }
        return new ImmutableCollections.MapN(objArr);
    }

    static <K, V> Entry<K, V> entry(K k, V v) {
        return new KeyValueHolder(k, v);
    }

    static <K, V> Map<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableCollections.AbstractImmutableMap) {
            return map;
        }
        return ofEntries((Entry[]) map.entrySet().toArray(new Entry[0]));
    }
}
