package java.util.concurrent;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import jdk.internal.misc.Unsafe;

public class ConcurrentHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    private static final int ABASE;
    private static final int ASHIFT;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    static final int HASH_BITS = Integer.MAX_VALUE;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int MAX_RESIZERS = 65535;
    private static final int MIN_TRANSFER_STRIDE = 16;
    static final int MIN_TREEIFY_CAPACITY = 64;
    static final int MOVED = -1;
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    static final int RESERVED = -3;
    private static final int RESIZE_STAMP_BITS = 16;
    private static final int RESIZE_STAMP_SHIFT = 16;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    static final int TREEBIN = -2;
    static final int TREEIFY_THRESHOLD = 8;

    /* renamed from: U */
    private static final Unsafe f735U;
    static final int UNTREEIFY_THRESHOLD = 6;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE)};
    private static final long serialVersionUID = 7249069246763182397L;
    private volatile transient long baseCount;
    private volatile transient int cellsBusy;
    private volatile transient CounterCell[] counterCells;
    private transient EntrySetView<K, V> entrySet;
    private transient KeySetView<K, V> keySet;
    private volatile transient Node<K, V>[] nextTable;
    private volatile transient int sizeCtl;
    volatile transient Node<K, V>[] table;
    private volatile transient int transferIndex;
    private transient ValuesView<K, V> values;

    static final int spread(int i) {
        return (i ^ (i >>> 16)) & Integer.MAX_VALUE;
    }

    static {
        Unsafe unsafe = Unsafe.getUnsafe();
        f735U = unsafe;
        Class<ConcurrentHashMap> cls = ConcurrentHashMap.class;
        SIZECTL = unsafe.objectFieldOffset(cls, "sizeCtl");
        TRANSFERINDEX = unsafe.objectFieldOffset(cls, "transferIndex");
        BASECOUNT = unsafe.objectFieldOffset(cls, "baseCount");
        CELLSBUSY = unsafe.objectFieldOffset(cls, "cellsBusy");
        CELLVALUE = unsafe.objectFieldOffset(CounterCell.class, "value");
        ABASE = unsafe.arrayBaseOffset(Node[].class);
        int arrayIndexScale = unsafe.arrayIndexScale(Node[].class);
        if (((arrayIndexScale - 1) & arrayIndexScale) == 0) {
            ASHIFT = 31 - Integer.numberOfLeadingZeros(arrayIndexScale);
            Class<LockSupport> cls2 = LockSupport.class;
            Class<ReservationNode> cls3 = ReservationNode.class;
            return;
        }
        throw new ExceptionInInitializerError("array index scale not a power of two");
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        volatile Node<K, V> next;
        volatile V val;

        Node(int i, K k, V v) {
            this.hash = i;
            this.key = k;
            this.val = v;
        }

        Node(int i, K k, V v, Node<K, V> node) {
            this(i, k, v);
            this.next = node;
        }

        public final K getKey() {
            return this.key;
        }

        public final V getValue() {
            return this.val;
        }

        public final int hashCode() {
            return this.val.hashCode() ^ this.key.hashCode();
        }

        public final String toString() {
            return Helpers.mapEntryToString(this.key, this.val);
        }

        public final V setValue(V v) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
            r2 = r2.val;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r3 = (java.util.Map.Entry) r3;
            r0 = r3.getKey();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
            r3 = r3.getValue();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0012, code lost:
            r1 = r2.key;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean equals(java.lang.Object r3) {
            /*
                r2 = this;
                boolean r0 = r3 instanceof java.util.Map.Entry
                if (r0 == 0) goto L_0x0028
                java.util.Map$Entry r3 = (java.util.Map.Entry) r3
                java.lang.Object r0 = r3.getKey()
                if (r0 == 0) goto L_0x0028
                java.lang.Object r3 = r3.getValue()
                if (r3 == 0) goto L_0x0028
                K r1 = r2.key
                if (r0 == r1) goto L_0x001c
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L_0x0028
            L_0x001c:
                V r2 = r2.val
                if (r3 == r2) goto L_0x0026
                boolean r2 = r3.equals(r2)
                if (r2 == 0) goto L_0x0028
            L_0x0026:
                r2 = 1
                goto L_0x0029
            L_0x0028:
                r2 = 0
            L_0x0029:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.Node.equals(java.lang.Object):boolean");
        }

        /* access modifiers changed from: package-private */
        public Node<K, V> find(int i, Object obj) {
            K k;
            if (obj == null) {
                return null;
            }
            do {
                if (this.hash == i && ((k = this.key) == obj || (k != null && obj.equals(k)))) {
                    return this;
                }
                this = this.next;
            } while (this != null);
            return null;
        }
    }

    private static final int tableSizeFor(int i) {
        int numberOfLeadingZeros = -1 >>> Integer.numberOfLeadingZeros(i - 1);
        if (numberOfLeadingZeros < 0) {
            return 1;
        }
        if (numberOfLeadingZeros >= 1073741824) {
            return 1073741824;
        }
        return 1 + numberOfLeadingZeros;
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

    static final <K, V> Node<K, V> tabAt(Node<K, V>[] nodeArr, int i) {
        return (Node) f735U.getObjectAcquire(nodeArr, (((long) i) << ASHIFT) + ((long) ABASE));
    }

    static final <K, V> boolean casTabAt(Node<K, V>[] nodeArr, int i, Node<K, V> node, Node<K, V> node2) {
        return f735U.compareAndSetObject(nodeArr, (((long) i) << ASHIFT) + ((long) ABASE), node, node2);
    }

    static final <K, V> void setTabAt(Node<K, V>[] nodeArr, int i, Node<K, V> node) {
        f735U.putObjectRelease(nodeArr, (((long) i) << ASHIFT) + ((long) ABASE), node);
    }

    public ConcurrentHashMap() {
    }

    public ConcurrentHashMap(int i) {
        this(i, 0.75f, 1);
    }

    public ConcurrentHashMap(Map<? extends K, ? extends V> map) {
        this.sizeCtl = 16;
        putAll(map);
    }

    public ConcurrentHashMap(int i, float f) {
        this(i, f, 1);
    }

    public ConcurrentHashMap(int i, float f, int i2) {
        int i3;
        if (f <= 0.0f || i < 0 || i2 <= 0) {
            throw new IllegalArgumentException();
        }
        long j = (long) (((double) (((float) ((long) (i < i2 ? i2 : i))) / f)) + 1.0d);
        if (j >= 1073741824) {
            i3 = 1073741824;
        } else {
            i3 = tableSizeFor((int) j);
        }
        this.sizeCtl = i3;
    }

    public int size() {
        long sumCount = sumCount();
        if (sumCount < 0) {
            return 0;
        }
        if (sumCount > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) sumCount;
    }

    public boolean isEmpty() {
        return sumCount() <= 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004d, code lost:
        return r3.val;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public V get(java.lang.Object r4) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            int r0 = spread(r0)
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r3 = r3.table
            r1 = 0
            if (r3 == 0) goto L_0x004e
            int r2 = r3.length
            if (r2 <= 0) goto L_0x004e
            int r2 = r2 + -1
            r2 = r2 & r0
            java.util.concurrent.ConcurrentHashMap$Node r3 = tabAt(r3, r2)
            if (r3 == 0) goto L_0x004e
            int r2 = r3.hash
            if (r2 != r0) goto L_0x002c
            K r2 = r3.key
            if (r2 == r4) goto L_0x0029
            if (r2 == 0) goto L_0x0037
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L_0x0037
        L_0x0029:
            V r3 = r3.val
            return r3
        L_0x002c:
            if (r2 >= 0) goto L_0x0037
            java.util.concurrent.ConcurrentHashMap$Node r3 = r3.find(r0, r4)
            if (r3 == 0) goto L_0x0036
            V r1 = r3.val
        L_0x0036:
            return r1
        L_0x0037:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r3 = r3.next
            if (r3 == 0) goto L_0x004e
            int r2 = r3.hash
            if (r2 != r0) goto L_0x0037
            K r2 = r3.key
            if (r2 == r4) goto L_0x004b
            if (r2 == 0) goto L_0x0037
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L_0x0037
        L_0x004b:
            V r3 = r3.val
            return r3
        L_0x004e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.get(java.lang.Object):java.lang.Object");
    }

    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    public boolean containsValue(Object obj) {
        obj.getClass();
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                V v = advance.val;
                if (v == obj) {
                    return true;
                }
                if (v != null && obj.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    public V put(K k, V v) {
        return putVal(k, v, false);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00b1, code lost:
        addCount(1, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00b6, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final V putVal(K r9, V r10, boolean r11) {
        /*
            r8 = this;
            r0 = 0
            if (r9 == 0) goto L_0x00c0
            if (r10 == 0) goto L_0x00c0
            int r1 = r9.hashCode()
            int r1 = spread(r1)
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r2 = r8.table
            r3 = 0
        L_0x0010:
            if (r2 == 0) goto L_0x00ba
            int r4 = r2.length
            if (r4 != 0) goto L_0x0017
            goto L_0x00ba
        L_0x0017:
            int r4 = r4 + -1
            r4 = r4 & r1
            java.util.concurrent.ConcurrentHashMap$Node r5 = tabAt(r2, r4)
            if (r5 != 0) goto L_0x002d
            java.util.concurrent.ConcurrentHashMap$Node r5 = new java.util.concurrent.ConcurrentHashMap$Node
            r5.<init>(r1, r9, r10)
            boolean r4 = casTabAt(r2, r4, r0, r5)
            if (r4 == 0) goto L_0x0010
            goto L_0x00b1
        L_0x002d:
            int r6 = r5.hash
            r7 = -1
            if (r6 != r7) goto L_0x0037
            java.util.concurrent.ConcurrentHashMap$Node[] r2 = r8.helpTransfer(r2, r5)
            goto L_0x0010
        L_0x0037:
            if (r11 == 0) goto L_0x004c
            if (r6 != r1) goto L_0x004c
            K r7 = r5.key
            if (r7 == r9) goto L_0x0047
            if (r7 == 0) goto L_0x004c
            boolean r7 = r9.equals(r7)
            if (r7 == 0) goto L_0x004c
        L_0x0047:
            V r7 = r5.val
            if (r7 == 0) goto L_0x004c
            return r7
        L_0x004c:
            monitor-enter(r5)
            java.util.concurrent.ConcurrentHashMap$Node r7 = tabAt(r2, r4)     // Catch:{ all -> 0x00b7 }
            if (r7 != r5) goto L_0x00a3
            if (r6 < 0) goto L_0x007e
            r3 = 1
            r6 = r5
        L_0x0057:
            int r7 = r6.hash     // Catch:{ all -> 0x00b7 }
            if (r7 != r1) goto L_0x006e
            K r7 = r6.key     // Catch:{ all -> 0x00b7 }
            if (r7 == r9) goto L_0x0067
            if (r7 == 0) goto L_0x006e
            boolean r7 = r9.equals(r7)     // Catch:{ all -> 0x00b7 }
            if (r7 == 0) goto L_0x006e
        L_0x0067:
            V r7 = r6.val     // Catch:{ all -> 0x00b7 }
            if (r11 != 0) goto L_0x00a4
            r6.val = r10     // Catch:{ all -> 0x00b7 }
            goto L_0x00a4
        L_0x006e:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r7 = r6.next     // Catch:{ all -> 0x00b7 }
            if (r7 != 0) goto L_0x007a
            java.util.concurrent.ConcurrentHashMap$Node r7 = new java.util.concurrent.ConcurrentHashMap$Node     // Catch:{ all -> 0x00b7 }
            r7.<init>(r1, r9, r10)     // Catch:{ all -> 0x00b7 }
            r6.next = r7     // Catch:{ all -> 0x00b7 }
            goto L_0x00a3
        L_0x007a:
            int r3 = r3 + 1
            r6 = r7
            goto L_0x0057
        L_0x007e:
            boolean r6 = r5 instanceof java.util.concurrent.ConcurrentHashMap.TreeBin     // Catch:{ all -> 0x00b7 }
            if (r6 == 0) goto L_0x0096
            r3 = r5
            java.util.concurrent.ConcurrentHashMap$TreeBin r3 = (java.util.concurrent.ConcurrentHashMap.TreeBin) r3     // Catch:{ all -> 0x00b7 }
            java.util.concurrent.ConcurrentHashMap$TreeNode r3 = r3.putTreeVal(r1, r9, r10)     // Catch:{ all -> 0x00b7 }
            if (r3 == 0) goto L_0x0093
            V r6 = r3.val     // Catch:{ all -> 0x00b7 }
            if (r11 != 0) goto L_0x0091
            r3.val = r10     // Catch:{ all -> 0x00b7 }
        L_0x0091:
            r7 = r6
            goto L_0x0094
        L_0x0093:
            r7 = r0
        L_0x0094:
            r3 = 2
            goto L_0x00a4
        L_0x0096:
            boolean r6 = r5 instanceof java.util.concurrent.ConcurrentHashMap.ReservationNode     // Catch:{ all -> 0x00b7 }
            if (r6 != 0) goto L_0x009b
            goto L_0x00a3
        L_0x009b:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00b7 }
            java.lang.String r9 = "Recursive update"
            r8.<init>((java.lang.String) r9)     // Catch:{ all -> 0x00b7 }
            throw r8     // Catch:{ all -> 0x00b7 }
        L_0x00a3:
            r7 = r0
        L_0x00a4:
            monitor-exit(r5)     // Catch:{ all -> 0x00b7 }
            if (r3 == 0) goto L_0x0010
            r9 = 8
            if (r3 < r9) goto L_0x00ae
            r8.treeifyBin(r2, r4)
        L_0x00ae:
            if (r7 == 0) goto L_0x00b1
            return r7
        L_0x00b1:
            r9 = 1
            r8.addCount(r9, r3)
            return r0
        L_0x00b7:
            r8 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x00b7 }
            throw r8
        L_0x00ba:
            java.util.concurrent.ConcurrentHashMap$Node[] r2 = r8.initTable()
            goto L_0x0010
        L_0x00c0:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.putVal(java.lang.Object, java.lang.Object, boolean):java.lang.Object");
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        tryPresize(map.size());
        for (Map.Entry next : map.entrySet()) {
            putVal(next.getKey(), next.getValue(), false);
        }
    }

    public V remove(Object obj) {
        return replaceNode(obj, (Object) null, (Object) null);
    }

    /* access modifiers changed from: package-private */
    public final V replaceNode(Object obj, V v, Object obj2) {
        int length;
        int i;
        Node<K, V> tabAt;
        V v2;
        boolean z;
        TreeNode<K, V> findTreeNode;
        K k;
        int spread = spread(obj.hashCode());
        Node<K, V>[] nodeArr = this.table;
        while (true) {
            if (nodeArr == null || (length = nodeArr.length) == 0 || (tabAt = tabAt(nodeArr, i)) == null) {
                break;
            }
            int i2 = tabAt.hash;
            if (i2 == -1) {
                nodeArr = helpTransfer(nodeArr, tabAt);
            } else {
                synchronized (tabAt) {
                    if (tabAt(nodeArr, (i = (length - 1) & spread)) == tabAt) {
                        z = true;
                        if (i2 >= 0) {
                            Node<K, V> node = null;
                            Node<K, V> node2 = tabAt;
                            while (true) {
                                if (node2.hash != spread || ((k = node2.key) != obj && (k == null || !obj.equals(k)))) {
                                    Node<K, V> node3 = node2.next;
                                    if (node3 == null) {
                                        break;
                                    }
                                    Node<K, V> node4 = node3;
                                    node = node2;
                                    node2 = node4;
                                }
                            }
                            v2 = node2.val;
                            if (obj2 == null || obj2 == v2 || (v2 != null && obj2.equals(v2))) {
                                if (v != null) {
                                    node2.val = v;
                                } else if (node != null) {
                                    node.next = node2.next;
                                } else {
                                    setTabAt(nodeArr, i, node2.next);
                                }
                            }
                            v2 = null;
                        } else if (tabAt instanceof TreeBin) {
                            TreeBin treeBin = (TreeBin) tabAt;
                            TreeNode<K, V> treeNode = treeBin.root;
                            if (treeNode != null && (findTreeNode = treeNode.findTreeNode(spread, obj, (Class<?>) null)) != null) {
                                v2 = findTreeNode.val;
                                if (obj2 == null || obj2 == v2 || (v2 != null && obj2.equals(v2))) {
                                    if (v != null) {
                                        findTreeNode.val = v;
                                    } else if (treeBin.removeTreeNode(findTreeNode)) {
                                        setTabAt(nodeArr, i, untreeify(treeBin.first));
                                    }
                                }
                            }
                            v2 = null;
                        } else if (tabAt instanceof ReservationNode) {
                            throw new IllegalStateException("Recursive update");
                        }
                    }
                    z = false;
                    v2 = null;
                }
                if (z) {
                    if (v2 != null) {
                        if (v == null) {
                            addCount(-1, -1);
                        }
                        return v2;
                    }
                }
            }
        }
        return null;
    }

    public void clear() {
        Node<K, V> node;
        Node<K, V>[] nodeArr = this.table;
        long j = 0;
        loop0:
        while (true) {
            int i = 0;
            while (nodeArr != null && i < nodeArr.length) {
                Node<K, V> tabAt = tabAt(nodeArr, i);
                if (tabAt == null) {
                    i++;
                } else {
                    int i2 = tabAt.hash;
                    if (i2 == -1) {
                        nodeArr = helpTransfer(nodeArr, tabAt);
                    } else {
                        synchronized (tabAt) {
                            if (tabAt(nodeArr, i) == tabAt) {
                                if (i2 >= 0) {
                                    node = tabAt;
                                } else {
                                    node = tabAt instanceof TreeBin ? ((TreeBin) tabAt).first : null;
                                }
                                while (node != null) {
                                    j--;
                                    node = node.next;
                                }
                                setTabAt(nodeArr, i, (Node) null);
                                i++;
                            }
                        }
                    }
                }
            }
        }
        if (j != 0) {
            addCount(j, -1);
        }
    }

    public Set<K> keySet() {
        KeySetView<K, V> keySetView = this.keySet;
        if (keySetView != null) {
            return keySetView;
        }
        KeySetView<K, V> keySetView2 = new KeySetView<>(this, null);
        this.keySet = keySetView2;
        return keySetView2;
    }

    public Collection<V> values() {
        ValuesView<K, V> valuesView = this.values;
        if (valuesView != null) {
            return valuesView;
        }
        ValuesView<K, V> valuesView2 = new ValuesView<>(this);
        this.values = valuesView2;
        return valuesView2;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        EntrySetView<K, V> entrySetView = this.entrySet;
        if (entrySetView != null) {
            return entrySetView;
        }
        EntrySetView<K, V> entrySetView2 = new EntrySetView<>(this);
        this.entrySet = entrySetView2;
        return entrySetView2;
    }

    public int hashCode() {
        Node<K, V>[] nodeArr = this.table;
        int i = 0;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                i += advance.val.hashCode() ^ advance.key.hashCode();
            }
        }
        return i;
    }

    public String toString() {
        Node<K, V>[] nodeArr = this.table;
        int length = nodeArr == null ? 0 : nodeArr.length;
        Traverser traverser = new Traverser(nodeArr, length, 0, length);
        StringBuilder sb = new StringBuilder("{");
        Node advance = traverser.advance();
        if (advance != null) {
            while (true) {
                K k = advance.key;
                V v = advance.val;
                if (k == this) {
                    k = "(this Map)";
                }
                sb.append((Object) k);
                sb.append('=');
                if (v == this) {
                    v = "(this Map)";
                }
                sb.append((Object) v);
                advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object obj) {
        Object value;
        Object obj2;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        Node<K, V>[] nodeArr = this.table;
        int length = nodeArr == null ? 0 : nodeArr.length;
        Traverser traverser = new Traverser(nodeArr, length, 0, length);
        while (true) {
            Node advance = traverser.advance();
            if (advance != null) {
                V v = advance.val;
                V v2 = map.get(advance.key);
                if (v2 == null || (v2 != v && !v2.equals(v))) {
                    return false;
                }
            } else {
                for (Map.Entry entry : map.entrySet()) {
                    Object key = entry.getKey();
                    if (key == null || (value = entry.getValue()) == null || (obj2 = get(key)) == null || (value != obj2 && !value.equals(obj2))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    static class Segment<K, V> extends ReentrantLock implements Serializable {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;

        Segment(float f) {
            this.loadFactor = f;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int i = 0;
        int i2 = 1;
        while (i2 < 16) {
            i++;
            i2 <<= 1;
        }
        int i3 = 32 - i;
        int i4 = i2 - 1;
        Segment[] segmentArr = new Segment[16];
        for (int i5 = 0; i5 < 16; i5++) {
            segmentArr[i5] = new Segment(0.75f);
        }
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("segments", (Object) segmentArr);
        putFields.put("segmentShift", i3);
        putFields.put("segmentMask", i4);
        objectOutputStream.writeFields();
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                objectOutputStream.writeObject(advance.key);
                objectOutputStream.writeObject(advance.val);
            }
        }
        objectOutputStream.writeObject((Object) null);
        objectOutputStream.writeObject((Object) null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        long j;
        int i;
        boolean z;
        boolean z2;
        K k;
        this.sizeCtl = -1;
        objectInputStream.defaultReadObject();
        long j2 = 0;
        long j3 = 0;
        Node<K, V> node = null;
        while (true) {
            Object readObject = objectInputStream.readObject();
            Object readObject2 = objectInputStream.readObject();
            j = 1;
            if (readObject != null && readObject2 != null) {
                j3++;
                node = new Node<>(spread(readObject.hashCode()), readObject, readObject2, node);
            }
        }
        if (j3 == 0) {
            this.sizeCtl = 0;
            return;
        }
        long j4 = (long) (((double) (((float) j3) / 0.75f)) + 1.0d);
        if (j4 >= 1073741824) {
            i = 1073741824;
        } else {
            i = tableSizeFor((int) j4);
        }
        Node<K, V>[] nodeArr = new Node[i];
        int i2 = i - 1;
        while (node != null) {
            Node<K, V> node2 = node.next;
            int i3 = node.hash;
            int i4 = i3 & i2;
            Node<K, V> tabAt = tabAt(nodeArr, i4);
            if (tabAt == null) {
                z = true;
            } else {
                K k2 = node.key;
                if (tabAt.hash >= 0) {
                    Node<K, V> node3 = tabAt;
                    int i5 = 0;
                    while (true) {
                        if (node3 == null) {
                            z2 = true;
                            break;
                        } else if (node3.hash != i3 || ((k = node3.key) != k2 && (k == null || !k2.equals(k)))) {
                            i5++;
                            node3 = node3.next;
                        }
                    }
                    z2 = false;
                    if (!z2 || i5 < 8) {
                        z = z2;
                    } else {
                        long j5 = j2 + 1;
                        node.next = tabAt;
                        Node<K, V> node4 = node;
                        TreeNode<K, V> treeNode = null;
                        TreeNode<K, V> treeNode2 = null;
                        while (node4 != null) {
                            long j6 = j5;
                            TreeNode<K, V> treeNode3 = new TreeNode<>(node4.hash, node4.key, node4.val, (Node) null, (TreeNode) null);
                            treeNode3.prev = treeNode2;
                            if (treeNode2 == null) {
                                treeNode = treeNode3;
                            } else {
                                treeNode2.next = treeNode3;
                            }
                            node4 = node4.next;
                            treeNode2 = treeNode3;
                            j5 = j6;
                        }
                        setTabAt(nodeArr, i4, new TreeBin(treeNode));
                        j2 = j5;
                    }
                } else if (((TreeBin) tabAt).putTreeVal(i3, k2, node.val) == null) {
                    j2 += j;
                }
                z = false;
            }
            if (z) {
                j2++;
                node.next = tabAt;
                setTabAt(nodeArr, i4, node);
            }
            j = 1;
            node = node2;
        }
        this.table = nodeArr;
        this.sizeCtl = i - (i >>> 2);
        this.baseCount = j2;
    }

    public V putIfAbsent(K k, V v) {
        return putVal(k, v, true);
    }

    public boolean remove(Object obj, Object obj2) {
        obj.getClass();
        return (obj2 == null || replaceNode(obj, (Object) null, obj2) == null) ? false : true;
    }

    public boolean replace(K k, V v, V v2) {
        if (k != null && v != null && v2 != null) {
            return replaceNode(k, v2, v) != null;
        }
        throw null;
    }

    public V replace(K k, V v) {
        if (k != null && v != null) {
            return replaceNode(k, v, (Object) null);
        }
        throw null;
    }

    public V getOrDefault(Object obj, V v) {
        V v2 = get(obj);
        return v2 == null ? v : v2;
    }

    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        biConsumer.getClass();
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node advance = traverser.advance();
                if (advance != null) {
                    biConsumer.accept(advance.key, advance.val);
                } else {
                    return;
                }
            }
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        biFunction.getClass();
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node advance = traverser.advance();
                if (advance != null) {
                    V v = advance.val;
                    K k = advance.key;
                    do {
                        Object apply = biFunction.apply(k, v);
                        apply.getClass();
                        if (replaceNode(k, apply, v) != null || (v = get(k)) == null) {
                        }
                        Object apply2 = biFunction.apply(k, v);
                        apply2.getClass();
                        break;
                    } while ((v = get(k)) == null);
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean removeEntryIf(Predicate<? super Map.Entry<K, V>> predicate) {
        predicate.getClass();
        Node<K, V>[] nodeArr = this.table;
        boolean z = false;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                K k = advance.key;
                V v = advance.val;
                if (predicate.test(new AbstractMap.SimpleImmutableEntry(k, v)) && replaceNode(k, (Object) null, v) != null) {
                    z = true;
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public boolean removeValueIf(Predicate<? super V> predicate) {
        predicate.getClass();
        Node<K, V>[] nodeArr = this.table;
        boolean z = false;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                K k = advance.key;
                V v = advance.val;
                if (predicate.test(v) && replaceNode(k, (Object) null, v) != null) {
                    z = true;
                }
            }
        }
        return z;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x00f1, code lost:
        if (r5 == null) goto L_0x00f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x00f3, code lost:
        addCount(1, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x00f8, code lost:
        return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public V computeIfAbsent(K r13, java.util.function.Function<? super K, ? extends V> r14) {
        /*
            r12 = this;
            r0 = 0
            if (r13 == 0) goto L_0x0102
            if (r14 == 0) goto L_0x0102
            int r1 = r13.hashCode()
            int r1 = spread(r1)
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r2 = r12.table
            r3 = 0
            r5 = r0
            r4 = r3
        L_0x0012:
            if (r2 == 0) goto L_0x00fc
            int r6 = r2.length
            if (r6 != 0) goto L_0x0019
            goto L_0x00fc
        L_0x0019:
            int r6 = r6 + -1
            r6 = r6 & r1
            java.util.concurrent.ConcurrentHashMap$Node r7 = tabAt(r2, r6)
            r8 = 1
            if (r7 != 0) goto L_0x004f
            java.util.concurrent.ConcurrentHashMap$ReservationNode r9 = new java.util.concurrent.ConcurrentHashMap$ReservationNode
            r9.<init>()
            monitor-enter(r9)
            boolean r7 = casTabAt(r2, r6, r0, r9)     // Catch:{ all -> 0x004c }
            if (r7 == 0) goto L_0x0047
            java.lang.Object r4 = r14.apply(r13)     // Catch:{ all -> 0x0042 }
            if (r4 == 0) goto L_0x003b
            java.util.concurrent.ConcurrentHashMap$Node r5 = new java.util.concurrent.ConcurrentHashMap$Node     // Catch:{ all -> 0x0042 }
            r5.<init>(r1, r13, r4)     // Catch:{ all -> 0x0042 }
            goto L_0x003c
        L_0x003b:
            r5 = r0
        L_0x003c:
            setTabAt(r2, r6, r5)     // Catch:{ all -> 0x004c }
            r5 = r4
            r4 = r8
            goto L_0x0047
        L_0x0042:
            r12 = move-exception
            setTabAt(r2, r6, r0)     // Catch:{ all -> 0x004c }
            throw r12     // Catch:{ all -> 0x004c }
        L_0x0047:
            monitor-exit(r9)     // Catch:{ all -> 0x004c }
            if (r4 == 0) goto L_0x0012
            goto L_0x00f1
        L_0x004c:
            r12 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x004c }
            throw r12
        L_0x004f:
            int r9 = r7.hash
            r10 = -1
            if (r9 != r10) goto L_0x0059
            java.util.concurrent.ConcurrentHashMap$Node[] r2 = r12.helpTransfer(r2, r7)
            goto L_0x0012
        L_0x0059:
            if (r9 != r1) goto L_0x006c
            K r10 = r7.key
            if (r10 == r13) goto L_0x0067
            if (r10 == 0) goto L_0x006c
            boolean r10 = r13.equals(r10)
            if (r10 == 0) goto L_0x006c
        L_0x0067:
            V r10 = r7.val
            if (r10 == 0) goto L_0x006c
            return r10
        L_0x006c:
            monitor-enter(r7)
            java.util.concurrent.ConcurrentHashMap$Node r10 = tabAt(r2, r6)     // Catch:{ all -> 0x00f9 }
            if (r10 != r7) goto L_0x00e3
            if (r9 < 0) goto L_0x00b0
            r4 = r7
            r5 = r8
        L_0x0077:
            int r9 = r4.hash     // Catch:{ all -> 0x00f9 }
            if (r9 != r1) goto L_0x008b
            K r9 = r4.key     // Catch:{ all -> 0x00f9 }
            if (r9 == r13) goto L_0x0087
            if (r9 == 0) goto L_0x008b
            boolean r9 = r13.equals(r9)     // Catch:{ all -> 0x00f9 }
            if (r9 == 0) goto L_0x008b
        L_0x0087:
            V r4 = r4.val     // Catch:{ all -> 0x00f9 }
            r8 = r3
            goto L_0x00d2
        L_0x008b:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r9 = r4.next     // Catch:{ all -> 0x00f9 }
            if (r9 != 0) goto L_0x00ac
            java.lang.Object r9 = r14.apply(r13)     // Catch:{ all -> 0x00f9 }
            if (r9 == 0) goto L_0x00a9
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r10 = r4.next     // Catch:{ all -> 0x00f9 }
            if (r10 != 0) goto L_0x00a1
            java.util.concurrent.ConcurrentHashMap$Node r10 = new java.util.concurrent.ConcurrentHashMap$Node     // Catch:{ all -> 0x00f9 }
            r10.<init>(r1, r13, r9)     // Catch:{ all -> 0x00f9 }
            r4.next = r10     // Catch:{ all -> 0x00f9 }
            goto L_0x00aa
        L_0x00a1:
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00f9 }
            java.lang.String r13 = "Recursive update"
            r12.<init>((java.lang.String) r13)     // Catch:{ all -> 0x00f9 }
            throw r12     // Catch:{ all -> 0x00f9 }
        L_0x00a9:
            r8 = r3
        L_0x00aa:
            r4 = r9
            goto L_0x00d2
        L_0x00ac:
            int r5 = r5 + 1
            r4 = r9
            goto L_0x0077
        L_0x00b0:
            boolean r9 = r7 instanceof java.util.concurrent.ConcurrentHashMap.TreeBin     // Catch:{ all -> 0x00f9 }
            if (r9 == 0) goto L_0x00d6
            r4 = r7
            java.util.concurrent.ConcurrentHashMap$TreeBin r4 = (java.util.concurrent.ConcurrentHashMap.TreeBin) r4     // Catch:{ all -> 0x00f9 }
            java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r5 = r4.root     // Catch:{ all -> 0x00f9 }
            if (r5 == 0) goto L_0x00c5
            java.util.concurrent.ConcurrentHashMap$TreeNode r5 = r5.findTreeNode(r1, r13, r0)     // Catch:{ all -> 0x00f9 }
            if (r5 == 0) goto L_0x00c5
            java.lang.Object r4 = r5.val     // Catch:{ all -> 0x00f9 }
            r8 = r3
            goto L_0x00d1
        L_0x00c5:
            java.lang.Object r5 = r14.apply(r13)     // Catch:{ all -> 0x00f9 }
            if (r5 == 0) goto L_0x00cf
            r4.putTreeVal(r1, r13, r5)     // Catch:{ all -> 0x00f9 }
            goto L_0x00d0
        L_0x00cf:
            r8 = r3
        L_0x00d0:
            r4 = r5
        L_0x00d1:
            r5 = 2
        L_0x00d2:
            r11 = r5
            r5 = r4
            r4 = r11
            goto L_0x00e4
        L_0x00d6:
            boolean r8 = r7 instanceof java.util.concurrent.ConcurrentHashMap.ReservationNode     // Catch:{ all -> 0x00f9 }
            if (r8 != 0) goto L_0x00db
            goto L_0x00e3
        L_0x00db:
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00f9 }
            java.lang.String r13 = "Recursive update"
            r12.<init>((java.lang.String) r13)     // Catch:{ all -> 0x00f9 }
            throw r12     // Catch:{ all -> 0x00f9 }
        L_0x00e3:
            r8 = r3
        L_0x00e4:
            monitor-exit(r7)     // Catch:{ all -> 0x00f9 }
            if (r4 == 0) goto L_0x0012
            r13 = 8
            if (r4 < r13) goto L_0x00ee
            r12.treeifyBin(r2, r6)
        L_0x00ee:
            if (r8 != 0) goto L_0x00f1
            return r5
        L_0x00f1:
            if (r5 == 0) goto L_0x00f8
            r13 = 1
            r12.addCount(r13, r4)
        L_0x00f8:
            return r5
        L_0x00f9:
            r12 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x00f9 }
            throw r12
        L_0x00fc:
            java.util.concurrent.ConcurrentHashMap$Node[] r2 = r12.initTable()
            goto L_0x0012
        L_0x0102:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.computeIfAbsent(java.lang.Object, java.util.function.Function):java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00aa, code lost:
        if (r3 == 0) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00ac, code lost:
        addCount((long) r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00b0, code lost:
        return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public V computeIfPresent(K r14, java.util.function.BiFunction<? super K, ? super V, ? extends V> r15) {
        /*
            r13 = this;
            r0 = 0
            if (r14 == 0) goto L_0x00ba
            if (r15 == 0) goto L_0x00ba
            int r1 = r14.hashCode()
            int r1 = spread(r1)
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r2 = r13.table
            r3 = 0
            r5 = r0
            r4 = r3
        L_0x0012:
            if (r2 == 0) goto L_0x00b4
            int r6 = r2.length
            if (r6 != 0) goto L_0x0019
            goto L_0x00b4
        L_0x0019:
            int r6 = r6 + -1
            r6 = r6 & r1
            java.util.concurrent.ConcurrentHashMap$Node r7 = tabAt(r2, r6)
            if (r7 != 0) goto L_0x0024
            goto L_0x00aa
        L_0x0024:
            int r8 = r7.hash
            r9 = -1
            if (r8 != r9) goto L_0x002e
            java.util.concurrent.ConcurrentHashMap$Node[] r2 = r13.helpTransfer(r2, r7)
            goto L_0x0012
        L_0x002e:
            monitor-enter(r7)
            java.util.concurrent.ConcurrentHashMap$Node r10 = tabAt(r2, r6)     // Catch:{ all -> 0x00b1 }
            if (r10 != r7) goto L_0x00a7
            if (r8 < 0) goto L_0x006c
            r4 = 1
            r10 = r0
            r8 = r7
        L_0x003a:
            int r11 = r8.hash     // Catch:{ all -> 0x00b1 }
            if (r11 != r1) goto L_0x0061
            K r11 = r8.key     // Catch:{ all -> 0x00b1 }
            if (r11 == r14) goto L_0x004a
            if (r11 == 0) goto L_0x0061
            boolean r11 = r14.equals(r11)     // Catch:{ all -> 0x00b1 }
            if (r11 == 0) goto L_0x0061
        L_0x004a:
            V r5 = r8.val     // Catch:{ all -> 0x00b1 }
            java.lang.Object r5 = r15.apply(r14, r5)     // Catch:{ all -> 0x00b1 }
            if (r5 == 0) goto L_0x0055
            r8.val = r5     // Catch:{ all -> 0x00b1 }
            goto L_0x00a7
        L_0x0055:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r3 = r8.next     // Catch:{ all -> 0x00b1 }
            if (r10 == 0) goto L_0x005c
            r10.next = r3     // Catch:{ all -> 0x00b1 }
            goto L_0x005f
        L_0x005c:
            setTabAt(r2, r6, r3)     // Catch:{ all -> 0x00b1 }
        L_0x005f:
            r3 = r9
            goto L_0x00a7
        L_0x0061:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r10 = r8.next     // Catch:{ all -> 0x00b1 }
            if (r10 != 0) goto L_0x0066
            goto L_0x00a7
        L_0x0066:
            int r4 = r4 + 1
            r12 = r10
            r10 = r8
            r8 = r12
            goto L_0x003a
        L_0x006c:
            boolean r8 = r7 instanceof java.util.concurrent.ConcurrentHashMap.TreeBin     // Catch:{ all -> 0x00b1 }
            if (r8 == 0) goto L_0x009a
            r4 = r7
            java.util.concurrent.ConcurrentHashMap$TreeBin r4 = (java.util.concurrent.ConcurrentHashMap.TreeBin) r4     // Catch:{ all -> 0x00b1 }
            java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r8 = r4.root     // Catch:{ all -> 0x00b1 }
            if (r8 == 0) goto L_0x0098
            java.util.concurrent.ConcurrentHashMap$TreeNode r8 = r8.findTreeNode(r1, r14, r0)     // Catch:{ all -> 0x00b1 }
            if (r8 == 0) goto L_0x0098
            java.lang.Object r5 = r8.val     // Catch:{ all -> 0x00b1 }
            java.lang.Object r5 = r15.apply(r14, r5)     // Catch:{ all -> 0x00b1 }
            if (r5 == 0) goto L_0x0088
            r8.val = r5     // Catch:{ all -> 0x00b1 }
            goto L_0x0098
        L_0x0088:
            boolean r3 = r4.removeTreeNode(r8)     // Catch:{ all -> 0x00b1 }
            if (r3 == 0) goto L_0x0097
            java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r3 = r4.first     // Catch:{ all -> 0x00b1 }
            java.util.concurrent.ConcurrentHashMap$Node r3 = untreeify(r3)     // Catch:{ all -> 0x00b1 }
            setTabAt(r2, r6, r3)     // Catch:{ all -> 0x00b1 }
        L_0x0097:
            r3 = r9
        L_0x0098:
            r4 = 2
            goto L_0x00a7
        L_0x009a:
            boolean r6 = r7 instanceof java.util.concurrent.ConcurrentHashMap.ReservationNode     // Catch:{ all -> 0x00b1 }
            if (r6 != 0) goto L_0x009f
            goto L_0x00a7
        L_0x009f:
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00b1 }
            java.lang.String r14 = "Recursive update"
            r13.<init>((java.lang.String) r14)     // Catch:{ all -> 0x00b1 }
            throw r13     // Catch:{ all -> 0x00b1 }
        L_0x00a7:
            monitor-exit(r7)     // Catch:{ all -> 0x00b1 }
            if (r4 == 0) goto L_0x0012
        L_0x00aa:
            if (r3 == 0) goto L_0x00b0
            long r14 = (long) r3
            r13.addCount(r14, r4)
        L_0x00b0:
            return r5
        L_0x00b1:
            r13 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x00b1 }
            throw r13
        L_0x00b4:
            java.util.concurrent.ConcurrentHashMap$Node[] r2 = r13.initTable()
            goto L_0x0012
        L_0x00ba:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.computeIfPresent(java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x010a, code lost:
        if (r4 == 0) goto L_0x0110;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x010c, code lost:
        addCount((long) r4, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0110, code lost:
        return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public V compute(K r14, java.util.function.BiFunction<? super K, ? super V, ? extends V> r15) {
        /*
            r13 = this;
            r0 = 0
            if (r14 == 0) goto L_0x011a
            if (r15 == 0) goto L_0x011a
            int r1 = r14.hashCode()
            int r1 = spread(r1)
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r2 = r13.table
            r3 = 0
            r5 = r0
            r4 = r3
        L_0x0012:
            if (r2 == 0) goto L_0x0114
            int r6 = r2.length
            if (r6 != 0) goto L_0x0019
            goto L_0x0114
        L_0x0019:
            int r6 = r6 + -1
            r6 = r6 & r1
            java.util.concurrent.ConcurrentHashMap$Node r7 = tabAt(r2, r6)
            r8 = 1
            if (r7 != 0) goto L_0x0052
            java.util.concurrent.ConcurrentHashMap$ReservationNode r9 = new java.util.concurrent.ConcurrentHashMap$ReservationNode
            r9.<init>()
            monitor-enter(r9)
            boolean r7 = casTabAt(r2, r6, r0, r9)     // Catch:{ all -> 0x004f }
            if (r7 == 0) goto L_0x004a
            java.lang.Object r3 = r15.apply(r14, r0)     // Catch:{ all -> 0x0045 }
            if (r3 == 0) goto L_0x003c
            java.util.concurrent.ConcurrentHashMap$Node r4 = new java.util.concurrent.ConcurrentHashMap$Node     // Catch:{ all -> 0x0045 }
            r4.<init>(r1, r14, r3)     // Catch:{ all -> 0x0045 }
            r5 = r8
            goto L_0x003e
        L_0x003c:
            r5 = r4
            r4 = r0
        L_0x003e:
            setTabAt(r2, r6, r4)     // Catch:{ all -> 0x004f }
            r4 = r5
            r5 = r3
            r3 = r8
            goto L_0x004a
        L_0x0045:
            r13 = move-exception
            setTabAt(r2, r6, r0)     // Catch:{ all -> 0x004f }
            throw r13     // Catch:{ all -> 0x004f }
        L_0x004a:
            monitor-exit(r9)     // Catch:{ all -> 0x004f }
            if (r3 == 0) goto L_0x0012
            goto L_0x010a
        L_0x004f:
            r13 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x004f }
            throw r13
        L_0x0052:
            int r9 = r7.hash
            r10 = -1
            if (r9 != r10) goto L_0x005c
            java.util.concurrent.ConcurrentHashMap$Node[] r2 = r13.helpTransfer(r2, r7)
            goto L_0x0012
        L_0x005c:
            monitor-enter(r7)
            java.util.concurrent.ConcurrentHashMap$Node r11 = tabAt(r2, r6)     // Catch:{ all -> 0x0111 }
            if (r11 != r7) goto L_0x0100
            if (r9 < 0) goto L_0x00b7
            r9 = r0
            r5 = r7
            r3 = r8
        L_0x0068:
            int r11 = r5.hash     // Catch:{ all -> 0x0111 }
            if (r11 != r1) goto L_0x008f
            K r11 = r5.key     // Catch:{ all -> 0x0111 }
            if (r11 == r14) goto L_0x0078
            if (r11 == 0) goto L_0x008f
            boolean r11 = r14.equals(r11)     // Catch:{ all -> 0x0111 }
            if (r11 == 0) goto L_0x008f
        L_0x0078:
            V r8 = r5.val     // Catch:{ all -> 0x0111 }
            java.lang.Object r8 = r15.apply(r14, r8)     // Catch:{ all -> 0x0111 }
            if (r8 == 0) goto L_0x0083
            r5.val = r8     // Catch:{ all -> 0x0111 }
            goto L_0x00af
        L_0x0083:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r4 = r5.next     // Catch:{ all -> 0x0111 }
            if (r9 == 0) goto L_0x008a
            r9.next = r4     // Catch:{ all -> 0x0111 }
            goto L_0x008d
        L_0x008a:
            setTabAt(r2, r6, r4)     // Catch:{ all -> 0x0111 }
        L_0x008d:
            r4 = r10
            goto L_0x00af
        L_0x008f:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r9 = r5.next     // Catch:{ all -> 0x0111 }
            if (r9 != 0) goto L_0x00b1
            java.lang.Object r9 = r15.apply(r14, r0)     // Catch:{ all -> 0x0111 }
            if (r9 == 0) goto L_0x00ae
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r4 = r5.next     // Catch:{ all -> 0x0111 }
            if (r4 != 0) goto L_0x00a6
            java.util.concurrent.ConcurrentHashMap$Node r4 = new java.util.concurrent.ConcurrentHashMap$Node     // Catch:{ all -> 0x0111 }
            r4.<init>(r1, r14, r9)     // Catch:{ all -> 0x0111 }
            r5.next = r4     // Catch:{ all -> 0x0111 }
            r4 = r8
            goto L_0x00ae
        L_0x00a6:
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0111 }
            java.lang.String r14 = "Recursive update"
            r13.<init>((java.lang.String) r14)     // Catch:{ all -> 0x0111 }
            throw r13     // Catch:{ all -> 0x0111 }
        L_0x00ae:
            r8 = r9
        L_0x00af:
            r5 = r8
            goto L_0x0100
        L_0x00b1:
            int r3 = r3 + 1
            r12 = r9
            r9 = r5
            r5 = r12
            goto L_0x0068
        L_0x00b7:
            boolean r9 = r7 instanceof java.util.concurrent.ConcurrentHashMap.TreeBin     // Catch:{ all -> 0x0111 }
            if (r9 == 0) goto L_0x00f3
            r3 = r7
            java.util.concurrent.ConcurrentHashMap$TreeBin r3 = (java.util.concurrent.ConcurrentHashMap.TreeBin) r3     // Catch:{ all -> 0x0111 }
            java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r5 = r3.root     // Catch:{ all -> 0x0111 }
            if (r5 == 0) goto L_0x00c7
            java.util.concurrent.ConcurrentHashMap$TreeNode r5 = r5.findTreeNode(r1, r14, r0)     // Catch:{ all -> 0x0111 }
            goto L_0x00c8
        L_0x00c7:
            r5 = r0
        L_0x00c8:
            if (r5 != 0) goto L_0x00cc
            r9 = r0
            goto L_0x00ce
        L_0x00cc:
            java.lang.Object r9 = r5.val     // Catch:{ all -> 0x0111 }
        L_0x00ce:
            java.lang.Object r9 = r15.apply(r14, r9)     // Catch:{ all -> 0x0111 }
            if (r9 == 0) goto L_0x00de
            if (r5 == 0) goto L_0x00d9
            r5.val = r9     // Catch:{ all -> 0x0111 }
            goto L_0x00f0
        L_0x00d9:
            r3.putTreeVal(r1, r14, r9)     // Catch:{ all -> 0x0111 }
            r4 = r8
            goto L_0x00f0
        L_0x00de:
            if (r5 == 0) goto L_0x00f0
            boolean r4 = r3.removeTreeNode(r5)     // Catch:{ all -> 0x0111 }
            if (r4 == 0) goto L_0x00ef
            java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r3 = r3.first     // Catch:{ all -> 0x0111 }
            java.util.concurrent.ConcurrentHashMap$Node r3 = untreeify(r3)     // Catch:{ all -> 0x0111 }
            setTabAt(r2, r6, r3)     // Catch:{ all -> 0x0111 }
        L_0x00ef:
            r4 = r10
        L_0x00f0:
            r3 = r8
            r5 = r9
            goto L_0x0100
        L_0x00f3:
            boolean r8 = r7 instanceof java.util.concurrent.ConcurrentHashMap.ReservationNode     // Catch:{ all -> 0x0111 }
            if (r8 != 0) goto L_0x00f8
            goto L_0x0100
        L_0x00f8:
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0111 }
            java.lang.String r14 = "Recursive update"
            r13.<init>((java.lang.String) r14)     // Catch:{ all -> 0x0111 }
            throw r13     // Catch:{ all -> 0x0111 }
        L_0x0100:
            monitor-exit(r7)     // Catch:{ all -> 0x0111 }
            if (r3 == 0) goto L_0x0012
            r14 = 8
            if (r3 < r14) goto L_0x010a
            r13.treeifyBin(r2, r6)
        L_0x010a:
            if (r4 == 0) goto L_0x0110
            long r14 = (long) r4
            r13.addCount(r14, r3)
        L_0x0110:
            return r5
        L_0x0111:
            r13 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0111 }
            throw r13
        L_0x0114:
            java.util.concurrent.ConcurrentHashMap$Node[] r2 = r13.initTable()
            goto L_0x0012
        L_0x011a:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.compute(java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        int i;
        TreeNode<K, V> treeNode;
        V v2;
        V v3;
        K k2;
        K k3 = k;
        V v4 = v;
        BiFunction<? super V, ? super V, ? extends V> biFunction2 = biFunction;
        if (k3 == null || v4 == null || biFunction2 == null) {
            throw null;
        }
        int spread = spread(k.hashCode());
        Node<K, V>[] nodeArr = this.table;
        int i2 = 0;
        V v5 = null;
        int i3 = 0;
        while (true) {
            if (nodeArr != null) {
                int length = nodeArr.length;
                if (length != 0) {
                    int i4 = (length - 1) & spread;
                    Node<K, V> tabAt = tabAt(nodeArr, i4);
                    i = 1;
                    if (tabAt != null) {
                        int i5 = tabAt.hash;
                        if (i5 == -1) {
                            nodeArr = helpTransfer(nodeArr, tabAt);
                        } else {
                            synchronized (tabAt) {
                                if (tabAt(nodeArr, i4) == tabAt) {
                                    if (i5 >= 0) {
                                        Node<K, V> node = null;
                                        Node<K, V> node2 = tabAt;
                                        int i6 = 1;
                                        while (true) {
                                            if (node2.hash != spread || ((k2 = node2.key) != k3 && (k2 == null || !k3.equals(k2)))) {
                                                Node<K, V> node3 = node2.next;
                                                if (node3 == null) {
                                                    node2.next = new Node<>(spread, k3, v4);
                                                    i3 = 1;
                                                    v3 = v4;
                                                    break;
                                                }
                                                i6++;
                                                Node<K, V> node4 = node3;
                                                node = node2;
                                                node2 = node4;
                                            }
                                        }
                                        v3 = biFunction2.apply(node2.val, v4);
                                        if (v3 != null) {
                                            node2.val = v3;
                                        } else {
                                            Node<K, V> node5 = node2.next;
                                            if (node != null) {
                                                node.next = node5;
                                            } else {
                                                setTabAt(nodeArr, i4, node5);
                                            }
                                            i3 = -1;
                                        }
                                        i2 = i6;
                                        v5 = v3;
                                    } else if (tabAt instanceof TreeBin) {
                                        TreeBin treeBin = (TreeBin) tabAt;
                                        TreeNode<K, V> treeNode2 = treeBin.root;
                                        if (treeNode2 == null) {
                                            treeNode = null;
                                        } else {
                                            treeNode = treeNode2.findTreeNode(spread, k3, (Class<?>) null);
                                        }
                                        if (treeNode == null) {
                                            v2 = v4;
                                        } else {
                                            v2 = biFunction2.apply(treeNode.val, v4);
                                        }
                                        if (v2 != null) {
                                            if (treeNode != null) {
                                                treeNode.val = v2;
                                            } else {
                                                treeBin.putTreeVal(spread, k3, v2);
                                                i3 = 1;
                                            }
                                        } else if (treeNode != null) {
                                            if (treeBin.removeTreeNode(treeNode)) {
                                                setTabAt(nodeArr, i4, untreeify(treeBin.first));
                                            }
                                            i3 = -1;
                                        }
                                        i2 = 2;
                                        v5 = v2;
                                    } else if (tabAt instanceof ReservationNode) {
                                        throw new IllegalStateException("Recursive update");
                                    }
                                }
                            }
                            if (i2 != 0) {
                                if (i2 >= 8) {
                                    treeifyBin(nodeArr, i4);
                                }
                                i = i3;
                                v4 = v5;
                            }
                        }
                    } else if (casTabAt(nodeArr, i4, (Node) null, new Node(spread, k3, v4))) {
                        break;
                    }
                }
            }
            nodeArr = initTable();
        }
        if (i != 0) {
            addCount((long) i, i2);
        }
        return v4;
    }

    public boolean contains(Object obj) {
        return containsValue(obj);
    }

    public Enumeration<K> keys() {
        Node<K, V>[] nodeArr = this.table;
        int length = nodeArr == null ? 0 : nodeArr.length;
        return new KeyIterator(nodeArr, length, 0, length, this);
    }

    public Enumeration<V> elements() {
        Node<K, V>[] nodeArr = this.table;
        int length = nodeArr == null ? 0 : nodeArr.length;
        return new ValueIterator(nodeArr, length, 0, length, this);
    }

    public long mappingCount() {
        long sumCount = sumCount();
        if (sumCount < 0) {
            return 0;
        }
        return sumCount;
    }

    public static <K> KeySetView<K, Boolean> newKeySet() {
        return new KeySetView<>(new ConcurrentHashMap(), Boolean.TRUE);
    }

    public static <K> KeySetView<K, Boolean> newKeySet(int i) {
        return new KeySetView<>(new ConcurrentHashMap(i), Boolean.TRUE);
    }

    public KeySetView<K, V> keySet(V v) {
        v.getClass();
        return new KeySetView<>(this, v);
    }

    static final class ForwardingNode<K, V> extends Node<K, V> {
        final Node<K, V>[] nextTable;

        ForwardingNode(Node<K, V>[] nodeArr) {
            super(-1, null, null);
            this.nextTable = nodeArr;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0029, code lost:
            if ((r3 instanceof java.util.concurrent.ConcurrentHashMap.ForwardingNode) == false) goto L_0x0030;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x002b, code lost:
            r3 = ((java.util.concurrent.ConcurrentHashMap.ForwardingNode) r3).nextTable;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0034, code lost:
            return r3.find(r4, r5);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.concurrent.ConcurrentHashMap.Node<K, V> find(int r4, java.lang.Object r5) {
            /*
                r3 = this;
                java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r3 = r3.nextTable
            L_0x0002:
                r0 = 0
                if (r5 == 0) goto L_0x0039
                if (r3 == 0) goto L_0x0039
                int r1 = r3.length
                if (r1 == 0) goto L_0x0039
                int r1 = r1 + -1
                r1 = r1 & r4
                java.util.concurrent.ConcurrentHashMap$Node r3 = java.util.concurrent.ConcurrentHashMap.tabAt(r3, r1)
                if (r3 != 0) goto L_0x0014
                goto L_0x0039
            L_0x0014:
                int r1 = r3.hash
                if (r1 != r4) goto L_0x0025
                K r2 = r3.key
                if (r2 == r5) goto L_0x0024
                if (r2 == 0) goto L_0x0025
                boolean r2 = r5.equals(r2)
                if (r2 == 0) goto L_0x0025
            L_0x0024:
                return r3
            L_0x0025:
                if (r1 >= 0) goto L_0x0035
                boolean r0 = r3 instanceof java.util.concurrent.ConcurrentHashMap.ForwardingNode
                if (r0 == 0) goto L_0x0030
                java.util.concurrent.ConcurrentHashMap$ForwardingNode r3 = (java.util.concurrent.ConcurrentHashMap.ForwardingNode) r3
                java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r3 = r3.nextTable
                goto L_0x0002
            L_0x0030:
                java.util.concurrent.ConcurrentHashMap$Node r3 = r3.find(r4, r5)
                return r3
            L_0x0035:
                java.util.concurrent.ConcurrentHashMap$Node<K, V> r3 = r3.next
                if (r3 != 0) goto L_0x0014
            L_0x0039:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.ForwardingNode.find(int, java.lang.Object):java.util.concurrent.ConcurrentHashMap$Node");
        }
    }

    static final class ReservationNode<K, V> extends Node<K, V> {
        /* access modifiers changed from: package-private */
        public Node<K, V> find(int i, Object obj) {
            return null;
        }

        ReservationNode() {
            super(-3, null, null);
        }
    }

    static final int resizeStamp(int i) {
        return Integer.numberOfLeadingZeros(i) | 32768;
    }

    /* JADX INFO: finally extract failed */
    private final Node<K, V>[] initTable() {
        while (true) {
            Node<K, V>[] nodeArr = this.table;
            if (nodeArr != null && nodeArr.length != 0) {
                return nodeArr;
            }
            int i = this.sizeCtl;
            if (i < 0) {
                Thread.yield();
            } else {
                if (f735U.compareAndSetInt(this, SIZECTL, i, -1)) {
                    try {
                        Node<K, V>[] nodeArr2 = this.table;
                        if (nodeArr2 == null || nodeArr2.length == 0) {
                            int i2 = i > 0 ? i : 16;
                            Node<K, V>[] nodeArr3 = new Node[i2];
                            this.table = nodeArr3;
                            i = i2 - (i2 >>> 2);
                            nodeArr2 = nodeArr3;
                        }
                        this.sizeCtl = i;
                        return nodeArr2;
                    } catch (Throwable th) {
                        this.sizeCtl = i;
                        throw th;
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0012, code lost:
        if (r1.compareAndSetLong(r11, r3, r5, r9) == false) goto L_0x0014;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void addCount(long r12, int r14) {
        /*
            r11 = this;
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r0 = r11.counterCells
            if (r0 != 0) goto L_0x0014
            jdk.internal.misc.Unsafe r1 = f735U
            long r3 = BASECOUNT
            long r5 = r11.baseCount
            long r9 = r5 + r12
            r2 = r11
            r7 = r9
            boolean r1 = r1.compareAndSetLong(r2, r3, r5, r7)
            if (r1 != 0) goto L_0x003b
        L_0x0014:
            r1 = 1
            if (r0 == 0) goto L_0x0094
            int r2 = r0.length
            int r2 = r2 - r1
            if (r2 < 0) goto L_0x0094
            int r3 = java.util.concurrent.ThreadLocalRandom.getProbe()
            r2 = r2 & r3
            r4 = r0[r2]
            if (r4 == 0) goto L_0x0094
            jdk.internal.misc.Unsafe r3 = f735U
            long r5 = CELLVALUE
            long r7 = r4.value
            long r9 = r7 + r12
            boolean r0 = r3.compareAndSetLong(r4, r5, r7, r9)
            if (r0 != 0) goto L_0x0034
            r1 = r0
            goto L_0x0094
        L_0x0034:
            if (r14 > r1) goto L_0x0037
            return
        L_0x0037:
            long r9 = r11.sumCount()
        L_0x003b:
            if (r14 < 0) goto L_0x0093
        L_0x003d:
            int r4 = r11.sizeCtl
            long r12 = (long) r4
            int r12 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1))
            if (r12 < 0) goto L_0x0093
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r12 = r11.table
            if (r12 == 0) goto L_0x0093
            int r13 = r12.length
            r14 = 1073741824(0x40000000, float:2.0)
            if (r13 >= r14) goto L_0x0093
            int r13 = resizeStamp(r13)
            if (r4 >= 0) goto L_0x007b
            int r14 = r4 >>> 16
            if (r14 != r13) goto L_0x0093
            int r14 = r13 + 1
            if (r4 == r14) goto L_0x0093
            r14 = 65535(0xffff, float:9.1834E-41)
            int r13 = r13 + r14
            if (r4 == r13) goto L_0x0093
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r13 = r11.nextTable
            if (r13 == 0) goto L_0x0093
            int r14 = r11.transferIndex
            if (r14 > 0) goto L_0x006a
            goto L_0x0093
        L_0x006a:
            jdk.internal.misc.Unsafe r0 = f735U
            long r2 = SIZECTL
            int r5 = r4 + 1
            r1 = r11
            boolean r14 = r0.compareAndSetInt(r1, r2, r4, r5)
            if (r14 == 0) goto L_0x008e
            r11.transfer(r12, r13)
            goto L_0x008e
        L_0x007b:
            jdk.internal.misc.Unsafe r0 = f735U
            long r2 = SIZECTL
            int r13 = r13 << 16
            int r5 = r13 + 2
            r1 = r11
            boolean r13 = r0.compareAndSetInt(r1, r2, r4, r5)
            if (r13 == 0) goto L_0x008e
            r13 = 0
            r11.transfer(r12, r13)
        L_0x008e:
            long r9 = r11.sumCount()
            goto L_0x003d
        L_0x0093:
            return
        L_0x0094:
            r11.fullAddCount(r12, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.addCount(long, int):void");
    }

    /* access modifiers changed from: package-private */
    public final Node<K, V>[] helpTransfer(Node<K, V>[] nodeArr, Node<K, V> node) {
        Node<K, V>[] nodeArr2;
        int i;
        if (nodeArr == null || !(node instanceof ForwardingNode) || (nodeArr2 = ((ForwardingNode) node).nextTable) == null) {
            return this.table;
        }
        int resizeStamp = resizeStamp(nodeArr.length);
        while (true) {
            if (nodeArr2 != this.nextTable || this.table != nodeArr || (i = this.sizeCtl) >= 0 || (i >>> 16) != resizeStamp || i == resizeStamp + 1 || i == 65535 + resizeStamp || this.transferIndex <= 0) {
                break;
            }
            if (f735U.compareAndSetInt(this, SIZECTL, i, i + 1)) {
                transfer(nodeArr, nodeArr2);
                break;
            }
        }
        return nodeArr2;
    }

    private final void tryPresize(int i) {
        int length;
        int tableSizeFor = i >= 536870912 ? 1073741824 : tableSizeFor(i + (i >>> 1) + 1);
        while (true) {
            int i2 = this.sizeCtl;
            if (i2 >= 0) {
                Node<K, V>[] nodeArr = this.table;
                if (nodeArr == null || (length = nodeArr.length) == 0) {
                    int i3 = i2 > tableSizeFor ? i2 : tableSizeFor;
                    if (f735U.compareAndSetInt(this, SIZECTL, i2, -1)) {
                        try {
                            if (this.table == nodeArr) {
                                this.table = new Node[i3];
                                i2 = i3 - (i3 >>> 2);
                            }
                        } finally {
                            this.sizeCtl = i2;
                        }
                    }
                } else if (tableSizeFor > i2 && length < 1073741824) {
                    if (nodeArr == this.table) {
                        if (f735U.compareAndSetInt(this, SIZECTL, i2, (resizeStamp(length) << 16) + 2)) {
                            transfer(nodeArr, (Node<K, V>[]) null);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v10, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v5, resolved type: java.util.concurrent.ConcurrentHashMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v6, resolved type: java.util.concurrent.ConcurrentHashMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v11, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v12, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v13, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v14, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v15, resolved type: java.util.concurrent.ConcurrentHashMap$TreeNode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v7, resolved type: java.util.concurrent.ConcurrentHashMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v8, resolved type: java.util.concurrent.ConcurrentHashMap$Node} */
    /* JADX WARNING: type inference failed for: r13v12, types: [java.util.concurrent.ConcurrentHashMap$Node] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void transfer(java.util.concurrent.ConcurrentHashMap.Node<K, V>[] r31, java.util.concurrent.ConcurrentHashMap.Node<K, V>[] r32) {
        /*
            r30 = this;
            r0 = r30
            r7 = r31
            int r8 = r7.length
            int r1 = NCPU
            r9 = 1
            if (r1 <= r9) goto L_0x000e
            int r2 = r8 >>> 3
            int r2 = r2 / r1
            goto L_0x000f
        L_0x000e:
            r2 = r8
        L_0x000f:
            r10 = 16
            if (r2 >= r10) goto L_0x0015
            r11 = r10
            goto L_0x0016
        L_0x0015:
            r11 = r2
        L_0x0016:
            if (r32 != 0) goto L_0x0028
            int r1 = r8 << 1
            java.util.concurrent.ConcurrentHashMap$Node[] r1 = new java.util.concurrent.ConcurrentHashMap.Node[r1]     // Catch:{ all -> 0x0022 }
            r0.nextTable = r1
            r0.transferIndex = r8
            r12 = r1
            goto L_0x002a
        L_0x0022:
            r1 = 2147483647(0x7fffffff, float:NaN)
            r0.sizeCtl = r1
            return
        L_0x0028:
            r12 = r32
        L_0x002a:
            int r13 = r12.length
            java.util.concurrent.ConcurrentHashMap$ForwardingNode r14 = new java.util.concurrent.ConcurrentHashMap$ForwardingNode
            r14.<init>(r12)
            r16 = r9
            r5 = 0
            r6 = 0
            r17 = 0
        L_0x0036:
            r1 = -1
            if (r16 == 0) goto L_0x007d
            int r6 = r6 + -1
            if (r6 >= r5) goto L_0x0072
            if (r17 == 0) goto L_0x0040
            goto L_0x0072
        L_0x0040:
            int r3 = r0.transferIndex
            if (r3 > 0) goto L_0x0046
            r6 = r1
            goto L_0x007a
        L_0x0046:
            jdk.internal.misc.Unsafe r1 = f735U
            long r18 = TRANSFERINDEX
            if (r3 <= r11) goto L_0x0051
            int r2 = r3 - r11
            r20 = r2
            goto L_0x0053
        L_0x0051:
            r20 = 0
        L_0x0053:
            r2 = r30
            r21 = r3
            r3 = r18
            r18 = r5
            r5 = r21
            r19 = r6
            r6 = r20
            boolean r1 = r1.compareAndSetInt(r2, r3, r5, r6)
            if (r1 == 0) goto L_0x006d
            int r3 = r21 + -1
            r6 = r3
            r5 = r20
            goto L_0x007a
        L_0x006d:
            r5 = r18
            r6 = r19
            goto L_0x0036
        L_0x0072:
            r18 = r5
            r19 = r6
            r5 = r18
            r6 = r19
        L_0x007a:
            r16 = 0
            goto L_0x0036
        L_0x007d:
            r18 = r5
            r2 = 0
            if (r6 < 0) goto L_0x01ab
            if (r6 >= r8) goto L_0x01ab
            int r3 = r6 + r8
            if (r3 < r13) goto L_0x008a
            goto L_0x01ab
        L_0x008a:
            java.util.concurrent.ConcurrentHashMap$Node r4 = tabAt(r7, r6)
            if (r4 != 0) goto L_0x00a1
            boolean r1 = casTabAt(r7, r6, r2, r14)
            r16 = r1
            r2 = r10
            r21 = r11
            r22 = r13
            r10 = r9
            r9 = r0
            r0 = r7
        L_0x009e:
            r7 = r14
            goto L_0x01ed
        L_0x00a1:
            int r5 = r4.hash
            if (r5 != r1) goto L_0x00b1
            r16 = r9
            r2 = r10
            r21 = r11
            r22 = r13
            r9 = r0
            r0 = r7
            r10 = r16
            goto L_0x009e
        L_0x00b1:
            monitor-enter(r4)
            java.util.concurrent.ConcurrentHashMap$Node r1 = tabAt(r7, r6)     // Catch:{ all -> 0x01a8 }
            if (r1 != r4) goto L_0x019b
            if (r5 < 0) goto L_0x010e
            r1 = r5 & r8
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r5 = r4.next     // Catch:{ all -> 0x01a8 }
            r15 = r4
        L_0x00bf:
            if (r5 == 0) goto L_0x00cd
            int r10 = r5.hash     // Catch:{ all -> 0x01a8 }
            r10 = r10 & r8
            if (r10 == r1) goto L_0x00c8
            r15 = r5
            r1 = r10
        L_0x00c8:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r5 = r5.next     // Catch:{ all -> 0x01a8 }
            r10 = 16
            goto L_0x00bf
        L_0x00cd:
            if (r1 != 0) goto L_0x00d2
            r1 = r2
            r2 = r15
            goto L_0x00d3
        L_0x00d2:
            r1 = r15
        L_0x00d3:
            r5 = r4
        L_0x00d4:
            if (r5 == r15) goto L_0x00fb
            int r10 = r5.hash     // Catch:{ all -> 0x01a8 }
            K r9 = r5.key     // Catch:{ all -> 0x01a8 }
            r21 = r11
            V r11 = r5.val     // Catch:{ all -> 0x01a8 }
            r16 = r10 & r8
            if (r16 != 0) goto L_0x00eb
            r22 = r13
            java.util.concurrent.ConcurrentHashMap$Node r13 = new java.util.concurrent.ConcurrentHashMap$Node     // Catch:{ all -> 0x01a8 }
            r13.<init>(r10, r9, r11, r2)     // Catch:{ all -> 0x01a8 }
            r2 = r13
            goto L_0x00f3
        L_0x00eb:
            r22 = r13
            java.util.concurrent.ConcurrentHashMap$Node r13 = new java.util.concurrent.ConcurrentHashMap$Node     // Catch:{ all -> 0x01a8 }
            r13.<init>(r10, r9, r11, r1)     // Catch:{ all -> 0x01a8 }
            r1 = r13
        L_0x00f3:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r5 = r5.next     // Catch:{ all -> 0x01a8 }
            r11 = r21
            r13 = r22
            r9 = 1
            goto L_0x00d4
        L_0x00fb:
            r21 = r11
            r22 = r13
            setTabAt(r12, r6, r2)     // Catch:{ all -> 0x01a8 }
            setTabAt(r12, r3, r1)     // Catch:{ all -> 0x01a8 }
            setTabAt(r7, r6, r14)     // Catch:{ all -> 0x01a8 }
            r0 = r7
            r7 = r14
        L_0x010a:
            r16 = 1
            goto L_0x01a1
        L_0x010e:
            r21 = r11
            r22 = r13
            boolean r1 = r4 instanceof java.util.concurrent.ConcurrentHashMap.TreeBin     // Catch:{ all -> 0x01a8 }
            if (r1 == 0) goto L_0x0199
            r1 = r4
            java.util.concurrent.ConcurrentHashMap$TreeBin r1 = (java.util.concurrent.ConcurrentHashMap.TreeBin) r1     // Catch:{ all -> 0x01a8 }
            java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r5 = r1.first     // Catch:{ all -> 0x01a8 }
            r9 = r2
            r10 = r9
            r11 = r5
            r13 = 0
            r15 = 0
            r5 = r10
        L_0x0121:
            if (r11 == 0) goto L_0x0163
            r16 = r1
            int r1 = r11.hash     // Catch:{ all -> 0x01a8 }
            java.util.concurrent.ConcurrentHashMap$TreeNode r0 = new java.util.concurrent.ConcurrentHashMap$TreeNode     // Catch:{ all -> 0x01a8 }
            K r7 = r11.key     // Catch:{ all -> 0x01a8 }
            r29 = r14
            V r14 = r11.val     // Catch:{ all -> 0x01a8 }
            r27 = 0
            r28 = 0
            r23 = r0
            r24 = r1
            r25 = r7
            r26 = r14
            r23.<init>(r24, r25, r26, r27, r28)     // Catch:{ all -> 0x01a8 }
            r1 = r1 & r8
            if (r1 != 0) goto L_0x014d
            r0.prev = r10     // Catch:{ all -> 0x01a8 }
            if (r10 != 0) goto L_0x0147
            r2 = r0
            goto L_0x0149
        L_0x0147:
            r10.next = r0     // Catch:{ all -> 0x01a8 }
        L_0x0149:
            int r13 = r13 + 1
            r10 = r0
            goto L_0x0158
        L_0x014d:
            r0.prev = r9     // Catch:{ all -> 0x01a8 }
            if (r9 != 0) goto L_0x0153
            r5 = r0
            goto L_0x0155
        L_0x0153:
            r9.next = r0     // Catch:{ all -> 0x01a8 }
        L_0x0155:
            int r15 = r15 + 1
            r9 = r0
        L_0x0158:
            java.util.concurrent.ConcurrentHashMap$Node<K, V> r11 = r11.next     // Catch:{ all -> 0x01a8 }
            r0 = r30
            r7 = r31
            r1 = r16
            r14 = r29
            goto L_0x0121
        L_0x0163:
            r16 = r1
            r29 = r14
            r0 = 6
            if (r13 > r0) goto L_0x016f
            java.util.concurrent.ConcurrentHashMap$Node r1 = untreeify(r2)     // Catch:{ all -> 0x01a8 }
            goto L_0x0179
        L_0x016f:
            if (r15 == 0) goto L_0x0177
            java.util.concurrent.ConcurrentHashMap$TreeBin r1 = new java.util.concurrent.ConcurrentHashMap$TreeBin     // Catch:{ all -> 0x01a8 }
            r1.<init>(r2)     // Catch:{ all -> 0x01a8 }
            goto L_0x0179
        L_0x0177:
            r1 = r16
        L_0x0179:
            if (r15 > r0) goto L_0x0180
            java.util.concurrent.ConcurrentHashMap$Node r0 = untreeify(r5)     // Catch:{ all -> 0x01a8 }
            goto L_0x018a
        L_0x0180:
            if (r13 == 0) goto L_0x0188
            java.util.concurrent.ConcurrentHashMap$TreeBin r0 = new java.util.concurrent.ConcurrentHashMap$TreeBin     // Catch:{ all -> 0x01a8 }
            r0.<init>(r5)     // Catch:{ all -> 0x01a8 }
            goto L_0x018a
        L_0x0188:
            r0 = r16
        L_0x018a:
            setTabAt(r12, r6, r1)     // Catch:{ all -> 0x01a8 }
            setTabAt(r12, r3, r0)     // Catch:{ all -> 0x01a8 }
            r0 = r31
            r7 = r29
            setTabAt(r0, r6, r7)     // Catch:{ all -> 0x01a8 }
            goto L_0x010a
        L_0x0199:
            r0 = r7
            goto L_0x01a0
        L_0x019b:
            r0 = r7
            r21 = r11
            r22 = r13
        L_0x01a0:
            r7 = r14
        L_0x01a1:
            monitor-exit(r4)     // Catch:{ all -> 0x01a8 }
            r9 = r30
            r2 = 16
            r10 = 1
            goto L_0x01ed
        L_0x01a8:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x01a8 }
            throw r0
        L_0x01ab:
            r0 = r7
            r21 = r11
            r22 = r13
            r7 = r14
            if (r17 == 0) goto L_0x01c2
            r9 = r30
            r9.nextTable = r2
            r9.table = r12
            int r0 = r8 << 1
            r10 = 1
            int r1 = r8 >>> 1
            int r0 = r0 - r1
            r9.sizeCtl = r0
            return
        L_0x01c2:
            r9 = r30
            r10 = 1
            jdk.internal.misc.Unsafe r1 = f735U
            long r3 = SIZECTL
            int r11 = r9.sizeCtl
            int r13 = r11 + -1
            r2 = r30
            r5 = r11
            r15 = r6
            r6 = r13
            boolean r1 = r1.compareAndSetInt(r2, r3, r5, r6)
            if (r1 == 0) goto L_0x01ea
            int r11 = r11 + -2
            int r1 = resizeStamp(r8)
            r2 = 16
            int r1 = r1 << r2
            if (r11 == r1) goto L_0x01e4
            return
        L_0x01e4:
            r6 = r8
            r16 = r10
            r17 = r16
            goto L_0x01ed
        L_0x01ea:
            r2 = 16
            r6 = r15
        L_0x01ed:
            r14 = r7
            r5 = r18
            r11 = r21
            r13 = r22
            r7 = r0
            r0 = r9
            r9 = r10
            r10 = r2
            goto L_0x0036
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.transfer(java.util.concurrent.ConcurrentHashMap$Node[], java.util.concurrent.ConcurrentHashMap$Node[]):void");
    }

    static final class CounterCell {
        volatile long value;

        CounterCell(long j) {
            this.value = j;
        }
    }

    /* access modifiers changed from: package-private */
    public final long sumCount() {
        CounterCell[] counterCellArr = this.counterCells;
        long j = this.baseCount;
        if (counterCellArr != null) {
            for (CounterCell counterCell : counterCellArr) {
                if (counterCell != null) {
                    j += counterCell.value;
                }
            }
        }
        return j;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x009d, code lost:
        if (r9.counterCells != r7) goto L_0x00a9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x009f, code lost:
        r9.counterCells = (java.util.concurrent.ConcurrentHashMap.CounterCell[]) java.util.Arrays.copyOf((T[]) r7, r8 << 1);
     */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00fc A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x001b A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void fullAddCount(long r25, boolean r27) {
        /*
            r24 = this;
            r9 = r24
            r10 = r25
            int r0 = java.util.concurrent.ThreadLocalRandom.getProbe()
            r12 = 1
            if (r0 != 0) goto L_0x0015
            java.util.concurrent.ThreadLocalRandom.localInit()
            int r0 = java.util.concurrent.ThreadLocalRandom.getProbe()
            r1 = r0
            r0 = r12
            goto L_0x0018
        L_0x0015:
            r1 = r0
            r0 = r27
        L_0x0018:
            r13 = 0
            r14 = r1
        L_0x001a:
            r15 = r13
        L_0x001b:
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r7 = r9.counterCells
            if (r7 == 0) goto L_0x00b8
            int r8 = r7.length
            if (r8 <= 0) goto L_0x00b8
            int r1 = r8 + -1
            r1 = r1 & r14
            r1 = r7[r1]
            if (r1 != 0) goto L_0x0063
            int r1 = r9.cellsBusy
            if (r1 != 0) goto L_0x0061
            java.util.concurrent.ConcurrentHashMap$CounterCell r7 = new java.util.concurrent.ConcurrentHashMap$CounterCell
            r7.<init>(r10)
            int r1 = r9.cellsBusy
            if (r1 != 0) goto L_0x0061
            jdk.internal.misc.Unsafe r1 = f735U
            long r3 = CELLSBUSY
            r5 = 0
            r6 = 1
            r2 = r24
            boolean r1 = r1.compareAndSetInt(r2, r3, r5, r6)
            if (r1 == 0) goto L_0x0061
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r1 = r9.counterCells     // Catch:{ all -> 0x005d }
            if (r1 == 0) goto L_0x0056
            int r2 = r1.length     // Catch:{ all -> 0x005d }
            if (r2 <= 0) goto L_0x0056
            int r2 = r2 + -1
            r2 = r2 & r14
            r3 = r1[r2]     // Catch:{ all -> 0x005d }
            if (r3 != 0) goto L_0x0056
            r1[r2] = r7     // Catch:{ all -> 0x005d }
            r1 = r12
            goto L_0x0057
        L_0x0056:
            r1 = r13
        L_0x0057:
            r9.cellsBusy = r13
            if (r1 == 0) goto L_0x001b
            goto L_0x00fc
        L_0x005d:
            r0 = move-exception
            r9.cellsBusy = r13
            throw r0
        L_0x0061:
            r15 = r13
            goto L_0x00b1
        L_0x0063:
            if (r0 != 0) goto L_0x0067
            r0 = r12
            goto L_0x00b1
        L_0x0067:
            jdk.internal.misc.Unsafe r2 = f735U
            long r18 = CELLVALUE
            long r3 = r1.value
            long r22 = r3 + r10
            r16 = r2
            r17 = r1
            r20 = r3
            boolean r1 = r16.compareAndSetLong(r17, r18, r20, r22)
            if (r1 == 0) goto L_0x007d
            goto L_0x00fc
        L_0x007d:
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r1 = r9.counterCells
            if (r1 != r7) goto L_0x0061
            int r1 = NCPU
            if (r8 < r1) goto L_0x0086
            goto L_0x0061
        L_0x0086:
            if (r15 != 0) goto L_0x008a
            r15 = r12
            goto L_0x00b1
        L_0x008a:
            int r1 = r9.cellsBusy
            if (r1 != 0) goto L_0x00b1
            long r3 = CELLSBUSY
            r5 = 0
            r6 = 1
            r1 = r2
            r2 = r24
            boolean r1 = r1.compareAndSetInt(r2, r3, r5, r6)
            if (r1 == 0) goto L_0x00b1
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r1 = r9.counterCells     // Catch:{ all -> 0x00ad }
            if (r1 != r7) goto L_0x00a9
            int r1 = r8 << 1
            java.lang.Object[] r1 = java.util.Arrays.copyOf((T[]) r7, (int) r1)     // Catch:{ all -> 0x00ad }
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r1 = (java.util.concurrent.ConcurrentHashMap.CounterCell[]) r1     // Catch:{ all -> 0x00ad }
            r9.counterCells = r1     // Catch:{ all -> 0x00ad }
        L_0x00a9:
            r9.cellsBusy = r13
            goto L_0x001a
        L_0x00ad:
            r0 = move-exception
            r9.cellsBusy = r13
            throw r0
        L_0x00b1:
            int r1 = java.util.concurrent.ThreadLocalRandom.advanceProbe(r14)
            r14 = r1
            goto L_0x001b
        L_0x00b8:
            int r1 = r9.cellsBusy
            if (r1 != 0) goto L_0x00ec
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r1 = r9.counterCells
            if (r1 != r7) goto L_0x00ec
            jdk.internal.misc.Unsafe r1 = f735U
            long r3 = CELLSBUSY
            r5 = 0
            r6 = 1
            r2 = r24
            boolean r1 = r1.compareAndSetInt(r2, r3, r5, r6)
            if (r1 == 0) goto L_0x00ec
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r1 = r9.counterCells     // Catch:{ all -> 0x00e8 }
            if (r1 != r7) goto L_0x00e2
            r1 = 2
            java.util.concurrent.ConcurrentHashMap$CounterCell[] r1 = new java.util.concurrent.ConcurrentHashMap.CounterCell[r1]     // Catch:{ all -> 0x00e8 }
            r2 = r14 & 1
            java.util.concurrent.ConcurrentHashMap$CounterCell r3 = new java.util.concurrent.ConcurrentHashMap$CounterCell     // Catch:{ all -> 0x00e8 }
            r3.<init>(r10)     // Catch:{ all -> 0x00e8 }
            r1[r2] = r3     // Catch:{ all -> 0x00e8 }
            r9.counterCells = r1     // Catch:{ all -> 0x00e8 }
            r1 = r12
            goto L_0x00e3
        L_0x00e2:
            r1 = r13
        L_0x00e3:
            r9.cellsBusy = r13
            if (r1 == 0) goto L_0x001b
            goto L_0x00fc
        L_0x00e8:
            r0 = move-exception
            r9.cellsBusy = r13
            throw r0
        L_0x00ec:
            jdk.internal.misc.Unsafe r1 = f735U
            long r3 = BASECOUNT
            long r5 = r9.baseCount
            long r7 = r5 + r10
            r2 = r24
            boolean r1 = r1.compareAndSetLong(r2, r3, r5, r7)
            if (r1 == 0) goto L_0x001b
        L_0x00fc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.fullAddCount(long, boolean):void");
    }

    private final void treeifyBin(Node<K, V>[] nodeArr, int i) {
        if (nodeArr != null) {
            int length = nodeArr.length;
            if (length < 64) {
                tryPresize(length << 1);
                return;
            }
            Node<K, V> tabAt = tabAt(nodeArr, i);
            if (tabAt != null && tabAt.hash >= 0) {
                synchronized (tabAt) {
                    if (tabAt(nodeArr, i) == tabAt) {
                        TreeNode<K, V> treeNode = null;
                        Node<K, V> node = tabAt;
                        TreeNode<K, V> treeNode2 = null;
                        while (node != null) {
                            TreeNode<K, V> treeNode3 = new TreeNode<>(node.hash, node.key, node.val, (Node) null, (TreeNode) null);
                            treeNode3.prev = treeNode2;
                            if (treeNode2 == null) {
                                treeNode = treeNode3;
                            } else {
                                treeNode2.next = treeNode3;
                            }
                            node = node.next;
                            treeNode2 = treeNode3;
                        }
                        setTabAt(nodeArr, i, new TreeBin(treeNode));
                    }
                }
            }
        }
    }

    static <K, V> Node<K, V> untreeify(Node<K, V> node) {
        Node<K, V> node2 = null;
        Node<K, V> node3 = null;
        while (node != null) {
            Node<K, V> node4 = new Node<>(node.hash, node.key, node.val);
            if (node3 == null) {
                node2 = node4;
            } else {
                node3.next = node4;
            }
            node = node.next;
            node3 = node4;
        }
        return node2;
    }

    static final class TreeNode<K, V> extends Node<K, V> {
        TreeNode<K, V> left;
        TreeNode<K, V> parent;
        TreeNode<K, V> prev;
        boolean red;
        TreeNode<K, V> right;

        TreeNode(int i, K k, V v, Node<K, V> node, TreeNode<K, V> treeNode) {
            super(i, k, v, node);
            this.parent = treeNode;
        }

        /* access modifiers changed from: package-private */
        public Node<K, V> find(int i, Object obj) {
            return findTreeNode(i, obj, (Class<?>) null);
        }

        /* access modifiers changed from: package-private */
        public final TreeNode<K, V> findTreeNode(int i, Object obj, Class<?> cls) {
            int compareComparables;
            if (obj == null) {
                return null;
            }
            do {
                TreeNode<K, V> treeNode = r4.left;
                TreeNode<K, V> treeNode2 = r4.right;
                int i2 = r4.hash;
                if (i2 <= i) {
                    if (i2 >= i) {
                        Object obj2 = r4.key;
                        if (obj2 == obj || (obj2 != null && obj.equals(obj2))) {
                            return r4;
                        }
                        if (treeNode != null) {
                            if (treeNode2 != null) {
                                if ((cls == null && (cls = ConcurrentHashMap.comparableClassFor(obj)) == null) || (compareComparables = ConcurrentHashMap.compareComparables(cls, obj, obj2)) == 0) {
                                    TreeNode<K, V> findTreeNode = treeNode2.findTreeNode(i, obj, cls);
                                    if (findTreeNode != null) {
                                        return findTreeNode;
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
    }

    static final class TreeBin<K, V> extends Node<K, V> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long LOCKSTATE;
        static final int READER = 4;

        /* renamed from: U */
        private static final Unsafe f736U;
        static final int WAITER = 2;
        static final int WRITER = 1;
        volatile TreeNode<K, V> first;
        volatile int lockState;
        TreeNode<K, V> root;
        volatile Thread waiter;

        static {
            Class<ConcurrentHashMap> cls = ConcurrentHashMap.class;
            Unsafe unsafe = Unsafe.getUnsafe();
            f736U = unsafe;
            LOCKSTATE = unsafe.objectFieldOffset(TreeBin.class, "lockState");
        }

        static int tieBreakOrder(Object obj, Object obj2) {
            int compareTo;
            if (obj != null && obj2 != null && (compareTo = obj.getClass().getName().compareTo(obj2.getClass().getName())) != 0) {
                return compareTo;
            }
            return System.identityHashCode(obj) <= System.identityHashCode(obj2) ? -1 : 1;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002f, code lost:
            r6 = java.util.concurrent.ConcurrentHashMap.comparableClassFor(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0035, code lost:
            r8 = java.util.concurrent.ConcurrentHashMap.compareComparables(r6, r3, r7);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        TreeBin(java.util.concurrent.ConcurrentHashMap.TreeNode<K, V> r10) {
            /*
                r9 = this;
                r0 = -2
                r1 = 0
                r9.<init>(r0, r1, r1)
                r9.first = r10
                r0 = r1
            L_0x0008:
                if (r10 == 0) goto L_0x005c
                java.util.concurrent.ConcurrentHashMap$Node r2 = r10.next
                java.util.concurrent.ConcurrentHashMap$TreeNode r2 = (java.util.concurrent.ConcurrentHashMap.TreeNode) r2
                r10.right = r1
                r10.left = r1
                if (r0 != 0) goto L_0x001b
                r10.parent = r1
                r0 = 0
                r10.red = r0
            L_0x0019:
                r0 = r10
                goto L_0x0058
            L_0x001b:
                java.lang.Object r3 = r10.key
                int r4 = r10.hash
                r5 = r0
                r6 = r1
            L_0x0021:
                java.lang.Object r7 = r5.key
                int r8 = r5.hash
                if (r8 <= r4) goto L_0x0029
                r7 = -1
                goto L_0x0041
            L_0x0029:
                if (r8 >= r4) goto L_0x002d
                r7 = 1
                goto L_0x0041
            L_0x002d:
                if (r6 != 0) goto L_0x0035
                java.lang.Class r6 = java.util.concurrent.ConcurrentHashMap.comparableClassFor(r3)
                if (r6 == 0) goto L_0x003b
            L_0x0035:
                int r8 = java.util.concurrent.ConcurrentHashMap.compareComparables(r6, r3, r7)
                if (r8 != 0) goto L_0x0040
            L_0x003b:
                int r7 = tieBreakOrder(r3, r7)
                goto L_0x0041
            L_0x0040:
                r7 = r8
            L_0x0041:
                if (r7 > 0) goto L_0x0046
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r8 = r5.left
                goto L_0x0048
            L_0x0046:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r8 = r5.right
            L_0x0048:
                if (r8 != 0) goto L_0x005a
                r10.parent = r5
                if (r7 > 0) goto L_0x0051
                r5.left = r10
                goto L_0x0053
            L_0x0051:
                r5.right = r10
            L_0x0053:
                java.util.concurrent.ConcurrentHashMap$TreeNode r10 = balanceInsertion(r0, r10)
                goto L_0x0019
            L_0x0058:
                r10 = r2
                goto L_0x0008
            L_0x005a:
                r5 = r8
                goto L_0x0021
            L_0x005c:
                r9.root = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.TreeBin.<init>(java.util.concurrent.ConcurrentHashMap$TreeNode):void");
        }

        private final void lockRoot() {
            if (!f736U.compareAndSetInt(this, LOCKSTATE, 0, 1)) {
                contendedLock();
            }
        }

        private final void unlockRoot() {
            this.lockState = 0;
        }

        private final void contendedLock() {
            boolean z = false;
            while (true) {
                int i = this.lockState;
                if ((i & -3) == 0) {
                    if (f736U.compareAndSetInt(this, LOCKSTATE, i, 1)) {
                        break;
                    }
                } else if ((i & 2) == 0) {
                    if (f736U.compareAndSetInt(this, LOCKSTATE, i, i | 2)) {
                        this.waiter = Thread.currentThread();
                        z = true;
                    }
                } else if (z) {
                    LockSupport.park(this);
                }
            }
            if (z) {
                this.waiter = null;
            }
        }

        /* access modifiers changed from: package-private */
        public final Node<K, V> find(int i, Object obj) {
            Thread thread;
            Thread thread2;
            K k;
            TreeNode<K, V> treeNode = null;
            if (obj != null) {
                Node<K, V> node = this.first;
                while (node != null) {
                    int i2 = this.lockState;
                    if ((i2 & 3) == 0) {
                        Unsafe unsafe = f736U;
                        long j = LOCKSTATE;
                        if (unsafe.compareAndSetInt(this, j, i2, i2 + 4)) {
                            try {
                                TreeNode<K, V> treeNode2 = this.root;
                                if (treeNode2 != null) {
                                    treeNode = treeNode2.findTreeNode(i, obj, (Class<?>) null);
                                }
                                if (unsafe.getAndAddInt(this, j, -4) == 6 && (thread2 = this.waiter) != null) {
                                    LockSupport.unpark(thread2);
                                }
                                return treeNode;
                            } catch (Throwable th) {
                                if (f736U.getAndAddInt(this, LOCKSTATE, -4) == 6 && (thread = this.waiter) != null) {
                                    LockSupport.unpark(thread);
                                }
                                throw th;
                            }
                        }
                    } else if (node.hash == i && ((k = node.key) == obj || (k != null && obj.equals(k)))) {
                        return node;
                    } else {
                        node = node.next;
                    }
                }
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0056, code lost:
            return r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0096, code lost:
            return null;
         */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0060  */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x0063  */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x009c A[LOOP:0: B:1:0x0005->B:53:0x009c, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:58:0x0067 A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final java.util.concurrent.ConcurrentHashMap.TreeNode<K, V> putTreeVal(int r13, K r14, V r15) {
            /*
                r12 = this;
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r0 = r12.root
                r1 = 0
                r2 = 0
                r3 = r1
            L_0x0005:
                if (r0 != 0) goto L_0x0018
                java.util.concurrent.ConcurrentHashMap$TreeNode r0 = new java.util.concurrent.ConcurrentHashMap$TreeNode
                r8 = 0
                r9 = 0
                r4 = r0
                r5 = r13
                r6 = r14
                r7 = r15
                r4.<init>(r5, r6, r7, r8, r9)
                r12.root = r0
                r12.first = r0
                goto L_0x0096
            L_0x0018:
                int r4 = r0.hash
                r9 = 1
                if (r4 <= r13) goto L_0x0020
                r4 = -1
            L_0x001e:
                r10 = r4
                goto L_0x005e
            L_0x0020:
                if (r4 >= r13) goto L_0x0024
                r10 = r9
                goto L_0x005e
            L_0x0024:
                java.lang.Object r4 = r0.key
                if (r4 == r14) goto L_0x009f
                if (r4 == 0) goto L_0x0032
                boolean r5 = r14.equals(r4)
                if (r5 == 0) goto L_0x0032
                goto L_0x009f
            L_0x0032:
                if (r3 != 0) goto L_0x003a
                java.lang.Class r3 = java.util.concurrent.ConcurrentHashMap.comparableClassFor(r14)
                if (r3 == 0) goto L_0x0040
            L_0x003a:
                int r5 = java.util.concurrent.ConcurrentHashMap.compareComparables(r3, r14, r4)
                if (r5 != 0) goto L_0x005d
            L_0x0040:
                if (r2 != 0) goto L_0x0058
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r2 = r0.left
                if (r2 == 0) goto L_0x004c
                java.util.concurrent.ConcurrentHashMap$TreeNode r2 = r2.findTreeNode(r13, r14, r3)
                if (r2 != 0) goto L_0x0056
            L_0x004c:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r2 = r0.right
                if (r2 == 0) goto L_0x0057
                java.util.concurrent.ConcurrentHashMap$TreeNode r2 = r2.findTreeNode(r13, r14, r3)
                if (r2 == 0) goto L_0x0057
            L_0x0056:
                return r2
            L_0x0057:
                r2 = r9
            L_0x0058:
                int r4 = tieBreakOrder(r14, r4)
                goto L_0x001e
            L_0x005d:
                r10 = r5
            L_0x005e:
                if (r10 > 0) goto L_0x0063
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r4 = r0.left
                goto L_0x0065
            L_0x0063:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r4 = r0.right
            L_0x0065:
                if (r4 != 0) goto L_0x009c
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r2 = r12.first
                java.util.concurrent.ConcurrentHashMap$TreeNode r11 = new java.util.concurrent.ConcurrentHashMap$TreeNode
                r3 = r11
                r4 = r13
                r5 = r14
                r6 = r15
                r7 = r2
                r8 = r0
                r3.<init>(r4, r5, r6, r7, r8)
                r12.first = r11
                if (r2 == 0) goto L_0x007a
                r2.prev = r11
            L_0x007a:
                if (r10 > 0) goto L_0x007f
                r0.left = r11
                goto L_0x0081
            L_0x007f:
                r0.right = r11
            L_0x0081:
                boolean r13 = r0.red
                if (r13 != 0) goto L_0x0088
                r11.red = r9
                goto L_0x0096
            L_0x0088:
                r12.lockRoot()
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r13 = r12.root     // Catch:{ all -> 0x0097 }
                java.util.concurrent.ConcurrentHashMap$TreeNode r13 = balanceInsertion(r13, r11)     // Catch:{ all -> 0x0097 }
                r12.root = r13     // Catch:{ all -> 0x0097 }
                r12.unlockRoot()
            L_0x0096:
                return r1
            L_0x0097:
                r13 = move-exception
                r12.unlockRoot()
                throw r13
            L_0x009c:
                r0 = r4
                goto L_0x0005
            L_0x009f:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.TreeBin.putTreeVal(int, java.lang.Object, java.lang.Object):java.util.concurrent.ConcurrentHashMap$TreeNode");
        }

        /* JADX INFO: finally extract failed */
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x0091 A[Catch:{ all -> 0x00cd }] */
        /* JADX WARNING: Removed duplicated region for block: B:70:0x00ac A[Catch:{ all -> 0x00cd }] */
        /* JADX WARNING: Removed duplicated region for block: B:71:0x00ad A[Catch:{ all -> 0x00cd }] */
        /* JADX WARNING: Removed duplicated region for block: B:78:0x00bd A[Catch:{ all -> 0x00cd }] */
        /* JADX WARNING: Removed duplicated region for block: B:79:0x00c0 A[Catch:{ all -> 0x00cd }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean removeTreeNode(java.util.concurrent.ConcurrentHashMap.TreeNode<K, V> r10) {
            /*
                r9 = this;
                java.util.concurrent.ConcurrentHashMap$Node r0 = r10.next
                java.util.concurrent.ConcurrentHashMap$TreeNode r0 = (java.util.concurrent.ConcurrentHashMap.TreeNode) r0
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r1 = r10.prev
                if (r1 != 0) goto L_0x000b
                r9.first = r0
                goto L_0x000d
            L_0x000b:
                r1.next = r0
            L_0x000d:
                if (r0 == 0) goto L_0x0011
                r0.prev = r1
            L_0x0011:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r0 = r9.first
                r1 = 1
                r2 = 0
                if (r0 != 0) goto L_0x001a
                r9.root = r2
                return r1
            L_0x001a:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r0 = r9.root
                if (r0 == 0) goto L_0x00d2
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r3 = r0.right
                if (r3 == 0) goto L_0x00d2
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r3 = r0.left
                if (r3 == 0) goto L_0x00d2
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r3 = r3.left
                if (r3 != 0) goto L_0x002c
                goto L_0x00d2
            L_0x002c:
                r9.lockRoot()
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r1 = r10.left     // Catch:{ all -> 0x00cd }
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r3 = r10.right     // Catch:{ all -> 0x00cd }
                if (r1 == 0) goto L_0x0087
                if (r3 == 0) goto L_0x0087
                r4 = r3
            L_0x0038:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r5 = r4.left     // Catch:{ all -> 0x00cd }
                if (r5 == 0) goto L_0x003e
                r4 = r5
                goto L_0x0038
            L_0x003e:
                boolean r5 = r4.red     // Catch:{ all -> 0x00cd }
                boolean r6 = r10.red     // Catch:{ all -> 0x00cd }
                r4.red = r6     // Catch:{ all -> 0x00cd }
                r10.red = r5     // Catch:{ all -> 0x00cd }
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r5 = r4.right     // Catch:{ all -> 0x00cd }
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r6 = r10.parent     // Catch:{ all -> 0x00cd }
                if (r4 != r3) goto L_0x0051
                r10.parent = r4     // Catch:{ all -> 0x00cd }
                r4.right = r10     // Catch:{ all -> 0x00cd }
                goto L_0x0066
            L_0x0051:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r7 = r4.parent     // Catch:{ all -> 0x00cd }
                r10.parent = r7     // Catch:{ all -> 0x00cd }
                if (r7 == 0) goto L_0x0060
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r8 = r7.left     // Catch:{ all -> 0x00cd }
                if (r4 != r8) goto L_0x005e
                r7.left = r10     // Catch:{ all -> 0x00cd }
                goto L_0x0060
            L_0x005e:
                r7.right = r10     // Catch:{ all -> 0x00cd }
            L_0x0060:
                r4.right = r3     // Catch:{ all -> 0x00cd }
                if (r3 == 0) goto L_0x0066
                r3.parent = r4     // Catch:{ all -> 0x00cd }
            L_0x0066:
                r10.left = r2     // Catch:{ all -> 0x00cd }
                r10.right = r5     // Catch:{ all -> 0x00cd }
                if (r5 == 0) goto L_0x006e
                r5.parent = r10     // Catch:{ all -> 0x00cd }
            L_0x006e:
                r4.left = r1     // Catch:{ all -> 0x00cd }
                if (r1 == 0) goto L_0x0074
                r1.parent = r4     // Catch:{ all -> 0x00cd }
            L_0x0074:
                r4.parent = r6     // Catch:{ all -> 0x00cd }
                if (r6 != 0) goto L_0x007a
                r0 = r4
                goto L_0x0083
            L_0x007a:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r1 = r6.left     // Catch:{ all -> 0x00cd }
                if (r10 != r1) goto L_0x0081
                r6.left = r4     // Catch:{ all -> 0x00cd }
                goto L_0x0083
            L_0x0081:
                r6.right = r4     // Catch:{ all -> 0x00cd }
            L_0x0083:
                if (r5 == 0) goto L_0x008e
                r1 = r5
                goto L_0x008f
            L_0x0087:
                if (r1 == 0) goto L_0x008a
                goto L_0x008f
            L_0x008a:
                if (r3 == 0) goto L_0x008e
                r1 = r3
                goto L_0x008f
            L_0x008e:
                r1 = r10
            L_0x008f:
                if (r1 == r10) goto L_0x00a8
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r3 = r10.parent     // Catch:{ all -> 0x00cd }
                r1.parent = r3     // Catch:{ all -> 0x00cd }
                if (r3 != 0) goto L_0x0099
                r0 = r1
                goto L_0x00a2
            L_0x0099:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r4 = r3.left     // Catch:{ all -> 0x00cd }
                if (r10 != r4) goto L_0x00a0
                r3.left = r1     // Catch:{ all -> 0x00cd }
                goto L_0x00a2
            L_0x00a0:
                r3.right = r1     // Catch:{ all -> 0x00cd }
            L_0x00a2:
                r10.parent = r2     // Catch:{ all -> 0x00cd }
                r10.right = r2     // Catch:{ all -> 0x00cd }
                r10.left = r2     // Catch:{ all -> 0x00cd }
            L_0x00a8:
                boolean r3 = r10.red     // Catch:{ all -> 0x00cd }
                if (r3 == 0) goto L_0x00ad
                goto L_0x00b1
            L_0x00ad:
                java.util.concurrent.ConcurrentHashMap$TreeNode r0 = balanceDeletion(r0, r1)     // Catch:{ all -> 0x00cd }
            L_0x00b1:
                r9.root = r0     // Catch:{ all -> 0x00cd }
                if (r10 != r1) goto L_0x00c8
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r0 = r10.parent     // Catch:{ all -> 0x00cd }
                if (r0 == 0) goto L_0x00c8
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r1 = r0.left     // Catch:{ all -> 0x00cd }
                if (r10 != r1) goto L_0x00c0
                r0.left = r2     // Catch:{ all -> 0x00cd }
                goto L_0x00c6
            L_0x00c0:
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r1 = r0.right     // Catch:{ all -> 0x00cd }
                if (r10 != r1) goto L_0x00c6
                r0.right = r2     // Catch:{ all -> 0x00cd }
            L_0x00c6:
                r10.parent = r2     // Catch:{ all -> 0x00cd }
            L_0x00c8:
                r9.unlockRoot()
                r9 = 0
                return r9
            L_0x00cd:
                r10 = move-exception
                r9.unlockRoot()
                throw r10
            L_0x00d2:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.TreeBin.removeTreeNode(java.util.concurrent.ConcurrentHashMap$TreeNode):boolean");
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

    static final class TableStack<K, V> {
        int index;
        int length;
        TableStack<K, V> next;
        Node<K, V>[] tab;

        TableStack() {
        }
    }

    static class Traverser<K, V> {
        int baseIndex;
        int baseLimit;
        final int baseSize;
        int index;
        Node<K, V> next = null;
        TableStack<K, V> spare;
        TableStack<K, V> stack;
        Node<K, V>[] tab;

        Traverser(Node<K, V>[] nodeArr, int i, int i2, int i3) {
            this.tab = nodeArr;
            this.baseSize = i;
            this.index = i2;
            this.baseIndex = i2;
            this.baseLimit = i3;
        }

        /* access modifiers changed from: package-private */
        public final Node<K, V> advance() {
            Node<K, V> node;
            Node<K, V>[] nodeArr;
            int length;
            int i;
            Node<K, V> node2 = this.next;
            if (node2 != null) {
                node2 = node2.next;
            }
            while (node == null) {
                if (this.baseIndex >= this.baseLimit || (nodeArr = this.tab) == null || (length = nodeArr.length) <= (i = this.index) || i < 0) {
                    this.next = null;
                    return null;
                }
                Node<K, V> tabAt = ConcurrentHashMap.tabAt(nodeArr, i);
                if (tabAt == null || tabAt.hash >= 0) {
                    node = tabAt;
                } else if (tabAt instanceof ForwardingNode) {
                    this.tab = ((ForwardingNode) tabAt).nextTable;
                    pushState(nodeArr, i, length);
                    node = null;
                } else {
                    node = tabAt instanceof TreeBin ? ((TreeBin) tabAt).first : null;
                }
                if (this.stack != null) {
                    recoverState(length);
                } else {
                    int i2 = i + this.baseSize;
                    this.index = i2;
                    if (i2 >= length) {
                        int i3 = this.baseIndex + 1;
                        this.baseIndex = i3;
                        this.index = i3;
                    }
                }
            }
            this.next = node;
            return node;
        }

        private void pushState(Node<K, V>[] nodeArr, int i, int i2) {
            TableStack<K, V> tableStack = this.spare;
            if (tableStack != null) {
                this.spare = tableStack.next;
            } else {
                tableStack = new TableStack<>();
            }
            tableStack.tab = nodeArr;
            tableStack.length = i2;
            tableStack.index = i;
            tableStack.next = this.stack;
            this.stack = tableStack;
        }

        private void recoverState(int i) {
            TableStack<K, V> tableStack;
            while (true) {
                tableStack = this.stack;
                if (tableStack == null) {
                    break;
                }
                int i2 = this.index;
                int i3 = tableStack.length;
                int i4 = i2 + i3;
                this.index = i4;
                if (i4 < i) {
                    break;
                }
                this.index = tableStack.index;
                this.tab = tableStack.tab;
                tableStack.tab = null;
                TableStack<K, V> tableStack2 = tableStack.next;
                tableStack.next = this.spare;
                this.stack = tableStack2;
                this.spare = tableStack;
                i = i3;
            }
            if (tableStack == null) {
                int i5 = this.index + this.baseSize;
                this.index = i5;
                if (i5 >= i) {
                    int i6 = this.baseIndex + 1;
                    this.baseIndex = i6;
                    this.index = i6;
                }
            }
        }
    }

    static class BaseIterator<K, V> extends Traverser<K, V> {
        Node<K, V> lastReturned;
        final ConcurrentHashMap<K, V> map;

        BaseIterator(Node<K, V>[] nodeArr, int i, int i2, int i3, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i, i2, i3);
            this.map = concurrentHashMap;
            advance();
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        public final boolean hasMoreElements() {
            return this.next != null;
        }

        public final void remove() {
            Node<K, V> node = this.lastReturned;
            if (node != null) {
                this.lastReturned = null;
                this.map.replaceNode(node.key, null, (Object) null);
                return;
            }
            throw new IllegalStateException();
        }
    }

    static final class KeyIterator<K, V> extends BaseIterator<K, V> implements Iterator<K>, Enumeration<K> {
        KeyIterator(Node<K, V>[] nodeArr, int i, int i2, int i3, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i, i2, i3, concurrentHashMap);
        }

        public final K next() {
            Node node = this.next;
            if (node != null) {
                K k = node.key;
                this.lastReturned = node;
                advance();
                return k;
            }
            throw new NoSuchElementException();
        }

        public final K nextElement() {
            return next();
        }
    }

    static final class ValueIterator<K, V> extends BaseIterator<K, V> implements Iterator<V>, Enumeration<V> {
        ValueIterator(Node<K, V>[] nodeArr, int i, int i2, int i3, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i, i2, i3, concurrentHashMap);
        }

        public final V next() {
            Node node = this.next;
            if (node != null) {
                V v = node.val;
                this.lastReturned = node;
                advance();
                return v;
            }
            throw new NoSuchElementException();
        }

        public final V nextElement() {
            return next();
        }
    }

    static final class EntryIterator<K, V> extends BaseIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        EntryIterator(Node<K, V>[] nodeArr, int i, int i2, int i3, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i, i2, i3, concurrentHashMap);
        }

        public final Map.Entry<K, V> next() {
            Node node = this.next;
            if (node != null) {
                K k = node.key;
                V v = node.val;
                this.lastReturned = node;
                advance();
                return new MapEntry(k, v, this.map);
            }
            throw new NoSuchElementException();
        }
    }

    static final class MapEntry<K, V> implements Map.Entry<K, V> {
        final K key;
        final ConcurrentHashMap<K, V> map;
        V val;

        MapEntry(K k, V v, ConcurrentHashMap<K, V> concurrentHashMap) {
            this.key = k;
            this.val = v;
            this.map = concurrentHashMap;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.val;
        }

        public int hashCode() {
            return this.val.hashCode() ^ this.key.hashCode();
        }

        public String toString() {
            return Helpers.mapEntryToString(this.key, this.val);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
            r2 = r2.val;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r3 = (java.util.Map.Entry) r3;
            r0 = r3.getKey();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
            r3 = r3.getValue();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0012, code lost:
            r1 = r2.key;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r3) {
            /*
                r2 = this;
                boolean r0 = r3 instanceof java.util.Map.Entry
                if (r0 == 0) goto L_0x0028
                java.util.Map$Entry r3 = (java.util.Map.Entry) r3
                java.lang.Object r0 = r3.getKey()
                if (r0 == 0) goto L_0x0028
                java.lang.Object r3 = r3.getValue()
                if (r3 == 0) goto L_0x0028
                K r1 = r2.key
                if (r0 == r1) goto L_0x001c
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L_0x0028
            L_0x001c:
                V r2 = r2.val
                if (r3 == r2) goto L_0x0026
                boolean r2 = r3.equals(r2)
                if (r2 == 0) goto L_0x0028
            L_0x0026:
                r2 = 1
                goto L_0x0029
            L_0x0028:
                r2 = 0
            L_0x0029:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.MapEntry.equals(java.lang.Object):boolean");
        }

        public V setValue(V v) {
            v.getClass();
            V v2 = this.val;
            this.val = v;
            this.map.put(this.key, v);
            return v2;
        }
    }

    static final class KeySpliterator<K, V> extends Traverser<K, V> implements Spliterator<K> {
        long est;

        public int characteristics() {
            return 4353;
        }

        KeySpliterator(Node<K, V>[] nodeArr, int i, int i2, int i3, long j) {
            super(nodeArr, i, i2, i3);
            this.est = j;
        }

        public KeySpliterator<K, V> trySplit() {
            int i = this.baseIndex;
            int i2 = this.baseLimit;
            int i3 = (i + i2) >>> 1;
            if (i3 <= i) {
                return null;
            }
            Node[] nodeArr = this.tab;
            int i4 = this.baseSize;
            this.baseLimit = i3;
            long j = this.est >>> 1;
            this.est = j;
            return new KeySpliterator<>(nodeArr, i4, i3, i2, j);
        }

        public void forEachRemaining(Consumer<? super K> consumer) {
            consumer.getClass();
            while (true) {
                Node advance = advance();
                if (advance != null) {
                    consumer.accept(advance.key);
                } else {
                    return;
                }
            }
        }

        public boolean tryAdvance(Consumer<? super K> consumer) {
            consumer.getClass();
            Node advance = advance();
            if (advance == null) {
                return false;
            }
            consumer.accept(advance.key);
            return true;
        }

        public long estimateSize() {
            return this.est;
        }
    }

    static final class ValueSpliterator<K, V> extends Traverser<K, V> implements Spliterator<V> {
        long est;

        public int characteristics() {
            return 4352;
        }

        ValueSpliterator(Node<K, V>[] nodeArr, int i, int i2, int i3, long j) {
            super(nodeArr, i, i2, i3);
            this.est = j;
        }

        public ValueSpliterator<K, V> trySplit() {
            int i = this.baseIndex;
            int i2 = this.baseLimit;
            int i3 = (i + i2) >>> 1;
            if (i3 <= i) {
                return null;
            }
            Node[] nodeArr = this.tab;
            int i4 = this.baseSize;
            this.baseLimit = i3;
            long j = this.est >>> 1;
            this.est = j;
            return new ValueSpliterator<>(nodeArr, i4, i3, i2, j);
        }

        public void forEachRemaining(Consumer<? super V> consumer) {
            consumer.getClass();
            while (true) {
                Node advance = advance();
                if (advance != null) {
                    consumer.accept(advance.val);
                } else {
                    return;
                }
            }
        }

        public boolean tryAdvance(Consumer<? super V> consumer) {
            consumer.getClass();
            Node advance = advance();
            if (advance == null) {
                return false;
            }
            consumer.accept(advance.val);
            return true;
        }

        public long estimateSize() {
            return this.est;
        }
    }

    static final class EntrySpliterator<K, V> extends Traverser<K, V> implements Spliterator<Map.Entry<K, V>> {
        long est;
        final ConcurrentHashMap<K, V> map;

        public int characteristics() {
            return 4353;
        }

        EntrySpliterator(Node<K, V>[] nodeArr, int i, int i2, int i3, long j, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i, i2, i3);
            this.map = concurrentHashMap;
            this.est = j;
        }

        public EntrySpliterator<K, V> trySplit() {
            int i = this.baseIndex;
            int i2 = this.baseLimit;
            int i3 = (i + i2) >>> 1;
            if (i3 <= i) {
                return null;
            }
            Node[] nodeArr = this.tab;
            int i4 = this.baseSize;
            this.baseLimit = i3;
            long j = this.est >>> 1;
            this.est = j;
            return new EntrySpliterator<>(nodeArr, i4, i3, i2, j, this.map);
        }

        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            while (true) {
                Node advance = advance();
                if (advance != null) {
                    consumer.accept(new MapEntry(advance.key, advance.val, this.map));
                } else {
                    return;
                }
            }
        }

        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            Node advance = advance();
            if (advance == null) {
                return false;
            }
            consumer.accept(new MapEntry(advance.key, advance.val, this.map));
            return true;
        }

        public long estimateSize() {
            return this.est;
        }
    }

    /* access modifiers changed from: package-private */
    public final int batchFor(long j) {
        if (j == Long.MAX_VALUE) {
            return 0;
        }
        long sumCount = sumCount();
        if (sumCount <= 1 || sumCount < j) {
            return 0;
        }
        int commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism() << 2;
        if (j <= 0) {
            return commonPoolParallelism;
        }
        long j2 = sumCount / j;
        return j2 >= ((long) commonPoolParallelism) ? commonPoolParallelism : (int) j2;
    }

    public void forEach(long j, BiConsumer<? super K, ? super V> biConsumer) {
        biConsumer.getClass();
        new ForEachMappingTask((BulkTask) null, batchFor(j), 0, 0, this.table, biConsumer).invoke();
    }

    public <U> void forEach(long j, BiFunction<? super K, ? super V, ? extends U> biFunction, Consumer<? super U> consumer) {
        if (biFunction == null || consumer == null) {
            throw null;
        }
        new ForEachTransformedMappingTask((BulkTask) null, batchFor(j), 0, 0, this.table, biFunction, consumer).invoke();
    }

    public <U> U search(long j, BiFunction<? super K, ? super V, ? extends U> biFunction) {
        biFunction.getClass();
        return new SearchMappingsTask((BulkTask) null, batchFor(j), 0, 0, this.table, biFunction, new AtomicReference()).invoke();
    }

    public <U> U reduce(long j, BiFunction<? super K, ? super V, ? extends U> biFunction, BiFunction<? super U, ? super U, ? extends U> biFunction2) {
        if (biFunction != null && biFunction2 != null) {
            return new MapReduceMappingsTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceMappingsTask) null, biFunction, biFunction2).invoke();
        }
        throw null;
    }

    public double reduceToDouble(long j, ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction, double d, DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleBiFunction != null && doubleBinaryOperator != null) {
            return ((Double) new MapReduceMappingsToDoubleTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceMappingsToDoubleTask) null, toDoubleBiFunction, d, doubleBinaryOperator).invoke()).doubleValue();
        }
        throw null;
    }

    public long reduceToLong(long j, ToLongBiFunction<? super K, ? super V> toLongBiFunction, long j2, LongBinaryOperator longBinaryOperator) {
        if (toLongBiFunction != null && longBinaryOperator != null) {
            return ((Long) new MapReduceMappingsToLongTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceMappingsToLongTask) null, toLongBiFunction, j2, longBinaryOperator).invoke()).longValue();
        }
        throw null;
    }

    public int reduceToInt(long j, ToIntBiFunction<? super K, ? super V> toIntBiFunction, int i, IntBinaryOperator intBinaryOperator) {
        if (toIntBiFunction != null && intBinaryOperator != null) {
            return ((Integer) new MapReduceMappingsToIntTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceMappingsToIntTask) null, toIntBiFunction, i, intBinaryOperator).invoke()).intValue();
        }
        throw null;
    }

    public void forEachKey(long j, Consumer<? super K> consumer) {
        consumer.getClass();
        new ForEachKeyTask((BulkTask) null, batchFor(j), 0, 0, this.table, consumer).invoke();
    }

    public <U> void forEachKey(long j, Function<? super K, ? extends U> function, Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw null;
        }
        new ForEachTransformedKeyTask((BulkTask) null, batchFor(j), 0, 0, this.table, function, consumer).invoke();
    }

    public <U> U searchKeys(long j, Function<? super K, ? extends U> function) {
        function.getClass();
        return new SearchKeysTask((BulkTask) null, batchFor(j), 0, 0, this.table, function, new AtomicReference()).invoke();
    }

    public K reduceKeys(long j, BiFunction<? super K, ? super K, ? extends K> biFunction) {
        biFunction.getClass();
        return new ReduceKeysTask((BulkTask) null, batchFor(j), 0, 0, this.table, (ReduceKeysTask) null, biFunction).invoke();
    }

    public <U> U reduceKeys(long j, Function<? super K, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function != null && biFunction != null) {
            return new MapReduceKeysTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceKeysTask) null, function, biFunction).invoke();
        }
        throw null;
    }

    public double reduceKeysToDouble(long j, ToDoubleFunction<? super K> toDoubleFunction, double d, DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction != null && doubleBinaryOperator != null) {
            return ((Double) new MapReduceKeysToDoubleTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceKeysToDoubleTask) null, toDoubleFunction, d, doubleBinaryOperator).invoke()).doubleValue();
        }
        throw null;
    }

    public long reduceKeysToLong(long j, ToLongFunction<? super K> toLongFunction, long j2, LongBinaryOperator longBinaryOperator) {
        if (toLongFunction != null && longBinaryOperator != null) {
            return ((Long) new MapReduceKeysToLongTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceKeysToLongTask) null, toLongFunction, j2, longBinaryOperator).invoke()).longValue();
        }
        throw null;
    }

    public int reduceKeysToInt(long j, ToIntFunction<? super K> toIntFunction, int i, IntBinaryOperator intBinaryOperator) {
        if (toIntFunction != null && intBinaryOperator != null) {
            return ((Integer) new MapReduceKeysToIntTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceKeysToIntTask) null, toIntFunction, i, intBinaryOperator).invoke()).intValue();
        }
        throw null;
    }

    public void forEachValue(long j, Consumer<? super V> consumer) {
        consumer.getClass();
        new ForEachValueTask((BulkTask) null, batchFor(j), 0, 0, this.table, consumer).invoke();
    }

    public <U> void forEachValue(long j, Function<? super V, ? extends U> function, Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw null;
        }
        new ForEachTransformedValueTask((BulkTask) null, batchFor(j), 0, 0, this.table, function, consumer).invoke();
    }

    public <U> U searchValues(long j, Function<? super V, ? extends U> function) {
        function.getClass();
        return new SearchValuesTask((BulkTask) null, batchFor(j), 0, 0, this.table, function, new AtomicReference()).invoke();
    }

    public V reduceValues(long j, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        biFunction.getClass();
        return new ReduceValuesTask((BulkTask) null, batchFor(j), 0, 0, this.table, (ReduceValuesTask) null, biFunction).invoke();
    }

    public <U> U reduceValues(long j, Function<? super V, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function != null && biFunction != null) {
            return new MapReduceValuesTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceValuesTask) null, function, biFunction).invoke();
        }
        throw null;
    }

    public double reduceValuesToDouble(long j, ToDoubleFunction<? super V> toDoubleFunction, double d, DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction != null && doubleBinaryOperator != null) {
            return ((Double) new MapReduceValuesToDoubleTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceValuesToDoubleTask) null, toDoubleFunction, d, doubleBinaryOperator).invoke()).doubleValue();
        }
        throw null;
    }

    public long reduceValuesToLong(long j, ToLongFunction<? super V> toLongFunction, long j2, LongBinaryOperator longBinaryOperator) {
        if (toLongFunction != null && longBinaryOperator != null) {
            return ((Long) new MapReduceValuesToLongTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceValuesToLongTask) null, toLongFunction, j2, longBinaryOperator).invoke()).longValue();
        }
        throw null;
    }

    public int reduceValuesToInt(long j, ToIntFunction<? super V> toIntFunction, int i, IntBinaryOperator intBinaryOperator) {
        if (toIntFunction != null && intBinaryOperator != null) {
            return ((Integer) new MapReduceValuesToIntTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceValuesToIntTask) null, toIntFunction, i, intBinaryOperator).invoke()).intValue();
        }
        throw null;
    }

    public void forEachEntry(long j, Consumer<? super Map.Entry<K, V>> consumer) {
        consumer.getClass();
        new ForEachEntryTask((BulkTask) null, batchFor(j), 0, 0, this.table, consumer).invoke();
    }

    public <U> void forEachEntry(long j, Function<Map.Entry<K, V>, ? extends U> function, Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw null;
        }
        new ForEachTransformedEntryTask((BulkTask) null, batchFor(j), 0, 0, this.table, function, consumer).invoke();
    }

    public <U> U searchEntries(long j, Function<Map.Entry<K, V>, ? extends U> function) {
        function.getClass();
        return new SearchEntriesTask((BulkTask) null, batchFor(j), 0, 0, this.table, function, new AtomicReference()).invoke();
    }

    public Map.Entry<K, V> reduceEntries(long j, BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFunction) {
        biFunction.getClass();
        return (Map.Entry) new ReduceEntriesTask((BulkTask) null, batchFor(j), 0, 0, this.table, (ReduceEntriesTask) null, biFunction).invoke();
    }

    public <U> U reduceEntries(long j, Function<Map.Entry<K, V>, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function != null && biFunction != null) {
            return new MapReduceEntriesTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceEntriesTask) null, function, biFunction).invoke();
        }
        throw null;
    }

    public double reduceEntriesToDouble(long j, ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction, double d, DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction != null && doubleBinaryOperator != null) {
            return ((Double) new MapReduceEntriesToDoubleTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceEntriesToDoubleTask) null, toDoubleFunction, d, doubleBinaryOperator).invoke()).doubleValue();
        }
        throw null;
    }

    public long reduceEntriesToLong(long j, ToLongFunction<Map.Entry<K, V>> toLongFunction, long j2, LongBinaryOperator longBinaryOperator) {
        if (toLongFunction != null && longBinaryOperator != null) {
            return ((Long) new MapReduceEntriesToLongTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceEntriesToLongTask) null, toLongFunction, j2, longBinaryOperator).invoke()).longValue();
        }
        throw null;
    }

    public int reduceEntriesToInt(long j, ToIntFunction<Map.Entry<K, V>> toIntFunction, int i, IntBinaryOperator intBinaryOperator) {
        if (toIntFunction != null && intBinaryOperator != null) {
            return ((Integer) new MapReduceEntriesToIntTask((BulkTask) null, batchFor(j), 0, 0, this.table, (MapReduceEntriesToIntTask) null, toIntFunction, i, intBinaryOperator).invoke()).intValue();
        }
        throw null;
    }

    static abstract class CollectionView<K, V, E> implements Collection<E>, Serializable {
        private static final String OOME_MSG = "Required array size too large";
        private static final long serialVersionUID = 7249069246763182397L;
        final ConcurrentHashMap<K, V> map;

        public abstract boolean contains(Object obj);

        public abstract Iterator<E> iterator();

        public abstract boolean remove(Object obj);

        CollectionView(ConcurrentHashMap<K, V> concurrentHashMap) {
            this.map = concurrentHashMap;
        }

        public ConcurrentHashMap<K, V> getMap() {
            return this.map;
        }

        public final void clear() {
            this.map.clear();
        }

        public final int size() {
            return this.map.size();
        }

        public final boolean isEmpty() {
            return this.map.isEmpty();
        }

        public final Object[] toArray() {
            long mappingCount = this.map.mappingCount();
            if (mappingCount <= 2147483639) {
                int i = (int) mappingCount;
                Object[] objArr = new Object[i];
                Iterator it = iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    Object next = it.next();
                    if (i2 == i) {
                        int i3 = ConcurrentHashMap.MAX_ARRAY_SIZE;
                        if (i < ConcurrentHashMap.MAX_ARRAY_SIZE) {
                            if (i < 1073741819) {
                                i3 = (i >>> 1) + 1 + i;
                            }
                            objArr = Arrays.copyOf((T[]) objArr, i3);
                            i = i3;
                        } else {
                            throw new OutOfMemoryError(OOME_MSG);
                        }
                    }
                    objArr[i2] = next;
                    i2++;
                }
                if (i2 == i) {
                    return objArr;
                }
                return Arrays.copyOf((T[]) objArr, i2);
            }
            throw new OutOfMemoryError(OOME_MSG);
        }

        public final <T> T[] toArray(T[] tArr) {
            T[] tArr2;
            long mappingCount = this.map.mappingCount();
            if (mappingCount <= 2147483639) {
                int i = (int) mappingCount;
                if (tArr.length >= i) {
                    tArr2 = tArr;
                } else {
                    tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i);
                }
                int length = tArr2.length;
                Iterator it = iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    T next = it.next();
                    if (i2 == length) {
                        int i3 = ConcurrentHashMap.MAX_ARRAY_SIZE;
                        if (length < ConcurrentHashMap.MAX_ARRAY_SIZE) {
                            if (length < 1073741819) {
                                i3 = (length >>> 1) + 1 + length;
                            }
                            tArr2 = Arrays.copyOf(tArr2, i3);
                            length = i3;
                        } else {
                            throw new OutOfMemoryError(OOME_MSG);
                        }
                    }
                    tArr2[i2] = next;
                    i2++;
                }
                if (tArr == tArr2 && i2 < length) {
                    tArr2[i2] = null;
                    return tArr2;
                } else if (i2 == length) {
                    return tArr2;
                } else {
                    return Arrays.copyOf(tArr2, i2);
                }
            } else {
                throw new OutOfMemoryError(OOME_MSG);
            }
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
            Iterator it = iterator();
            if (it.hasNext()) {
                while (true) {
                    Object next = it.next();
                    if (next == this) {
                        next = "(this Collection)";
                    }
                    sb.append(next);
                    if (!it.hasNext()) {
                        break;
                    }
                    sb.append(", ");
                }
            }
            sb.append(']');
            return sb.toString();
        }

        /* JADX WARNING: Removed duplicated region for block: B:4:0x000c  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean containsAll(java.util.Collection<?> r2) {
            /*
                r1 = this;
                if (r2 == r1) goto L_0x001a
                java.util.Iterator r2 = r2.iterator()
            L_0x0006:
                boolean r0 = r2.hasNext()
                if (r0 == 0) goto L_0x001a
                java.lang.Object r0 = r2.next()
                if (r0 == 0) goto L_0x0018
                boolean r0 = r1.contains(r0)
                if (r0 != 0) goto L_0x0006
            L_0x0018:
                r1 = 0
                return r1
            L_0x001a:
                r1 = 1
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.CollectionView.containsAll(java.util.Collection):boolean");
        }

        public boolean removeAll(Collection<?> collection) {
            collection.getClass();
            Node<K, V>[] nodeArr = this.map.table;
            boolean z = false;
            if (nodeArr == null) {
                return false;
            }
            if (!(collection instanceof Set) || collection.size() <= nodeArr.length) {
                for (Object remove : collection) {
                    z |= remove(remove);
                }
            } else {
                Iterator it = iterator();
                while (it.hasNext()) {
                    if (collection.contains(it.next())) {
                        it.remove();
                        z = true;
                    }
                }
            }
            return z;
        }

        public final boolean retainAll(Collection<?> collection) {
            collection.getClass();
            Iterator it = iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (!collection.contains(it.next())) {
                    it.remove();
                    z = true;
                }
            }
            return z;
        }
    }

    public static class KeySetView<K, V> extends CollectionView<K, V, K> implements Set<K>, Serializable {
        private static final long serialVersionUID = 7249069246763182397L;
        private final V value;

        public /* bridge */ /* synthetic */ ConcurrentHashMap getMap() {
            return super.getMap();
        }

        public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
            return super.removeAll(collection);
        }

        KeySetView(ConcurrentHashMap<K, V> concurrentHashMap, V v) {
            super(concurrentHashMap);
            this.value = v;
        }

        public V getMappedValue() {
            return this.value;
        }

        public boolean contains(Object obj) {
            return this.map.containsKey(obj);
        }

        public boolean remove(Object obj) {
            return this.map.remove(obj) != null;
        }

        public Iterator<K> iterator() {
            ConcurrentHashMap concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new KeyIterator(nodeArr, length, 0, length, concurrentHashMap);
        }

        public boolean add(K k) {
            V v = this.value;
            if (v != null) {
                return this.map.putVal(k, v, true) == null;
            }
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends K> collection) {
            V v = this.value;
            if (v != null) {
                boolean z = false;
                for (Object putVal : collection) {
                    if (this.map.putVal(putVal, v, true) == null) {
                        z = true;
                    }
                }
                return z;
            }
            throw new UnsupportedOperationException();
        }

        public int hashCode() {
            Iterator it = iterator();
            int i = 0;
            while (it.hasNext()) {
                i += it.next().hashCode();
            }
            return i;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r2 = (java.util.Set) r2;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r2) {
            /*
                r1 = this;
                boolean r0 = r2 instanceof java.util.Set
                if (r0 == 0) goto L_0x0016
                java.util.Set r2 = (java.util.Set) r2
                if (r2 == r1) goto L_0x0014
                boolean r0 = r1.containsAll(r2)
                if (r0 == 0) goto L_0x0016
                boolean r1 = r2.containsAll(r1)
                if (r1 == 0) goto L_0x0016
            L_0x0014:
                r1 = 1
                goto L_0x0017
            L_0x0016:
                r1 = 0
            L_0x0017:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.KeySetView.equals(java.lang.Object):boolean");
        }

        public Spliterator<K> spliterator() {
            ConcurrentHashMap concurrentHashMap = this.map;
            long sumCount = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            long j = 0;
            if (sumCount >= 0) {
                j = sumCount;
            }
            return new KeySpliterator(nodeArr, length, 0, length, j);
        }

        public void forEach(Consumer<? super K> consumer) {
            consumer.getClass();
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node advance = traverser.advance();
                    if (advance != null) {
                        consumer.accept(advance.key);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    static final class ValuesView<K, V> extends CollectionView<K, V, V> implements Collection<V>, Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        ValuesView(ConcurrentHashMap<K, V> concurrentHashMap) {
            super(concurrentHashMap);
        }

        public final boolean contains(Object obj) {
            return this.map.containsValue(obj);
        }

        public final boolean remove(Object obj) {
            if (obj == null) {
                return false;
            }
            Iterator it = iterator();
            while (it.hasNext()) {
                if (obj.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        public final Iterator<V> iterator() {
            ConcurrentHashMap concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new ValueIterator(nodeArr, length, 0, length, concurrentHashMap);
        }

        public final boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        public final boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            collection.getClass();
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

        public boolean removeIf(Predicate<? super V> predicate) {
            return this.map.removeValueIf(predicate);
        }

        public Spliterator<V> spliterator() {
            ConcurrentHashMap concurrentHashMap = this.map;
            long sumCount = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            long j = 0;
            if (sumCount >= 0) {
                j = sumCount;
            }
            return new ValueSpliterator(nodeArr, length, 0, length, j);
        }

        public void forEach(Consumer<? super V> consumer) {
            consumer.getClass();
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node advance = traverser.advance();
                    if (advance != null) {
                        consumer.accept(advance.val);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    static final class EntrySetView<K, V> extends CollectionView<K, V, Map.Entry<K, V>> implements Set<Map.Entry<K, V>>, Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        EntrySetView(ConcurrentHashMap<K, V> concurrentHashMap) {
            super(concurrentHashMap);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
            r1 = r1.map.get((r0 = r2.getKey()));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0014, code lost:
            r2 = (r2 = (java.util.Map.Entry) r2).getValue();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean contains(java.lang.Object r2) {
            /*
                r1 = this;
                boolean r0 = r2 instanceof java.util.Map.Entry
                if (r0 == 0) goto L_0x0024
                java.util.Map$Entry r2 = (java.util.Map.Entry) r2
                java.lang.Object r0 = r2.getKey()
                if (r0 == 0) goto L_0x0024
                java.util.concurrent.ConcurrentHashMap r1 = r1.map
                java.lang.Object r1 = r1.get(r0)
                if (r1 == 0) goto L_0x0024
                java.lang.Object r2 = r2.getValue()
                if (r2 == 0) goto L_0x0024
                if (r2 == r1) goto L_0x0022
                boolean r1 = r2.equals(r1)
                if (r1 == 0) goto L_0x0024
            L_0x0022:
                r1 = 1
                goto L_0x0025
            L_0x0024:
                r1 = 0
            L_0x0025:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.EntrySetView.contains(java.lang.Object):boolean");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r2 = (java.util.Map.Entry) r2;
            r0 = r2.getKey();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
            r2 = r2.getValue();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean remove(java.lang.Object r2) {
            /*
                r1 = this;
                boolean r0 = r2 instanceof java.util.Map.Entry
                if (r0 == 0) goto L_0x001c
                java.util.Map$Entry r2 = (java.util.Map.Entry) r2
                java.lang.Object r0 = r2.getKey()
                if (r0 == 0) goto L_0x001c
                java.lang.Object r2 = r2.getValue()
                if (r2 == 0) goto L_0x001c
                java.util.concurrent.ConcurrentHashMap r1 = r1.map
                boolean r1 = r1.remove(r0, r2)
                if (r1 == 0) goto L_0x001c
                r1 = 1
                goto L_0x001d
            L_0x001c:
                r1 = 0
            L_0x001d:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.EntrySetView.remove(java.lang.Object):boolean");
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            ConcurrentHashMap concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new EntryIterator(nodeArr, length, 0, length, concurrentHashMap);
        }

        public boolean add(Map.Entry<K, V> entry) {
            return this.map.putVal(entry.getKey(), entry.getValue(), false) == null;
        }

        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            boolean z = false;
            for (Map.Entry add : collection) {
                if (add(add)) {
                    z = true;
                }
            }
            return z;
        }

        public boolean removeIf(Predicate<? super Map.Entry<K, V>> predicate) {
            return this.map.removeEntryIf(predicate);
        }

        public final int hashCode() {
            Node<K, V>[] nodeArr = this.map.table;
            int i = 0;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node advance = traverser.advance();
                    if (advance == null) {
                        break;
                    }
                    i += advance.hashCode();
                }
            }
            return i;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r2 = (java.util.Set) r2;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean equals(java.lang.Object r2) {
            /*
                r1 = this;
                boolean r0 = r2 instanceof java.util.Set
                if (r0 == 0) goto L_0x0016
                java.util.Set r2 = (java.util.Set) r2
                if (r2 == r1) goto L_0x0014
                boolean r0 = r1.containsAll(r2)
                if (r0 == 0) goto L_0x0016
                boolean r1 = r2.containsAll(r1)
                if (r1 == 0) goto L_0x0016
            L_0x0014:
                r1 = 1
                goto L_0x0017
            L_0x0016:
                r1 = 0
            L_0x0017:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.EntrySetView.equals(java.lang.Object):boolean");
        }

        public Spliterator<Map.Entry<K, V>> spliterator() {
            ConcurrentHashMap concurrentHashMap = this.map;
            long sumCount = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            long j = 0;
            if (sumCount >= 0) {
                j = sumCount;
            }
            return new EntrySpliterator(nodeArr, length, 0, length, j, concurrentHashMap);
        }

        public void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node advance = traverser.advance();
                    if (advance != null) {
                        consumer.accept(new MapEntry(advance.key, advance.val, this.map));
                    } else {
                        return;
                    }
                }
            }
        }
    }

    static abstract class BulkTask<K, V, R> extends CountedCompleter<R> {
        int baseIndex;
        int baseLimit;
        final int baseSize;
        int batch;
        int index;
        Node<K, V> next;
        TableStack<K, V> spare;
        TableStack<K, V> stack;
        Node<K, V>[] tab;

        BulkTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr) {
            super(bulkTask);
            this.batch = i;
            this.baseIndex = i2;
            this.index = i2;
            this.tab = nodeArr;
            if (nodeArr == null) {
                this.baseLimit = 0;
                this.baseSize = 0;
            } else if (bulkTask == null) {
                int length = nodeArr.length;
                this.baseLimit = length;
                this.baseSize = length;
            } else {
                this.baseLimit = i3;
                this.baseSize = bulkTask.baseSize;
            }
        }

        /* access modifiers changed from: package-private */
        public final Node<K, V> advance() {
            Node<K, V> node;
            Node<K, V>[] nodeArr;
            int length;
            int i;
            Node<K, V> node2 = this.next;
            if (node2 != null) {
                node2 = node2.next;
            }
            while (node == null) {
                if (this.baseIndex >= this.baseLimit || (nodeArr = this.tab) == null || (length = nodeArr.length) <= (i = this.index) || i < 0) {
                    this.next = null;
                    return null;
                }
                Node<K, V> tabAt = ConcurrentHashMap.tabAt(nodeArr, i);
                if (tabAt == null || tabAt.hash >= 0) {
                    node = tabAt;
                } else if (tabAt instanceof ForwardingNode) {
                    this.tab = ((ForwardingNode) tabAt).nextTable;
                    pushState(nodeArr, i, length);
                    node = null;
                } else {
                    node = tabAt instanceof TreeBin ? ((TreeBin) tabAt).first : null;
                }
                if (this.stack != null) {
                    recoverState(length);
                } else {
                    int i2 = i + this.baseSize;
                    this.index = i2;
                    if (i2 >= length) {
                        int i3 = this.baseIndex + 1;
                        this.baseIndex = i3;
                        this.index = i3;
                    }
                }
            }
            this.next = node;
            return node;
        }

        private void pushState(Node<K, V>[] nodeArr, int i, int i2) {
            TableStack<K, V> tableStack = this.spare;
            if (tableStack != null) {
                this.spare = tableStack.next;
            } else {
                tableStack = new TableStack<>();
            }
            tableStack.tab = nodeArr;
            tableStack.length = i2;
            tableStack.index = i;
            tableStack.next = this.stack;
            this.stack = tableStack;
        }

        private void recoverState(int i) {
            TableStack<K, V> tableStack;
            while (true) {
                tableStack = this.stack;
                if (tableStack == null) {
                    break;
                }
                int i2 = this.index;
                int i3 = tableStack.length;
                int i4 = i2 + i3;
                this.index = i4;
                if (i4 < i) {
                    break;
                }
                this.index = tableStack.index;
                this.tab = tableStack.tab;
                tableStack.tab = null;
                TableStack<K, V> tableStack2 = tableStack.next;
                tableStack.next = this.spare;
                this.stack = tableStack2;
                this.spare = tableStack;
                i = i3;
            }
            if (tableStack == null) {
                int i5 = this.index + this.baseSize;
                this.index = i5;
                if (i5 >= i) {
                    int i6 = this.baseIndex + 1;
                    this.baseIndex = i6;
                    this.index = i6;
                }
            }
        }
    }

    static final class ForEachKeyTask<K, V> extends BulkTask<K, V, Void> {
        final Consumer<? super K> action;

        ForEachKeyTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Consumer<? super K> consumer) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.action = consumer;
        }

        public final void compute() {
            Consumer<? super K> consumer = this.action;
            if (consumer != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachKeyTask(this, i4, i3, i2, this.tab, consumer).fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance != null) {
                        consumer.accept(advance.key);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    static final class ForEachValueTask<K, V> extends BulkTask<K, V, Void> {
        final Consumer<? super V> action;

        ForEachValueTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Consumer<? super V> consumer) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.action = consumer;
        }

        public final void compute() {
            Consumer<? super V> consumer = this.action;
            if (consumer != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachValueTask(this, i4, i3, i2, this.tab, consumer).fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance != null) {
                        consumer.accept(advance.val);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    static final class ForEachEntryTask<K, V> extends BulkTask<K, V, Void> {
        final Consumer<? super Map.Entry<K, V>> action;

        ForEachEntryTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Consumer<? super Map.Entry<K, V>> consumer) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.action = consumer;
        }

        public final void compute() {
            Consumer<? super Map.Entry<K, V>> consumer = this.action;
            if (consumer != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachEntryTask(this, i4, i3, i2, this.tab, consumer).fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance != null) {
                        consumer.accept(advance);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    static final class ForEachMappingTask<K, V> extends BulkTask<K, V, Void> {
        final BiConsumer<? super K, ? super V> action;

        ForEachMappingTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, BiConsumer<? super K, ? super V> biConsumer) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.action = biConsumer;
        }

        public final void compute() {
            BiConsumer<? super K, ? super V> biConsumer = this.action;
            if (biConsumer != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachMappingTask(this, i4, i3, i2, this.tab, biConsumer).fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance != null) {
                        biConsumer.accept(advance.key, advance.val);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    static final class ForEachTransformedKeyTask<K, V, U> extends BulkTask<K, V, Void> {
        final Consumer<? super U> action;
        final Function<? super K, ? extends U> transformer;

        ForEachTransformedKeyTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Function<? super K, ? extends U> function, Consumer<? super U> consumer) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.transformer = function;
            this.action = consumer;
        }

        public final void compute() {
            Consumer<? super U> consumer;
            Function<? super K, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachTransformedKeyTask(this, i4, i3, i2, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance != null) {
                        Object apply = function.apply(advance.key);
                        if (apply != null) {
                            consumer.accept(apply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    static final class ForEachTransformedValueTask<K, V, U> extends BulkTask<K, V, Void> {
        final Consumer<? super U> action;
        final Function<? super V, ? extends U> transformer;

        ForEachTransformedValueTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Function<? super V, ? extends U> function, Consumer<? super U> consumer) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.transformer = function;
            this.action = consumer;
        }

        public final void compute() {
            Consumer<? super U> consumer;
            Function<? super V, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachTransformedValueTask(this, i4, i3, i2, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance != null) {
                        Object apply = function.apply(advance.val);
                        if (apply != null) {
                            consumer.accept(apply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    static final class ForEachTransformedEntryTask<K, V, U> extends BulkTask<K, V, Void> {
        final Consumer<? super U> action;
        final Function<Map.Entry<K, V>, ? extends U> transformer;

        ForEachTransformedEntryTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Function<Map.Entry<K, V>, ? extends U> function, Consumer<? super U> consumer) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.transformer = function;
            this.action = consumer;
        }

        public final void compute() {
            Consumer<? super U> consumer;
            Function<Map.Entry<K, V>, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachTransformedEntryTask(this, i4, i3, i2, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance != null) {
                        Object apply = function.apply(advance);
                        if (apply != null) {
                            consumer.accept(apply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    static final class ForEachTransformedMappingTask<K, V, U> extends BulkTask<K, V, Void> {
        final Consumer<? super U> action;
        final BiFunction<? super K, ? super V, ? extends U> transformer;

        ForEachTransformedMappingTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, BiFunction<? super K, ? super V, ? extends U> biFunction, Consumer<? super U> consumer) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.transformer = biFunction;
            this.action = consumer;
        }

        public final void compute() {
            Consumer<? super U> consumer;
            BiFunction<? super K, ? super V, ? extends U> biFunction = this.transformer;
            if (biFunction != null && (consumer = this.action) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachTransformedMappingTask(this, i4, i3, i2, this.tab, biFunction, consumer).fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance != null) {
                        Object apply = biFunction.apply(advance.key, advance.val);
                        if (apply != null) {
                            consumer.accept(apply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    static final class SearchKeysTask<K, V, U> extends BulkTask<K, V, U> {
        final AtomicReference<U> result;
        final Function<? super K, ? extends U> searchFunction;

        SearchKeysTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Function<? super K, ? extends U> function, AtomicReference<U> atomicReference) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.searchFunction = function;
            this.result = atomicReference;
        }

        public final U getRawResult() {
            return this.result.get();
        }

        public final void compute() {
            AtomicReference<U> atomicReference;
            Function<? super K, ? extends U> function = this.searchFunction;
            if (function != null && (atomicReference = this.result) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    } else if (atomicReference.get() == null) {
                        addToPendingCount(1);
                        int i4 = this.batch >>> 1;
                        this.batch = i4;
                        this.baseLimit = i3;
                        new SearchKeysTask(this, i4, i3, i2, this.tab, function, atomicReference).fork();
                    } else {
                        return;
                    }
                }
                while (atomicReference.get() == null) {
                    Node advance = advance();
                    if (advance == null) {
                        propagateCompletion();
                        return;
                    }
                    Object apply = function.apply(advance.key);
                    if (apply != null) {
                        if (atomicReference.compareAndSet(null, apply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    static final class SearchValuesTask<K, V, U> extends BulkTask<K, V, U> {
        final AtomicReference<U> result;
        final Function<? super V, ? extends U> searchFunction;

        SearchValuesTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Function<? super V, ? extends U> function, AtomicReference<U> atomicReference) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.searchFunction = function;
            this.result = atomicReference;
        }

        public final U getRawResult() {
            return this.result.get();
        }

        public final void compute() {
            AtomicReference<U> atomicReference;
            Function<? super V, ? extends U> function = this.searchFunction;
            if (function != null && (atomicReference = this.result) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    } else if (atomicReference.get() == null) {
                        addToPendingCount(1);
                        int i4 = this.batch >>> 1;
                        this.batch = i4;
                        this.baseLimit = i3;
                        new SearchValuesTask(this, i4, i3, i2, this.tab, function, atomicReference).fork();
                    } else {
                        return;
                    }
                }
                while (atomicReference.get() == null) {
                    Node advance = advance();
                    if (advance == null) {
                        propagateCompletion();
                        return;
                    }
                    Object apply = function.apply(advance.val);
                    if (apply != null) {
                        if (atomicReference.compareAndSet(null, apply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    static final class SearchEntriesTask<K, V, U> extends BulkTask<K, V, U> {
        final AtomicReference<U> result;
        final Function<Map.Entry<K, V>, ? extends U> searchFunction;

        SearchEntriesTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, Function<Map.Entry<K, V>, ? extends U> function, AtomicReference<U> atomicReference) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.searchFunction = function;
            this.result = atomicReference;
        }

        public final U getRawResult() {
            return this.result.get();
        }

        public final void compute() {
            AtomicReference<U> atomicReference;
            Function<Map.Entry<K, V>, ? extends U> function = this.searchFunction;
            if (function != null && (atomicReference = this.result) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    } else if (atomicReference.get() == null) {
                        addToPendingCount(1);
                        int i4 = this.batch >>> 1;
                        this.batch = i4;
                        this.baseLimit = i3;
                        new SearchEntriesTask(this, i4, i3, i2, this.tab, function, atomicReference).fork();
                    } else {
                        return;
                    }
                }
                while (atomicReference.get() == null) {
                    Node advance = advance();
                    if (advance == null) {
                        propagateCompletion();
                        return;
                    }
                    Object apply = function.apply(advance);
                    if (apply != null) {
                        if (atomicReference.compareAndSet(null, apply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    static final class SearchMappingsTask<K, V, U> extends BulkTask<K, V, U> {
        final AtomicReference<U> result;
        final BiFunction<? super K, ? super V, ? extends U> searchFunction;

        SearchMappingsTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, BiFunction<? super K, ? super V, ? extends U> biFunction, AtomicReference<U> atomicReference) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.searchFunction = biFunction;
            this.result = atomicReference;
        }

        public final U getRawResult() {
            return this.result.get();
        }

        public final void compute() {
            AtomicReference<U> atomicReference;
            BiFunction<? super K, ? super V, ? extends U> biFunction = this.searchFunction;
            if (biFunction != null && (atomicReference = this.result) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    } else if (atomicReference.get() == null) {
                        addToPendingCount(1);
                        int i4 = this.batch >>> 1;
                        this.batch = i4;
                        this.baseLimit = i3;
                        new SearchMappingsTask(this, i4, i3, i2, this.tab, biFunction, atomicReference).fork();
                    } else {
                        return;
                    }
                }
                while (atomicReference.get() == null) {
                    Node advance = advance();
                    if (advance == null) {
                        propagateCompletion();
                        return;
                    }
                    Object apply = biFunction.apply(advance.key, advance.val);
                    if (apply != null) {
                        if (atomicReference.compareAndSet(null, apply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    static final class ReduceKeysTask<K, V> extends BulkTask<K, V, K> {
        ReduceKeysTask<K, V> nextRight;
        final BiFunction<? super K, ? super K, ? extends K> reducer;
        K result;
        ReduceKeysTask<K, V> rights;

        ReduceKeysTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, ReduceKeysTask<K, V> reduceKeysTask, BiFunction<? super K, ? super K, ? extends K> biFunction) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = reduceKeysTask;
            this.reducer = biFunction;
        }

        public final K getRawResult() {
            return this.result;
        }

        public final void compute() {
            BiFunction<? super K, ? super K, ? extends K> biFunction = this.reducer;
            if (biFunction != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ReduceKeysTask reduceKeysTask = new ReduceKeysTask(this, i4, i3, i2, this.tab, this.rights, biFunction);
                    this.rights = reduceKeysTask;
                    reduceKeysTask.fork();
                }
                K k = null;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    K k2 = advance.key;
                    if (k == null) {
                        k = k2;
                    } else if (k2 != null) {
                        k = biFunction.apply(k, k2);
                    }
                }
                this.result = k;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    ReduceKeysTask reduceKeysTask2 = (ReduceKeysTask) firstComplete;
                    ReduceKeysTask<K, V> reduceKeysTask3 = reduceKeysTask2.rights;
                    while (reduceKeysTask3 != null) {
                        K k3 = reduceKeysTask3.result;
                        if (k3 != null) {
                            K k4 = reduceKeysTask2.result;
                            if (k4 != null) {
                                k3 = biFunction.apply(k4, k3);
                            }
                            reduceKeysTask2.result = k3;
                        }
                        reduceKeysTask3 = reduceKeysTask3.nextRight;
                        reduceKeysTask2.rights = reduceKeysTask3;
                    }
                }
            }
        }
    }

    static final class ReduceValuesTask<K, V> extends BulkTask<K, V, V> {
        ReduceValuesTask<K, V> nextRight;
        final BiFunction<? super V, ? super V, ? extends V> reducer;
        V result;
        ReduceValuesTask<K, V> rights;

        ReduceValuesTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, ReduceValuesTask<K, V> reduceValuesTask, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = reduceValuesTask;
            this.reducer = biFunction;
        }

        public final V getRawResult() {
            return this.result;
        }

        public final void compute() {
            BiFunction<? super V, ? super V, ? extends V> biFunction = this.reducer;
            if (biFunction != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ReduceValuesTask reduceValuesTask = new ReduceValuesTask(this, i4, i3, i2, this.tab, this.rights, biFunction);
                    this.rights = reduceValuesTask;
                    reduceValuesTask.fork();
                }
                V v = null;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    V v2 = advance.val;
                    if (v == null) {
                        v = v2;
                    } else {
                        v = biFunction.apply(v, v2);
                    }
                }
                this.result = v;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    ReduceValuesTask reduceValuesTask2 = (ReduceValuesTask) firstComplete;
                    ReduceValuesTask<K, V> reduceValuesTask3 = reduceValuesTask2.rights;
                    while (reduceValuesTask3 != null) {
                        V v3 = reduceValuesTask3.result;
                        if (v3 != null) {
                            V v4 = reduceValuesTask2.result;
                            if (v4 != null) {
                                v3 = biFunction.apply(v4, v3);
                            }
                            reduceValuesTask2.result = v3;
                        }
                        reduceValuesTask3 = reduceValuesTask3.nextRight;
                        reduceValuesTask2.rights = reduceValuesTask3;
                    }
                }
            }
        }
    }

    static final class ReduceEntriesTask<K, V> extends BulkTask<K, V, Map.Entry<K, V>> {
        ReduceEntriesTask<K, V> nextRight;
        final BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
        Map.Entry<K, V> result;
        ReduceEntriesTask<K, V> rights;

        ReduceEntriesTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, ReduceEntriesTask<K, V> reduceEntriesTask, BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFunction) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = reduceEntriesTask;
            this.reducer = biFunction;
        }

        public final Map.Entry<K, V> getRawResult() {
            return this.result;
        }

        public final void compute() {
            BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFunction = this.reducer;
            if (biFunction != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ReduceEntriesTask reduceEntriesTask = new ReduceEntriesTask(this, i4, i3, i2, this.tab, this.rights, biFunction);
                    this.rights = reduceEntriesTask;
                    reduceEntriesTask.fork();
                }
                Map.Entry<K, V> entry = null;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    } else if (entry == null) {
                        entry = advance;
                    } else {
                        entry = (Map.Entry) biFunction.apply(entry, advance);
                    }
                }
                this.result = entry;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    ReduceEntriesTask reduceEntriesTask2 = (ReduceEntriesTask) firstComplete;
                    ReduceEntriesTask<K, V> reduceEntriesTask3 = reduceEntriesTask2.rights;
                    while (reduceEntriesTask3 != null) {
                        Map.Entry<K, V> entry2 = reduceEntriesTask3.result;
                        if (entry2 != null) {
                            Map.Entry<K, V> entry3 = reduceEntriesTask2.result;
                            if (entry3 != null) {
                                entry2 = (Map.Entry) biFunction.apply(entry3, entry2);
                            }
                            reduceEntriesTask2.result = entry2;
                        }
                        reduceEntriesTask3 = reduceEntriesTask3.nextRight;
                        reduceEntriesTask2.rights = reduceEntriesTask3;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysTask<K, V, U> extends BulkTask<K, V, U> {
        MapReduceKeysTask<K, V, U> nextRight;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceKeysTask<K, V, U> rights;
        final Function<? super K, ? extends U> transformer;

        MapReduceKeysTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceKeysTask<K, V, U> mapReduceKeysTask, Function<? super K, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceKeysTask;
            this.transformer = function;
            this.reducer = biFunction;
        }

        public final U getRawResult() {
            return this.result;
        }

        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<? super K, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceKeysTask mapReduceKeysTask = new MapReduceKeysTask(this, i4, i3, i2, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceKeysTask;
                    mapReduceKeysTask.fork();
                }
                U u = null;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    U apply = function.apply(advance.key);
                    if (apply != null) {
                        if (u != null) {
                            apply = biFunction.apply(u, apply);
                        }
                        u = apply;
                    }
                }
                this.result = u;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceKeysTask mapReduceKeysTask2 = (MapReduceKeysTask) firstComplete;
                    MapReduceKeysTask<K, V, U> mapReduceKeysTask3 = mapReduceKeysTask2.rights;
                    while (mapReduceKeysTask3 != null) {
                        U u2 = mapReduceKeysTask3.result;
                        if (u2 != null) {
                            U u3 = mapReduceKeysTask2.result;
                            if (u3 != null) {
                                u2 = biFunction.apply(u3, u2);
                            }
                            mapReduceKeysTask2.result = u2;
                        }
                        mapReduceKeysTask3 = mapReduceKeysTask3.nextRight;
                        mapReduceKeysTask2.rights = mapReduceKeysTask3;
                    }
                }
            }
        }
    }

    static final class MapReduceValuesTask<K, V, U> extends BulkTask<K, V, U> {
        MapReduceValuesTask<K, V, U> nextRight;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceValuesTask<K, V, U> rights;
        final Function<? super V, ? extends U> transformer;

        MapReduceValuesTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceValuesTask<K, V, U> mapReduceValuesTask, Function<? super V, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceValuesTask;
            this.transformer = function;
            this.reducer = biFunction;
        }

        public final U getRawResult() {
            return this.result;
        }

        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<? super V, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceValuesTask mapReduceValuesTask = new MapReduceValuesTask(this, i4, i3, i2, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceValuesTask;
                    mapReduceValuesTask.fork();
                }
                U u = null;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    U apply = function.apply(advance.val);
                    if (apply != null) {
                        if (u != null) {
                            apply = biFunction.apply(u, apply);
                        }
                        u = apply;
                    }
                }
                this.result = u;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceValuesTask mapReduceValuesTask2 = (MapReduceValuesTask) firstComplete;
                    MapReduceValuesTask<K, V, U> mapReduceValuesTask3 = mapReduceValuesTask2.rights;
                    while (mapReduceValuesTask3 != null) {
                        U u2 = mapReduceValuesTask3.result;
                        if (u2 != null) {
                            U u3 = mapReduceValuesTask2.result;
                            if (u3 != null) {
                                u2 = biFunction.apply(u3, u2);
                            }
                            mapReduceValuesTask2.result = u2;
                        }
                        mapReduceValuesTask3 = mapReduceValuesTask3.nextRight;
                        mapReduceValuesTask2.rights = mapReduceValuesTask3;
                    }
                }
            }
        }
    }

    static final class MapReduceEntriesTask<K, V, U> extends BulkTask<K, V, U> {
        MapReduceEntriesTask<K, V, U> nextRight;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceEntriesTask<K, V, U> rights;
        final Function<Map.Entry<K, V>, ? extends U> transformer;

        MapReduceEntriesTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceEntriesTask<K, V, U> mapReduceEntriesTask, Function<Map.Entry<K, V>, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceEntriesTask;
            this.transformer = function;
            this.reducer = biFunction;
        }

        public final U getRawResult() {
            return this.result;
        }

        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<Map.Entry<K, V>, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceEntriesTask mapReduceEntriesTask = new MapReduceEntriesTask(this, i4, i3, i2, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceEntriesTask;
                    mapReduceEntriesTask.fork();
                }
                U u = null;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    U apply = function.apply(advance);
                    if (apply != null) {
                        if (u != null) {
                            apply = biFunction.apply(u, apply);
                        }
                        u = apply;
                    }
                }
                this.result = u;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceEntriesTask mapReduceEntriesTask2 = (MapReduceEntriesTask) firstComplete;
                    MapReduceEntriesTask<K, V, U> mapReduceEntriesTask3 = mapReduceEntriesTask2.rights;
                    while (mapReduceEntriesTask3 != null) {
                        U u2 = mapReduceEntriesTask3.result;
                        if (u2 != null) {
                            U u3 = mapReduceEntriesTask2.result;
                            if (u3 != null) {
                                u2 = biFunction.apply(u3, u2);
                            }
                            mapReduceEntriesTask2.result = u2;
                        }
                        mapReduceEntriesTask3 = mapReduceEntriesTask3.nextRight;
                        mapReduceEntriesTask2.rights = mapReduceEntriesTask3;
                    }
                }
            }
        }
    }

    static final class MapReduceMappingsTask<K, V, U> extends BulkTask<K, V, U> {
        MapReduceMappingsTask<K, V, U> nextRight;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceMappingsTask<K, V, U> rights;
        final BiFunction<? super K, ? super V, ? extends U> transformer;

        MapReduceMappingsTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceMappingsTask<K, V, U> mapReduceMappingsTask, BiFunction<? super K, ? super V, ? extends U> biFunction, BiFunction<? super U, ? super U, ? extends U> biFunction2) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceMappingsTask;
            this.transformer = biFunction;
            this.reducer = biFunction2;
        }

        public final U getRawResult() {
            return this.result;
        }

        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            BiFunction<? super K, ? super V, ? extends U> biFunction2 = this.transformer;
            if (biFunction2 != null && (biFunction = this.reducer) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceMappingsTask mapReduceMappingsTask = new MapReduceMappingsTask(this, i4, i3, i2, this.tab, this.rights, biFunction2, biFunction);
                    this.rights = mapReduceMappingsTask;
                    mapReduceMappingsTask.fork();
                }
                U u = null;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    U apply = biFunction2.apply(advance.key, advance.val);
                    if (apply != null) {
                        if (u != null) {
                            apply = biFunction.apply(u, apply);
                        }
                        u = apply;
                    }
                }
                this.result = u;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceMappingsTask mapReduceMappingsTask2 = (MapReduceMappingsTask) firstComplete;
                    MapReduceMappingsTask<K, V, U> mapReduceMappingsTask3 = mapReduceMappingsTask2.rights;
                    while (mapReduceMappingsTask3 != null) {
                        U u2 = mapReduceMappingsTask3.result;
                        if (u2 != null) {
                            U u3 = mapReduceMappingsTask2.result;
                            if (u3 != null) {
                                u2 = biFunction.apply(u3, u2);
                            }
                            mapReduceMappingsTask2.result = u2;
                        }
                        mapReduceMappingsTask3 = mapReduceMappingsTask3.nextRight;
                        mapReduceMappingsTask2.rights = mapReduceMappingsTask3;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final double basis;
        MapReduceKeysToDoubleTask<K, V> nextRight;
        final DoubleBinaryOperator reducer;
        double result;
        MapReduceKeysToDoubleTask<K, V> rights;
        final ToDoubleFunction<? super K> transformer;

        MapReduceKeysToDoubleTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask, ToDoubleFunction<? super K> toDoubleFunction, double d, DoubleBinaryOperator doubleBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceKeysToDoubleTask;
            this.transformer = toDoubleFunction;
            this.basis = d;
            this.reducer = doubleBinaryOperator;
        }

        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        public final void compute() {
            DoubleBinaryOperator doubleBinaryOperator;
            ToDoubleFunction<? super K> toDoubleFunction = this.transformer;
            if (toDoubleFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double d = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ToDoubleFunction<? super K> toDoubleFunction2 = toDoubleFunction;
                    ToDoubleFunction<? super K> toDoubleFunction3 = toDoubleFunction;
                    MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask = r0;
                    MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask2 = new MapReduceKeysToDoubleTask<>(this, i4, i3, i2, this.tab, this.rights, toDoubleFunction2, d, doubleBinaryOperator);
                    this.rights = mapReduceKeysToDoubleTask;
                    mapReduceKeysToDoubleTask.fork();
                    toDoubleFunction = toDoubleFunction3;
                    i = i;
                }
                ToDoubleFunction<? super K> toDoubleFunction4 = toDoubleFunction;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    d = doubleBinaryOperator.applyAsDouble(d, toDoubleFunction4.applyAsDouble(advance.key));
                }
                this.result = d;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceKeysToDoubleTask mapReduceKeysToDoubleTask3 = (MapReduceKeysToDoubleTask) firstComplete;
                    MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask4 = mapReduceKeysToDoubleTask3.rights;
                    while (mapReduceKeysToDoubleTask4 != null) {
                        mapReduceKeysToDoubleTask3.result = doubleBinaryOperator.applyAsDouble(mapReduceKeysToDoubleTask3.result, mapReduceKeysToDoubleTask4.result);
                        mapReduceKeysToDoubleTask4 = mapReduceKeysToDoubleTask4.nextRight;
                        mapReduceKeysToDoubleTask3.rights = mapReduceKeysToDoubleTask4;
                    }
                }
            }
        }
    }

    static final class MapReduceValuesToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final double basis;
        MapReduceValuesToDoubleTask<K, V> nextRight;
        final DoubleBinaryOperator reducer;
        double result;
        MapReduceValuesToDoubleTask<K, V> rights;
        final ToDoubleFunction<? super V> transformer;

        MapReduceValuesToDoubleTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask, ToDoubleFunction<? super V> toDoubleFunction, double d, DoubleBinaryOperator doubleBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceValuesToDoubleTask;
            this.transformer = toDoubleFunction;
            this.basis = d;
            this.reducer = doubleBinaryOperator;
        }

        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        public final void compute() {
            DoubleBinaryOperator doubleBinaryOperator;
            ToDoubleFunction<? super V> toDoubleFunction = this.transformer;
            if (toDoubleFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double d = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ToDoubleFunction<? super V> toDoubleFunction2 = toDoubleFunction;
                    ToDoubleFunction<? super V> toDoubleFunction3 = toDoubleFunction;
                    MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask = r0;
                    MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask2 = new MapReduceValuesToDoubleTask<>(this, i4, i3, i2, this.tab, this.rights, toDoubleFunction2, d, doubleBinaryOperator);
                    this.rights = mapReduceValuesToDoubleTask;
                    mapReduceValuesToDoubleTask.fork();
                    toDoubleFunction = toDoubleFunction3;
                    i = i;
                }
                ToDoubleFunction<? super V> toDoubleFunction4 = toDoubleFunction;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    d = doubleBinaryOperator.applyAsDouble(d, toDoubleFunction4.applyAsDouble(advance.val));
                }
                this.result = d;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceValuesToDoubleTask mapReduceValuesToDoubleTask3 = (MapReduceValuesToDoubleTask) firstComplete;
                    MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask4 = mapReduceValuesToDoubleTask3.rights;
                    while (mapReduceValuesToDoubleTask4 != null) {
                        mapReduceValuesToDoubleTask3.result = doubleBinaryOperator.applyAsDouble(mapReduceValuesToDoubleTask3.result, mapReduceValuesToDoubleTask4.result);
                        mapReduceValuesToDoubleTask4 = mapReduceValuesToDoubleTask4.nextRight;
                        mapReduceValuesToDoubleTask3.rights = mapReduceValuesToDoubleTask4;
                    }
                }
            }
        }
    }

    static final class MapReduceEntriesToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final double basis;
        MapReduceEntriesToDoubleTask<K, V> nextRight;
        final DoubleBinaryOperator reducer;
        double result;
        MapReduceEntriesToDoubleTask<K, V> rights;
        final ToDoubleFunction<Map.Entry<K, V>> transformer;

        MapReduceEntriesToDoubleTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask, ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction, double d, DoubleBinaryOperator doubleBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceEntriesToDoubleTask;
            this.transformer = toDoubleFunction;
            this.basis = d;
            this.reducer = doubleBinaryOperator;
        }

        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        public final void compute() {
            DoubleBinaryOperator doubleBinaryOperator;
            ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction = this.transformer;
            if (toDoubleFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double d = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction2 = toDoubleFunction;
                    ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction3 = toDoubleFunction;
                    MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask = r0;
                    MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask2 = new MapReduceEntriesToDoubleTask<>(this, i4, i3, i2, this.tab, this.rights, toDoubleFunction2, d, doubleBinaryOperator);
                    this.rights = mapReduceEntriesToDoubleTask;
                    mapReduceEntriesToDoubleTask.fork();
                    toDoubleFunction = toDoubleFunction3;
                    i = i;
                }
                ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction4 = toDoubleFunction;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    d = doubleBinaryOperator.applyAsDouble(d, toDoubleFunction4.applyAsDouble(advance));
                }
                this.result = d;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceEntriesToDoubleTask mapReduceEntriesToDoubleTask3 = (MapReduceEntriesToDoubleTask) firstComplete;
                    MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask4 = mapReduceEntriesToDoubleTask3.rights;
                    while (mapReduceEntriesToDoubleTask4 != null) {
                        mapReduceEntriesToDoubleTask3.result = doubleBinaryOperator.applyAsDouble(mapReduceEntriesToDoubleTask3.result, mapReduceEntriesToDoubleTask4.result);
                        mapReduceEntriesToDoubleTask4 = mapReduceEntriesToDoubleTask4.nextRight;
                        mapReduceEntriesToDoubleTask3.rights = mapReduceEntriesToDoubleTask4;
                    }
                }
            }
        }
    }

    static final class MapReduceMappingsToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final double basis;
        MapReduceMappingsToDoubleTask<K, V> nextRight;
        final DoubleBinaryOperator reducer;
        double result;
        MapReduceMappingsToDoubleTask<K, V> rights;
        final ToDoubleBiFunction<? super K, ? super V> transformer;

        MapReduceMappingsToDoubleTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask, ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction, double d, DoubleBinaryOperator doubleBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceMappingsToDoubleTask;
            this.transformer = toDoubleBiFunction;
            this.basis = d;
            this.reducer = doubleBinaryOperator;
        }

        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        public final void compute() {
            DoubleBinaryOperator doubleBinaryOperator;
            ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction = this.transformer;
            if (toDoubleBiFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double d = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction2 = toDoubleBiFunction;
                    ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction3 = toDoubleBiFunction;
                    MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask = r0;
                    MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask2 = new MapReduceMappingsToDoubleTask<>(this, i4, i3, i2, this.tab, this.rights, toDoubleBiFunction2, d, doubleBinaryOperator);
                    this.rights = mapReduceMappingsToDoubleTask;
                    mapReduceMappingsToDoubleTask.fork();
                    toDoubleBiFunction = toDoubleBiFunction3;
                    i = i;
                }
                ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction4 = toDoubleBiFunction;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    d = doubleBinaryOperator.applyAsDouble(d, toDoubleBiFunction4.applyAsDouble(advance.key, advance.val));
                }
                this.result = d;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceMappingsToDoubleTask mapReduceMappingsToDoubleTask3 = (MapReduceMappingsToDoubleTask) firstComplete;
                    MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask4 = mapReduceMappingsToDoubleTask3.rights;
                    while (mapReduceMappingsToDoubleTask4 != null) {
                        mapReduceMappingsToDoubleTask3.result = doubleBinaryOperator.applyAsDouble(mapReduceMappingsToDoubleTask3.result, mapReduceMappingsToDoubleTask4.result);
                        mapReduceMappingsToDoubleTask4 = mapReduceMappingsToDoubleTask4.nextRight;
                        mapReduceMappingsToDoubleTask3.rights = mapReduceMappingsToDoubleTask4;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysToLongTask<K, V> extends BulkTask<K, V, Long> {
        final long basis;
        MapReduceKeysToLongTask<K, V> nextRight;
        final LongBinaryOperator reducer;
        long result;
        MapReduceKeysToLongTask<K, V> rights;
        final ToLongFunction<? super K> transformer;

        MapReduceKeysToLongTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask, ToLongFunction<? super K> toLongFunction, long j, LongBinaryOperator longBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceKeysToLongTask;
            this.transformer = toLongFunction;
            this.basis = j;
            this.reducer = longBinaryOperator;
        }

        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        public final void compute() {
            LongBinaryOperator longBinaryOperator;
            ToLongFunction<? super K> toLongFunction = this.transformer;
            if (toLongFunction != null && (longBinaryOperator = this.reducer) != null) {
                long j = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ToLongFunction<? super K> toLongFunction2 = toLongFunction;
                    ToLongFunction<? super K> toLongFunction3 = toLongFunction;
                    MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask = r0;
                    MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask2 = new MapReduceKeysToLongTask<>(this, i4, i3, i2, this.tab, this.rights, toLongFunction2, j, longBinaryOperator);
                    this.rights = mapReduceKeysToLongTask;
                    mapReduceKeysToLongTask.fork();
                    toLongFunction = toLongFunction3;
                    i = i;
                }
                ToLongFunction<? super K> toLongFunction4 = toLongFunction;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    j = longBinaryOperator.applyAsLong(j, toLongFunction4.applyAsLong(advance.key));
                }
                this.result = j;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceKeysToLongTask mapReduceKeysToLongTask3 = (MapReduceKeysToLongTask) firstComplete;
                    MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask4 = mapReduceKeysToLongTask3.rights;
                    while (mapReduceKeysToLongTask4 != null) {
                        mapReduceKeysToLongTask3.result = longBinaryOperator.applyAsLong(mapReduceKeysToLongTask3.result, mapReduceKeysToLongTask4.result);
                        mapReduceKeysToLongTask4 = mapReduceKeysToLongTask4.nextRight;
                        mapReduceKeysToLongTask3.rights = mapReduceKeysToLongTask4;
                    }
                }
            }
        }
    }

    static final class MapReduceValuesToLongTask<K, V> extends BulkTask<K, V, Long> {
        final long basis;
        MapReduceValuesToLongTask<K, V> nextRight;
        final LongBinaryOperator reducer;
        long result;
        MapReduceValuesToLongTask<K, V> rights;
        final ToLongFunction<? super V> transformer;

        MapReduceValuesToLongTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask, ToLongFunction<? super V> toLongFunction, long j, LongBinaryOperator longBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceValuesToLongTask;
            this.transformer = toLongFunction;
            this.basis = j;
            this.reducer = longBinaryOperator;
        }

        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        public final void compute() {
            LongBinaryOperator longBinaryOperator;
            ToLongFunction<? super V> toLongFunction = this.transformer;
            if (toLongFunction != null && (longBinaryOperator = this.reducer) != null) {
                long j = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ToLongFunction<? super V> toLongFunction2 = toLongFunction;
                    ToLongFunction<? super V> toLongFunction3 = toLongFunction;
                    MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask = r0;
                    MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask2 = new MapReduceValuesToLongTask<>(this, i4, i3, i2, this.tab, this.rights, toLongFunction2, j, longBinaryOperator);
                    this.rights = mapReduceValuesToLongTask;
                    mapReduceValuesToLongTask.fork();
                    toLongFunction = toLongFunction3;
                    i = i;
                }
                ToLongFunction<? super V> toLongFunction4 = toLongFunction;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    j = longBinaryOperator.applyAsLong(j, toLongFunction4.applyAsLong(advance.val));
                }
                this.result = j;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceValuesToLongTask mapReduceValuesToLongTask3 = (MapReduceValuesToLongTask) firstComplete;
                    MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask4 = mapReduceValuesToLongTask3.rights;
                    while (mapReduceValuesToLongTask4 != null) {
                        mapReduceValuesToLongTask3.result = longBinaryOperator.applyAsLong(mapReduceValuesToLongTask3.result, mapReduceValuesToLongTask4.result);
                        mapReduceValuesToLongTask4 = mapReduceValuesToLongTask4.nextRight;
                        mapReduceValuesToLongTask3.rights = mapReduceValuesToLongTask4;
                    }
                }
            }
        }
    }

    static final class MapReduceEntriesToLongTask<K, V> extends BulkTask<K, V, Long> {
        final long basis;
        MapReduceEntriesToLongTask<K, V> nextRight;
        final LongBinaryOperator reducer;
        long result;
        MapReduceEntriesToLongTask<K, V> rights;
        final ToLongFunction<Map.Entry<K, V>> transformer;

        MapReduceEntriesToLongTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask, ToLongFunction<Map.Entry<K, V>> toLongFunction, long j, LongBinaryOperator longBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceEntriesToLongTask;
            this.transformer = toLongFunction;
            this.basis = j;
            this.reducer = longBinaryOperator;
        }

        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        public final void compute() {
            LongBinaryOperator longBinaryOperator;
            ToLongFunction<Map.Entry<K, V>> toLongFunction = this.transformer;
            if (toLongFunction != null && (longBinaryOperator = this.reducer) != null) {
                long j = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ToLongFunction<Map.Entry<K, V>> toLongFunction2 = toLongFunction;
                    ToLongFunction<Map.Entry<K, V>> toLongFunction3 = toLongFunction;
                    MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask = r0;
                    MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask2 = new MapReduceEntriesToLongTask<>(this, i4, i3, i2, this.tab, this.rights, toLongFunction2, j, longBinaryOperator);
                    this.rights = mapReduceEntriesToLongTask;
                    mapReduceEntriesToLongTask.fork();
                    toLongFunction = toLongFunction3;
                    i = i;
                }
                ToLongFunction<Map.Entry<K, V>> toLongFunction4 = toLongFunction;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    j = longBinaryOperator.applyAsLong(j, toLongFunction4.applyAsLong(advance));
                }
                this.result = j;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceEntriesToLongTask mapReduceEntriesToLongTask3 = (MapReduceEntriesToLongTask) firstComplete;
                    MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask4 = mapReduceEntriesToLongTask3.rights;
                    while (mapReduceEntriesToLongTask4 != null) {
                        mapReduceEntriesToLongTask3.result = longBinaryOperator.applyAsLong(mapReduceEntriesToLongTask3.result, mapReduceEntriesToLongTask4.result);
                        mapReduceEntriesToLongTask4 = mapReduceEntriesToLongTask4.nextRight;
                        mapReduceEntriesToLongTask3.rights = mapReduceEntriesToLongTask4;
                    }
                }
            }
        }
    }

    static final class MapReduceMappingsToLongTask<K, V> extends BulkTask<K, V, Long> {
        final long basis;
        MapReduceMappingsToLongTask<K, V> nextRight;
        final LongBinaryOperator reducer;
        long result;
        MapReduceMappingsToLongTask<K, V> rights;
        final ToLongBiFunction<? super K, ? super V> transformer;

        MapReduceMappingsToLongTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask, ToLongBiFunction<? super K, ? super V> toLongBiFunction, long j, LongBinaryOperator longBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceMappingsToLongTask;
            this.transformer = toLongBiFunction;
            this.basis = j;
            this.reducer = longBinaryOperator;
        }

        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        public final void compute() {
            LongBinaryOperator longBinaryOperator;
            ToLongBiFunction<? super K, ? super V> toLongBiFunction = this.transformer;
            if (toLongBiFunction != null && (longBinaryOperator = this.reducer) != null) {
                long j = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ToLongBiFunction<? super K, ? super V> toLongBiFunction2 = toLongBiFunction;
                    ToLongBiFunction<? super K, ? super V> toLongBiFunction3 = toLongBiFunction;
                    MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask = r0;
                    MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask2 = new MapReduceMappingsToLongTask<>(this, i4, i3, i2, this.tab, this.rights, toLongBiFunction2, j, longBinaryOperator);
                    this.rights = mapReduceMappingsToLongTask;
                    mapReduceMappingsToLongTask.fork();
                    toLongBiFunction = toLongBiFunction3;
                    i = i;
                }
                ToLongBiFunction<? super K, ? super V> toLongBiFunction4 = toLongBiFunction;
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    j = longBinaryOperator.applyAsLong(j, toLongBiFunction4.applyAsLong(advance.key, advance.val));
                }
                this.result = j;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceMappingsToLongTask mapReduceMappingsToLongTask3 = (MapReduceMappingsToLongTask) firstComplete;
                    MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask4 = mapReduceMappingsToLongTask3.rights;
                    while (mapReduceMappingsToLongTask4 != null) {
                        mapReduceMappingsToLongTask3.result = longBinaryOperator.applyAsLong(mapReduceMappingsToLongTask3.result, mapReduceMappingsToLongTask4.result);
                        mapReduceMappingsToLongTask4 = mapReduceMappingsToLongTask4.nextRight;
                        mapReduceMappingsToLongTask3.rights = mapReduceMappingsToLongTask4;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final int basis;
        MapReduceKeysToIntTask<K, V> nextRight;
        final IntBinaryOperator reducer;
        int result;
        MapReduceKeysToIntTask<K, V> rights;
        final ToIntFunction<? super K> transformer;

        MapReduceKeysToIntTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask, ToIntFunction<? super K> toIntFunction, int i4, IntBinaryOperator intBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceKeysToIntTask;
            this.transformer = toIntFunction;
            this.basis = i4;
            this.reducer = intBinaryOperator;
        }

        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        public final void compute() {
            IntBinaryOperator intBinaryOperator;
            ToIntFunction<? super K> toIntFunction = this.transformer;
            if (toIntFunction != null && (intBinaryOperator = this.reducer) != null) {
                int i = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceKeysToIntTask mapReduceKeysToIntTask = new MapReduceKeysToIntTask(this, i5, i4, i3, this.tab, this.rights, toIntFunction, i, intBinaryOperator);
                    this.rights = mapReduceKeysToIntTask;
                    mapReduceKeysToIntTask.fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    i = intBinaryOperator.applyAsInt(i, toIntFunction.applyAsInt(advance.key));
                }
                this.result = i;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceKeysToIntTask mapReduceKeysToIntTask2 = (MapReduceKeysToIntTask) firstComplete;
                    MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask3 = mapReduceKeysToIntTask2.rights;
                    while (mapReduceKeysToIntTask3 != null) {
                        mapReduceKeysToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceKeysToIntTask2.result, mapReduceKeysToIntTask3.result);
                        mapReduceKeysToIntTask3 = mapReduceKeysToIntTask3.nextRight;
                        mapReduceKeysToIntTask2.rights = mapReduceKeysToIntTask3;
                    }
                }
            }
        }
    }

    static final class MapReduceValuesToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final int basis;
        MapReduceValuesToIntTask<K, V> nextRight;
        final IntBinaryOperator reducer;
        int result;
        MapReduceValuesToIntTask<K, V> rights;
        final ToIntFunction<? super V> transformer;

        MapReduceValuesToIntTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask, ToIntFunction<? super V> toIntFunction, int i4, IntBinaryOperator intBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceValuesToIntTask;
            this.transformer = toIntFunction;
            this.basis = i4;
            this.reducer = intBinaryOperator;
        }

        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        public final void compute() {
            IntBinaryOperator intBinaryOperator;
            ToIntFunction<? super V> toIntFunction = this.transformer;
            if (toIntFunction != null && (intBinaryOperator = this.reducer) != null) {
                int i = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceValuesToIntTask mapReduceValuesToIntTask = new MapReduceValuesToIntTask(this, i5, i4, i3, this.tab, this.rights, toIntFunction, i, intBinaryOperator);
                    this.rights = mapReduceValuesToIntTask;
                    mapReduceValuesToIntTask.fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    i = intBinaryOperator.applyAsInt(i, toIntFunction.applyAsInt(advance.val));
                }
                this.result = i;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceValuesToIntTask mapReduceValuesToIntTask2 = (MapReduceValuesToIntTask) firstComplete;
                    MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask3 = mapReduceValuesToIntTask2.rights;
                    while (mapReduceValuesToIntTask3 != null) {
                        mapReduceValuesToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceValuesToIntTask2.result, mapReduceValuesToIntTask3.result);
                        mapReduceValuesToIntTask3 = mapReduceValuesToIntTask3.nextRight;
                        mapReduceValuesToIntTask2.rights = mapReduceValuesToIntTask3;
                    }
                }
            }
        }
    }

    static final class MapReduceEntriesToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final int basis;
        MapReduceEntriesToIntTask<K, V> nextRight;
        final IntBinaryOperator reducer;
        int result;
        MapReduceEntriesToIntTask<K, V> rights;
        final ToIntFunction<Map.Entry<K, V>> transformer;

        MapReduceEntriesToIntTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask, ToIntFunction<Map.Entry<K, V>> toIntFunction, int i4, IntBinaryOperator intBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceEntriesToIntTask;
            this.transformer = toIntFunction;
            this.basis = i4;
            this.reducer = intBinaryOperator;
        }

        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        public final void compute() {
            IntBinaryOperator intBinaryOperator;
            ToIntFunction<Map.Entry<K, V>> toIntFunction = this.transformer;
            if (toIntFunction != null && (intBinaryOperator = this.reducer) != null) {
                int i = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceEntriesToIntTask mapReduceEntriesToIntTask = new MapReduceEntriesToIntTask(this, i5, i4, i3, this.tab, this.rights, toIntFunction, i, intBinaryOperator);
                    this.rights = mapReduceEntriesToIntTask;
                    mapReduceEntriesToIntTask.fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    i = intBinaryOperator.applyAsInt(i, toIntFunction.applyAsInt(advance));
                }
                this.result = i;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceEntriesToIntTask mapReduceEntriesToIntTask2 = (MapReduceEntriesToIntTask) firstComplete;
                    MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask3 = mapReduceEntriesToIntTask2.rights;
                    while (mapReduceEntriesToIntTask3 != null) {
                        mapReduceEntriesToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceEntriesToIntTask2.result, mapReduceEntriesToIntTask3.result);
                        mapReduceEntriesToIntTask3 = mapReduceEntriesToIntTask3.nextRight;
                        mapReduceEntriesToIntTask2.rights = mapReduceEntriesToIntTask3;
                    }
                }
            }
        }
    }

    static final class MapReduceMappingsToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final int basis;
        MapReduceMappingsToIntTask<K, V> nextRight;
        final IntBinaryOperator reducer;
        int result;
        MapReduceMappingsToIntTask<K, V> rights;
        final ToIntBiFunction<? super K, ? super V> transformer;

        MapReduceMappingsToIntTask(BulkTask<K, V, ?> bulkTask, int i, int i2, int i3, Node<K, V>[] nodeArr, MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask, ToIntBiFunction<? super K, ? super V> toIntBiFunction, int i4, IntBinaryOperator intBinaryOperator) {
            super(bulkTask, i, i2, i3, nodeArr);
            this.nextRight = mapReduceMappingsToIntTask;
            this.transformer = toIntBiFunction;
            this.basis = i4;
            this.reducer = intBinaryOperator;
        }

        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        public final void compute() {
            IntBinaryOperator intBinaryOperator;
            ToIntBiFunction<? super K, ? super V> toIntBiFunction = this.transformer;
            if (toIntBiFunction != null && (intBinaryOperator = this.reducer) != null) {
                int i = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceMappingsToIntTask mapReduceMappingsToIntTask = new MapReduceMappingsToIntTask(this, i5, i4, i3, this.tab, this.rights, toIntBiFunction, i, intBinaryOperator);
                    this.rights = mapReduceMappingsToIntTask;
                    mapReduceMappingsToIntTask.fork();
                }
                while (true) {
                    Node advance = advance();
                    if (advance == null) {
                        break;
                    }
                    i = intBinaryOperator.applyAsInt(i, toIntBiFunction.applyAsInt(advance.key, advance.val));
                }
                this.result = i;
                for (CountedCompleter<?> firstComplete = firstComplete(); firstComplete != null; firstComplete = firstComplete.nextComplete()) {
                    MapReduceMappingsToIntTask mapReduceMappingsToIntTask2 = (MapReduceMappingsToIntTask) firstComplete;
                    MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask3 = mapReduceMappingsToIntTask2.rights;
                    while (mapReduceMappingsToIntTask3 != null) {
                        mapReduceMappingsToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceMappingsToIntTask2.result, mapReduceMappingsToIntTask3.result);
                        mapReduceMappingsToIntTask3 = mapReduceMappingsToIntTask3.nextRight;
                        mapReduceMappingsToIntTask2.rights = mapReduceMappingsToIntTask3;
                    }
                }
            }
        }
    }
}
