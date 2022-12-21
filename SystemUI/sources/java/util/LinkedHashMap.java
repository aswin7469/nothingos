package java.util;

import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LinkedHashMap<K, V> extends HashMap<K, V> implements Map<K, V> {
    private static final long serialVersionUID = 3801124242820219131L;
    final boolean accessOrder;
    transient LinkedHashMapEntry<K, V> head;
    transient LinkedHashMapEntry<K, V> tail;

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return false;
    }

    static class LinkedHashMapEntry<K, V> extends HashMap.Node<K, V> {
        LinkedHashMapEntry<K, V> after;
        LinkedHashMapEntry<K, V> before;

        LinkedHashMapEntry(int i, K k, V v, HashMap.Node<K, V> node) {
            super(i, k, v, node);
        }
    }

    private void linkNodeLast(LinkedHashMapEntry<K, V> linkedHashMapEntry) {
        LinkedHashMapEntry<K, V> linkedHashMapEntry2 = this.tail;
        this.tail = linkedHashMapEntry;
        if (linkedHashMapEntry2 == null) {
            this.head = linkedHashMapEntry;
            return;
        }
        linkedHashMapEntry.before = linkedHashMapEntry2;
        linkedHashMapEntry2.after = linkedHashMapEntry;
    }

    private void transferLinks(LinkedHashMapEntry<K, V> linkedHashMapEntry, LinkedHashMapEntry<K, V> linkedHashMapEntry2) {
        LinkedHashMapEntry<K, V> linkedHashMapEntry3 = linkedHashMapEntry.before;
        linkedHashMapEntry2.before = linkedHashMapEntry3;
        LinkedHashMapEntry<K, V> linkedHashMapEntry4 = linkedHashMapEntry.after;
        linkedHashMapEntry2.after = linkedHashMapEntry4;
        if (linkedHashMapEntry3 == null) {
            this.head = linkedHashMapEntry2;
        } else {
            linkedHashMapEntry3.after = linkedHashMapEntry2;
        }
        if (linkedHashMapEntry4 == null) {
            this.tail = linkedHashMapEntry2;
        } else {
            linkedHashMapEntry4.before = linkedHashMapEntry2;
        }
    }

    /* access modifiers changed from: package-private */
    public void reinitialize() {
        super.reinitialize();
        this.tail = null;
        this.head = null;
    }

    /* access modifiers changed from: package-private */
    public HashMap.Node<K, V> newNode(int i, K k, V v, HashMap.Node<K, V> node) {
        LinkedHashMapEntry linkedHashMapEntry = new LinkedHashMapEntry(i, k, v, node);
        linkNodeLast(linkedHashMapEntry);
        return linkedHashMapEntry;
    }

    /* access modifiers changed from: package-private */
    public HashMap.Node<K, V> replacementNode(HashMap.Node<K, V> node, HashMap.Node<K, V> node2) {
        LinkedHashMapEntry linkedHashMapEntry = (LinkedHashMapEntry) node;
        LinkedHashMapEntry linkedHashMapEntry2 = new LinkedHashMapEntry(linkedHashMapEntry.hash, linkedHashMapEntry.key, linkedHashMapEntry.value, node2);
        transferLinks(linkedHashMapEntry, linkedHashMapEntry2);
        return linkedHashMapEntry2;
    }

    /* access modifiers changed from: package-private */
    public HashMap.TreeNode<K, V> newTreeNode(int i, K k, V v, HashMap.Node<K, V> node) {
        HashMap.TreeNode<K, V> treeNode = new HashMap.TreeNode<>(i, k, v, node);
        linkNodeLast(treeNode);
        return treeNode;
    }

    /* access modifiers changed from: package-private */
    public HashMap.TreeNode<K, V> replacementTreeNode(HashMap.Node<K, V> node, HashMap.Node<K, V> node2) {
        LinkedHashMapEntry linkedHashMapEntry = (LinkedHashMapEntry) node;
        HashMap.TreeNode<K, V> treeNode = new HashMap.TreeNode<>(linkedHashMapEntry.hash, linkedHashMapEntry.key, linkedHashMapEntry.value, node2);
        transferLinks(linkedHashMapEntry, treeNode);
        return treeNode;
    }

    /* access modifiers changed from: package-private */
    public void afterNodeRemoval(HashMap.Node<K, V> node) {
        LinkedHashMapEntry linkedHashMapEntry = (LinkedHashMapEntry) node;
        LinkedHashMapEntry<K, V> linkedHashMapEntry2 = linkedHashMapEntry.before;
        LinkedHashMapEntry<K, V> linkedHashMapEntry3 = linkedHashMapEntry.after;
        linkedHashMapEntry.after = null;
        linkedHashMapEntry.before = null;
        if (linkedHashMapEntry2 == null) {
            this.head = linkedHashMapEntry3;
        } else {
            linkedHashMapEntry2.after = linkedHashMapEntry3;
        }
        if (linkedHashMapEntry3 == null) {
            this.tail = linkedHashMapEntry2;
        } else {
            linkedHashMapEntry3.before = linkedHashMapEntry2;
        }
    }

    /* access modifiers changed from: package-private */
    public void afterNodeInsertion(boolean z) {
        LinkedHashMapEntry<K, V> linkedHashMapEntry;
        if (z && (linkedHashMapEntry = this.head) != null && removeEldestEntry(linkedHashMapEntry)) {
            Object obj = linkedHashMapEntry.key;
            removeNode(hash(obj), obj, (Object) null, false, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void afterNodeAccess(HashMap.Node<K, V> node) {
        LinkedHashMapEntry<K, V> linkedHashMapEntry;
        if (this.accessOrder && (linkedHashMapEntry = this.tail) != node) {
            LinkedHashMapEntry<K, V> linkedHashMapEntry2 = (LinkedHashMapEntry) node;
            LinkedHashMapEntry<K, V> linkedHashMapEntry3 = linkedHashMapEntry2.before;
            LinkedHashMapEntry<K, V> linkedHashMapEntry4 = linkedHashMapEntry2.after;
            linkedHashMapEntry2.after = null;
            if (linkedHashMapEntry3 == null) {
                this.head = linkedHashMapEntry4;
            } else {
                linkedHashMapEntry3.after = linkedHashMapEntry4;
            }
            if (linkedHashMapEntry4 != null) {
                linkedHashMapEntry4.before = linkedHashMapEntry3;
            } else {
                linkedHashMapEntry = linkedHashMapEntry3;
            }
            if (linkedHashMapEntry == null) {
                this.head = linkedHashMapEntry2;
            } else {
                linkedHashMapEntry2.before = linkedHashMapEntry;
                linkedHashMapEntry.after = linkedHashMapEntry2;
            }
            this.tail = linkedHashMapEntry2;
            this.modCount++;
        }
    }

    /* access modifiers changed from: package-private */
    public void internalWriteEntries(ObjectOutputStream objectOutputStream) throws IOException {
        for (LinkedHashMapEntry<K, V> linkedHashMapEntry = this.head; linkedHashMapEntry != null; linkedHashMapEntry = linkedHashMapEntry.after) {
            objectOutputStream.writeObject(linkedHashMapEntry.key);
            objectOutputStream.writeObject(linkedHashMapEntry.value);
        }
    }

    public LinkedHashMap(int i, float f) {
        super(i, f);
        this.accessOrder = false;
    }

    public LinkedHashMap(int i) {
        super(i);
        this.accessOrder = false;
    }

    public LinkedHashMap() {
        this.accessOrder = false;
    }

    public LinkedHashMap(Map<? extends K, ? extends V> map) {
        this.accessOrder = false;
        putMapEntries(map, false);
    }

    public LinkedHashMap(int i, float f, boolean z) {
        super(i, f);
        this.accessOrder = z;
    }

    public boolean containsValue(Object obj) {
        for (LinkedHashMapEntry<K, V> linkedHashMapEntry = this.head; linkedHashMapEntry != null; linkedHashMapEntry = linkedHashMapEntry.after) {
            Object obj2 = linkedHashMapEntry.value;
            if (obj2 == obj) {
                return true;
            }
            if (obj != null && obj.equals(obj2)) {
                return true;
            }
        }
        return false;
    }

    public V get(Object obj) {
        HashMap.Node node = getNode(hash(obj), obj);
        if (node == null) {
            return null;
        }
        if (this.accessOrder) {
            afterNodeAccess(node);
        }
        return node.value;
    }

    public V getOrDefault(Object obj, V v) {
        HashMap.Node node = getNode(hash(obj), obj);
        if (node == null) {
            return v;
        }
        if (this.accessOrder) {
            afterNodeAccess(node);
        }
        return node.value;
    }

    public void clear() {
        super.clear();
        this.tail = null;
        this.head = null;
    }

    public Map.Entry<K, V> eldest() {
        return this.head;
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        LinkedKeySet linkedKeySet = new LinkedKeySet();
        this.keySet = linkedKeySet;
        return linkedKeySet;
    }

    final class LinkedKeySet extends AbstractSet<K> {
        LinkedKeySet() {
        }

        public final int size() {
            return LinkedHashMap.this.size;
        }

        public final void clear() {
            LinkedHashMap.this.clear();
        }

        public final Iterator<K> iterator() {
            return new LinkedKeyIterator();
        }

        public final boolean contains(Object obj) {
            return LinkedHashMap.this.containsKey(obj);
        }

        public final boolean remove(Object obj) {
            return LinkedHashMap.this.removeNode(HashMap.hash(obj), obj, (Object) null, false, true) != null;
        }

        public final Spliterator<K> spliterator() {
            return Spliterators.spliterator(this, 81);
        }

        public final void forEach(Consumer<? super K> consumer) {
            consumer.getClass();
            int i = LinkedHashMap.this.modCount;
            for (LinkedHashMapEntry<K, V> linkedHashMapEntry = LinkedHashMap.this.head; linkedHashMapEntry != null && LinkedHashMap.this.modCount == i; linkedHashMapEntry = linkedHashMapEntry.after) {
                consumer.accept(linkedHashMapEntry.key);
            }
            if (LinkedHashMap.this.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        LinkedValues linkedValues = new LinkedValues();
        this.values = linkedValues;
        return linkedValues;
    }

    final class LinkedValues extends AbstractCollection<V> {
        LinkedValues() {
        }

        public final int size() {
            return LinkedHashMap.this.size;
        }

        public final void clear() {
            LinkedHashMap.this.clear();
        }

        public final Iterator<V> iterator() {
            return new LinkedValueIterator();
        }

        public final boolean contains(Object obj) {
            return LinkedHashMap.this.containsValue(obj);
        }

        public final Spliterator<V> spliterator() {
            return Spliterators.spliterator(this, 80);
        }

        public final void forEach(Consumer<? super V> consumer) {
            consumer.getClass();
            int i = LinkedHashMap.this.modCount;
            for (LinkedHashMapEntry<K, V> linkedHashMapEntry = LinkedHashMap.this.head; linkedHashMapEntry != null && LinkedHashMap.this.modCount == i; linkedHashMapEntry = linkedHashMapEntry.after) {
                consumer.accept(linkedHashMapEntry.value);
            }
            if (LinkedHashMap.this.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        LinkedEntrySet linkedEntrySet = new LinkedEntrySet();
        this.entrySet = linkedEntrySet;
        return linkedEntrySet;
    }

    final class LinkedEntrySet extends AbstractSet<Map.Entry<K, V>> {
        LinkedEntrySet() {
        }

        public final int size() {
            return LinkedHashMap.this.size;
        }

        public final void clear() {
            LinkedHashMap.this.clear();
        }

        public final Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedEntryIterator();
        }

        public final boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            HashMap.Node node = LinkedHashMap.this.getNode(HashMap.hash(key), key);
            if (node == null || !node.equals(entry)) {
                return false;
            }
            return true;
        }

        public final boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            if (LinkedHashMap.this.removeNode(HashMap.hash(key), key, entry.getValue(), true, true) != null) {
                return true;
            }
            return false;
        }

        public final Spliterator<Map.Entry<K, V>> spliterator() {
            return Spliterators.spliterator(this, 81);
        }

        public final void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            int i = LinkedHashMap.this.modCount;
            for (LinkedHashMapEntry<K, V> linkedHashMapEntry = LinkedHashMap.this.head; linkedHashMapEntry != null && i == LinkedHashMap.this.modCount; linkedHashMapEntry = linkedHashMapEntry.after) {
                consumer.accept(linkedHashMapEntry);
            }
            if (LinkedHashMap.this.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        biConsumer.getClass();
        int i = this.modCount;
        LinkedHashMapEntry<K, V> linkedHashMapEntry = this.head;
        while (this.modCount == i && linkedHashMapEntry != null) {
            biConsumer.accept(linkedHashMapEntry.key, linkedHashMapEntry.value);
            linkedHashMapEntry = linkedHashMapEntry.after;
        }
        if (this.modCount != i) {
            throw new ConcurrentModificationException();
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        biFunction.getClass();
        int i = this.modCount;
        LinkedHashMapEntry<K, V> linkedHashMapEntry = this.head;
        while (this.modCount == i && linkedHashMapEntry != null) {
            linkedHashMapEntry.value = biFunction.apply(linkedHashMapEntry.key, linkedHashMapEntry.value);
            linkedHashMapEntry = linkedHashMapEntry.after;
        }
        if (this.modCount != i) {
            throw new ConcurrentModificationException();
        }
    }

    abstract class LinkedHashIterator {
        LinkedHashMapEntry<K, V> current = null;
        int expectedModCount;
        LinkedHashMapEntry<K, V> next;

        LinkedHashIterator() {
            this.next = LinkedHashMap.this.head;
            this.expectedModCount = LinkedHashMap.this.modCount;
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        /* access modifiers changed from: package-private */
        public final LinkedHashMapEntry<K, V> nextNode() {
            LinkedHashMapEntry<K, V> linkedHashMapEntry = this.next;
            if (LinkedHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else if (linkedHashMapEntry != null) {
                this.current = linkedHashMapEntry;
                this.next = linkedHashMapEntry.after;
                return linkedHashMapEntry;
            } else {
                throw new NoSuchElementException();
            }
        }

        public final void remove() {
            LinkedHashMapEntry<K, V> linkedHashMapEntry = this.current;
            if (linkedHashMapEntry == null) {
                throw new IllegalStateException();
            } else if (LinkedHashMap.this.modCount == this.expectedModCount) {
                this.current = null;
                LinkedHashMap.this.removeNode(linkedHashMapEntry.hash, linkedHashMapEntry.key, (Object) null, false, false);
                this.expectedModCount = LinkedHashMap.this.modCount;
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    final class LinkedKeyIterator extends LinkedHashMap<K, V>.LinkedHashIterator implements Iterator<K> {
        LinkedKeyIterator() {
            super();
        }

        public final K next() {
            return nextNode().getKey();
        }
    }

    final class LinkedValueIterator extends LinkedHashMap<K, V>.LinkedHashIterator implements Iterator<V> {
        LinkedValueIterator() {
            super();
        }

        public final V next() {
            return nextNode().value;
        }
    }

    final class LinkedEntryIterator extends LinkedHashMap<K, V>.LinkedHashIterator implements Iterator<Map.Entry<K, V>> {
        LinkedEntryIterator() {
            super();
        }

        public final Map.Entry<K, V> next() {
            return nextNode();
        }
    }
}
