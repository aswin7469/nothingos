package com.android.systemui.controls.p010ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
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
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.Key;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

@Metadata(mo64986d1 = {"\u0000Ì\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00012\u00020\u0001:\u0002\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e¢\u0006\u0002\u0010\u000fJ\u000e\u0010i\u001a\u00020P2\u0006\u0010i\u001a\u00020CJ\u000e\u0010j\u001a\u00020P2\u0006\u0010k\u001a\u00020\u000eJ \u0010l\u001a\u00020P2\u0006\u0010m\u001a\u00020>2\u0006\u0010n\u001a\u00020>2\u0006\u0010o\u001a\u00020\u000eH\u0002J\u001e\u0010p\u001a\u00020P2\u0006\u0010m\u001a\u00020>2\f\u0010q\u001a\b\u0012\u0004\u0012\u00020P0OH\u0002J4\u0010r\u001a\u00020P2\u0006\u0010s\u001a\u00020t2\u0006\u0010u\u001a\u00020\u000e2\b\b\u0001\u0010v\u001a\u00020\u000e2\b\b\u0001\u0010w\u001a\u00020\u000e2\u0006\u0010x\u001a\u00020^H\u0002J'\u0010y\u001a\u00020P2\u0006\u0010n\u001a\u00020>2\u0006\u0010z\u001a\u00020\u000e2\b\b\u0002\u0010m\u001a\u00020>H\u0000¢\u0006\u0002\b{J*\u0010|\u001a\u00020\u00132\b\u0010}\u001a\u0004\u0018\u00010\u00132\u000e\u0010~\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001302\b\b\u0002\u0010z\u001a\u00020\u000eJ\u0018\u0010\u0001\u001a\u00020P2\u0006\u00103\u001a\u0002042\u0007\u0010\u0001\u001a\u00020>J\u0007\u0010\u0001\u001a\u00020PJ\u0010\u0010\u0001\u001a\u000b \u0001*\u0004\u0018\u00010M0MJ\u0011\u0010\u0001\u001a\u00020P2\u0006\u0010n\u001a\u00020>H\u0002J\u0007\u0010\u0001\u001a\u00020PJ\u001b\u0010\u0001\u001a\u00020P2\u0007\u0010\u0001\u001a\u00020M2\t\b\u0002\u0010\u0001\u001a\u00020>J\u0010\u0010\u0001\u001a\u00020P2\u0007\u0010\u0001\u001a\u00020^J-\u0010\u0001\u001a\u00020P2\u0006\u0010s\u001a\u00020t2\u0006\u0010u\u001a\u00020\u000e2\b\b\u0001\u0010v\u001a\u00020\u000e2\b\b\u0001\u0010w\u001a\u00020\u000eH\u0002J\t\u0010\u0001\u001a\u00020PH\u0002J?\u0010\u0001\u001a\u00020P2\u0006\u0010n\u001a\u00020>2\u0007\u0010\u0001\u001a\u00020M2\u0007\u0010\u0001\u001a\u00020t2\b\u0010\u0001\u001a\u00030\u00012\n\u0010\u0001\u001a\u0005\u0018\u00010\u0001H\u0001¢\u0006\u0003\b\u0001J\u0007\u0010\u0001\u001a\u00020>R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\u001b¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u001e\u001a\u00020\u001f¢\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020#¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0011\u0010(\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b)\u0010*R\u0011\u0010+\u001a\u00020,8F¢\u0006\u0006\u001a\u0004\b-\u0010.R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b1\u00102R\u001a\u00103\u001a\u000204X.¢\u0006\u000e\n\u0000\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u0011\u00109\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b:\u0010*R\u0011\u0010;\u001a\u00020\u001b¢\u0006\b\n\u0000\u001a\u0004\b<\u0010\u001dR\u001a\u0010=\u001a\u00020>X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010?\"\u0004\b@\u0010AR\u001c\u0010B\u001a\u0004\u0018\u00010CX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010E\"\u0004\bF\u0010GR\u0010\u0010H\u001a\u0004\u0018\u00010IX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\bJ\u0010KR\u000e\u0010L\u001a\u00020MX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010N\u001a\b\u0012\u0004\u0012\u00020P0OX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010Q\u001a\u0004\u0018\u00010RX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010S\u001a\u00020T¢\u0006\b\n\u0000\u001a\u0004\bU\u0010VR\u0010\u0010W\u001a\u0004\u0018\u00010XX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010Y\u001a\u00020T¢\u0006\b\n\u0000\u001a\u0004\bZ\u0010VR\u0011\u0010[\u001a\u00020T¢\u0006\b\n\u0000\u001a\u0004\b\\\u0010VR\u000e\u0010]\u001a\u00020^X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b_\u0010\u0019R\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b`\u0010*R\u001a\u0010a\u001a\u00020>X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bb\u0010?\"\u0004\bc\u0010AR\u001c\u0010d\u001a\u0004\u0018\u00010IX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\be\u0010f\"\u0004\bg\u0010h¨\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ControlViewHolder;", "", "layout", "Landroid/view/ViewGroup;", "controlsController", "Lcom/android/systemui/controls/controller/ControlsController;", "uiExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "bgExecutor", "controlActionCoordinator", "Lcom/android/systemui/controls/ui/ControlActionCoordinator;", "controlsMetricsLogger", "Lcom/android/systemui/controls/ControlsMetricsLogger;", "uid", "", "(Landroid/view/ViewGroup;Lcom/android/systemui/controls/controller/ControlsController;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/controls/ui/ControlActionCoordinator;Lcom/android/systemui/controls/ControlsMetricsLogger;I)V", "baseLayer", "Landroid/graphics/drawable/GradientDrawable;", "behavior", "Lcom/android/systemui/controls/ui/Behavior;", "getBehavior", "()Lcom/android/systemui/controls/ui/Behavior;", "setBehavior", "(Lcom/android/systemui/controls/ui/Behavior;)V", "getBgExecutor", "()Lcom/android/systemui/util/concurrency/DelayableExecutor;", "chevronIcon", "Landroid/widget/ImageView;", "getChevronIcon", "()Landroid/widget/ImageView;", "clipLayer", "Landroid/graphics/drawable/ClipDrawable;", "getClipLayer", "()Landroid/graphics/drawable/ClipDrawable;", "context", "Landroid/content/Context;", "getContext", "()Landroid/content/Context;", "getControlActionCoordinator", "()Lcom/android/systemui/controls/ui/ControlActionCoordinator;", "controlStatus", "getControlStatus", "()I", "controlTemplate", "Landroid/service/controls/templates/ControlTemplate;", "getControlTemplate", "()Landroid/service/controls/templates/ControlTemplate;", "getControlsController", "()Lcom/android/systemui/controls/controller/ControlsController;", "getControlsMetricsLogger", "()Lcom/android/systemui/controls/ControlsMetricsLogger;", "cws", "Lcom/android/systemui/controls/ui/ControlWithState;", "getCws", "()Lcom/android/systemui/controls/ui/ControlWithState;", "setCws", "(Lcom/android/systemui/controls/ui/ControlWithState;)V", "deviceType", "getDeviceType", "icon", "getIcon", "isLoading", "", "()Z", "setLoading", "(Z)V", "lastAction", "Landroid/service/controls/actions/ControlAction;", "getLastAction", "()Landroid/service/controls/actions/ControlAction;", "setLastAction", "(Landroid/service/controls/actions/ControlAction;)V", "lastChallengeDialog", "Landroid/app/Dialog;", "getLayout", "()Landroid/view/ViewGroup;", "nextStatusText", "", "onDialogCancel", "Lkotlin/Function0;", "", "stateAnimator", "Landroid/animation/ValueAnimator;", "status", "Landroid/widget/TextView;", "getStatus", "()Landroid/widget/TextView;", "statusAnimator", "Landroid/animation/Animator;", "subtitle", "getSubtitle", "title", "getTitle", "toggleBackgroundIntensity", "", "getUiExecutor", "getUid", "userInteractionInProgress", "getUserInteractionInProgress", "setUserInteractionInProgress", "visibleDialog", "getVisibleDialog", "()Landroid/app/Dialog;", "setVisibleDialog", "(Landroid/app/Dialog;)V", "action", "actionResponse", "response", "animateBackgroundChange", "animated", "enabled", "bgColor", "animateStatusChange", "statusRowUpdater", "applyBackgroundChange", "clipDrawable", "Landroid/graphics/drawable/Drawable;", "newAlpha", "newClipColor", "newBaseColor", "newLayoutAlpha", "applyRenderInfo", "offset", "applyRenderInfo$SystemUI_nothingRelease", "bindBehavior", "existingBehavior", "supplier", "Ljava/util/function/Supplier;", "bindData", "isLocked", "dismiss", "getStatusText", "kotlin.jvm.PlatformType", "setEnabled", "setErrorStatus", "setStatusText", "text", "immediately", "setStatusTextSize", "textSize", "startBackgroundAnimation", "updateContentDescription", "updateStatusRow", "drawable", "color", "Landroid/content/res/ColorStateList;", "control", "Landroid/service/controls/Control;", "updateStatusRow$SystemUI_nothingRelease", "usePanel", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlViewHolder */
/* compiled from: ControlViewHolder.kt */
public final class ControlViewHolder {
    private static final int ALPHA_DISABLED = 0;
    private static final int ALPHA_ENABLED = 255;
    private static final int[] ATTR_DISABLED = {-16842910};
    private static final int[] ATTR_ENABLED = {16842910};
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final Set<Integer> FORCE_PANEL_DEVICES = SetsKt.setOf(49, 50);
    public static final int MAX_LEVEL = 10000;
    public static final int MIN_LEVEL = 0;
    public static final long STATE_ANIMATION_DURATION = 700;
    private static final float STATUS_ALPHA_DIMMED = 0.45f;
    private static final float STATUS_ALPHA_ENABLED = 1.0f;
    private final GradientDrawable baseLayer;
    private Behavior behavior;
    private final DelayableExecutor bgExecutor;
    private final ImageView chevronIcon;
    private final ClipDrawable clipLayer;
    private final Context context;
    private final ControlActionCoordinator controlActionCoordinator;
    private final ControlsController controlsController;
    private final ControlsMetricsLogger controlsMetricsLogger;
    public ControlWithState cws;
    private final ImageView icon;
    private boolean isLoading;
    private ControlAction lastAction;
    /* access modifiers changed from: private */
    public Dialog lastChallengeDialog;
    private final ViewGroup layout;
    private CharSequence nextStatusText = "";
    private final Function0<Unit> onDialogCancel;
    /* access modifiers changed from: private */
    public ValueAnimator stateAnimator;
    private final TextView status;
    /* access modifiers changed from: private */
    public Animator statusAnimator;
    private final TextView subtitle;
    private final TextView title;
    private final float toggleBackgroundIntensity;
    private final DelayableExecutor uiExecutor;
    private final int uid;
    private boolean userInteractionInProgress;
    private Dialog visibleDialog;

    public ControlViewHolder(ViewGroup viewGroup, ControlsController controlsController2, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, ControlActionCoordinator controlActionCoordinator2, ControlsMetricsLogger controlsMetricsLogger2, int i) {
        Intrinsics.checkNotNullParameter(viewGroup, "layout");
        Intrinsics.checkNotNullParameter(controlsController2, "controlsController");
        Intrinsics.checkNotNullParameter(delayableExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "bgExecutor");
        Intrinsics.checkNotNullParameter(controlActionCoordinator2, "controlActionCoordinator");
        Intrinsics.checkNotNullParameter(controlsMetricsLogger2, "controlsMetricsLogger");
        this.layout = viewGroup;
        this.controlsController = controlsController2;
        this.uiExecutor = delayableExecutor;
        this.bgExecutor = delayableExecutor2;
        this.controlActionCoordinator = controlActionCoordinator2;
        this.controlsMetricsLogger = controlsMetricsLogger2;
        this.uid = i;
        this.toggleBackgroundIntensity = viewGroup.getContext().getResources().getFraction(C1893R.fraction.controls_toggle_bg_intensity, 1, 1);
        View requireViewById = viewGroup.requireViewById(C1893R.C1897id.icon);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "layout.requireViewById(R.id.icon)");
        this.icon = (ImageView) requireViewById;
        View requireViewById2 = viewGroup.requireViewById(C1893R.C1897id.status);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "layout.requireViewById(R.id.status)");
        TextView textView = (TextView) requireViewById2;
        this.status = textView;
        View requireViewById3 = viewGroup.requireViewById(C1893R.C1897id.title);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "layout.requireViewById(R.id.title)");
        this.title = (TextView) requireViewById3;
        View requireViewById4 = viewGroup.requireViewById(C1893R.C1897id.subtitle);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "layout.requireViewById(R.id.subtitle)");
        this.subtitle = (TextView) requireViewById4;
        View requireViewById5 = viewGroup.requireViewById(C1893R.C1897id.chevron_icon);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "layout.requireViewById(R.id.chevron_icon)");
        this.chevronIcon = (ImageView) requireViewById5;
        Context context2 = viewGroup.getContext();
        Intrinsics.checkNotNullExpressionValue(context2, "layout.getContext()");
        this.context = context2;
        this.onDialogCancel = new ControlViewHolder$onDialogCancel$1(this);
        Drawable background = viewGroup.getBackground();
        if (background != null) {
            LayerDrawable layerDrawable = (LayerDrawable) background;
            layerDrawable.mutate();
            Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(C1893R.C1897id.clip_layer);
            if (findDrawableByLayerId != null) {
                this.clipLayer = (ClipDrawable) findDrawableByLayerId;
                Drawable findDrawableByLayerId2 = layerDrawable.findDrawableByLayerId(C1893R.C1897id.background);
                if (findDrawableByLayerId2 != null) {
                    this.baseLayer = (GradientDrawable) findDrawableByLayerId2;
                    textView.setSelected(true);
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
            }
            throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.ClipDrawable");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    public final ViewGroup getLayout() {
        return this.layout;
    }

    public final ControlsController getControlsController() {
        return this.controlsController;
    }

    public final DelayableExecutor getUiExecutor() {
        return this.uiExecutor;
    }

    public final DelayableExecutor getBgExecutor() {
        return this.bgExecutor;
    }

    public final ControlActionCoordinator getControlActionCoordinator() {
        return this.controlActionCoordinator;
    }

    public final ControlsMetricsLogger getControlsMetricsLogger() {
        return this.controlsMetricsLogger;
    }

    public final int getUid() {
        return this.uid;
    }

    @Metadata(mo64986d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J&\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u00132\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010XT¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ControlViewHolder$Companion;", "", "()V", "ALPHA_DISABLED", "", "ALPHA_ENABLED", "ATTR_DISABLED", "", "ATTR_ENABLED", "FORCE_PANEL_DEVICES", "", "MAX_LEVEL", "MIN_LEVEL", "STATE_ANIMATION_DURATION", "", "STATUS_ALPHA_DIMMED", "", "STATUS_ALPHA_ENABLED", "findBehaviorClass", "Ljava/util/function/Supplier;", "Lcom/android/systemui/controls/ui/Behavior;", "status", "template", "Landroid/service/controls/templates/ControlTemplate;", "deviceType", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.controls.ui.ControlViewHolder$Companion */
    /* compiled from: ControlViewHolder.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-0  reason: not valid java name */
        public static final StatusBehavior m2691findBehaviorClass$lambda0() {
            return new StatusBehavior();
        }

        public final Supplier<? extends Behavior> findBehaviorClass(int i, ControlTemplate controlTemplate, int i2) {
            Intrinsics.checkNotNullParameter(controlTemplate, "template");
            if (i != 1) {
                return new ControlViewHolder$Companion$$ExternalSyntheticLambda0();
            }
            if (Intrinsics.areEqual((Object) controlTemplate, (Object) ControlTemplate.NO_TEMPLATE)) {
                return new ControlViewHolder$Companion$$ExternalSyntheticLambda1();
            }
            if (controlTemplate instanceof ThumbnailTemplate) {
                return new ControlViewHolder$Companion$$ExternalSyntheticLambda2();
            }
            if (i2 == 50) {
                return new ControlViewHolder$Companion$$ExternalSyntheticLambda3();
            }
            if (controlTemplate instanceof ToggleTemplate) {
                return new ControlViewHolder$Companion$$ExternalSyntheticLambda4();
            }
            if (controlTemplate instanceof StatelessTemplate) {
                return new ControlViewHolder$Companion$$ExternalSyntheticLambda5();
            }
            if (controlTemplate instanceof ToggleRangeTemplate) {
                return new ControlViewHolder$Companion$$ExternalSyntheticLambda6();
            }
            if (controlTemplate instanceof RangeTemplate) {
                return new ControlViewHolder$Companion$$ExternalSyntheticLambda7();
            }
            return controlTemplate instanceof TemperatureControlTemplate ? new ControlViewHolder$Companion$$ExternalSyntheticLambda8() : new ControlViewHolder$Companion$$ExternalSyntheticLambda9();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-1  reason: not valid java name */
        public static final TouchBehavior m2692findBehaviorClass$lambda1() {
            return new TouchBehavior();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-2  reason: not valid java name */
        public static final ThumbnailBehavior m2693findBehaviorClass$lambda2() {
            return new ThumbnailBehavior();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-3  reason: not valid java name */
        public static final TouchBehavior m2694findBehaviorClass$lambda3() {
            return new TouchBehavior();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-4  reason: not valid java name */
        public static final ToggleBehavior m2695findBehaviorClass$lambda4() {
            return new ToggleBehavior();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-5  reason: not valid java name */
        public static final TouchBehavior m2696findBehaviorClass$lambda5() {
            return new TouchBehavior();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-6  reason: not valid java name */
        public static final ToggleRangeBehavior m2697findBehaviorClass$lambda6() {
            return new ToggleRangeBehavior();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-7  reason: not valid java name */
        public static final ToggleRangeBehavior m2698findBehaviorClass$lambda7() {
            return new ToggleRangeBehavior();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-8  reason: not valid java name */
        public static final TemperatureControlBehavior m2699findBehaviorClass$lambda8() {
            return new TemperatureControlBehavior();
        }

        /* access modifiers changed from: private */
        /* renamed from: findBehaviorClass$lambda-9  reason: not valid java name */
        public static final DefaultBehavior m2700findBehaviorClass$lambda9() {
            return new DefaultBehavior();
        }
    }

    public final ImageView getIcon() {
        return this.icon;
    }

    public final TextView getStatus() {
        return this.status;
    }

    public final TextView getTitle() {
        return this.title;
    }

    public final TextView getSubtitle() {
        return this.subtitle;
    }

    public final ImageView getChevronIcon() {
        return this.chevronIcon;
    }

    public final Context getContext() {
        return this.context;
    }

    public final ClipDrawable getClipLayer() {
        return this.clipLayer;
    }

    public final ControlWithState getCws() {
        ControlWithState controlWithState = this.cws;
        if (controlWithState != null) {
            return controlWithState;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cws");
        return null;
    }

    public final void setCws(ControlWithState controlWithState) {
        Intrinsics.checkNotNullParameter(controlWithState, "<set-?>");
        this.cws = controlWithState;
    }

    public final Behavior getBehavior() {
        return this.behavior;
    }

    public final void setBehavior(Behavior behavior2) {
        this.behavior = behavior2;
    }

    public final ControlAction getLastAction() {
        return this.lastAction;
    }

    public final void setLastAction(ControlAction controlAction) {
        this.lastAction = controlAction;
    }

    public final boolean isLoading() {
        return this.isLoading;
    }

    public final void setLoading(boolean z) {
        this.isLoading = z;
    }

    public final Dialog getVisibleDialog() {
        return this.visibleDialog;
    }

    public final void setVisibleDialog(Dialog dialog) {
        this.visibleDialog = dialog;
    }

    public final int getDeviceType() {
        Control control = getCws().getControl();
        return control != null ? control.getDeviceType() : getCws().getCi().getDeviceType();
    }

    public final int getControlStatus() {
        Control control = getCws().getControl();
        if (control != null) {
            return control.getStatus();
        }
        return 0;
    }

    public final ControlTemplate getControlTemplate() {
        Control control = getCws().getControl();
        ControlTemplate controlTemplate = control != null ? control.getControlTemplate() : null;
        if (controlTemplate != null) {
            return controlTemplate;
        }
        ControlTemplate controlTemplate2 = ControlTemplate.NO_TEMPLATE;
        Intrinsics.checkNotNullExpressionValue(controlTemplate2, "NO_TEMPLATE");
        return controlTemplate2;
    }

    public final boolean getUserInteractionInProgress() {
        return this.userInteractionInProgress;
    }

    public final void setUserInteractionInProgress(boolean z) {
        this.userInteractionInProgress = z;
    }

    public final void bindData(ControlWithState controlWithState, boolean z) {
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        if (!this.userInteractionInProgress) {
            setCws(controlWithState);
            boolean z2 = false;
            if (getControlStatus() == 0 || getControlStatus() == 2) {
                this.title.setText(controlWithState.getCi().getControlTitle());
                this.subtitle.setText(controlWithState.getCi().getControlSubtitle());
            } else {
                Control control = controlWithState.getControl();
                if (control != null) {
                    this.title.setText(control.getTitle());
                    this.subtitle.setText(control.getSubtitle());
                    this.chevronIcon.setVisibility(usePanel() ? 0 : 4);
                }
            }
            if (controlWithState.getControl() != null) {
                this.layout.setClickable(true);
                this.layout.setOnLongClickListener(new ControlViewHolder$$ExternalSyntheticLambda1(this));
                this.controlActionCoordinator.runPendingAction(controlWithState.getCi().getControlId());
            }
            boolean z3 = this.isLoading;
            this.isLoading = false;
            this.behavior = bindBehavior$default(this, this.behavior, Companion.findBehaviorClass(getControlStatus(), getControlTemplate(), getDeviceType()), 0, 4, (Object) null);
            updateContentDescription();
            if (z3 && !this.isLoading) {
                z2 = true;
            }
            if (z2) {
                this.controlsMetricsLogger.refreshEnd(this, z);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: bindData$lambda-5$lambda-4  reason: not valid java name */
    public static final boolean m2685bindData$lambda5$lambda4(ControlViewHolder controlViewHolder, View view) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "this$0");
        controlViewHolder.controlActionCoordinator.longPress(controlViewHolder);
        return true;
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
            if (createConfirmationDialog != null) {
                createConfirmationDialog.show();
            }
        } else if (i == 4) {
            Dialog createPinDialog = ChallengeDialogs.INSTANCE.createPinDialog(this, false, z, this.onDialogCancel);
            this.lastChallengeDialog = createPinDialog;
            if (createPinDialog != null) {
                createPinDialog.show();
            }
        } else if (i == 5) {
            Dialog createPinDialog2 = ChallengeDialogs.INSTANCE.createPinDialog(this, true, z, this.onDialogCancel);
            this.lastChallengeDialog = createPinDialog2;
            if (createPinDialog2 != null) {
                createPinDialog2.show();
            }
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
        animateStatusChange(true, new ControlViewHolder$setErrorStatus$1(this, this.context.getResources().getString(C1893R.string.controls_error_failed)));
    }

    private final void updateContentDescription() {
        this.layout.setContentDescription(new StringBuilder().append((Object) this.title.getText()).append(' ').append((Object) this.subtitle.getText()).append(' ').append((Object) this.status.getText()).toString());
    }

    public final void action(ControlAction controlAction) {
        Intrinsics.checkNotNullParameter(controlAction, "action");
        this.lastAction = controlAction;
        this.controlsController.action(getCws().getComponentName(), getCws().getCi(), controlAction);
    }

    public final boolean usePanel() {
        return FORCE_PANEL_DEVICES.contains(Integer.valueOf(getDeviceType())) || Intrinsics.areEqual((Object) getControlTemplate(), (Object) ControlTemplate.NO_TEMPLATE);
    }

    public static /* synthetic */ Behavior bindBehavior$default(ControlViewHolder controlViewHolder, Behavior behavior2, Supplier supplier, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return controlViewHolder.bindBehavior(behavior2, supplier, i);
    }

    public final Behavior bindBehavior(Behavior behavior2, Supplier<? extends Behavior> supplier, int i) {
        Intrinsics.checkNotNullParameter(supplier, "supplier");
        Behavior behavior3 = (Behavior) supplier.get();
        if (behavior2 == null || !Intrinsics.areEqual((Object) Reflection.getOrCreateKotlinClass(behavior2.getClass()), (Object) Reflection.getOrCreateKotlinClass(behavior3.getClass()))) {
            behavior3.initialize(this);
            this.layout.setAccessibilityDelegate((View.AccessibilityDelegate) null);
            behavior2 = behavior3;
        }
        behavior2.bind(getCws(), i);
        return behavior2;
    }

    public static /* synthetic */ void applyRenderInfo$SystemUI_nothingRelease$default(ControlViewHolder controlViewHolder, boolean z, int i, boolean z2, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z2 = true;
        }
        controlViewHolder.applyRenderInfo$SystemUI_nothingRelease(z, i, z2);
    }

    public final void applyRenderInfo$SystemUI_nothingRelease(boolean z, int i, boolean z2) {
        int i2;
        if (getControlStatus() == 1 || getControlStatus() == 0) {
            i2 = getDeviceType();
        } else {
            i2 = -1000;
        }
        RenderInfo lookup = RenderInfo.Companion.lookup(this.context, getCws().getComponentName(), i2, i);
        ColorStateList colorStateList = this.context.getResources().getColorStateList(lookup.getForeground(), this.context.getTheme());
        CharSequence charSequence = this.nextStatusText;
        Control control = getCws().getControl();
        if (Intrinsics.areEqual((Object) charSequence, (Object) this.status.getText())) {
            z2 = false;
        }
        animateStatusChange(z2, new ControlViewHolder$applyRenderInfo$1(this, z, charSequence, lookup, colorStateList, control));
        animateBackgroundChange(z2, z, lookup.getEnabledBackground());
    }

    public final CharSequence getStatusText() {
        return this.status.getText();
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

    public final void setStatusText(CharSequence charSequence, boolean z) {
        Intrinsics.checkNotNullParameter(charSequence, "text");
        if (z) {
            this.status.setAlpha(1.0f);
            this.status.setText(charSequence);
            updateContentDescription();
        }
        this.nextStatusText = charSequence;
    }

    private final void animateBackgroundChange(boolean z, boolean z2, int i) {
        List list;
        int i2;
        ColorStateList customColor;
        int color = this.context.getResources().getColor(C1893R.C1894color.control_default_background, this.context.getTheme());
        if (z2) {
            Control control = getCws().getControl();
            if (control == null || (customColor = control.getCustomColor()) == null) {
                i2 = this.context.getResources().getColor(i, this.context.getTheme());
            } else {
                i2 = customColor.getColorForState(new int[]{16842910}, customColor.getDefaultColor());
            }
            list = CollectionsKt.listOf(Integer.valueOf(i2), 255);
        } else {
            list = CollectionsKt.listOf(Integer.valueOf(this.context.getResources().getColor(C1893R.C1894color.control_default_background, this.context.getTheme())), 0);
        }
        int intValue = ((Number) list.get(0)).intValue();
        int intValue2 = ((Number) list.get(1)).intValue();
        if (this.behavior instanceof ToggleRangeBehavior) {
            color = ColorUtils.blendARGB(color, intValue, this.toggleBackgroundIntensity);
        }
        int i3 = color;
        Drawable drawable = this.clipLayer.getDrawable();
        if (drawable != null) {
            this.clipLayer.setAlpha(0);
            ValueAnimator valueAnimator = this.stateAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (z) {
                startBackgroundAnimation(drawable, intValue2, intValue, i3);
            } else {
                applyBackgroundChange(drawable, intValue2, intValue, i3, 1.0f);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = ((android.graphics.drawable.GradientDrawable) r10).getColor();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void startBackgroundAnimation(android.graphics.drawable.Drawable r10, int r11, int r12, int r13) {
        /*
            r9 = this;
            boolean r0 = r10 instanceof android.graphics.drawable.GradientDrawable
            if (r0 == 0) goto L_0x0013
            r0 = r10
            android.graphics.drawable.GradientDrawable r0 = (android.graphics.drawable.GradientDrawable) r0
            android.content.res.ColorStateList r0 = r0.getColor()
            if (r0 == 0) goto L_0x0013
            int r0 = r0.getDefaultColor()
            r2 = r0
            goto L_0x0014
        L_0x0013:
            r2 = r12
        L_0x0014:
            android.graphics.drawable.GradientDrawable r0 = r9.baseLayer
            android.content.res.ColorStateList r0 = r0.getColor()
            if (r0 == 0) goto L_0x0022
            int r0 = r0.getDefaultColor()
            r4 = r0
            goto L_0x0023
        L_0x0022:
            r4 = r13
        L_0x0023:
            android.view.ViewGroup r0 = r9.layout
            float r6 = r0.getAlpha()
            r0 = 2
            int[] r0 = new int[r0]
            android.graphics.drawable.ClipDrawable r1 = r9.clipLayer
            int r1 = r1.getAlpha()
            r3 = 0
            r0[r3] = r1
            r1 = 1
            r0[r1] = r11
            android.animation.ValueAnimator r11 = android.animation.ValueAnimator.ofInt(r0)
            com.android.systemui.controls.ui.ControlViewHolder$$ExternalSyntheticLambda0 r0 = new com.android.systemui.controls.ui.ControlViewHolder$$ExternalSyntheticLambda0
            r1 = r0
            r3 = r12
            r5 = r13
            r7 = r9
            r8 = r10
            r1.<init>(r2, r3, r4, r5, r6, r7, r8)
            r11.addUpdateListener(r0)
            com.android.systemui.controls.ui.ControlViewHolder$startBackgroundAnimation$1$2 r10 = new com.android.systemui.controls.ui.ControlViewHolder$startBackgroundAnimation$1$2
            r10.<init>(r9)
            android.animation.Animator$AnimatorListener r10 = (android.animation.Animator.AnimatorListener) r10
            r11.addListener(r10)
            r12 = 700(0x2bc, double:3.46E-321)
            r11.setDuration(r12)
            android.view.animation.Interpolator r10 = com.android.systemui.animation.Interpolators.CONTROL_STATE
            android.animation.TimeInterpolator r10 = (android.animation.TimeInterpolator) r10
            r11.setInterpolator(r10)
            r11.start()
            r9.stateAnimator = r11
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.p010ui.ControlViewHolder.startBackgroundAnimation(android.graphics.drawable.Drawable, int, int, int):void");
    }

    /* access modifiers changed from: private */
    /* renamed from: startBackgroundAnimation$lambda-10$lambda-9  reason: not valid java name */
    public static final void m2686startBackgroundAnimation$lambda10$lambda9(int i, int i2, int i3, int i4, float f, ControlViewHolder controlViewHolder, Drawable drawable, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "this$0");
        Intrinsics.checkNotNullParameter(drawable, "$clipDrawable");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            controlViewHolder.applyBackgroundChange(drawable, ((Integer) animatedValue).intValue(), ColorUtils.blendARGB(i, i2, valueAnimator.getAnimatedFraction()), ColorUtils.blendARGB(i3, i4, valueAnimator.getAnimatedFraction()), MathUtils.lerp(f, 1.0f, valueAnimator.getAnimatedFraction()));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    private final void applyBackgroundChange(Drawable drawable, int i, int i2, int i3, float f) {
        drawable.setAlpha(i);
        if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(i2);
        }
        this.baseLayer.setColor(i3);
        this.layout.setAlpha(f);
    }

    private final void animateStatusChange(boolean z, Function0<Unit> function0) {
        Animator animator = this.statusAnimator;
        if (animator != null) {
            animator.cancel();
        }
        if (!z) {
            function0.invoke();
        } else if (this.isLoading) {
            function0.invoke();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.status, Key.ALPHA, new float[]{0.45f});
            ofFloat.setRepeatMode(2);
            ofFloat.setRepeatCount(-1);
            ofFloat.setDuration(500);
            ofFloat.setInterpolator(Interpolators.LINEAR);
            ofFloat.setStartDelay(900);
            ofFloat.start();
            this.statusAnimator = ofFloat;
        } else {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.status, Key.ALPHA, new float[]{0.0f});
            ofFloat2.setDuration(200);
            ofFloat2.setInterpolator(Interpolators.LINEAR);
            ofFloat2.addListener(new ControlViewHolder$animateStatusChange$fadeOut$1$1(function0));
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.status, Key.ALPHA, new float[]{1.0f});
            ofFloat3.setDuration(200);
            ofFloat3.setInterpolator(Interpolators.LINEAR);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(new Animator[]{ofFloat2, ofFloat3});
            animatorSet.addListener(new ControlViewHolder$animateStatusChange$2$1(this));
            animatorSet.start();
            this.statusAnimator = animatorSet;
        }
    }

    public final void updateStatusRow$SystemUI_nothingRelease(boolean z, CharSequence charSequence, Drawable drawable, ColorStateList colorStateList, Control control) {
        Unit unit;
        Icon customIcon;
        Intrinsics.checkNotNullParameter(charSequence, "text");
        Intrinsics.checkNotNullParameter(drawable, "drawable");
        Intrinsics.checkNotNullParameter(colorStateList, "color");
        setEnabled(z);
        this.status.setText(charSequence);
        updateContentDescription();
        this.status.setTextColor(colorStateList);
        if (control == null || (customIcon = control.getCustomIcon()) == null) {
            unit = null;
        } else {
            this.icon.setImageIcon(customIcon);
            this.icon.setImageTintList(customIcon.getTintList());
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            ControlViewHolder controlViewHolder = this;
            if (drawable instanceof StateListDrawable) {
                if (this.icon.getDrawable() == null || !(this.icon.getDrawable() instanceof StateListDrawable)) {
                    this.icon.setImageDrawable(drawable);
                }
                this.icon.setImageState(z ? ATTR_ENABLED : ATTR_DISABLED, true);
            } else {
                this.icon.setImageDrawable(drawable);
            }
            if (getDeviceType() != 52) {
                this.icon.setImageTintList(colorStateList);
            }
        }
        this.chevronIcon.setImageTintList(this.icon.getImageTintList());
    }

    private final void setEnabled(boolean z) {
        this.status.setEnabled(z);
        this.icon.setEnabled(z);
        this.chevronIcon.setEnabled(z);
    }
}
