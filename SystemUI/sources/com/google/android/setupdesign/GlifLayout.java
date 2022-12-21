package com.google.android.setupdesign;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.StatusBarMixin;
import com.google.android.setupdesign.template.DescriptionMixin;
import com.google.android.setupdesign.template.HeaderMixin;
import com.google.android.setupdesign.template.IconMixin;
import com.google.android.setupdesign.template.IllustrationProgressMixin;
import com.google.android.setupdesign.template.ProgressBarMixin;
import com.google.android.setupdesign.template.RequireScrollMixin;
import com.google.android.setupdesign.template.ScrollViewScrollHandlingDelegate;
import com.google.android.setupdesign.util.DescriptionStyler;
import com.google.android.setupdesign.util.LayoutStyler;

public class GlifLayout extends PartnerCustomizationLayout {
    private boolean applyPartnerHeavyThemeResource;
    private ColorStateList backgroundBaseColor;
    private boolean backgroundPatterned;
    private ColorStateList primaryColor;

    public GlifLayout(Context context) {
        this(context, 0, 0);
    }

    public GlifLayout(Context context, int i) {
        this(context, i, 0);
    }

    public GlifLayout(Context context, int i, int i2) {
        super(context, i, i2);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init((AttributeSet) null, C3953R.attr.sudLayoutTheme);
    }

    public GlifLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init(attributeSet, C3953R.attr.sudLayoutTheme);
    }

    public GlifLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init(attributeSet, i);
    }

    private void init(AttributeSet attributeSet, int i) {
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, C3953R.styleable.SudGlifLayout, i, 0);
            this.applyPartnerHeavyThemeResource = shouldApplyPartnerResource() && obtainStyledAttributes.getBoolean(C3953R.styleable.SudGlifLayout_sudUsePartnerHeavyTheme, false);
            registerMixin(HeaderMixin.class, new HeaderMixin(this, attributeSet, i));
            registerMixin(DescriptionMixin.class, new DescriptionMixin(this, attributeSet, i));
            registerMixin(IconMixin.class, new IconMixin(this, attributeSet, i));
            registerMixin(ProgressBarMixin.class, new ProgressBarMixin(this, attributeSet, i));
            registerMixin(IllustrationProgressMixin.class, new IllustrationProgressMixin(this));
            RequireScrollMixin requireScrollMixin = new RequireScrollMixin(this);
            registerMixin(RequireScrollMixin.class, requireScrollMixin);
            ScrollView scrollView = getScrollView();
            if (scrollView != null) {
                requireScrollMixin.setScrollHandlingDelegate(new ScrollViewScrollHandlingDelegate(requireScrollMixin, scrollView));
            }
            ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(C3953R.styleable.SudGlifLayout_sudColorPrimary);
            if (colorStateList != null) {
                setPrimaryColor(colorStateList);
            }
            if (shouldApplyPartnerHeavyThemeResource()) {
                updateContentBackgroundColorWithPartnerConfig();
            }
            View findManagedViewById = findManagedViewById(C3953R.C3956id.sud_layout_content);
            if (findManagedViewById != null) {
                if (shouldApplyPartnerResource()) {
                    LayoutStyler.applyPartnerCustomizationExtraPaddingStyle(findManagedViewById);
                }
                if (!(this instanceof GlifPreferenceLayout)) {
                    tryApplyPartnerCustomizationContentPaddingTopStyle(findManagedViewById);
                }
            }
            updateLandscapeMiddleHorizontalSpacing();
            setBackgroundBaseColor(obtainStyledAttributes.getColorStateList(C3953R.styleable.SudGlifLayout_sudBackgroundBaseColor));
            setBackgroundPatterned(obtainStyledAttributes.getBoolean(C3953R.styleable.SudGlifLayout_sudBackgroundPatterned, true));
            int resourceId = obtainStyledAttributes.getResourceId(C3953R.styleable.SudGlifLayout_sudStickyHeader, 0);
            if (resourceId != 0) {
                inflateStickyHeader(resourceId);
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ((IconMixin) getMixin(IconMixin.class)).tryApplyPartnerCustomizationStyle();
        ((HeaderMixin) getMixin(HeaderMixin.class)).tryApplyPartnerCustomizationStyle();
        ((DescriptionMixin) getMixin(DescriptionMixin.class)).tryApplyPartnerCustomizationStyle();
        ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).tryApplyPartnerCustomizationStyle();
        tryApplyPartnerCustomizationStyleToShortDescription();
    }

    private void tryApplyPartnerCustomizationStyleToShortDescription() {
        TextView textView = (TextView) findManagedViewById(C3953R.C3956id.sud_layout_description);
        if (textView == null) {
            return;
        }
        if (this.applyPartnerHeavyThemeResource) {
            DescriptionStyler.applyPartnerCustomizationHeavyStyle(textView);
        } else if (shouldApplyPartnerResource()) {
            DescriptionStyler.applyPartnerCustomizationLightStyle(textView);
        }
    }

    /* access modifiers changed from: protected */
    public void updateLandscapeMiddleHorizontalSpacing() {
        int i;
        int i2;
        int dimensionPixelSize = getResources().getDimensionPixelSize(C3953R.dimen.sud_glif_land_middle_horizontal_spacing);
        if (shouldApplyPartnerResource() && PartnerConfigHelper.get(getContext()).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAND_MIDDLE_HORIZONTAL_SPACING)) {
            dimensionPixelSize = (int) PartnerConfigHelper.get(getContext()).getDimension(getContext(), PartnerConfig.CONFIG_LAND_MIDDLE_HORIZONTAL_SPACING);
        }
        View findManagedViewById = findManagedViewById(C3953R.C3956id.sud_landscape_header_area);
        int i3 = 0;
        if (findManagedViewById != null) {
            if (!shouldApplyPartnerResource() || !PartnerConfigHelper.get(getContext()).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_END)) {
                TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{C3953R.attr.sudMarginEnd});
                int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(0, 0);
                obtainStyledAttributes.recycle();
                i2 = dimensionPixelSize2;
            } else {
                i2 = (int) PartnerConfigHelper.get(getContext()).getDimension(getContext(), PartnerConfig.CONFIG_LAYOUT_MARGIN_END);
            }
            findManagedViewById.setPadding(findManagedViewById.getPaddingStart(), findManagedViewById.getPaddingTop(), (dimensionPixelSize / 2) - i2, findManagedViewById.getPaddingBottom());
        }
        View findManagedViewById2 = findManagedViewById(C3953R.C3956id.sud_landscape_content_area);
        if (findManagedViewById2 != null) {
            if (!shouldApplyPartnerResource() || !PartnerConfigHelper.get(getContext()).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_START)) {
                TypedArray obtainStyledAttributes2 = getContext().obtainStyledAttributes(new int[]{C3953R.attr.sudMarginStart});
                int dimensionPixelSize3 = obtainStyledAttributes2.getDimensionPixelSize(0, 0);
                obtainStyledAttributes2.recycle();
                i = dimensionPixelSize3;
            } else {
                i = (int) PartnerConfigHelper.get(getContext()).getDimension(getContext(), PartnerConfig.CONFIG_LAYOUT_MARGIN_START);
            }
            if (findManagedViewById != null) {
                i3 = (dimensionPixelSize / 2) - i;
            }
            findManagedViewById2.setPadding(i3, findManagedViewById2.getPaddingTop(), findManagedViewById2.getPaddingEnd(), findManagedViewById2.getPaddingBottom());
        }
    }

    /* access modifiers changed from: protected */
    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = C3953R.layout.sud_glif_template;
        }
        return inflateTemplate(layoutInflater, C3953R.style.SudThemeGlif_Light, i);
    }

    /* access modifiers changed from: protected */
    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = C3953R.C3956id.sud_layout_content;
        }
        return super.findContainer(i);
    }

    public View inflateStickyHeader(int i) {
        ViewStub viewStub = (ViewStub) findManagedViewById(C3953R.C3956id.sud_layout_sticky_header);
        viewStub.setLayoutResource(i);
        return viewStub.inflate();
    }

    public ScrollView getScrollView() {
        View findManagedViewById = findManagedViewById(C3953R.C3956id.sud_scroll_view);
        if (findManagedViewById instanceof ScrollView) {
            return (ScrollView) findManagedViewById;
        }
        return null;
    }

    public TextView getHeaderTextView() {
        return ((HeaderMixin) getMixin(HeaderMixin.class)).getTextView();
    }

    public void setHeaderText(int i) {
        ((HeaderMixin) getMixin(HeaderMixin.class)).setText(i);
    }

    public void setHeaderText(CharSequence charSequence) {
        ((HeaderMixin) getMixin(HeaderMixin.class)).setText(charSequence);
    }

    public CharSequence getHeaderText() {
        return ((HeaderMixin) getMixin(HeaderMixin.class)).getText();
    }

    public TextView getDescriptionTextView() {
        return ((DescriptionMixin) getMixin(DescriptionMixin.class)).getTextView();
    }

    public void setDescriptionText(int i) {
        ((DescriptionMixin) getMixin(DescriptionMixin.class)).setText(i);
    }

    public void setDescriptionText(CharSequence charSequence) {
        ((DescriptionMixin) getMixin(DescriptionMixin.class)).setText(charSequence);
    }

    public CharSequence getDescriptionText() {
        return ((DescriptionMixin) getMixin(DescriptionMixin.class)).getText();
    }

    public void setHeaderColor(ColorStateList colorStateList) {
        ((HeaderMixin) getMixin(HeaderMixin.class)).setTextColor(colorStateList);
    }

    public ColorStateList getHeaderColor() {
        return ((HeaderMixin) getMixin(HeaderMixin.class)).getTextColor();
    }

    public void setIcon(Drawable drawable) {
        ((IconMixin) getMixin(IconMixin.class)).setIcon(drawable);
    }

    public Drawable getIcon() {
        return ((IconMixin) getMixin(IconMixin.class)).getIcon();
    }

    public void setLandscapeHeaderAreaVisible(boolean z) {
        View findManagedViewById = findManagedViewById(C3953R.C3956id.sud_landscape_header_area);
        if (findManagedViewById != null) {
            if (z) {
                findManagedViewById.setVisibility(0);
            } else {
                findManagedViewById.setVisibility(8);
            }
            updateLandscapeMiddleHorizontalSpacing();
        }
    }

    public void setPrimaryColor(ColorStateList colorStateList) {
        this.primaryColor = colorStateList;
        updateBackground();
        ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).setColor(colorStateList);
    }

    public ColorStateList getPrimaryColor() {
        return this.primaryColor;
    }

    public void setBackgroundBaseColor(ColorStateList colorStateList) {
        this.backgroundBaseColor = colorStateList;
        updateBackground();
    }

    public ColorStateList getBackgroundBaseColor() {
        return this.backgroundBaseColor;
    }

    public void setBackgroundPatterned(boolean z) {
        this.backgroundPatterned = z;
        updateBackground();
    }

    public boolean isBackgroundPatterned() {
        return this.backgroundPatterned;
    }

    private void updateBackground() {
        int i;
        Drawable drawable;
        if (findManagedViewById(C3953R.C3956id.suc_layout_status) != null) {
            ColorStateList colorStateList = this.backgroundBaseColor;
            if (colorStateList != null) {
                i = colorStateList.getDefaultColor();
            } else {
                ColorStateList colorStateList2 = this.primaryColor;
                i = colorStateList2 != null ? colorStateList2.getDefaultColor() : 0;
            }
            if (this.backgroundPatterned) {
                drawable = new GlifPatternDrawable(i);
            } else {
                drawable = new ColorDrawable(i);
            }
            ((StatusBarMixin) getMixin(StatusBarMixin.class)).setStatusBarBackground(drawable);
        }
    }

    public boolean isProgressBarShown() {
        return ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).isShown();
    }

    public void setProgressBarShown(boolean z) {
        ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).setShown(z);
    }

    public ProgressBar peekProgressBar() {
        return ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).peekProgressBar();
    }

    public boolean shouldApplyPartnerHeavyThemeResource() {
        return this.applyPartnerHeavyThemeResource || (shouldApplyPartnerResource() && PartnerConfigHelper.shouldApplyExtendedPartnerConfig(getContext()));
    }

    private void updateContentBackgroundColorWithPartnerConfig() {
        if (!useFullDynamicColor()) {
            getRootView().setBackgroundColor(PartnerConfigHelper.get(getContext()).getColor(getContext(), PartnerConfig.CONFIG_LAYOUT_BACKGROUND_COLOR));
        }
    }

    /* access modifiers changed from: protected */
    public void tryApplyPartnerCustomizationContentPaddingTopStyle(View view) {
        int dimension;
        Context context = view.getContext();
        boolean isPartnerConfigAvailable = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CONTENT_PADDING_TOP);
        if (shouldApplyPartnerResource() && isPartnerConfigAvailable && (dimension = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CONTENT_PADDING_TOP)) != view.getPaddingTop()) {
            view.setPadding(view.getPaddingStart(), dimension, view.getPaddingEnd(), view.getPaddingBottom());
        }
    }
}
