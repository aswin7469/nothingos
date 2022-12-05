package com.android.systemui.controls.ui;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.ThumbnailTemplate;
import android.util.TypedValue;
import android.view.View;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ThumbnailBehavior.kt */
/* loaded from: classes.dex */
public final class ThumbnailBehavior implements Behavior {
    public Control control;
    public ControlViewHolder cvh;
    private int shadowColor;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private float shadowRadius;
    public ThumbnailTemplate template;

    @NotNull
    public final ThumbnailTemplate getTemplate() {
        ThumbnailTemplate thumbnailTemplate = this.template;
        if (thumbnailTemplate != null) {
            return thumbnailTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("template");
        throw null;
    }

    public final void setTemplate(@NotNull ThumbnailTemplate thumbnailTemplate) {
        Intrinsics.checkNotNullParameter(thumbnailTemplate, "<set-?>");
        this.template = thumbnailTemplate;
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
        return getTemplate().isActive();
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(@NotNull final ControlViewHolder cvh) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        setCvh(cvh);
        TypedValue typedValue = new TypedValue();
        cvh.getContext().getResources().getValue(R$dimen.controls_thumbnail_shadow_x, typedValue, true);
        this.shadowOffsetX = typedValue.getFloat();
        cvh.getContext().getResources().getValue(R$dimen.controls_thumbnail_shadow_y, typedValue, true);
        this.shadowOffsetY = typedValue.getFloat();
        cvh.getContext().getResources().getValue(R$dimen.controls_thumbnail_shadow_radius, typedValue, true);
        this.shadowRadius = typedValue.getFloat();
        this.shadowColor = cvh.getContext().getResources().getColor(R$color.control_thumbnail_shadow_color);
        cvh.getLayout().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$initialize$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlActionCoordinator controlActionCoordinator = ControlViewHolder.this.getControlActionCoordinator();
                ControlViewHolder controlViewHolder = ControlViewHolder.this;
                String templateId = this.getTemplate().getTemplateId();
                Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
                controlActionCoordinator.touch(controlViewHolder, templateId, this.getControl());
            }
        });
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(@NotNull ControlWithState cws, final int i) {
        Intrinsics.checkNotNullParameter(cws, "cws");
        Control control = cws.getControl();
        Intrinsics.checkNotNull(control);
        setControl(control);
        ControlViewHolder cvh = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        ControlViewHolder.setStatusText$default(cvh, statusText, false, 2, null);
        ControlTemplate controlTemplate = getControl().getControlTemplate();
        Objects.requireNonNull(controlTemplate, "null cannot be cast to non-null type android.service.controls.templates.ThumbnailTemplate");
        setTemplate((ThumbnailTemplate) controlTemplate);
        Drawable background = getCvh().getLayout().getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer);
        Objects.requireNonNull(findDrawableByLayerId, "null cannot be cast to non-null type android.graphics.drawable.ClipDrawable");
        final ClipDrawable clipDrawable = (ClipDrawable) findDrawableByLayerId;
        clipDrawable.setLevel(getEnabled() ? 10000 : 0);
        if (getTemplate().isActive()) {
            getCvh().getTitle().setVisibility(4);
            getCvh().getSubtitle().setVisibility(4);
            getCvh().getStatus().setShadowLayer(this.shadowOffsetX, this.shadowOffsetY, this.shadowRadius, this.shadowColor);
            getCvh().getBgExecutor().execute(new Runnable() { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$bind$1
                @Override // java.lang.Runnable
                public final void run() {
                    final Drawable loadDrawable = ThumbnailBehavior.this.getTemplate().getThumbnail().loadDrawable(ThumbnailBehavior.this.getCvh().getContext());
                    DelayableExecutor uiExecutor = ThumbnailBehavior.this.getCvh().getUiExecutor();
                    final ThumbnailBehavior thumbnailBehavior = ThumbnailBehavior.this;
                    final ClipDrawable clipDrawable2 = clipDrawable;
                    final int i2 = i;
                    uiExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$bind$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            boolean enabled;
                            ClipDrawable clipDrawable3 = clipDrawable2;
                            Drawable drawable = loadDrawable;
                            Intrinsics.checkNotNullExpressionValue(drawable, "drawable");
                            clipDrawable3.setDrawable(new CornerDrawable(drawable, ThumbnailBehavior.this.getCvh().getContext().getResources().getDimensionPixelSize(R$dimen.control_corner_radius)));
                            clipDrawable2.setColorFilter(new BlendModeColorFilter(ThumbnailBehavior.this.getCvh().getContext().getResources().getColor(R$color.control_thumbnail_tint), BlendMode.LUMINOSITY));
                            ControlViewHolder cvh2 = ThumbnailBehavior.this.getCvh();
                            enabled = ThumbnailBehavior.this.getEnabled();
                            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(cvh2, enabled, i2, false, 4, null);
                        }
                    });
                }
            });
        } else {
            getCvh().getTitle().setVisibility(0);
            getCvh().getSubtitle().setVisibility(0);
            getCvh().getStatus().setShadowLayer(0.0f, 0.0f, 0.0f, this.shadowColor);
        }
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), getEnabled(), i, false, 4, null);
    }
}
