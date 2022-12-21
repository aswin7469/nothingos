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
    private transient NavigableMap<E, Object> f715m;

    TreeSet(NavigableMap<E, Object> navigableMap) {
        this.f715m = navigableMap;
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
        return this.f715m.navigableKeySet().iterator();
    }

    public Iterator<E> descendingIterator() {
        return this.f715m.descendingKeySet().iterator();
    }

    public NavigableSet<E> descendingSet() {
        return new TreeSet(this.f715m.descendingMap());
    }

    public int size() {
        return this.f715m.size();
    }

    public boolean isEmpty() {
        return this.f715m.isEmpty();
    }

    public boolean contains(Object obj) {
        return this.f715m.containsKey(obj);
    }

    public boolean add(E e) {
        return this.f715m.put(e, PRESENT) == null;
    }

    public boolean remove(Object obj) {
        return this.f715m.remove(obj) == PRESENT;
    }

    public void clear() {
        this.f715m.clear();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0018, code lost:
        r1 = (java.util.SortedSet) r5;
        r0 = (java.util.TreeMap) r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addAll(java.util.Collection<? extends E> r5) {
        /*
            r4 = this;
            java.util.NavigableMap<E, java.lang.Object> r0 = r4.f715m
            int r0 = r0.size()
            if (r0 != 0) goto L_0x0036
            int r0 = r5.size()
            if (r0 <= 0) goto L_0x0036
            boolean r0 = r5 instanceof java.util.SortedSet
            if (r0 == 0) goto L_0x0036
            java.util.NavigableMap<E, java.lang.Object> r0 = r4.f715m
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
        return new TreeSet(this.f715m.subMap(e, z, e2, z2));
    }

    public NavigableSet<E> headSet(E e, boolean z) {
        return new TreeSet(this.f715m.headMap(e, z));
    }

    public NavigableSet<E> tailSet(E e, boolean z) {
        return new TreeSet(this.f715m.tailMap(e, z));
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
        return this.f715m.comparator();
    }

    public E first() {
        return this.f715m.firstKey();
    }

    public E last() {
        return this.f715m.lastKey();
    }

    public E lower(E e) {
        return this.f715m.lowerKey(e);
    }

    public E floor(E e) {
        return this.f715m.floorKey(e);
    }

    public E ceiling(E e) {
        return this.f715m.ceilingKey(e);
    }

    public E higher(E e) {
        return this.f715m.higherKey(e);
    }

    public E pollFirst() {
        Map.Entry<E, Object> pollFirstEntry = this.f715m.pollFirstEntry();
        if (pollFirstEntry == null) {
            return null;
        }
        return pollFirstEntry.getKey();
    }

    public E pollLast() {
        Map.Entry<E, Object> pollLastEntry = this.f715m.pollLastEntry();
        if (pollLastEntry == null) {
            return null;
        }
        return pollLastEntry.getKey();
    }

    public Object clone() {
        try {
            TreeSet treeSet = (TreeSet) super.clone();
            treeSet.f715m = new TreeMap(this.f715m);
            return treeSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.f715m.comparator());
        objectOutputStream.writeInt(this.f715m.size());
        for (E writeObject : this.f715m.keySet()) {
            objectOutputStream.writeObject(writeObject);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        TreeMap treeMap = new TreeMap((Comparator) objectInputStream.readObject());
        this.f715m = treeMap;
        treeMap.readTreeSet(objectInputStream.readInt(), objectInputStream, PRESENT);
    }

    public Spliterator<E> spliterator() {
        return TreeMap.keySpliteratorFor(this.f715m);
    }
}
