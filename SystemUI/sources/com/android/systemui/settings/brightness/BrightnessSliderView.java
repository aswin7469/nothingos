package com.android.systemui.settings.brightness;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import com.android.settingslib.RestrictedLockUtils;
import com.android.systemui.C1893R;
import com.android.systemui.Gefingerpoken;

public class BrightnessSliderView extends FrameLayout {
    private DispatchTouchEventListener mListener;
    private Gefingerpoken mOnInterceptListener;
    private Drawable mProgressDrawable;
    private float mScale;
    private ToggleSeekBar mSlider;

    @FunctionalInterface
    interface DispatchTouchEventListener {
        boolean onDispatchTouchEvent(MotionEvent motionEvent);
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

    public BrightnessSliderView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BrightnessSliderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mScale = 1.0f;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        setLayerType(2, (Paint) null);
        ToggleSeekBar toggleSeekBar = (ToggleSeekBar) requireViewById(C1893R.C1897id.slider);
        this.mSlider = toggleSeekBar;
        toggleSeekBar.setAccessibilityLabel(getContentDescription().toString());
        try {
            this.mProgressDrawable = ((LayerDrawable) ((DrawableWrapper) ((LayerDrawable) this.mSlider.getProgressDrawable()).findDrawableByLayerId(16908301)).getDrawable()).findDrawableByLayerId(C1893R.C1897id.slider_foreground);
        } catch (Exception unused) {
        }
    }

    public void setOnDispatchTouchEventListener(DispatchTouchEventListener dispatchTouchEventListener) {
        this.mListener = dispatchTouchEventListener;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        DispatchTouchEventListener dispatchTouchEventListener = this.mListener;
        if (dispatchTouchEventListener != null) {
            dispatchTouchEventListener.onDispatchTouchEvent(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (this.mParent != null) {
            this.mParent.requestDisallowInterceptTouchEvent(z);
        }
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mSlider.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public void setEnforcedAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mSlider.setEnabled(enforcedAdmin == null);
        this.mSlider.setEnforcedAdmin(enforcedAdmin);
    }

    public void enableSlider(boolean z) {
        this.mSlider.setEnabled(z);
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

    public void setOnInterceptListener(Gefingerpoken gefingerpoken) {
        this.mOnInterceptListener = gefingerpoken;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.mOnInterceptListener;
        if (gefingerpoken != null) {
            return gefingerpoken.onInterceptTouchEvent(motionEvent);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
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
            int intrinsicHeight = (int) (((float) this.mProgressDrawable.getIntrinsicHeight()) * this.mScale);
            int intrinsicHeight2 = (this.mProgressDrawable.getIntrinsicHeight() - intrinsicHeight) / 2;
            this.mProgressDrawable.setBounds(bounds.left, intrinsicHeight2, bounds.right, intrinsicHeight + intrinsicHeight2);
        }
    }

    public float getSliderScaleY() {
        return this.mScale;
    }
}
