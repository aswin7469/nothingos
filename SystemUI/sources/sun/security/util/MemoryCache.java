package sun.security.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import sun.security.util.Cache;

/* compiled from: Cache */
class MemoryCache<K, V> extends Cache<K, V> {
    private static final boolean DEBUG = false;
    private static final float LOAD_FACTOR = 0.75f;
    private final Map<K, CacheEntry<K, V>> cacheMap;
    private long lifetime;
    private int maxSize;
    private final ReferenceQueue<V> queue;

    /* compiled from: Cache */
    private interface CacheEntry<K, V> {
        K getKey();

        V getValue();

        void invalidate();

        boolean isValid(long j);
    }

    public MemoryCache(boolean z, int i) {
        this(z, i, 0);
    }

    public MemoryCache(boolean z, int i, int i2) {
        this.maxSize = i;
        this.lifetime = (long) (i2 * 1000);
        if (z) {
            this.queue = new ReferenceQueue<>();
        } else {
            this.queue = null;
        }
        this.cacheMap = new LinkedHashMap(((int) (((float) i) / 0.75f)) + 1, 0.75f, true);
    }

    private void emptyQueue() {
        CacheEntry remove;
        if (this.queue != null) {
            this.cacheMap.size();
            while (true) {
                CacheEntry cacheEntry = (CacheEntry) this.queue.poll();
                if (cacheEntry != null) {
                    Object key = cacheEntry.getKey();
                    if (!(key == null || (remove = this.cacheMap.remove(key)) == null || cacheEntry == remove)) {
                        this.cacheMap.put(key, remove);
                    }
                } else {
                    return;
                }
            }
        }
    }

    private void expungeExpiredEntries() {
        emptyQueue();
        if (this.lifetime != 0) {
            long currentTimeMillis = System.currentTimeMillis();
            Iterator<CacheEntry<K, V>> it = this.cacheMap.values().iterator();
            while (it.hasNext()) {
                if (!it.next().isValid(currentTimeMillis)) {
                    it.remove();
                }
            }
        }
    }

    public synchronized int size() {
        expungeExpiredEntries();
        return this.cacheMap.size();
    }

    public synchronized void clear() {
        if (this.queue != null) {
            for (CacheEntry<K, V> invalidate : this.cacheMap.values()) {
                invalidate.invalidate();
            }
            while (this.queue.poll() != null) {
            }
        }
        this.cacheMap.clear();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0060, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void put(K r11, V r12) {
        /*
            r10 = this;
            monitor-enter(r10)
            r10.emptyQueue()     // Catch:{ all -> 0x0061 }
            long r0 = r10.lifetime     // Catch:{ all -> 0x0061 }
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x000e
        L_0x000c:
            r7 = r2
            goto L_0x0016
        L_0x000e:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0061 }
            long r2 = r10.lifetime     // Catch:{ all -> 0x0061 }
            long r2 = r2 + r0
            goto L_0x000c
        L_0x0016:
            java.lang.ref.ReferenceQueue<V> r9 = r10.queue     // Catch:{ all -> 0x0061 }
            r4 = r10
            r5 = r11
            r6 = r12
            sun.security.util.MemoryCache$CacheEntry r12 = r4.newEntry(r5, r6, r7, r9)     // Catch:{ all -> 0x0061 }
            java.util.Map<K, sun.security.util.MemoryCache$CacheEntry<K, V>> r0 = r10.cacheMap     // Catch:{ all -> 0x0061 }
            java.lang.Object r11 = r0.put(r11, r12)     // Catch:{ all -> 0x0061 }
            sun.security.util.MemoryCache$CacheEntry r11 = (sun.security.util.MemoryCache.CacheEntry) r11     // Catch:{ all -> 0x0061 }
            if (r11 == 0) goto L_0x002e
            r11.invalidate()     // Catch:{ all -> 0x0061 }
            monitor-exit(r10)
            return
        L_0x002e:
            int r11 = r10.maxSize     // Catch:{ all -> 0x0061 }
            if (r11 <= 0) goto L_0x005f
            java.util.Map<K, sun.security.util.MemoryCache$CacheEntry<K, V>> r11 = r10.cacheMap     // Catch:{ all -> 0x0061 }
            int r11 = r11.size()     // Catch:{ all -> 0x0061 }
            int r12 = r10.maxSize     // Catch:{ all -> 0x0061 }
            if (r11 <= r12) goto L_0x005f
            r10.expungeExpiredEntries()     // Catch:{ all -> 0x0061 }
            java.util.Map<K, sun.security.util.MemoryCache$CacheEntry<K, V>> r11 = r10.cacheMap     // Catch:{ all -> 0x0061 }
            int r11 = r11.size()     // Catch:{ all -> 0x0061 }
            int r12 = r10.maxSize     // Catch:{ all -> 0x0061 }
            if (r11 <= r12) goto L_0x005f
            java.util.Map<K, sun.security.util.MemoryCache$CacheEntry<K, V>> r11 = r10.cacheMap     // Catch:{ all -> 0x0061 }
            java.util.Collection r11 = r11.values()     // Catch:{ all -> 0x0061 }
            java.util.Iterator r11 = r11.iterator()     // Catch:{ all -> 0x0061 }
            java.lang.Object r12 = r11.next()     // Catch:{ all -> 0x0061 }
            sun.security.util.MemoryCache$CacheEntry r12 = (sun.security.util.MemoryCache.CacheEntry) r12     // Catch:{ all -> 0x0061 }
            r11.remove()     // Catch:{ all -> 0x0061 }
            r12.invalidate()     // Catch:{ all -> 0x0061 }
        L_0x005f:
            monitor-exit(r10)
            return
        L_0x0061:
            r11 = move-exception
            monitor-exit(r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.MemoryCache.put(java.lang.Object, java.lang.Object):void");
    }

    public synchronized V get(Object obj) {
        emptyQueue();
        CacheEntry cacheEntry = this.cacheMap.get(obj);
        if (cacheEntry == null) {
            return null;
        }
        long j = 0;
        if (this.lifetime != 0) {
            j = System.currentTimeMillis();
        }
        if (!cacheEntry.isValid(j)) {
            this.cacheMap.remove(obj);
            return null;
        }
        return cacheEntry.getValue();
    }

    public synchronized void remove(Object obj) {
        emptyQueue();
        CacheEntry remove = this.cacheMap.remove(obj);
        if (remove != null) {
            remove.invalidate();
        }
    }

    public synchronized void setCapacity(int i) {
        expungeExpiredEntries();
        if (i > 0 && this.cacheMap.size() > i) {
            Iterator<CacheEntry<K, V>> it = this.cacheMap.values().iterator();
            for (int size = this.cacheMap.size() - i; size > 0; size--) {
                it.remove();
                it.next().invalidate();
            }
        }
        if (i <= 0) {
            i = 0;
        }
        this.maxSize = i;
    }

    public synchronized void setTimeout(int i) {
        emptyQueue();
        this.lifetime = i > 0 ? ((long) i) * 1000 : 0;
    }

    public synchronized void accept(Cache.CacheVisitor<K, V> cacheVisitor) {
        expungeExpiredEntries();
        cacheVisitor.visit(getCachedEntries());
    }

    private Map<K, V> getCachedEntries() {
        HashMap hashMap = new HashMap(this.cacheMap.size());
        for (CacheEntry next : this.cacheMap.values()) {
            hashMap.put(next.getKey(), next.getValue());
        }
        return hashMap;
    }

    /* access modifiers changed from: protected */
    public CacheEntry<K, V> newEntry(K k, V v, long j, ReferenceQueue<V> referenceQueue) {
        if (referenceQueue != null) {
            return new SoftCacheEntry(k, v, j, referenceQueue);
        }
        return new HardCacheEntry(k, v, j);
    }

    /* compiled from: Cache */
    private static class HardCacheEntry<K, V> implements CacheEntry<K, V> {
        private long expirationTime;
        private K key;
        private V value;

        HardCacheEntry(K k, V v, long j) {
            this.key = k;
            this.value = v;
            this.expirationTime = j;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public boolean isValid(long j) {
            boolean z = j <= this.expirationTime;
            if (!z) {
                invalidate();
            }
            return z;
        }

        public void invalidate() {
            this.key = null;
            this.value = null;
            this.expirationTime = -1;
        }
    }

    /* compiled from: Cache */
    private static class SoftCacheEntry<K, V> extends SoftReference<V> implements CacheEntry<K, V> {
        private long expirationTime;
        private K key;

        SoftCacheEntry(K k, V v, long j, ReferenceQueue<V> referenceQueue) {
            super(v, referenceQueue);
            this.key = k;
            this.expirationTime = j;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return get();
        }

        public boolean isValid(long j) {
            boolean z = j <= this.expirationTime && get() != null;
            if (!z) {
                invalidate();
            }
            return z;
        }

        public void invalidate() {
            clear();
            this.key = null;
            this.expirationTime = -1;
        }
    }
}
