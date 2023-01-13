package com.google.android.setupdesign.util;

import android.content.Context;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.BuildCompatUtils;
import com.google.android.setupdesign.C3963R;
import com.google.android.setupdesign.util.TextViewPartnerStyler;

public final class HeaderAreaStyler {
    private static final String TAG = "HeaderAreaStyler";
    static final String WARN_TO_USE_DRAWABLE = "To achieve scaling icon in SetupDesign lib, should use vector drawable icon from ";

    public static void applyPartnerCustomizationHeaderStyle(TextView textView) {
        if (textView != null) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_HEADER_TEXT_COLOR, (PartnerConfig) null, PartnerConfig.CONFIG_HEADER_TEXT_SIZE, PartnerConfig.CONFIG_HEADER_FONT_FAMILY, (PartnerConfig) null, PartnerConfig.CONFIG_HEADER_TEXT_MARGIN_TOP, PartnerConfig.CONFIG_HEADER_TEXT_MARGIN_BOTTOM, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationDescriptionHeavyStyle(TextView textView) {
        if (textView != null) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_DESCRIPTION_TEXT_COLOR, PartnerConfig.CONFIG_DESCRIPTION_LINK_TEXT_COLOR, PartnerConfig.CONFIG_DESCRIPTION_TEXT_SIZE, PartnerConfig.CONFIG_DESCRIPTION_FONT_FAMILY, PartnerConfig.CONFIG_DESCRIPTION_LINK_FONT_FAMILY, PartnerConfig.CONFIG_DESCRIPTION_TEXT_MARGIN_TOP, PartnerConfig.CONFIG_DESCRIPTION_TEXT_MARGIN_BOTTOM, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationHeaderAreaStyle(ViewGroup viewGroup) {
        if (viewGroup != null) {
            Context context = viewGroup.getContext();
            viewGroup.setBackgroundColor(PartnerConfigHelper.get(context).getColor(context, PartnerConfig.CONFIG_HEADER_AREA_BACKGROUND_COLOR));
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_HEADER_CONTAINER_MARGIN_BOTTOM)) {
                ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_HEADER_CONTAINER_MARGIN_BOTTOM));
                    viewGroup.setLayoutParams(layoutParams);
                }
            }
        }
    }

    public static void applyPartnerCustomizationProgressBarStyle(ProgressBar progressBar) {
        if (progressBar != null) {
            Context context = progressBar.getContext();
            ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                int i = marginLayoutParams.topMargin;
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_PROGRESS_BAR_MARGIN_TOP)) {
                    i = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_PROGRESS_BAR_MARGIN_TOP, context.getResources().getDimension(C3963R.dimen.sud_progress_bar_margin_top));
                }
                int i2 = marginLayoutParams.bottomMargin;
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_PROGRESS_BAR_MARGIN_BOTTOM)) {
                    i2 = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_PROGRESS_BAR_MARGIN_BOTTOM, context.getResources().getDimension(C3963R.dimen.sud_progress_bar_margin_bottom));
                }
                if (i != marginLayoutParams.topMargin || i2 != marginLayoutParams.bottomMargin) {
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, i, marginLayoutParams.rightMargin, i2);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0077  */
    /* JADX WARNING: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void applyPartnerCustomizationIconStyle(android.widget.ImageView r4, android.widget.FrameLayout r5) {
        /*
            if (r4 == 0) goto L_0x008e
            if (r5 != 0) goto L_0x0006
            goto L_0x008e
        L_0x0006:
            android.content.Context r0 = r4.getContext()
            int r1 = com.google.android.setupdesign.util.PartnerStyleHelper.getLayoutGravity(r0)
            if (r1 == 0) goto L_0x0013
            setGravity(r4, r1)
        L_0x0013:
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r1 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r2 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_ICON_SIZE
            boolean r1 = r1.isPartnerConfigAvailable(r2)
            if (r1 == 0) goto L_0x0062
            checkImageType(r4)
            android.view.ViewGroup$LayoutParams r1 = r4.getLayoutParams()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r2 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r3 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_ICON_SIZE
            float r2 = r2.getDimension(r0, r3)
            int r2 = (int) r2
            r1.height = r2
            r2 = -2
            r1.width = r2
            android.widget.ImageView$ScaleType r2 = android.widget.ImageView.ScaleType.FIT_CENTER
            r4.setScaleType(r2)
            android.graphics.drawable.Drawable r4 = r4.getDrawable()
            if (r4 == 0) goto L_0x0062
            int r2 = r4.getIntrinsicWidth()
            int r4 = r4.getIntrinsicHeight()
            int r4 = r4 * 2
            if (r2 <= r4) goto L_0x0062
            android.content.res.Resources r4 = r0.getResources()
            int r2 = com.google.android.setupdesign.C3963R.dimen.sud_horizontal_icon_height
            float r4 = r4.getDimension(r2)
            int r4 = (int) r4
            int r2 = r1.height
            if (r2 <= r4) goto L_0x0062
            int r2 = r1.height
            int r2 = r2 - r4
            r1.height = r4
            goto L_0x0063
        L_0x0062:
            r2 = 0
        L_0x0063:
            android.view.ViewGroup$LayoutParams r4 = r5.getLayoutParams()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r5 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r1 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_ICON_MARGIN_TOP
            boolean r5 = r5.isPartnerConfigAvailable(r1)
            if (r5 == 0) goto L_0x008e
            boolean r5 = r4 instanceof android.view.ViewGroup.MarginLayoutParams
            if (r5 == 0) goto L_0x008e
            android.view.ViewGroup$MarginLayoutParams r4 = (android.view.ViewGroup.MarginLayoutParams) r4
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r5 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r1 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_ICON_MARGIN_TOP
            float r5 = r5.getDimension(r0, r1)
            int r5 = (int) r5
            int r5 = r5 + r2
            int r0 = r4.leftMargin
            int r1 = r4.rightMargin
            int r2 = r4.bottomMargin
            r4.setMargins(r0, r5, r1, r2)
        L_0x008e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupdesign.util.HeaderAreaStyler.applyPartnerCustomizationIconStyle(android.widget.ImageView, android.widget.FrameLayout):void");
    }

    private static void checkImageType(final ImageView imageView) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (!BuildCompatUtils.isAtLeastS() || imageView.getDrawable() == null || (imageView.getDrawable() instanceof VectorDrawable) || (imageView.getDrawable() instanceof VectorDrawableCompat)) {
                    return true;
                }
                if (!Build.TYPE.equals("userdebug") && !Build.TYPE.equals("eng")) {
                    return true;
                }
                Log.w(HeaderAreaStyler.TAG, HeaderAreaStyler.WARN_TO_USE_DRAWABLE + imageView.getContext().getPackageName());
                return true;
            }
        });
    }

    private static void setGravity(ImageView imageView, int i) {
        if (imageView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.gravity = i;
            imageView.setLayoutParams(layoutParams);
        }
    }

    private HeaderAreaStyler() {
    }
}
