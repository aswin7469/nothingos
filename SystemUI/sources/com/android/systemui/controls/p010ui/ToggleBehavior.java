package com.android.systemui.controls.p010ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ToggleTemplate;
import android.util.Log;
import android.view.View;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0010\u0010!\u001a\u00020\u001c2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0016X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001a¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ToggleBehavior;", "Lcom/android/systemui/controls/ui/Behavior;", "()V", "clipLayer", "Landroid/graphics/drawable/Drawable;", "getClipLayer", "()Landroid/graphics/drawable/Drawable;", "setClipLayer", "(Landroid/graphics/drawable/Drawable;)V", "control", "Landroid/service/controls/Control;", "getControl", "()Landroid/service/controls/Control;", "setControl", "(Landroid/service/controls/Control;)V", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "getCvh", "()Lcom/android/systemui/controls/ui/ControlViewHolder;", "setCvh", "(Lcom/android/systemui/controls/ui/ControlViewHolder;)V", "template", "Landroid/service/controls/templates/ToggleTemplate;", "getTemplate", "()Landroid/service/controls/templates/ToggleTemplate;", "setTemplate", "(Landroid/service/controls/templates/ToggleTemplate;)V", "bind", "", "cws", "Lcom/android/systemui/controls/ui/ControlWithState;", "colorOffset", "", "initialize", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ToggleBehavior */
/* compiled from: ToggleBehavior.kt */
public final class ToggleBehavior implements Behavior {
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    public ToggleTemplate template;

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

    public final ToggleTemplate getTemplate() {
        ToggleTemplate toggleTemplate = this.template;
        if (toggleTemplate != null) {
            return toggleTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("template");
        return null;
    }

    public final void setTemplate(ToggleTemplate toggleTemplate) {
        Intrinsics.checkNotNullParameter(toggleTemplate, "<set-?>");
        this.template = toggleTemplate;
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

    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
        controlViewHolder.getLayout().setOnClickListener(new ToggleBehavior$$ExternalSyntheticLambda0(controlViewHolder, this));
    }

    /* access modifiers changed from: private */
    /* renamed from: initialize$lambda-0  reason: not valid java name */
    public static final void m2735initialize$lambda0(ControlViewHolder controlViewHolder, ToggleBehavior toggleBehavior, View view) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        Intrinsics.checkNotNullParameter(toggleBehavior, "this$0");
        ControlActionCoordinator controlActionCoordinator = controlViewHolder.getControlActionCoordinator();
        String templateId = toggleBehavior.getTemplate().getTemplateId();
        Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
        controlActionCoordinator.toggle(controlViewHolder, templateId, toggleBehavior.getTemplate().isChecked());
    }

    public void bind(ControlWithState controlWithState, int i) {
        ToggleTemplate toggleTemplate;
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        ControlViewHolder cvh2 = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        ControlViewHolder.setStatusText$default(cvh2, statusText, false, 2, (Object) null);
        TemperatureControlTemplate controlTemplate = getControl().getControlTemplate();
        if (controlTemplate instanceof ToggleTemplate) {
            Intrinsics.checkNotNullExpressionValue(controlTemplate, "controlTemplate");
            toggleTemplate = (ToggleTemplate) controlTemplate;
        } else if (controlTemplate instanceof TemperatureControlTemplate) {
            ControlTemplate template2 = controlTemplate.getTemplate();
            if (template2 != null) {
                toggleTemplate = (ToggleTemplate) template2;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.service.controls.templates.ToggleTemplate");
            }
        } else {
            Log.e("ControlsUiController", "Unsupported template type: " + controlTemplate);
            return;
        }
        setTemplate(toggleTemplate);
        Drawable background = getCvh().getLayout().getBackground();
        if (background != null) {
            Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(C1894R.C1898id.clip_layer);
            Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
            setClipLayer(findDrawableByLayerId);
            getClipLayer().setLevel(10000);
            ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(getCvh(), getTemplate().isChecked(), i, false, 4, (Object) null);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }
}
