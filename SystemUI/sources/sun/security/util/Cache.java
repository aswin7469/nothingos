package sun.security.util;

import java.util.Arrays;
import java.util.Map;

public abstract class Cache<K, V> {

    public interface CacheVisitor<K, V> {
        void visit(Map<K, V> map);
    }

    public abstract void accept(CacheVisitor<K, V> cacheVisitor);

    public abstract void clear();

    public abstract V get(Object obj);

    public abstract void put(K k, V v);

    public abstract void remove(Object obj);

    public abstract void setCapacity(int i);

    public abstract void setTimeout(int i);

    public abstract int size();

    protected Cache() {
    }

    public static <K, V> Cache<K, V> newSoftMemoryCache(int i) {
        return new MemoryCache(true, i);
    }

    public static <K, V> Cache<K, V> newSoftMemoryCache(int i, int i2) {
        return new MemoryCache(true, i, i2);
    }

    public static <K, V> Cache<K, V> newHardMemoryCache(int i) {
        return new MemoryCache(false, i);
    }

    public static <K, V> Cache<K, V> newNullCache() {
        return NullCache.INSTANCE;
    }

    public static <K, V> Cache<K, V> newHardMemoryCache(int i, int i2) {
        return new MemoryCache(false, i, i2);
    }

    public static class EqualByteArray {

        /* renamed from: b */
        private final byte[] f916b;
        private volatile int hash;

        public EqualByteArray(byte[] bArr) {
            this.f916b = bArr;
        }

        public int hashCode() {
            int i = this.hash;
            if (i == 0) {
                i = this.f916b.length + 1;
                int i2 = 0;
                while (true) {
                    byte[] bArr = this.f916b;
                    if (i2 >= bArr.length) {
                        break;
                    }
                    i += (bArr[i2] & 255) * 37;
                    i2++;
                }
                this.hash = i;
            }
            return i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof EqualByteArray)) {
                return false;
            }
            return Arrays.equals(this.f916b, ((EqualByteArray) obj).f916b);
        }
    }
}
