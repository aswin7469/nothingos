package com.google.android.setupdesign.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupdesign.C3953R;

public final class LayoutStyler {
    public static void applyPartnerCustomizationLayoutPaddingStyle(View view) {
        int i;
        int i2;
        if (view != null) {
            Context context = view.getContext();
            boolean isPartnerConfigAvailable = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_START);
            boolean isPartnerConfigAvailable2 = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_END);
            if (!PartnerStyleHelper.shouldApplyPartnerResource(view)) {
                return;
            }
            if (isPartnerConfigAvailable || isPartnerConfigAvailable2) {
                if (isPartnerConfigAvailable) {
                    i = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_START);
                } else {
                    i = view.getPaddingStart();
                }
                if (isPartnerConfigAvailable2) {
                    i2 = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_END);
                } else {
                    i2 = view.getPaddingEnd();
                }
                if (i != view.getPaddingStart() || i2 != view.getPaddingEnd()) {
                    view.setPadding(i, view.getPaddingTop(), i2, view.getPaddingBottom());
                }
            }
        }
    }

    public static void applyPartnerCustomizationExtraPaddingStyle(View view) {
        int i;
        int i2;
        ViewGroup.MarginLayoutParams marginLayoutParams;
        if (view != null) {
            Context context = view.getContext();
            boolean isPartnerConfigAvailable = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_START);
            boolean isPartnerConfigAvailable2 = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_END);
            if (!PartnerStyleHelper.shouldApplyPartnerResource(view)) {
                return;
            }
            if (isPartnerConfigAvailable || isPartnerConfigAvailable2) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{C3953R.attr.sudMarginStart, C3953R.attr.sudMarginEnd});
                int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(0, 0);
                int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(1, 0);
                obtainStyledAttributes.recycle();
                if (isPartnerConfigAvailable) {
                    i = ((int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_START)) - dimensionPixelSize;
                } else {
                    i = view.getPaddingStart();
                }
                if (isPartnerConfigAvailable2) {
                    i2 = ((int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_END)) - dimensionPixelSize2;
                    if (view.getId() == C3953R.C3956id.sud_layout_content) {
                        i2 = ((int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_START)) - dimensionPixelSize2;
                    }
                } else {
                    i2 = view.getPaddingEnd();
                    if (view.getId() == C3953R.C3956id.sud_layout_content) {
                        i2 = view.getPaddingStart();
                    }
                }
                if (i != view.getPaddingStart() || i2 != view.getPaddingEnd()) {
                    if (view.getId() == C3953R.C3956id.sud_layout_content) {
                        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                            marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                        } else {
                            marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
                        }
                        marginLayoutParams.setMargins(i, view.getPaddingTop(), i2, view.getPaddingBottom());
                        return;
                    }
                    view.setPadding(i, view.getPaddingTop(), i2, view.getPaddingBottom());
                }
            }
        }
    }

    private LayoutStyler() {
    }
}
