package com.android.systemui.p012qs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import java.util.ArrayList;

/* renamed from: com.android.systemui.qs.PageIndicator */
public class PageIndicator extends ViewGroup {
    private static final long ANIMATION_DURATION = 250;
    private static final boolean DEBUG = false;
    private static final float MINOR_ALPHA = 0.42f;
    private static final String TAG = "PageIndicator";
    /* access modifiers changed from: private */
    public boolean mAnimating;
    /* access modifiers changed from: private */
    public final Animatable2.AnimationCallback mAnimationCallback = new Animatable2.AnimationCallback() {
        public void onAnimationEnd(Drawable drawable) {
            super.onAnimationEnd(drawable);
            if (drawable instanceof AnimatedVectorDrawable) {
                ((AnimatedVectorDrawable) drawable).unregisterAnimationCallback(PageIndicator.this.mAnimationCallback);
            }
            boolean unused = PageIndicator.this.mAnimating = false;
            if (PageIndicator.this.mQueuedPositions.size() != 0) {
                PageIndicator pageIndicator = PageIndicator.this;
                pageIndicator.setPosition(((Integer) pageIndicator.mQueuedPositions.remove(0)).intValue());
            }
        }
    };
    private final int mPageDotWidth;
    private final int mPageIndicatorHeight;
    private final int mPageIndicatorWidth;
    private int mPosition = -1;
    /* access modifiers changed from: private */
    public final ArrayList<Integer> mQueuedPositions = new ArrayList<>();
    private ColorStateList mTint;

    private float getAlpha(boolean z) {
        if (z) {
            return 1.0f;
        }
        return MINOR_ALPHA;
    }

    private int getTransition(boolean z, boolean z2, boolean z3) {
        return z3 ? z ? z2 ? C1894R.C1896drawable.major_b_a_animation : C1894R.C1896drawable.major_b_c_animation : z2 ? C1894R.C1896drawable.major_a_b_animation : C1894R.C1896drawable.major_c_b_animation : z ? z2 ? C1894R.C1896drawable.minor_b_c_animation : C1894R.C1896drawable.minor_b_a_animation : z2 ? C1894R.C1896drawable.minor_c_b_animation : C1894R.C1896drawable.minor_a_b_animation;
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public PageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{16843041});
        if (obtainStyledAttributes.hasValue(0)) {
            this.mTint = obtainStyledAttributes.getColorStateList(0);
        } else {
            this.mTint = Utils.getColorAccent(context);
        }
        obtainStyledAttributes.recycle();
        Resources resources = context.getResources();
        this.mPageIndicatorWidth = resources.getDimensionPixelSize(C1894R.dimen.qs_page_indicator_width);
        this.mPageIndicatorHeight = resources.getDimensionPixelSize(C1894R.dimen.qs_page_indicator_height);
        this.mPageDotWidth = resources.getDimensionPixelSize(C1894R.dimen.qs_page_indicator_dot_width);
    }

    public void setNumPages(int i) {
        setVisibility(i > 1 ? 0 : 8);
        if (i != getChildCount()) {
            if (this.mAnimating) {
                Log.w(TAG, "setNumPages during animation");
            }
            while (i < getChildCount()) {
                removeViewAt(getChildCount() - 1);
            }
            while (i > getChildCount()) {
                ImageView imageView = new ImageView(this.mContext);
                imageView.setImageResource(C1894R.C1896drawable.minor_a_b);
                imageView.setImageTintList(this.mTint);
                addView(imageView, new ViewGroup.LayoutParams(this.mPageIndicatorWidth, this.mPageIndicatorHeight));
            }
            setIndex(this.mPosition >> 1);
            requestLayout();
        }
    }

    public ColorStateList getTintList() {
        return this.mTint;
    }

    public void setTintList(ColorStateList colorStateList) {
        if (!colorStateList.equals(this.mTint)) {
            this.mTint = colorStateList;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (childAt instanceof ImageView) {
                    ((ImageView) childAt).setImageTintList(this.mTint);
                }
            }
        }
    }

    public void setLocation(float f) {
        int i = (int) f;
        int i2 = 0;
        setContentDescription(getContext().getString(C1894R.string.accessibility_quick_settings_page, new Object[]{Integer.valueOf(i + 1), Integer.valueOf(getChildCount())}));
        int i3 = i << 1;
        if (f != ((float) i)) {
            i2 = 1;
        }
        int i4 = i3 | i2;
        int i5 = this.mPosition;
        if (this.mQueuedPositions.size() != 0) {
            ArrayList<Integer> arrayList = this.mQueuedPositions;
            i5 = arrayList.get(arrayList.size() - 1).intValue();
        }
        if (i4 != i5) {
            if (this.mAnimating) {
                this.mQueuedPositions.add(Integer.valueOf(i4));
            } else {
                setPosition(i4);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setPosition(int i) {
        if (!isVisibleToUser() || Math.abs(this.mPosition - i) != 1) {
            setIndex(i >> 1);
        } else {
            animate(this.mPosition, i);
        }
        this.mPosition = i;
    }

    private void setIndex(int i) {
        int childCount = getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            ImageView imageView = (ImageView) getChildAt(i2);
            imageView.setTranslationX(0.0f);
            imageView.setImageResource(C1894R.C1896drawable.major_a_b);
            imageView.setAlpha(getAlpha(i2 == i));
            i2++;
        }
    }

    private void animate(int i, int i2) {
        int i3 = i >> 1;
        int i4 = i2 >> 1;
        setIndex(i3);
        boolean z = (i & 1) != 0;
        boolean z2 = !z ? i < i2 : i > i2;
        int min = Math.min(i3, i4);
        int max = Math.max(i3, i4);
        if (max == min) {
            max++;
        }
        ImageView imageView = (ImageView) getChildAt(min);
        ImageView imageView2 = (ImageView) getChildAt(max);
        if (imageView != null && imageView2 != null) {
            imageView2.setTranslationX(imageView.getX() - imageView2.getX());
            playAnimation(imageView, getTransition(z, z2, false));
            imageView.setAlpha(getAlpha(false));
            playAnimation(imageView2, getTransition(z, z2, true));
            imageView2.setAlpha(getAlpha(true));
            this.mAnimating = true;
        }
    }

    private void playAnimation(ImageView imageView, int i) {
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) getContext().getDrawable(i);
        imageView.setImageDrawable(animatedVectorDrawable);
        animatedVectorDrawable.forceAnimationOnUI();
        animatedVectorDrawable.registerAnimationCallback(this.mAnimationCallback);
        animatedVectorDrawable.start();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int childCount = getChildCount();
        if (childCount == 0) {
            super.onMeasure(i, i2);
            return;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mPageIndicatorWidth, 1073741824);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(this.mPageIndicatorHeight, 1073741824);
        for (int i3 = 0; i3 < childCount; i3++) {
            getChildAt(i3).measure(makeMeasureSpec, makeMeasureSpec2);
        }
        int i4 = this.mPageIndicatorWidth;
        int i5 = this.mPageDotWidth;
        setMeasuredDimension(((i4 - i5) * (childCount - 1)) + i5, this.mPageIndicatorHeight);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        if (childCount != 0) {
            for (int i5 = 0; i5 < childCount; i5++) {
                int i6 = (this.mPageIndicatorWidth - this.mPageDotWidth) * i5;
                getChildAt(i5).layout(i6, 0, this.mPageIndicatorWidth + i6, this.mPageIndicatorHeight);
            }
        }
    }
}
