package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.RangeTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ToggleRangeTemplate;
import android.util.Log;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ToggleRangeBehavior.kt */
/* loaded from: classes.dex */
public final class ToggleRangeBehavior implements Behavior {
    @NotNull
    public static final Companion Companion = new Companion(null);
    public Drawable clipLayer;
    private int colorOffset;
    public Context context;
    public Control control;
    public ControlViewHolder cvh;
    private boolean isChecked;
    private boolean isToggleable;
    @Nullable
    private ValueAnimator rangeAnimator;
    public RangeTemplate rangeTemplate;
    public String templateId;
    @NotNull
    private CharSequence currentStatusText = "";
    @NotNull
    private String currentRangeValue = "";

    @NotNull
    public final Drawable getClipLayer() {
        Drawable drawable = this.clipLayer;
        if (drawable != null) {
            return drawable;
        }
        Intrinsics.throwUninitializedPropertyAccessException("clipLayer");
        throw null;
    }

    public final void setClipLayer(@NotNull Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "<set-?>");
        this.clipLayer = drawable;
    }

    @NotNull
    public final String getTemplateId() {
        String str = this.templateId;
        if (str != null) {
            return str;
        }
        Intrinsics.throwUninitializedPropertyAccessException("templateId");
        throw null;
    }

    public final void setTemplateId(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.templateId = str;
    }

    @NotNull
    public final Control getControl() {
        Control control = this.control;
        if (control != null) {
            return control;
        }
        Intrinsics.throwUninitializedPropertyAccessException("control");
        throw null;
    }

    public final void setControl(@NotNull Control control) {
        Intrinsics.checkNotNullParameter(control, "<set-?>");
        this.control = control;
    }

    @NotNull
    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cvh");
        throw null;
    }

    public final void setCvh(@NotNull ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "<set-?>");
        this.cvh = controlViewHolder;
    }

    @NotNull
    public final RangeTemplate getRangeTemplate() {
        RangeTemplate rangeTemplate = this.rangeTemplate;
        if (rangeTemplate != null) {
            return rangeTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("rangeTemplate");
        throw null;
    }

    public final void setRangeTemplate(@NotNull RangeTemplate rangeTemplate) {
        Intrinsics.checkNotNullParameter(rangeTemplate, "<set-?>");
        this.rangeTemplate = rangeTemplate;
    }

    @NotNull
    public final Context getContext() {
        Context context = this.context;
        if (context != null) {
            return context;
        }
        Intrinsics.throwUninitializedPropertyAccessException("context");
        throw null;
    }

    public final void setContext(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "<set-?>");
        this.context = context;
    }

    public final boolean isChecked() {
        return this.isChecked;
    }

    public final boolean isToggleable() {
        return this.isToggleable;
    }

    /* compiled from: ToggleRangeBehavior.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(@NotNull ControlViewHolder cvh) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        setCvh(cvh);
        setContext(cvh.getContext());
        final ToggleRangeGestureListener toggleRangeGestureListener = new ToggleRangeGestureListener(this, cvh.getLayout());
        final GestureDetector gestureDetector = new GestureDetector(getContext(), toggleRangeGestureListener);
        cvh.getLayout().setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$initialize$1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(@NotNull View v, @NotNull MotionEvent e) {
                Intrinsics.checkNotNullParameter(v, "v");
                Intrinsics.checkNotNullParameter(e, "e");
                if (!gestureDetector.onTouchEvent(e) && e.getAction() == 1 && toggleRangeGestureListener.isDragging()) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    toggleRangeGestureListener.setDragging(false);
                    this.endUpdateRange();
                }
                return false;
            }
        });
    }

    private final void setup(ToggleRangeTemplate toggleRangeTemplate) {
        RangeTemplate range = toggleRangeTemplate.getRange();
        Intrinsics.checkNotNullExpressionValue(range, "template.getRange()");
        setRangeTemplate(range);
        this.isToggleable = true;
        this.isChecked = toggleRangeTemplate.isChecked();
    }

    private final void setup(RangeTemplate rangeTemplate) {
        setRangeTemplate(rangeTemplate);
        this.isChecked = !(getRangeTemplate().getCurrentValue() == getRangeTemplate().getMinValue());
    }

    private final boolean setupTemplate(ControlTemplate controlTemplate) {
        if (controlTemplate instanceof ToggleRangeTemplate) {
            setup((ToggleRangeTemplate) controlTemplate);
            return true;
        } else if (controlTemplate instanceof RangeTemplate) {
            setup((RangeTemplate) controlTemplate);
            return true;
        } else if (!(controlTemplate instanceof TemperatureControlTemplate)) {
            Log.e("ControlsUiController", Intrinsics.stringPlus("Unsupported template type: ", controlTemplate));
            return false;
        } else {
            ControlTemplate template = ((TemperatureControlTemplate) controlTemplate).getTemplate();
            Intrinsics.checkNotNullExpressionValue(template, "template.getTemplate()");
            return setupTemplate(template);
        }
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(@NotNull ControlWithState cws, int i) {
        Intrinsics.checkNotNullParameter(cws, "cws");
        Control control = cws.getControl();
        Intrinsics.checkNotNull(control);
        setControl(control);
        this.colorOffset = i;
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        this.currentStatusText = statusText;
        getCvh().getLayout().setOnLongClickListener(null);
        Drawable background = getCvh().getLayout().getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
        setClipLayer(findDrawableByLayerId);
        ControlTemplate template = getControl().getControlTemplate();
        Intrinsics.checkNotNullExpressionValue(template, "template");
        if (!setupTemplate(template)) {
            return;
        }
        String templateId = template.getTemplateId();
        Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
        setTemplateId(templateId);
        updateRange(rangeToLevelValue(getRangeTemplate().getCurrentValue()), this.isChecked, false);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), this.isChecked, i, false, 4, null);
        getCvh().getLayout().setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$bind$1
            @Override // android.view.View.AccessibilityDelegate
            public boolean onRequestSendAccessibilityEvent(@NotNull ViewGroup host, @NotNull View child, @NotNull AccessibilityEvent event) {
                Intrinsics.checkNotNullParameter(host, "host");
                Intrinsics.checkNotNullParameter(child, "child");
                Intrinsics.checkNotNullParameter(event, "event");
                return true;
            }

            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(@NotNull View host, @NotNull AccessibilityNodeInfo info) {
                float levelToRangeValue;
                float levelToRangeValue2;
                float levelToRangeValue3;
                Intrinsics.checkNotNullParameter(host, "host");
                Intrinsics.checkNotNullParameter(info, "info");
                super.onInitializeAccessibilityNodeInfo(host, info);
                int i2 = 0;
                levelToRangeValue = ToggleRangeBehavior.this.levelToRangeValue(0);
                ToggleRangeBehavior toggleRangeBehavior = ToggleRangeBehavior.this;
                levelToRangeValue2 = toggleRangeBehavior.levelToRangeValue(toggleRangeBehavior.getClipLayer().getLevel());
                levelToRangeValue3 = ToggleRangeBehavior.this.levelToRangeValue(10000);
                double stepValue = ToggleRangeBehavior.this.getRangeTemplate().getStepValue();
                if (stepValue == Math.floor(stepValue)) {
                    i2 = 1;
                }
                int i3 = i2 ^ 1;
                if (ToggleRangeBehavior.this.isChecked()) {
                    info.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(i3, levelToRangeValue, levelToRangeValue3, levelToRangeValue2));
                }
                info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
            }

            @Override // android.view.View.AccessibilityDelegate
            public boolean performAccessibilityAction(@NotNull View host, int i2, @Nullable Bundle bundle) {
                int rangeToLevelValue;
                boolean z;
                Intrinsics.checkNotNullParameter(host, "host");
                if (i2 == 16) {
                    if (ToggleRangeBehavior.this.isToggleable()) {
                        ToggleRangeBehavior.this.getCvh().getControlActionCoordinator().toggle(ToggleRangeBehavior.this.getCvh(), ToggleRangeBehavior.this.getTemplateId(), ToggleRangeBehavior.this.isChecked());
                        z = true;
                    }
                    z = false;
                } else {
                    if (i2 == 32) {
                        ToggleRangeBehavior.this.getCvh().getControlActionCoordinator().longPress(ToggleRangeBehavior.this.getCvh());
                    } else {
                        if (i2 == AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS.getId() && bundle != null && bundle.containsKey("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE")) {
                            rangeToLevelValue = ToggleRangeBehavior.this.rangeToLevelValue(bundle.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"));
                            ToggleRangeBehavior toggleRangeBehavior = ToggleRangeBehavior.this;
                            toggleRangeBehavior.updateRange(rangeToLevelValue, toggleRangeBehavior.isChecked(), true);
                            ToggleRangeBehavior.this.endUpdateRange();
                        }
                        z = false;
                    }
                    z = true;
                }
                return z || super.performAccessibilityAction(host, i2, bundle);
            }
        });
    }

    public final void beginUpdateRange() {
        getCvh().setUserInteractionInProgress(true);
        getCvh().setStatusTextSize(getContext().getResources().getDimensionPixelSize(R$dimen.control_status_expanded));
    }

    public final void updateRange(int i, boolean z, boolean z2) {
        int max = Math.max(0, Math.min(10000, i));
        if (getClipLayer().getLevel() == 0 && max > 0) {
            getCvh().applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(z, this.colorOffset, false);
        }
        ValueAnimator valueAnimator = this.rangeAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (z2) {
            boolean z3 = max == 0 || max == 10000;
            if (getClipLayer().getLevel() != max) {
                getCvh().getControlActionCoordinator().drag(z3);
                getClipLayer().setLevel(max);
            }
        } else if (max != getClipLayer().getLevel()) {
            ValueAnimator ofInt = ValueAnimator.ofInt(getCvh().getClipLayer().getLevel(), max);
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$updateRange$1$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ClipDrawable clipLayer = ToggleRangeBehavior.this.getCvh().getClipLayer();
                    Object animatedValue = valueAnimator2.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                    clipLayer.setLevel(((Integer) animatedValue).intValue());
                }
            });
            ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$updateRange$1$2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(@Nullable Animator animator) {
                    ToggleRangeBehavior.this.rangeAnimator = null;
                }
            });
            ofInt.setDuration(700L);
            ofInt.setInterpolator(Interpolators.CONTROL_STATE);
            ofInt.start();
            Unit unit = Unit.INSTANCE;
            this.rangeAnimator = ofInt;
        }
        if (z) {
            this.currentRangeValue = format(getRangeTemplate().getFormatString().toString(), "%.1f", levelToRangeValue(max));
            if (z2) {
                getCvh().setStatusText(this.currentRangeValue, true);
                return;
            }
            ControlViewHolder cvh = getCvh();
            ControlViewHolder.setStatusText$default(cvh, ((Object) this.currentStatusText) + ' ' + this.currentRangeValue, false, 2, null);
            return;
        }
        ControlViewHolder.setStatusText$default(getCvh(), this.currentStatusText, false, 2, null);
    }

    private final String format(String str, String str2, float f) {
        try {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String format = String.format(str, Arrays.copyOf(new Object[]{Float.valueOf(findNearestStep(f))}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            return format;
        } catch (IllegalFormatException e) {
            Log.w("ControlsUiController", "Illegal format in range template", e);
            return Intrinsics.areEqual(str2, "") ? "" : format(str2, "", f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final float levelToRangeValue(int i) {
        return MathUtils.constrainedMap(getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), 0.0f, 10000.0f, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int rangeToLevelValue(float f) {
        return (int) MathUtils.constrainedMap(0.0f, 10000.0f, getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), f);
    }

    public final void endUpdateRange() {
        getCvh().setStatusTextSize(getContext().getResources().getDimensionPixelSize(R$dimen.control_status_normal));
        ControlViewHolder cvh = getCvh();
        cvh.setStatusText(((Object) this.currentStatusText) + ' ' + this.currentRangeValue, true);
        ControlActionCoordinator controlActionCoordinator = getCvh().getControlActionCoordinator();
        ControlViewHolder cvh2 = getCvh();
        String templateId = getRangeTemplate().getTemplateId();
        Intrinsics.checkNotNullExpressionValue(templateId, "rangeTemplate.getTemplateId()");
        controlActionCoordinator.setValue(cvh2, templateId, findNearestStep(levelToRangeValue(getClipLayer().getLevel())));
        getCvh().setUserInteractionInProgress(false);
    }

    public final float findNearestStep(float f) {
        float minValue = getRangeTemplate().getMinValue();
        float f2 = Float.MAX_VALUE;
        while (minValue <= getRangeTemplate().getMaxValue()) {
            float abs = Math.abs(f - minValue);
            if (abs >= f2) {
                return minValue - getRangeTemplate().getStepValue();
            }
            minValue += getRangeTemplate().getStepValue();
            f2 = abs;
        }
        return getRangeTemplate().getMaxValue();
    }

    /* compiled from: ToggleRangeBehavior.kt */
    /* loaded from: classes.dex */
    public final class ToggleRangeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean isDragging;
        final /* synthetic */ ToggleRangeBehavior this$0;
        @NotNull
        private final View v;

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(@NotNull MotionEvent e) {
            Intrinsics.checkNotNullParameter(e, "e");
            return true;
        }

        public ToggleRangeGestureListener(@NotNull ToggleRangeBehavior this$0, View v) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(v, "v");
            this.this$0 = this$0;
            this.v = v;
        }

        public final boolean isDragging() {
            return this.isDragging;
        }

        public final void setDragging(boolean z) {
            this.isDragging = z;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(@NotNull MotionEvent e) {
            Intrinsics.checkNotNullParameter(e, "e");
            if (this.isDragging) {
                return;
            }
            this.this$0.getCvh().getControlActionCoordinator().longPress(this.this$0.getCvh());
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(@NotNull MotionEvent e1, @NotNull MotionEvent e2, float f, float f2) {
            Intrinsics.checkNotNullParameter(e1, "e1");
            Intrinsics.checkNotNullParameter(e2, "e2");
            if (!this.isDragging) {
                this.v.getParent().requestDisallowInterceptTouchEvent(true);
                this.this$0.beginUpdateRange();
                this.isDragging = true;
            }
            ToggleRangeBehavior toggleRangeBehavior = this.this$0;
            toggleRangeBehavior.updateRange(toggleRangeBehavior.getClipLayer().getLevel() + ((int) (10000 * ((-f) / this.v.getWidth()))), true, true);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(@NotNull MotionEvent e) {
            Intrinsics.checkNotNullParameter(e, "e");
            if (!this.this$0.isToggleable()) {
                return false;
            }
            this.this$0.getCvh().getControlActionCoordinator().toggle(this.this$0.getCvh(), this.this$0.getTemplateId(), this.this$0.isChecked());
            return true;
        }
    }
}
