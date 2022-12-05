package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.view.View;
import com.android.systemui.R$id;
import com.android.systemui.controls.ui.ControlViewHolder;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: TemperatureControlBehavior.kt */
/* loaded from: classes.dex */
public final class TemperatureControlBehavior implements Behavior {
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    @Nullable
    private Behavior subBehavior;

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

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(@NotNull ControlViewHolder cvh) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        setCvh(cvh);
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(@NotNull ControlWithState cws, int i) {
        Intrinsics.checkNotNullParameter(cws, "cws");
        Control control = cws.getControl();
        Intrinsics.checkNotNull(control);
        setControl(control);
        ControlViewHolder cvh = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        int i2 = 0;
        ControlViewHolder.setStatusText$default(cvh, statusText, false, 2, null);
        Drawable background = getCvh().getLayout().getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
        setClipLayer(findDrawableByLayerId);
        ControlTemplate controlTemplate = getControl().getControlTemplate();
        Objects.requireNonNull(controlTemplate, "null cannot be cast to non-null type android.service.controls.templates.TemperatureControlTemplate");
        final TemperatureControlTemplate temperatureControlTemplate = (TemperatureControlTemplate) controlTemplate;
        int currentActiveMode = temperatureControlTemplate.getCurrentActiveMode();
        ControlTemplate subTemplate = temperatureControlTemplate.getTemplate();
        if (Intrinsics.areEqual(subTemplate, ControlTemplate.getNoTemplateObject()) || Intrinsics.areEqual(subTemplate, ControlTemplate.getErrorTemplate())) {
            boolean z = (currentActiveMode == 0 || currentActiveMode == 1) ? false : true;
            Drawable clipLayer = getClipLayer();
            if (z) {
                i2 = 10000;
            }
            clipLayer.setLevel(i2);
            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), z, currentActiveMode, false, 4, null);
            getCvh().getLayout().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.TemperatureControlBehavior$bind$1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ControlActionCoordinator controlActionCoordinator = TemperatureControlBehavior.this.getCvh().getControlActionCoordinator();
                    ControlViewHolder cvh2 = TemperatureControlBehavior.this.getCvh();
                    String templateId = temperatureControlTemplate.getTemplateId();
                    Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
                    controlActionCoordinator.touch(cvh2, templateId, TemperatureControlBehavior.this.getControl());
                }
            });
            return;
        }
        ControlViewHolder cvh2 = getCvh();
        Behavior behavior = this.subBehavior;
        ControlViewHolder.Companion companion = ControlViewHolder.Companion;
        int status = getControl().getStatus();
        Intrinsics.checkNotNullExpressionValue(subTemplate, "subTemplate");
        this.subBehavior = cvh2.bindBehavior(behavior, companion.findBehaviorClass(status, subTemplate, getControl().getDeviceType()), currentActiveMode);
    }
}
