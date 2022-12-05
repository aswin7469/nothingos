package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.RangeTemplate;
import android.service.controls.templates.StatelessTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ThumbnailTemplate;
import android.service.controls.templates.ToggleRangeTemplate;
import android.service.controls.templates.ToggleTemplate;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.R$color;
import com.android.systemui.R$fraction;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlViewHolder.kt */
/* loaded from: classes.dex */
public final class ControlViewHolder {
    @NotNull
    private final GradientDrawable baseLayer;
    @Nullable
    private Behavior behavior;
    @NotNull
    private final DelayableExecutor bgExecutor;
    @NotNull
    private final ClipDrawable clipLayer;
    @NotNull
    private final Context context;
    @NotNull
    private final ControlActionCoordinator controlActionCoordinator;
    @NotNull
    private final ControlsController controlsController;
    @NotNull
    private final ControlsMetricsLogger controlsMetricsLogger;
    public ControlWithState cws;
    @NotNull
    private final ImageView icon;
    private boolean isLoading;
    @Nullable
    private ControlAction lastAction;
    @Nullable
    private Dialog lastChallengeDialog;
    @NotNull
    private final ViewGroup layout;
    @NotNull
    private CharSequence nextStatusText = "";
    @NotNull
    private final Function0<Unit> onDialogCancel = new ControlViewHolder$onDialogCancel$1(this);
    @Nullable
    private ValueAnimator stateAnimator;
    @NotNull
    private final TextView status;
    @Nullable
    private Animator statusAnimator;
    @NotNull
    private final TextView subtitle;
    @NotNull
    private final TextView title;
    private final float toggleBackgroundIntensity;
    @NotNull
    private final DelayableExecutor uiExecutor;
    private final int uid;
    private boolean userInteractionInProgress;
    @Nullable
    private Dialog visibleDialog;
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final Set<Integer> FORCE_PANEL_DEVICES = SetsKt.setOf((Object[]) new Integer[]{49, 50});
    @NotNull
    private static final int[] ATTR_ENABLED = {16842910};
    @NotNull
    private static final int[] ATTR_DISABLED = {-16842910};

    public ControlViewHolder(@NotNull ViewGroup layout, @NotNull ControlsController controlsController, @NotNull DelayableExecutor uiExecutor, @NotNull DelayableExecutor bgExecutor, @NotNull ControlActionCoordinator controlActionCoordinator, @NotNull ControlsMetricsLogger controlsMetricsLogger, int i) {
        Intrinsics.checkNotNullParameter(layout, "layout");
        Intrinsics.checkNotNullParameter(controlsController, "controlsController");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(controlActionCoordinator, "controlActionCoordinator");
        Intrinsics.checkNotNullParameter(controlsMetricsLogger, "controlsMetricsLogger");
        this.layout = layout;
        this.controlsController = controlsController;
        this.uiExecutor = uiExecutor;
        this.bgExecutor = bgExecutor;
        this.controlActionCoordinator = controlActionCoordinator;
        this.controlsMetricsLogger = controlsMetricsLogger;
        this.uid = i;
        this.toggleBackgroundIntensity = layout.getContext().getResources().getFraction(R$fraction.controls_toggle_bg_intensity, 1, 1);
        View requireViewById = layout.requireViewById(R$id.icon);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "layout.requireViewById(R.id.icon)");
        this.icon = (ImageView) requireViewById;
        View requireViewById2 = layout.requireViewById(R$id.status);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "layout.requireViewById(R.id.status)");
        TextView textView = (TextView) requireViewById2;
        this.status = textView;
        View requireViewById3 = layout.requireViewById(R$id.title);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "layout.requireViewById(R.id.title)");
        this.title = (TextView) requireViewById3;
        View requireViewById4 = layout.requireViewById(R$id.subtitle);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "layout.requireViewById(R.id.subtitle)");
        this.subtitle = (TextView) requireViewById4;
        Context context = layout.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "layout.getContext()");
        this.context = context;
        Drawable background = layout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        LayerDrawable layerDrawable = (LayerDrawable) background;
        layerDrawable.mutate();
        Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(R$id.clip_layer);
        Objects.requireNonNull(findDrawableByLayerId, "null cannot be cast to non-null type android.graphics.drawable.ClipDrawable");
        this.clipLayer = (ClipDrawable) findDrawableByLayerId;
        Drawable findDrawableByLayerId2 = layerDrawable.findDrawableByLayerId(R$id.background);
        Objects.requireNonNull(findDrawableByLayerId2, "null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
        this.baseLayer = (GradientDrawable) findDrawableByLayerId2;
        textView.setSelected(true);
    }

    @NotNull
    public final ViewGroup getLayout() {
        return this.layout;
    }

    @NotNull
    public final DelayableExecutor getUiExecutor() {
        return this.uiExecutor;
    }

    @NotNull
    public final DelayableExecutor getBgExecutor() {
        return this.bgExecutor;
    }

    @NotNull
    public final ControlActionCoordinator getControlActionCoordinator() {
        return this.controlActionCoordinator;
    }

    public final int getUid() {
        return this.uid;
    }

    /* compiled from: ControlViewHolder.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final KClass<? extends Behavior> findBehaviorClass(int i, @NotNull ControlTemplate template, int i2) {
            Intrinsics.checkNotNullParameter(template, "template");
            if (i != 1) {
                return Reflection.getOrCreateKotlinClass(StatusBehavior.class);
            }
            if (Intrinsics.areEqual(template, ControlTemplate.NO_TEMPLATE)) {
                return Reflection.getOrCreateKotlinClass(TouchBehavior.class);
            }
            if (template instanceof ThumbnailTemplate) {
                return Reflection.getOrCreateKotlinClass(ThumbnailBehavior.class);
            }
            if (i2 == 50) {
                return Reflection.getOrCreateKotlinClass(TouchBehavior.class);
            }
            if (template instanceof ToggleTemplate) {
                return Reflection.getOrCreateKotlinClass(ToggleBehavior.class);
            }
            if (template instanceof StatelessTemplate) {
                return Reflection.getOrCreateKotlinClass(TouchBehavior.class);
            }
            if (!(template instanceof ToggleRangeTemplate) && !(template instanceof RangeTemplate)) {
                return Reflection.getOrCreateKotlinClass(template instanceof TemperatureControlTemplate ? TemperatureControlBehavior.class : DefaultBehavior.class);
            }
            return Reflection.getOrCreateKotlinClass(ToggleRangeBehavior.class);
        }
    }

    @NotNull
    public final ImageView getIcon() {
        return this.icon;
    }

    @NotNull
    public final TextView getStatus() {
        return this.status;
    }

    @NotNull
    public final TextView getTitle() {
        return this.title;
    }

    @NotNull
    public final TextView getSubtitle() {
        return this.subtitle;
    }

    @NotNull
    public final Context getContext() {
        return this.context;
    }

    @NotNull
    public final ClipDrawable getClipLayer() {
        return this.clipLayer;
    }

    @NotNull
    public final ControlWithState getCws() {
        ControlWithState controlWithState = this.cws;
        if (controlWithState != null) {
            return controlWithState;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cws");
        throw null;
    }

    public final void setCws(@NotNull ControlWithState controlWithState) {
        Intrinsics.checkNotNullParameter(controlWithState, "<set-?>");
        this.cws = controlWithState;
    }

    @Nullable
    public final ControlAction getLastAction() {
        return this.lastAction;
    }

    public final void setLoading(boolean z) {
        this.isLoading = z;
    }

    public final void setVisibleDialog(@Nullable Dialog dialog) {
        this.visibleDialog = dialog;
    }

    public final int getDeviceType() {
        Control control = getCws().getControl();
        Integer valueOf = control == null ? null : Integer.valueOf(control.getDeviceType());
        return valueOf == null ? getCws().getCi().getDeviceType() : valueOf.intValue();
    }

    public final int getControlStatus() {
        Control control = getCws().getControl();
        if (control == null) {
            return 0;
        }
        return control.getStatus();
    }

    @NotNull
    public final ControlTemplate getControlTemplate() {
        Control control = getCws().getControl();
        ControlTemplate controlTemplate = control == null ? null : control.getControlTemplate();
        if (controlTemplate == null) {
            ControlTemplate NO_TEMPLATE = ControlTemplate.NO_TEMPLATE;
            Intrinsics.checkNotNullExpressionValue(NO_TEMPLATE, "NO_TEMPLATE");
            return NO_TEMPLATE;
        }
        return controlTemplate;
    }

    public final void setUserInteractionInProgress(boolean z) {
        this.userInteractionInProgress = z;
    }

    public final void bindData(@NotNull ControlWithState cws, boolean z) {
        Intrinsics.checkNotNullParameter(cws, "cws");
        if (this.userInteractionInProgress) {
            return;
        }
        setCws(cws);
        if (getControlStatus() == 0 || getControlStatus() == 2) {
            this.title.setText(cws.getCi().getControlTitle());
            this.subtitle.setText(cws.getCi().getControlSubtitle());
        } else {
            Control control = cws.getControl();
            if (control != null) {
                getTitle().setText(control.getTitle());
                getSubtitle().setText(control.getSubtitle());
            }
        }
        boolean z2 = true;
        if (cws.getControl() != null) {
            getLayout().setClickable(true);
            getLayout().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.controls.ui.ControlViewHolder$bindData$2$1
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    ControlViewHolder.this.getControlActionCoordinator().longPress(ControlViewHolder.this);
                    return true;
                }
            });
            getControlActionCoordinator().runPendingAction(cws.getCi().getControlId());
        }
        boolean z3 = this.isLoading;
        this.isLoading = false;
        this.behavior = bindBehavior$default(this, this.behavior, Companion.findBehaviorClass(getControlStatus(), getControlTemplate(), getDeviceType()), 0, 4, null);
        updateContentDescription();
        if (!z3 || this.isLoading) {
            z2 = false;
        }
        if (!z2) {
            return;
        }
        this.controlsMetricsLogger.refreshEnd(this, z);
    }

    public final void actionResponse(int i) {
        this.controlActionCoordinator.enableActionOnTouch(getCws().getCi().getControlId());
        boolean z = this.lastChallengeDialog != null;
        if (i == 0) {
            this.lastChallengeDialog = null;
            setErrorStatus();
        } else if (i == 1) {
            this.lastChallengeDialog = null;
        } else if (i == 2) {
            this.lastChallengeDialog = null;
            setErrorStatus();
        } else if (i == 3) {
            Dialog createConfirmationDialog = ChallengeDialogs.INSTANCE.createConfirmationDialog(this, this.onDialogCancel);
            this.lastChallengeDialog = createConfirmationDialog;
            if (createConfirmationDialog == null) {
                return;
            }
            createConfirmationDialog.show();
        } else if (i == 4) {
            Dialog createPinDialog = ChallengeDialogs.INSTANCE.createPinDialog(this, false, z, this.onDialogCancel);
            this.lastChallengeDialog = createPinDialog;
            if (createPinDialog == null) {
                return;
            }
            createPinDialog.show();
        } else if (i != 5) {
        } else {
            Dialog createPinDialog2 = ChallengeDialogs.INSTANCE.createPinDialog(this, true, z, this.onDialogCancel);
            this.lastChallengeDialog = createPinDialog2;
            if (createPinDialog2 == null) {
                return;
            }
            createPinDialog2.show();
        }
    }

    public final void dismiss() {
        Dialog dialog = this.lastChallengeDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        this.lastChallengeDialog = null;
        Dialog dialog2 = this.visibleDialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
        this.visibleDialog = null;
    }

    public final void setErrorStatus() {
        animateStatusChange(true, new ControlViewHolder$setErrorStatus$1(this, this.context.getResources().getString(R$string.controls_error_failed)));
    }

    private final void updateContentDescription() {
        ViewGroup viewGroup = this.layout;
        StringBuilder sb = new StringBuilder();
        sb.append((Object) this.title.getText());
        sb.append(' ');
        sb.append((Object) this.subtitle.getText());
        sb.append(' ');
        sb.append((Object) this.status.getText());
        viewGroup.setContentDescription(sb.toString());
    }

    public final void action(@NotNull ControlAction action) {
        Intrinsics.checkNotNullParameter(action, "action");
        this.lastAction = action;
        this.controlsController.action(getCws().getComponentName(), getCws().getCi(), action);
    }

    public final boolean usePanel() {
        return FORCE_PANEL_DEVICES.contains(Integer.valueOf(getDeviceType())) || Intrinsics.areEqual(getControlTemplate(), ControlTemplate.NO_TEMPLATE);
    }

    public static /* synthetic */ Behavior bindBehavior$default(ControlViewHolder controlViewHolder, Behavior behavior, KClass kClass, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return controlViewHolder.bindBehavior(behavior, kClass, i);
    }

    @NotNull
    public final Behavior bindBehavior(@Nullable Behavior behavior, @NotNull KClass<? extends Behavior> clazz, int i) {
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        if (behavior == null || !Intrinsics.areEqual(Reflection.getOrCreateKotlinClass(behavior.getClass()), clazz)) {
            behavior = (Behavior) JvmClassMappingKt.getJavaClass(clazz).newInstance();
            behavior.initialize(this);
            this.layout.setAccessibilityDelegate(null);
        }
        behavior.bind(getCws(), i);
        Intrinsics.checkNotNullExpressionValue(behavior, "behavior.also {\n            it.bind(cws, offset)\n        }");
        return behavior;
    }

    public static /* synthetic */ void applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(ControlViewHolder controlViewHolder, boolean z, int i, boolean z2, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z2 = true;
        }
        controlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(z, i, z2);
    }

    public final void applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(boolean z, int i, boolean z2) {
        RenderInfo lookup = RenderInfo.Companion.lookup(this.context, getCws().getComponentName(), (getControlStatus() == 1 || getControlStatus() == 0) ? getDeviceType() : -1000, i);
        ColorStateList colorStateList = this.context.getResources().getColorStateList(lookup.getForeground(), this.context.getTheme());
        CharSequence charSequence = this.nextStatusText;
        Control control = getCws().getControl();
        if (Intrinsics.areEqual(charSequence, this.status.getText())) {
            z2 = false;
        }
        animateStatusChange(z2, new ControlViewHolder$applyRenderInfo$1(this, z, charSequence, lookup, colorStateList, control));
        animateBackgroundChange(z2, z, lookup.getEnabledBackground());
    }

    public final void setStatusTextSize(float f) {
        this.status.setTextSize(0, f);
    }

    public static /* synthetic */ void setStatusText$default(ControlViewHolder controlViewHolder, CharSequence charSequence, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        controlViewHolder.setStatusText(charSequence, z);
    }

    public final void setStatusText(@NotNull CharSequence text, boolean z) {
        Intrinsics.checkNotNullParameter(text, "text");
        if (z) {
            this.status.setAlpha(1.0f);
            this.status.setText(text);
            updateContentDescription();
        }
        this.nextStatusText = text;
    }

    private final void animateBackgroundChange(boolean z, boolean z2, int i) {
        List listOf;
        int intValue;
        ColorStateList customColor;
        Resources resources = this.context.getResources();
        int i2 = R$color.control_default_background;
        int color = resources.getColor(i2, this.context.getTheme());
        if (z2) {
            Control control = getCws().getControl();
            Integer num = null;
            if (control != null && (customColor = control.getCustomColor()) != null) {
                num = Integer.valueOf(customColor.getColorForState(new int[]{16842910}, customColor.getDefaultColor()));
            }
            if (num != null) {
                intValue = num.intValue();
            } else {
                intValue = this.context.getResources().getColor(i, this.context.getTheme());
            }
            listOf = CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(intValue), 255});
        } else {
            listOf = CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(this.context.getResources().getColor(i2, this.context.getTheme())), 0});
        }
        int intValue2 = ((Number) listOf.get(0)).intValue();
        int intValue3 = ((Number) listOf.get(1)).intValue();
        if (this.behavior instanceof ToggleRangeBehavior) {
            color = ColorUtils.blendARGB(color, intValue2, this.toggleBackgroundIntensity);
        }
        int i3 = color;
        Drawable drawable = this.clipLayer.getDrawable();
        if (drawable == null) {
            return;
        }
        getClipLayer().setAlpha(0);
        ValueAnimator valueAnimator = this.stateAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (z) {
            startBackgroundAnimation(drawable, intValue3, intValue2, i3);
        } else {
            applyBackgroundChange(drawable, intValue3, intValue2, i3, 1.0f);
        }
    }

    private final void startBackgroundAnimation(final Drawable drawable, int i, final int i2, final int i3) {
        ColorStateList color;
        final int defaultColor = (!(drawable instanceof GradientDrawable) || (color = ((GradientDrawable) drawable).getColor()) == null) ? i2 : color.getDefaultColor();
        ColorStateList color2 = this.baseLayer.getColor();
        final int defaultColor2 = color2 == null ? i3 : color2.getDefaultColor();
        final float alpha = this.layout.getAlpha();
        ValueAnimator ofInt = ValueAnimator.ofInt(this.clipLayer.getAlpha(), i);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.controls.ui.ControlViewHolder$startBackgroundAnimation$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                this.applyBackgroundChange(drawable, ((Integer) animatedValue).intValue(), ColorUtils.blendARGB(defaultColor, i2, valueAnimator.getAnimatedFraction()), ColorUtils.blendARGB(defaultColor2, i3, valueAnimator.getAnimatedFraction()), MathUtils.lerp(alpha, 1.0f, valueAnimator.getAnimatedFraction()));
            }
        });
        ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ControlViewHolder$startBackgroundAnimation$1$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator) {
                ControlViewHolder.this.stateAnimator = null;
            }
        });
        ofInt.setDuration(700L);
        ofInt.setInterpolator(Interpolators.CONTROL_STATE);
        ofInt.start();
        Unit unit = Unit.INSTANCE;
        this.stateAnimator = ofInt;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void applyBackgroundChange(Drawable drawable, int i, int i2, int i3, float f) {
        drawable.setAlpha(i);
        if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(i2);
        }
        this.baseLayer.setColor(i3);
        this.layout.setAlpha(f);
    }

    private final void animateStatusChange(boolean z, final Function0<Unit> function0) {
        Animator animator = this.statusAnimator;
        if (animator != null) {
            animator.cancel();
        }
        if (!z) {
            function0.mo1951invoke();
        } else if (this.isLoading) {
            function0.mo1951invoke();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.status, "alpha", 0.45f);
            ofFloat.setRepeatMode(2);
            ofFloat.setRepeatCount(-1);
            ofFloat.setDuration(500L);
            ofFloat.setInterpolator(Interpolators.LINEAR);
            ofFloat.setStartDelay(900L);
            ofFloat.start();
            Unit unit = Unit.INSTANCE;
            this.statusAnimator = ofFloat;
        } else {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.status, "alpha", 0.0f);
            ofFloat2.setDuration(200L);
            Interpolator interpolator = Interpolators.LINEAR;
            ofFloat2.setInterpolator(interpolator);
            ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ControlViewHolder$animateStatusChange$fadeOut$1$1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(@Nullable Animator animator2) {
                    function0.mo1951invoke();
                }
            });
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.status, "alpha", 1.0f);
            ofFloat3.setDuration(200L);
            ofFloat3.setInterpolator(interpolator);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(ofFloat2, ofFloat3);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ControlViewHolder$animateStatusChange$2$1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(@Nullable Animator animator2) {
                    ControlViewHolder.this.getStatus().setAlpha(1.0f);
                    ControlViewHolder.this.statusAnimator = null;
                }
            });
            animatorSet.start();
            Unit unit2 = Unit.INSTANCE;
            this.statusAnimator = animatorSet;
        }
    }

    public final void updateStatusRow$frameworks__base__packages__SystemUI__android_common__SystemUI_core(boolean z, @NotNull CharSequence text, @NotNull Drawable drawable, @NotNull ColorStateList color, @Nullable Control control) {
        Icon customIcon;
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(drawable, "drawable");
        Intrinsics.checkNotNullParameter(color, "color");
        setEnabled(z);
        this.status.setText(text);
        updateContentDescription();
        this.status.setTextColor(color);
        Unit unit = null;
        if (control != null && (customIcon = control.getCustomIcon()) != null) {
            getIcon().setImageIcon(customIcon);
            getIcon().setImageTintList(customIcon.getTintList());
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            if (drawable instanceof StateListDrawable) {
                if (getIcon().getDrawable() == null || !(getIcon().getDrawable() instanceof StateListDrawable)) {
                    getIcon().setImageDrawable(drawable);
                }
                getIcon().setImageState(z ? ATTR_ENABLED : ATTR_DISABLED, true);
            } else {
                getIcon().setImageDrawable(drawable);
            }
            if (getDeviceType() == 52) {
                return;
            }
            getIcon().setImageTintList(color);
        }
    }

    private final void setEnabled(boolean z) {
        this.status.setEnabled(z);
        this.icon.setEnabled(z);
    }
}
