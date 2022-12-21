package com.android.systemui.controls.p010ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.StatelessTemplate;
import android.view.View;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 (2\u00020\u0001:\u0001(B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u001aH\u0016J\u0010\u0010'\u001a\u00020#2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u00168BX\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u00020\u001dX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!¨\u0006)"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/TouchBehavior;", "Lcom/android/systemui/controls/ui/Behavior;", "()V", "clipLayer", "Landroid/graphics/drawable/Drawable;", "getClipLayer", "()Landroid/graphics/drawable/Drawable;", "setClipLayer", "(Landroid/graphics/drawable/Drawable;)V", "control", "Landroid/service/controls/Control;", "getControl", "()Landroid/service/controls/Control;", "setControl", "(Landroid/service/controls/Control;)V", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "getCvh", "()Lcom/android/systemui/controls/ui/ControlViewHolder;", "setCvh", "(Lcom/android/systemui/controls/ui/ControlViewHolder;)V", "enabled", "", "getEnabled", "()Z", "lastColorOffset", "", "statelessTouch", "template", "Landroid/service/controls/templates/ControlTemplate;", "getTemplate", "()Landroid/service/controls/templates/ControlTemplate;", "setTemplate", "(Landroid/service/controls/templates/ControlTemplate;)V", "bind", "", "cws", "Lcom/android/systemui/controls/ui/ControlWithState;", "colorOffset", "initialize", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.TouchBehavior */
/* compiled from: TouchBehavior.kt */
public final class TouchBehavior implements Behavior {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final long STATELESS_ENABLE_TIMEOUT_IN_MILLIS = 3000;
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    private int lastColorOffset;
    private boolean statelessTouch;
    public ControlTemplate template;

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

    public final ControlTemplate getTemplate() {
        ControlTemplate controlTemplate = this.template;
        if (controlTemplate != null) {
            return controlTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("template");
        return null;
    }

    public final void setTemplate(ControlTemplate controlTemplate) {
        Intrinsics.checkNotNullParameter(controlTemplate, "<set-?>");
        this.template = controlTemplate;
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

    private final boolean getEnabled() {
        return this.lastColorOffset > 0 || this.statelessTouch;
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/TouchBehavior$Companion;", "", "()V", "STATELESS_ENABLE_TIMEOUT_IN_MILLIS", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.controls.ui.TouchBehavior$Companion */
    /* compiled from: TouchBehavior.kt */
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
        controlViewHolder.getLayout().setOnClickListener(new TouchBehavior$$ExternalSyntheticLambda0(controlViewHolder, this));
    }

    /* access modifiers changed from: private */
    /* renamed from: initialize$lambda-1  reason: not valid java name */
    public static final void m2733initialize$lambda1(ControlViewHolder controlViewHolder, TouchBehavior touchBehavior, View view) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        Intrinsics.checkNotNullParameter(touchBehavior, "this$0");
        ControlActionCoordinator controlActionCoordinator = controlViewHolder.getControlActionCoordinator();
        String templateId = touchBehavior.getTemplate().getTemplateId();
        Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
        controlActionCoordinator.touch(controlViewHolder, templateId, touchBehavior.getControl());
        if (touchBehavior.getTemplate() instanceof StatelessTemplate) {
            touchBehavior.statelessTouch = true;
            ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(controlViewHolder, touchBehavior.getEnabled(), touchBehavior.lastColorOffset, false, 4, (Object) null);
            controlViewHolder.getUiExecutor().executeDelayed(new TouchBehavior$$ExternalSyntheticLambda1(touchBehavior, controlViewHolder), 3000);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: initialize$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2734initialize$lambda1$lambda0(TouchBehavior touchBehavior, ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(touchBehavior, "this$0");
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        touchBehavior.statelessTouch = false;
        ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(controlViewHolder, touchBehavior.getEnabled(), touchBehavior.lastColorOffset, false, 4, (Object) null);
    }

    public void bind(ControlWithState controlWithState, int i) {
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        this.lastColorOffset = i;
        ControlViewHolder cvh2 = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        int i2 = 0;
        ControlViewHolder.setStatusText$default(cvh2, statusText, false, 2, (Object) null);
        ControlTemplate controlTemplate = getControl().getControlTemplate();
        Intrinsics.checkNotNullExpressionValue(controlTemplate, "control.getControlTemplate()");
        setTemplate(controlTemplate);
        Drawable background = getCvh().getLayout().getBackground();
        if (background != null) {
            Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(C1893R.C1897id.clip_layer);
            Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
            setClipLayer(findDrawableByLayerId);
            Drawable clipLayer2 = getClipLayer();
            if (getEnabled()) {
                i2 = 10000;
            }
            clipLayer2.setLevel(i2);
            ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(getCvh(), getEnabled(), i, false, 4, (Object) null);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }
}
