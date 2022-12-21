package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.animation.AnimationUtil;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;

@Metadata(mo64986d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u001bH\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u001eH\u0002J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\fH\u0002J\b\u0010#\u001a\u00020\u001bH\u0016J\u0010\u0010$\u001a\u00020\u001b2\u0006\u0010%\u001a\u00020\u0017H\u0016J-\u0010&\u001a\u00020\u001e2%\u0010'\u001a!\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b)\u0012\b\b*\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u00020\u00140(j\u0002`+J\u0010\u0010,\u001a\u00020\u001e2\u0006\u0010-\u001a\u00020\fH\u0002J\u0018\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\fH\u0002J\u0010\u00101\u001a\u00020\u001e2\u0006\u00102\u001a\u00020\fH\u0002J\b\u00103\u001a\u00020\u001eH\u0002R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X.¢\u0006\u0002\n\u0000¨\u00064"}, mo64987d2 = {"Lcom/android/systemui/statusbar/events/SystemEventChipAnimationController;", "Lcom/android/systemui/statusbar/events/SystemStatusAnimationCallback;", "context", "Landroid/content/Context;", "statusBarWindowController", "Lcom/android/systemui/statusbar/window/StatusBarWindowController;", "contentInsetsProvider", "Lcom/android/systemui/statusbar/phone/StatusBarContentInsetsProvider;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/window/StatusBarWindowController;Lcom/android/systemui/statusbar/phone/StatusBarContentInsetsProvider;)V", "animRect", "Landroid/graphics/Rect;", "animationDirection", "", "animationWindowView", "Landroid/widget/FrameLayout;", "chipLeft", "chipMinWidth", "chipRight", "chipWidth", "currentAnimatedView", "Lcom/android/systemui/statusbar/events/BackgroundAnimatableView;", "dotSize", "initialized", "", "themedContext", "Landroid/view/ContextThemeWrapper;", "createMoveOutAnimationDefault", "Landroid/animation/Animator;", "createMoveOutAnimationForDot", "init", "", "initializeAnimRect", "layoutParamsDefault", "Landroid/widget/FrameLayout$LayoutParams;", "marginEnd", "onSystemEventAnimationBegin", "onSystemEventAnimationFinish", "hasPersistentDot", "prepareChipAnimation", "viewCreator", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lcom/android/systemui/statusbar/events/ViewCreator;", "updateAnimatedBoundsX", "translation", "updateAnimatedViewBoundsHeight", "height", "verticalCenter", "updateAnimatedViewBoundsWidth", "width", "updateCurrentAnimatedView", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SystemEventChipAnimationController.kt */
public final class SystemEventChipAnimationController implements SystemStatusAnimationCallback {
    private Rect animRect;
    private int animationDirection = 1;
    /* access modifiers changed from: private */
    public FrameLayout animationWindowView;
    private int chipLeft;
    private int chipMinWidth;
    private int chipRight;
    private int chipWidth;
    private final StatusBarContentInsetsProvider contentInsetsProvider;
    private final Context context;
    /* access modifiers changed from: private */
    public BackgroundAnimatableView currentAnimatedView;
    private int dotSize;
    private boolean initialized;
    private final StatusBarWindowController statusBarWindowController;
    private ContextThemeWrapper themedContext;

    @Inject
    public SystemEventChipAnimationController(Context context2, StatusBarWindowController statusBarWindowController2, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(statusBarWindowController2, "statusBarWindowController");
        Intrinsics.checkNotNullParameter(statusBarContentInsetsProvider, "contentInsetsProvider");
        this.context = context2;
        this.statusBarWindowController = statusBarWindowController2;
        this.contentInsetsProvider = statusBarContentInsetsProvider;
        this.chipMinWidth = context2.getResources().getDimensionPixelSize(C1893R.dimen.ongoing_appops_chip_min_animation_width);
        this.dotSize = context2.getResources().getDimensionPixelSize(C1893R.dimen.ongoing_appops_dot_diameter);
        this.animRect = new Rect();
    }

    public final void prepareChipAnimation(Function1<? super Context, ? extends BackgroundAnimatableView> function1) {
        Integer num;
        Intrinsics.checkNotNullParameter(function1, "viewCreator");
        if (!this.initialized) {
            init();
        }
        FrameLayout frameLayout = this.animationWindowView;
        FrameLayout frameLayout2 = null;
        if (frameLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            frameLayout = null;
        }
        this.animationDirection = frameLayout.isLayoutRtl() ? 2 : 1;
        Pair<Integer, Integer> statusBarContentInsetsForCurrentRotation = this.contentInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        ContextThemeWrapper contextThemeWrapper = this.themedContext;
        if (contextThemeWrapper == null) {
            Intrinsics.throwUninitializedPropertyAccessException("themedContext");
            contextThemeWrapper = null;
        }
        BackgroundAnimatableView backgroundAnimatableView = (BackgroundAnimatableView) function1.invoke(contextThemeWrapper);
        FrameLayout frameLayout3 = this.animationWindowView;
        if (frameLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            frameLayout3 = null;
        }
        View view = backgroundAnimatableView.getView();
        FrameLayout frameLayout4 = this.animationWindowView;
        if (frameLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            frameLayout4 = null;
        }
        if (frameLayout4.isLayoutRtl()) {
            num = (Integer) statusBarContentInsetsForCurrentRotation.first;
        } else {
            num = (Integer) statusBarContentInsetsForCurrentRotation.second;
        }
        Intrinsics.checkNotNullExpressionValue(num, "if (animationWindowView.…       else insets.second");
        frameLayout3.addView(view, layoutParamsDefault(num.intValue()));
        backgroundAnimatableView.getView().setAlpha(0.0f);
        View view2 = backgroundAnimatableView.getView();
        FrameLayout frameLayout5 = this.animationWindowView;
        if (frameLayout5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            frameLayout5 = null;
        }
        ViewParent parent = frameLayout5.getParent();
        if (parent != null) {
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) parent).getWidth(), Integer.MIN_VALUE);
            FrameLayout frameLayout6 = this.animationWindowView;
            if (frameLayout6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            } else {
                frameLayout2 = frameLayout6;
            }
            view2.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(frameLayout2.getHeight(), Integer.MIN_VALUE));
            this.chipWidth = backgroundAnimatableView.getChipWidth();
            this.currentAnimatedView = backgroundAnimatableView;
            Rect statusBarContentAreaForCurrentRotation = this.contentInsetsProvider.getStatusBarContentAreaForCurrentRotation();
            if (this.animationDirection == 1) {
                this.chipRight = statusBarContentAreaForCurrentRotation.right;
                this.chipLeft = statusBarContentAreaForCurrentRotation.right - this.chipWidth;
                return;
            }
            this.chipLeft = statusBarContentAreaForCurrentRotation.left;
            this.chipRight = statusBarContentAreaForCurrentRotation.left + this.chipWidth;
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.View");
    }

    public Animator onSystemEventAnimationBegin() {
        initializeAnimRect();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setStartDelay(AnimationUtil.Companion.getFrames(7));
        ofFloat.setDuration(AnimationUtil.Companion.getFrames(5));
        ofFloat.setInterpolator((TimeInterpolator) null);
        ofFloat.addUpdateListener(new SystemEventChipAnimationController$$ExternalSyntheticLambda0(this, ofFloat));
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.chipMinWidth, this.chipWidth});
        ofInt.setStartDelay(AnimationUtil.Companion.getFrames(7));
        ofInt.setDuration(AnimationUtil.Companion.getFrames(23));
        ofInt.setInterpolator(SystemStatusAnimationSchedulerKt.STATUS_BAR_X_MOVE_IN);
        ofInt.addUpdateListener(new SystemEventChipAnimationController$$ExternalSyntheticLambda1(this, ofInt));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofFloat, ofInt});
        return animatorSet;
    }

    /* access modifiers changed from: private */
    /* renamed from: onSystemEventAnimationBegin$lambda-2$lambda-1  reason: not valid java name */
    public static final void m3071onSystemEventAnimationBegin$lambda2$lambda1(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "this$0");
        BackgroundAnimatableView backgroundAnimatableView = systemEventChipAnimationController.currentAnimatedView;
        View view = backgroundAnimatableView != null ? backgroundAnimatableView.getView() : null;
        if (view != null) {
            Object animatedValue = valueAnimator.getAnimatedValue();
            if (animatedValue != null) {
                view.setAlpha(((Float) animatedValue).floatValue());
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onSystemEventAnimationBegin$lambda-4$lambda-3  reason: not valid java name */
    public static final void m3072onSystemEventAnimationBegin$lambda4$lambda3(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            systemEventChipAnimationController.updateAnimatedViewBoundsWidth(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public Animator onSystemEventAnimationFinish(boolean z) {
        Animator animator;
        initializeAnimRect();
        if (z) {
            animator = createMoveOutAnimationForDot();
        } else {
            animator = createMoveOutAnimationDefault();
        }
        animator.addListener(new C2635xd070aad3(this));
        return animator;
    }

    private final Animator createMoveOutAnimationForDot() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.chipWidth, this.chipMinWidth});
        ofInt.setDuration(AnimationUtil.Companion.getFrames(9));
        ofInt.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1());
        ofInt.addUpdateListener(new SystemEventChipAnimationController$$ExternalSyntheticLambda2(this));
        ValueAnimator ofInt2 = ValueAnimator.ofInt(new int[]{this.chipMinWidth, this.dotSize});
        ofInt2.setStartDelay(AnimationUtil.Companion.getFrames(9));
        ofInt2.setDuration(AnimationUtil.Companion.getFrames(20));
        ofInt2.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2());
        ofInt2.addUpdateListener(new SystemEventChipAnimationController$$ExternalSyntheticLambda3(this));
        int i = this.dotSize * 2;
        BackgroundAnimatableView backgroundAnimatableView = this.currentAnimatedView;
        Intrinsics.checkNotNull(backgroundAnimatableView);
        View view = backgroundAnimatableView.getView();
        int top = view.getTop() + (view.getMeasuredHeight() / 2);
        BackgroundAnimatableView backgroundAnimatableView2 = this.currentAnimatedView;
        Intrinsics.checkNotNull(backgroundAnimatableView2);
        ValueAnimator ofInt3 = ValueAnimator.ofInt(new int[]{backgroundAnimatableView2.getView().getMeasuredHeight(), i});
        ofInt3.setStartDelay(AnimationUtil.Companion.getFrames(8));
        ofInt3.setDuration(AnimationUtil.Companion.getFrames(6));
        ofInt3.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1());
        ofInt3.addUpdateListener(new SystemEventChipAnimationController$$ExternalSyntheticLambda4(this, top));
        ValueAnimator ofInt4 = ValueAnimator.ofInt(new int[]{i, this.dotSize});
        ofInt4.setStartDelay(AnimationUtil.Companion.getFrames(14));
        ofInt4.setDuration(AnimationUtil.Companion.getFrames(15));
        ofInt4.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2());
        ofInt4.addUpdateListener(new SystemEventChipAnimationController$$ExternalSyntheticLambda5(this, top));
        ValueAnimator ofInt5 = ValueAnimator.ofInt(new int[]{0, this.dotSize});
        ofInt5.setStartDelay(AnimationUtil.Companion.getFrames(3));
        ofInt5.setDuration(AnimationUtil.Companion.getFrames(11));
        ofInt5.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_MOVE_TO_DOT());
        ofInt5.addUpdateListener(new SystemEventChipAnimationController$$ExternalSyntheticLambda6(this, ofInt5));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofInt, ofInt2, ofInt3, ofInt4, ofInt5});
        return animatorSet;
    }

    /* access modifiers changed from: private */
    /* renamed from: createMoveOutAnimationForDot$lambda-6$lambda-5  reason: not valid java name */
    public static final void m3069createMoveOutAnimationForDot$lambda6$lambda5(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            systemEventChipAnimationController.updateAnimatedViewBoundsWidth(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    /* access modifiers changed from: private */
    /* renamed from: createMoveOutAnimationForDot$lambda-8$lambda-7  reason: not valid java name */
    public static final void m3070createMoveOutAnimationForDot$lambda8$lambda7(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            systemEventChipAnimationController.updateAnimatedViewBoundsWidth(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    /* access modifiers changed from: private */
    /* renamed from: createMoveOutAnimationForDot$lambda-10$lambda-9  reason: not valid java name */
    public static final void m3066createMoveOutAnimationForDot$lambda10$lambda9(SystemEventChipAnimationController systemEventChipAnimationController, int i, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            systemEventChipAnimationController.updateAnimatedViewBoundsHeight(((Integer) animatedValue).intValue(), i);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    /* access modifiers changed from: private */
    /* renamed from: createMoveOutAnimationForDot$lambda-12$lambda-11  reason: not valid java name */
    public static final void m3067createMoveOutAnimationForDot$lambda12$lambda11(SystemEventChipAnimationController systemEventChipAnimationController, int i, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            systemEventChipAnimationController.updateAnimatedViewBoundsHeight(((Integer) animatedValue).intValue(), i);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    /* access modifiers changed from: private */
    /* renamed from: createMoveOutAnimationForDot$lambda-14$lambda-13  reason: not valid java name */
    public static final void m3068createMoveOutAnimationForDot$lambda14$lambda13(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        int i;
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "this$0");
        if (systemEventChipAnimationController.animationDirection == 1) {
            Object animatedValue = valueAnimator.getAnimatedValue();
            if (animatedValue != null) {
                i = ((Integer) animatedValue).intValue();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
        } else {
            Object animatedValue2 = valueAnimator.getAnimatedValue();
            if (animatedValue2 != null) {
                i = -((Integer) animatedValue2).intValue();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
        }
        systemEventChipAnimationController.updateAnimatedBoundsX(i);
    }

    private final Animator createMoveOutAnimationDefault() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.chipWidth, this.chipMinWidth});
        ofInt.setDuration(AnimationUtil.Companion.getFrames(23));
        ofInt.addUpdateListener(new SystemEventChipAnimationController$$ExternalSyntheticLambda7(this));
        Intrinsics.checkNotNullExpressionValue(ofInt, "moveOut");
        return ofInt;
    }

    /* access modifiers changed from: private */
    /* renamed from: createMoveOutAnimationDefault$lambda-17$lambda-16  reason: not valid java name */
    public static final void m3065createMoveOutAnimationDefault$lambda17$lambda16(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "this$0");
        if (systemEventChipAnimationController.currentAnimatedView != null) {
            Object animatedValue = valueAnimator.getAnimatedValue();
            if (animatedValue != null) {
                systemEventChipAnimationController.updateAnimatedViewBoundsWidth(((Integer) animatedValue).intValue());
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
        }
    }

    private final void init() {
        this.initialized = true;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.context, C1893R.style.Theme_SystemUI_QuickSettings);
        this.themedContext = contextThemeWrapper;
        FrameLayout frameLayout = null;
        View inflate = LayoutInflater.from(contextThemeWrapper).inflate(C1893R.layout.system_event_animation_window, (ViewGroup) null);
        if (inflate != null) {
            this.animationWindowView = (FrameLayout) inflate;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            layoutParams.gravity = 8388629;
            StatusBarWindowController statusBarWindowController2 = this.statusBarWindowController;
            FrameLayout frameLayout2 = this.animationWindowView;
            if (frameLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
                frameLayout2 = null;
            }
            statusBarWindowController2.addViewToWindow(frameLayout2, layoutParams);
            FrameLayout frameLayout3 = this.animationWindowView;
            if (frameLayout3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
                frameLayout3 = null;
            }
            frameLayout3.setClipToPadding(false);
            FrameLayout frameLayout4 = this.animationWindowView;
            if (frameLayout4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            } else {
                frameLayout = frameLayout4;
            }
            frameLayout.setClipChildren(false);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout");
    }

    private final FrameLayout.LayoutParams layoutParamsDefault(int i) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 8388629;
        layoutParams.setMarginEnd(i);
        return layoutParams;
    }

    private final void initializeAnimRect() {
        Rect rect = this.animRect;
        int i = this.chipLeft;
        BackgroundAnimatableView backgroundAnimatableView = this.currentAnimatedView;
        Intrinsics.checkNotNull(backgroundAnimatableView);
        int top = backgroundAnimatableView.getView().getTop();
        int i2 = this.chipRight;
        BackgroundAnimatableView backgroundAnimatableView2 = this.currentAnimatedView;
        Intrinsics.checkNotNull(backgroundAnimatableView2);
        rect.set(i, top, i2, backgroundAnimatableView2.getView().getBottom());
    }

    private final void updateAnimatedViewBoundsWidth(int i) {
        if (this.animationDirection == 1) {
            Rect rect = this.animRect;
            rect.set(this.chipRight - i, rect.top, this.chipRight, this.animRect.bottom);
        } else {
            Rect rect2 = this.animRect;
            rect2.set(this.chipLeft, rect2.top, this.chipLeft + i, this.animRect.bottom);
        }
        updateCurrentAnimatedView();
    }

    private final void updateAnimatedViewBoundsHeight(int i, int i2) {
        Rect rect = this.animRect;
        float f = ((float) i) / ((float) 2);
        rect.set(rect.left, i2 - MathKt.roundToInt(f), this.animRect.right, i2 + MathKt.roundToInt(f));
        updateCurrentAnimatedView();
    }

    private final void updateAnimatedBoundsX(int i) {
        BackgroundAnimatableView backgroundAnimatableView = this.currentAnimatedView;
        View view = backgroundAnimatableView != null ? backgroundAnimatableView.getView() : null;
        if (view != null) {
            view.setTranslationX((float) i);
        }
    }

    private final void updateCurrentAnimatedView() {
        BackgroundAnimatableView backgroundAnimatableView = this.currentAnimatedView;
        if (backgroundAnimatableView != null) {
            backgroundAnimatableView.setBoundsForAnimation(this.animRect.left, this.animRect.top, this.animRect.right, this.animRect.bottom);
        }
    }
}
