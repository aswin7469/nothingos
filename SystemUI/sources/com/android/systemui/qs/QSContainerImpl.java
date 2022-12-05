package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.util.Utils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class QSContainerImpl extends FrameLayout implements Dumpable {
    private boolean mClippingEnabled;
    private int mFancyClippingBottom;
    private int mFancyClippingTop;
    private QuickStatusBarHeader mHeader;
    private QSCustomizer mQSCustomizer;
    private View mQSDetail;
    private NonInterceptingScrollView mQSPanelContainer;
    private boolean mQsDisabled;
    private float mQsExpansion;
    private boolean mShouldUseSplitNotificationShade;
    private int mSideMargins;
    private final Point mSizePoint = new Point();
    private final float[] mFancyClippingRadii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private final Path mFancyClippingPath = new Path();
    private int mHeightOverride = -1;
    private int mContentPadding = -1;
    private int mNavBarInset = 0;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.view.View
    public boolean performClick() {
        return true;
    }

    public QSContainerImpl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mQSPanelContainer = (NonInterceptingScrollView) findViewById(R$id.expanded_qs_scroll_view);
        this.mQSDetail = findViewById(R$id.qs_detail);
        this.mHeader = (QuickStatusBarHeader) findViewById(R$id.header);
        this.mQSCustomizer = (QSCustomizer) findViewById(R$id.qs_customize);
        setImportantForAccessibility(2);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mSizePoint.set(0, 0);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mNavBarInset = windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        NonInterceptingScrollView nonInterceptingScrollView = this.mQSPanelContainer;
        nonInterceptingScrollView.setPaddingRelative(nonInterceptingScrollView.getPaddingStart(), this.mQSPanelContainer.getPaddingTop(), this.mQSPanelContainer.getPaddingEnd(), this.mNavBarInset);
        return super.onApplyWindowInsets(windowInsets);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mQSPanelContainer.getLayoutParams();
        int displayHeight = ((getDisplayHeight() - marginLayoutParams.topMargin) - marginLayoutParams.bottomMargin) - getPaddingBottom();
        int i3 = ((FrameLayout) this).mPaddingLeft + ((FrameLayout) this).mPaddingRight + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        this.mQSPanelContainer.measure(FrameLayout.getChildMeasureSpec(i, i3, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(displayHeight, Integer.MIN_VALUE));
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(this.mQSPanelContainer.getMeasuredWidth() + i3, 1073741824), View.MeasureSpec.makeMeasureSpec(getDisplayHeight(), 1073741824));
        this.mQSCustomizer.measure(i, View.MeasureSpec.makeMeasureSpec(getDisplayHeight(), 1073741824));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (!this.mFancyClippingPath.isEmpty()) {
            canvas.translate(0.0f, -getTranslationY());
            canvas.clipOutPath(this.mFancyClippingPath);
            canvas.translate(0.0f, getTranslationY());
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View view, int i, int i2, int i3, int i4) {
        if (view != this.mQSPanelContainer) {
            super.measureChildWithMargins(view, i, i2, i3, i4);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateExpansion();
        updateClippingPath();
    }

    public void disable(int i, int i2, boolean z) {
        boolean z2 = true;
        if ((i2 & 1) == 0) {
            z2 = false;
        }
        if (z2 == this.mQsDisabled) {
            return;
        }
        this.mQsDisabled = z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateResources(QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController) {
        this.mShouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade((FeatureFlags) Dependency.get(FeatureFlags.class), getResources());
        this.mHeader.updatePortQSStatusBarVisibility();
        this.mQSPanelContainer.setPaddingRelative(getPaddingStart(), ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(this.mShouldUseSplitNotificationShade ? R$dimen.quick_qs_offset_height : 17105476), getPaddingEnd(), getPaddingBottom());
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.notification_side_paddings);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.notification_shade_content_margin_horizontal);
        if (dimensionPixelSize2 == this.mContentPadding) {
            int i = this.mSideMargins;
        }
        this.mContentPadding = dimensionPixelSize2;
        this.mSideMargins = dimensionPixelSize;
        updatePaddingsAndMargins(qSPanelController, quickStatusBarHeaderController);
    }

    public void setHeightOverride(int i) {
        this.mHeightOverride = i;
        updateExpansion();
    }

    public void updateExpansion() {
        int calculateContainerHeight = calculateContainerHeight();
        int calculateContainerBottom = calculateContainerBottom();
        setBottom(getTop() + calculateContainerHeight);
        this.mQSDetail.setBottom(getTop() + calculateContainerBottom);
        this.mQSDetail.setBottom((getTop() + calculateContainerBottom) - ((ViewGroup.MarginLayoutParams) this.mQSDetail.getLayoutParams()).bottomMargin);
    }

    protected int calculateContainerHeight() {
        int i = this.mHeightOverride;
        if (i == -1) {
            i = getMeasuredHeight();
        }
        if (this.mQSCustomizer.isCustomizing()) {
            return this.mQSCustomizer.getHeight();
        }
        return this.mHeader.getHeight() + Math.round(this.mQsExpansion * (i - this.mHeader.getHeight()));
    }

    int calculateContainerBottom() {
        int i = this.mHeightOverride;
        if (i == -1) {
            i = getMeasuredHeight();
        }
        if (this.mQSCustomizer.isCustomizing()) {
            return this.mQSCustomizer.getHeight();
        }
        return this.mHeader.getHeight() + Math.round(this.mQsExpansion * (((i + this.mQSPanelContainer.getScrollRange()) - this.mQSPanelContainer.getScrollY()) - this.mHeader.getHeight()));
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
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                int i2 = this.mSideMargins;
                layoutParams.rightMargin = i2;
                boolean z = this.mShouldUseSplitNotificationShade;
                if (z) {
                    i2 = 0;
                }
                layoutParams.leftMargin = i2;
                if (childAt == this.mQSPanelContainer) {
                    qSPanelController.setContentMargins(z ? 0 : this.mContentPadding, this.mContentPadding);
                    qSPanelController.setPageMargin(this.mSideMargins);
                } else if (childAt == this.mHeader) {
                    quickStatusBarHeaderController.setContentMargins(z ? 0 : this.mContentPadding, this.mContentPadding);
                } else {
                    childAt.setPaddingRelative(z ? 0 : this.mContentPadding, childAt.getPaddingTop(), this.mContentPadding, childAt.getPaddingBottom());
                }
            }
        }
    }

    private int getDisplayHeight() {
        if (this.mSizePoint.y == 0) {
            getDisplay().getRealSize(this.mSizePoint);
        }
        return this.mSizePoint.y;
    }

    public void setFancyClipping(int i, int i2, int i3, boolean z) {
        float[] fArr = this.mFancyClippingRadii;
        boolean z2 = false;
        float f = i3;
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

    protected boolean isTransformedTouchPointInView(float f, float f2, View view, PointF pointF) {
        if (!this.mClippingEnabled || getTranslationY() + f2 <= this.mFancyClippingTop) {
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
        this.mFancyClippingPath.addRoundRect(0.0f, this.mFancyClippingTop, getWidth(), this.mFancyClippingBottom, this.mFancyClippingRadii, Path.Direction.CW);
        invalidate();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + " updateClippingPath: top(" + this.mFancyClippingTop + ") bottom(" + this.mFancyClippingBottom + ") mClippingEnabled(" + this.mClippingEnabled + ")");
    }
}
