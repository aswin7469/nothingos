package com.android.systemui.util.leak;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RotationUtils {
    public static final int ROTATION_LANDSCAPE = 1;
    public static final int ROTATION_NONE = 0;
    public static final int ROTATION_SEASCAPE = 3;
    public static final int ROTATION_UPSIDE_DOWN = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Rotation {
    }

    public static int getRotation(Context context) {
        int rotation = context.getDisplay().getRotation();
        if (rotation == 1) {
            return 1;
        }
        return rotation == 3 ? 3 : 0;
    }

    public static int getExactRotation(Context context) {
        int rotation = context.getDisplay().getRotation();
        if (rotation == 1) {
            return 1;
        }
        if (rotation == 3) {
            return 3;
        }
        return rotation == 2 ? 2 : 0;
    }

    public static String toString(int i) {
        if (i == 0) {
            return "None (0)";
        }
        if (i == 1) {
            return "Landscape (1)";
        }
        if (i != 2) {
            return i != 3 ? "Unknown (" + i + NavigationBarInflaterView.KEY_CODE_END : "Seascape (3)";
        }
        return "Upside down (2)";
    }

    public static Resources getResourcesForRotation(int i, Context context) {
        int i2 = 1;
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        throw new IllegalArgumentException("Unknown rotation: " + i);
                    }
                }
            }
            i2 = 2;
        }
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.orientation = i2;
        return context.createConfigurationContext(configuration).getResources();
    }
}
