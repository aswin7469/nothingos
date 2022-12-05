package com.android.systemui.util.animation;

import android.animation.ValueAnimator;
import android.util.MathUtils;
import com.android.systemui.animation.Interpolators;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: TransitionLayoutController.kt */
/* loaded from: classes2.dex */
public class TransitionLayoutController {
    @Nullable
    private TransitionViewState animationStartState;
    @NotNull
    private ValueAnimator animator;
    private int currentHeight;
    private int currentWidth;
    @Nullable
    private Function2<? super Integer, ? super Integer, Unit> sizeChangedListener;
    @Nullable
    private TransitionLayout transitionLayout;
    @NotNull
    private TransitionViewState currentState = new TransitionViewState();
    @NotNull
    private TransitionViewState state = new TransitionViewState();

    public TransitionLayoutController() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(0.0f, 1.0f)");
        this.animator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.util.animation.TransitionLayoutController$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                TransitionLayoutController.this.updateStateFromAnimation();
            }
        });
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
    }

    public final void setSizeChangedListener(@Nullable Function2<? super Integer, ? super Integer, Unit> function2) {
        this.sizeChangedListener = function2;
    }

    public final void updateStateFromAnimation() {
        if (this.animationStartState == null || !this.animator.isRunning()) {
            return;
        }
        TransitionViewState transitionViewState = this.animationStartState;
        Intrinsics.checkNotNull(transitionViewState);
        TransitionViewState interpolatedState = getInterpolatedState(transitionViewState, this.state, this.animator.getAnimatedFraction(), this.currentState);
        this.currentState = interpolatedState;
        applyStateToLayout(interpolatedState);
    }

    private final void applyStateToLayout(TransitionViewState transitionViewState) {
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout != null) {
            transitionLayout.setState(transitionViewState);
        }
        if (this.currentHeight == transitionViewState.getHeight() && this.currentWidth == transitionViewState.getWidth()) {
            return;
        }
        this.currentHeight = transitionViewState.getHeight();
        int width = transitionViewState.getWidth();
        this.currentWidth = width;
        Function2<? super Integer, ? super Integer, Unit> function2 = this.sizeChangedListener;
        if (function2 == null) {
            return;
        }
        function2.mo1950invoke(Integer.valueOf(width), Integer.valueOf(this.currentHeight));
    }

    @NotNull
    public final TransitionViewState getGoneState(@NotNull TransitionViewState viewState, @NotNull DisappearParameters disappearParameters, float f, @Nullable TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(viewState, "viewState");
        Intrinsics.checkNotNullParameter(disappearParameters, "disappearParameters");
        float constrain = MathUtils.constrain(MathUtils.map(disappearParameters.getDisappearStart(), disappearParameters.getDisappearEnd(), 0.0f, 1.0f, f), 0.0f, 1.0f);
        TransitionViewState copy = viewState.copy(transitionViewState);
        copy.setWidth((int) MathUtils.lerp(viewState.getWidth(), viewState.getWidth() * disappearParameters.getDisappearSize().x, constrain));
        copy.setHeight((int) MathUtils.lerp(viewState.getHeight(), viewState.getHeight() * disappearParameters.getDisappearSize().y, constrain));
        copy.getTranslation().x = (viewState.getWidth() - copy.getWidth()) * disappearParameters.getGonePivot().x;
        copy.getTranslation().y = (viewState.getHeight() - copy.getHeight()) * disappearParameters.getGonePivot().y;
        copy.getContentTranslation().x = (disappearParameters.getContentTranslationFraction().x - 1.0f) * copy.getTranslation().x;
        copy.getContentTranslation().y = (disappearParameters.getContentTranslationFraction().y - 1.0f) * copy.getTranslation().y;
        copy.setAlpha(MathUtils.constrain(MathUtils.map(disappearParameters.getFadeStartPosition(), 1.0f, 1.0f, 0.0f, constrain), 0.0f, 1.0f));
        return copy;
    }

    public static /* synthetic */ TransitionViewState getInterpolatedState$default(TransitionLayoutController transitionLayoutController, TransitionViewState transitionViewState, TransitionViewState transitionViewState2, float f, TransitionViewState transitionViewState3, int i, Object obj) {
        if (obj == null) {
            if ((i & 8) != 0) {
                transitionViewState3 = null;
            }
            return transitionLayoutController.getInterpolatedState(transitionViewState, transitionViewState2, f, transitionViewState3);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getInterpolatedState");
    }

    @NotNull
    public final TransitionViewState getInterpolatedState(@NotNull TransitionViewState startState, @NotNull TransitionViewState endState, float f, @Nullable TransitionViewState transitionViewState) {
        TransitionLayoutController transitionLayoutController;
        TransitionViewState transitionViewState2;
        WidgetState widgetState;
        int measureWidth;
        int measureHeight;
        float lerp;
        float lerp2;
        float lerp3;
        float f2;
        float f3;
        boolean z;
        Intrinsics.checkNotNullParameter(startState, "startState");
        Intrinsics.checkNotNullParameter(endState, "endState");
        if (transitionViewState == null) {
            transitionViewState2 = new TransitionViewState();
            transitionLayoutController = this;
        } else {
            transitionLayoutController = this;
            transitionViewState2 = transitionViewState;
        }
        TransitionLayout transitionLayout = transitionLayoutController.transitionLayout;
        if (transitionLayout == null) {
            return transitionViewState2;
        }
        int childCount = transitionLayout.getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                int id = transitionLayout.getChildAt(i).getId();
                WidgetState widgetState2 = transitionViewState2.getWidgetStates().get(Integer.valueOf(id));
                if (widgetState2 == null) {
                    widgetState2 = new WidgetState(0.0f, 0.0f, 0, 0, 0, 0, 0.0f, 0.0f, false, 511, null);
                }
                WidgetState widgetState3 = startState.getWidgetStates().get(Integer.valueOf(id));
                if (widgetState3 != null && (widgetState = endState.getWidgetStates().get(Integer.valueOf(id))) != null) {
                    if (widgetState3.getGone() != widgetState.getGone()) {
                        if (widgetState3.getGone()) {
                            float map = MathUtils.map(0.8f, 1.0f, 0.0f, 1.0f, f);
                            boolean z2 = f < 0.8f;
                            float scale = widgetState.getScale();
                            float lerp4 = MathUtils.lerp(0.8f * scale, scale, f);
                            int measureWidth2 = widgetState.getMeasureWidth();
                            int measureHeight2 = widgetState.getMeasureHeight();
                            float lerp5 = MathUtils.lerp(widgetState3.getX() - (measureWidth2 / 2.0f), widgetState.getX(), f);
                            lerp3 = MathUtils.lerp(widgetState3.getY() - (measureHeight2 / 2.0f), widgetState.getY(), f);
                            measureWidth = measureWidth2;
                            lerp2 = lerp5;
                            measureHeight = measureHeight2;
                            z = z2;
                            f3 = map;
                            lerp = lerp4;
                            f2 = 1.0f;
                        } else {
                            float map2 = MathUtils.map(0.0f, 0.19999999f, 0.0f, 1.0f, f);
                            boolean z3 = f > 0.19999999f;
                            float scale2 = widgetState3.getScale();
                            float lerp6 = MathUtils.lerp(scale2, 0.8f * scale2, f);
                            int measureWidth3 = widgetState3.getMeasureWidth();
                            int measureHeight3 = widgetState3.getMeasureHeight();
                            float lerp7 = MathUtils.lerp(widgetState3.getX(), widgetState.getX() - (measureWidth3 / 2.0f), f);
                            boolean z4 = z3;
                            float lerp8 = MathUtils.lerp(widgetState3.getY(), widgetState.getY() - (measureHeight3 / 2.0f), f);
                            f3 = map2;
                            measureWidth = measureWidth3;
                            lerp = lerp6;
                            measureHeight = measureHeight3;
                            z = z4;
                            f2 = 0.0f;
                            lerp3 = lerp8;
                            lerp2 = lerp7;
                        }
                        widgetState2.setGone(z);
                    } else {
                        widgetState2.setGone(widgetState3.getGone());
                        measureWidth = widgetState.getMeasureWidth();
                        measureHeight = widgetState.getMeasureHeight();
                        lerp = MathUtils.lerp(widgetState3.getScale(), widgetState.getScale(), f);
                        lerp2 = MathUtils.lerp(widgetState3.getX(), widgetState.getX(), f);
                        lerp3 = MathUtils.lerp(widgetState3.getY(), widgetState.getY(), f);
                        f2 = f;
                        f3 = f2;
                    }
                    widgetState2.setX(lerp2);
                    widgetState2.setY(lerp3);
                    widgetState2.setAlpha(MathUtils.lerp(widgetState3.getAlpha(), widgetState.getAlpha(), f3));
                    widgetState2.setWidth((int) MathUtils.lerp(widgetState3.getWidth(), widgetState.getWidth(), f2));
                    widgetState2.setHeight((int) MathUtils.lerp(widgetState3.getHeight(), widgetState.getHeight(), f2));
                    widgetState2.setScale(lerp);
                    widgetState2.setMeasureWidth(measureWidth);
                    widgetState2.setMeasureHeight(measureHeight);
                    transitionViewState2.getWidgetStates().put(Integer.valueOf(id), widgetState2);
                }
                if (i2 >= childCount) {
                    break;
                }
                i = i2;
            }
        }
        transitionViewState2.setWidth((int) MathUtils.lerp(startState.getWidth(), endState.getWidth(), f));
        transitionViewState2.setHeight((int) MathUtils.lerp(startState.getHeight(), endState.getHeight(), f));
        transitionViewState2.getTranslation().x = MathUtils.lerp(startState.getTranslation().x, endState.getTranslation().x, f);
        transitionViewState2.getTranslation().y = MathUtils.lerp(startState.getTranslation().y, endState.getTranslation().y, f);
        transitionViewState2.setAlpha(MathUtils.lerp(startState.getAlpha(), endState.getAlpha(), f));
        transitionViewState2.getContentTranslation().x = MathUtils.lerp(startState.getContentTranslation().x, endState.getContentTranslation().x, f);
        transitionViewState2.getContentTranslation().y = MathUtils.lerp(startState.getContentTranslation().y, endState.getContentTranslation().y, f);
        return transitionViewState2;
    }

    public final void attach(@NotNull TransitionLayout transitionLayout) {
        Intrinsics.checkNotNullParameter(transitionLayout, "transitionLayout");
        this.transitionLayout = transitionLayout;
    }

    public final void setState(@NotNull TransitionViewState state, boolean z, boolean z2, long j, long j2) {
        Intrinsics.checkNotNullParameter(state, "state");
        boolean z3 = z2 && this.currentState.getWidth() != 0;
        this.state = TransitionViewState.copy$default(state, null, 1, null);
        if (z || this.transitionLayout == null) {
            this.animator.cancel();
            applyStateToLayout(this.state);
            this.currentState = state.copy(this.currentState);
        } else if (z3) {
            this.animationStartState = TransitionViewState.copy$default(this.currentState, null, 1, null);
            this.animator.setDuration(j);
            this.animator.setStartDelay(j2);
            this.animator.start();
        } else if (this.animator.isRunning()) {
        } else {
            applyStateToLayout(this.state);
            this.currentState = state.copy(this.currentState);
        }
    }

    public final void setMeasureState(@NotNull TransitionViewState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout == null) {
            return;
        }
        transitionLayout.setMeasureState(state);
    }
}
