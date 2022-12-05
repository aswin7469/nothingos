package com.android.internal.util.function.pooled;

import android.os.TraceNameSupplier;
import com.android.internal.util.FunctionalUtils;
/* loaded from: classes4.dex */
public interface PooledRunnable extends PooledLambda, Runnable, FunctionalUtils.ThrowingRunnable, TraceNameSupplier {
    @Override // com.android.internal.util.function.pooled.PooledSupplier.OfInt, com.android.internal.util.function.pooled.PooledSupplier.OfLong, com.android.internal.util.function.pooled.PooledSupplier.OfDouble
    /* renamed from: recycleOnUse  reason: collision with other method in class */
    PooledRunnable mo3494recycleOnUse();
}
