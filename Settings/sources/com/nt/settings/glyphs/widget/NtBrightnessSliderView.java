package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class NtBrightnessSliderView extends FrameLayout {
    private final int MAX_LEVEL;
    private ImageView mLevelView;
    private DispatchTouchEventListener mListener;
    private Drawable mProgressDrawable;
    private float mScale;
    private SeekBar mSlider;

    @FunctionalInterface
    /* loaded from: classes2.dex */
    interface DispatchTouchEventListener {
        boolean onDispatchTouchEvent(MotionEvent motionEvent);
    }

    public NtBrightnessSliderView(Context context) {
        this(context, null);
    }

    public NtBrightnessSliderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mScale = 1.0f;
        this.MAX_LEVEL = 4095;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        SeekBar seekBar = (SeekBar) requireViewById(R.id.slider);
        this.mSlider = seekBar;
        try {
            this.mProgressDrawable = ((LayerDrawable) ((DrawableWrapper) ((LayerDrawable) seekBar.getProgressDrawable()).findDrawableByLayerId(16908301)).getDrawable()).findDrawableByLayerId(R.id.slider_foreground);
        } catch (Exception unused) {
        }
        ntInit();
    }

    public void setOnDispatchTouchEventListener(DispatchTouchEventListener dispatchTouchEventListener) {
        this.mListener = dispatchTouchEventListener;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        DispatchTouchEventListener dispatchTouchEventListener = this.mListener;
        if (dispatchTouchEventListener != null) {
            dispatchTouchEventListener.onDispatchTouchEvent(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        ViewParent viewParent = ((FrameLayout) this).mParent;
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(z);
        }
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mSlider.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public int getMax() {
        return this.mSlider.getMax();
    }

    public void setMax(int i) {
        this.mSlider.setMax(i);
    }

    public void setValue(int i) {
        this.mSlider.setProgress(i);
    }

    public int getValue() {
        return this.mSlider.getProgress();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        applySliderScale();
    }

    public void setSliderScaleY(float f) {
        if (f != this.mScale) {
            this.mScale = f;
            applySliderScale();
        }
    }

    private void applySliderScale() {
        Drawable drawable = this.mProgressDrawable;
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            int intrinsicHeight = (int) (this.mProgressDrawable.getIntrinsicHeight() * this.mScale);
            int intrinsicHeight2 = (this.mProgressDrawable.getIntrinsicHeight() - intrinsicHeight) / 2;
            this.mProgressDrawable.setBounds(bounds.left, intrinsicHeight2, bounds.right, intrinsicHeight + intrinsicHeight2);
        }
    }

    public float getSliderScaleY() {
        return this.mScale;
    }

    private void ntInit() {
        this.mLevelView = (ImageView) findViewById(R.id.level);
    }

    public void setLevel(int i) {
        this.mLevelView.getDrawable().setLevel((i * 4095) / this.mSlider.getMax());
    }
}
