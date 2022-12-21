package java.util;

import java.lang.Enum;
import java.lang.reflect.Array;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

public class EnumMap<K extends Enum<K>, V> extends AbstractMap<K, V> implements Serializable, Cloneable {
    private static final Object NULL = new Object() {
        public int hashCode() {
            return 0;
        }

        public String toString() {
            return "java.util.EnumMap.NULL";
        }
    };
    private static final long serialVersionUID = 458661240069192865L;
    private transient Set<Map.Entry<K, V>> entrySet;
    private final Class<K> keyType;
    /* access modifiers changed from: private */
    public transient K[] keyUniverse;
    /* access modifiers changed from: private */
    public transient int size = 0;
    /* access modifiers changed from: private */
    public transient Object[] vals;

    /* access modifiers changed from: private */
    public Object maskNull(Object obj) {
        return obj == null ? NULL : obj;
    }

    /* access modifiers changed from: private */
    public V unmaskNull(Object obj) {
        if (obj == NULL) {
            return null;
        }
        return obj;
    }

    public EnumMap(Class<K> cls) {
        this.keyType = cls;
        K[] keyUniverse2 = getKeyUniverse(cls);
        this.keyUniverse = keyUniverse2;
        this.vals = new Object[keyUniverse2.length];
    }

    public EnumMap(EnumMap<K, ? extends V> enumMap) {
        this.keyType = enumMap.keyType;
        this.keyUniverse = enumMap.keyUniverse;
        this.vals = (Object[]) enumMap.vals.clone();
        this.size = enumMap.size;
    }

    public EnumMap(Map<K, ? extends V> map) {
        if (map instanceof EnumMap) {
            EnumMap enumMap = (EnumMap) map;
            this.keyType = enumMap.keyType;
            this.keyUniverse = enumMap.keyUniverse;
            this.vals = (Object[]) enumMap.vals.clone();
            this.size = enumMap.size;
        } else if (!map.isEmpty()) {
            Class<K> declaringClass = ((Enum) map.keySet().iterator().next()).getDeclaringClass();
            this.keyType = declaringClass;
            K[] keyUniverse2 = getKeyUniverse(declaringClass);
            this.keyUniverse = keyUniverse2;
            this.vals = new Object[keyUniverse2.length];
            putAll(map);
        } else {
            throw new IllegalArgumentException("Specified map is empty");
        }
    }

    public int size() {
        return this.size;
    }

    public boolean containsValue(Object obj) {
        Object maskNull = maskNull(obj);
        for (Object equals : this.vals) {
            if (maskNull.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsKey(Object obj) {
        return isValidKey(obj) && this.vals[((Enum) obj).ordinal()] != null;
    }

    /* access modifiers changed from: private */
    public boolean containsMapping(Object obj, Object obj2) {
        return isValidKey(obj) && maskNull(obj2).equals(this.vals[((Enum) obj).ordinal()]);
    }

    public V get(Object obj) {
        if (isValidKey(obj)) {
            return unmaskNull(this.vals[((Enum) obj).ordinal()]);
        }
        return null;
    }

    public V put(K k, V v) {
        typeCheck(k);
        int ordinal = k.ordinal();
        Object[] objArr = this.vals;
        Object obj = objArr[ordinal];
        objArr[ordinal] = maskNull(v);
        if (obj == null) {
            this.size++;
        }
        return unmaskNull(obj);
    }

    public V remove(Object obj) {
        if (!isValidKey(obj)) {
            return null;
        }
        int ordinal = ((Enum) obj).ordinal();
        Object[] objArr = this.vals;
        Object obj2 = objArr[ordinal];
        objArr[ordinal] = null;
        if (obj2 != null) {
            this.size--;
        }
        return unmaskNull(obj2);
    }

    /* access modifiers changed from: private */
    public boolean removeMapping(Object obj, Object obj2) {
        if (!isValidKey(obj)) {
            return false;
        }
        int ordinal = ((Enum) obj).ordinal();
        if (!maskNull(obj2).equals(this.vals[ordinal])) {
            return false;
        }
        this.vals[ordinal] = null;
        this.size--;
        return true;
    }

    private boolean isValidKey(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        if (cls == this.keyType || cls.getSuperclass() == this.keyType) {
            return true;
        }
        return false;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        if (map instanceof EnumMap) {
            EnumMap enumMap = (EnumMap) map;
            if (enumMap.keyType == this.keyType) {
                for (int i = 0; i < this.keyUniverse.length; i++) {
                    Object obj = enumMap.vals[i];
                    if (obj != null) {
                        Object[] objArr = this.vals;
                        if (objArr[i] == null) {
                            this.size++;
                        }
                        objArr[i] = obj;
                    }
                }
            } else if (!enumMap.isEmpty()) {
                throw new ClassCastException(enumMap.keyType + " != " + this.keyType);
            }
        } else {
            super.putAll(map);
        }
    }

    public void clear() {
        Arrays.fill(this.vals, (Object) null);
        this.size = 0;
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        KeySet keySet = new KeySet();
        this.keySet = keySet;
        return keySet;
    }

    private class KeySet extends AbstractSet<K> {
        private KeySet() {
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public int size() {
            return EnumMap.this.size;
        }

        public boolean contains(Object obj) {
            return EnumMap.this.containsKey(obj);
        }

        public boolean remove(Object obj) {
            int r0 = EnumMap.this.size;
            EnumMap.this.remove(obj);
            return EnumMap.this.size != r0;
        }

        public void clear() {
            EnumMap.this.clear();
        }
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        Values values = new Values();
        this.values = values;
        return values;
    }

    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public int size() {
            return EnumMap.this.size;
        }

        public boolean contains(Object obj) {
            return EnumMap.this.containsValue(obj);
        }

        public boolean remove(Object obj) {
            Object r4 = EnumMap.this.maskNull(obj);
            for (int i = 0; i < EnumMap.this.vals.length; i++) {
                if (r4.equals(EnumMap.this.vals[i])) {
                    EnumMap.this.vals[i] = null;
                    EnumMap enumMap = EnumMap.this;
                    enumMap.size = enumMap.size - 1;
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            EnumMap.this.clear();
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        EntrySet entrySet2 = new EntrySet();
        this.entrySet = entrySet2;
        return entrySet2;
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        private EntrySet() {
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return EnumMap.this.containsMapping(entry.getKey(), entry.getValue());
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return EnumMap.this.removeMapping(entry.getKey(), entry.getValue());
        }

        public int size() {
            return EnumMap.this.size;
        }

        public void clear() {
            EnumMap.this.clear();
        }

        public Object[] toArray() {
            return fillEntryArray(new Object[EnumMap.this.size]);
        }

        public <T> T[] toArray(T[] tArr) {
            int size = size();
            if (tArr.length < size) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            }
            if (tArr.length > size) {
                tArr[size] = null;
            }
            return fillEntryArray(tArr);
        }

        private Object[] fillEntryArray(Object[] objArr) {
            int i = 0;
            for (int i2 = 0; i2 < EnumMap.this.vals.length; i2++) {
                if (EnumMap.this.vals[i2] != null) {
                    Enum enumR = EnumMap.this.keyUniverse[i2];
                    EnumMap enumMap = EnumMap.this;
                    objArr[i] = new AbstractMap.SimpleEntry(enumR, enumMap.unmaskNull(enumMap.vals[i2]));
                    i++;
                }
            }
            return objArr;
        }
    }

    private abstract class EnumMapIterator<T> implements Iterator<T> {
        int index;
        int lastReturnedIndex;

        private EnumMapIterator() {
            this.index = 0;
            this.lastReturnedIndex = -1;
        }

        public boolean hasNext() {
            while (this.index < EnumMap.this.vals.length) {
                Object[] r0 = EnumMap.this.vals;
                int i = this.index;
                if (r0[i] != null) {
                    break;
                }
                this.index = i + 1;
            }
            return this.index != EnumMap.this.vals.length;
        }

        public void remove() {
            checkLastReturnedIndex();
            if (EnumMap.this.vals[this.lastReturnedIndex] != null) {
                EnumMap.this.vals[this.lastReturnedIndex] = null;
                EnumMap enumMap = EnumMap.this;
                enumMap.size = enumMap.size - 1;
            }
            this.lastReturnedIndex = -1;
        }

        private void checkLastReturnedIndex() {
            if (this.lastReturnedIndex < 0) {
                throw new IllegalStateException();
            }
        }
    }

    private class KeyIterator extends EnumMap<K, V>.EnumMapIterator<K> {
        private KeyIterator() {
            super();
        }

        public K next() {
            if (hasNext()) {
                int i = this.index;
                this.index = i + 1;
                this.lastReturnedIndex = i;
                return EnumMap.this.keyUniverse[this.lastReturnedIndex];
            }
            throw new NoSuchElementException();
        }
    }

    private class ValueIterator extends EnumMap<K, V>.EnumMapIterator<V> {
        private ValueIterator() {
            super();
        }

        public V next() {
            if (hasNext()) {
                int i = this.index;
                this.index = i + 1;
                this.lastReturnedIndex = i;
                EnumMap enumMap = EnumMap.this;
                return enumMap.unmaskNull(enumMap.vals[this.lastReturnedIndex]);
            }
            throw new NoSuchElementException();
        }
    }

    private class EntryIterator extends EnumMap<K, V>.EnumMapIterator<Map.Entry<K, V>> {
        private EnumMap<K, V>.EntryIterator.Entry lastReturnedEntry;

        private EntryIterator() {
            super();
        }

        public Map.Entry<K, V> next() {
            if (hasNext()) {
                int i = this.index;
                this.index = i + 1;
                Entry entry = new Entry(i);
                this.lastReturnedEntry = entry;
                return entry;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            EnumMap<K, V>.EntryIterator.Entry entry = this.lastReturnedEntry;
            this.lastReturnedIndex = entry == null ? -1 : entry.index;
            super.remove();
            this.lastReturnedEntry.index = this.lastReturnedIndex;
            this.lastReturnedEntry = null;
        }

        private class Entry implements Map.Entry<K, V> {
            /* access modifiers changed from: private */
            public int index;

            private Entry(int i) {
                this.index = i;
            }

            public K getKey() {
                checkIndexForEntryUse();
                return EnumMap.this.keyUniverse[this.index];
            }

            public V getValue() {
                checkIndexForEntryUse();
                return EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
            }

            public V setValue(V v) {
                checkIndexForEntryUse();
                V r0 = EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
                EnumMap.this.vals[this.index] = EnumMap.this.maskNull(v);
                return r0;
            }

            public boolean equals(Object obj) {
                if (this.index < 0) {
                    return obj == this;
                }
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                Object r0 = EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
                Object value = entry.getValue();
                if (entry.getKey() == EnumMap.this.keyUniverse[this.index]) {
                    if (r0 == value) {
                        return true;
                    }
                    if (r0 != null && r0.equals(value)) {
                        return true;
                    }
                }
                return false;
            }

            public int hashCode() {
                if (this.index < 0) {
                    return super.hashCode();
                }
                return EnumMap.this.entryHashCode(this.index);
            }

            public String toString() {
                if (this.index < 0) {
                    return super.toString();
                }
                return EnumMap.this.keyUniverse[this.index] + "=" + EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
            }

            private void checkIndexForEntryUse() {
                if (this.index < 0) {
                    throw new IllegalStateException("Entry was removed");
                }
            }
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof EnumMap) {
            return equals((EnumMap<?, ?>) (EnumMap) obj);
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        if (this.size != map.size()) {
            return false;
        }
        int i = 0;
        while (true) {
            K[] kArr = this.keyUniverse;
            if (i >= kArr.length) {
                return true;
            }
            Object obj2 = this.vals[i];
            if (obj2 != null) {
                K k = kArr[i];
                Object unmaskNull = unmaskNull(obj2);
                if (unmaskNull == null) {
                    if (map.get(k) != null || !map.containsKey(k)) {
                        return false;
                    }
                } else if (!unmaskNull.equals(map.get(k))) {
                    return false;
                }
            }
            i++;
        }
        return false;
    }

    private boolean equals(EnumMap<?, ?> enumMap) {
        int i = enumMap.size;
        int i2 = this.size;
        if (i != i2) {
            return false;
        }
        if (enumMap.keyType == this.keyType) {
            for (int i3 = 0; i3 < this.keyUniverse.length; i3++) {
                Object obj = this.vals[i3];
                Object obj2 = enumMap.vals[i3];
                if (obj2 != obj && (obj2 == null || !obj2.equals(obj))) {
                    return false;
                }
            }
            return true;
        } else if (i2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = 0;
        for (int i2 = 0; i2 < this.keyUniverse.length; i2++) {
            if (this.vals[i2] != null) {
                i += entryHashCode(i2);
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    public int entryHashCode(int i) {
        return this.vals[i].hashCode() ^ this.keyUniverse[i].hashCode();
    }

    public EnumMap<K, V> clone() {
        try {
            EnumMap<K, V> enumMap = (EnumMap) super.clone();
            enumMap.vals = (Object[]) enumMap.vals.clone();
            enumMap.entrySet = null;
            return enumMap;
        } catch (CloneNotSupportedException unused) {
            throw new AssertionError();
        }
    }

    private void typeCheck(K k) {
        Class<?> cls = k.getClass();
        if (cls != this.keyType && cls.getSuperclass() != this.keyType) {
            throw new ClassCastException(cls + " != " + this.keyType);
        }
    }

    private static <K extends Enum<K>> K[] getKeyUniverse(Class<K> cls) {
        return (Enum[]) cls.getEnumConstantsShared();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        int i = this.size;
        int i2 = 0;
        while (i > 0) {
            if (this.vals[i2] != null) {
                objectOutputStream.writeObject(this.keyUniverse[i2]);
                objectOutputStream.writeObject(unmaskNull(this.vals[i2]));
                i--;
            }
            i2++;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        K[] keyUniverse2 = getKeyUniverse(this.keyType);
        this.keyUniverse = keyUniverse2;
        this.vals = new Object[keyUniverse2.length];
        int readInt = objectInputStream.readInt();
        for (int i = 0; i < readInt; i++) {
            put((Enum) objectInputStream.readObject(), objectInputStream.readObject());
        }
    }
}
