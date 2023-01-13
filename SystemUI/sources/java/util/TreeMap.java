package java.util;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TreeMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Cloneable, Serializable {
    private static final boolean BLACK = true;
    private static final boolean RED = false;
    /* access modifiers changed from: private */
    public static final Object UNBOUNDED = new Object();
    private static final long serialVersionUID = 919286545866124006L;
    /* access modifiers changed from: private */
    public final Comparator<? super K> comparator;
    private transient NavigableMap<K, V> descendingMap;
    private transient TreeMap<K, V>.EntrySet entrySet;
    /* access modifiers changed from: private */
    public transient int modCount;
    private transient KeySet<K> navigableKeySet;
    /* access modifiers changed from: private */
    public transient TreeMapEntry<K, V> root;
    /* access modifiers changed from: private */
    public transient int size;

    public TreeMap() {
        this.size = 0;
        this.modCount = 0;
        this.comparator = null;
    }

    public TreeMap(Comparator<? super K> comparator2) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = comparator2;
    }

    public TreeMap(Map<? extends K, ? extends V> map) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = null;
        putAll(map);
    }

    public TreeMap(SortedMap<K, ? extends V> sortedMap) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = sortedMap.comparator();
        try {
            buildFromSorted(sortedMap.size(), sortedMap.entrySet().iterator(), (ObjectInputStream) null, (Object) null);
        } catch (IOException | ClassNotFoundException unused) {
        }
    }

    public int size() {
        return this.size;
    }

    public boolean containsKey(Object obj) {
        return getEntry(obj) != null;
    }

    public boolean containsValue(Object obj) {
        for (TreeMapEntry firstEntry = getFirstEntry(); firstEntry != null; firstEntry = successor(firstEntry)) {
            if (valEquals(obj, firstEntry.value)) {
                return true;
            }
        }
        return false;
    }

    public V get(Object obj) {
        TreeMapEntry entry = getEntry(obj);
        if (entry == null) {
            return null;
        }
        return entry.value;
    }

    public Comparator<? super K> comparator() {
        return this.comparator;
    }

    public K firstKey() {
        return key(getFirstEntry());
    }

    public K lastKey() {
        return key(getLastEntry());
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        Comparator<? super K> comparator2;
        Comparator<? super K> comparator3;
        int size2 = map.size();
        if (this.size != 0 || size2 == 0 || !(map instanceof SortedMap) || ((comparator2 = ((SortedMap) map).comparator()) != (comparator3 = this.comparator) && (comparator2 == null || !comparator2.equals(comparator3)))) {
            super.putAll(map);
            return;
        }
        this.modCount++;
        try {
            buildFromSorted(size2, map.entrySet().iterator(), (ObjectInputStream) null, (Object) null);
        } catch (IOException | ClassNotFoundException unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public final TreeMapEntry<K, V> getEntry(Object obj) {
        if (this.comparator != null) {
            return getEntryUsingComparator(obj);
        }
        obj.getClass();
        Comparable comparable = (Comparable) obj;
        TreeMapEntry<K, V> treeMapEntry = this.root;
        while (treeMapEntry != null) {
            int compareTo = comparable.compareTo(treeMapEntry.key);
            if (compareTo < 0) {
                treeMapEntry = treeMapEntry.left;
            } else if (compareTo <= 0) {
                return treeMapEntry;
            } else {
                treeMapEntry = treeMapEntry.right;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public final TreeMapEntry<K, V> getEntryUsingComparator(Object obj) {
        Comparator<? super K> comparator2 = this.comparator;
        if (comparator2 == null) {
            return null;
        }
        TreeMapEntry<K, V> treeMapEntry = this.root;
        while (treeMapEntry != null) {
            int compare = comparator2.compare(obj, treeMapEntry.key);
            if (compare < 0) {
                treeMapEntry = treeMapEntry.left;
            } else if (compare <= 0) {
                return treeMapEntry;
            } else {
                treeMapEntry = treeMapEntry.right;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public final TreeMapEntry<K, V> getCeilingEntry(K k) {
        TreeMapEntry<K, V> treeMapEntry = this.root;
        while (treeMapEntry != null) {
            int compare = compare(k, treeMapEntry.key);
            if (compare >= 0) {
                if (compare > 0) {
                    if (treeMapEntry.right == null) {
                        TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.parent;
                        while (true) {
                            TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry;
                            treeMapEntry = treeMapEntry2;
                            TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3;
                            if (treeMapEntry == null || treeMapEntry4 != treeMapEntry.right) {
                                break;
                            }
                            treeMapEntry2 = treeMapEntry.parent;
                        }
                    } else {
                        treeMapEntry = treeMapEntry.right;
                    }
                }
                return treeMapEntry;
            } else if (treeMapEntry.left == null) {
                return treeMapEntry;
            } else {
                treeMapEntry = treeMapEntry.left;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public final TreeMapEntry<K, V> getFloorEntry(K k) {
        TreeMapEntry<K, V> treeMapEntry = this.root;
        while (treeMapEntry != null) {
            int compare = compare(k, treeMapEntry.key);
            if (compare <= 0) {
                if (compare < 0) {
                    if (treeMapEntry.left == null) {
                        TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.parent;
                        while (true) {
                            TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry;
                            treeMapEntry = treeMapEntry2;
                            TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3;
                            if (treeMapEntry == null || treeMapEntry4 != treeMapEntry.left) {
                                break;
                            }
                            treeMapEntry2 = treeMapEntry.parent;
                        }
                    } else {
                        treeMapEntry = treeMapEntry.left;
                    }
                }
                return treeMapEntry;
            } else if (treeMapEntry.right == null) {
                return treeMapEntry;
            } else {
                treeMapEntry = treeMapEntry.right;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public final TreeMapEntry<K, V> getHigherEntry(K k) {
        TreeMapEntry<K, V> treeMapEntry = this.root;
        while (treeMapEntry != null) {
            if (compare(k, treeMapEntry.key) < 0) {
                if (treeMapEntry.left == null) {
                    return treeMapEntry;
                }
                treeMapEntry = treeMapEntry.left;
            } else if (treeMapEntry.right != null) {
                treeMapEntry = treeMapEntry.right;
            } else {
                TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.parent;
                while (true) {
                    TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry;
                    treeMapEntry = treeMapEntry2;
                    TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3;
                    if (treeMapEntry == null || treeMapEntry4 != treeMapEntry.right) {
                        return treeMapEntry;
                    }
                    treeMapEntry2 = treeMapEntry.parent;
                }
                return treeMapEntry;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public final TreeMapEntry<K, V> getLowerEntry(K k) {
        TreeMapEntry<K, V> treeMapEntry = this.root;
        while (treeMapEntry != null) {
            if (compare(k, treeMapEntry.key) > 0) {
                if (treeMapEntry.right == null) {
                    return treeMapEntry;
                }
                treeMapEntry = treeMapEntry.right;
            } else if (treeMapEntry.left != null) {
                treeMapEntry = treeMapEntry.left;
            } else {
                TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.parent;
                while (true) {
                    TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry;
                    treeMapEntry = treeMapEntry2;
                    TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3;
                    if (treeMapEntry == null || treeMapEntry4 != treeMapEntry.left) {
                        return treeMapEntry;
                    }
                    treeMapEntry2 = treeMapEntry.parent;
                }
                return treeMapEntry;
            }
        }
        return null;
    }

    public V put(K k, V v) {
        int i;
        TreeMapEntry<K, V> treeMapEntry;
        TreeMapEntry<K, V> treeMapEntry2;
        TreeMapEntry<K, V> treeMapEntry3 = this.root;
        if (treeMapEntry3 == null) {
            compare(k, k);
            this.root = new TreeMapEntry<>(k, v, (TreeMapEntry) null);
            this.size = 1;
            this.modCount++;
            return null;
        }
        Comparator<? super K> comparator2 = this.comparator;
        if (comparator2 != null) {
            while (true) {
                i = comparator2.compare(k, treeMapEntry3.key);
                if (i < 0) {
                    treeMapEntry2 = treeMapEntry3.left;
                } else if (i <= 0) {
                    return treeMapEntry3.setValue(v);
                } else {
                    treeMapEntry2 = treeMapEntry3.right;
                }
                if (treeMapEntry2 == null) {
                    break;
                }
                treeMapEntry3 = treeMapEntry2;
            }
        } else {
            k.getClass();
            Comparable comparable = (Comparable) k;
            while (true) {
                i = comparable.compareTo(treeMapEntry3.key);
                if (i < 0) {
                    treeMapEntry = treeMapEntry3.left;
                } else if (i <= 0) {
                    return treeMapEntry3.setValue(v);
                } else {
                    treeMapEntry = treeMapEntry3.right;
                }
                if (treeMapEntry == null) {
                    break;
                }
                treeMapEntry3 = treeMapEntry;
            }
        }
        TreeMapEntry<K, V> treeMapEntry4 = new TreeMapEntry<>(k, v, treeMapEntry3);
        if (i < 0) {
            treeMapEntry3.left = treeMapEntry4;
        } else {
            treeMapEntry3.right = treeMapEntry4;
        }
        fixAfterInsertion(treeMapEntry4);
        this.size++;
        this.modCount++;
        return null;
    }

    public V remove(Object obj) {
        TreeMapEntry entry = getEntry(obj);
        if (entry == null) {
            return null;
        }
        V v = entry.value;
        deleteEntry(entry);
        return v;
    }

    public void clear() {
        this.modCount++;
        this.size = 0;
        this.root = null;
    }

    public Object clone() {
        try {
            TreeMap treeMap = (TreeMap) super.clone();
            treeMap.root = null;
            treeMap.size = 0;
            treeMap.modCount = 0;
            treeMap.entrySet = null;
            treeMap.navigableKeySet = null;
            treeMap.descendingMap = null;
            try {
                treeMap.buildFromSorted(this.size, entrySet().iterator(), (ObjectInputStream) null, (Object) null);
            } catch (IOException | ClassNotFoundException unused) {
            }
            return treeMap;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    public Map.Entry<K, V> firstEntry() {
        return exportEntry(getFirstEntry());
    }

    public Map.Entry<K, V> lastEntry() {
        return exportEntry(getLastEntry());
    }

    public Map.Entry<K, V> pollFirstEntry() {
        TreeMapEntry firstEntry = getFirstEntry();
        Map.Entry<K, V> exportEntry = exportEntry(firstEntry);
        if (firstEntry != null) {
            deleteEntry(firstEntry);
        }
        return exportEntry;
    }

    public Map.Entry<K, V> pollLastEntry() {
        TreeMapEntry lastEntry = getLastEntry();
        Map.Entry<K, V> exportEntry = exportEntry(lastEntry);
        if (lastEntry != null) {
            deleteEntry(lastEntry);
        }
        return exportEntry;
    }

    public Map.Entry<K, V> lowerEntry(K k) {
        return exportEntry(getLowerEntry(k));
    }

    public K lowerKey(K k) {
        return keyOrNull(getLowerEntry(k));
    }

    public Map.Entry<K, V> floorEntry(K k) {
        return exportEntry(getFloorEntry(k));
    }

    public K floorKey(K k) {
        return keyOrNull(getFloorEntry(k));
    }

    public Map.Entry<K, V> ceilingEntry(K k) {
        return exportEntry(getCeilingEntry(k));
    }

    public K ceilingKey(K k) {
        return keyOrNull(getCeilingEntry(k));
    }

    public Map.Entry<K, V> higherEntry(K k) {
        return exportEntry(getHigherEntry(k));
    }

    public K higherKey(K k) {
        return keyOrNull(getHigherEntry(k));
    }

    public Set<K> keySet() {
        return navigableKeySet();
    }

    public NavigableSet<K> navigableKeySet() {
        KeySet<K> keySet = this.navigableKeySet;
        if (keySet != null) {
            return keySet;
        }
        KeySet<K> keySet2 = new KeySet<>(this);
        this.navigableKeySet = keySet2;
        return keySet2;
    }

    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
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

    public Set<Map.Entry<K, V>> entrySet() {
        TreeMap<K, V>.EntrySet entrySet2 = this.entrySet;
        if (entrySet2 != null) {
            return entrySet2;
        }
        TreeMap<K, V>.EntrySet entrySet3 = new EntrySet();
        this.entrySet = entrySet3;
        return entrySet3;
    }

    public NavigableMap<K, V> descendingMap() {
        NavigableMap<K, V> navigableMap = this.descendingMap;
        if (navigableMap != null) {
            return navigableMap;
        }
        DescendingSubMap descendingSubMap = new DescendingSubMap(this, true, null, true, true, null, true);
        this.descendingMap = descendingSubMap;
        return descendingSubMap;
    }

    public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
        return new AscendingSubMap(this, false, k, z, false, k2, z2);
    }

    public NavigableMap<K, V> headMap(K k, boolean z) {
        return new AscendingSubMap(this, true, null, true, false, k, z);
    }

    public NavigableMap<K, V> tailMap(K k, boolean z) {
        return new AscendingSubMap(this, false, k, z, true, null, true);
    }

    public SortedMap<K, V> subMap(K k, K k2) {
        return subMap(k, true, k2, false);
    }

    public SortedMap<K, V> headMap(K k) {
        return headMap(k, false);
    }

    public SortedMap<K, V> tailMap(K k) {
        return tailMap(k, true);
    }

    public boolean replace(K k, V v, V v2) {
        TreeMapEntry entry = getEntry(k);
        if (entry == null || !Objects.equals(v, entry.value)) {
            return false;
        }
        entry.value = v2;
        return true;
    }

    public V replace(K k, V v) {
        TreeMapEntry entry = getEntry(k);
        if (entry == null) {
            return null;
        }
        V v2 = entry.value;
        entry.value = v;
        return v2;
    }

    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        int i = this.modCount;
        TreeMapEntry firstEntry = getFirstEntry();
        while (firstEntry != null) {
            biConsumer.accept(firstEntry.key, firstEntry.value);
            if (i == this.modCount) {
                firstEntry = successor(firstEntry);
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int i = this.modCount;
        TreeMapEntry firstEntry = getFirstEntry();
        while (firstEntry != null) {
            firstEntry.value = biFunction.apply(firstEntry.key, firstEntry.value);
            if (i == this.modCount) {
                firstEntry = successor(firstEntry);
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    class Values extends AbstractCollection<V> {
        Values() {
        }

        public Iterator<V> iterator() {
            TreeMap treeMap = TreeMap.this;
            return new ValueIterator(treeMap.getFirstEntry());
        }

        public int size() {
            return TreeMap.this.size();
        }

        public boolean contains(Object obj) {
            return TreeMap.this.containsValue(obj);
        }

        public boolean remove(Object obj) {
            for (TreeMapEntry firstEntry = TreeMap.this.getFirstEntry(); firstEntry != null; firstEntry = TreeMap.successor(firstEntry)) {
                if (TreeMap.valEquals(firstEntry.getValue(), obj)) {
                    TreeMap.this.deleteEntry(firstEntry);
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            TreeMap.this.clear();
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(TreeMap.this, (TreeMapEntry) null, (TreeMapEntry) null, 0, -1, 0);
        }
    }

    class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            TreeMap treeMap = TreeMap.this;
            return new EntryIterator(treeMap.getFirstEntry());
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object value = entry.getValue();
            TreeMapEntry entry2 = TreeMap.this.getEntry(entry.getKey());
            if (entry2 == null || !TreeMap.valEquals(entry2.getValue(), value)) {
                return false;
            }
            return true;
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object value = entry.getValue();
            TreeMapEntry entry2 = TreeMap.this.getEntry(entry.getKey());
            if (entry2 == null || !TreeMap.valEquals(entry2.getValue(), value)) {
                return false;
            }
            TreeMap.this.deleteEntry(entry2);
            return true;
        }

        public int size() {
            return TreeMap.this.size();
        }

        public void clear() {
            TreeMap.this.clear();
        }

        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(TreeMap.this, (TreeMapEntry) null, (TreeMapEntry) null, 0, -1, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public Iterator<K> keyIterator() {
        return new KeyIterator(getFirstEntry());
    }

    /* access modifiers changed from: package-private */
    public Iterator<K> descendingKeyIterator() {
        return new DescendingKeyIterator(getLastEntry());
    }

    static final class KeySet<E> extends AbstractSet<E> implements NavigableSet<E> {

        /* renamed from: m */
        private final NavigableMap<E, ?> f709m;

        KeySet(NavigableMap<E, ?> navigableMap) {
            this.f709m = navigableMap;
        }

        public Iterator<E> iterator() {
            NavigableMap<E, ?> navigableMap = this.f709m;
            if (navigableMap instanceof TreeMap) {
                return ((TreeMap) navigableMap).keyIterator();
            }
            return ((NavigableSubMap) navigableMap).keyIterator();
        }

        public Iterator<E> descendingIterator() {
            NavigableMap<E, ?> navigableMap = this.f709m;
            if (navigableMap instanceof TreeMap) {
                return ((TreeMap) navigableMap).descendingKeyIterator();
            }
            return ((NavigableSubMap) navigableMap).descendingKeyIterator();
        }

        public int size() {
            return this.f709m.size();
        }

        public boolean isEmpty() {
            return this.f709m.isEmpty();
        }

        public boolean contains(Object obj) {
            return this.f709m.containsKey(obj);
        }

        public void clear() {
            this.f709m.clear();
        }

        public E lower(E e) {
            return this.f709m.lowerKey(e);
        }

        public E floor(E e) {
            return this.f709m.floorKey(e);
        }

        public E ceiling(E e) {
            return this.f709m.ceilingKey(e);
        }

        public E higher(E e) {
            return this.f709m.higherKey(e);
        }

        public E first() {
            return this.f709m.firstKey();
        }

        public E last() {
            return this.f709m.lastKey();
        }

        public Comparator<? super E> comparator() {
            return this.f709m.comparator();
        }

        public E pollFirst() {
            Map.Entry<E, ?> pollFirstEntry = this.f709m.pollFirstEntry();
            if (pollFirstEntry == null) {
                return null;
            }
            return pollFirstEntry.getKey();
        }

        public E pollLast() {
            Map.Entry<E, ?> pollLastEntry = this.f709m.pollLastEntry();
            if (pollLastEntry == null) {
                return null;
            }
            return pollLastEntry.getKey();
        }

        public boolean remove(Object obj) {
            int size = size();
            this.f709m.remove(obj);
            return size() != size;
        }

        public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
            return new KeySet(this.f709m.subMap(e, z, e2, z2));
        }

        public NavigableSet<E> headSet(E e, boolean z) {
            return new KeySet(this.f709m.headMap(e, z));
        }

        public NavigableSet<E> tailSet(E e, boolean z) {
            return new KeySet(this.f709m.tailMap(e, z));
        }

        public SortedSet<E> subSet(E e, E e2) {
            return subSet(e, true, e2, false);
        }

        public SortedSet<E> headSet(E e) {
            return headSet(e, false);
        }

        public SortedSet<E> tailSet(E e) {
            return tailSet(e, true);
        }

        public NavigableSet<E> descendingSet() {
            return new KeySet(this.f709m.descendingMap());
        }

        public Spliterator<E> spliterator() {
            return TreeMap.keySpliteratorFor(this.f709m);
        }
    }

    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        int expectedModCount;
        TreeMapEntry<K, V> lastReturned = null;
        TreeMapEntry<K, V> next;

        PrivateEntryIterator(TreeMapEntry<K, V> treeMapEntry) {
            this.expectedModCount = TreeMap.this.modCount;
            this.next = treeMapEntry;
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> nextEntry() {
            TreeMapEntry<K, V> treeMapEntry = this.next;
            if (treeMapEntry == null) {
                throw new NoSuchElementException();
            } else if (TreeMap.this.modCount == this.expectedModCount) {
                this.next = TreeMap.successor(treeMapEntry);
                this.lastReturned = treeMapEntry;
                return treeMapEntry;
            } else {
                throw new ConcurrentModificationException();
            }
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> prevEntry() {
            TreeMapEntry<K, V> treeMapEntry = this.next;
            if (treeMapEntry == null) {
                throw new NoSuchElementException();
            } else if (TreeMap.this.modCount == this.expectedModCount) {
                this.next = TreeMap.predecessor(treeMapEntry);
                this.lastReturned = treeMapEntry;
                return treeMapEntry;
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            } else if (TreeMap.this.modCount == this.expectedModCount) {
                if (!(this.lastReturned.left == null || this.lastReturned.right == null)) {
                    this.next = this.lastReturned;
                }
                TreeMap.this.deleteEntry(this.lastReturned);
                this.expectedModCount = TreeMap.this.modCount;
                this.lastReturned = null;
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    final class EntryIterator extends TreeMap<K, V>.PrivateEntryIterator<Map.Entry<K, V>> {
        EntryIterator(TreeMapEntry<K, V> treeMapEntry) {
            super(treeMapEntry);
        }

        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    final class ValueIterator extends TreeMap<K, V>.PrivateEntryIterator<V> {
        ValueIterator(TreeMapEntry<K, V> treeMapEntry) {
            super(treeMapEntry);
        }

        public V next() {
            return nextEntry().value;
        }
    }

    final class KeyIterator extends TreeMap<K, V>.PrivateEntryIterator<K> {
        KeyIterator(TreeMapEntry<K, V> treeMapEntry) {
            super(treeMapEntry);
        }

        public K next() {
            return nextEntry().key;
        }
    }

    final class DescendingKeyIterator extends TreeMap<K, V>.PrivateEntryIterator<K> {
        DescendingKeyIterator(TreeMapEntry<K, V> treeMapEntry) {
            super(treeMapEntry);
        }

        public K next() {
            return prevEntry().key;
        }

        public void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            } else if (TreeMap.this.modCount == this.expectedModCount) {
                TreeMap.this.deleteEntry(this.lastReturned);
                this.lastReturned = null;
                this.expectedModCount = TreeMap.this.modCount;
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final int compare(Object obj, Object obj2) {
        Comparator<? super K> comparator2 = this.comparator;
        if (comparator2 == null) {
            return ((Comparable) obj).compareTo(obj2);
        }
        return comparator2.compare(obj, obj2);
    }

    static final boolean valEquals(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    static <K, V> Map.Entry<K, V> exportEntry(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry == null) {
            return null;
        }
        return new AbstractMap.SimpleImmutableEntry(treeMapEntry);
    }

    static <K, V> K keyOrNull(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry == null) {
            return null;
        }
        return treeMapEntry.key;
    }

    static <K> K key(TreeMapEntry<K, ?> treeMapEntry) {
        if (treeMapEntry != null) {
            return treeMapEntry.key;
        }
        throw new NoSuchElementException();
    }

    static abstract class NavigableSubMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Serializable {
        private static final long serialVersionUID = 2765629423043303731L;
        transient NavigableMap<K, V> descendingMapView;
        transient NavigableSubMap<K, V>.EntrySetView entrySetView;
        final boolean fromStart;

        /* renamed from: hi */
        final K f710hi;
        final boolean hiInclusive;

        /* renamed from: lo */
        final K f711lo;
        final boolean loInclusive;

        /* renamed from: m */
        final TreeMap<K, V> f712m;
        transient KeySet<K> navigableKeySetView;
        final boolean toEnd;

        /* access modifiers changed from: package-private */
        public abstract Iterator<K> descendingKeyIterator();

        /* access modifiers changed from: package-private */
        public abstract Iterator<K> keyIterator();

        /* access modifiers changed from: package-private */
        public abstract Spliterator<K> keySpliterator();

        /* access modifiers changed from: package-private */
        public abstract TreeMapEntry<K, V> subCeiling(K k);

        /* access modifiers changed from: package-private */
        public abstract TreeMapEntry<K, V> subFloor(K k);

        /* access modifiers changed from: package-private */
        public abstract TreeMapEntry<K, V> subHigher(K k);

        /* access modifiers changed from: package-private */
        public abstract TreeMapEntry<K, V> subHighest();

        /* access modifiers changed from: package-private */
        public abstract TreeMapEntry<K, V> subLower(K k);

        /* access modifiers changed from: package-private */
        public abstract TreeMapEntry<K, V> subLowest();

        NavigableSubMap(TreeMap<K, V> treeMap, boolean z, K k, boolean z2, boolean z3, K k2, boolean z4) {
            if (z || z3) {
                if (!z) {
                    treeMap.compare(k, k);
                }
                if (!z3) {
                    treeMap.compare(k2, k2);
                }
            } else if (treeMap.compare(k, k2) > 0) {
                throw new IllegalArgumentException("fromKey > toKey");
            }
            this.f712m = treeMap;
            this.fromStart = z;
            this.f711lo = k;
            this.loInclusive = z2;
            this.toEnd = z3;
            this.f710hi = k2;
            this.hiInclusive = z4;
        }

        /* access modifiers changed from: package-private */
        public final boolean tooLow(Object obj) {
            if (this.fromStart) {
                return false;
            }
            int compare = this.f712m.compare(obj, this.f711lo);
            if (compare >= 0) {
                return compare == 0 && !this.loInclusive;
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public final boolean tooHigh(Object obj) {
            if (this.toEnd) {
                return false;
            }
            int compare = this.f712m.compare(obj, this.f710hi);
            if (compare <= 0) {
                return compare == 0 && !this.hiInclusive;
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public final boolean inRange(Object obj) {
            return !tooLow(obj) && !tooHigh(obj);
        }

        /* access modifiers changed from: package-private */
        public final boolean inClosedRange(Object obj) {
            return (this.fromStart || this.f712m.compare(obj, this.f711lo) >= 0) && (this.toEnd || this.f712m.compare(this.f710hi, obj) >= 0);
        }

        /* access modifiers changed from: package-private */
        public final boolean inRange(Object obj, boolean z) {
            return z ? inRange(obj) : inClosedRange(obj);
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> absLowest() {
            TreeMapEntry<K, V> treeMapEntry;
            if (this.fromStart) {
                treeMapEntry = this.f712m.getFirstEntry();
            } else if (this.loInclusive) {
                treeMapEntry = this.f712m.getCeilingEntry(this.f711lo);
            } else {
                treeMapEntry = this.f712m.getHigherEntry(this.f711lo);
            }
            if (treeMapEntry == null || tooHigh(treeMapEntry.key)) {
                return null;
            }
            return treeMapEntry;
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> absHighest() {
            TreeMapEntry<K, V> treeMapEntry;
            if (this.toEnd) {
                treeMapEntry = this.f712m.getLastEntry();
            } else if (this.hiInclusive) {
                treeMapEntry = this.f712m.getFloorEntry(this.f710hi);
            } else {
                treeMapEntry = this.f712m.getLowerEntry(this.f710hi);
            }
            if (treeMapEntry == null || tooLow(treeMapEntry.key)) {
                return null;
            }
            return treeMapEntry;
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> absCeiling(K k) {
            if (tooLow(k)) {
                return absLowest();
            }
            TreeMapEntry<K, V> ceilingEntry = this.f712m.getCeilingEntry(k);
            if (ceilingEntry == null || tooHigh(ceilingEntry.key)) {
                return null;
            }
            return ceilingEntry;
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> absHigher(K k) {
            if (tooLow(k)) {
                return absLowest();
            }
            TreeMapEntry<K, V> higherEntry = this.f712m.getHigherEntry(k);
            if (higherEntry == null || tooHigh(higherEntry.key)) {
                return null;
            }
            return higherEntry;
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> absFloor(K k) {
            if (tooHigh(k)) {
                return absHighest();
            }
            TreeMapEntry<K, V> floorEntry = this.f712m.getFloorEntry(k);
            if (floorEntry == null || tooLow(floorEntry.key)) {
                return null;
            }
            return floorEntry;
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> absLower(K k) {
            if (tooHigh(k)) {
                return absHighest();
            }
            TreeMapEntry<K, V> lowerEntry = this.f712m.getLowerEntry(k);
            if (lowerEntry == null || tooLow(lowerEntry.key)) {
                return null;
            }
            return lowerEntry;
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> absHighFence() {
            if (this.toEnd) {
                return null;
            }
            if (this.hiInclusive) {
                return this.f712m.getHigherEntry(this.f710hi);
            }
            return this.f712m.getCeilingEntry(this.f710hi);
        }

        /* access modifiers changed from: package-private */
        public final TreeMapEntry<K, V> absLowFence() {
            if (this.fromStart) {
                return null;
            }
            if (this.loInclusive) {
                return this.f712m.getLowerEntry(this.f711lo);
            }
            return this.f712m.getFloorEntry(this.f711lo);
        }

        public boolean isEmpty() {
            return (!this.fromStart || !this.toEnd) ? entrySet().isEmpty() : this.f712m.isEmpty();
        }

        public int size() {
            return (!this.fromStart || !this.toEnd) ? entrySet().size() : this.f712m.size();
        }

        public final boolean containsKey(Object obj) {
            return inRange(obj) && this.f712m.containsKey(obj);
        }

        public final V put(K k, V v) {
            if (inRange(k)) {
                return this.f712m.put(k, v);
            }
            throw new IllegalArgumentException("key out of range");
        }

        public final V get(Object obj) {
            if (!inRange(obj)) {
                return null;
            }
            return this.f712m.get(obj);
        }

        public final V remove(Object obj) {
            if (!inRange(obj)) {
                return null;
            }
            return this.f712m.remove(obj);
        }

        public final Map.Entry<K, V> ceilingEntry(K k) {
            return TreeMap.exportEntry(subCeiling(k));
        }

        public final K ceilingKey(K k) {
            return TreeMap.keyOrNull(subCeiling(k));
        }

        public final Map.Entry<K, V> higherEntry(K k) {
            return TreeMap.exportEntry(subHigher(k));
        }

        public final K higherKey(K k) {
            return TreeMap.keyOrNull(subHigher(k));
        }

        public final Map.Entry<K, V> floorEntry(K k) {
            return TreeMap.exportEntry(subFloor(k));
        }

        public final K floorKey(K k) {
            return TreeMap.keyOrNull(subFloor(k));
        }

        public final Map.Entry<K, V> lowerEntry(K k) {
            return TreeMap.exportEntry(subLower(k));
        }

        public final K lowerKey(K k) {
            return TreeMap.keyOrNull(subLower(k));
        }

        public final K firstKey() {
            return TreeMap.key(subLowest());
        }

        public final K lastKey() {
            return TreeMap.key(subHighest());
        }

        public final Map.Entry<K, V> firstEntry() {
            return TreeMap.exportEntry(subLowest());
        }

        public final Map.Entry<K, V> lastEntry() {
            return TreeMap.exportEntry(subHighest());
        }

        public final Map.Entry<K, V> pollFirstEntry() {
            TreeMapEntry subLowest = subLowest();
            Map.Entry<K, V> exportEntry = TreeMap.exportEntry(subLowest);
            if (subLowest != null) {
                this.f712m.deleteEntry(subLowest);
            }
            return exportEntry;
        }

        public final Map.Entry<K, V> pollLastEntry() {
            TreeMapEntry subHighest = subHighest();
            Map.Entry<K, V> exportEntry = TreeMap.exportEntry(subHighest);
            if (subHighest != null) {
                this.f712m.deleteEntry(subHighest);
            }
            return exportEntry;
        }

        public final NavigableSet<K> navigableKeySet() {
            KeySet<K> keySet = this.navigableKeySetView;
            if (keySet != null) {
                return keySet;
            }
            KeySet<K> keySet2 = new KeySet<>(this);
            this.navigableKeySetView = keySet2;
            return keySet2;
        }

        public final Set<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        public final SortedMap<K, V> subMap(K k, K k2) {
            return subMap(k, true, k2, false);
        }

        public final SortedMap<K, V> headMap(K k) {
            return headMap(k, false);
        }

        public final SortedMap<K, V> tailMap(K k) {
            return tailMap(k, true);
        }

        abstract class EntrySetView extends AbstractSet<Map.Entry<K, V>> {
            private transient int size = -1;
            private transient int sizeModCount;

            EntrySetView() {
            }

            public int size() {
                if (NavigableSubMap.this.fromStart && NavigableSubMap.this.toEnd) {
                    return NavigableSubMap.this.f712m.size();
                }
                if (this.size == -1 || this.sizeModCount != NavigableSubMap.this.f712m.modCount) {
                    this.sizeModCount = NavigableSubMap.this.f712m.modCount;
                    this.size = 0;
                    Iterator it = iterator();
                    while (it.hasNext()) {
                        this.size++;
                        it.next();
                    }
                }
                return this.size;
            }

            public boolean isEmpty() {
                TreeMapEntry absLowest = NavigableSubMap.this.absLowest();
                return absLowest == null || NavigableSubMap.this.tooHigh(absLowest.key);
            }

            public boolean contains(Object obj) {
                TreeMapEntry<K, V> entry;
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry2 = (Map.Entry) obj;
                Object key = entry2.getKey();
                if (NavigableSubMap.this.inRange(key) && (entry = NavigableSubMap.this.f712m.getEntry(key)) != null && TreeMap.valEquals(entry.getValue(), entry2.getValue())) {
                    return true;
                }
                return false;
            }

            public boolean remove(Object obj) {
                TreeMapEntry<K, V> entry;
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry2 = (Map.Entry) obj;
                Object key = entry2.getKey();
                if (!NavigableSubMap.this.inRange(key) || (entry = NavigableSubMap.this.f712m.getEntry(key)) == null || !TreeMap.valEquals(entry.getValue(), entry2.getValue())) {
                    return false;
                }
                NavigableSubMap.this.f712m.deleteEntry(entry);
                return true;
            }
        }

        abstract class SubMapIterator<T> implements Iterator<T> {
            int expectedModCount;
            final Object fenceKey;
            TreeMapEntry<K, V> lastReturned = null;
            TreeMapEntry<K, V> next;

            SubMapIterator(TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2) {
                this.expectedModCount = NavigableSubMap.this.f712m.modCount;
                this.next = treeMapEntry;
                this.fenceKey = treeMapEntry2 == null ? TreeMap.UNBOUNDED : treeMapEntry2.key;
            }

            public final boolean hasNext() {
                TreeMapEntry<K, V> treeMapEntry = this.next;
                return (treeMapEntry == null || treeMapEntry.key == this.fenceKey) ? false : true;
            }

            /* access modifiers changed from: package-private */
            public final TreeMapEntry<K, V> nextEntry() {
                TreeMapEntry<K, V> treeMapEntry = this.next;
                if (treeMapEntry == null || treeMapEntry.key == this.fenceKey) {
                    throw new NoSuchElementException();
                } else if (NavigableSubMap.this.f712m.modCount == this.expectedModCount) {
                    this.next = TreeMap.successor(treeMapEntry);
                    this.lastReturned = treeMapEntry;
                    return treeMapEntry;
                } else {
                    throw new ConcurrentModificationException();
                }
            }

            /* access modifiers changed from: package-private */
            public final TreeMapEntry<K, V> prevEntry() {
                TreeMapEntry<K, V> treeMapEntry = this.next;
                if (treeMapEntry == null || treeMapEntry.key == this.fenceKey) {
                    throw new NoSuchElementException();
                } else if (NavigableSubMap.this.f712m.modCount == this.expectedModCount) {
                    this.next = TreeMap.predecessor(treeMapEntry);
                    this.lastReturned = treeMapEntry;
                    return treeMapEntry;
                } else {
                    throw new ConcurrentModificationException();
                }
            }

            /* access modifiers changed from: package-private */
            public final void removeAscending() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                } else if (NavigableSubMap.this.f712m.modCount == this.expectedModCount) {
                    if (!(this.lastReturned.left == null || this.lastReturned.right == null)) {
                        this.next = this.lastReturned;
                    }
                    NavigableSubMap.this.f712m.deleteEntry(this.lastReturned);
                    this.lastReturned = null;
                    this.expectedModCount = NavigableSubMap.this.f712m.modCount;
                } else {
                    throw new ConcurrentModificationException();
                }
            }

            /* access modifiers changed from: package-private */
            public final void removeDescending() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                } else if (NavigableSubMap.this.f712m.modCount == this.expectedModCount) {
                    NavigableSubMap.this.f712m.deleteEntry(this.lastReturned);
                    this.lastReturned = null;
                    this.expectedModCount = NavigableSubMap.this.f712m.modCount;
                } else {
                    throw new ConcurrentModificationException();
                }
            }
        }

        final class SubMapEntryIterator extends NavigableSubMap<K, V>.SubMapIterator<Map.Entry<K, V>> {
            SubMapEntryIterator(TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2) {
                super(treeMapEntry, treeMapEntry2);
            }

            public Map.Entry<K, V> next() {
                return nextEntry();
            }

            public void remove() {
                removeAscending();
            }
        }

        final class DescendingSubMapEntryIterator extends NavigableSubMap<K, V>.SubMapIterator<Map.Entry<K, V>> {
            DescendingSubMapEntryIterator(TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2) {
                super(treeMapEntry, treeMapEntry2);
            }

            public Map.Entry<K, V> next() {
                return prevEntry();
            }

            public void remove() {
                removeDescending();
            }
        }

        final class SubMapKeyIterator extends NavigableSubMap<K, V>.SubMapIterator<K> implements Spliterator<K> {
            public int characteristics() {
                return 21;
            }

            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            public Spliterator<K> trySplit() {
                return null;
            }

            SubMapKeyIterator(TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2) {
                super(treeMapEntry, treeMapEntry2);
            }

            public K next() {
                return nextEntry().key;
            }

            public void remove() {
                removeAscending();
            }

            public void forEachRemaining(Consumer<? super K> consumer) {
                while (hasNext()) {
                    consumer.accept(next());
                }
            }

            public boolean tryAdvance(Consumer<? super K> consumer) {
                if (!hasNext()) {
                    return false;
                }
                consumer.accept(next());
                return true;
            }

            public final Comparator<? super K> getComparator() {
                return NavigableSubMap.this.comparator();
            }
        }

        final class DescendingSubMapKeyIterator extends NavigableSubMap<K, V>.SubMapIterator<K> implements Spliterator<K> {
            public int characteristics() {
                return 17;
            }

            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            public Spliterator<K> trySplit() {
                return null;
            }

            DescendingSubMapKeyIterator(TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2) {
                super(treeMapEntry, treeMapEntry2);
            }

            public K next() {
                return prevEntry().key;
            }

            public void remove() {
                removeDescending();
            }

            public void forEachRemaining(Consumer<? super K> consumer) {
                while (hasNext()) {
                    consumer.accept(next());
                }
            }

            public boolean tryAdvance(Consumer<? super K> consumer) {
                if (!hasNext()) {
                    return false;
                }
                consumer.accept(next());
                return true;
            }
        }
    }

    static final class AscendingSubMap<K, V> extends NavigableSubMap<K, V> {
        private static final long serialVersionUID = 912986545866124060L;

        AscendingSubMap(TreeMap<K, V> treeMap, boolean z, K k, boolean z2, boolean z3, K k2, boolean z4) {
            super(treeMap, z, k, z2, z3, k2, z4);
        }

        public Comparator<? super K> comparator() {
            return this.f712m.comparator();
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            if (!inRange(k, z)) {
                throw new IllegalArgumentException("fromKey out of range");
            } else if (inRange(k2, z2)) {
                return new AscendingSubMap(this.f712m, false, k, z, false, k2, z2);
            } else {
                throw new IllegalArgumentException("toKey out of range");
            }
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            if (inRange(k) || (!this.toEnd && this.f712m.compare(k, this.f710hi) == 0 && !this.hiInclusive && !z)) {
                return new AscendingSubMap(this.f712m, this.fromStart, this.f711lo, this.loInclusive, false, k, z);
            }
            throw new IllegalArgumentException("toKey out of range");
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            if (inRange(k) || (!this.fromStart && this.f712m.compare(k, this.f711lo) == 0 && !this.loInclusive && !z)) {
                return new AscendingSubMap(this.f712m, false, k, z, this.toEnd, this.f710hi, this.hiInclusive);
            }
            throw new IllegalArgumentException("fromKey out of range");
        }

        public NavigableMap<K, V> descendingMap() {
            NavigableMap<K, V> navigableMap = this.descendingMapView;
            if (navigableMap != null) {
                return navigableMap;
            }
            DescendingSubMap descendingSubMap = new DescendingSubMap(this.f712m, this.fromStart, this.f711lo, this.loInclusive, this.toEnd, this.f710hi, this.hiInclusive);
            this.descendingMapView = descendingSubMap;
            return descendingSubMap;
        }

        /* access modifiers changed from: package-private */
        public Iterator<K> keyIterator() {
            return new NavigableSubMap.SubMapKeyIterator(absLowest(), absHighFence());
        }

        /* access modifiers changed from: package-private */
        public Spliterator<K> keySpliterator() {
            return new NavigableSubMap.SubMapKeyIterator(absLowest(), absHighFence());
        }

        /* access modifiers changed from: package-private */
        public Iterator<K> descendingKeyIterator() {
            return new NavigableSubMap.DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }

        final class AscendingEntrySetView extends NavigableSubMap<K, V>.EntrySetView {
            AscendingEntrySetView() {
                super();
            }

            public Iterator<Map.Entry<K, V>> iterator() {
                AscendingSubMap ascendingSubMap = AscendingSubMap.this;
                return new NavigableSubMap.SubMapEntryIterator(ascendingSubMap.absLowest(), AscendingSubMap.this.absHighFence());
            }
        }

        public Set<Map.Entry<K, V>> entrySet() {
            NavigableSubMap.EntrySetView entrySetView = this.entrySetView;
            if (entrySetView != null) {
                return entrySetView;
            }
            AscendingEntrySetView ascendingEntrySetView = new AscendingEntrySetView();
            this.entrySetView = ascendingEntrySetView;
            return ascendingEntrySetView;
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subLowest() {
            return absLowest();
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subHighest() {
            return absHighest();
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subCeiling(K k) {
            return absCeiling(k);
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subHigher(K k) {
            return absHigher(k);
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subFloor(K k) {
            return absFloor(k);
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subLower(K k) {
            return absLower(k);
        }
    }

    static final class DescendingSubMap<K, V> extends NavigableSubMap<K, V> {
        private static final long serialVersionUID = 912986545866120460L;
        private final Comparator<? super K> reverseComparator = Collections.reverseOrder(this.f712m.comparator);

        DescendingSubMap(TreeMap<K, V> treeMap, boolean z, K k, boolean z2, boolean z3, K k2, boolean z4) {
            super(treeMap, z, k, z2, z3, k2, z4);
        }

        public Comparator<? super K> comparator() {
            return this.reverseComparator;
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            if (!inRange(k, z)) {
                throw new IllegalArgumentException("fromKey out of range");
            } else if (inRange(k2, z2)) {
                return new DescendingSubMap(this.f712m, false, k2, z2, false, k, z);
            } else {
                throw new IllegalArgumentException("toKey out of range");
            }
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            if (inRange(k) || (!this.fromStart && this.f712m.compare(k, this.f711lo) == 0 && !this.loInclusive && !z)) {
                return new DescendingSubMap(this.f712m, false, k, z, this.toEnd, this.f710hi, this.hiInclusive);
            }
            throw new IllegalArgumentException("toKey out of range");
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            if (inRange(k) || (!this.toEnd && this.f712m.compare(k, this.f710hi) == 0 && !this.hiInclusive && !z)) {
                return new DescendingSubMap(this.f712m, this.fromStart, this.f711lo, this.loInclusive, false, k, z);
            }
            throw new IllegalArgumentException("fromKey out of range");
        }

        public NavigableMap<K, V> descendingMap() {
            NavigableMap<K, V> navigableMap = this.descendingMapView;
            if (navigableMap != null) {
                return navigableMap;
            }
            AscendingSubMap ascendingSubMap = new AscendingSubMap(this.f712m, this.fromStart, this.f711lo, this.loInclusive, this.toEnd, this.f710hi, this.hiInclusive);
            this.descendingMapView = ascendingSubMap;
            return ascendingSubMap;
        }

        /* access modifiers changed from: package-private */
        public Iterator<K> keyIterator() {
            return new NavigableSubMap.DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }

        /* access modifiers changed from: package-private */
        public Spliterator<K> keySpliterator() {
            return new NavigableSubMap.DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }

        /* access modifiers changed from: package-private */
        public Iterator<K> descendingKeyIterator() {
            return new NavigableSubMap.SubMapKeyIterator(absLowest(), absHighFence());
        }

        final class DescendingEntrySetView extends NavigableSubMap<K, V>.EntrySetView {
            DescendingEntrySetView() {
                super();
            }

            public Iterator<Map.Entry<K, V>> iterator() {
                DescendingSubMap descendingSubMap = DescendingSubMap.this;
                return new NavigableSubMap.DescendingSubMapEntryIterator(descendingSubMap.absHighest(), DescendingSubMap.this.absLowFence());
            }
        }

        public Set<Map.Entry<K, V>> entrySet() {
            NavigableSubMap.EntrySetView entrySetView = this.entrySetView;
            if (entrySetView != null) {
                return entrySetView;
            }
            DescendingEntrySetView descendingEntrySetView = new DescendingEntrySetView();
            this.entrySetView = descendingEntrySetView;
            return descendingEntrySetView;
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subLowest() {
            return absHighest();
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subHighest() {
            return absLowest();
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subCeiling(K k) {
            return absFloor(k);
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subHigher(K k) {
            return absLower(k);
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subFloor(K k) {
            return absCeiling(k);
        }

        /* access modifiers changed from: package-private */
        public TreeMapEntry<K, V> subLower(K k) {
            return absHigher(k);
        }
    }

    private class SubMap extends AbstractMap<K, V> implements SortedMap<K, V>, Serializable {
        private static final long serialVersionUID = -6520786458950516097L;
        private K fromKey;
        private boolean fromStart = false;
        private boolean toEnd = false;
        private K toKey;

        private SubMap() {
        }

        private Object readResolve() {
            return new AscendingSubMap(TreeMap.this, this.fromStart, this.fromKey, true, this.toEnd, this.toKey, false);
        }

        public Set<Map.Entry<K, V>> entrySet() {
            throw new InternalError();
        }

        public K lastKey() {
            throw new InternalError();
        }

        public K firstKey() {
            throw new InternalError();
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            throw new InternalError();
        }

        public SortedMap<K, V> headMap(K k) {
            throw new InternalError();
        }

        public SortedMap<K, V> tailMap(K k) {
            throw new InternalError();
        }

        public Comparator<? super K> comparator() {
            throw new InternalError();
        }
    }

    static final class TreeMapEntry<K, V> implements Map.Entry<K, V> {
        boolean color = true;
        K key;
        TreeMapEntry<K, V> left;
        TreeMapEntry<K, V> parent;
        TreeMapEntry<K, V> right;
        V value;

        TreeMapEntry(K k, V v, TreeMapEntry<K, V> treeMapEntry) {
            this.key = k;
            this.value = v;
            this.parent = treeMapEntry;
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
            if (!TreeMap.valEquals(this.key, entry.getKey()) || !TreeMap.valEquals(this.value, entry.getValue())) {
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

    /* access modifiers changed from: package-private */
    public final TreeMapEntry<K, V> getFirstEntry() {
        TreeMapEntry<K, V> treeMapEntry = this.root;
        if (treeMapEntry != null) {
            while (treeMapEntry.left != null) {
                treeMapEntry = treeMapEntry.left;
            }
        }
        return treeMapEntry;
    }

    /* access modifiers changed from: package-private */
    public final TreeMapEntry<K, V> getLastEntry() {
        TreeMapEntry<K, V> treeMapEntry = this.root;
        if (treeMapEntry != null) {
            while (treeMapEntry.right != null) {
                treeMapEntry = treeMapEntry.right;
            }
        }
        return treeMapEntry;
    }

    static <K, V> TreeMapEntry<K, V> successor(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry == null) {
            return null;
        }
        if (treeMapEntry.right != null) {
            TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.right;
            while (treeMapEntry2.left != null) {
                treeMapEntry2 = treeMapEntry2.left;
            }
            return treeMapEntry2;
        }
        TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry.parent;
        while (true) {
            TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3;
            TreeMapEntry<K, V> treeMapEntry5 = treeMapEntry;
            treeMapEntry = treeMapEntry4;
            if (treeMapEntry == null || treeMapEntry5 != treeMapEntry.right) {
                return treeMapEntry;
            }
            treeMapEntry3 = treeMapEntry.parent;
        }
        return treeMapEntry;
    }

    static <K, V> TreeMapEntry<K, V> predecessor(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry == null) {
            return null;
        }
        if (treeMapEntry.left != null) {
            TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.left;
            while (treeMapEntry2.right != null) {
                treeMapEntry2 = treeMapEntry2.right;
            }
            return treeMapEntry2;
        }
        TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry.parent;
        while (true) {
            TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3;
            TreeMapEntry<K, V> treeMapEntry5 = treeMapEntry;
            treeMapEntry = treeMapEntry4;
            if (treeMapEntry == null || treeMapEntry5 != treeMapEntry.left) {
                return treeMapEntry;
            }
            treeMapEntry3 = treeMapEntry.parent;
        }
        return treeMapEntry;
    }

    private static <K, V> boolean colorOf(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry == null) {
            return true;
        }
        return treeMapEntry.color;
    }

    private static <K, V> TreeMapEntry<K, V> parentOf(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry == null) {
            return null;
        }
        return treeMapEntry.parent;
    }

    private static <K, V> void setColor(TreeMapEntry<K, V> treeMapEntry, boolean z) {
        if (treeMapEntry != null) {
            treeMapEntry.color = z;
        }
    }

    private static <K, V> TreeMapEntry<K, V> leftOf(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry == null) {
            return null;
        }
        return treeMapEntry.left;
    }

    private static <K, V> TreeMapEntry<K, V> rightOf(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry == null) {
            return null;
        }
        return treeMapEntry.right;
    }

    private void rotateLeft(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry != null) {
            TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.right;
            treeMapEntry.right = treeMapEntry2.left;
            if (treeMapEntry2.left != null) {
                treeMapEntry2.left.parent = treeMapEntry;
            }
            treeMapEntry2.parent = treeMapEntry.parent;
            if (treeMapEntry.parent == null) {
                this.root = treeMapEntry2;
            } else if (treeMapEntry.parent.left == treeMapEntry) {
                treeMapEntry.parent.left = treeMapEntry2;
            } else {
                treeMapEntry.parent.right = treeMapEntry2;
            }
            treeMapEntry2.left = treeMapEntry;
            treeMapEntry.parent = treeMapEntry2;
        }
    }

    private void rotateRight(TreeMapEntry<K, V> treeMapEntry) {
        if (treeMapEntry != null) {
            TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.left;
            treeMapEntry.left = treeMapEntry2.right;
            if (treeMapEntry2.right != null) {
                treeMapEntry2.right.parent = treeMapEntry;
            }
            treeMapEntry2.parent = treeMapEntry.parent;
            if (treeMapEntry.parent == null) {
                this.root = treeMapEntry2;
            } else if (treeMapEntry.parent.right == treeMapEntry) {
                treeMapEntry.parent.right = treeMapEntry2;
            } else {
                treeMapEntry.parent.left = treeMapEntry2;
            }
            treeMapEntry2.right = treeMapEntry;
            treeMapEntry.parent = treeMapEntry2;
        }
    }

    private void fixAfterInsertion(TreeMapEntry<K, V> treeMapEntry) {
        treeMapEntry.color = false;
        while (treeMapEntry != null && treeMapEntry != this.root && !treeMapEntry.parent.color) {
            if (parentOf(treeMapEntry) == leftOf(parentOf(parentOf(treeMapEntry)))) {
                TreeMapEntry<K, V> rightOf = rightOf(parentOf(parentOf(treeMapEntry)));
                if (!colorOf(rightOf)) {
                    setColor(parentOf(treeMapEntry), true);
                    setColor(rightOf, true);
                    setColor(parentOf(parentOf(treeMapEntry)), false);
                    treeMapEntry = parentOf(parentOf(treeMapEntry));
                } else {
                    if (treeMapEntry == rightOf(parentOf(treeMapEntry))) {
                        treeMapEntry = parentOf(treeMapEntry);
                        rotateLeft(treeMapEntry);
                    }
                    setColor(parentOf(treeMapEntry), true);
                    setColor(parentOf(parentOf(treeMapEntry)), false);
                    rotateRight(parentOf(parentOf(treeMapEntry)));
                }
            } else {
                TreeMapEntry<K, V> leftOf = leftOf(parentOf(parentOf(treeMapEntry)));
                if (!colorOf(leftOf)) {
                    setColor(parentOf(treeMapEntry), true);
                    setColor(leftOf, true);
                    setColor(parentOf(parentOf(treeMapEntry)), false);
                    treeMapEntry = parentOf(parentOf(treeMapEntry));
                } else {
                    if (treeMapEntry == leftOf(parentOf(treeMapEntry))) {
                        treeMapEntry = parentOf(treeMapEntry);
                        rotateRight(treeMapEntry);
                    }
                    setColor(parentOf(treeMapEntry), true);
                    setColor(parentOf(parentOf(treeMapEntry)), false);
                    rotateLeft(parentOf(parentOf(treeMapEntry)));
                }
            }
        }
        this.root.color = true;
    }

    /* access modifiers changed from: private */
    public void deleteEntry(TreeMapEntry<K, V> treeMapEntry) {
        this.modCount++;
        this.size--;
        if (!(treeMapEntry.left == null || treeMapEntry.right == null)) {
            TreeMapEntry<K, V> successor = successor(treeMapEntry);
            treeMapEntry.key = successor.key;
            treeMapEntry.value = successor.value;
            treeMapEntry = successor;
        }
        TreeMapEntry<K, V> treeMapEntry2 = treeMapEntry.left != null ? treeMapEntry.left : treeMapEntry.right;
        if (treeMapEntry2 != null) {
            treeMapEntry2.parent = treeMapEntry.parent;
            if (treeMapEntry.parent == null) {
                this.root = treeMapEntry2;
            } else if (treeMapEntry == treeMapEntry.parent.left) {
                treeMapEntry.parent.left = treeMapEntry2;
            } else {
                treeMapEntry.parent.right = treeMapEntry2;
            }
            treeMapEntry.parent = null;
            treeMapEntry.right = null;
            treeMapEntry.left = null;
            if (treeMapEntry.color) {
                fixAfterDeletion(treeMapEntry2);
            }
        } else if (treeMapEntry.parent == null) {
            this.root = null;
        } else {
            if (treeMapEntry.color) {
                fixAfterDeletion(treeMapEntry);
            }
            if (treeMapEntry.parent != null) {
                if (treeMapEntry == treeMapEntry.parent.left) {
                    treeMapEntry.parent.left = null;
                } else if (treeMapEntry == treeMapEntry.parent.right) {
                    treeMapEntry.parent.right = null;
                }
                treeMapEntry.parent = null;
            }
        }
    }

    private void fixAfterDeletion(TreeMapEntry<K, V> treeMapEntry) {
        while (treeMapEntry != this.root && colorOf(treeMapEntry)) {
            if (treeMapEntry == leftOf(parentOf(treeMapEntry))) {
                TreeMapEntry<K, V> rightOf = rightOf(parentOf(treeMapEntry));
                if (!colorOf(rightOf)) {
                    setColor(rightOf, true);
                    setColor(parentOf(treeMapEntry), false);
                    rotateLeft(parentOf(treeMapEntry));
                    rightOf = rightOf(parentOf(treeMapEntry));
                }
                if (colorOf(leftOf(rightOf)) && colorOf(rightOf(rightOf))) {
                    setColor(rightOf, false);
                    treeMapEntry = parentOf(treeMapEntry);
                } else {
                    if (colorOf(rightOf(rightOf))) {
                        setColor(leftOf(rightOf), true);
                        setColor(rightOf, false);
                        rotateRight(rightOf);
                        rightOf = rightOf(parentOf(treeMapEntry));
                    }
                    setColor(rightOf, colorOf(parentOf(treeMapEntry)));
                    setColor(parentOf(treeMapEntry), true);
                    setColor(rightOf(rightOf), true);
                    rotateLeft(parentOf(treeMapEntry));
                    treeMapEntry = this.root;
                }
            } else {
                TreeMapEntry<K, V> leftOf = leftOf(parentOf(treeMapEntry));
                if (!colorOf(leftOf)) {
                    setColor(leftOf, true);
                    setColor(parentOf(treeMapEntry), false);
                    rotateRight(parentOf(treeMapEntry));
                    leftOf = leftOf(parentOf(treeMapEntry));
                }
                if (colorOf(rightOf(leftOf)) && colorOf(leftOf(leftOf))) {
                    setColor(leftOf, false);
                    treeMapEntry = parentOf(treeMapEntry);
                } else {
                    if (colorOf(leftOf(leftOf))) {
                        setColor(rightOf(leftOf), true);
                        setColor(leftOf, false);
                        rotateLeft(leftOf);
                        leftOf = leftOf(parentOf(treeMapEntry));
                    }
                    setColor(leftOf, colorOf(parentOf(treeMapEntry)));
                    setColor(parentOf(treeMapEntry), true);
                    setColor(leftOf(leftOf), true);
                    rotateRight(parentOf(treeMapEntry));
                    treeMapEntry = this.root;
                }
            }
        }
        setColor(treeMapEntry, true);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        for (Map.Entry entry : entrySet()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        buildFromSorted(objectInputStream.readInt(), (Iterator<?>) null, objectInputStream, (Object) null);
    }

    /* access modifiers changed from: package-private */
    public void readTreeSet(int i, ObjectInputStream objectInputStream, V v) throws IOException, ClassNotFoundException {
        buildFromSorted(i, (Iterator<?>) null, objectInputStream, v);
    }

    /* access modifiers changed from: package-private */
    public void addAllForTreeSet(SortedSet<? extends K> sortedSet, V v) {
        try {
            buildFromSorted(sortedSet.size(), sortedSet.iterator(), (ObjectInputStream) null, v);
        } catch (IOException | ClassNotFoundException unused) {
        }
    }

    private void buildFromSorted(int i, Iterator<?> it, ObjectInputStream objectInputStream, V v) throws IOException, ClassNotFoundException {
        this.size = i;
        this.root = buildFromSorted(0, 0, i - 1, computeRedLevel(i), it, objectInputStream, v);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x005a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.util.TreeMap.TreeMapEntry<K, V> buildFromSorted(int r13, int r14, int r15, int r16, java.util.Iterator<?> r17, java.p026io.ObjectInputStream r18, V r19) throws java.p026io.IOException, java.lang.ClassNotFoundException {
        /*
            r12 = this;
            r0 = r13
            r3 = r14
            r9 = r15
            r10 = 0
            if (r9 >= r3) goto L_0x0007
            return r10
        L_0x0007:
            int r1 = r3 + r9
            int r11 = r1 >>> 1
            if (r3 >= r11) goto L_0x0020
            int r2 = r0 + 1
            int r4 = r11 + -1
            r1 = r12
            r3 = r14
            r5 = r16
            r6 = r17
            r7 = r18
            r8 = r19
            java.util.TreeMap$TreeMapEntry r1 = r1.buildFromSorted(r2, r3, r4, r5, r6, r7, r8)
            goto L_0x0021
        L_0x0020:
            r1 = r10
        L_0x0021:
            if (r17 == 0) goto L_0x003b
            if (r19 != 0) goto L_0x0034
            java.lang.Object r2 = r17.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.Object r3 = r2.getKey()
            java.lang.Object r2 = r2.getValue()
            goto L_0x0046
        L_0x0034:
            java.lang.Object r3 = r17.next()
        L_0x0038:
            r2 = r19
            goto L_0x0046
        L_0x003b:
            java.lang.Object r3 = r18.readObject()
            if (r19 == 0) goto L_0x0042
            goto L_0x0038
        L_0x0042:
            java.lang.Object r2 = r18.readObject()
        L_0x0046:
            java.util.TreeMap$TreeMapEntry r8 = new java.util.TreeMap$TreeMapEntry
            r8.<init>(r3, r2, r10)
            r4 = r16
            if (r0 != r4) goto L_0x0052
            r2 = 0
            r8.color = r2
        L_0x0052:
            if (r1 == 0) goto L_0x0058
            r8.left = r1
            r1.parent = r8
        L_0x0058:
            if (r11 >= r9) goto L_0x0070
            int r1 = r0 + 1
            int r2 = r11 + 1
            r0 = r12
            r3 = r15
            r4 = r16
            r5 = r17
            r6 = r18
            r7 = r19
            java.util.TreeMap$TreeMapEntry r0 = r0.buildFromSorted(r1, r2, r3, r4, r5, r6, r7)
            r8.right = r0
            r0.parent = r8
        L_0x0070:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TreeMap.buildFromSorted(int, int, int, int, java.util.Iterator, java.io.ObjectInputStream, java.lang.Object):java.util.TreeMap$TreeMapEntry");
    }

    private static int computeRedLevel(int i) {
        return 31 - Integer.numberOfLeadingZeros(i + 1);
    }

    static <K> Spliterator<K> keySpliteratorFor(NavigableMap<K, ?> navigableMap) {
        if (navigableMap instanceof TreeMap) {
            return ((TreeMap) navigableMap).keySpliterator();
        }
        if (navigableMap instanceof DescendingSubMap) {
            DescendingSubMap descendingSubMap = (DescendingSubMap) navigableMap;
            TreeMap treeMap = descendingSubMap.f712m;
            if (descendingSubMap == treeMap.descendingMap) {
                return treeMap.descendingKeySpliterator();
            }
        }
        return ((NavigableSubMap) navigableMap).keySpliterator();
    }

    /* access modifiers changed from: package-private */
    public final Spliterator<K> keySpliterator() {
        return new KeySpliterator(this, (TreeMapEntry) null, (TreeMapEntry) null, 0, -1, 0);
    }

    /* access modifiers changed from: package-private */
    public final Spliterator<K> descendingKeySpliterator() {
        return new DescendingKeySpliterator(this, (TreeMapEntry) null, (TreeMapEntry) null, 0, -2, 0);
    }

    static class TreeMapSpliterator<K, V> {
        TreeMapEntry<K, V> current;
        int est;
        int expectedModCount;
        TreeMapEntry<K, V> fence;
        int side;
        final TreeMap<K, V> tree;

        TreeMapSpliterator(TreeMap<K, V> treeMap, TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2, int i, int i2, int i3) {
            this.tree = treeMap;
            this.current = treeMapEntry;
            this.fence = treeMapEntry2;
            this.side = i;
            this.est = i2;
            this.expectedModCount = i3;
        }

        /* access modifiers changed from: package-private */
        public final int getEstimate() {
            int i = this.est;
            if (i >= 0) {
                return i;
            }
            TreeMap<K, V> treeMap = this.tree;
            if (treeMap != null) {
                this.current = i == -1 ? treeMap.getFirstEntry() : treeMap.getLastEntry();
                int r0 = treeMap.size;
                this.est = r0;
                this.expectedModCount = treeMap.modCount;
                return r0;
            }
            this.est = 0;
            return 0;
        }

        public final long estimateSize() {
            return (long) getEstimate();
        }
    }

    static final class KeySpliterator<K, V> extends TreeMapSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(TreeMap<K, V> treeMap, TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2, int i, int i2, int i3) {
            super(treeMap, treeMapEntry, treeMapEntry2, i, i2, i3);
        }

        public KeySpliterator<K, V> trySplit() {
            TreeMapEntry<K, V> treeMapEntry;
            TreeMapEntry<K, V> treeMapEntry2;
            if (this.est < 0) {
                getEstimate();
            }
            int i = this.side;
            TreeMapEntry<K, V> treeMapEntry3 = this.current;
            TreeMapEntry<K, V> treeMapEntry4 = this.fence;
            if (!(treeMapEntry3 == null || treeMapEntry3 == treeMapEntry4)) {
                if (i == 0) {
                    treeMapEntry2 = this.tree.root;
                } else if (i > 0) {
                    treeMapEntry2 = treeMapEntry3.right;
                } else if (i < 0 && treeMapEntry4 != null) {
                    treeMapEntry2 = treeMapEntry4.left;
                }
                treeMapEntry = treeMapEntry2;
                if (treeMapEntry != null || treeMapEntry == treeMapEntry3 || treeMapEntry == treeMapEntry4 || this.tree.compare(treeMapEntry3.key, treeMapEntry.key) >= 0) {
                    return null;
                }
                this.side = 1;
                TreeMap treeMap = this.tree;
                this.current = treeMapEntry;
                int i2 = this.est >>> 1;
                this.est = i2;
                return new KeySpliterator(treeMap, treeMapEntry3, treeMapEntry, -1, i2, this.expectedModCount);
            }
            treeMapEntry = null;
            if (treeMapEntry != null) {
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super K> consumer) {
            consumer.getClass();
            if (this.est < 0) {
                getEstimate();
            }
            TreeMapEntry<K, V> treeMapEntry = this.fence;
            TreeMapEntry<K, V> treeMapEntry2 = this.current;
            if (treeMapEntry2 != null && treeMapEntry2 != treeMapEntry) {
                this.current = treeMapEntry;
                do {
                    consumer.accept(treeMapEntry2.key);
                    TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry2.right;
                    if (treeMapEntry3 == null) {
                        while (true) {
                            treeMapEntry3 = treeMapEntry2.parent;
                            if (treeMapEntry3 == null || treeMapEntry2 != treeMapEntry3.right) {
                                break;
                            }
                            treeMapEntry2 = treeMapEntry3;
                        }
                    } else {
                        while (true) {
                            TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3.left;
                            if (treeMapEntry4 == null) {
                                break;
                            }
                            treeMapEntry3 = treeMapEntry4;
                        }
                    }
                    treeMapEntry2 = treeMapEntry3;
                    if (treeMapEntry2 == null) {
                        break;
                    }
                } while (treeMapEntry2 != treeMapEntry);
                if (this.tree.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        public boolean tryAdvance(Consumer<? super K> consumer) {
            consumer.getClass();
            if (this.est < 0) {
                getEstimate();
            }
            TreeMapEntry treeMapEntry = this.current;
            if (treeMapEntry == null || treeMapEntry == this.fence) {
                return false;
            }
            this.current = TreeMap.successor(treeMapEntry);
            consumer.accept(treeMapEntry.key);
            if (this.tree.modCount == this.expectedModCount) {
                return true;
            }
            throw new ConcurrentModificationException();
        }

        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 4 | 16;
        }

        public final Comparator<? super K> getComparator() {
            return this.tree.comparator;
        }
    }

    static final class DescendingKeySpliterator<K, V> extends TreeMapSpliterator<K, V> implements Spliterator<K> {
        DescendingKeySpliterator(TreeMap<K, V> treeMap, TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2, int i, int i2, int i3) {
            super(treeMap, treeMapEntry, treeMapEntry2, i, i2, i3);
        }

        public DescendingKeySpliterator<K, V> trySplit() {
            TreeMapEntry<K, V> treeMapEntry;
            TreeMapEntry<K, V> treeMapEntry2;
            if (this.est < 0) {
                getEstimate();
            }
            int i = this.side;
            TreeMapEntry<K, V> treeMapEntry3 = this.current;
            TreeMapEntry<K, V> treeMapEntry4 = this.fence;
            if (!(treeMapEntry3 == null || treeMapEntry3 == treeMapEntry4)) {
                if (i == 0) {
                    treeMapEntry2 = this.tree.root;
                } else if (i < 0) {
                    treeMapEntry2 = treeMapEntry3.left;
                } else if (i > 0 && treeMapEntry4 != null) {
                    treeMapEntry2 = treeMapEntry4.right;
                }
                treeMapEntry = treeMapEntry2;
                if (treeMapEntry != null || treeMapEntry == treeMapEntry3 || treeMapEntry == treeMapEntry4 || this.tree.compare(treeMapEntry3.key, treeMapEntry.key) <= 0) {
                    return null;
                }
                this.side = 1;
                TreeMap treeMap = this.tree;
                this.current = treeMapEntry;
                int i2 = this.est >>> 1;
                this.est = i2;
                return new DescendingKeySpliterator(treeMap, treeMapEntry3, treeMapEntry, -1, i2, this.expectedModCount);
            }
            treeMapEntry = null;
            if (treeMapEntry != null) {
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super K> consumer) {
            consumer.getClass();
            if (this.est < 0) {
                getEstimate();
            }
            TreeMapEntry<K, V> treeMapEntry = this.fence;
            TreeMapEntry<K, V> treeMapEntry2 = this.current;
            if (treeMapEntry2 != null && treeMapEntry2 != treeMapEntry) {
                this.current = treeMapEntry;
                do {
                    consumer.accept(treeMapEntry2.key);
                    TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry2.left;
                    if (treeMapEntry3 == null) {
                        while (true) {
                            treeMapEntry3 = treeMapEntry2.parent;
                            if (treeMapEntry3 == null || treeMapEntry2 != treeMapEntry3.left) {
                                break;
                            }
                            treeMapEntry2 = treeMapEntry3;
                        }
                    } else {
                        while (true) {
                            TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3.right;
                            if (treeMapEntry4 == null) {
                                break;
                            }
                            treeMapEntry3 = treeMapEntry4;
                        }
                    }
                    treeMapEntry2 = treeMapEntry3;
                    if (treeMapEntry2 == null) {
                        break;
                    }
                } while (treeMapEntry2 != treeMapEntry);
                if (this.tree.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        public boolean tryAdvance(Consumer<? super K> consumer) {
            consumer.getClass();
            if (this.est < 0) {
                getEstimate();
            }
            TreeMapEntry treeMapEntry = this.current;
            if (treeMapEntry == null || treeMapEntry == this.fence) {
                return false;
            }
            this.current = TreeMap.predecessor(treeMapEntry);
            consumer.accept(treeMapEntry.key);
            if (this.tree.modCount == this.expectedModCount) {
                return true;
            }
            throw new ConcurrentModificationException();
        }

        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 16;
        }
    }

    static final class ValueSpliterator<K, V> extends TreeMapSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(TreeMap<K, V> treeMap, TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2, int i, int i2, int i3) {
            super(treeMap, treeMapEntry, treeMapEntry2, i, i2, i3);
        }

        public ValueSpliterator<K, V> trySplit() {
            TreeMapEntry<K, V> treeMapEntry;
            TreeMapEntry<K, V> treeMapEntry2;
            if (this.est < 0) {
                getEstimate();
            }
            int i = this.side;
            TreeMapEntry<K, V> treeMapEntry3 = this.current;
            TreeMapEntry<K, V> treeMapEntry4 = this.fence;
            if (!(treeMapEntry3 == null || treeMapEntry3 == treeMapEntry4)) {
                if (i == 0) {
                    treeMapEntry2 = this.tree.root;
                } else if (i > 0) {
                    treeMapEntry2 = treeMapEntry3.right;
                } else if (i < 0 && treeMapEntry4 != null) {
                    treeMapEntry2 = treeMapEntry4.left;
                }
                treeMapEntry = treeMapEntry2;
                if (treeMapEntry != null || treeMapEntry == treeMapEntry3 || treeMapEntry == treeMapEntry4 || this.tree.compare(treeMapEntry3.key, treeMapEntry.key) >= 0) {
                    return null;
                }
                this.side = 1;
                TreeMap treeMap = this.tree;
                this.current = treeMapEntry;
                int i2 = this.est >>> 1;
                this.est = i2;
                return new ValueSpliterator(treeMap, treeMapEntry3, treeMapEntry, -1, i2, this.expectedModCount);
            }
            treeMapEntry = null;
            if (treeMapEntry != null) {
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super V> consumer) {
            consumer.getClass();
            if (this.est < 0) {
                getEstimate();
            }
            TreeMapEntry<K, V> treeMapEntry = this.fence;
            TreeMapEntry<K, V> treeMapEntry2 = this.current;
            if (treeMapEntry2 != null && treeMapEntry2 != treeMapEntry) {
                this.current = treeMapEntry;
                do {
                    consumer.accept(treeMapEntry2.value);
                    TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry2.right;
                    if (treeMapEntry3 == null) {
                        while (true) {
                            treeMapEntry3 = treeMapEntry2.parent;
                            if (treeMapEntry3 == null || treeMapEntry2 != treeMapEntry3.right) {
                                break;
                            }
                            treeMapEntry2 = treeMapEntry3;
                        }
                    } else {
                        while (true) {
                            TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3.left;
                            if (treeMapEntry4 == null) {
                                break;
                            }
                            treeMapEntry3 = treeMapEntry4;
                        }
                    }
                    treeMapEntry2 = treeMapEntry3;
                    if (treeMapEntry2 == null) {
                        break;
                    }
                } while (treeMapEntry2 != treeMapEntry);
                if (this.tree.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        public boolean tryAdvance(Consumer<? super V> consumer) {
            consumer.getClass();
            if (this.est < 0) {
                getEstimate();
            }
            TreeMapEntry treeMapEntry = this.current;
            if (treeMapEntry == null || treeMapEntry == this.fence) {
                return false;
            }
            this.current = TreeMap.successor(treeMapEntry);
            consumer.accept(treeMapEntry.value);
            if (this.tree.modCount == this.expectedModCount) {
                return true;
            }
            throw new ConcurrentModificationException();
        }

        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 16;
        }
    }

    static final class EntrySpliterator<K, V> extends TreeMapSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        EntrySpliterator(TreeMap<K, V> treeMap, TreeMapEntry<K, V> treeMapEntry, TreeMapEntry<K, V> treeMapEntry2, int i, int i2, int i3) {
            super(treeMap, treeMapEntry, treeMapEntry2, i, i2, i3);
        }

        public EntrySpliterator<K, V> trySplit() {
            TreeMapEntry<K, V> treeMapEntry;
            TreeMapEntry<K, V> treeMapEntry2;
            if (this.est < 0) {
                getEstimate();
            }
            int i = this.side;
            TreeMapEntry<K, V> treeMapEntry3 = this.current;
            TreeMapEntry<K, V> treeMapEntry4 = this.fence;
            if (!(treeMapEntry3 == null || treeMapEntry3 == treeMapEntry4)) {
                if (i == 0) {
                    treeMapEntry2 = this.tree.root;
                } else if (i > 0) {
                    treeMapEntry2 = treeMapEntry3.right;
                } else if (i < 0 && treeMapEntry4 != null) {
                    treeMapEntry2 = treeMapEntry4.left;
                }
                treeMapEntry = treeMapEntry2;
                if (treeMapEntry != null || treeMapEntry == treeMapEntry3 || treeMapEntry == treeMapEntry4 || this.tree.compare(treeMapEntry3.key, treeMapEntry.key) >= 0) {
                    return null;
                }
                this.side = 1;
                TreeMap treeMap = this.tree;
                this.current = treeMapEntry;
                int i2 = this.est >>> 1;
                this.est = i2;
                return new EntrySpliterator(treeMap, treeMapEntry3, treeMapEntry, -1, i2, this.expectedModCount);
            }
            treeMapEntry = null;
            if (treeMapEntry != null) {
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            if (this.est < 0) {
                getEstimate();
            }
            TreeMapEntry<K, V> treeMapEntry = this.fence;
            TreeMapEntry<K, V> treeMapEntry2 = this.current;
            if (treeMapEntry2 != null && treeMapEntry2 != treeMapEntry) {
                this.current = treeMapEntry;
                do {
                    consumer.accept(treeMapEntry2);
                    TreeMapEntry<K, V> treeMapEntry3 = treeMapEntry2.right;
                    if (treeMapEntry3 == null) {
                        while (true) {
                            treeMapEntry3 = treeMapEntry2.parent;
                            if (treeMapEntry3 == null || treeMapEntry2 != treeMapEntry3.right) {
                                break;
                            }
                            treeMapEntry2 = treeMapEntry3;
                        }
                    } else {
                        while (true) {
                            TreeMapEntry<K, V> treeMapEntry4 = treeMapEntry3.left;
                            if (treeMapEntry4 == null) {
                                break;
                            }
                            treeMapEntry3 = treeMapEntry4;
                        }
                    }
                    treeMapEntry2 = treeMapEntry3;
                    if (treeMapEntry2 == null) {
                        break;
                    }
                } while (treeMapEntry2 != treeMapEntry);
                if (this.tree.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            if (this.est < 0) {
                getEstimate();
            }
            TreeMapEntry treeMapEntry = this.current;
            if (treeMapEntry == null || treeMapEntry == this.fence) {
                return false;
            }
            this.current = TreeMap.successor(treeMapEntry);
            consumer.accept(treeMapEntry);
            if (this.tree.modCount == this.expectedModCount) {
                return true;
            }
            throw new ConcurrentModificationException();
        }

        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 4 | 16;
        }

        public Comparator<Map.Entry<K, V>> getComparator() {
            if (this.tree.comparator != null) {
                return Map.Entry.comparingByKey(this.tree.comparator);
            }
            return new TreeMap$EntrySpliterator$$ExternalSyntheticLambda0();
        }
    }
}
