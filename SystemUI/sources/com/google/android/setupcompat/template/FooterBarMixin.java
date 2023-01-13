package com.google.android.setupcompat.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.setupcompat.C3941R;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.FooterButtonPartnerConfig;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.logging.internal.FooterBarMixinMetrics;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterButton;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class FooterBarMixin implements Mixin {
    private static final AtomicInteger nextGeneratedId = new AtomicInteger(1);
    final boolean applyDynamicColor;
    final boolean applyPartnerResources;
    public LinearLayout buttonContainer;
    private final Context context;
    int defaultPadding;
    private int footerBarPaddingBottom;
    int footerBarPaddingEnd;
    int footerBarPaddingStart;
    private int footerBarPaddingTop;
    private final int footerBarPrimaryBackgroundColor;
    private final int footerBarSecondaryBackgroundColor;
    private final ViewStub footerStub;
    /* access modifiers changed from: private */
    public boolean isSecondaryButtonInPrimaryStyle = false;
    public final FooterBarMixinMetrics metrics;
    private FooterButton primaryButton;
    /* access modifiers changed from: private */
    public int primaryButtonId;
    public FooterButtonPartnerConfig primaryButtonPartnerConfigForTesting;
    private boolean removeFooterBarWhenEmpty = true;
    private FooterButton secondaryButton;
    private int secondaryButtonId;
    public FooterButtonPartnerConfig secondaryButtonPartnerConfigForTesting;
    final boolean useFullDynamicColor;

    private FooterButton.OnButtonEventListener createButtonEventListener(final int i) {
        return new FooterButton.OnButtonEventListener() {
            public void onEnabledChanged(boolean z) {
                Button button;
                PartnerConfig partnerConfig;
                PartnerConfig partnerConfig2;
                if (FooterBarMixin.this.buttonContainer != null && (button = (Button) FooterBarMixin.this.buttonContainer.findViewById(i)) != null) {
                    button.setEnabled(z);
                    if (FooterBarMixin.this.applyPartnerResources && !FooterBarMixin.this.applyDynamicColor) {
                        FooterBarMixin footerBarMixin = FooterBarMixin.this;
                        if (i == footerBarMixin.primaryButtonId || FooterBarMixin.this.isSecondaryButtonInPrimaryStyle) {
                            partnerConfig = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR;
                        } else {
                            partnerConfig = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR;
                        }
                        if (i == FooterBarMixin.this.primaryButtonId || FooterBarMixin.this.isSecondaryButtonInPrimaryStyle) {
                            partnerConfig2 = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_DISABLED_TEXT_COLOR;
                        } else {
                            partnerConfig2 = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_DISABLED_TEXT_COLOR;
                        }
                        footerBarMixin.updateButtonTextColorWithStates(button, partnerConfig, partnerConfig2);
                    }
                }
            }

            public void onVisibilityChanged(int i) {
                Button button;
                if (FooterBarMixin.this.buttonContainer != null && (button = (Button) FooterBarMixin.this.buttonContainer.findViewById(i)) != null) {
                    button.setVisibility(i);
                    FooterBarMixin.this.autoSetButtonBarVisibility();
                }
            }

            public void onTextChanged(CharSequence charSequence) {
                Button button;
                if (FooterBarMixin.this.buttonContainer != null && (button = (Button) FooterBarMixin.this.buttonContainer.findViewById(i)) != null) {
                    button.setText(charSequence);
                }
            }

            public void onLocaleChanged(Locale locale) {
                Button button;
                if (FooterBarMixin.this.buttonContainer != null && (button = (Button) FooterBarMixin.this.buttonContainer.findViewById(i)) != null && locale != null) {
                    button.setTextLocale(locale);
                }
            }

            public void onDirectionChanged(int i) {
                if (FooterBarMixin.this.buttonContainer != null && i != -1) {
                    FooterBarMixin.this.buttonContainer.setLayoutDirection(i);
                }
            }
        };
    }

    public FooterBarMixin(TemplateLayout templateLayout, AttributeSet attributeSet, int i) {
        FooterBarMixinMetrics footerBarMixinMetrics = new FooterBarMixinMetrics();
        this.metrics = footerBarMixinMetrics;
        Context context2 = templateLayout.getContext();
        this.context = context2;
        this.footerStub = (ViewStub) templateLayout.findManagedViewById(C3941R.C3943id.suc_layout_footer);
        boolean z = templateLayout instanceof PartnerCustomizationLayout;
        this.applyPartnerResources = z && ((PartnerCustomizationLayout) templateLayout).shouldApplyPartnerResource();
        this.applyDynamicColor = z && ((PartnerCustomizationLayout) templateLayout).shouldApplyDynamicColor();
        this.useFullDynamicColor = z && ((PartnerCustomizationLayout) templateLayout).useFullDynamicColor();
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, C3941R.styleable.SucFooterBarMixin, i, 0);
        this.defaultPadding = obtainStyledAttributes.getDimensionPixelSize(C3941R.styleable.SucFooterBarMixin_sucFooterBarPaddingVertical, 0);
        this.footerBarPaddingTop = obtainStyledAttributes.getDimensionPixelSize(C3941R.styleable.SucFooterBarMixin_sucFooterBarPaddingTop, this.defaultPadding);
        this.footerBarPaddingBottom = obtainStyledAttributes.getDimensionPixelSize(C3941R.styleable.SucFooterBarMixin_sucFooterBarPaddingBottom, this.defaultPadding);
        this.footerBarPaddingStart = obtainStyledAttributes.getDimensionPixelSize(C3941R.styleable.SucFooterBarMixin_sucFooterBarPaddingStart, 0);
        this.footerBarPaddingEnd = obtainStyledAttributes.getDimensionPixelSize(C3941R.styleable.SucFooterBarMixin_sucFooterBarPaddingEnd, 0);
        this.footerBarPrimaryBackgroundColor = obtainStyledAttributes.getColor(C3941R.styleable.SucFooterBarMixin_sucFooterBarPrimaryFooterBackground, 0);
        this.footerBarSecondaryBackgroundColor = obtainStyledAttributes.getColor(C3941R.styleable.SucFooterBarMixin_sucFooterBarSecondaryFooterBackground, 0);
        int resourceId = obtainStyledAttributes.getResourceId(C3941R.styleable.SucFooterBarMixin_sucFooterBarPrimaryFooterButton, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(C3941R.styleable.SucFooterBarMixin_sucFooterBarSecondaryFooterButton, 0);
        obtainStyledAttributes.recycle();
        FooterButtonInflater footerButtonInflater = new FooterButtonInflater(context2);
        if (resourceId2 != 0) {
            setSecondaryButton(footerButtonInflater.inflate(resourceId2));
            footerBarMixinMetrics.logPrimaryButtonInitialStateVisibility(true, true);
        }
        if (resourceId != 0) {
            setPrimaryButton(footerButtonInflater.inflate(resourceId));
            footerBarMixinMetrics.logSecondaryButtonInitialStateVisibility(true, true);
        }
        FooterButtonStyleUtils.clearSavedDefaultTextColor();
    }

    /* access modifiers changed from: protected */
    public boolean isFooterButtonAlignedEnd() {
        if (PartnerConfigHelper.get(this.context).isPartnerConfigAvailable(PartnerConfig.CONFIG_FOOTER_BUTTON_ALIGNED_END)) {
            return PartnerConfigHelper.get(this.context).getBoolean(this.context, PartnerConfig.CONFIG_FOOTER_BUTTON_ALIGNED_END, false);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isFooterButtonsEvenlyWeighted() {
        if (!this.isSecondaryButtonInPrimaryStyle) {
            return false;
        }
        PartnerConfigHelper.get(this.context);
        return PartnerConfigHelper.isNeutralButtonStyleEnabled(this.context);
    }

    private View addSpace() {
        LinearLayout ensureFooterInflated = ensureFooterInflated();
        View view = new View(this.context);
        view.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
        view.setVisibility(4);
        ensureFooterInflated.addView(view);
        return view;
    }

    private LinearLayout ensureFooterInflated() {
        if (this.buttonContainer == null) {
            if (this.footerStub != null) {
                LinearLayout linearLayout = (LinearLayout) inflateFooter(C3941R.layout.suc_footer_button_bar);
                this.buttonContainer = linearLayout;
                onFooterBarInflated(linearLayout);
                onFooterBarApplyPartnerResource(this.buttonContainer);
            } else {
                throw new IllegalStateException("Footer stub is not found in this template");
            }
        }
        return this.buttonContainer;
    }

    /* access modifiers changed from: protected */
    public void onFooterBarInflated(LinearLayout linearLayout) {
        if (linearLayout != null) {
            linearLayout.setId(View.generateViewId());
            updateFooterBarPadding(linearLayout, this.footerBarPaddingStart, this.footerBarPaddingTop, this.footerBarPaddingEnd, this.footerBarPaddingBottom);
            if (isFooterButtonAlignedEnd()) {
                linearLayout.setGravity(8388629);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFooterBarApplyPartnerResource(LinearLayout linearLayout) {
        int dimension;
        if (linearLayout != null && this.applyPartnerResources) {
            if (!this.useFullDynamicColor) {
                linearLayout.setBackgroundColor(PartnerConfigHelper.get(this.context).getColor(this.context, PartnerConfig.CONFIG_FOOTER_BAR_BG_COLOR));
            }
            if (PartnerConfigHelper.get(this.context).isPartnerConfigAvailable(PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_TOP)) {
                this.footerBarPaddingTop = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_TOP);
            }
            if (PartnerConfigHelper.get(this.context).isPartnerConfigAvailable(PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_BOTTOM)) {
                this.footerBarPaddingBottom = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_BOTTOM);
            }
            if (PartnerConfigHelper.get(this.context).isPartnerConfigAvailable(PartnerConfig.CONFIG_FOOTER_BAR_PADDING_START)) {
                this.footerBarPaddingStart = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, PartnerConfig.CONFIG_FOOTER_BAR_PADDING_START);
            }
            if (PartnerConfigHelper.get(this.context).isPartnerConfigAvailable(PartnerConfig.CONFIG_FOOTER_BAR_PADDING_END)) {
                this.footerBarPaddingEnd = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, PartnerConfig.CONFIG_FOOTER_BAR_PADDING_END);
            }
            updateFooterBarPadding(linearLayout, this.footerBarPaddingStart, this.footerBarPaddingTop, this.footerBarPaddingEnd, this.footerBarPaddingBottom);
            if (PartnerConfigHelper.get(this.context).isPartnerConfigAvailable(PartnerConfig.CONFIG_FOOTER_BAR_MIN_HEIGHT) && (dimension = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, PartnerConfig.CONFIG_FOOTER_BAR_MIN_HEIGHT)) > 0) {
                linearLayout.setMinimumHeight(dimension);
            }
        }
    }

    /* access modifiers changed from: protected */
    public FooterActionButton createThemedButton(Context context2, int i) {
        return (FooterActionButton) LayoutInflater.from(new ContextThemeWrapper(context2, i)).inflate(C3941R.layout.suc_button, (ViewGroup) null, false);
    }

    public void setPrimaryButton(FooterButton footerButton) {
        Preconditions.ensureOnMainThread("setPrimaryButton");
        ensureFooterInflated();
        FooterButtonPartnerConfig build = new FooterButtonPartnerConfig.Builder(footerButton).setPartnerTheme(getPartnerTheme(footerButton, C3941R.style.SucPartnerCustomizationButton_Primary, PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR)).setButtonBackgroundConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR).setButtonDisableAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_ALPHA).setButtonDisableBackgroundConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR).setButtonDisableTextColorConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_DISABLED_TEXT_COLOR).setButtonIconConfig(getDrawablePartnerConfig(footerButton.getButtonType())).setButtonRadiusConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS).setButtonRippleColorAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA).setTextColorConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR).setMarginStartConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_MARGIN_START).setTextSizeConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_SIZE).setButtonMinHeight(PartnerConfig.CONFIG_FOOTER_BUTTON_MIN_HEIGHT).setTextTypeFaceConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY).setTextStyleConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_STYLE).build();
        FooterActionButton inflateButton = inflateButton(footerButton, build);
        this.primaryButtonId = inflateButton.getId();
        inflateButton.setPrimaryButtonStyle(true);
        this.primaryButton = footerButton;
        this.primaryButtonPartnerConfigForTesting = build;
        onFooterButtonInflated(inflateButton, this.footerBarPrimaryBackgroundColor);
        onFooterButtonApplyPartnerResource(inflateButton, build);
        repopulateButtons();
    }

    public FooterButton getPrimaryButton() {
        return this.primaryButton;
    }

    public Button getPrimaryButtonView() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout == null) {
            return null;
        }
        return (Button) linearLayout.findViewById(this.primaryButtonId);
    }

    /* access modifiers changed from: package-private */
    public boolean isPrimaryButtonVisible() {
        return getPrimaryButtonView() != null && getPrimaryButtonView().getVisibility() == 0;
    }

    public void setSecondaryButton(FooterButton footerButton) {
        setSecondaryButton(footerButton, false);
    }

    public void setSecondaryButton(FooterButton footerButton, boolean z) {
        int i;
        PartnerConfig partnerConfig;
        PartnerConfig partnerConfig2;
        PartnerConfig partnerConfig3;
        PartnerConfig partnerConfig4;
        Preconditions.ensureOnMainThread("setSecondaryButton");
        this.isSecondaryButtonInPrimaryStyle = z;
        ensureFooterInflated();
        FooterButtonPartnerConfig.Builder builder = new FooterButtonPartnerConfig.Builder(footerButton);
        if (z) {
            i = C3941R.style.SucPartnerCustomizationButton_Primary;
        } else {
            i = C3941R.style.SucPartnerCustomizationButton_Secondary;
        }
        if (z) {
            partnerConfig = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR;
        } else {
            partnerConfig = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR;
        }
        FooterButtonPartnerConfig.Builder partnerTheme = builder.setPartnerTheme(getPartnerTheme(footerButton, i, partnerConfig));
        if (z) {
            partnerConfig2 = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR;
        } else {
            partnerConfig2 = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR;
        }
        FooterButtonPartnerConfig.Builder buttonDisableBackgroundConfig = partnerTheme.setButtonBackgroundConfig(partnerConfig2).setButtonDisableAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_ALPHA).setButtonDisableBackgroundConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR);
        if (z) {
            partnerConfig3 = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_DISABLED_TEXT_COLOR;
        } else {
            partnerConfig3 = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_DISABLED_TEXT_COLOR;
        }
        FooterButtonPartnerConfig.Builder buttonRippleColorAlphaConfig = buttonDisableBackgroundConfig.setButtonDisableTextColorConfig(partnerConfig3).setButtonIconConfig(getDrawablePartnerConfig(footerButton.getButtonType())).setButtonRadiusConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS).setButtonRippleColorAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA);
        if (z) {
            partnerConfig4 = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR;
        } else {
            partnerConfig4 = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR;
        }
        FooterButtonPartnerConfig build = buttonRippleColorAlphaConfig.setTextColorConfig(partnerConfig4).setMarginStartConfig(PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_MARGIN_START).setTextSizeConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_SIZE).setButtonMinHeight(PartnerConfig.CONFIG_FOOTER_BUTTON_MIN_HEIGHT).setTextTypeFaceConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY).setTextStyleConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_STYLE).build();
        FooterActionButton inflateButton = inflateButton(footerButton, build);
        this.secondaryButtonId = inflateButton.getId();
        inflateButton.setPrimaryButtonStyle(z);
        this.secondaryButton = footerButton;
        this.secondaryButtonPartnerConfigForTesting = build;
        onFooterButtonInflated(inflateButton, this.footerBarSecondaryBackgroundColor);
        onFooterButtonApplyPartnerResource(inflateButton, build);
        repopulateButtons();
    }

    /* access modifiers changed from: protected */
    public void repopulateButtons() {
        LinearLayout ensureFooterInflated = ensureFooterInflated();
        Button primaryButtonView = getPrimaryButtonView();
        Button secondaryButtonView = getSecondaryButtonView();
        ensureFooterInflated.removeAllViews();
        boolean isFooterButtonsEvenlyWeighted = isFooterButtonsEvenlyWeighted();
        if ((this.context.getResources().getConfiguration().orientation == 2) && isFooterButtonsEvenlyWeighted && isFooterButtonAlignedEnd()) {
            addSpace();
        }
        if (secondaryButtonView != null) {
            if (this.isSecondaryButtonInPrimaryStyle) {
                updateFooterBarPadding(ensureFooterInflated, ensureFooterInflated.getPaddingRight(), ensureFooterInflated.getPaddingTop(), ensureFooterInflated.getPaddingRight(), ensureFooterInflated.getPaddingBottom());
            }
            ensureFooterInflated.addView(secondaryButtonView);
        }
        if (!isFooterButtonAlignedEnd()) {
            addSpace();
        }
        if (primaryButtonView != null) {
            ensureFooterInflated.addView(primaryButtonView);
        }
        setEvenlyWeightedButtons(primaryButtonView, secondaryButtonView, isFooterButtonsEvenlyWeighted);
    }

    private void setEvenlyWeightedButtons(Button button, Button button2, boolean z) {
        LinearLayout.LayoutParams layoutParams;
        LinearLayout.LayoutParams layoutParams2;
        if (button == null || button2 == null || !z) {
            if (!(button == null || (layoutParams2 = (LinearLayout.LayoutParams) button.getLayoutParams()) == null)) {
                layoutParams2.width = -2;
                layoutParams2.weight = 0.0f;
                button.setLayoutParams(layoutParams2);
            }
            if (button2 != null && (layoutParams = (LinearLayout.LayoutParams) button2.getLayoutParams()) != null) {
                layoutParams.width = -2;
                layoutParams.weight = 0.0f;
                button2.setLayoutParams(layoutParams);
                return;
            }
            return;
        }
        button.measure(0, 0);
        int measuredWidth = button.getMeasuredWidth();
        button2.measure(0, 0);
        int max = Math.max(measuredWidth, button2.getMeasuredWidth());
        button.getLayoutParams().width = max;
        button2.getLayoutParams().width = max;
    }

    /* access modifiers changed from: protected */
    public void onFooterButtonInflated(Button button, int i) {
        if (i != 0) {
            FooterButtonStyleUtils.updateButtonBackground(button, i);
        }
        this.buttonContainer.addView(button);
        autoSetButtonBarVisibility();
    }

    private int getPartnerTheme(FooterButton footerButton, int i, PartnerConfig partnerConfig) {
        int theme = footerButton.getTheme();
        if (footerButton.getTheme() != 0 && !this.applyPartnerResources) {
            i = theme;
        }
        if (!this.applyPartnerResources) {
            return i;
        }
        if (PartnerConfigHelper.get(this.context).getColor(this.context, partnerConfig) == 0) {
            return C3941R.style.SucPartnerCustomizationButton_Secondary;
        }
        return C3941R.style.SucPartnerCustomizationButton_Primary;
    }

    public LinearLayout getButtonContainer() {
        return this.buttonContainer;
    }

    public FooterButton getSecondaryButton() {
        return this.secondaryButton;
    }

    public void setRemoveFooterBarWhenEmpty(boolean z) {
        this.removeFooterBarWhenEmpty = z;
        autoSetButtonBarVisibility();
    }

    /* access modifiers changed from: private */
    public void autoSetButtonBarVisibility() {
        Button primaryButtonView = getPrimaryButtonView();
        Button secondaryButtonView = getSecondaryButtonView();
        boolean z = true;
        int i = 0;
        boolean z2 = primaryButtonView != null && primaryButtonView.getVisibility() == 0;
        if (secondaryButtonView == null || secondaryButtonView.getVisibility() != 0) {
            z = false;
        }
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout != null) {
            if (!z2 && !z) {
                i = this.removeFooterBarWhenEmpty ? 8 : 4;
            }
            linearLayout.setVisibility(i);
        }
    }

    public int getVisibility() {
        return this.buttonContainer.getVisibility();
    }

    public Button getSecondaryButtonView() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout == null) {
            return null;
        }
        return (Button) linearLayout.findViewById(this.secondaryButtonId);
    }

    /* access modifiers changed from: package-private */
    public boolean isSecondaryButtonVisible() {
        return getSecondaryButtonView() != null && getSecondaryButtonView().getVisibility() == 0;
    }

    private static int generateViewId() {
        AtomicInteger atomicInteger;
        int i;
        int i2;
        do {
            atomicInteger = nextGeneratedId;
            i = atomicInteger.get();
            i2 = i + 1;
            if (i2 > 16777215) {
                i2 = 1;
            }
        } while (!atomicInteger.compareAndSet(i, i2));
        return i;
    }

    private FooterActionButton inflateButton(FooterButton footerButton, FooterButtonPartnerConfig footerButtonPartnerConfig) {
        FooterActionButton createThemedButton = createThemedButton(this.context, footerButtonPartnerConfig.getPartnerTheme());
        createThemedButton.setId(View.generateViewId());
        createThemedButton.setText(footerButton.getText());
        createThemedButton.setOnClickListener(footerButton);
        createThemedButton.setVisibility(footerButton.getVisibility());
        createThemedButton.setEnabled(footerButton.isEnabled());
        createThemedButton.setFooterButton(footerButton);
        footerButton.setOnButtonEventListener(createButtonEventListener(createThemedButton.getId()));
        return createThemedButton;
    }

    private void onFooterButtonApplyPartnerResource(Button button, FooterButtonPartnerConfig footerButtonPartnerConfig) {
        if (this.applyPartnerResources) {
            FooterButtonStyleUtils.applyButtonPartnerResources(this.context, button, this.applyDynamicColor, button.getId() == this.primaryButtonId, footerButtonPartnerConfig);
            if (!this.applyDynamicColor) {
                updateButtonTextColorWithStates(button, footerButtonPartnerConfig.getButtonTextColorConfig(), footerButtonPartnerConfig.getButtonDisableTextColorConfig());
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateButtonTextColorWithStates(Button button, PartnerConfig partnerConfig, PartnerConfig partnerConfig2) {
        if (button.isEnabled()) {
            FooterButtonStyleUtils.updateButtonTextEnabledColorWithPartnerConfig(this.context, button, partnerConfig);
        } else {
            FooterButtonStyleUtils.updateButtonTextDisabledColorWithPartnerConfig(this.context, button, partnerConfig2);
        }
    }

    private static PartnerConfig getDrawablePartnerConfig(int i) {
        switch (i) {
            case 1:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_ADD_ANOTHER;
            case 2:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_CANCEL;
            case 3:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_CLEAR;
            case 4:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_DONE;
            case 5:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_NEXT;
            case 6:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_OPT_IN;
            case 7:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_SKIP;
            case 8:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_STOP;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public View inflateFooter(int i) {
        this.footerStub.setLayoutInflater(LayoutInflater.from(new ContextThemeWrapper(this.context, C3941R.style.SucPartnerCustomizationButtonBar_Stackable)));
        this.footerStub.setLayoutResource(i);
        return this.footerStub.inflate();
    }

    private void updateFooterBarPadding(LinearLayout linearLayout, int i, int i2, int i3, int i4) {
        if (linearLayout != null) {
            linearLayout.setPadding(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: package-private */
    public int getPaddingTop() {
        LinearLayout linearLayout = this.buttonContainer;
        return linearLayout != null ? linearLayout.getPaddingTop() : this.footerStub.getPaddingTop();
    }

    /* access modifiers changed from: package-private */
    public int getPaddingBottom() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout != null) {
            return linearLayout.getPaddingBottom();
        }
        return this.footerStub.getPaddingBottom();
    }

    public void onAttachedToWindow() {
        this.metrics.logPrimaryButtonInitialStateVisibility(isPrimaryButtonVisible(), false);
        this.metrics.logSecondaryButtonInitialStateVisibility(isSecondaryButtonVisible(), false);
    }

    public void onDetachedFromWindow() {
        this.metrics.updateButtonVisibility(isPrimaryButtonVisible(), isSecondaryButtonVisible());
    }

    public PersistableBundle getLoggingMetrics() {
        return this.metrics.getMetrics();
    }
}
