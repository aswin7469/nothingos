package java.util.concurrent;

import androidx.core.view.InputDeviceCompat;
import java.p026io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CopyOnWriteArraySet<E> extends AbstractSet<E> implements Serializable {
    private static final long serialVersionUID = 5457747651344034263L;

    /* renamed from: al */
    private final CopyOnWriteArrayList<E> f750al;

    public CopyOnWriteArraySet() {
        this.f750al = new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArraySet(Collection<? extends E> collection) {
        if (collection.getClass() == CopyOnWriteArraySet.class) {
            this.f750al = new CopyOnWriteArrayList<>(((CopyOnWriteArraySet) collection).f750al);
            return;
        }
        CopyOnWriteArrayList<E> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        this.f750al = copyOnWriteArrayList;
        copyOnWriteArrayList.addAllAbsent(collection);
    }

    public int size() {
        return this.f750al.size();
    }

    public boolean isEmpty() {
        return this.f750al.isEmpty();
    }

    public boolean contains(Object obj) {
        return this.f750al.contains(obj);
    }

    public Object[] toArray() {
        return this.f750al.toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return this.f750al.toArray(tArr);
    }

    public void clear() {
        this.f750al.clear();
    }

    public boolean remove(Object obj) {
        return this.f750al.remove(obj);
    }

    public boolean add(E e) {
        return this.f750al.addIfAbsent(e);
    }

    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof Set) {
            return compareSets(this.f750al.getArray(), (Set) collection) >= 0;
        }
        return this.f750al.containsAll(collection);
    }

    private static int compareSets(Object[] objArr, Set<?> set) {
        int length = objArr.length;
        boolean[] zArr = new boolean[length];
        int i = 0;
        for (Object next : set) {
            int i2 = i;
            while (i2 < length) {
                if (zArr[i2] || !Objects.equals(next, objArr[i2])) {
                    i2++;
                } else {
                    zArr[i2] = true;
                    if (i2 == i) {
                        do {
                            i++;
                            if (i >= length) {
                                break;
                            }
                        } while (!zArr[i]);
                    }
                }
            }
            return -1;
        }
        if (i == length) {
            return 0;
        }
        return 1;
    }

    public boolean addAll(Collection<? extends E> collection) {
        return this.f750al.addAllAbsent(collection) > 0;
    }

    public boolean removeAll(Collection<?> collection) {
        return this.f750al.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return this.f750al.retainAll(collection);
    }

    public Iterator<E> iterator() {
        return this.f750al.iterator();
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof Set) && compareSets(this.f750al.getArray(), (Set) obj) == 0);
    }

    public boolean removeIf(Predicate<? super E> predicate) {
        return this.f750al.removeIf(predicate);
    }

    public void forEach(Consumer<? super E> consumer) {
        this.f750al.forEach(consumer);
    }

    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this.f750al.getArray(), (int) InputDeviceCompat.SOURCE_GAMEPAD);
    }
}
