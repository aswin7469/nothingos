package com.android.systemui.qs.tileimpl;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.tileimpl.HeightOverrideable;
import com.nothingos.systemui.qs.CircleTileTextView;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: QSTileViewImpl.kt */
/* loaded from: classes.dex */
public class QSTileViewImpl extends QSTileView implements HeightOverrideable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final QSIconView _icon;
    @Nullable
    private String accessibilityClass;
    private ImageView chevronView;
    private final boolean collapsed;
    private int colorActive;
    protected Drawable colorBackgroundDrawable;
    private int colorInactive;
    private int colorInactiveCircle;
    private int colorLabelActive;
    private int colorLabelInactive;
    private int colorSecondaryLabelActive;
    private int colorSecondaryLabelInactive;
    private ImageView customDrawableView;
    private boolean isSignalOrBt;
    private boolean isTesla;
    protected TextView label;
    protected IgnorableChildLinearLayout labelContainer;
    @Nullable
    private CharSequence lastStateDescription;
    private int paintColor;
    protected RippleDrawable ripple;
    protected TextView secondaryLabel;
    private boolean showRippleEffect;
    protected ViewGroup sideView;
    @NotNull
    private final ValueAnimator singleAnimator;
    @Nullable
    private CharSequence stateDescriptionDeltas;
    private boolean tileState;
    private int heightOverride = -1;
    private int colorUnavailable = Utils.applyAlpha(0.3f, getColorInactive());
    private int colorLabelUnavailable = Utils.applyAlpha(0.3f, getColorLabelInactive());
    private int colorSecondaryLabelUnavailable = Utils.applyAlpha(0.3f, getColorSecondaryLabelInactive());
    private int lastState = -1;
    @NotNull
    private final int[] locInScreen = new int[2];
    private int colorUnavailableCircle = Utils.applyAlpha(0.3f, getColorInactiveCircle());

    public int getTileGravity() {
        return 8388627;
    }

    public int getTileOrientation() {
        return 0;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.qs.tileimpl.HeightOverrideable
    public void resetOverride() {
        HeightOverrideable.DefaultImpls.resetOverride(this);
    }

    @NotNull
    public final QSIconView get_icon() {
        return this._icon;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public QSTileViewImpl(@NotNull Context context, @NotNull QSIconView _icon, boolean z) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(_icon, "_icon");
        this._icon = _icon;
        this.collapsed = z;
        this.colorActive = Utils.getColorAttrDefaultColor(context, 17956900);
        this.colorInactive = Utils.getColorAttrDefaultColor(context, R$attr.offStateColor);
        this.colorLabelActive = Utils.getColorAttrDefaultColor(context, 16842809);
        this.colorLabelInactive = Utils.getColorAttrDefaultColor(context, 16842806);
        this.colorSecondaryLabelActive = Utils.getColorAttrDefaultColor(context, 16842810);
        this.colorSecondaryLabelInactive = Utils.getColorAttrDefaultColor(context, 16842808);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.qs.tileimpl.QSTileViewImpl$singleAnimator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                QSTileViewImpl qSTileViewImpl = QSTileViewImpl.this;
                Object animatedValue = valueAnimator2.getAnimatedValue("background");
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                int intValue = ((Integer) animatedValue).intValue();
                Object animatedValue2 = valueAnimator2.getAnimatedValue("label");
                Objects.requireNonNull(animatedValue2, "null cannot be cast to non-null type kotlin.Int");
                int intValue2 = ((Integer) animatedValue2).intValue();
                Object animatedValue3 = valueAnimator2.getAnimatedValue("secondaryLabel");
                Objects.requireNonNull(animatedValue3, "null cannot be cast to non-null type kotlin.Int");
                int intValue3 = ((Integer) animatedValue3).intValue();
                Object animatedValue4 = valueAnimator2.getAnimatedValue("chevron");
                Objects.requireNonNull(animatedValue4, "null cannot be cast to non-null type kotlin.Int");
                qSTileViewImpl.setAllColors(intValue, intValue2, intValue3, ((Integer) animatedValue4).intValue());
            }
        });
        Unit unit = Unit.INSTANCE;
        this.singleAnimator = valueAnimator;
        setId(LinearLayout.generateViewId());
        setOrientation(getTileOrientation());
        setGravity(getTileGravity());
        setImportantForAccessibility(1);
        setClipChildren(false);
        setClipToPadding(false);
        setFocusable(true);
        setBackground(createTileBackground());
        setColor(getBackgroundColorForState(2));
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.qs_tile_padding);
        setPaddingRelative(getResources().getDimensionPixelSize(R$dimen.qs_tile_start_padding), dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        dealAndAddIcon();
        createAndAddLabels();
        createAndAddSideView();
        this.colorInactiveCircle = Utils.getColorAttrDefaultColor(context, R$attr.circleOffStateColor);
    }

    /* compiled from: QSTileViewImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getTILE_STATE_RES_PREFIX$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
        }

        private Companion() {
        }
    }

    public int getHeightOverride() {
        return this.heightOverride;
    }

    @Override // com.android.systemui.qs.tileimpl.HeightOverrideable
    public void setHeightOverride(int i) {
        this.heightOverride = i;
    }

    public int getColorActive() {
        return this.colorActive;
    }

    public int getColorInactive() {
        return this.colorInactive;
    }

    public int getColorUnavailable() {
        return this.colorUnavailable;
    }

    public int getColorLabelActive() {
        return this.colorLabelActive;
    }

    public int getColorLabelInactive() {
        return this.colorLabelInactive;
    }

    public int getColorLabelUnavailable() {
        return this.colorLabelUnavailable;
    }

    public int getColorSecondaryLabelActive() {
        return this.colorSecondaryLabelActive;
    }

    public int getColorSecondaryLabelInactive() {
        return this.colorSecondaryLabelInactive;
    }

    public int getColorSecondaryLabelUnavailable() {
        return this.colorSecondaryLabelUnavailable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    /* renamed from: getLabel  reason: collision with other method in class */
    public final TextView mo811getLabel() {
        TextView textView = this.label;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("label");
        throw null;
    }

    protected final void setLabel(@NotNull TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.label = textView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    /* renamed from: getSecondaryLabel  reason: collision with other method in class */
    public final TextView mo813getSecondaryLabel() {
        TextView textView = this.secondaryLabel;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("secondaryLabel");
        throw null;
    }

    protected final void setSecondaryLabel(@NotNull TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.secondaryLabel = textView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    /* renamed from: getLabelContainer  reason: collision with other method in class */
    public final IgnorableChildLinearLayout mo812getLabelContainer() {
        IgnorableChildLinearLayout ignorableChildLinearLayout = this.labelContainer;
        if (ignorableChildLinearLayout != null) {
            return ignorableChildLinearLayout;
        }
        Intrinsics.throwUninitializedPropertyAccessException("labelContainer");
        throw null;
    }

    protected final void setLabelContainer(@NotNull IgnorableChildLinearLayout ignorableChildLinearLayout) {
        Intrinsics.checkNotNullParameter(ignorableChildLinearLayout, "<set-?>");
        this.labelContainer = ignorableChildLinearLayout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NotNull
    public final ViewGroup getSideView() {
        ViewGroup viewGroup = this.sideView;
        if (viewGroup != null) {
            return viewGroup;
        }
        Intrinsics.throwUninitializedPropertyAccessException("sideView");
        throw null;
    }

    protected final void setSideView(@NotNull ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<set-?>");
        this.sideView = viewGroup;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setShowRippleEffect(boolean z) {
        this.showRippleEffect = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NotNull
    public final RippleDrawable getRipple() {
        RippleDrawable rippleDrawable = this.ripple;
        if (rippleDrawable != null) {
            return rippleDrawable;
        }
        Intrinsics.throwUninitializedPropertyAccessException("ripple");
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setRipple(@NotNull RippleDrawable rippleDrawable) {
        Intrinsics.checkNotNullParameter(rippleDrawable, "<set-?>");
        this.ripple = rippleDrawable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NotNull
    public final Drawable getColorBackgroundDrawable() {
        Drawable drawable = this.colorBackgroundDrawable;
        if (drawable != null) {
            return drawable;
        }
        Intrinsics.throwUninitializedPropertyAccessException("colorBackgroundDrawable");
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setColorBackgroundDrawable(@NotNull Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "<set-?>");
        this.colorBackgroundDrawable = drawable;
    }

    public final int getLastState() {
        return this.lastState;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(@Nullable Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    public void updateResources() {
        TextView mo811getLabel = mo811getLabel();
        int i = R$dimen.qs_tile_text_size;
        FontSizeUtils.updateFontSize(mo811getLabel, i);
        FontSizeUtils.updateFontSize(mo813getSecondaryLabel(), i);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
        ViewGroup.LayoutParams layoutParams = this._icon.getLayoutParams();
        layoutParams.height = dimensionPixelSize;
        layoutParams.width = dimensionPixelSize;
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.qs_tile_padding);
        setPaddingRelative(getResources().getDimensionPixelSize(R$dimen.qs_tile_start_padding), dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize2);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R$dimen.qs_label_container_margin);
        if (getTileOrientation() == 0) {
            ViewGroup.LayoutParams layoutParams2 = mo812getLabelContainer().getLayoutParams();
            Objects.requireNonNull(layoutParams2, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            ((ViewGroup.MarginLayoutParams) layoutParams2).setMarginStart(dimensionPixelSize3);
        }
        ViewGroup.LayoutParams layoutParams3 = getSideView().getLayoutParams();
        Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        ((ViewGroup.MarginLayoutParams) layoutParams3).setMarginStart(dimensionPixelSize3);
        ImageView imageView = this.chevronView;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("chevronView");
            throw null;
        }
        ViewGroup.LayoutParams layoutParams4 = imageView.getLayoutParams();
        Objects.requireNonNull(layoutParams4, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams4;
        marginLayoutParams.height = dimensionPixelSize;
        marginLayoutParams.width = dimensionPixelSize;
        int dimensionPixelSize4 = getResources().getDimensionPixelSize(R$dimen.qs_drawable_end_margin);
        ImageView imageView2 = this.customDrawableView;
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
            throw null;
        }
        ViewGroup.LayoutParams layoutParams5 = imageView2.getLayoutParams();
        Objects.requireNonNull(layoutParams5, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams5;
        marginLayoutParams2.height = dimensionPixelSize;
        marginLayoutParams2.setMarginEnd(dimensionPixelSize4);
    }

    public void createAndAddLabels() {
        View inflate = LayoutInflater.from(getContext()).inflate(getTileOrientation() == 0 ? R$layout.qs_tile_label : R$layout.qs_tile_label_circle, (ViewGroup) this, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout");
        setLabelContainer((IgnorableChildLinearLayout) inflate);
        View requireViewById = mo812getLabelContainer().requireViewById(R$id.tile_label);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "labelContainer.requireViewById(R.id.tile_label)");
        setLabel((TextView) requireViewById);
        View requireViewById2 = mo812getLabelContainer().requireViewById(R$id.app_label);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "labelContainer.requireViewById(R.id.app_label)");
        setSecondaryLabel((TextView) requireViewById2);
        if (this.collapsed) {
            mo812getLabelContainer().setIgnoreLastView(true);
            mo812getLabelContainer().setForceUnspecifiedMeasure(true);
            mo813getSecondaryLabel().setAlpha(0.0f);
        }
        setLabelColor(getLabelColorForState(2));
        setSecondaryLabelColor(getSecondaryLabelColorForState(2));
        addView(mo812getLabelContainer());
    }

    private final void createAndAddSideView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.qs_tile_side_icon, (ViewGroup) this, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        setSideView((ViewGroup) inflate);
        View requireViewById = getSideView().requireViewById(R$id.customDrawable);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "sideView.requireViewById(R.id.customDrawable)");
        this.customDrawableView = (ImageView) requireViewById;
        View requireViewById2 = getSideView().requireViewById(R$id.chevron);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "sideView.requireViewById(R.id.chevron)");
        this.chevronView = (ImageView) requireViewById2;
        setChevronColor(getChevronColorForState(2));
        if (getTileOrientation() != 1) {
            addView(getSideView());
        }
    }

    @NotNull
    public Drawable createTileBackground() {
        Drawable drawable = ((LinearLayout) this).mContext.getDrawable(R$drawable.qs_tile_background);
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
        setRipple((RippleDrawable) drawable);
        Drawable findDrawableByLayerId = getRipple().findDrawableByLayerId(R$id.background);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ripple.findDrawableByLayerId(R.id.background)");
        setColorBackgroundDrawable(findDrawableByLayerId);
        return getColorBackgroundDrawable();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (getHeightOverride() != -1) {
            setBottom(getTop() + getHeightOverride());
        }
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    public View updateAccessibilityOrder(@Nullable View view) {
        setAccessibilityTraversalAfter(view == null ? 0 : view.getId());
        return this;
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    public QSIconView getIcon() {
        return this._icon;
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    public View getIconWithBackground() {
        return getIcon();
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public void init(@NotNull final QSTile tile) {
        Intrinsics.checkNotNullParameter(tile, "tile");
        init(new View.OnClickListener() { // from class: com.android.systemui.qs.tileimpl.QSTileViewImpl$init$1
            @Override // android.view.View.OnClickListener
            public final void onClick(@Nullable View view) {
                QSTile.this.click(this);
            }
        }, new View.OnLongClickListener() { // from class: com.android.systemui.qs.tileimpl.QSTileViewImpl$init$2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(@Nullable View view) {
                if (QSTileViewImpl.this.isTesla()) {
                    QSTileViewImpl.this.setTag(R$id.qs_tesla_tag, "tesla");
                }
                tile.longClick(QSTileViewImpl.this);
                return true;
            }
        });
        boolean z = QSTileHost.isSignalTile(tile.getTileSpec()) || QSTileHost.isBluetoothTile(tile.getTileSpec());
        this.isSignalOrBt = z;
        if (z) {
            ((CircleTileTextView) mo811getLabel()).setIsLabel(true);
            ((CircleTileTextView) mo813getSecondaryLabel()).setIsSecondLabel(true);
        }
    }

    private final void init(View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        setOnClickListener(onClickListener);
        setOnLongClickListener(onLongClickListener);
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public void onStateChanged(@NotNull final QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        post(new Runnable() { // from class: com.android.systemui.qs.tileimpl.QSTileViewImpl$onStateChanged$1
            @Override // java.lang.Runnable
            public final void run() {
                QSTileViewImpl.this.handleStateChanged(state);
            }
        });
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public int getDetailY() {
        return getTop() + (getHeight() / 2);
    }

    @Override // android.view.View
    public void setClickable(boolean z) {
        super.setClickable(z);
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    /* renamed from: getLabelContainer */
    public View mo812getLabelContainer() {
        return mo812getLabelContainer();
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    /* renamed from: getSecondaryLabel */
    public View mo813getSecondaryLabel() {
        return mo813getSecondaryLabel();
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    public View getSecondaryIcon() {
        return getSideView();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(@NotNull AccessibilityEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        super.onInitializeAccessibilityEvent(event);
        if (!TextUtils.isEmpty(this.accessibilityClass)) {
            event.setClassName(this.accessibilityClass);
        }
        if (event.getContentChangeTypes() != 64 || this.stateDescriptionDeltas == null) {
            return;
        }
        event.getText().add(this.stateDescriptionDeltas);
        this.stateDescriptionDeltas = null;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(@NotNull AccessibilityNodeInfo info) {
        Intrinsics.checkNotNullParameter(info, "info");
        super.onInitializeAccessibilityNodeInfo(info);
        info.setSelected(false);
        if (!TextUtils.isEmpty(this.accessibilityClass)) {
            info.setClassName(this.accessibilityClass);
            if (!Intrinsics.areEqual(Switch.class.getName(), this.accessibilityClass)) {
                return;
            }
            info.setText(getResources().getString(this.tileState ? R$string.switch_bar_on : R$string.switch_bar_off));
            info.setChecked(this.tileState);
            info.setCheckable(true);
            if (!isLongClickable()) {
                return;
            }
            info.addAction(new AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK.getId(), getResources().getString(R$string.accessibility_long_click_tile)));
        }
    }

    @Override // android.view.View
    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append('[');
        sb.append("locInScreen=(" + this.locInScreen[0] + ", " + this.locInScreen[1] + ')');
        sb.append(Intrinsics.stringPlus(", iconView=", this._icon));
        sb.append(Intrinsics.stringPlus(", tileState=", Boolean.valueOf(this.tileState)));
        sb.append(Intrinsics.stringPlus(", lastState=", Integer.valueOf(this.lastState)));
        sb.append(Intrinsics.stringPlus(", des=", getContentDescription()));
        sb.append(Intrinsics.stringPlus(", clickable=", Boolean.valueOf(isClickable())));
        sb.append(Intrinsics.stringPlus(", labelVis=", Integer.valueOf(mo811getLabel().getVisibility())));
        sb.append(Intrinsics.stringPlus(", labelText=", mo811getLabel().getText()));
        sb.append(Intrinsics.stringPlus(", iconVis=", Integer.valueOf(getIcon().getVisibility())));
        sb.append(Intrinsics.stringPlus(", ripple=", Boolean.valueOf(this.showRippleEffect)));
        sb.append(Intrinsics.stringPlus(", isSignalOrBt=", Boolean.valueOf(this.isSignalOrBt)));
        sb.append(Intrinsics.stringPlus(", bg=", getBackground()));
        sb.append(Intrinsics.stringPlus(", colorActive=", Integer.toHexString(getColorActive())));
        sb.append(Intrinsics.stringPlus(", colorLabelActive=", Integer.toHexString(getColorLabelActive())));
        sb.append("]");
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleStateChanged(@NotNull QSTile.State state) {
        PropertyValuesHolder colorValuesHolder;
        PropertyValuesHolder colorValuesHolder2;
        PropertyValuesHolder colorValuesHolder3;
        PropertyValuesHolder colorValuesHolder4;
        boolean z;
        Intrinsics.checkNotNullParameter(state, "state");
        boolean animationsEnabled = animationsEnabled();
        this.showRippleEffect = false;
        setClickable(state.state != 0);
        setLongClickable(state.handlesLongClick);
        getIcon().setIcon(state, animationsEnabled);
        getIcon().setCircleIconBgColor(getCircleIconBackgroundColorForState(state.state));
        QSTile.Icon icon = state.icon;
        if (icon != null) {
            this.isTesla = icon.isTesla;
        }
        setContentDescription(state.contentDescription);
        StringBuilder sb = new StringBuilder();
        String stateText = getStateText(state);
        if (!TextUtils.isEmpty(stateText)) {
            sb.append(stateText);
            if (TextUtils.isEmpty(state.secondaryLabel)) {
                state.secondaryLabel = stateText;
            }
        }
        if (!TextUtils.isEmpty(state.stateDescription)) {
            sb.append(", ");
            sb.append(state.stateDescription);
            int i = this.lastState;
            if (i != -1 && state.state == i && !Intrinsics.areEqual(state.stateDescription, this.lastStateDescription)) {
                this.stateDescriptionDeltas = state.stateDescription;
            }
        }
        setStateDescription(sb.toString());
        this.lastStateDescription = state.stateDescription;
        this.accessibilityClass = state.state == 0 ? null : state.expandedAccessibilityClassName;
        if ((state instanceof QSTile.BooleanState) && this.tileState != (z = ((QSTile.BooleanState) state).value)) {
            this.tileState = z;
        }
        if (!Objects.equals(mo811getLabel().getText(), state.label)) {
            mo811getLabel().setText(state.label);
        }
        if (!Objects.equals(mo813getSecondaryLabel().getText(), state.secondaryLabel)) {
            mo813getSecondaryLabel().setText(state.secondaryLabel);
            mo813getSecondaryLabel().setVisibility(TextUtils.isEmpty(state.secondaryLabel) ? 8 : 0);
        }
        if (state.state != this.lastState) {
            this.singleAnimator.cancel();
            if (animationsEnabled) {
                ValueAnimator valueAnimator = this.singleAnimator;
                PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[4];
                colorValuesHolder = QSTileViewImplKt.colorValuesHolder("background", this.paintColor, getBackgroundColorForState(state.state));
                propertyValuesHolderArr[0] = colorValuesHolder;
                colorValuesHolder2 = QSTileViewImplKt.colorValuesHolder("label", mo811getLabel().getCurrentTextColor(), getLabelColorForState(state.state));
                propertyValuesHolderArr[1] = colorValuesHolder2;
                colorValuesHolder3 = QSTileViewImplKt.colorValuesHolder("secondaryLabel", mo813getSecondaryLabel().getCurrentTextColor(), getSecondaryLabelColorForState(state.state));
                propertyValuesHolderArr[2] = colorValuesHolder3;
                int[] iArr = new int[2];
                ImageView imageView = this.chevronView;
                if (imageView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                    throw null;
                }
                ColorStateList imageTintList = imageView.getImageTintList();
                iArr[0] = imageTintList == null ? 0 : imageTintList.getDefaultColor();
                iArr[1] = getChevronColorForState(state.state);
                colorValuesHolder4 = QSTileViewImplKt.colorValuesHolder("chevron", iArr);
                propertyValuesHolderArr[3] = colorValuesHolder4;
                valueAnimator.setValues(propertyValuesHolderArr);
                this.singleAnimator.start();
            } else {
                setAllColors(getBackgroundColorForState(state.state), getLabelColorForState(state.state), getSecondaryLabelColorForState(state.state), getChevronColorForState(state.state));
            }
        }
        loadSideViewDrawableIfNecessary(state);
        mo811getLabel().setEnabled(!state.disabledByPolicy);
        this.lastState = state.state;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setAllColors(int i, int i2, int i3, int i4) {
        setColor(i);
        setLabelColor(i2);
        setSecondaryLabelColor(i3);
        setChevronColor(i4);
    }

    public final void setColor(int i) {
        getColorBackgroundDrawable().setTint(i);
        this.paintColor = i;
    }

    private final void setLabelColor(int i) {
        mo811getLabel().setTextColor(i);
    }

    private final void setSecondaryLabelColor(int i) {
        mo813getSecondaryLabel().setTextColor(i);
    }

    private final void setChevronColor(int i) {
        ImageView imageView = this.chevronView;
        if (imageView != null) {
            imageView.setImageTintList(ColorStateList.valueOf(i));
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("chevronView");
            throw null;
        }
    }

    private final void loadSideViewDrawableIfNecessary(QSTile.State state) {
        Drawable drawable = state.sideViewCustomDrawable;
        if (drawable != null) {
            ImageView imageView = this.customDrawableView;
            if (imageView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
            imageView.setImageDrawable(drawable);
            ImageView imageView2 = this.customDrawableView;
            if (imageView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
            imageView2.setVisibility(0);
            ImageView imageView3 = this.chevronView;
            if (imageView3 != null) {
                imageView3.setVisibility(8);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                throw null;
            }
        } else if (!(state instanceof QSTile.BooleanState) || ((QSTile.BooleanState) state).forceExpandIcon) {
            ImageView imageView4 = this.customDrawableView;
            if (imageView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
            imageView4.setImageDrawable(null);
            ImageView imageView5 = this.customDrawableView;
            if (imageView5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
            imageView5.setVisibility(8);
            ImageView imageView6 = this.chevronView;
            if (imageView6 != null) {
                imageView6.setVisibility(0);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                throw null;
            }
        } else {
            ImageView imageView7 = this.customDrawableView;
            if (imageView7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
            imageView7.setImageDrawable(null);
            ImageView imageView8 = this.customDrawableView;
            if (imageView8 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
            imageView8.setVisibility(8);
            ImageView imageView9 = this.chevronView;
            if (imageView9 != null) {
                imageView9.setVisibility(8);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                throw null;
            }
        }
    }

    private final String getStateText(QSTile.State state) {
        if (state.disabledByPolicy) {
            String string = getContext().getString(R$string.tile_disabled);
            Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.string.tile_disabled)");
            return string;
        } else if (state.state != 0 && !(state instanceof QSTile.BooleanState)) {
            return "";
        } else {
            String str = getResources().getStringArray(SubtitleArrayMapping.INSTANCE.getSubtitleId(state.spec))[state.state];
            Intrinsics.checkNotNullExpressionValue(str, "{\n            var arrayResId = SubtitleArrayMapping.getSubtitleId(state.spec)\n            val array = resources.getStringArray(arrayResId)\n            array[state.state]\n        }");
            return str;
        }
    }

    protected boolean animationsEnabled() {
        if (!isShown()) {
            return false;
        }
        if (!(getAlpha() == 1.0f)) {
            return false;
        }
        getLocationOnScreen(this.locInScreen);
        return this.locInScreen[1] >= (-getHeight());
    }

    public int getBackgroundColorForState(int i) {
        if (this.isSignalOrBt) {
            return getColorInactive();
        }
        if (i == 0) {
            return getColorUnavailable();
        }
        if (i == 1) {
            return getColorInactive();
        }
        if (i == 2) {
            return getColorActive();
        }
        Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
        return 0;
    }

    public int getLabelColorForState(int i) {
        if (i != 0) {
            if (i == 1) {
                return getColorLabelInactive();
            }
            if (i == 2) {
                if (this.isSignalOrBt) {
                    return getColorLabelInactive();
                }
                return getColorLabelActive();
            }
            Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
            return 0;
        }
        return getColorLabelUnavailable();
    }

    public int getSecondaryLabelColorForState(int i) {
        if (i != 0) {
            if (i == 1) {
                return getColorSecondaryLabelInactive();
            }
            if (i == 2) {
                return getColorSecondaryLabelActive();
            }
            Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
            return 0;
        }
        return getColorSecondaryLabelUnavailable();
    }

    private final int getChevronColorForState(int i) {
        return getSecondaryLabelColorForState(i);
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    @NotNull
    /* renamed from: getLabel */
    public View mo811getLabel() {
        return mo811getLabel();
    }

    protected void dealAndAddIcon() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
        addView(this._icon, new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize));
    }

    public int getCircleIconBackgroundColorForState(int i) {
        if (i != 0) {
            if (i == 1) {
                return getColorInactiveCircle();
            }
            if (i == 2) {
                return getColorActive();
            }
            Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
            return 0;
        }
        return getColorUnavailableCircle();
    }

    public int getColorInactiveCircle() {
        return this.colorInactiveCircle;
    }

    public int getColorUnavailableCircle() {
        return this.colorUnavailableCircle;
    }

    public final boolean isTesla() {
        return this.isTesla;
    }
}
