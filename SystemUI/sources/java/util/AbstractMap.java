package java.util;

import java.p026io.Serializable;
import java.util.Map;

public abstract class AbstractMap<K, V> implements Map<K, V> {
    transient Set<K> keySet;
    transient Collection<V> values;

    public abstract Set<Map.Entry<K, V>> entrySet();

    protected AbstractMap() {
    }

    public int size() {
        return entrySet().size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsValue(Object obj) {
        Iterator it = entrySet().iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (((Map.Entry) it.next()).getValue() == null) {
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(((Map.Entry) it.next()).getValue())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsKey(Object obj) {
        Iterator it = entrySet().iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (((Map.Entry) it.next()).getKey() == null) {
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(((Map.Entry) it.next()).getKey())) {
                return true;
            }
        }
        return false;
    }

    public V get(Object obj) {
        Iterator it = entrySet().iterator();
        if (obj == null) {
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getKey() == null) {
                    return entry.getValue();
                }
            }
            return null;
        }
        while (it.hasNext()) {
            Map.Entry entry2 = (Map.Entry) it.next();
            if (obj.equals(entry2.getKey())) {
                return entry2.getValue();
            }
        }
        return null;
    }

    public V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    public V remove(Object obj) {
        Map.Entry entry;
        Iterator it = entrySet().iterator();
        if (obj == null) {
            entry = null;
            while (entry == null && it.hasNext()) {
                Map.Entry entry2 = (Map.Entry) it.next();
                if (entry2.getKey() == null) {
                    entry = entry2;
                }
            }
        } else {
            Map.Entry entry3 = null;
            while (entry3 == null && it.hasNext()) {
                Map.Entry entry4 = (Map.Entry) it.next();
                if (obj.equals(entry4.getKey())) {
                    entry3 = entry4;
                }
            }
            entry = entry3;
        }
        if (entry == null) {
            return null;
        }
        V value = entry.getValue();
        it.remove();
        return value;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry next : map.entrySet()) {
            put(next.getKey(), next.getValue());
        }
    }

    public void clear() {
        entrySet().clear();
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        C43811 r0 = new AbstractSet<K>() {
            public Iterator<K> iterator() {
                return new Iterator<K>() {

                    /* renamed from: i */
                    private Iterator<Map.Entry<K, V>> f585i;

                    {
                        this.f585i = AbstractMap.this.entrySet().iterator();
                    }

                    public boolean hasNext() {
                        return this.f585i.hasNext();
                    }

                    public K next() {
                        return this.f585i.next().getKey();
                    }

                    public void remove() {
                        this.f585i.remove();
                    }
                };
            }

            public int size() {
                return AbstractMap.this.size();
            }

            public boolean isEmpty() {
                return AbstractMap.this.isEmpty();
            }

            public void clear() {
                AbstractMap.this.clear();
            }

            public boolean contains(Object obj) {
                return AbstractMap.this.containsKey(obj);
            }
        };
        this.keySet = r0;
        return r0;
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        C43832 r0 = new AbstractCollection<V>() {
            public Iterator<V> iterator() {
                return new Iterator<V>() {

                    /* renamed from: i */
                    private Iterator<Map.Entry<K, V>> f586i;

                    {
                        this.f586i = AbstractMap.this.entrySet().iterator();
                    }

                    public boolean hasNext() {
                        return this.f586i.hasNext();
                    }

                    public V next() {
                        return this.f586i.next().getValue();
                    }

                    public void remove() {
                        this.f586i.remove();
                    }
                };
            }

            public int size() {
                return AbstractMap.this.size();
            }

            public boolean isEmpty() {
                return AbstractMap.this.isEmpty();
            }

            public void clear() {
                AbstractMap.this.clear();
            }

            public boolean contains(Object obj) {
                return AbstractMap.this.containsValue(obj);
            }
        };
        this.values = r0;
        return r0;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        if (map.size() != size()) {
            return false;
        }
        try {
            for (Map.Entry entry : entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                    if (map.get(key) != null || !map.containsKey(key)) {
                        return false;
                    }
                } else if (!value.equals(map.get(key))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    public int hashCode() {
        int i = 0;
        for (Map.Entry hashCode : entrySet()) {
            i += hashCode.hashCode();
        }
        return i;
    }

    public String toString() {
        Iterator it = entrySet().iterator();
        if (!it.hasNext()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        while (true) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key == this) {
                key = "(this Map)";
            }
            sb.append(key);
            sb.append('=');
            if (value == this) {
                value = "(this Map)";
            }
            sb.append(value);
            if (!it.hasNext()) {
                sb.append('}');
                return sb.toString();
            }
            sb.append(", ");
        }
    }

    /* access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        AbstractMap abstractMap = (AbstractMap) super.clone();
        abstractMap.keySet = null;
        abstractMap.values = null;
        return abstractMap;
    }

    /* access modifiers changed from: private */
    /* renamed from: eq */
    public static boolean m1719eq(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    public static class SimpleEntry<K, V> implements Map.Entry<K, V>, Serializable {
        private static final long serialVersionUID = -8499721149061103585L;
        private final K key;
        private V value;

        public SimpleEntry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        public SimpleEntry(Map.Entry<? extends K, ? extends V> entry) {
            this.key = entry.getKey();
            this.value = entry.getValue();
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            if (!AbstractMap.m1719eq(this.key, entry.getKey()) || !AbstractMap.m1719eq(this.value, entry.getValue())) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            K k = this.key;
            int i = 0;
            int hashCode = k == null ? 0 : k.hashCode();
            V v = this.value;
            if (v != null) {
                i = v.hashCode();
            }
            return hashCode ^ i;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    public static class SimpleImmutableEntry<K, V> implements Map.Entry<K, V>, Serializable {
        private static final long serialVersionUID = 7138329143949025153L;
        private final K key;
        private final V value;

        public SimpleImmutableEntry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        public SimpleImmutableEntry(Map.Entry<? extends K, ? extends V> entry) {
            this.key = entry.getKey();
            this.value = entry.getValue();
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            if (!AbstractMap.m1719eq(this.key, entry.getKey()) || !AbstractMap.m1719eq(this.value, entry.getValue())) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            K k = this.key;
            int i = 0;
            int hashCode = k == null ? 0 : k.hashCode();
            V v = this.value;
            if (v != null) {
                i = v.hashCode();
            }
            return hashCode ^ i;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
