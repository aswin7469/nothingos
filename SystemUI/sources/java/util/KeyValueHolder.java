package java.util;

import java.util.Map;
import jdk.internal.p027vm.annotation.Stable;

final class KeyValueHolder<K, V> implements Map.Entry<K, V> {
    @Stable
    final K key;
    @Stable
    final V value;

    KeyValueHolder(K k, V v) {
        this.key = Objects.requireNonNull(k);
        this.value = Objects.requireNonNull(v);
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public V setValue(V v) {
        throw new UnsupportedOperationException("not supported");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        Map.Entry entry = (Map.Entry) obj;
        if (!this.key.equals(entry.getKey()) || !this.value.equals(entry.getValue())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.value.hashCode() ^ this.key.hashCode();
    }

    public String toString() {
        return this.key + "=" + this.value;
    }
}
