package com.android.systemui.dreams.complication;

import java.util.Set;

public class ComplicationUtils {
    public static int convertComplicationType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                if (i == 3) {
                    return 4;
                }
                if (i != 4) {
                    return i != 5 ? 0 : 16;
                }
                return 8;
            }
        }
        return i2;
    }

    static /* synthetic */ int lambda$convertComplicationTypes$0(int i, int i2) {
        return i | i2;
    }

    public static int convertComplicationTypes(Set<Integer> set) {
        return set.stream().mapToInt(new ComplicationUtils$$ExternalSyntheticLambda0()).reduce(0, new ComplicationUtils$$ExternalSyntheticLambda1());
    }
}
