package com.android.internal.util.function.pooled;

import com.android.internal.util.FunctionalUtils;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
/* loaded from: classes4.dex */
public interface PooledSupplier<T> extends PooledLambda, Supplier<T>, FunctionalUtils.ThrowingSupplier<T> {

    /* loaded from: classes4.dex */
    public interface OfDouble extends DoubleSupplier, PooledLambda {
        @Override // 
        /* renamed from: recycleOnUse  reason: collision with other method in class */
        OfDouble mo3494recycleOnUse();
    }

    /* loaded from: classes4.dex */
    public interface OfInt extends IntSupplier, PooledLambda {
        @Override // com.android.internal.util.function.pooled.PooledSupplier.OfLong, com.android.internal.util.function.pooled.PooledSupplier.OfDouble
        /* renamed from: recycleOnUse  reason: collision with other method in class */
        OfInt mo3494recycleOnUse();
    }

    /* loaded from: classes4.dex */
    public interface OfLong extends LongSupplier, PooledLambda {
        @Override // com.android.internal.util.function.pooled.PooledSupplier.OfDouble
        /* renamed from: recycleOnUse  reason: collision with other method in class */
        OfLong mo3494recycleOnUse();
    }

    PooledRunnable asRunnable();

    @Override // com.android.internal.util.function.pooled.PooledRunnable, com.android.internal.util.function.pooled.PooledSupplier.OfInt, com.android.internal.util.function.pooled.PooledSupplier.OfLong, com.android.internal.util.function.pooled.PooledSupplier.OfDouble
    /* renamed from: recycleOnUse  reason: collision with other method in class */
    PooledSupplier<T> mo3494recycleOnUse();
}
