package com.google.android.setupdesign.util;

import android.content.Context;
import com.google.android.setupdesign.C3963R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class DynamicColorPalette {

    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorType {
        public static final int ACCENT = 0;
        public static final int BACKGROUND = 7;
        public static final int DISABLED_OPTION = 3;
        public static final int ERROR_WARNING = 4;
        public static final int FALLBACK_ACCENT = 6;
        public static final int PRIMARY_TEXT = 1;
        public static final int SECONDARY_TEXT = 2;
        public static final int SUCCESS_DONE = 5;
        public static final int SURFACE = 8;
    }

    private DynamicColorPalette() {
    }

    public static int getColor(Context context, int i) {
        int i2;
        switch (i) {
            case 0:
                i2 = C3963R.C3964color.sud_dynamic_color_accent_glif_v3;
                break;
            case 1:
                i2 = C3963R.C3964color.sud_system_primary_text;
                break;
            case 2:
                i2 = C3963R.C3964color.sud_system_secondary_text;
                break;
            case 3:
                i2 = C3963R.C3964color.sud_system_tertiary_text_inactive;
                break;
            case 4:
                i2 = C3963R.C3964color.sud_system_error_warning;
                break;
            case 5:
                i2 = C3963R.C3964color.sud_system_success_done;
                break;
            case 6:
                i2 = C3963R.C3964color.sud_system_fallback_accent;
                break;
            case 7:
                i2 = C3963R.C3964color.sud_system_background_surface;
                break;
            case 8:
                i2 = C3963R.C3964color.sud_system_surface;
                break;
            default:
                i2 = 0;
                break;
        }
        return context.getResources().getColor(i2);
    }
}
