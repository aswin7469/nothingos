package libcore.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class BasicLruCache<K, V> {
    private final LinkedHashMap<K, V> map;
    private final int maxSize;

    /* access modifiers changed from: protected */
    public V create(K k) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void entryEvicted(K k, V v) {
    }

    public BasicLruCache(int i) {
        if (i > 0) {
            this.maxSize = i;
            this.map = new LinkedHashMap<>(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
        if (r0 == null) goto L_0x001f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r2.map.put(r3, r0);
        trimToSize(r2.maxSize);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0020, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000e, code lost:
        r0 = create(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        monitor-enter(r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final V get(K r3) {
        /*
            r2 = this;
            if (r3 == 0) goto L_0x0027
            monitor-enter(r2)
            java.util.LinkedHashMap<K, V> r0 = r2.map     // Catch:{ all -> 0x0024 }
            java.lang.Object r0 = r0.get(r3)     // Catch:{ all -> 0x0024 }
            if (r0 == 0) goto L_0x000d
            monitor-exit(r2)     // Catch:{ all -> 0x0024 }
            return r0
        L_0x000d:
            monitor-exit(r2)     // Catch:{ all -> 0x0024 }
            java.lang.Object r0 = r2.create(r3)
            monitor-enter(r2)
            if (r0 == 0) goto L_0x001f
            java.util.LinkedHashMap<K, V> r1 = r2.map     // Catch:{ all -> 0x0021 }
            r1.put(r3, r0)     // Catch:{ all -> 0x0021 }
            int r3 = r2.maxSize     // Catch:{ all -> 0x0021 }
            r2.trimToSize(r3)     // Catch:{ all -> 0x0021 }
        L_0x001f:
            monitor-exit(r2)     // Catch:{ all -> 0x0021 }
            return r0
        L_0x0021:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0021 }
            throw r3
        L_0x0024:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0024 }
            throw r3
        L_0x0027:
            java.lang.NullPointerException r2 = new java.lang.NullPointerException
            java.lang.String r3 = "key == null"
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: libcore.util.BasicLruCache.get(java.lang.Object):java.lang.Object");
    }

    public final synchronized V put(K k, V v) {
        V put;
        if (k == null) {
            throw new NullPointerException("key == null");
        } else if (v != null) {
            put = this.map.put(k, v);
            trimToSize(this.maxSize);
        } else {
            throw new NullPointerException("value == null");
        }
        return put;
    }

    private void trimToSize(int i) {
        while (this.map.size() > i) {
            Map.Entry<K, V> eldest = this.map.eldest();
            K key = eldest.getKey();
            V value = eldest.getValue();
            this.map.remove(key);
            entryEvicted(key, value);
        }
    }

    public final synchronized Map<K, V> snapshot() {
        return new LinkedHashMap(this.map);
    }

    public final synchronized void evictAll() {
        trimToSize(0);
    }
}
