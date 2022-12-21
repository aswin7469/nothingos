package java.util.concurrent;

import android.icu.text.DateFormat;
import java.lang.reflect.Field;
import java.p026io.Serializable;
import java.security.AccessController;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable {
    private static final long serialVersionUID = -2479143111061671589L;

    /* renamed from: m */
    private final ConcurrentNavigableMap<E, Object> f748m;

    public ConcurrentSkipListSet() {
        this.f748m = new ConcurrentSkipListMap();
    }

    public ConcurrentSkipListSet(Comparator<? super E> comparator) {
        this.f748m = new ConcurrentSkipListMap(comparator);
    }

    public ConcurrentSkipListSet(Collection<? extends E> collection) {
        this.f748m = new ConcurrentSkipListMap();
        addAll(collection);
    }

    public ConcurrentSkipListSet(SortedSet<E> sortedSet) {
        this.f748m = new ConcurrentSkipListMap(sortedSet.comparator());
        addAll(sortedSet);
    }

    ConcurrentSkipListSet(ConcurrentNavigableMap<E, Object> concurrentNavigableMap) {
        this.f748m = concurrentNavigableMap;
    }

    public ConcurrentSkipListSet<E> clone() {
        try {
            ConcurrentSkipListSet<E> concurrentSkipListSet = (ConcurrentSkipListSet) super.clone();
            concurrentSkipListSet.setMap(new ConcurrentSkipListMap(this.f748m));
            return concurrentSkipListSet;
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    public int size() {
        return this.f748m.size();
    }

    public boolean isEmpty() {
        return this.f748m.isEmpty();
    }

    public boolean contains(Object obj) {
        return this.f748m.containsKey(obj);
    }

    public boolean add(E e) {
        return this.f748m.putIfAbsent(e, Boolean.TRUE) == null;
    }

    public boolean remove(Object obj) {
        return this.f748m.remove(obj, Boolean.TRUE);
    }

    public void clear() {
        this.f748m.clear();
    }

    public Iterator<E> iterator() {
        return this.f748m.navigableKeySet().iterator();
    }

    public Iterator<E> descendingIterator() {
        return this.f748m.descendingKeySet().iterator();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Collection collection = (Collection) obj;
        try {
            if (!containsAll(collection) || !collection.containsAll(this)) {
                return false;
            }
            return true;
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    public boolean removeAll(Collection<?> collection) {
        boolean z = false;
        for (Object remove : collection) {
            if (remove(remove)) {
                z = true;
            }
        }
        return z;
    }

    public E lower(E e) {
        return this.f748m.lowerKey(e);
    }

    public E floor(E e) {
        return this.f748m.floorKey(e);
    }

    public E ceiling(E e) {
        return this.f748m.ceilingKey(e);
    }

    public E higher(E e) {
        return this.f748m.higherKey(e);
    }

    public E pollFirst() {
        Map.Entry<E, Object> pollFirstEntry = this.f748m.pollFirstEntry();
        if (pollFirstEntry == null) {
            return null;
        }
        return pollFirstEntry.getKey();
    }

    public E pollLast() {
        Map.Entry<E, Object> pollLastEntry = this.f748m.pollLastEntry();
        if (pollLastEntry == null) {
            return null;
        }
        return pollLastEntry.getKey();
    }

    public Comparator<? super E> comparator() {
        return this.f748m.comparator();
    }

    public E first() {
        return this.f748m.firstKey();
    }

    public E last() {
        return this.f748m.lastKey();
    }

    public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
        return new ConcurrentSkipListSet(this.f748m.subMap((Object) e, z, (Object) e2, z2));
    }

    public NavigableSet<E> headSet(E e, boolean z) {
        return new ConcurrentSkipListSet(this.f748m.headMap((Object) e, z));
    }

    public NavigableSet<E> tailSet(E e, boolean z) {
        return new ConcurrentSkipListSet(this.f748m.tailMap((Object) e, z));
    }

    public NavigableSet<E> subSet(E e, E e2) {
        return subSet(e, true, e2, false);
    }

    public NavigableSet<E> headSet(E e) {
        return headSet(e, false);
    }

    public NavigableSet<E> tailSet(E e) {
        return tailSet(e, true);
    }

    public NavigableSet<E> descendingSet() {
        return new ConcurrentSkipListSet(this.f748m.descendingMap());
    }

    public Spliterator<E> spliterator() {
        ConcurrentNavigableMap<E, Object> concurrentNavigableMap = this.f748m;
        if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
            return ((ConcurrentSkipListMap) concurrentNavigableMap).keySpliterator();
        }
        ConcurrentSkipListMap.SubMap subMap = (ConcurrentSkipListMap.SubMap) concurrentNavigableMap;
        Objects.requireNonNull(subMap);
        return new ConcurrentSkipListMap.SubMap.SubMapKeyIterator();
    }

    private void setMap(ConcurrentNavigableMap<E, Object> concurrentNavigableMap) {
        try {
            ((Field) AccessController.doPrivileged(new ConcurrentSkipListSet$$ExternalSyntheticLambda0())).set(this, concurrentNavigableMap);
        } catch (IllegalAccessException e) {
            throw new Error((Throwable) e);
        }
    }

    static /* synthetic */ Field lambda$setMap$0() {
        try {
            Field declaredField = ConcurrentSkipListSet.class.getDeclaredField(DateFormat.MINUTE);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (ReflectiveOperationException e) {
            throw new Error((Throwable) e);
        }
    }
}
