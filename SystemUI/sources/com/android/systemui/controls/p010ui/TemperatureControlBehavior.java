package com.android.systemui.controls.p010ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.view.View;
import com.android.systemui.C1894R;
import com.android.systemui.controls.p010ui.ControlViewHolder;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0010\u0010 \u001a\u00020\u001b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0001X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019¨\u0006!"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/TemperatureControlBehavior;", "Lcom/android/systemui/controls/ui/Behavior;", "()V", "clipLayer", "Landroid/graphics/drawable/Drawable;", "getClipLayer", "()Landroid/graphics/drawable/Drawable;", "setClipLayer", "(Landroid/graphics/drawable/Drawable;)V", "control", "Landroid/service/controls/Control;", "getControl", "()Landroid/service/controls/Control;", "setControl", "(Landroid/service/controls/Control;)V", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "getCvh", "()Lcom/android/systemui/controls/ui/ControlViewHolder;", "setCvh", "(Lcom/android/systemui/controls/ui/ControlViewHolder;)V", "subBehavior", "getSubBehavior", "()Lcom/android/systemui/controls/ui/Behavior;", "setSubBehavior", "(Lcom/android/systemui/controls/ui/Behavior;)V", "bind", "", "cws", "Lcom/android/systemui/controls/ui/ControlWithState;", "colorOffset", "", "initialize", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.TemperatureControlBehavior */
/* compiled from: TemperatureControlBehavior.kt */
public final class TemperatureControlBehavior implements Behavior {
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    private Behavior subBehavior;

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

    public final Behavior getSubBehavior() {
        return this.subBehavior;
    }

    public final void setSubBehavior(Behavior behavior) {
        this.subBehavior = behavior;
    }

    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
    }

    public void bind(ControlWithState controlWithState, int i) {
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        ControlViewHolder cvh2 = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        int i2 = 0;
        ControlViewHolder.setStatusText$default(cvh2, statusText, false, 2, (Object) null);
        Drawable background = getCvh().getLayout().getBackground();
        if (background != null) {
            Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(C1894R.C1898id.clip_layer);
            Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
            setClipLayer(findDrawableByLayerId);
            TemperatureControlTemplate controlTemplate = getControl().getControlTemplate();
            if (controlTemplate != null) {
                TemperatureControlTemplate temperatureControlTemplate = controlTemplate;
                int currentActiveMode = temperatureControlTemplate.getCurrentActiveMode();
                ControlTemplate template = temperatureControlTemplate.getTemplate();
                if (Intrinsics.areEqual((Object) template, (Object) ControlTemplate.getNoTemplateObject()) || Intrinsics.areEqual((Object) template, (Object) ControlTemplate.getErrorTemplate())) {
                    boolean z = (currentActiveMode == 0 || currentActiveMode == 1) ? false : true;
                    Drawable clipLayer2 = getClipLayer();
                    if (z) {
                        i2 = 10000;
                    }
                    clipLayer2.setLevel(i2);
                    ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(getCvh(), z, currentActiveMode, false, 4, (Object) null);
                    getCvh().getLayout().setOnClickListener(new TemperatureControlBehavior$$ExternalSyntheticLambda0(this, temperatureControlTemplate));
                    return;
                }
                ControlViewHolder cvh3 = getCvh();
                Behavior behavior = this.subBehavior;
                ControlViewHolder.Companion companion = ControlViewHolder.Companion;
                int status = getControl().getStatus();
                Intrinsics.checkNotNullExpressionValue(template, "subTemplate");
                this.subBehavior = cvh3.bindBehavior(behavior, companion.findBehaviorClass(status, template, getControl().getDeviceType()), currentActiveMode);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.service.controls.templates.TemperatureControlTemplate");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    /* access modifiers changed from: private */
    /* renamed from: bind$lambda-0  reason: not valid java name */
    public static final void m2728bind$lambda0(TemperatureControlBehavior temperatureControlBehavior, TemperatureControlTemplate temperatureControlTemplate, View view) {
        Intrinsics.checkNotNullParameter(temperatureControlBehavior, "this$0");
        Intrinsics.checkNotNullParameter(temperatureControlTemplate, "$template");
        ControlActionCoordinator controlActionCoordinator = temperatureControlBehavior.getCvh().getControlActionCoordinator();
        ControlViewHolder cvh2 = temperatureControlBehavior.getCvh();
        String templateId = temperatureControlTemplate.getTemplateId();
        Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
        controlActionCoordinator.touch(cvh2, templateId, temperatureControlBehavior.getControl());
    }
}
