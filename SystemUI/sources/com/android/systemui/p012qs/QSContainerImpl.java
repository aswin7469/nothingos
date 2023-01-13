package com.android.systemui.p012qs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.customize.QSCustomizer;
import java.p026io.PrintWriter;

/* renamed from: com.android.systemui.qs.QSContainerImpl */
public class QSContainerImpl extends FrameLayout implements Dumpable {
    private boolean mClippingEnabled;
    private int mContentHorizontalPadding = -1;
    private int mFancyClippingBottom;
    private final Path mFancyClippingPath = new Path();
    private final float[] mFancyClippingRadii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private int mFancyClippingTop;
    private QuickStatusBarHeader mHeader;
    private int mHeightOverride = -1;
    private int mHorizontalMargins;
    private QSCustomizer mQSCustomizer;
    private NonInterceptingScrollView mQSPanelContainer;
    private boolean mQsDisabled;
    private float mQsExpansion;
    private int mTilesPageMargin;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean performClick() {
        return true;
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

    public QSContainerImpl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mQSPanelContainer = (NonInterceptingScrollView) findViewById(C1894R.C1898id.expanded_qs_scroll_view);
        this.mHeader = (QuickStatusBarHeader) findViewById(C1894R.C1898id.header);
        this.mQSCustomizer = (QSCustomizer) findViewById(C1894R.C1898id.qs_customize);
        setImportantForAccessibility(2);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mQSPanelContainer.getLayoutParams();
        int size = View.MeasureSpec.getSize(i2);
        int paddingBottom = ((size - marginLayoutParams.topMargin) - marginLayoutParams.bottomMargin) - getPaddingBottom();
        int i3 = this.mPaddingLeft + this.mPaddingRight + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        int i4 = Integer.MIN_VALUE;
        this.mQSPanelContainer.measure(getChildMeasureSpec(i, i3, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(paddingBottom, Integer.MIN_VALUE));
        int measuredWidth = this.mQSPanelContainer.getMeasuredWidth() + i3;
        boolean z = getResources().getConfiguration().orientation == 2;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824);
        if (!z) {
            i4 = 1073741824;
        }
        super.onMeasure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(size, i4));
        this.mQSCustomizer.measure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
    }

    public void dispatchDraw(Canvas canvas) {
        if (!this.mFancyClippingPath.isEmpty()) {
            canvas.translate(0.0f, -getTranslationY());
            canvas.clipOutPath(this.mFancyClippingPath);
            canvas.translate(0.0f, getTranslationY());
        }
        super.dispatchDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void measureChildWithMargins(View view, int i, int i2, int i3, int i4) {
        if (view != this.mQSPanelContainer) {
            super.measureChildWithMargins(view, i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateExpansion();
        updateClippingPath();
    }

    public void disable(int i, int i2, boolean z) {
        boolean z2 = true;
        if ((i2 & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.mQsDisabled) {
            this.mQsDisabled = z2;
        }
    }

    /* access modifiers changed from: package-private */
    public void updateResources(QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController) {
        NonInterceptingScrollView nonInterceptingScrollView = this.mQSPanelContainer;
        nonInterceptingScrollView.setPaddingRelative(nonInterceptingScrollView.getPaddingStart(), QSUtils.getQsHeaderSystemIconsAreaHeight(this.mContext), this.mQSPanelContainer.getPaddingEnd(), this.mQSPanelContainer.getPaddingBottom());
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.qs_horizontal_margin);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C1894R.dimen.qs_content_horizontal_padding);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(C1894R.dimen.qs_tiles_page_horizontal_margin);
        boolean z = (dimensionPixelSize2 == this.mContentHorizontalPadding && dimensionPixelSize == this.mHorizontalMargins && dimensionPixelSize3 == this.mTilesPageMargin) ? false : true;
        this.mContentHorizontalPadding = dimensionPixelSize2;
        this.mHorizontalMargins = dimensionPixelSize;
        this.mTilesPageMargin = dimensionPixelSize3;
        if (z) {
            updatePaddingsAndMargins(qSPanelController, quickStatusBarHeaderController);
        }
    }

    public void setHeightOverride(int i) {
        this.mHeightOverride = i;
        updateExpansion();
    }

    public void updateExpansion() {
        setBottom(getTop() + calculateContainerHeight());
    }

    /* access modifiers changed from: protected */
    public int calculateContainerHeight() {
        int i = this.mHeightOverride;
        if (i == -1) {
            i = getMeasuredHeight();
        }
        if (this.mQSCustomizer.isCustomizing()) {
            return this.mQSCustomizer.getHeight();
        }
        return this.mHeader.getHeight() + Math.round(this.mQsExpansion * ((float) (i - this.mHeader.getHeight())));
    }

    public void setExpansion(float f) {
        this.mQsExpansion = f;
        this.mQSPanelContainer.setScrollingEnabled(f > 0.0f);
        updateExpansion();
    }

    private void updatePaddingsAndMargins(QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != this.mQSCustomizer) {
                if (!(childAt instanceof FooterActionsView)) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                    layoutParams.rightMargin = this.mHorizontalMargins;
                    layoutParams.leftMargin = this.mHorizontalMargins;
                }
                if (childAt == this.mQSPanelContainer) {
                    int i2 = this.mContentHorizontalPadding;
                    qSPanelController.setContentMargins(i2, i2);
                    qSPanelController.setPageMargin(this.mTilesPageMargin);
                } else if (childAt == this.mHeader) {
                    int i3 = this.mContentHorizontalPadding;
                    quickStatusBarHeaderController.setContentMargins(i3, i3);
                } else {
                    childAt.setPaddingRelative(this.mContentHorizontalPadding, childAt.getPaddingTop(), this.mContentHorizontalPadding, childAt.getPaddingBottom());
                }
            }
        }
    }

    public void setFancyClipping(int i, int i2, int i3, boolean z) {
        float[] fArr = this.mFancyClippingRadii;
        boolean z2 = false;
        float f = (float) i3;
        boolean z3 = true;
        if (fArr[0] != f) {
            fArr[0] = f;
            fArr[1] = f;
            fArr[2] = f;
            fArr[3] = f;
            z2 = true;
        }
        if (this.mFancyClippingTop != i) {
            this.mFancyClippingTop = i;
            z2 = true;
        }
        if (this.mFancyClippingBottom != i2) {
            this.mFancyClippingBottom = i2;
            z2 = true;
        }
        if (this.mClippingEnabled != z) {
            this.mClippingEnabled = z;
        } else {
            z3 = z2;
        }
        if (z3) {
            updateClippingPath();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isTransformedTouchPointInView(float f, float f2, View view, PointF pointF) {
        if (!this.mClippingEnabled || getTranslationY() + f2 <= ((float) this.mFancyClippingTop)) {
            return super.isTransformedTouchPointInView(f, f2, view, pointF);
        }
        return false;
    }

    private void updateClippingPath() {
        this.mFancyClippingPath.reset();
        if (!this.mClippingEnabled) {
            invalidate();
            return;
        }
        this.mFancyClippingPath.addRoundRect(0.0f, (float) this.mFancyClippingTop, (float) getWidth(), (float) this.mFancyClippingBottom, this.mFancyClippingRadii, Path.Direction.CW);
        invalidate();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + " updateClippingPath: top(" + this.mFancyClippingTop + ") bottom(" + this.mFancyClippingBottom + ") mClippingEnabled(" + this.mClippingEnabled + NavigationBarInflaterView.KEY_CODE_END);
    }
}
