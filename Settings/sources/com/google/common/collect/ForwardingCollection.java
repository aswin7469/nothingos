package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: classes2.dex */
public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E> {
    @Override // com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    protected abstract Collection<E> mo848delegate();

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return mo848delegate().iterator();
    }

    @Override // java.util.Collection
    public int size() {
        return mo848delegate().size();
    }

    @Override // java.util.Collection
    @CanIgnoreReturnValue
    public boolean removeAll(Collection<?> collection) {
        return mo848delegate().removeAll(collection);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return mo848delegate().isEmpty();
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        return mo848delegate().contains(obj);
    }

    @Override // java.util.Collection
    @CanIgnoreReturnValue
    public boolean add(E e) {
        return mo848delegate().add(e);
    }

    @Override // java.util.Collection
    @CanIgnoreReturnValue
    public boolean remove(Object obj) {
        return mo848delegate().remove(obj);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return mo848delegate().containsAll(collection);
    }

    @Override // java.util.Collection
    @CanIgnoreReturnValue
    public boolean addAll(Collection<? extends E> collection) {
        return mo848delegate().addAll(collection);
    }

    @Override // java.util.Collection
    @CanIgnoreReturnValue
    public boolean retainAll(Collection<?> collection) {
        return mo848delegate().retainAll(collection);
    }

    @Override // java.util.Collection
    public void clear() {
        mo848delegate().clear();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return mo848delegate().toArray();
    }

    @Override // java.util.Collection
    @CanIgnoreReturnValue
    public <T> T[] toArray(T[] tArr) {
        return (T[]) mo848delegate().toArray(tArr);
    }
}
