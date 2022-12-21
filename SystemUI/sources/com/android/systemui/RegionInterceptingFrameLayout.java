package com.android.systemui;

import android.content.Context;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class RegionInterceptingFrameLayout extends FrameLayout {
    private final ViewTreeObserver.OnComputeInternalInsetsListener mInsetsListener = new RegionInterceptingFrameLayout$$ExternalSyntheticLambda0(this);

    public interface RegionInterceptableView {
        Region getInterceptRegion();

        boolean shouldInterceptTouch() {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public RegionInterceptingFrameLayout(Context context) {
        super(context);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this.mInsetsListener);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsListener);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-RegionInterceptingFrameLayout  reason: not valid java name */
    public /* synthetic */ void m2522lambda$new$0$comandroidsystemuiRegionInterceptingFrameLayout(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        Region interceptRegion;
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.setEmpty();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof RegionInterceptableView) {
                RegionInterceptableView regionInterceptableView = (RegionInterceptableView) childAt;
                if (regionInterceptableView.shouldInterceptTouch() && (interceptRegion = regionInterceptableView.getInterceptRegion()) != null) {
                    internalInsetsInfo.touchableRegion.op(interceptRegion, Region.Op.UNION);
                }
            }
        }
    }
}
