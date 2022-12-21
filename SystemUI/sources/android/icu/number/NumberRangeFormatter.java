package android.icu.number;

import android.icu.util.ULocale;
import java.util.Locale;

public abstract class NumberRangeFormatter {

    public enum RangeCollapse {
        AUTO,
        NONE,
        UNIT,
        ALL
    }

    public enum RangeIdentityFallback {
        SINGLE_VALUE,
        APPROXIMATELY_OR_SINGLE_VALUE,
        APPROXIMATELY,
        RANGE
    }

    public enum RangeIdentityResult {
        EQUAL_BEFORE_ROUNDING,
        EQUAL_AFTER_ROUNDING,
        NOT_EQUAL
    }

    private NumberRangeFormatter() {
        throw new RuntimeException("Stub!");
    }

    public static UnlocalizedNumberRangeFormatter with() {
        throw new RuntimeException("Stub!");
    }

    public static LocalizedNumberRangeFormatter withLocale(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static LocalizedNumberRangeFormatter withLocale(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }
}
