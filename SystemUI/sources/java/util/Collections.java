package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import dalvik.system.VMRuntime;
import java.lang.reflect.Array;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Collections {
    private static final int BINARYSEARCH_THRESHOLD = 5000;
    private static final int COPY_THRESHOLD = 10;
    public static final List EMPTY_LIST = new EmptyList();
    public static final Map EMPTY_MAP = new EmptyMap();
    public static final Set EMPTY_SET = new EmptySet();
    private static final int FILL_THRESHOLD = 25;
    private static final int INDEXOFSUBLIST_THRESHOLD = 35;
    private static final int REPLACEALL_THRESHOLD = 11;
    private static final int REVERSE_THRESHOLD = 18;
    private static final int ROTATE_THRESHOLD = 100;
    private static final int SHUFFLE_THRESHOLD = 5;

    /* renamed from: r */
    private static Random f641r;

    private Collections() {
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        sort(list, (Comparator) null);
    }

    public static <T> void sort(List<T> list, Comparator<? super T> comparator) {
        if (VMRuntime.getRuntime().getTargetSdkVersion() > 25) {
            list.sort(comparator);
            return;
        }
        if (list.getClass() == ArrayList.class) {
            Arrays.sort(((ArrayList) list).elementData, 0, list.size(), comparator);
            return;
        }
        Object[] array = list.toArray();
        Arrays.sort(array, comparator);
        ListIterator<T> listIterator = list.listIterator();
        for (Object obj : array) {
            listIterator.next();
            listIterator.set(obj);
        }
    }

    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T t) {
        if ((list instanceof RandomAccess) || list.size() < 5000) {
            return indexedBinarySearch(list, t);
        }
        return iteratorBinarySearch(list, t);
    }

    private static <T> int indexedBinarySearch(List<? extends Comparable<? super T>> list, T t) {
        int size = list.size() - 1;
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) >>> 1;
            int compareTo = ((Comparable) list.get(i2)).compareTo(t);
            if (compareTo < 0) {
                i = i2 + 1;
            } else if (compareTo <= 0) {
                return i2;
            } else {
                size = i2 - 1;
            }
        }
        return -(i + 1);
    }

    private static <T> int iteratorBinarySearch(List<? extends Comparable<? super T>> list, T t) {
        int size = list.size() - 1;
        ListIterator<? extends Comparable<? super T>> listIterator = list.listIterator();
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) >>> 1;
            int compareTo = ((Comparable) get(listIterator, i2)).compareTo(t);
            if (compareTo < 0) {
                i = i2 + 1;
            } else if (compareTo <= 0) {
                return i2;
            } else {
                size = i2 - 1;
            }
        }
        return -(i + 1);
    }

    private static <T> T get(ListIterator<? extends T> listIterator, int i) {
        T t;
        int nextIndex = listIterator.nextIndex();
        if (nextIndex <= i) {
            while (true) {
                t = listIterator.next();
                int i2 = nextIndex + 1;
                if (nextIndex >= i) {
                    break;
                }
                nextIndex = i2;
            }
        } else {
            do {
                t = listIterator.previous();
                nextIndex--;
            } while (nextIndex > i);
        }
        return t;
    }

    public static <T> int binarySearch(List<? extends T> list, T t, Comparator<? super T> comparator) {
        if (comparator == null) {
            return binarySearch(list, t);
        }
        if ((list instanceof RandomAccess) || list.size() < 5000) {
            return indexedBinarySearch(list, t, comparator);
        }
        return iteratorBinarySearch(list, t, comparator);
    }

    private static <T> int indexedBinarySearch(List<? extends T> list, T t, Comparator<? super T> comparator) {
        int size = list.size() - 1;
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) >>> 1;
            int compare = comparator.compare(list.get(i2), t);
            if (compare < 0) {
                i = i2 + 1;
            } else if (compare <= 0) {
                return i2;
            } else {
                size = i2 - 1;
            }
        }
        return -(i + 1);
    }

    private static <T> int iteratorBinarySearch(List<? extends T> list, T t, Comparator<? super T> comparator) {
        int size = list.size() - 1;
        ListIterator<? extends T> listIterator = list.listIterator();
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) >>> 1;
            int compare = comparator.compare(get(listIterator, i2), t);
            if (compare < 0) {
                i = i2 + 1;
            } else if (compare <= 0) {
                return i2;
            } else {
                size = i2 - 1;
            }
        }
        return -(i + 1);
    }

    public static void reverse(List<?> list) {
        int size = list.size();
        int i = 0;
        if (size < 18 || (list instanceof RandomAccess)) {
            int i2 = size >> 1;
            int i3 = size - 1;
            while (i < i2) {
                swap(list, i, i3);
                i++;
                i3--;
            }
            return;
        }
        ListIterator<?> listIterator = list.listIterator();
        ListIterator<?> listIterator2 = list.listIterator(size);
        int size2 = list.size() >> 1;
        while (i < size2) {
            Object next = listIterator.next();
            listIterator.set(listIterator2.previous());
            listIterator2.set(next);
            i++;
        }
    }

    public static void shuffle(List<?> list) {
        Random random = f641r;
        if (random == null) {
            random = new Random();
            f641r = random;
        }
        shuffle(list, random);
    }

    public static void shuffle(List<?> list, Random random) {
        int size = list.size();
        if (size < 5 || (list instanceof RandomAccess)) {
            while (size > 1) {
                swap(list, size - 1, random.nextInt(size));
                size--;
            }
            return;
        }
        Object[] array = list.toArray();
        while (size > 1) {
            swap(array, size - 1, random.nextInt(size));
            size--;
        }
        ListIterator<?> listIterator = list.listIterator();
        for (Object obj : array) {
            listIterator.next();
            listIterator.set(obj);
        }
    }

    public static void swap(List<?> list, int i, int i2) {
        list.set(i, list.set(i2, list.get(i)));
    }

    private static void swap(Object[] objArr, int i, int i2) {
        Object obj = objArr[i];
        objArr[i] = objArr[i2];
        objArr[i2] = obj;
    }

    public static <T> void fill(List<? super T> list, T t) {
        int size = list.size();
        int i = 0;
        if (size < 25 || (list instanceof RandomAccess)) {
            while (i < size) {
                list.set(i, t);
                i++;
            }
            return;
        }
        ListIterator<? super T> listIterator = list.listIterator();
        while (i < size) {
            listIterator.next();
            listIterator.set(t);
            i++;
        }
    }

    public static <T> void copy(List<? super T> list, List<? extends T> list2) {
        int size = list2.size();
        if (size <= list.size()) {
            int i = 0;
            if (size < 10 || ((list2 instanceof RandomAccess) && (list instanceof RandomAccess))) {
                while (i < size) {
                    list.set(i, list2.get(i));
                    i++;
                }
                return;
            }
            ListIterator<? super T> listIterator = list.listIterator();
            ListIterator<? extends T> listIterator2 = list2.listIterator();
            while (i < size) {
                listIterator.next();
                listIterator.set(listIterator2.next());
                i++;
            }
            return;
        }
        throw new IndexOutOfBoundsException("Source does not fit in dest");
    }

    public static <T extends Comparable<? super T>> T min(Collection<? extends T> collection) {
        Iterator<? extends T> it = collection.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (((Comparable) next2).compareTo(next) < 0) {
                next = next2;
            }
        }
        return next;
    }

    public static <T> T min(Collection<? extends T> collection, Comparator<? super T> comparator) {
        if (comparator == null) {
            return min(collection);
        }
        Iterator<? extends T> it = collection.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (comparator.compare(next2, next) < 0) {
                next = next2;
            }
        }
        return next;
    }

    public static <T extends Comparable<? super T>> T max(Collection<? extends T> collection) {
        Iterator<? extends T> it = collection.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (((Comparable) next2).compareTo(next) > 0) {
                next = next2;
            }
        }
        return next;
    }

    public static <T> T max(Collection<? extends T> collection, Comparator<? super T> comparator) {
        if (comparator == null) {
            return max(collection);
        }
        Iterator<? extends T> it = collection.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (comparator.compare(next2, next) > 0) {
                next = next2;
            }
        }
        return next;
    }

    public static void rotate(List<?> list, int i) {
        if ((list instanceof RandomAccess) || list.size() < 100) {
            rotate1(list, i);
        } else {
            rotate2(list, i);
        }
    }

    private static <T> void rotate1(List<T> list, int i) {
        int size = list.size();
        if (size != 0) {
            int i2 = i % size;
            if (i2 < 0) {
                i2 += size;
            }
            if (i2 != 0) {
                int i3 = 0;
                int i4 = 0;
                while (i3 != size) {
                    T t = list.get(i4);
                    int i5 = i4;
                    do {
                        i5 += i2;
                        if (i5 >= size) {
                            i5 -= size;
                        }
                        t = list.set(i5, t);
                        i3++;
                    } while (i5 != i4);
                    i4++;
                }
            }
        }
    }

    private static void rotate2(List<?> list, int i) {
        int size = list.size();
        if (size != 0) {
            int i2 = (-i) % size;
            if (i2 < 0) {
                i2 += size;
            }
            if (i2 != 0) {
                reverse(list.subList(0, i2));
                reverse(list.subList(i2, size));
                reverse(list);
            }
        }
    }

    public static <T> boolean replaceAll(List<T> list, T t, T t2) {
        boolean z;
        int size = list.size();
        int i = 0;
        if (size >= 11 && !(list instanceof RandomAccess)) {
            ListIterator<T> listIterator = list.listIterator();
            if (t == null) {
                boolean z2 = false;
                while (i < size) {
                    if (listIterator.next() == null) {
                        listIterator.set(t2);
                        z2 = true;
                    }
                    i++;
                }
                return z2;
            }
            z = false;
            while (i < size) {
                if (t.equals(listIterator.next())) {
                    listIterator.set(t2);
                    z = true;
                }
                i++;
            }
        } else if (t == null) {
            boolean z3 = false;
            while (i < size) {
                if (list.get(i) == null) {
                    list.set(i, t2);
                    z3 = true;
                }
                i++;
            }
            return z3;
        } else {
            z = false;
            while (i < size) {
                if (t.equals(list.get(i))) {
                    list.set(i, t2);
                    z = true;
                }
                i++;
            }
        }
        return z;
    }

    public static int indexOfSubList(List<?> list, List<?> list2) {
        int size = list.size();
        int size2 = list2.size();
        int i = size - size2;
        if (size < 35 || ((list instanceof RandomAccess) && (list2 instanceof RandomAccess))) {
            int i2 = 0;
            while (i2 <= i) {
                int i3 = i2;
                int i4 = 0;
                while (i4 < size2) {
                    if (!m1715eq(list2.get(i4), list.get(i3))) {
                        i2++;
                    } else {
                        i4++;
                        i3++;
                    }
                }
                return i2;
            }
            return -1;
        }
        ListIterator<?> listIterator = list.listIterator();
        int i5 = 0;
        while (i5 <= i) {
            ListIterator<?> listIterator2 = list2.listIterator();
            int i6 = 0;
            while (i6 < size2) {
                if (!m1715eq(listIterator2.next(), listIterator.next())) {
                    for (int i7 = 0; i7 < i6; i7++) {
                        listIterator.previous();
                    }
                    i5++;
                } else {
                    i6++;
                }
            }
            return i5;
        }
        return -1;
    }

    public static int lastIndexOfSubList(List<?> list, List<?> list2) {
        int size = list.size();
        int size2 = list2.size();
        int i = size - size2;
        if (size < 35 || (list instanceof RandomAccess)) {
            while (i >= 0) {
                int i2 = i;
                int i3 = 0;
                while (i3 < size2) {
                    if (!m1715eq(list2.get(i3), list.get(i2))) {
                        i--;
                    } else {
                        i3++;
                        i2++;
                    }
                }
                return i;
            }
        } else if (i < 0) {
            return -1;
        } else {
            ListIterator<?> listIterator = list.listIterator(i);
            while (i >= 0) {
                ListIterator<?> listIterator2 = list2.listIterator();
                int i4 = 0;
                while (i4 < size2) {
                    if (!m1715eq(listIterator2.next(), listIterator.next())) {
                        if (i != 0) {
                            for (int i5 = 0; i5 <= i4 + 1; i5++) {
                                listIterator.previous();
                            }
                        }
                        i--;
                    } else {
                        i4++;
                    }
                }
                return i;
            }
        }
        return -1;
    }

    public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> collection) {
        return new UnmodifiableCollection(collection);
    }

    static class UnmodifiableCollection<E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 1820017752578914078L;

        /* renamed from: c */
        final Collection<? extends E> f663c;

        UnmodifiableCollection(Collection<? extends E> collection) {
            collection.getClass();
            this.f663c = collection;
        }

        public int size() {
            return this.f663c.size();
        }

        public boolean isEmpty() {
            return this.f663c.isEmpty();
        }

        public boolean contains(Object obj) {
            return this.f663c.contains(obj);
        }

        public Object[] toArray() {
            return this.f663c.toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return this.f663c.toArray(tArr);
        }

        public String toString() {
            return this.f663c.toString();
        }

        public Iterator<E> iterator() {
            return new Iterator<E>() {

                /* renamed from: i */
                private final Iterator<? extends E> f664i;

                {
                    this.f664i = UnmodifiableCollection.this.f663c.iterator();
                }

                public boolean hasNext() {
                    return this.f664i.hasNext();
                }

                public E next() {
                    return this.f664i.next();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                public void forEachRemaining(Consumer<? super E> consumer) {
                    this.f664i.forEachRemaining(consumer);
                }
            };
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        public boolean containsAll(Collection<?> collection) {
            return this.f663c.containsAll(collection);
        }

        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public void forEach(Consumer<? super E> consumer) {
            this.f663c.forEach(consumer);
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            throw new UnsupportedOperationException();
        }

        public Spliterator<E> spliterator() {
            return this.f663c.spliterator();
        }

        public Stream<E> stream() {
            return this.f663c.stream();
        }

        public Stream<E> parallelStream() {
            return this.f663c.parallelStream();
        }
    }

    public static <T> Set<T> unmodifiableSet(Set<? extends T> set) {
        return new UnmodifiableSet(set);
    }

    static class UnmodifiableSet<E> extends UnmodifiableCollection<E> implements Set<E>, Serializable {
        private static final long serialVersionUID = -9215047833775013803L;

        UnmodifiableSet(Set<? extends E> set) {
            super(set);
        }

        public boolean equals(Object obj) {
            return obj == this || this.f663c.equals(obj);
        }

        public int hashCode() {
            return this.f663c.hashCode();
        }
    }

    public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<T> sortedSet) {
        return new UnmodifiableSortedSet(sortedSet);
    }

    static class UnmodifiableSortedSet<E> extends UnmodifiableSet<E> implements SortedSet<E>, Serializable {
        private static final long serialVersionUID = -4929149591599911165L;

        /* renamed from: ss */
        private final SortedSet<E> f673ss;

        UnmodifiableSortedSet(SortedSet<E> sortedSet) {
            super(sortedSet);
            this.f673ss = sortedSet;
        }

        public Comparator<? super E> comparator() {
            return this.f673ss.comparator();
        }

        public SortedSet<E> subSet(E e, E e2) {
            return new UnmodifiableSortedSet(this.f673ss.subSet(e, e2));
        }

        public SortedSet<E> headSet(E e) {
            return new UnmodifiableSortedSet(this.f673ss.headSet(e));
        }

        public SortedSet<E> tailSet(E e) {
            return new UnmodifiableSortedSet(this.f673ss.tailSet(e));
        }

        public E first() {
            return this.f673ss.first();
        }

        public E last() {
            return this.f673ss.last();
        }
    }

    public static <T> NavigableSet<T> unmodifiableNavigableSet(NavigableSet<T> navigableSet) {
        return new UnmodifiableNavigableSet(navigableSet);
    }

    static class UnmodifiableNavigableSet<E> extends UnmodifiableSortedSet<E> implements NavigableSet<E>, Serializable {
        /* access modifiers changed from: private */
        public static final NavigableSet<?> EMPTY_NAVIGABLE_SET = new EmptyNavigableSet();
        private static final long serialVersionUID = -6027448201786391929L;

        /* renamed from: ns */
        private final NavigableSet<E> f671ns;

        private static class EmptyNavigableSet<E> extends UnmodifiableNavigableSet<E> implements Serializable {
            private static final long serialVersionUID = -6291252904449939134L;

            public EmptyNavigableSet() {
                super(new TreeSet());
            }

            private Object readResolve() {
                return UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
            }
        }

        UnmodifiableNavigableSet(NavigableSet<E> navigableSet) {
            super(navigableSet);
            this.f671ns = navigableSet;
        }

        public E lower(E e) {
            return this.f671ns.lower(e);
        }

        public E floor(E e) {
            return this.f671ns.floor(e);
        }

        public E ceiling(E e) {
            return this.f671ns.ceiling(e);
        }

        public E higher(E e) {
            return this.f671ns.higher(e);
        }

        public E pollFirst() {
            throw new UnsupportedOperationException();
        }

        public E pollLast() {
            throw new UnsupportedOperationException();
        }

        public NavigableSet<E> descendingSet() {
            return new UnmodifiableNavigableSet(this.f671ns.descendingSet());
        }

        public Iterator<E> descendingIterator() {
            return descendingSet().iterator();
        }

        public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
            return new UnmodifiableNavigableSet(this.f671ns.subSet(e, z, e2, z2));
        }

        public NavigableSet<E> headSet(E e, boolean z) {
            return new UnmodifiableNavigableSet(this.f671ns.headSet(e, z));
        }

        public NavigableSet<E> tailSet(E e, boolean z) {
            return new UnmodifiableNavigableSet(this.f671ns.tailSet(e, z));
        }
    }

    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        if (list instanceof RandomAccess) {
            return new UnmodifiableRandomAccessList(list);
        }
        return new UnmodifiableList(list);
    }

    static class UnmodifiableList<E> extends UnmodifiableCollection<E> implements List<E> {
        private static final long serialVersionUID = -283967356065247728L;
        final List<? extends E> list;

        UnmodifiableList(List<? extends E> list2) {
            super(list2);
            this.list = list2;
        }

        public boolean equals(Object obj) {
            return obj == this || this.list.equals(obj);
        }

        public int hashCode() {
            return this.list.hashCode();
        }

        public E get(int i) {
            return this.list.get(i);
        }

        public E set(int i, E e) {
            throw new UnsupportedOperationException();
        }

        public void add(int i, E e) {
            throw new UnsupportedOperationException();
        }

        public E remove(int i) {
            throw new UnsupportedOperationException();
        }

        public int indexOf(Object obj) {
            return this.list.indexOf(obj);
        }

        public int lastIndexOf(Object obj) {
            return this.list.lastIndexOf(obj);
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        public void replaceAll(UnaryOperator<E> unaryOperator) {
            throw new UnsupportedOperationException();
        }

        public void sort(Comparator<? super E> comparator) {
            throw new UnsupportedOperationException();
        }

        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        public ListIterator<E> listIterator(int i) {
            return new ListIterator<E>(i) {

                /* renamed from: i */
                private final ListIterator<? extends E> f665i;
                final /* synthetic */ int val$index;

                {
                    this.val$index = r2;
                    this.f665i = UnmodifiableList.this.list.listIterator(r2);
                }

                public boolean hasNext() {
                    return this.f665i.hasNext();
                }

                public E next() {
                    return this.f665i.next();
                }

                public boolean hasPrevious() {
                    return this.f665i.hasPrevious();
                }

                public E previous() {
                    return this.f665i.previous();
                }

                public int nextIndex() {
                    return this.f665i.nextIndex();
                }

                public int previousIndex() {
                    return this.f665i.previousIndex();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                public void set(E e) {
                    throw new UnsupportedOperationException();
                }

                public void add(E e) {
                    throw new UnsupportedOperationException();
                }

                public void forEachRemaining(Consumer<? super E> consumer) {
                    this.f665i.forEachRemaining(consumer);
                }
            };
        }

        public List<E> subList(int i, int i2) {
            return new UnmodifiableList(this.list.subList(i, i2));
        }

        private Object readResolve() {
            List<? extends E> list2 = this.list;
            return list2 instanceof RandomAccess ? new UnmodifiableRandomAccessList(list2) : this;
        }
    }

    static class UnmodifiableRandomAccessList<E> extends UnmodifiableList<E> implements RandomAccess {
        private static final long serialVersionUID = -2542308836966382001L;

        UnmodifiableRandomAccessList(List<? extends E> list) {
            super(list);
        }

        public List<E> subList(int i, int i2) {
            return new UnmodifiableRandomAccessList(this.list.subList(i, i2));
        }

        private Object writeReplace() {
            return new UnmodifiableList(this.list);
        }
    }

    public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> map) {
        return new UnmodifiableMap(map);
    }

    private static class UnmodifiableMap<K, V> implements Map<K, V>, Serializable {
        private static final long serialVersionUID = -1034234728574286014L;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Set<K> keySet;

        /* renamed from: m */
        private final Map<? extends K, ? extends V> f666m;
        private transient Collection<V> values;

        UnmodifiableMap(Map<? extends K, ? extends V> map) {
            map.getClass();
            this.f666m = map;
        }

        public int size() {
            return this.f666m.size();
        }

        public boolean isEmpty() {
            return this.f666m.isEmpty();
        }

        public boolean containsKey(Object obj) {
            return this.f666m.containsKey(obj);
        }

        public boolean containsValue(Object obj) {
            return this.f666m.containsValue(obj);
        }

        public V get(Object obj) {
            return this.f666m.get(obj);
        }

        public V put(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public V remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Set<K> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.unmodifiableSet(this.f666m.keySet());
            }
            return this.keySet;
        }

        public Set<Map.Entry<K, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = new UnmodifiableEntrySet(this.f666m.entrySet());
            }
            return this.entrySet;
        }

        public Collection<V> values() {
            if (this.values == null) {
                this.values = Collections.unmodifiableCollection(this.f666m.values());
            }
            return this.values;
        }

        public boolean equals(Object obj) {
            return obj == this || this.f666m.equals(obj);
        }

        public int hashCode() {
            return this.f666m.hashCode();
        }

        public String toString() {
            return this.f666m.toString();
        }

        public V getOrDefault(Object obj, V v) {
            return this.f666m.getOrDefault(obj, v);
        }

        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            this.f666m.forEach(biConsumer);
        }

        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        public V putIfAbsent(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object obj, Object obj2) {
            throw new UnsupportedOperationException();
        }

        public boolean replace(K k, V v, V v2) {
            throw new UnsupportedOperationException();
        }

        public V replace(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        static class UnmodifiableEntrySet<K, V> extends UnmodifiableSet<Map.Entry<K, V>> {
            private static final long serialVersionUID = 7854390611657943733L;

            UnmodifiableEntrySet(Set<? extends Map.Entry<? extends K, ? extends V>> set) {
                super(set);
            }

            static <K, V> Consumer<Map.Entry<K, V>> entryConsumer(Consumer<? super Map.Entry<K, V>> consumer) {
                return new C4383x2c563d70(consumer);
            }

            public void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
                Objects.requireNonNull(consumer);
                this.f663c.forEach(entryConsumer(consumer));
            }

            static final class UnmodifiableEntrySetSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {

                /* renamed from: s */
                final Spliterator<Map.Entry<K, V>> f669s;

                UnmodifiableEntrySetSpliterator(Spliterator<Map.Entry<K, V>> spliterator) {
                    this.f669s = spliterator;
                }

                public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
                    Objects.requireNonNull(consumer);
                    return this.f669s.tryAdvance(UnmodifiableEntrySet.entryConsumer(consumer));
                }

                public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
                    Objects.requireNonNull(consumer);
                    this.f669s.forEachRemaining(UnmodifiableEntrySet.entryConsumer(consumer));
                }

                public Spliterator<Map.Entry<K, V>> trySplit() {
                    Spliterator<Map.Entry<K, V>> trySplit = this.f669s.trySplit();
                    if (trySplit == null) {
                        return null;
                    }
                    return new UnmodifiableEntrySetSpliterator(trySplit);
                }

                public long estimateSize() {
                    return this.f669s.estimateSize();
                }

                public long getExactSizeIfKnown() {
                    return this.f669s.getExactSizeIfKnown();
                }

                public int characteristics() {
                    return this.f669s.characteristics();
                }

                public boolean hasCharacteristics(int i) {
                    return this.f669s.hasCharacteristics(i);
                }

                public Comparator<? super Map.Entry<K, V>> getComparator() {
                    return this.f669s.getComparator();
                }
            }

            public Spliterator<Map.Entry<K, V>> spliterator() {
                return new UnmodifiableEntrySetSpliterator(this.f663c.spliterator());
            }

            public Stream<Map.Entry<K, V>> stream() {
                return StreamSupport.stream(spliterator(), false);
            }

            public Stream<Map.Entry<K, V>> parallelStream() {
                return StreamSupport.stream(spliterator(), true);
            }

            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>() {

                    /* renamed from: i */
                    private final Iterator<? extends Map.Entry<? extends K, ? extends V>> f667i;

                    {
                        this.f667i = UnmodifiableEntrySet.this.f663c.iterator();
                    }

                    public boolean hasNext() {
                        return this.f667i.hasNext();
                    }

                    public Map.Entry<K, V> next() {
                        return new UnmodifiableEntry((Map.Entry) this.f667i.next());
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            public Object[] toArray() {
                Object[] array = this.f663c.toArray();
                for (int i = 0; i < array.length; i++) {
                    array[i] = new UnmodifiableEntry((Map.Entry) array[i]);
                }
                return array;
            }

            public <T> T[] toArray(T[] tArr) {
                T[] array = this.f663c.toArray(tArr.length == 0 ? tArr : Arrays.copyOf(tArr, 0));
                for (int i = 0; i < array.length; i++) {
                    array[i] = new UnmodifiableEntry((Map.Entry) array[i]);
                }
                if (array.length > tArr.length) {
                    return array;
                }
                System.arraycopy((Object) array, 0, (Object) tArr, 0, array.length);
                if (tArr.length > array.length) {
                    tArr[array.length] = null;
                }
                return tArr;
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                return this.f663c.contains(new UnmodifiableEntry((Map.Entry) obj));
            }

            public boolean containsAll(Collection<?> collection) {
                for (Object contains : collection) {
                    if (!contains(contains)) {
                        return false;
                    }
                }
                return true;
            }

            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (!(obj instanceof Set)) {
                    return false;
                }
                Set set = (Set) obj;
                if (set.size() != this.f663c.size()) {
                    return false;
                }
                return containsAll(set);
            }

            private static class UnmodifiableEntry<K, V> implements Map.Entry<K, V> {

                /* renamed from: e */
                private Map.Entry<? extends K, ? extends V> f668e;

                UnmodifiableEntry(Map.Entry<? extends K, ? extends V> entry) {
                    this.f668e = (Map.Entry) Objects.requireNonNull(entry);
                }

                public K getKey() {
                    return this.f668e.getKey();
                }

                public V getValue() {
                    return this.f668e.getValue();
                }

                public V setValue(V v) {
                    throw new UnsupportedOperationException();
                }

                public int hashCode() {
                    return this.f668e.hashCode();
                }

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (!(obj instanceof Map.Entry)) {
                        return false;
                    }
                    Map.Entry entry = (Map.Entry) obj;
                    if (!Collections.m1715eq(this.f668e.getKey(), entry.getKey()) || !Collections.m1715eq(this.f668e.getValue(), entry.getValue())) {
                        return false;
                    }
                    return true;
                }

                public String toString() {
                    return this.f668e.toString();
                }
            }
        }
    }

    public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> sortedMap) {
        return new UnmodifiableSortedMap(sortedMap);
    }

    static class UnmodifiableSortedMap<K, V> extends UnmodifiableMap<K, V> implements SortedMap<K, V>, Serializable {
        private static final long serialVersionUID = -8806743815996713206L;

        /* renamed from: sm */
        private final SortedMap<K, ? extends V> f672sm;

        UnmodifiableSortedMap(SortedMap<K, ? extends V> sortedMap) {
            super(sortedMap);
            this.f672sm = sortedMap;
        }

        public Comparator<? super K> comparator() {
            return this.f672sm.comparator();
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return new UnmodifiableSortedMap(this.f672sm.subMap(k, k2));
        }

        public SortedMap<K, V> headMap(K k) {
            return new UnmodifiableSortedMap(this.f672sm.headMap(k));
        }

        public SortedMap<K, V> tailMap(K k) {
            return new UnmodifiableSortedMap(this.f672sm.tailMap(k));
        }

        public K firstKey() {
            return this.f672sm.firstKey();
        }

        public K lastKey() {
            return this.f672sm.lastKey();
        }
    }

    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> navigableMap) {
        return new UnmodifiableNavigableMap(navigableMap);
    }

    static class UnmodifiableNavigableMap<K, V> extends UnmodifiableSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
        /* access modifiers changed from: private */
        public static final EmptyNavigableMap<?, ?> EMPTY_NAVIGABLE_MAP = new EmptyNavigableMap<>();
        private static final long serialVersionUID = -4858195264774772197L;

        /* renamed from: nm */
        private final NavigableMap<K, ? extends V> f670nm;

        private static class EmptyNavigableMap<K, V> extends UnmodifiableNavigableMap<K, V> implements Serializable {
            private static final long serialVersionUID = -2239321462712562324L;

            EmptyNavigableMap() {
                super(new TreeMap());
            }

            public NavigableSet<K> navigableKeySet() {
                return Collections.emptyNavigableSet();
            }

            private Object readResolve() {
                return UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
            }
        }

        UnmodifiableNavigableMap(NavigableMap<K, ? extends V> navigableMap) {
            super(navigableMap);
            this.f670nm = navigableMap;
        }

        public K lowerKey(K k) {
            return this.f670nm.lowerKey(k);
        }

        public K floorKey(K k) {
            return this.f670nm.floorKey(k);
        }

        public K ceilingKey(K k) {
            return this.f670nm.ceilingKey(k);
        }

        public K higherKey(K k) {
            return this.f670nm.higherKey(k);
        }

        public Map.Entry<K, V> lowerEntry(K k) {
            Map.Entry<K, ? extends V> lowerEntry = this.f670nm.lowerEntry(k);
            if (lowerEntry != null) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(lowerEntry);
            }
            return null;
        }

        public Map.Entry<K, V> floorEntry(K k) {
            Map.Entry<K, ? extends V> floorEntry = this.f670nm.floorEntry(k);
            if (floorEntry != null) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(floorEntry);
            }
            return null;
        }

        public Map.Entry<K, V> ceilingEntry(K k) {
            Map.Entry<K, ? extends V> ceilingEntry = this.f670nm.ceilingEntry(k);
            if (ceilingEntry != null) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(ceilingEntry);
            }
            return null;
        }

        public Map.Entry<K, V> higherEntry(K k) {
            Map.Entry<K, ? extends V> higherEntry = this.f670nm.higherEntry(k);
            if (higherEntry != null) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(higherEntry);
            }
            return null;
        }

        public Map.Entry<K, V> firstEntry() {
            Map.Entry<K, ? extends V> firstEntry = this.f670nm.firstEntry();
            if (firstEntry != null) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(firstEntry);
            }
            return null;
        }

        public Map.Entry<K, V> lastEntry() {
            Map.Entry<K, ? extends V> lastEntry = this.f670nm.lastEntry();
            if (lastEntry != null) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(lastEntry);
            }
            return null;
        }

        public Map.Entry<K, V> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        public Map.Entry<K, V> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        public NavigableMap<K, V> descendingMap() {
            return Collections.unmodifiableNavigableMap(this.f670nm.descendingMap());
        }

        public NavigableSet<K> navigableKeySet() {
            return Collections.unmodifiableNavigableSet(this.f670nm.navigableKeySet());
        }

        public NavigableSet<K> descendingKeySet() {
            return Collections.unmodifiableNavigableSet(this.f670nm.descendingKeySet());
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            return Collections.unmodifiableNavigableMap(this.f670nm.subMap(k, z, k2, z2));
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            return Collections.unmodifiableNavigableMap(this.f670nm.headMap(k, z));
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            return Collections.unmodifiableNavigableMap(this.f670nm.tailMap(k, z));
        }
    }

    public static <T> Collection<T> synchronizedCollection(Collection<T> collection) {
        return new SynchronizedCollection(collection);
    }

    static <T> Collection<T> synchronizedCollection(Collection<T> collection, Object obj) {
        return new SynchronizedCollection(collection, obj);
    }

    static class SynchronizedCollection<E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 3053995032091335093L;

        /* renamed from: c */
        final Collection<E> f657c;
        final Object mutex;

        SynchronizedCollection(Collection<E> collection) {
            this.f657c = (Collection) Objects.requireNonNull(collection);
            this.mutex = this;
        }

        SynchronizedCollection(Collection<E> collection, Object obj) {
            this.f657c = (Collection) Objects.requireNonNull(collection);
            this.mutex = Objects.requireNonNull(obj);
        }

        public int size() {
            int size;
            synchronized (this.mutex) {
                size = this.f657c.size();
            }
            return size;
        }

        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.mutex) {
                isEmpty = this.f657c.isEmpty();
            }
            return isEmpty;
        }

        public boolean contains(Object obj) {
            boolean contains;
            synchronized (this.mutex) {
                contains = this.f657c.contains(obj);
            }
            return contains;
        }

        public Object[] toArray() {
            Object[] array;
            synchronized (this.mutex) {
                array = this.f657c.toArray();
            }
            return array;
        }

        public <T> T[] toArray(T[] tArr) {
            T[] array;
            synchronized (this.mutex) {
                array = this.f657c.toArray(tArr);
            }
            return array;
        }

        public Iterator<E> iterator() {
            return this.f657c.iterator();
        }

        public boolean add(E e) {
            boolean add;
            synchronized (this.mutex) {
                add = this.f657c.add(e);
            }
            return add;
        }

        public boolean remove(Object obj) {
            boolean remove;
            synchronized (this.mutex) {
                remove = this.f657c.remove(obj);
            }
            return remove;
        }

        public boolean containsAll(Collection<?> collection) {
            boolean containsAll;
            synchronized (this.mutex) {
                containsAll = this.f657c.containsAll(collection);
            }
            return containsAll;
        }

        public boolean addAll(Collection<? extends E> collection) {
            boolean addAll;
            synchronized (this.mutex) {
                addAll = this.f657c.addAll(collection);
            }
            return addAll;
        }

        public boolean removeAll(Collection<?> collection) {
            boolean removeAll;
            synchronized (this.mutex) {
                removeAll = this.f657c.removeAll(collection);
            }
            return removeAll;
        }

        public boolean retainAll(Collection<?> collection) {
            boolean retainAll;
            synchronized (this.mutex) {
                retainAll = this.f657c.retainAll(collection);
            }
            return retainAll;
        }

        public void clear() {
            synchronized (this.mutex) {
                this.f657c.clear();
            }
        }

        public String toString() {
            String obj;
            synchronized (this.mutex) {
                obj = this.f657c.toString();
            }
            return obj;
        }

        public void forEach(Consumer<? super E> consumer) {
            synchronized (this.mutex) {
                this.f657c.forEach(consumer);
            }
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            boolean removeIf;
            synchronized (this.mutex) {
                removeIf = this.f657c.removeIf(predicate);
            }
            return removeIf;
        }

        public Spliterator<E> spliterator() {
            return this.f657c.spliterator();
        }

        public Stream<E> stream() {
            return this.f657c.stream();
        }

        public Stream<E> parallelStream() {
            return this.f657c.parallelStream();
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            synchronized (this.mutex) {
                objectOutputStream.defaultWriteObject();
            }
        }
    }

    public static <T> Set<T> synchronizedSet(Set<T> set) {
        return new SynchronizedSet(set);
    }

    static <T> Set<T> synchronizedSet(Set<T> set, Object obj) {
        return new SynchronizedSet(set, obj);
    }

    static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
        private static final long serialVersionUID = 487447009682186044L;

        SynchronizedSet(Set<E> set) {
            super(set);
        }

        SynchronizedSet(Set<E> set, Object obj) {
            super(set, obj);
        }

        public boolean equals(Object obj) {
            boolean equals;
            if (this == obj) {
                return true;
            }
            synchronized (this.mutex) {
                equals = this.f657c.equals(obj);
            }
            return equals;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = this.f657c.hashCode();
            }
            return hashCode;
        }
    }

    public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> sortedSet) {
        return new SynchronizedSortedSet(sortedSet);
    }

    static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E> {
        private static final long serialVersionUID = 8695801310862127406L;

        /* renamed from: ss */
        private final SortedSet<E> f662ss;

        SynchronizedSortedSet(SortedSet<E> sortedSet) {
            super(sortedSet);
            this.f662ss = sortedSet;
        }

        SynchronizedSortedSet(SortedSet<E> sortedSet, Object obj) {
            super(sortedSet, obj);
            this.f662ss = sortedSet;
        }

        public Comparator<? super E> comparator() {
            Comparator<? super E> comparator;
            synchronized (this.mutex) {
                comparator = this.f662ss.comparator();
            }
            return comparator;
        }

        public SortedSet<E> subSet(E e, E e2) {
            SynchronizedSortedSet synchronizedSortedSet;
            synchronized (this.mutex) {
                synchronizedSortedSet = new SynchronizedSortedSet(this.f662ss.subSet(e, e2), this.mutex);
            }
            return synchronizedSortedSet;
        }

        public SortedSet<E> headSet(E e) {
            SynchronizedSortedSet synchronizedSortedSet;
            synchronized (this.mutex) {
                synchronizedSortedSet = new SynchronizedSortedSet(this.f662ss.headSet(e), this.mutex);
            }
            return synchronizedSortedSet;
        }

        public SortedSet<E> tailSet(E e) {
            SynchronizedSortedSet synchronizedSortedSet;
            synchronized (this.mutex) {
                synchronizedSortedSet = new SynchronizedSortedSet(this.f662ss.tailSet(e), this.mutex);
            }
            return synchronizedSortedSet;
        }

        public E first() {
            E first;
            synchronized (this.mutex) {
                first = this.f662ss.first();
            }
            return first;
        }

        public E last() {
            E last;
            synchronized (this.mutex) {
                last = this.f662ss.last();
            }
            return last;
        }
    }

    public static <T> NavigableSet<T> synchronizedNavigableSet(NavigableSet<T> navigableSet) {
        return new SynchronizedNavigableSet(navigableSet);
    }

    static class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {
        private static final long serialVersionUID = -5505529816273629798L;

        /* renamed from: ns */
        private final NavigableSet<E> f660ns;

        SynchronizedNavigableSet(NavigableSet<E> navigableSet) {
            super(navigableSet);
            this.f660ns = navigableSet;
        }

        SynchronizedNavigableSet(NavigableSet<E> navigableSet, Object obj) {
            super(navigableSet, obj);
            this.f660ns = navigableSet;
        }

        public E lower(E e) {
            E lower;
            synchronized (this.mutex) {
                lower = this.f660ns.lower(e);
            }
            return lower;
        }

        public E floor(E e) {
            E floor;
            synchronized (this.mutex) {
                floor = this.f660ns.floor(e);
            }
            return floor;
        }

        public E ceiling(E e) {
            E ceiling;
            synchronized (this.mutex) {
                ceiling = this.f660ns.ceiling(e);
            }
            return ceiling;
        }

        public E higher(E e) {
            E higher;
            synchronized (this.mutex) {
                higher = this.f660ns.higher(e);
            }
            return higher;
        }

        public E pollFirst() {
            E pollFirst;
            synchronized (this.mutex) {
                pollFirst = this.f660ns.pollFirst();
            }
            return pollFirst;
        }

        public E pollLast() {
            E pollLast;
            synchronized (this.mutex) {
                pollLast = this.f660ns.pollLast();
            }
            return pollLast;
        }

        public NavigableSet<E> descendingSet() {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f660ns.descendingSet(), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        public Iterator<E> descendingIterator() {
            Iterator<E> it;
            synchronized (this.mutex) {
                it = descendingSet().iterator();
            }
            return it;
        }

        public NavigableSet<E> subSet(E e, E e2) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f660ns.subSet(e, true, e2, false), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        public NavigableSet<E> headSet(E e) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f660ns.headSet(e, false), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        public NavigableSet<E> tailSet(E e) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f660ns.tailSet(e, true), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f660ns.subSet(e, z, e2, z2), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        public NavigableSet<E> headSet(E e, boolean z) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f660ns.headSet(e, z), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        public NavigableSet<E> tailSet(E e, boolean z) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f660ns.tailSet(e, z), this.mutex);
            }
            return synchronizedNavigableSet;
        }
    }

    public static <T> List<T> synchronizedList(List<T> list) {
        if (list instanceof RandomAccess) {
            return new SynchronizedRandomAccessList(list);
        }
        return new SynchronizedList(list);
    }

    static <T> List<T> synchronizedList(List<T> list, Object obj) {
        if (list instanceof RandomAccess) {
            return new SynchronizedRandomAccessList(list, obj);
        }
        return new SynchronizedList(list, obj);
    }

    static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = -7754090372962971524L;
        final List<E> list;

        SynchronizedList(List<E> list2) {
            super(list2);
            this.list = list2;
        }

        SynchronizedList(List<E> list2, Object obj) {
            super(list2, obj);
            this.list = list2;
        }

        public boolean equals(Object obj) {
            boolean equals;
            if (this == obj) {
                return true;
            }
            synchronized (this.mutex) {
                equals = this.list.equals(obj);
            }
            return equals;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = this.list.hashCode();
            }
            return hashCode;
        }

        public E get(int i) {
            E e;
            synchronized (this.mutex) {
                e = this.list.get(i);
            }
            return e;
        }

        public E set(int i, E e) {
            E e2;
            synchronized (this.mutex) {
                e2 = this.list.set(i, e);
            }
            return e2;
        }

        public void add(int i, E e) {
            synchronized (this.mutex) {
                this.list.add(i, e);
            }
        }

        public E remove(int i) {
            E remove;
            synchronized (this.mutex) {
                remove = this.list.remove(i);
            }
            return remove;
        }

        public int indexOf(Object obj) {
            int indexOf;
            synchronized (this.mutex) {
                indexOf = this.list.indexOf(obj);
            }
            return indexOf;
        }

        public int lastIndexOf(Object obj) {
            int lastIndexOf;
            synchronized (this.mutex) {
                lastIndexOf = this.list.lastIndexOf(obj);
            }
            return lastIndexOf;
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            boolean addAll;
            synchronized (this.mutex) {
                addAll = this.list.addAll(i, collection);
            }
            return addAll;
        }

        public ListIterator<E> listIterator() {
            return this.list.listIterator();
        }

        public ListIterator<E> listIterator(int i) {
            return this.list.listIterator(i);
        }

        public List<E> subList(int i, int i2) {
            SynchronizedList synchronizedList;
            synchronized (this.mutex) {
                synchronizedList = new SynchronizedList(this.list.subList(i, i2), this.mutex);
            }
            return synchronizedList;
        }

        public void replaceAll(UnaryOperator<E> unaryOperator) {
            synchronized (this.mutex) {
                this.list.replaceAll(unaryOperator);
            }
        }

        public void sort(Comparator<? super E> comparator) {
            synchronized (this.mutex) {
                this.list.sort(comparator);
            }
        }

        private Object readResolve() {
            List<E> list2 = this.list;
            return list2 instanceof RandomAccess ? new SynchronizedRandomAccessList(list2) : this;
        }
    }

    static class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess {
        private static final long serialVersionUID = 1530674583602358482L;

        SynchronizedRandomAccessList(List<E> list) {
            super(list);
        }

        SynchronizedRandomAccessList(List<E> list, Object obj) {
            super(list, obj);
        }

        public List<E> subList(int i, int i2) {
            SynchronizedRandomAccessList synchronizedRandomAccessList;
            synchronized (this.mutex) {
                synchronizedRandomAccessList = new SynchronizedRandomAccessList(this.list.subList(i, i2), this.mutex);
            }
            return synchronizedRandomAccessList;
        }

        private Object writeReplace() {
            return new SynchronizedList(this.list);
        }
    }

    public static <K, V> Map<K, V> synchronizedMap(Map<K, V> map) {
        return new SynchronizedMap(map);
    }

    private static class SynchronizedMap<K, V> implements Map<K, V>, Serializable {
        private static final long serialVersionUID = 1978198479659022715L;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Set<K> keySet;

        /* renamed from: m */
        private final Map<K, V> f658m;
        final Object mutex;
        private transient Collection<V> values;

        SynchronizedMap(Map<K, V> map) {
            this.f658m = (Map) Objects.requireNonNull(map);
            this.mutex = this;
        }

        SynchronizedMap(Map<K, V> map, Object obj) {
            this.f658m = map;
            this.mutex = obj;
        }

        public int size() {
            int size;
            synchronized (this.mutex) {
                size = this.f658m.size();
            }
            return size;
        }

        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.mutex) {
                isEmpty = this.f658m.isEmpty();
            }
            return isEmpty;
        }

        public boolean containsKey(Object obj) {
            boolean containsKey;
            synchronized (this.mutex) {
                containsKey = this.f658m.containsKey(obj);
            }
            return containsKey;
        }

        public boolean containsValue(Object obj) {
            boolean containsValue;
            synchronized (this.mutex) {
                containsValue = this.f658m.containsValue(obj);
            }
            return containsValue;
        }

        public V get(Object obj) {
            V v;
            synchronized (this.mutex) {
                v = this.f658m.get(obj);
            }
            return v;
        }

        public V put(K k, V v) {
            V put;
            synchronized (this.mutex) {
                put = this.f658m.put(k, v);
            }
            return put;
        }

        public V remove(Object obj) {
            V remove;
            synchronized (this.mutex) {
                remove = this.f658m.remove(obj);
            }
            return remove;
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            synchronized (this.mutex) {
                this.f658m.putAll(map);
            }
        }

        public void clear() {
            synchronized (this.mutex) {
                this.f658m.clear();
            }
        }

        public Set<K> keySet() {
            Set<K> set;
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = new SynchronizedSet(this.f658m.keySet(), this.mutex);
                }
                set = this.keySet;
            }
            return set;
        }

        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set;
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = new SynchronizedSet(this.f658m.entrySet(), this.mutex);
                }
                set = this.entrySet;
            }
            return set;
        }

        public Collection<V> values() {
            Collection<V> collection;
            synchronized (this.mutex) {
                if (this.values == null) {
                    this.values = new SynchronizedCollection(this.f658m.values(), this.mutex);
                }
                collection = this.values;
            }
            return collection;
        }

        public boolean equals(Object obj) {
            boolean equals;
            if (this == obj) {
                return true;
            }
            synchronized (this.mutex) {
                equals = this.f658m.equals(obj);
            }
            return equals;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = this.f658m.hashCode();
            }
            return hashCode;
        }

        public String toString() {
            String obj;
            synchronized (this.mutex) {
                obj = this.f658m.toString();
            }
            return obj;
        }

        public V getOrDefault(Object obj, V v) {
            V orDefault;
            synchronized (this.mutex) {
                orDefault = this.f658m.getOrDefault(obj, v);
            }
            return orDefault;
        }

        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            synchronized (this.mutex) {
                this.f658m.forEach(biConsumer);
            }
        }

        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            synchronized (this.mutex) {
                this.f658m.replaceAll(biFunction);
            }
        }

        public V putIfAbsent(K k, V v) {
            V putIfAbsent;
            synchronized (this.mutex) {
                putIfAbsent = this.f658m.putIfAbsent(k, v);
            }
            return putIfAbsent;
        }

        public boolean remove(Object obj, Object obj2) {
            boolean remove;
            synchronized (this.mutex) {
                remove = this.f658m.remove(obj, obj2);
            }
            return remove;
        }

        public boolean replace(K k, V v, V v2) {
            boolean replace;
            synchronized (this.mutex) {
                replace = this.f658m.replace(k, v, v2);
            }
            return replace;
        }

        public V replace(K k, V v) {
            V replace;
            synchronized (this.mutex) {
                replace = this.f658m.replace(k, v);
            }
            return replace;
        }

        public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
            V computeIfAbsent;
            synchronized (this.mutex) {
                computeIfAbsent = this.f658m.computeIfAbsent(k, function);
            }
            return computeIfAbsent;
        }

        public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            V computeIfPresent;
            synchronized (this.mutex) {
                computeIfPresent = this.f658m.computeIfPresent(k, biFunction);
            }
            return computeIfPresent;
        }

        public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            V compute;
            synchronized (this.mutex) {
                compute = this.f658m.compute(k, biFunction);
            }
            return compute;
        }

        public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            V merge;
            synchronized (this.mutex) {
                merge = this.f658m.merge(k, v, biFunction);
            }
            return merge;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            synchronized (this.mutex) {
                objectOutputStream.defaultWriteObject();
            }
        }
    }

    public static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> sortedMap) {
        return new SynchronizedSortedMap(sortedMap);
    }

    static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {
        private static final long serialVersionUID = -8798146769416483793L;

        /* renamed from: sm */
        private final SortedMap<K, V> f661sm;

        SynchronizedSortedMap(SortedMap<K, V> sortedMap) {
            super(sortedMap);
            this.f661sm = sortedMap;
        }

        SynchronizedSortedMap(SortedMap<K, V> sortedMap, Object obj) {
            super(sortedMap, obj);
            this.f661sm = sortedMap;
        }

        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator;
            synchronized (this.mutex) {
                comparator = this.f661sm.comparator();
            }
            return comparator;
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            SynchronizedSortedMap synchronizedSortedMap;
            synchronized (this.mutex) {
                synchronizedSortedMap = new SynchronizedSortedMap(this.f661sm.subMap(k, k2), this.mutex);
            }
            return synchronizedSortedMap;
        }

        public SortedMap<K, V> headMap(K k) {
            SynchronizedSortedMap synchronizedSortedMap;
            synchronized (this.mutex) {
                synchronizedSortedMap = new SynchronizedSortedMap(this.f661sm.headMap(k), this.mutex);
            }
            return synchronizedSortedMap;
        }

        public SortedMap<K, V> tailMap(K k) {
            SynchronizedSortedMap synchronizedSortedMap;
            synchronized (this.mutex) {
                synchronizedSortedMap = new SynchronizedSortedMap(this.f661sm.tailMap(k), this.mutex);
            }
            return synchronizedSortedMap;
        }

        public K firstKey() {
            K firstKey;
            synchronized (this.mutex) {
                firstKey = this.f661sm.firstKey();
            }
            return firstKey;
        }

        public K lastKey() {
            K lastKey;
            synchronized (this.mutex) {
                lastKey = this.f661sm.lastKey();
            }
            return lastKey;
        }
    }

    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
        return new SynchronizedNavigableMap(navigableMap);
    }

    static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> {
        private static final long serialVersionUID = 699392247599746807L;

        /* renamed from: nm */
        private final NavigableMap<K, V> f659nm;

        SynchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
            super(navigableMap);
            this.f659nm = navigableMap;
        }

        SynchronizedNavigableMap(NavigableMap<K, V> navigableMap, Object obj) {
            super(navigableMap, obj);
            this.f659nm = navigableMap;
        }

        public Map.Entry<K, V> lowerEntry(K k) {
            Map.Entry<K, V> lowerEntry;
            synchronized (this.mutex) {
                lowerEntry = this.f659nm.lowerEntry(k);
            }
            return lowerEntry;
        }

        public K lowerKey(K k) {
            K lowerKey;
            synchronized (this.mutex) {
                lowerKey = this.f659nm.lowerKey(k);
            }
            return lowerKey;
        }

        public Map.Entry<K, V> floorEntry(K k) {
            Map.Entry<K, V> floorEntry;
            synchronized (this.mutex) {
                floorEntry = this.f659nm.floorEntry(k);
            }
            return floorEntry;
        }

        public K floorKey(K k) {
            K floorKey;
            synchronized (this.mutex) {
                floorKey = this.f659nm.floorKey(k);
            }
            return floorKey;
        }

        public Map.Entry<K, V> ceilingEntry(K k) {
            Map.Entry<K, V> ceilingEntry;
            synchronized (this.mutex) {
                ceilingEntry = this.f659nm.ceilingEntry(k);
            }
            return ceilingEntry;
        }

        public K ceilingKey(K k) {
            K ceilingKey;
            synchronized (this.mutex) {
                ceilingKey = this.f659nm.ceilingKey(k);
            }
            return ceilingKey;
        }

        public Map.Entry<K, V> higherEntry(K k) {
            Map.Entry<K, V> higherEntry;
            synchronized (this.mutex) {
                higherEntry = this.f659nm.higherEntry(k);
            }
            return higherEntry;
        }

        public K higherKey(K k) {
            K higherKey;
            synchronized (this.mutex) {
                higherKey = this.f659nm.higherKey(k);
            }
            return higherKey;
        }

        public Map.Entry<K, V> firstEntry() {
            Map.Entry<K, V> firstEntry;
            synchronized (this.mutex) {
                firstEntry = this.f659nm.firstEntry();
            }
            return firstEntry;
        }

        public Map.Entry<K, V> lastEntry() {
            Map.Entry<K, V> lastEntry;
            synchronized (this.mutex) {
                lastEntry = this.f659nm.lastEntry();
            }
            return lastEntry;
        }

        public Map.Entry<K, V> pollFirstEntry() {
            Map.Entry<K, V> pollFirstEntry;
            synchronized (this.mutex) {
                pollFirstEntry = this.f659nm.pollFirstEntry();
            }
            return pollFirstEntry;
        }

        public Map.Entry<K, V> pollLastEntry() {
            Map.Entry<K, V> pollLastEntry;
            synchronized (this.mutex) {
                pollLastEntry = this.f659nm.pollLastEntry();
            }
            return pollLastEntry;
        }

        public NavigableMap<K, V> descendingMap() {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.f659nm.descendingMap(), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        public NavigableSet<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> navigableKeySet() {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f659nm.navigableKeySet(), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        public NavigableSet<K> descendingKeySet() {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.f659nm.descendingKeySet(), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.f659nm.subMap(k, true, k2, false), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        public SortedMap<K, V> headMap(K k) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.f659nm.headMap(k, false), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        public SortedMap<K, V> tailMap(K k) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.f659nm.tailMap(k, true), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.f659nm.subMap(k, z, k2, z2), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.f659nm.headMap(k, z), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.f659nm.tailMap(k, z), this.mutex);
            }
            return synchronizedNavigableMap;
        }
    }

    public static <E> Collection<E> checkedCollection(Collection<E> collection, Class<E> cls) {
        return new CheckedCollection(collection, cls);
    }

    static <T> T[] zeroLengthArray(Class<T> cls) {
        return (Object[]) Array.newInstance((Class<?>) cls, 0);
    }

    static class CheckedCollection<E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 1578914078182001775L;

        /* renamed from: c */
        final Collection<E> f644c;
        final Class<E> type;
        private E[] zeroLengthElementArray;

        /* access modifiers changed from: package-private */
        public E typeCheck(Object obj) {
            if (obj == null || this.type.isInstance(obj)) {
                return obj;
            }
            throw new ClassCastException(badElementMsg(obj));
        }

        private String badElementMsg(Object obj) {
            return "Attempt to insert " + obj.getClass() + " element into collection with element type " + this.type;
        }

        CheckedCollection(Collection<E> collection, Class<E> cls) {
            this.f644c = (Collection) Objects.requireNonNull(collection, "c");
            this.type = (Class) Objects.requireNonNull(cls, "type");
        }

        public int size() {
            return this.f644c.size();
        }

        public boolean isEmpty() {
            return this.f644c.isEmpty();
        }

        public boolean contains(Object obj) {
            return this.f644c.contains(obj);
        }

        public Object[] toArray() {
            return this.f644c.toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return this.f644c.toArray(tArr);
        }

        public String toString() {
            return this.f644c.toString();
        }

        public boolean remove(Object obj) {
            return this.f644c.remove(obj);
        }

        public void clear() {
            this.f644c.clear();
        }

        public boolean containsAll(Collection<?> collection) {
            return this.f644c.containsAll(collection);
        }

        public boolean removeAll(Collection<?> collection) {
            return this.f644c.removeAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return this.f644c.retainAll(collection);
        }

        public Iterator<E> iterator() {
            final Iterator<E> it = this.f644c.iterator();
            return new Iterator<E>() {
                public boolean hasNext() {
                    return it.hasNext();
                }

                public E next() {
                    return it.next();
                }

                public void remove() {
                    it.remove();
                }
            };
        }

        public boolean add(E e) {
            return this.f644c.add(typeCheck(e));
        }

        private E[] zeroLengthElementArray() {
            E[] eArr = this.zeroLengthElementArray;
            if (eArr != null) {
                return eArr;
            }
            E[] zeroLengthArray = Collections.zeroLengthArray(this.type);
            this.zeroLengthElementArray = zeroLengthArray;
            return zeroLengthArray;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.Object[]} */
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.Collection<E> checkedCopyOf(java.util.Collection<? extends E> r5) {
            /*
                r4 = this;
                java.lang.Object[] r0 = r4.zeroLengthElementArray()     // Catch:{ ArrayStoreException -> 0x001c }
                java.lang.Object[] r1 = r5.toArray((T[]) r0)     // Catch:{ ArrayStoreException -> 0x001c }
                java.lang.Class r2 = r1.getClass()     // Catch:{ ArrayStoreException -> 0x001c }
                java.lang.Class r3 = r0.getClass()     // Catch:{ ArrayStoreException -> 0x001c }
                if (r2 == r3) goto L_0x0033
                int r2 = r1.length     // Catch:{ ArrayStoreException -> 0x001c }
                java.lang.Class r0 = r0.getClass()     // Catch:{ ArrayStoreException -> 0x001c }
                java.lang.Object[] r1 = java.util.Arrays.copyOf(r1, r2, r0)     // Catch:{ ArrayStoreException -> 0x001c }
                goto L_0x0033
            L_0x001c:
                java.lang.Object[] r5 = r5.toArray()
                java.lang.Object r5 = r5.clone()
                r1 = r5
                java.lang.Object[] r1 = (java.lang.Object[]) r1
                int r5 = r1.length
                r0 = 0
            L_0x0029:
                if (r0 >= r5) goto L_0x0033
                r2 = r1[r0]
                r4.typeCheck(r2)
                int r0 = r0 + 1
                goto L_0x0029
            L_0x0033:
                java.util.List r4 = java.util.Arrays.asList(r1)
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Collections.CheckedCollection.checkedCopyOf(java.util.Collection):java.util.Collection");
        }

        public boolean addAll(Collection<? extends E> collection) {
            return this.f644c.addAll(checkedCopyOf(collection));
        }

        public void forEach(Consumer<? super E> consumer) {
            this.f644c.forEach(consumer);
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            return this.f644c.removeIf(predicate);
        }

        public Spliterator<E> spliterator() {
            return this.f644c.spliterator();
        }

        public Stream<E> stream() {
            return this.f644c.stream();
        }

        public Stream<E> parallelStream() {
            return this.f644c.parallelStream();
        }
    }

    public static <E> Queue<E> checkedQueue(Queue<E> queue, Class<E> cls) {
        return new CheckedQueue(queue, cls);
    }

    static class CheckedQueue<E> extends CheckedCollection<E> implements Queue<E>, Serializable {
        private static final long serialVersionUID = 1433151992604707767L;
        final Queue<E> queue;

        CheckedQueue(Queue<E> queue2, Class<E> cls) {
            super(queue2, cls);
            this.queue = queue2;
        }

        public E element() {
            return this.queue.element();
        }

        public boolean equals(Object obj) {
            return obj == this || this.f644c.equals(obj);
        }

        public int hashCode() {
            return this.f644c.hashCode();
        }

        public E peek() {
            return this.queue.peek();
        }

        public E poll() {
            return this.queue.poll();
        }

        public E remove() {
            return this.queue.remove();
        }

        public boolean offer(E e) {
            return this.queue.offer(typeCheck(e));
        }
    }

    public static <E> Set<E> checkedSet(Set<E> set, Class<E> cls) {
        return new CheckedSet(set, cls);
    }

    static class CheckedSet<E> extends CheckedCollection<E> implements Set<E>, Serializable {
        private static final long serialVersionUID = 4694047833775013803L;

        CheckedSet(Set<E> set, Class<E> cls) {
            super(set, cls);
        }

        public boolean equals(Object obj) {
            return obj == this || this.f644c.equals(obj);
        }

        public int hashCode() {
            return this.f644c.hashCode();
        }
    }

    public static <E> SortedSet<E> checkedSortedSet(SortedSet<E> sortedSet, Class<E> cls) {
        return new CheckedSortedSet(sortedSet, cls);
    }

    static class CheckedSortedSet<E> extends CheckedSet<E> implements SortedSet<E>, Serializable {
        private static final long serialVersionUID = 1599911165492914959L;

        /* renamed from: ss */
        private final SortedSet<E> f651ss;

        CheckedSortedSet(SortedSet<E> sortedSet, Class<E> cls) {
            super(sortedSet, cls);
            this.f651ss = sortedSet;
        }

        public Comparator<? super E> comparator() {
            return this.f651ss.comparator();
        }

        public E first() {
            return this.f651ss.first();
        }

        public E last() {
            return this.f651ss.last();
        }

        public SortedSet<E> subSet(E e, E e2) {
            return Collections.checkedSortedSet(this.f651ss.subSet(e, e2), this.type);
        }

        public SortedSet<E> headSet(E e) {
            return Collections.checkedSortedSet(this.f651ss.headSet(e), this.type);
        }

        public SortedSet<E> tailSet(E e) {
            return Collections.checkedSortedSet(this.f651ss.tailSet(e), this.type);
        }
    }

    public static <E> NavigableSet<E> checkedNavigableSet(NavigableSet<E> navigableSet, Class<E> cls) {
        return new CheckedNavigableSet(navigableSet, cls);
    }

    static class CheckedNavigableSet<E> extends CheckedSortedSet<E> implements NavigableSet<E>, Serializable {
        private static final long serialVersionUID = -5429120189805438922L;

        /* renamed from: ns */
        private final NavigableSet<E> f649ns;

        CheckedNavigableSet(NavigableSet<E> navigableSet, Class<E> cls) {
            super(navigableSet, cls);
            this.f649ns = navigableSet;
        }

        public E lower(E e) {
            return this.f649ns.lower(e);
        }

        public E floor(E e) {
            return this.f649ns.floor(e);
        }

        public E ceiling(E e) {
            return this.f649ns.ceiling(e);
        }

        public E higher(E e) {
            return this.f649ns.higher(e);
        }

        public E pollFirst() {
            return this.f649ns.pollFirst();
        }

        public E pollLast() {
            return this.f649ns.pollLast();
        }

        public NavigableSet<E> descendingSet() {
            return Collections.checkedNavigableSet(this.f649ns.descendingSet(), this.type);
        }

        public Iterator<E> descendingIterator() {
            return Collections.checkedNavigableSet(this.f649ns.descendingSet(), this.type).iterator();
        }

        public NavigableSet<E> subSet(E e, E e2) {
            return Collections.checkedNavigableSet(this.f649ns.subSet(e, true, e2, false), this.type);
        }

        public NavigableSet<E> headSet(E e) {
            return Collections.checkedNavigableSet(this.f649ns.headSet(e, false), this.type);
        }

        public NavigableSet<E> tailSet(E e) {
            return Collections.checkedNavigableSet(this.f649ns.tailSet(e, true), this.type);
        }

        public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
            return Collections.checkedNavigableSet(this.f649ns.subSet(e, z, e2, z2), this.type);
        }

        public NavigableSet<E> headSet(E e, boolean z) {
            return Collections.checkedNavigableSet(this.f649ns.headSet(e, z), this.type);
        }

        public NavigableSet<E> tailSet(E e, boolean z) {
            return Collections.checkedNavigableSet(this.f649ns.tailSet(e, z), this.type);
        }
    }

    public static <E> List<E> checkedList(List<E> list, Class<E> cls) {
        if (list instanceof RandomAccess) {
            return new CheckedRandomAccessList(list, cls);
        }
        return new CheckedList(list, cls);
    }

    static class CheckedList<E> extends CheckedCollection<E> implements List<E> {
        private static final long serialVersionUID = 65247728283967356L;
        final List<E> list;

        CheckedList(List<E> list2, Class<E> cls) {
            super(list2, cls);
            this.list = list2;
        }

        public boolean equals(Object obj) {
            return obj == this || this.list.equals(obj);
        }

        public int hashCode() {
            return this.list.hashCode();
        }

        public E get(int i) {
            return this.list.get(i);
        }

        public E remove(int i) {
            return this.list.remove(i);
        }

        public int indexOf(Object obj) {
            return this.list.indexOf(obj);
        }

        public int lastIndexOf(Object obj) {
            return this.list.lastIndexOf(obj);
        }

        public E set(int i, E e) {
            return this.list.set(i, typeCheck(e));
        }

        public void add(int i, E e) {
            this.list.add(i, typeCheck(e));
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            return this.list.addAll(i, checkedCopyOf(collection));
        }

        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        public ListIterator<E> listIterator(int i) {
            final ListIterator<E> listIterator = this.list.listIterator(i);
            return new ListIterator<E>() {
                public boolean hasNext() {
                    return listIterator.hasNext();
                }

                public E next() {
                    return listIterator.next();
                }

                public boolean hasPrevious() {
                    return listIterator.hasPrevious();
                }

                public E previous() {
                    return listIterator.previous();
                }

                public int nextIndex() {
                    return listIterator.nextIndex();
                }

                public int previousIndex() {
                    return listIterator.previousIndex();
                }

                public void remove() {
                    listIterator.remove();
                }

                public void set(E e) {
                    listIterator.set(CheckedList.this.typeCheck(e));
                }

                public void add(E e) {
                    listIterator.add(CheckedList.this.typeCheck(e));
                }

                public void forEachRemaining(Consumer<? super E> consumer) {
                    listIterator.forEachRemaining(consumer);
                }
            };
        }

        public List<E> subList(int i, int i2) {
            return new CheckedList(this.list.subList(i, i2), this.type);
        }

        public void replaceAll(UnaryOperator<E> unaryOperator) {
            Objects.requireNonNull(unaryOperator);
            this.list.replaceAll(new Collections$CheckedList$$ExternalSyntheticLambda0(this, unaryOperator));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$replaceAll$0$java-util-Collections$CheckedList  reason: not valid java name */
        public /* synthetic */ Object m3735lambda$replaceAll$0$javautilCollections$CheckedList(UnaryOperator unaryOperator, Object obj) {
            return typeCheck(unaryOperator.apply(obj));
        }

        public void sort(Comparator<? super E> comparator) {
            this.list.sort(comparator);
        }
    }

    static class CheckedRandomAccessList<E> extends CheckedList<E> implements RandomAccess {
        private static final long serialVersionUID = 1638200125423088369L;

        CheckedRandomAccessList(List<E> list, Class<E> cls) {
            super(list, cls);
        }

        public List<E> subList(int i, int i2) {
            return new CheckedRandomAccessList(this.list.subList(i, i2), this.type);
        }
    }

    public static <K, V> Map<K, V> checkedMap(Map<K, V> map, Class<K> cls, Class<V> cls2) {
        return new CheckedMap(map, cls, cls2);
    }

    private static class CheckedMap<K, V> implements Map<K, V>, Serializable {
        private static final long serialVersionUID = 5742860141034234728L;
        private transient Set<Map.Entry<K, V>> entrySet;
        final Class<K> keyType;

        /* renamed from: m */
        private final Map<K, V> f645m;
        final Class<V> valueType;

        private void typeCheck(Object obj, Object obj2) {
            if (obj != null && !this.keyType.isInstance(obj)) {
                throw new ClassCastException(badKeyMsg(obj));
            } else if (obj2 != null && !this.valueType.isInstance(obj2)) {
                throw new ClassCastException(badValueMsg(obj2));
            }
        }

        private BiFunction<? super K, ? super V, ? extends V> typeCheck(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            Objects.requireNonNull(biFunction);
            return new Collections$CheckedMap$$ExternalSyntheticLambda2(this, biFunction);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$typeCheck$0$java-util-Collections$CheckedMap  reason: not valid java name */
        public /* synthetic */ Object m3738lambda$typeCheck$0$javautilCollections$CheckedMap(BiFunction biFunction, Object obj, Object obj2) {
            Object apply = biFunction.apply(obj, obj2);
            typeCheck(obj, apply);
            return apply;
        }

        private String badKeyMsg(Object obj) {
            return "Attempt to insert " + obj.getClass() + " key into map with key type " + this.keyType;
        }

        private String badValueMsg(Object obj) {
            return "Attempt to insert " + obj.getClass() + " value into map with value type " + this.valueType;
        }

        CheckedMap(Map<K, V> map, Class<K> cls, Class<V> cls2) {
            this.f645m = (Map) Objects.requireNonNull(map);
            this.keyType = (Class) Objects.requireNonNull(cls);
            this.valueType = (Class) Objects.requireNonNull(cls2);
        }

        public int size() {
            return this.f645m.size();
        }

        public boolean isEmpty() {
            return this.f645m.isEmpty();
        }

        public boolean containsKey(Object obj) {
            return this.f645m.containsKey(obj);
        }

        public boolean containsValue(Object obj) {
            return this.f645m.containsValue(obj);
        }

        public V get(Object obj) {
            return this.f645m.get(obj);
        }

        public V remove(Object obj) {
            return this.f645m.remove(obj);
        }

        public void clear() {
            this.f645m.clear();
        }

        public Set<K> keySet() {
            return this.f645m.keySet();
        }

        public Collection<V> values() {
            return this.f645m.values();
        }

        public boolean equals(Object obj) {
            return obj == this || this.f645m.equals(obj);
        }

        public int hashCode() {
            return this.f645m.hashCode();
        }

        public String toString() {
            return this.f645m.toString();
        }

        public V put(K k, V v) {
            typeCheck(k, v);
            return this.f645m.put(k, v);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            Object[] array = map.entrySet().toArray();
            ArrayList<Map.Entry> arrayList = new ArrayList<>(array.length);
            for (Object obj : array) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object value = entry.getValue();
                typeCheck(key, value);
                arrayList.add(new AbstractMap.SimpleImmutableEntry(key, value));
            }
            for (Map.Entry entry2 : arrayList) {
                this.f645m.put(entry2.getKey(), entry2.getValue());
            }
        }

        public Set<Map.Entry<K, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = new CheckedEntrySet(this.f645m.entrySet(), this.valueType);
            }
            return this.entrySet;
        }

        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            this.f645m.forEach(biConsumer);
        }

        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            this.f645m.replaceAll(typeCheck(biFunction));
        }

        public V putIfAbsent(K k, V v) {
            typeCheck(k, v);
            return this.f645m.putIfAbsent(k, v);
        }

        public boolean remove(Object obj, Object obj2) {
            return this.f645m.remove(obj, obj2);
        }

        public boolean replace(K k, V v, V v2) {
            typeCheck(k, v2);
            return this.f645m.replace(k, v, v2);
        }

        public V replace(K k, V v) {
            typeCheck(k, v);
            return this.f645m.replace(k, v);
        }

        public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
            Objects.requireNonNull(function);
            return this.f645m.computeIfAbsent(k, new Collections$CheckedMap$$ExternalSyntheticLambda1(this, function));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$computeIfAbsent$1$java-util-Collections$CheckedMap  reason: not valid java name */
        public /* synthetic */ Object m3736lambda$computeIfAbsent$1$javautilCollections$CheckedMap(Function function, Object obj) {
            Object apply = function.apply(obj);
            typeCheck(obj, apply);
            return apply;
        }

        public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            return this.f645m.computeIfPresent(k, typeCheck(biFunction));
        }

        public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            return this.f645m.compute(k, typeCheck(biFunction));
        }

        public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            Objects.requireNonNull(biFunction);
            return this.f645m.merge(k, v, new Collections$CheckedMap$$ExternalSyntheticLambda0(this, biFunction));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$merge$2$java-util-Collections$CheckedMap  reason: not valid java name */
        public /* synthetic */ Object m3737lambda$merge$2$javautilCollections$CheckedMap(BiFunction biFunction, Object obj, Object obj2) {
            Object apply = biFunction.apply(obj, obj2);
            typeCheck((Object) null, apply);
            return apply;
        }

        static class CheckedEntrySet<K, V> implements Set<Map.Entry<K, V>> {

            /* renamed from: s */
            private final Set<Map.Entry<K, V>> f646s;
            private final Class<V> valueType;

            CheckedEntrySet(Set<Map.Entry<K, V>> set, Class<V> cls) {
                this.f646s = set;
                this.valueType = cls;
            }

            public int size() {
                return this.f646s.size();
            }

            public boolean isEmpty() {
                return this.f646s.isEmpty();
            }

            public String toString() {
                return this.f646s.toString();
            }

            public int hashCode() {
                return this.f646s.hashCode();
            }

            public void clear() {
                this.f646s.clear();
            }

            public boolean add(Map.Entry<K, V> entry) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
                throw new UnsupportedOperationException();
            }

            public Iterator<Map.Entry<K, V>> iterator() {
                final Iterator<Map.Entry<K, V>> it = this.f646s.iterator();
                final Class<V> cls = this.valueType;
                return new Iterator<Map.Entry<K, V>>() {
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public void remove() {
                        it.remove();
                    }

                    public Map.Entry<K, V> next() {
                        return CheckedEntrySet.checkedEntry((Map.Entry) it.next(), cls);
                    }
                };
            }

            public Object[] toArray() {
                Object[] objArr;
                Object[] array = this.f646s.toArray();
                if (CheckedEntry.class.isInstance(array.getClass().getComponentType())) {
                    objArr = array;
                } else {
                    objArr = new Object[array.length];
                }
                for (int i = 0; i < array.length; i++) {
                    objArr[i] = checkedEntry((Map.Entry) array[i], this.valueType);
                }
                return objArr;
            }

            public <T> T[] toArray(T[] tArr) {
                T[] array = this.f646s.toArray(tArr.length == 0 ? tArr : Arrays.copyOf(tArr, 0));
                for (int i = 0; i < array.length; i++) {
                    array[i] = checkedEntry((Map.Entry) array[i], this.valueType);
                }
                if (array.length > tArr.length) {
                    return array;
                }
                System.arraycopy((Object) array, 0, (Object) tArr, 0, array.length);
                if (tArr.length > array.length) {
                    tArr[array.length] = null;
                }
                return tArr;
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                Set<Map.Entry<K, V>> set = this.f646s;
                if (!(entry instanceof CheckedEntry)) {
                    entry = checkedEntry(entry, this.valueType);
                }
                return set.contains(entry);
            }

            public boolean containsAll(Collection<?> collection) {
                for (Object contains : collection) {
                    if (!contains(contains)) {
                        return false;
                    }
                }
                return true;
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                return this.f646s.remove(new AbstractMap.SimpleImmutableEntry((Map.Entry) obj));
            }

            public boolean removeAll(Collection<?> collection) {
                return batchRemove(collection, false);
            }

            public boolean retainAll(Collection<?> collection) {
                return batchRemove(collection, true);
            }

            private boolean batchRemove(Collection<?> collection, boolean z) {
                Objects.requireNonNull(collection);
                Iterator it = iterator();
                boolean z2 = false;
                while (it.hasNext()) {
                    if (collection.contains(it.next()) != z) {
                        it.remove();
                        z2 = true;
                    }
                }
                return z2;
            }

            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (!(obj instanceof Set)) {
                    return false;
                }
                Set set = (Set) obj;
                if (set.size() != this.f646s.size() || !containsAll(set)) {
                    return false;
                }
                return true;
            }

            static <K, V, T> CheckedEntry<K, V, T> checkedEntry(Map.Entry<K, V> entry, Class<T> cls) {
                return new CheckedEntry<>(entry, cls);
            }

            private static class CheckedEntry<K, V, T> implements Map.Entry<K, V> {

                /* renamed from: e */
                private final Map.Entry<K, V> f647e;
                private final Class<T> valueType;

                CheckedEntry(Map.Entry<K, V> entry, Class<T> cls) {
                    this.f647e = (Map.Entry) Objects.requireNonNull(entry);
                    this.valueType = (Class) Objects.requireNonNull(cls);
                }

                public K getKey() {
                    return this.f647e.getKey();
                }

                public V getValue() {
                    return this.f647e.getValue();
                }

                public int hashCode() {
                    return this.f647e.hashCode();
                }

                public String toString() {
                    return this.f647e.toString();
                }

                public V setValue(V v) {
                    if (v == null || this.valueType.isInstance(v)) {
                        return this.f647e.setValue(v);
                    }
                    throw new ClassCastException(badValueMsg(v));
                }

                private String badValueMsg(Object obj) {
                    return "Attempt to insert " + obj.getClass() + " value into map with value type " + this.valueType;
                }

                public boolean equals(Object obj) {
                    if (obj == this) {
                        return true;
                    }
                    if (!(obj instanceof Map.Entry)) {
                        return false;
                    }
                    return this.f647e.equals(new AbstractMap.SimpleImmutableEntry((Map.Entry) obj));
                }
            }
        }
    }

    public static <K, V> SortedMap<K, V> checkedSortedMap(SortedMap<K, V> sortedMap, Class<K> cls, Class<V> cls2) {
        return new CheckedSortedMap(sortedMap, cls, cls2);
    }

    static class CheckedSortedMap<K, V> extends CheckedMap<K, V> implements SortedMap<K, V>, Serializable {
        private static final long serialVersionUID = 1599671320688067438L;

        /* renamed from: sm */
        private final SortedMap<K, V> f650sm;

        CheckedSortedMap(SortedMap<K, V> sortedMap, Class<K> cls, Class<V> cls2) {
            super(sortedMap, cls, cls2);
            this.f650sm = sortedMap;
        }

        public Comparator<? super K> comparator() {
            return this.f650sm.comparator();
        }

        public K firstKey() {
            return this.f650sm.firstKey();
        }

        public K lastKey() {
            return this.f650sm.lastKey();
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return Collections.checkedSortedMap(this.f650sm.subMap(k, k2), this.keyType, this.valueType);
        }

        public SortedMap<K, V> headMap(K k) {
            return Collections.checkedSortedMap(this.f650sm.headMap(k), this.keyType, this.valueType);
        }

        public SortedMap<K, V> tailMap(K k) {
            return Collections.checkedSortedMap(this.f650sm.tailMap(k), this.keyType, this.valueType);
        }
    }

    public static <K, V> NavigableMap<K, V> checkedNavigableMap(NavigableMap<K, V> navigableMap, Class<K> cls, Class<V> cls2) {
        return new CheckedNavigableMap(navigableMap, cls, cls2);
    }

    static class CheckedNavigableMap<K, V> extends CheckedSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
        private static final long serialVersionUID = -4852462692372534096L;

        /* renamed from: nm */
        private final NavigableMap<K, V> f648nm;

        CheckedNavigableMap(NavigableMap<K, V> navigableMap, Class<K> cls, Class<V> cls2) {
            super(navigableMap, cls, cls2);
            this.f648nm = navigableMap;
        }

        public Comparator<? super K> comparator() {
            return this.f648nm.comparator();
        }

        public K firstKey() {
            return this.f648nm.firstKey();
        }

        public K lastKey() {
            return this.f648nm.lastKey();
        }

        public Map.Entry<K, V> lowerEntry(K k) {
            Map.Entry<K, V> lowerEntry = this.f648nm.lowerEntry(k);
            if (lowerEntry != null) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(lowerEntry, this.valueType);
            }
            return null;
        }

        public K lowerKey(K k) {
            return this.f648nm.lowerKey(k);
        }

        public Map.Entry<K, V> floorEntry(K k) {
            Map.Entry<K, V> floorEntry = this.f648nm.floorEntry(k);
            if (floorEntry != null) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(floorEntry, this.valueType);
            }
            return null;
        }

        public K floorKey(K k) {
            return this.f648nm.floorKey(k);
        }

        public Map.Entry<K, V> ceilingEntry(K k) {
            Map.Entry<K, V> ceilingEntry = this.f648nm.ceilingEntry(k);
            if (ceilingEntry != null) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(ceilingEntry, this.valueType);
            }
            return null;
        }

        public K ceilingKey(K k) {
            return this.f648nm.ceilingKey(k);
        }

        public Map.Entry<K, V> higherEntry(K k) {
            Map.Entry<K, V> higherEntry = this.f648nm.higherEntry(k);
            if (higherEntry != null) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(higherEntry, this.valueType);
            }
            return null;
        }

        public K higherKey(K k) {
            return this.f648nm.higherKey(k);
        }

        public Map.Entry<K, V> firstEntry() {
            Map.Entry<K, V> firstEntry = this.f648nm.firstEntry();
            if (firstEntry != null) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(firstEntry, this.valueType);
            }
            return null;
        }

        public Map.Entry<K, V> lastEntry() {
            Map.Entry<K, V> lastEntry = this.f648nm.lastEntry();
            if (lastEntry != null) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(lastEntry, this.valueType);
            }
            return null;
        }

        public Map.Entry<K, V> pollFirstEntry() {
            Map.Entry<K, V> pollFirstEntry = this.f648nm.pollFirstEntry();
            if (pollFirstEntry == null) {
                return null;
            }
            return new CheckedMap.CheckedEntrySet.CheckedEntry(pollFirstEntry, this.valueType);
        }

        public Map.Entry<K, V> pollLastEntry() {
            Map.Entry<K, V> pollLastEntry = this.f648nm.pollLastEntry();
            if (pollLastEntry == null) {
                return null;
            }
            return new CheckedMap.CheckedEntrySet.CheckedEntry(pollLastEntry, this.valueType);
        }

        public NavigableMap<K, V> descendingMap() {
            return Collections.checkedNavigableMap(this.f648nm.descendingMap(), this.keyType, this.valueType);
        }

        public NavigableSet<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> navigableKeySet() {
            return Collections.checkedNavigableSet(this.f648nm.navigableKeySet(), this.keyType);
        }

        public NavigableSet<K> descendingKeySet() {
            return Collections.checkedNavigableSet(this.f648nm.descendingKeySet(), this.keyType);
        }

        public NavigableMap<K, V> subMap(K k, K k2) {
            return Collections.checkedNavigableMap(this.f648nm.subMap(k, true, k2, false), this.keyType, this.valueType);
        }

        public NavigableMap<K, V> headMap(K k) {
            return Collections.checkedNavigableMap(this.f648nm.headMap(k, false), this.keyType, this.valueType);
        }

        public NavigableMap<K, V> tailMap(K k) {
            return Collections.checkedNavigableMap(this.f648nm.tailMap(k, true), this.keyType, this.valueType);
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            return Collections.checkedNavigableMap(this.f648nm.subMap(k, z, k2, z2), this.keyType, this.valueType);
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            return Collections.checkedNavigableMap(this.f648nm.headMap(k, z), this.keyType, this.valueType);
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            return Collections.checkedNavigableMap(this.f648nm.tailMap(k, z), this.keyType, this.valueType);
        }
    }

    public static <T> Iterator<T> emptyIterator() {
        return EmptyIterator.EMPTY_ITERATOR;
    }

    private static class EmptyIterator<E> implements Iterator<E> {
        static final EmptyIterator<Object> EMPTY_ITERATOR = new EmptyIterator<>();

        public boolean hasNext() {
            return false;
        }

        private EmptyIterator() {
        }

        public E next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
        }
    }

    public static <T> ListIterator<T> emptyListIterator() {
        return EmptyListIterator.EMPTY_ITERATOR;
    }

    private static class EmptyListIterator<E> extends EmptyIterator<E> implements ListIterator<E> {
        static final EmptyListIterator<Object> EMPTY_ITERATOR = new EmptyListIterator<>();

        public boolean hasPrevious() {
            return false;
        }

        public int nextIndex() {
            return 0;
        }

        public int previousIndex() {
            return -1;
        }

        private EmptyListIterator() {
            super();
        }

        public E previous() {
            throw new NoSuchElementException();
        }

        public void set(E e) {
            throw new IllegalStateException();
        }

        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    public static <T> Enumeration<T> emptyEnumeration() {
        return EmptyEnumeration.EMPTY_ENUMERATION;
    }

    private static class EmptyEnumeration<E> implements Enumeration<E> {
        static final EmptyEnumeration<Object> EMPTY_ENUMERATION = new EmptyEnumeration<>();

        public boolean hasMoreElements() {
            return false;
        }

        private EmptyEnumeration() {
        }

        public E nextElement() {
            throw new NoSuchElementException();
        }
    }

    public static final <T> Set<T> emptySet() {
        return EMPTY_SET;
    }

    private static class EmptySet<E> extends AbstractSet<E> implements Serializable {
        private static final long serialVersionUID = 1582296315990362920L;

        public boolean contains(Object obj) {
            return false;
        }

        public boolean isEmpty() {
            return true;
        }

        public int size() {
            return 0;
        }

        public Object[] toArray() {
            return new Object[0];
        }

        private EmptySet() {
        }

        public Iterator<E> iterator() {
            return Collections.emptyIterator();
        }

        public boolean containsAll(Collection<?> collection) {
            return collection.isEmpty();
        }

        public <T> T[] toArray(T[] tArr) {
            if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }

        public void forEach(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            Objects.requireNonNull(predicate);
            return false;
        }

        public Spliterator<E> spliterator() {
            return Spliterators.emptySpliterator();
        }

        private Object readResolve() {
            return Collections.EMPTY_SET;
        }
    }

    public static <E> SortedSet<E> emptySortedSet() {
        return UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
    }

    public static <E> NavigableSet<E> emptyNavigableSet() {
        return UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
    }

    public static final <T> List<T> emptyList() {
        return EMPTY_LIST;
    }

    private static class EmptyList<E> extends AbstractList<E> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 8842843931221139166L;

        public boolean contains(Object obj) {
            return false;
        }

        public int hashCode() {
            return 1;
        }

        public boolean isEmpty() {
            return true;
        }

        public int size() {
            return 0;
        }

        public void sort(Comparator<? super E> comparator) {
        }

        public Object[] toArray() {
            return new Object[0];
        }

        private EmptyList() {
        }

        public Iterator<E> iterator() {
            return Collections.emptyIterator();
        }

        public ListIterator<E> listIterator() {
            return Collections.emptyListIterator();
        }

        public boolean containsAll(Collection<?> collection) {
            return collection.isEmpty();
        }

        public <T> T[] toArray(T[] tArr) {
            if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }

        public E get(int i) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }

        public boolean equals(Object obj) {
            return (obj instanceof List) && ((List) obj).isEmpty();
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            Objects.requireNonNull(predicate);
            return false;
        }

        public void replaceAll(UnaryOperator<E> unaryOperator) {
            Objects.requireNonNull(unaryOperator);
        }

        public void forEach(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
        }

        public Spliterator<E> spliterator() {
            return Spliterators.emptySpliterator();
        }

        private Object readResolve() {
            return Collections.EMPTY_LIST;
        }
    }

    public static final <K, V> Map<K, V> emptyMap() {
        return EMPTY_MAP;
    }

    public static final <K, V> SortedMap<K, V> emptySortedMap() {
        return UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
    }

    public static final <K, V> NavigableMap<K, V> emptyNavigableMap() {
        return UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
    }

    private static class EmptyMap<K, V> extends AbstractMap<K, V> implements Serializable {
        private static final long serialVersionUID = 6428348081105594320L;

        public boolean containsKey(Object obj) {
            return false;
        }

        public boolean containsValue(Object obj) {
            return false;
        }

        public V get(Object obj) {
            return null;
        }

        public V getOrDefault(Object obj, V v) {
            return v;
        }

        public int hashCode() {
            return 0;
        }

        public boolean isEmpty() {
            return true;
        }

        public int size() {
            return 0;
        }

        private EmptyMap() {
        }

        public Set<K> keySet() {
            return Collections.emptySet();
        }

        public Collection<V> values() {
            return Collections.emptySet();
        }

        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }

        public boolean equals(Object obj) {
            return (obj instanceof Map) && ((Map) obj).isEmpty();
        }

        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            Objects.requireNonNull(biConsumer);
        }

        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            Objects.requireNonNull(biFunction);
        }

        public V putIfAbsent(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object obj, Object obj2) {
            throw new UnsupportedOperationException();
        }

        public boolean replace(K k, V v, V v2) {
            throw new UnsupportedOperationException();
        }

        public V replace(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        private Object readResolve() {
            return Collections.EMPTY_MAP;
        }
    }

    public static <T> Set<T> singleton(T t) {
        return new SingletonSet(t);
    }

    static <E> Iterator<E> singletonIterator(final E e) {
        return new Iterator<E>() {
            private boolean hasNext = true;

            public boolean hasNext() {
                return this.hasNext;
            }

            public E next() {
                if (this.hasNext) {
                    this.hasNext = false;
                    return Object.this;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public void forEachRemaining(Consumer<? super E> consumer) {
                Objects.requireNonNull(consumer);
                if (this.hasNext) {
                    consumer.accept(Object.this);
                    this.hasNext = false;
                }
            }
        };
    }

    static <T> Spliterator<T> singletonSpliterator(final T t) {
        return new Spliterator<T>() {
            long est = 1;

            public Spliterator<T> trySplit() {
                return null;
            }

            public boolean tryAdvance(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                long j = this.est;
                if (j <= 0) {
                    return false;
                }
                this.est = j - 1;
                consumer.accept(Object.this);
                return true;
            }

            public void forEachRemaining(Consumer<? super T> consumer) {
                tryAdvance(consumer);
            }

            public long estimateSize() {
                return this.est;
            }

            public int characteristics() {
                return (Object.this != null ? 256 : 0) | 64 | 16384 | 1024 | 1 | 16;
            }
        };
    }

    private static class SingletonSet<E> extends AbstractSet<E> implements Serializable {
        private static final long serialVersionUID = 3193687207550431679L;
        private final E element;

        public int size() {
            return 1;
        }

        SingletonSet(E e) {
            this.element = e;
        }

        public Iterator<E> iterator() {
            return Collections.singletonIterator(this.element);
        }

        public boolean contains(Object obj) {
            return Collections.m1715eq(obj, this.element);
        }

        public void forEach(Consumer<? super E> consumer) {
            consumer.accept(this.element);
        }

        public Spliterator<E> spliterator() {
            return Collections.singletonSpliterator(this.element);
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            throw new UnsupportedOperationException();
        }
    }

    public static <T> List<T> singletonList(T t) {
        return new SingletonList(t);
    }

    private static class SingletonList<E> extends AbstractList<E> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 3093736618740652951L;
        private final E element;

        public int size() {
            return 1;
        }

        public void sort(Comparator<? super E> comparator) {
        }

        SingletonList(E e) {
            this.element = e;
        }

        public Iterator<E> iterator() {
            return Collections.singletonIterator(this.element);
        }

        public boolean contains(Object obj) {
            return Collections.m1715eq(obj, this.element);
        }

        public E get(int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: 1");
        }

        public void forEach(Consumer<? super E> consumer) {
            consumer.accept(this.element);
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            throw new UnsupportedOperationException();
        }

        public void replaceAll(UnaryOperator<E> unaryOperator) {
            throw new UnsupportedOperationException();
        }

        public Spliterator<E> spliterator() {
            return Collections.singletonSpliterator(this.element);
        }
    }

    public static <K, V> Map<K, V> singletonMap(K k, V v) {
        return new SingletonMap(k, v);
    }

    private static class SingletonMap<K, V> extends AbstractMap<K, V> implements Serializable {
        private static final long serialVersionUID = -6979724477215052911L;
        private transient Set<Map.Entry<K, V>> entrySet;

        /* renamed from: k */
        private final K f655k;
        private transient Set<K> keySet;

        /* renamed from: v */
        private final V f656v;
        private transient Collection<V> values;

        public boolean isEmpty() {
            return false;
        }

        public int size() {
            return 1;
        }

        SingletonMap(K k, V v) {
            this.f655k = k;
            this.f656v = v;
        }

        public boolean containsKey(Object obj) {
            return Collections.m1715eq(obj, this.f655k);
        }

        public boolean containsValue(Object obj) {
            return Collections.m1715eq(obj, this.f656v);
        }

        public V get(Object obj) {
            if (Collections.m1715eq(obj, this.f655k)) {
                return this.f656v;
            }
            return null;
        }

        public Set<K> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.singleton(this.f655k);
            }
            return this.keySet;
        }

        public Set<Map.Entry<K, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = Collections.singleton(new AbstractMap.SimpleImmutableEntry(this.f655k, this.f656v));
            }
            return this.entrySet;
        }

        public Collection<V> values() {
            if (this.values == null) {
                this.values = Collections.singleton(this.f656v);
            }
            return this.values;
        }

        public V getOrDefault(Object obj, V v) {
            return Collections.m1715eq(obj, this.f655k) ? this.f656v : v;
        }

        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            biConsumer.accept(this.f655k, this.f656v);
        }

        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        public V putIfAbsent(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object obj, Object obj2) {
            throw new UnsupportedOperationException();
        }

        public boolean replace(K k, V v, V v2) {
            throw new UnsupportedOperationException();
        }

        public V replace(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }
    }

    public static <T> List<T> nCopies(int i, T t) {
        if (i >= 0) {
            return new CopiesList(i, t);
        }
        throw new IllegalArgumentException("List length = " + i);
    }

    private static class CopiesList<E> extends AbstractList<E> implements RandomAccess, Serializable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long serialVersionUID = 2739099268398711800L;
        final E element;

        /* renamed from: n */
        final int f652n;

        static {
            Class<Collections> cls = Collections.class;
        }

        CopiesList(int i, E e) {
            this.f652n = i;
            this.element = e;
        }

        public int size() {
            return this.f652n;
        }

        public boolean contains(Object obj) {
            return this.f652n != 0 && Collections.m1715eq(obj, this.element);
        }

        public int indexOf(Object obj) {
            return contains(obj) ? 0 : -1;
        }

        public int lastIndexOf(Object obj) {
            if (contains(obj)) {
                return this.f652n - 1;
            }
            return -1;
        }

        public E get(int i) {
            if (i >= 0 && i < this.f652n) {
                return this.element;
            }
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + this.f652n);
        }

        public Object[] toArray() {
            int i = this.f652n;
            Object[] objArr = new Object[i];
            E e = this.element;
            if (e != null) {
                Arrays.fill(objArr, 0, i, (Object) e);
            }
            return objArr;
        }

        public <T> T[] toArray(T[] tArr) {
            int i = this.f652n;
            if (tArr.length < i) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i);
                E e = this.element;
                if (e != null) {
                    Arrays.fill((Object[]) tArr, 0, i, (Object) e);
                }
            } else {
                Arrays.fill((Object[]) tArr, 0, i, (Object) this.element);
                if (tArr.length > i) {
                    tArr[i] = null;
                }
            }
            return tArr;
        }

        public List<E> subList(int i, int i2) {
            if (i < 0) {
                throw new IndexOutOfBoundsException("fromIndex = " + i);
            } else if (i2 > this.f652n) {
                throw new IndexOutOfBoundsException("toIndex = " + i2);
            } else if (i <= i2) {
                return new CopiesList(i2 - i, this.element);
            } else {
                throw new IllegalArgumentException("fromIndex(" + i + ") > toIndex(" + i2 + NavigationBarInflaterView.KEY_CODE_END);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$stream$0$java-util-Collections$CopiesList  reason: not valid java name */
        public /* synthetic */ Object m3740lambda$stream$0$javautilCollections$CopiesList(int i) {
            return this.element;
        }

        public Stream<E> stream() {
            return IntStream.range(0, this.f652n).mapToObj(new Collections$CopiesList$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$parallelStream$1$java-util-Collections$CopiesList  reason: not valid java name */
        public /* synthetic */ Object m3739lambda$parallelStream$1$javautilCollections$CopiesList(int i) {
            return this.element;
        }

        public Stream<E> parallelStream() {
            return IntStream.range(0, this.f652n).parallel().mapToObj(new Collections$CopiesList$$ExternalSyntheticLambda0(this));
        }

        public Spliterator<E> spliterator() {
            return stream().spliterator();
        }
    }

    public static <T> Comparator<T> reverseOrder() {
        return ReverseComparator.REVERSE_ORDER;
    }

    private static class ReverseComparator implements Comparator<Comparable<Object>>, Serializable {
        static final ReverseComparator REVERSE_ORDER = new ReverseComparator();
        private static final long serialVersionUID = 7207038068494060240L;

        private ReverseComparator() {
        }

        public int compare(Comparable<Object> comparable, Comparable<Object> comparable2) {
            return comparable2.compareTo(comparable);
        }

        private Object readResolve() {
            return Collections.reverseOrder();
        }

        public Comparator<Comparable<Object>> reversed() {
            return Comparator.naturalOrder();
        }
    }

    public static <T> Comparator<T> reverseOrder(Comparator<T> comparator) {
        if (comparator == null) {
            return reverseOrder();
        }
        if (comparator instanceof ReverseComparator2) {
            return ((ReverseComparator2) comparator).cmp;
        }
        return new ReverseComparator2(comparator);
    }

    private static class ReverseComparator2<T> implements Comparator<T>, Serializable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long serialVersionUID = 4374092139857L;
        final Comparator<T> cmp;

        static {
            Class<Collections> cls = Collections.class;
        }

        ReverseComparator2(Comparator<T> comparator) {
            this.cmp = comparator;
        }

        public int compare(T t, T t2) {
            return this.cmp.compare(t2, t);
        }

        public boolean equals(Object obj) {
            return obj == this || ((obj instanceof ReverseComparator2) && this.cmp.equals(((ReverseComparator2) obj).cmp));
        }

        public int hashCode() {
            return this.cmp.hashCode() ^ Integer.MIN_VALUE;
        }

        public Comparator<T> reversed() {
            return this.cmp;
        }
    }

    public static <T> Enumeration<T> enumeration(Collection<T> collection) {
        return new Enumeration<T>() {

            /* renamed from: i */
            private final Iterator<T> f642i;

            {
                this.f642i = Collection.this.iterator();
            }

            public boolean hasMoreElements() {
                return this.f642i.hasNext();
            }

            public T nextElement() {
                return this.f642i.next();
            }
        };
    }

    public static <T> ArrayList<T> list(Enumeration<T> enumeration) {
        ArrayList<T> arrayList = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            arrayList.add(enumeration.nextElement());
        }
        return arrayList;
    }

    /* renamed from: eq */
    static boolean m1715eq(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    public static int frequency(Collection<?> collection, Object obj) {
        int i = 0;
        if (obj == null) {
            for (Object obj2 : collection) {
                if (obj2 == null) {
                    i++;
                }
            }
        } else {
            for (Object equals : collection) {
                if (obj.equals(equals)) {
                    i++;
                }
            }
        }
        return i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0017, code lost:
        if (r0 > r2) goto L_0x0019;
     */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0028  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean disjoint(java.util.Collection<?> r4, java.util.Collection<?> r5) {
        /*
            boolean r0 = r4 instanceof java.util.Set
            r1 = 1
            if (r0 == 0) goto L_0x0006
            goto L_0x0019
        L_0x0006:
            boolean r0 = r5 instanceof java.util.Set
            if (r0 != 0) goto L_0x001e
            int r0 = r4.size()
            int r2 = r5.size()
            if (r0 == 0) goto L_0x001d
            if (r2 != 0) goto L_0x0017
            goto L_0x001d
        L_0x0017:
            if (r0 <= r2) goto L_0x001e
        L_0x0019:
            r3 = r5
            r5 = r4
            r4 = r3
            goto L_0x001e
        L_0x001d:
            return r1
        L_0x001e:
            java.util.Iterator r4 = r4.iterator()
        L_0x0022:
            boolean r0 = r4.hasNext()
            if (r0 == 0) goto L_0x0034
            java.lang.Object r0 = r4.next()
            boolean r0 = r5.contains(r0)
            if (r0 == 0) goto L_0x0022
            r4 = 0
            return r4
        L_0x0034:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Collections.disjoint(java.util.Collection, java.util.Collection):boolean");
    }

    @SafeVarargs
    public static <T> boolean addAll(Collection<? super T> collection, T... tArr) {
        boolean z = false;
        for (T add : tArr) {
            z |= collection.add(add);
        }
        return z;
    }

    public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        return new SetFromMap(map);
    }

    private static class SetFromMap<E> extends AbstractSet<E> implements Set<E>, Serializable {
        private static final long serialVersionUID = 2454657854757543876L;

        /* renamed from: m */
        private final Map<E, Boolean> f653m;

        /* renamed from: s */
        private transient Set<E> f654s;

        SetFromMap(Map<E, Boolean> map) {
            if (map.isEmpty()) {
                this.f653m = map;
                this.f654s = map.keySet();
                return;
            }
            throw new IllegalArgumentException("Map is non-empty");
        }

        public void clear() {
            this.f653m.clear();
        }

        public int size() {
            return this.f653m.size();
        }

        public boolean isEmpty() {
            return this.f653m.isEmpty();
        }

        public boolean contains(Object obj) {
            return this.f653m.containsKey(obj);
        }

        public boolean remove(Object obj) {
            return this.f653m.remove(obj) != null;
        }

        public boolean add(E e) {
            return this.f653m.put(e, Boolean.TRUE) == null;
        }

        public Iterator<E> iterator() {
            return this.f654s.iterator();
        }

        public Object[] toArray() {
            return this.f654s.toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return this.f654s.toArray(tArr);
        }

        public String toString() {
            return this.f654s.toString();
        }

        public int hashCode() {
            return this.f654s.hashCode();
        }

        public boolean equals(Object obj) {
            return obj == this || this.f654s.equals(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return this.f654s.containsAll(collection);
        }

        public boolean removeAll(Collection<?> collection) {
            return this.f654s.removeAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return this.f654s.retainAll(collection);
        }

        public void forEach(Consumer<? super E> consumer) {
            this.f654s.forEach(consumer);
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            return this.f654s.removeIf(predicate);
        }

        public Spliterator<E> spliterator() {
            return this.f654s.spliterator();
        }

        public Stream<E> stream() {
            return this.f654s.stream();
        }

        public Stream<E> parallelStream() {
            return this.f654s.parallelStream();
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.f654s = this.f653m.keySet();
        }
    }

    public static <T> Queue<T> asLifoQueue(Deque<T> deque) {
        return new AsLIFOQueue(deque);
    }

    static class AsLIFOQueue<E> extends AbstractQueue<E> implements Queue<E>, Serializable {
        private static final long serialVersionUID = 1802017725587941708L;

        /* renamed from: q */
        private final Deque<E> f643q;

        AsLIFOQueue(Deque<E> deque) {
            this.f643q = deque;
        }

        public boolean add(E e) {
            this.f643q.addFirst(e);
            return true;
        }

        public boolean offer(E e) {
            return this.f643q.offerFirst(e);
        }

        public E poll() {
            return this.f643q.pollFirst();
        }

        public E remove() {
            return this.f643q.removeFirst();
        }

        public E peek() {
            return this.f643q.peekFirst();
        }

        public E element() {
            return this.f643q.getFirst();
        }

        public void clear() {
            this.f643q.clear();
        }

        public int size() {
            return this.f643q.size();
        }

        public boolean isEmpty() {
            return this.f643q.isEmpty();
        }

        public boolean contains(Object obj) {
            return this.f643q.contains(obj);
        }

        public boolean remove(Object obj) {
            return this.f643q.remove(obj);
        }

        public Iterator<E> iterator() {
            return this.f643q.iterator();
        }

        public Object[] toArray() {
            return this.f643q.toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return this.f643q.toArray(tArr);
        }

        public String toString() {
            return this.f643q.toString();
        }

        public boolean containsAll(Collection<?> collection) {
            return this.f643q.containsAll(collection);
        }

        public boolean removeAll(Collection<?> collection) {
            return this.f643q.removeAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return this.f643q.retainAll(collection);
        }

        public void forEach(Consumer<? super E> consumer) {
            this.f643q.forEach(consumer);
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            return this.f643q.removeIf(predicate);
        }

        public Spliterator<E> spliterator() {
            return this.f643q.spliterator();
        }

        public Stream<E> stream() {
            return this.f643q.stream();
        }

        public Stream<E> parallelStream() {
            return this.f643q.parallelStream();
        }
    }
}
