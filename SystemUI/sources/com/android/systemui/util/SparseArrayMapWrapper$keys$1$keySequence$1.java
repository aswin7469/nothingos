package com.android.systemui.util;

import com.android.systemui.util.SparseArrayMapWrapper;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0005*\u0004\u0018\u0001H\u0002H\u00020\u0004H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, mo64987d2 = {"<anonymous>", "", "T", "it", "Lcom/android/systemui/util/SparseArrayMapWrapper$Entry;", "kotlin.jvm.PlatformType", "invoke", "(Lcom/android/systemui/util/SparseArrayMapWrapper$Entry;)Ljava/lang/Integer;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SparseArrayUtils.kt */
final class SparseArrayMapWrapper$keys$1$keySequence$1 extends Lambda implements Function1<SparseArrayMapWrapper.Entry<T>, Integer> {
    public static final SparseArrayMapWrapper$keys$1$keySequence$1 INSTANCE = new SparseArrayMapWrapper$keys$1$keySequence$1();

    SparseArrayMapWrapper$keys$1$keySequence$1() {
        super(1);
    }

    public final Integer invoke(SparseArrayMapWrapper.Entry<T> entry) {
        Intrinsics.checkNotNullParameter(entry, "it");
        return entry.getKey();
    }
}
