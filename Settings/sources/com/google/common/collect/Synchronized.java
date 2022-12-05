package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Synchronized {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class SynchronizedObject implements Serializable {
        private static final long serialVersionUID = 0;
        final Object delegate;
        final Object mutex;

        SynchronizedObject(Object obj, Object obj2) {
            this.delegate = Preconditions.checkNotNull(obj);
            this.mutex = obj2 == null ? this : obj2;
        }

        /* renamed from: delegate */
        Object mo839delegate() {
            return this.delegate;
        }

        public String toString() {
            String obj;
            synchronized (this.mutex) {
                obj = this.delegate.toString();
            }
            return obj;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            synchronized (this.mutex) {
                objectOutputStream.defaultWriteObject();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> Collection<E> collection(Collection<E> collection, Object obj) {
        return new SynchronizedCollection(collection, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class SynchronizedCollection<E> extends SynchronizedObject implements Collection<E> {
        private static final long serialVersionUID = 0;

        private SynchronizedCollection(Collection<E> collection, Object obj) {
            super(collection, obj);
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate */
        Collection<E> mo839delegate() {
            return (Collection) super.mo839delegate();
        }

        @Override // java.util.Collection
        public boolean add(E e) {
            boolean add;
            synchronized (this.mutex) {
                add = mo839delegate().add(e);
            }
            return add;
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            boolean addAll;
            synchronized (this.mutex) {
                addAll = mo839delegate().addAll(collection);
            }
            return addAll;
        }

        @Override // java.util.Collection
        public void clear() {
            synchronized (this.mutex) {
                mo839delegate().clear();
            }
        }

        @Override // java.util.Collection
        public boolean contains(Object obj) {
            boolean contains;
            synchronized (this.mutex) {
                contains = mo839delegate().contains(obj);
            }
            return contains;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            boolean containsAll;
            synchronized (this.mutex) {
                containsAll = mo839delegate().containsAll(collection);
            }
            return containsAll;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.mutex) {
                isEmpty = mo839delegate().isEmpty();
            }
            return isEmpty;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator<E> iterator() {
            return mo839delegate().iterator();
        }

        @Override // java.util.Collection
        public boolean remove(Object obj) {
            boolean remove;
            synchronized (this.mutex) {
                remove = mo839delegate().remove(obj);
            }
            return remove;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            boolean removeAll;
            synchronized (this.mutex) {
                removeAll = mo839delegate().removeAll(collection);
            }
            return removeAll;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            boolean retainAll;
            synchronized (this.mutex) {
                retainAll = mo839delegate().retainAll(collection);
            }
            return retainAll;
        }

        @Override // java.util.Collection
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = mo839delegate().size();
            }
            return size;
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            Object[] array;
            synchronized (this.mutex) {
                array = mo839delegate().toArray();
            }
            return array;
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            T[] tArr2;
            synchronized (this.mutex) {
                tArr2 = (T[]) mo839delegate().toArray(tArr);
            }
            return tArr2;
        }
    }

    static <E> Set<E> set(Set<E> set, Object obj) {
        return new SynchronizedSet(set, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
        private static final long serialVersionUID = 0;

        SynchronizedSet(Set<E> set, Object obj) {
            super(set, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedCollection, com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate  reason: collision with other method in class */
        public Set<E> mo839delegate() {
            return (Set) super.mo839delegate();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(Object obj) {
            boolean equals;
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                equals = mo839delegate().equals(obj);
            }
            return equals;
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = mo839delegate().hashCode();
            }
            return hashCode;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> SortedSet<E> sortedSet(SortedSet<E> sortedSet, Object obj) {
        return new SynchronizedSortedSet(sortedSet, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E> {
        private static final long serialVersionUID = 0;

        SynchronizedSortedSet(SortedSet<E> sortedSet, Object obj) {
            super(sortedSet, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedSet, com.google.common.collect.Synchronized.SynchronizedCollection, com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate */
        public SortedSet<E> mo839delegate() {
            return (SortedSet) super.mo839delegate();
        }

        @Override // java.util.SortedSet
        public Comparator<? super E> comparator() {
            Comparator<? super E> comparator;
            synchronized (this.mutex) {
                comparator = mo839delegate().comparator();
            }
            return comparator;
        }

        public SortedSet<E> subSet(E e, E e2) {
            SortedSet<E> sortedSet;
            synchronized (this.mutex) {
                sortedSet = Synchronized.sortedSet(mo839delegate().subSet(e, e2), this.mutex);
            }
            return sortedSet;
        }

        public SortedSet<E> headSet(E e) {
            SortedSet<E> sortedSet;
            synchronized (this.mutex) {
                sortedSet = Synchronized.sortedSet(mo839delegate().headSet(e), this.mutex);
            }
            return sortedSet;
        }

        public SortedSet<E> tailSet(E e) {
            SortedSet<E> sortedSet;
            synchronized (this.mutex) {
                sortedSet = Synchronized.sortedSet(mo839delegate().tailSet(e), this.mutex);
            }
            return sortedSet;
        }

        @Override // java.util.SortedSet
        public E first() {
            E first;
            synchronized (this.mutex) {
                first = mo839delegate().first();
            }
            return first;
        }

        @Override // java.util.SortedSet
        public E last() {
            E last;
            synchronized (this.mutex) {
                last = mo839delegate().last();
            }
            return last;
        }
    }

    static <K, V> Map<K, V> map(Map<K, V> map, Object obj) {
        return new SynchronizedMap(map, obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class SynchronizedMap<K, V> extends SynchronizedObject implements Map<K, V> {
        private static final long serialVersionUID = 0;
        transient Set<Map.Entry<K, V>> entrySet;
        transient Set<K> keySet;
        transient Collection<V> values;

        SynchronizedMap(Map<K, V> map, Object obj) {
            super(map, obj);
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate */
        Map<K, V> mo839delegate() {
            return (Map) super.mo839delegate();
        }

        @Override // java.util.Map
        public void clear() {
            synchronized (this.mutex) {
                mo839delegate().clear();
            }
        }

        @Override // java.util.Map
        public boolean containsKey(Object obj) {
            boolean containsKey;
            synchronized (this.mutex) {
                containsKey = mo839delegate().containsKey(obj);
            }
            return containsKey;
        }

        @Override // java.util.Map
        public boolean containsValue(Object obj) {
            boolean containsValue;
            synchronized (this.mutex) {
                containsValue = mo839delegate().containsValue(obj);
            }
            return containsValue;
        }

        @Override // java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set;
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.set(mo839delegate().entrySet(), this.mutex);
                }
                set = this.entrySet;
            }
            return set;
        }

        @Override // java.util.Map
        public V get(Object obj) {
            V v;
            synchronized (this.mutex) {
                v = mo839delegate().get(obj);
            }
            return v;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.mutex) {
                isEmpty = mo839delegate().isEmpty();
            }
            return isEmpty;
        }

        @Override // java.util.Map
        public Set<K> keySet() {
            Set<K> set;
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = Synchronized.set(mo839delegate().keySet(), this.mutex);
                }
                set = this.keySet;
            }
            return set;
        }

        @Override // java.util.Map
        public V put(K k, V v) {
            V put;
            synchronized (this.mutex) {
                put = mo839delegate().put(k, v);
            }
            return put;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends V> map) {
            synchronized (this.mutex) {
                mo839delegate().putAll(map);
            }
        }

        @Override // java.util.Map
        public V remove(Object obj) {
            V remove;
            synchronized (this.mutex) {
                remove = mo839delegate().remove(obj);
            }
            return remove;
        }

        @Override // java.util.Map
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = mo839delegate().size();
            }
            return size;
        }

        /* renamed from: values */
        public Collection<V> mo833values() {
            Collection<V> collection;
            synchronized (this.mutex) {
                if (this.values == null) {
                    this.values = Synchronized.collection(mo839delegate().values(), this.mutex);
                }
                collection = this.values;
            }
            return collection;
        }

        @Override // java.util.Map
        public boolean equals(Object obj) {
            boolean equals;
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                equals = mo839delegate().equals(obj);
            }
            return equals;
        }

        @Override // java.util.Map
        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = mo839delegate().hashCode();
            }
            return hashCode;
        }
    }

    static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, Object obj) {
        return new SynchronizedSortedMap(sortedMap, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {
        private static final long serialVersionUID = 0;

        SynchronizedSortedMap(SortedMap<K, V> sortedMap, Object obj) {
            super(sortedMap, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedMap, com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate  reason: collision with other method in class */
        public SortedMap<K, V> mo839delegate() {
            return (SortedMap) super.mo839delegate();
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator;
            synchronized (this.mutex) {
                comparator = mo839delegate().comparator();
            }
            return comparator;
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            K firstKey;
            synchronized (this.mutex) {
                firstKey = mo839delegate().firstKey();
            }
            return firstKey;
        }

        public SortedMap<K, V> headMap(K k) {
            SortedMap<K, V> sortedMap;
            synchronized (this.mutex) {
                sortedMap = Synchronized.sortedMap(mo839delegate().headMap(k), this.mutex);
            }
            return sortedMap;
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            K lastKey;
            synchronized (this.mutex) {
                lastKey = mo839delegate().lastKey();
            }
            return lastKey;
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            SortedMap<K, V> sortedMap;
            synchronized (this.mutex) {
                sortedMap = Synchronized.sortedMap(mo839delegate().subMap(k, k2), this.mutex);
            }
            return sortedMap;
        }

        public SortedMap<K, V> tailMap(K k) {
            SortedMap<K, V> sortedMap;
            synchronized (this.mutex) {
                sortedMap = Synchronized.sortedMap(mo839delegate().tailMap(k), this.mutex);
            }
            return sortedMap;
        }
    }

    /* loaded from: classes2.dex */
    static class SynchronizedBiMap<K, V> extends SynchronizedMap<K, V> implements BiMap<K, V> {
        private static final long serialVersionUID = 0;
        private transient Set<V> valueSet;

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedMap, com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate */
        public BiMap<K, V> mo839delegate() {
            return (BiMap) super.mo839delegate();
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedMap, java.util.Map, com.google.common.collect.BiMap
        /* renamed from: values  reason: collision with other method in class */
        public Set<V> mo833values() {
            Set<V> set;
            synchronized (this.mutex) {
                if (this.valueSet == null) {
                    this.valueSet = Synchronized.set(mo839delegate().mo833values(), this.mutex);
                }
                set = this.valueSet;
            }
            return set;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {
        private static final long serialVersionUID = 0;
        transient NavigableSet<E> descendingSet;

        SynchronizedNavigableSet(NavigableSet<E> navigableSet, Object obj) {
            super(navigableSet, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedSortedSet, com.google.common.collect.Synchronized.SynchronizedSet, com.google.common.collect.Synchronized.SynchronizedCollection, com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate  reason: collision with other method in class */
        public NavigableSet<E> mo839delegate() {
            return (NavigableSet) super.mo839delegate();
        }

        @Override // java.util.NavigableSet
        public E ceiling(E e) {
            E ceiling;
            synchronized (this.mutex) {
                ceiling = mo839delegate().ceiling(e);
            }
            return ceiling;
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return mo839delegate().descendingIterator();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            synchronized (this.mutex) {
                NavigableSet<E> navigableSet = this.descendingSet;
                if (navigableSet == null) {
                    NavigableSet<E> navigableSet2 = Synchronized.navigableSet(mo839delegate().descendingSet(), this.mutex);
                    this.descendingSet = navigableSet2;
                    return navigableSet2;
                }
                return navigableSet;
            }
        }

        @Override // java.util.NavigableSet
        public E floor(E e) {
            E floor;
            synchronized (this.mutex) {
                floor = mo839delegate().floor(e);
            }
            return floor;
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e, boolean z) {
            NavigableSet<E> navigableSet;
            synchronized (this.mutex) {
                navigableSet = Synchronized.navigableSet(mo839delegate().headSet(e, z), this.mutex);
            }
            return navigableSet;
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public SortedSet<E> headSet(E e) {
            return headSet(e, false);
        }

        @Override // java.util.NavigableSet
        public E higher(E e) {
            E higher;
            synchronized (this.mutex) {
                higher = mo839delegate().higher(e);
            }
            return higher;
        }

        @Override // java.util.NavigableSet
        public E lower(E e) {
            E lower;
            synchronized (this.mutex) {
                lower = mo839delegate().lower(e);
            }
            return lower;
        }

        @Override // java.util.NavigableSet
        public E pollFirst() {
            E pollFirst;
            synchronized (this.mutex) {
                pollFirst = mo839delegate().pollFirst();
            }
            return pollFirst;
        }

        @Override // java.util.NavigableSet
        public E pollLast() {
            E pollLast;
            synchronized (this.mutex) {
                pollLast = mo839delegate().pollLast();
            }
            return pollLast;
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
            NavigableSet<E> navigableSet;
            synchronized (this.mutex) {
                navigableSet = Synchronized.navigableSet(mo839delegate().subSet(e, z, e2, z2), this.mutex);
            }
            return navigableSet;
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public SortedSet<E> subSet(E e, E e2) {
            return subSet(e, true, e2, false);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e, boolean z) {
            NavigableSet<E> navigableSet;
            synchronized (this.mutex) {
                navigableSet = Synchronized.navigableSet(mo839delegate().tailSet(e, z), this.mutex);
            }
            return navigableSet;
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public SortedSet<E> tailSet(E e) {
            return tailSet(e, true);
        }
    }

    static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, Object obj) {
        return new SynchronizedNavigableSet(navigableSet, obj);
    }

    static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, Object obj) {
        return new SynchronizedNavigableMap(navigableMap, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> {
        private static final long serialVersionUID = 0;
        transient NavigableSet<K> descendingKeySet;
        transient NavigableMap<K, V> descendingMap;
        transient NavigableSet<K> navigableKeySet;

        SynchronizedNavigableMap(NavigableMap<K, V> navigableMap, Object obj) {
            super(navigableMap, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedSortedMap, com.google.common.collect.Synchronized.SynchronizedMap, com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate  reason: collision with other method in class */
        public NavigableMap<K, V> mo839delegate() {
            return (NavigableMap) super.mo839delegate();
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> ceilingEntry(K k) {
            Map.Entry<K, V> nullableSynchronizedEntry;
            synchronized (this.mutex) {
                nullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(mo839delegate().ceilingEntry(k), this.mutex);
            }
            return nullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public K ceilingKey(K k) {
            K ceilingKey;
            synchronized (this.mutex) {
                ceilingKey = mo839delegate().ceilingKey(k);
            }
            return ceilingKey;
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            synchronized (this.mutex) {
                NavigableSet<K> navigableSet = this.descendingKeySet;
                if (navigableSet == null) {
                    NavigableSet<K> navigableSet2 = Synchronized.navigableSet(mo839delegate().descendingKeySet(), this.mutex);
                    this.descendingKeySet = navigableSet2;
                    return navigableSet2;
                }
                return navigableSet;
            }
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            synchronized (this.mutex) {
                NavigableMap<K, V> navigableMap = this.descendingMap;
                if (navigableMap == null) {
                    NavigableMap<K, V> navigableMap2 = Synchronized.navigableMap(mo839delegate().descendingMap(), this.mutex);
                    this.descendingMap = navigableMap2;
                    return navigableMap2;
                }
                return navigableMap;
            }
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> firstEntry() {
            Map.Entry<K, V> nullableSynchronizedEntry;
            synchronized (this.mutex) {
                nullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(mo839delegate().firstEntry(), this.mutex);
            }
            return nullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> floorEntry(K k) {
            Map.Entry<K, V> nullableSynchronizedEntry;
            synchronized (this.mutex) {
                nullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(mo839delegate().floorEntry(k), this.mutex);
            }
            return nullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public K floorKey(K k) {
            K floorKey;
            synchronized (this.mutex) {
                floorKey = mo839delegate().floorKey(k);
            }
            return floorKey;
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(K k, boolean z) {
            NavigableMap<K, V> navigableMap;
            synchronized (this.mutex) {
                navigableMap = Synchronized.navigableMap(mo839delegate().headMap(k, z), this.mutex);
            }
            return navigableMap;
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedSortedMap, java.util.SortedMap, java.util.NavigableMap
        public SortedMap<K, V> headMap(K k) {
            return headMap(k, false);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> higherEntry(K k) {
            Map.Entry<K, V> nullableSynchronizedEntry;
            synchronized (this.mutex) {
                nullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(mo839delegate().higherEntry(k), this.mutex);
            }
            return nullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public K higherKey(K k) {
            K higherKey;
            synchronized (this.mutex) {
                higherKey = mo839delegate().higherKey(k);
            }
            return higherKey;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lastEntry() {
            Map.Entry<K, V> nullableSynchronizedEntry;
            synchronized (this.mutex) {
                nullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(mo839delegate().lastEntry(), this.mutex);
            }
            return nullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lowerEntry(K k) {
            Map.Entry<K, V> nullableSynchronizedEntry;
            synchronized (this.mutex) {
                nullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(mo839delegate().lowerEntry(k), this.mutex);
            }
            return nullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public K lowerKey(K k) {
            K lowerKey;
            synchronized (this.mutex) {
                lowerKey = mo839delegate().lowerKey(k);
            }
            return lowerKey;
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedMap, java.util.Map
        public Set<K> keySet() {
            return navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            synchronized (this.mutex) {
                NavigableSet<K> navigableSet = this.navigableKeySet;
                if (navigableSet == null) {
                    NavigableSet<K> navigableSet2 = Synchronized.navigableSet(mo839delegate().navigableKeySet(), this.mutex);
                    this.navigableKeySet = navigableSet2;
                    return navigableSet2;
                }
                return navigableSet;
            }
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollFirstEntry() {
            Map.Entry<K, V> nullableSynchronizedEntry;
            synchronized (this.mutex) {
                nullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(mo839delegate().pollFirstEntry(), this.mutex);
            }
            return nullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollLastEntry() {
            Map.Entry<K, V> nullableSynchronizedEntry;
            synchronized (this.mutex) {
                nullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(mo839delegate().pollLastEntry(), this.mutex);
            }
            return nullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            NavigableMap<K, V> navigableMap;
            synchronized (this.mutex) {
                navigableMap = Synchronized.navigableMap(mo839delegate().subMap(k, z, k2, z2), this.mutex);
            }
            return navigableMap;
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedSortedMap, java.util.SortedMap, java.util.NavigableMap
        public SortedMap<K, V> subMap(K k, K k2) {
            return subMap(k, true, k2, false);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(K k, boolean z) {
            NavigableMap<K, V> navigableMap;
            synchronized (this.mutex) {
                navigableMap = Synchronized.navigableMap(mo839delegate().tailMap(k, z), this.mutex);
            }
            return navigableMap;
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedSortedMap, java.util.SortedMap, java.util.NavigableMap
        public SortedMap<K, V> tailMap(K k) {
            return tailMap(k, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(Map.Entry<K, V> entry, Object obj) {
        if (entry == null) {
            return null;
        }
        return new SynchronizedEntry(entry, obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class SynchronizedEntry<K, V> extends SynchronizedObject implements Map.Entry<K, V> {
        private static final long serialVersionUID = 0;

        SynchronizedEntry(Map.Entry<K, V> entry, Object obj) {
            super(entry, obj);
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedObject
        /* renamed from: delegate */
        Map.Entry<K, V> mo839delegate() {
            return (Map.Entry) super.mo839delegate();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            boolean equals;
            synchronized (this.mutex) {
                equals = mo839delegate().equals(obj);
            }
            return equals;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = mo839delegate().hashCode();
            }
            return hashCode;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            K key;
            synchronized (this.mutex) {
                key = mo839delegate().getKey();
            }
            return key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            V value;
            synchronized (this.mutex) {
                value = mo839delegate().getValue();
            }
            return value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            V value;
            synchronized (this.mutex) {
                value = mo839delegate().setValue(v);
            }
            return value;
        }
    }
}
