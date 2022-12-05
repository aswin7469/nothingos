package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.StatelessTemplate;
import android.view.View;
import com.android.systemui.R$id;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: TouchBehavior.kt */
/* loaded from: classes.dex */
public final class TouchBehavior implements Behavior {
    @NotNull
    public static final Companion Companion = new Companion(null);
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    private int lastColorOffset;
    private boolean statelessTouch;
    public ControlTemplate template;

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
    public final ControlTemplate getTemplate() {
        ControlTemplate controlTemplate = this.template;
        if (controlTemplate != null) {
            return controlTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("template");
        throw null;
    }

    public final void setTemplate(@NotNull ControlTemplate controlTemplate) {
        Intrinsics.checkNotNullParameter(controlTemplate, "<set-?>");
        this.template = controlTemplate;
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

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean getEnabled() {
        return this.lastColorOffset > 0 || this.statelessTouch;
    }

    /* compiled from: TouchBehavior.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(@NotNull final ControlViewHolder cvh) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        setCvh(cvh);
        cvh.getLayout().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.TouchBehavior$initialize$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean enabled;
                int i;
                ControlActionCoordinator controlActionCoordinator = ControlViewHolder.this.getControlActionCoordinator();
                ControlViewHolder controlViewHolder = ControlViewHolder.this;
                String templateId = this.getTemplate().getTemplateId();
                Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
                controlActionCoordinator.touch(controlViewHolder, templateId, this.getControl());
                if (this.getTemplate() instanceof StatelessTemplate) {
                    this.statelessTouch = true;
                    ControlViewHolder controlViewHolder2 = ControlViewHolder.this;
                    enabled = this.getEnabled();
                    i = this.lastColorOffset;
                    ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(controlViewHolder2, enabled, i, false, 4, null);
                    DelayableExecutor uiExecutor = ControlViewHolder.this.getUiExecutor();
                    final TouchBehavior touchBehavior = this;
                    final ControlViewHolder controlViewHolder3 = ControlViewHolder.this;
                    uiExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.ui.TouchBehavior$initialize$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            boolean enabled2;
                            int i2;
                            TouchBehavior.this.statelessTouch = false;
                            ControlViewHolder controlViewHolder4 = controlViewHolder3;
                            enabled2 = TouchBehavior.this.getEnabled();
                            i2 = TouchBehavior.this.lastColorOffset;
                            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(controlViewHolder4, enabled2, i2, false, 4, null);
                        }
                    }, 3000L);
                }
            }
        });
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(@NotNull ControlWithState cws, int i) {
        Intrinsics.checkNotNullParameter(cws, "cws");
        Control control = cws.getControl();
        Intrinsics.checkNotNull(control);
        setControl(control);
        this.lastColorOffset = i;
        ControlViewHolder cvh = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        int i2 = 0;
        ControlViewHolder.setStatusText$default(cvh, statusText, false, 2, null);
        ControlTemplate controlTemplate = getControl().getControlTemplate();
        Intrinsics.checkNotNullExpressionValue(controlTemplate, "control.getControlTemplate()");
        setTemplate(controlTemplate);
        Drawable background = getCvh().getLayout().getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
        setClipLayer(findDrawableByLayerId);
        Drawable clipLayer = getClipLayer();
        if (getEnabled()) {
            i2 = 10000;
        }
        clipLayer.setLevel(i2);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), getEnabled(), i, false, 4, null);
    }
}
