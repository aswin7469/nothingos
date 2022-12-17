package com.android.settings.display;

import android.content.res.Resources;
import android.util.ArrayMap;
import com.android.settings.R$array;
import java.util.Map;

final class ColorModeUtils {
    static Map<Integer, String> getColorModeSummaryAndValueMapping(Resources resources) {
        String[] stringArray = resources.getStringArray(R$array.nt_config_color_mode_options_summary);
        int[] intArray = resources.getIntArray(R$array.nt_config_color_mode_options_values);
        if (stringArray.length == intArray.length) {
            ArrayMap arrayMap = new ArrayMap();
            for (int i = 0; i < intArray.length; i++) {
                arrayMap.put(Integer.valueOf(intArray[i]), stringArray[i]);
            }
            return arrayMap;
        }
        throw new RuntimeException("Color mode options of unequal length");
    }
}
