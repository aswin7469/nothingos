package java.util;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.Map;

public class TreeSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable {
    private static final Object PRESENT = new Object();
    private static final long serialVersionUID = -2479143000061671589L;

    /* renamed from: m */
    private transient NavigableMap<E, Object> f713m;

    TreeSet(NavigableMap<E, Object> navigableMap) {
        this.f713m = navigableMap;
    }

    public TreeSet() {
        this(new TreeMap());
    }

    public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap(comparator));
    }

    public TreeSet(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    public TreeSet(SortedSet<E> sortedSet) {
        this(sortedSet.comparator());
        addAll(sortedSet);
    }

    public Iterator<E> iterator() {
        return this.f713m.navigableKeySet().iterator();
    }

    public Iterator<E> descendingIterator() {
        return this.f713m.descendingKeySet().iterator();
    }

    public NavigableSet<E> descendingSet() {
        return new TreeSet(this.f713m.descendingMap());
    }

    public int size() {
        return this.f713m.size();
    }

    public boolean isEmpty() {
        return this.f713m.isEmpty();
    }

    public boolean contains(Object obj) {
        return this.f713m.containsKey(obj);
    }

    public boolean add(E e) {
        return this.f713m.put(e, PRESENT) == null;
    }

    public boolean remove(Object obj) {
        return this.f713m.remove(obj) == PRESENT;
    }

    public void clear() {
        this.f713m.clear();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0018, code lost:
        r1 = (java.util.SortedSet) r5;
        r0 = (java.util.TreeMap) r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addAll(java.util.Collection<? extends E> r5) {
        /*
            r4 = this;
            java.util.NavigableMap<E, java.lang.Object> r0 = r4.f713m
            int r0 = r0.size()
            if (r0 != 0) goto L_0x0036
            int r0 = r5.size()
            if (r0 <= 0) goto L_0x0036
            boolean r0 = r5 instanceof java.util.SortedSet
            if (r0 == 0) goto L_0x0036
            java.util.NavigableMap<E, java.lang.Object> r0 = r4.f713m
            boolean r1 = r0 instanceof java.util.TreeMap
            if (r1 == 0) goto L_0x0036
            r1 = r5
            java.util.SortedSet r1 = (java.util.SortedSet) r1
            java.util.TreeMap r0 = (java.util.TreeMap) r0
            java.util.Comparator r2 = r1.comparator()
            java.util.Comparator r3 = r0.comparator()
            if (r2 == r3) goto L_0x002f
            if (r2 == 0) goto L_0x0036
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0036
        L_0x002f:
            java.lang.Object r4 = PRESENT
            r0.addAllForTreeSet(r1, r4)
            r4 = 1
            return r4
        L_0x0036:
            boolean r4 = super.addAll(r5)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TreeSet.addAll(java.util.Collection):boolean");
    }

    public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
        return new TreeSet(this.f713m.subMap(e, z, e2, z2));
    }

    public NavigableSet<E> headSet(E e, boolean z) {
        return new TreeSet(this.f713m.headMap(e, z));
    }

    public NavigableSet<E> tailSet(E e, boolean z) {
        return new TreeSet(this.f713m.tailMap(e, z));
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

    public Comparator<? super E> comparator() {
        return this.f713m.comparator();
    }

    public E first() {
        return this.f713m.firstKey();
    }

    public E last() {
        return this.f713m.lastKey();
    }

    public E lower(E e) {
        return this.f713m.lowerKey(e);
    }

    public E floor(E e) {
        return this.f713m.floorKey(e);
    }

    public E ceiling(E e) {
        return this.f713m.ceilingKey(e);
    }

    public E higher(E e) {
        return this.f713m.higherKey(e);
    }

    public E pollFirst() {
        Map.Entry<E, Object> pollFirstEntry = this.f713m.pollFirstEntry();
        if (pollFirstEntry == null) {
            return null;
        }
        return pollFirstEntry.getKey();
    }

    public E pollLast() {
        Map.Entry<E, Object> pollLastEntry = this.f713m.pollLastEntry();
        if (pollLastEntry == null) {
            return null;
        }
        return pollLastEntry.getKey();
    }

    public Object clone() {
        try {
            TreeSet treeSet = (TreeSet) super.clone();
            treeSet.f713m = new TreeMap(this.f713m);
            return treeSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.f713m.comparator());
        objectOutputStream.writeInt(this.f713m.size());
        for (E writeObject : this.f713m.keySet()) {
            objectOutputStream.writeObject(writeObject);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        TreeMap treeMap = new TreeMap((Comparator) objectInputStream.readObject());
        this.f713m = treeMap;
        treeMap.readTreeSet(objectInputStream.readInt(), objectInputStream, PRESENT);
    }

    public Spliterator<E> spliterator() {
        return TreeMap.keySpliteratorFor(this.f713m);
    }
}
