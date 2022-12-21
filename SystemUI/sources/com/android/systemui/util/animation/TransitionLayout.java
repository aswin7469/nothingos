package com.android.systemui.util.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.statusbar.CrossFadeHelper;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000k\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010#\n\u0000\n\u0002\u0010%\n\u0002\u0010\u0007\n\u0000\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0012*\u0001\u001e\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\"\u001a\u00020#H\u0002J\b\u0010$\u001a\u00020#H\u0002J\u0010\u0010%\u001a\u00020#2\u0006\u0010&\u001a\u00020'H\u0002J\"\u0010(\u001a\u00020\f2\u0006\u0010)\u001a\u00020*2\u0006\u0010&\u001a\u00020'2\n\b\u0002\u0010+\u001a\u0004\u0018\u00010\fJ\u0012\u0010,\u001a\u00020#2\b\u0010-\u001a\u0004\u0018\u00010.H\u0014J\b\u0010/\u001a\u00020#H\u0002J\b\u00100\u001a\u00020#H\u0014J\b\u00101\u001a\u00020#H\u0014J0\u00102\u001a\u00020#2\u0006\u00103\u001a\u00020\u00102\u0006\u00104\u001a\u00020\u00072\u0006\u00105\u001a\u00020\u00072\u0006\u00106\u001a\u00020\u00072\u0006\u00107\u001a\u00020\u0007H\u0014J\u0018\u00108\u001a\u00020#2\u0006\u00109\u001a\u00020\u00072\u0006\u0010:\u001a\u00020\u0007H\u0014J\u000e\u0010;\u001a\u00020#2\u0006\u0010<\u001a\u00020\fJ\u0010\u0010=\u001a\u00020#2\u0006\u0010>\u001a\u00020\u0007H\u0016J\b\u0010?\u001a\u00020#H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R$\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\f@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00070\u0019X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u001c0\u001bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0004\n\u0002\u0010\u001fR\u000e\u0010 \u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000¨\u0006@"}, mo64987d2 = {"Lcom/android/systemui/util/animation/TransitionLayout;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "boundsRect", "Landroid/graphics/Rect;", "currentState", "Lcom/android/systemui/util/animation/TransitionViewState;", "desiredMeasureHeight", "desiredMeasureWidth", "isPreDrawApplicatorRegistered", "", "measureAsConstraint", "value", "measureState", "getMeasureState", "()Lcom/android/systemui/util/animation/TransitionViewState;", "setMeasureState", "(Lcom/android/systemui/util/animation/TransitionViewState;)V", "originalGoneChildrenSet", "", "originalViewAlphas", "", "", "preDrawApplicator", "com/android/systemui/util/animation/TransitionLayout$preDrawApplicator$1", "Lcom/android/systemui/util/animation/TransitionLayout$preDrawApplicator$1;", "transitionVisibility", "updateScheduled", "applyCurrentState", "", "applyCurrentStateOnPredraw", "applySetToFullLayout", "constraintSet", "Landroidx/constraintlayout/widget/ConstraintSet;", "calculateViewState", "input", "Lcom/android/systemui/util/animation/MeasurementInput;", "existing", "dispatchDraw", "canvas", "Landroid/graphics/Canvas;", "ensureViewsNotGone", "onDetachedFromWindow", "onFinishInflate", "onLayout", "changed", "l", "t", "r", "b", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "setState", "state", "setTransitionVisibility", "visibility", "updateBounds", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TransitionLayout.kt */
public final class TransitionLayout extends ConstraintLayout {
    public Map<Integer, View> _$_findViewCache;
    private final Rect boundsRect;
    private TransitionViewState currentState;
    private int desiredMeasureHeight;
    private int desiredMeasureWidth;
    /* access modifiers changed from: private */
    public boolean isPreDrawApplicatorRegistered;
    private boolean measureAsConstraint;
    private TransitionViewState measureState;
    private final Set<Integer> originalGoneChildrenSet;
    private final Map<Integer, Float> originalViewAlphas;
    private final TransitionLayout$preDrawApplicator$1 preDrawApplicator;
    private int transitionVisibility;
    /* access modifiers changed from: private */
    public boolean updateScheduled;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public TransitionLayout(Context context) {
        this(context, (AttributeSet) null, 0, 6, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public TransitionLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TransitionLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
        this.boundsRect = new Rect();
        this.originalGoneChildrenSet = new LinkedHashSet();
        this.originalViewAlphas = new LinkedHashMap();
        this.currentState = new TransitionViewState();
        this.measureState = new TransitionViewState();
        this.preDrawApplicator = new TransitionLayout$preDrawApplicator$1(this);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ TransitionLayout(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    public final TransitionViewState getMeasureState() {
        return this.measureState;
    }

    public final void setMeasureState(TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(transitionViewState, "value");
        int width = transitionViewState.getWidth();
        int height = transitionViewState.getHeight();
        if (width != this.desiredMeasureWidth || height != this.desiredMeasureHeight) {
            this.desiredMeasureWidth = width;
            this.desiredMeasureHeight = height;
            if (isInLayout()) {
                forceLayout();
            } else {
                requestLayout();
            }
        }
    }

    public void setTransitionVisibility(int i) {
        super.setTransitionVisibility(i);
        this.transitionVisibility = i;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getId() == -1) {
                childAt.setId(i);
            }
            if (childAt.getVisibility() == 8) {
                this.originalGoneChildrenSet.add(Integer.valueOf(childAt.getId()));
            }
            this.originalViewAlphas.put(Integer.valueOf(childAt.getId()), Float.valueOf(childAt.getAlpha()));
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isPreDrawApplicatorRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this.preDrawApplicator);
            this.isPreDrawApplicatorRegistered = false;
        }
    }

    /* access modifiers changed from: private */
    public final void applyCurrentState() {
        Integer num;
        int i;
        int childCount = getChildCount();
        int i2 = (int) this.currentState.getContentTranslation().x;
        int i3 = (int) this.currentState.getContentTranslation().y;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
            if (widgetState != null) {
                if (!(childAt instanceof TextView) || widgetState.getWidth() >= widgetState.getMeasureWidth()) {
                    num = null;
                    Integer num2 = null;
                } else {
                    num = Integer.valueOf(((TextView) childAt).getLayout().getParagraphDirection(0) == -1 ? widgetState.getMeasureWidth() - widgetState.getWidth() : 0);
                }
                if (!(childAt.getMeasuredWidth() == widgetState.getMeasureWidth() && childAt.getMeasuredHeight() == widgetState.getMeasureHeight())) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureHeight(), 1073741824));
                    childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                }
                int intValue = num != null ? num.intValue() : 0;
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
        }
        updateBounds();
        setTranslationX(this.currentState.getTranslation().x);
        setTranslationY(this.currentState.getTranslation().y);
        CrossFadeHelper.fadeIn(this, this.currentState.getAlpha());
        int i5 = this.transitionVisibility;
        if (i5 != 0) {
            setTransitionVisibility(i5);
        }
    }

    private final void applyCurrentStateOnPredraw() {
        if (!this.updateScheduled) {
            this.updateScheduled = true;
            if (!this.isPreDrawApplicatorRegistered) {
                getViewTreeObserver().addOnPreDrawListener(this.preDrawApplicator);
                this.isPreDrawApplicatorRegistered = true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.measureAsConstraint) {
            super.onMeasure(i, i2);
            return;
        }
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
            if (widgetState != null) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureHeight(), 1073741824));
            }
        }
        setMeasuredDimension(this.desiredMeasureWidth, this.desiredMeasureHeight);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.measureAsConstraint) {
            super.onLayout(z, getLeft(), getTop(), getRight(), getBottom());
            return;
        }
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
        }
        applyCurrentState();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.save();
        }
        if (canvas != null) {
            canvas.clipRect(this.boundsRect);
        }
        super.dispatchDraw(canvas);
        if (canvas != null) {
            canvas.restore();
        }
    }

    private final void updateBounds() {
        int left = getLeft();
        int top = getTop();
        setLeftTopRightBottom(left, top, this.currentState.getWidth() + left, this.currentState.getHeight() + top);
        this.boundsRect.set(0, 0, getWidth(), getHeight());
    }

    public static /* synthetic */ TransitionViewState calculateViewState$default(TransitionLayout transitionLayout, MeasurementInput measurementInput, ConstraintSet constraintSet, TransitionViewState transitionViewState, int i, Object obj) {
        if ((i & 4) != 0) {
            transitionViewState = null;
        }
        return transitionLayout.calculateViewState(measurementInput, constraintSet, transitionViewState);
    }

    public final TransitionViewState calculateViewState(MeasurementInput measurementInput, ConstraintSet constraintSet, TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(measurementInput, "input");
        Intrinsics.checkNotNullParameter(constraintSet, "constraintSet");
        if (transitionViewState == null) {
            transitionViewState = new TransitionViewState();
        }
        applySetToFullLayout(constraintSet);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        this.measureAsConstraint = true;
        measure(measurementInput.getWidthMeasureSpec(), measurementInput.getHeightMeasureSpec());
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
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (this.originalGoneChildrenSet.contains(Integer.valueOf(childAt.getId()))) {
                childAt.setVisibility(8);
            }
            Float f = this.originalViewAlphas.get(Integer.valueOf(childAt.getId()));
            childAt.setAlpha(f != null ? f.floatValue() : 1.0f);
        }
        constraintSet.applyTo(this);
    }

    private final void ensureViewsNotGone() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
            childAt.setVisibility(!(widgetState != null && !widgetState.getGone()) ? 4 : 0);
        }
    }

    public final void setState(TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(transitionViewState, AuthDialog.KEY_BIOMETRIC_STATE);
        this.currentState = transitionViewState;
        applyCurrentState();
    }
}
