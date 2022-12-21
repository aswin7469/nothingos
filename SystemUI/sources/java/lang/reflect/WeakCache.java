package java.lang.reflect;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

final class WeakCache<K, P, V> {
    private final ConcurrentMap<Object, ConcurrentMap<Object, Supplier<V>>> map = new ConcurrentHashMap();
    private final ReferenceQueue<K> refQueue = new ReferenceQueue<>();
    /* access modifiers changed from: private */
    public final ConcurrentMap<Supplier<V>, Boolean> reverseMap = new ConcurrentHashMap();
    private final BiFunction<K, P, ?> subKeyFactory;
    /* access modifiers changed from: private */
    public final BiFunction<K, P, V> valueFactory;

    private interface Value<V> extends Supplier<V> {
    }

    public WeakCache(BiFunction<K, P, ?> biFunction, BiFunction<K, P, V> biFunction2) {
        this.subKeyFactory = (BiFunction) Objects.requireNonNull(biFunction);
        this.valueFactory = (BiFunction) Objects.requireNonNull(biFunction2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0016, code lost:
        r1 = r10.map;
        r2 = new java.util.concurrent.ConcurrentHashMap();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public V get(K r11, P r12) {
        /*
            r10 = this;
            java.util.Objects.requireNonNull(r12)
            r10.expungeStaleEntries()
            java.lang.ref.ReferenceQueue<K> r0 = r10.refQueue
            java.lang.Object r0 = java.lang.reflect.WeakCache.CacheKey.valueOf(r11, r0)
            java.util.concurrent.ConcurrentMap<java.lang.Object, java.util.concurrent.ConcurrentMap<java.lang.Object, java.util.function.Supplier<V>>> r1 = r10.map
            java.lang.Object r1 = r1.get(r0)
            java.util.concurrent.ConcurrentMap r1 = (java.util.concurrent.ConcurrentMap) r1
            if (r1 != 0) goto L_0x0028
            java.util.concurrent.ConcurrentMap<java.lang.Object, java.util.concurrent.ConcurrentMap<java.lang.Object, java.util.function.Supplier<V>>> r1 = r10.map
            java.util.concurrent.ConcurrentHashMap r2 = new java.util.concurrent.ConcurrentHashMap
            r2.<init>()
            java.lang.Object r0 = r1.putIfAbsent(r0, r2)
            r1 = r0
            java.util.concurrent.ConcurrentMap r1 = (java.util.concurrent.ConcurrentMap) r1
            if (r1 == 0) goto L_0x0027
            goto L_0x0028
        L_0x0027:
            r1 = r2
        L_0x0028:
            java.util.function.BiFunction<K, P, ?> r0 = r10.subKeyFactory
            java.lang.Object r0 = r0.apply(r11, r12)
            java.lang.Object r0 = java.util.Objects.requireNonNull(r0)
            java.lang.Object r2 = r1.get(r0)
            java.util.function.Supplier r2 = (java.util.function.Supplier) r2
            r3 = 0
        L_0x0039:
            if (r2 == 0) goto L_0x0042
            java.lang.Object r4 = r2.get()
            if (r4 == 0) goto L_0x0042
            return r4
        L_0x0042:
            if (r3 != 0) goto L_0x004f
            java.lang.reflect.WeakCache$Factory r9 = new java.lang.reflect.WeakCache$Factory
            r3 = r9
            r4 = r10
            r5 = r11
            r6 = r12
            r7 = r0
            r8 = r1
            r3.<init>(r5, r6, r7, r8)
        L_0x004f:
            if (r2 != 0) goto L_0x005a
            java.lang.Object r2 = r1.putIfAbsent(r0, r3)
            java.util.function.Supplier r2 = (java.util.function.Supplier) r2
            if (r2 != 0) goto L_0x0039
            goto L_0x0060
        L_0x005a:
            boolean r2 = r1.replace(r0, r2, r3)
            if (r2 == 0) goto L_0x0062
        L_0x0060:
            r2 = r3
            goto L_0x0039
        L_0x0062:
            java.lang.Object r2 = r1.get(r0)
            java.util.function.Supplier r2 = (java.util.function.Supplier) r2
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.reflect.WeakCache.get(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public boolean containsValue(V v) {
        Objects.requireNonNull(v);
        expungeStaleEntries();
        return this.reverseMap.containsKey(new LookupValue(v));
    }

    public int size() {
        expungeStaleEntries();
        return this.reverseMap.size();
    }

    private void expungeStaleEntries() {
        while (true) {
            CacheKey cacheKey = (CacheKey) this.refQueue.poll();
            if (cacheKey != null) {
                cacheKey.expungeFrom(this.map, this.reverseMap);
            } else {
                return;
            }
        }
    }

    private final class Factory implements Supplier<V> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final K key;
        private final P parameter;
        private final Object subKey;
        private final ConcurrentMap<Object, Supplier<V>> valuesMap;

        static {
            Class<WeakCache> cls = WeakCache.class;
        }

        Factory(K k, P p, Object obj, ConcurrentMap<Object, Supplier<V>> concurrentMap) {
            this.key = k;
            this.parameter = p;
            this.subKey = obj;
            this.valuesMap = concurrentMap;
        }

        public synchronized V get() {
            if (this.valuesMap.get(this.subKey) != this) {
                return null;
            }
            try {
                V requireNonNull = Objects.requireNonNull(WeakCache.this.valueFactory.apply(this.key, this.parameter));
                if (requireNonNull == null) {
                }
                CacheValue cacheValue = new CacheValue(requireNonNull);
                if (this.valuesMap.replace(this.subKey, this, cacheValue)) {
                    WeakCache.this.reverseMap.put(cacheValue, Boolean.TRUE);
                    return requireNonNull;
                }
                throw new AssertionError((Object) "Should not reach here");
            } finally {
                this.valuesMap.remove(this.subKey, this);
            }
        }
    }

    private static final class LookupValue<V> implements Value<V> {
        private final V value;

        LookupValue(V v) {
            this.value = v;
        }

        public V get() {
            return this.value;
        }

        public int hashCode() {
            return System.identityHashCode(this.value);
        }

        public boolean equals(Object obj) {
            return obj == this || ((obj instanceof Value) && this.value == ((Value) obj).get());
        }
    }

    private static final class CacheValue<V> extends WeakReference<V> implements Value<V> {
        private final int hash;

        CacheValue(V v) {
            super(v);
            this.hash = System.identityHashCode(v);
        }

        public int hashCode() {
            return this.hash;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:3:0x0006, code lost:
            r1 = get();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r2) {
            /*
                r1 = this;
                if (r2 == r1) goto L_0x0017
                boolean r0 = r2 instanceof java.lang.reflect.WeakCache.Value
                if (r0 == 0) goto L_0x0015
                java.lang.Object r1 = r1.get()
                if (r1 == 0) goto L_0x0015
                java.lang.reflect.WeakCache$Value r2 = (java.lang.reflect.WeakCache.Value) r2
                java.lang.Object r2 = r2.get()
                if (r1 != r2) goto L_0x0015
                goto L_0x0017
            L_0x0015:
                r1 = 0
                goto L_0x0018
            L_0x0017:
                r1 = 1
            L_0x0018:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.reflect.WeakCache.CacheValue.equals(java.lang.Object):boolean");
        }
    }

    private static final class CacheKey<K> extends WeakReference<K> {
        private static final Object NULL_KEY = new Object();
        private final int hash;

        static <K> Object valueOf(K k, ReferenceQueue<K> referenceQueue) {
            if (k == null) {
                return NULL_KEY;
            }
            return new CacheKey(k, referenceQueue);
        }

        private CacheKey(K k, ReferenceQueue<K> referenceQueue) {
            super(k, referenceQueue);
            this.hash = System.identityHashCode(k);
        }

        public int hashCode() {
            return this.hash;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000e, code lost:
            r2 = get();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r3) {
            /*
                r2 = this;
                if (r3 == r2) goto L_0x001f
                if (r3 == 0) goto L_0x001d
                java.lang.Class r0 = r3.getClass()
                java.lang.Class r1 = r2.getClass()
                if (r0 != r1) goto L_0x001d
                java.lang.Object r2 = r2.get()
                if (r2 == 0) goto L_0x001d
                java.lang.reflect.WeakCache$CacheKey r3 = (java.lang.reflect.WeakCache.CacheKey) r3
                java.lang.Object r3 = r3.get()
                if (r2 != r3) goto L_0x001d
                goto L_0x001f
            L_0x001d:
                r2 = 0
                goto L_0x0020
            L_0x001f:
                r2 = 1
            L_0x0020:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.reflect.WeakCache.CacheKey.equals(java.lang.Object):boolean");
        }

        /* access modifiers changed from: package-private */
        public void expungeFrom(ConcurrentMap<?, ? extends ConcurrentMap<?, ?>> concurrentMap, ConcurrentMap<?, Boolean> concurrentMap2) {
            ConcurrentMap concurrentMap3 = (ConcurrentMap) concurrentMap.remove(this);
            if (concurrentMap3 != null) {
                for (Object remove : concurrentMap3.values()) {
                    concurrentMap2.remove(remove);
                }
            }
        }
    }
}
