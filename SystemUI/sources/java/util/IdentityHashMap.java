package java.util;

import java.lang.reflect.Array;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.p026io.StreamCorruptedException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class IdentityHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Serializable, Cloneable {
    private static final int DEFAULT_CAPACITY = 32;
    private static final int MAXIMUM_CAPACITY = 536870912;
    private static final int MINIMUM_CAPACITY = 4;
    static final Object NULL_KEY = new Object();
    private static final long serialVersionUID = 8188218128353913216L;
    private transient Set<Map.Entry<K, V>> entrySet;
    transient int modCount;
    int size;
    transient Object[] table;

    /* access modifiers changed from: private */
    public static int nextKeyIndex(int i, int i2) {
        int i3 = i + 2;
        if (i3 < i2) {
            return i3;
        }
        return 0;
    }

    private static Object maskNull(Object obj) {
        return obj == null ? NULL_KEY : obj;
    }

    static final Object unmaskNull(Object obj) {
        if (obj == NULL_KEY) {
            return null;
        }
        return obj;
    }

    public IdentityHashMap() {
        init(32);
    }

    public IdentityHashMap(int i) {
        if (i >= 0) {
            init(capacity(i));
            return;
        }
        throw new IllegalArgumentException("expectedMaxSize is negative: " + i);
    }

    private static int capacity(int i) {
        if (i > 178956970) {
            return 536870912;
        }
        if (i <= 2) {
            return 4;
        }
        return Integer.highestOneBit(i + (i << 1));
    }

    private void init(int i) {
        this.table = new Object[(i * 2)];
    }

    public IdentityHashMap(Map<? extends K, ? extends V> map) {
        this((int) (((double) (map.size() + 1)) * 1.1d));
        putAll(map);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    /* access modifiers changed from: private */
    public static int hash(Object obj, int i) {
        int identityHashCode = System.identityHashCode(obj);
        return ((identityHashCode << 1) - (identityHashCode << 8)) & (i - 1);
    }

    public V get(Object obj) {
        V maskNull = maskNull(obj);
        V[] vArr = this.table;
        int length = vArr.length;
        int hash = hash(maskNull, length);
        while (true) {
            V v = vArr[hash];
            if (v == maskNull) {
                return vArr[hash + 1];
            }
            if (v == null) {
                return null;
            }
            hash = nextKeyIndex(hash, length);
        }
    }

    public boolean containsKey(Object obj) {
        Object maskNull = maskNull(obj);
        Object[] objArr = this.table;
        int length = objArr.length;
        int hash = hash(maskNull, length);
        while (true) {
            Object obj2 = objArr[hash];
            if (obj2 == maskNull) {
                return true;
            }
            if (obj2 == null) {
                return false;
            }
            hash = nextKeyIndex(hash, length);
        }
    }

    public boolean containsValue(Object obj) {
        Object[] objArr = this.table;
        for (int i = 1; i < objArr.length; i += 2) {
            if (objArr[i] == obj && objArr[i - 1] != null) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean containsMapping(Object obj, Object obj2) {
        Object maskNull = maskNull(obj);
        Object[] objArr = this.table;
        int length = objArr.length;
        int hash = hash(maskNull, length);
        while (true) {
            Object obj3 = objArr[hash];
            if (obj3 == maskNull) {
                if (objArr[hash + 1] == obj2) {
                    return true;
                }
                return false;
            } else if (obj3 == null) {
                return false;
            } else {
                hash = nextKeyIndex(hash, length);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001d, code lost:
        r3 = r5.size + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0024, code lost:
        if (((r3 << 1) + r3) <= r1) goto L_0x002d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public V put(K r6, V r7) {
        /*
            r5 = this;
            java.lang.Object r6 = maskNull(r6)
        L_0x0004:
            java.lang.Object[] r0 = r5.table
            int r1 = r0.length
            int r2 = hash(r6, r1)
        L_0x000b:
            r3 = r0[r2]
            if (r3 == 0) goto L_0x001d
            if (r3 != r6) goto L_0x0018
            int r2 = r2 + 1
            r5 = r0[r2]
            r0[r2] = r7
            return r5
        L_0x0018:
            int r2 = nextKeyIndex(r2, r1)
            goto L_0x000b
        L_0x001d:
            int r3 = r5.size
            int r3 = r3 + 1
            int r4 = r3 << 1
            int r4 = r4 + r3
            if (r4 <= r1) goto L_0x002d
            boolean r1 = r5.resize(r1)
            if (r1 == 0) goto L_0x002d
            goto L_0x0004
        L_0x002d:
            int r1 = r5.modCount
            int r1 = r1 + 1
            r5.modCount = r1
            r0[r2] = r6
            int r2 = r2 + 1
            r0[r2] = r7
            r5.size = r3
            r5 = 0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.IdentityHashMap.put(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    private boolean resize(int i) {
        int i2 = i * 2;
        Object[] objArr = this.table;
        int length = objArr.length;
        if (length == 1073741824) {
            if (this.size != 536870911) {
                return false;
            }
            throw new IllegalStateException("Capacity exhausted.");
        } else if (length >= i2) {
            return false;
        } else {
            Object[] objArr2 = new Object[i2];
            for (int i3 = 0; i3 < length; i3 += 2) {
                Object obj = objArr[i3];
                if (obj != null) {
                    int i4 = i3 + 1;
                    Object obj2 = objArr[i4];
                    objArr[i3] = null;
                    objArr[i4] = null;
                    int hash = hash(obj, i2);
                    while (objArr2[hash] != null) {
                        hash = nextKeyIndex(hash, i2);
                    }
                    objArr2[hash] = obj;
                    objArr2[hash + 1] = obj2;
                }
            }
            this.table = objArr2;
            return true;
        }
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        int size2 = map.size();
        if (size2 != 0) {
            if (size2 > this.size) {
                resize(capacity(size2));
            }
            for (Map.Entry next : map.entrySet()) {
                put(next.getKey(), next.getValue());
            }
        }
    }

    public V remove(Object obj) {
        V maskNull = maskNull(obj);
        V[] vArr = this.table;
        int length = vArr.length;
        int hash = hash(maskNull, length);
        while (true) {
            V v = vArr[hash];
            if (v == maskNull) {
                this.modCount++;
                this.size--;
                int i = hash + 1;
                V v2 = vArr[i];
                vArr[i] = null;
                vArr[hash] = null;
                closeDeletion(hash);
                return v2;
            } else if (v == null) {
                return null;
            } else {
                hash = nextKeyIndex(hash, length);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean removeMapping(Object obj, Object obj2) {
        Object maskNull = maskNull(obj);
        Object[] objArr = this.table;
        int length = objArr.length;
        int hash = hash(maskNull, length);
        while (true) {
            Object obj3 = objArr[hash];
            if (obj3 == maskNull) {
                int i = hash + 1;
                if (objArr[i] != obj2) {
                    return false;
                }
                this.modCount++;
                this.size--;
                objArr[hash] = null;
                objArr[i] = null;
                closeDeletion(hash);
                return true;
            } else if (obj3 == null) {
                return false;
            } else {
                hash = nextKeyIndex(hash, length);
            }
        }
    }

    private void closeDeletion(int i) {
        Object[] objArr = this.table;
        int length = objArr.length;
        int nextKeyIndex = nextKeyIndex(i, length);
        while (true) {
            Object obj = objArr[nextKeyIndex];
            if (obj != null) {
                int hash = hash(obj, length);
                if ((nextKeyIndex < hash && (hash <= i || i <= nextKeyIndex)) || (hash <= i && i <= nextKeyIndex)) {
                    objArr[i] = obj;
                    int i2 = nextKeyIndex + 1;
                    objArr[i + 1] = objArr[i2];
                    objArr[nextKeyIndex] = null;
                    objArr[i2] = null;
                    i = nextKeyIndex;
                }
                nextKeyIndex = nextKeyIndex(nextKeyIndex, length);
            } else {
                return;
            }
        }
    }

    public void clear() {
        this.modCount++;
        Object[] objArr = this.table;
        for (int i = 0; i < objArr.length; i++) {
            objArr[i] = null;
        }
        this.size = 0;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof IdentityHashMap) {
            IdentityHashMap identityHashMap = (IdentityHashMap) obj;
            if (identityHashMap.size() != this.size) {
                return false;
            }
            Object[] objArr = identityHashMap.table;
            for (int i = 0; i < objArr.length; i += 2) {
                Object obj2 = objArr[i];
                if (obj2 != null && !containsMapping(obj2, objArr[i + 1])) {
                    return false;
                }
            }
            return true;
        } else if (obj instanceof Map) {
            return entrySet().equals(((Map) obj).entrySet());
        } else {
            return false;
        }
    }

    public int hashCode() {
        Object[] objArr = this.table;
        int i = 0;
        for (int i2 = 0; i2 < objArr.length; i2 += 2) {
            Object obj = objArr[i2];
            if (obj != null) {
                i += System.identityHashCode(unmaskNull(obj)) ^ System.identityHashCode(objArr[i2 + 1]);
            }
        }
        return i;
    }

    public Object clone() {
        try {
            IdentityHashMap identityHashMap = (IdentityHashMap) super.clone();
            identityHashMap.entrySet = null;
            identityHashMap.table = (Object[]) this.table.clone();
            return identityHashMap;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    private abstract class IdentityHashMapIterator<T> implements Iterator<T> {
        int expectedModCount;
        int index;
        boolean indexValid;
        int lastReturnedIndex;
        Object[] traversalTable;

        private IdentityHashMapIterator() {
            this.index = IdentityHashMap.this.size != 0 ? 0 : IdentityHashMap.this.table.length;
            this.expectedModCount = IdentityHashMap.this.modCount;
            this.lastReturnedIndex = -1;
            this.traversalTable = IdentityHashMap.this.table;
        }

        public boolean hasNext() {
            Object[] objArr = this.traversalTable;
            for (int i = this.index; i < objArr.length; i += 2) {
                if (objArr[i] != null) {
                    this.index = i;
                    this.indexValid = true;
                    return true;
                }
            }
            this.index = objArr.length;
            return false;
        }

        /* access modifiers changed from: protected */
        public int nextIndex() {
            if (IdentityHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else if (this.indexValid || hasNext()) {
                this.indexValid = false;
                int i = this.index;
                this.lastReturnedIndex = i;
                this.index = i + 2;
                return i;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (this.lastReturnedIndex == -1) {
                throw new IllegalStateException();
            } else if (IdentityHashMap.this.modCount == this.expectedModCount) {
                IdentityHashMap identityHashMap = IdentityHashMap.this;
                int i = identityHashMap.modCount + 1;
                identityHashMap.modCount = i;
                this.expectedModCount = i;
                int i2 = this.lastReturnedIndex;
                this.lastReturnedIndex = -1;
                this.index = i2;
                this.indexValid = false;
                Object[] objArr = this.traversalTable;
                int length = objArr.length;
                Object obj = objArr[i2];
                objArr[i2] = null;
                objArr[i2 + 1] = null;
                if (objArr != IdentityHashMap.this.table) {
                    IdentityHashMap.this.remove(obj);
                    this.expectedModCount = IdentityHashMap.this.modCount;
                    return;
                }
                IdentityHashMap identityHashMap2 = IdentityHashMap.this;
                identityHashMap2.size--;
                int r4 = IdentityHashMap.nextKeyIndex(i2, length);
                int i3 = i2;
                while (true) {
                    Object obj2 = objArr[r4];
                    if (obj2 != null) {
                        int r8 = IdentityHashMap.hash(obj2, length);
                        if ((r4 < r8 && (r8 <= i3 || i3 <= r4)) || (r8 <= i3 && i3 <= r4)) {
                            if (r4 < i2 && i3 >= i2 && this.traversalTable == IdentityHashMap.this.table) {
                                int i4 = length - i2;
                                Object[] objArr2 = new Object[i4];
                                System.arraycopy((Object) objArr, i2, (Object) objArr2, 0, i4);
                                this.traversalTable = objArr2;
                                this.index = 0;
                            }
                            objArr[i3] = obj2;
                            int i5 = r4 + 1;
                            objArr[i3 + 1] = objArr[i5];
                            objArr[r4] = null;
                            objArr[i5] = null;
                            i3 = r4;
                        }
                        r4 = IdentityHashMap.nextKeyIndex(r4, length);
                    } else {
                        return;
                    }
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class KeyIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<K> {
        private KeyIterator() {
            super();
        }

        public K next() {
            return IdentityHashMap.unmaskNull(this.traversalTable[nextIndex()]);
        }
    }

    private class ValueIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<V> {
        private ValueIterator() {
            super();
        }

        public V next() {
            return this.traversalTable[nextIndex() + 1];
        }
    }

    private class EntryIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<Map.Entry<K, V>> {
        private IdentityHashMap<K, V>.EntryIterator.Entry lastReturnedEntry;

        private EntryIterator() {
            super();
        }

        public Map.Entry<K, V> next() {
            Entry entry = new Entry(nextIndex());
            this.lastReturnedEntry = entry;
            return entry;
        }

        public void remove() {
            IdentityHashMap<K, V>.EntryIterator.Entry entry = this.lastReturnedEntry;
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
                return IdentityHashMap.unmaskNull(EntryIterator.this.traversalTable[this.index]);
            }

            public V getValue() {
                checkIndexForEntryUse();
                return EntryIterator.this.traversalTable[this.index + 1];
            }

            public V setValue(V v) {
                checkIndexForEntryUse();
                V v2 = EntryIterator.this.traversalTable[this.index + 1];
                EntryIterator.this.traversalTable[this.index + 1] = v;
                if (EntryIterator.this.traversalTable != IdentityHashMap.this.table) {
                    IdentityHashMap.this.put(EntryIterator.this.traversalTable[this.index], v);
                }
                return v2;
            }

            public boolean equals(Object obj) {
                if (this.index < 0) {
                    return super.equals(obj);
                }
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                if (entry.getKey() == IdentityHashMap.unmaskNull(EntryIterator.this.traversalTable[this.index]) && entry.getValue() == EntryIterator.this.traversalTable[this.index + 1]) {
                    return true;
                }
                return false;
            }

            public int hashCode() {
                if (EntryIterator.this.lastReturnedIndex < 0) {
                    return super.hashCode();
                }
                return System.identityHashCode(EntryIterator.this.traversalTable[this.index + 1]) ^ System.identityHashCode(IdentityHashMap.unmaskNull(EntryIterator.this.traversalTable[this.index]));
            }

            public String toString() {
                if (this.index < 0) {
                    return super.toString();
                }
                return IdentityHashMap.unmaskNull(EntryIterator.this.traversalTable[this.index]) + "=" + EntryIterator.this.traversalTable[this.index + 1];
            }

            private void checkIndexForEntryUse() {
                if (this.index < 0) {
                    throw new IllegalStateException("Entry was removed");
                }
            }
        }
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
            return IdentityHashMap.this.size;
        }

        public boolean contains(Object obj) {
            return IdentityHashMap.this.containsKey(obj);
        }

        public boolean remove(Object obj) {
            int i = IdentityHashMap.this.size;
            IdentityHashMap.this.remove(obj);
            return IdentityHashMap.this.size != i;
        }

        public boolean removeAll(Collection<?> collection) {
            Objects.requireNonNull(collection);
            Iterator it = iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    z = true;
                }
            }
            return z;
        }

        public void clear() {
            IdentityHashMap.this.clear();
        }

        public int hashCode() {
            Iterator it = iterator();
            int i = 0;
            while (it.hasNext()) {
                i += System.identityHashCode(it.next());
            }
            return i;
        }

        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        public <T> T[] toArray(T[] tArr) {
            int i = IdentityHashMap.this.modCount;
            int size = size();
            if (tArr.length < size) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            }
            Object[] objArr = IdentityHashMap.this.table;
            int i2 = 0;
            for (int i3 = 0; i3 < objArr.length; i3 += 2) {
                Object obj = objArr[i3];
                if (obj != null) {
                    if (i2 < size) {
                        tArr[i2] = IdentityHashMap.unmaskNull(obj);
                        i2++;
                    } else {
                        throw new ConcurrentModificationException();
                    }
                }
            }
            if (i2 < size || i != IdentityHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (i2 < tArr.length) {
                tArr[i2] = null;
            }
            return tArr;
        }

        public Spliterator<K> spliterator() {
            return new KeySpliterator(IdentityHashMap.this, 0, -1, 0, 0);
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
            return IdentityHashMap.this.size;
        }

        public boolean contains(Object obj) {
            return IdentityHashMap.this.containsValue(obj);
        }

        public boolean remove(Object obj) {
            Iterator it = iterator();
            while (it.hasNext()) {
                if (it.next() == obj) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            IdentityHashMap.this.clear();
        }

        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        public <T> T[] toArray(T[] tArr) {
            int i = IdentityHashMap.this.modCount;
            int size = size();
            if (tArr.length < size) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            }
            T[] tArr2 = IdentityHashMap.this.table;
            int i2 = 0;
            for (int i3 = 0; i3 < tArr2.length; i3 += 2) {
                if (tArr2[i3] != null) {
                    if (i2 < size) {
                        tArr[i2] = tArr2[i3 + 1];
                        i2++;
                    } else {
                        throw new ConcurrentModificationException();
                    }
                }
            }
            if (i2 < size || i != IdentityHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (i2 < tArr.length) {
                tArr[i2] = null;
            }
            return tArr;
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(IdentityHashMap.this, 0, -1, 0, 0);
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
            return IdentityHashMap.this.containsMapping(entry.getKey(), entry.getValue());
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return IdentityHashMap.this.removeMapping(entry.getKey(), entry.getValue());
        }

        public int size() {
            return IdentityHashMap.this.size;
        }

        public void clear() {
            IdentityHashMap.this.clear();
        }

        public boolean removeAll(Collection<?> collection) {
            Objects.requireNonNull(collection);
            Iterator it = iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    z = true;
                }
            }
            return z;
        }

        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        public <T> T[] toArray(T[] tArr) {
            int i = IdentityHashMap.this.modCount;
            int size = size();
            if (tArr.length < size) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            }
            Object[] objArr = IdentityHashMap.this.table;
            int i2 = 0;
            for (int i3 = 0; i3 < objArr.length; i3 += 2) {
                Object obj = objArr[i3];
                if (obj != null) {
                    if (i2 < size) {
                        tArr[i2] = new AbstractMap.SimpleEntry(IdentityHashMap.unmaskNull(obj), objArr[i3 + 1]);
                        i2++;
                    } else {
                        throw new ConcurrentModificationException();
                    }
                }
            }
            if (i2 < size || i != IdentityHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (i2 < tArr.length) {
                tArr[i2] = null;
            }
            return tArr;
        }

        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(IdentityHashMap.this, 0, -1, 0, 0);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        Object[] objArr = this.table;
        for (int i = 0; i < objArr.length; i += 2) {
            Object obj = objArr[i];
            if (obj != null) {
                objectOutputStream.writeObject(unmaskNull(obj));
                objectOutputStream.writeObject(objArr[i + 1]);
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        if (readInt >= 0) {
            init(capacity(readInt));
            for (int i = 0; i < readInt; i++) {
                putForCreate(objectInputStream.readObject(), objectInputStream.readObject());
            }
            return;
        }
        throw new StreamCorruptedException("Illegal mappings count: " + readInt);
    }

    private void putForCreate(K k, V v) throws StreamCorruptedException {
        Object maskNull = maskNull(k);
        Object[] objArr = this.table;
        int length = objArr.length;
        int hash = hash(maskNull, length);
        while (true) {
            Object obj = objArr[hash];
            if (obj == null) {
                objArr[hash] = maskNull;
                objArr[hash + 1] = v;
                return;
            } else if (obj != maskNull) {
                hash = nextKeyIndex(hash, length);
            } else {
                throw new StreamCorruptedException();
            }
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        int i = this.modCount;
        Object[] objArr = this.table;
        int i2 = 0;
        while (i2 < objArr.length) {
            Object obj = objArr[i2];
            if (obj != null) {
                biConsumer.accept(unmaskNull(obj), objArr[i2 + 1]);
            }
            if (this.modCount == i) {
                i2 += 2;
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int i = this.modCount;
        Object[] objArr = this.table;
        int i2 = 0;
        while (i2 < objArr.length) {
            Object obj = objArr[i2];
            if (obj != null) {
                int i3 = i2 + 1;
                objArr[i3] = biFunction.apply(unmaskNull(obj), objArr[i3]);
            }
            if (this.modCount == i) {
                i2 += 2;
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    static class IdentityHashMapSpliterator<K, V> {
        int est;
        int expectedModCount;
        int fence;
        int index;
        final IdentityHashMap<K, V> map;

        IdentityHashMapSpliterator(IdentityHashMap<K, V> identityHashMap, int i, int i2, int i3, int i4) {
            this.map = identityHashMap;
            this.index = i;
            this.fence = i2;
            this.est = i3;
            this.expectedModCount = i4;
        }

        /* access modifiers changed from: package-private */
        public final int getFence() {
            int i = this.fence;
            if (i >= 0) {
                return i;
            }
            this.est = this.map.size;
            this.expectedModCount = this.map.modCount;
            int length = this.map.table.length;
            this.fence = length;
            return length;
        }

        public final long estimateSize() {
            getFence();
            return (long) this.est;
        }
    }

    static final class KeySpliterator<K, V> extends IdentityHashMapSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(IdentityHashMap<K, V> identityHashMap, int i, int i2, int i3, int i4) {
            super(identityHashMap, i, i2, i3, i4);
        }

        public KeySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = ((fence + i) >>> 1) & -2;
            if (i >= i2) {
                return null;
            }
            IdentityHashMap identityHashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new KeySpliterator<>(identityHashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super K> consumer) {
            Object[] objArr;
            int i;
            consumer.getClass();
            IdentityHashMap identityHashMap = this.map;
            if (!(identityHashMap == null || (objArr = identityHashMap.table) == null || (i = this.index) < 0)) {
                int fence = getFence();
                this.index = fence;
                if (fence <= objArr.length) {
                    for (i = this.index; i < fence; i += 2) {
                        Object obj = objArr[i];
                        if (obj != null) {
                            consumer.accept(IdentityHashMap.unmaskNull(obj));
                        }
                    }
                    if (identityHashMap.modCount == this.expectedModCount) {
                        return;
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super K> consumer) {
            consumer.getClass();
            Object[] objArr = this.map.table;
            int fence = getFence();
            while (this.index < fence) {
                Object obj = objArr[this.index];
                this.index += 2;
                if (obj != null) {
                    consumer.accept(IdentityHashMap.unmaskNull(obj));
                    if (this.map.modCount == this.expectedModCount) {
                        return true;
                    }
                    throw new ConcurrentModificationException();
                }
            }
            return false;
        }

        public int characteristics() {
            return ((this.fence < 0 || this.est == this.map.size) ? 64 : 0) | 1;
        }
    }

    static final class ValueSpliterator<K, V> extends IdentityHashMapSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(IdentityHashMap<K, V> identityHashMap, int i, int i2, int i3, int i4) {
            super(identityHashMap, i, i2, i3, i4);
        }

        public ValueSpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = ((fence + i) >>> 1) & -2;
            if (i >= i2) {
                return null;
            }
            IdentityHashMap identityHashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new ValueSpliterator<>(identityHashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super V> consumer) {
            Object[] objArr;
            int i;
            consumer.getClass();
            IdentityHashMap identityHashMap = this.map;
            if (!(identityHashMap == null || (objArr = identityHashMap.table) == null || (i = this.index) < 0)) {
                int fence = getFence();
                this.index = fence;
                if (fence <= objArr.length) {
                    for (i = this.index; i < fence; i += 2) {
                        if (objArr[i] != null) {
                            consumer.accept(objArr[i + 1]);
                        }
                    }
                    if (identityHashMap.modCount == this.expectedModCount) {
                        return;
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super V> consumer) {
            consumer.getClass();
            Object[] objArr = this.map.table;
            int fence = getFence();
            while (this.index < fence) {
                Object obj = objArr[this.index];
                Object obj2 = objArr[this.index + 1];
                this.index += 2;
                if (obj != null) {
                    consumer.accept(obj2);
                    if (this.map.modCount == this.expectedModCount) {
                        return true;
                    }
                    throw new ConcurrentModificationException();
                }
            }
            return false;
        }

        public int characteristics() {
            return (this.fence < 0 || this.est == this.map.size) ? 64 : 0;
        }
    }

    static final class EntrySpliterator<K, V> extends IdentityHashMapSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        EntrySpliterator(IdentityHashMap<K, V> identityHashMap, int i, int i2, int i3, int i4) {
            super(identityHashMap, i, i2, i3, i4);
        }

        public EntrySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = ((fence + i) >>> 1) & -2;
            if (i >= i2) {
                return null;
            }
            IdentityHashMap identityHashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new EntrySpliterator<>(identityHashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            Object[] objArr;
            int i;
            consumer.getClass();
            IdentityHashMap identityHashMap = this.map;
            if (!(identityHashMap == null || (objArr = identityHashMap.table) == null || (i = this.index) < 0)) {
                int fence = getFence();
                this.index = fence;
                if (fence <= objArr.length) {
                    for (i = this.index; i < fence; i += 2) {
                        Object obj = objArr[i];
                        if (obj != null) {
                            consumer.accept(new AbstractMap.SimpleImmutableEntry(IdentityHashMap.unmaskNull(obj), objArr[i + 1]));
                        }
                    }
                    if (identityHashMap.modCount == this.expectedModCount) {
                        return;
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            Object[] objArr = this.map.table;
            int fence = getFence();
            while (this.index < fence) {
                Object obj = objArr[this.index];
                Object obj2 = objArr[this.index + 1];
                this.index += 2;
                if (obj != null) {
                    consumer.accept(new AbstractMap.SimpleImmutableEntry(IdentityHashMap.unmaskNull(obj), obj2));
                    if (this.map.modCount == this.expectedModCount) {
                        return true;
                    }
                    throw new ConcurrentModificationException();
                }
            }
            return false;
        }

        public int characteristics() {
            return ((this.fence < 0 || this.est == this.map.size) ? 64 : 0) | 1;
        }
    }
}
