package sun.util.locale;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class LocaleObjectCache<K, V> {
    private ConcurrentMap<K, CacheEntry<K, V>> map;
    private ReferenceQueue<V> queue;

    /* access modifiers changed from: protected */
    public abstract V createObject(K k);

    /* access modifiers changed from: protected */
    public K normalizeKey(K k) {
        return k;
    }

    public LocaleObjectCache() {
        this(16, 0.75f, 16);
    }

    public LocaleObjectCache(int i, float f, int i2) {
        this.queue = new ReferenceQueue<>();
        this.map = new ConcurrentHashMap(i, f, i2);
    }

    public V get(K k) {
        cleanStaleEntries();
        CacheEntry cacheEntry = this.map.get(k);
        V v = cacheEntry != null ? cacheEntry.get() : null;
        if (v != null) {
            return v;
        }
        V createObject = createObject(k);
        Object normalizeKey = normalizeKey(k);
        if (normalizeKey == null || createObject == null) {
            return null;
        }
        CacheEntry cacheEntry2 = new CacheEntry(normalizeKey, createObject, this.queue);
        CacheEntry putIfAbsent = this.map.putIfAbsent(normalizeKey, cacheEntry2);
        if (putIfAbsent == null) {
            return createObject;
        }
        V v2 = putIfAbsent.get();
        if (v2 != null) {
            return v2;
        }
        this.map.put(normalizeKey, cacheEntry2);
        return createObject;
    }

    /* access modifiers changed from: protected */
    public V put(K k, V v) {
        CacheEntry put = this.map.put(k, new CacheEntry(k, v, this.queue));
        if (put == null) {
            return null;
        }
        return put.get();
    }

    public void cleanStaleEntries() {
        while (true) {
            CacheEntry cacheEntry = (CacheEntry) this.queue.poll();
            if (cacheEntry != null) {
                this.map.remove(cacheEntry.getKey());
            } else {
                return;
            }
        }
    }

    private static class CacheEntry<K, V> extends SoftReference<V> {
        private K key;

        CacheEntry(K k, V v, ReferenceQueue<V> referenceQueue) {
            super(v, referenceQueue);
            this.key = k;
        }

        /* access modifiers changed from: package-private */
        public K getKey() {
            return this.key;
        }
    }
}
