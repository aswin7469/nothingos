package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import androidx.leanback.R$attr;
import androidx.leanback.R$integer;
import androidx.leanback.R$styleable;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class BaseCardView extends FrameLayout {
    private static final int[] LB_PRESSED_STATE_SET = {16842919};
    private final int mActivatedAnimDuration;
    private Animation mAnim;
    private final Runnable mAnimationTrigger;
    private int mCardType;
    private boolean mDelaySelectedAnim;
    ArrayList<View> mExtraViewList;
    private int mExtraVisibility;
    float mInfoAlpha;
    float mInfoOffset;
    ArrayList<View> mInfoViewList;
    float mInfoVisFraction;
    private int mInfoVisibility;
    private ArrayList<View> mMainViewList;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private final int mSelectedAnimDuration;
    private int mSelectedAnimationDelay;

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public BaseCardView(Context context, AttributeSet attrs) {
        this(context, attrs, R$attr.baseCardViewStyle);
    }

    @SuppressLint({"CustomViewStyleable"})
    public BaseCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mAnimationTrigger = new Runnable() { // from class: androidx.leanback.widget.BaseCardView.1
            @Override // java.lang.Runnable
            public void run() {
                BaseCardView.this.animateInfoOffset(true);
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R$styleable.lbBaseCardView, defStyleAttr, 0);
        try {
            this.mCardType = obtainStyledAttributes.getInteger(R$styleable.lbBaseCardView_cardType, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.lbBaseCardView_cardForeground);
            if (drawable != null) {
                setForeground(drawable);
            }
            Drawable drawable2 = obtainStyledAttributes.getDrawable(R$styleable.lbBaseCardView_cardBackground);
            if (drawable2 != null) {
                setBackground(drawable2);
            }
            this.mInfoVisibility = obtainStyledAttributes.getInteger(R$styleable.lbBaseCardView_infoVisibility, 1);
            int integer = obtainStyledAttributes.getInteger(R$styleable.lbBaseCardView_extraVisibility, 2);
            this.mExtraVisibility = integer;
            int i = this.mInfoVisibility;
            if (integer < i) {
                this.mExtraVisibility = i;
            }
            this.mSelectedAnimationDelay = obtainStyledAttributes.getInteger(R$styleable.lbBaseCardView_selectedAnimationDelay, getResources().getInteger(R$integer.lb_card_selected_animation_delay));
            this.mSelectedAnimDuration = obtainStyledAttributes.getInteger(R$styleable.lbBaseCardView_selectedAnimationDuration, getResources().getInteger(R$integer.lb_card_selected_animation_duration));
            this.mActivatedAnimDuration = obtainStyledAttributes.getInteger(R$styleable.lbBaseCardView_activatedAnimationDuration, getResources().getInteger(R$integer.lb_card_activated_animation_duration));
            obtainStyledAttributes.recycle();
            this.mDelaySelectedAnim = true;
            this.mMainViewList = new ArrayList<>();
            this.mInfoViewList = new ArrayList<>();
            this.mExtraViewList = new ArrayList<>();
            this.mInfoOffset = 0.0f;
            this.mInfoVisFraction = getFinalInfoVisFraction();
            this.mInfoAlpha = getFinalInfoAlpha();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    final float getFinalInfoVisFraction() {
        return (this.mCardType == 2 && this.mInfoVisibility == 2 && !isSelected()) ? 0.0f : 1.0f;
    }

    final float getFinalInfoAlpha() {
        return (this.mCardType == 1 && this.mInfoVisibility == 2 && !isSelected()) ? 0.0f : 1.0f;
    }

    @Override // android.view.View
    public void setActivated(boolean activated) {
        if (activated != isActivated()) {
            super.setActivated(activated);
            applyActiveState();
        }
    }

    @Override // android.view.View
    public void setSelected(boolean selected) {
        if (selected != isSelected()) {
            super.setSelected(selected);
            applySelectedState(isSelected());
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int i2;
        boolean z = false;
        this.mMeasuredWidth = 0;
        this.mMeasuredHeight = 0;
        findChildrenViews();
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < this.mMainViewList.size(); i5++) {
            View view = this.mMainViewList.get(i5);
            if (view.getVisibility() != 8) {
                measureChild(view, makeMeasureSpec, makeMeasureSpec);
                this.mMeasuredWidth = Math.max(this.mMeasuredWidth, view.getMeasuredWidth());
                i3 += view.getMeasuredHeight();
                i4 = View.combineMeasuredStates(i4, view.getMeasuredState());
            }
        }
        setPivotX(this.mMeasuredWidth / 2);
        setPivotY(i3 / 2);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(this.mMeasuredWidth, 1073741824);
        if (hasInfoRegion()) {
            i = 0;
            for (int i6 = 0; i6 < this.mInfoViewList.size(); i6++) {
                View view2 = this.mInfoViewList.get(i6);
                if (view2.getVisibility() != 8) {
                    measureChild(view2, makeMeasureSpec2, makeMeasureSpec);
                    if (this.mCardType != 1) {
                        i += view2.getMeasuredHeight();
                    }
                    i4 = View.combineMeasuredStates(i4, view2.getMeasuredState());
                }
            }
            if (hasExtraRegion()) {
                i2 = 0;
                for (int i7 = 0; i7 < this.mExtraViewList.size(); i7++) {
                    View view3 = this.mExtraViewList.get(i7);
                    if (view3.getVisibility() != 8) {
                        measureChild(view3, makeMeasureSpec2, makeMeasureSpec);
                        i2 += view3.getMeasuredHeight();
                        i4 = View.combineMeasuredStates(i4, view3.getMeasuredState());
                    }
                }
            } else {
                i2 = 0;
            }
        } else {
            i = 0;
            i2 = 0;
        }
        if (hasInfoRegion() && this.mInfoVisibility == 2) {
            z = true;
        }
        float f = i3;
        float f2 = i;
        if (z) {
            f2 *= this.mInfoVisFraction;
        }
        this.mMeasuredHeight = (int) (((f + f2) + i2) - (z ? 0.0f : this.mInfoOffset));
        setMeasuredDimension(View.resolveSizeAndState(this.mMeasuredWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec, i4), View.resolveSizeAndState(this.mMeasuredHeight + getPaddingTop() + getPaddingBottom(), heightMeasureSpec, i4 << 16));
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float paddingTop = getPaddingTop();
        for (int i = 0; i < this.mMainViewList.size(); i++) {
            View view = this.mMainViewList.get(i);
            if (view.getVisibility() != 8) {
                view.layout(getPaddingLeft(), (int) paddingTop, this.mMeasuredWidth + getPaddingLeft(), (int) (view.getMeasuredHeight() + paddingTop));
                paddingTop += view.getMeasuredHeight();
            }
        }
        if (hasInfoRegion()) {
            float f = 0.0f;
            for (int i2 = 0; i2 < this.mInfoViewList.size(); i2++) {
                f += this.mInfoViewList.get(i2).getMeasuredHeight();
            }
            int i3 = this.mCardType;
            if (i3 == 1) {
                paddingTop -= f;
                if (paddingTop < 0.0f) {
                    paddingTop = 0.0f;
                }
            } else if (i3 == 2) {
                if (this.mInfoVisibility == 2) {
                    f *= this.mInfoVisFraction;
                }
            } else {
                paddingTop -= this.mInfoOffset;
            }
            for (int i4 = 0; i4 < this.mInfoViewList.size(); i4++) {
                View view2 = this.mInfoViewList.get(i4);
                if (view2.getVisibility() != 8) {
                    int measuredHeight = view2.getMeasuredHeight();
                    if (measuredHeight > f) {
                        measuredHeight = (int) f;
                    }
                    float f2 = measuredHeight;
                    paddingTop += f2;
                    view2.layout(getPaddingLeft(), (int) paddingTop, this.mMeasuredWidth + getPaddingLeft(), (int) paddingTop);
                    f -= f2;
                    if (f <= 0.0f) {
                        break;
                    }
                }
            }
            if (hasExtraRegion()) {
                for (int i5 = 0; i5 < this.mExtraViewList.size(); i5++) {
                    View view3 = this.mExtraViewList.get(i5);
                    if (view3.getVisibility() != 8) {
                        view3.layout(getPaddingLeft(), (int) paddingTop, this.mMeasuredWidth + getPaddingLeft(), (int) (view3.getMeasuredHeight() + paddingTop));
                        paddingTop += view3.getMeasuredHeight();
                    }
                }
            }
        }
        onSizeChanged(0, 0, right - left, bottom - top);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mAnimationTrigger);
        cancelAnimations();
    }

    private boolean hasInfoRegion() {
        return this.mCardType != 0;
    }

    private boolean hasExtraRegion() {
        return this.mCardType == 3;
    }

    private boolean isRegionVisible(int regionVisibility) {
        if (regionVisibility != 0) {
            if (regionVisibility == 1) {
                return isActivated();
            }
            if (regionVisibility == 2) {
                return isSelected();
            }
            return false;
        }
        return true;
    }

    private boolean isCurrentRegionVisible(int regionVisibility) {
        if (regionVisibility != 0) {
            if (regionVisibility == 1) {
                return isActivated();
            }
            if (regionVisibility != 2) {
                return false;
            }
            if (this.mCardType != 2) {
                return isSelected();
            }
            return this.mInfoVisFraction > 0.0f;
        }
        return true;
    }

    private void findChildrenViews() {
        this.mMainViewList.clear();
        this.mInfoViewList.clear();
        this.mExtraViewList.clear();
        int childCount = getChildCount();
        boolean z = hasInfoRegion() && isCurrentRegionVisible(this.mInfoVisibility);
        boolean z2 = hasExtraRegion() && this.mInfoOffset > 0.0f;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null) {
                int i2 = ((LayoutParams) childAt.getLayoutParams()).viewType;
                int i3 = 8;
                if (i2 == 1) {
                    childAt.setAlpha(this.mInfoAlpha);
                    this.mInfoViewList.add(childAt);
                    if (z) {
                        i3 = 0;
                    }
                    childAt.setVisibility(i3);
                } else if (i2 == 2) {
                    this.mExtraViewList.add(childAt);
                    if (z2) {
                        i3 = 0;
                    }
                    childAt.setVisibility(i3);
                } else {
                    this.mMainViewList.add(childAt);
                    childAt.setVisibility(0);
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] onCreateDrawableState = super.onCreateDrawableState(extraSpace);
        int length = onCreateDrawableState.length;
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < length; i++) {
            if (onCreateDrawableState[i] == 16842919) {
                z = true;
            }
            if (onCreateDrawableState[i] == 16842910) {
                z2 = true;
            }
        }
        if (!z || !z2) {
            if (z) {
                return LB_PRESSED_STATE_SET;
            }
            if (z2) {
                return View.ENABLED_STATE_SET;
            }
            return View.EMPTY_STATE_SET;
        }
        return View.PRESSED_ENABLED_STATE_SET;
    }

    private void applyActiveState() {
        int i;
        if (!hasInfoRegion() || (i = this.mInfoVisibility) != 1) {
            return;
        }
        setInfoViewVisibility(isRegionVisible(i));
    }

    private void setInfoViewVisibility(boolean visible) {
        int i = this.mCardType;
        if (i != 3) {
            if (i == 2) {
                if (this.mInfoVisibility == 2) {
                    animateInfoHeight(visible);
                } else {
                    for (int i2 = 0; i2 < this.mInfoViewList.size(); i2++) {
                        this.mInfoViewList.get(i2).setVisibility(visible ? 0 : 8);
                    }
                }
            } else {
                if (i != 1) {
                    return;
                }
                animateInfoAlpha(visible);
            }
        } else if (visible) {
            for (int i3 = 0; i3 < this.mInfoViewList.size(); i3++) {
                this.mInfoViewList.get(i3).setVisibility(0);
            }
        } else {
            for (int i4 = 0; i4 < this.mInfoViewList.size(); i4++) {
                this.mInfoViewList.get(i4).setVisibility(8);
            }
            for (int i5 = 0; i5 < this.mExtraViewList.size(); i5++) {
                this.mExtraViewList.get(i5).setVisibility(8);
            }
            this.mInfoOffset = 0.0f;
        }
    }

    private void applySelectedState(boolean focused) {
        removeCallbacks(this.mAnimationTrigger);
        if (this.mCardType != 3) {
            if (this.mInfoVisibility != 2) {
                return;
            }
            setInfoViewVisibility(focused);
        } else if (focused) {
            if (!this.mDelaySelectedAnim) {
                post(this.mAnimationTrigger);
                this.mDelaySelectedAnim = true;
                return;
            }
            postDelayed(this.mAnimationTrigger, this.mSelectedAnimationDelay);
        } else {
            animateInfoOffset(false);
        }
    }

    void cancelAnimations() {
        Animation animation = this.mAnim;
        if (animation != null) {
            animation.cancel();
            this.mAnim = null;
            clearAnimation();
        }
    }

    void animateInfoOffset(boolean shown) {
        cancelAnimations();
        int i = 0;
        if (shown) {
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mMeasuredWidth, 1073741824);
            int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
            int i2 = 0;
            for (int i3 = 0; i3 < this.mExtraViewList.size(); i3++) {
                View view = this.mExtraViewList.get(i3);
                view.setVisibility(0);
                view.measure(makeMeasureSpec, makeMeasureSpec2);
                i2 = Math.max(i2, view.getMeasuredHeight());
            }
            i = i2;
        }
        InfoOffsetAnimation infoOffsetAnimation = new InfoOffsetAnimation(this.mInfoOffset, shown ? i : 0.0f);
        this.mAnim = infoOffsetAnimation;
        infoOffsetAnimation.setDuration(this.mSelectedAnimDuration);
        this.mAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mAnim.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.leanback.widget.BaseCardView.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (BaseCardView.this.mInfoOffset == 0.0f) {
                    for (int i4 = 0; i4 < BaseCardView.this.mExtraViewList.size(); i4++) {
                        BaseCardView.this.mExtraViewList.get(i4).setVisibility(8);
                    }
                }
            }
        });
        startAnimation(this.mAnim);
    }

    private void animateInfoHeight(boolean shown) {
        cancelAnimations();
        if (shown) {
            for (int i = 0; i < this.mInfoViewList.size(); i++) {
                this.mInfoViewList.get(i).setVisibility(0);
            }
        }
        float f = shown ? 1.0f : 0.0f;
        if (this.mInfoVisFraction == f) {
            return;
        }
        InfoHeightAnimation infoHeightAnimation = new InfoHeightAnimation(this.mInfoVisFraction, f);
        this.mAnim = infoHeightAnimation;
        infoHeightAnimation.setDuration(this.mSelectedAnimDuration);
        this.mAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mAnim.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.leanback.widget.BaseCardView.3
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (BaseCardView.this.mInfoVisFraction == 0.0f) {
                    for (int i2 = 0; i2 < BaseCardView.this.mInfoViewList.size(); i2++) {
                        BaseCardView.this.mInfoViewList.get(i2).setVisibility(8);
                    }
                }
            }
        });
        startAnimation(this.mAnim);
    }

    private void animateInfoAlpha(boolean shown) {
        cancelAnimations();
        if (shown) {
            for (int i = 0; i < this.mInfoViewList.size(); i++) {
                this.mInfoViewList.get(i).setVisibility(0);
            }
        }
        float f = 1.0f;
        if ((shown ? 1.0f : 0.0f) == this.mInfoAlpha) {
            return;
        }
        float f2 = this.mInfoAlpha;
        if (!shown) {
            f = 0.0f;
        }
        InfoAlphaAnimation infoAlphaAnimation = new InfoAlphaAnimation(f2, f);
        this.mAnim = infoAlphaAnimation;
        infoAlphaAnimation.setDuration(this.mActivatedAnimDuration);
        this.mAnim.setInterpolator(new DecelerateInterpolator());
        this.mAnim.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.leanback.widget.BaseCardView.4
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (BaseCardView.this.mInfoAlpha == 0.0d) {
                    for (int i2 = 0; i2 < BaseCardView.this.mInfoViewList.size(); i2++) {
                        BaseCardView.this.mInfoViewList.get(i2).setVisibility(8);
                    }
                }
            }
        });
        startAnimation(this.mAnim);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: generateLayoutParams  reason: collision with other method in class */
    public LayoutParams mo113generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: generateDefaultLayoutParams  reason: collision with other method in class */
    public LayoutParams mo111generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) lp);
        }
        return new LayoutParams(lp);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends FrameLayout.LayoutParams {
        @ViewDebug.ExportedProperty(category = "layout", mapping = {@ViewDebug.IntToString(from = 0, to = "MAIN"), @ViewDebug.IntToString(from = 1, to = "INFO"), @ViewDebug.IntToString(from = 2, to = "EXTRA")})
        public int viewType;

        @SuppressLint({"CustomViewStyleable"})
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.viewType = 0;
            TypedArray obtainStyledAttributes = c.obtainStyledAttributes(attrs, R$styleable.lbBaseCardView_Layout);
            this.viewType = obtainStyledAttributes.getInt(R$styleable.lbBaseCardView_Layout_layout_viewType, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.viewType = 0;
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
            this.viewType = 0;
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.viewType = 0;
            this.viewType = source.viewType;
        }
    }

    /* loaded from: classes.dex */
    class AnimationBase extends Animation {
        AnimationBase() {
        }

        final void mockStart() {
            getTransformation(0L, null);
        }

        final void mockEnd() {
            applyTransformation(1.0f, null);
            BaseCardView.this.cancelAnimations();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class InfoOffsetAnimation extends AnimationBase {
        private float mDelta;
        private float mStartValue;

        public InfoOffsetAnimation(float start, float end) {
            super();
            this.mStartValue = start;
            this.mDelta = end - start;
        }

        @Override // android.view.animation.Animation
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            BaseCardView baseCardView = BaseCardView.this;
            baseCardView.mInfoOffset = this.mStartValue + (interpolatedTime * this.mDelta);
            baseCardView.requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class InfoHeightAnimation extends AnimationBase {
        private float mDelta;
        private float mStartValue;

        public InfoHeightAnimation(float start, float end) {
            super();
            this.mStartValue = start;
            this.mDelta = end - start;
        }

        @Override // android.view.animation.Animation
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            BaseCardView baseCardView = BaseCardView.this;
            baseCardView.mInfoVisFraction = this.mStartValue + (interpolatedTime * this.mDelta);
            baseCardView.requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class InfoAlphaAnimation extends AnimationBase {
        private float mDelta;
        private float mStartValue;

        public InfoAlphaAnimation(float start, float end) {
            super();
            this.mStartValue = start;
            this.mDelta = end - start;
        }

        @Override // android.view.animation.Animation
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            BaseCardView.this.mInfoAlpha = this.mStartValue + (interpolatedTime * this.mDelta);
            for (int i = 0; i < BaseCardView.this.mInfoViewList.size(); i++) {
                BaseCardView.this.mInfoViewList.get(i).setAlpha(BaseCardView.this.mInfoAlpha);
            }
        }
    }

    @Override // android.view.View
    public String toString() {
        return super.toString();
    }
}
