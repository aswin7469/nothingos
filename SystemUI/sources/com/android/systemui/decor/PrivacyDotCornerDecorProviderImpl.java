package com.android.systemui.decor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J \u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0003H\u0016J*\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u00032\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016R\u0014\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\u0005\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u000e\u0010\u0006\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/decor/PrivacyDotCornerDecorProviderImpl;", "Lcom/android/systemui/decor/CornerDecorProvider;", "viewId", "", "alignedBound1", "alignedBound2", "layoutId", "(IIII)V", "getAlignedBound1", "()I", "getAlignedBound2", "getViewId", "inflateView", "Landroid/view/View;", "context", "Landroid/content/Context;", "parent", "Landroid/view/ViewGroup;", "rotation", "onReloadResAndMeasure", "", "view", "reloadToken", "displayUniqueId", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyDotDecorProviderFactory.kt */
public final class PrivacyDotCornerDecorProviderImpl extends CornerDecorProvider {
    private final int alignedBound1;
    private final int alignedBound2;
    private final int layoutId;
    private final int viewId;

    public void onReloadResAndMeasure(View view, int i, int i2, String str) {
        Intrinsics.checkNotNullParameter(view, "view");
    }

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

    public PrivacyDotCornerDecorProviderImpl(int i, int i2, int i3, int i4) {
        this.viewId = i;
        this.alignedBound1 = i2;
        this.alignedBound2 = i3;
        this.layoutId = i4;
    }

    public View inflateView(Context context, ViewGroup viewGroup, int i) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        LayoutInflater.from(context).inflate(this.layoutId, viewGroup, true);
        View childAt = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        Intrinsics.checkNotNullExpressionValue(childAt, "parent.getChildAt(parent…atest new added child */)");
        return childAt;
    }
}
