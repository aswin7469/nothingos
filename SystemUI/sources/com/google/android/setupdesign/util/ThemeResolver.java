package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Intent;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.C3953R;

public class ThemeResolver {
    private static ThemeResolver defaultResolver;
    /* access modifiers changed from: private */
    public final int defaultTheme;
    private final ThemeSupplier defaultThemeSupplier;
    /* access modifiers changed from: private */
    public final String oldestSupportedTheme;
    /* access modifiers changed from: private */
    public final boolean useDayNight;

    public interface ThemeSupplier {
        String getTheme();
    }

    public static void setDefault(ThemeResolver themeResolver) {
        defaultResolver = themeResolver;
    }

    public static ThemeResolver getDefault() {
        if (defaultResolver == null) {
            defaultResolver = new Builder().setDefaultTheme(C3953R.style.SudThemeGlif_DayNight).setUseDayNight(true).build();
        }
        return defaultResolver;
    }

    private ThemeResolver(int i, String str, ThemeSupplier themeSupplier, boolean z) {
        this.defaultTheme = i;
        this.oldestSupportedTheme = str;
        this.defaultThemeSupplier = themeSupplier;
        this.useDayNight = z;
    }

    public int resolve(Intent intent) {
        return resolve(intent.getStringExtra(WizardManagerHelper.EXTRA_THEME), WizardManagerHelper.isAnySetupWizard(intent));
    }

    public int resolve(Intent intent, boolean z) {
        return resolve(intent.getStringExtra(WizardManagerHelper.EXTRA_THEME), z);
    }

    @Deprecated
    public int resolve(String str) {
        return resolve(str, false);
    }

    public int resolve(String str, boolean z) {
        int themeRes = (!this.useDayNight || z) ? getThemeRes(str) : getDayNightThemeRes(str);
        if (themeRes == 0) {
            ThemeSupplier themeSupplier = this.defaultThemeSupplier;
            if (themeSupplier != null) {
                str = themeSupplier.getTheme();
                themeRes = (!this.useDayNight || z) ? getThemeRes(str) : getDayNightThemeRes(str);
            }
            if (themeRes == 0) {
                return this.defaultTheme;
            }
        }
        String str2 = this.oldestSupportedTheme;
        return (str2 == null || compareThemes(str, str2) >= 0) ? themeRes : this.defaultTheme;
    }

    public void applyTheme(Activity activity) {
        activity.setTheme(resolve(activity.getIntent(), WizardManagerHelper.isAnySetupWizard(activity.getIntent()) && !ThemeHelper.isSetupWizardDayNightEnabled(activity)));
    }

    private static int getDayNightThemeRes(String str) {
        if (str != null) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2128555920:
                    if (str.equals(ThemeHelper.THEME_GLIF_V2_LIGHT)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1270463490:
                    if (str.equals(ThemeHelper.THEME_MATERIAL_LIGHT)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1241052239:
                    if (str.equals(ThemeHelper.THEME_GLIF_V3_LIGHT)) {
                        c = 2;
                        break;
                    }
                    break;
                case -353548558:
                    if (str.equals(ThemeHelper.THEME_GLIF_V4_LIGHT)) {
                        c = 3;
                        break;
                    }
                    break;
                case 3175618:
                    if (str.equals(ThemeHelper.THEME_GLIF)) {
                        c = 4;
                        break;
                    }
                    break;
                case 115650329:
                    if (str.equals(ThemeHelper.THEME_GLIF_V2)) {
                        c = 5;
                        break;
                    }
                    break;
                case 115650330:
                    if (str.equals(ThemeHelper.THEME_GLIF_V3)) {
                        c = 6;
                        break;
                    }
                    break;
                case 115650331:
                    if (str.equals(ThemeHelper.THEME_GLIF_V4)) {
                        c = 7;
                        break;
                    }
                    break;
                case 299066663:
                    if (str.equals(ThemeHelper.THEME_MATERIAL)) {
                        c = 8;
                        break;
                    }
                    break;
                case 767685465:
                    if (str.equals(ThemeHelper.THEME_GLIF_LIGHT)) {
                        c = 9;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 5:
                    return C3953R.style.SudThemeGlifV2_DayNight;
                case 1:
                case 8:
                    return C3953R.style.SudThemeMaterial_DayNight;
                case 2:
                case 6:
                    return C3953R.style.SudThemeGlifV3_DayNight;
                case 3:
                case 7:
                    return C3953R.style.SudThemeGlifV4_DayNight;
                case 4:
                case 9:
                    return C3953R.style.SudThemeGlif_DayNight;
            }
        }
        return 0;
    }

    private static int getThemeRes(String str) {
        if (str != null) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2128555920:
                    if (str.equals(ThemeHelper.THEME_GLIF_V2_LIGHT)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1270463490:
                    if (str.equals(ThemeHelper.THEME_MATERIAL_LIGHT)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1241052239:
                    if (str.equals(ThemeHelper.THEME_GLIF_V3_LIGHT)) {
                        c = 2;
                        break;
                    }
                    break;
                case -353548558:
                    if (str.equals(ThemeHelper.THEME_GLIF_V4_LIGHT)) {
                        c = 3;
                        break;
                    }
                    break;
                case 3175618:
                    if (str.equals(ThemeHelper.THEME_GLIF)) {
                        c = 4;
                        break;
                    }
                    break;
                case 115650329:
                    if (str.equals(ThemeHelper.THEME_GLIF_V2)) {
                        c = 5;
                        break;
                    }
                    break;
                case 115650330:
                    if (str.equals(ThemeHelper.THEME_GLIF_V3)) {
                        c = 6;
                        break;
                    }
                    break;
                case 115650331:
                    if (str.equals(ThemeHelper.THEME_GLIF_V4)) {
                        c = 7;
                        break;
                    }
                    break;
                case 299066663:
                    if (str.equals(ThemeHelper.THEME_MATERIAL)) {
                        c = 8;
                        break;
                    }
                    break;
                case 767685465:
                    if (str.equals(ThemeHelper.THEME_GLIF_LIGHT)) {
                        c = 9;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return C3953R.style.SudThemeGlifV2_Light;
                case 1:
                    return C3953R.style.SudThemeMaterial_Light;
                case 2:
                    return C3953R.style.SudThemeGlifV3_Light;
                case 3:
                    return C3953R.style.SudThemeGlifV4_Light;
                case 4:
                    return C3953R.style.SudThemeGlif;
                case 5:
                    return C3953R.style.SudThemeGlifV2;
                case 6:
                    return C3953R.style.SudThemeGlifV3;
                case 7:
                    return C3953R.style.SudThemeGlifV4;
                case 8:
                    return C3953R.style.SudThemeMaterial;
                case 9:
                    return C3953R.style.SudThemeGlif_Light;
            }
        }
        return 0;
    }

    private static int compareThemes(String str, String str2) {
        return Integer.valueOf(getThemeVersion(str)).compareTo(Integer.valueOf(getThemeVersion(str2)));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getThemeVersion(java.lang.String r7) {
        /*
            r0 = -1
            if (r7 == 0) goto L_0x008f
            r7.hashCode()
            int r1 = r7.hashCode()
            r2 = 5
            r3 = 4
            r4 = 3
            r5 = 2
            r6 = 1
            switch(r1) {
                case -2128555920: goto L_0x007c;
                case -1270463490: goto L_0x0071;
                case -1241052239: goto L_0x0066;
                case -353548558: goto L_0x005b;
                case 3175618: goto L_0x0050;
                case 115650329: goto L_0x0045;
                case 115650330: goto L_0x003a;
                case 115650331: goto L_0x002f;
                case 299066663: goto L_0x0022;
                case 767685465: goto L_0x0015;
                default: goto L_0x0012;
            }
        L_0x0012:
            r7 = r0
            goto L_0x0086
        L_0x0015:
            java.lang.String r1 = "glif_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x001e
            goto L_0x0012
        L_0x001e:
            r7 = 9
            goto L_0x0086
        L_0x0022:
            java.lang.String r1 = "material"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x002b
            goto L_0x0012
        L_0x002b:
            r7 = 8
            goto L_0x0086
        L_0x002f:
            java.lang.String r1 = "glif_v4"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0038
            goto L_0x0012
        L_0x0038:
            r7 = 7
            goto L_0x0086
        L_0x003a:
            java.lang.String r1 = "glif_v3"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0043
            goto L_0x0012
        L_0x0043:
            r7 = 6
            goto L_0x0086
        L_0x0045:
            java.lang.String r1 = "glif_v2"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x004e
            goto L_0x0012
        L_0x004e:
            r7 = r2
            goto L_0x0086
        L_0x0050:
            java.lang.String r1 = "glif"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0059
            goto L_0x0012
        L_0x0059:
            r7 = r3
            goto L_0x0086
        L_0x005b:
            java.lang.String r1 = "glif_v4_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0064
            goto L_0x0012
        L_0x0064:
            r7 = r4
            goto L_0x0086
        L_0x0066:
            java.lang.String r1 = "glif_v3_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x006f
            goto L_0x0012
        L_0x006f:
            r7 = r5
            goto L_0x0086
        L_0x0071:
            java.lang.String r1 = "material_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x007a
            goto L_0x0012
        L_0x007a:
            r7 = r6
            goto L_0x0086
        L_0x007c:
            java.lang.String r1 = "glif_v2_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0085
            goto L_0x0012
        L_0x0085:
            r7 = 0
        L_0x0086:
            switch(r7) {
                case 0: goto L_0x008e;
                case 1: goto L_0x008d;
                case 2: goto L_0x008c;
                case 3: goto L_0x008b;
                case 4: goto L_0x008a;
                case 5: goto L_0x008e;
                case 6: goto L_0x008c;
                case 7: goto L_0x008b;
                case 8: goto L_0x008d;
                case 9: goto L_0x008a;
                default: goto L_0x0089;
            }
        L_0x0089:
            goto L_0x008f
        L_0x008a:
            return r5
        L_0x008b:
            return r2
        L_0x008c:
            return r3
        L_0x008d:
            return r6
        L_0x008e:
            return r4
        L_0x008f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupdesign.util.ThemeResolver.getThemeVersion(java.lang.String):int");
    }

    public static class Builder {
        private int defaultTheme = C3953R.style.SudThemeGlif_DayNight;
        private ThemeSupplier defaultThemeSupplier;
        private String oldestSupportedTheme = null;
        private boolean useDayNight = true;

        public Builder() {
        }

        public Builder(ThemeResolver themeResolver) {
            this.defaultTheme = themeResolver.defaultTheme;
            this.oldestSupportedTheme = themeResolver.oldestSupportedTheme;
            this.useDayNight = themeResolver.useDayNight;
        }

        public Builder setDefaultThemeSupplier(ThemeSupplier themeSupplier) {
            this.defaultThemeSupplier = themeSupplier;
            return this;
        }

        public Builder setDefaultTheme(int i) {
            this.defaultTheme = i;
            return this;
        }

        public Builder setOldestSupportedTheme(String str) {
            this.oldestSupportedTheme = str;
            return this;
        }

        public Builder setUseDayNight(boolean z) {
            this.useDayNight = z;
            return this;
        }

        public ThemeResolver build() {
            return new ThemeResolver(this.defaultTheme, this.oldestSupportedTheme, this.defaultThemeSupplier, this.useDayNight);
        }
    }
}
