package sun.security.util;

import sun.security.util.Cache;

/* compiled from: Cache */
class NullCache<K, V> extends Cache<K, V> {
    static final Cache<Object, Object> INSTANCE = new NullCache();

    public void accept(Cache.CacheVisitor<K, V> cacheVisitor) {
    }

    public void clear() {
    }

    public V get(Object obj) {
        return null;
    }

    public void put(K k, V v) {
    }

    public void remove(Object obj) {
    }

    public void setCapacity(int i) {
    }

    public void setTimeout(int i) {
    }

    public int size() {
        return 0;
    }

    private NullCache() {
    }
}
