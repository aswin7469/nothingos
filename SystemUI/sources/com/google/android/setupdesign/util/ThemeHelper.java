package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.BuildCompatUtils;
import com.google.android.setupcompat.util.Logger;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.C3963R;
import java.util.Objects;

public final class ThemeHelper {
    private static final Logger LOG = new Logger("ThemeHelper");
    public static final String THEME_GLIF = "glif";
    public static final String THEME_GLIF_LIGHT = "glif_light";
    public static final String THEME_GLIF_V2 = "glif_v2";
    public static final String THEME_GLIF_V2_LIGHT = "glif_v2_light";
    public static final String THEME_GLIF_V3 = "glif_v3";
    public static final String THEME_GLIF_V3_LIGHT = "glif_v3_light";
    public static final String THEME_GLIF_V4 = "glif_v4";
    public static final String THEME_GLIF_V4_LIGHT = "glif_v4_light";
    public static final String THEME_HOLO = "holo";
    public static final String THEME_HOLO_LIGHT = "holo_light";
    public static final String THEME_MATERIAL = "material";
    public static final String THEME_MATERIAL_LIGHT = "material_light";

    public static boolean isLightTheme(Intent intent, boolean z) {
        return isLightTheme(intent.getStringExtra(WizardManagerHelper.EXTRA_THEME), z);
    }

    public static boolean isLightTheme(String str, boolean z) {
        if (THEME_HOLO_LIGHT.equals(str) || THEME_MATERIAL_LIGHT.equals(str) || THEME_GLIF_LIGHT.equals(str) || THEME_GLIF_V2_LIGHT.equals(str) || THEME_GLIF_V3_LIGHT.equals(str) || THEME_GLIF_V4_LIGHT.equals(str)) {
            return true;
        }
        if (THEME_HOLO.equals(str) || THEME_MATERIAL.equals(str) || THEME_GLIF.equals(str) || THEME_GLIF_V2.equals(str) || THEME_GLIF_V3.equals(str) || THEME_GLIF_V4.equals(str)) {
            return false;
        }
        return z;
    }

    public static void applyTheme(Activity activity) {
        ThemeResolver.getDefault().applyTheme(activity);
    }

    public static boolean isSetupWizardDayNightEnabled(Context context) {
        return PartnerConfigHelper.isSetupWizardDayNightEnabled(context);
    }

    public static boolean shouldApplyExtendedPartnerConfig(Context context) {
        return PartnerConfigHelper.shouldApplyExtendedPartnerConfig(context);
    }

    public static boolean shouldApplyMaterialYouStyle(Context context) {
        return PartnerConfigHelper.shouldApplyMaterialYouStyle(context);
    }

    public static boolean shouldApplyDynamicColor(Context context) {
        return PartnerConfigHelper.isSetupWizardDynamicColorEnabled(context);
    }

    public static int getDynamicColorTheme(Context context) {
        int i;
        try {
            boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(PartnerCustomizationLayout.lookupActivityFromContext(context).getIntent());
            boolean isSetupWizardDayNightEnabled = isSetupWizardDayNightEnabled(context);
            if (!isAnySetupWizard) {
                if (isSetupWizardDayNightEnabled) {
                    i = C3963R.style.SudFullDynamicColorTheme_DayNight;
                } else {
                    i = C3963R.style.SudFullDynamicColorTheme_Light;
                }
                LOG.atInfo("Return " + (isSetupWizardDayNightEnabled ? "SudFullDynamicColorTheme_DayNight" : "SudFullDynamicColorTheme_Light"));
            } else if (isSetupWizardDayNightEnabled) {
                i = C3963R.style.SudDynamicColorTheme_DayNight;
            } else {
                i = C3963R.style.SudDynamicColorTheme_Light;
            }
            Logger logger = LOG;
            String str = "n/a";
            StringBuilder append = new StringBuilder("Gets the dynamic accentColor: [Light] ").append(colorIntToHex(context, C3963R.C3964color.sud_dynamic_color_accent_glif_v3_light)).append(", ").append(BuildCompatUtils.isAtLeastS() ? colorIntToHex(context, 17170495) : str).append(", [Dark] ").append(colorIntToHex(context, C3963R.C3964color.sud_dynamic_color_accent_glif_v3_dark)).append(", ");
            if (BuildCompatUtils.isAtLeastS()) {
                str = colorIntToHex(context, 17170490);
            }
            logger.atDebug(append.append(str).toString());
            return i;
        } catch (IllegalArgumentException e) {
            LOG.mo55170e((String) Objects.requireNonNull(e.getMessage()));
            return 0;
        }
    }

    public static boolean trySetDynamicColor(Context context) {
        if (!BuildCompatUtils.isAtLeastS()) {
            LOG.mo55175w("Dynamic color require platform version at least S.");
            return false;
        } else if (!shouldApplyDynamicColor(context)) {
            LOG.mo55175w("SetupWizard does not support the dynamic color or supporting status unknown.");
            return false;
        } else {
            try {
                Activity lookupActivityFromContext = PartnerCustomizationLayout.lookupActivityFromContext(context);
                int dynamicColorTheme = getDynamicColorTheme(context);
                if (dynamicColorTheme != 0) {
                    lookupActivityFromContext.setTheme(dynamicColorTheme);
                    return true;
                }
                LOG.mo55175w("Error occurred on getting dynamic color theme.");
                return false;
            } catch (IllegalArgumentException e) {
                LOG.mo55170e((String) Objects.requireNonNull(e.getMessage()));
                return false;
            }
        }
    }

    private static String colorIntToHex(Context context, int i) {
        return String.format("#%06X", Integer.valueOf(context.getResources().getColor(i) & 16777215));
    }

    private ThemeHelper() {
    }
}
