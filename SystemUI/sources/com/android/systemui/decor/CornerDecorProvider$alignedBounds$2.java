package com.android.systemui.decor;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\f\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\n¢\u0006\u0002\b\u0003"}, mo64987d2 = {"<anonymous>", "", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DecorProvider.kt */
final class CornerDecorProvider$alignedBounds$2 extends Lambda implements Function0<List<? extends Integer>> {
    final /* synthetic */ CornerDecorProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    CornerDecorProvider$alignedBounds$2(CornerDecorProvider cornerDecorProvider) {
        super(0);
        this.this$0 = cornerDecorProvider;
    }

    public final List<Integer> invoke() {
        return CollectionsKt.listOf(Integer.valueOf(this.this$0.getAlignedBound1()), Integer.valueOf(this.this$0.getAlignedBound2()));
    }
}
