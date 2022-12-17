package com.android.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sysprop.SetupWizardProperties;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.util.ThemeHelper;
import java.util.Arrays;

public class SetupWizardUtils {
    public static String getThemeString(Intent intent) {
        String stringExtra = intent.getStringExtra("theme");
        return stringExtra == null ? (String) SetupWizardProperties.theme().orElse("") : stringExtra;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getTheme(android.content.Context r18, android.content.Intent r19) {
        /*
            java.lang.String r0 = getThemeString(r19)
            if (r0 == 0) goto L_0x0160
            boolean r1 = com.google.android.setupcompat.util.WizardManagerHelper.isAnySetupWizard(r19)
            java.lang.String r3 = "glif_light"
            r4 = 6
            java.lang.String r5 = "glif_v4"
            r6 = 5
            java.lang.String r7 = "glif_v3"
            r8 = 4
            java.lang.String r9 = "glif_v2"
            r10 = 3
            java.lang.String r11 = "glif"
            r12 = 2
            java.lang.String r13 = "glif_v4_light"
            r14 = 1
            java.lang.String r15 = "glif_v3_light"
            r16 = 0
            java.lang.String r2 = "glif_v2_light"
            r17 = -1
            if (r1 == 0) goto L_0x00fe
            boolean r1 = com.google.android.setupdesign.util.ThemeHelper.isSetupWizardDayNightEnabled(r18)
            if (r1 == 0) goto L_0x008f
            int r1 = r0.hashCode()
            switch(r1) {
                case -2128555920: goto L_0x0075;
                case -1241052239: goto L_0x006c;
                case -353548558: goto L_0x0063;
                case 3175618: goto L_0x005a;
                case 115650329: goto L_0x0051;
                case 115650330: goto L_0x0048;
                case 115650331: goto L_0x003f;
                case 767685465: goto L_0x0036;
                default: goto L_0x0033;
            }
        L_0x0033:
            r2 = r17
            goto L_0x007e
        L_0x0036:
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x003d
            goto L_0x0033
        L_0x003d:
            r2 = 7
            goto L_0x007e
        L_0x003f:
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L_0x0046
            goto L_0x0033
        L_0x0046:
            r2 = r4
            goto L_0x007e
        L_0x0048:
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x004f
            goto L_0x0033
        L_0x004f:
            r2 = r6
            goto L_0x007e
        L_0x0051:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x0058
            goto L_0x0033
        L_0x0058:
            r2 = r8
            goto L_0x007e
        L_0x005a:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x0061
            goto L_0x0033
        L_0x0061:
            r2 = r10
            goto L_0x007e
        L_0x0063:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x006a
            goto L_0x0033
        L_0x006a:
            r2 = r12
            goto L_0x007e
        L_0x006c:
            boolean r0 = r0.equals(r15)
            if (r0 != 0) goto L_0x0073
            goto L_0x0033
        L_0x0073:
            r2 = r14
            goto L_0x007e
        L_0x0075:
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x007c
            goto L_0x0033
        L_0x007c:
            r2 = r16
        L_0x007e:
            switch(r2) {
                case 0: goto L_0x008c;
                case 1: goto L_0x0089;
                case 2: goto L_0x0086;
                case 3: goto L_0x0083;
                case 4: goto L_0x008c;
                case 5: goto L_0x0089;
                case 6: goto L_0x0086;
                case 7: goto L_0x0083;
                default: goto L_0x0081;
            }
        L_0x0081:
            goto L_0x0160
        L_0x0083:
            int r0 = com.android.settings.R$style.GlifTheme_DayNight
            return r0
        L_0x0086:
            int r0 = com.android.settings.R$style.GlifV4Theme_DayNight
            return r0
        L_0x0089:
            int r0 = com.android.settings.R$style.GlifV3Theme_DayNight
            return r0
        L_0x008c:
            int r0 = com.android.settings.R$style.GlifV2Theme_DayNight
            return r0
        L_0x008f:
            int r1 = r0.hashCode()
            switch(r1) {
                case -2128555920: goto L_0x00d8;
                case -1241052239: goto L_0x00cf;
                case -353548558: goto L_0x00c6;
                case 3175618: goto L_0x00bd;
                case 115650329: goto L_0x00b4;
                case 115650330: goto L_0x00ab;
                case 115650331: goto L_0x00a2;
                case 767685465: goto L_0x0099;
                default: goto L_0x0096;
            }
        L_0x0096:
            r2 = r17
            goto L_0x00e1
        L_0x0099:
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00a0
            goto L_0x0096
        L_0x00a0:
            r2 = 7
            goto L_0x00e1
        L_0x00a2:
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L_0x00a9
            goto L_0x0096
        L_0x00a9:
            r2 = r4
            goto L_0x00e1
        L_0x00ab:
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x00b2
            goto L_0x0096
        L_0x00b2:
            r2 = r6
            goto L_0x00e1
        L_0x00b4:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x00bb
            goto L_0x0096
        L_0x00bb:
            r2 = r8
            goto L_0x00e1
        L_0x00bd:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x00c4
            goto L_0x0096
        L_0x00c4:
            r2 = r10
            goto L_0x00e1
        L_0x00c6:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x00cd
            goto L_0x0096
        L_0x00cd:
            r2 = r12
            goto L_0x00e1
        L_0x00cf:
            boolean r0 = r0.equals(r15)
            if (r0 != 0) goto L_0x00d6
            goto L_0x0096
        L_0x00d6:
            r2 = r14
            goto L_0x00e1
        L_0x00d8:
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x00df
            goto L_0x0096
        L_0x00df:
            r2 = r16
        L_0x00e1:
            switch(r2) {
                case 0: goto L_0x00fb;
                case 1: goto L_0x00f8;
                case 2: goto L_0x00f5;
                case 3: goto L_0x00f2;
                case 4: goto L_0x00ef;
                case 5: goto L_0x00ec;
                case 6: goto L_0x00e9;
                case 7: goto L_0x00e6;
                default: goto L_0x00e4;
            }
        L_0x00e4:
            goto L_0x0160
        L_0x00e6:
            int r0 = com.android.settings.R$style.GlifTheme_Light
            return r0
        L_0x00e9:
            int r0 = com.android.settings.R$style.GlifV4Theme
            return r0
        L_0x00ec:
            int r0 = com.android.settings.R$style.GlifV3Theme
            return r0
        L_0x00ef:
            int r0 = com.android.settings.R$style.GlifV2Theme
            return r0
        L_0x00f2:
            int r0 = com.android.settings.R$style.GlifTheme
            return r0
        L_0x00f5:
            int r0 = com.android.settings.R$style.GlifV4Theme_Light
            return r0
        L_0x00f8:
            int r0 = com.android.settings.R$style.GlifV3Theme_Light
            return r0
        L_0x00fb:
            int r0 = com.android.settings.R$style.GlifV2Theme_Light
            return r0
        L_0x00fe:
            int r1 = r0.hashCode()
            switch(r1) {
                case -2128555920: goto L_0x0147;
                case -1241052239: goto L_0x013e;
                case -353548558: goto L_0x0135;
                case 3175618: goto L_0x012c;
                case 115650329: goto L_0x0123;
                case 115650330: goto L_0x011a;
                case 115650331: goto L_0x0111;
                case 767685465: goto L_0x0108;
                default: goto L_0x0105;
            }
        L_0x0105:
            r2 = r17
            goto L_0x0150
        L_0x0108:
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x010f
            goto L_0x0105
        L_0x010f:
            r2 = 7
            goto L_0x0150
        L_0x0111:
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L_0x0118
            goto L_0x0105
        L_0x0118:
            r2 = r4
            goto L_0x0150
        L_0x011a:
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0121
            goto L_0x0105
        L_0x0121:
            r2 = r6
            goto L_0x0150
        L_0x0123:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x012a
            goto L_0x0105
        L_0x012a:
            r2 = r8
            goto L_0x0150
        L_0x012c:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x0133
            goto L_0x0105
        L_0x0133:
            r2 = r10
            goto L_0x0150
        L_0x0135:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x013c
            goto L_0x0105
        L_0x013c:
            r2 = r12
            goto L_0x0150
        L_0x013e:
            boolean r0 = r0.equals(r15)
            if (r0 != 0) goto L_0x0145
            goto L_0x0105
        L_0x0145:
            r2 = r14
            goto L_0x0150
        L_0x0147:
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x014e
            goto L_0x0105
        L_0x014e:
            r2 = r16
        L_0x0150:
            switch(r2) {
                case 0: goto L_0x015d;
                case 1: goto L_0x015a;
                case 2: goto L_0x0157;
                case 3: goto L_0x0154;
                case 4: goto L_0x015d;
                case 5: goto L_0x015a;
                case 6: goto L_0x0157;
                case 7: goto L_0x0154;
                default: goto L_0x0153;
            }
        L_0x0153:
            goto L_0x0160
        L_0x0154:
            int r0 = com.android.settings.R$style.GlifTheme
            return r0
        L_0x0157:
            int r0 = com.android.settings.R$style.GlifV4Theme
            return r0
        L_0x015a:
            int r0 = com.android.settings.R$style.GlifV3Theme
            return r0
        L_0x015d:
            int r0 = com.android.settings.R$style.GlifV2Theme
            return r0
        L_0x0160:
            int r0 = com.android.settings.R$style.GlifTheme
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.SetupWizardUtils.getTheme(android.content.Context, android.content.Intent):int");
    }

    public static int getTransparentTheme(Context context, Intent intent) {
        int i;
        int theme = getTheme(context, intent);
        if (ThemeHelper.isSetupWizardDayNightEnabled(context)) {
            i = R$style.GlifV2Theme_DayNight_Transparent;
        } else {
            i = R$style.GlifV2Theme_Light_Transparent;
        }
        if (theme == R$style.GlifV3Theme_DayNight) {
            return R$style.GlifV3Theme_DayNight_Transparent;
        }
        if (theme == R$style.GlifV3Theme_Light) {
            return R$style.GlifV3Theme_Light_Transparent;
        }
        if (theme == R$style.GlifV2Theme_DayNight) {
            return R$style.GlifV2Theme_DayNight_Transparent;
        }
        if (theme == R$style.GlifV2Theme_Light) {
            return R$style.GlifV2Theme_Light_Transparent;
        }
        if (theme == R$style.GlifTheme_DayNight) {
            return R$style.SetupWizardTheme_DayNight_Transparent;
        }
        if (theme == R$style.GlifTheme_Light) {
            return R$style.SetupWizardTheme_Light_Transparent;
        }
        if (theme == R$style.GlifV3Theme) {
            return R$style.GlifV3Theme_Transparent;
        }
        if (theme == R$style.GlifV2Theme) {
            return R$style.GlifV2Theme_Transparent;
        }
        return theme == R$style.GlifTheme ? R$style.SetupWizardTheme_Transparent : i;
    }

    public static void copySetupExtras(Intent intent, Intent intent2) {
        WizardManagerHelper.copyWizardManagerExtras(intent, intent2);
    }

    public static Bundle copyLifecycleExtra(Bundle bundle, Bundle bundle2) {
        for (String str : Arrays.asList(new String[]{"firstRun", "isSetupFlow"})) {
            bundle2.putBoolean(str, bundle.getBoolean(str, false));
        }
        return bundle2;
    }
}
