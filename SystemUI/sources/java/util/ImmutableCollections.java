package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import jdk.internal.p027vm.annotation.Stable;

class ImmutableCollections {
    static final int EXPAND_FACTOR = 2;
    static final int SALT;

    static {
        long nanoTime = System.nanoTime();
        SALT = (int) (nanoTime ^ (nanoTime >>> 32));
    }

    private ImmutableCollections() {
    }

    static UnsupportedOperationException uoe() {
        return new UnsupportedOperationException();
    }

    static abstract class AbstractImmutableCollection<E> extends AbstractCollection<E> {
        AbstractImmutableCollection() {
        }

        public boolean add(E e) {
            throw ImmutableCollections.uoe();
        }

        public boolean addAll(Collection<? extends E> collection) {
            throw ImmutableCollections.uoe();
        }

        public void clear() {
            throw ImmutableCollections.uoe();
        }

        public boolean remove(Object obj) {
            throw ImmutableCollections.uoe();
        }

        public boolean removeAll(Collection<?> collection) {
            throw ImmutableCollections.uoe();
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            throw ImmutableCollections.uoe();
        }

        public boolean retainAll(Collection<?> collection) {
            throw ImmutableCollections.uoe();
        }
    }

    static <E> List<E> listCopy(Collection<? extends E> collection) {
        if (!(collection instanceof AbstractImmutableList) || collection.getClass() == SubList.class) {
            return List.m1733of((E[]) collection.toArray());
        }
        return (List) collection;
    }

    static <E> List<E> emptyList() {
        return ListN.EMPTY_LIST;
    }

    static abstract class AbstractImmutableList<E> extends AbstractImmutableCollection<E> implements List<E>, RandomAccess {
        AbstractImmutableList() {
        }

        public void add(int i, E e) {
            throw ImmutableCollections.uoe();
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            throw ImmutableCollections.uoe();
        }

        public E remove(int i) {
            throw ImmutableCollections.uoe();
        }

        public void replaceAll(UnaryOperator<E> unaryOperator) {
            throw ImmutableCollections.uoe();
        }

        public E set(int i, E e) {
            throw ImmutableCollections.uoe();
        }

        public void sort(Comparator<? super E> comparator) {
            throw ImmutableCollections.uoe();
        }

        public List<E> subList(int i, int i2) {
            subListRangeCheck(i, i2, size());
            return SubList.fromList(this, i, i2);
        }

        static void subListRangeCheck(int i, int i2, int i3) {
            if (i < 0) {
                throw new IndexOutOfBoundsException("fromIndex = " + i);
            } else if (i2 > i3) {
                throw new IndexOutOfBoundsException("toIndex = " + i2);
            } else if (i > i2) {
                throw new IllegalArgumentException("fromIndex(" + i + ") > toIndex(" + i2 + NavigationBarInflaterView.KEY_CODE_END);
            }
        }

        public Iterator<E> iterator() {
            return new ListItr(this, size());
        }

        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        public ListIterator<E> listIterator(int i) {
            int size = size();
            if (i >= 0 && i <= size) {
                return new ListItr(this, size, i);
            }
            throw outOfBounds(i);
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof List)) {
                return false;
            }
            Iterator it = ((List) obj).iterator();
            int size = size();
            for (int i = 0; i < size; i++) {
                if (!it.hasNext() || !get(i).equals(it.next())) {
                    return false;
                }
            }
            return !it.hasNext();
        }

        public int indexOf(Object obj) {
            Objects.requireNonNull(obj);
            int size = size();
            for (int i = 0; i < size; i++) {
                if (obj.equals(get(i))) {
                    return i;
                }
            }
            return -1;
        }

        public int lastIndexOf(Object obj) {
            Objects.requireNonNull(obj);
            for (int size = size() - 1; size >= 0; size--) {
                if (obj.equals(get(size))) {
                    return size;
                }
            }
            return -1;
        }

        public int hashCode() {
            int size = size();
            int i = 1;
            for (int i2 = 0; i2 < size; i2++) {
                i = (i * 31) + get(i2).hashCode();
            }
            return i;
        }

        public boolean contains(Object obj) {
            return indexOf(obj) >= 0;
        }

        /* access modifiers changed from: package-private */
        public IndexOutOfBoundsException outOfBounds(int i) {
            return new IndexOutOfBoundsException("Index: " + i + " Size: " + size());
        }
    }

    static final class ListItr<E> implements ListIterator<E> {
        private int cursor;
        @Stable
        private final boolean isListIterator;
        @Stable
        private final List<E> list;
        @Stable
        private final int size;

        ListItr(List<E> list2, int i) {
            this.list = list2;
            this.size = i;
            this.cursor = 0;
            this.isListIterator = false;
        }

        ListItr(List<E> list2, int i, int i2) {
            this.list = list2;
            this.size = i;
            this.cursor = i2;
            this.isListIterator = true;
        }

        public boolean hasNext() {
            return this.cursor != this.size;
        }

        public E next() {
            try {
                int i = this.cursor;
                E e = this.list.get(i);
                this.cursor = i + 1;
                return e;
            } catch (IndexOutOfBoundsException unused) {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            throw ImmutableCollections.uoe();
        }

        public boolean hasPrevious() {
            if (this.isListIterator) {
                return this.cursor != 0;
            }
            throw ImmutableCollections.uoe();
        }

        public E previous() {
            if (this.isListIterator) {
                try {
                    int i = this.cursor - 1;
                    E e = this.list.get(i);
                    this.cursor = i;
                    return e;
                } catch (IndexOutOfBoundsException unused) {
                    throw new NoSuchElementException();
                }
            } else {
                throw ImmutableCollections.uoe();
            }
        }

        public int nextIndex() {
            if (this.isListIterator) {
                return this.cursor;
            }
            throw ImmutableCollections.uoe();
        }

        public int previousIndex() {
            if (this.isListIterator) {
                return this.cursor - 1;
            }
            throw ImmutableCollections.uoe();
        }

        public void set(E e) {
            throw ImmutableCollections.uoe();
        }

        public void add(E e) {
            throw ImmutableCollections.uoe();
        }
    }

    static final class SubList<E> extends AbstractImmutableList<E> implements RandomAccess {
        @Stable
        private final int offset;
        @Stable
        private final List<E> root;
        @Stable
        private final int size;

        private SubList(List<E> list, int i, int i2) {
            this.root = list;
            this.offset = i;
            this.size = i2;
        }

        static <E> SubList<E> fromSubList(SubList<E> subList, int i, int i2) {
            return new SubList<>(subList.root, subList.offset + i, i2 - i);
        }

        static <E> SubList<E> fromList(List<E> list, int i, int i2) {
            return new SubList<>(list, i, i2 - i);
        }

        public E get(int i) {
            Objects.checkIndex(i, this.size);
            return this.root.get(this.offset + i);
        }

        public int size() {
            return this.size;
        }

        public Iterator<E> iterator() {
            return new ListItr(this, size());
        }

        public ListIterator<E> listIterator(int i) {
            rangeCheck(i);
            return new ListItr(this, size(), i);
        }

        public List<E> subList(int i, int i2) {
            subListRangeCheck(i, i2, this.size);
            return fromSubList(this, i, i2);
        }

        private void rangeCheck(int i) {
            if (i < 0 || i > this.size) {
                throw outOfBounds(i);
            }
        }
    }

    static final class List12<E> extends AbstractImmutableList<E> implements Serializable {
        @Stable

        /* renamed from: e0 */
        private final E f692e0;
        @Stable

        /* renamed from: e1 */
        private final E f693e1;

        List12(E e) {
            this.f692e0 = Objects.requireNonNull(e);
            this.f693e1 = null;
        }

        List12(E e, E e2) {
            this.f692e0 = Objects.requireNonNull(e);
            this.f693e1 = Objects.requireNonNull(e2);
        }

        public int size() {
            return this.f693e1 != null ? 2 : 1;
        }

        public E get(int i) {
            E e;
            Objects.checkIndex(i, 2);
            if (i == 0) {
                return this.f692e0;
            }
            if (i == 1 && (e = this.f693e1) != null) {
                return e;
            }
            throw outOfBounds(i);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            E e = this.f693e1;
            if (e == null) {
                return new CollSer(1, this.f692e0);
            }
            return new CollSer(1, this.f692e0, e);
        }
    }

    static final class ListN<E> extends AbstractImmutableList<E> implements Serializable {
        @Stable
        static List<?> EMPTY_LIST = new ListN(new Object[0]);
        @Stable
        private final E[] elements;

        static {
            if (EMPTY_LIST == null) {
            }
        }

        @SafeVarargs
        ListN(E... eArr) {
            E[] eArr2 = new Object[eArr.length];
            for (int i = 0; i < eArr.length; i++) {
                eArr2[i] = Objects.requireNonNull(eArr[i]);
            }
            this.elements = eArr2;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public int size() {
            return this.elements.length;
        }

        public E get(int i) {
            return this.elements[i];
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            return new CollSer(1, this.elements);
        }
    }

    static abstract class AbstractImmutableSet<E> extends AbstractSet<E> implements Serializable {
        AbstractImmutableSet() {
        }

        public boolean add(E e) {
            throw ImmutableCollections.uoe();
        }

        public boolean addAll(Collection<? extends E> collection) {
            throw ImmutableCollections.uoe();
        }

        public void clear() {
            throw ImmutableCollections.uoe();
        }

        public boolean remove(Object obj) {
            throw ImmutableCollections.uoe();
        }

        public boolean removeAll(Collection<?> collection) {
            throw ImmutableCollections.uoe();
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            throw ImmutableCollections.uoe();
        }

        public boolean retainAll(Collection<?> collection) {
            throw ImmutableCollections.uoe();
        }
    }

    static final class Set0<E> extends AbstractImmutableSet<E> {
        private static final Set0<?> INSTANCE = new Set0<>();

        public int hashCode() {
            return 0;
        }

        public int size() {
            return 0;
        }

        static <T> Set0<T> instance() {
            return INSTANCE;
        }

        private Set0() {
        }

        public boolean contains(Object obj) {
            Objects.requireNonNull(obj);
            return false;
        }

        public boolean containsAll(Collection<?> collection) {
            return collection.isEmpty();
        }

        public Iterator<E> iterator() {
            return Collections.emptyIterator();
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            return new CollSer(2, new Object[0]);
        }
    }

    static final class Set1<E> extends AbstractImmutableSet<E> {
        @Stable

        /* renamed from: e0 */
        private final E f696e0;

        public int size() {
            return 1;
        }

        Set1(E e) {
            this.f696e0 = Objects.requireNonNull(e);
        }

        public boolean contains(Object obj) {
            return obj.equals(this.f696e0);
        }

        public Iterator<E> iterator() {
            return Collections.singletonIterator(this.f696e0);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            return new CollSer(2, this.f696e0);
        }

        public int hashCode() {
            return this.f696e0.hashCode();
        }
    }

    static final class Set2<E> extends AbstractImmutableSet<E> {
        @Stable

        /* renamed from: e0 */
        final E f697e0;
        @Stable

        /* renamed from: e1 */
        final E f698e1;

        public int size() {
            return 2;
        }

        Set2(E e, E e2) {
            if (e.equals(Objects.requireNonNull(e2))) {
                throw new IllegalArgumentException("duplicate element: " + e);
            } else if (ImmutableCollections.SALT >= 0) {
                this.f697e0 = e;
                this.f698e1 = e2;
            } else {
                this.f697e0 = e2;
                this.f698e1 = e;
            }
        }

        public boolean contains(Object obj) {
            return obj.equals(this.f697e0) || obj.equals(this.f698e1);
        }

        public int hashCode() {
            return this.f697e0.hashCode() + this.f698e1.hashCode();
        }

        public Iterator<E> iterator() {
            return new Iterator<E>() {
                private int idx = 0;

                public boolean hasNext() {
                    return this.idx < 2;
                }

                public E next() {
                    int i = this.idx;
                    if (i == 0) {
                        this.idx = 1;
                        return Set2.this.f697e0;
                    } else if (i == 1) {
                        this.idx = 2;
                        return Set2.this.f698e1;
                    } else {
                        throw new NoSuchElementException();
                    }
                }
            };
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            return new CollSer(2, this.f697e0, this.f698e1);
        }
    }

    static final class SetN<E> extends AbstractImmutableSet<E> implements Serializable {
        @Stable
        static Set<?> EMPTY_SET = new SetN(new Object[0]);
        @Stable
        final E[] elements;
        @Stable
        final int size;

        static {
            if (EMPTY_SET == null) {
            }
        }

        @SafeVarargs
        SetN(E... eArr) {
            this.size = eArr.length;
            this.elements = new Object[(eArr.length * 2)];
            int i = 0;
            while (i < eArr.length) {
                E e = eArr[i];
                int probe = probe(e);
                if (probe < 0) {
                    this.elements[-(probe + 1)] = e;
                    i++;
                } else {
                    throw new IllegalArgumentException("duplicate element: " + e);
                }
            }
        }

        public int size() {
            return this.size;
        }

        public boolean contains(Object obj) {
            return probe(obj) >= 0;
        }

        public Iterator<E> iterator() {
            return new Iterator<E>() {
                private int idx = 0;

                public boolean hasNext() {
                    while (this.idx < SetN.this.elements.length) {
                        E[] eArr = SetN.this.elements;
                        int i = this.idx;
                        if (eArr[i] != null) {
                            return true;
                        }
                        this.idx = i + 1;
                    }
                    return false;
                }

                public E next() {
                    if (hasNext()) {
                        E[] eArr = SetN.this.elements;
                        int i = this.idx;
                        this.idx = i + 1;
                        return eArr[i];
                    }
                    throw new NoSuchElementException();
                }
            };
        }

        public int hashCode() {
            int i = 0;
            for (E e : this.elements) {
                if (e != null) {
                    i += e.hashCode();
                }
            }
            return i;
        }

        private int probe(Object obj) {
            int floorMod = Math.floorMod(obj.hashCode() ^ ImmutableCollections.SALT, this.elements.length);
            while (true) {
                E e = this.elements[floorMod];
                if (e == null) {
                    return (-floorMod) - 1;
                }
                if (obj.equals(e)) {
                    return floorMod;
                }
                floorMod++;
                if (floorMod == this.elements.length) {
                    floorMod = 0;
                }
            }
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            Object[] objArr = new Object[this.size];
            int i = 0;
            for (E e : this.elements) {
                if (e != null) {
                    objArr[i] = e;
                    i++;
                }
            }
            return new CollSer(2, objArr);
        }
    }

    static abstract class AbstractImmutableMap<K, V> extends AbstractMap<K, V> implements Serializable {
        AbstractImmutableMap() {
        }

        public void clear() {
            throw ImmutableCollections.uoe();
        }

        public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw ImmutableCollections.uoe();
        }

        public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
            throw ImmutableCollections.uoe();
        }

        public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw ImmutableCollections.uoe();
        }

        public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            throw ImmutableCollections.uoe();
        }

        public V put(K k, V v) {
            throw ImmutableCollections.uoe();
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            throw ImmutableCollections.uoe();
        }

        public V putIfAbsent(K k, V v) {
            throw ImmutableCollections.uoe();
        }

        public V remove(Object obj) {
            throw ImmutableCollections.uoe();
        }

        public boolean remove(Object obj, Object obj2) {
            throw ImmutableCollections.uoe();
        }

        public V replace(K k, V v) {
            throw ImmutableCollections.uoe();
        }

        public boolean replace(K k, V v, V v2) {
            throw ImmutableCollections.uoe();
        }

        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw ImmutableCollections.uoe();
        }
    }

    static final class Map0<K, V> extends AbstractImmutableMap<K, V> {
        private static final Map0<?, ?> INSTANCE = new Map0<>();

        public int hashCode() {
            return 0;
        }

        static <K, V> Map0<K, V> instance() {
            return INSTANCE;
        }

        private Map0() {
        }

        public Set<Map.Entry<K, V>> entrySet() {
            return Set.m1750of();
        }

        public boolean containsKey(Object obj) {
            Objects.requireNonNull(obj);
            return false;
        }

        public boolean containsValue(Object obj) {
            Objects.requireNonNull(obj);
            return false;
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            return new CollSer(3, new Object[0]);
        }
    }

    static final class Map1<K, V> extends AbstractImmutableMap<K, V> {
        @Stable

        /* renamed from: k0 */
        private final K f694k0;
        @Stable

        /* renamed from: v0 */
        private final V f695v0;

        Map1(K k, V v) {
            this.f694k0 = Objects.requireNonNull(k);
            this.f695v0 = Objects.requireNonNull(v);
        }

        public Set<Map.Entry<K, V>> entrySet() {
            return Set.m1751of(new KeyValueHolder(this.f694k0, this.f695v0));
        }

        public V get(Object obj) {
            if (obj.equals(this.f694k0)) {
                return this.f695v0;
            }
            return null;
        }

        public boolean containsKey(Object obj) {
            return obj.equals(this.f694k0);
        }

        public boolean containsValue(Object obj) {
            return obj.equals(this.f695v0);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            return new CollSer(3, this.f694k0, this.f695v0);
        }

        public int hashCode() {
            return this.f695v0.hashCode() ^ this.f694k0.hashCode();
        }
    }

    static final class MapN<K, V> extends AbstractImmutableMap<K, V> {
        @Stable
        static Map<?, ?> EMPTY_MAP = new MapN(new Object[0]);
        @Stable
        final int size;
        @Stable
        final Object[] table;

        static {
            if (EMPTY_MAP == null) {
            }
        }

        MapN(Object... objArr) {
            if ((objArr.length & 1) == 0) {
                this.size = objArr.length >> 1;
                this.table = new Object[(((objArr.length * 2) + 1) & -2)];
                int i = 0;
                while (i < objArr.length) {
                    Object requireNonNull = Objects.requireNonNull(objArr[i]);
                    Object requireNonNull2 = Objects.requireNonNull(objArr[i + 1]);
                    int probe = probe(requireNonNull);
                    if (probe < 0) {
                        int i2 = -(probe + 1);
                        Object[] objArr2 = this.table;
                        objArr2[i2] = requireNonNull;
                        objArr2[i2 + 1] = requireNonNull2;
                        i += 2;
                    } else {
                        throw new IllegalArgumentException("duplicate key: " + requireNonNull);
                    }
                }
                return;
            }
            throw new InternalError("length is odd");
        }

        public boolean containsKey(Object obj) {
            return probe(obj) >= 0;
        }

        public boolean containsValue(Object obj) {
            int i = 1;
            while (true) {
                Object[] objArr = this.table;
                if (i >= objArr.length) {
                    return false;
                }
                Object obj2 = objArr[i];
                if (obj2 != null && obj.equals(obj2)) {
                    return true;
                }
                i += 2;
            }
        }

        public int hashCode() {
            int i = 0;
            int i2 = 0;
            while (true) {
                Object[] objArr = this.table;
                if (i >= objArr.length) {
                    return i2;
                }
                Object obj = objArr[i];
                if (obj != null) {
                    i2 += obj.hashCode() ^ this.table[i + 1].hashCode();
                }
                i += 2;
            }
        }

        public V get(Object obj) {
            int probe = probe(obj);
            if (probe >= 0) {
                return this.table[probe + 1];
            }
            return null;
        }

        public int size() {
            return this.size;
        }

        public Set<Map.Entry<K, V>> entrySet() {
            return new AbstractSet<Map.Entry<K, V>>() {
                public int size() {
                    return MapN.this.size;
                }

                public Iterator<Map.Entry<K, V>> iterator() {
                    return new Iterator<Map.Entry<K, V>>() {
                        int idx = 0;

                        public boolean hasNext() {
                            while (this.idx < MapN.this.table.length) {
                                Object[] objArr = MapN.this.table;
                                int i = this.idx;
                                if (objArr[i] != null) {
                                    return true;
                                }
                                this.idx = i + 2;
                            }
                            return false;
                        }

                        public Map.Entry<K, V> next() {
                            if (hasNext()) {
                                KeyValueHolder keyValueHolder = new KeyValueHolder(MapN.this.table[this.idx], MapN.this.table[this.idx + 1]);
                                this.idx += 2;
                                return keyValueHolder;
                            }
                            throw new NoSuchElementException();
                        }
                    };
                }
            };
        }

        private int probe(Object obj) {
            int floorMod = Math.floorMod(obj.hashCode() ^ ImmutableCollections.SALT, this.table.length >> 1) << 1;
            while (true) {
                Object obj2 = this.table[floorMod];
                if (obj2 == null) {
                    return (-floorMod) - 1;
                }
                if (obj.equals(obj2)) {
                    return floorMod;
                }
                floorMod += 2;
                if (floorMod == this.table.length) {
                    floorMod = 0;
                }
            }
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            throw new InvalidObjectException("not serial proxy");
        }

        private Object writeReplace() {
            Object[] objArr = new Object[(this.size * 2)];
            int length = this.table.length;
            int i = 0;
            for (int i2 = 0; i2 < length; i2 += 2) {
                Object[] objArr2 = this.table;
                Object obj = objArr2[i2];
                if (obj != null) {
                    int i3 = i + 1;
                    objArr[i] = obj;
                    i = i3 + 1;
                    objArr[i3] = objArr2[i2 + 1];
                }
            }
            return new CollSer(3, objArr);
        }
    }
}
