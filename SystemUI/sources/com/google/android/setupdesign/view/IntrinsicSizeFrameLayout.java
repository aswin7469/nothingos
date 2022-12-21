package com.google.android.setupdesign.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.BuildCompatUtils;
import com.google.android.setupdesign.C3953R;

public class IntrinsicSizeFrameLayout extends FrameLayout {
    private int intrinsicHeight = 0;
    private int intrinsicWidth = 0;
    private Object lastInsets;
    private final Rect windowVisibleDisplayRect = new Rect();

    public IntrinsicSizeFrameLayout(Context context) {
        super(context);
        init(context, (AttributeSet) null, 0);
    }

    public IntrinsicSizeFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public IntrinsicSizeFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C3953R.styleable.SudIntrinsicSizeFrameLayout, i, 0);
            this.intrinsicHeight = obtainStyledAttributes.getDimensionPixelSize(C3953R.styleable.SudIntrinsicSizeFrameLayout_android_height, 0);
            this.intrinsicWidth = obtainStyledAttributes.getDimensionPixelSize(C3953R.styleable.SudIntrinsicSizeFrameLayout_android_width, 0);
            obtainStyledAttributes.recycle();
            if (BuildCompatUtils.isAtLeastS()) {
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CARD_VIEW_INTRINSIC_HEIGHT)) {
                    this.intrinsicHeight = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CARD_VIEW_INTRINSIC_HEIGHT);
                }
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CARD_VIEW_INTRINSIC_WIDTH)) {
                    this.intrinsicWidth = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CARD_VIEW_INTRINSIC_WIDTH);
                }
            }
        }
    }

    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (BuildCompatUtils.isAtLeastS() && this.intrinsicHeight == 0 && this.intrinsicWidth == 0) {
            layoutParams.width = -1;
            layoutParams.height = -1;
        }
        super.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        if (isWindowSizeSmallerThanDisplaySize()) {
            getWindowVisibleDisplayFrame(this.windowVisibleDisplayRect);
            i3 = View.MeasureSpec.makeMeasureSpec(this.windowVisibleDisplayRect.width(), 1073741824);
        } else {
            i3 = getIntrinsicMeasureSpec(i, this.intrinsicWidth);
        }
        super.onMeasure(i3, getIntrinsicMeasureSpec(i2, this.intrinsicHeight));
    }

    /* access modifiers changed from: package-private */
    public boolean isWindowSizeSmallerThanDisplaySize() {
        this.windowVisibleDisplayRect.set(((WindowManager) getContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds());
        Display display = getDisplay();
        if (display != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getRealMetrics(displayMetrics);
            return this.windowVisibleDisplayRect.width() > 0 && this.windowVisibleDisplayRect.width() < displayMetrics.widthPixels;
        }
    }

    private int getIntrinsicMeasureSpec(int i, int i2) {
        if (i2 <= 0) {
            return i;
        }
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 0) {
            return View.MeasureSpec.makeMeasureSpec(this.intrinsicHeight, 1073741824);
        }
        return mode == Integer.MIN_VALUE ? View.MeasureSpec.makeMeasureSpec(Math.min(size, this.intrinsicHeight), 1073741824) : i;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.lastInsets == null) {
            requestApplyInsets();
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.lastInsets = windowInsets;
        return super.onApplyWindowInsets(windowInsets);
    }
}
