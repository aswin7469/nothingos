package java.util.concurrent;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface ConcurrentMap<K, V> extends Map<K, V> {
    V putIfAbsent(K k, V v);

    boolean remove(Object obj, Object obj2);

    V replace(K k, V v);

    boolean replace(K k, V v, V v2);

    V getOrDefault(Object obj, V v) {
        V v2 = get(obj);
        return v2 != null ? v2 : v;
    }

    void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        for (Map.Entry entry : entrySet()) {
            try {
                biConsumer.accept(entry.getKey(), entry.getValue());
            } catch (IllegalStateException unused) {
            }
        }
    }

    void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        forEach(new ConcurrentMap$$ExternalSyntheticLambda0(this, biFunction));
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP:0: B:0:0x0000->B:3:0x000e, LOOP_START, MTH_ENTER_BLOCK, PHI: r4 
      PHI: (r4v1 java.lang.Object) = (r4v0 java.lang.Object), (r4v3 java.lang.Object) binds: [B:0:0x0000, B:3:0x000e] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void lambda$replaceAll$0(java.util.concurrent.ConcurrentMap r1, java.util.function.BiFunction r2, java.lang.Object r3, java.lang.Object r4) {
        /*
        L_0x0000:
            java.lang.Object r0 = r2.apply(r3, r4)
            boolean r4 = r1.replace(r3, r4, r0)
            if (r4 != 0) goto L_0x0010
            java.lang.Object r4 = r1.get(r3)
            if (r4 != 0) goto L_0x0000
        L_0x0010:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentMap.lambda$replaceAll$0(java.util.concurrent.ConcurrentMap, java.util.function.BiFunction, java.lang.Object, java.lang.Object):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000f, code lost:
        r0 = putIfAbsent(r2, (r3 = r3.apply(r2)));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    V computeIfAbsent(K r2, java.util.function.Function<? super K, ? extends V> r3) {
        /*
            r1 = this;
            java.util.Objects.requireNonNull(r3)
            java.lang.Object r0 = r1.get(r2)
            if (r0 != 0) goto L_0x0016
            java.lang.Object r3 = r3.apply(r2)
            if (r3 == 0) goto L_0x0016
            java.lang.Object r0 = r1.putIfAbsent(r2, r3)
            if (r0 != 0) goto L_0x0016
            goto L_0x0017
        L_0x0016:
            r3 = r0
        L_0x0017:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentMap.computeIfAbsent(java.lang.Object, java.util.function.Function):java.lang.Object");
    }

    V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        V apply;
        Objects.requireNonNull(biFunction);
        while (true) {
            Object obj = get(k);
            if (obj == null) {
                return null;
            }
            apply = biFunction.apply(k, obj);
            if (apply == null) {
                if (remove(k, obj)) {
                    break;
                }
            } else if (replace(k, obj, apply)) {
                break;
            }
        }
        return apply;
    }

    V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        while (true) {
            Object obj = get(k);
            while (true) {
                V apply = biFunction.apply(k, obj);
                if (apply != null) {
                    if (obj == null) {
                        obj = putIfAbsent(k, apply);
                        if (obj == null) {
                            return apply;
                        }
                    } else if (replace(k, obj, apply)) {
                        return apply;
                    }
                } else if (obj == null || remove(k, obj)) {
                    return null;
                }
            }
        }
    }

    V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v);
        while (true) {
            Object obj = get(k);
            while (obj == null) {
                obj = putIfAbsent(k, v);
                if (obj == null) {
                    return v;
                }
            }
            V apply = biFunction.apply(obj, v);
            if (apply != null) {
                if (replace(k, obj, apply)) {
                    return apply;
                }
            } else if (remove(k, obj)) {
                return null;
            }
        }
    }
}
