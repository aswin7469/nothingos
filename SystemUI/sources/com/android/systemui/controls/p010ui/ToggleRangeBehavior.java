package com.android.systemui.controls.p010ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.icu.text.DateFormat;
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
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import java.util.Arrays;
import java.util.IllegalFormatException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 X2\u00020\u0001:\u0002XYB\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010?\u001a\u00020@J\u0018\u0010A\u001a\u00020@2\u0006\u0010B\u001a\u00020C2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0006\u0010D\u001a\u00020@J\u000e\u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020FJ \u0010H\u001a\u00020\u001c2\u0006\u0010I\u001a\u00020\u001c2\u0006\u0010J\u001a\u00020\u001c2\u0006\u0010G\u001a\u00020FH\u0002J\u0010\u0010K\u001a\u00020@2\u0006\u0010'\u001a\u00020(H\u0016J\u0010\u0010L\u001a\u00020F2\u0006\u0010M\u001a\u00020\nH\u0002J\u0010\u0010N\u001a\u00020\n2\u0006\u0010M\u001a\u00020FH\u0002J\u0010\u0010O\u001a\u00020@2\u0006\u0010P\u001a\u000207H\u0002J\u0010\u0010O\u001a\u00020@2\u0006\u0010P\u001a\u00020QH\u0002J\u0010\u0010R\u001a\u00020.2\u0006\u0010P\u001a\u00020SH\u0002J\u001e\u0010T\u001a\u00020@2\u0006\u0010U\u001a\u00020\n2\u0006\u0010V\u001a\u00020.2\u0006\u0010W\u001a\u00020.R\u001a\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0016X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001b\u001a\u00020\u001cX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001a\u0010!\u001a\u00020\"X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001a\u0010'\u001a\u00020(X.¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u001a\u0010-\u001a\u00020.X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010/\"\u0004\b0\u00101R\u001a\u00102\u001a\u00020.X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u0010/\"\u0004\b3\u00101R\u0010\u00104\u001a\u0004\u0018\u000105X\u000e¢\u0006\u0002\n\u0000R\u001a\u00106\u001a\u000207X.¢\u0006\u000e\n\u0000\u001a\u0004\b8\u00109\"\u0004\b:\u0010;R\u001a\u0010<\u001a\u00020\u001cX.¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\u001e\"\u0004\b>\u0010 ¨\u0006Z"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ToggleRangeBehavior;", "Lcom/android/systemui/controls/ui/Behavior;", "()V", "clipLayer", "Landroid/graphics/drawable/Drawable;", "getClipLayer", "()Landroid/graphics/drawable/Drawable;", "setClipLayer", "(Landroid/graphics/drawable/Drawable;)V", "colorOffset", "", "getColorOffset", "()I", "setColorOffset", "(I)V", "context", "Landroid/content/Context;", "getContext", "()Landroid/content/Context;", "setContext", "(Landroid/content/Context;)V", "control", "Landroid/service/controls/Control;", "getControl", "()Landroid/service/controls/Control;", "setControl", "(Landroid/service/controls/Control;)V", "currentRangeValue", "", "getCurrentRangeValue", "()Ljava/lang/String;", "setCurrentRangeValue", "(Ljava/lang/String;)V", "currentStatusText", "", "getCurrentStatusText", "()Ljava/lang/CharSequence;", "setCurrentStatusText", "(Ljava/lang/CharSequence;)V", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "getCvh", "()Lcom/android/systemui/controls/ui/ControlViewHolder;", "setCvh", "(Lcom/android/systemui/controls/ui/ControlViewHolder;)V", "isChecked", "", "()Z", "setChecked", "(Z)V", "isToggleable", "setToggleable", "rangeAnimator", "Landroid/animation/ValueAnimator;", "rangeTemplate", "Landroid/service/controls/templates/RangeTemplate;", "getRangeTemplate", "()Landroid/service/controls/templates/RangeTemplate;", "setRangeTemplate", "(Landroid/service/controls/templates/RangeTemplate;)V", "templateId", "getTemplateId", "setTemplateId", "beginUpdateRange", "", "bind", "cws", "Lcom/android/systemui/controls/ui/ControlWithState;", "endUpdateRange", "findNearestStep", "", "value", "format", "primaryFormat", "backupFormat", "initialize", "levelToRangeValue", "i", "rangeToLevelValue", "setup", "template", "Landroid/service/controls/templates/ToggleRangeTemplate;", "setupTemplate", "Landroid/service/controls/templates/ControlTemplate;", "updateRange", "level", "checked", "isDragging", "Companion", "ToggleRangeGestureListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ToggleRangeBehavior */
/* compiled from: ToggleRangeBehavior.kt */
public final class ToggleRangeBehavior implements Behavior {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String DEFAULT_FORMAT = "%.1f";
    public Drawable clipLayer;
    private int colorOffset;
    public Context context;
    public Control control;
    private String currentRangeValue = "";
    private CharSequence currentStatusText = "";
    public ControlViewHolder cvh;
    private boolean isChecked;
    private boolean isToggleable;
    /* access modifiers changed from: private */
    public ValueAnimator rangeAnimator;
    public RangeTemplate rangeTemplate;
    public String templateId;

    public final Drawable getClipLayer() {
        Drawable drawable = this.clipLayer;
        if (drawable != null) {
            return drawable;
        }
        Intrinsics.throwUninitializedPropertyAccessException("clipLayer");
        return null;
    }

    public final void setClipLayer(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "<set-?>");
        this.clipLayer = drawable;
    }

    public final String getTemplateId() {
        String str = this.templateId;
        if (str != null) {
            return str;
        }
        Intrinsics.throwUninitializedPropertyAccessException("templateId");
        return null;
    }

    public final void setTemplateId(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.templateId = str;
    }

    public final Control getControl() {
        Control control2 = this.control;
        if (control2 != null) {
            return control2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("control");
        return null;
    }

    public final void setControl(Control control2) {
        Intrinsics.checkNotNullParameter(control2, "<set-?>");
        this.control = control2;
    }

    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cvh");
        return null;
    }

    public final void setCvh(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "<set-?>");
        this.cvh = controlViewHolder;
    }

    public final RangeTemplate getRangeTemplate() {
        RangeTemplate rangeTemplate2 = this.rangeTemplate;
        if (rangeTemplate2 != null) {
            return rangeTemplate2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("rangeTemplate");
        return null;
    }

    public final void setRangeTemplate(RangeTemplate rangeTemplate2) {
        Intrinsics.checkNotNullParameter(rangeTemplate2, "<set-?>");
        this.rangeTemplate = rangeTemplate2;
    }

    public final Context getContext() {
        Context context2 = this.context;
        if (context2 != null) {
            return context2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("context");
        return null;
    }

    public final void setContext(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "<set-?>");
        this.context = context2;
    }

    public final CharSequence getCurrentStatusText() {
        return this.currentStatusText;
    }

    public final void setCurrentStatusText(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "<set-?>");
        this.currentStatusText = charSequence;
    }

    public final String getCurrentRangeValue() {
        return this.currentRangeValue;
    }

    public final void setCurrentRangeValue(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.currentRangeValue = str;
    }

    public final boolean isChecked() {
        return this.isChecked;
    }

    public final void setChecked(boolean z) {
        this.isChecked = z;
    }

    public final boolean isToggleable() {
        return this.isToggleable;
    }

    public final void setToggleable(boolean z) {
        this.isToggleable = z;
    }

    public final int getColorOffset() {
        return this.colorOffset;
    }

    public final void setColorOffset(int i) {
        this.colorOffset = i;
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ToggleRangeBehavior$Companion;", "", "()V", "DEFAULT_FORMAT", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.controls.ui.ToggleRangeBehavior$Companion */
    /* compiled from: ToggleRangeBehavior.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
        setContext(controlViewHolder.getContext());
        ToggleRangeGestureListener toggleRangeGestureListener = new ToggleRangeGestureListener(this, controlViewHolder.getLayout());
        controlViewHolder.getLayout().setOnTouchListener(new ToggleRangeBehavior$$ExternalSyntheticLambda1(new GestureDetector(getContext(), toggleRangeGestureListener), toggleRangeGestureListener, this));
    }

    /* access modifiers changed from: private */
    /* renamed from: initialize$lambda-0  reason: not valid java name */
    public static final boolean m2731initialize$lambda0(GestureDetector gestureDetector, ToggleRangeGestureListener toggleRangeGestureListener, ToggleRangeBehavior toggleRangeBehavior, View view, MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(gestureDetector, "$gestureDetector");
        Intrinsics.checkNotNullParameter(toggleRangeGestureListener, "$gestureListener");
        Intrinsics.checkNotNullParameter(toggleRangeBehavior, "this$0");
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        Intrinsics.checkNotNullParameter(motionEvent, "e");
        if (!gestureDetector.onTouchEvent(motionEvent) && motionEvent.getAction() == 1 && toggleRangeGestureListener.isDragging()) {
            view.getParent().requestDisallowInterceptTouchEvent(false);
            toggleRangeGestureListener.setDragging(false);
            toggleRangeBehavior.endUpdateRange();
        }
        return false;
    }

    private final void setup(ToggleRangeTemplate toggleRangeTemplate) {
        RangeTemplate range = toggleRangeTemplate.getRange();
        Intrinsics.checkNotNullExpressionValue(range, "template.getRange()");
        setRangeTemplate(range);
        this.isToggleable = true;
        this.isChecked = toggleRangeTemplate.isChecked();
    }

    private final void setup(RangeTemplate rangeTemplate2) {
        setRangeTemplate(rangeTemplate2);
        this.isChecked = !(getRangeTemplate().getCurrentValue() == getRangeTemplate().getMinValue());
    }

    private final boolean setupTemplate(ControlTemplate controlTemplate) {
        if (controlTemplate instanceof ToggleRangeTemplate) {
            setup((ToggleRangeTemplate) controlTemplate);
            return true;
        } else if (controlTemplate instanceof RangeTemplate) {
            setup((RangeTemplate) controlTemplate);
            return true;
        } else if (controlTemplate instanceof TemperatureControlTemplate) {
            ControlTemplate template = ((TemperatureControlTemplate) controlTemplate).getTemplate();
            Intrinsics.checkNotNullExpressionValue(template, "template.getTemplate()");
            return setupTemplate(template);
        } else {
            Log.e("ControlsUiController", "Unsupported template type: " + controlTemplate);
            return false;
        }
    }

    public void bind(ControlWithState controlWithState, int i) {
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        this.colorOffset = i;
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        this.currentStatusText = statusText;
        getCvh().getLayout().setOnLongClickListener((View.OnLongClickListener) null);
        Drawable background = getCvh().getLayout().getBackground();
        if (background != null) {
            Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(C1893R.C1897id.clip_layer);
            Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
            setClipLayer(findDrawableByLayerId);
            ControlTemplate controlTemplate = getControl().getControlTemplate();
            Intrinsics.checkNotNullExpressionValue(controlTemplate, "template");
            if (setupTemplate(controlTemplate)) {
                String templateId2 = controlTemplate.getTemplateId();
                Intrinsics.checkNotNullExpressionValue(templateId2, "template.getTemplateId()");
                setTemplateId(templateId2);
                updateRange(rangeToLevelValue(getRangeTemplate().getCurrentValue()), this.isChecked, false);
                ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(getCvh(), this.isChecked, i, false, 4, (Object) null);
                getCvh().getLayout().setAccessibilityDelegate(new ToggleRangeBehavior$bind$1(this));
                return;
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    public final void beginUpdateRange() {
        getCvh().setUserInteractionInProgress(true);
        getCvh().setStatusTextSize((float) getContext().getResources().getDimensionPixelSize(C1893R.dimen.control_status_expanded));
    }

    public final void updateRange(int i, boolean z, boolean z2) {
        int max = Math.max(0, Math.min(10000, i));
        if (getClipLayer().getLevel() == 0 && max > 0) {
            getCvh().applyRenderInfo$SystemUI_nothingRelease(z, this.colorOffset, false);
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
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{getCvh().getClipLayer().getLevel(), max});
            ofInt.addUpdateListener(new ToggleRangeBehavior$$ExternalSyntheticLambda0(this));
            ofInt.addListener(new ToggleRangeBehavior$updateRange$1$2(this));
            ofInt.setDuration(700);
            ofInt.setInterpolator(Interpolators.CONTROL_STATE);
            ofInt.start();
            this.rangeAnimator = ofInt;
        }
        if (z) {
            this.currentRangeValue = format(getRangeTemplate().getFormatString().toString(), DEFAULT_FORMAT, levelToRangeValue(max));
            if (z2) {
                getCvh().setStatusText(this.currentRangeValue, true);
            } else {
                ControlViewHolder.setStatusText$default(getCvh(), this.currentStatusText + ' ' + this.currentRangeValue, false, 2, (Object) null);
            }
        } else {
            ControlViewHolder.setStatusText$default(getCvh(), this.currentStatusText, false, 2, (Object) null);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateRange$lambda-2$lambda-1  reason: not valid java name */
    public static final void m2732updateRange$lambda2$lambda1(ToggleRangeBehavior toggleRangeBehavior, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(toggleRangeBehavior, "this$0");
        ClipDrawable clipLayer2 = toggleRangeBehavior.getCvh().getClipLayer();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            clipLayer2.setLevel(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    private final String format(String str, String str2, float f) {
        try {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String format = String.format(str, Arrays.copyOf((T[]) new Object[]{Float.valueOf(findNearestStep(f))}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "format(format, *args)");
            return format;
        } catch (IllegalFormatException e) {
            Log.w("ControlsUiController", "Illegal format in range template", e);
            if (Intrinsics.areEqual((Object) str2, (Object) "")) {
                return "";
            }
            return format(str2, "", f);
        }
    }

    /* access modifiers changed from: private */
    public final float levelToRangeValue(int i) {
        return MathUtils.constrainedMap(getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), 0.0f, 10000.0f, (float) i);
    }

    /* access modifiers changed from: private */
    public final int rangeToLevelValue(float f) {
        return (int) MathUtils.constrainedMap(0.0f, 10000.0f, getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), f);
    }

    public final void endUpdateRange() {
        getCvh().setStatusTextSize((float) getContext().getResources().getDimensionPixelSize(C1893R.dimen.control_status_normal));
        getCvh().setStatusText(this.currentStatusText + ' ' + this.currentRangeValue, true);
        ControlActionCoordinator controlActionCoordinator = getCvh().getControlActionCoordinator();
        ControlViewHolder cvh2 = getCvh();
        String templateId2 = getRangeTemplate().getTemplateId();
        Intrinsics.checkNotNullExpressionValue(templateId2, "rangeTemplate.getTemplateId()");
        controlActionCoordinator.setValue(cvh2, templateId2, findNearestStep(levelToRangeValue(getClipLayer().getLevel())));
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

    @Metadata(mo64986d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u000eH\u0016J(\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0016J\u0010\u0010\u0017\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0007\"\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0018"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ToggleRangeBehavior$ToggleRangeGestureListener;", "Landroid/view/GestureDetector$SimpleOnGestureListener;", "v", "Landroid/view/View;", "(Lcom/android/systemui/controls/ui/ToggleRangeBehavior;Landroid/view/View;)V", "isDragging", "", "()Z", "setDragging", "(Z)V", "getV", "()Landroid/view/View;", "onDown", "e", "Landroid/view/MotionEvent;", "onLongPress", "", "onScroll", "e1", "e2", "xDiff", "", "yDiff", "onSingleTapUp", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.controls.ui.ToggleRangeBehavior$ToggleRangeGestureListener */
    /* compiled from: ToggleRangeBehavior.kt */
    public final class ToggleRangeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean isDragging;
        final /* synthetic */ ToggleRangeBehavior this$0;

        /* renamed from: v */
        private final View f301v;

        public boolean onDown(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "e");
            return true;
        }

        public ToggleRangeGestureListener(ToggleRangeBehavior toggleRangeBehavior, View view) {
            Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
            this.this$0 = toggleRangeBehavior;
            this.f301v = view;
        }

        public final View getV() {
            return this.f301v;
        }

        public final boolean isDragging() {
            return this.isDragging;
        }

        public final void setDragging(boolean z) {
            this.isDragging = z;
        }

        public void onLongPress(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "e");
            if (!this.isDragging) {
                this.this$0.getCvh().getControlActionCoordinator().longPress(this.this$0.getCvh());
            }
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            Intrinsics.checkNotNullParameter(motionEvent, "e1");
            Intrinsics.checkNotNullParameter(motionEvent2, "e2");
            if (!this.isDragging) {
                this.f301v.getParent().requestDisallowInterceptTouchEvent(true);
                this.this$0.beginUpdateRange();
                this.isDragging = true;
            }
            ToggleRangeBehavior toggleRangeBehavior = this.this$0;
            toggleRangeBehavior.updateRange(toggleRangeBehavior.getClipLayer().getLevel() + ((int) (((float) 10000) * ((-f) / ((float) this.f301v.getWidth())))), true, true);
            return true;
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "e");
            if (!this.this$0.isToggleable()) {
                return false;
            }
            this.this$0.getCvh().getControlActionCoordinator().toggle(this.this$0.getCvh(), this.this$0.getTemplateId(), this.this$0.isChecked());
            return true;
        }
    }
}
