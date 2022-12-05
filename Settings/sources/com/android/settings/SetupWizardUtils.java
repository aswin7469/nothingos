package com.android.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sysprop.SetupWizardProperties;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.util.ThemeHelper;
import java.util.Arrays;
/* loaded from: classes.dex */
public class SetupWizardUtils {
    public static String getThemeString(Intent intent) {
        String stringExtra = intent.getStringExtra("theme");
        return stringExtra == null ? (String) SetupWizardProperties.theme().orElse("") : stringExtra;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0032, code lost:
        if (r0.equals("glif_light") == false) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x007c, code lost:
        if (r0.equals("glif_light") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x00cf, code lost:
        if (r0.equals("glif_light") == false) goto L70;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int getTheme(Context context, Intent intent) {
        String themeString = getThemeString(intent);
        if (themeString != null) {
            char c = 5;
            if (WizardManagerHelper.isAnySetupWizard(intent)) {
                if (ThemeHelper.isSetupWizardDayNightEnabled(context)) {
                    switch (themeString.hashCode()) {
                        case -2128555920:
                            if (themeString.equals("glif_v2_light")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1241052239:
                            if (themeString.equals("glif_v3_light")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 3175618:
                            if (themeString.equals("glif")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 115650329:
                            if (themeString.equals("glif_v2")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case 115650330:
                            if (themeString.equals("glif_v3")) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case 767685465:
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                        case 3:
                            return R.style.GlifV2Theme_DayNight;
                        case 1:
                        case 4:
                            return R.style.GlifV3Theme_DayNight;
                        case 2:
                        case 5:
                            return R.style.GlifTheme_DayNight;
                    }
                }
                switch (themeString.hashCode()) {
                    case -2128555920:
                        if (themeString.equals("glif_v2_light")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1241052239:
                        if (themeString.equals("glif_v3_light")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case 3175618:
                        if (themeString.equals("glif")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case 115650329:
                        if (themeString.equals("glif_v2")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case 115650330:
                        if (themeString.equals("glif_v3")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case 767685465:
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        return R.style.GlifV2Theme_Light;
                    case 1:
                        return R.style.GlifV3Theme_Light;
                    case 2:
                        return R.style.GlifTheme;
                    case 3:
                        return R.style.GlifV2Theme;
                    case 4:
                        return R.style.GlifV3Theme;
                    case 5:
                        return R.style.GlifTheme_Light;
                }
            }
            switch (themeString.hashCode()) {
                case -2128555920:
                    if (themeString.equals("glif_v2_light")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1241052239:
                    if (themeString.equals("glif_v3_light")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 3175618:
                    if (themeString.equals("glif")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 115650329:
                    if (themeString.equals("glif_v2")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 115650330:
                    if (themeString.equals("glif_v3")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 767685465:
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                case 3:
                    return R.style.GlifV2Theme;
                case 1:
                case 4:
                    return R.style.GlifV3Theme;
                case 2:
                case 5:
                    return R.style.GlifTheme;
            }
        }
        return R.style.GlifTheme;
    }

    public static int getTransparentTheme(Context context, Intent intent) {
        int i;
        int theme = getTheme(context, intent);
        if (ThemeHelper.isSetupWizardDayNightEnabled(context)) {
            i = R.style.GlifV2Theme_DayNight_Transparent;
        } else {
            i = R.style.GlifV2Theme_Light_Transparent;
        }
        if (theme == R.style.GlifV3Theme_DayNight) {
            return R.style.GlifV3Theme_DayNight_Transparent;
        }
        if (theme == R.style.GlifV3Theme_Light) {
            return R.style.GlifV3Theme_Light_Transparent;
        }
        if (theme == R.style.GlifV2Theme_DayNight) {
            return R.style.GlifV2Theme_DayNight_Transparent;
        }
        if (theme == R.style.GlifV2Theme_Light) {
            return R.style.GlifV2Theme_Light_Transparent;
        }
        if (theme == R.style.GlifTheme_DayNight) {
            return R.style.SetupWizardTheme_DayNight_Transparent;
        }
        if (theme == R.style.GlifTheme_Light) {
            return R.style.SetupWizardTheme_Light_Transparent;
        }
        if (theme == R.style.GlifV3Theme) {
            return R.style.GlifV3Theme_Transparent;
        }
        if (theme == R.style.GlifV2Theme) {
            return R.style.GlifV2Theme_Transparent;
        }
        return theme == R.style.GlifTheme ? R.style.SetupWizardTheme_Transparent : i;
    }

    public static void copySetupExtras(Intent intent, Intent intent2) {
        WizardManagerHelper.copyWizardManagerExtras(intent, intent2);
    }

    public static Bundle copyLifecycleExtra(Bundle bundle, Bundle bundle2) {
        for (String str : Arrays.asList("firstRun", "isSetupFlow")) {
            bundle2.putBoolean(str, bundle.getBoolean(str, false));
        }
        return bundle2;
    }
}
