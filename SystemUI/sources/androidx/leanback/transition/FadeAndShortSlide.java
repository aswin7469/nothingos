package androidx.leanback.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import androidx.leanback.R$styleable;
/* loaded from: classes.dex */
public class FadeAndShortSlide extends Visibility {
    private float mDistance;
    private Visibility mFade;
    private CalculateSlide mSlideCalculator;
    final CalculateSlide sCalculateTopBottom;
    private static final TimeInterpolator sDecelerate = new DecelerateInterpolator();
    static final CalculateSlide sCalculateStart = new CalculateSlide() { // from class: androidx.leanback.transition.FadeAndShortSlide.1
        @Override // androidx.leanback.transition.FadeAndShortSlide.CalculateSlide
        public float getGoneX(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
            boolean z = true;
            if (sceneRoot.getLayoutDirection() != 1) {
                z = false;
            }
            if (z) {
                return view.getTranslationX() + t.getHorizontalDistance(sceneRoot);
            }
            return view.getTranslationX() - t.getHorizontalDistance(sceneRoot);
        }
    };
    static final CalculateSlide sCalculateEnd = new CalculateSlide() { // from class: androidx.leanback.transition.FadeAndShortSlide.2
        @Override // androidx.leanback.transition.FadeAndShortSlide.CalculateSlide
        public float getGoneX(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
            boolean z = true;
            if (sceneRoot.getLayoutDirection() != 1) {
                z = false;
            }
            if (z) {
                return view.getTranslationX() - t.getHorizontalDistance(sceneRoot);
            }
            return view.getTranslationX() + t.getHorizontalDistance(sceneRoot);
        }
    };
    static final CalculateSlide sCalculateStartEnd = new CalculateSlide() { // from class: androidx.leanback.transition.FadeAndShortSlide.3
        @Override // androidx.leanback.transition.FadeAndShortSlide.CalculateSlide
        public float getGoneX(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
            int centerX;
            int width = position[0] + (view.getWidth() / 2);
            sceneRoot.getLocationOnScreen(position);
            Rect epicenter = t.getEpicenter();
            if (epicenter == null) {
                centerX = position[0] + (sceneRoot.getWidth() / 2);
            } else {
                centerX = epicenter.centerX();
            }
            if (width < centerX) {
                return view.getTranslationX() - t.getHorizontalDistance(sceneRoot);
            }
            return view.getTranslationX() + t.getHorizontalDistance(sceneRoot);
        }
    };
    static final CalculateSlide sCalculateBottom = new CalculateSlide() { // from class: androidx.leanback.transition.FadeAndShortSlide.4
        @Override // androidx.leanback.transition.FadeAndShortSlide.CalculateSlide
        public float getGoneY(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
            return view.getTranslationY() + t.getVerticalDistance(sceneRoot);
        }
    };
    static final CalculateSlide sCalculateTop = new CalculateSlide() { // from class: androidx.leanback.transition.FadeAndShortSlide.5
        @Override // androidx.leanback.transition.FadeAndShortSlide.CalculateSlide
        public float getGoneY(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
            return view.getTranslationY() - t.getVerticalDistance(sceneRoot);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class CalculateSlide {
        CalculateSlide() {
        }

        float getGoneX(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
            return view.getTranslationX();
        }

        float getGoneY(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
            return view.getTranslationY();
        }
    }

    float getHorizontalDistance(ViewGroup sceneRoot) {
        float f = this.mDistance;
        return f >= 0.0f ? f : sceneRoot.getWidth() / 4;
    }

    float getVerticalDistance(ViewGroup sceneRoot) {
        float f = this.mDistance;
        return f >= 0.0f ? f : sceneRoot.getHeight() / 4;
    }

    public FadeAndShortSlide() {
        this(8388611);
    }

    public FadeAndShortSlide(int slideEdge) {
        this.mFade = new Fade();
        this.mDistance = -1.0f;
        this.sCalculateTopBottom = new CalculateSlide() { // from class: androidx.leanback.transition.FadeAndShortSlide.6
            @Override // androidx.leanback.transition.FadeAndShortSlide.CalculateSlide
            public float getGoneY(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
                int centerY;
                int height = position[1] + (view.getHeight() / 2);
                sceneRoot.getLocationOnScreen(position);
                Rect epicenter = FadeAndShortSlide.this.getEpicenter();
                if (epicenter == null) {
                    centerY = position[1] + (sceneRoot.getHeight() / 2);
                } else {
                    centerY = epicenter.centerY();
                }
                if (height < centerY) {
                    return view.getTranslationY() - t.getVerticalDistance(sceneRoot);
                }
                return view.getTranslationY() + t.getVerticalDistance(sceneRoot);
            }
        };
        setSlideEdge(slideEdge);
    }

    public FadeAndShortSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mFade = new Fade();
        this.mDistance = -1.0f;
        this.sCalculateTopBottom = new CalculateSlide() { // from class: androidx.leanback.transition.FadeAndShortSlide.6
            @Override // androidx.leanback.transition.FadeAndShortSlide.CalculateSlide
            public float getGoneY(FadeAndShortSlide t, ViewGroup sceneRoot, View view, int[] position) {
                int centerY;
                int height = position[1] + (view.getHeight() / 2);
                sceneRoot.getLocationOnScreen(position);
                Rect epicenter = FadeAndShortSlide.this.getEpicenter();
                if (epicenter == null) {
                    centerY = position[1] + (sceneRoot.getHeight() / 2);
                } else {
                    centerY = epicenter.centerY();
                }
                if (height < centerY) {
                    return view.getTranslationY() - t.getVerticalDistance(sceneRoot);
                }
                return view.getTranslationY() + t.getVerticalDistance(sceneRoot);
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R$styleable.lbSlide);
        setSlideEdge(obtainStyledAttributes.getInt(R$styleable.lbSlide_lb_slideEdge, 8388611));
        obtainStyledAttributes.recycle();
    }

    @Override // android.transition.Transition
    public void setEpicenterCallback(Transition.EpicenterCallback epicenterCallback) {
        this.mFade.setEpicenterCallback(epicenterCallback);
        super.setEpicenterCallback(epicenterCallback);
    }

    private void captureValues(TransitionValues transitionValues) {
        int[] iArr = new int[2];
        transitionValues.view.getLocationOnScreen(iArr);
        transitionValues.values.put("android:fadeAndShortSlideTransition:screenPosition", iArr);
    }

    @Override // android.transition.Visibility, android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        this.mFade.captureStartValues(transitionValues);
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    @Override // android.transition.Visibility, android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        this.mFade.captureEndValues(transitionValues);
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    public void setSlideEdge(int slideEdge) {
        if (slideEdge == 48) {
            this.mSlideCalculator = sCalculateTop;
        } else if (slideEdge == 80) {
            this.mSlideCalculator = sCalculateBottom;
        } else if (slideEdge == 112) {
            this.mSlideCalculator = this.sCalculateTopBottom;
        } else if (slideEdge == 8388611) {
            this.mSlideCalculator = sCalculateStart;
        } else if (slideEdge == 8388613) {
            this.mSlideCalculator = sCalculateEnd;
        } else if (slideEdge == 8388615) {
            this.mSlideCalculator = sCalculateStartEnd;
        } else {
            throw new IllegalArgumentException("Invalid slide direction");
        }
    }

    @Override // android.transition.Visibility
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        if (endValues == null || sceneRoot == view) {
            return null;
        }
        int[] iArr = (int[]) endValues.values.get("android:fadeAndShortSlideTransition:screenPosition");
        int i = iArr[0];
        int i2 = iArr[1];
        float translationX = view.getTranslationX();
        Animator createAnimation = TranslationAnimationCreator.createAnimation(view, endValues, i, i2, this.mSlideCalculator.getGoneX(this, sceneRoot, view, iArr), this.mSlideCalculator.getGoneY(this, sceneRoot, view, iArr), translationX, view.getTranslationY(), sDecelerate, this);
        Animator onAppear = this.mFade.onAppear(sceneRoot, view, startValues, endValues);
        if (createAnimation == null) {
            return onAppear;
        }
        if (onAppear == null) {
            return createAnimation;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(createAnimation).with(onAppear);
        return animatorSet;
    }

    @Override // android.transition.Visibility
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || sceneRoot == view) {
            return null;
        }
        int[] iArr = (int[]) startValues.values.get("android:fadeAndShortSlideTransition:screenPosition");
        Animator createAnimation = TranslationAnimationCreator.createAnimation(view, startValues, iArr[0], iArr[1], view.getTranslationX(), view.getTranslationY(), this.mSlideCalculator.getGoneX(this, sceneRoot, view, iArr), this.mSlideCalculator.getGoneY(this, sceneRoot, view, iArr), sDecelerate, this);
        Animator onDisappear = this.mFade.onDisappear(sceneRoot, view, startValues, endValues);
        if (createAnimation == null) {
            return onDisappear;
        }
        if (onDisappear == null) {
            return createAnimation;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(createAnimation).with(onDisappear);
        return animatorSet;
    }

    @Override // android.transition.Transition
    public Transition addListener(Transition.TransitionListener listener) {
        this.mFade.addListener(listener);
        return super.addListener(listener);
    }

    @Override // android.transition.Transition
    public Transition removeListener(Transition.TransitionListener listener) {
        this.mFade.removeListener(listener);
        return super.removeListener(listener);
    }

    @Override // android.transition.Transition
    /* renamed from: clone */
    public Transition mo110clone() {
        FadeAndShortSlide fadeAndShortSlide = (FadeAndShortSlide) super.clone();
        fadeAndShortSlide.mFade = (Visibility) this.mFade.clone();
        return fadeAndShortSlide;
    }
}
