package androidx.core.text;

import android.text.TextUtils;
import java.util.Locale;

public final class TextUtilsCompat {
    private static final Locale ROOT = new Locale("", "");

    public static int getLayoutDirectionFromLocale(Locale locale) {
        return Api17Impl.getLayoutDirectionFromLocale(locale);
    }

    static class Api17Impl {
        static int getLayoutDirectionFromLocale(Locale locale) {
            return TextUtils.getLayoutDirectionFromLocale(locale);
        }
    }
}
