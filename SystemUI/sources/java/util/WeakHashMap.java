package java.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class WeakHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final Object NULL_KEY = new Object();
    private transient Set<Map.Entry<K, V>> entrySet;
    private final float loadFactor;
    int modCount;
    private final ReferenceQueue<Object> queue;
    private int size;
    Entry<K, V>[] table;
    private int threshold;

    private static int indexFor(int i, int i2) {
        return i & (i2 - 1);
    }

    private Entry<K, V>[] newTable(int i) {
        return new Entry[i];
    }

    public WeakHashMap(int i, float f) {
        this.queue = new ReferenceQueue<>();
        if (i >= 0) {
            i = i > 1073741824 ? 1073741824 : i;
            if (f <= 0.0f || Float.isNaN(f)) {
                throw new IllegalArgumentException("Illegal Load factor: " + f);
            }
            int i2 = 1;
            while (i2 < i) {
                i2 <<= 1;
            }
            this.table = newTable(i2);
            this.loadFactor = f;
            this.threshold = (int) (((float) i2) * f);
            return;
        }
        throw new IllegalArgumentException("Illegal Initial Capacity: " + i);
    }

    public WeakHashMap(int i) {
        this(i, 0.75f);
    }

    public WeakHashMap() {
        this(16, 0.75f);
    }

    public WeakHashMap(Map<? extends K, ? extends V> map) {
        this(Math.max(((int) (((float) map.size()) / 0.75f)) + 1, 16), 0.75f);
        putAll(map);
    }

    private static Object maskNull(Object obj) {
        return obj == null ? NULL_KEY : obj;
    }

    static Object unmaskNull(Object obj) {
        if (obj == NULL_KEY) {
            return null;
        }
        return obj;
    }

    /* renamed from: eq */
    private static boolean m1762eq(Object obj, Object obj2) {
        return obj == obj2 || obj.equals(obj2);
    }

    /* access modifiers changed from: package-private */
    public final int hash(Object obj) {
        int hashCode = obj.hashCode();
        int i = hashCode ^ ((hashCode >>> 20) ^ (hashCode >>> 12));
        return (i >>> 4) ^ ((i >>> 7) ^ i);
    }

    private void expungeStaleEntries() {
        while (true) {
            Reference<? extends Object> poll = this.queue.poll();
            if (poll != null) {
                synchronized (this.queue) {
                    Entry<K, V> entry = (Entry) poll;
                    int indexFor = indexFor(entry.hash, this.table.length);
                    Entry<K, V> entry2 = this.table[indexFor];
                    Entry<K, V> entry3 = entry2;
                    while (true) {
                        if (entry2 == null) {
                            break;
                        }
                        Entry<K, V> entry4 = entry2.next;
                        if (entry2 == entry) {
                            if (entry3 == entry) {
                                this.table[indexFor] = entry4;
                            } else {
                                entry3.next = entry4;
                            }
                            entry.value = null;
                            this.size--;
                        } else {
                            entry3 = entry2;
                            entry2 = entry4;
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    private Entry<K, V>[] getTable() {
        expungeStaleEntries();
        return this.table;
    }

    public int size() {
        if (this.size == 0) {
            return 0;
        }
        expungeStaleEntries();
        return this.size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public V get(Object obj) {
        Object maskNull = maskNull(obj);
        int hash = hash(maskNull);
        Entry<K, V>[] table2 = getTable();
        for (Entry<K, V> entry = table2[indexFor(hash, table2.length)]; entry != null; entry = entry.next) {
            if (entry.hash == hash && m1762eq(maskNull, entry.get())) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean containsKey(Object obj) {
        return getEntry(obj) != null;
    }

    /* access modifiers changed from: package-private */
    public Entry<K, V> getEntry(Object obj) {
        Object maskNull = maskNull(obj);
        int hash = hash(maskNull);
        Entry<K, V>[] table2 = getTable();
        Entry<K, V> entry = table2[indexFor(hash, table2.length)];
        while (entry != null && (entry.hash != hash || !m1762eq(maskNull, entry.get()))) {
            entry = entry.next;
        }
        return entry;
    }

    public V put(K k, V v) {
        Object maskNull = maskNull(k);
        int hash = hash(maskNull);
        Entry<K, V>[] table2 = getTable();
        int indexFor = indexFor(hash, table2.length);
        Entry<K, V> entry = table2[indexFor];
        while (entry != null) {
            if (hash != entry.hash || !m1762eq(maskNull, entry.get())) {
                entry = entry.next;
            } else {
                V v2 = entry.value;
                if (v != v2) {
                    entry.value = v;
                }
                return v2;
            }
        }
        this.modCount++;
        table2[indexFor] = new Entry<>(maskNull, v, this.queue, hash, table2[indexFor]);
        int i = this.size + 1;
        this.size = i;
        if (i < this.threshold) {
            return null;
        }
        resize(table2.length * 2);
        return null;
    }

    /* access modifiers changed from: package-private */
    public void resize(int i) {
        Entry<K, V>[] table2 = getTable();
        if (table2.length == 1073741824) {
            this.threshold = Integer.MAX_VALUE;
            return;
        }
        Entry<K, V>[] newTable = newTable(i);
        transfer(table2, newTable);
        this.table = newTable;
        if (this.size >= this.threshold / 2) {
            this.threshold = (int) (((float) i) * this.loadFactor);
            return;
        }
        expungeStaleEntries();
        transfer(newTable, table2);
        this.table = table2;
    }

    private void transfer(Entry<K, V>[] entryArr, Entry<K, V>[] entryArr2) {
        for (int i = 0; i < entryArr.length; i++) {
            Entry<K, V> entry = entryArr[i];
            entryArr[i] = null;
            while (entry != null) {
                Entry<K, V> entry2 = entry.next;
                if (entry.get() == null) {
                    entry.next = null;
                    entry.value = null;
                    this.size--;
                } else {
                    int indexFor = indexFor(entry.hash, entryArr2.length);
                    entry.next = entryArr2[indexFor];
                    entryArr2[indexFor] = entry;
                }
                entry = entry2;
            }
        }
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        int size2 = map.size();
        if (size2 != 0) {
            if (size2 > this.threshold) {
                int i = (int) ((((float) size2) / this.loadFactor) + 1.0f);
                if (i > 1073741824) {
                    i = 1073741824;
                }
                int length = this.table.length;
                while (length < i) {
                    length <<= 1;
                }
                if (length > this.table.length) {
                    resize(length);
                }
            }
            for (Map.Entry next : map.entrySet()) {
                put(next.getKey(), next.getValue());
            }
        }
    }

    public V remove(Object obj) {
        Object maskNull = maskNull(obj);
        int hash = hash(maskNull);
        Entry<K, V>[] table2 = getTable();
        int indexFor = indexFor(hash, table2.length);
        Entry<K, V> entry = table2[indexFor];
        Entry<K, V> entry2 = entry;
        while (entry != null) {
            Entry<K, V> entry3 = entry.next;
            if (hash != entry.hash || !m1762eq(maskNull, entry.get())) {
                entry2 = entry;
                entry = entry3;
            } else {
                this.modCount++;
                this.size--;
                if (entry2 == entry) {
                    table2[indexFor] = entry3;
                } else {
                    entry2.next = entry3;
                }
                return entry.value;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean removeMapping(Object obj) {
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        Entry<K, V>[] table2 = getTable();
        Map.Entry entry = (Map.Entry) obj;
        int hash = hash(maskNull(entry.getKey()));
        int indexFor = indexFor(hash, table2.length);
        Entry<K, V> entry2 = table2[indexFor];
        Entry<K, V> entry3 = entry2;
        while (entry2 != null) {
            Entry<K, V> entry4 = entry2.next;
            if (hash != entry2.hash || !entry2.equals(entry)) {
                entry3 = entry2;
                entry2 = entry4;
            } else {
                this.modCount++;
                this.size--;
                if (entry3 == entry2) {
                    table2[indexFor] = entry4;
                } else {
                    entry3.next = entry4;
                }
                return true;
            }
        }
        return false;
    }

    public void clear() {
        do {
        } while (this.queue.poll() != null);
        this.modCount++;
        Arrays.fill((Object[]) this.table, (Object) null);
        this.size = 0;
        do {
        } while (this.queue.poll() != null);
    }

    public boolean containsValue(Object obj) {
        if (obj == null) {
            return containsNullValue();
        }
        Entry<K, V>[] table2 = getTable();
        int length = table2.length;
        while (true) {
            int i = length - 1;
            if (length <= 0) {
                return false;
            }
            for (Entry<K, V> entry = table2[i]; entry != null; entry = entry.next) {
                if (obj.equals(entry.value)) {
                    return true;
                }
            }
            length = i;
        }
    }

    private boolean containsNullValue() {
        Entry<K, V>[] table2 = getTable();
        int length = table2.length;
        while (true) {
            int i = length - 1;
            if (length <= 0) {
                return false;
            }
            for (Entry<K, V> entry = table2[i]; entry != null; entry = entry.next) {
                if (entry.value == null) {
                    return true;
                }
            }
            length = i;
        }
    }

    private static class Entry<K, V> extends WeakReference<Object> implements Map.Entry<K, V> {
        final int hash;
        Entry<K, V> next;
        V value;

        Entry(Object obj, V v, ReferenceQueue<Object> referenceQueue, int i, Entry<K, V> entry) {
            super(obj, referenceQueue);
            this.value = v;
            this.hash = i;
            this.next = entry;
        }

        public K getKey() {
            return WeakHashMap.unmaskNull(get());
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
            Object key = getKey();
            Object key2 = entry.getKey();
            if (key == key2 || (key != null && key.equals(key2))) {
                Object value2 = getValue();
                Object value3 = entry.getValue();
                if (value2 == value3) {
                    return true;
                }
                if (value2 == null || !value2.equals(value3)) {
                    return false;
                }
                return true;
            }
            return false;
        }

        public int hashCode() {
            Object key = getKey();
            Object value2 = getValue();
            return Objects.hashCode(value2) ^ Objects.hashCode(key);
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    private abstract class HashIterator<T> implements Iterator<T> {
        private Object currentKey;
        private Entry<K, V> entry;
        private int expectedModCount;
        private int index;
        private Entry<K, V> lastReturned;
        private Object nextKey;

        HashIterator() {
            this.expectedModCount = WeakHashMap.this.modCount;
            this.index = WeakHashMap.this.isEmpty() ? 0 : WeakHashMap.this.table.length;
        }

        public boolean hasNext() {
            Entry<K, V>[] entryArr = WeakHashMap.this.table;
            while (this.nextKey == null) {
                Entry<K, V> entry2 = this.entry;
                int i = this.index;
                while (entry2 == null && i > 0) {
                    i--;
                    entry2 = entryArr[i];
                }
                this.entry = entry2;
                this.index = i;
                if (entry2 == null) {
                    this.currentKey = null;
                    return false;
                }
                Object obj = entry2.get();
                this.nextKey = obj;
                if (obj == null) {
                    this.entry = this.entry.next;
                }
            }
            return true;
        }

        /* access modifiers changed from: protected */
        public Entry<K, V> nextEntry() {
            if (WeakHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else if (this.nextKey != null || hasNext()) {
                Entry<K, V> entry2 = this.entry;
                this.lastReturned = entry2;
                this.entry = entry2.next;
                this.currentKey = this.nextKey;
                this.nextKey = null;
                return this.lastReturned;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            } else if (WeakHashMap.this.modCount == this.expectedModCount) {
                WeakHashMap.this.remove(this.currentKey);
                this.expectedModCount = WeakHashMap.this.modCount;
                this.lastReturned = null;
                this.currentKey = null;
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class ValueIterator extends WeakHashMap<K, V>.HashIterator<V> {
        private ValueIterator() {
            super();
        }

        public V next() {
            return nextEntry().value;
        }
    }

    private class KeyIterator extends WeakHashMap<K, V>.HashIterator<K> {
        private KeyIterator() {
            super();
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    private class EntryIterator extends WeakHashMap<K, V>.HashIterator<Map.Entry<K, V>> {
        private EntryIterator() {
            super();
        }

        public Map.Entry<K, V> next() {
            return nextEntry();
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
            return WeakHashMap.this.size();
        }

        public boolean contains(Object obj) {
            return WeakHashMap.this.containsKey(obj);
        }

        public boolean remove(Object obj) {
            if (!WeakHashMap.this.containsKey(obj)) {
                return false;
            }
            WeakHashMap.this.remove(obj);
            return true;
        }

        public void clear() {
            WeakHashMap.this.clear();
        }

        public Spliterator<K> spliterator() {
            return new KeySpliterator(WeakHashMap.this, 0, -1, 0, 0);
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
            return WeakHashMap.this.size();
        }

        public boolean contains(Object obj) {
            return WeakHashMap.this.containsValue(obj);
        }

        public void clear() {
            WeakHashMap.this.clear();
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(WeakHashMap.this, 0, -1, 0, 0);
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
            Entry entry2 = WeakHashMap.this.getEntry(entry.getKey());
            if (entry2 == null || !entry2.equals(entry)) {
                return false;
            }
            return true;
        }

        public boolean remove(Object obj) {
            return WeakHashMap.this.removeMapping(obj);
        }

        public int size() {
            return WeakHashMap.this.size();
        }

        public void clear() {
            WeakHashMap.this.clear();
        }

        private List<Map.Entry<K, V>> deepCopy() {
            ArrayList arrayList = new ArrayList(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                arrayList.add(new AbstractMap.SimpleEntry((Map.Entry) it.next()));
            }
            return arrayList;
        }

        public Object[] toArray() {
            return deepCopy().toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return deepCopy().toArray(tArr);
        }

        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(WeakHashMap.this, 0, -1, 0, 0);
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        int i = this.modCount;
        for (Entry<K, V> entry : getTable()) {
            while (entry != null) {
                Object obj = entry.get();
                if (obj != null) {
                    biConsumer.accept(unmaskNull(obj), entry.value);
                }
                entry = entry.next;
                if (i != this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int i = this.modCount;
        for (Entry<K, V> entry : getTable()) {
            while (entry != null) {
                Object obj = entry.get();
                if (obj != null) {
                    entry.value = biFunction.apply(unmaskNull(obj), entry.value);
                }
                entry = entry.next;
                if (i != this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    static class WeakHashMapSpliterator<K, V> {
        Entry<K, V> current;
        int est;
        int expectedModCount;
        int fence;
        int index;
        final WeakHashMap<K, V> map;

        WeakHashMapSpliterator(WeakHashMap<K, V> weakHashMap, int i, int i2, int i3, int i4) {
            this.map = weakHashMap;
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
            WeakHashMap<K, V> weakHashMap = this.map;
            this.est = weakHashMap.size();
            this.expectedModCount = weakHashMap.modCount;
            int length = weakHashMap.table.length;
            this.fence = length;
            return length;
        }

        public final long estimateSize() {
            getFence();
            return (long) this.est;
        }
    }

    static final class KeySpliterator<K, V> extends WeakHashMapSpliterator<K, V> implements Spliterator<K> {
        public int characteristics() {
            return 1;
        }

        KeySpliterator(WeakHashMap<K, V> weakHashMap, int i, int i2, int i3, int i4) {
            super(weakHashMap, i, i2, i3, i4);
        }

        public KeySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = (fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            WeakHashMap weakHashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new KeySpliterator<>(weakHashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super K> consumer) {
            int i;
            int i2;
            consumer.getClass();
            WeakHashMap weakHashMap = this.map;
            Entry<K, V>[] entryArr = weakHashMap.table;
            int i3 = this.fence;
            if (i3 < 0) {
                int i4 = weakHashMap.modCount;
                this.expectedModCount = i4;
                int length = entryArr.length;
                this.fence = length;
                int i5 = length;
                i = i4;
                i3 = i5;
            } else {
                i = this.expectedModCount;
            }
            if (entryArr.length >= i3 && (i2 = this.index) >= 0) {
                this.index = i3;
                if (i2 < i3 || this.current != null) {
                    Entry<K, V> entry = this.current;
                    this.current = null;
                    while (true) {
                        if (entry == null) {
                            entry = entryArr[i2];
                            i2++;
                        } else {
                            Object obj = entry.get();
                            entry = entry.next;
                            if (obj != null) {
                                consumer.accept(WeakHashMap.unmaskNull(obj));
                            }
                        }
                        if (entry == null && i2 >= i3) {
                            break;
                        }
                    }
                }
            }
            if (weakHashMap.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean tryAdvance(Consumer<? super K> consumer) {
            consumer.getClass();
            Entry<K, V>[] entryArr = this.map.table;
            int length = entryArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current == null && this.index >= fence) {
                    return false;
                }
                if (this.current == null) {
                    int i = this.index;
                    this.index = i + 1;
                    this.current = entryArr[i];
                } else {
                    Object obj = this.current.get();
                    this.current = this.current.next;
                    if (obj != null) {
                        consumer.accept(WeakHashMap.unmaskNull(obj));
                        if (this.map.modCount == this.expectedModCount) {
                            return true;
                        }
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }
    }

    static final class ValueSpliterator<K, V> extends WeakHashMapSpliterator<K, V> implements Spliterator<V> {
        public int characteristics() {
            return 0;
        }

        ValueSpliterator(WeakHashMap<K, V> weakHashMap, int i, int i2, int i3, int i4) {
            super(weakHashMap, i, i2, i3, i4);
        }

        public ValueSpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = (fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            WeakHashMap weakHashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new ValueSpliterator<>(weakHashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super V> consumer) {
            int i;
            int i2;
            consumer.getClass();
            WeakHashMap weakHashMap = this.map;
            Entry<K, V>[] entryArr = weakHashMap.table;
            int i3 = this.fence;
            if (i3 < 0) {
                int i4 = weakHashMap.modCount;
                this.expectedModCount = i4;
                int length = entryArr.length;
                this.fence = length;
                int i5 = length;
                i = i4;
                i3 = i5;
            } else {
                i = this.expectedModCount;
            }
            if (entryArr.length >= i3 && (i2 = this.index) >= 0) {
                this.index = i3;
                if (i2 < i3 || this.current != null) {
                    Entry<K, V> entry = this.current;
                    this.current = null;
                    while (true) {
                        if (entry == null) {
                            entry = entryArr[i2];
                            i2++;
                        } else {
                            Object obj = entry.get();
                            V v = entry.value;
                            entry = entry.next;
                            if (obj != null) {
                                consumer.accept(v);
                            }
                        }
                        if (entry == null && i2 >= i3) {
                            break;
                        }
                    }
                }
            }
            if (weakHashMap.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean tryAdvance(Consumer<? super V> consumer) {
            consumer.getClass();
            Entry<K, V>[] entryArr = this.map.table;
            int length = entryArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current == null && this.index >= fence) {
                    return false;
                }
                if (this.current == null) {
                    int i = this.index;
                    this.index = i + 1;
                    this.current = entryArr[i];
                } else {
                    Object obj = this.current.get();
                    V v = this.current.value;
                    this.current = this.current.next;
                    if (obj != null) {
                        consumer.accept(v);
                        if (this.map.modCount == this.expectedModCount) {
                            return true;
                        }
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }
    }

    static final class EntrySpliterator<K, V> extends WeakHashMapSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        public int characteristics() {
            return 1;
        }

        EntrySpliterator(WeakHashMap<K, V> weakHashMap, int i, int i2, int i3, int i4) {
            super(weakHashMap, i, i2, i3, i4);
        }

        public EntrySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = (fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            WeakHashMap weakHashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new EntrySpliterator<>(weakHashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            int i;
            int i2;
            consumer.getClass();
            WeakHashMap weakHashMap = this.map;
            Entry<K, V>[] entryArr = weakHashMap.table;
            int i3 = this.fence;
            if (i3 < 0) {
                int i4 = weakHashMap.modCount;
                this.expectedModCount = i4;
                int length = entryArr.length;
                this.fence = length;
                int i5 = length;
                i = i4;
                i3 = i5;
            } else {
                i = this.expectedModCount;
            }
            if (entryArr.length >= i3 && (i2 = this.index) >= 0) {
                this.index = i3;
                if (i2 < i3 || this.current != null) {
                    Entry<K, V> entry = this.current;
                    this.current = null;
                    while (true) {
                        if (entry == null) {
                            entry = entryArr[i2];
                            i2++;
                        } else {
                            Object obj = entry.get();
                            V v = entry.value;
                            entry = entry.next;
                            if (obj != null) {
                                consumer.accept(new AbstractMap.SimpleImmutableEntry(WeakHashMap.unmaskNull(obj), v));
                            }
                        }
                        if (entry == null && i2 >= i3) {
                            break;
                        }
                    }
                }
            }
            if (weakHashMap.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            Entry<K, V>[] entryArr = this.map.table;
            int length = entryArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current == null && this.index >= fence) {
                    return false;
                }
                if (this.current == null) {
                    int i = this.index;
                    this.index = i + 1;
                    this.current = entryArr[i];
                } else {
                    Object obj = this.current.get();
                    V v = this.current.value;
                    this.current = this.current.next;
                    if (obj != null) {
                        consumer.accept(new AbstractMap.SimpleImmutableEntry(WeakHashMap.unmaskNull(obj), v));
                        if (this.map.modCount == this.expectedModCount) {
                            return true;
                        }
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }
    }
}
