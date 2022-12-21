package java.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MIN_TREEIFY_CAPACITY = 64;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    private static final long serialVersionUID = 362498820763181265L;
    transient Set<Map.Entry<K, V>> entrySet;
    final float loadFactor;
    transient int modCount;
    transient int size;
    transient Node<K, V>[] table;
    int threshold;

    static final int tableSizeFor(int i) {
        int i2 = i - 1;
        int i3 = i2 | (i2 >>> 1);
        int i4 = i3 | (i3 >>> 2);
        int i5 = i4 | (i4 >>> 4);
        int i6 = i5 | (i5 >>> 8);
        int i7 = i6 | (i6 >>> 16);
        if (i7 < 0) {
            return 1;
        }
        if (i7 >= 1073741824) {
            return 1073741824;
        }
        return 1 + i7;
    }

    /* access modifiers changed from: package-private */
    public void afterNodeAccess(Node<K, V> node) {
    }

    /* access modifiers changed from: package-private */
    public void afterNodeInsertion(boolean z) {
    }

    /* access modifiers changed from: package-private */
    public void afterNodeRemoval(Node<K, V> node) {
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        Node<K, V> next;
        V value;

        Node(int i, K k, V v, Node<K, V> node) {
            this.hash = i;
            this.key = k;
            this.value = v;
            this.next = node;
        }

        public final K getKey() {
            return this.key;
        }

        public final V getValue() {
            return this.value;
        }

        public final String toString() {
            return this.key + "=" + this.value;
        }

        public final int hashCode() {
            return Objects.hashCode(this.value) ^ Objects.hashCode(this.key);
        }

        public final V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        public final boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            if (!Objects.equals(this.key, entry.getKey()) || !Objects.equals(this.value, entry.getValue())) {
                return false;
            }
            return true;
        }
    }

    static final int hash(Object obj) {
        if (obj == null) {
            return 0;
        }
        int hashCode = obj.hashCode();
        return hashCode ^ (hashCode >>> 16);
    }

    static Class<?> comparableClassFor(Object obj) {
        Type[] actualTypeArguments;
        if (!(obj instanceof Comparable)) {
            return null;
        }
        Class<?> cls = obj.getClass();
        if (cls == String.class) {
            return cls;
        }
        Type[] genericInterfaces = cls.getGenericInterfaces();
        if (genericInterfaces == null) {
            return null;
        }
        for (Type type : genericInterfaces) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getRawType() == Comparable.class && (actualTypeArguments = parameterizedType.getActualTypeArguments()) != null && actualTypeArguments.length == 1 && actualTypeArguments[0] == cls) {
                    return cls;
                }
            }
        }
        return null;
    }

    static int compareComparables(Class<?> cls, Object obj, Object obj2) {
        if (obj2 == null || obj2.getClass() != cls) {
            return 0;
        }
        return ((Comparable) obj).compareTo(obj2);
    }

    public HashMap(int i, float f) {
        if (i >= 0) {
            i = i > 1073741824 ? 1073741824 : i;
            if (f <= 0.0f || Float.isNaN(f)) {
                throw new IllegalArgumentException("Illegal load factor: " + f);
            }
            this.loadFactor = f;
            this.threshold = tableSizeFor(i);
            return;
        }
        throw new IllegalArgumentException("Illegal initial capacity: " + i);
    }

    public HashMap(int i) {
        this(i, 0.75f);
    }

    public HashMap() {
        this.loadFactor = 0.75f;
    }

    public HashMap(Map<? extends K, ? extends V> map) {
        this.loadFactor = 0.75f;
        putMapEntries(map, false);
    }

    /* access modifiers changed from: package-private */
    public final void putMapEntries(Map<? extends K, ? extends V> map, boolean z) {
        int size2 = map.size();
        if (size2 > 0) {
            if (this.table == null) {
                float f = (((float) size2) / this.loadFactor) + 1.0f;
                int i = f < 1.07374182E9f ? (int) f : 1073741824;
                if (i > this.threshold) {
                    this.threshold = tableSizeFor(i);
                }
            } else if (size2 > this.threshold) {
                resize();
            }
            for (Map.Entry next : map.entrySet()) {
                Object key = next.getKey();
                putVal(hash(key), key, next.getValue(), false, z);
            }
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public V get(Object obj) {
        Node node = getNode(hash(obj), obj);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    /* access modifiers changed from: package-private */
    public final Node<K, V> getNode(int i, Object obj) {
        int length;
        Node<K, V> node;
        K k;
        K k2;
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr == null || (length = nodeArr.length) <= 0 || (node = nodeArr[(length - 1) & i]) == null) {
            return null;
        }
        if (node.hash == i && ((k2 = node.key) == obj || (obj != null && obj.equals(k2)))) {
            return node;
        }
        Node<K, V> node2 = node.next;
        if (node2 == null) {
            return null;
        }
        if (node instanceof TreeNode) {
            return ((TreeNode) node).getTreeNode(i, obj);
        }
        do {
            if (node2.hash == i && ((k = node2.key) == obj || (obj != null && obj.equals(k)))) {
                return node2;
            }
            node2 = node2.next;
        } while (node2 != null);
        return null;
    }

    public boolean containsKey(Object obj) {
        return getNode(hash(obj), obj) != null;
    }

    public V put(K k, V v) {
        return putVal(hash(k), k, v, false, true);
    }

    /* access modifiers changed from: package-private */
    public final V putVal(int i, K k, V v, boolean z, boolean z2) {
        int i2;
        Node<K, V> node;
        K k2;
        K k3;
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr == null || (i2 = nodeArr.length) == 0) {
            nodeArr = resize();
            i2 = nodeArr.length;
        }
        Node<K, V>[] nodeArr2 = nodeArr;
        int i3 = (i2 - 1) & i;
        Node<K, V> node2 = nodeArr2[i3];
        if (node2 == null) {
            nodeArr2[i3] = newNode(i, k, v, (Node) null);
        } else {
            if (node2.hash != i || ((k3 = node2.key) != k && (k == null || !k.equals(k3)))) {
                if (node2 instanceof TreeNode) {
                    node2 = ((TreeNode) node2).putTreeVal(this, nodeArr2, i, k, v);
                } else {
                    int i4 = 0;
                    while (true) {
                        node = node2.next;
                        if (node != null) {
                            if (node.hash == i && ((k2 = node.key) == k || (k != null && k.equals(k2)))) {
                                break;
                            }
                            i4++;
                            node2 = node;
                        } else {
                            node2.next = newNode(i, k, v, (Node) null);
                            if (i4 >= 7) {
                                treeifyBin(nodeArr2, i);
                            }
                        }
                    }
                    node2 = node;
                }
            }
            if (node2 != null) {
                V v2 = node2.value;
                if (!z || v2 == null) {
                    node2.value = v;
                }
                afterNodeAccess(node2);
                return v2;
            }
        }
        this.modCount++;
        int i5 = this.size + 1;
        this.size = i5;
        if (i5 > this.threshold) {
            resize();
        }
        afterNodeInsertion(z2);
        return null;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0041 A[LOOP:0: B:26:0x0041->B:52:0x008d, LOOP_START, PHI: r1 
      PHI: (r1v1 int) = (r1v0 int), (r1v2 int) binds: [B:25:0x003f, B:52:0x008d] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.HashMap.Node<K, V>[] resize() {
        /*
            r13 = this;
            java.util.HashMap$Node<K, V>[] r0 = r13.table
            r1 = 0
            if (r0 != 0) goto L_0x0007
            r2 = r1
            goto L_0x0008
        L_0x0007:
            int r2 = r0.length
        L_0x0008:
            int r3 = r13.threshold
            r4 = 2147483647(0x7fffffff, float:NaN)
            r5 = 16
            r6 = 1073741824(0x40000000, float:2.0)
            if (r2 <= 0) goto L_0x0021
            if (r2 < r6) goto L_0x0018
            r13.threshold = r4
            return r0
        L_0x0018:
            int r7 = r2 << 1
            if (r7 >= r6) goto L_0x0024
            if (r2 < r5) goto L_0x0024
            int r3 = r3 << 1
            goto L_0x0029
        L_0x0021:
            if (r3 <= 0) goto L_0x0026
            r7 = r3
        L_0x0024:
            r3 = r1
            goto L_0x0029
        L_0x0026:
            r3 = 12
            r7 = r5
        L_0x0029:
            if (r3 != 0) goto L_0x0039
            float r3 = (float) r7
            float r5 = r13.loadFactor
            float r3 = r3 * r5
            if (r7 >= r6) goto L_0x0038
            r5 = 1317011456(0x4e800000, float:1.07374182E9)
            int r5 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r5 >= 0) goto L_0x0038
            int r4 = (int) r3
        L_0x0038:
            r3 = r4
        L_0x0039:
            r13.threshold = r3
            java.util.HashMap$Node[] r3 = new java.util.HashMap.Node[r7]
            r13.table = r3
            if (r0 == 0) goto L_0x0090
        L_0x0041:
            if (r1 >= r2) goto L_0x0090
            r4 = r0[r1]
            if (r4 == 0) goto L_0x008d
            r5 = 0
            r0[r1] = r5
            java.util.HashMap$Node<K, V> r6 = r4.next
            if (r6 != 0) goto L_0x0056
            int r5 = r4.hash
            int r6 = r7 + -1
            r5 = r5 & r6
            r3[r5] = r4
            goto L_0x008d
        L_0x0056:
            boolean r6 = r4 instanceof java.util.HashMap.TreeNode
            if (r6 == 0) goto L_0x0060
            java.util.HashMap$TreeNode r4 = (java.util.HashMap.TreeNode) r4
            r4.split(r13, r3, r1, r2)
            goto L_0x008d
        L_0x0060:
            r6 = r5
            r8 = r6
            r9 = r8
            r10 = r9
        L_0x0064:
            java.util.HashMap$Node<K, V> r11 = r4.next
            int r12 = r4.hash
            r12 = r12 & r2
            if (r12 != 0) goto L_0x0073
            if (r8 != 0) goto L_0x006f
            r9 = r4
            goto L_0x0071
        L_0x006f:
            r8.next = r4
        L_0x0071:
            r8 = r4
            goto L_0x007a
        L_0x0073:
            if (r6 != 0) goto L_0x0077
            r10 = r4
            goto L_0x0079
        L_0x0077:
            r6.next = r4
        L_0x0079:
            r6 = r4
        L_0x007a:
            if (r11 != 0) goto L_0x008b
            if (r8 == 0) goto L_0x0082
            r8.next = r5
            r3[r1] = r9
        L_0x0082:
            if (r6 == 0) goto L_0x008d
            r6.next = r5
            int r4 = r1 + r2
            r3[r4] = r10
            goto L_0x008d
        L_0x008b:
            r4 = r11
            goto L_0x0064
        L_0x008d:
            int r1 = r1 + 1
            goto L_0x0041
        L_0x0090:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.resize():java.util.HashMap$Node[]");
    }

    /* access modifiers changed from: package-private */
    public final void treeifyBin(Node<K, V>[] nodeArr, int i) {
        int length;
        if (nodeArr == null || (length = nodeArr.length) < 64) {
            resize();
            return;
        }
        int i2 = i & (length - 1);
        Node<K, V> node = nodeArr[i2];
        if (node != null) {
            TreeNode<K, V> treeNode = null;
            TreeNode<K, V> treeNode2 = null;
            while (true) {
                TreeNode<K, V> replacementTreeNode = replacementTreeNode(node, (Node<K, V>) null);
                if (treeNode == null) {
                    treeNode2 = replacementTreeNode;
                } else {
                    replacementTreeNode.prev = treeNode;
                    treeNode.next = replacementTreeNode;
                }
                node = node.next;
                if (node == null) {
                    break;
                }
                treeNode = replacementTreeNode;
            }
            nodeArr[i2] = treeNode2;
            if (treeNode2 != null) {
                treeNode2.treeify(nodeArr);
            }
        }
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        putMapEntries(map, true);
    }

    public V remove(Object obj) {
        Node removeNode = removeNode(hash(obj), obj, (Object) null, false, true);
        if (removeNode == null) {
            return null;
        }
        return removeNode.value;
    }

    /* access modifiers changed from: package-private */
    public final Node<K, V> removeNode(int i, Object obj, Object obj2, boolean z, boolean z2) {
        int length;
        int i2;
        TreeNode treeNode;
        Node<K, V> node;
        V v;
        K k;
        K k2;
        Node<K, V>[] nodeArr = this.table;
        if (!(nodeArr == null || (length = nodeArr.length) <= 0 || (treeNode = nodeArr[i2]) == null)) {
            if (treeNode.hash != i || ((k2 = treeNode.key) != obj && (obj == null || !obj.equals(k2)))) {
                Node<K, V> node2 = treeNode.next;
                if (node2 != null) {
                    if (!(treeNode instanceof TreeNode)) {
                        while (true) {
                            Node<K, V> node3 = node2;
                            node = treeNode;
                            treeNode = node3;
                            if (treeNode.hash == i && ((k = treeNode.key) == obj || (obj != null && obj.equals(k)))) {
                                break;
                            }
                            node2 = treeNode.next;
                            if (node2 == null) {
                                break;
                            }
                        }
                    } else {
                        node = treeNode;
                        treeNode = ((TreeNode) treeNode).getTreeNode(i, obj);
                    }
                } else {
                    node = treeNode;
                    treeNode = null;
                }
            } else {
                node = treeNode;
            }
            if (treeNode != null && (!z || (v = treeNode.value) == obj2 || (obj2 != null && obj2.equals(v)))) {
                if (treeNode instanceof TreeNode) {
                    treeNode.removeTreeNode(this, nodeArr, z2);
                } else if (treeNode == node) {
                    nodeArr[(i2 = (length - 1) & i)] = treeNode.next;
                } else {
                    node.next = treeNode.next;
                }
                this.modCount++;
                this.size--;
                afterNodeRemoval(treeNode);
                return treeNode;
            }
        }
        return null;
    }

    public void clear() {
        this.modCount++;
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null && this.size > 0) {
            this.size = 0;
            for (int i = 0; i < nodeArr.length; i++) {
                nodeArr[i] = null;
            }
        }
    }

    public boolean containsValue(Object obj) {
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null && this.size > 0) {
            for (Node<K, V> node : nodeArr) {
                while (node != null) {
                    V v = node.value;
                    if (v == obj) {
                        return true;
                    }
                    if (obj != null && obj.equals(v)) {
                        return true;
                    }
                    node = node.next;
                }
            }
        }
        return false;
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

    final class KeySet extends AbstractSet<K> {
        KeySet() {
        }

        public final int size() {
            return HashMap.this.size;
        }

        public final void clear() {
            HashMap.this.clear();
        }

        public final Iterator<K> iterator() {
            return new KeyIterator();
        }

        public final boolean contains(Object obj) {
            return HashMap.this.containsKey(obj);
        }

        public final boolean remove(Object obj) {
            return HashMap.this.removeNode(HashMap.hash(obj), obj, (Object) null, false, true) != null;
        }

        public final Spliterator<K> spliterator() {
            return new KeySpliterator(HashMap.this, 0, -1, 0, 0);
        }

        public final void forEach(Consumer<? super K> consumer) {
            Node<K, V>[] nodeArr;
            consumer.getClass();
            if (HashMap.this.size > 0 && (nodeArr = HashMap.this.table) != null) {
                int i = HashMap.this.modCount;
                for (int i2 = 0; i2 < nodeArr.length && HashMap.this.modCount == i; i2++) {
                    for (Node<K, V> node = nodeArr[i2]; node != null; node = node.next) {
                        consumer.accept(node.key);
                    }
                }
                if (HashMap.this.modCount != i) {
                    throw new ConcurrentModificationException();
                }
            }
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

    final class Values extends AbstractCollection<V> {
        Values() {
        }

        public final int size() {
            return HashMap.this.size;
        }

        public final void clear() {
            HashMap.this.clear();
        }

        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        public final boolean contains(Object obj) {
            return HashMap.this.containsValue(obj);
        }

        public final Spliterator<V> spliterator() {
            return new ValueSpliterator(HashMap.this, 0, -1, 0, 0);
        }

        public final void forEach(Consumer<? super V> consumer) {
            Node<K, V>[] nodeArr;
            consumer.getClass();
            if (HashMap.this.size > 0 && (nodeArr = HashMap.this.table) != null) {
                int i = HashMap.this.modCount;
                for (int i2 = 0; i2 < nodeArr.length && HashMap.this.modCount == i; i2++) {
                    for (Node<K, V> node = nodeArr[i2]; node != null; node = node.next) {
                        consumer.accept(node.value);
                    }
                }
                if (HashMap.this.modCount != i) {
                    throw new ConcurrentModificationException();
                }
            }
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

    final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        public final int size() {
            return HashMap.this.size;
        }

        public final void clear() {
            HashMap.this.clear();
        }

        public final Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public final boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Node node = HashMap.this.getNode(HashMap.hash(key), key);
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
            if (HashMap.this.removeNode(HashMap.hash(key), key, entry.getValue(), true, true) != null) {
                return true;
            }
            return false;
        }

        public final Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(HashMap.this, 0, -1, 0, 0);
        }

        public final void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
            Node<K, V>[] nodeArr;
            consumer.getClass();
            if (HashMap.this.size > 0 && (nodeArr = HashMap.this.table) != null) {
                int i = HashMap.this.modCount;
                for (int i2 = 0; i2 < nodeArr.length && HashMap.this.modCount == i; i2++) {
                    for (Node<K, V> node = nodeArr[i2]; node != null; node = node.next) {
                        consumer.accept(node);
                    }
                }
                if (HashMap.this.modCount != i) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    public V getOrDefault(Object obj, V v) {
        Node node = getNode(hash(obj), obj);
        return node == null ? v : node.value;
    }

    public V putIfAbsent(K k, V v) {
        return putVal(hash(k), k, v, true, true);
    }

    public boolean remove(Object obj, Object obj2) {
        return removeNode(hash(obj), obj, obj2, true, true) != null;
    }

    public boolean replace(K k, V v, V v2) {
        Node node = getNode(hash(k), k);
        if (node == null) {
            return false;
        }
        V v3 = node.value;
        if (v3 != v && (v3 == null || !v3.equals(v))) {
            return false;
        }
        node.value = v2;
        afterNodeAccess(node);
        return true;
    }

    public V replace(K k, V v) {
        Node node = getNode(hash(k), k);
        if (node == null) {
            return null;
        }
        V v2 = node.value;
        node.value = v;
        afterNodeAccess(node);
        return v2;
    }

    public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
        int i;
        Node<K, V>[] nodeArr;
        Node<K, V> node;
        TreeNode treeNode;
        V v;
        K k2;
        function.getClass();
        int hash = hash(k);
        if (this.size > this.threshold || (nodeArr = this.table) == null || (i = nodeArr.length) == 0) {
            nodeArr = resize();
            i = nodeArr.length;
        }
        Node<K, V>[] nodeArr2 = nodeArr;
        int i2 = (i - 1) & hash;
        Node<K, V> node2 = nodeArr2[i2];
        int i3 = 0;
        if (node2 != null) {
            if (node2 instanceof TreeNode) {
                treeNode = (TreeNode) node2;
                node = treeNode.getTreeNode(hash, k);
            } else {
                node = node2;
                while (true) {
                    if (node.hash != hash || ((k2 = node.key) != k && (k == null || !k.equals(k2)))) {
                        i3++;
                        node = node.next;
                        if (node == null) {
                            treeNode = null;
                            node = null;
                            break;
                        }
                    }
                }
                treeNode = null;
            }
            if (!(node == null || (v = node.value) == null)) {
                afterNodeAccess(node);
                return v;
            }
        } else {
            treeNode = null;
            node = null;
        }
        V apply = function.apply(k);
        if (apply == null) {
            return null;
        }
        if (node != null) {
            node.value = apply;
            afterNodeAccess(node);
            return apply;
        }
        if (treeNode != null) {
            treeNode.putTreeVal(this, nodeArr2, hash, k, apply);
        } else {
            nodeArr2[i2] = newNode(hash, k, apply, node2);
            if (i3 >= 7) {
                treeifyBin(nodeArr2, hash);
            }
        }
        this.modCount++;
        this.size++;
        afterNodeInsertion(true);
        return apply;
    }

    public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        V v;
        biFunction.getClass();
        int hash = hash(k);
        Node node = getNode(hash, k);
        if (node == null || (v = node.value) == null) {
            return null;
        }
        V apply = biFunction.apply(k, v);
        if (apply != null) {
            node.value = apply;
            afterNodeAccess(node);
            return apply;
        }
        removeNode(hash, k, (Object) null, false, true);
        return null;
    }

    public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        int i;
        Node<K, V>[] nodeArr;
        Node<K, V> node;
        TreeNode treeNode;
        K k2;
        biFunction.getClass();
        int hash = hash(k);
        if (this.size > this.threshold || (nodeArr = this.table) == null || (i = nodeArr.length) == 0) {
            nodeArr = resize();
            i = nodeArr.length;
        }
        Node<K, V>[] nodeArr2 = nodeArr;
        int i2 = (i - 1) & hash;
        Node<K, V> node2 = nodeArr2[i2];
        V v = null;
        int i3 = 0;
        if (node2 == null) {
            treeNode = null;
            node = null;
        } else if (node2 instanceof TreeNode) {
            treeNode = (TreeNode) node2;
            node = treeNode.getTreeNode(hash, k);
        } else {
            node = node2;
            while (true) {
                if (node.hash != hash || ((k2 = node.key) != k && (k == null || !k.equals(k2)))) {
                    i3++;
                    node = node.next;
                    if (node == null) {
                        break;
                    }
                }
            }
            treeNode = null;
        }
        if (node != null) {
            v = node.value;
        }
        V apply = biFunction.apply(k, v);
        if (node != null) {
            if (apply != null) {
                node.value = apply;
                afterNodeAccess(node);
            } else {
                removeNode(hash, k, (Object) null, false, true);
            }
        } else if (apply != null) {
            if (treeNode != null) {
                treeNode.putTreeVal(this, nodeArr2, hash, k, apply);
            } else {
                nodeArr2[i2] = newNode(hash, k, apply, node2);
                if (i3 >= 7) {
                    treeifyBin(nodeArr2, hash);
                }
            }
            this.modCount++;
            this.size++;
            afterNodeInsertion(true);
        }
        return apply;
    }

    /* JADX WARNING: type inference failed for: r12v0, types: [java.util.function.BiFunction<? super V, ? super V, ? extends V>, java.util.function.BiFunction, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public V merge(K r10, V r11, java.util.function.BiFunction<? super V, ? super V, ? extends V> r12) {
        /*
            r9 = this;
            r11.getClass()
            r12.getClass()
            int r3 = hash(r10)
            int r0 = r9.size
            int r1 = r9.threshold
            if (r0 > r1) goto L_0x0017
            java.util.HashMap$Node<K, V>[] r0 = r9.table
            if (r0 == 0) goto L_0x0017
            int r1 = r0.length
            if (r1 != 0) goto L_0x001c
        L_0x0017:
            java.util.HashMap$Node[] r0 = r9.resize()
            int r1 = r0.length
        L_0x001c:
            r2 = r0
            r6 = 1
            int r1 = r1 - r6
            r0 = r1 & r3
            r1 = r2[r0]
            r4 = 0
            r5 = 0
            if (r1 == 0) goto L_0x004b
            boolean r7 = r1 instanceof java.util.HashMap.TreeNode
            if (r7 == 0) goto L_0x0033
            r4 = r1
            java.util.HashMap$TreeNode r4 = (java.util.HashMap.TreeNode) r4
            java.util.HashMap$TreeNode r7 = r4.getTreeNode(r3, r10)
            goto L_0x004c
        L_0x0033:
            r7 = r1
        L_0x0034:
            int r8 = r7.hash
            if (r8 != r3) goto L_0x0045
            K r8 = r7.key
            if (r8 == r10) goto L_0x004c
            if (r10 == 0) goto L_0x0045
            boolean r8 = r10.equals(r8)
            if (r8 == 0) goto L_0x0045
            goto L_0x004c
        L_0x0045:
            int r5 = r5 + 1
            java.util.HashMap$Node<K, V> r7 = r7.next
            if (r7 != 0) goto L_0x0034
        L_0x004b:
            r7 = r4
        L_0x004c:
            if (r7 == 0) goto L_0x006b
            V r0 = r7.value
            if (r0 == 0) goto L_0x0058
            V r0 = r7.value
            java.lang.Object r11 = r12.apply(r0, r11)
        L_0x0058:
            if (r11 == 0) goto L_0x0060
            r7.value = r11
            r9.afterNodeAccess(r7)
            goto L_0x006a
        L_0x0060:
            r12 = 0
            r4 = 0
            r5 = 1
            r0 = r9
            r1 = r3
            r2 = r10
            r3 = r12
            r0.removeNode(r1, r2, r3, r4, r5)
        L_0x006a:
            return r11
        L_0x006b:
            if (r11 == 0) goto L_0x0090
            if (r4 == 0) goto L_0x0077
            r0 = r4
            r1 = r9
            r4 = r10
            r5 = r11
            r0.putTreeVal(r1, r2, r3, r4, r5)
            goto L_0x0083
        L_0x0077:
            java.util.HashMap$Node r10 = r9.newNode(r3, r10, r11, r1)
            r2[r0] = r10
            r10 = 7
            if (r5 < r10) goto L_0x0083
            r9.treeifyBin(r2, r3)
        L_0x0083:
            int r10 = r9.modCount
            int r10 = r10 + r6
            r9.modCount = r10
            int r10 = r9.size
            int r10 = r10 + r6
            r9.size = r10
            r9.afterNodeInsertion(r6)
        L_0x0090:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.merge(java.lang.Object, java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Node<K, V>[] nodeArr;
        biConsumer.getClass();
        if (this.size > 0 && (nodeArr = this.table) != null) {
            int i = this.modCount;
            for (int i2 = 0; i2 < nodeArr.length && i == this.modCount; i2++) {
                for (Node<K, V> node = nodeArr[i2]; node != null; node = node.next) {
                    biConsumer.accept(node.key, node.value);
                }
            }
            if (this.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Node<K, V>[] nodeArr;
        biFunction.getClass();
        if (this.size > 0 && (nodeArr = this.table) != null) {
            int i = this.modCount;
            for (Node<K, V> node : nodeArr) {
                while (node != null) {
                    node.value = biFunction.apply(node.key, node.value);
                    node = node.next;
                }
            }
            if (this.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public Object clone() {
        try {
            HashMap hashMap = (HashMap) super.clone();
            hashMap.reinitialize();
            hashMap.putMapEntries(this, false);
            return hashMap;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    /* access modifiers changed from: package-private */
    public final float loadFactor() {
        return this.loadFactor;
    }

    /* access modifiers changed from: package-private */
    public final int capacity() {
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            return nodeArr.length;
        }
        int i = this.threshold;
        if (i > 0) {
            return i;
        }
        return 16;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int capacity = capacity();
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(capacity);
        objectOutputStream.writeInt(this.size);
        internalWriteEntries(objectOutputStream);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        reinitialize();
        float f = this.loadFactor;
        if (f <= 0.0f || Float.isNaN(f)) {
            throw new InvalidObjectException("Illegal load factor: " + this.loadFactor);
        }
        objectInputStream.readInt();
        int readInt = objectInputStream.readInt();
        if (readInt < 0) {
            throw new InvalidObjectException("Illegal mappings count: " + readInt);
        } else if (readInt > 0) {
            float min = Math.min(Math.max(0.25f, this.loadFactor), 4.0f);
            float f2 = (((float) readInt) / min) + 1.0f;
            int tableSizeFor = f2 < 16.0f ? 16 : f2 >= 1.07374182E9f ? 1073741824 : tableSizeFor((int) f2);
            float f3 = ((float) tableSizeFor) * min;
            this.threshold = (tableSizeFor >= 1073741824 || f3 >= 1.07374182E9f) ? Integer.MAX_VALUE : (int) f3;
            this.table = new Node[tableSizeFor];
            for (int i = 0; i < readInt; i++) {
                Object readObject = objectInputStream.readObject();
                putVal(hash(readObject), readObject, objectInputStream.readObject(), false, false);
            }
        }
    }

    abstract class HashIterator {
        Node<K, V> current = null;
        int expectedModCount;
        int index = 0;
        Node<K, V> next = null;

        HashIterator() {
            Node<K, V> node;
            this.expectedModCount = HashMap.this.modCount;
            Node<K, V>[] nodeArr = HashMap.this.table;
            if (nodeArr != null && HashMap.this.size > 0) {
                do {
                    int i = this.index;
                    if (i < nodeArr.length) {
                        this.index = i + 1;
                        node = nodeArr[i];
                        this.next = node;
                    } else {
                        return;
                    }
                } while (node == null);
            }
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        /* access modifiers changed from: package-private */
        public final Node<K, V> nextNode() {
            Node<K, V>[] nodeArr;
            Node<K, V> node;
            Node<K, V> node2 = this.next;
            if (HashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else if (node2 != null) {
                this.current = node2;
                Node<K, V> node3 = node2.next;
                this.next = node3;
                if (node3 == null && (nodeArr = HashMap.this.table) != null) {
                    do {
                        int i = this.index;
                        if (i >= nodeArr.length) {
                            break;
                        }
                        this.index = i + 1;
                        node = nodeArr[i];
                        this.next = node;
                    } while (node == null);
                }
                return node2;
            } else {
                throw new NoSuchElementException();
            }
        }

        public final void remove() {
            Node<K, V> node = this.current;
            if (node == null) {
                throw new IllegalStateException();
            } else if (HashMap.this.modCount == this.expectedModCount) {
                this.current = null;
                K k = node.key;
                HashMap.this.removeNode(HashMap.hash(k), k, (Object) null, false, false);
                this.expectedModCount = HashMap.this.modCount;
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    final class KeyIterator extends HashMap<K, V>.HashIterator implements Iterator<K> {
        KeyIterator() {
            super();
        }

        public final K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashMap<K, V>.HashIterator implements Iterator<V> {
        ValueIterator() {
            super();
        }

        public final V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HashMap<K, V>.HashIterator implements Iterator<Map.Entry<K, V>> {
        EntryIterator() {
            super();
        }

        public final Map.Entry<K, V> next() {
            return nextNode();
        }
    }

    static class HashMapSpliterator<K, V> {
        Node<K, V> current;
        int est;
        int expectedModCount;
        int fence;
        int index;
        final HashMap<K, V> map;

        HashMapSpliterator(HashMap<K, V> hashMap, int i, int i2, int i3, int i4) {
            this.map = hashMap;
            this.index = i;
            this.fence = i2;
            this.est = i3;
            this.expectedModCount = i4;
        }

        /* access modifiers changed from: package-private */
        public final int getFence() {
            int i = this.fence;
            if (i < 0) {
                HashMap<K, V> hashMap = this.map;
                this.est = hashMap.size;
                this.expectedModCount = hashMap.modCount;
                Node<K, V>[] nodeArr = hashMap.table;
                if (nodeArr == null) {
                    i = 0;
                } else {
                    i = nodeArr.length;
                }
                this.fence = i;
            }
            return i;
        }

        public final long estimateSize() {
            getFence();
            return (long) this.est;
        }
    }

    static final class KeySpliterator<K, V> extends HashMapSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(HashMap<K, V> hashMap, int i, int i2, int i3, int i4) {
            super(hashMap, i, i2, i3, i4);
        }

        public KeySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = (fence + i) >>> 1;
            if (i >= i2 || this.current != null) {
                return null;
            }
            HashMap hashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new KeySpliterator(hashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super K> consumer) {
            int i;
            int i2;
            int i3;
            consumer.getClass();
            HashMap hashMap = this.map;
            Node<K, V>[] nodeArr = hashMap.table;
            int i4 = this.fence;
            if (i4 < 0) {
                int i5 = hashMap.modCount;
                this.expectedModCount = i5;
                if (nodeArr == null) {
                    i3 = 0;
                } else {
                    i3 = nodeArr.length;
                }
                this.fence = i3;
                int i6 = i3;
                i = i5;
                i4 = i6;
            } else {
                i = this.expectedModCount;
            }
            if (nodeArr != null && nodeArr.length >= i4 && (i2 = this.index) >= 0) {
                this.index = i4;
                if (i2 < i4 || this.current != null) {
                    Node<K, V> node = this.current;
                    this.current = null;
                    while (true) {
                        if (node == null) {
                            node = nodeArr[i2];
                            i2++;
                        } else {
                            consumer.accept(node.key);
                            node = node.next;
                        }
                        if (node == null && i2 >= i4) {
                            break;
                        }
                    }
                    if (hashMap.modCount != i) {
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }

        public boolean tryAdvance(Consumer<? super K> consumer) {
            int fence;
            consumer.getClass();
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr == null || nodeArr.length < (fence = getFence()) || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current == null && this.index >= fence) {
                    return false;
                }
                if (this.current == null) {
                    int i = this.index;
                    this.index = i + 1;
                    this.current = nodeArr[i];
                } else {
                    K k = this.current.key;
                    this.current = this.current.next;
                    consumer.accept(k);
                    if (this.map.modCount == this.expectedModCount) {
                        return true;
                    }
                    throw new ConcurrentModificationException();
                }
            }
        }

        public int characteristics() {
            return ((this.fence < 0 || this.est == this.map.size) ? 64 : 0) | 1;
        }
    }

    static final class ValueSpliterator<K, V> extends HashMapSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(HashMap<K, V> hashMap, int i, int i2, int i3, int i4) {
            super(hashMap, i, i2, i3, i4);
        }

        public ValueSpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = (fence + i) >>> 1;
            if (i >= i2 || this.current != null) {
                return null;
            }
            HashMap hashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new ValueSpliterator(hashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super V> consumer) {
            int i;
            int i2;
            int i3;
            consumer.getClass();
            HashMap hashMap = this.map;
            Node<K, V>[] nodeArr = hashMap.table;
            int i4 = this.fence;
            if (i4 < 0) {
                int i5 = hashMap.modCount;
                this.expectedModCount = i5;
                if (nodeArr == null) {
                    i3 = 0;
                } else {
                    i3 = nodeArr.length;
                }
                this.fence = i3;
                int i6 = i3;
                i = i5;
                i4 = i6;
            } else {
                i = this.expectedModCount;
            }
            if (nodeArr != null && nodeArr.length >= i4 && (i2 = this.index) >= 0) {
                this.index = i4;
                if (i2 < i4 || this.current != null) {
                    Node<K, V> node = this.current;
                    this.current = null;
                    while (true) {
                        if (node == null) {
                            node = nodeArr[i2];
                            i2++;
                        } else {
                            consumer.accept(node.value);
                            node = node.next;
                        }
                        if (node == null && i2 >= i4) {
                            break;
                        }
                    }
                    if (hashMap.modCount != i) {
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }

        public boolean tryAdvance(Consumer<? super V> consumer) {
            int fence;
            consumer.getClass();
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr == null || nodeArr.length < (fence = getFence()) || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current == null && this.index >= fence) {
                    return false;
                }
                if (this.current == null) {
                    int i = this.index;
                    this.index = i + 1;
                    this.current = nodeArr[i];
                } else {
                    V v = this.current.value;
                    this.current = this.current.next;
                    consumer.accept(v);
                    if (this.map.modCount == this.expectedModCount) {
                        return true;
                    }
                    throw new ConcurrentModificationException();
                }
            }
        }

        public int characteristics() {
            return (this.fence < 0 || this.est == this.map.size) ? 64 : 0;
        }
    }

    static final class EntrySpliterator<K, V> extends HashMapSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        EntrySpliterator(HashMap<K, V> hashMap, int i, int i2, int i3, int i4) {
            super(hashMap, i, i2, i3, i4);
        }

        public EntrySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = (fence + i) >>> 1;
            if (i >= i2 || this.current != null) {
                return null;
            }
            HashMap hashMap = this.map;
            this.index = i2;
            int i3 = this.est >>> 1;
            this.est = i3;
            return new EntrySpliterator(hashMap, i, i2, i3, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            int i;
            int i2;
            int i3;
            consumer.getClass();
            HashMap hashMap = this.map;
            Node<K, V>[] nodeArr = hashMap.table;
            int i4 = this.fence;
            if (i4 < 0) {
                int i5 = hashMap.modCount;
                this.expectedModCount = i5;
                if (nodeArr == null) {
                    i3 = 0;
                } else {
                    i3 = nodeArr.length;
                }
                this.fence = i3;
                int i6 = i3;
                i = i5;
                i4 = i6;
            } else {
                i = this.expectedModCount;
            }
            if (nodeArr != null && nodeArr.length >= i4 && (i2 = this.index) >= 0) {
                this.index = i4;
                if (i2 < i4 || this.current != null) {
                    Node<K, V> node = this.current;
                    this.current = null;
                    while (true) {
                        if (node == null) {
                            node = nodeArr[i2];
                            i2++;
                        } else {
                            consumer.accept(node);
                            node = node.next;
                        }
                        if (node == null && i2 >= i4) {
                            break;
                        }
                    }
                    if (hashMap.modCount != i) {
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }

        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            int fence;
            consumer.getClass();
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr == null || nodeArr.length < (fence = getFence()) || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current == null && this.index >= fence) {
                    return false;
                }
                if (this.current == null) {
                    int i = this.index;
                    this.index = i + 1;
                    this.current = nodeArr[i];
                } else {
                    Node node = this.current;
                    this.current = this.current.next;
                    consumer.accept(node);
                    if (this.map.modCount == this.expectedModCount) {
                        return true;
                    }
                    throw new ConcurrentModificationException();
                }
            }
        }

        public int characteristics() {
            return ((this.fence < 0 || this.est == this.map.size) ? 64 : 0) | 1;
        }
    }

    /* access modifiers changed from: package-private */
    public Node<K, V> newNode(int i, K k, V v, Node<K, V> node) {
        return new Node<>(i, k, v, node);
    }

    /* access modifiers changed from: package-private */
    public Node<K, V> replacementNode(Node<K, V> node, Node<K, V> node2) {
        return new Node<>(node.hash, node.key, node.value, node2);
    }

    /* access modifiers changed from: package-private */
    public TreeNode<K, V> newTreeNode(int i, K k, V v, Node<K, V> node) {
        return new TreeNode<>(i, k, v, node);
    }

    /* access modifiers changed from: package-private */
    public TreeNode<K, V> replacementTreeNode(Node<K, V> node, Node<K, V> node2) {
        return new TreeNode<>(node.hash, node.key, node.value, node2);
    }

    /* access modifiers changed from: package-private */
    public void reinitialize() {
        this.table = null;
        this.entrySet = null;
        this.keySet = null;
        this.values = null;
        this.modCount = 0;
        this.threshold = 0;
        this.size = 0;
    }

    /* access modifiers changed from: package-private */
    public void internalWriteEntries(ObjectOutputStream objectOutputStream) throws IOException {
        Node<K, V>[] nodeArr;
        if (this.size > 0 && (nodeArr = this.table) != null) {
            for (Node<K, V> node : nodeArr) {
                while (node != null) {
                    objectOutputStream.writeObject(node.key);
                    objectOutputStream.writeObject(node.value);
                    node = node.next;
                }
            }
        }
    }

    static final class TreeNode<K, V> extends LinkedHashMap.LinkedHashMapEntry<K, V> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        TreeNode<K, V> left;
        TreeNode<K, V> parent;
        TreeNode<K, V> prev;
        boolean red;
        TreeNode<K, V> right;

        static {
            Class<HashMap> cls = HashMap.class;
        }

        TreeNode(int i, K k, V v, Node<K, V> node) {
            super(i, k, v, node);
        }

        /* access modifiers changed from: package-private */
        public final TreeNode<K, V> root() {
            while (true) {
                TreeNode<K, V> treeNode = this.parent;
                if (treeNode == null) {
                    return this;
                }
                this = treeNode;
            }
        }

        static <K, V> void moveRootToFront(Node<K, V>[] nodeArr, TreeNode<K, V> treeNode) {
            int length;
            if (treeNode != null && nodeArr != null && (length = nodeArr.length) > 0) {
                int i = (length - 1) & treeNode.hash;
                TreeNode<K, V> treeNode2 = (TreeNode) nodeArr[i];
                if (treeNode != treeNode2) {
                    nodeArr[i] = treeNode;
                    TreeNode<K, V> treeNode3 = treeNode.prev;
                    Node node = treeNode.next;
                    if (node != null) {
                        ((TreeNode) node).prev = treeNode3;
                    }
                    if (treeNode3 != null) {
                        treeNode3.next = node;
                    }
                    if (treeNode2 != null) {
                        treeNode2.prev = treeNode;
                    }
                    treeNode.next = treeNode2;
                    treeNode.prev = null;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final TreeNode<K, V> find(int i, Object obj, Class<?> cls) {
            int compareComparables;
            do {
                TreeNode<K, V> treeNode = r4.left;
                TreeNode<K, V> treeNode2 = r4.right;
                int i2 = r4.hash;
                if (i2 <= i) {
                    if (i2 >= i) {
                        Object obj2 = r4.key;
                        if (obj2 == obj) {
                            return r4;
                        }
                        if (obj != null && obj.equals(obj2)) {
                            return r4;
                        }
                        if (treeNode != null) {
                            if (treeNode2 != null) {
                                if ((cls == null && (cls = HashMap.comparableClassFor(obj)) == null) || (compareComparables = HashMap.compareComparables(cls, obj, obj2)) == 0) {
                                    TreeNode<K, V> find = treeNode2.find(i, obj, cls);
                                    if (find != null) {
                                        return find;
                                    }
                                } else if (compareComparables >= 0) {
                                    treeNode = treeNode2;
                                }
                            }
                        }
                    }
                    r4 = treeNode2;
                    continue;
                }
                r4 = treeNode;
                continue;
            } while (r4 != null);
            return null;
        }

        /* access modifiers changed from: package-private */
        public final TreeNode<K, V> getTreeNode(int i, Object obj) {
            if (this.parent != null) {
                this = root();
            }
            return this.find(i, obj, (Class<?>) null);
        }

        static int tieBreakOrder(Object obj, Object obj2) {
            int compareTo;
            if (obj != null && obj2 != null && (compareTo = obj.getClass().getName().compareTo(obj2.getClass().getName())) != 0) {
                return compareTo;
            }
            return System.identityHashCode(obj) <= System.identityHashCode(obj2) ? -1 : 1;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
            r6 = java.util.HashMap.comparableClassFor(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x002f, code lost:
            r8 = java.util.HashMap.compareComparables(r6, r3, r7);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void treeify(java.util.HashMap.Node<K, V>[] r10) {
            /*
                r9 = this;
                r0 = 0
                r1 = r0
            L_0x0002:
                if (r9 == 0) goto L_0x0056
                java.util.HashMap$Node r2 = r9.next
                java.util.HashMap$TreeNode r2 = (java.util.HashMap.TreeNode) r2
                r9.right = r0
                r9.left = r0
                if (r1 != 0) goto L_0x0015
                r9.parent = r0
                r1 = 0
                r9.red = r1
            L_0x0013:
                r1 = r9
                goto L_0x0052
            L_0x0015:
                java.lang.Object r3 = r9.key
                int r4 = r9.hash
                r6 = r0
                r5 = r1
            L_0x001b:
                java.lang.Object r7 = r5.key
                int r8 = r5.hash
                if (r8 <= r4) goto L_0x0023
                r7 = -1
                goto L_0x003b
            L_0x0023:
                if (r8 >= r4) goto L_0x0027
                r7 = 1
                goto L_0x003b
            L_0x0027:
                if (r6 != 0) goto L_0x002f
                java.lang.Class r6 = java.util.HashMap.comparableClassFor(r3)
                if (r6 == 0) goto L_0x0035
            L_0x002f:
                int r8 = java.util.HashMap.compareComparables(r6, r3, r7)
                if (r8 != 0) goto L_0x003a
            L_0x0035:
                int r7 = tieBreakOrder(r3, r7)
                goto L_0x003b
            L_0x003a:
                r7 = r8
            L_0x003b:
                if (r7 > 0) goto L_0x0040
                java.util.HashMap$TreeNode<K, V> r8 = r5.left
                goto L_0x0042
            L_0x0040:
                java.util.HashMap$TreeNode<K, V> r8 = r5.right
            L_0x0042:
                if (r8 != 0) goto L_0x0054
                r9.parent = r5
                if (r7 > 0) goto L_0x004b
                r5.left = r9
                goto L_0x004d
            L_0x004b:
                r5.right = r9
            L_0x004d:
                java.util.HashMap$TreeNode r9 = balanceInsertion(r1, r9)
                goto L_0x0013
            L_0x0052:
                r9 = r2
                goto L_0x0002
            L_0x0054:
                r5 = r8
                goto L_0x001b
            L_0x0056:
                moveRootToFront(r10, r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.TreeNode.treeify(java.util.HashMap$Node[]):void");
        }

        /* access modifiers changed from: package-private */
        public final Node<K, V> untreeify(HashMap<K, V> hashMap) {
            Node<K, V> node = null;
            Node<K, V> node2 = null;
            for (this = this; this != null; this = this.next) {
                Node<K, V> replacementNode = hashMap.replacementNode(this, (Node<K, V>) null);
                if (node2 == null) {
                    node = replacementNode;
                } else {
                    node2.next = replacementNode;
                }
                node2 = replacementNode;
            }
            return node;
        }

        /* access modifiers changed from: package-private */
        public final TreeNode<K, V> putTreeVal(HashMap<K, V> hashMap, Node<K, V>[] nodeArr, int i, K k, V v) {
            int i2;
            TreeNode<K, V> treeNode;
            TreeNode<K, V> treeNode2;
            int compareComparables;
            if (this.parent != null) {
                this = root();
            }
            boolean z = false;
            TreeNode<K, V> treeNode3 = this;
            Class<?> cls = null;
            while (true) {
                int i3 = treeNode3.hash;
                if (i3 > i) {
                    i2 = -1;
                } else if (i3 < i) {
                    i2 = 1;
                } else {
                    K k2 = treeNode3.key;
                    if (k2 == k || (k != null && k.equals(k2))) {
                        return treeNode3;
                    }
                    if ((cls == null && (cls = HashMap.comparableClassFor(k)) == null) || (compareComparables = HashMap.compareComparables(cls, k, k2)) == 0) {
                        if (!z) {
                            TreeNode<K, V> treeNode4 = treeNode3.left;
                            if ((treeNode4 == null || (treeNode = treeNode4.find(i, k, cls)) == null) && ((treeNode2 = treeNode3.right) == null || (treeNode = treeNode2.find(i, k, cls)) == null)) {
                                z = true;
                            }
                        }
                        i2 = tieBreakOrder(k, k2);
                    } else {
                        i2 = compareComparables;
                    }
                }
                TreeNode<K, V> treeNode5 = i2 <= 0 ? treeNode3.left : treeNode3.right;
                if (treeNode5 == null) {
                    Node node = treeNode3.next;
                    TreeNode<K, V> newTreeNode = hashMap.newTreeNode(i, k, v, node);
                    if (i2 <= 0) {
                        treeNode3.left = newTreeNode;
                    } else {
                        treeNode3.right = newTreeNode;
                    }
                    treeNode3.next = newTreeNode;
                    newTreeNode.prev = treeNode3;
                    newTreeNode.parent = treeNode3;
                    if (node != null) {
                        ((TreeNode) node).prev = newTreeNode;
                    }
                    moveRootToFront(nodeArr, balanceInsertion(this, newTreeNode));
                    return null;
                }
                treeNode3 = treeNode5;
            }
            return treeNode;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x00a1  */
        /* JADX WARNING: Removed duplicated region for block: B:70:0x00bd  */
        /* JADX WARNING: Removed duplicated region for block: B:72:0x00c3  */
        /* JADX WARNING: Removed duplicated region for block: B:81:0x00d8  */
        /* JADX WARNING: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void removeTreeNode(java.util.HashMap<K, V> r9, java.util.HashMap.Node<K, V>[] r10, boolean r11) {
            /*
                r8 = this;
                if (r10 == 0) goto L_0x00e2
                int r0 = r10.length
                if (r0 != 0) goto L_0x0007
                goto L_0x00e2
            L_0x0007:
                int r0 = r0 + -1
                int r1 = r8.hash
                r0 = r0 & r1
                r1 = r10[r0]
                java.util.HashMap$TreeNode r1 = (java.util.HashMap.TreeNode) r1
                java.util.HashMap$Node r2 = r8.next
                java.util.HashMap$TreeNode r2 = (java.util.HashMap.TreeNode) r2
                java.util.HashMap$TreeNode<K, V> r3 = r8.prev
                if (r3 != 0) goto L_0x001c
                r10[r0] = r2
                r4 = r2
                goto L_0x001f
            L_0x001c:
                r3.next = r2
                r4 = r1
            L_0x001f:
                if (r2 == 0) goto L_0x0023
                r2.prev = r3
            L_0x0023:
                if (r4 != 0) goto L_0x0026
                return
            L_0x0026:
                java.util.HashMap$TreeNode<K, V> r2 = r1.parent
                if (r2 == 0) goto L_0x002e
                java.util.HashMap$TreeNode r1 = r1.root()
            L_0x002e:
                if (r1 == 0) goto L_0x00dc
                java.util.HashMap$TreeNode<K, V> r2 = r1.right
                if (r2 == 0) goto L_0x00dc
                java.util.HashMap$TreeNode<K, V> r2 = r1.left
                if (r2 == 0) goto L_0x00dc
                java.util.HashMap$TreeNode<K, V> r2 = r2.left
                if (r2 != 0) goto L_0x003e
                goto L_0x00dc
            L_0x003e:
                java.util.HashMap$TreeNode<K, V> r9 = r8.left
                java.util.HashMap$TreeNode<K, V> r0 = r8.right
                r2 = 0
                if (r9 == 0) goto L_0x0097
                if (r0 == 0) goto L_0x0097
                r3 = r0
            L_0x0048:
                java.util.HashMap$TreeNode<K, V> r4 = r3.left
                if (r4 == 0) goto L_0x004e
                r3 = r4
                goto L_0x0048
            L_0x004e:
                boolean r4 = r3.red
                boolean r5 = r8.red
                r3.red = r5
                r8.red = r4
                java.util.HashMap$TreeNode<K, V> r4 = r3.right
                java.util.HashMap$TreeNode<K, V> r5 = r8.parent
                if (r3 != r0) goto L_0x0061
                r8.parent = r3
                r3.right = r8
                goto L_0x0076
            L_0x0061:
                java.util.HashMap$TreeNode<K, V> r6 = r3.parent
                r8.parent = r6
                if (r6 == 0) goto L_0x0070
                java.util.HashMap$TreeNode<K, V> r7 = r6.left
                if (r3 != r7) goto L_0x006e
                r6.left = r8
                goto L_0x0070
            L_0x006e:
                r6.right = r8
            L_0x0070:
                r3.right = r0
                if (r0 == 0) goto L_0x0076
                r0.parent = r3
            L_0x0076:
                r8.left = r2
                r8.right = r4
                if (r4 == 0) goto L_0x007e
                r4.parent = r8
            L_0x007e:
                r3.left = r9
                if (r9 == 0) goto L_0x0084
                r9.parent = r3
            L_0x0084:
                r3.parent = r5
                if (r5 != 0) goto L_0x008a
                r1 = r3
                goto L_0x0093
            L_0x008a:
                java.util.HashMap$TreeNode<K, V> r9 = r5.left
                if (r8 != r9) goto L_0x0091
                r5.left = r3
                goto L_0x0093
            L_0x0091:
                r5.right = r3
            L_0x0093:
                if (r4 == 0) goto L_0x009e
                r9 = r4
                goto L_0x009f
            L_0x0097:
                if (r9 == 0) goto L_0x009a
                goto L_0x009f
            L_0x009a:
                if (r0 == 0) goto L_0x009e
                r9 = r0
                goto L_0x009f
            L_0x009e:
                r9 = r8
            L_0x009f:
                if (r9 == r8) goto L_0x00b8
                java.util.HashMap$TreeNode<K, V> r0 = r8.parent
                r9.parent = r0
                if (r0 != 0) goto L_0x00a9
                r1 = r9
                goto L_0x00b2
            L_0x00a9:
                java.util.HashMap$TreeNode<K, V> r3 = r0.left
                if (r8 != r3) goto L_0x00b0
                r0.left = r9
                goto L_0x00b2
            L_0x00b0:
                r0.right = r9
            L_0x00b2:
                r8.parent = r2
                r8.right = r2
                r8.left = r2
            L_0x00b8:
                boolean r0 = r8.red
                if (r0 == 0) goto L_0x00bd
                goto L_0x00c1
            L_0x00bd:
                java.util.HashMap$TreeNode r1 = balanceDeletion(r1, r9)
            L_0x00c1:
                if (r9 != r8) goto L_0x00d6
                java.util.HashMap$TreeNode<K, V> r9 = r8.parent
                r8.parent = r2
                if (r9 == 0) goto L_0x00d6
                java.util.HashMap$TreeNode<K, V> r0 = r9.left
                if (r8 != r0) goto L_0x00d0
                r9.left = r2
                goto L_0x00d6
            L_0x00d0:
                java.util.HashMap$TreeNode<K, V> r0 = r9.right
                if (r8 != r0) goto L_0x00d6
                r9.right = r2
            L_0x00d6:
                if (r11 == 0) goto L_0x00db
                moveRootToFront(r10, r1)
            L_0x00db:
                return
            L_0x00dc:
                java.util.HashMap$Node r8 = r4.untreeify(r9)
                r10[r0] = r8
            L_0x00e2:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.TreeNode.removeTreeNode(java.util.HashMap, java.util.HashMap$Node[], boolean):void");
        }

        /* access modifiers changed from: package-private */
        public final void split(HashMap<K, V> hashMap, Node<K, V>[] nodeArr, int i, int i2) {
            int i3 = 0;
            TreeNode<K, V> treeNode = null;
            TreeNode<K, V> treeNode2 = null;
            TreeNode<K, V> treeNode3 = null;
            TreeNode<K, V> treeNode4 = null;
            int i4 = 0;
            while (this != null) {
                TreeNode<K, V> treeNode5 = (TreeNode) this.next;
                this.next = null;
                if ((this.hash & i2) == 0) {
                    this.prev = treeNode3;
                    if (treeNode3 == null) {
                        treeNode = this;
                    } else {
                        treeNode3.next = this;
                    }
                    i3++;
                    treeNode3 = this;
                } else {
                    this.prev = treeNode2;
                    if (treeNode2 == null) {
                        treeNode4 = this;
                    } else {
                        treeNode2.next = this;
                    }
                    i4++;
                    treeNode2 = this;
                }
                this = treeNode5;
            }
            if (treeNode != null) {
                if (i3 <= 6) {
                    nodeArr[i] = treeNode.untreeify(hashMap);
                } else {
                    nodeArr[i] = treeNode;
                    if (treeNode4 != null) {
                        treeNode.treeify(nodeArr);
                    }
                }
            }
            if (treeNode4 == null) {
                return;
            }
            if (i4 <= 6) {
                nodeArr[i + i2] = treeNode4.untreeify(hashMap);
                return;
            }
            nodeArr[i + i2] = treeNode4;
            if (treeNode != null) {
                treeNode4.treeify(nodeArr);
            }
        }

        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode<K, V> treeNode3;
            if (!(treeNode2 == null || (treeNode3 = treeNode2.right) == null)) {
                TreeNode<K, V> treeNode4 = treeNode3.left;
                treeNode2.right = treeNode4;
                if (treeNode4 != null) {
                    treeNode4.parent = treeNode2;
                }
                TreeNode<K, V> treeNode5 = treeNode2.parent;
                treeNode3.parent = treeNode5;
                if (treeNode5 == null) {
                    treeNode3.red = false;
                    treeNode = treeNode3;
                } else if (treeNode5.left == treeNode2) {
                    treeNode5.left = treeNode3;
                } else {
                    treeNode5.right = treeNode3;
                }
                treeNode3.left = treeNode2;
                treeNode2.parent = treeNode3;
            }
            return treeNode;
        }

        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode<K, V> treeNode3;
            if (!(treeNode2 == null || (treeNode3 = treeNode2.left) == null)) {
                TreeNode<K, V> treeNode4 = treeNode3.right;
                treeNode2.left = treeNode4;
                if (treeNode4 != null) {
                    treeNode4.parent = treeNode2;
                }
                TreeNode<K, V> treeNode5 = treeNode2.parent;
                treeNode3.parent = treeNode5;
                if (treeNode5 == null) {
                    treeNode3.red = false;
                    treeNode = treeNode3;
                } else if (treeNode5.right == treeNode2) {
                    treeNode5.right = treeNode3;
                } else {
                    treeNode5.left = treeNode3;
                }
                treeNode3.right = treeNode2;
                treeNode2.parent = treeNode3;
            }
            return treeNode;
        }

        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode<K, V> treeNode3;
            treeNode2.red = true;
            while (true) {
                TreeNode<K, V> treeNode4 = treeNode2.parent;
                if (treeNode4 == null) {
                    treeNode2.red = false;
                    return treeNode2;
                } else if (!treeNode4.red || (treeNode3 = treeNode4.parent) == null) {
                    return treeNode;
                } else {
                    TreeNode<K, V> treeNode5 = treeNode3.left;
                    if (treeNode4 == treeNode5) {
                        TreeNode<K, V> treeNode6 = treeNode3.right;
                        if (treeNode6 == null || !treeNode6.red) {
                            if (treeNode2 == treeNode4.right) {
                                treeNode = rotateLeft(treeNode, treeNode4);
                                TreeNode<K, V> treeNode7 = treeNode4.parent;
                                treeNode3 = treeNode7 == null ? null : treeNode7.parent;
                                TreeNode<K, V> treeNode8 = treeNode4;
                                treeNode4 = treeNode7;
                                treeNode2 = treeNode8;
                            }
                            if (treeNode4 != null) {
                                treeNode4.red = false;
                                if (treeNode3 != null) {
                                    treeNode3.red = true;
                                    treeNode = rotateRight(treeNode, treeNode3);
                                }
                            }
                        } else {
                            treeNode6.red = false;
                            treeNode4.red = false;
                            treeNode3.red = true;
                        }
                    } else if (treeNode5 == null || !treeNode5.red) {
                        if (treeNode2 == treeNode4.left) {
                            treeNode = rotateRight(treeNode, treeNode4);
                            TreeNode<K, V> treeNode9 = treeNode4.parent;
                            treeNode3 = treeNode9 == null ? null : treeNode9.parent;
                            TreeNode<K, V> treeNode10 = treeNode4;
                            treeNode4 = treeNode9;
                            treeNode2 = treeNode10;
                        }
                        if (treeNode4 != null) {
                            treeNode4.red = false;
                            if (treeNode3 != null) {
                                treeNode3.red = true;
                                treeNode = rotateLeft(treeNode, treeNode3);
                            }
                        }
                    } else {
                        treeNode5.red = false;
                        treeNode4.red = false;
                        treeNode3.red = true;
                    }
                    treeNode2 = treeNode3;
                }
            }
            return treeNode;
        }

        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            boolean z;
            boolean z2;
            while (treeNode2 != null && treeNode2 != treeNode) {
                TreeNode<K, V> treeNode3 = treeNode2.parent;
                if (treeNode3 == null) {
                    treeNode2.red = false;
                    return treeNode2;
                } else if (treeNode2.red) {
                    treeNode2.red = false;
                    return treeNode;
                } else {
                    TreeNode<K, V> treeNode4 = treeNode3.left;
                    TreeNode<K, V> treeNode5 = null;
                    if (treeNode4 == treeNode2) {
                        TreeNode<K, V> treeNode6 = treeNode3.right;
                        if (treeNode6 != null && treeNode6.red) {
                            treeNode6.red = false;
                            treeNode3.red = true;
                            treeNode = rotateLeft(treeNode, treeNode3);
                            treeNode3 = treeNode2.parent;
                            treeNode6 = treeNode3 == null ? null : treeNode3.right;
                        }
                        if (treeNode6 != null) {
                            TreeNode<K, V> treeNode7 = treeNode6.left;
                            TreeNode<K, V> treeNode8 = treeNode6.right;
                            if ((treeNode8 == null || !treeNode8.red) && (treeNode7 == null || !treeNode7.red)) {
                                treeNode6.red = true;
                            } else {
                                if (treeNode8 == null || !treeNode8.red) {
                                    if (treeNode7 != null) {
                                        treeNode7.red = false;
                                    }
                                    treeNode6.red = true;
                                    treeNode = rotateRight(treeNode, treeNode6);
                                    treeNode3 = treeNode2.parent;
                                    if (treeNode3 != null) {
                                        treeNode5 = treeNode3.right;
                                    }
                                    treeNode6 = treeNode5;
                                }
                                if (treeNode6 != null) {
                                    if (treeNode3 == null) {
                                        z2 = false;
                                    } else {
                                        z2 = treeNode3.red;
                                    }
                                    treeNode6.red = z2;
                                    TreeNode<K, V> treeNode9 = treeNode6.right;
                                    if (treeNode9 != null) {
                                        treeNode9.red = false;
                                    }
                                }
                                if (treeNode3 != null) {
                                    treeNode3.red = false;
                                    treeNode = rotateLeft(treeNode, treeNode3);
                                }
                            }
                        }
                        treeNode2 = treeNode3;
                    } else {
                        if (treeNode4 != null && treeNode4.red) {
                            treeNode4.red = false;
                            treeNode3.red = true;
                            treeNode = rotateRight(treeNode, treeNode3);
                            treeNode3 = treeNode2.parent;
                            treeNode4 = treeNode3 == null ? null : treeNode3.left;
                        }
                        if (treeNode4 != null) {
                            TreeNode<K, V> treeNode10 = treeNode4.left;
                            TreeNode<K, V> treeNode11 = treeNode4.right;
                            if ((treeNode10 == null || !treeNode10.red) && (treeNode11 == null || !treeNode11.red)) {
                                treeNode4.red = true;
                            } else {
                                if (treeNode10 == null || !treeNode10.red) {
                                    if (treeNode11 != null) {
                                        treeNode11.red = false;
                                    }
                                    treeNode4.red = true;
                                    treeNode = rotateLeft(treeNode, treeNode4);
                                    treeNode3 = treeNode2.parent;
                                    if (treeNode3 != null) {
                                        treeNode5 = treeNode3.left;
                                    }
                                    treeNode4 = treeNode5;
                                }
                                if (treeNode4 != null) {
                                    if (treeNode3 == null) {
                                        z = false;
                                    } else {
                                        z = treeNode3.red;
                                    }
                                    treeNode4.red = z;
                                    TreeNode<K, V> treeNode12 = treeNode4.left;
                                    if (treeNode12 != null) {
                                        treeNode12.red = false;
                                    }
                                }
                                if (treeNode3 != null) {
                                    treeNode3.red = false;
                                    treeNode = rotateRight(treeNode, treeNode3);
                                }
                            }
                        }
                        treeNode2 = treeNode3;
                    }
                    treeNode2 = treeNode;
                }
            }
            return treeNode;
        }

        static <K, V> boolean checkInvariants(TreeNode<K, V> treeNode) {
            TreeNode<K, V> treeNode2 = treeNode.parent;
            TreeNode<K, V> treeNode3 = treeNode.left;
            TreeNode<K, V> treeNode4 = treeNode.right;
            TreeNode<K, V> treeNode5 = treeNode.prev;
            TreeNode treeNode6 = (TreeNode) treeNode.next;
            if (treeNode5 != null && treeNode5.next != treeNode) {
                return false;
            }
            if (treeNode6 != null && treeNode6.prev != treeNode) {
                return false;
            }
            if (treeNode2 != null && treeNode != treeNode2.left && treeNode != treeNode2.right) {
                return false;
            }
            if (treeNode3 != null && (treeNode3.parent != treeNode || treeNode3.hash > treeNode.hash)) {
                return false;
            }
            if (treeNode4 != null && (treeNode4.parent != treeNode || treeNode4.hash < treeNode.hash)) {
                return false;
            }
            if (treeNode.red && treeNode3 != null && treeNode3.red && treeNode4 != null && treeNode4.red) {
                return false;
            }
            if (treeNode3 != null && !checkInvariants(treeNode3)) {
                return false;
            }
            if (treeNode4 == null || checkInvariants(treeNode4)) {
                return true;
            }
            return false;
        }
    }
}
