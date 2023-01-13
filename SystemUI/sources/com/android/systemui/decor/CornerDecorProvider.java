package com.android.systemui.decor;

import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0018\u0010\u0003\u001a\u00020\u0004X¤\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007R\u0018\u0010\b\u001a\u00020\u0004X¤\u0004¢\u0006\f\u0012\u0004\b\t\u0010\u0002\u001a\u0004\b\n\u0010\u0007R!\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\f8VX\u0002¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000e¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/decor/CornerDecorProvider;", "Lcom/android/systemui/decor/DecorProvider;", "()V", "alignedBound1", "", "getAlignedBound1$annotations", "getAlignedBound1", "()I", "alignedBound2", "getAlignedBound2$annotations", "getAlignedBound2", "alignedBounds", "", "getAlignedBounds", "()Ljava/util/List;", "alignedBounds$delegate", "Lkotlin/Lazy;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DecorProvider.kt */
public abstract class CornerDecorProvider extends DecorProvider {
    private final Lazy alignedBounds$delegate = LazyKt.lazy(new CornerDecorProvider$alignedBounds$2(this));

    protected static /* synthetic */ void getAlignedBound1$annotations() {
    }

    protected static /* synthetic */ void getAlignedBound2$annotations() {
    }

    /* access modifiers changed from: protected */
    public abstract int getAlignedBound1();

    /* access modifiers changed from: protected */
    public abstract int getAlignedBound2();

    public List<Integer> getAlignedBounds() {
        return (List) this.alignedBounds$delegate.getValue();
    }
}
