package com.google.android.material.color;

import com.google.android.material.C3631R;

public final class HarmonizedColorAttributes {
    private static final int[] HARMONIZED_MATERIAL_ATTRIBUTES = {C3631R.attr.colorError, C3631R.attr.colorOnError, C3631R.attr.colorErrorContainer, C3631R.attr.colorOnErrorContainer};
    private final int[] attributes;
    private final int themeOverlay;

    public static HarmonizedColorAttributes create(int[] iArr) {
        return new HarmonizedColorAttributes(iArr, 0);
    }

    public static HarmonizedColorAttributes create(int[] iArr, int i) {
        return new HarmonizedColorAttributes(iArr, i);
    }

    public static HarmonizedColorAttributes createMaterialDefaults() {
        return create(HARMONIZED_MATERIAL_ATTRIBUTES, C3631R.style.ThemeOverlay_Material3_HarmonizedColors);
    }

    private HarmonizedColorAttributes(int[] iArr, int i) {
        if (i == 0 || iArr.length != 0) {
            this.attributes = iArr;
            this.themeOverlay = i;
            return;
        }
        throw new IllegalArgumentException("Theme overlay should be used with the accompanying int[] attributes.");
    }

    public int[] getAttributes() {
        return this.attributes;
    }

    public int getThemeOverlay() {
        return this.themeOverlay;
    }
}
