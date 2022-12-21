package com.google.android.setupdesign.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupdesign.C3953R;
import com.google.android.setupdesign.util.TextViewPartnerStyler;
import java.util.Locale;

public final class ContentStyler {
    public static void applyBodyPartnerCustomizationStyle(TextView textView) {
        if (PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource((View) textView)) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_CONTENT_TEXT_COLOR, PartnerConfig.CONFIG_CONTENT_LINK_TEXT_COLOR, PartnerConfig.CONFIG_CONTENT_TEXT_SIZE, PartnerConfig.CONFIG_CONTENT_FONT_FAMILY, PartnerConfig.CONFIG_DESCRIPTION_LINK_FONT_FAMILY, (PartnerConfig) null, (PartnerConfig) null, getPartnerContentTextGravity(textView.getContext())));
        }
    }

    public static void applyInfoPartnerCustomizationStyle(View view, ImageView imageView, TextView textView) {
        float f;
        float f2;
        View view2 = view;
        ImageView imageView2 = imageView;
        TextView textView2 = textView;
        if (PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource((View) textView)) {
            Context context = textView.getContext();
            boolean isPartnerConfigAvailable = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CONTENT_INFO_TEXT_SIZE);
            boolean isPartnerConfigAvailable2 = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CONTENT_INFO_FONT_FAMILY);
            boolean isPartnerConfigAvailable3 = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_DESCRIPTION_LINK_FONT_FAMILY);
            PartnerConfig partnerConfig = isPartnerConfigAvailable ? PartnerConfig.CONFIG_CONTENT_INFO_TEXT_SIZE : null;
            PartnerConfig partnerConfig2 = isPartnerConfigAvailable2 ? PartnerConfig.CONFIG_CONTENT_INFO_FONT_FAMILY : null;
            PartnerConfig partnerConfig3 = isPartnerConfigAvailable3 ? PartnerConfig.CONFIG_DESCRIPTION_LINK_FONT_FAMILY : null;
            TextViewPartnerStyler.TextPartnerConfigs textPartnerConfigs = r7;
            TextViewPartnerStyler.TextPartnerConfigs textPartnerConfigs2 = new TextViewPartnerStyler.TextPartnerConfigs((PartnerConfig) null, (PartnerConfig) null, partnerConfig, partnerConfig2, partnerConfig3, (PartnerConfig) null, (PartnerConfig) null, 0);
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView2, textPartnerConfigs);
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CONTENT_INFO_LINE_SPACING_EXTRA)) {
                int dimension = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CONTENT_INFO_LINE_SPACING_EXTRA);
                float textSize = textView.getTextSize();
                if (isPartnerConfigAvailable) {
                    float dimension2 = PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CONTENT_INFO_TEXT_SIZE, 0.0f);
                    if (dimension2 > 0.0f) {
                        textSize = dimension2;
                    }
                }
                textView2.setLineHeight(Math.round(((float) dimension) + textSize));
            }
            if (imageView2 != null) {
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CONTENT_INFO_ICON_SIZE)) {
                    int i = layoutParams.height;
                    layoutParams.height = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CONTENT_INFO_ICON_SIZE);
                    layoutParams.width = (layoutParams.width * layoutParams.height) / i;
                    imageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CONTENT_INFO_ICON_MARGIN_END) && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CONTENT_INFO_ICON_MARGIN_END), marginLayoutParams.bottomMargin);
                }
            }
            if (view2 != null) {
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CONTENT_INFO_PADDING_TOP)) {
                    f = PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CONTENT_INFO_PADDING_TOP);
                } else {
                    f = (float) view.getPaddingTop();
                }
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_CONTENT_INFO_PADDING_BOTTOM)) {
                    f2 = PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_CONTENT_INFO_PADDING_BOTTOM);
                } else {
                    f2 = (float) view.getPaddingBottom();
                }
                if (f != ((float) view.getPaddingTop()) || f2 != ((float) view.getPaddingBottom())) {
                    view2.setPadding(0, (int) f, 0, (int) f2);
                }
            }
        }
    }

    public static float getPartnerContentMarginStart(Context context) {
        float dimension = context.getResources().getDimension(C3953R.dimen.sud_layout_margin_sides);
        return PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_START) ? PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_START, dimension) : dimension;
    }

    private static int getPartnerContentTextGravity(Context context) {
        String string = PartnerConfigHelper.get(context).getString(context, PartnerConfig.CONFIG_CONTENT_LAYOUT_GRAVITY);
        if (string == null) {
            return 0;
        }
        String lowerCase = string.toLowerCase(Locale.ROOT);
        lowerCase.hashCode();
        char c = 65535;
        switch (lowerCase.hashCode()) {
            case -1364013995:
                if (lowerCase.equals("center")) {
                    c = 0;
                    break;
                }
                break;
            case 100571:
                if (lowerCase.equals("end")) {
                    c = 1;
                    break;
                }
                break;
            case 109757538:
                if (lowerCase.equals("start")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 17;
            case 1:
                return GravityCompat.END;
            case 2:
                return 8388611;
            default:
                return 0;
        }
    }

    private ContentStyler() {
    }
}
