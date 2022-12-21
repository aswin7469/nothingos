package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.C3953R;
import com.google.android.setupdesign.util.HeaderAreaStyler;
import com.google.android.setupdesign.util.LayoutStyler;
import com.google.android.setupdesign.util.PartnerStyleHelper;

public class HeaderMixin implements Mixin {
    private static final int AUTO_SIZE_DEFAULT_MAX_LINES = 6;
    boolean autoTextSizeEnabled = false;
    /* access modifiers changed from: private */
    public float headerAutoSizeLineExtraSpacingInPx;
    /* access modifiers changed from: private */
    public int headerAutoSizeMaxLineOfMaxSize;
    private float headerAutoSizeMaxTextSizeInPx;
    /* access modifiers changed from: private */
    public float headerAutoSizeMinTextSizeInPx;
    private final TemplateLayout templateLayout;

    public HeaderMixin(TemplateLayout templateLayout2, AttributeSet attributeSet, int i) {
        this.templateLayout = templateLayout2;
        TypedArray obtainStyledAttributes = templateLayout2.getContext().obtainStyledAttributes(attributeSet, C3953R.styleable.SucHeaderMixin, i, 0);
        CharSequence text = obtainStyledAttributes.getText(C3953R.styleable.SucHeaderMixin_sucHeaderText);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(C3953R.styleable.SucHeaderMixin_sucHeaderTextColor);
        obtainStyledAttributes.recycle();
        tryUpdateAutoTextSizeFlagWithPartnerConfig();
        if (text != null) {
            setText(text);
        }
        if (colorStateList != null) {
            setTextColor(colorStateList);
        }
    }

    private void tryUpdateAutoTextSizeFlagWithPartnerConfig() {
        Context context = this.templateLayout.getContext();
        if (!PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
            this.autoTextSizeEnabled = false;
            return;
        }
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_HEADER_AUTO_SIZE_ENABLED)) {
            this.autoTextSizeEnabled = PartnerConfigHelper.get(context).getBoolean(context, PartnerConfig.CONFIG_HEADER_AUTO_SIZE_ENABLED, this.autoTextSizeEnabled);
        }
        if (this.autoTextSizeEnabled) {
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MAX_TEXT_SIZE)) {
                this.headerAutoSizeMaxTextSizeInPx = PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MAX_TEXT_SIZE);
            }
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MIN_TEXT_SIZE)) {
                this.headerAutoSizeMinTextSizeInPx = PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MIN_TEXT_SIZE);
            }
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_HEADER_AUTO_SIZE_LINE_SPACING_EXTRA)) {
                this.headerAutoSizeLineExtraSpacingInPx = PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_HEADER_AUTO_SIZE_LINE_SPACING_EXTRA);
            }
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MAX_LINE_OF_MAX_SIZE)) {
                this.headerAutoSizeMaxLineOfMaxSize = PartnerConfigHelper.get(context).getInteger(context, PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MAX_LINE_OF_MAX_SIZE, 0);
            }
            if (this.headerAutoSizeMaxLineOfMaxSize >= 1) {
                float f = this.headerAutoSizeMinTextSizeInPx;
                if (f > 0.0f && this.headerAutoSizeMaxTextSizeInPx >= f) {
                    return;
                }
            }
            Log.w("HeaderMixin", "Invalid configs, disable auto text size.");
            this.autoTextSizeEnabled = false;
        }
    }

    public void tryApplyPartnerCustomizationStyle() {
        TextView textView = (TextView) this.templateLayout.findManagedViewById(C3953R.C3956id.suc_layout_title);
        if (PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
            View findManagedViewById = this.templateLayout.findManagedViewById(C3953R.C3956id.sud_layout_header);
            LayoutStyler.applyPartnerCustomizationExtraPaddingStyle(findManagedViewById);
            HeaderAreaStyler.applyPartnerCustomizationHeaderStyle(textView);
            HeaderAreaStyler.applyPartnerCustomizationHeaderAreaStyle((ViewGroup) findManagedViewById);
        }
        tryUpdateAutoTextSizeFlagWithPartnerConfig();
        if (this.autoTextSizeEnabled) {
            autoAdjustTextSize(textView);
        }
    }

    public TextView getTextView() {
        return (TextView) this.templateLayout.findManagedViewById(C3953R.C3956id.suc_layout_title);
    }

    public void setText(int i) {
        TextView textView = getTextView();
        if (textView != null) {
            if (this.autoTextSizeEnabled) {
                autoAdjustTextSize(textView);
            }
            textView.setText(i);
        }
    }

    public void setText(CharSequence charSequence) {
        TextView textView = getTextView();
        if (textView != null) {
            if (this.autoTextSizeEnabled) {
                autoAdjustTextSize(textView);
            }
            textView.setText(charSequence);
        }
    }

    private void autoAdjustTextSize(final TextView textView) {
        if (textView != null) {
            textView.setTextSize(0, this.headerAutoSizeMaxTextSizeInPx);
            textView.setLineHeight(Math.round(this.headerAutoSizeLineExtraSpacingInPx + this.headerAutoSizeMaxTextSizeInPx));
            textView.setMaxLines(6);
            textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    textView.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (textView.getLineCount() <= HeaderMixin.this.headerAutoSizeMaxLineOfMaxSize) {
                        return true;
                    }
                    textView.setTextSize(0, HeaderMixin.this.headerAutoSizeMinTextSizeInPx);
                    textView.setLineHeight(Math.round(HeaderMixin.this.headerAutoSizeLineExtraSpacingInPx + HeaderMixin.this.headerAutoSizeMinTextSizeInPx));
                    textView.invalidate();
                    return false;
                }
            });
        }
    }

    public CharSequence getText() {
        TextView textView = getTextView();
        if (textView != null) {
            return textView.getText();
        }
        return null;
    }

    public void setVisibility(int i) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setVisibility(i);
        }
    }

    public void setTextColor(ColorStateList colorStateList) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public void setBackgroundColor(int i) {
        TextView textView = getTextView();
        if (textView != null) {
            ViewParent parent = textView.getParent();
            if (parent instanceof LinearLayout) {
                ((LinearLayout) parent).setBackgroundColor(i);
            }
        }
    }

    public ColorStateList getTextColor() {
        TextView textView = getTextView();
        if (textView != null) {
            return textView.getTextColors();
        }
        return null;
    }
}
