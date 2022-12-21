package com.android.systemui.util;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u0013\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00028\u0000¢\u0006\u0002\u0010\tJ\u0006\u0010\n\u001a\u00020\u0007J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\fH\u0002J\u0013\u0010\r\u001a\u00020\u00072\u0006\u0010\b\u001a\u00028\u0000¢\u0006\u0002\u0010\tR\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/util/ListenerSet;", "E", "", "()V", "listeners", "Ljava/util/concurrent/CopyOnWriteArrayList;", "addIfAbsent", "", "element", "(Ljava/lang/Object;)Z", "isEmpty", "iterator", "", "remove", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ListenerSet.kt */
public final class ListenerSet<E> implements Iterable<E>, KMappedMarker {
    private final CopyOnWriteArrayList<E> listeners = new CopyOnWriteArrayList<>();

    public final boolean addIfAbsent(E e) {
        return this.listeners.addIfAbsent(e);
    }

    public final boolean remove(E e) {
        return this.listeners.remove((Object) e);
    }

    public final boolean isEmpty() {
        return this.listeners.isEmpty();
    }

    public Iterator<E> iterator() {
        Iterator<E> it = this.listeners.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "listeners.iterator()");
        return it;
    }
}
