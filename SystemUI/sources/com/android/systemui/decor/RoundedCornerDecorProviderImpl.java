package com.android.systemui.decor;

import android.content.Context;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ \u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0003H\u0016J\u0018\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0015\u001a\u00020\u0003H\u0002J*\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00032\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016R\u0014\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u0005\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n¨\u0006\u001e"}, mo65043d2 = {"Lcom/android/systemui/decor/RoundedCornerDecorProviderImpl;", "Lcom/android/systemui/decor/CornerDecorProvider;", "viewId", "", "alignedBound1", "alignedBound2", "roundedCornerResDelegate", "Lcom/android/systemui/decor/RoundedCornerResDelegate;", "(IIILcom/android/systemui/decor/RoundedCornerResDelegate;)V", "getAlignedBound1", "()I", "getAlignedBound2", "isTop", "", "getViewId", "inflateView", "Landroid/view/View;", "context", "Landroid/content/Context;", "parent", "Landroid/view/ViewGroup;", "rotation", "initView", "", "view", "Landroid/widget/ImageView;", "onReloadResAndMeasure", "reloadToken", "displayUniqueId", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RoundedCornerDecorProviderImpl.kt */
public final class RoundedCornerDecorProviderImpl extends CornerDecorProvider {
    private final int alignedBound1;
    private final int alignedBound2;
    private final boolean isTop = getAlignedBounds().contains(1);
    private final RoundedCornerResDelegate roundedCornerResDelegate;
    private final int viewId;

    public int getViewId() {
        return this.viewId;
    }

    /* access modifiers changed from: protected */
    public int getAlignedBound1() {
        return this.alignedBound1;
    }

    /* access modifiers changed from: protected */
    public int getAlignedBound2() {
        return this.alignedBound2;
    }

    public RoundedCornerDecorProviderImpl(int i, int i2, int i3, RoundedCornerResDelegate roundedCornerResDelegate2) {
        Intrinsics.checkNotNullParameter(roundedCornerResDelegate2, "roundedCornerResDelegate");
        this.viewId = i;
        this.alignedBound1 = i2;
        this.alignedBound2 = i3;
        this.roundedCornerResDelegate = roundedCornerResDelegate2;
    }

    public View inflateView(Context context, ViewGroup viewGroup, int i) {
        Size size;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        ImageView imageView = new ImageView(context);
        imageView.setId(getViewId());
        initView(imageView, i);
        if (this.isTop) {
            size = this.roundedCornerResDelegate.getTopRoundedSize();
        } else {
            size = this.roundedCornerResDelegate.getBottomRoundedSize();
        }
        View view = imageView;
        viewGroup.addView(view, new FrameLayout.LayoutParams(size.getWidth(), size.getHeight(), RoundedCornerDecorProviderImplKt.toLayoutGravity(getAlignedBound2(), i) | RoundedCornerDecorProviderImplKt.toLayoutGravity(getAlignedBound1(), i)));
        return view;
    }

    private final void initView(ImageView imageView, int i) {
        RoundedCornerDecorProviderImplKt.setRoundedCornerImage(imageView, this.roundedCornerResDelegate, this.isTop);
        RoundedCornerDecorProviderImplKt.adjustRotation(imageView, getAlignedBounds(), i);
        imageView.setImageTintList(this.roundedCornerResDelegate.getColorTintList());
    }

    public void onReloadResAndMeasure(View view, int i, int i2, String str) {
        Size size;
        Intrinsics.checkNotNullParameter(view, "view");
        this.roundedCornerResDelegate.updateDisplayUniqueId(str, Integer.valueOf(i));
        ImageView imageView = (ImageView) view;
        initView(imageView, i2);
        if (this.isTop) {
            size = this.roundedCornerResDelegate.getTopRoundedSize();
        } else {
            size = this.roundedCornerResDelegate.getBottomRoundedSize();
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (layoutParams != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
            layoutParams2.width = size.getWidth();
            layoutParams2.height = size.getHeight();
            layoutParams2.gravity = RoundedCornerDecorProviderImplKt.toLayoutGravity(getAlignedBound2(), i2) | RoundedCornerDecorProviderImplKt.toLayoutGravity(getAlignedBound1(), i2);
            view.setLayoutParams(layoutParams2);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
    }
}
