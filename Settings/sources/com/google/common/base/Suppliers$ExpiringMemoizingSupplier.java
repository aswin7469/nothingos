package com.google.common.base;

import java.io.Serializable;
/* loaded from: classes2.dex */
class Suppliers$ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
    private static final long serialVersionUID = 0;
    final Supplier<T> delegate;
    final long durationNanos;
    volatile transient long expirationNanos;
    volatile transient T value;

    @Override // com.google.common.base.Supplier
    /* renamed from: get */
    public T mo854get() {
        long j = this.expirationNanos;
        long systemNanoTime = Platform.systemNanoTime();
        if (j == 0 || systemNanoTime - j >= 0) {
            synchronized (this) {
                if (j == this.expirationNanos) {
                    T mo854get = this.delegate.mo854get();
                    this.value = mo854get;
                    long j2 = systemNanoTime + this.durationNanos;
                    if (j2 == 0) {
                        j2 = 1;
                    }
                    this.expirationNanos = j2;
                    return mo854get;
                }
            }
        }
        return this.value;
    }

    public String toString() {
        return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
    }
}
