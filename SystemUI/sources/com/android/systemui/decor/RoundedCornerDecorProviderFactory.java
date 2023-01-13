package com.android.systemui.decor;

import com.android.systemui.C1894R;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n8VX\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/decor/RoundedCornerDecorProviderFactory;", "Lcom/android/systemui/decor/DecorProviderFactory;", "roundedCornerResDelegate", "Lcom/android/systemui/decor/RoundedCornerResDelegate;", "(Lcom/android/systemui/decor/RoundedCornerResDelegate;)V", "hasProviders", "", "getHasProviders", "()Z", "providers", "", "Lcom/android/systemui/decor/DecorProvider;", "getProviders", "()Ljava/util/List;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RoundedCornerDecorProviderFactory.kt */
public final class RoundedCornerDecorProviderFactory extends DecorProviderFactory {
    private final RoundedCornerResDelegate roundedCornerResDelegate;

    public RoundedCornerDecorProviderFactory(RoundedCornerResDelegate roundedCornerResDelegate2) {
        Intrinsics.checkNotNullParameter(roundedCornerResDelegate2, "roundedCornerResDelegate");
        this.roundedCornerResDelegate = roundedCornerResDelegate2;
    }

    public boolean getHasProviders() {
        RoundedCornerResDelegate roundedCornerResDelegate2 = this.roundedCornerResDelegate;
        return roundedCornerResDelegate2.getHasTop() || roundedCornerResDelegate2.getHasBottom();
    }

    public List<DecorProvider> getProviders() {
        boolean hasTop = this.roundedCornerResDelegate.getHasTop();
        boolean hasBottom = this.roundedCornerResDelegate.getHasBottom();
        if (hasTop && hasBottom) {
            return CollectionsKt.listOf(new RoundedCornerDecorProviderImpl(C1894R.C1898id.rounded_corner_top_left, 1, 0, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(C1894R.C1898id.rounded_corner_top_right, 1, 2, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(C1894R.C1898id.rounded_corner_bottom_left, 3, 0, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(C1894R.C1898id.rounded_corner_bottom_right, 3, 2, this.roundedCornerResDelegate));
        } else if (hasTop) {
            return CollectionsKt.listOf(new RoundedCornerDecorProviderImpl(C1894R.C1898id.rounded_corner_top_left, 1, 0, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(C1894R.C1898id.rounded_corner_top_right, 1, 2, this.roundedCornerResDelegate));
        } else if (!hasBottom) {
            return CollectionsKt.emptyList();
        } else {
            return CollectionsKt.listOf(new RoundedCornerDecorProviderImpl(C1894R.C1898id.rounded_corner_bottom_left, 3, 0, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(C1894R.C1898id.rounded_corner_bottom_right, 3, 2, this.roundedCornerResDelegate));
        }
    }
}
