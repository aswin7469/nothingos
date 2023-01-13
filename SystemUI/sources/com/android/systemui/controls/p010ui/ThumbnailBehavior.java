package com.android.systemui.controls.p010ui;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ThumbnailTemplate;
import android.util.TypedValue;
import android.view.View;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0014H\u0016J\u0010\u0010$\u001a\u00020 2\u0006\u0010\t\u001a\u00020\nH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\u00108BX\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u00020\u001aX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001e¨\u0006%"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ThumbnailBehavior;", "Lcom/android/systemui/controls/ui/Behavior;", "()V", "control", "Landroid/service/controls/Control;", "getControl", "()Landroid/service/controls/Control;", "setControl", "(Landroid/service/controls/Control;)V", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "getCvh", "()Lcom/android/systemui/controls/ui/ControlViewHolder;", "setCvh", "(Lcom/android/systemui/controls/ui/ControlViewHolder;)V", "enabled", "", "getEnabled", "()Z", "shadowColor", "", "shadowOffsetX", "", "shadowOffsetY", "shadowRadius", "template", "Landroid/service/controls/templates/ThumbnailTemplate;", "getTemplate", "()Landroid/service/controls/templates/ThumbnailTemplate;", "setTemplate", "(Landroid/service/controls/templates/ThumbnailTemplate;)V", "bind", "", "cws", "Lcom/android/systemui/controls/ui/ControlWithState;", "colorOffset", "initialize", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ThumbnailBehavior */
/* compiled from: ThumbnailBehavior.kt */
public final class ThumbnailBehavior implements Behavior {
    public Control control;
    public ControlViewHolder cvh;
    private int shadowColor;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private float shadowRadius;
    public ThumbnailTemplate template;

    public final ThumbnailTemplate getTemplate() {
        ThumbnailTemplate thumbnailTemplate = this.template;
        if (thumbnailTemplate != null) {
            return thumbnailTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("template");
        return null;
    }

    public final void setTemplate(ThumbnailTemplate thumbnailTemplate) {
        Intrinsics.checkNotNullParameter(thumbnailTemplate, "<set-?>");
        this.template = thumbnailTemplate;
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
        return getTemplate().isActive();
    }

    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
        TypedValue typedValue = new TypedValue();
        controlViewHolder.getContext().getResources().getValue(C1894R.dimen.controls_thumbnail_shadow_x, typedValue, true);
        this.shadowOffsetX = typedValue.getFloat();
        controlViewHolder.getContext().getResources().getValue(C1894R.dimen.controls_thumbnail_shadow_y, typedValue, true);
        this.shadowOffsetY = typedValue.getFloat();
        controlViewHolder.getContext().getResources().getValue(C1894R.dimen.controls_thumbnail_shadow_radius, typedValue, true);
        this.shadowRadius = typedValue.getFloat();
        this.shadowColor = controlViewHolder.getContext().getResources().getColor(C1894R.C1895color.control_thumbnail_shadow_color);
        controlViewHolder.getLayout().setOnClickListener(new ThumbnailBehavior$$ExternalSyntheticLambda1(controlViewHolder, this));
    }

    /* access modifiers changed from: private */
    /* renamed from: initialize$lambda-0  reason: not valid java name */
    public static final void m2734initialize$lambda0(ControlViewHolder controlViewHolder, ThumbnailBehavior thumbnailBehavior, View view) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        Intrinsics.checkNotNullParameter(thumbnailBehavior, "this$0");
        ControlActionCoordinator controlActionCoordinator = controlViewHolder.getControlActionCoordinator();
        String templateId = thumbnailBehavior.getTemplate().getTemplateId();
        Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
        controlActionCoordinator.touch(controlViewHolder, templateId, thumbnailBehavior.getControl());
    }

    public void bind(ControlWithState controlWithState, int i) {
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        ControlViewHolder cvh2 = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        ControlViewHolder.setStatusText$default(cvh2, statusText, false, 2, (Object) null);
        ThumbnailTemplate controlTemplate = getControl().getControlTemplate();
        if (controlTemplate != null) {
            setTemplate(controlTemplate);
            Drawable background = getCvh().getLayout().getBackground();
            if (background != null) {
                Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(C1894R.C1898id.clip_layer);
                if (findDrawableByLayerId != null) {
                    ClipDrawable clipDrawable = (ClipDrawable) findDrawableByLayerId;
                    clipDrawable.setLevel(getEnabled() ? 10000 : 0);
                    if (getTemplate().isActive()) {
                        getCvh().getTitle().setVisibility(4);
                        getCvh().getSubtitle().setVisibility(4);
                        getCvh().getStatus().setShadowLayer(this.shadowOffsetX, this.shadowOffsetY, this.shadowRadius, this.shadowColor);
                        getCvh().getBgExecutor().execute(new ThumbnailBehavior$$ExternalSyntheticLambda0(this, clipDrawable, i));
                    } else {
                        getCvh().getTitle().setVisibility(0);
                        getCvh().getSubtitle().setVisibility(0);
                        getCvh().getStatus().setShadowLayer(0.0f, 0.0f, 0.0f, this.shadowColor);
                    }
                    ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(getCvh(), getEnabled(), i, false, 4, (Object) null);
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.ClipDrawable");
            }
            throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.service.controls.templates.ThumbnailTemplate");
    }

    /* access modifiers changed from: private */
    /* renamed from: bind$lambda-2  reason: not valid java name */
    public static final void m2732bind$lambda2(ThumbnailBehavior thumbnailBehavior, ClipDrawable clipDrawable, int i) {
        Intrinsics.checkNotNullParameter(thumbnailBehavior, "this$0");
        Intrinsics.checkNotNullParameter(clipDrawable, "$clipLayer");
        thumbnailBehavior.getCvh().getUiExecutor().execute(new ThumbnailBehavior$$ExternalSyntheticLambda2(thumbnailBehavior, clipDrawable, thumbnailBehavior.getTemplate().getThumbnail().loadDrawable(thumbnailBehavior.getCvh().getContext()), i));
    }

    /* access modifiers changed from: private */
    /* renamed from: bind$lambda-2$lambda-1  reason: not valid java name */
    public static final void m2733bind$lambda2$lambda1(ThumbnailBehavior thumbnailBehavior, ClipDrawable clipDrawable, Drawable drawable, int i) {
        Intrinsics.checkNotNullParameter(thumbnailBehavior, "this$0");
        Intrinsics.checkNotNullParameter(clipDrawable, "$clipLayer");
        Intrinsics.checkNotNullExpressionValue(drawable, "drawable");
        clipDrawable.setDrawable(new CornerDrawable(drawable, (float) thumbnailBehavior.getCvh().getContext().getResources().getDimensionPixelSize(C1894R.dimen.control_corner_radius)));
        clipDrawable.setColorFilter(new BlendModeColorFilter(thumbnailBehavior.getCvh().getContext().getResources().getColor(C1894R.C1895color.control_thumbnail_tint), BlendMode.LUMINOSITY));
        ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(thumbnailBehavior.getCvh(), thumbnailBehavior.getEnabled(), i, false, 4, (Object) null);
    }
}
