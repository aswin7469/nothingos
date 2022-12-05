package com.android.systemui.util.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.statusbar.CrossFadeHelper;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: TransitionLayout.kt */
/* loaded from: classes2.dex */
public final class TransitionLayout extends ConstraintLayout {
    @NotNull
    private final Rect boundsRect;
    @NotNull
    private TransitionViewState currentState;
    private int desiredMeasureHeight;
    private int desiredMeasureWidth;
    private boolean isPreDrawApplicatorRegistered;
    private boolean measureAsConstraint;
    @NotNull
    private TransitionViewState measureState;
    @NotNull
    private final Set<Integer> originalGoneChildrenSet;
    @NotNull
    private final Map<Integer, Float> originalViewAlphas;
    @NotNull
    private final TransitionLayout$preDrawApplicator$1 preDrawApplicator;
    private int transitionVisibility;
    private boolean updateScheduled;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public TransitionLayout(@NotNull Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public TransitionLayout(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ TransitionLayout(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.systemui.util.animation.TransitionLayout$preDrawApplicator$1] */
    public TransitionLayout(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        this.boundsRect = new Rect();
        this.originalGoneChildrenSet = new LinkedHashSet();
        this.originalViewAlphas = new LinkedHashMap();
        this.currentState = new TransitionViewState();
        this.measureState = new TransitionViewState();
        this.preDrawApplicator = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.util.animation.TransitionLayout$preDrawApplicator$1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                TransitionLayout.this.updateScheduled = false;
                TransitionLayout.this.getViewTreeObserver().removeOnPreDrawListener(this);
                TransitionLayout.this.isPreDrawApplicatorRegistered = false;
                TransitionLayout.this.applyCurrentState();
                return true;
            }
        };
    }

    public final void setMeasureState(@NotNull TransitionViewState value) {
        Intrinsics.checkNotNullParameter(value, "value");
        int width = value.getWidth();
        int height = value.getHeight();
        if (width == this.desiredMeasureWidth && height == this.desiredMeasureHeight) {
            return;
        }
        this.desiredMeasureWidth = width;
        this.desiredMeasureHeight = height;
        if (isInLayout()) {
            forceLayout();
        } else {
            requestLayout();
        }
    }

    @Override // android.view.View
    public void setTransitionVisibility(int i) {
        super.setTransitionVisibility(i);
        this.transitionVisibility = i;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = getChildAt(i);
                if (childAt.getId() == -1) {
                    childAt.setId(i);
                }
                if (childAt.getVisibility() == 8) {
                    this.originalGoneChildrenSet.add(Integer.valueOf(childAt.getId()));
                }
                this.originalViewAlphas.put(Integer.valueOf(childAt.getId()), Float.valueOf(childAt.getAlpha()));
                if (i2 >= childCount) {
                    return;
                }
                i = i2;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isPreDrawApplicatorRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this.preDrawApplicator);
            this.isPreDrawApplicatorRegistered = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void applyCurrentState() {
        Integer num;
        int i;
        int childCount = getChildCount();
        int i2 = (int) this.currentState.getContentTranslation().x;
        int i3 = (int) this.currentState.getContentTranslation().y;
        if (childCount > 0) {
            int i4 = 0;
            while (true) {
                int i5 = i4 + 1;
                View childAt = getChildAt(i4);
                WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
                if (widgetState != null) {
                    if (!(childAt instanceof TextView) || widgetState.getWidth() >= widgetState.getMeasureWidth()) {
                        num = null;
                    } else {
                        TextView textView = (TextView) childAt;
                        num = Integer.valueOf((textView.getLayout() == null || textView.getLayout().getParagraphDirection(0) != -1) ? 0 : widgetState.getMeasureWidth() - widgetState.getWidth());
                    }
                    if (childAt.getMeasuredWidth() != widgetState.getMeasureWidth() || childAt.getMeasuredHeight() != widgetState.getMeasureHeight()) {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureHeight(), 1073741824));
                        childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                    }
                    int intValue = num == null ? 0 : num.intValue();
                    int x = (((int) widgetState.getX()) + i2) - intValue;
                    int y = ((int) widgetState.getY()) + i3;
                    boolean z = true;
                    boolean z2 = num != null;
                    childAt.setLeftTopRightBottom(x, y, (z2 ? widgetState.getMeasureWidth() : widgetState.getWidth()) + x, (z2 ? widgetState.getMeasureHeight() : widgetState.getHeight()) + y);
                    childAt.setScaleX(widgetState.getScale());
                    childAt.setScaleY(widgetState.getScale());
                    Rect clipBounds = childAt.getClipBounds();
                    if (clipBounds == null) {
                        clipBounds = new Rect();
                    }
                    clipBounds.set(intValue, 0, widgetState.getWidth() + intValue, widgetState.getHeight());
                    childAt.setClipBounds(clipBounds);
                    CrossFadeHelper.fadeIn(childAt, widgetState.getAlpha());
                    if (!widgetState.getGone()) {
                        if (widgetState.getAlpha() != 0.0f) {
                            z = false;
                        }
                        if (!z) {
                            i = 0;
                            childAt.setVisibility(i);
                        }
                    }
                    i = 4;
                    childAt.setVisibility(i);
                }
                if (i5 >= childCount) {
                    break;
                }
                i4 = i5;
            }
        }
        updateBounds();
        setTranslationX(this.currentState.getTranslation().x);
        setTranslationY(this.currentState.getTranslation().y);
        CrossFadeHelper.fadeIn(this, this.currentState.getAlpha());
        int i6 = this.transitionVisibility;
        if (i6 != 0) {
            setTransitionVisibility(i6);
        }
    }

    private final void applyCurrentStateOnPredraw() {
        if (!this.updateScheduled) {
            this.updateScheduled = true;
            if (this.isPreDrawApplicatorRegistered) {
                return;
            }
            getViewTreeObserver().addOnPreDrawListener(this.preDrawApplicator);
            this.isPreDrawApplicatorRegistered = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (this.measureAsConstraint) {
            super.onMeasure(i, i2);
            return;
        }
        int i3 = 0;
        int childCount = getChildCount();
        if (childCount > 0) {
            while (true) {
                int i4 = i3 + 1;
                View childAt = getChildAt(i3);
                WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
                if (widgetState != null) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureHeight(), 1073741824));
                }
                if (i4 >= childCount) {
                    break;
                }
                i3 = i4;
            }
        }
        setMeasuredDimension(this.desiredMeasureWidth, this.desiredMeasureHeight);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.measureAsConstraint) {
            super.onLayout(z, getLeft(), getTop(), getRight(), getBottom());
            return;
        }
        int childCount = getChildCount();
        if (childCount > 0) {
            int i5 = 0;
            while (true) {
                int i6 = i5 + 1;
                View childAt = getChildAt(i5);
                childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                if (i6 >= childCount) {
                    break;
                }
                i5 = i6;
            }
        }
        applyCurrentState();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(@Nullable Canvas canvas) {
        if (canvas != null) {
            canvas.save();
        }
        if (canvas != null) {
            canvas.clipRect(this.boundsRect);
        }
        super.dispatchDraw(canvas);
        if (canvas == null) {
            return;
        }
        canvas.restore();
    }

    private final void updateBounds() {
        int left = getLeft();
        int top = getTop();
        setLeftTopRightBottom(left, top, this.currentState.getWidth() + left, this.currentState.getHeight() + top);
        this.boundsRect.set(0, 0, getWidth(), getHeight());
    }

    @NotNull
    public final TransitionViewState calculateViewState(@NotNull MeasurementInput input, @NotNull ConstraintSet constraintSet, @Nullable TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(constraintSet, "constraintSet");
        if (transitionViewState == null) {
            transitionViewState = new TransitionViewState();
        }
        applySetToFullLayout(constraintSet);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        this.measureAsConstraint = true;
        measure(input.getWidthMeasureSpec(), input.getHeightMeasureSpec());
        int left = getLeft();
        int top = getTop();
        layout(left, top, getMeasuredWidth() + left, getMeasuredHeight() + top);
        this.measureAsConstraint = false;
        transitionViewState.initFromLayout(this);
        ensureViewsNotGone();
        setMeasuredDimension(measuredWidth, measuredHeight);
        applyCurrentStateOnPredraw();
        return transitionViewState;
    }

    private final void applySetToFullLayout(ConstraintSet constraintSet) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = getChildAt(i);
                if (this.originalGoneChildrenSet.contains(Integer.valueOf(childAt.getId()))) {
                    childAt.setVisibility(8);
                }
                Float f = this.originalViewAlphas.get(Integer.valueOf(childAt.getId()));
                childAt.setAlpha(f == null ? 1.0f : f.floatValue());
                if (i2 >= childCount) {
                    break;
                }
                i = i2;
            }
        }
        constraintSet.applyTo(this);
    }

    private final void ensureViewsNotGone() {
        int childCount = getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = getChildAt(i);
                WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
                childAt.setVisibility(!Intrinsics.areEqual(widgetState == null ? null : Boolean.valueOf(widgetState.getGone()), Boolean.FALSE) ? 4 : 0);
                if (i2 >= childCount) {
                    return;
                }
                i = i2;
            }
        }
    }

    public final void setState(@NotNull TransitionViewState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.currentState = state;
        applyCurrentState();
    }
}
