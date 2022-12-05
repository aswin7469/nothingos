package com.google.common.base;
/* loaded from: classes2.dex */
class Suppliers$NonSerializableMemoizingSupplier<T> implements Supplier<T> {
    volatile Supplier<T> delegate;
    volatile boolean initialized;
    T value;

    @Override // com.google.common.base.Supplier
    /* renamed from: get */
    public T mo854get() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    T mo854get = this.delegate.mo854get();
                    this.value = mo854get;
                    this.initialized = true;
                    this.delegate = null;
                    return mo854get;
                }
            }
        }
        return this.value;
    }

    public String toString() {
        Object obj = this.delegate;
        StringBuilder sb = new StringBuilder();
        sb.append("Suppliers.memoize(");
        if (obj == null) {
            obj = "<supplier that returned " + this.value + ">";
        }
        sb.append(obj);
        sb.append(")");
        return sb.toString();
    }
}
