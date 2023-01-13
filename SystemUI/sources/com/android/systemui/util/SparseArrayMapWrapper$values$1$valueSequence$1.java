package com.android.systemui.util;

import com.android.systemui.util.SparseArrayMapWrapper;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\f\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\n \u0002*\u0004\u0018\u0001H\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\u0014\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0002*\u0004\u0018\u0001H\u0001H\u00010\u0004H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, mo65043d2 = {"<anonymous>", "T", "kotlin.jvm.PlatformType", "it", "Lcom/android/systemui/util/SparseArrayMapWrapper$Entry;", "invoke", "(Lcom/android/systemui/util/SparseArrayMapWrapper$Entry;)Ljava/lang/Object;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SparseArrayUtils.kt */
final class SparseArrayMapWrapper$values$1$valueSequence$1 extends Lambda implements Function1<SparseArrayMapWrapper.Entry<T>, T> {
    public static final SparseArrayMapWrapper$values$1$valueSequence$1 INSTANCE = new SparseArrayMapWrapper$values$1$valueSequence$1();

    SparseArrayMapWrapper$values$1$valueSequence$1() {
        super(1);
    }

    public final T invoke(SparseArrayMapWrapper.Entry<T> entry) {
        Intrinsics.checkNotNullParameter(entry, "it");
        return entry.getValue();
    }
}
