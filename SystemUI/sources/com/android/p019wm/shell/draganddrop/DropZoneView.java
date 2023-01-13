package com.android.p019wm.shell.draganddrop;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.animation.Interpolators;

/* renamed from: com.android.wm.shell.draganddrop.DropZoneView */
public class DropZoneView extends FrameLayout {
    private static final float HIGHLIGHT_ALPHA = 1.0f;
    private static final FloatProperty<DropZoneView> INSETS = new FloatProperty<DropZoneView>("insets") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(DropZoneView dropZoneView, float f) {
            dropZoneView.setMarginPercent(f);
        }

        public Float get(DropZoneView dropZoneView) {
            return Float.valueOf(dropZoneView.getMarginPercent());
        }
    };
    private static final int MARGIN_ANIMATION_ENTER_DURATION = 400;
    private static final int MARGIN_ANIMATION_EXIT_DURATION = 250;
    private static final float SPLASHSCREEN_ALPHA = 0.9f;
    private ObjectAnimator mBackgroundAnimator;
    /* access modifiers changed from: private */
    public float mBottomInset;
    private ColorDrawable mColorDrawable;
    /* access modifiers changed from: private */
    public final float[] mContainerMargin;
    /* access modifiers changed from: private */
    public float mCornerRadius;
    private int mHighlightColor;
    /* access modifiers changed from: private */
    public boolean mIgnoreBottomMargin;
    private ObjectAnimator mMarginAnimator;
    /* access modifiers changed from: private */
    public int mMarginColor;
    /* access modifiers changed from: private */
    public float mMarginPercent;
    private MarginView mMarginView;
    /* access modifiers changed from: private */
    public final Path mPath;
    private boolean mShowingHighlight;
    private boolean mShowingMargin;
    private boolean mShowingSplash;
    private int mSplashScreenColor;
    private ImageView mSplashScreenView;

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

    public DropZoneView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DropZoneView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DropZoneView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DropZoneView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mPath = new Path();
        this.mContainerMargin = new float[4];
        setContainerMargin(0.0f, 0.0f, 0.0f, 0.0f);
        this.mCornerRadius = ScreenDecorationsUtils.getWindowCornerRadius(context);
        this.mMarginColor = getResources().getColor(C3353R.C3354color.taskbar_background);
        int color = getResources().getColor(17170494);
        this.mHighlightColor = Color.argb(1.0f, (float) Color.red(color), (float) Color.green(color), (float) Color.blue(color));
        this.mSplashScreenColor = Color.argb(0.9f, 0.0f, 0.0f, 0.0f);
        ColorDrawable colorDrawable = new ColorDrawable();
        this.mColorDrawable = colorDrawable;
        setBackgroundDrawable(colorDrawable);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C3353R.dimen.split_icon_size);
        ImageView imageView = new ImageView(context);
        this.mSplashScreenView = imageView;
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(this.mSplashScreenView, new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize, 17));
        this.mSplashScreenView.setAlpha(0.0f);
        MarginView marginView = new MarginView(context);
        this.mMarginView = marginView;
        addView(marginView, new FrameLayout.LayoutParams(-1, -1));
    }

    public void onThemeChange() {
        this.mCornerRadius = ScreenDecorationsUtils.getWindowCornerRadius(getContext());
        this.mMarginColor = getResources().getColor(C3353R.C3354color.taskbar_background);
        this.mHighlightColor = getResources().getColor(17170494);
        if (this.mMarginPercent > 0.0f) {
            this.mMarginView.invalidate();
        }
    }

    public void setContainerMargin(float f, float f2, float f3, float f4) {
        float[] fArr = this.mContainerMargin;
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = f3;
        fArr[3] = f4;
        if (this.mMarginPercent > 0.0f) {
            this.mMarginView.invalidate();
        }
    }

    public void setForceIgnoreBottomMargin(boolean z) {
        this.mIgnoreBottomMargin = z;
        if (this.mMarginPercent > 0.0f) {
            this.mMarginView.invalidate();
        }
    }

    public void setBottomInset(float f) {
        this.mBottomInset = f;
        ((FrameLayout.LayoutParams) this.mSplashScreenView.getLayoutParams()).bottomMargin = (int) f;
        if (this.mMarginPercent > 0.0f) {
            this.mMarginView.invalidate();
        }
    }

    public void setAppInfo(int i, Drawable drawable) {
        Color valueOf = Color.valueOf(i);
        this.mSplashScreenColor = Color.argb(0.9f, valueOf.red(), valueOf.green(), valueOf.blue());
        this.mSplashScreenView.setImageDrawable(drawable);
    }

    public Animator getAnimator() {
        ObjectAnimator objectAnimator = this.mMarginAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            return this.mMarginAnimator;
        }
        ObjectAnimator objectAnimator2 = this.mBackgroundAnimator;
        if (objectAnimator2 == null || !objectAnimator2.isRunning()) {
            return null;
        }
        return this.mBackgroundAnimator;
    }

    public void animateSwitch() {
        boolean z = !this.mShowingHighlight;
        this.mShowingHighlight = z;
        this.mShowingSplash = !z;
        animateBackground(this.mColorDrawable.getColor(), z ? this.mHighlightColor : this.mSplashScreenColor);
        animateSplashScreenIcon();
    }

    public void setShowingHighlight(boolean z) {
        this.mShowingHighlight = z;
        this.mShowingSplash = !z;
        animateBackground(0, z ? this.mHighlightColor : this.mSplashScreenColor);
        animateSplashScreenIcon();
    }

    public void setShowingMargin(boolean z) {
        if (this.mShowingMargin != z) {
            this.mShowingMargin = z;
            animateMarginToState();
        }
        if (!this.mShowingMargin) {
            this.mShowingHighlight = false;
            this.mShowingSplash = false;
            animateBackground(this.mColorDrawable.getColor(), 0);
            animateSplashScreenIcon();
        }
    }

    private void animateBackground(int i, int i2) {
        ObjectAnimator objectAnimator = this.mBackgroundAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        ObjectAnimator ofArgb = ObjectAnimator.ofArgb(this.mColorDrawable, "color", new int[]{i, i2});
        this.mBackgroundAnimator = ofArgb;
        if (!this.mShowingSplash && !this.mShowingHighlight) {
            ofArgb.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        }
        this.mBackgroundAnimator.start();
    }

    private void animateSplashScreenIcon() {
        this.mSplashScreenView.animate().alpha(this.mShowingSplash ? 1.0f : 0.0f).start();
    }

    private void animateMarginToState() {
        ObjectAnimator objectAnimator = this.mMarginAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        FloatProperty<DropZoneView> floatProperty = INSETS;
        float[] fArr = new float[2];
        fArr[0] = this.mMarginPercent;
        fArr[1] = this.mShowingMargin ? 1.0f : 0.0f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, floatProperty, fArr);
        this.mMarginAnimator = ofFloat;
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mMarginAnimator.setDuration(this.mShowingMargin ? 400 : 250);
        this.mMarginAnimator.start();
    }

    /* access modifiers changed from: private */
    public void setMarginPercent(float f) {
        if (f != this.mMarginPercent) {
            this.mMarginPercent = f;
            this.mMarginView.invalidate();
        }
    }

    /* access modifiers changed from: private */
    public float getMarginPercent() {
        return this.mMarginPercent;
    }

    /* renamed from: com.android.wm.shell.draganddrop.DropZoneView$MarginView */
    private class MarginView extends View {
        MarginView(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            DropZoneView.this.mPath.reset();
            DropZoneView.this.mPath.addRoundRect(DropZoneView.this.mMarginPercent * DropZoneView.this.mContainerMargin[0], DropZoneView.this.mMarginPercent * DropZoneView.this.mContainerMargin[1], ((float) getWidth()) - (DropZoneView.this.mContainerMargin[2] * DropZoneView.this.mMarginPercent), (((float) getHeight()) - (DropZoneView.this.mContainerMargin[3] * DropZoneView.this.mMarginPercent)) - (DropZoneView.this.mIgnoreBottomMargin ? 0.0f : DropZoneView.this.mBottomInset), DropZoneView.this.mMarginPercent * DropZoneView.this.mCornerRadius, DropZoneView.this.mMarginPercent * DropZoneView.this.mCornerRadius, Path.Direction.CW);
            DropZoneView.this.mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            canvas.clipPath(DropZoneView.this.mPath);
            canvas.drawColor(DropZoneView.this.mMarginColor);
        }
    }
}
