package com.google.android.setupdesign.util;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupdesign.C3953R;
import com.google.android.setupdesign.util.TextViewPartnerStyler;

public final class ItemStyler {
    public static void applyPartnerCustomizationItemStyle(View view) {
        if (view != null && PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource(view)) {
            applyPartnerCustomizationItemTitleStyle((TextView) view.findViewById(C3953R.C3956id.sud_items_title));
            TextView textView = (TextView) view.findViewById(C3953R.C3956id.sud_items_summary);
            if (textView.getVisibility() == 8 && (view instanceof LinearLayout)) {
                ((LinearLayout) view).setGravity(16);
            }
            applyPartnerCustomizationItemSummaryStyle(textView);
            applyPartnerCustomizationItemViewLayoutStyle(view);
        }
    }

    public static void applyPartnerCustomizationItemTitleStyle(TextView textView) {
        if (PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource((View) textView)) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs((PartnerConfig) null, (PartnerConfig) null, PartnerConfig.CONFIG_ITEMS_TITLE_TEXT_SIZE, PartnerConfig.CONFIG_ITEMS_TITLE_FONT_FAMILY, (PartnerConfig) null, (PartnerConfig) null, (PartnerConfig) null, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationItemSummaryStyle(TextView textView) {
        if (PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource((View) textView)) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs((PartnerConfig) null, (PartnerConfig) null, PartnerConfig.CONFIG_ITEMS_SUMMARY_TEXT_SIZE, PartnerConfig.CONFIG_ITEMS_SUMMARY_FONT_FAMILY, (PartnerConfig) null, PartnerConfig.CONFIG_ITEMS_SUMMARY_MARGIN_TOP, (PartnerConfig) null, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    private static void applyPartnerCustomizationItemViewLayoutStyle(View view) {
        float f;
        float f2;
        Context context = view.getContext();
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_ITEMS_PADDING_TOP)) {
            f = PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_ITEMS_PADDING_TOP);
        } else {
            f = (float) view.getPaddingTop();
        }
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_ITEMS_PADDING_BOTTOM)) {
            f2 = PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_ITEMS_PADDING_BOTTOM);
        } else {
            f2 = (float) view.getPaddingBottom();
        }
        if (!(f == ((float) view.getPaddingTop()) && f2 == ((float) view.getPaddingBottom()))) {
            view.setPadding(view.getPaddingStart(), (int) f, view.getPaddingEnd(), (int) f2);
        }
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_ITEMS_MIN_HEIGHT)) {
            view.setMinimumHeight((int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_ITEMS_MIN_HEIGHT));
        }
    }

    private ItemStyler() {
    }
}
