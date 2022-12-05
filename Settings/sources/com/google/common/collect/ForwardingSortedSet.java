package com.google.common.collect;

import java.util.Comparator;
import java.util.SortedSet;
/* loaded from: classes2.dex */
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E> implements SortedSet<E> {
    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    protected abstract SortedSet<E> mo848delegate();

    @Override // java.util.SortedSet
    public Comparator<? super E> comparator() {
        return mo848delegate().comparator();
    }

    @Override // java.util.SortedSet
    public E first() {
        return mo848delegate().first();
    }

    @Override // java.util.SortedSet
    public SortedSet<E> headSet(E e) {
        return mo848delegate().headSet(e);
    }

    @Override // java.util.SortedSet
    public E last() {
        return mo848delegate().last();
    }

    @Override // java.util.SortedSet
    public SortedSet<E> subSet(E e, E e2) {
        return mo848delegate().subSet(e, e2);
    }

    @Override // java.util.SortedSet
    public SortedSet<E> tailSet(E e) {
        return mo848delegate().tailSet(e);
    }
}
