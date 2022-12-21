package com.android.systemui.util.animation;

import android.animation.ValueAnimator;
import android.util.MathUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.AuthDialog;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0004H\u0002J\u000e\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u0014J*\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0004J*\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u00042\u0006\u0010 \u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u001c2\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\"\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0004J2\u0010#\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020%2\b\b\u0002\u0010'\u001a\u00020(2\b\b\u0002\u0010)\u001a\u00020(J\b\u0010*\u001a\u00020\rH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R.\u0010\u000b\u001a\u0016\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r\u0018\u00010\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000¨\u0006+"}, mo64987d2 = {"Lcom/android/systemui/util/animation/TransitionLayoutController;", "", "()V", "animationStartState", "Lcom/android/systemui/util/animation/TransitionViewState;", "animator", "Landroid/animation/ValueAnimator;", "currentHeight", "", "currentState", "currentWidth", "sizeChangedListener", "Lkotlin/Function2;", "", "getSizeChangedListener", "()Lkotlin/jvm/functions/Function2;", "setSizeChangedListener", "(Lkotlin/jvm/functions/Function2;)V", "state", "transitionLayout", "Lcom/android/systemui/util/animation/TransitionLayout;", "applyStateToLayout", "attach", "getGoneState", "viewState", "disappearParameters", "Lcom/android/systemui/util/animation/DisappearParameters;", "goneProgress", "", "reusedState", "getInterpolatedState", "startState", "endState", "progress", "setMeasureState", "setState", "applyImmediately", "", "animate", "duration", "", "delay", "updateStateFromAnimation", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TransitionLayoutController.kt */
public class TransitionLayoutController {
    private TransitionViewState animationStartState;
    private ValueAnimator animator;
    private int currentHeight;
    private TransitionViewState currentState = new TransitionViewState();
    private int currentWidth;
    private Function2<? super Integer, ? super Integer, Unit> sizeChangedListener;
    private TransitionViewState state = new TransitionViewState();
    private TransitionLayout transitionLayout;

    public TransitionLayoutController() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(0.0f, 1.0f)");
        this.animator = ofFloat;
        ofFloat.addUpdateListener(new TransitionLayoutController$$ExternalSyntheticLambda0(this));
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
    }

    public final Function2<Integer, Integer, Unit> getSizeChangedListener() {
        return this.sizeChangedListener;
    }

    public final void setSizeChangedListener(Function2<? super Integer, ? super Integer, Unit> function2) {
        this.sizeChangedListener = function2;
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda-1$lambda-0  reason: not valid java name */
    public static final void m3304lambda1$lambda0(TransitionLayoutController transitionLayoutController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(transitionLayoutController, "this$0");
        transitionLayoutController.updateStateFromAnimation();
    }

    private final void updateStateFromAnimation() {
        if (this.animationStartState != null && this.animator.isRunning()) {
            TransitionViewState transitionViewState = this.animationStartState;
            Intrinsics.checkNotNull(transitionViewState);
            TransitionViewState interpolatedState = getInterpolatedState(transitionViewState, this.state, this.animator.getAnimatedFraction(), this.currentState);
            this.currentState = interpolatedState;
            applyStateToLayout(interpolatedState);
        }
    }

    private final void applyStateToLayout(TransitionViewState transitionViewState) {
        TransitionLayout transitionLayout2 = this.transitionLayout;
        if (transitionLayout2 != null) {
            transitionLayout2.setState(transitionViewState);
        }
        if (this.currentHeight != transitionViewState.getHeight() || this.currentWidth != transitionViewState.getWidth()) {
            this.currentHeight = transitionViewState.getHeight();
            int width = transitionViewState.getWidth();
            this.currentWidth = width;
            Function2<? super Integer, ? super Integer, Unit> function2 = this.sizeChangedListener;
            if (function2 != null) {
                function2.invoke(Integer.valueOf(width), Integer.valueOf(this.currentHeight));
            }
        }
    }

    public static /* synthetic */ TransitionViewState getGoneState$default(TransitionLayoutController transitionLayoutController, TransitionViewState transitionViewState, DisappearParameters disappearParameters, float f, TransitionViewState transitionViewState2, int i, Object obj) {
        if (obj == null) {
            if ((i & 8) != 0) {
                transitionViewState2 = null;
            }
            return transitionLayoutController.getGoneState(transitionViewState, disappearParameters, f, transitionViewState2);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getGoneState");
    }

    public final TransitionViewState getGoneState(TransitionViewState transitionViewState, DisappearParameters disappearParameters, float f, TransitionViewState transitionViewState2) {
        Intrinsics.checkNotNullParameter(transitionViewState, "viewState");
        Intrinsics.checkNotNullParameter(disappearParameters, "disappearParameters");
        float constrain = MathUtils.constrain(MathUtils.map(disappearParameters.getDisappearStart(), disappearParameters.getDisappearEnd(), 0.0f, 1.0f, f), 0.0f, 1.0f);
        TransitionViewState copy = transitionViewState.copy(transitionViewState2);
        copy.setWidth((int) MathUtils.lerp((float) transitionViewState.getWidth(), ((float) transitionViewState.getWidth()) * disappearParameters.getDisappearSize().x, constrain));
        copy.setHeight((int) MathUtils.lerp((float) transitionViewState.getHeight(), ((float) transitionViewState.getHeight()) * disappearParameters.getDisappearSize().y, constrain));
        copy.getTranslation().x = ((float) (transitionViewState.getWidth() - copy.getWidth())) * disappearParameters.getGonePivot().x;
        copy.getTranslation().y = ((float) (transitionViewState.getHeight() - copy.getHeight())) * disappearParameters.getGonePivot().y;
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

    public final TransitionViewState getInterpolatedState(TransitionViewState transitionViewState, TransitionViewState transitionViewState2, float f, TransitionViewState transitionViewState3) {
        TransitionLayoutController transitionLayoutController;
        TransitionViewState transitionViewState4;
        WidgetState widgetState;
        int i;
        int i2;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        boolean z;
        float f7 = f;
        Intrinsics.checkNotNullParameter(transitionViewState, "startState");
        Intrinsics.checkNotNullParameter(transitionViewState2, "endState");
        if (transitionViewState3 == null) {
            transitionViewState4 = new TransitionViewState();
            transitionLayoutController = this;
        } else {
            transitionLayoutController = this;
            transitionViewState4 = transitionViewState3;
        }
        TransitionLayout transitionLayout2 = transitionLayoutController.transitionLayout;
        if (transitionLayout2 == null) {
            return transitionViewState4;
        }
        int childCount = transitionLayout2.getChildCount();
        int i3 = 0;
        while (i3 < childCount) {
            int id = transitionLayout2.getChildAt(i3).getId();
            WidgetState widgetState2 = transitionViewState4.getWidgetStates().get(Integer.valueOf(id));
            if (widgetState2 == null) {
                widgetState2 = new WidgetState(0.0f, 0.0f, 0, 0, 0, 0, 0.0f, 0.0f, false, 511, (DefaultConstructorMarker) null);
            }
            WidgetState widgetState3 = transitionViewState.getWidgetStates().get(Integer.valueOf(id));
            if (!(widgetState3 == null || (widgetState = transitionViewState2.getWidgetStates().get(Integer.valueOf(id))) == null)) {
                if (widgetState3.getGone() != widgetState.getGone()) {
                    if (widgetState3.getGone()) {
                        float map = MathUtils.map(0.8f, 1.0f, 0.0f, 1.0f, f7);
                        boolean z2 = f7 < 0.8f;
                        float scale = widgetState.getScale();
                        float lerp = MathUtils.lerp(0.8f * scale, scale, f7);
                        int measureWidth = widgetState.getMeasureWidth();
                        int measureHeight = widgetState.getMeasureHeight();
                        f5 = MathUtils.lerp(widgetState3.getY() - (((float) measureHeight) / 2.0f), widgetState.getY(), f7);
                        i = measureWidth;
                        f6 = MathUtils.lerp(widgetState3.getX() - (((float) measureWidth) / 2.0f), widgetState.getX(), f7);
                        i2 = measureHeight;
                        z = z2;
                        f2 = map;
                        f3 = lerp;
                        f4 = 1.0f;
                    } else {
                        float map2 = MathUtils.map(0.0f, 0.19999999f, 0.0f, 1.0f, f7);
                        boolean z3 = f7 > 0.19999999f;
                        float scale2 = widgetState3.getScale();
                        float lerp2 = MathUtils.lerp(scale2, 0.8f * scale2, f7);
                        int measureWidth2 = widgetState3.getMeasureWidth();
                        int measureHeight2 = widgetState3.getMeasureHeight();
                        float lerp3 = MathUtils.lerp(widgetState3.getX(), widgetState.getX() - (((float) measureWidth2) / 2.0f), f7);
                        boolean z4 = z3;
                        float lerp4 = MathUtils.lerp(widgetState3.getY(), widgetState.getY() - (((float) measureHeight2) / 2.0f), f7);
                        f2 = map2;
                        i = measureWidth2;
                        f3 = lerp2;
                        i2 = measureHeight2;
                        z = z4;
                        f4 = 0.0f;
                        float f8 = lerp3;
                        f5 = lerp4;
                        f6 = f8;
                    }
                    widgetState2.setGone(z);
                } else {
                    widgetState2.setGone(widgetState3.getGone());
                    i = widgetState.getMeasureWidth();
                    i2 = widgetState.getMeasureHeight();
                    f3 = MathUtils.lerp(widgetState3.getScale(), widgetState.getScale(), f7);
                    f6 = MathUtils.lerp(widgetState3.getX(), widgetState.getX(), f7);
                    f5 = MathUtils.lerp(widgetState3.getY(), widgetState.getY(), f7);
                    f4 = f7;
                    f2 = f4;
                }
                widgetState2.setX(f6);
                widgetState2.setY(f5);
                widgetState2.setAlpha(MathUtils.lerp(widgetState3.getAlpha(), widgetState.getAlpha(), f2));
                widgetState2.setWidth((int) MathUtils.lerp((float) widgetState3.getWidth(), (float) widgetState.getWidth(), f4));
                widgetState2.setHeight((int) MathUtils.lerp((float) widgetState3.getHeight(), (float) widgetState.getHeight(), f4));
                widgetState2.setScale(f3);
                widgetState2.setMeasureWidth(i);
                widgetState2.setMeasureHeight(i2);
                transitionViewState4.getWidgetStates().put(Integer.valueOf(id), widgetState2);
            }
            i3++;
            TransitionViewState transitionViewState5 = transitionViewState;
            TransitionViewState transitionViewState6 = transitionViewState2;
        }
        transitionViewState4.setWidth((int) MathUtils.lerp((float) transitionViewState.getWidth(), (float) transitionViewState2.getWidth(), f7));
        transitionViewState4.setHeight((int) MathUtils.lerp((float) transitionViewState.getHeight(), (float) transitionViewState2.getHeight(), f7));
        transitionViewState4.getTranslation().x = MathUtils.lerp(transitionViewState.getTranslation().x, transitionViewState2.getTranslation().x, f7);
        transitionViewState4.getTranslation().y = MathUtils.lerp(transitionViewState.getTranslation().y, transitionViewState2.getTranslation().y, f7);
        transitionViewState4.setAlpha(MathUtils.lerp(transitionViewState.getAlpha(), transitionViewState2.getAlpha(), f7));
        transitionViewState4.getContentTranslation().x = MathUtils.lerp(transitionViewState.getContentTranslation().x, transitionViewState2.getContentTranslation().x, f7);
        transitionViewState4.getContentTranslation().y = MathUtils.lerp(transitionViewState.getContentTranslation().y, transitionViewState2.getContentTranslation().y, f7);
        return transitionViewState4;
    }

    public final void attach(TransitionLayout transitionLayout2) {
        Intrinsics.checkNotNullParameter(transitionLayout2, "transitionLayout");
        this.transitionLayout = transitionLayout2;
    }

    public static /* synthetic */ void setState$default(TransitionLayoutController transitionLayoutController, TransitionViewState transitionViewState, boolean z, boolean z2, long j, long j2, int i, Object obj) {
        if (obj == null) {
            transitionLayoutController.setState(transitionViewState, z, z2, (i & 8) != 0 ? 0 : j, (i & 16) != 0 ? 0 : j2);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setState");
    }

    public final void setState(TransitionViewState transitionViewState, boolean z, boolean z2, long j, long j2) {
        Intrinsics.checkNotNullParameter(transitionViewState, AuthDialog.KEY_BIOMETRIC_STATE);
        boolean z3 = z2 && this.currentState.getWidth() != 0;
        this.state = TransitionViewState.copy$default(transitionViewState, (TransitionViewState) null, 1, (Object) null);
        if (z || this.transitionLayout == null) {
            this.animator.cancel();
            applyStateToLayout(this.state);
            this.currentState = transitionViewState.copy(this.currentState);
        } else if (z3) {
            this.animationStartState = TransitionViewState.copy$default(this.currentState, (TransitionViewState) null, 1, (Object) null);
            this.animator.setDuration(j);
            this.animator.setStartDelay(j2);
            this.animator.start();
        } else if (!this.animator.isRunning()) {
            applyStateToLayout(this.state);
            this.currentState = transitionViewState.copy(this.currentState);
        }
    }

    public final void setMeasureState(TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(transitionViewState, AuthDialog.KEY_BIOMETRIC_STATE);
        TransitionLayout transitionLayout2 = this.transitionLayout;
        if (transitionLayout2 != null) {
            transitionLayout2.setMeasureState(transitionViewState);
        }
    }
}
