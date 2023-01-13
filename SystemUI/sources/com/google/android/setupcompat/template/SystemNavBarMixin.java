package com.google.android.setupcompat.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Window;
import androidx.core.view.ViewCompat;
import com.google.android.setupcompat.C3941R;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.SystemBarHelper;

public class SystemNavBarMixin implements Mixin {
    final boolean applyPartnerResources;
    private int sucSystemNavBarBackgroundColor = 0;
    private final TemplateLayout templateLayout;
    final boolean useFullDynamicColor;
    private final Window windowOfActivity;

    public SystemNavBarMixin(TemplateLayout templateLayout2, Window window) {
        boolean z = false;
        this.templateLayout = templateLayout2;
        this.windowOfActivity = window;
        boolean z2 = templateLayout2 instanceof PartnerCustomizationLayout;
        this.applyPartnerResources = z2 && ((PartnerCustomizationLayout) templateLayout2).shouldApplyPartnerResource();
        if (z2 && ((PartnerCustomizationLayout) templateLayout2).useFullDynamicColor()) {
            z = true;
        }
        this.useFullDynamicColor = z;
    }

    public void applyPartnerCustomizations(AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = this.templateLayout.getContext().obtainStyledAttributes(attributeSet, C3941R.styleable.SucSystemNavBarMixin, i, 0);
        int color = obtainStyledAttributes.getColor(C3941R.styleable.SucSystemNavBarMixin_sucSystemNavBarBackgroundColor, 0);
        this.sucSystemNavBarBackgroundColor = color;
        setSystemNavBarBackground(color);
        setLightSystemNavBar(obtainStyledAttributes.getBoolean(C3941R.styleable.SucSystemNavBarMixin_sucLightSystemNavBar, isLightSystemNavBar()));
        TypedArray obtainStyledAttributes2 = this.templateLayout.getContext().obtainStyledAttributes(new int[]{16844141});
        setSystemNavBarDividerColor(obtainStyledAttributes.getColor(C3941R.styleable.SucSystemNavBarMixin_sucSystemNavBarDividerColor, obtainStyledAttributes2.getColor(0, 0)));
        obtainStyledAttributes2.recycle();
        obtainStyledAttributes.recycle();
    }

    public void setSystemNavBarBackground(int i) {
        if (this.windowOfActivity != null) {
            if (this.applyPartnerResources && !this.useFullDynamicColor) {
                Context context = this.templateLayout.getContext();
                i = PartnerConfigHelper.get(context).getColor(context, PartnerConfig.CONFIG_NAVIGATION_BAR_BG_COLOR);
            }
            this.windowOfActivity.setNavigationBarColor(i);
        }
    }

    public int getSystemNavBarBackground() {
        Window window = this.windowOfActivity;
        return window != null ? window.getNavigationBarColor() : ViewCompat.MEASURED_STATE_MASK;
    }

    public void setLightSystemNavBar(boolean z) {
        if (this.windowOfActivity != null) {
            if (this.applyPartnerResources) {
                Context context = this.templateLayout.getContext();
                z = PartnerConfigHelper.get(context).getBoolean(context, PartnerConfig.CONFIG_LIGHT_NAVIGATION_BAR, false);
            }
            if (z) {
                this.windowOfActivity.getDecorView().setSystemUiVisibility(this.windowOfActivity.getDecorView().getSystemUiVisibility() | 16);
            } else {
                this.windowOfActivity.getDecorView().setSystemUiVisibility(this.windowOfActivity.getDecorView().getSystemUiVisibility() & -17);
            }
        }
    }

    public boolean isLightSystemNavBar() {
        Window window = this.windowOfActivity;
        if (window == null || (window.getDecorView().getSystemUiVisibility() & 16) == 16) {
            return true;
        }
        return false;
    }

    public void setSystemNavBarDividerColor(int i) {
        if (this.windowOfActivity != null) {
            if (this.applyPartnerResources) {
                Context context = this.templateLayout.getContext();
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_NAVIGATION_BAR_DIVIDER_COLOR)) {
                    i = PartnerConfigHelper.get(context).getColor(context, PartnerConfig.CONFIG_NAVIGATION_BAR_DIVIDER_COLOR);
                }
            }
            this.windowOfActivity.setNavigationBarDividerColor(i);
        }
    }

    public void hideSystemBars(Window window) {
        SystemBarHelper.addVisibilityFlag(window, (int) SystemBarHelper.DEFAULT_IMMERSIVE_FLAGS);
        SystemBarHelper.addImmersiveFlagsToDecorView(window, SystemBarHelper.DEFAULT_IMMERSIVE_FLAGS);
        window.setNavigationBarColor(0);
        window.setStatusBarColor(0);
    }

    public void showSystemBars(Window window, Context context) {
        SystemBarHelper.removeVisibilityFlag(window, (int) SystemBarHelper.DEFAULT_IMMERSIVE_FLAGS);
        SystemBarHelper.removeImmersiveFlagsFromDecorView(window, SystemBarHelper.DEFAULT_IMMERSIVE_FLAGS);
        if (context != null) {
            int i = 0;
            if (this.applyPartnerResources) {
                int color = PartnerConfigHelper.get(context).getColor(context, PartnerConfig.CONFIG_NAVIGATION_BAR_BG_COLOR);
                window.setStatusBarColor(0);
                window.setNavigationBarColor(color);
                return;
            }
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843857, 16843858});
            int color2 = obtainStyledAttributes.getColor(0, 0);
            int color3 = obtainStyledAttributes.getColor(1, 0);
            if (this.templateLayout instanceof PartnerCustomizationLayout) {
                color3 = this.sucSystemNavBarBackgroundColor;
            } else {
                i = color2;
            }
            window.setStatusBarColor(i);
            window.setNavigationBarColor(color3);
            obtainStyledAttributes.recycle();
        }
    }
}
