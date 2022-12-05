package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: classes2.dex */
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {
    @Override // com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    protected abstract Map<K, V> mo848delegate();

    @Override // java.util.Map
    public int size() {
        return mo848delegate().size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return mo848delegate().isEmpty();
    }

    @Override // java.util.Map
    @CanIgnoreReturnValue
    public V remove(Object obj) {
        return mo848delegate().remove(obj);
    }

    @Override // java.util.Map
    public void clear() {
        mo848delegate().clear();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return mo848delegate().containsKey(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return mo848delegate().containsValue(obj);
    }

    @Override // java.util.Map
    public V get(Object obj) {
        return mo848delegate().get(obj);
    }

    @Override // java.util.Map
    @CanIgnoreReturnValue
    public V put(K k, V v) {
        return mo848delegate().put(k, v);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        mo848delegate().putAll(map);
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return mo848delegate().keySet();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return mo848delegate().values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return mo848delegate().entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        return obj == this || mo848delegate().equals(obj);
    }

    @Override // java.util.Map
    public int hashCode() {
        return mo848delegate().hashCode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String standardToString() {
        return Maps.toStringImpl(this);
    }
}
